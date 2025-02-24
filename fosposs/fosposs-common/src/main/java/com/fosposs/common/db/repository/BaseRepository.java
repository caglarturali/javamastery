package com.fosposs.common.db.repository;

public abstract class BaseRepository<T, ID> implements Repository<T, ID> {
    public static String getCreateTableQuery() {
        return null;
    }
}
