package it.cnr.mobilebot.game.mindgames.supermarket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import it.cnr.mobilebot.MQTTManager;
import it.cnr.mobilebot.R;

public class SuperMarketAdapter extends RecyclerView.Adapter<SuperMarketAdapter.MyViewHolder> {

    private Context context;
    private int right = 0;
    private List<CheckBox> list = new ArrayList<>();

    public SuperMarketAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.checkbox, parent, false);
        return new SuperMarketAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.check.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return MQTTManager.superMarketBlob.getSolutionProducts().size();
    }

    public static void update(int x){
        for(int i = 0;i < x;i++){
            CheckBoxManager.getInstance().getCheckBoxes().get(i).check.setVisibility(View.VISIBLE);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView square;
        private ImageView check;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            square = itemView.findViewById(R.id.square);
            check = itemView.findViewById(R.id.checked);

        }
    }
}
