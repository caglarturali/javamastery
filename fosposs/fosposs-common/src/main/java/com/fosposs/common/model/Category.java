package com.fosposs.common.model;

import java.time.Instant;
import java.util.UUID;

public record Category(
        UUID id,
        String name,
        String description,
        UUID parentId,
        int sortOrder,
        boolean active,
        Instant createdAt,
        Instant updatedAt
) {
    public Category {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Category name cannot be empty");
        }
        if (description == null) {
            description = "";
        }
        if (sortOrder < 0) {
            throw new IllegalArgumentException("Sort order cannot be negative");
        }
        if (createdAt == null) {
            createdAt = Instant.now();
        }
        if (updatedAt == null) {
            updatedAt = createdAt;
        }
    }

    /**
     * Checks if this category is a root category (has no parent)
     */
    public boolean isRoot() {
        return parentId == null;
    }

    public static CategoryBuilder builder() {
        return new CategoryBuilder();
    }

    public static CategoryBuilder builder(Category category) {
        return new CategoryBuilder(category);
    }

    public static class CategoryBuilder {
        private UUID id = UUID.randomUUID();
        private String name;
        private String description = "";
        private UUID parentId;
        private int sortOrder = 0;
        private boolean active = true;
        private final Instant createdAt = Instant.now();
        private final Instant updatedAt = createdAt;

        public CategoryBuilder() {
        }

        public CategoryBuilder(Category category) {
            this.id = category.id();
            this.name = category.name();
            this.description = category.description();
            this.parentId = category.parentId();
            this.sortOrder = category.sortOrder();
            this.active = category.active();
        }

        public CategoryBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public CategoryBuilder name(String name) {
            this.name = name;
            return this;
        }

        public CategoryBuilder description(String description) {
            this.description = description;
            return this;
        }

        public CategoryBuilder parentId(UUID parentId) {
            this.parentId = parentId;
            return this;
        }

        public CategoryBuilder sortOrder(int sortOrder) {
            this.sortOrder = sortOrder;
            return this;
        }

        public CategoryBuilder active(boolean active) {
            this.active = active;
            return this;
        }

        public Category build() {
            return new Category(
                    id, name, description, parentId, sortOrder,
                    active, createdAt, updatedAt
            );
        }
    }
}
