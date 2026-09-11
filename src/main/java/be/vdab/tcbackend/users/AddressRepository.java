package be.vdab.tcbackend.users;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<Address, Long> {

    List<Address> findByUserId(long userId);

    Optional<Address> findByIdAndUserId(long id, long userId);
}
