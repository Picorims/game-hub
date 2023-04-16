package com.gamehub.utils;

/**
 * Exception thrown when the menu faces a problem,
 * such as bad arguments.
 */
public class MenuException extends Exception {
    public MenuException() {super();}
    public MenuException(String msg) {super(msg);}
}