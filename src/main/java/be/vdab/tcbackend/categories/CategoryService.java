package be.vdab.tcbackend.categories;

import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
class CategoryService {

    private final CategoryRepository categoryRepository;


    CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    /* Method that returns the count of categories in table products */
    long findCount() {
        return categoryRepository.count();
    }

    /* Method that returns a list of all categories */
    List<Category> findAll() {
        return categoryRepository.findAll(Sort.by("name"));
    }

    /* Method that finds by id and returns an optional Category */
    Optional<Category> findById(long id) {
        return categoryRepository.findById(id);
    }

    /* Method that finds a category by name */
    Optional<Category> findByName(String name) {
        return categoryRepository.findByName(name);
    }

    /* Method that creates a new category */
    /* Thema 9: Toevoegen */
    // UPDATE - Thema 21: @ManyToOne
    @Transactional
    long create(NewCategory newCategory) {
        if (findByName(newCategory.name()).isPresent()) {
            throw new CategoryAlreadyExistsException();
        }

        var category = new Category(newCategory.name());
        IO.println("CATEGORY " + category.getId() + " " + category.getName());

        categoryRepository.save(category);

        return category.getId();
    }

    /* Method that updates the name of a category */
    @Transactional
    void updateName(long id, String name) {
        // Checks that the categor already exists
        var category = categoryRepository.findById(id)
                .orElseThrow(CategoryNotFoundException::new);

        // The name is already present
        if (findByName(name).isPresent()) {
            throw new CategoryAlreadyExistsException();
        }

        category.setName(name);
    }

    /* Method that deletes a category */
    @Transactional
    void delete(long id) {
        var category = categoryRepository.findById(id)
                .orElseThrow(CategoryNotFoundException::new);

        categoryRepository.delete(category);
    }



}
