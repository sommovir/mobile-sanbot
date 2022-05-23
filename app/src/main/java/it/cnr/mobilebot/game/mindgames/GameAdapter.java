package it.cnr.mobilebot.game.mindgames;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import it.cnr.mobilebot.R;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.MyViewHolder>{

    private Context context;

    public GameAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_game, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        holder.title.setText(GameManager.getInstance().getCognitiveGames().get(position).getName());
        holder.descr.setText(GameManager.getInstance().getCognitiveGames().get(position).getDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"Hai selezionato: " + GameManager.getInstance().getCognitiveGames().get(holder.getAdapterPosition()).getName(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return GameManager.getInstance().getCognitiveGames().size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private TextView descr;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
                title = itemView.findViewById(R.id.title);
                descr = itemView.findViewById(R.id.description);

            }
        }

    }

