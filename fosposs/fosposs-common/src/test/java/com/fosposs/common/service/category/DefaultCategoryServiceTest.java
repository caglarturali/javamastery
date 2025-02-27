package com.fosposs.common.service.category;

import com.fosposs.common.db.exception.DatabaseException;
import com.fosposs.common.db.repository.category.CategoryRepository;
import com.fosposs.common.model.Category;
import com.fosposs.common.service.exception.ServiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("DefaultCategoryService Tests")
class DefaultCategoryServiceTest {

    private CategoryRepository mockRepository;
    private DefaultCategoryService service;
    private Category testCategory;
    private UUID testId;

    @BeforeEach
    void setUp() {
        mockRepository = mock(CategoryRepository.class);
        service = new DefaultCategoryService(mockRepository);
        testId = UUID.randomUUID();
        testCategory = Category.builder()
                .id(testId)
                .name("Test Category")
                .description("Test Description")
                .build();
    }

    @Test
    @DisplayName("create should save the category")
    void createShouldSaveCategory() throws DatabaseException, ServiceException {
        // Arrange
        when(mockRepository.save(testCategory)).thenReturn(testCategory);

        // Act
        Category result = service.create(testCategory);

        // Assert
        assertNotNull(result);
        assertEquals(testCategory, result);
        verify(mockRepository).save(testCategory);
    }

    @Test
    @DisplayName("create should throw ServiceException when save fails")
    void createShouldThrowServiceExceptionWhenSaveFails() throws DatabaseException {
        // Arrange
        when(mockRepository.save(testCategory))
                .thenThrow(new DatabaseException("Test Database Exception"));

        // Act & Assert
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            service.create(testCategory);
        });
        assertTrue(exception.getMessage().contains("Failed to create category"));
        assertInstanceOf(DatabaseException.class, exception.getCause());
    }

    @Test
    @DisplayName("update should verify existence then save category")
    void updateShouldVerifyExistenceThenSaveCategory() throws DatabaseException, ServiceException {
        // Arrange
        when(mockRepository.exists(testId)).thenReturn(true);
        when(mockRepository.save(testCategory)).thenReturn(testCategory);

        // Act
        Category result = service.update(testCategory);

        // Assert
        assertNotNull(result);
        assertEquals(testCategory, result);
        verify(mockRepository).exists(testId);
        verify(mockRepository).save(testCategory);
    }

    @Test
    @DisplayName("update should throw ServiceException when category doesn't exist")
    void updateShouldThrowServiceExceptionWhenCategoryDoesntExist() throws DatabaseException {
        // Arrange
        when(mockRepository.exists(testId)).thenReturn(false);

        // Act & Assert
        ServiceException exception = assertThrows(ServiceException.class, () -> {
            service.update(testCategory);
        });
        assertTrue(exception.getMessage().contains("Category not found"));
        verify(mockRepository).exists(testId);
        verify(mockRepository, never()).save(any(Category.class));
    }

    @Test
    @DisplayName("getById should return category from repository")
    void getByIdShouldReturnCategoryFromRepository() throws DatabaseException, ServiceException {
        // Arrange
        when(mockRepository.findById(testId)).thenReturn(testCategory);

        // Act
        Category result = service.getById(testId);

        // Assert
        assertNotNull(result);
        assertEquals(testCategory, result);
        verify(mockRepository).findById(testId);
    }

    @Test
    @DisplayName("getById should return null when category doesn't exist")
    void getByIdShouldReturnNullWhenCategoryDoesntExist() throws DatabaseException, ServiceException {
        // Arrange
        when(mockRepository.findById(testId)).thenReturn(null);

        // Act
        Category result = service.getById(testId);

        // Assert
        assertNull(result);
        verify(mockRepository).findById(testId);
    }

    @Test
    @DisplayName("getAll should return all categories from repository")
    void getAllShouldReturnAllCategoriesFromRepository() throws DatabaseException, ServiceException {
        // Arrange
        List<Category> categories = Arrays.asList(
                testCategory,
                Category.builder().name("Category 2").build()
        );
        when(mockRepository.findAll()).thenReturn(categories);

        // Act
        List<Category> result = service.getAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(mockRepository).findAll();
    }

    @Test
    @DisplayName("delete should delete category from repository")
    void deleteShouldDeleteCategoryFromRepository() throws DatabaseException, ServiceException {
        // Arrange
        when(mockRepository.delete(testId)).thenReturn(true);

        // Act
        boolean result = service.delete(testId);

        // Assert
        assertTrue(result);
        verify(mockRepository).delete(testId);
    }

    @Test
    @DisplayName("delete should return false when category doesn't exist")
    void deleteShouldReturnFalseWhenCategoryDoesntExist() throws DatabaseException, ServiceException {
        // Arrange
        when(mockRepository.delete(testId)).thenReturn(false);

        // Act
        boolean result = service.delete(testId);

        // Assert
        assertFalse(result);
        verify(mockRepository).delete(testId);
    }

    @Test
    @DisplayName("search should return categories matching search term")
    void searchShouldReturnCategoriesMatchingSearchTerm() throws DatabaseException, ServiceException {
        // Arrange
        String searchTerm = "test";
        List<Category> categories = Arrays.asList(
                testCategory,
                Category.builder().name("Test Category 2").build()
        );
        when(mockRepository.search(searchTerm)).thenReturn(categories);

        // Act
        List<Category> result = service.search(searchTerm);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(mockRepository).search(searchTerm);
    }

    @Test
    @DisplayName("getRootCategories should return root categories from repository")
    void getRootCategoriesShouldReturnRootCategoriesFromRepository() throws DatabaseException, ServiceException {
        // Arrange
        List<Category> rootCategories = Arrays.asList(
                testCategory,
                Category.builder().name("Root Category 2").build()
        );
        when(mockRepository.findRootCategories()).thenReturn(rootCategories);

        // Act
        List<Category> result = service.getRootCategories();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(mockRepository).findRootCategories();
    }

    @Test
    @DisplayName("getChildCategories should return child categories from repository")
    void getChildCategoriesShouldReturnChildCategoriesFromRepository() throws DatabaseException, ServiceException {
        // Arrange
        UUID parentId = UUID.randomUUID();
        List<Category> childCategories = Arrays.asList(
                Category.builder().name("Child 1").parentId(parentId).build(),
                Category.builder().name("Child 2").parentId(parentId).build()
        );
        when(mockRepository.findChildCategories(parentId)).thenReturn(childCategories);

        // Act
        List<Category> result = service.getChildCategories(parentId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(mockRepository).findChildCategories(parentId);
    }

    @Test
    @DisplayName("getCategoryPath should return category path from repository")
    void getCategoryPathShouldReturnCategoryPathFromRepository() throws DatabaseException, ServiceException {
        // Arrange
        List<String> path = Arrays.asList("Parent", "Child", "Grandchild");
        when(mockRepository.getCategoryPath(testId)).thenReturn(path);

        // Act
        List<String> result = service.getCategoryPath(testId);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("Parent", result.get(0));
        assertEquals("Child", result.get(1));
        assertEquals("Grandchild", result.get(2));
        verify(mockRepository).getCategoryPath(testId);
    }

    @Test
    @DisplayName("hasChildren should return true when category has children")
    void hasChildrenShouldReturnTrueWhenCategoryHasChildren() throws DatabaseException, ServiceException {
        // Arrange
        when(mockRepository.hasChildren(testId)).thenReturn(true);

        // Act
        boolean result = service.hasChildren(testId);

        // Assert
        assertTrue(result);
        verify(mockRepository).hasChildren(testId);
    }

    @Test
    @DisplayName("hasProducts should return true when category has products")
    void hasProductsShouldReturnTrueWhenCategoryHasProducts() throws DatabaseException, ServiceException {
        // Arrange
        when(mockRepository.hasProducts(testId)).thenReturn(true);

        // Act
        boolean result = service.hasProducts(testId);

        // Assert
        assertTrue(result);
        verify(mockRepository).hasProducts(testId);
    }
}