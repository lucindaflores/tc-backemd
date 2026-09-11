package be.vdab.tcbackend.users;

import jakarta.validation.constraints.NotBlank;

record NewUser(
        @NotBlank String email,
        @NotBlank String password,
        @NotBlank String firstName,
        @NotBlank String lastName
) {

}
