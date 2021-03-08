package it.cnr.mobilebot;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import java.util.Set;

public class Settings {
    private static Settings _instance = null;
    private static Context context = null;

    public static Settings getInstance(Context ctx){
        if(context == null){
            context = ctx;
        }
        if(_instance == null){
            _instance = new Settings();
        }

        return _instance;
    }

    private Settings(){
        loadSettings();
    }

    private boolean googleSpeechEnabled = false;

    private void loadSettings(){
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);

        if(sharedPref != null){

            googleSpeechEnabled = sharedPref.getBoolean("googleSTT", false);
            //googleSpeechEnabled = Boolean.parseBoolean(sharedPref.getString("googleSTT", "false"));
            System.out.println("PREFERENCE FOUND: google -> "+googleSpeechEnabled);

            sharedPref.registerOnSharedPreferenceChangeListener(new SharedPreferences.OnSharedPreferenceChangeListener() {
                @Override
                public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                    if(key.equals("googleSTT")){
                        googleSpeechEnabled = sharedPreferences.getBoolean("googleSTT", false);
                    }
                }
            });
        }else{
            System.err.println("NO PREFERENZO");
        }
    }

    public boolean isGoogleSpeechEnabled() {
        return googleSpeechEnabled;
    }
}
