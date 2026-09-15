package be.vdab.tcbackend.categories;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("categories")
@CrossOrigin
class CategoryController {

    private final CategoryService categoryService;

    CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    private record CategoryName(long id, String name) {
        CategoryName(Category category) {
            this(category.getId(), category.getName());
        }
    }

    /* DTOs */
    private record UpdateName(@NotBlank String name) { }

    /* GET request that returns the count of categories  */
    // GET http://localhost:8080/categories/count
    @GetMapping("count")
    long findCount() { return categoryService.findCount(); }

    /* GET request that returns all the category names */
    // GET http://localhost:8080/categories
    @GetMapping()
    List<CategoryName> findAll() {
        return categoryService.findAll()
                .stream()
                //.map(category -> new CategoryName(category));
                .map(CategoryName::new)
                .toList();
    }

    // GET requests finds by categoryId returns the category name */
    // GET http://localhost:8080/categories/{{id}}
    @GetMapping("{id}")
    CategoryName findById(@PathVariable long id) {
        return categoryService.findById(id)
                .map(CategoryName::new)
                .orElseThrow(CategoryNotFoundException::new);
    }

    /* REQUESTS */
    /* POST request that creates a new category */
    // POST http://localhost:8080/categories
    @PostMapping
    long create(@RequestBody @Valid NewCategory newCategory) {
        return categoryService.create(newCategory);
    }

    /* Thema 10: Verwijderen */
    /* DELETE request to delete a category by Id */
    //DELETE http://localhost:8080/products/51
    @DeleteMapping("{id}")
    void delete(@PathVariable long id) {
        categoryService.delete(id);
    }

    // Updates Category name
    @PutMapping("{id}/name")
    void updateName(@PathVariable long id,
                    @RequestBody @Valid UpdateName updateName) {

        categoryService.updateName(id, updateName.name());
    }



}
