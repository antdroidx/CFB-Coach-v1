package antdroid.cfbcoach;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

class SaveFilesListArrayAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final String[] values;

    public SaveFilesListArrayAdapter(Context context, String[] values) {
        super(context, R.layout.child_player_stats, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.child_player_stats, parent, false);

        Button butt = rowView.findViewById(R.id.buttonPlayerStatsViewAll);
        butt.setVisibility(View.GONE);

        String[] detailSplit = values[position].split(">");
        TextView itemL = rowView.findViewById(R.id.textPlayerStatsLeftChild);
        itemL.setPadding(5, 0, 5, 0);
        itemL.setText(detailSplit[0]);

        return rowView;
    }
}
