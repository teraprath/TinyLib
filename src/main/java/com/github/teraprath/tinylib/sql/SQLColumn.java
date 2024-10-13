package com.github.teraprath.tinylib.sql;

import java.util.ArrayList;

public class SQLColumn {

    private final String name; // The name of the column
    private final SQLDataType type; // The SQL data type of the column
    private final ArrayList<String> params; // Optional parameters for the column (e.g., length)
    private Object defaultValue; // The default value for the column
    private boolean auto; // Indicates if the column is AUTO_INCREMENT
    private boolean notNull; // Indicates if the column is NOT NULL

    /**
     * Constructor for SQLColumn, initializing with a column name and type.
     *
     * @param name The name of the column.
     * @param type The data type for the column (e.g., INT, VARCHAR).
     */
    public SQLColumn(String name, SQLDataType type) {
        this.name = name;
        this.type = type;
        this.params = new ArrayList<>();
        this.auto = false; // Default: column is not AUTO_INCREMENT
        this.notNull = false; // Default: column can be null
    }

    /**
     * Sets parameters for the column, typically used for defining length or size constraints.
     * Converts the parameters to strings and adds them to the params list.
     *
     * @param params The parameters to be set for the column (e.g., length, precision).
     * @return The current SQLColumn instance, allowing method chaining.
     */
    public SQLColumn setParameter(Object... params) {
        for (Object param : params) {
            this.params.add(param.toString());
        }
        return this;
    }

    /**
     * Sets the default value for the column.
     *
     * @param value The default value to be set for this column.
     * @return The current SQLColumn instance, allowing method chaining.
     */
    public SQLColumn setDefaultValue(Object value) {
        this.defaultValue = value;
        return this;
    }

    /**
     * Enables or disables AUTO_INCREMENT for the column.
     * Typically used for primary key columns.
     *
     * @param enabled Set to true to enable AUTO_INCREMENT, false to disable.
     * @return The current SQLColumn instance, allowing method chaining.
     */
    public SQLColumn setAuto(boolean enabled) {
        this.auto = enabled;
        return this;
    }

    /**
     * Sets whether the column should be NOT NULL, meaning the column cannot have null values.
     *
     * @param enabled Set to true to enforce NOT NULL, false to allow null values.
     * @return The current SQLColumn instance, allowing method chaining.
     */
    public SQLColumn setNotNull(boolean enabled) {
        this.notNull = enabled;
        return this;
    }

    /**
     * Gets the name of the column.
     *
     * @return The name of the column.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the parameters defined for the column (e.g., length, precision).
     *
     * @return A list of strings representing the parameters of the column.
     */
    public ArrayList<String> getParams() {
        return params;
    }

    /**
     * Checks if the column is set to AUTO_INCREMENT.
     *
     * @return true if the column is AUTO_INCREMENT, false otherwise.
     */
    public boolean isAuto() {
        return auto;
    }

    /**
     * Checks if the column is marked as NOT NULL.
     *
     * @return true if the column is NOT NULL, false otherwise.
     */
    public boolean isNotNull() {
        return notNull;
    }

    /**
     * Gets the default value for the column, if any.
     *
     * @return The default value of the column, or null if none is set.
     */
    public Object getDefaultValue() {
        return defaultValue;
    }

    /**
     * Gets the SQL data type of the column.
     *
     * @return The SQL data type (e.g., INT, VARCHAR).
     */
    public SQLDataType getType() {
        return type;
    }

}