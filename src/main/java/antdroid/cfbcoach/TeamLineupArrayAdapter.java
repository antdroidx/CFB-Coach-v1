package antdroid.cfbcoach;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;

import Simulation.Player;

/**
 * Created by Achi Jones on 3/18/2016.
 */
public class TeamLineupArrayAdapter extends ArrayAdapter<Player> {
    private final Context context;
    public final ArrayList<Player> players;
    public ArrayList<Player> playersSelected;
    public int playersRequired;

    public TeamLineupArrayAdapter(Context context, ArrayList<Player> values, int playersRequired) {
        super(context, R.layout.team_lineup_list_item, values);
        this.context = context;
        this.players = values;
        this.playersRequired = playersRequired;
        playersSelected = new ArrayList<>();
        for (int i = 0; i < playersRequired; ++i) {
            playersSelected.add(players.get(i));
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.team_lineup_list_item, parent, false);

        TextView playerInfo = (TextView) rowView.findViewById(R.id.textViewLineupPlayerInfo);

        if (players.get(position).injury == null) {
            playerInfo.setText(players.get(position).getInfoForLineup());
        } else {
            playerInfo.setText(players.get(position).getInfoLineupInjury());
        }


        CheckBox isPlayerStarting = (CheckBox) rowView.findViewById(R.id.checkboxPlayerStartingLineup);
        if (playersSelected.contains(players.get(position))) {
            isPlayerStarting.setChecked(true);
        } else if (players.get(position).year == 0) {
            // Is redshirt
            isPlayerStarting.setEnabled(false);
            playerInfo.setTextColor(Color.RED);
        } else if (players.get(position).isInjured) {
            // Is injured
            isPlayerStarting.setEnabled(false);
            playerInfo.setTextColor(Color.RED);
        } else {
            isPlayerStarting.setChecked(false);
        }

        isPlayerStarting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    playersSelected.add(players.get(position));
                } else {
                    playersSelected.remove(players.get(position));
                }
            }
        });


        return rowView;


    }
}
