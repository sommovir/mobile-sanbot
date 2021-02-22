package it.cnr.mobilebot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Window;
import android.widget.ImageView;

import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;

public class ImageDisplayerDialog extends AppCompatActivity {

    public static String externalURL = null;

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
        ImageView imageView = findViewById(R.id.imageView_Display);

        /*
        Picasso.with(this.getContext())
                .load(currentArticle.getmImageUrl())
                .centerCrop()
                .transform(new CircleTransform(50,0))
                .fit()
                .into(image);*/
        //Picasso.get().load("http://i.imgur.com/DvpvklR.png").fit().into(imageView);

        /*
        final OkHttpClient client = new OkHttpClient.Builder()
                .protocols(Collections.singletonList(Protocol.HTTP_1_1))
                .build();

        final Picasso picasso = new Picasso.Builder(this)
                .downloader(new OkHttp3Downloader(client))
                .build();


         */

//        Picasso.setSingletonInstance(picasso);
        /*
        builder.listener(new Picasso.Listener() {
            @Override
            public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                exception.printStackTrace();
            }});

         */

        System.out.println("PICAZZO");
        //picasso.setLoggingEnabled(true);
        //picasso.load("https://i.imgur.com/DvpvklR.png").fit().into(imageView);

        if(externalURL ==null) {
            Picasso.get()
                    .load("https://drive.google.com/uc?export=view&id=1RWIaXXQC0L-C6bAiVpJcqqx-e__Vv2sI")
                    .placeholder(R.drawable.loadingo)
                    .resize(screenWidth, screenHeight)
                    .centerInside()
                    .into(imageView);
        }else{
            Picasso.get()
                    .load(externalURL)
                    .placeholder(R.drawable.loadingo)
                    .resize(screenWidth, screenHeight)
                    .centerInside()
                    .into(imageView);
        }

        System.out.println("BOH");


/*
        URL newurl = null;
        try {
            newurl = new URL("http://i.imgur.com/DvpvklR.png");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
            imageView.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }*/


    }

    public void show(){
        ImageView imageView = findViewById(R.id.imageView_Display);
      //  Picasso.get().load("http://i.imgur.com/DvpvklR.png").fit().into(imageView);
    }
}