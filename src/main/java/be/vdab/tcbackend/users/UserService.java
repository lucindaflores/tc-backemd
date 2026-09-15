package be.vdab.tcbackend.users;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
class UserService {

    private final UserRepository userRepository;

    UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /* Method that finds by id and returns an optional User  */
    Optional<User> findById(long id) {
        return  userRepository.findById(id);
    }

    List<User> findAll() {
        return userRepository.findAll();
    }

    Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    long create(NewUser newUser) {
        if (userRepository.findByEmail(newUser.email()).isPresent()) {
            throw new UserAlreadyExistsException();
        }

        var user = new User(
                newUser.email(),
                newUser.firstName(),
                newUser.lastName()
        );

        userRepository.save(user);

        return user.getId();
    }

    @Transactional
    void update(long id,
                String firstName,
                String lastName) {

        var user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        user.update(firstName, lastName);
    }

    @Transactional
    void delete(long id) {
        var user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        userRepository.delete(user);
    }

    @Transactional
    void updateRole(long id, Role role) {
        var user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        user.updateRole(role);
    }

    @Transactional
    void deactivate(long id) {
        var user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        user.deactivate();
    }

}
