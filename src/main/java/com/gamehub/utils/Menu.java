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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Utility for displaying interactive menu
 * and asking for user input.
 */
public class Menu {
    public static final String DATE_FORMAT = "dd/MM/yyyy";
    private static final int PAGE_LENGTH = 25;

    // must not be closed or the input stream is closed
    // and unavailable until the program restarts.
    public static final Scanner scanner = new Scanner(System.in);
    /**
     * Asks the user for an integer while the input is not valid
     * and return the result
     * @param promptMsg display message
     * @param min min value
     * @param max max value
     * @return user response
     */
    public static int getInputInt(String promptMsg, int min, int max) {
        int response = -1;

        boolean invalid = true;

        // re-ask until the input is valid
        while (invalid) {
            System.out.print(promptMsg + ": ");
            
            try {
                response = scanner.nextInt();
                invalid = (response > max || response < min);
            } catch (InputMismatchException e) {
                invalid = true;
                // empty the queue to not cause an infinite loop
                // due to the invalid value not being consumed.
                scanner.next();
            }
            
            if (invalid) System.out.println("invalid input!");
        }

        scanner.nextLine(); // consume the \n not consumed by nextInt()

        return response;
    }

    /**
     * Asks the user for an integer while the input is not valid
     * and return the result
     * @param promptMsg display message
     * @return user response
     */
    public static int getInputInt(String promptMsg) {
        return getInputInt(promptMsg, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    /**
     * Ask the user for a string while the input is invalid
     * and return the result
     * @param promptMsg
     * @return
     */
    public static String getInputString(String promptMsg) {
        String response = null;

        boolean invalid = true;

        // re-ask until the input is valid
        while (invalid) {
            System.out.print(promptMsg + ": ");
            
            try {
                response = scanner.nextLine();
                invalid = false;
            } catch (InputMismatchException e) {
                invalid = true;
                // empty the queue to not cause an infinite loop
                // due to the invalid value not being consumed.
                scanner.next();
            }
            
            if (invalid) System.out.println("invalid input!");
        }

        return response;
    }

    /**
     * Shows a menu of options, with an index associated for each option.
     * Asks the user for one of the options, and execute the associated action.
     * @param title
     * @param options (at least one must be provided)
     * @param page the page to show
     */
    public static void showMenu(String title, ArrayList<MenuOption> options, int page) {
        if (options == null || options.isEmpty()) {
            throw new MenuException("At least one option must be provided");
        }

        int min = page * PAGE_LENGTH;
        int max = Math.min(min + PAGE_LENGTH, options.size());

        // display
        long pageMax = (long) Math.floor(options.size() / PAGE_LENGTH);
        System.out.println("================ ["+title+" | page "+(page+1)+"/"+(pageMax+1)+"] ================");

        for (int i = min; i < max; i++) {
            System.out.println(i + " - " + options.get(i).getTitle());
        }

        int minInput = 0;
        int respPrevious = Integer.MIN_VALUE;
        int respNext = Integer.MIN_VALUE;
        if (min > 0) {
            minInput--;
            respPrevious = minInput;
            System.out.println(minInput + " - PREVIOUS");
        }
        if (min + PAGE_LENGTH <= options.size()) {
            minInput--;
            respNext = minInput;
            System.out.println(minInput + " - NEXT");
        }

        // input
        int response = getInputInt("-> action", minInput, options.size()-1);

        // action
        if (response == respPrevious) {
            showMenu(title, options, page - 1);

        } else if (response == respNext) {
            showMenu(title, options, page + 1);
        
        } else {
            System.out.println("================ [ => " + options.get(response).getTitle() + "] ================");
            options.get(response).call();    
        }
    }

    public static void showMenu(String title, ArrayList<MenuOption> options) {
        showMenu(title, options, 0);
    }

    /**
     * Parses a date string according to the app format.
     * @param date
     * @return the date object
     * @throws ParseException
     */
    public static Date parseDate(String date) throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        Date dateObj = dateFormat.parse(date);
        return dateObj;
    }

    /**
     * Put the program on hold until the user presses Enter.
     * @param promptMsg message to display
     */
    public static void pressEnterToConfirm(String promptMsg) {
        System.out.print(promptMsg + " [press Enter]");
        scanner.nextLine();
    }

    public static void pressEnterToConfirm() {
        pressEnterToConfirm("");
    }



    /**
     * tests
     * @param args
     */
    public static void main(String[] args) {
        int v = getInputInt("test");
        System.out.println("=> " + v);

        int w = getInputInt("test", -2, 2);
        System.out.println("=> " + w);

        String s = getInputString("test str");
        System.out.println("=> " + s);

        pressEnterToConfirm("back");

        try {
            showMenu(null, null);
        } catch (Exception e) {
            // it should throw if no option is given (not for a null title)
            showMenu("yes or no ?", new ArrayList<>(Arrays.asList(
                new MenuOption("yes", () -> {
                    System.out.println("it's a yes.");
                }),
                new MenuOption("no", () -> {
                    System.out.println("it's a no.");
                })
            )));

            ArrayList<MenuOption> options = new ArrayList<>();
            for (int i = 0; i < 70; i++) {
                final int index = i;
                options.add(new MenuOption("do " + i, () -> {
                    System.out.println("it is " + index);
                }));
            }
            showMenu("a lot of stuff", options);
        }
    }
}
