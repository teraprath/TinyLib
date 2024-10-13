package com.github.teraprath.tinylib.sql;

import javax.annotation.Nonnull;
import java.io.File;

public class SQLAuth {

    private String host; // The host address of the SQL database
    private int port; // The port number to connect to the SQL database
    private String database; // The name of the database to connect to
    private String user; // The username for database authentication
    private String password; // The password for database authentication
    private File file; // A file containing database configuration (optional)

    /**
     * Constructor for SQLAuth, initializing the connection parameters for the SQL database.
     *
     * @param host The host address of the SQL database.
     * @param port The port number used to connect to the SQL database.
     * @param database The name of the database to connect to.
     * @param user The username used for database authentication.
     * @param password The password used for database authentication.
     */
    public SQLAuth(String host, int port, String database, String user, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.user = user;
        this.password = password;
    }

    /**
     * Constructor for SQLAuth, initializing the object with a file that contains database configuration.
     *
     * @param file A File object representing the configuration file for the database.
     */
    public SQLAuth(@Nonnull File file) {
        this.file = file;
    }

    /**
     * Gets the host address for the SQL database.
     *
     * @return The host address of the SQL database.
     */
    public String getHost() {
        return host;
    }

    /**
     * Gets the name of the database.
     *
     * @return The name of the database to connect to.
     */
    public String getDatabase() {
        return database;
    }

    /**
     * Gets the port number used to connect to the SQL database.
     *
     * @return The port number for the SQL connection.
     */
    public int getPort() {
        return port;
    }

    /**
     * Gets the password used for database authentication.
     *
     * @return The password for the database user.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets the username used for database authentication.
     *
     * @return The username for the database connection.
     */
    public String getUser() {
        return user;
    }

    /**
     * Gets the configuration file associated with the SQLAuth instance.
     *
     * @return A File object representing the database configuration file, or null if not set.
     */
    public File getFile() {
        return file;
    }
}