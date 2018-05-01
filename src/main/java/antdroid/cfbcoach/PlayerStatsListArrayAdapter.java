package antdroid.cfbcoach;

/*
  Created by Achi Jones on 2/20/2016.
 */

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

class PlayerStatsListArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;

    public PlayerStatsListArrayAdapter(Context context, String[] values) {
        super(context, R.layout.child_player_stats, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.child_player_stats, parent, false);

        String[] detailSplit = values[position].split(">");
        if (detailSplit.length == 2) {
            TextView itemL = rowView.findViewById(R.id.textPlayerStatsLeftChild);
            TextView itemR = rowView.findViewById(R.id.textPlayerStatsRightChild);

            if (values[position].substring(0, 3).equals("[I]")) {
                // Injury, make red
                itemL.setText(detailSplit[0].substring(3));
                itemR.setText(detailSplit[1]);
                itemL.setTextColor(Color.RED);
                itemR.setTextColor(Color.RED);
            } else {
                itemL.setText(detailSplit[0]);
                itemR.setText(detailSplit[1]);
                colorizeRatings(itemL, detailSplit[0]);
                colorizeRatings(itemR, detailSplit[1]);
            }

            TextView itemC = rowView.findViewById(R.id.textPlayerStatsCenter);
            itemC.setText("");
        } else {
            // Only one, center it
            TextView itemC = rowView.findViewById(R.id.textPlayerStatsCenter);
            if (values[position].substring(0, 3).equals("[B]")) {
                // Bold it
                itemC.setText(values[position].substring(3));
                itemC.setTypeface(null, Typeface.BOLD);
            } else if (values[position].substring(0, 3).equals("[I]")) {
                // Red it for injury
                itemC.setText(values[position].substring(3));
                itemC.setTextColor(Color.RED);
            } else {
                itemC.setText(values[position]);
            }

            TextView itemL = rowView.findViewById(R.id.textPlayerStatsLeftChild);
            itemL.setVisibility(View.GONE);
            TextView itemR = rowView.findViewById(R.id.textPlayerStatsRightChild);
            itemR.setVisibility(View.GONE);
        }

        Button butt = rowView.findViewById(R.id.buttonPlayerStatsViewAll);
        butt.setVisibility(View.GONE);

        return rowView;
    }

    private void colorizeRatings(TextView textV, String rating) {
        String[] ratSplit = rating.split(" ");
        // The last index is always the rating: A+, C, etc
        if (ratSplit.length > 0 && rating.split(",").length == 1) {
            String letter = ratSplit[ratSplit.length - 1];
            if (letter.equals("A") || letter.equals("A+")) {
                textV.setTextColor(Color.parseColor("#5994de"));
            } else if (letter.equals("B") || letter.equals("B+")) {
                textV.setTextColor(Color.parseColor("#00b300"));
            } else if (letter.equals("C") || letter.equals("C+")) {
                textV.setTextColor(Color.YELLOW);
            } else if (letter.equals("D") || letter.equals("D+")) {
                textV.setTextColor(Color.parseColor("#e68a00"));
            } else if (letter.equals("F") || letter.equals("F+")) {
                textV.setTextColor(Color.RED);
            }
        }
    }
}
