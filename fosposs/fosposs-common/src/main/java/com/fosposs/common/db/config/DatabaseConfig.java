package com.fosposs.common.db.config;

import com.fosposs.common.db.repository.BaseRepository;

import java.sql.SQLException;
import java.util.Collection;

public interface DatabaseConfig {
    String getJdbcUrl();

    void initialize(Collection<Class<? extends BaseRepository<?, ?>>> repositoryClasses) throws SQLException;
}
