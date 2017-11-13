package antdroid.cfbcoach;

/*
  Created by ahngu on 9/29/2017.
 */

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MainRankings extends ArrayAdapter<String> {
    private final Context context;
    private final ArrayList<String> values;
    private String userTeamStrRep;

    public MainRankings(Context context, ArrayList<String> values, String userTeamStrRep) {
        super(context, R.layout.team_stats_list_item, values);
        this.context = context;
        this.values = values;
        this.userTeamStrRep = userTeamStrRep;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.rankings_main, parent, false);
        TextView textLeft = rowView.findViewById(R.id.textRankLeft);
        TextView textCenter = rowView.findViewById(R.id.textRankCenter);
        TextView textRight = rowView.findViewById(R.id.textRankRight);

        String[] teamStat = values.get(position).split(",");
        textLeft.setText(teamStat[0]);
        textCenter.setText(teamStat[1]);
        textRight.setText(teamStat[2]);

        if (teamStat[1].equals(userTeamStrRep)) {
            // Bold user team
            textLeft.setTypeface(textLeft.getTypeface(), Typeface.BOLD);
            textLeft.setTextColor(Color.parseColor("#1A75FF"));
            textCenter.setTypeface(textCenter.getTypeface(), Typeface.BOLD);
            textCenter.setTextColor(Color.parseColor("#1A75FF"));
            textRight.setTypeface(textRight.getTypeface(), Typeface.BOLD);
            textRight.setTextColor(Color.parseColor("#1A75FF"));
        }

        return rowView;
    }

    public void setUserTeamStrRep(String userTeamStrRep) {
        this.userTeamStrRep = userTeamStrRep;
    }
}

