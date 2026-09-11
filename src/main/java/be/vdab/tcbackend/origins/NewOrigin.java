package be.vdab.tcbackend.origins;

import jakarta.validation.constraints.NotBlank;

record NewOrigin(@NotBlank String name) {
}
