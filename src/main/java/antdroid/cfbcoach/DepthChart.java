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

import positions.Player;

/**
 * Created by Achi Jones on 3/18/2016.
 */
class DepthChart extends ArrayAdapter<Player> {
    private final Context context;
    public final ArrayList<Player> players;
    public final ArrayList<Player> playersSelected;
    public int playersRequired;

    public DepthChart(Context context, ArrayList<Player> values, int playersRequired) {
        super(context, R.layout.team_lineup_list_item, values);
        this.context = context;
        this.players = values;
        this.playersRequired = playersRequired;
        playersSelected = new ArrayList<>();
        if (players.size() >= playersRequired) {
            for (int i = 0; i < playersRequired; ++i) {
                playersSelected.add(players.get(i));
            }
        } else {
            for (int i = 0; i < players.size(); ++i) {
                playersSelected.add(players.get(i));
            }
        }
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.team_lineup_list_item, parent, false);

        TextView playerInfo = rowView.findViewById(R.id.textViewLineupPlayerInfo);

        if (players.get(position).injury == null && !players.get(position).isTransfer && !players.get(position).isSuspended) {
            playerInfo.setText(players.get(position).getInfoForLineup());
        } else if (players.get(position).isInjured) {
            playerInfo.setText(players.get(position).getInfoLineupInjury());
        } else if (players.get(position).isTransfer) {
            playerInfo.setText(players.get(position).getInfoLineupTransfer());
        } else if (players.get(position).isSuspended) {
            playerInfo.setText(players.get(position).getInfoLineupSuspended());

        }


        CheckBox isPlayerStarting = rowView.findViewById(R.id.checkboxPlayerStartingLineup);
        if (playersSelected.contains(players.get(position))) {
            isPlayerStarting.setChecked(true);
        } else if (players.get(position).isInjured) {
            // Is injured
            isPlayerStarting.setEnabled(false);
            playerInfo.setTextColor(Color.parseColor("#ffc34d"));
        } else if (players.get(position).isTransfer) {
            // Is Transfer
            isPlayerStarting.setEnabled(false);
            isPlayerStarting.setText("T");
            playerInfo.setTextColor(Color.RED);
        } else if (players.get(position).isSuspended) {
            // Is suspended
            isPlayerStarting.setEnabled(false);
            playerInfo.setTextColor(Color.parseColor("#ffc34d"));
        } else if (players.get(position).isRedshirt) {
            isPlayerStarting.setEnabled(false);
            isPlayerStarting.setText("RS");
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
