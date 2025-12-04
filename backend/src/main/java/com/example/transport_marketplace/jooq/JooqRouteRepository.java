package com.example.transport_marketplace.jooq;

import com.example.transport_marketplace.dto.routes.RouteResponse;
import com.example.transport_marketplace.dto.routes.RouteSearchRequest;
import com.example.transport_marketplace.jooq.tables.records.RoutesRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.example.transport_marketplace.jooq.tables.Routes.ROUTES;

@Slf4j
@Component
@RequiredArgsConstructor
public class JooqRouteRepository {
    private final DSLContext dsl;

    public Page<RouteResponse> searchRoutes(RouteSearchRequest request, Pageable pageable){
        Condition condition = buildSearchCondition(request);

        Integer total = dsl.selectCount()
                .from(ROUTES)
                .where(condition)
                .fetchOne(0, int.class);

        if(total == null || total == 0){
            return new PageImpl<>(Collections.emptyList(), pageable, 0);
        }

        List<RouteResponse> content = dsl.select()
                .from(ROUTES)
                .where(condition)
                .orderBy(ROUTES.DESTINATION_TIME.asc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch(record -> mapToRouteResponse(record.into(ROUTES)));

        return new PageImpl<>(content, pageable, total);
    }

    public Condition buildSearchCondition(RouteSearchRequest request){
        List<Condition> conditions = new ArrayList<>();

        conditions.add(ROUTES.DESTINATION_TIME.greaterThan(LocalDateTime.now()));

        if(StringUtils.hasText(request.getRouteFrom())){
            conditions.add(ROUTES.ROUTE_FROM.likeIgnoreCase("%" + request.getRouteFrom() + "%"));
        }

        if(StringUtils.hasText(request.getRouteTo())){
            conditions.add(ROUTES.ROUTE_TO.likeIgnoreCase("%" + request.getRouteFrom() + "%"));
        }

        if(StringUtils.hasText(request.getTransport())){
            conditions.add(ROUTES.TRANSPORT.likeIgnoreCase("%" + request.getRouteFrom() + "%"));
        }

        if(StringUtils.hasText(request.getDate())){
            try {
                LocalDate searchDate = LocalDate.parse(request.getDate());

                LocalDateTime startOfDay = searchDate.atStartOfDay();
                LocalDateTime endOfDay = searchDate.plusDays(1).atStartOfDay();

                conditions.add(ROUTES.DESTINATION_TIME.greaterOrEqual(startOfDay));
                conditions.add(ROUTES.DESTINATION_TIME.lessThan(endOfDay));
            }
            catch(Exception e){
                log.error("Filter by date error: " + e.getMessage());
            }
        }


        return conditions.stream()
                .reduce(Condition::and)
                .orElse(DSL.trueCondition());
    }

    public RouteResponse mapToRouteResponse(RoutesRecord record){
        return new RouteResponse(
                record.getId(),
                record.getRouteFrom(),
                record.getRouteTo(),
                record.getTransport(),
                record.getDestinationTime(),
                record.getArrivalTime(),
                record.getAvailableSeats(),
                record.getPrice()
        );
    }
}
