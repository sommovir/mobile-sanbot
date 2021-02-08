package it.cnr.mobilebot;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.appcompat.widget.Toolbar;

public class VideoDialog extends AppCompatActivity {

    private View videoContainer= null;
    private MediaController mc;

    @NonNull
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //setTheme(android.R.style.Theme_Dialog);
        super.onCreate(savedInstanceState);
       // requestWindowFeature(Window.FEATURE_NO_TITLE);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenWidth = (int) (metrics.widthPixels * 0.80);
        int screenHeight = (int) (metrics.heightPixels * 0.85);
        videoContainer = findViewById(R.id.videoContainer);

       // Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);

//        if(toolbar == null){
//            System.out.println("\t\t\tMA ALLORA SEI UN PAZZO");
//        }
//        // Sets the Toolbar to act as the ActionBar for this Activity window.
//        // Make sure the toolbar exists in the activity and is not null
//        setSupportActionBar(toolbar);
//
//
//
//        if(this.getSupportActionBar() == null){
//            System.out.println("SUPPORT ACTION BAR IS NULLONE");
//        }else{
//            System.out.println("C'è vita su support action bar");
//        }
//        if(this.getActionBar() == null){
//            System.out.println("ACTION BAR IS NULLONE");
//        }else{
//            System.out.println("c'è vita su action bar normale");
//        }



        setContentView(R.layout.video_dialog);

        if(this.getSupportActionBar() == null){
            System.out.println("SUPPORT ACTION BAR IS NULLONE");
        }
        if(this.getActionBar() == null){
            System.out.println("ACTION BAR IS NULLONE");
        }

        //getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        getWindow().setLayout(screenWidth, screenHeight);
        //getWindow().set

        final VideoView videoView = findViewById(R.id.videoView);
        String videoPath = "android.resource://it.cnr.mobilebot" + "/"+R.raw.peggio;
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);

      //  final MediaController mc = new MediaController(this);
     //   videoView.setMediaController(mc);
     //   mc.setAnchorView(videoView);





        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int width, int height) {
                        /*
                         * add media controller
                         */
                        mc = new MediaController(VideoDialog.this);
                       // mc.setPadding(0, 0, 0, 0);
                        videoView.setMediaController(mc);
                        /*
                         * and set its position on screen
                         */
                        mc.setAnchorView(videoView);
                      //  mc.setPadding(0,0,0,0);
                    }
                });
            }
        });



        videoView.start();






        // AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
       // LayoutInflater inflater = getActivity().getLayoutInflater();
       // videoContainer = inflater.inflate(R.layout.video_dialog, null);
        /*
        builder.setView(videoContainer).setTitle("Video").setNegativeButton("Chiudi", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        */




        //return builder.create();
    }



    @Override
    protected void  onResume(){
        super.onResume();

        if(true){
            return;
        }

        final VideoView videoView = findViewById(R.id.videoView);

        String videoPath = "android.resource://it.cnr.mobilebot" + "/"+R.raw.peggio;
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);


        // mediaController.setEnabled(true);
        // mediaController.show();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {
                //progressDialog.dismiss();
                //isVideoBuffered = true;
              //  final MediaController mediaController = new MediaController(getApplicationContext());
            //    videoView.setMediaController(mediaController);
              //  mediaController.setAnchorView(videoView);

                videoView.requestFocus();
                videoView.setZOrderOnTop(true);
             //   mediaController.setEnabled(true);
             //   mediaController.setVisibility(View.VISIBLE);
//                mediaController.show();
                videoView.start();
                System.out.println("STARTANDO MEDIA CONTROLLER BOH");
            }
        });
    }

    /*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.video_dialog, container, false);
    }

     */

/*
    public void onPostCreate(@Nullable final Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState);

        if(videoContainer == null){
            System.out.println("EH NO EH !!!!!! NON SI FANNO QUESTE COSE");
        }
        final VideoView videoView = videoContainer.findViewById(R.id.videoView);
        String videoPath = "android.resource://it.cnr.mobilebot" + "/"+R.raw.peggio;
        Uri uri = Uri.parse(videoPath);
        videoView.setVideoURI(uri);


       // mediaController.setEnabled(true);
      // mediaController.show();

        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {
                //progressDialog.dismiss();
                //isVideoBuffered = true;
                final MediaController mediaController = new MediaController(getApplicationContext());
                videoView.setMediaController(mediaController);
                mediaController.setAnchorView(videoView);

                videoView.requestFocus();
                videoView.setZOrderOnTop(true);
                mediaController.setEnabled(true);
                mediaController.setVisibility(View.VISIBLE);
                 mediaController.show();
                videoView.start();
                System.out.println("STARTANDO MEDIA CONTROLLER BOH");
            }
        });

        //videoView.start();
    }


 */

    public void play(String video){




    }
}
