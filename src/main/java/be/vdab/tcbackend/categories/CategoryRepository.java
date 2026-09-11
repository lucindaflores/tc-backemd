package be.vdab.tcbackend.categories;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// <entity class, variable type of the PK>
public interface CategoryRepository extends JpaRepository<Category, Long> {

    /* Method that finds a category by name */
    Optional<Category> findByName(String name);

}
