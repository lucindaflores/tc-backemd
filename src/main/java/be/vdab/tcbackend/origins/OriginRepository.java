package be.vdab.tcbackend.origins;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface OriginRepository extends JpaRepository<Origin, Long> {

    /* Method that finds an origin by name */
    Optional<Origin> findByName(String name);


}