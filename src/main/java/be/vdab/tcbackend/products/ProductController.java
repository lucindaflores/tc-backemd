package be.vdab.tcbackend.products;


import be.vdab.tcbackend.categories.Category;
import be.vdab.tcbackend.origins.Origin;
import jakarta.validation.Valid;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

@RestController
@RequestMapping("products")
class ProductController {

    private final ProductService productService;

    ProductController(ProductService productService) {
        this.productService = productService;
    }


    private record ProductName(String name) {
        ProductName(Product product) {
            this(product.getName());
        }
    }

    /* This DTO adds the corresponding categories to a product */
    private record ProductWithAllDetails(
            long productId,
            String code,
            String name,
            String description,
            BigDecimal price,
            int stock,
            String imageUrl,
            boolean isActive,
            Category categoryId,
            Origin originId,
            Set<Material> materialSet) {
        ProductWithAllDetails(Product product) {
            this(product.getId(),
                    product.getCode(),
                    product.getName(),
                    product.getDescription(),
                    product.getPrice(),
                    product.getStock(),
                    product.getImageUrl(),
                    product.isActive(),
                    product.getCategory(),
                    product.getOrigin(),
                    product.getMaterials());
        }

    }


    /* GET request that returns the count of products  */
    // GET http://localhost:8080/products/count
    @GetMapping("count")
    long findCount() { return productService.findCount(); }

    /* GET request that returns the current version of product  */
    @GetMapping("version/{id}")
    long findVersionById(@PathVariable long id) {
        return productService.findVersionById(id);
    }

    /* GET request that returns all the product names */
    // GET http://localhost:8080/products/all
    @GetMapping("all")
    List<ProductName> findAll() {
        return productService.findAll()
                .stream()
                .map(product -> new ProductName(product))
                .toList();
    }
    //List means: I already have a collection of results.
    // Stream: I want to process a sequence of objects.

    // GET requests finds by id returns one product */
    // GET http://localhost:8080/products/{{id}}
    @GetMapping("{id}")
    ProductWithAllDetails findById(@PathVariable long id) {
        return productService.findById(id)
                .map(product -> new ProductWithAllDetails(product))
                .orElseThrow(ProductNotFoundException::new);
    }

    // GET requests finds by categoryId returns one product */
    // GET http://localhost:8080/products/bycategory/{{id}}
    @GetMapping("/bycategory/{categoryId}")
    List<ProductWithAllDetails> findByCategoryId(@PathVariable long categoryId) {
        return productService.findByCategoryId(categoryId)
                .stream()
                .map(ProductWithAllDetails::new)
                .toList();
    }

    // GET requests finds by originId returns one product */
    // GET http://localhost:8080/products/byorigin/{{id}}
    @GetMapping("/byorigin/{originId}")
    List<ProductWithAllDetails> findByOriginId(@PathVariable long originId) {
        return productService.findByOriginId(originId)
                .stream()
                .map(ProductWithAllDetails::new)
                .toList();
    }

    // GET request to find the products by materialId
    // GET http://localhost:8080/products/bymaterial/{{materialId}}
    @GetMapping("bymaterial/{materialId}")
    List<ProductWithAllDetails> findByMaterialId(@PathVariable long materialId) {
        return productService.findByMaterialId(materialId)
                .stream()
                .map(ProductWithAllDetails::new)
                .toList();
    }

    // GET request to find products by a set of materials
    // GET http://localhost:8080/products/bymaterials?materialIds=1,2,3
    @GetMapping("bymaterials")
    List<ProductWithAllDetails> findByMaterialIds(
            @RequestParam Set<Long> materialIds) {

        return productService.findByMaterialIds(materialIds)
                .stream()
                .map(ProductWithAllDetails::new)
                .toList();
    }

    // GET request to find all products in stock
    // GET http://localhost:8080/products/instock
    @GetMapping("instock")
    List<ProductWithAllDetails> findInStock() {
        return productService.findInStock()
                .stream()
                .map(ProductWithAllDetails::new)
                .toList();
    }

    // GET request to find the stock of a product
    // GET http://localhost:8080/products/{{id}}/stock
    @GetMapping("{id}/stock")
    int findStockById(@PathVariable long id) {
        return productService.findStockById(id);
    }

    /* POST request to create a new Product */
    // POST http://localhost:8080/product
    /* Thema 9: Toevoegen */
    @PostMapping()
    long create(@RequestBody @Valid NewProduct newProduct) {
        return productService.create(newProduct);
    }

    /* PUT request to update a Product by id */
    // PUT http://localhost:8080/products/{{id}}
    // NOTE: find the version first and send the put tih current version
    @PutMapping("{id}")
    void update(@PathVariable long id,
                @RequestBody @Valid EditProduct editProduct) {
        productService.update(id, editProduct);
    }

    /* Thema 10: Verwijderen */
    /* DELETE request to delete a product by Id */
    //DELETE http://localhost:8080/products/51
    @DeleteMapping("{id}")
    void delete(@PathVariable long id) {
        try {
            productService.delete(id);
        } catch (EmptyResultDataAccessException _) {
        }
    }

}