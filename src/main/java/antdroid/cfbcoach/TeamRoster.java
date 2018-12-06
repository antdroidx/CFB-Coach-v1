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

class TeamRoster extends ArrayAdapter<String> {
    private final Context context;
    private final ArrayList<String> values;
    private String userTeamStrRep;
    private final MainActivity mainAct;


    public TeamRoster(Context context, ArrayList<String> values, MainActivity mainAct) {
        super(context, R.layout.team_stats_list_item, values);
        this.context = context;
        this.values = values;
        this.mainAct = mainAct;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.rankings_main, parent, false);
        TextView textLeft = rowView.findViewById(R.id.textRankLeft);
        final TextView textCenter = rowView.findViewById(R.id.textRankCenter);
        TextView textRight = rowView.findViewById(R.id.textRankRight);

        final String[] teamStat = values.get(position).split(",");
        textLeft.setText(teamStat[0]);
        textCenter.setText(teamStat[1] + " " + teamStat[2]);
        textRight.setText(teamStat[3]);

        if (teamStat[0].equals(" ")) {
            // Bold user team
            textCenter.setTypeface(textCenter.getTypeface(), Typeface.BOLD);
            textCenter.setTextColor(Color.parseColor("#5994de"));
        }
        if (teamStat[2].equals("*")) {
            textCenter.setTypeface(textCenter.getTypeface(), Typeface.BOLD);
            textCenter.setTextColor(Color.WHITE);
        }
        if (teamStat[2].contains("RS") || teamStat[2].contains("[T]")) {
            textCenter.setTypeface(textCenter.getTypeface(), Typeface.BOLD);
            textCenter.setTextColor(Color.RED);
        }
        if (teamStat[2].contains("Suspended")) {
            textCenter.setTypeface(textCenter.getTypeface(), Typeface.BOLD);
            textCenter.setTextColor(Color.DKGRAY);
        }
        if (teamStat[2].contains("INJ")) {
            textCenter.setTypeface(textCenter.getTypeface(), Typeface.BOLD);
            textCenter.setTextColor(Color.YELLOW);
        }


        if(!teamStat[3].contains(" ")) {
            if (Integer.parseInt(teamStat[3]) > 90) {
                textRight.setTextColor(Color.parseColor("#5994de"));
            } else if (Integer.parseInt(teamStat[3]) > 80) {
                textRight.setTextColor(Color.GREEN);
            }
        }




        textCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainAct.examinePlayer(teamStat[1]);
            }
        });

        return rowView;
    }

}

