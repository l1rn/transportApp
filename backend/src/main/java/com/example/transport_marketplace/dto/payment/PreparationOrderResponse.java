package com.example.transport_marketplace.dto.payment;

import com.example.transport_marketplace.enums.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PreparationOrderResponse {
    private Integer routeId;
    private String orderFullName;
    private Double price;
    private List<PaymentMethod> paymentMethods;
    private boolean hasEmail;
    private boolean inProgress;
    private boolean paid;
}
