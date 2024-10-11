package com.github.teraprath.tinylib.sql;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class SQLTable {

    private final String name;
    private String primary;
    private ArrayList<SQLColumn> columns;

    public SQLTable(@Nonnull String name) {
        this.name = name;
        this.columns = new ArrayList<>();
    }

    public SQLTable addColumn(@Nonnull SQLColumn column) {
        this.columns.add(column);
        return this;
    }

    public SQLTable setPrimary(@Nonnull String columnName) {
        this.primary = columnName;
        return this;
    }

    public SQLColumn getColumn(@Nonnull String name) {
        for (SQLColumn column : this.columns) {
            if (column.getName().equals(name)) {
                return column;
            }
        }
        return null;
    }

    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("CREATE TABLE IF NOT EXISTS ").append(name).append(" (");
        this.columns.forEach(column -> {
            if (!(columns.get(0).equals(column))) {
                result.append(" ");
            }
            result.append(column.getName());
            result.append(" ").append(column.getType().name());
            if (!column.getParams().isEmpty()) {
                result.append(" (");
                column.getParams().forEach(result::append);
                result.append(")");
            }
            if (column.getDefaultValue() != null) {
                result.append(" DEFAULT ").append(column.getDefaultValue());
            }
            if (column.isNotNull()) {
                result.append(" NOT NULL");
            }
            if (column.isAuto()) {
                result.append(" AUTO_INCREMENT");
            }
            result.append(",");
        });
        if (this.primary != null) {
            result.append(" PRIMARY KEY (uuid)");
        } else {
            result.deleteCharAt(result.length() - 1);
        }
        result.append(")");
        return result.toString();
    }

    public String getName() {
        return this.name;
    }

    public String getPrimary() {
        return this.primary;
    }

    public ArrayList<SQLColumn> getColumns() {
        return this.columns;
    }
}