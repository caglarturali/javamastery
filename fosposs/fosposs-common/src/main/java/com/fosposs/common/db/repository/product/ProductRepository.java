package com.fosposs.common.db.repository.product;

import com.fosposs.common.db.connection.ConnectionManager;
import com.fosposs.common.db.exception.DatabaseException;
import com.fosposs.common.db.repository.BaseRepository;
import com.fosposs.common.model.Product;

import java.sql.*;
import java.util.UUID;

public class ProductRepository extends BaseRepository<Product, UUID> {

    @SuppressWarnings("unused")
    public static String getCreateTableQuery() {
        return """
                    CREATE TABLE IF NOT EXISTS products (
                        id TEXT PRIMARY KEY,
                        name TEXT NOT NULL,
                        description TEXT,
                        barcode TEXT,
                        price DECIMAL(10,2) NOT NULL,
                        cost DECIMAL(10,2) NOT NULL,
                        category_id TEXT,
                        stock_quantity INTEGER NOT NULL,
                        min_stock_level INTEGER NOT NULL,
                        active BOOLEAN NOT NULL,
                        created_at TEXT NOT NULL,
                        updated_at TEXT NOT NULL
                        FOREIGN KEY (category_id) REFERENCES categories(id)
                    )
                """;
    }

    @Override
    public Product save(Product product) throws DatabaseException {
        String sql = """
                    INSERT OR REPLACE INTO products (
                        id, name, description, barcode, price, cost, category_id,
                        stock_quantity, min_stock_level, active, created_at, updated_at
                    ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, product.id().toString());
            stmt.setString(2, product.name());
            stmt.setString(3, product.description());
            stmt.setString(4, product.barcode());
            stmt.setBigDecimal(5, product.price());
            stmt.setBigDecimal(6, product.cost());
            if (product.categoryId() != null) {
                stmt.setString(7, product.categoryId().toString());
            } else {
                stmt.setNull(7, Types.VARCHAR);
            }
            stmt.setInt(8, product.stockQuantity());
            stmt.setInt(9, product.minStockLevel());
            stmt.setBoolean(10, product.active());
            stmt.setString(11, product.createdAt().toString());
            stmt.setString(12, product.updatedAt().toString());

            stmt.executeUpdate();
            return product;
        } catch (SQLException e) {
            throw new DatabaseException("Failed to save product", e);
        }
    }

    @Override
    public Product findById(UUID id) throws DatabaseException {
        String sql = "SELECT * FROM products WHERE id = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id.toString());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToProduct(rs);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to find product with id: " + id, e);
        }
    }

    @Override
    public void delete(UUID id) throws DatabaseException {
        String sql = "DELETE FROM products WHERE id = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id.toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to delete product with id: " + id, e);
        }
    }

    @Override
    public boolean exists(UUID id) throws DatabaseException {
        String sql = "SELECT 1 FROM products WHERE id = ?";

        try (Connection conn = ConnectionManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, id.toString());

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to check existence of product with id: " + id, e);
        }
    }

    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        UUID categoryId = rs.getString("category_id") != null
                ? UUID.fromString(rs.getString("category_id"))
                : null;

        return Product.builder()
                .id(UUID.fromString(rs.getString("id")))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .barcode(rs.getString("barcode"))
                .price(rs.getBigDecimal("price"))
                .cost(rs.getBigDecimal("cost"))
                .categoryId(categoryId)
                .stockQuantity(rs.getInt("stock_quantity"))
                .minStockLevel(rs.getInt("min_stock_level"))
                .active(rs.getBoolean("active"))
                .build();
    }
}
