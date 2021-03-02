package it.cnr.mobilebot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import it.cnr.mobilebot.logic.EventManager;
import it.cnr.mobilebot.logic.MediaEventListener;

public class YouTubeActivity extends AppCompatActivity implements MediaEventListener {

    public static String id = null;
    public static boolean active = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth = (int) (metrics.widthPixels * 0.80);
        int screenHeight = (int) (metrics.heightPixels * 0.85);

        setContentView(R.layout.activity_image_displayer_dialog);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        getWindow().setLayout(screenWidth, screenHeight);

        EventManager.getInstance().addMediaEventListener(this);
        setContentView(R.layout.activity_you_tube);




        System.out.println("ID = "+id);
        active = true;
        System.out.println("--------------------------------------nessuno mi caga");
        YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);



        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                System.out.println("---------------------------------------------a questo punto id  ="+id);
                String videoId = id==null ? "2XKBGRryVDQ" : id;
                youTubePlayer.loadVideo(videoId, 0f);
            }
        });

    }

    @Override
    public void showYoutubeVideo(final String link) {

    }
}