package be.vdab.tcbackend.users;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

record NewUser(
        @NotBlank @Email String email,
//        @Size(min = 8) String password,
        @NotBlank String firstName,
        @NotBlank String lastName
) {
}
