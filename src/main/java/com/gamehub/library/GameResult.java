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

import com.gamehub.user.RegisteredPlayer;

/**
 * Represents the result of a match between two players.
 */
public class GameResult {
    private final RegisteredPlayer winner;
    private final RegisteredPlayer loser;

    private final Game game;

    public GameResult(Game game, RegisteredPlayer winner, RegisteredPlayer loser) {
        if (game == null || winner == null || loser == null) {
            throw new GameResultException("parameters can't be null");
        }

        this.winner = winner;
        this.loser = loser;
        this.game = game;
        game.addGameResult(this);
    }

    public RegisteredPlayer getWinner() {
        return winner;
    }

    public RegisteredPlayer getLoser() {
        return loser;
    }
}
