package be.vdab.tcbackend.users;

import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
class AddressService {

    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    AddressService(AddressRepository addressRepository, UserRepository userRepository) {
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    Optional<Address> findById(long id) {
        return addressRepository.findById(id);
    }

    List<Address> findByUserId(long userId) {
        return addressRepository.findByUserId(userId);
    }

    @Transactional
    long create(long userId, NewAddress newAddress) {
        var user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        var address = new Address(
                newAddress.street(),
                newAddress.houseNumber(),
                newAddress.bus(),
                newAddress.city(),
                newAddress.postalCode(),
                newAddress.country(),
                user
        );

        addressRepository.save(address);

        return address.getId();
    }

    @Transactional
    void update(long id, @NonNull EditAddress editAddress) {
        IO.println("ID:" + id + "-" + editAddress.toString());
        var address = addressRepository.findById(id)
                .orElseThrow(AddressNotFoundException::new);

        address.update(editAddress.street(),
                       editAddress.houseNumber(),
                       editAddress.bus(),
                       editAddress.city(),
                       editAddress.postalCode(),
                       editAddress .country());
    }

    @Transactional
    void delete(long id) {
        var address = addressRepository.findById(id)
                .orElseThrow(AddressNotFoundException::new);
        addressRepository.delete(address);

    }
}
