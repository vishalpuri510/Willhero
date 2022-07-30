package WillHero;


import Exceptions.SaveNotFoundException;

import java.io.File;
import java.util.ArrayList;

public class LoadGames {
    private final ArrayList<String> loadableGames;
    private final ArrayList<String> loadablePlayers;

    public LoadGames() {
        loadableGames = new ArrayList<>();
        loadablePlayers = new ArrayList<>();
    }

    public ArrayList<String> getLoadableGamesList() {
        String currentDir = System.getProperty("user.dir") + "/WillHero/src/java/";

        File [] directory = new File(currentDir + "SavedGames").listFiles();
        if (directory != null) {
            for(File file : directory) {
                loadableGames.add(currentDir + "/SavedGames/" + file.getName());
            }
            return loadableGames;
        }
        else {
            throw new SaveNotFoundException("No games found");
        }
    }

    public ArrayList<String> getLoadablePlayersList() {

        String currentDir = System.getProperty("user.dir") + "/WillHero/src/java/";

        File[] directory = new File(currentDir + "SavedPlayers").listFiles();
        if (directory != null) {
            for (File file : directory) {
                loadablePlayers.add(currentDir + "/SavedPlayers/" + file.getName());
            }
            return loadablePlayers;
        } else {
            throw new SaveNotFoundException("No players found");
        }
    }
}
