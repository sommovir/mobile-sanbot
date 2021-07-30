package it.cnr.mobilebot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import it.cnr.mobilebot.logic.CognitiveGame;
import it.cnr.mobilebot.logic.CognitiveGameListModel;

public class GameSelectorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_selector);

        List<CognitiveGame> cognitiveGames = new ArrayList<>();
        cognitiveGames.add(new CognitiveGame("Lista della Spesa", "questo gioco consiste nell'annoiarsi a morte"));
        cognitiveGames.add(new CognitiveGame("Duck Nukem 3D", "gioco fighissimo anni 90"));
        cognitiveGames.add(new CognitiveGame("Freecell", "gioco per dipendenti pubblici"));

        ListView listview = (ListView) findViewById(R.id.gameSelector);
        if(listview == null){
            System.out.println("NULLONE DEMMERDAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        }
        listview.setAdapter(new CognitiveGameListModel(this, cognitiveGames));

    }
}