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
import java.util.HashMap;
import java.util.List;

import com.gamehub.library.Game;
import com.gamehub.library.GameCollection;
import com.gamehub.library.GameResult;
import com.gamehub.library.GameResultException;
import com.gamehub.user.Admin;
import com.gamehub.user.Child;
import com.gamehub.user.GameAcquiringException;
import com.gamehub.user.Player;
import com.gamehub.user.RegisteredPlayer;
import com.gamehub.user.TutoringException;
import com.gamehub.user.bot.Bot;
import com.gamehub.utils.Menu;
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

    public static RegisteredPlayer getLoggedInUser() {
        return loggedInUser;
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
        menuOptions.add(new MenuOption("show game information", GameHub::showGameInfoMenu));
        menuOptions.add(new MenuOption("show player information", GameHub::showPlayerInfoMenu));
        menuOptions.add(new MenuOption("logout", GameHub::logout));

        Menu.showMenu("Welcome " + loggedInUser.getUsername() + ", please choose an action", menuOptions);
    }

    /**
     * Show the menu to choose a game
     */
    private static void showGameInfoMenu() { //TODO make generic
        ArrayList<MenuOption> menuOptions = new ArrayList<>();

        List<String> gameNames = collection.getGameNames();

        for (int i = 0; i < gameNames.size(); i++) {
            final int gameIndex = i;
            menuOptions.add(new MenuOption(gameNames.get(i), () -> {
                printGameInfo(gameNames.get(gameIndex));
            }));
        }

        if (menuOptions.size() > 0) {
            Menu.showMenu("Please choose a game", menuOptions);
        } else {
            Menu.showMenu("Please choose a game", new ArrayList<>(Arrays.asList(
                new MenuOption("none, go back", GameHub::showLoggedInMenu)
            )));
        }
    }

    /**
     * Display information for the given game.
     * @param name the name of the game
     */
    private static void printGameInfo(String name) {
        System.out.println(collection.getGame(name));
        Menu.pressEnterToConfirm("back");
        showLoggedInMenu();
    }

    /**
     * Show the menu to choose a player
     */
    private static void showPlayerInfoMenu() { //TODO make generic
        ArrayList<MenuOption> menuOptions = new ArrayList<>();

        for (Player p : players.values()) {
            if (p instanceof RegisteredPlayer) {
                final RegisteredPlayer player = (RegisteredPlayer) p;
                menuOptions.add(new MenuOption(p.getUsername(), () -> {
                    printPlayerInfo(player);
                }));
            }
        }

        if (menuOptions.size() > 0) {
            Menu.showMenu("Please choose a player", menuOptions);
        } else {
            Menu.showMenu("Please choose a player", new ArrayList<>(Arrays.asList(
                new MenuOption("none, go back", GameHub::showLoggedInMenu)
            )));
        }
    }

    /**
     * Display information for the given player.
     * @param player the name of the player
     */
    private static void printPlayerInfo(Player player) {
        System.out.println(player);
        Menu.pressEnterToConfirm("back");
        showLoggedInMenu();
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
            Game minecraft = collection.getGame("Minecraft");
            
            RegisteredPlayer p1 = new RegisteredPlayer("john", "john@example.com", Menu.parseDate("01/02/1993"), collection.gePlatform("X360"));
            p1.obtainGame(minecraft); // 240
            
            RegisteredPlayer p2 = new RegisteredPlayer("dan", "dan@example.com", Menu.parseDate("08/04/1997"), collection.gePlatform("PS3"));
            p2.obtainGame(minecraft); // 240
            
            RegisteredPlayer p3 = new RegisteredPlayer("jeff", "jeff@example.com", Menu.parseDate("08/11/1994"), collection.gePlatform("PS4"));
            p3.obtainGame(minecraft); // 240

            RegisteredPlayer p4 = new Child("jessica", "jessica@example.com", Menu.parseDate("08/11/2007"), collection.gePlatform("PS4"), p1);
            p3.obtainGame(minecraft); // 240

            new GameResult(minecraft, p1, p3);
            new GameResult(minecraft, p2, p1);
            new GameResult(minecraft, p2, p3);
            new GameResult(minecraft, p4, p1);

        } catch (ParseException | GameAcquiringException | GameResultException | TutoringException e) {
            System.out.println("incoherent test data.");
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