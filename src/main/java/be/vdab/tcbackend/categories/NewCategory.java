package be.vdab.tcbackend.categories;

import jakarta.validation.constraints.NotBlank;

record NewCategory(@NotBlank String name) {
}
