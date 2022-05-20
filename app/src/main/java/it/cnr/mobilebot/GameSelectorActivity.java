package it.cnr.mobilebot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import it.cnr.mobilebot.game.GameAdapter;
import it.cnr.mobilebot.logic.CognitiveGame;
import it.cnr.mobilebot.logic.CognitiveGameListModel;

public class GameSelectorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_selector);

        RecyclerView recyclerView;
        GameAdapter gameAdapter = new GameAdapter(this);

        recyclerView = findViewById(R.id.RecycleGame);

        recyclerView.setAdapter(gameAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }
}