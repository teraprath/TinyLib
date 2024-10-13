package com.github.teraprath.tinylib.sql;

import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.sql.*;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

public abstract class SQLAdapter {

    protected final JavaPlugin plugin; // The Bukkit plugin instance for logging and context
    private final SQLType type; // The type of SQL database (e.g., MySQL or SQLite)
    protected final SQLAuth auth; // Authentication details for connecting to the database
    protected Connection connection; // The SQL connection object
    private final HashMap<String, SQLTable> tables = new HashMap<>(); // Cache for SQL tables
    private boolean debug; // Flag to enable debug logging

    /**
     * Constructor for the SQLAdapter class, initializing the database connection parameters.
     *
     * @param plugin The Bukkit plugin instance.
     * @param type   The type of SQL database (e.g., MySQL or SQLite).
     * @param auth   The authentication details required to connect to the database.
     */
    public SQLAdapter(@Nonnull JavaPlugin plugin, @Nonnull SQLType type, SQLAuth auth) {
        this.plugin = plugin;
        this.type = type;
        this.auth = auth;
    };

    /**
     * Establishes a connection to the SQL database and triggers the onConnect method.
     */
    public void connect() {
        this.connection = getConnection();
        this.onConnect();
    }

    /**
     * Disconnects from the SQL database if a connection exists, and triggers the onDisconnect method.
     */
    public void disconnect() {
        try {
            if (this.hasConnection()) {
                this.connection.close();
                this.onDisconnect();
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Log any SQLException that occurs during disconnection
        }
    }

    /**
     * Enables or disables debug mode for logging SQL statements.
     *
     * @param enabled true to enable debug mode, false to disable it.
     */
    public void setDebugMode(boolean enabled) {
        this.debug = enabled;
    }

    /**
     * Checks if there is an active database connection.
     *
     * @return true if there is a connection; otherwise, false.
     */
    public boolean hasConnection() {
        return this.connection != null;
    }

    /**
     * Executes a SQL statement for modifying the database (e.g., INSERT, UPDATE, DELETE).
     *
     * @param statement The SQL statement to execute.
     */
    public void commit(String statement) {
        try {
            PreparedStatement st = this.connection.prepareStatement(statement);
            if (debug) { plugin.getLogger().info("[SQL] " + statement); } // Log the statement if debug is enabled
            st.execute(); // Execute the SQL statement
        } catch (SQLException e) {
            e.printStackTrace(); // Log any SQLException that occurs during execution
        }
    }

    /**
     * Executes a SQL query and returns the result set.
     *
     * @param statement The SQL query to execute.
     * @return The ResultSet containing the results of the query.
     */
    public ResultSet query(String statement) {
        try {
            PreparedStatement st = this.connection.prepareStatement(statement);
            if (debug) { plugin.getLogger().info("[SQL] " + statement); } // Log the statement if debug is enabled
            return st.executeQuery(); // Execute the SQL query and return the result set
        } catch (SQLException e) {
            e.printStackTrace(); // Log any SQLException that occurs during execution
        }
        return null; // Return null if an exception occurs
    }

    /**
     * Inserts a new row into the specified table.
     *
     * @param tableName The name of the table where the row will be inserted.
     * @param values    The values for the new row, corresponding to the table's columns.
     */
    public void insert(@Nonnull String tableName, Object... values) {
        SQLTable table = getTable(tableName); // Retrieve the SQLTable object for the specified table
        StringBuilder result = new StringBuilder();
        result.append("INSERT IGNORE INTO ").append(table.getName()).append(" VALUES (");
        for (int i = 0; i < table.getColumns().size(); i++) {
            Object val;
            try {
                val = values[i]; // Get the value for the current column
            } catch (Exception e) {
                val = null; // If an index is out of bounds, set the value to null
            }
            // Determine the value to insert, handling defaults and nulls
            if (val == null) {
                if (table.getColumns().get(i).getDefaultValue() != null) {
                    val = table.getColumns().get(i).getDefaultValue(); // Use the default value if available
                } else {
                    val = "null"; // Otherwise, set it as a SQL null
                }
            }
            result.append("'").append(val).append("'").append(","); // Append the value to the insert statement
        }
        result.deleteCharAt(result.length() - 1); // Remove the trailing comma
        result.append(")");
        commit(result.toString()); // Execute the insert statement
    }

    /**
     * Updates a specific column of a row identified by the primary key in the specified table.
     *
     * @param tableName  The name of the table where the row is located.
     * @param primaryKey The primary key of the row to update.
     * @param columnName The name of the column to update.
     * @param newValue   The new value to set for the specified column.
     */
    public void update(@Nonnull String tableName, @Nonnull Object primaryKey, @Nonnull String columnName, Object newValue) {
        SQLTable table = getTable(tableName); // Retrieve the SQLTable object for the specified table
        StringBuilder result = new StringBuilder();
        result.append("UPDATE ").append(table.getName()).append(" SET ").append(columnName).append(" = '").append(newValue.toString());
        result.append("' WHERE ").append(table.getPrimary()).append(" = '").append(primaryKey).append("'");
        commit(result.toString()); // Execute the update statement
    }

    /**
     * Deletes a row from the specified table identified by the primary key.
     *
     * @param tableName  The name of the table where the row is located.
     * @param primaryKey The primary key of the row to delete.
     */
    public void delete(@Nonnull String tableName, @Nonnull Object primaryKey) {
        SQLTable table = getTable(tableName); // Retrieve the SQLTable object for the specified table
        StringBuilder result = new StringBuilder();
        result.append("DELETE FROM ").append(table.getName()).append(" WHERE ").append(table.getPrimary()).append(" = '").append(primaryKey).append("'");
        commit(result.toString()); // Execute the delete statement
    }

    /**
     * Checks if a row exists in the specified table by its primary key.
     *
     * @param tableName  The name of the table to check.
     * @param primaryKey The primary key of the row to check.
     * @return true if the row exists; otherwise, false.
     */
    public boolean exists(@Nonnull String tableName, @Nonnull Object primaryKey) {
        SQLTable table = getTable(tableName); // Retrieve the SQLTable object for the specified table
        ResultSet set = query("SELECT * FROM " + table.getName() + " WHERE " + table.getPrimary() + " = '" + primaryKey + "'");
        try {
            if (set.next()) { // If there is a result, the row exists
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e); // Wrap and throw the exception
        }
        return false; // Return false if no results found
    }

    /**
     * Adds a new SQLTable to the adapter and commits its creation to the database.
     *
     * @param table The SQLTable object representing the new table.
     */
    public void addTable(@Nonnull SQLTable table) {
        this.tables.put(table.getName(), table); // Store the table in the adapter
        commit(table.toString()); // Execute the CREATE TABLE statement
    }

    /**
     * Retrieves a SQLTable by name, either from the cache or by fetching its schema from the database.
     *
     * @param name The name of the table to retrieve.
     * @return The SQLTable object representing the table.
     */
    public SQLTable getTable(@Nonnull String name) {
        SQLTable result = this.tables.get(name); // Check the cache for the table
        if (result == null) { result = fetchTable(name); } // If not found, fetch it from the database
        return result; // Return the SQLTable object
    }

    /**
     * Retrieves a string value from a specified column in a table by the primary key.
     *
     * @param tableName  The name of the table to query.
     * @param primaryKey The primary key of the row to retrieve data from.
     * @param columnName The name of the column to retrieve.
     * @return The string value from the specified column, or null if not found.
     */
    public String getString(@Nonnull String tableName, @Nonnull Object primaryKey, @Nonnull String columnName) {
        SQLTable table = getTable(tableName); // Retrieve the SQLTable object for the specified table
        ResultSet set = query("SELECT " + columnName + " FROM " + table.getName() + " WHERE " + table.getPrimary() + " = '" + primaryKey + "'");
        try {
            if (set.next()) { // If there is a result, return the value
                return set.getString(columnName);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e); // Wrap and throw the exception
        }
        return null; // Return null if no results found
    }

    /**
     * Retrieves an integer value from a specified column in a table by the primary key.
     *
     * @param tableName  The name of the table to query.
     * @param primaryKey The primary key of the row to retrieve data from.
     * @param columnName The name of the column to retrieve.
     * @return The integer value from the specified column, or null if not found.
     */
    public Integer getInt(@Nonnull String tableName, @Nonnull Object primaryKey, @Nonnull String columnName) {
        SQLTable table = getTable(tableName); // Retrieve the SQLTable object for the specified table
        ResultSet set = query("SELECT " + columnName + " FROM " + table.getName() + " WHERE " + table.getPrimary() + " = '" + primaryKey + "'");
        try {
            if (set.next()) { // If there is a result, return the value
                return set.getInt(columnName);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e); // Wrap and throw the exception
        }
        return null; // Return null if no results found
    }

    /**
     * Retrieves a double value from a specified column in a table by the primary key.
     *
     * @param tableName  The name of the table to query.
     * @param primaryKey The primary key of the row to retrieve data from.
     * @param columnName The name of the column to retrieve.
     * @return The double value from the specified column, or null if not found.
     */
    public Double getDouble(@Nonnull String tableName, @Nonnull Object primaryKey, @Nonnull String columnName) {
        SQLTable table = getTable(tableName); // Retrieve the SQLTable object for the specified table
        ResultSet set = query("SELECT " + columnName + " FROM " + table.getName() + " WHERE " + table.getPrimary() + " = '" + primaryKey + "'");
        try {
            if (set.next()) { // If there is a result, return the value
                return set.getDouble(columnName);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e); // Wrap and throw the exception
        }
        return null; // Return null if no results found
    }

    /**
     * Retrieves a float value from a specified column in a table by the primary key.
     *
     * @param tableName  The name of the table to query.
     * @param primaryKey The primary key of the row to retrieve data from.
     * @param columnName The name of the column to retrieve.
     * @return The float value from the specified column, or null if not found.
     */
    public Float getFloat(@Nonnull String tableName, @Nonnull Object primaryKey, @Nonnull String columnName) {
        SQLTable table = getTable(tableName); // Retrieve the SQLTable object for the specified table
        ResultSet set = query("SELECT " + columnName + " FROM " + table.getName() + " WHERE " + table.getPrimary() + " = '" + primaryKey + "'");
        try {
            if (set.next()) { // If there is a result, return the value
                return set.getFloat(columnName);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e); // Wrap and throw the exception
        }
        return null; // Return null if no results found
    }

    /**
     * Retrieves an SQL Array object from a specified column in a table by the primary key.
     *
     * @param tableName  The name of the table to query.
     * @param primaryKey The primary key of the row to retrieve data from.
     * @param columnName The name of the column to retrieve.
     * @return The SQL Array object from the specified column, or null if not found.
     */
    public Array getArray(@Nonnull String tableName, @Nonnull Object primaryKey, @Nonnull String columnName) {
        SQLTable table = getTable(tableName); // Retrieve the SQLTable object for the specified table
        ResultSet set = query("SELECT " + columnName + " FROM " + table.getName() + " WHERE " + table.getPrimary() + " = '" + primaryKey + "'");
        try {
            if (set.next()) { // If there is a result, return the array
                return set.getArray(columnName);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e); // Wrap and throw the exception
        }
        return null; // Return null if no results found
    }

    /**
     * Retrieves a Date object from a specified column in a table by the primary key.
     *
     * @param tableName  The name of the table to query.
     * @param primaryKey The primary key of the row to retrieve data from.
     * @param columnName The name of the column to retrieve.
     * @return The Date object from the specified column, or null if not found.
     */
    public Date getDate(@Nonnull String tableName, @Nonnull Object primaryKey, @Nonnull String columnName) {
        SQLTable table = getTable(tableName); // Retrieve the SQLTable object for the specified table
        ResultSet set = query("SELECT " + columnName + " FROM " + table.getName() + " WHERE " + table.getPrimary() + " = '" + primaryKey + "'");
        try {
            if (set.next()) { // If there is a result, return the date
                return set.getDate(columnName);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e); // Wrap and throw the exception
        }
        return null; // Return null if no results found
    }

    /**
     * Retrieves a boolean value from a specified column in a table by the primary key.
     *
     * @param tableName  The name of the table to query.
     * @param primaryKey The primary key of the row to retrieve data from.
     * @param columnName The name of the column to retrieve.
     * @return The boolean value from the specified column, or false if not found.
     */
    public boolean getBoolean(@Nonnull String tableName, @Nonnull Object primaryKey, @Nonnull String columnName) {
        SQLTable table = getTable(tableName); // Retrieve the SQLTable object for the specified table
        ResultSet set = query("SELECT " + columnName + " FROM " + table.getName() + " WHERE " + table.getPrimary() + " = '" + primaryKey + "'");
        try {
            if (set.next()) { // If there is a result, return the boolean value
                return set.getBoolean(columnName);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e); // Wrap and throw the exception
        }
        return false; // Return false if no results found
    }

    /**
     * Retrieves an Object value from a specified column in a table by the primary key.
     *
     * @param tableName  The name of the table to query.
     * @param primaryKey The primary key of the row to retrieve data from.
     * @param columnName The name of the column to retrieve.
     * @return The Object value from the specified column, or null if not found.
     */
    public Object getObject(@Nonnull String tableName, @Nonnull Object primaryKey, @Nonnull String columnName) {
        SQLTable table = getTable(tableName); // Retrieve the SQLTable object for the specified table
        ResultSet set = query("SELECT " + columnName + " FROM " + table.getName() + " WHERE " + table.getPrimary() + " = '" + primaryKey + "'");
        try {
            if (set.next()) { // If there is a result, return the object
                return set.getObject(columnName);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e); // Wrap and throw the exception
        }
        return null; // Return null if no results found
    }

    /**
     * Fetches the schema of a table from the database, populating a SQLTable object with its details.
     *
     * @param name The name of the table to fetch.
     * @return A SQLTable object representing the fetched table schema.
     */
    private SQLTable fetchTable(@Nonnull String name) {
        ResultSet set = query("DESC " + name); // Execute a DESC command to get the table schema
        SQLTable result = new SQLTable(name); // Create a new SQLTable object for the results
        try {
            while (set.next()) { // Iterate over the schema results
                StringBuilder paramBuilder = null; // For storing parameters if any
                StringBuilder typeBuilder = new StringBuilder();

                typeBuilder.append(set.getString("Type")); // Get the data type of the column

                // Handle parameterized types (e.g., VARCHAR(255))
                if (typeBuilder.indexOf("(") > 0) {
                    paramBuilder = new StringBuilder();
                    paramBuilder.append(set.getString("Type")).delete(0, paramBuilder.indexOf("(") + 1).deleteCharAt(paramBuilder.length() - 1);
                    typeBuilder.delete(typeBuilder.indexOf("("), typeBuilder.length());
                }

                // Create a new SQLColumn object for the current column
                SQLColumn column = new SQLColumn(set.getString("Field"), SQLDataType.valueOf(typeBuilder.toString().toUpperCase()));

                if (paramBuilder != null) {
                    column.setParameter(Arrays.asList(paramBuilder.toString())); // Set parameters for the column if applicable
                }

                // Check if the column is not nullable
                if (set.getString("Null").equals("NO")) {
                    column.setNotNull(true);
                }

                // Check if the column is a primary key
                if (set.getString("Key").equals("PRI")) {
                    result.setPrimary(column.getName()); // Set the primary key for the table
                }

                // Check and set the default value if present
                if (set.getObject("Default") != null) {
                    column.setDefaultValue(set.getObject("Default"));
                }

                result.addColumn(column); // Add the column to the SQLTable object
            }
        } catch (SQLException e) {
            throw new RuntimeException(e); // Wrap and throw the exception
        }
        return result; // Return the populated SQLTable object
    }

    /**
     * Establishes a connection to the database based on the specified SQL type and authentication details.
     *
     * @return A Connection object representing the established database connection.
     */
    private Connection getConnection() {
        try {
            if (this.type.equals(SQLType.SQLITE)) {
                return DriverManager.getConnection(SQLType.SQLITE + auth.getFile().getPath()); // SQLite connection
            } else {
                return DriverManager.getConnection(SQLType.MYSQL + auth.getHost() + ":" + auth.getPort() + "/" + auth.getDatabase() + "?autoReconnect=true", auth.getUser(), auth.getPassword()); // MySQL connection
            }
        } catch (SQLException e) {
            throw new RuntimeException(e); // Wrap and throw the exception
        }
    }

    /**
     * Method to be implemented for handling actions on successful database connection.
     */
    public abstract void onConnect();

    /**
     * Method to be implemented for handling actions on database disconnection.
     */
    public abstract void onDisconnect();
}