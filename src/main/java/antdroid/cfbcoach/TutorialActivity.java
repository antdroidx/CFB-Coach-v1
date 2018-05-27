package antdroid.cfbcoach;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

public class TutorialActivity extends AppCompatActivity {

    private Spinner tutorialSpinner;
    private TextView tutorialTitle;
    private TextView tutorialContent;
    private ArrayList<String> titles;
    private ArrayList<String> contents;
    private ArrayAdapter dataAdapterTutorial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        tutorialSpinner = findViewById(R.id.tutorialSpinner);
        tutorialTitle = findViewById(R.id.tutorialTitle);
        tutorialContent = findViewById(R.id.tutorialContent);

        titles = new ArrayList<>();
        contents = new ArrayList<>();

        titles.add("Basics");
        contents.add(getString(R.string.tutBasics));

        titles.add("Playing a Season");
        contents.add(getString(R.string.tutPlayingSeason));

        titles.add("Rankings");
        contents.add(getString(R.string.tutRankings));

        titles.add("Roster");
        contents.add(getString(R.string.tutRoster));

        titles.add("Team Strategies");
        contents.add(getString(R.string.tutTeamStrategy));

        titles.add("Settings Menu & Built-In Game Mods");
        contents.add(getString(R.string.tutSettings));

        titles.add("User Customization");
        contents.add(getString(R.string.tutCustomizations));

        titles.add("Customization Example");
        contents.add(getString(R.string.tutExampleCustom));

        dataAdapterTutorial = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, titles);
        dataAdapterTutorial.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        tutorialSpinner.setAdapter(dataAdapterTutorial);
        tutorialSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        tutorialTitle.setText(titles.get(position));
                        tutorialContent.setText(contents.get(position));
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        //heh
                    }
                });

    }

    @Override
    public void onBackPressed() {
        finish();
        Intent myIntent = new Intent(TutorialActivity.this, Home.class);
        TutorialActivity.this.startActivity(myIntent);
    }

}
