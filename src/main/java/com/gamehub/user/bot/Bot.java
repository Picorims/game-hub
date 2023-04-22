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

    public Bot(String username) {
        super(username);
        try {
            setMemberProfile(new BotProfile());
        } catch (IllegalProfileException e) {
            e.printStackTrace();
        }
    }
    
    public void addGame(Game game) {games.add(game);}
    public void addStrategy(GameAI ai) {strategies.add(ai);}

    //TODO ternary relationship between bot, game and ai = bot options
    // TODO list bots in gameub
}
