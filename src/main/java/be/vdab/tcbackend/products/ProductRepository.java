package be.vdab.tcbackend.products;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.Set;

// <entity class, variable type of the PK>
public interface ProductRepository extends JpaRepository<Product, Long> {

    /* Method that finds by categoryId and returns optional product */
    List<Product> findByCategoryId(long categoryId);

    /* Method that finds by originId and returns optional product */
    List<Product> findByOriginId(long originId);

    /* Method that find a list of products for a  materialId */
    List<Product> findDistinctByMaterialsId(long materialId);

    /* Method that find a list of products for a Set of materials */
    List<Product> findDistinctByMaterials_IdIn(Set<Long> materialIds);

    /* Method that finds the current version of a given id */
    @Query("""
       select product.version
       from Product product
       where product.id = :id
       """)
    long findVersionById(long id);

    /* Method that finds a list of products if the stock >0 */
    List<Product> findByStockGreaterThan(int stock);

    /* Method that finds the stock of a product */
    @Query("""
       select product.stock
       from Product product
       where product.id = :id
       """)
    Optional<Integer> findStockById(long id);



}
