package be.vdab.tcbackend.orders;

import jakarta.validation.constraints.Positive;

record NewOrderDetail(
        @Positive int quantity,
        @Positive long productId
        ) {
}
