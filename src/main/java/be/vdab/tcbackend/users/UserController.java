package be.vdab.tcbackend.users;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("users")
@CrossOrigin
class UserController {

    private final UserService userService;

    UserController(UserService userService) {
        this.userService = userService;
    }

    /* DTOs */
    private record UserDetails(
            long userId,
            String firstName,
            String lastName,
            String email) {
        UserDetails (User user) {
            this(user.getId(),
                    user.getFirstName() ,
                    user.getLastName(),
                    user.getEmail());
        }
    }

    private record UpdateUser(
            @NotBlank String firstName,
            @NotBlank String lastName) {
    }

    private record UpdateRole(@NotNull Role role) { }

    // GET requests finds by id returns one USER */
    // GET http://localhost:8080/USERS/{{id}}
    @GetMapping("{id}")
    UserDetails findById(@PathVariable long id) {
        return userService.findById(id)
                .map(UserDetails::new)
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
    @GetMapping("byEmail")
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

    @PutMapping("{id}")
    void update(@PathVariable long id,
                @RequestBody @Valid UpdateUser updateUser) {
        userService.update(id,
                updateUser.firstName(),
                updateUser.lastName());
    }

    // Update role
    // This needs to only be called by the Admin
    @PutMapping("{id}/role")
    void updateRole(@PathVariable long id,
                    @RequestBody @Valid UpdateRole updateRole) {
        userService.updateRole(id, updateRole.role());
    }

    @PutMapping("{id}/deactivate")
    void deactivate(@PathVariable long id) {
        userService.deactivate(id);
    }
}
