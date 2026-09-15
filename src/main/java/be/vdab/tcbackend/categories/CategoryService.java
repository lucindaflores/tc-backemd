package be.vdab.tcbackend.categories;

import be.vdab.tcbackend.products.ProductRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly = true)
class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    CategoryService(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
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
        var category = categoryRepository.findById(id)
                .orElseThrow(CategoryNotFoundException::new);

        var categoryWithSameName = categoryRepository.findByName(name);

        if (categoryWithSameName.isPresent() && categoryWithSameName.get().getId() != id) {
            throw new CategoryAlreadyExistsException();
        }

        category.updateName(name);
    }

    /* Method that deletes a category */
    @Transactional
    void delete(long id) {
        var category = categoryRepository.findById(id)
                .orElseThrow(CategoryNotFoundException::new);

        if (productRepository.existsByCategoryId(id)) {
            throw new CategoryIsInUseException();
        }

        categoryRepository.delete(category);
    }



}
