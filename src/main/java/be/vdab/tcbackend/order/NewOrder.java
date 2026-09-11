package be.vdab.tcbackend.order;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;
import java.util.Set;

/*  JSON representation arriving from a POST request */
record NewOrder(
      //  LocalDateTime orderDate, // Validation notnull occurs in the db
      //  @NotNull Status status,
        @Positive long userId,
        @Positive long addressId,
        @NotNull Set<NewOrderDetail> orderDetails
        ) {
}
