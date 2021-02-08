package it.cnr.mobilebot.logic;

import java.util.LinkedList;
import java.util.List;

public class EventManager {

    public static EventManager _instance = null;

    private List<ConnectionEventListener> connectionEventListenerList = new LinkedList<>();

    private EventManager(){

    }

    public static EventManager getInstance(){
        if(_instance == null){
            _instance = new EventManager();
        }
        return _instance;
    }

    public void addConnectionEventListener(ConnectionEventListener listener){
        this.connectionEventListenerList.add(listener);
    }

    public void serverOnline(){
        for (ConnectionEventListener listener : connectionEventListenerList) {
            listener.serverOnline();
        }
    }

    public void serverOffline(){
        for (ConnectionEventListener listener : connectionEventListenerList) {
            listener.serverOffline();
        }
    }



}
