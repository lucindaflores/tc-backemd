package be.vdab.tcbackend.categories;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Stream;

@RestController
@RequestMapping("categories")
//@CrossOrigin
class CategoryController {

    private final CategoryService categoryService;

    CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    private record CategoryName(String name) {
        CategoryName(Category category) {
            this(category.getName());
        }
    }

    /* GET request that returns the count of categories  */
    // GET http://localhost:8080/categories/count
    @GetMapping("count")
    long findCount() { return categoryService.findCount(); }

    /* GET request that returns all the category names */
    // GET http://localhost:8080/categories
    @GetMapping()
    Stream<CategoryName> findAll() {
        return categoryService.findAll()
                .stream()
                .map(category -> new CategoryName(category));
    }




}
