package be.vdab.tcbackend.users;

import jakarta.validation.constraints.NotBlank;

record NewAddress(
        @NotBlank String street,
        @NotBlank String houseNumber,
        String bus,
        @NotBlank String city,
        @NotBlank String postalCode,
        @NotBlank String country
) {

}
