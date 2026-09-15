package be.vdab.tcbackend.products;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.Set;

record EditProduct(
        @NotBlank String code,
        @NotBlank String name,
        @NotBlank String description,
        @NotNull @Positive BigDecimal price,
        @PositiveOrZero int stock,
        String imageUrl,
        boolean active,
        @Positive long categoryId,
        @Positive long originId,
        @NotNull Set<Long> materialIds,
        @PositiveOrZero long version //the editing version
){
}
