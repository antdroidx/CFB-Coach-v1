package antdroid.cfbcoach;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import simulation.League;

public class CoachDatabase  extends ArrayAdapter<String> {
    private final Context context;
    private final ArrayList<String> values;
    private String userHC;
    private final MainActivity mainAct;


    public CoachDatabase(Context context, ArrayList<String> values, String userHC, MainActivity mainAct) {
        super(context, R.layout.team_rankings_list_item, values);
        this.context = context;
        this.values = values;
        this.userHC = userHC;
        this.mainAct = mainAct;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.team_rankings_list_item, parent, false);
        TextView textLeft = rowView.findViewById(R.id.textTeamRankingsLeft);
        TextView textCenter = rowView.findViewById(R.id.textTeamRankingsCenter);
        TextView textRight = rowView.findViewById(R.id.textTeamRankingsRight);


        final String[] teamStat = values.get(position).split(",");
        textLeft.setText(teamStat[0]);
        textCenter.setText(teamStat[1]);
        textRight.setText(teamStat[2]);

        if (teamStat[1].equals(userHC)) {
            // Bold user team
            textLeft.setTypeface(textLeft.getTypeface(), Typeface.BOLD);
            textLeft.setTextColor(Color.parseColor("#5994de"));
            textCenter.setTypeface(textCenter.getTypeface(), Typeface.BOLD);
            textCenter.setTextColor(Color.parseColor("#5994de"));
            textRight.setTypeface(textRight.getTypeface(), Typeface.BOLD);
            textRight.setTextColor(Color.parseColor("#5994de"));
        }
        if (teamStat[2].split(" ").length > 1 && teamStat[2].split(" ")[2].contains("+")) {
            // Highlight Prestige Changes in off-season
            textRight.setTextColor(Color.GREEN);
        } else if (teamStat[2].split(" ").length > 1 && teamStat[2].split(" ")[2].contains("-")) {
            textRight.setTextColor(Color.RED);
        }


        textCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainAct.examineCoachDB(teamStat[1]);
            }
        });


        return rowView;
    }



    public void setupUserHC(String userHC) {
        this.userHC = userHC;
    }
}
