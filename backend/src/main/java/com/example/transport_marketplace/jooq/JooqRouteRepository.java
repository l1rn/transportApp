package com.example.transport_marketplace.jooq;

import com.example.transport_marketplace.dto.routes.RouteResponse;
import com.example.transport_marketplace.dto.routes.RouteSearchRequest;
import com.example.transport_marketplace.jooq.tables.records.RoutesRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.example.transport_marketplace.jooq.tables.Routes.ROUTES;

@Component
@RequiredArgsConstructor
public class JooqRouteRepository {
    private final DSLContext dsl;

    public List<RouteResponse> searchRoutes(RouteSearchRequest request){
        Condition condition = buildSearchCondition(request);

        return dsl.select()
                .from(ROUTES)
                .where(condition)
                .orderBy(ROUTES.DESTINATION_TIME.asc())
                .fetch(record -> mapToRouteResponse(record.into(ROUTES)));
    }

    public Condition buildSearchCondition(RouteSearchRequest request){
        List<Condition> conditions = new ArrayList<>();

        conditions.add(ROUTES.DESTINATION_TIME.greaterThan(LocalDateTime.now()));

        if(StringUtils.hasText(request.getRouteFrom())){
            conditions.add(ROUTES.ROUTE_FROM.likeIgnoreCase("%" + request.getRouteFrom() + "%"));
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
