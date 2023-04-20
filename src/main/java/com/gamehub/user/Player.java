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

package com.gamehub.user;

import com.gamehub.user.bot.Bot;
import com.gamehub.user.profile.BotProfile;
import com.gamehub.user.profile.IllegalProfileException;
import com.gamehub.user.profile.MemberProfile;

/**
 * Defines the root for every entity that can play games
 * (registered players, bots, etc.)
 */
public abstract class Player {
    protected final String username;
    protected MemberProfile memberProfile;

    public Player(String username) {
        this.username = username;
        this.memberProfile = null;
    }

    /**
     * Defines the user profile.
     * @param profile
     * @throws IllegalProfileException if an attempt to assign an impossible profile is done.
     */
    public void setMemberProfile(MemberProfile profile) throws IllegalProfileException {
        if (this instanceof Bot && !(profile instanceof BotProfile)) {
            throw new IllegalProfileException("Bots can only have the bot profile.");
        }

        if (this instanceof RegisteredPlayer && (profile instanceof BotProfile)) {
            throw new IllegalProfileException("Registered players can't have the bot profile.");
        }

        this.memberProfile = profile;
    }
}
