package antdroid.cfbcoach;

/*
  Created by Achi Jones on 4/17/2016.
 */

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

class TeamHistoryList extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;

    public TeamHistoryList(Context context, String[] values) {
        super(context, R.layout.team_history_list_item, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.team_history_list_item, parent, false);
        TextView textTop = rowView.findViewById(R.id.textViewTeamHistoryTitle);
        TextView textBottom = rowView.findViewById(R.id.textViewTeamHistoryDetail);

        String[] teamHist = values[position].split(">");
        if (teamHist.length > 1) {
            textTop.setText(teamHist[0]);
            String[] yearSplit = teamHist[0].split(" ");
            boolean wonNC = false, wonCC = false, wonB = false;
            for (String s : yearSplit) {
                if (s.equals("NCW")) wonNC = true;
                else if (s.equals("CC")) wonCC = true;
                else if (s.equals("BW")) wonB = true;
            }

            if (wonNC) textTop.setTextColor(Color.parseColor("#FF9933"));
            else if (wonCC) textTop.setTextColor(Color.parseColor("#00B300"));
            else if (wonB) textTop.setTextColor(Color.parseColor("#5994de"));

            String detail = "";
            for (int i = 1; i < teamHist.length; ++i) {
                detail += teamHist[i];
                if (i != teamHist.length - 1) detail += "\n";
            }
            textBottom.setText(detail);
        } else {
            textTop.setText(values[position]);

            String[] yearSplit = values[position].split(" ");
            boolean wonNC = false, wonCC = false, wonB = false;
            for (String s : yearSplit) {
                if (s.equals("NCW")) wonNC = true;
                else if (s.equals("CC")) wonCC = true;
                else if (s.equals("BW")) wonB = true;
            }

            if (wonNC) textTop.setTextColor(Color.parseColor("#FF9933"));
            else if (wonCC) textTop.setTextColor(Color.parseColor("#00B300"));
            else if (wonB) textTop.setTextColor(Color.parseColor("#5994de"));

            textBottom.setVisibility(View.GONE);
        }

        return rowView;
    }
}
