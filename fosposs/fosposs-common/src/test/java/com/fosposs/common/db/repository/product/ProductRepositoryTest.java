package com.fosposs.common.db.repository.product;

import com.fosposs.common.db.config.DatabaseConfig;
import com.fosposs.common.db.config.SQLiteConfig;
import com.fosposs.common.db.connection.ConnectionManager;
import com.fosposs.common.db.exception.DatabaseException;
import com.fosposs.common.db.repository.category.CategoryRepository;
import com.fosposs.common.model.Category;
import com.fosposs.common.model.Product;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Product Repository Operations")
class ProductRepositoryTest {
    private static DatabaseConfig config;
    private ProductRepository repository;
    private CategoryRepository categoryRepository;
    private Product testProduct;
    private Category testCategory;

    @BeforeAll
    static void setupDatabase() throws Exception {
        // Use in-memory SQLite database for testing
        config = new SQLiteConfig(Path.of(":memory:"));
        ConnectionManager.initialize(config);
        config.initialize(List.of(
                CategoryRepository.class,
                ProductRepository.class)
        );
    }

    @AfterAll
    static void tearDownDatabase() {
        ConnectionManager.shutdown();
    }

    @BeforeEach
    void setUp() throws DatabaseException {
        repository = new ProductRepository();
        categoryRepository = new CategoryRepository();

        // Create and save a test category
        testCategory = categoryRepository.save(
                Category.builder().name("Test Category").build()
        );

        testProduct = Product.builder()
                .name("Test Coffee")
                .description("Premium test beans")
                .barcode("TEST123")
                .price(new BigDecimal("3.99"))
                .cost(new BigDecimal("2.00"))
                .categoryId(testCategory.id())
                .stockQuantity(50)
                .minStockLevel(10)
                .build();

        // Start fresh
        repository.clear();
    }

    @Test
    @DisplayName("Should save and retrieve a product")
    void shouldSaveAndRetrieveProduct() throws DatabaseException {
        // Save the product
        Product saved = repository.save(testProduct);
        assertNotNull(saved);

        // Retrieve the product
        Product retrieved = repository.findById(saved.id());
        assertNotNull(retrieved);

        // Verify all fields match
        assertEquals(saved.id(), retrieved.id());
        assertEquals("Test Coffee", retrieved.name());
        assertEquals("Premium test beans", retrieved.description());
        assertEquals("TEST123", retrieved.barcode());
        assertEquals(0, new BigDecimal("3.99").compareTo(retrieved.price()));
        assertEquals(0, new BigDecimal("2.00").compareTo(retrieved.cost()));
        assertEquals(testCategory.id(), retrieved.categoryId());
        assertEquals(50, retrieved.stockQuantity());
        assertEquals(10, retrieved.minStockLevel());
        assertTrue(retrieved.active());
    }

    @Test
    @DisplayName("Should retrieve all products ordered by name")
    void shouldRetrieveAllProductsOrdered() throws DatabaseException {
        // Save a couple of products
        repository.save(Product.builder().name("Product 1").build());
        repository.save(Product.builder().name("Product 2").build());
        repository.save(Product.builder().name("Product 3").build());

        // Retrieve them
        var products = repository.findAll();

        // Verify
        assertNotNull(products);
        assertEquals(3, products.size());
        assertEquals(
                List.of("Product 1", "Product 2", "Product 3"),
                products.stream().map(Product::name).toList()
        );
    }

    @Test
    @DisplayName("Search should return empty result when there is no match")
    void searchReturnsEmptyNoMatch() throws DatabaseException {
        // Save a couple of products
        repository.save(Product.builder().name("Coffee").build());
        repository.save(Product.builder().name("Orange Juice").build());
        repository.save(Product.builder().name("Water").build());

        // Try to fetch a non-existent product
        var products = repository.search("Tea");

        // Verify empty result set
        assertNotNull(products);
        assertTrue(products.isEmpty());
    }

    @Test
    @DisplayName("Search should return matching products")
    void searchReturnsMatchingProducts() throws DatabaseException {
        // Save a couple of products
        repository.save(Product.builder().name("Coffee").build());
        repository.save(Product.builder().name("Orange Juice").build());
        repository.save(Product.builder().name("Black Tea").build());

        // Try to fetch an existent product
        var products = repository.search("Tea");

        // Verify results
        assertNotNull(products);
        assertEquals(1, products.size());
        assertEquals("Black Tea", products.getFirst().name());
    }

    @Test
    @DisplayName("Should update existing product")
    void shouldUpdateExistingProduct() throws DatabaseException {
        // Create another category
        Category anotherCategory = categoryRepository.save(
                Category.builder().name("Another Category").build()
        );

        // Save initial product
        Product saved = repository.save(testProduct);

        // Create updated version
        Product updated = Product.builder()
                .id(saved.id())
                .name("Updated Coffee")
                .description("Updated description")
                .barcode(saved.barcode())
                .price(new BigDecimal("4.99"))
                .cost(saved.cost())
                .categoryId(anotherCategory.id())
                .stockQuantity(saved.stockQuantity())
                .minStockLevel(saved.minStockLevel())
                .build();

        // Save updated product
        repository.save(updated);

        // Retrieve and verify
        Product retrieved = repository.findById(saved.id());
        assertNotNull(retrieved);
        assertEquals("Updated Coffee", retrieved.name());
        assertEquals("Updated description", retrieved.description());
        assertEquals(0, new BigDecimal("4.99").compareTo(retrieved.price()));
        assertEquals(anotherCategory.id(), retrieved.categoryId());
    }

    @Test
    @DisplayName("Should delete product")
    void shouldDeleteProduct() throws DatabaseException {
        // Save product
        Product saved = repository.save(testProduct);
        assertTrue(repository.exists(saved.id()));

        // Delete product
        var isDeleted = repository.delete(saved.id());

        // Verify deletion
        assertTrue(isDeleted);
        assertFalse(repository.exists(saved.id()));
        assertNull(repository.findById(saved.id()));
    }

    @Test
    @DisplayName("Should handle non-existent product")
    void shouldHandleNonExistentProduct() throws DatabaseException {
        UUID randomId = UUID.randomUUID();
        assertNull(repository.findById(randomId));
        assertFalse(repository.exists(randomId));
    }

    @Test
    @DisplayName("Should handle empty product fields appropriately")
    void shouldHandleEmptyFields() throws DatabaseException {
        Product minimalProduct = Product.builder()
                .name("Minimal Product") // Only required field
                .build();

        Product saved = repository.save(minimalProduct);
        Product retrieved = repository.findById(saved.id());

        assertNotNull(retrieved);
        assertEquals("Minimal Product", retrieved.name());
        assertEquals("", retrieved.description());
        assertNull(retrieved.barcode());
        assertEquals(0, BigDecimal.ZERO.compareTo(retrieved.price()));
    }
}