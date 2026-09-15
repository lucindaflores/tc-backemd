package be.vdab.tcbackend.orders;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.Set;

record NewOrder(
        @Positive long userId,
        @Positive long addressId,
        @NotNull @Valid Set<NewOrderDetail> orderDetails
        ) {
}
