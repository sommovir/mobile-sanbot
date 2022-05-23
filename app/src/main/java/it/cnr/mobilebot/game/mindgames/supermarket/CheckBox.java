package it.cnr.mobilebot.game.mindgames.supermarket;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import it.cnr.mobilebot.R;

public class CheckBox {

    ImageView square;
    ImageView check;
    boolean checked = false;

    public CheckBox() {
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
