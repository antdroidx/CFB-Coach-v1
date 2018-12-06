package antdroid.cfbcoach;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Achi on 3/1/2016.
 */
class NewsStories extends ArrayAdapter<String> {
    private final Context context;
    private final ArrayList<String> values;

    public NewsStories(Context context, ArrayList<String> values) {
        super(context, R.layout.news_story_list_item, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.news_story_list_item, parent, false);
        TextView newsTitle = rowView.findViewById(R.id.textNewsTitle);
        TextView newsContent = rowView.findViewById(R.id.textNewsContent);

        String[] story = values.get(position).split(">");
        newsTitle.setText(story[0]);
        newsContent.setText(story[1]);

        return rowView;
    }
}
