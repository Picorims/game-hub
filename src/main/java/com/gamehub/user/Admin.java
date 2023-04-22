package com.gamehub.user;

import java.util.Date;

import com.gamehub.library.Platform;

public class Admin extends RegisteredPlayer {

    public Admin() {
        super("admin", "admin@gamehub.com", new Date(0), new Platform("N/A"));
    }
}