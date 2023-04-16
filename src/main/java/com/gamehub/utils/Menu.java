package com.gamehub.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Utility for displaying interactive menu
 * and asking for user input.
 */
public class Menu {
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

        // open scanner (closes automatically)
        boolean invalid = true;

        // re-ask until the input is valid
        while (invalid) {
            System.out.println(promptMsg + ":");
            
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
     * Shows a menu of options, with an index associated for each option.
     * Asks the user for one of the options, and execute the associated action.
     * @param title
     * @param options
     * @throws MenuException
     */
    public static void showMenu(String title, ArrayList<MenuOption> options) throws MenuException {
        if (options == null || options.isEmpty()) {
            throw new MenuException("At least one option must be provided");
        }

        // display
        System.out.println("================ [" + title + "] ================");

        for (int i = 0; i < options.size(); i++) {
            System.out.println(i + " - " + options.get(i).getTitle());
        }

        // input
        int response = getInputInt("-> action", 0, options.size()-1);

        // action
        System.out.println("================ [ => " + options.get(response).getTitle() + "] ================");
        options.get(response).call();
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

        try {
            showMenu(null, null);
        } catch (MenuException e) {
            // it should throw if no option is given (not for a null title)
            try {
                showMenu("yes or no ?", new ArrayList<>(Arrays.asList(
                    new MenuOption("yes", () -> {
                        System.out.println("it's a yes.");
                    }),
                    new MenuOption("no", () -> {
                        System.out.println("it's a no.");
                    })
                )));
            } catch (MenuException e2) {
                e2.printStackTrace();
            }
        }
    }
}
