package com.fosposs.common.db.repository.category;

import com.fosposs.common.db.config.DatabaseConfig;
import com.fosposs.common.db.config.SQLiteConfig;
import com.fosposs.common.db.connection.ConnectionManager;
import com.fosposs.common.db.exception.DatabaseException;
import com.fosposs.common.db.repository.product.ProductRepository;
import com.fosposs.common.model.Category;
import com.fosposs.common.model.Product;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Category Repository operations")
class CategoryRepositoryTest {
    private static DatabaseConfig config;
    private CategoryRepository repository;
    private ProductRepository productRepository;
    private Category testCategory;

    @BeforeAll
    static void setupDatabase() throws Exception {
        // Use in-memory SQLite database for testing
        config = new SQLiteConfig(Path.of(":memory:"));
        ConnectionManager.initialize(config);
        config.initialize(List.of(
                CategoryRepository.class,
                ProductRepository.class
        ));
    }

    @AfterAll
    static void tearDownDatabase() {
        ConnectionManager.shutdown();
    }

    @BeforeEach
    void setUp() throws DatabaseException {
        repository = new CategoryRepository();
        productRepository = new ProductRepository();

        testCategory = Category.builder()
                .name("Test Category")
                .description("Test description")
                .sortOrder(1)
                .build();

        // Start fresh
        repository.clear();
    }

    @Test
    @DisplayName("Should save and retrieve a category")
    void shouldSaveAndRetrieveCategory() throws DatabaseException {
        // Save the category
        Category saved = repository.save(testCategory);
        assertNotNull(saved);

        // Retrieve the category
        Category retrieved = repository.findById(saved.id());
        assertNotNull(retrieved);

        // Verify all fields match
        assertEquals(saved.id(), retrieved.id());
        assertEquals("Test Category", retrieved.name());
        assertEquals("Test description", retrieved.description());
        assertNull(retrieved.parentId());
        assertEquals(1, retrieved.sortOrder());
        assertTrue(retrieved.active());
    }

    @Test
    @DisplayName("Should retrieve all categories ordered by name")
    void shouldRetrieveAllCategoriesOrdered() throws DatabaseException {
        // Save some categories
        repository.save(Category.builder().name("Category 1").build());
        repository.save(Category.builder().name("Category 2").build());
        repository.save(Category.builder().name("Category 3").build());

        // Retrieve them
        var categories = repository.findAll();

        // Verify
        assertNotNull(categories);
        assertEquals(3, categories.size());
        assertEquals(
                List.of("Category 1", "Category 2", "Category 3"),
                categories.stream().map(Category::name).toList()
        );
    }

    @Test
    @DisplayName("Search should return empty result when there is no match")
    void searchReturnsEmptyNoMatch() throws DatabaseException {
        // Save some categories
        repository.save(Category.builder().name("Category 1").build());
        repository.save(Category.builder().name("Category 2").build());
        repository.save(Category.builder().name("Category 3").build());

        // Search for a non-existent category
        var categories = repository.search("Beverages");

        // Verify empty result set
        assertNotNull(categories);
        assertTrue(categories.isEmpty());
    }

    @Test
    @DisplayName("Search should return matching categories")
    void searchReturnsMatchingCategories() throws DatabaseException {
        // Save some categories
        repository.save(Category.builder().name("Some Category").build());
        repository.save(Category.builder().name("Another Category").build());
        repository.save(Category.builder().name("Beverages").build());

        // Search for an existing category
        var categories = repository.search("Category");

        // Verify
        assertNotNull(categories);
        assertEquals(2, categories.size());
        assertTrue(categories.stream().map(Category::name).allMatch(c -> c.contains("Category")));
    }

    @Test
    @DisplayName("Should update existing category")
    void shouldUpdateExistingCategory() throws DatabaseException {
        // Save initial category
        Category saved = repository.save(testCategory);

        // Create updated version
        Category updated = Category.builder()
                .id(saved.id())
                .name("Updated Category")
                .description("Updated description")
                .sortOrder(2)
                .build();

        // Save updated category
        repository.save(updated);

        // Retrieve and verify
        Category retrieved = repository.findById(saved.id());
        assertNotNull(retrieved);
        assertEquals("Updated Category", retrieved.name());
        assertEquals("Updated description", retrieved.description());
        assertEquals(2, retrieved.sortOrder());
    }

    @Test
    @DisplayName("Should delete category")
    void shouldDeleteCategory() throws DatabaseException {
        // Save category
        Category saved = repository.save(testCategory);
        assertTrue(repository.exists(saved.id()));

        // Delete category
        var isDeleted = repository.delete(saved.id());

        // Verify deletion
        assertTrue(isDeleted);
        assertFalse(repository.exists(saved.id()));
        assertNull(repository.findById(saved.id()));
    }

    @Test
    @DisplayName("Should handle non-existent category")
    void shouldHandleNonExistentCategory() throws DatabaseException {
        UUID randomId = UUID.randomUUID();
        assertNull(repository.findById(randomId));
        assertFalse(repository.exists(randomId));
    }

    @Test
    @DisplayName("Should find root categories")
    void shouldFindRootCategories() throws DatabaseException {
        // Create root categories
        Category root1 = repository.save(Category.builder().name("Root 1").sortOrder(1).build());
        Category root2 = repository.save(Category.builder().name("Root 2").sortOrder(2).build());
        Category root3 = repository.save(Category.builder().name("Root 3").sortOrder(3).build());

        // Create child category
        repository.save(Category.builder()
                .name("Child 1")
                .parentId(root1.id())
                .sortOrder(1)
                .build());

        // Get root categories
        var rootCategories = repository.findRootCategories();

        // Verify
        assertEquals(3, rootCategories.size());
        assertEquals("Root 1", rootCategories.get(0).name());
        assertEquals("Root 2", rootCategories.get(1).name());
        assertEquals("Root 3", rootCategories.get(2).name());
    }

    @Test
    @DisplayName("Should find child categories")
    void shouldFindChildCategories() throws DatabaseException {
        // Create a parent category
        Category parent = repository.save(Category.builder().name("Parent").build());

        // Create child categories
        repository.save(Category.builder()
                .name("Child 1")
                .parentId(parent.id())
                .sortOrder(1)
                .build());
        repository.save(Category.builder()
                .name("Child 2")
                .parentId(parent.id())
                .sortOrder(2)
                .build());
        repository.save(Category.builder()
                .name("Child 3")
                .parentId(parent.id())
                .sortOrder(3)
                .build());

        // Create another category that's not a child of parent
        repository.save(Category.builder().name("Not Child").build());

        // Get child categories
        var childCategories = repository.findChildCategories(parent.id());

        // Verify
        assertEquals(3, childCategories.size());
        assertEquals("Child 1", childCategories.get(0).name());
        assertEquals("Child 2", childCategories.get(1).name());
        assertEquals("Child 3", childCategories.get(2).name());
    }

    @Test
    @DisplayName("Should prevent deleting category with children")
    void shouldPreventDeletingCategoryWithChildren() throws DatabaseException {
        // Create a parent category
        Category parent = repository.save(Category.builder().name("Parent").build());

        // Create a child category
        repository.save(Category.builder()
                .name("Child")
                .parentId(parent.id())
                .build());

        // Try to delete
        assertThrows(DatabaseException.class, () -> repository.delete(parent.id()));
        assertTrue(repository.exists(parent.id()));
    }

    @Test
    @DisplayName("Should prevent deleting category with products")
    void shouldPreventDeletingCategoryWithProducts() throws DatabaseException {
        // Create a category
        Category category = repository.save(Category.builder().name("Category").build());

        // Create a product in that category
        Product product = Product.builder()
                .name("Test Product")
                .price(new BigDecimal("9.99"))
                .cost(new BigDecimal("5.99"))
                .categoryId(category.id())
                .build();
        productRepository.save(product);

        // Try to delete
        assertThrows(DatabaseException.class, () -> repository.delete(category.id()));
        assertTrue(repository.exists(category.id()));
    }

    @Test
    @DisplayName("Should check if category has children correctly")
    void shouldCheckIfCategoryHasChildrenCorrectly() throws DatabaseException {
        // Create a parent category
        Category parent = repository.save(Category.builder().name("Parent").build());

        // Initially should have no children
        assertFalse(repository.hasChildren(parent.id()));

        // Add a child
        repository.save(Category.builder()
                .name("Child")
                .parentId(parent.id())
                .build());

        // Now should have children
        assertTrue(repository.hasChildren(parent.id()));
    }

    @Test
    @DisplayName("Should create and retrieve category path correctly")
    void shouldCreateAndRetrieveCategoryPathCorrectly() throws DatabaseException {
        // Create hierarchy
        Category electronics = repository.save(Category.builder()
                .name("Electronics")
                .build());
        Category computers = repository.save(Category.builder()
                .name("Computers")
                .parentId(electronics.id())
                .build());
        Category laptops = repository.save(Category.builder()
                .name("Laptops")
                .parentId(computers.id())
                .build());

        // Get path for laptops
        var path = repository.getCategoryPath(laptops.id());

        // Verify
        assertEquals(3, path.size());
        assertEquals("Electronics", path.get(0));
        assertEquals("Computers", path.get(1));
        assertEquals("Laptops", path.get(2));
    }

}