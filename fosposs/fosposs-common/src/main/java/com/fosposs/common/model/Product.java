package com.fosposs.common.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record Product(
        UUID id,
        String name,
        String description,
        String barcode,
        BigDecimal price,
        BigDecimal cost,
        UUID categoryId,
        int stockQuantity,
        int minStockLevel,
        boolean active,
        Instant createdAt,
        Instant updatedAt
) {
    public Product {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        if (cost == null || cost.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Cost cannot be negative");
        }
        if (stockQuantity < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be negative");
        }
        if (minStockLevel < 0) {
            throw new IllegalArgumentException("Minimum stock level cannot be negative");
        }

        if (createdAt == null) {
            createdAt = Instant.now();
        }
        if (updatedAt == null) {
            updatedAt = createdAt;
        }
    }

    public static ProductBuilder builder() {
        return new ProductBuilder();
    }

    public static class ProductBuilder {
        private UUID id = UUID.randomUUID();
        private String name;
        private String description = "";
        private String barcode;
        private BigDecimal price = BigDecimal.ZERO;
        private BigDecimal cost = BigDecimal.ZERO;
        private UUID categoryId;
        private int stockQuantity = 0;
        private int minStockLevel = 0;
        private boolean active = true;
        private final Instant createdAt = Instant.now();
        private final Instant updatedAt = createdAt;

        public ProductBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public ProductBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ProductBuilder description(String description) {
            this.description = description;
            return this;
        }

        public ProductBuilder barcode(String barcode) {
            this.barcode = barcode;
            return this;
        }

        public ProductBuilder price(BigDecimal price) {
            this.price = price;
            return this;
        }

        public ProductBuilder cost(BigDecimal cost) {
            this.cost = cost;
            return this;
        }

        public ProductBuilder categoryId(UUID categoryId) {
            this.categoryId = categoryId;
            return this;
        }

        public ProductBuilder stockQuantity(int stockQuantity) {
            this.stockQuantity = stockQuantity;
            return this;
        }

        public ProductBuilder minStockLevel(int minStockLevel) {
            this.minStockLevel = minStockLevel;
            return this;
        }

        public ProductBuilder active(boolean active) {
            this.active = active;
            return this;
        }

        public Product build() {
            return new Product(
                    id, name, description, barcode, price, cost,
                    categoryId, stockQuantity, minStockLevel, active,
                    createdAt, updatedAt
            );
        }
    }
}
