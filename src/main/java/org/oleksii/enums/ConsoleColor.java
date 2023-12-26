package org.oleksii.enums;

public enum ConsoleColor {
    // Colors
    BLACK("\u001B[30m"),
    RED("\u001B[31m"),
    GREEN("\u001B[32m"),
    YELLOW("\u001B[33m"),
    BLUE("\u001B[34m"),
    MAGENTA("\u001B[35m"),
    CYAN("\u001B[36m"),
    WHITE("\u001B[37m"),

    // Styles
    RESET("\u001B[0m"),
    BOLD("\u001B[1m"),
    ITALIC("\u001B[3m"),
    UNDERLINE("\u001B[4m"),
    BLINK("\u001B[5m"),
    INVERT("\u001B[7m");

    private final String code;

    ConsoleColor(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}

