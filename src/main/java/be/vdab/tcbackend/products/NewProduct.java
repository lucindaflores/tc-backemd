package be.vdab.tcbackend.products;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import jdk.jfr.BooleanFlag;

import java.math.BigDecimal;
import java.util.Set;

/*  JSON representation arriving from a POST request */
record NewProduct(
        @NotBlank String code,
        @NotBlank String name,
        @NotBlank String description,
        @NotNull @Positive BigDecimal price,
        @PositiveOrZero int stock,
        String imageUrl,
        @BooleanFlag boolean isActive,
        @Positive long categoryId,  // can be null
        @Positive long originId,
        @NotNull Set<Long> materialIds // the materials used in this product (product_materials)
){ //can be null

};
