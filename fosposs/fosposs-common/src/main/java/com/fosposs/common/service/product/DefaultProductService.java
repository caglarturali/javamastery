package com.fosposs.common.service.product;

import com.fosposs.common.db.exception.DatabaseException;
import com.fosposs.common.db.repository.product.ProductRepository;
import com.fosposs.common.model.Product;
import com.fosposs.common.service.exception.ServiceException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DefaultProductService implements ProductService {
    private final ProductRepository productRepository;

    public DefaultProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public Product create(Product product) throws ServiceException {
        try {
            return productRepository.save(product);
        } catch (DatabaseException e) {
            throw new ServiceException("Failed to create product: " + e.getMessage(), e);
        }
    }

    @Override
    public Product update(Product product) throws ServiceException {
        try {
            // Verify the product exists
            if (!productRepository.exists(product.id())) {
                throw new ServiceException("Product not found with ID: " + product.id());
            }
            return productRepository.save(product);
        } catch (DatabaseException e) {
            throw new ServiceException("Failed to update product: " + e.getMessage(), e);
        }
    }

    @Override
    public Product getById(UUID id) throws ServiceException {
        try {
            return productRepository.findById(id);
        } catch (DatabaseException e) {
            throw new ServiceException("Failed to get product by ID: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Product> getAll() throws ServiceException {
        try {
            return productRepository.findAll();
        } catch (DatabaseException e) {
            throw new ServiceException("Failed to get all products: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(UUID id) throws ServiceException {
        try {
            return productRepository.delete(id);
        } catch (DatabaseException e) {
            throw new ServiceException("Failed to delete product: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Product> search(String searchTerm) throws ServiceException {
        try {
            return productRepository.search(searchTerm);
        } catch (DatabaseException e) {
            throw new ServiceException("Failed to search products: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Product> getProductsByCategory(UUID categoryId) throws ServiceException {
        try {
            // Filter all products by the given category ID
            return productRepository.findAll().stream()
                    .filter(product -> categoryId.equals(product.categoryId()))
                    .collect(Collectors.toList());
        } catch (DatabaseException e) {
            throw new ServiceException("Failed to get products by category: " + e.getMessage(), e);
        }
    }

    @Override
    public Product updateStock(UUID id, int quantityChange) throws ServiceException {
        try {
            // Get the current product
            Product product = productRepository.findById(id);
            if (product == null) {
                throw new ServiceException("Product not found with ID: " + id);
            }

            // Calculate new quantity
            int newQuantity = product.stockQuantity() + quantityChange;

            // Ensure stock doesn't go negative
            if (newQuantity < 0) {
                throw new ServiceException("Cannot reduce stock below zero. Current stock: " + product.stockQuantity());
            }

            // Create updated product
            Product updatedProduct = Product.builder(product)
                    .stockQuantity(newQuantity)
                    .build();

            // Save and return
            return productRepository.save(updatedProduct);
        } catch (DatabaseException e) {
            throw new ServiceException("Failed to update product stock: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Product> getLowStockProducts() throws ServiceException {
        try {
            // Get all products and filter those below minimum stock level
            return productRepository.findAll().stream()
                    .filter(product -> product.stockQuantity() < product.minStockLevel())
                    .collect(Collectors.toList());
        } catch (DatabaseException e) {
            throw new ServiceException("Failed to get low stock products: " + e.getMessage(), e);
        }
    }
}
