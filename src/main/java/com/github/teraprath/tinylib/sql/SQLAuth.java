package com.github.teraprath.tinylib.sql;

import javax.annotation.Nonnull;
import java.io.File;

public class SQLAuth {

    private String host;
    private int port;
    private String database;
    private String user;
    private String password;
    private File file;

    public SQLAuth(String host, int port, String database, String user, String password) {
        this.host = host;
        this.port = port;
        this.database = database;
        this.user = user;
        this.password = password;
    }

    public SQLAuth(@Nonnull File file) {
        this.file = file;
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

    public File getFile() { return file; }
}
