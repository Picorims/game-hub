/*
MIT License

Copyright (c) 2023 Picorims alias Charly Schmidt

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

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

    public MenuOption(String title) {
        this(title, null);
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
        if (action != null) action.action();
    }
}
