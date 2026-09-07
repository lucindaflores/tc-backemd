package be.vdab.tcbackend.products;

import org.springframework.data.jpa.repository.JpaRepository;

// <entity class, variable type of the PK>
interface MaterialsRepository extends JpaRepository<Material, Long> {
}
