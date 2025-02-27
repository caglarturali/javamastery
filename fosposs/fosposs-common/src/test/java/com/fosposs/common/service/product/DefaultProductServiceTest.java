package com.fosposs.common.service.product;

import com.fosposs.common.db.exception.DatabaseException;
import com.fosposs.common.db.repository.product.ProductRepository;
import com.fosposs.common.model.Product;
import com.fosposs.common.service.exception.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("DefaultProductService Tests")
class DefaultProductServiceTest {

    private ProductRepository mockRepository;
    private DefaultProductService service;
    private Product testProduct;
    private UUID testId;
    private UUID testCategoryId;

    @BeforeEach
    void setUp() {
        mockRepository = mock(ProductRepository.class);
        service = new DefaultProductService(mockRepository);
        testId = UUID.randomUUID();
        testCategoryId = UUID.randomUUID();
        testProduct = Product.builder()
                .id(testId)
                .name("Test Product")
                .description("Test Description")
                .price(new BigDecimal("9.99"))
                .cost(new BigDecimal("5.99"))
                .stockQuantity(10)
                .minStockLevel(5)
                .categoryId(testCategoryId)
                .build();
    }

    @Test
    @DisplayName("create should save the product")
    void createShouldSaveProduct() throws DatabaseException, ServiceException {
        // Arrange
        when(mockRepository.save(testProduct)).thenReturn(testProduct);

        // Act
        Product result = service.create(testProduct);

        // Assert
        assertNotNull(result);
        assertEquals(testProduct, result);
        verify(mockRepository).save(testProduct);
    }

    @Test
    @DisplayName("create should throw ServiceException when save fails")
    void createShouldThrowServiceExceptionWhenSaveFails() throws DatabaseException {
        // Arrange
        when(mockRepository.save(testProduct))
                .thenThrow(new DatabaseException("Test Database Exception"));

        // Act & Assert
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            service.create(testProduct);
        });
        assertTrue(exception.getMessage().contains("Failed to create product"));
        assertInstanceOf(DatabaseException.class, exception.getCause());
    }

    @Test
    @DisplayName("update should verify existence then save product")
    void updateShouldVerifyExistenceThenSaveProduct() throws DatabaseException, ServiceException {
        // Arrange
        when(mockRepository.exists(testId)).thenReturn(true);
        when(mockRepository.save(testProduct)).thenReturn(testProduct);

        // Act
        Product result = service.update(testProduct);

        // Assert
        assertNotNull(result);
        assertEquals(testProduct, result);
        verify(mockRepository).exists(testId);
        verify(mockRepository).save(testProduct);
    }

    @Test
    @DisplayName("update should throw ServiceException when product doesn't exist")
    void updateShouldThrowServiceExceptionWhenProductDoesntExist() throws DatabaseException {
        // Arrange
        when(mockRepository.exists(testId)).thenReturn(false);

        // Act & Assert
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            service.update(testProduct);
        });
        assertTrue(exception.getMessage().contains("Product not found"));
        verify(mockRepository).exists(testId);
        verify(mockRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("getById should return product from repository")
    void getByIdShouldReturnProductFromRepository() throws DatabaseException, ServiceException {
        // Arrange
        when(mockRepository.findById(testId)).thenReturn(testProduct);

        // Act
        Product result = service.getById(testId);

        // Assert
        assertNotNull(result);
        assertEquals(testProduct, result);
        verify(mockRepository).findById(testId);
    }

    @Test
    @DisplayName("getById should return null when product doesn't exist")
    void getByIdShouldReturnNullWhenProductDoesntExist() throws DatabaseException, ServiceException {
        // Arrange
        when(mockRepository.findById(testId)).thenReturn(null);

        // Act
        Product result = service.getById(testId);

        // Assert
        assertNull(result);
        verify(mockRepository).findById(testId);
    }

    @Test
    @DisplayName("getAll should return all products from repository")
    void getAllShouldReturnAllProductsFromRepository() throws DatabaseException, ServiceException {
        // Arrange
        List<Product> products = Arrays.asList(
                testProduct,
                Product.builder().name("Product 2").price(BigDecimal.ONE).cost(BigDecimal.ONE).build()
        );
        when(mockRepository.findAll()).thenReturn(products);

        // Act
        List<Product> result = service.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(mockRepository).findAll();
    }

    @Test
    @DisplayName("delete should delete product from repository")
    void deleteShouldDeleteProductFromRepository() throws DatabaseException, ServiceException {
        // Arrange
        when(mockRepository.delete(testId)).thenReturn(true);

        // Act
        boolean result = service.delete(testId);

        // Assert
        assertTrue(result);
        verify(mockRepository).delete(testId);
    }

    @Test
    @DisplayName("delete should return false when product doesn't exist")
    void deleteShouldReturnFalseWhenProductDoesntExist() throws DatabaseException, ServiceException {
        // Arrange
        when(mockRepository.delete(testId)).thenReturn(false);

        // Act
        boolean result = service.delete(testId);

        // Assert
        assertFalse(result);
        verify(mockRepository).delete(testId);
    }

    @Test
    @DisplayName("search should return products matching search term")
    void searchShouldReturnProductsMatchingSearchTerm() throws DatabaseException, ServiceException {
        // Arrange
        String searchTerm = "test";
        List<Product> products = Arrays.asList(
                testProduct,
                Product.builder().name("Test Product 2").price(BigDecimal.ONE).cost(BigDecimal.ONE).build()
        );
        when(mockRepository.search(searchTerm)).thenReturn(products);

        // Act
        List<Product> result = service.search(searchTerm);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(mockRepository).search(searchTerm);
    }

    @Test
    @DisplayName("getProductsByCategory should return products with given category")
    void getProductsByCategoryShouldReturnProductsWithGivenCategory() throws DatabaseException, ServiceException {
        // Arrange
        UUID categoryId = UUID.randomUUID();

        // Products in the category
        Product product1 = Product.builder()
                .name("In Category 1")
                .price(BigDecimal.ONE)
                .cost(BigDecimal.ONE)
                .categoryId(categoryId)
                .build();

        Product product2 = Product.builder()
                .name("In Category 2")
                .price(BigDecimal.ONE)
                .cost(BigDecimal.ONE)
                .categoryId(categoryId)
                .build();

        // Product not in the category
        Product product3 = Product.builder()
                .name("Not In Category")
                .price(BigDecimal.ONE)
                .cost(BigDecimal.ONE)
                .categoryId(UUID.randomUUID())
                .build();

        List<Product> allProducts = Arrays.asList(product1, product2, product3);

        when(mockRepository.findAll()).thenReturn(allProducts);

        // Act
        List<Product> result = service.getProductsByCategory(categoryId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(p -> categoryId.equals(p.categoryId())));
        verify(mockRepository).findAll();
    }

    @Test
    @DisplayName("updateStock should update product stock quantity")
    void updateStockShouldUpdateProductStockQuantity() throws DatabaseException, ServiceException {
        // Arrange
        int initialStock = 10;
        int change = 5;
        int expectedStock = 15;

        Product originalProduct = Product.builder()
                .id(testId)
                .name("Test Product")
                .price(BigDecimal.ONE)
                .cost(BigDecimal.ONE)
                .stockQuantity(initialStock)
                .build();

        Product expectedProduct = Product.builder(originalProduct)
                .stockQuantity(expectedStock)
                .build();

        when(mockRepository.findById(testId)).thenReturn(originalProduct);
        when(mockRepository.save(any(Product.class))).thenReturn(expectedProduct);

        // Act
        Product result = service.updateStock(testId, change);

        // Assert
        assertNotNull(result);
        assertEquals(expectedStock, result.stockQuantity());
        verify(mockRepository).findById(testId);

        // Capture and verify the saved product
        verify(mockRepository).save(argThat(product ->
                product.id().equals(testId) &&
                        product.stockQuantity() == expectedStock
        ));
    }

    @Test
    @DisplayName("updateStock should throw ServiceException when product not found")
    void updateStockShouldThrowServiceExceptionWhenProductNotFound() throws DatabaseException {
        // Arrange
        when(mockRepository.findById(testId)).thenReturn(null);

        // Act & Assert
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            service.updateStock(testId, 5);
        });
        assertTrue(exception.getMessage().contains("Product not found"));
        verify(mockRepository).findById(testId);
        verify(mockRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("updateStock should throw ServiceException when stock would go negative")
    void updateStockShouldThrowServiceExceptionWhenStockWouldGoNegative() throws DatabaseException {
        // Arrange
        Product product = Product.builder()
                .id(testId)
                .name("Test Product")
                .price(BigDecimal.ONE)
                .cost(BigDecimal.ONE)
                .stockQuantity(10)
                .build();

        when(mockRepository.findById(testId)).thenReturn(product);

        // Act & Assert
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            service.updateStock(testId, -15); // Would make stock -5
        });
        assertTrue(exception.getMessage().contains("Cannot reduce stock below zero"));
        verify(mockRepository).findById(testId);
        verify(mockRepository, never()).save(any(Product.class));
    }

    @Test
    @DisplayName("getLowStockProducts should return products below minimum stock level")
    void getLowStockProductsShouldReturnProductsBelowMinimumStockLevel() throws DatabaseException, ServiceException {
        // Arrange
        Product lowStock1 = Product.builder()
                .name("Low Stock 1")
                .price(BigDecimal.ONE)
                .cost(BigDecimal.ONE)
                .stockQuantity(2)
                .minStockLevel(5)
                .build();

        Product lowStock2 = Product.builder()
                .name("Low Stock 2")
                .price(BigDecimal.ONE)
                .cost(BigDecimal.ONE)
                .stockQuantity(3)
                .minStockLevel(10)
                .build();

        Product okStock = Product.builder()
                .name("OK Stock")
                .price(BigDecimal.ONE)
                .cost(BigDecimal.ONE)
                .stockQuantity(20)
                .minStockLevel(10)
                .build();

        List<Product> allProducts = Arrays.asList(lowStock1, lowStock2, okStock);

        when(mockRepository.findAll()).thenReturn(allProducts);

        // Act
        List<Product> result = service.getLowStockProducts();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(p -> p.stockQuantity() < p.minStockLevel()));
        verify(mockRepository).findAll();
    }
}