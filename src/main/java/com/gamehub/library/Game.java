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
import com.gamehub.user.bot.Bot;

/**
 * Represents a game for any platform.
 * If the name is identical in the CSV, it is the same game.
 * For each platform, a GameVersion is created.
 * A Game may have at most one bot that can be used to play
 * the game.
 */
public class Game {
    private final String name;
    private final String genre;
    private ArrayList<GameVersion> versions;
    private Bot bot;
    private ArrayList<RegisteredPlayer> players;
    private ArrayList<GameResult> results;

    public Game(String name, String genre) {
        this.name = name;
        this.genre = genre;
        versions = new ArrayList<>();
        this.bot = null;
        players = new ArrayList<>();
        results = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addGameVersion(GameVersion version) {
        versions.add(version);
    }

    public void addBot(Bot bot) {
        this.bot = bot;
    }

    public void addPlayer(RegisteredPlayer p) {
        this.players.add(p);
    }

    /**
     * Note that removing a player remove its associated
     * game results.
     * @param p
     */
    public void removePlayer(RegisteredPlayer p) {
        players.remove(p);

        // remove game results containing the player
        for (int i = results.size(); i >= 0; i--) {
            GameResult result = results.get(i);
            if (result.getWinner() == p || result.getLoser() == p) {
                results.remove(result);
            }
        }
    }

    public void addGameResult(GameResult g) {
        results.add(g);
    }


    /**
     * Returns a number between 0 and 1 representing
     * the percentage of wins for the game.
     * @param p Player for which the ratio is computed.
     * @return
     */
    public double getWinRatio(RegisteredPlayer p) {
        int wins = 0;
        int games = 0;

        for (GameResult result : results) {
            if (result.getWinner().equals(p)) {
                wins++;
                games++;
            }
            if (result.getLoser().equals(p)) {
                games++;
            }
        }

        return (games == 0)? 0 : (double) wins / (double) games;
    }

    /**
     * Returns if the game has a version supporting the given platform.
     * @param p the platform to test
     * @return
     */
    public boolean supportsPlatform(Platform p) {
        if (p instanceof NullPlatform) return false;
        boolean supported = false;
        for (GameVersion gv: versions) {
            if (gv.getPlatform().equals(p)) {
                supported = true;
                break;
            }
        }
        return supported;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("name: " + this.name + "\n");
        sb.append("genre: " + this.genre + "\n");
        sb.append("versions:\n");
        for (GameVersion gv : versions) {
            sb.append("---\n");
            sb.append(gv);
        }
        sb.append("---\n");
        String botName = (bot == null)? "N/A" : bot.getUsername();
        sb.append("bot: " + botName + "\n");
        sb.append("players count: " + players.size() + "\n");

        return sb.toString();
    }
}
