package com.github.teraprath.tinylib.sql;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class SQLTable {

    private final String name; // Name of the SQL table
    private String primary; // Primary key column name
    private ArrayList<SQLColumn> columns; // List of columns in the table

    /**
     * Constructor for SQLTable, initializing with a table name.
     *
     * @param name The name of the SQL table.
     */
    public SQLTable(@Nonnull String name) {
        this.name = name;
        this.columns = new ArrayList<>();
    }

    /**
     * Adds a new column to the table definition.
     *
     * @param column The SQLColumn object representing a column.
     * @return The current SQLTable instance, allowing method chaining.
     */
    public SQLTable addColumn(@Nonnull SQLColumn column) {
        this.columns.add(column);
        return this;
    }

    /**
     * Sets the primary key column for the table.
     *
     * @param columnName The name of the column that will be the primary key.
     * @return The current SQLTable instance, allowing method chaining.
     */
    public SQLTable setPrimary(@Nonnull String columnName) {
        this.primary = columnName;
        return this;
    }

    /**
     * Retrieves a column by its name.
     *
     * @param name The name of the column to retrieve.
     * @return The SQLColumn object if found, otherwise null.
     */
    public SQLColumn getColumn(@Nonnull String name) {
        for (SQLColumn column : this.columns) {
            if (column.getName().equals(name)) {
                return column; // Return the column if found
            }
        }
        return null; // Return null if the column is not found
    }

    /**
     * Converts the SQLTable object into a SQL table creation string.
     * Generates the SQL syntax to create a table with the defined columns and primary key.
     *
     * @return A string representing the SQL CREATE TABLE statement.
     */
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("CREATE TABLE IF NOT EXISTS ").append(name).append(" (");

        // Iterate through the columns to append their definitions
        this.columns.forEach(column -> {
            if (!(columns.get(0).equals(column))) {
                result.append(" ");
            }
            result.append(column.getName()); // Column name
            result.append(" ").append(column.getType().name()); // Column data type

            // If the column has additional parameters (e.g., length), append them
            if (!column.getParams().isEmpty()) {
                result.append(" (");
                column.getParams().forEach(result::append);
                result.append(")");
            }

            // If the column has a default value, append it
            if (column.getDefaultValue() != null) {
                result.append(" DEFAULT ").append(column.getDefaultValue());
            }

            // If the column cannot be null, append NOT NULL
            if (column.isNotNull()) {
                result.append(" NOT NULL");
            }

            // If the column is auto-incrementing, append AUTO_INCREMENT
            if (column.isAuto()) {
                result.append(" AUTO_INCREMENT");
            }

            result.append(","); // Append a comma to separate columns
        });

        // Append the primary key definition, if it exists
        if (this.primary != null) {
            result.append(" PRIMARY KEY (").append(this.primary).append(")");
        } else {
            result.deleteCharAt(result.length() - 1); // Remove the last comma if no primary key is defined
        }

        result.append(")");
        return result.toString(); // Return the complete SQL statement
    }

    /**
     * Returns the name of the SQL table.
     *
     * @return The name of the table.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns the primary key column name.
     *
     * @return The name of the primary key column, or null if none is set.
     */
    public String getPrimary() {
        return this.primary;
    }

    /**
     * Returns the list of columns in the SQL table.
     *
     * @return A list of SQLColumn objects representing the columns of the table.
     */
    public ArrayList<SQLColumn> getColumns() {
        return this.columns;
    }
}