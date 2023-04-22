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

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

/**
 * Collection of all the games of the platform,
 * loaded from the CSV file.
 */
public class GameCollection {
    String os = System.getProperty("os.name");

    enum CSVHeader {
        RANK,
        NAME,
        PLATFORM,
        YEAR,
        GENRE,
        PUBLISHER,
        NA_SALES,
        EU_SALES,
        JP_SALES,
        OTHER_SALES,
        GLOBAL_SALES
    }

    private HashMap<String, Game> games;
    private HashMap<String, Platform> platforms;
    // TODO game results
    
    /**
     * Parses the CSV and creates the game collection
     * @param dataPath CSV file path
     */
    public GameCollection(String dataPath) {

        this.games = new HashMap<>();
        this.platforms = new HashMap<>();

        try (Reader reader = new FileReader(dataPath)) {
            try (CSVParser parser = new CSVParser(reader, CSVFormat.DEFAULT.builder()
                    .setHeader(CSVHeader.class)
                    .setSkipHeaderRecord(true)
                    .build())
            ) {
                // for each line
                for (CSVRecord record : parser) {
                    int year;
                    try {
                        year = Integer.parseInt(record.get(CSVHeader.YEAR));
                    } catch (NumberFormatException e) {
                        year = 0;
                    }
                    
                    float globalSales = Float.parseFloat(record.get(CSVHeader.GLOBAL_SALES));

                    // keep games matching requirements 
                    if (globalSales > 0.5 && year >= 2010) {

                        // platform
                        String platformName = record.get(CSVHeader.PLATFORM);
                        if (platforms.get(platformName) == null) {
                            platforms.put(platformName, new Platform(platformName));
                        }

                        // game
                        String name = record.get(CSVHeader.NAME);
                        String genre = record.get(CSVHeader.GENRE);

                        if (games.get(name) == null) {
                            // new game, create the game instance
                            Game game = new Game(name, genre);
                            games.put(name, game);    
                        }
                        
                        // game version
                        Game game = games.get(name);
                        Platform platform = platforms.get(platformName);
                        String publisher = record.get(CSVHeader.PUBLISHER);
                        GameVersion version = new GameVersion(game, platform, year, publisher, globalSales);
                        game.addGameVersion(version);
                        platform.addGameVersion(version);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("The file could not be loaded:");
            e.printStackTrace();
            System.exit(-1);
        }

        System.out.println("Loaded " + games.size() + " games and " + platforms.size() + " platforms.");
    }
}
