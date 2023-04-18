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

import java.util.Random;

/**
 * IA that wins if a random value is greater than
 * the win probability.
 */
public class BasicGameIA implements GameIA {
    private static Random random = new Random();
    private float winProbability;

    public BasicGameIA(float winProbability) {
        this.winProbability = winProbability;
    }

    /**
     * Uses 0.5f as the default probability.
     */
    public BasicGameIA() {
        this(0.5f);
    }

    public void setWinProbability(float winProbability) {
        this.winProbability = winProbability;
    }

    @Override
    public boolean wins() {
        return random.nextFloat() >= winProbability;
    }

    public static void main(String[] args) {
        //tests
        BasicGameIA ia = new BasicGameIA(0);
        assert ia.wins();
        ia.setWinProbability(1);
        assert !ia.wins();
        System.out.println("BasicGameIA OK");
    }
    
}
