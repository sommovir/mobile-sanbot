package it.cnr.mobilebot.logic;

import android.content.Context;
import android.provider.MediaStore;

import java.util.LinkedList;
import java.util.List;

import it.cnr.mobilebot.game.mindgames.supermarket.CheckBox;
import it.cnr.mobilebot.game.mindgames.supermarket.CheckBoxManager;
import it.cnr.mobilebot.game.mindgames.supermarket.SuperMarket;
import it.cnr.mobilebot.game.mindgames.supermarket.SuperMarketBlob;

public class EventManager {

    private boolean serverOnline = false;

    private static EventManager _instance = null;
    private Context context;
    private SuperMarketBlob superMarketBlob;
    private int correttiG1 = 0;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    private List<ConnectionEventListener> connectionEventListenerList = new LinkedList<>();
    private List<MediaEventListener> mediaEventListeners = new LinkedList<>();

    private EventManager(){

    }

    public boolean isServerOnline() {
        return serverOnline;
    }

    public static EventManager getInstance(){
        if(_instance == null){
            _instance = new EventManager();
        }
        return _instance;
    }

    public void addMediaEventListener(MediaEventListener listener){
        System.out.println("LISTENER SI AGGIUNGE");
        this.mediaEventListeners.add(listener);
    }

    public void addConnectionEventListener(ConnectionEventListener listener){
        this.connectionEventListenerList.add(listener);
    }

    public void serverOnline(){
        this.serverOnline = true;
        for (ConnectionEventListener listener : connectionEventListenerList) {
            listener.serverOnline();
        }
    }

    public void serverOffline(){
        this.serverOnline = false;
        for (ConnectionEventListener listener : connectionEventListenerList) {
            listener.serverOffline();
        }
    }

    public int getCorrettiG1() {
        return correttiG1;
    }

    public void setCorrettiG1(int correttiG1) {
        this.correttiG1 = correttiG1;
    }

    public void playYouTubeVideo(String id){
        for (MediaEventListener listener : mediaEventListeners) {
            System.out.println("CALLING LISTENER");
            listener.showYoutubeVideo(id);
        }
    }

    public SuperMarketBlob getSuperMarketBlob() {
        return superMarketBlob;
    }

    public void setSuperMarketBlob(SuperMarketBlob superMarketBlob) {
        this.superMarketBlob = superMarketBlob;
    }
}
