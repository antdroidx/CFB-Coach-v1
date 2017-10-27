package antdroid.cfbcoach;

/*
  Created by Achi Jones on 3/29/2016.
 */

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class LeagueHistoryListArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;
    private String userTeamAbbr;

    public LeagueHistoryListArrayAdapter(Context context, String[] values, String userTeamAbbr) {
        super(context, R.layout.league_history_list_item, values);
        this.context = context;
        this.values = values;
        this.userTeamAbbr = userTeamAbbr;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.league_history_list_item, parent, false);
        TextView textTop = (TextView) rowView.findViewById(R.id.textViewLeagueHistoryTop);
        TextView textMiddle = (TextView) rowView.findViewById(R.id.textViewLeagueHistoryMiddle);
        TextView textBottom = (TextView) rowView.findViewById(R.id.textViewLeagueHistoryBottom);
        if (position == 2) {
            position = 1;
        }
        String[] record = values[position].split("\n");
        if (record.length == 3) {
            textTop.setText(record[0]);
            textMiddle.setText(record[1]);
            textBottom.setText(record[2]);
            if (record[1].split(" ")[1].equals(userTeamAbbr)) {
                // User team won NCG, make it special color
                textMiddle.setTextColor(Color.parseColor("#1A75FF"));
            }
            if (record[2].split(" ")[5].equals(userTeamAbbr)) {
                // User team won POTY, make it special color
                textBottom.setTextColor(Color.parseColor("#1A75FF"));
            }
        }

        return rowView;
    }
}
