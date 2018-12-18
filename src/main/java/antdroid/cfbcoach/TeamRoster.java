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
    private final int week;


    public TeamRoster(Context context, ArrayList<String> values, MainActivity mainAct, int week) {
        super(context, R.layout.team_roster, values);
        this.context = context;
        this.values = values;
        this.mainAct = mainAct;
        this.week = week;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.team_roster, parent, false);
        TextView textLeft = rowView.findViewById(R.id.textTeamRosterLeft);
        TextView textClass = rowView.findViewById(R.id.textTeamRosterClass);
        final TextView textCenter = rowView.findViewById(R.id.textTeamRosterCenter);
        TextView textRight = rowView.findViewById(R.id.textTeamRosterRight);
        TextView textProg = rowView.findViewById(R.id.textTeamRosterProgression);

        final String[] teamStat = values.get(position).split(",");
        textLeft.setText(teamStat[0]);
        textClass.setText(teamStat[1]);
        textCenter.setText(teamStat[2] + " " + teamStat[3]);
        textRight.setText(teamStat[4]);

        if (teamStat[0].equals(" ")) {
            textCenter.setTypeface(textCenter.getTypeface(), Typeface.BOLD);
            textCenter.setTextColor(Color.parseColor("#5994de"));
        }
        if (teamStat[3].equals("*")) {
            textCenter.setTypeface(textCenter.getTypeface(), Typeface.BOLD);
            textCenter.setTextColor(Color.WHITE);
        }
        if (teamStat[3].contains("RS") || teamStat[3].contains("[T]")) {
            textCenter.setTypeface(textCenter.getTypeface(), Typeface.BOLD);
            textCenter.setTextColor(Color.DKGRAY);
        }
        if (teamStat[3].contains("Suspended")) {
            textCenter.setTypeface(textCenter.getTypeface(), Typeface.BOLD);
            textCenter.setTextColor(Color.RED);
        }
        if (teamStat[3].contains("INJ")) {
            textCenter.setTypeface(textCenter.getTypeface(), Typeface.BOLD);
            textCenter.setTextColor(Color.YELLOW);
        }
        if (teamStat[3].contains("Hot Seat")) {
            textCenter.setTypeface(textCenter.getTypeface(), Typeface.BOLD);
            textCenter.setTextColor(Color.RED);
        }

        if(!teamStat[4].contains(" ")) {
            if (Integer.parseInt(teamStat[4]) > 90) {
                textRight.setTextColor(Color.parseColor("#5994de"));
            } else if (Integer.parseInt(teamStat[4]) > 80) {
                textRight.setTextColor(Color.parseColor("#8FBC8F"));
            }
        }

        if(teamStat.length > 5) {
            if (teamStat[5].equals("1")) {
                textCenter.setTypeface(textCenter.getTypeface(), Typeface.BOLD);
                textCenter.setTextColor(Color.parseColor("#8FBC8F"));
                teamStat[3] = " :  All-Fr";
                textCenter.setText(teamStat[2] + " " + teamStat[3]);
            } else if (teamStat[5].equals("2")) {
                textCenter.setTypeface(textCenter.getTypeface(), Typeface.BOLD);
                textCenter.setTextColor(Color.parseColor("#00B300"));
                teamStat[3] = " :  All-Conf";
                textCenter.setText(teamStat[2] + " " + teamStat[3]);
            } else if (teamStat[5].equals("3")) {
                textCenter.setTypeface(textCenter.getTypeface(), Typeface.BOLD);
                textCenter.setTextColor(Color.parseColor("#1A75FF"));
                teamStat[3] = " :  All-Am";
                textCenter.setText(teamStat[2] + " " + teamStat[3]);
            } else if (teamStat[5].equals("4")) {
                textCenter.setTypeface(textCenter.getTypeface(), Typeface.BOLD);
                textCenter.setTextColor(Color.parseColor("#FF9933"));
                if(teamStat[0].contains("HC")) teamStat[3] = " :  COTY";
                else teamStat[3] = " :  POTY";
                textCenter.setText(teamStat[2] + " " + teamStat[3]);
            }
        }

        if(week > 17 && week < 22 && teamStat[1].contains("Sr")) {
            textCenter.setTypeface(textCenter.getTypeface(), Typeface.ITALIC);
            textCenter.setTextColor(Color.DKGRAY);
            textClass.setTypeface(textCenter.getTypeface(), Typeface.ITALIC);
            textClass.setTextColor(Color.DKGRAY);
        }

        if(week > 5 && week < 8 && teamStat.length > 6 || week > 21 && teamStat.length > 6  ) {
            textProg.setText(teamStat[6]);
        }

        textCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainAct.examinePlayer(teamStat[2]);
            }
        });

        return rowView;
    }

}

