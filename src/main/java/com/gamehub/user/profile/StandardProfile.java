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

import com.gamehub.user.Child;
import com.gamehub.user.Player;
import com.gamehub.user.RegisteredPlayer;
import com.gamehub.user.bot.Bot;

public class StandardProfile implements MemberProfile {
    @Override
    public String getName() {
        return "Standard profile";
    }
    
    @Override
    public int maxGames() {
        return 50;
    }

    @Override
    public boolean canOwnGames() {
        return true;
    }

    @Override
    public int maxFriends() {
        return 100;
    }

    @Override
    public boolean canAskFriendship(Player sender, Player guest) {
        if (sender instanceof RegisteredPlayer && guest instanceof Child) {
            RegisteredPlayer senderP = (RegisteredPlayer) sender;
            Child guestC = (Child) guest;

            return senderP.hasChild(guestC);
        } else {
            return (sender instanceof RegisteredPlayer
                    && (guest instanceof RegisteredPlayer || guest instanceof Bot));
        }
    }

}
