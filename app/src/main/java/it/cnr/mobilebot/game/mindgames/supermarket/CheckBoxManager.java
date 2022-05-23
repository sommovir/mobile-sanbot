package it.cnr.mobilebot.game.mindgames.supermarket;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

import it.cnr.mobilebot.MQTTManager;
import it.cnr.mobilebot.game.mindgames.GameManager;
import it.cnr.mobilebot.logic.CognitiveGame;

public class CheckBoxManager {
    private static CheckBoxManager _instance;
    List<CheckBox> checkBoxes = new ArrayList<>();


    private CheckBoxManager() {
        checkBoxes.clear();
        for(int i = 0; i < MQTTManager.superMarketBlob.getSolutionProducts().size(); i++){
            checkBoxes.add(new CheckBox());
        }
    }


    public static CheckBoxManager getInstance() {
        if (_instance == null) {
            _instance = new CheckBoxManager();
        }
        return _instance;
    }

    public static CheckBoxManager get_instance() {
        return _instance;
    }

    public static void set_instance(CheckBoxManager _instance) {
        CheckBoxManager._instance = _instance;
    }

    public List<CheckBox> getCheckBoxes() {
        return checkBoxes;
    }


}

