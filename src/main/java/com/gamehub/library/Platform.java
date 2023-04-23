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

package com.gamehub.library;

import java.util.ArrayList;

import com.gamehub.user.RegisteredPlayer;

/**
 * Represents a device that can host games
 */
public class Platform {
    private final String name;
    private ArrayList<GameVersion> gameVersions;
    private ArrayList<RegisteredPlayer> players;
    
    public Platform(String name) {
        this.name = name;
        gameVersions = new ArrayList<>();
        players = new ArrayList<>();
    }
    
    public String getName() {
        return name;
    }

    public void addGameVersion(GameVersion gameVersion) {
        gameVersions.add(gameVersion);
    }

    public void addPlayer(RegisteredPlayer p) {
        players.add(p);
    }

    public void removePlayer(RegisteredPlayer p) {
        players.remove(p);
    }
}
