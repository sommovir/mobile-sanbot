package it.cnr.mobilebot.game.mindgames;

import java.util.ArrayList;
import java.util.List;

import it.cnr.mobilebot.logic.CognitiveGame;

public class GameManager {

    private static GameManager _instance;
    List<CognitiveGame> cognitiveGames = new ArrayList<>();


    private GameManager() {
        cognitiveGames.clear();
        cognitiveGames.add(new CognitiveGame("Lista della Spesa", "questo gioco consiste nell'annoiarsi a morte"));
        cognitiveGames.add(new CognitiveGame("Duck Nukem 3D", "gioco fighissimo anni 90"));
        cognitiveGames.add(new CognitiveGame("Freecell", "gioco per dipendenti pubblici"));
    }


    public static GameManager getInstance() {
        if (_instance == null) {
            _instance = new GameManager();
        }
        return _instance;
    }

    public static GameManager get_instance() {
        return _instance;
    }

    public static void set_instance(GameManager _instance) {
        GameManager._instance = _instance;
    }

    public List<CognitiveGame> getCognitiveGames() {
        return cognitiveGames;
    }
}
