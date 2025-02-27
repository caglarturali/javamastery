package com.fosposs.common.service.product;

import com.fosposs.common.model.Product;
import com.fosposs.common.service.Service;
import com.fosposs.common.service.exception.ServiceException;

import java.util.List;
import java.util.UUID;

/**
 * Service for managing product operations
 */
public interface ProductService extends Service<Product, UUID> {

    List<Product> getProductsByCategory(UUID categoryId) throws ServiceException;

    Product updateStock(UUID id, int quantityChange) throws ServiceException;

    List<Product> getLowStockProducts() throws ServiceException;
}
