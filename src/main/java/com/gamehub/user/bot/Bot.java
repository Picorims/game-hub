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
package com.gamehub.user.bot;

import java.util.ArrayList;
import java.util.HashMap;

import com.gamehub.library.Game;
import com.gamehub.user.Player;
import com.gamehub.user.profile.BotProfile;
import com.gamehub.user.profile.IllegalProfileException;

/**
 * A bot can play against a player using an AI.
 * It is associated to games that it supports.
 */
public class Bot extends Player {

    private ArrayList<Game> games;
    private ArrayList<GameAI> strategies;
    /**
     * Each entry is a list of AI available for the said game,
     * that can be played by the bot.
     */
    private HashMap<Game, ArrayList<GameAI>> gameOptions;

    public Bot(String username) {
        super(username);
        try {
            setMemberProfile(new BotProfile());
        } catch (IllegalProfileException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
    
    public void addGame(Game game) {games.add(game);}
    public void addStrategy(GameAI ai) {strategies.add(ai);}

    /**
     * Add an AI for a given game. A game can have multiple AIs.
     * @param game
     * @param ai
     * @throws IllegalGameOptionException
     */
    public void addGameOption(Game game, GameAI ai) throws IllegalGameOptionException {
        if (game == null || ai == null) {
            throw new IllegalArgumentException("parameters can't be null");
        }
        
        if (!games.contains(game) || !strategies.contains(ai)) {
            throw new IllegalGameOptionException("The bot do not own either the game or the ai.");
        }

        if (gameOptions.get(game) == null) {
            gameOptions.put(game, new ArrayList<>());
        }
        gameOptions.get(game).add(ai);
    }

    public ArrayList<GameAI> getGameOptions(Game game) {
        return (ArrayList<GameAI>) gameOptions.get(game).clone();
    }
}
