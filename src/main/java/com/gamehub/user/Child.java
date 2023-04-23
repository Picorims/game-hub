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

import com.gamehub.GameHub;
import com.gamehub.library.Platform;

/**
 * A Child is a RegisteredPlayer with more restricted actions.
 */
public class Child extends RegisteredPlayer {

    private ArrayList<RegisteredPlayer> tutors;

    public Child(String username, String email, Date birthDate, Platform platform, RegisteredPlayer tutor) throws TutoringException {
        super(username, email, birthDate, platform);

        tutors = new ArrayList<>();
        addTutor(tutor);
    }

    /**
     * Add a tutor for the child and add the child to the tutor's children.
     * @param tutor
     * @throws TutoringException if tutoring conditions do not match.
     */
    public void addTutor(RegisteredPlayer tutor) throws TutoringException {
        if (tutor == null) throw new TutoringException("A tutor must be specified");
        if (tutor instanceof Child) throw new TutoringException("Tutors must be adults");
        if (tutors.size() == 2) throw new TutoringException("Maximum of tutors reached.");

        tutors.add(tutor);
        tutor.addChild(this);

    }

    /**
     * Removes a tutor from a child;
     * @param tutor
     * @throws TutoringException
     */
    public void removeTutor(RegisteredPlayer tutor) throws TutoringException {
        if (tutor == null) throw new TutoringException("A tutor must be specified");
        if (tutor instanceof Child) throw new TutoringException("Tutors must be adults");
        if (tutors.size() == 1) throw new TutoringException("Minimum of tutors reached.");
        if (!tutors.contains(tutor)) throw new TutoringException("This player is not a tutor of the child.");

        tutors.remove(tutor);
        tutor.removeChild(this);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        if (tutors.contains(GameHub.getLoggedInUser())) {
            // not restricted for tutors
            sb.append(super.toString());
        } else {
            // restritcted
            sb.append("username: " + username + "\n");
            sb.append("games count: " + games.size() + "\n");
            sb.append("friends count: " + friends.size() + "\n");
        }

        return sb.toString();
    }
}
