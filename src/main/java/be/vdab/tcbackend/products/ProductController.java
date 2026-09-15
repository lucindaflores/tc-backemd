package be.vdab.tcbackend.products;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("products")
@CrossOrigin
class ProductController {

    private final ProductService productService;

    ProductController(ProductService productService) {
        this.productService = productService;
    }

    /* DTOs */
    private record ProductName(String name) {
        ProductName(Product product) {
            this(product.getName());
        }
    }

    private record ProductCondensed(
            long id,
            String name,
            BigDecimal price,
            int stock,
            String imageUrl,
            String originName) {
        ProductCondensed(Product product) {
            this(product.getId(),
                    product.getName(),
                    product.getPrice(),
                    product.getStock(),
                    product.getImageUrl(),
                    product.getOrigin().getName()
            );
        }
    }


    /* This DTO shows a product with all details for findById */
    private record ProductWithAllDetails(
            long id,
            String code,
            String name,
            String description,
            BigDecimal price,
            int stock,
            String imageUrl,
            boolean active,
            long categoryId,
            String categoryName,
            long originId,
            String originName,
            Set<MaterialDetails> materialSet) {
        ProductWithAllDetails(Product product) {
            this(product.getId(),
                    product.getCode(),
                    product.getName(),
                    product.getDescription(),
                    product.getPrice(),
                    product.getStock(),
                    product.getImageUrl(),
                    product.active(),

                    product.getCategory().getId(),
                    product.getCategory().getName(),

                    product.getOrigin().getId(),
                    product.getOrigin().getName(),

                    product.getMaterials()
                            .stream()
                            .map(MaterialDetails::new)
                            .collect(Collectors.toSet())
            );
        }
    }

    private record MaterialDetails(long id, String name) {
        MaterialDetails(Material material) {
            this(material.getId(), material.getName());
        }
    }


    /* GET request that returns the count of products  */
    // GET http://localhost:8080/products/count
    @GetMapping("count")
    long findCount() { return productService.findCount(); }

    /* GET request that returns all the product names */
    // GET http://localhost:8080/products/all
    @GetMapping()
    List<ProductCondensed> findAll() {
        return productService.findAll()
                .stream()
                .map(ProductCondensed::new)
                .toList();
    }

    /* GET request that returns all the product names */
    // GET http://localhost:8080/products/all
    @GetMapping("names")
    List<ProductName> findAllNames() {
        return productService.findAll()
                .stream()
                .map(ProductName::new)
                .toList();
    }

    // GET requests finds by id returns one product */
    // GET http://localhost:8080/products/{{id}}
    @GetMapping("{id}")
    ProductWithAllDetails findById(@PathVariable long id) {
        return productService.findById(id)
                // .stream()
                .map(ProductWithAllDetails::new)
                // .toList();
                .orElseThrow(ProductNotFoundException::new);
    }

    // GET requests finds by categoryId returns one product */
    // GET http://localhost:8080/products/bycategory/{{id}}
    @GetMapping("/bycategory/{categoryId}")
    List<ProductCondensed> findByCategoryId(@PathVariable long categoryId) {
        return productService.findByCategoryId(categoryId)
                .stream()
                .map(ProductCondensed::new)
                .toList();
    }

    // GET requests finds by originId returns one product */
    // GET http://localhost:8080/products/byorigin/{{id}}
    @GetMapping("/byorigin/{originId}")
    List<ProductCondensed> findByOriginId(@PathVariable long originId) {
        return productService.findByOriginId(originId)
                .stream()
                .map(ProductCondensed::new)
                .toList();
    }

    // GET request to find the products by materialId
    // GET http://localhost:8080/products/bymaterial/{{materialId}}
    @GetMapping("byMaterial/{materialId}")
    List<ProductCondensed> findByMaterialId(@PathVariable long materialId) {
        return productService.findByMaterialId(materialId)
                .stream()
                .map(ProductCondensed::new)
                .toList();
    }

    // GET request to find products by a set of materials
    // GET http://localhost:8080/products/bymaterials?materialIds=1,2,3
    @GetMapping("byMaterials")
    List<ProductCondensed> findByMaterialIds(@RequestParam Set<Long> materialIds) {
        return productService.findByMaterialIds(materialIds)
                .stream()
                .map(ProductCondensed::new)
                .toList();
    }

    // GET request to find all products in stock
    // GET http://localhost:8080/products/instock
    @GetMapping("inStock")
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
    // POST http://localhost:8080/products
    /* Thema 9: Toevoegen */
    @PostMapping()
    long create(@RequestBody @Valid NewProduct newProduct) {
        return productService.create(newProduct);
    }
/*
###
POST http://localhost:8080/products
Content-Type: application/json

{
  "code": "CW-006",
  "name": "Copper cup large with flower motives",
  "description": "Copper cup large size made...",
  "price": 22,
  "stock": 50,
  "imageUrl": "image/dos.png",
  "active": true,
  "categoryId": 1,
  "originId": 2,
  "materialIds": [
     1, 8
  ]
}
 */

    /* PUT request to update a Product by id */
    // PUT http://localhost:8080/products/{{id}}
    @PutMapping("{id}")
    void update(@PathVariable long id,
                @RequestBody @Valid EditProduct editProduct) {
        productService.update(id, editProduct);
    }

    /* DELETE request to delete a product by id */
    //DELETE http://localhost:8080/products/{{id}}
    @DeleteMapping("{id}")
    void delete(@PathVariable long id) {
        productService.delete(id);
    }

}