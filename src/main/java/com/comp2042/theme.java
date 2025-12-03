package com.comp2042;

public class Theme {
    public enum Type {COLOR, IMAGE}

    private final String name;
    private final Type type;
    private final String value;

    public Theme(String name, Type type , String value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public String getValue() {
        return value;
    }
}
