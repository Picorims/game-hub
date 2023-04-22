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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import com.gamehub.library.GameCollection;
import com.gamehub.user.Player;
import com.gamehub.utils.Menu;
import com.gamehub.utils.MenuException;
import com.gamehub.utils.MenuOption;

/**
 * Root point of the GameHub application.
 * Entry point of the executable.
 */
public class GameHub {

    private static boolean adminMode = false;
    private static GameCollection collection;
    /**
     * Store players based on their usernames.
     */
    private static HashMap<String, Player> players;
    // TODO check for uniqueness upon account creation

    private static void showMainMenu() {
        try {
            Menu.showMenu("What do you want to do?", new ArrayList<>(Arrays.asList(
                new MenuOption("quit", GameHub::quit)
            )));
        } catch (MenuException e) {
            System.err.println("Could not handle the menu prompt correctly: ");
            e.printStackTrace();
        }
    }

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
        
        showMainMenu();

        // TODO menu state machine
    }
}