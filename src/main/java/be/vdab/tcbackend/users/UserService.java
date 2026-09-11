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
        var user = new User(
                newUser.email(),
                newUser.password(),
                newUser.firstName(),
                newUser.lastName()
        );

        userRepository.save(user);

        return user.getId();
    }

    @Transactional
    void update(long id,
                String email,
                String firstName,
                String lastName) {

        var user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        user.update(email, firstName, lastName);
    }

    @Transactional
    void delete(long id) {
        var user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        userRepository.delete(user);
    }

    @Transactional
    void updatePassword(long id, String passwordHash) {
        var user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        user.updatePassword(passwordHash);
    }

    @Transactional
    void updateRole(long id, Role role) {
        var user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        user.updateRole(role);
    }

    @Transactional
    void updateFirstName(long id, String firstName) {
        var user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        user.updateFirstName(firstName);
    }

    @Transactional
    void updateLastName(long id, String lastName) {
        var user = userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);

        user.updateLastName(lastName);
    }
}
