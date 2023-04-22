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

package com.gamehub.user.profile;

import com.gamehub.user.Player;

/**
 * Template for creating registered player profiles.
 * Describes the permissions of a profile.
 * 
 * Note that methods are not static as a static method
 * can't be overriden, requiring to instantiate the class
 * to access them.
 */
public interface MemberProfile {
    public int maxGames();
    public boolean canOwnGames();
    public int maxFriends();
    /**
     * Verifies if two player can befriend by looking
     * at their properties and profile.
     * @param sender a player with this profile.
     * @param guest a player with any profile.
     * @return
     */
    public boolean canAskFriendship(Player sender, Player guest);
}

// TODO implement x4