package be.vdab.tcbackend;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
@Sql({"/productsTesting.sql", "/materialsTesting.sql"})
@AutoConfigureMockMvc
class ProductControllerTest {

    private final static String PRODUCTS_TABLE = "products";
    private final MockMvcTester mockMvcTester;

    private final JdbcClient jdbcClient;
    private final EntityManager entityManager;

    private static String URL= "/products";

    ProductControllerTest(MockMvcTester mockMvcTester, JdbcClient jdbcClient, EntityManager entityManager) {
        this.mockMvcTester = mockMvcTester;
        this.jdbcClient = jdbcClient;
        this.entityManager = entityManager;
    }

    /* Helper methods */
    // This method looks for the ide of the 'test1'
    // FIXED: Settings → Languages & Frameworks → SQL Resolution Scopes (top dropdpwn)
    private int idOfTestProduct1() {
        return jdbcClient.sql("select id from products where name = 'Test Product 1'")
                .query(Integer.class)
                .single();
    }

    /* Tests */
    @Test
    @DisplayName("Find product by ID returns the correct product")
    void findByIdReturnsTestProduct1() {
        var productId = idOfTestProduct1();

        mockMvcTester.get()
                .uri(URL + "/" + productId)
                .assertThat()
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$.name")
                .isEqualTo("Test Product 1");

    }

    // GET http://localhost:8080/products/18
    @Test
    @DisplayName("Find by unknown ID returns HTTP 404")
    void findByIdWithUnknownIdReturnsNotFound() {
        mockMvcTester.get()
                .uri(URL + "/9999")
                .assertThat()
                .hasStatus(HttpStatus.NOT_FOUND);
    }

    // Compares with the count of the db
    @Test
    @DisplayName("Count returns the number of products in the database")
    void findCountReturnsTheNumberOfProducts() {
        var expectedNumberOfProducts = JdbcTestUtils.countRowsInTable(jdbcClient, PRODUCTS_TABLE);

        mockMvcTester.get()
                .uri(URL + "/count")
                .assertThat()
                .hasStatusOk()
                .bodyText()
                .isEqualTo(String.valueOf(expectedNumberOfProducts));
    }


    @Test
    @DisplayName("Find all returns all products")
    void findAllReturnsAllProducts() {
        var expectedNumberOfProducts = JdbcTestUtils.countRowsInTable(jdbcClient, PRODUCTS_TABLE);

        mockMvcTester.get()
                .uri(URL + "/all")
                .assertThat()
                .hasStatusOk()
                .bodyJson()
                .extractingPath("length()")
                .isEqualTo(expectedNumberOfProducts);
    }

    @Test
    @DisplayName("Find all contains the SQL test product1")
    void findAllContainsTestProduct1() {
        mockMvcTester.get()
                .uri(URL + "/all")
                .assertThat()
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$[*].name") //$ every array of element [*]
                .asList()
                .contains("Test Product 1");
    }

    @Test
    @DisplayName("Create valid product that persists it in the database")
    void createAddsAProduct() throws Exception {
        var jsonData = new ClassPathResource("jsonTesting/productCorrect.json")
                .getContentAsString(StandardCharsets.UTF_8);

        mockMvcTester.post()
                .uri(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData)
                .assertThat()
                .hasStatusOk();

        // forces Hibernate to send pending SQL statements to the database immediately
        // This is just needed in the test!
        entityManager.flush();

        assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcClient, PRODUCTS_TABLE, "code = 'TE-003'")).isOne();
    }

    @Test
    @DisplayName("Creating a product persists its material relationship")
    void createAddsMaterialToProductMaterials() throws Exception {
        var jsonData = new ClassPathResource("jsonTesting/productCorrect.json")
                .getContentAsString(StandardCharsets.UTF_8);

        mockMvcTester.post()
                .uri(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData)
                .assertThat()
                .hasStatusOk();

        entityManager.flush();

        var productId = jdbcClient.sql("""
                select id
                from products
                where code = 'TE-003'
                """)
                .query(Integer.class)
                .single();

        var numberOfMaterialLinks = jdbcClient.sql("""
                select count(*)
                from product_materials
                where product_id = :productId
                and material_id = 1
                """)
                .param("productId", productId)
                .query(Integer.class)
                .single();

        assertThat(numberOfMaterialLinks).isEqualTo(1);
    }

    @ParameterizedTest
    @DisplayName("Invalid product data returns HTTP 400")
    @ValueSource(strings = {"jsonTesting/productWithEmptyCode.json",
                            "jsonTesting/productWithEmptyName.json",
                            "jsonTesting/productWithNegativePrice.json",
                            "jsonTesting/productWithNegativeStock.json",
                            "jsonTesting/productWithoutCode.json",
                            "jsonTesting/productWithoutDescription.json",
                            "jsonTesting/productWithPriceZero.json",
                             "jsonTesting/productWithoutPrice.json",
                             "jsonTesting/productWithoutStock.json",
            "jsonTesting/productWithoutCategoru.json",
    })
    void productWithWrongDataFails(String fileName) throws Exception {
        var jsonData = new ClassPathResource(fileName)
                .getContentAsString(StandardCharsets.UTF_8);

        mockMvcTester.post()
                .uri(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData)
                .assertThat()
                .hasStatus(HttpStatus.BAD_REQUEST);
    }

        @Test
    @DisplayName("Invalid product data returns HTTP 404")
    void productWithWrongDataDoesNotExists() throws Exception {
        var jsonData = new ClassPathResource("jsonTesting/productWithAnInvalidOriginId.json")
                .getContentAsString(StandardCharsets.UTF_8);

        mockMvcTester.post()
                .uri(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData)
                .assertThat()
                .hasStatus(HttpStatus.NOT_FOUND);
    }

    @Test
    @DisplayName("Product with zero stock can be created")
    void createWithZeroStockSucceeds() throws Exception {
        var jsonData = new ClassPathResource("jsonTesting/productWithStockZero.json")
                .getContentAsString(StandardCharsets.UTF_8);

        mockMvcTester.post()
                .uri(URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonData)
                .assertThat()
                .hasStatusOk();
    }

    @Test
    @DisplayName("Find by category returns products from that category")
    void findByCategoryIdReturnsProductsOfThatCategory() {
        mockMvcTester.get()
                .uri(URL + "/bycategory/1")
                .assertThat()
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$[*].name")
                .asList()
                .contains("Test Product 1");
    }

    @Test
    @DisplayName("Find by origin returns products from that origin")
    void findByOriginIdReturnsProductsOfThatOrigin() {
        mockMvcTester.get()
                .uri(URL + "/byorigin/1")
                .assertThat()
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$[*].name")
                .asList()
                .contains("Test Product 1");
    }

    @Test
    @DisplayName("Find in stock by Id returns the stock of that product ")
    void findStockByIdReturnsTheStockOfProduct() {
        var productId = idOfTestProduct1();

        var expectedStock = jdbcClient.sql("""
            select stock
            from products
            where id = :id
            """)
                .param("id", productId)
                .query(Integer.class)
                .single();

        mockMvcTester.get()
                .uri(URL + "/" + productId + "/stock")
                .assertThat()
                .hasStatusOk()
                .bodyText()
                .isEqualTo(String.valueOf(expectedStock));
    }

    @Test
    @DisplayName("Find in stock returns all products with available stock")
    void findInStockReturnsOnlyProductsWithStock() {
        mockMvcTester.get()
                .uri(URL + "/instock")
                .assertThat()
                .hasStatusOk()
                .bodyJson()
                .extractingPath("$[*].name")  //$ every array of element [*]
                .asList()
                .contains("Test Product 1");
    }

    @Test
    @DisplayName("Deleting a product removes it from the database")
    void deleteRemovesProduct() {
        var productId = idOfTestProduct1();

        mockMvcTester.delete()
                .uri(URL + "/" + productId)
                .assertThat()
                .hasStatusOk();

        entityManager.flush();

        assertThat(JdbcTestUtils.countRowsInTableWhere(jdbcClient, PRODUCTS_TABLE, "id = " + productId)).isZero();

    }

    @Test
    @DisplayName("Deleting a product with an unknown id throws exception")
    void deleteWithUnknownIdThrows() throws Exception {
        mockMvcTester.delete()
                .uri(URL + "/999999")
                .assertThat()
                .hasStatus(HttpStatus.NOT_FOUND);

    }


    @Test
    @DisplayName("Deleting a product removes its material links but keeps the material")
    void deleteProductWithMaterialsRemovesOnlyMaterialLinks() {
        var productId = idOfTestProduct1();

        var materialId = 99990;

        // When
        mockMvcTester.delete()
                .uri(URL + "/" + productId)
                .assertThat()
                .hasStatusOk();

        entityManager.flush();

        // Then: product is gone
        var productCount = JdbcTestUtils.countRowsInTableWhere(
                jdbcClient,
                "products",
                "id = " + productId
        );

        assertThat(productCount).isZero();

        // Join-table relationship is gone
        var materialLinkCount = JdbcTestUtils.countRowsInTableWhere(
                jdbcClient,
                "product_materials",
                "product_id = " + productId
        );

        assertThat(materialLinkCount).isZero();

        // But Material itself still exists
        var materialCount = JdbcTestUtils.countRowsInTableWhere(
                jdbcClient,
                "materials",
                "id = " + materialId
        );

        assertThat(materialCount).isOne();
    }

}
