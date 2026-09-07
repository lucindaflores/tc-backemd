package be.vdab.tcbackend.categories;

import org.springframework.data.jpa.repository.JpaRepository;

// <entity class, variable type of the PK>
public interface CategoryRepository extends JpaRepository<Category, Long> {

}
