package be.vdab.tcbackend.products;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly=true)
class MaterialService {

    private final MaterialRepository materialRepository;

    MaterialService(MaterialRepository materialRepository) {
        this.materialRepository = materialRepository;
    }

    List<Material> findAll() {
        return materialRepository.findAll();
    }

    Optional<Material> findBy(long id) {
        return materialRepository.findById(id);
    }

    /* Method that finds a material by name */
    Optional<Material> findByName(String name) {
        return materialRepository.findByName(name);
    }

    /* Method that creates a new material */
    /* Thema 9: Toevoegen */
    // UPDATE - Thema 21: @ManyToOne
    @Transactional
    long create(NewMaterial newMaterial) {
        if (findByName(newMaterial.name()).isPresent()) {
            throw new MaterialAlreadyExistsException();
        }

        var material = new Material(newMaterial.name(), newMaterial.nameSpanish(), newMaterial.technique());

        IO.println("MATERIAL: " + material);
        materialRepository.save(material);

        return material.getId();

    }

    /* Method that updates all fields in the material model */
    @Transactional
    void update(long id, NewMaterial newMaterial) {
        // Check that the material exists in the db
        var material = materialRepository.findByName(newMaterial.name())
                .orElseThrow(MaterialNotFoundException::new);

        // Checks if the material name is present
        if (!material.getName().equals(newMaterial.name())) {
            throw new MaterialAlreadyExistsException();
        }

        material.update(
                newMaterial.name(),
                newMaterial.nameSpanish(),
                newMaterial.technique());
    }

    /* Method that deletes a material */
    @Transactional
    void delete(long id) {
        var material = materialRepository.findById(id)
                .orElseThrow(MaterialNotFoundException::new);

        materialRepository.delete(material);
    }

}
