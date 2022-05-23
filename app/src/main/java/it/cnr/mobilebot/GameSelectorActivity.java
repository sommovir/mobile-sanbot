package it.cnr.mobilebot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import it.cnr.mobilebot.game.mindgames.GameAdapter;

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