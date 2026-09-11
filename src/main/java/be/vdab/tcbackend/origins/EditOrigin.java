package be.vdab.tcbackend.origins;

import jakarta.validation.constraints.NotBlank;

record EditOrigin(@NotBlank String name) {
}
