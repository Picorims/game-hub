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

/**
 * Represents a version of a game for a specific platform
 */
public class GameVersion {
    private final Game game;
    private final Platform platform;
    private final int year;
    private final String publisher;
    private final Float globalSales;

    public GameVersion(Game game, Platform platform, int year, String publisher, Float globalSales) {
        this.game = game;
        this.platform = platform;
        this.year = year;
        this.publisher = publisher;
        this.globalSales = globalSales;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("platform: " + platform.getName() + "\n");
        sb.append("year: " + year + "\n");
        sb.append("publisher: " + publisher + "\n");
        sb.append("globalSales: " + globalSales + " millions of copies\n");

        return sb.toString();
    }
}
