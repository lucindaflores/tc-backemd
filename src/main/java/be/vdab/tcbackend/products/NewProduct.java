package be.vdab.tcbackend.products;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.util.Set;

record NewProduct(
        @NotBlank String code,
        @NotBlank String name,
        @NotBlank String description,
        @NotNull @Positive BigDecimal price,
        @PositiveOrZero int stock,
        String imageUrl,
        boolean active,
        @NotNull @Positive long categoryId,
        @NotNull @Positive Long originId,
        @NotNull Set<Long> materialIds // the materials used in this product (product_materials)
) {

}
