package be.vdab.tcbackend.products;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

// <entity class, variable type of the PK>
interface MaterialRepository extends JpaRepository<Material, Long> {

    /* Method that finds a material by name */
    Optional<Material> findByName(String name);
}
