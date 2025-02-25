---
title: "Day #15: Category Chronicles: Hierarchical Mastery with Foreign Keys 🌳"
date: 2025-02-25
description: "Implementing hierarchical category management with proper database relationships in our Fosposs project"
tags: ["month-1", "week-3", "fosposs", "database-design", "hierarchy"]
showToc: true
TocOpen: false
---

Today we enhanced our Fosposs (Free and Open Source Point of Sale Software) project by implementing a complete hierarchical category system and integrating it with our existing product management.

> The code changes from this session can be found [here](https://github.com/caglarturali/javamastery/tree/765d09d5b292f5baac75478be464237914a114f5).

# Day 15 Summary

## Overview
Implemented a robust category management system with support for hierarchical structures while upgrading the product model to use proper foreign key relationships.

## Environment & Tools
- Maven multi-module project structure
- JUnit 5 testing framework
- SQLite with HikariCP connection pooling
- Git workflow with proper commit practices

## Concepts Covered

### Hierarchical Data Modeling
- Parent-child relationships in database design
- Recursive foreign key relationships
- Tree traversal patterns
- Path generation for navigation breadcrumbs

### Domain Modeling
- Record types for immutable data (Category)
- Builder pattern implementation
- Field validation in compact constructors
- Foreign key relationships between entities

### Database Design
- Proper schema design with foreign key constraints
- Recursive relationships for hierarchical data
- Clean SQL query patterns
- Handling of NULL references in SQLite

### Repository Pattern Extensions
- Specialized repository methods for hierarchical data
- Data integrity protection
- SQL JOIN operations
- Complex query patterns

### Testing Practices
- Category entity testing
- Repository integration testing with in-memory database
- Verifying hierarchical data structure
- Testing foreign key constraint enforcement

## Implementation Progress
- Created Category domain model with builder pattern
- Implemented CategoryRepository with hierarchy support
- Integrated categories with products using foreign keys
- Refactored Product to use proper category references
- Added comprehensive test coverage

## Key Takeaways
- Importance of proper hierarchical data modeling
- Benefits of using UUID references over string identifiers
- Value of proper foreign key constraints
- Importance of data integrity protection
- Clean separation of domain and data access concerns

## Next Steps
Ready to explore:
- User interface for category management
- Hierarchical category display components
- Search and filtering by category
- Product management with category selection
- Reporting by category

## Master's Reflection
The integration of a proper hierarchical category system demonstrated an excellent understanding of database relationships and domain modeling. The thoughtful approach to data integrity, particularly preventing deletion of categories with dependencies, shows growing maturity in system design. The decision to return an ordered list of category names rather than a concatenated string reveals an understanding of flexible API design and separation of presentation from data management.