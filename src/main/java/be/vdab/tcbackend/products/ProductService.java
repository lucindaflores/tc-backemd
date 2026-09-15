package be.vdab.tcbackend.products;

import be.vdab.tcbackend.categories.CategoryNotFoundException;
import be.vdab.tcbackend.categories.CategoryRepository;
import be.vdab.tcbackend.origins.OriginNotFoundException;
import be.vdab.tcbackend.origins.OriginRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional(readOnly=true)
class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final OriginRepository originRepository;
    private final MaterialRepository materialRepository;

    ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, OriginRepository originRepository, MaterialRepository materialRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.originRepository = originRepository;
        this.materialRepository = materialRepository;
    }

    /* Method that returns the count of products in table products */
    long findCount() {
        return productRepository.count();
    }

    /* Method that returns a list of all products */
    List<Product> findAll() {
        return productRepository.findAll();
    }

    /* Method that finds by id and returns an optional Product  */
    Optional<Product> findById(long id) {
        return productRepository.findById(id);
    }

    /* Method that finds by categoryId and returns  a list of products */
    List<Product> findByCategoryId(long categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    /* Method that finds by originId and returns  a list of products */
    List<Product> findByOriginId(long originId) {
        return  productRepository.findByOriginId(originId);
    }

    /* Method that finds by materialId and returns a list of products */
    List<Product> findByMaterialId(long materialId) {
        return productRepository.findDistinctByMaterialsId(materialId);
    }

    /* Method that find a list of products for a Set of materials */
    List<Product> findByMaterialIds(Set<Long> materialIds) {
        return productRepository.findDistinctByMaterials_IdIn(materialIds);
    }

    /* Method that finds a list of products if the stock >0 */
    List<Product> findInStock() {
        return productRepository.findByStockGreaterThan(0);
    }

    /* Method that finds the stock of a product */
    int findStockById(long id) {
        return productRepository.findStockById(id)
                .orElseThrow(ProductNotFoundException::new);
    }

    /* Method that creates a new product */
    /* Thema 9: Toevoegen */
    // UPDATE - Thema 21: @ManyToOne
    @Transactional
    long create(NewProduct newProduct) {
        try {
            // Find the cat
            var category = categoryRepository.findById(newProduct.categoryId())
                    .orElseThrow(CategoryNotFoundException::new);
            // RETURNS: Optional<Category>

            //  Find origin
            var origin = originRepository.findById(newProduct.originId())
                    .orElseThrow(OriginNotFoundException::new);
            // RETURNS: Optional<Origin>

            // Find Materials
            // Product: materials added in the Product constructor & function
            var materials = materialRepository.findAllById(newProduct.materialIds());

            IO.println("Requested: " + newProduct.materialIds());
            IO.println("Found: " + materials.size());

            // Compares sets sizes to see if they have the same number of elements
            if (materials.size() != newProduct.materialIds().size()) {
                throw new MaterialNotFoundException();
            }

            var product = new Product(newProduct.code(),
                    newProduct.name(),
                    newProduct.description(),
                    newProduct.price(),
                    newProduct.stock(),
                    newProduct.imageUrl(),
                    newProduct.active(),
                    category,
                    origin);

            productRepository.save(product);

            // Adding the materials to product_materials table
            for (var material : materials) {
                product.add(material); // owning side
                material.add(product); // inverse side to synchronize the set
            }
           // materials.forEach(product::add);

             return product.getId();
        } catch (DataIntegrityViolationException _) {
            throw new ProductAlreadyExistsException();
        }
    }

    /* Method that updates all fields for the product model */
    @Transactional
    void update(long id, EditProduct editProduct) {
        try {
        var product = productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);

        var category = categoryRepository.findById(editProduct.categoryId())
                .orElseThrow(CategoryNotFoundException::new);

        var origin = originRepository.findById(editProduct.originId())
                .orElseThrow(OriginNotFoundException::new);

        var materials = materialRepository.findAllById(editProduct.materialIds());

            if (materials.size() != editProduct.materialIds().size()) {
                throw new MaterialNotFoundException();
            }

        product.update(editProduct.code(),
                    editProduct.name(),
                    editProduct.description(),
                    editProduct.price(),
                    editProduct.stock(),
                    editProduct.imageUrl(),
                    editProduct.active(),
                    category,
                    origin);

        product.replaceMaterials(materials);
        } catch (DataIntegrityViolationException _) {
            throw new ProductAlreadyExistsException();
        }
    }

    /* Thema 10: Verwijderen */
    @Transactional
    void delete(long id) {
        var product = productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);

        productRepository.delete(product);
    }

}
