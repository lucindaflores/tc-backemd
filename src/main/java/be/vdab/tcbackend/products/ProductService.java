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
    private final MaterialsRepository materialsRepository;

    ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, OriginRepository originRepository, MaterialsRepository materialsRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.originRepository = originRepository;
        this.materialsRepository = materialsRepository;
    }

    /* Method that returns the count of products in table products */
    long findCount() {
        return productRepository.count();
    }

    long findVersionById(long id) { return productRepository.findVersionById(id); }

    /* Method that returns a list of all products */
    List<Product> findAll() {
        return productRepository.findAll();
    }

    /* Method that finds by id and returns an optional Product  */
    Optional<Product> findById(long id) {
        return  productRepository.findById(id);
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
            // 2. Find the Category identified by categoryId.
            var category = categoryRepository.findById(newProduct.categoryId())
                    .orElseThrow(CategoryNotFoundException::new);
            // RETURNS: Optional<Category>

            //3. If originId exists: find Origin.
            var origin = originRepository.findById(newProduct.originId())
                    .orElseThrow(OriginNotFoundException::new);
            // RETURNS: Optional<Origin>

            // 4. Find Materials identified by materialIds.
            // Product: materials added in the Product constructor & function
            var materials = materialsRepository.findAllById(newProduct.materialIds());

           //  5. Create a Product Java object.
            var product = new Product(newProduct.code(),
                    newProduct.name(),
                    newProduct.description(),
                    newProduct.price(),
                    newProduct.stock(),
                    newProduct.imageUrl(),
                    newProduct.isActive(),
                    category,
                    origin);

            //  6. Ask ProductRepository to save it.
            productRepository.save(product);

            // Adding the materials to product_materials table
            for (var material : materials) {
                material.add(product); // owning side
                product.add(material); // opposite side
            }
           // materials.forEach(product::add);

            // 7. Return its generated ID.
             return product.getId();
        } catch (DataIntegrityViolationException _) {
            throw new ProductAlreadyExistsException();
        }
    }

    @Transactional
    void update(long id, EditProduct editProduct) {
        try {
        var product = productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);


        if (product.getVersion() != editProduct.version()) {
            throw new ProductVersionConflictException();
        }

        var category = categoryRepository.findById(editProduct.categoryId())
                .orElseThrow(CategoryNotFoundException::new);

        var origin = originRepository.findById(editProduct.originId())
                .orElseThrow(OriginNotFoundException::new);

        var materials = materialsRepository.findAllById(editProduct.materialIds());

        product.update(editProduct.code(),
                    editProduct.name(),
                    editProduct.description(),
                    editProduct.price(),
                    editProduct.stock(),
                    editProduct.imageUrl(),
                    editProduct.isActive(),
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
