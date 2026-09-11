package be.vdab.tcbackend.order;

import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

record NewOrderDetail(
        @Positive int quantity,
        @Positive long productId
        ) {
}
