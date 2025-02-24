package com.fosposs.common.db.config;

import com.fosposs.common.db.connection.ConnectionManager;
import com.fosposs.common.db.repository.BaseRepository;

import java.lang.reflect.Method;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

public class SQLiteConfig implements DatabaseConfig {
    private final Path dbPath;

    public SQLiteConfig(Path dbPath) {
        this.dbPath = dbPath;
    }

    @Override
    public String getJdbcUrl() {
        return "jdbc:sqlite:" + dbPath;
    }

    @Override
    public void initialize(Collection<Class<? extends BaseRepository<?, ?>>> repositoryClasses) throws SQLException {
        try (Connection conn = ConnectionManager.getConnection()) {
            for (var repoClass : repositoryClasses) {
                try {
                    Method getCreateTableQuery = repoClass.getMethod("getCreateTableQuery");
                    String query = (String) getCreateTableQuery.invoke(null);
                    if (query != null) {
                        try (var stmt = conn.createStatement()) {
                            stmt.execute(query);
                        }
                    }
                } catch (ReflectiveOperationException e) {
                    throw new SQLException("Failed to initialize schema for " + repoClass.getSimpleName(), e);
                }
            }
        }
    }
}
