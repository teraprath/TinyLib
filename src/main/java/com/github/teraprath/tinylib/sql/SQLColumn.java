package com.github.teraprath.tinylib.sql;

import java.util.ArrayList;

public class SQLColumn {

    private final String name;
    private final SQLDataType type;
    private final ArrayList<String> params;
    private Object defaultValue;
    private boolean auto;
    private boolean notNull;

    public SQLColumn(String name, SQLDataType type) {
        this.name = name;
        this.type = type;
        this.params = new ArrayList<>();
        this.auto = false;
        this.notNull = false;
    }

    public SQLColumn setParameter(Object... params) {
        for (Object param : params) {
            this.params.add(param.toString());
        }
        return this;
    }

    public SQLColumn setDefaultValue(Object value) {
        this.defaultValue = value;
        return this;
    }

    public SQLColumn setAuto(boolean enabled) {
        this.auto = enabled;
        return this;
    }

    public SQLColumn setNotNull(boolean enabled) {
        this.notNull = enabled;
        return this;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getParams() {
        return params;
    }

    public boolean isAuto() {
        return auto;
    }

    public boolean isNotNull() {
        return notNull;
    }

    public Object getDefaultValue() {
        return defaultValue;
    }

    public SQLDataType getType() {
        return type;
    }

}
