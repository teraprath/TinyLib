package com.github.teraprath.tinylib.sql;

import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.sql.*;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

public abstract class SQLAdapter {

    protected final JavaPlugin plugin;
    private final SQLType type;
    protected final SQLAuth auth;
    protected Connection connection;
    private final HashMap<String, SQLTable> tables = new HashMap<>();
    private boolean debug;

    public SQLAdapter(@Nonnull JavaPlugin plugin, @Nonnull SQLType type, SQLAuth auth) {
        this.plugin = plugin;
        this.type = type;
        this.auth = auth;
    };

    public void connect() {
        this.connection = getConnection();
        this.onConnect();
    }

    public void disconnect() {
        try {
            if (this.hasConnection()) {
                this.connection.close();
                this.onDisconnect();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void setDebugMode(boolean enabled) {
        this.debug = enabled;
    }

    public boolean hasConnection() {
        return this.connection != null;
    }

    public void commit(String statement) {
        try {
            PreparedStatement st = this.connection.prepareStatement(statement);
            if (debug) { plugin.getLogger().info("[SQL] " + statement); }
            st.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet query(String statement) {
        try {
            PreparedStatement st = this.connection.prepareStatement(statement);
            if (debug) { plugin.getLogger().info("[SQL] " + statement); }
            return st.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void insert(@Nonnull String tableName, Object... values) {
        SQLTable table = getTable(tableName);
        StringBuilder result = new StringBuilder();
        result.append("INSERT IGNORE INTO ").append(table.getName()).append(" VALUES (");
        for (int i = 0; i < table.getColumns().size(); i++) {
            Object val;
            try {
                val = values[i];
            } catch (Exception e) {
                val = null;
            }
            if (val == null) {
                if (table.getColumns().get(i).getDefaultValue() != null) {
                    val = table.getColumns().get(i).getDefaultValue();
                } else {
                    val = "null";
                }
            }
            result.append("'").append(val).append("'").append(",");
        }
        result.deleteCharAt(result.length() - 1);
        result.append(")");
        commit(result.toString());
    }

    public void update(@Nonnull String tableName, @Nonnull Object primaryKey, @Nonnull String columnName, Object newValue) {
        SQLTable table = getTable(tableName);
        StringBuilder result = new StringBuilder();
        result.append("UPDATE ").append(table.getName()).append(" SET ").append(columnName).append(" = '").append(newValue.toString());
        result.append("' WHERE ").append(table.getPrimary()).append(" = '").append(primaryKey).append("'");
        commit(result.toString());
    }

    public void delete(@Nonnull String tableName, @Nonnull Object primaryKey) {
        SQLTable table = getTable(tableName);
        StringBuilder result = new StringBuilder();
        result.append("DELETE FROM ").append(table.getName()).append(" WHERE ").append(table.getPrimary()).append(" = '").append(primaryKey).append("'");
        commit(result.toString());
    }

    public boolean exists(@Nonnull String tableName, @Nonnull Object primaryKey) {
        SQLTable table = getTable(tableName);
        ResultSet set = query("SELECT * FROM " + table.getName() + " WHERE " + table.getPrimary() + " = '" + primaryKey + "'");
        try {
            if (set.next()) {
                return true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public void addTable(@Nonnull SQLTable table) {
        this.tables.put(table.getName(), table);
        commit(table.toString());
    }

    public SQLTable getTable(@Nonnull String name) {
        SQLTable result = this.tables.get(name);
        if (result == null) { result = fetchTable(name); }
        return result;
    }

    public String getString(@Nonnull String tableName, @Nonnull Object primaryKey, @Nonnull String columnName) {
        SQLTable table = getTable(tableName);
        ResultSet set = query("SELECT " + columnName + " FROM " + table.getName() + " WHERE " + table.getPrimary() + " = '" + primaryKey + "'");
        try {
            if (set.next()) {
                return set.getString(columnName);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Integer getInt(@Nonnull String tableName, @Nonnull Object primaryKey, @Nonnull String columnName) {
        SQLTable table = getTable(tableName);
        ResultSet set = query("SELECT " + columnName + " FROM " + table.getName() + " WHERE " + table.getPrimary() + " = '" + primaryKey + "'");
        try {
            if (set.next()) {
                return set.getInt(columnName);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Double getDouble(@Nonnull String tableName, @Nonnull Object primaryKey, @Nonnull String columnName) {
        SQLTable table = getTable(tableName);
        ResultSet set = query("SELECT " + columnName + " FROM " + table.getName() + " WHERE " + table.getPrimary() + " = '" + primaryKey + "'");
        try {
            if (set.next()) {
                return set.getDouble(columnName);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Float getFloat(@Nonnull String tableName, @Nonnull Object primaryKey, @Nonnull String columnName) {
        SQLTable table = getTable(tableName);
        ResultSet set = query("SELECT " + columnName + " FROM " + table.getName() + " WHERE " + table.getPrimary() + " = '" + primaryKey + "'");
        try {
            if (set.next()) {
                return set.getFloat(columnName);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Array getArray(@Nonnull String tableName, @Nonnull Object primaryKey, @Nonnull String columnName) {
        SQLTable table = getTable(tableName);
        ResultSet set = query("SELECT " + columnName + " FROM " + table.getName() + " WHERE " + table.getPrimary() + " = '" + primaryKey + "'");
        try {
            if (set.next()) {
                return set.getArray(columnName);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public Date getDate(@Nonnull String tableName, @Nonnull Object primaryKey, @Nonnull String columnName) {
        SQLTable table = getTable(tableName);
        ResultSet set = query("SELECT " + columnName + " FROM " + table.getName() + " WHERE " + table.getPrimary() + " = '" + primaryKey + "'");
        try {
            if (set.next()) {
                return set.getDate(columnName);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public boolean getBoolean(@Nonnull String tableName, @Nonnull Object primaryKey, @Nonnull String columnName) {
        SQLTable table = getTable(tableName);
        ResultSet set = query("SELECT " + columnName + " FROM " + table.getName() + " WHERE " + table.getPrimary() + " = '" + primaryKey + "'");
        try {
            if (set.next()) {
                return set.getBoolean(columnName);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public Object getObject(@Nonnull String tableName, @Nonnull Object primaryKey, @Nonnull String columnName) {
        SQLTable table = getTable(tableName);
        ResultSet set = query("SELECT " + columnName + " FROM " + table.getName() + " WHERE " + table.getPrimary() + " = '" + primaryKey + "'");
        try {
            if (set.next()) {
                return set.getObject(columnName);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    private SQLTable fetchTable(@Nonnull String name) {
        ResultSet set = query("DESC " + name);
        SQLTable result = new SQLTable(name);
        try {
            while (set.next()) {

                StringBuilder paramBuilder = null;
                StringBuilder typeBuilder = new StringBuilder();

                typeBuilder.append(set.getString("Type"));

                if (typeBuilder.indexOf("(") > 0) {
                    paramBuilder = new StringBuilder();
                    paramBuilder.append(set.getString("Type")).delete(0, paramBuilder.indexOf("(") + 1).deleteCharAt(paramBuilder.length() - 1);
                    typeBuilder.delete(typeBuilder.indexOf("("), typeBuilder.length());
                }

                SQLColumn column = new SQLColumn(set.getString("Field"), SQLDataType.valueOf(typeBuilder.toString().toUpperCase()));

                if (paramBuilder != null) {
                    column.setParameter(Arrays.asList(paramBuilder.toString()));
                }

                if (set.getString("Null").equals("NO")) {
                    column.setNotNull(true);
                }

                if (set.getString("Key").equals("PRI")) {
                    result.setPrimary(column.getName());
                }

                if (set.getObject("Default") != null) {
                    column.setDefaultValue(set.getObject("Default"));
                }

                result.addColumn(column);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    private Connection getConnection() {
        try {
            if (this.type.equals(SQLType.SQLITE)) {
                return DriverManager.getConnection(SQLType.SQLITE + auth.getFile().getPath());
            } else {
                return DriverManager.getConnection(SQLType.MYSQL + auth.getHost() + ":" + auth.getPort() + "/" + auth.getDatabase() + "?autoReconnect=true", auth.getUser(), auth.getPassword());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public abstract void onConnect();
    public abstract void onDisconnect();

}