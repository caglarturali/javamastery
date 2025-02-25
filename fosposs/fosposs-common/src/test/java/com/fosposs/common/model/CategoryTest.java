package com.fosposs.common.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Category operations")
class CategoryTest {

    @Test
    @DisplayName("Builder should create a valid category with minimal required fields")
    void builderCreatesValidCategoryWithMinimalFields() {
        Category category = Category.builder()
                .name("Test Category")
                .build();

        assertNotNull(category);
        assertNotNull(category.id());
        assertEquals("Test Category", category.name());
        assertEquals("", category.description());
        assertEquals(0, category.sortOrder());
        assertTrue(category.active());
        assertTrue(category.isRoot());
        assertNull(category.parentId());
    }

    @Test
    @DisplayName("Builder should create a category with all fields set")
    void builderCreatesCategoryWithAllFieldsSet() {
        UUID id = UUID.randomUUID();
        UUID parentId = UUID.randomUUID();

        Category category = Category.builder()
                .id(id)
                .name("Electronics")
                .description("Electronic devices and accessories")
                .parentId(parentId)
                .sortOrder(5)
                .active(true)
                .build();

        assertAll(
                () -> assertEquals(id, category.id()),
                () -> assertEquals("Electronics", category.name()),
                () -> assertEquals("Electronic devices and accessories", category.description()),
                () -> assertEquals(parentId, category.parentId()),
                () -> assertEquals(5, category.sortOrder()),
                () -> assertTrue(category.active()),
                () -> assertFalse(category.isRoot())
        );
    }

    @Test
    @DisplayName("Builder should support method chaining in any order")
    void builderSupportsMethodChainingInAnyOrder() {
        Category category = Category.builder()
                .sortOrder(3)
                .name("Beverages")
                .parentId(UUID.randomUUID())
                .description("Drinks and liquid refreshments")
                .build();

        assertAll(
                () -> assertEquals("Beverages", category.name()),
                () -> assertEquals(3, category.sortOrder()),
                () -> assertNotNull(category.parentId()),
                () -> assertEquals("Drinks and liquid refreshments", category.description())
        );
    }

    @Test
    @DisplayName("Category creation should fail with invalid data")
    void categoryCreationFailsWithInvalidData() {
        // Empty name
        assertThrows(IllegalArgumentException.class, () -> Category.builder().name("").build());

        // Null name
        assertThrows(IllegalArgumentException.class, () -> Category.builder().name(null).build());

        // Negative sort order
        assertThrows(IllegalArgumentException.class, () ->
                Category.builder()
                        .name("Test")
                        .sortOrder(-1)
                        .build()
        );
    }

    @Test
    @DisplayName("Categories should be immutable")
    void categoriesShouldBeImmutable() {
        Category original = Category.builder()
                .name("Original Category")
                .sortOrder(1)
                .build();

        Category modified = Category.builder()
                .id(original.id())
                .name(original.name())
                .sortOrder(2)
                .build();

        assertAll(
                () -> assertEquals(1, original.sortOrder()),
                () -> assertEquals(2, modified.sortOrder()),
                () -> assertNotSame(original, modified)
        );
    }

    @Test
    @DisplayName("isRoot should identify root categories correctly")
    void isRootShouldIdentifyRootCategoriesCorrectly() {
        Category rootCategory = Category.builder()
                .name("Root Category")
                .build();

        Category childCategory = Category.builder()
                .name("Child Category")
                .parentId(rootCategory.id())
                .build();

        assertTrue(rootCategory.isRoot());
        assertFalse(childCategory.isRoot());
    }

}