package be.vdab.tcbackend.origins;

import be.vdab.tcbackend.categories.CategoryAlreadyExistsException;
import be.vdab.tcbackend.categories.CategoryNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly=true)
class OriginService {

    private final OriginRepository originRepository;

    OriginService(OriginRepository originRepository) {
        this.originRepository = originRepository;
    }

    /* Method that returns the count of origins in table Origins */
    long findCount() {
        return originRepository.count();
    }

    List<Origin> findAll() {
        return originRepository.findAll();
    }

    Optional<Origin> findBy(long id) {
        return originRepository.findById(id);
    }

    /* Method that finds an origin by name */
    Optional<Origin> findByName(String name) {
        return originRepository.findByName(name);
    }


    /* Method that creates a new origin */
    /* Thema 9: Toevoegen */
    // UPDATE - Thema 21: @ManyToOne
    @Transactional
    long create(NewOrigin newOrigin) {
        if (findByName(newOrigin.name()).isPresent()) {
            throw new OriginAlreadyExistsException();
        }

        var origin = new Origin(newOrigin.name());

        IO.println("origin: " + origin);
        originRepository.save(origin);

        return origin.getId();
    }

    /* Method that updates the origin name */
    @Transactional
    void updateName(long id, String name) {
        // Checks that the categor already exists
        var origin = originRepository.findById(id)
                .orElseThrow(OriginNotFoundException::new);

        // The name is already present
        if (findByName(name).isPresent()) {
            throw new OriginAlreadyExistsException();
        }

        origin.setName(name);
    }

    @Transactional
    void delete(long id) {
        var origin = originRepository.findById(id)
                .orElseThrow(OriginNotFoundException::new);

        originRepository.delete(origin);
    }

}
