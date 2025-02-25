package com.fosposs.common.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Product operations")
class ProductTest {

    @Test
    @DisplayName("Builder should create a valid product with minimal required fields")
    void builderCreatesValidProductWithMinimalFields() {
        Product product = Product.builder()
                .name("Test Product")
                .build();

        assertNotNull(product);
        assertEquals("Test Product", product.name());
        assertEquals("", product.description());
        assertEquals(BigDecimal.ZERO, product.price());
        assertNull(product.categoryId());
        assertTrue(product.active());
        assertNotNull(product.id());
    }

    @Test
    @DisplayName("Builder should create a product with all fields set")
    void builderCreatesProductWithAllFields() {
        UUID id = UUID.randomUUID();
        UUID categoryId = UUID.randomUUID();

        Product product = Product.builder()
                .id(id)
                .name("Fancy Coffee")
                .description("Premium Arabic beans")
                .barcode("123456789")
                .price(new BigDecimal("4.99"))
                .cost(new BigDecimal("2.50"))
                .categoryId(categoryId)
                .stockQuantity(100)
                .minStockLevel(20)
                .active(true)
                .build();

        assertAll(
                () -> assertEquals(id, product.id()),
                () -> assertEquals("Fancy Coffee", product.name()),
                () -> assertEquals("Premium Arabic beans", product.description()),
                () -> assertEquals("123456789", product.barcode()),
                () -> assertEquals(new BigDecimal("4.99"), product.price()),
                () -> assertEquals(new BigDecimal("2.50"), product.cost()),
                () -> assertEquals(categoryId, product.categoryId()),
                () -> assertEquals(100, product.stockQuantity()),
                () -> assertEquals(20, product.minStockLevel()),
                () -> assertTrue(product.active())
        );
    }

    @Test
    @DisplayName("Builder should support method chaining in any order")
    void builderSupportsMethodChainingInAnyOrder() {
        UUID categoryId = UUID.randomUUID();
        Product product = Product.builder()
                .price(new BigDecimal("9.99"))
                .name("Chai Latte")
                .categoryId(categoryId)
                .description("Spiced tea with milk")
                .build();

        assertAll(
                () -> assertEquals("Chai Latte", product.name()),
                () -> assertEquals(new BigDecimal("9.99"), product.price()),
                () -> assertEquals(categoryId, product.categoryId()),
                () -> assertEquals("Spiced tea with milk", product.description())
        );
    }

    @Test
    @DisplayName("Product creation should fail with invalid data")
    void productCreationFailsWithInvalidData() {
        // Empty name
        assertThrows(IllegalArgumentException.class, () -> Product.builder().name("").build());

        // Null name
        assertThrows(IllegalArgumentException.class, () -> Product.builder().name(null).build());

        // Negative price
        assertThrows(IllegalArgumentException.class, () ->
                Product.builder()
                        .name("Test")
                        .price(new BigDecimal("-1.00"))
                        .build()
        );

        // Negative stock
        assertThrows(IllegalArgumentException.class, () ->
                Product.builder()
                        .name("Test")
                        .stockQuantity(-1)
                        .build()
        );
    }

    @Test
    @DisplayName("Products should be immutable")
    void productsShouldBeImmutable() {
        Product original = Product.builder()
                .name("Original Product")
                .price(new BigDecimal("10.00"))
                .build();

        Product modified = Product.builder()
                .id(original.id())
                .name(original.name())
                .price(new BigDecimal("15.00"))
                .build();

        assertAll(
                () -> assertEquals(new BigDecimal("10.00"), original.price()),
                () -> assertEquals(new BigDecimal("15.00"), modified.price()),
                () -> assertNotSame(original, modified)
        );
    }
}