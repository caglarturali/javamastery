package com.fosposs.common.db.repository.category;

import com.fosposs.common.db.connection.ConnectionManager;
import com.fosposs.common.db.exception.DatabaseException;
import com.fosposs.common.db.repository.BaseRepository;
import com.fosposs.common.model.Category;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class CategoryRepository extends BaseRepository<Category, UUID> {

    @SuppressWarnings("unused")
    public static String getCreateTableQuery() {
        return """
                CREATE TABLE IF NOT EXISTS categories (
                    id TEXT PRIMARY KEY,
                    name TEXT NOT NULL,
                    description TEXT,
                    parent_id TEXT,
                    sort_order INTEGER NOT NULL,
                    active BOOLEAN NOT NULL,
                    created_at TEXT NOT NULL,
                    updated_at TEXT NOT NULL,
                    FOREIGN KEY (parent_id) REFERENCES categories(id)
                )
                """;
    }

    @Override
    public Category save(Category category) throws DatabaseException {
        String sql = """
                    INSERT OR REPLACE INTO categories (
                        id, name, description, parent_id, sort_order,
                        active, created_at, updated_at
                    ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, category.id().toString());
            stmt.setString(2, category.name());
            stmt.setString(3, category.description());
            if (category.parentId() != null) {
                stmt.setString(4, category.parentId().toString());
            } else {
                stmt.setNull(4, Types.VARCHAR);
            }
            stmt.setInt(5, category.sortOrder());
            stmt.setBoolean(6, category.active());
            stmt.setString(7, category.createdAt().toString());
            stmt.setString(8, category.updatedAt().toString());

            stmt.executeUpdate();
            return category;

        } catch (SQLException e) {
            throw new DatabaseException("Failed to save category", e);
        }
    }

    @Override
    public Category findById(UUID id) throws DatabaseException {
        String sql = "SELECT * FROM categories WHERE id = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id.toString());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCategory(rs);
                }
                return null;
            }

        } catch (SQLException e) {
            throw new DatabaseException("Failed to find category with id: " + id, e);
        }
    }

    @Override
    public List<Category> findAll() throws DatabaseException {
        String sql = "SELECT * FROM categories ORDER BY name, sort_order";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            List<Category> categories = new ArrayList<>();
            while (rs.next()) {
                categories.add(mapResultSetToCategory(rs));
            }
            return categories;

        } catch (SQLException e) {
            throw new DatabaseException("Failed to fetch all categories");
        }
    }

    @Override
    public boolean delete(UUID id) throws DatabaseException {
        if (hasChildren(id)) {
            throw new DatabaseException("Cannot delete category with child categories");
        }

        if (hasProducts(id)) {
            throw new DatabaseException("Cannot delete category that has products");
        }

        String sql = "DELETE FROM categories WHERE id = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id.toString());
            return stmt.executeUpdate() != 0;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to delete category with id: " + id, e);
        }
    }

    @Override
    public boolean exists(UUID id) throws DatabaseException {
        String sql = "SELECT 1 FROM categories WHERE id = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id.toString());

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to check existence of category with id: " + id, e);
        }
    }

    @Override
    public List<Category> search(String searchTerm) throws DatabaseException {
        String sql = "SELECT * FROM categories WHERE name LIKE ? or description LIKE ? ORDER BY NAME, sort_order";
        String term = "%" + searchTerm + "%";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, term);
            stmt.setString(2, term);

            try (ResultSet rs = stmt.executeQuery()) {
                List<Category> categories = new ArrayList<>();
                while (rs.next()) {
                    categories.add(mapResultSetToCategory(rs));
                }
                return categories;
            }

        } catch (SQLException e) {
            throw new DatabaseException("Failed to search categories by term: " + searchTerm, e);
        }
    }

    @Override
    public void clear() throws DatabaseException {
        String sql = "DELETE FROM categories";

        try (Connection conn = ConnectionManager.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            throw new DatabaseException("Failed to clear categories table", e);
        }
    }

    /**
     * Checks if a category has any child categories
     */
    public boolean hasChildren(UUID id) throws DatabaseException {
        String sql = "SELECT 1 FROM categories WHERE parent_id = ? LIMIT 1";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id.toString());

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new DatabaseException("Failed to check if category has children: " + id, e);
        }
    }

    /**
     * Checks if any products are using this category
     */
    public boolean hasProducts(UUID id) throws DatabaseException {
        String sql = "SELECT 1 FROM products WHERE category_id = ? LIMIT 1";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id.toString());

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            throw new DatabaseException("Failed to check if category has products: " + id, e);
        }
    }

    /**
     * Finds all root categories (categories without a parent)
     */
    public List<Category> findRootCategories() throws DatabaseException {
        String sql = "SELECT * FROM categories WHERE parent_id IS NULL ORDER BY sort_order";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            List<Category> categories = new ArrayList<>();
            while (rs.next()) {
                categories.add(mapResultSetToCategory(rs));
            }
            return categories;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to find root categories", e);
        }
    }

    /**
     * Finds all child categories for a given parent category
     */
    public List<Category> findChildCategories(UUID parentId) throws DatabaseException {
        String sql = "SELECT * FROM categories WHERE parent_id = ? ORDER BY sort_order";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, parentId.toString());

            try (ResultSet rs = stmt.executeQuery()) {
                List<Category> categories = new ArrayList<>();
                while (rs.next()) {
                    categories.add(mapResultSetToCategory(rs));
                }
                return categories;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to find child categories for parent: " + parentId, e);
        }
    }

    /**
     * Gets the full category path as a list of category names
     */
    public List<String> getCategoryPath(UUID id) throws DatabaseException {
        Category category = findById(id);
        if (category == null) {
            return List.of();
        }

        if (category.isRoot()) {
            return List.of(category.name());
        }

        var path = new LinkedList<String>();
        path.add(category.name());
        UUID currParentId = category.parentId();

        while (currParentId != null) {
            Category parent = findById(currParentId);
            if (parent == null) {
                break;
            }
            path.addFirst(parent.name());
            currParentId = parent.parentId();
        }

        return path;
    }

    private Category mapResultSetToCategory(ResultSet rs) throws SQLException {
        UUID parentId = rs.getString("parent_id") != null
                ? UUID.fromString(rs.getString("parent_id"))
                : null;

        return Category.builder()
                .id(UUID.fromString(rs.getString("id")))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .parentId(parentId)
                .sortOrder(rs.getInt("sort_order"))
                .active(rs.getBoolean("active"))
                .build();
    }
}
