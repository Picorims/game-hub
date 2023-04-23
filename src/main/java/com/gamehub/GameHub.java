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
import java.util.Set;

import com.gamehub.library.Game;
import com.gamehub.library.GameCollection;
import com.gamehub.library.GameResult;
import com.gamehub.library.GameResultException;
import com.gamehub.library.NullPlatform;
import com.gamehub.library.Platform;
import com.gamehub.user.Admin;
import com.gamehub.user.Child;
import com.gamehub.user.GameAcquiringException;
import com.gamehub.user.IllegalFriendshipException;
import com.gamehub.user.Player;
import com.gamehub.user.RegisteredPlayer;
import com.gamehub.user.TutoringException;
import com.gamehub.user.bot.Bot;
import com.gamehub.user.profile.GoldProfile;
import com.gamehub.user.profile.IllegalProfileException;
import com.gamehub.user.profile.KidProfile;
import com.gamehub.user.profile.MemberProfile;
import com.gamehub.user.profile.StandardProfile;
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
     * Mode for the `selectPlayer` function.
     */
    private enum SelectPlayerMode {
        INCLUDE_SELF,
        EXCLUDE_SELF
    }

    private enum SelectPlayerType {
        ALL,
        CHILDREN
    }

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

    public static void removePlayer(Player p) {
        if (p == null) throw new IllegalArgumentException("The parameter can't be null");
        players.put(p.getUsername(), null);
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
        
        if (!(loggedInUser instanceof Child)) {
            menuOptions.add(new MenuOption("create a player", GameHub::createPlayer));
        }
        
        if (!(loggedInUser instanceof Admin) && !(loggedInUser instanceof Child)) {
            menuOptions.add(new MenuOption("obtain a game", GameHub::getGame));
            menuOptions.add(new MenuOption("add a tutor to a child", GameHub::addTutor));
        }

        if (!(loggedInUser instanceof Admin)) {
            menuOptions.add(new MenuOption("add a friend", GameHub::addFriend));
            menuOptions.add(new MenuOption("remove a friend", GameHub::removeFriend));
            menuOptions.add(new MenuOption("delete account", GameHub::deleteAccount));
        }
        
        menuOptions.add(new MenuOption("logout", GameHub::logout));

        Menu.showMenu("Welcome " + loggedInUser.getUsername() + ", please choose an action", menuOptions);
    }

    /**
     * Show the menu to choose a game
     */
    private static void showGameInfoMenu() {
        Game game = selectGame();
        if (game == null) {
            System.out.println("no game available.");
            showLoggedInMenu();
        } else {
            printGameInfo(game);
        }
    }

    /**
     * Display information for the given game.
     * @param name the name of the game
     */
    private static void printGameInfo(Game game) {
        System.out.println(game);
        Menu.pressEnterToConfirm("back");
        showLoggedInMenu();
    }

    /**
     * Show the menu to choose a player
     */
    private static void showPlayerInfoMenu() {
        RegisteredPlayer player = selectPlayer(SelectPlayerMode.INCLUDE_SELF);
        if (player == null) {
            System.out.println("No player available.");
            showLoggedInMenu();
        } else {
            printPlayerInfo(player);
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
     * Add a new player to the database.
     * An admin creates an adult player, while an adult user
     * creates a child user. 
     */
    private static void createPlayer() {
        if (loggedInUser instanceof Child) throw new IllegalStateException("Children can't create players");
        
        boolean creatingChild = false;
        if (loggedInUser instanceof Admin) {
            System.out.println("You are creating an adult player.");
        } else {
            creatingChild = true;
            System.out.println("You are creating a child player.");
        }

        String username = Menu.getInputString(
            "username (unique)",
            ((name) -> players.get(name) == null)
        );

        MemberProfile profile;
        if (creatingChild) profile = new KidProfile();
        else {
            int profileResp = Menu.showMenu("profile", new ArrayList<>(Arrays.asList(
                new MenuOption("standard"),
                new MenuOption("gold")
            )));
            if (profileResp == 0) {
                profile = new StandardProfile();
            } else {
                profile = new GoldProfile();
            }
        }

        String email = Menu.getInputString("email", ((name) -> name.contains("@")));
        String birthDate = Menu.getInputString("birth date (dd/mm/yyyy)", (date) -> {
            try {
                Menu.parseDate(date);
                return true;
            } catch (ParseException e) {
                return false;
            }
            // do not test if it is greater than today, nor if it is valid for a child (< 18 years old)
        });
        Platform platform = selectPlatform();

        Date date;
        try {
            date = Menu.parseDate(birthDate);

            if (creatingChild) {
                new Child(username, email, date, platform, loggedInUser);
                
            } else {
                RegisteredPlayer p = new RegisteredPlayer(username, email, date, platform);
                p.setMemberProfile(profile);
            }
    
        } catch (ParseException e) {
            System.err.println("date parsing error.");
            e.printStackTrace();
            System.exit(-1);

        } catch (TutoringException e) {
            System.out.println("Cannot create the child player.");
            System.out.println(e.getMessage());
            showLoggedInMenu();

        } catch (IllegalProfileException e) {
            System.err.println("profile assignation error.");
            e.printStackTrace();
            System.exit(-1);
        }

        System.out.println(username + " created.");
        showLoggedInMenu();
    }

    /**
     * Let a player give itself a new game
     */
    private static void getGame() {
        if (loggedInUser instanceof Admin || loggedInUser instanceof Child) {
            throw new IllegalStateException("only adult players can obtain games.");
        }

        Game game = selectGame(loggedInUser.getPlatform());
        if (game == null) {
            System.out.println("No game available for your platform.");
            showLoggedInMenu();
        } else {
            try {
                loggedInUser.obtainGame(game);
                System.out.println("game obtained!");
                showLoggedInMenu();
            } catch (GameAcquiringException e) {
                System.out.println("Could not obtain the game:");
                System.out.println(e.getMessage());
                showLoggedInMenu();
            }
        }
    }

    /**
     * Add a friend to the logged in user
     */
    private static void addFriend() {
        RegisteredPlayer friend = selectPlayer();
        if (friend == null) {
            System.out.println("No player available.");
            showLoggedInMenu();
        } else {
            try {
                loggedInUser.addFriend(friend);
                System.out.println("added " + friend.getUsername() + " as a friend.");
            } catch (IllegalFriendshipException e) {
                System.out.println("Could not add " + friend.getUsername() + " as a friend:");
                System.out.println(e.getMessage());
            } finally {
                showLoggedInMenu();
            }
        }
    }

    /**
     * Remove a friend from the logged in user
     */
    private static void removeFriend() {
        RegisteredPlayer friend = selectPlayer();
        if (friend == null) {
            System.out.println("No player available.");
            showLoggedInMenu();
        } else {
            try {
                loggedInUser.removeFriend(friend);
                System.out.println("removed " + friend.getUsername() + " as a friend.");
            } catch (IllegalFriendshipException e) {
                System.out.println("Could not remove " + friend.getUsername() + " as a friend:");
                System.out.println(e.getMessage());
            } finally {
                showLoggedInMenu();
            }
        }
    }

    private static void addTutor() {
        if (loggedInUser instanceof Admin || loggedInUser instanceof Child) {
            throw new IllegalStateException("only adults can add a tutor to a child");
        }

        Child child = (Child) selectPlayer(SelectPlayerMode.EXCLUDE_SELF, SelectPlayerType.CHILDREN);

        if (child == null) {
            System.out.println("no child available");
            showLoggedInMenu();
        } else {
            try {
                child.addTutor(loggedInUser);
                System.out.println("added " + loggedInUser.getUsername() + " as a tutor of " + child.getUsername());
            } catch (TutoringException e) {
                System.out.println("Could not add yourself as a tutor:");
                System.out.println(e.getMessage());
            } finally {
                showLoggedInMenu();
            }
        }
    }

    /**
     * Remove the logged in user from the database
     */
    private static void deleteAccount() {
        Menu.showMenu(
            "Are you sure you want to delete the account for " + loggedInUser.getUsername() + "?",
            new ArrayList<>(Arrays.asList(
                new MenuOption("yes", () -> {
                    loggedInUser.deleteAccount();
                    System.out.println("account deleted, logging out...");
                    logout();            
                }),
                new MenuOption("no", GameHub::showLoggedInMenu)
            ))
        );
    }










    /**
     * Select a platform from the list of available platforms through a menu.
     * @return
     */
    private static Platform selectPlatform() {
        ArrayList<MenuOption> options = new ArrayList<>();
        Set<String> platforms = collection.getPlatforms();

        for (String platform : platforms) {
            options.add(new MenuOption(platform));
        }

        if (options.size() == 0) {
            return new NullPlatform();
        } else {
            int result = Menu.showMenu("Select a platform", options);
            String name = options.get(result).getTitle();
            return collection.getPlatform(name);
        }
    }

    /**
     * Show a menu to select a registered player.
     * @param mode include the logged in user in the list. (exclude self by default)
     * @param type type of players to select. (all by default)
     * @return
     */
    private static RegisteredPlayer selectPlayer(SelectPlayerMode mode, SelectPlayerType type) {
        ArrayList<MenuOption> menuOptions = new ArrayList<>();

        for (Player p : players.values()) {
            if (p instanceof RegisteredPlayer) {
                if (mode == SelectPlayerMode.EXCLUDE_SELF && loggedInUser.equals(p)) {
                    // remove the logged in user
                    continue;
                }
                if (type == SelectPlayerType.CHILDREN && !(p instanceof Child)) {
                    // children only
                    continue;
                }
                menuOptions.add(new MenuOption(p.getUsername()));
            }
        }

        if (menuOptions.size() == 0) {
            return null;
        } else {
            int result = Menu.showMenu("Please choose a player", menuOptions);
            String name = menuOptions.get(result).getTitle();
            return (RegisteredPlayer) players.get(name);
        }
    }

    private static RegisteredPlayer selectPlayer(SelectPlayerMode mode) {
        return selectPlayer(mode, SelectPlayerType.ALL);
    }

    private static RegisteredPlayer selectPlayer() {
        return selectPlayer(SelectPlayerMode.EXCLUDE_SELF, SelectPlayerType.ALL);
    }

    /**
     * Show a menu to select a game, and return the name of the game selected.
     * @param requiredPlatform only show games for a given platform.
     * @return
     */
    private static Game selectGame(Platform requiredPlatftorm) {
        ArrayList<MenuOption> menuOptions = new ArrayList<>();

        Iterable<String> gameNames;
        if (requiredPlatftorm instanceof NullPlatform) {
            gameNames = collection.getGameNames();
        } else {
            gameNames = collection.getGameNames(requiredPlatftorm);
        }

        for (String name : gameNames) {
            menuOptions.add(new MenuOption(name));
        }

        if (menuOptions.size() == 0) {
            return null;
        } else {
            int result = Menu.showMenu("Please choose a game", menuOptions);
            String name = menuOptions.get(result).getTitle();
            return collection.getGame(name);
        }
    }

    private static Game selectGame() {
        return selectGame(new NullPlatform());
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
            
            RegisteredPlayer p1 = new RegisteredPlayer("john", "john@example.com", Menu.parseDate("01/02/1993"), collection.getPlatform("X360"));
            p1.obtainGame(minecraft); // 240
            
            RegisteredPlayer p2 = new RegisteredPlayer("dan", "dan@example.com", Menu.parseDate("08/04/1997"), collection.getPlatform("PS3"));
            p2.obtainGame(minecraft); // 240
            
            RegisteredPlayer p3 = new RegisteredPlayer("jeff", "jeff@example.com", Menu.parseDate("08/11/1994"), collection.getPlatform("PS4"));
            p3.obtainGame(minecraft); // 240

            RegisteredPlayer p4 = new Child("jessica", "jessica@example.com", Menu.parseDate("08/11/2007"), collection.getPlatform("PS4"), p1);
            p4.obtainGame(minecraft); // 240

            new GameResult(minecraft, p1, p3);
            new GameResult(minecraft, p2, p1);
            new GameResult(minecraft, p2, p3);
            new GameResult(minecraft, p4, p1);

            p1.addFriend(p2);

        } catch (ParseException | GameAcquiringException | GameResultException | TutoringException | IllegalFriendshipException e) {
            System.out.println("incoherent test data.");
            e.printStackTrace();
            System.exit(-1);
        }
        // =========================================================
        // =========================================================
        // =========================================================
        
        showMainMenu();

        // TODO menu state machine

        // TODO methods
    }
}