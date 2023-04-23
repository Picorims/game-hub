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

package com.gamehub;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

import com.gamehub.library.GameCollection;
import com.gamehub.user.Admin;
import com.gamehub.user.Player;
import com.gamehub.user.RegisteredPlayer;
import com.gamehub.user.bot.Bot;
import com.gamehub.utils.Menu;
import com.gamehub.utils.MenuException;
import com.gamehub.utils.MenuOption;

/**
 * Root point of the GameHub application.
 * Entry point of the executable.
 */
public class GameHub {

    private static GameCollection collection;
    private static RegisteredPlayer loggedInUser;
    /**
     * Store players based on their usernames.
     */
    private static HashMap<String, Player> players;

    /**
     * Check if a username is available by checking if a player instance
     * (bot or real player) has this username.
     * @param username
     * @return
     */
    public static boolean usernameAvailable(String username) {
        return players.get(username) == null;
    }

    /**
     * Adds a player to the user base.
     * If the overname is taken, its value is overwritten.
     * Check for availability using `usernameAvailable()`.
     * @param p the player to add
     */
    public static void addPlayer(Player p) {
        if (p == null) throw new IllegalArgumentException("The parameter can't be null");
        players.put(p.getUsername(), p);
    }










    // ================================= UI LOGIC ==================================
    // ================================= UI LOGIC ==================================
    // ================================= UI LOGIC ==================================

    /**
     * Menu displayed at start up
     */
    private static void showMainMenu() {
        Menu.showMenu("What do you want to do?", new ArrayList<>(Arrays.asList(
            new MenuOption("login", GameHub::login),
            new MenuOption("quit", GameHub::quit)
        )));
    }

    /**
     * Login as a user or an admin
     */
    private static void login() {
        String username = Menu.getInputString("username (back = '/b')");
        
        if (username.equals("admin")) {
            // admin
            loggedInUser = (Admin) players.get("admin");
            showLoggedInMenu();

        } else if (username.equals("/b")) {
            // back
            showMainMenu();

        } else {
            // user
            Player potentialPlayer = players.get(username);
            // valid ?
            if (potentialPlayer == null || potentialPlayer instanceof Bot) {
                // invalid
                System.out.println("Invalid username.");
                login();

            } else {
                // valid
                loggedInUser = (RegisteredPlayer) potentialPlayer;
                showLoggedInMenu();
            }
        }
    }

    /**
     * log out and go back to the main menu
     */
    private static void logout() {
        loggedInUser = null;
        showMainMenu();
    }

    /**
     * Menu when a user is logged in (admin, adult or child).
     * The menu adapts itself to the logged in user.
     */
    private static void showLoggedInMenu() {
        ArrayList<MenuOption> menuOptions = new ArrayList<>();

        // shared options
        menuOptions.add(new MenuOption("logout", GameHub::logout));

        Menu.showMenu("Welcome " + loggedInUser.getUsername() + ", please choose an action", menuOptions);
    }

    /**
     * Terminate the application
     */
    private static void quit() {
        System.out.println("Bye!");
    }

    public static void main(String[] args) {

        // check for the csv path
        if (args.length == 0) {
            System.err.println("Please provide a path for the required CSV file containing video games data");
            System.exit(-1);
        }

        System.out.println("""
====================================================================================================================================

        ::::::::             :::            :::   :::         ::::::::::            :::    :::        :::    :::         ::::::::: 
      :+:    :+:          :+: :+:         :+:+: :+:+:        :+:                   :+:    :+:        :+:    :+:         :+:    :+: 
     +:+                +:+   +:+       +:+ +:+:+ +:+       +:+                   +:+    +:+        +:+    +:+         +:+    +:+  
    :#:               +#++:++#++:      +#+  +:+  +#+       +#++:++#              +#++:++#++        +#+    +:+         +#++:++#+    
   +#+   +#+#        +#+     +#+      +#+       +#+       +#+                   +#+    +#+        +#+    +#+         +#+    +#+    
  #+#    #+#        #+#     #+#      #+#       #+#       #+#                   #+#    #+#        #+#    #+#         #+#    #+#     
  ########         ###     ###      ###       ###       ##########            ###    ###         ########          #########       

====================================================================================================================================

        """);
        System.out.println("Loading...");
        
        // init
        collection = new GameCollection(args[0]);
        players = new HashMap<>();
        new Admin(); // create the admin player and add it to players

        // ===================== default state =====================
        // ===================== default state =====================
        // ===================== default state =====================
        try {
            new RegisteredPlayer("john", "john@example.com", Menu.parseDate("01/02/1993"), collection.gePlatform("PC"));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // =========================================================
        // =========================================================
        // =========================================================
        
        showMainMenu();

        // TODO menu state machine

        // TODO methods
    }
}