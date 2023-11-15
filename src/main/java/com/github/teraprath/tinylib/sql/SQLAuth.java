package com.github.teraprath.tinylib.sql;

import javax.annotation.Nonnull;

public class SQLAuth {

    private final String host;
    private final int port;
    private final String database;
    private final String user;
    private final String password;

    public SQLAuth(@Nonnull String host, @Nonnull int port, @Nonnull String database, @Nonnull String user, @Nonnull String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.user = user;
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public String getDatabase() {
        return database;
    }

    public int getPort() {
        return port;
    }

    public String getPassword() {
        return password;
    }

    public String getUser() {
        return user;
    }
}
