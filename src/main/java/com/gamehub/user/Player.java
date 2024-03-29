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

import java.util.ArrayList;

import com.gamehub.GameHub;
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
    protected ArrayList<Player> friends;
    
    public Player(String username) {
        if (username == null) throw new IllegalArgumentException("A username must be specified");
        if (!GameHub.usernameAvailable(username)) {
            throw new IllegalArgumentException("Username taken: " + username);
        }
        this.username = username;
        this.memberProfile = null;
        this.friends = new ArrayList<>();
        GameHub.addPlayer(this);
    }
    
    public String getUsername() {
        return username;
    }

    /**
     * Adds p as a friend if players are allowed to do so.
     * @param p
     * @throws IllegalFriendshipException
     */
    public void addFriend(Player p) throws IllegalFriendshipException {
        if (!this.memberProfile.canAskFriendship(this, p)) {
            throw new IllegalFriendshipException("The profile do not allow this friendship.");
        }

        if (friends.contains(p)) {
            throw new IllegalFriendshipException(p.getUsername() + " is already a friend of " + this.getUsername());
        }

        this.friends.add(p);
        p.friends.add(this);
    }

    /**
     * Removes a friendship on both players if it exists.
     * @param p
     * @throws IllegalFriendshipException
     */
    public void removeFriend(Player p) throws IllegalFriendshipException {
        if (this.friends.contains(p) && p.friends.contains(this)) {
            this.friends.remove(p);
            p.friends.remove(this);
        } else {
            throw new IllegalFriendshipException("This friendship does not exist.");
        }
    }

    public boolean hasFriend(Player p) {
        return friends.contains(p);
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("username: " + username + "\n");
        sb.append("profile: " + memberProfile.getName() + "\n");
        sb.append("friends count: " + friends.size() + "\n");

        return sb.toString();
    }

    @Override
    public boolean equals(Object arg0) {
        if (this == arg0) return true;
        if (arg0 == null) return false;
        if (!(arg0 instanceof Player)) return false;
        Player p2 = (Player) arg0;
        return this.username == p2.username;
    }
}
