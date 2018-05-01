package antdroid.cfbcoach;

/*
  Created by Achi Jones on 3/29/2016.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

class HallOfFameListArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;
    private final String userTeam;

    public HallOfFameListArrayAdapter(Context context, String[] values, String userTeam) {
        super(context, R.layout.hall_fame_list_item, values);
        this.context = context;
        this.values = values;
        this.userTeam = userTeam;
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
        if (hof.length > 1) {
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
        }

        return rowView;
    }
}
