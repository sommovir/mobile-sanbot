package it.cnr.mobilebot.logic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import it.cnr.mobilebot.R;

public class CognitiveGameListModel extends BaseAdapter {

    private List<CognitiveGame> cognitiveGames;
    private Context context;
    private static LayoutInflater inflater = null;

    public CognitiveGameListModel(Context context, List<CognitiveGame> cognitiveGames) {
        this.cognitiveGames = cognitiveGames;
        this.context = context;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return this.cognitiveGames.size();
    }

    @Override
    public Object getItem(int position) {
        return this.cognitiveGames.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        if (vi == null)
            vi = inflater.inflate(R.layout.activity_cognitive_game_item, null);

        TextView headerTextView = (TextView) vi.findViewById(R.id.textView_header);
        headerTextView.setText(cognitiveGames.get(position).getName());

        TextView descriptionTextView = (TextView) vi.findViewById(R.id.textView_description);
        descriptionTextView.setText(cognitiveGames.get(position).getDescription());
        return vi;
    }
}
