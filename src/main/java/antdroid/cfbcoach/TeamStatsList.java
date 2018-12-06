package antdroid.cfbcoach;

/*
  Created by Achi Jones on 2/20/2016.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

class TeamStatsList extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;

    public TeamStatsList(Context context, String[] values) {
        super(context, R.layout.team_stats_list_item, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.team_stats_list_item, parent, false);
        TextView textLeft = rowView.findViewById(R.id.textTeamStatsLeft);
        TextView textCenter = rowView.findViewById(R.id.textTeamStatsCenter);
        TextView textRight = rowView.findViewById(R.id.textTeamStatsRight);

        String[] teamStat = values[position].split(",");
        textLeft.setText(teamStat[0]);
        textCenter.setText(teamStat[1]);
        textRight.setText(teamStat[2]);

        return rowView;
    }
}
