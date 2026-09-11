package be.vdab.tcbackend.users;

import be.vdab.tcbackend.products.ProductNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("users")
class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    /* DTOs */
    private record UserDetails(
            long userId,
            String fullName,
            String email) {
        UserDetails (User user) {
            this(user.getId(),
                    user.getFirstName() + " " + user.getLastName(),
                    user.getEmail());
        }
    }

    private record UpdateUser(
            @NotBlank String email,
            @NotBlank String firstName,
            @NotBlank String lastName) {
    }

    private record UpdatePassword(@NotBlank String password) { }

    private record UpdateRole(Role role) { }

    private record UpdateFirstName(@NotBlank String firstName) { }

    private record UpdateLastName(@NotBlank String lastName) { }


    // GET requests finds by id returns one USER */
    // GET http://localhost:8080/USERS/{{id}}
    @GetMapping("{id}")
    UserDetails findById(@PathVariable long id) {
        return userService.findById(id)
                .map(user -> new UserDetails(user))
                .orElseThrow(UserNotFoundException::new);
    }

    @GetMapping
    List<UserDetails> findAll() {
        return userService.findAll()
                .stream()
                .map(UserDetails::new)
                .toList();
    }

    // GET /users/byemail?email=diego@ramos.be
    @GetMapping("byemail")
    UserDetails findByEmail(@RequestParam String email) {
        return userService.findByEmail(email)
                .map(UserDetails::new)
                .orElseThrow(UserNotFoundException::new);
    }

    @PostMapping
    long create(@RequestBody @Valid NewUser newUser) {
        return userService.create(newUser);
    }

    @DeleteMapping("{id}")
    void delete(@PathVariable long id) {
        userService.delete(id);
    }

    // Update password
    @PutMapping("{id}/password")
    void updatePassword(
            @PathVariable long id,
            @RequestBody @Valid UpdatePassword updatePassword) {

        userService.updatePassword(id, updatePassword.password());
    }

    // Update role
    @PutMapping("{id}/role")
    void updateRole(
            @PathVariable long id,
            @RequestBody UpdateRole updateRole) {

        userService.updateRole(id, updateRole.role());
    }

    // Update first name
    @PutMapping("{id}/firstname")
    void updateFirstName(
            @PathVariable long id,
            @RequestBody @Valid UpdateFirstName updateFirstName) {

        userService.updateFirstName(
                id,
                updateFirstName.firstName());
    }

    // Update last name
    @PutMapping("{id}/lastname")
    void updateLastName(
            @PathVariable long id,
            @RequestBody @Valid UpdateLastName updateLastName) {

        userService.updateLastName(id, updateLastName.lastName());
    }
}
