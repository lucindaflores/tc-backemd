package be.vdab.tcbackend.products;

import jakarta.validation.constraints.NotBlank;

record NewMaterial(
        @NotBlank String name,
        @NotBlank String nameSpanish,
        @NotBlank String technique
) {
}
