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
import java.util.Date;

import com.gamehub.library.Game;
import com.gamehub.library.Platform;
import com.gamehub.user.profile.IllegalProfileException;
import com.gamehub.user.profile.StandardProfile;

/**
 * Represents a user that can interact with the platform.
 */
public class RegisteredPlayer extends Player {

    protected final String email;
    protected final Date birthDate;
    protected final Platform platform;
    protected ArrayList<RegisteredPlayer> friends;
    protected ArrayList<Game> games;
    protected ArrayList<Child> children;
    
    public RegisteredPlayer(String username, String email, Date birthDate, Platform platform) {
        super(username);
        if (platform == null) throw new IllegalArgumentException("platform must be defined.");
        if (birthDate == null) throw new IllegalArgumentException("birth date must be defined.");
        if (username == null || username == "") throw new IllegalArgumentException("username must be defined.");
        if (email == null || email == "") throw new IllegalArgumentException("email must be defined.");
        // for simplicity, it is not checked if an email is valid, nor if a username is unique.
        this.email = email;
        this.birthDate = birthDate;
        this.platform = platform;
        platform.addPlayer(this);
        this.friends = new ArrayList<>();
        this.games = new ArrayList<>();
        this.children = new ArrayList<>();

        try {
            setMemberProfile(new StandardProfile());
        } catch (IllegalProfileException e) {
            e.printStackTrace();
        }
    }

    public RegisteredPlayer(String username, String email, Date birthDate) {
        this(username, email, birthDate, null);
    }

    /**
     * Adds p as a friend if players are allowed to do so.
     * @param p
     * @throws IllegalFriendshipException
     */
    public void addFriend(RegisteredPlayer p) throws IllegalFriendshipException {
        if (this.memberProfile.canBefriend(this, p)) {
            this.friends.add(p);
            p.friends.add(this);
        } else {
            throw new IllegalFriendshipException("The profile do not allow this friendship.");
        }
    }

    /**
     * Removes a friendship on both players if it exists.
     * @param p
     * @throws IllegalFriendshipException
     */
    public void removeFriend(RegisteredPlayer p) throws IllegalFriendshipException {
        if (this.friends.contains(p) && p.friends.contains(this)) {
            this.friends.remove(p);
            p.friends.remove(this);
        } else {
            throw new IllegalFriendshipException("This friendship does not exist.");
        }
    }

    /**
     * Add a game to the player, let it be for itself or through a gift.
     * @param g game to obtain
     * @throws GameAcquiringException
     */
    public void obtainGame(Game g) throws GameAcquiringException {
        if (this.games.size() < this.memberProfile.maxGames()) {
            this.games.add(g);
            g.addPlayer(this);
        } else {
            throw new GameAcquiringException("Trying to give a game while the limit has been reached.");
        }
    }

    /**
     * Offer 
     * @param g game to offer
     * @param p receiver
     * @throws GameAcquiringException
     */
    public void offerGame(Game g, RegisteredPlayer p) throws GameAcquiringException {
        p.obtainGame(g);
    }

    public void deleteAccount() {
        for (RegisteredPlayer f : friends) {
            try {
                f.removeFriend(this);
            } catch (IllegalFriendshipException e) {
                System.err.println("Could not remove friendship between " + this.username + " and " + f.username + ".");
            }
        }
        for (Game g : games) g.removePlayer(this);
        platform.removePlayer(this);
    }

    /**
     * Add a child to the player making it a tutor.
     * @param child
     * @throws TutoringException
     */
    protected void addChild(Child child) throws TutoringException {
        if (child == null) throw new TutoringException("No child specified");
        if (children.contains(child)) throw new TutoringException("The child already exists in the list.");
        children.add(child);
    }

    protected void removeChild(Child child) throws TutoringException {
        if (child == null) throw new TutoringException("No child specified");
        if (!children.contains(child)) throw new TutoringException("The child does not exist in the list.");
        children.remove(child);
    }
}
