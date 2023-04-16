package com.gamehub.utils;

/**
 * Describes a menu item: the title
 * and it's associated action.
 */
public class MenuOption {
    private final String title;
    private final Action action;
    
    /**
     * Builds the menu option
     * @param title label displayed next to the number
     * @param action action executed upon selection of the option (function)
     */
    public MenuOption(String title, Action action) {
        this.title = title;
        this.action = action;
    }

    /**
     * Display text for the option
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     * Triggers the option's action
     */
    public void call() {
        action.action();
    }
}
