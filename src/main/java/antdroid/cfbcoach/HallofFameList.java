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

class HallofFameList extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;
    private final String userTeam;
    private final boolean team;

    public HallofFameList(Context context, String[] values, String userTeam, boolean team) {
        super(context, R.layout.hall_fame_list_item, values);
        this.context = context;
        this.values = values;
        this.userTeam = userTeam;
        this.team = team;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.hall_fame_list_item, parent, false);
        TextView textTop = rowView.findViewById(R.id.textViewHallFameName);
        TextView textLeft = rowView.findViewById(R.id.textViewHallFameLeft);
        TextView textRight = rowView.findViewById(R.id.textViewHallFameRight);

        String[] hof = values[position].split("&");

        String[] HOFentry = hof[0].split(":");
        String[] HOFline = hof[0].split(" ");
        String[] HOFyear= values[position].split("Yrs:");
        String[] HOFyear2= HOFyear[1].split("&");
        String[] HOFawards= values[position].split("Awards:");
        String[] HOFawards2= HOFawards[1].split(">");
        String entry = HOFline[1] + " " + HOFline[2] + " " + HOFline[3] + ", " + HOFentry[0] + ", " + HOFyear2[0] + "\n" + HOFawards2[0];
        String entryTeam = HOFline[1] + " " + HOFline[2] + " " + HOFline[3] + ", " + HOFyear2[0] + "\n" + HOFawards2[0];

/*        if (hof.length > 1) {
            textTop.setText(hof[0]);
            if (hof[0].split(":")[0].equals(userTeam)) {
                textTop.setTextColor(Color.parseColor("#5994de"));
            }
            StringBuilder left = new StringBuilder();
            StringBuilder right = new StringBuilder();
            for (int i = 1; i < hof.length; ++i) {
                String[] split = hof[i].split(">");
                left.append(split[0]);
                if (i != hof.length - 1) left.append("\n");
                if (split.length > 1) {
                    right.append(split[1]);
                    if (i != hof.length - 1) right.append("\n");
                }
            }
            textLeft.setText(left.toString());
            textRight.setText(right.toString());
        }*/

        if (hof.length > 1) {

            if(team) textTop.setText(entryTeam);
            else textTop.setText(entry);

            if (hof[0].split(":")[0].equals(userTeam)) {
                textTop.setTextColor(Color.parseColor("#5994de"));
            }

        }

        return rowView;
    }
}
