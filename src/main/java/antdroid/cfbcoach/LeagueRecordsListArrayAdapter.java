package antdroid.cfbcoach;

/*
  Created by Achi Jones on 3/29/2016.
 */

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

class LeagueRecordsListArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;
    private final String userTeamAbbr;
    private final String userTeamName;

    public LeagueRecordsListArrayAdapter(Context context, String[] values, String userTeamAbbr, String userTeamName) {
        super(context, R.layout.league_record_list_item, values);
        this.context = context;
        this.values = values;
        this.userTeamAbbr = userTeamAbbr;
        this.userTeamName = userTeamName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.league_record_list_item, parent, false);
        TextView textLeft = rowView.findViewById(R.id.textLeagueRecordLeft);
        TextView textCenter = rowView.findViewById(R.id.textLeagueRecordCenter);
        TextView textRight = rowView.findViewById(R.id.textLeagueRecordRight);

        String[] record = values[position].split(",");
        if (record[1].equals("-1")) {
            textLeft.setText("");
            textCenter.setText(record[0]);
            textCenter.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            textRight.setText("");
        } else if (!record[2].equals("XXX")) {
            // Only show record if it exists
            textLeft.setText(record[1]);
            textCenter.setText(record[0]);
            if (record[2].contains("%")) {
                String namesplit[] = record[2].split("%");
                textRight.setText(record[2].split("%")[0] + "\n" + record[2].split("%")[1] + " " + record[3]);
                if (record[2].split("%")[1].equals(userTeamAbbr) || record[2].split("%")[1].equals(userTeamName)) {
                    // User team record, make it special color
                    textRight.setTextColor(Color.parseColor("#5994de"));
                }
            } else {
                textRight.setText(record[2] + "\n" + record[3]);
                if (record[2].split(" ")[0].equals(userTeamAbbr) || record[2].split(" ")[0].equals(userTeamName)) {
                    // User team record, make it special color
                    textRight.setTextColor(Color.parseColor("#5994de"));
                }
            }
        }

        return rowView;
    }
}
