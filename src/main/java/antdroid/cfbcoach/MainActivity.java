package antdroid.cfbcoach;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import positions.HeadCoach;
import positions.Player;
import positions.PlayerCB;
import positions.PlayerDL;
import positions.PlayerK;
import positions.PlayerLB;
import positions.PlayerOL;
import positions.PlayerQB;
import positions.PlayerRB;
import positions.PlayerS;
import positions.PlayerTE;
import positions.PlayerWR;
import recruiting.RecruitingActivity;
import simulation.Conference;
import simulation.Game;
import simulation.League;
import simulation.PlaybookDefense;
import simulation.PlaybookOffense;
import simulation.Team;

public class MainActivity extends AppCompatActivity {
    private static final int READ_REQUEST_CODE = 43;
    private String oldConf;
    private HeadCoach userHC;
    private int season;
    private League simLeague;
    private Conference currentConference;
    private Team currentTeam;
    private int currentConferenceID;
    private Team userTeam;
    private File saveLeagueFile;
    private String username;
    private Uri dataUri;
    private String loadData;
    private String goals;

    private List<String> teamList;
    private List<String> confList;

    private int currTab;
    private String userTeamStr;
    private Spinner examineTeamSpinner;
    private ArrayAdapter<String> dataAdapterTeam;
    private Spinner examineConfSpinner;
    private ArrayAdapter<String> dataAdapterConf;
    private ListView mainList;
    private ExpandableListView expListPlayerStats;

    //recruiting
    private boolean wantUpdateConf;
    private boolean showToasts;
    private boolean showInjuryReport;
    private boolean redshirtComplete;
    private boolean budgetsComplete;

    //Universe Settings
    private final int teamsStart = 12;
    private final int confStart = 10;
    private final int seasonStart = 2017;

    String saveLeagueFileStr;
    private File customConfs;
    private File customTeams;
    private File customBowls;
    private String customUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Set up list
        mainList = findViewById(R.id.mainList);
        expListPlayerStats = findViewById(R.id.playerStatsExpandList);

        //LOAD GAME DATA
        //SAVE OR NO SAVE
        Bundle extras = getIntent().getExtras();
        boolean loadedLeague = false;
        if (extras != null) {
            String saveFileStr = extras.getString("SAVE_FILE");

            //NEW DYNASTY GAME
            if (saveFileStr.contains("NEW_LEAGUE_DYNASTY")) {
                //NEW DYNASTY GAME WITH CUSTOM DATABASE
                if (saveFileStr.contains("CUSTOM")) {
                    String[] filesSplit = saveFileStr.split(",");
                    this.customUri = filesSplit[1];
                    customConfs = new File(getFilesDir(), "conferences.txt");
                    customTeams = new File(getFilesDir(), "teams.txt");
                    customBowls = new File(getFilesDir(), "bowls.txt");
                    Uri uri = Uri.parse(customUri);
                    customLeague(uri);
                    simLeague = new League(getString(R.string.league_player_names), getString(R.string.league_last_names), false, customConfs, customTeams, customBowls);
                    season = seasonStart;
                    //NEW DYNASTY DEFAULT DATABASE
                } else {
                    simLeague = new League(getString(R.string.league_player_names), getString(R.string.league_last_names), getString(R.string.conferences), getString(R.string.teams), getString(R.string.bowls), false);
                    season = seasonStart;
                }
                //NEW CAREER GAME
            } else if (saveFileStr.contains("NEW_LEAGUE_CAREER")) {
                //NEW CAREER GAME WITH CUSTOM DATABASE
                if (saveFileStr.contains("CUSTOM")) {
                    String[] filesSplit = saveFileStr.split(",");
                    this.customUri = filesSplit[1];
                    customConfs = new File(getFilesDir(), "conferences.txt");
                    customTeams = new File(getFilesDir(), "teams.txt");
                    customBowls = new File(getFilesDir(), "bowls.txt");
                    Uri uri = Uri.parse(customUri);
                    customLeague(uri);
                    simLeague = new League(getString(R.string.league_player_names), getString(R.string.league_last_names), true, customConfs, customTeams, customBowls);
                    season = seasonStart;
                    //NEW CAREER GAME WITH DEFAULT DATABASE
                } else {
                    simLeague = new League(getString(R.string.league_player_names), getString(R.string.league_last_names), getString(R.string.conferences), getString(R.string.teams), getString(R.string.bowls), true);
                    season = seasonStart;
                }
                //LOADING A CURRENT GAME AFTER RECRUITING PERIOD
            } else if (saveFileStr.equals("DONE_RECRUITING")) {
                File saveFile = new File(getFilesDir(), "saveLeagueRecruiting.cfb");
                if (saveFile.exists()) {
                    simLeague = new League(saveFile, getString(R.string.league_player_names), getString(R.string.league_last_names));
                    userTeam = simLeague.userTeam;
                    userTeamStr = userTeam.name;
                    userTeam.recruitPlayersFromStr(extras.getString("RECRUITS"));
                    simLeague.updateTeamTalentRatings();
                    season = simLeague.getYear();
                    currentTeam = userTeam;
                    loadedLeague = true;
                }
                //LOADS A SAVE GAME
            } else {
                File saveFile = new File(getFilesDir(), saveFileStr);
                if (saveFile.exists()) {
                    simLeague = new League(saveFile, getString(R.string.league_player_names), getString(R.string.league_last_names));
                    userTeam = simLeague.userTeam;
                    userTeamStr = userTeam.name;
                    simLeague.updateTeamTalentRatings();
                    season = simLeague.getYear();
                    currentTeam = userTeam;
                    loadedLeague = true;
                    //STARTS A NEW GAME -- NOT SURE WHY THIS EXISTS
                }
            }
        } else {
            //STARTS A NEW GAME WITH NO EXTRAS - NOT USED CURRENTLY
            simLeague = new League(getString(R.string.league_player_names), getString(R.string.league_last_names), getString(R.string.conferences), getString(R.string.teams), getString(R.string.bowls), false);
            season = seasonStart;
        }

        wantUpdateConf = true; // 0 and 1, don't update, 2 update
        showToasts = true;
        showInjuryReport = true;

        if (!loadedLeague) {
            // Set it to 1st team until one selected
            userTeam = simLeague.teamList.get(0);
            simLeague.userTeam = userTeam;
            userTeam.userControlled = true;
            userTeamStr = userTeam.name;
            currentTeam = userTeam;
            currentTeam = simLeague.teamList.get(0);
            currentConference = simLeague.conferences.get(0);

            if (simLeague.isCareerMode()) careerModeOptions();
            else newGameDialog();
        }

        // Set toolbar text
        updateHeaderBar();

        //Team Name Display
        final TextView currentTeamText = findViewById(R.id.currentTeamText);
        currentTeamText.setText(currentTeam.name + " (" + currentTeam.wins + "-" + currentTeam.losses + ")");

        //Set up spinner for examining team.
        examineConfSpinner = findViewById(R.id.examineConfSpinner);
        confList = new ArrayList<>();
        for (int i = 0; i < confStart; i++) {
            confList.add(simLeague.conferences.get(i).confName);
        }
        dataAdapterConf = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, confList);
        dataAdapterConf.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        examineConfSpinner.setAdapter(dataAdapterConf);
        examineConfSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        currentConference = simLeague.findConference(parent.getItemAtPosition(position).toString());
                        currentConferenceID = position;
                        updateCurrConference();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        //heh
                    }
                });

        examineTeamSpinner = findViewById(R.id.examineTeamSpinner);
        teamList = new ArrayList<>();
        for (int i = 0; i < teamsStart; i++) {
            teamList.add(simLeague.teamList.get(i).strRep());
        }

        dataAdapterTeam = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, teamList);
        dataAdapterTeam.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        examineTeamSpinner.setAdapter(dataAdapterTeam);
        examineTeamSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        currentTeam = simLeague.findTeam(parent.getItemAtPosition(position).toString());
                        updateCurrTeam();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });


        //Home Button
        final Button homeButton = findViewById(R.id.buttonHome);
        homeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                currentTeam = userTeam;
                examineTeam(currentTeam.name);
            }
        });

        //News Page Button
        final Button newsButton = findViewById(R.id.buttonNews);
        newsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                currTab = 1;
                showNewsStoriesDialog();
            }
        });

        //Team Statistics Button
        final Button teamStatsButton = findViewById(R.id.teamStatsButton);
        teamStatsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                currTab = 2;
                updateTeamStats();
            }
        });

        //Roster Page Button
        final Button playerStatsButton = findViewById(R.id.playerStatsButton);
        playerStatsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                currTab = 3;
                updatePlayerStats();
            }
        });

        //Team Schedule Button
        final Button teamScheduleButton = findViewById(R.id.teamScheduleButton);
        teamScheduleButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                currTab = 4;
                updateSchedule();
            }
        });

        //Weekly Scoreboard Button
        final Button scoresButton = findViewById(R.id.buttonScores);
        scoresButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                currTab = 5;
                showWeeklyScores();
            }
        });

        //Conf Standings Button
        final Button standingsButton = findViewById(R.id.standingsButton);
        standingsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                currTab = 6;
                updateStandings();
            }
        });

        //Poll Rankings Button
        final Button rankingsButton = findViewById(R.id.rankingsButton);
        rankingsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                currTab = 7;
                updateRankings();
            }
        });

        //Team Depth Chart Button
        final Button depthchartButton = findViewById(R.id.buttonDepthChart);
        depthchartButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                currentTeam = userTeam;
                showTeamLineupDialog();
            }
        });

        //Strategy/Playbook
        final Button strategyButton = findViewById(R.id.buttonStrategy);
        strategyButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                currentTeam = userTeam;
                showTeamStrategyDialog();
            }
        });

        //Simulate Week Button
        final Button simGameButton = findViewById(R.id.simGameButton);
        simGameButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                simulateWeek();
            }
        });


        if (loadedLeague) {
            // Set rankings so that not everyone is rank #0
            simLeague.setTeamRanks();
            simLeague.preseasonNews();
            examineTeam(userTeam.name);
            showToasts = userTeam.showPopups;
        }

        if (simLeague.getYear() != seasonStart) {
            // Only show recruiting classes if it aint 2017
            showRecruitingClassDialog();
        }
    }

    //Update Header Bar
    private void updateHeaderBar() {
        if (simLeague.isCareerMode()) {
            getSupportActionBar().setTitle(season + " | " + userTeam.name + " | Career Mode");
        } else {
            getSupportActionBar().setTitle(season + " | " + userTeam.name + " | Dynasty Mode");
        }
    }

    private void newGameHardDialog() {
        //rank teams within conferences
        simLeague.updateTeamTalentRatings();
        //choose last 3 teams in each conference
        final ArrayList<Team> coachList = simLeague.getCoachList();
        final ArrayList<String> teamsArray = simLeague.getCoachListStr();
        final String[] teams = teamsArray.toArray(new String[teamsArray.size()]);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Job Offers Available:");
        builder.setItems(teams, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                userTeam.userControlled = false;
                simLeague.newJobtransfer(coachList.get(item).name);
                userTeam = simLeague.userTeam;
                userTeam.userControlled = true;
                userTeamStr = userTeam.name;
                currentTeam = userTeam;
                userNameDialog();
                userTeam.setupUserCoach(username);
                // set rankings so that not everyone is rank #0
                simLeague.setTeamRanks();
                simLeague.setTeamBenchMarks();
                simLeague.updateTeamTalentRatings();
                simLeague.preseasonNews();
                userHC = userTeam.HC.get(0);
                // Set toolbar text to '2017 Season' etc
                updateHeaderBar();
                examineTeam(currentTeam.name);
            }
        });
        builder.setCancelable(false);
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void newGameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose your team:");
        final String[] teams = simLeague.getTeamListStr();
        builder.setItems(teams, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                userTeam.userControlled = false;
                userTeam = simLeague.teamList.get(item);
                simLeague.userTeam = userTeam;
                userTeam.userControlled = true;
                userTeamStr = userTeam.name;
                currentTeam = userTeam;
                userNameDialog();
                userTeam.setupUserCoach(username);
                // set rankings so that not everyone is rank #0
                simLeague.setTeamRanks();
                simLeague.setTeamBenchMarks();
                simLeague.updateTeamTalentRatings();
                simLeague.preseasonNews();
                userHC = userTeam.HC.get(0);
                // Set toolbar text to '2017 Season' etc
                updateHeaderBar();
                examineTeam(currentTeam.name);
            }
        });
        builder.setCancelable(false);
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void userNameDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("The Coach Name is required to have a first and a last name.");
        builder.setTitle("Enter Name:")
                .setView(getLayoutInflater().inflate(R.layout.username_dialog, null));
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        dialog.show();

        final EditText changeHCEditText = dialog.findViewById(R.id.editTextChangeHC);
        changeHCEditText.setText(simLeague.getRandName());   //change Head Coach Name

        final TextView invalidHCText = dialog.findViewById(R.id.textViewChangeHC);

        changeHCEditText.addTextChangedListener(new TextWatcher() {
            String newHC;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                newHC = s.toString().trim();
                if (!simLeague.isNameValid(newHC)) {
                    invalidHCText.setText("Name already in use or has illegal characters!");
                } else {
                    invalidHCText.setText("");
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                newHC = s.toString().trim();
                if (!simLeague.isNameValid(newHC)) {
                    invalidHCText.setText("Name already in use or has illegal characters!");
                } else {
                    invalidHCText.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                newHC = s.toString().trim();
                if (!simLeague.isNameValid(newHC)) {
                    invalidHCText.setText("Name already in use or has illegal characters!");
                } else {
                    invalidHCText.setText("");
                }
            }

        });

        Button okChangeNameButton = dialog.findViewById(R.id.buttonOkChangeName);


        okChangeNameButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                String newHC = changeHCEditText.getText().toString().trim();
                if (isNameValid((newHC))) {
                    userTeam.HC.get(0).name = newHC;
                    examineTeam(currentTeam.name);
                    seasonGoals();
                    dialog.dismiss();
                } else {
                    if (showToasts)
                        Toast.makeText(MainActivity.this, "Invalid name/abbr! Name not changed.",
                                Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void resetTeamUI() {
        currentTeam = userTeam;
        examineTeam(userTeam.name);
    }

    public void examineTeam(String teamName) {
        wantUpdateConf = false;
        // Find team
        Team tempT = simLeague.teamList.get(0);
        for (Team t : simLeague.teamList) {
            if (t.name.equals(teamName)) {
                currentTeam = t;
                tempT = t;
                break;
            }
        }
        // Find conference
        for (int i = 0; i < simLeague.conferences.size(); ++i) {
            Conference c = simLeague.conferences.get(i);
            if (c.confName.equals(currentTeam.conference)) {
                if (c == currentConference) wantUpdateConf = true;
                currentConference = c;
                examineConfSpinner.setSelection(i);
                break;
            }
        }

        teamList = new ArrayList<>();
        dataAdapterTeam.clear();
        for (int i = 0; i < currentConference.confTeams.size(); i++) {
            teamList.add(currentConference.confTeams.get(i).strRep());
            dataAdapterTeam.add(teamList.get(i));
        }
        dataAdapterTeam.notifyDataSetChanged();

        for (int i = 0; i < currentConference.confTeams.size(); ++i) {
            String[] spinnerSplit = dataAdapterTeam.getItem(i).split(" ");
            if (spinnerSplit.length == 2 && spinnerSplit[1].equals(tempT.name)) {
                examineTeamSpinner.setSelection(i);
                currentTeam = tempT;
                TextView currentTeamText = findViewById(R.id.currentTeamText);
                currentTeamText.setText("#" + currentTeam.rankTeamPollScore +
                        " " + currentTeam.name + " (" + currentTeam.wins + "-" + currentTeam.losses + ") " +
                        currentTeam.confChampion + " " + currentTeam.semiFinalWL + currentTeam.natChampWL);
                break;
            } else if (spinnerSplit.length == 3 && (spinnerSplit[1] + " " + spinnerSplit[2]).toString().equals(tempT.name)) {
                examineTeamSpinner.setSelection(i);
                currentTeam = tempT;
                TextView currentTeamText = findViewById(R.id.currentTeamText);
                currentTeamText.setText("#" + currentTeam.rankTeamPollScore +
                        " " + currentTeam.name + " (" + currentTeam.wins + "-" + currentTeam.losses + ") " +
                        currentTeam.confChampion + " " + currentTeam.semiFinalWL + currentTeam.natChampWL);
                break;
            } else if (spinnerSplit.length == 4 && (spinnerSplit[1] + " " + spinnerSplit[2] + " " + spinnerSplit[3]).toString().equals(tempT.name)) {
                examineTeamSpinner.setSelection(i);
                currentTeam = tempT;
                TextView currentTeamText = findViewById(R.id.currentTeamText);
                currentTeamText.setText("#" + currentTeam.rankTeamPollScore +
                        " " + currentTeam.name + " (" + currentTeam.wins + "-" + currentTeam.losses + ") " +
                        currentTeam.confChampion + " " + currentTeam.semiFinalWL + currentTeam.natChampWL);
                break;
            }
        }

        if (currTab == 4) {
            currTab = 4;
            updateSchedule();
        } else if (currTab == 2) {
            currTab = 2;
            updateTeamStats();
        } else {
            currTab = 3;
            updatePlayerStats();
        }

    }

    private void updateCurrTeam() {
        teamList = new ArrayList<>();
        dataAdapterTeam.clear();
        for (int i = 0; i < teamsStart; i++) {
            teamList.add(currentConference.confTeams.get(i).strRep());
            dataAdapterTeam.add(teamList.get(i));
        }
        dataAdapterTeam.notifyDataSetChanged();
        TextView currentTeamText = findViewById(R.id.currentTeamText);
        currentTeamText.setText("#" + currentTeam.rankTeamPollScore +
                " " + currentTeam.name + " (" + currentTeam.wins + "-" + currentTeam.losses + ") " +
                currentTeam.confChampion + " " + currentTeam.semiFinalWL + currentTeam.natChampWL);

        if (currTab == 4) {
            currTab = 4;
            updateSchedule();
        } else if (currTab == 2) {
            currTab = 2;
            updateTeamStats();
        } else {
            currTab = 3;
            updatePlayerStats();
        }

    }

    private void updateCurrConference() {
        confList.clear();
        for (int i = 0; i < confStart; i++) {
            confList.add(simLeague.conferences.get(i).confName);
        }
        dataAdapterConf.notifyDataSetChanged();

        if (wantUpdateConf) {
            teamList = new ArrayList<>();
            dataAdapterTeam.clear();
            for (int i = 0; i < teamsStart; i++) {
                teamList.add(currentConference.confTeams.get(i).strRep());
                dataAdapterTeam.add(teamList.get(i));
            }
            dataAdapterTeam.notifyDataSetChanged();
            examineTeamSpinner.setSelection(0);
            currentTeam = currentConference.confTeams.get(0);
            updateCurrTeam();
        } else {
            wantUpdateConf = true;
        }
    }

    private void scrollToLatestGame() {
        if (currTab == 4 && simLeague.currentWeek > 2) {
            mainList.setSelection(currentTeam.numGames() - 3);
        }

    }

    //MAIN SCREEN BUTTONS

    //News Display
    private void showNewsStoriesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("News Stories")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing?
                    }
                })
                .setView(getLayoutInflater().inflate(R.layout.team_rankings_dialog, null));
        AlertDialog dialog = builder.create();
        dialog.show();

        ArrayList<String> rankings = new ArrayList<>();// = simLeague.getTeamRankingsStr(0);
        String[] weekSelection = new String[simLeague.currentWeek + 1];
        for (int i = 0; i < weekSelection.length; ++i) {
            if (i == 0) weekSelection[i] = "Pre-Season News";
            else if (i == 13) weekSelection[i] = "Conf Champ Week";
            else if (i == 14) weekSelection[i] = "Bowl Game Week";
            else if (i == 15) weekSelection[i] = "National Champ";
            else if (i == 16) weekSelection[i] = "Off-Season News";
            else if (i == 17) weekSelection[i] = "Coaching Contracts";
            else if (i == 18) weekSelection[i] = "Off-Season News";
            else if (i == 19) weekSelection[i] = "Coach Hirings";
            else if (i == 20) weekSelection[i] = "Roster News";
            else if (i == 21) weekSelection[i] = "Transfer News";
            else if (i == 22) weekSelection[i] = "Recruiting News";
            else weekSelection[i] = "Week " + i;
        }
        Spinner weekSelectionSpinner = dialog.findViewById(R.id.spinnerTeamRankings);
        ArrayAdapter<String> weekSelectionSpinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, weekSelection);
        weekSelectionSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weekSelectionSpinner.setAdapter(weekSelectionSpinnerAdapter);
        if (simLeague.currentWeek > 16 && simLeague.currentWeek < 19) {
            weekSelectionSpinner.setSelection(simLeague.currentWeek);
        } else {
            weekSelectionSpinner.setSelection(simLeague.currentWeek);
        }

        final ListView newsStoriesList = dialog.findViewById(R.id.listViewTeamRankings);
        final NewsStoriesListArrayAdapter newsStoriesAdapter = new NewsStoriesListArrayAdapter(this, rankings);
        newsStoriesList.setAdapter(newsStoriesAdapter);

        weekSelectionSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        ArrayList<String> rankings = simLeague.newsStories.get(position);
                        boolean isempty = false;
                        if (simLeague.currentWeek == 22 && rankings.size() == 0) {
                            rankings.add("National Letter of Intention Day!>Today marks the first day of open recruitment. Teams are now allowed to sign incoming freshmen to their schools.");
                        }
                        if (rankings.size() == 0) {
                            isempty = true;
                            rankings.add("No news stories.>I guess this week was a bit boring, huh?");
                        }
                        newsStoriesAdapter.clear();
                        newsStoriesAdapter.addAll(rankings);
                        newsStoriesAdapter.notifyDataSetChanged();
                        if (isempty) {
                            rankings.remove(0);
                        }
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        // do nothing
                    }
                });
    }

    //Team Stats
    private void updateTeamStats() {
        mainList.setVisibility(View.VISIBLE);
        expListPlayerStats.setVisibility(View.GONE);

        String[] teamStatsStr = currentTeam.getTeamStatsStrCSV().split("%\n");
        mainList.setAdapter(new TeamStatsListArrayAdapter(this, teamStatsStr));
    }

    //Player Stats
    private void updatePlayerStats() {
        mainList.setVisibility(View.GONE);
        expListPlayerStats.setVisibility(View.VISIBLE);

        List<String> playerHeaders = currentTeam.getPlayerStatsExpandListStr();
        Map<String, List<String>> playerInfos = currentTeam.getPlayerStatsExpandListMap(playerHeaders);
        final ExpandableListAdapterPlayerStats expListAdapterPlayerStats =
                new ExpandableListAdapterPlayerStats(this, this, playerHeaders, playerInfos);
        expListPlayerStats.setAdapter(expListAdapterPlayerStats);

        expListPlayerStats.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(
                    ExpandableListView parent, View v,
                    int groupPosition, int childPosition,
                    long id) {
                if (expListAdapterPlayerStats.getGroup(groupPosition).equals("BENCH > BENCH")) {
                    // Bench player, examine
                    examinePlayer(expListAdapterPlayerStats.getChild(groupPosition, childPosition));
                }
                return false;
            }
        });
    }

    //Open Player Profile
    public void examinePlayer(String player) {
        Player p = currentTeam.findBenchPlayer(player);
        if (p == null) p = currentTeam.getHC(0);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        ArrayList<String> pStatsList = p.getDetailAllStatsList(currentTeam.numGames());
        if (p.injury != null) pStatsList.add(0, "[I]Injured: " + p.injury.toString());
        pStatsList.add(0, "[B]" + p.getYrOvrPot_Str());
        String[] pStatsArray = pStatsList.toArray(new String[pStatsList.size()]);
        PlayerStatsListArrayAdapter pStatsAdapter = new PlayerStatsListArrayAdapter(this, pStatsArray);
        builder.setAdapter(pStatsAdapter, null)
                .setTitle(p.position + " " + p.name)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //Player Awards for Bio
    public int checkAwardPlayer(String player) {
        Player p = currentTeam.findBenchPlayer(player);
        if (p == null) return 0;
        if (p.wonHeisman) return 3;
        if (p.wonAllAmerican) return 2;
        if (p.wonAllConference) return 1;
        return 0;
    }

    //Schedule
    private void updateSchedule() {
        mainList.setVisibility(View.VISIBLE);
        expListPlayerStats.setVisibility(View.GONE);

        Game[] games = new Game[currentTeam.gameSchedule.size()];
        for (int i = 0; i < games.length; ++i) {
            games[i] = currentTeam.gameSchedule.get(i);
        }
        mainList.setAdapter(new GameScheduleListArrayAdapter(this, this, currentTeam, games));
        mainList.setSelection(currentTeam.numGames() - 3);
    }

    //Game Summary
    public void showGameDialog(Game g) {
        final String[] gameStr;
        if (g.hasPlayed) {
            // Show game sumamry dialog
            gameStr = g.getGameSummaryStr();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle(g.awayTeam.name + " @ " + g.homeTeam.name + ": " + g.gameName)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //do nothing?
                        }
                    })
                    .setView(getLayoutInflater().inflate(R.layout.game_dialog, null));
            AlertDialog dialog = builder.create();
            dialog.show();

            // Game score
            final TextView gameAwayScore = dialog.findViewById(R.id.gameDialogScoreAway);
            final TextView gameHomeScore = dialog.findViewById(R.id.gameDialogScoreHome);
            final TextView gameAwayScoreName = dialog.findViewById(R.id.gameDialogScoreAwayName);
            final TextView gameHomeScoreName = dialog.findViewById(R.id.gameDialogScoreHomeName);
            gameAwayScore.setText(g.awayScore + "");
            gameHomeScore.setText(g.homeScore + "");
            gameAwayScoreName.setText(g.awayTeam.getStrAbbrWL_2Lines());
            gameHomeScoreName.setText(g.homeTeam.getStrAbbrWL_2Lines());


            final TextView awayTeam = dialog.findViewById(R.id.teamAway);
            final TextView awayQT1 = dialog.findViewById(R.id.awayQT1);
            final TextView awayQT2 = dialog.findViewById(R.id.awayQT2);
            final TextView awayQT3 = dialog.findViewById(R.id.awayQT3);
            final TextView awayQT4 = dialog.findViewById(R.id.awayQT4);
            final TextView awayOT = dialog.findViewById(R.id.awayOT);
            final TextView homeTeam = dialog.findViewById(R.id.teamHome);
            final TextView homeQT1 = dialog.findViewById(R.id.homeQT1);
            final TextView homeQT2 = dialog.findViewById(R.id.homeQT2);
            final TextView homeQT3 = dialog.findViewById(R.id.homeQT3);
            final TextView homeQT4 = dialog.findViewById(R.id.homeQT4);
            final TextView homeOT = dialog.findViewById(R.id.homeOT);
            final TextView scoreOT = dialog.findViewById(R.id.scoreOT);


            awayTeam.setText(g.awayTeam.abbr);
            awayQT1.setText(Integer.toString(g.awayQScore[0]));
            awayQT2.setText(Integer.toString(g.awayQScore[1]));
            awayQT3.setText(Integer.toString(g.awayQScore[2]));
            awayQT4.setText(Integer.toString(g.awayQScore[3]));

            homeTeam.setText(g.homeTeam.abbr);
            homeQT1.setText(Integer.toString(g.homeQScore[0]));
            homeQT2.setText(Integer.toString(g.homeQScore[1]));
            homeQT3.setText(Integer.toString(g.homeQScore[2]));
            homeQT4.setText(Integer.toString(g.homeQScore[3]));

            if (g.numOT > 0) {
                int awayOTscore = g.awayScore - (g.awayQScore[0] + g.awayQScore[1] + g.awayQScore[2] + g.awayQScore[3]);
                int homeOTscore = g.homeScore - (g.homeQScore[0] + g.homeQScore[1] + g.homeQScore[2] + g.homeQScore[3]);
                awayOT.setText(Integer.toString(awayOTscore));
                homeOT.setText(Integer.toString(homeOTscore));
            } else {
                awayOT.setText("");
                homeOT.setText("");
                scoreOT.setText("");
            }

            final TextView gameDialogScoreDashName = dialog.findViewById(R.id.gameDialogScoreDashName);
            if (g.numOT > 0) {
                gameDialogScoreDashName.setText(g.numOT + "OT");
            } else gameDialogScoreDashName.setText("@");


            final TextView gameL = dialog.findViewById(R.id.gameDialogLeft);
            gameL.setText(gameStr[0]);
            final TextView gameC = dialog.findViewById(R.id.gameDialogCenter);
            gameC.setText(gameStr[1]);
            final TextView gameR = dialog.findViewById(R.id.gameDialogRight);
            gameR.setText(gameStr[2]);

            final TextView gameQL = dialog.findViewById(R.id.gameDialogQBLeft);
            gameQL.setText(gameStr[3]);
            final TextView gameQC = dialog.findViewById(R.id.gameDialogQBCenter);
            gameQC.setText(gameStr[4]);
            final TextView gameQR = dialog.findViewById(R.id.gameDialogQBRight);
            gameQR.setText(gameStr[5]);

            final TextView gameRL = dialog.findViewById(R.id.gameDialogRushLeft);
            gameRL.setText(gameStr[6]);
            final TextView gameRC = dialog.findViewById(R.id.gameDialogRushCenter);
            gameRC.setText(gameStr[7]);
            final TextView gameRR = dialog.findViewById(R.id.gameDialogRushRight);
            gameRR.setText(gameStr[8]);

            final TextView gameWL = dialog.findViewById(R.id.gameDialogRecLeft);
            gameWL.setText(gameStr[9]);
            final TextView gameWC = dialog.findViewById(R.id.gameDialogRecCenter);
            gameWC.setText(gameStr[10]);
            final TextView gameWR = dialog.findViewById(R.id.gameDialogRecRight);
            gameWR.setText(gameStr[11]);

            final TextView gameDL = dialog.findViewById(R.id.gameDialogDefLeft);
            gameDL.setText(gameStr[12]);
            final TextView gameDC = dialog.findViewById(R.id.gameDialogDefCenter);
            gameDC.setText(gameStr[13]);
            final TextView gameDR = dialog.findViewById(R.id.gameDialogDefRight);
            gameDR.setText(gameStr[14]);

            final TextView gameKL = dialog.findViewById(R.id.gameDialogKickLeft);
            gameKL.setText(gameStr[15]);
            final TextView gameKC = dialog.findViewById(R.id.gameDialogKickCenter);
            gameKC.setText(gameStr[16]);
            final TextView gameKR = dialog.findViewById(R.id.gameDialogKickRight);
            gameKR.setText(gameStr[17]);

            final TextView gameB = dialog.findViewById(R.id.gameDialogBottom);
            gameB.setText(gameStr[18] + "\n\n");

            String[] selection;
            selection = new String[5];
            selection[0] = "Game Summary";
            selection[1] = "Offense Stats";
            selection[2] = "Defense Stats";
            selection[3] = "Special Teams Stats";
            selection[4] = "Game Play Log";

            Spinner potySpinner = dialog.findViewById(R.id.boxscoreMenu);
            final ArrayAdapter<String> boxMenu = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, selection);
            boxMenu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            potySpinner.setAdapter(boxMenu);

            potySpinner.setOnItemSelectedListener(
                    new AdapterView.OnItemSelectedListener() {
                        public void onItemSelected(
                                AdapterView<?> parent, View view, int position, long id) {
                            if (position == 0) {
                                //Show Summary Only

                                gameQL.setVisibility(View.GONE);
                                gameQC.setVisibility(View.GONE);
                                gameQR.setVisibility(View.GONE);

                                gameRL.setVisibility(View.GONE);
                                gameRC.setVisibility(View.GONE);
                                gameRR.setVisibility(View.GONE);

                                gameWL.setVisibility(View.GONE);
                                gameWC.setVisibility(View.GONE);
                                gameWR.setVisibility(View.GONE);

                                gameDL.setVisibility(View.GONE);
                                gameDC.setVisibility(View.GONE);
                                gameDR.setVisibility(View.GONE);

                                gameKL.setVisibility(View.GONE);
                                gameKC.setVisibility(View.GONE);
                                gameKR.setVisibility(View.GONE);

                                gameB.setVisibility(View.GONE);

                            } else if (position == 1) {
                                //Show Summary + Offense Stats

                                gameQL.setVisibility(View.VISIBLE);
                                gameQC.setVisibility(View.VISIBLE);
                                gameQR.setVisibility(View.VISIBLE);

                                gameRL.setVisibility(View.VISIBLE);
                                gameRC.setVisibility(View.VISIBLE);
                                gameRR.setVisibility(View.VISIBLE);

                                gameWL.setVisibility(View.VISIBLE);
                                gameWC.setVisibility(View.VISIBLE);
                                gameWR.setVisibility(View.VISIBLE);

                                gameDL.setVisibility(View.GONE);
                                gameDC.setVisibility(View.GONE);
                                gameDR.setVisibility(View.GONE);

                                gameKL.setVisibility(View.GONE);
                                gameKC.setVisibility(View.GONE);
                                gameKR.setVisibility(View.GONE);
                                gameB.setVisibility(View.GONE);

                            } else if (position == 2) {
                                //Show Summary + Defense Stats

                                gameQL.setVisibility(View.GONE);
                                gameQC.setVisibility(View.GONE);
                                gameQR.setVisibility(View.GONE);

                                gameRL.setVisibility(View.GONE);
                                gameRC.setVisibility(View.GONE);
                                gameRR.setVisibility(View.GONE);

                                gameWL.setVisibility(View.GONE);
                                gameWC.setVisibility(View.GONE);
                                gameWR.setVisibility(View.GONE);

                                gameDL.setVisibility(View.VISIBLE);
                                gameDC.setVisibility(View.VISIBLE);
                                gameDR.setVisibility(View.VISIBLE);

                                gameKL.setVisibility(View.GONE);
                                gameKC.setVisibility(View.GONE);
                                gameKR.setVisibility(View.GONE);
                                gameB.setVisibility(View.GONE);

                            } else if (position == 3) {
                                //Show Summary + Specical Teams Stats

                                gameQL.setVisibility(View.GONE);
                                gameQC.setVisibility(View.GONE);
                                gameQR.setVisibility(View.GONE);

                                gameRL.setVisibility(View.GONE);
                                gameRC.setVisibility(View.GONE);
                                gameRR.setVisibility(View.GONE);

                                gameWL.setVisibility(View.GONE);
                                gameWC.setVisibility(View.GONE);
                                gameWR.setVisibility(View.GONE);

                                gameDL.setVisibility(View.GONE);
                                gameDC.setVisibility(View.GONE);
                                gameDR.setVisibility(View.GONE);

                                gameKL.setVisibility(View.VISIBLE);
                                gameKC.setVisibility(View.VISIBLE);
                                gameKR.setVisibility(View.VISIBLE);
                                gameB.setVisibility(View.GONE);

                            } else if (position == 4) {
                                //Show Summary + Play by Play

                                gameQL.setVisibility(View.GONE);
                                gameQC.setVisibility(View.GONE);
                                gameQR.setVisibility(View.GONE);

                                gameRL.setVisibility(View.GONE);
                                gameRC.setVisibility(View.GONE);
                                gameRR.setVisibility(View.GONE);

                                gameWL.setVisibility(View.GONE);
                                gameWC.setVisibility(View.GONE);
                                gameWR.setVisibility(View.GONE);

                                gameDL.setVisibility(View.GONE);
                                gameDC.setVisibility(View.GONE);
                                gameDR.setVisibility(View.GONE);

                                gameKL.setVisibility(View.GONE);
                                gameKC.setVisibility(View.GONE);
                                gameKR.setVisibility(View.GONE);

                                gameB.setVisibility(View.VISIBLE);

                            } else {

                                gameQL.setVisibility(View.GONE);
                                gameQC.setVisibility(View.GONE);
                                gameQR.setVisibility(View.GONE);

                                gameRL.setVisibility(View.GONE);
                                gameRC.setVisibility(View.GONE);
                                gameRR.setVisibility(View.GONE);

                                gameWL.setVisibility(View.GONE);
                                gameWC.setVisibility(View.GONE);
                                gameWR.setVisibility(View.GONE);

                                gameDL.setVisibility(View.GONE);
                                gameDC.setVisibility(View.GONE);
                                gameDR.setVisibility(View.GONE);

                                gameKL.setVisibility(View.GONE);
                                gameKC.setVisibility(View.GONE);
                                gameKR.setVisibility(View.GONE);

                                gameB.setVisibility(View.GONE);
                            }
                        }

                        public void onNothingSelected(AdapterView<?> parent) {
                            // do nothing
                        }
                    });


        } else {
            // Show game scouting dialog
            gameStr = g.getGameScoutStr();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(g.awayTeam.name + " @ " + g.homeTeam.name + ": " + g.gameName)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //do nothing?
                        }
                    })
                    .setView(getLayoutInflater().inflate(R.layout.game_scout_dialog, null));
            AlertDialog dialog = builder.create();
            dialog.show();

            final TextView gameL = dialog.findViewById(R.id.gameScoutDialogLeft);
            gameL.setText(gameStr[0]);
            final TextView gameC = dialog.findViewById(R.id.gameScoutDialogCenter);
            gameC.setText(gameStr[1]);
            final TextView gameR = dialog.findViewById(R.id.gameScoutDialogRight);
            gameR.setText(gameStr[2]);
            final TextView gameB = dialog.findViewById(R.id.gameScoutDialogBottom);
            gameB.setText(gameStr[3]);

            // Set up spinners to choose strategy, if the game involves the user team
            if (g.awayTeam == userTeam || g.homeTeam == userTeam) {

                // Set text to show user team's abbr
                TextView textScoutOffenseStrategy = dialog.findViewById(R.id.textScoutOffenseStrategy);
                TextView textScoutDefenseStrategy = dialog.findViewById(R.id.textScoutDefenseStrategy);
                textScoutOffenseStrategy.setText(userTeam.abbr + " Off Strategy:");
                textScoutDefenseStrategy.setText(userTeam.abbr + " Def Strategy:");

                // Get the strategy options
                final PlaybookOffense[] tsOff = userTeam.getPlaybookOff();
                final PlaybookDefense[] tsDef = userTeam.getPlaybookDef();
                int offStratNum = 0;
                int defStratNum = 0;

                String[] stratOffSelection = new String[tsOff.length];
                for (int i = 0; i < tsOff.length; ++i) {
                    stratOffSelection[i] = tsOff[i].getStratName();
                    if (stratOffSelection[i].equals(userTeam.playbookOff.getStratName()))
                        offStratNum = i;
                }

                String[] stratDefSelection = new String[tsDef.length];
                for (int i = 0; i < tsDef.length; ++i) {
                    stratDefSelection[i] = tsDef[i].getStratName();
                    if (stratDefSelection[i].equals(userTeam.playbookDef.getStratName()))
                        defStratNum = i;
                }

                // Offense Strategy Spinner
                Spinner stratOffSelectionSpinner = dialog.findViewById(R.id.spinnerScoutOffenseStrategy);
                ArrayAdapter<String> stratOffSpinnerAdapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_item, stratOffSelection);
                stratOffSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                stratOffSelectionSpinner.setAdapter(stratOffSpinnerAdapter);
                stratOffSelectionSpinner.setSelection(offStratNum);

                stratOffSelectionSpinner.setOnItemSelectedListener(
                        new AdapterView.OnItemSelectedListener() {
                            public void onItemSelected(
                                    AdapterView<?> parent, View view, int position, long id) {
                                userTeam.playbookOff = tsOff[position];
                                userTeam.playbookOffNum = position;
                            }

                            public void onNothingSelected(AdapterView<?> parent) {
                                // do nothing
                            }
                        });

                // Defense Spinner Adapter
                Spinner stratDefSelectionSpinner = dialog.findViewById(R.id.spinnerScoutDefenseStrategy);
                ArrayAdapter<String> stratDefSpinnerAdapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_item, stratDefSelection);
                stratDefSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                stratDefSelectionSpinner.setAdapter(stratDefSpinnerAdapter);
                stratDefSelectionSpinner.setSelection(defStratNum);

                stratDefSelectionSpinner.setOnItemSelectedListener(
                        new AdapterView.OnItemSelectedListener() {
                            public void onItemSelected(
                                    AdapterView<?> parent, View view, int position, long id) {
                                userTeam.playbookDef = tsDef[position];
                                userTeam.playbookDefNum = position;
                            }

                            public void onNothingSelected(AdapterView<?> parent) {
                                // do nothing
                            }
                        });
            } else {
                // Make the strategy stuff invisible
                Spinner stratOffSelectionSpinner = dialog.findViewById(R.id.spinnerScoutOffenseStrategy);
                Spinner stratDefSelectionSpinner = dialog.findViewById(R.id.spinnerScoutDefenseStrategy);
                stratOffSelectionSpinner.setVisibility(View.GONE);
                stratDefSelectionSpinner.setVisibility(View.GONE);

                TextView textScoutOffenseStrategy = dialog.findViewById(R.id.textScoutOffenseStrategy);
                TextView textScoutDefenseStrategy = dialog.findViewById(R.id.textScoutDefenseStrategy);
                textScoutOffenseStrategy.setVisibility(View.GONE);
                textScoutDefenseStrategy.setVisibility(View.GONE);
            }
        }
    }

    //Weekly Scoreboard
    private void showWeeklyScores() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Weekly Scoreboard")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing?
                    }
                })
                .setView(getLayoutInflater().inflate(R.layout.team_rankings_dialog, null));
        AlertDialog dialog = builder.create();
        dialog.show();

        ArrayList<String> rankings = new ArrayList<>();
        int dbSize;
        if (simLeague.currentWeek + 1 <= 16) dbSize = simLeague.currentWeek + 1;
        else dbSize = 16;

        String[] weekSelection = new String[dbSize];
        for (int i = 0; i < weekSelection.length; ++i) {
            if (i == 13) weekSelection[i] = "Conf Champ Week";
            else if (i == 14) weekSelection[i] = "Bowl Game Week";
            else if (i == 15) weekSelection[i] = "National Champ";
            else weekSelection[i] = "Week " + i;
        }
        Spinner weekSelectionSpinner = dialog.findViewById(R.id.spinnerTeamRankings);
        ArrayAdapter<String> weekSelectionSpinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, weekSelection);
        weekSelectionSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weekSelectionSpinner.setAdapter(weekSelectionSpinnerAdapter);
        weekSelectionSpinner.setSelection(dbSize - 1);

        final ListView newsStoriesList = dialog.findViewById(R.id.listViewTeamRankings);
        final NewsStoriesListArrayAdapter weeklyScoresAdapter = new NewsStoriesListArrayAdapter(this, rankings);
        newsStoriesList.setAdapter(weeklyScoresAdapter);

        weekSelectionSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        ArrayList<String> scores = simLeague.weeklyScores.get(position);
                        boolean isempty = false;
                        if (scores.size() == 0) {
                            isempty = true;
                            scores.add("No news stories.>I guess this week was a bit boring, huh?");
                        }
                        weeklyScoresAdapter.clear();
                        weeklyScoresAdapter.addAll(scores);
                        weeklyScoresAdapter.notifyDataSetChanged();
                        if (isempty) {
                            scores.remove(0);
                        }
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        // do nothing
                    }
                });
    }

    // Shows Conference Standings
    private void updateStandings() {
        mainList.setVisibility(View.VISIBLE);
        expListPlayerStats.setVisibility(View.GONE);

        ArrayList<String> standings;
        standings = simLeague.getConfStandings();

        final MainRankings teamRankings = new MainRankings(this, standings, userTeam.name, this);
        mainList.setAdapter(teamRankings);
    }

    // Shows AP Polls
    private void updateRankings() {

        mainList.setVisibility(View.VISIBLE);
        expListPlayerStats.setVisibility(View.GONE);

        ArrayList<String> standings;
        standings = simLeague.getTeamRankings();

        final MainRankings teamRankings = new MainRankings(this, standings, userTeam.name, this);
        mainList.setAdapter(teamRankings);
    }

    //Depth Chart
    private void showTeamLineupDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set Team Lineup")
                .setView(getLayoutInflater().inflate(R.layout.team_lineup_dialog, null));
        final AlertDialog dialog = builder.create();
        dialog.show();

        final String[] positionSelection = {"QB (1 starter)", "RB (2 starters)", "WR (3 starters)", "TE (1 starter)", "OL (5 starters)",
                "K (1 starter)", "DL (4 starters)", "LB (3 starters)", "CB (3 starters)", "S (2 starters)"};
        final int[] positionNumberRequired = {userTeam.startersQB, userTeam.startersRB, userTeam.startersWR, userTeam.startersTE, userTeam.startersOL, userTeam.startersK, userTeam.startersDL, userTeam.startersLB, userTeam.startersCB, userTeam.startersS};
        final Spinner teamLineupPositionSpinner = dialog.findViewById(R.id.spinnerTeamLineupPosition);
        ArrayAdapter<String> teamLineupPositionSpinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, positionSelection);
        teamLineupPositionSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        teamLineupPositionSpinner.setAdapter(teamLineupPositionSpinnerAdapter);

        // Text to show what each attr is
        final TextView textLineupPositionDescription = dialog.findViewById(R.id.textViewLineupPositionDescription);

        // List of team's players for selected position
        final ArrayList<Player> positionPlayers = new ArrayList<>();
        positionPlayers.addAll(userTeam.teamQBs);

        final ListView teamPositionList = dialog.findViewById(R.id.listViewTeamLineup);
        final TeamLineupArrayAdapter teamLineupAdapter = new TeamLineupArrayAdapter(this, positionPlayers, 1);
        teamPositionList.setAdapter(teamLineupAdapter);

        teamLineupPositionSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        updateLineupList(position, teamLineupAdapter, positionNumberRequired, positionPlayers, textLineupPositionDescription);
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        // do nothing
                    }
                });

        Button saveLineupsButton = dialog.findViewById(R.id.buttonSaveLineups);
        Button doneWithLineupsButton = dialog.findViewById(R.id.buttonDoneWithLineups);

        doneWithLineupsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                dialog.dismiss();
                updateCurrTeam();
            }
        });

        saveLineupsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Save the lineup that player set for the position
                int positionSpinner = teamLineupPositionSpinner.getSelectedItemPosition();
                if (teamLineupAdapter.playersSelected.size() == teamLineupAdapter.playersRequired) {
                    // Set starters to new selection
                    userTeam.setStarters(teamLineupAdapter.playersSelected, positionSpinner);

                    // Update list to show the change
                    updateLineupList(positionSpinner, teamLineupAdapter, positionNumberRequired, positionPlayers, textLineupPositionDescription);

                    Toast.makeText(MainActivity.this, "Saved lineup for " + positionSelection[positionSpinner] + "!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, teamLineupAdapter.playersSelected.size() + " players selected.\nNot the correct number of starters (" + teamLineupAdapter.playersRequired + ")",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    //Depth Chart Lineup Setup
    private void updateLineupList(int position, TeamLineupArrayAdapter teamLineupAdapter, int[] positionNumberRequired,
                                  ArrayList<Player> positionPlayers, TextView textLineupPositionDescription) {
        teamLineupAdapter.playersRequired = positionNumberRequired[position];
        teamLineupAdapter.playersSelected.clear();
        teamLineupAdapter.players.clear();
        positionPlayers.clear();
        // Change position players to correct position
        switch (position) {
            case 0:
                textLineupPositionDescription.setText("Name [Yr] Overall/Potential\n(Pass Strength, Pass Accuracy, Evasion, Speed)");
                positionPlayers.addAll(userTeam.teamQBs);
                break;
            case 1:
                textLineupPositionDescription.setText("Name [Yr] Overall/Potential\n(Power, Speed, Evasion, Catch)");
                positionPlayers.addAll(userTeam.teamRBs);
                break;
            case 2:
                textLineupPositionDescription.setText("Name [Yr] Overall/Potential\n(Catch, Speed, Evaasion, Jump)");
                positionPlayers.addAll(userTeam.teamWRs);
                break;
            case 3:
                textLineupPositionDescription.setText("Name [Yr] Overall/Potential\n(Catch, Run Block, Evasion, Speed)");
                positionPlayers.addAll(userTeam.teamTEs);
                break;
            case 4:
                textLineupPositionDescription.setText("Name [Yr] Overall/Potential\n(Strength, Run Block, Pass Block, Awareness)");
                positionPlayers.addAll(userTeam.teamOLs);
                break;
            case 5:
                textLineupPositionDescription.setText("Name [Yr] Overall/Potential\n(Kick Strength, Kick Accuracy, Clumsiness, Pressure)");
                positionPlayers.addAll(userTeam.teamKs);
                break;
            case 6:
                textLineupPositionDescription.setText("Name [Yr] Overall/Potential\n(Strength, Run Def, Pass Def, Tackle)");
                positionPlayers.addAll(userTeam.teamDLs);
                break;
            case 7:
                textLineupPositionDescription.setText("Name [Yr] Overall/Potential\n(Cover, Run Def, Tackle, Run Stop)");
                positionPlayers.addAll(userTeam.teamLBs);
                break;
            case 8:
                textLineupPositionDescription.setText("Name [Yr] Overall/Potential\n(Cover, Speed, Tackle, Jump)");
                positionPlayers.addAll(userTeam.teamCBs);
                break;
            case 9:
                textLineupPositionDescription.setText("Name [Yr] Overall/Potential\n(Cover, Speed, Tackle, Run Stop)");
                positionPlayers.addAll(userTeam.teamSs);
                break;

        }

        // Change starters to correct starters
        for (int i = 0; i < teamLineupAdapter.playersRequired; ++i) {
            teamLineupAdapter.playersSelected.add(positionPlayers.get(i));
        }
        teamLineupAdapter.notifyDataSetChanged();
    }

    //Team Stategy/Playbook
    private void showTeamStrategyDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Team Strategy")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing?
                    }
                })
                .setView(getLayoutInflater().inflate(R.layout.team_strategy_dialog, null));
        AlertDialog dialog = builder.create();
        dialog.show();

        // Get the options for team strategies in both offense and defense
        final PlaybookOffense[] tsOff = userTeam.getPlaybookOff();
        final PlaybookDefense[] tsDef = userTeam.getPlaybookDef();
        int offStratNum = 0;
        int defStratNum = 0;

        String[] stratOffSelection = new String[tsOff.length];
        for (int i = 0; i < tsOff.length; ++i) {
            stratOffSelection[i] = tsOff[i].getStratName();
            if (stratOffSelection[i].equals(userTeam.playbookOff.getStratName())) offStratNum = i;
        }

        String[] stratDefSelection = new String[tsDef.length];
        for (int i = 0; i < tsDef.length; ++i) {
            stratDefSelection[i] = tsDef[i].getStratName();
            if (stratDefSelection[i].equals(userTeam.playbookDef.getStratName())) defStratNum = i;
        }

        final TextView offStratDescription = dialog.findViewById(R.id.textOffenseStrategy);
        final TextView defStratDescription = dialog.findViewById(R.id.textDefenseStrategy);

        // Offense Strategy Spinner
        Spinner stratOffSelectionSpinner = dialog.findViewById(R.id.spinnerOffenseStrategy);
        ArrayAdapter<String> stratOffSpinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, stratOffSelection);
        stratOffSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stratOffSelectionSpinner.setAdapter(stratOffSpinnerAdapter);
        stratOffSelectionSpinner.setSelection(offStratNum);

        stratOffSelectionSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        offStratDescription.setText(tsOff[position].getStratDescription());
                        userTeam.playbookOff = tsOff[position];
                        userTeam.playbookOffNum = position;
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        // do nothing
                    }
                });

        // Defense Spinner Adapter
        Spinner stratDefSelectionSpinner = dialog.findViewById(R.id.spinnerDefenseStrategy);
        ArrayAdapter<String> stratDefSpinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, stratDefSelection);
        stratDefSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stratDefSelectionSpinner.setAdapter(stratDefSpinnerAdapter);
        stratDefSelectionSpinner.setSelection(defStratNum);

        stratDefSelectionSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        defStratDescription.setText(tsDef[position].getStratDescription());
                        userTeam.playbookDef = tsDef[position];
                        userTeam.playbookDefNum = position;
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        // do nothing
                    }
                });

    }

    //Simulate Week
    private void simulateWeek() {
        Button simGameButton = findViewById(R.id.simGameButton);
        // In-Season

        if (simLeague.currentWeek <= 14) {
            int numGamesPlayed = userTeam.gameWLSchedule.size();
            simLeague.playWeek();

            if (simLeague.currentWeek == 6) {
                midseasonSummary();
            }

            // Get injury report if there are injuries and just played a game
            if (userTeam.gameWLSchedule.size() > numGamesPlayed) {
                if (showInjuryReport)
                    showInjuryReportDialog();
                if (showToasts)
                    Toast.makeText(MainActivity.this, userTeam.weekSummaryStr(),
                            Toast.LENGTH_SHORT).show();
            }

            // Show notification for being invited/not invited to bowl or CCG
            if (simLeague.currentWeek >= 12) {
                if (!userTeam.gameSchedule.get(userTeam.gameSchedule.size() - 1).hasPlayed) {
                    String weekGameName = userTeam.gameSchedule.get(userTeam.gameSchedule.size() - 1).gameName;
                    if (weekGameName.equals("NCG")) {
                        if (showToasts)
                            Toast.makeText(MainActivity.this, "Congratulations! " + userTeam.name + " was invited to the National Championship Game!",
                                    Toast.LENGTH_SHORT).show();
                    } else {
                        if (showToasts)
                            Toast.makeText(MainActivity.this, "Congratulations! " + userTeam.name + " was invited to the " +
                                            weekGameName + "!",
                                    Toast.LENGTH_SHORT).show();
                    }
                } else if (simLeague.currentWeek == 12) {
                    if (showToasts)
                        Toast.makeText(MainActivity.this, userTeam.name + " was not invited to the Conference Championship.",
                                Toast.LENGTH_SHORT).show();
                } else if (simLeague.currentWeek == 13) {
                    if (showToasts)
                        Toast.makeText(MainActivity.this, userTeam.name + " was not invited to a bowl game.",
                                Toast.LENGTH_SHORT).show();
                }
            }

            if (simLeague.currentWeek < 12) {
                simGameButton.setTextSize(14);
                simGameButton.setText("Play Week " + (simLeague.currentWeek + 1));
            } else if (simLeague.currentWeek == 12) {
                simGameButton.setTextSize(11);
                simGameButton.setText("Play Conf Championships");
                examineTeam(currentTeam.name);
            } else if (simLeague.currentWeek == 13) {
                heismanCeremony();
                simGameButton.setTextSize(12);
                simGameButton.setText("Play Bowl Games");
                examineTeam(currentTeam.name);
            } else if (simLeague.currentWeek == 14) {
                simGameButton.setTextSize(10);
                simGameButton.setText("Play National Championship");
            } else if (simLeague.currentWeek == 15) {
                simGameButton.setTextSize(10);
                simGameButton.setText("Season Summary");
                showNewsStoriesDialog();
            }


            updateCurrTeam();
            scrollToLatestGame();

            //Off-Season
        } else if (simLeague.currentWeek == 15) {
            // Show NCG summary and check league records
            simLeague.enterOffseason();
            simLeague.checkLeagueRecords();
            simLeague.updateHCHistory();
            simLeague.updateTeamHistories();
            simLeague.updateLeagueHistory();
            seasonSummary();
            //netTotalPrestige();
            simLeague.currentWeek++;
            simGameButton.setTextSize(12);
            simGameButton.setText("Off-Season: Contracts");

        } else if (simLeague.currentWeek == 16) {
            userHC = userTeam.HC.get(0);
            simLeague.advanceHC();
            if (simLeague.isCareerMode()) contractDialog();
            //conf realignment
            conferenceRealignment();
            //Promotion/Relegation!
            promotionRelegation();
            simGameButton.setTextSize(12);
            if (simLeague.isCareerMode())
                simGameButton.setText("Off-Season: Job Offers");
            else simGameButton.setText("Off-Season: Continue");
            simLeague.currentWeek++;

        } else if (simLeague.currentWeek == 17 && userTeam.fired) {
            if (simLeague.isCareerMode()) jobOffers(userHC);
            simLeague.currentWeek++;
            simGameButton.setTextSize(12);
            simGameButton.setText("Off-Season: Coaching Changes");

        } else if (simLeague.currentWeek == 17 && !userTeam.fired) {
            if (simLeague.isCareerMode()) promotions(userHC);
            simLeague.currentWeek++;
            simGameButton.setTextSize(12);
            simGameButton.setText("Off-Season: Coaching Changes");

        } else if (simLeague.currentWeek == 18) {
            simLeague.coachCarousel();
            simGameButton.setTextSize(12);
            simLeague.currentWeek++;
            simGameButton.setText("Off-Season: Graduation");
            showNewsStoriesDialog();

        } else if (simLeague.currentWeek == 19) {
            simLeague.advanceSeason();
            simLeague.currentWeek++;
            if(simLeague.updateTV) newsTV();
            simGameButton.setTextSize(12);
            simGameButton.setText("Off-Season: Transfer List");

        } else if (simLeague.currentWeek == 20) {
            simLeague.transferPlayers();
            showNewsStoriesDialog(); //shows significant transfer options
            simLeague.currentWeek++;
            simGameButton.setTextSize(12);
            simGameButton.setText("Off-Season: Complete Transfers");

        } else if (simLeague.currentWeek == 21) {
            transfers(); //displays list of transfers
            simLeague.currentWeek++;
            simGameButton.setTextSize(12);
            simGameButton.setText("Begin Recruiting");

        } else if (simLeague.currentWeek >= 22) {
            beginRecruiting();

        }
        resetTeamUI();

    }

    //GAME MENU


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_heisman) {
            /*
              Clicked Heisman watch in drop down menu
             */
            if (simLeague.currentWeek < 13) {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Player of the Year Watch")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //do nothing?
                            }
                        })
                        .setView(getLayoutInflater().inflate(R.layout.team_rankings_dialog, null));
                AlertDialog dialog = builder.create();
                dialog.show();

                ArrayList<String> rankings = new ArrayList<>();
                String[] rankingsSelection =
                        {"Coach - Overall", "QB - Overall", "RB - Overall", "WR - Overall", "TE - Overall", "OL - Overall", "K - Overall", "DL - Overall", "LB - Overall", "CB - Overall", "S - Overall"};
                Spinner teamRankingsSpinner = dialog.findViewById(R.id.spinnerTeamRankings);
                ArrayAdapter<String> teamRankingsSpinnerAdapter = new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_item, rankingsSelection);
                teamRankingsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                teamRankingsSpinner.setAdapter(teamRankingsSpinnerAdapter);

                final ListView teamRankingsList = dialog.findViewById(R.id.listViewTeamRankings);
                final TeamRankingsListArrayAdapter teamRankingsAdapter =
                        new TeamRankingsListArrayAdapter(this, rankings, userTeam.abbr);
                teamRankingsList.setAdapter(teamRankingsAdapter);

                teamRankingsSpinner.setOnItemSelectedListener(
                        new AdapterView.OnItemSelectedListener() {
                            public void onItemSelected(
                                    AdapterView<?> parent, View view, int position, long id) {
                                ArrayList<String> rankings = simLeague.getAwardsWatch(position);
                                if (position == 12) {
                                    teamRankingsAdapter.setUserTeamStrRep(userTeam.abbr);
                                } else {
                                    teamRankingsAdapter.setUserTeamStrRep(userTeam.abbr);
                                }
                                teamRankingsAdapter.clear();
                                teamRankingsAdapter.addAll(rankings);
                                teamRankingsAdapter.notifyDataSetChanged();
                            }

                            public void onNothingSelected(AdapterView<?> parent) {
                                // do nothing
                            }
                        });

            } else {
                // Show dialog with All American spinner too
                heismanCeremony();
            }
        } else if (id == R.id.action_rankings) {
            /*
              Clicked Team Rankings in drop down menu
             */
            showTeamRankingsDialog();
        } else if (id == R.id.action_player_rankings && simLeague.currentWeek < 18) {

            showPlayerRankingsDialog();
        } else if (id == R.id.action_current_team_history) {
            /*
              Current selected team history
             */
            showCurrTeamHistoryDialog();
        } else if (id == R.id.action_league_history) {
            /*
              Clicked League History in drop down menu
             */
            showLeagueHistoryDialog();
        } else if (id == R.id.action_top_25_history) {
             /*
               Clicked Top 25 History
              */
            showTop25History();
        } else if (id == R.id.action_team_history) {
            /*
              Clicked User Team History in drop down menu
             */
            showCoachHistoryDialog();
        } else if (id == R.id.action_ccg_bowl_watch) {
            /*
              Clicked CCG / Bowl Watch in drop down menu
             */
            showBowlCCGDialog();
        } else if (id == R.id.action_import) {
            importData();

        } else if (id == R.id.action_save_league) {
            /*
              Clicked Save League in drop down menu
             */
            if (simLeague.currentWeek < 13) {
                saveLeague();
            } else {
                Toast.makeText(MainActivity.this, "Save Function Disabled. Save only available in Pre-Season and Reg Season.",
                        Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.action_return_main_menu) {
            /*
              Let user confirm that they actually do want to go to main menu
             */
            exitMainActivity();
        } else if (id == R.id.action_change_team_name) {
            /*
              Let user change their team name and abbr
             */
            changeSettingsDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    //MENU ITEMS

    //User Settings
    private void changeSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Game Settings")
                .setView(getLayoutInflater().inflate(R.layout.settings_menu, null));
        final AlertDialog dialog = builder.create();
        dialog.show();

        final CheckBox checkboxShowPopup = dialog.findViewById(R.id.checkboxShowPopups);
        checkboxShowPopup.setChecked(showToasts);

        final CheckBox checkboxShowInjury = dialog.findViewById(R.id.checkboxShowInjuryReport);
        checkboxShowInjury.setChecked(showInjuryReport);

        final CheckBox checkboxGameLog = dialog.findViewById(R.id.checkboxShowFullGameLog);
        checkboxGameLog.setChecked(simLeague.fullGameLog);

        final CheckBox checkboxPotential = dialog.findViewById(R.id.checkboxHidePotential);
        checkboxPotential.setChecked(simLeague.hidePotential);

        final CheckBox checkboxRealignment = dialog.findViewById(R.id.checkboxConfRealignment);
        checkboxRealignment.setChecked(simLeague.confRealignment);

        final CheckBox checkboxProRelegation = dialog.findViewById(R.id.checkboxProRelegation);
        checkboxProRelegation.setChecked(simLeague.enableProRel);

        final CheckBox checkboxTV = dialog.findViewById(R.id.checkboxTV);
        checkboxTV.setChecked(simLeague.enableTV);

        checkboxRealignment.setOnClickListener(new View.OnClickListener() {
                                                   @Override
                                                   public void onClick(View view) {
                                                       if (checkboxRealignment.isChecked()) {
                                                           checkboxProRelegation.setChecked(false);
                                                       }
                                                   }
                                               }
        );

        checkboxProRelegation.setOnClickListener(new View.OnClickListener() {
                                                     @Override
                                                     public void onClick(View view) {
                                                         if (checkboxProRelegation.isChecked()) {
                                                             checkboxRealignment.setChecked(false);
                                                         }
                                                     }
                                                 }
        );

        Button cancelButton = dialog.findViewById(R.id.buttonCancelSettings);
        Button okButton = dialog.findViewById(R.id.buttonOkSettings);
        Button changeTeamsButton = dialog.findViewById(R.id.buttonChangeTeams);
        Button gameEditorButton = dialog.findViewById(R.id.buttonGameEditor);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                dialog.dismiss();
            }
        });

        gameEditorButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                dialog.dismiss();
                gameEditor();

            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                showToasts = checkboxShowPopup.isChecked();
                showInjuryReport = checkboxShowInjury.isChecked();
                simLeague.fullGameLog = checkboxGameLog.isChecked();
                simLeague.hidePotential = checkboxPotential.isChecked();
                simLeague.confRealignment = checkboxRealignment.isChecked();
                simLeague.enableProRel = checkboxProRelegation.isChecked();
                simLeague.enableTV = checkboxTV.isChecked();
                userTeam.showPopups = showToasts;
                dialog.dismiss();

            }
        });
        changeTeamsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Confirmation");
                builder.setMessage("Are you sure you want to resign from this position?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Perform action on click
                        userHC = userTeam.HC.get(0);
                        //switchTeams();
                        if (simLeague.isCareerMode()) jobOffers(userHC);
                        else switchTeams(userHC);
                        dialog.dismiss();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(), "Canceled!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                    }
                });

                builder.show();

            }
        });
    }

    //Game Editor - Customize Names
    private void gameEditor() {

        AlertDialog.Builder GameEditor = new AlertDialog.Builder(this);
        GameEditor.setTitle("Game Editor")
                .setView(getLayoutInflater().inflate(R.layout.game_editor, null));
        final AlertDialog dialog = GameEditor.create();
        dialog.show();

        final EditText changeNameEditText = dialog.findViewById(R.id.editTextChangeName);
        changeNameEditText.setText(currentTeam.name);  //updated from userTeam to currentTeam
        final EditText changeAbbrEditText = dialog.findViewById(R.id.editTextChangeAbbr);
        changeAbbrEditText.setText(currentTeam.abbr);   //updated from userTeam to currentTeam
        final EditText changeConfEditText = dialog.findViewById(R.id.editTextChangeConf);
        changeConfEditText.setText(currentConference.confName);   //updated from userTeam to currentTeam
        final EditText changeHCEditText = dialog.findViewById(R.id.editTextChangeHC);
        changeHCEditText.setText(currentTeam.HC.get(0).name);   //change Head Coach Name


        final TextView invalidNameText = dialog.findViewById(R.id.textViewChangeName);
        final TextView invalidAbbrText = dialog.findViewById(R.id.textViewChangeAbbr);
        final TextView invalidConfText = dialog.findViewById(R.id.textViewChangeConf);
        final TextView invalidHCText = dialog.findViewById(R.id.textViewChangeHC);

        changeNameEditText.addTextChangedListener(new TextWatcher() {
            String newName;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                newName = s.toString().trim();
                if (!simLeague.isNameValid(newName)) {
                    invalidNameText.setText("Name already in use or has illegal characters!");
                } else {
                    invalidNameText.setText("");
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                newName = s.toString().trim();
                if (!simLeague.isNameValid(newName)) {
                    invalidNameText.setText("Name already in use or has illegal characters!");
                } else {
                    invalidNameText.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                newName = s.toString().trim();
                if (!simLeague.isNameValid(newName)) {
                    invalidNameText.setText("Name already in use or has illegal characters!");
                } else {
                    invalidNameText.setText("");
                }
            }
        });

        changeAbbrEditText.addTextChangedListener(new TextWatcher() {
            String newAbbr;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                newAbbr = s.toString().trim().toUpperCase();
                if (!simLeague.isAbbrValid(newAbbr)) {
                    invalidAbbrText.setText("Abbreviation already in use or has illegal characters!");
                } else {
                    invalidAbbrText.setText("");
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                newAbbr = s.toString().trim().toUpperCase();
                if (!simLeague.isAbbrValid(newAbbr)) {
                    invalidAbbrText.setText("Abbreviation already in use or has illegal characters!");
                } else {
                    invalidAbbrText.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                newAbbr = s.toString().trim().toUpperCase();
                if (!simLeague.isAbbrValid(newAbbr)) {
                    invalidAbbrText.setText("Abbr already in use or has illegal characters!");
                } else {
                    invalidAbbrText.setText("");
                }
            }
        });

        changeConfEditText.addTextChangedListener(new TextWatcher() {
            String newConf;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                newConf = s.toString().trim();
                if (!simLeague.isNameValid(newConf)) {
                    invalidConfText.setText("Name already in use or has illegal characters!");
                } else {
                    invalidConfText.setText("");
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                newConf = s.toString().trim();
                if (!simLeague.isNameValid(newConf)) {
                    invalidConfText.setText("Name already in use or has illegal characters!");
                } else {
                    invalidConfText.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                newConf = s.toString().trim();
                if (!simLeague.isNameValid(newConf)) {
                    invalidConfText.setText("Name already in use or has illegal characters!");
                } else {
                    invalidConfText.setText("");
                }
            }

        });

        changeHCEditText.addTextChangedListener(new TextWatcher() {
            String newHC;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                newHC = s.toString().trim();
                if (!simLeague.isNameValid(newHC)) {
                    invalidHCText.setText("Name already in use or has illegal characters!");
                } else {
                    invalidHCText.setText("");
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                newHC = s.toString().trim();
                if (!simLeague.isNameValid(newHC)) {
                    invalidHCText.setText("Name already in use or has illegal characters!");
                } else {
                    invalidHCText.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                newHC = s.toString().trim();
                if (!simLeague.isNameValid(newHC)) {
                    invalidHCText.setText("Name already in use or has illegal characters!");
                } else {
                    invalidHCText.setText("");
                }
            }

        });

        Button cancelChangeNameButton = dialog.findViewById(R.id.buttonCancelChangeName);
        Button okChangeNameButton = dialog.findViewById(R.id.buttonOkChangeName);

        cancelChangeNameButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                dialog.dismiss();
            }
        });

        okChangeNameButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                String newName = changeNameEditText.getText().toString().trim();
                String newAbbr = changeAbbrEditText.getText().toString().trim().toUpperCase();
                String newConf = changeConfEditText.getText().toString().trim();
                String newHC = changeHCEditText.getText().toString().trim();

                if (simLeague.isNameValid(newName) && simLeague.isAbbrValid(newAbbr) && simLeague.isNameValid(newConf) && isNameValid((newHC))) {
                    simLeague.changeAbbrHistoryRecords(currentTeam.abbr, newAbbr);
                    currentTeam.name = newName; //set new team name
                    currentTeam.abbr = newAbbr; //set new conference name
                    oldConf = currentConference.confName;
                    currentConference.confName = newConf;
                    currentTeam.HC.get(0).name = newHC;
                    simLeague.updateTeamConf(newConf, oldConf, currentConferenceID);  //update all other conf teams
                    getSupportActionBar().setTitle(season + " | " + userTeam.name);
                    Team rival = simLeague.findTeamAbbr(currentTeam.rivalTeam);  // Have to update rival's rival too!
                    rival.rivalTeam = currentTeam.abbr;
                    wantUpdateConf = true;
                    updateCurrConference();  //updates the UI
                    examineTeam(currentTeam.name);

                    dialog.dismiss();

                } else {
                    if (showToasts)
                        Toast.makeText(MainActivity.this, "Invalid name/abbr! Name not changed.",
                                Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    //League History
    private void showLeagueHistoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("League History / Records")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing?
                    }
                })
                .setView(getLayoutInflater().inflate(R.layout.team_rankings_dialog, null));
        AlertDialog dialog = builder.create();
        dialog.show();

        String[] historySelection = {"League History", "League Records", "League Stats", "Hall of Fame",};
        Spinner leagueHistorySpinner = dialog.findViewById(R.id.spinnerTeamRankings);
        ArrayAdapter<String> leagueHistorySpinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, historySelection);
        leagueHistorySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        leagueHistorySpinner.setAdapter(leagueHistorySpinnerAdapter);

        final ListView leagueHistoryList = dialog.findViewById(R.id.listViewTeamRankings);
        final String[] hofPlayers = new String[simLeague.leagueHoF.size()];
        for (int i = 0; i < simLeague.leagueHoF.size(); ++i) {
            hofPlayers[i] = simLeague.leagueHoF.get(i);
        }

        leagueHistorySpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        if (position == 1) {
                            final LeagueRecordsListArrayAdapter leagueRecordsAdapter =
                                    new LeagueRecordsListArrayAdapter(MainActivity.this, simLeague.getLeagueRecordsStr().split("\n"), userTeam.abbr, userTeam.name);
                            leagueHistoryList.setAdapter(leagueRecordsAdapter);
                        } else if (position == 3) {
                            HallOfFameListArrayAdapter hofAdapter = new HallOfFameListArrayAdapter(MainActivity.this, hofPlayers, userTeam.name);
                            leagueHistoryList.setAdapter(hofAdapter);
                        } else if (position == 2) {
                            showLeagueHistoryStats();
                        } else {
                            final LeagueHistoryListArrayAdapter leagueHistoryAdapter =
                                    new LeagueHistoryListArrayAdapter(MainActivity.this, simLeague.getLeagueHistoryStr().split("%"), userTeam.abbr);
                            leagueHistoryList.setAdapter(leagueHistoryAdapter);
                        }
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        // do nothing
                    }
                });
    }

    //League History Stats
    private void showLeagueHistoryStats() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("League Stats")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing?
                    }
                })
                .setView(getLayoutInflater().inflate(R.layout.team_rankings_dialog, null));
        AlertDialog dialog = builder.create();
        dialog.show();

        ArrayList<String> rankings = new ArrayList<>();// = simLeague.getTeamRankingsStr(0);
        String[] rankingsSelection =
                {"National Championships", "Conference Championships", "Bowl Victories", "Total Wins", "Active Coach Career Score"};
        Spinner teamRankingsSpinner = dialog.findViewById(R.id.spinnerTeamRankings);
        ArrayAdapter<String> teamRankingsSpinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, rankingsSelection);
        teamRankingsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        teamRankingsSpinner.setAdapter(teamRankingsSpinnerAdapter);

        final ListView teamRankingsList = dialog.findViewById(R.id.listViewTeamRankings);
        final TeamRankingsListArrayAdapter teamRankingsAdapter =
                new TeamRankingsListArrayAdapter(this, rankings, userTeam.name);
        teamRankingsList.setAdapter(teamRankingsAdapter);

        teamRankingsSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        ArrayList<String> rankings = simLeague.getLeagueHistoryStats(position);
                        if (position == 4) {
                            teamRankingsAdapter.setUserTeamStrRep(userTeam.HC.get(0).name + " (" + userTeam.abbr + ")");
                        } else {
                            teamRankingsAdapter.setUserTeamStrRep(userTeam.name);
                        }
                        teamRankingsAdapter.clear();
                        teamRankingsAdapter.addAll(rankings);
                        teamRankingsAdapter.notifyDataSetChanged();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        // do nothing
                    }
                });
    }

    //AP Poll History
    private void showTop25History() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("AP Poll History")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing?
                    }
                })
                .setView(getLayoutInflater().inflate(R.layout.bowl_ccg_dialog, null));
        AlertDialog dialog = builder.create();
        dialog.show();
        if (season == seasonStart) {
            String[] selection = {"No History to Display"};
            Spinner top25hisSpinner = dialog.findViewById(R.id.spinnerBowlCCG);
            final ArrayAdapter<String> top25Adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, selection);
            top25Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            top25hisSpinner.setAdapter(top25Adapter);
        } else {
            String[] selection = new String[simLeague.leagueHistory.size()];
            for (int i = 0; i < simLeague.leagueHistory.size(); ++i) {
                selection[i] = Integer.toString(seasonStart + i);
            }
            Spinner top25hisSpinner = dialog.findViewById(R.id.spinnerBowlCCG);
            final ArrayAdapter<String> top25Adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, selection);
            top25Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            top25hisSpinner.setAdapter(top25Adapter);

            final TextView top25his = dialog.findViewById(R.id.textViewBowlCCGDialog);

            top25hisSpinner.setOnItemSelectedListener(
                    new AdapterView.OnItemSelectedListener() {
                        public void onItemSelected(
                                AdapterView<?> parent, View view, int position, long id) {
                            top25his.setText(simLeague.getLeagueTop25History(position));
                        }

                        public void onNothingSelected(AdapterView<?> parent) {
                            // do nothing
                        }
                    });
        }
    }

    //Team History
    private void showCurrTeamHistoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Team History: " + currentTeam.name)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing?
                    }
                })
                .setView(getLayoutInflater().inflate(R.layout.team_rankings_dialog, null));
        final AlertDialog dialog = builder.create();
        dialog.show();

        String[] selection = {"Team History", "Team Records", "Hall of Fame", "Graph View: Prestige", "Graph View: Rankings"};
        Spinner teamHistSpinner = dialog.findViewById(R.id.spinnerTeamRankings);
        final ArrayAdapter<String> teamHistAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, selection);
        teamHistAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        teamHistSpinner.setAdapter(teamHistAdapter);

        final ListView teamHistoryList = dialog.findViewById(R.id.listViewTeamRankings);
        final String[] hofPlayers = new String[currentTeam.hallOfFame.size()];
        for (int i = 0; i < currentTeam.hallOfFame.size(); ++i) {
            hofPlayers[i] = currentTeam.hallOfFame.get(i);
        }

        teamHistSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0) {
                            TeamHistoryListArrayAdapter teamHistoryAdapter =
                                    new TeamHistoryListArrayAdapter(MainActivity.this, currentTeam.getTeamHistoryList());
                            teamHistoryList.setAdapter(teamHistoryAdapter);
                        } else if (position == 1) {
                            LeagueRecordsListArrayAdapter leagueRecordsAdapter =
                                    new LeagueRecordsListArrayAdapter(MainActivity.this, currentTeam.teamRecords.getRecordsStr().split("\n"), "---", "---");
                            teamHistoryList.setAdapter(leagueRecordsAdapter);
                        } else if (position == 2) {
                            HallOfFameListArrayAdapter hofAdapter = new HallOfFameListArrayAdapter(MainActivity.this, hofPlayers, userTeam.name);
                            teamHistoryList.setAdapter(hofAdapter);
                        } else if (position == 3) {
                            dialog.dismiss();
                            teamGraphView();
                        } else if (position == 4) {
                            dialog.dismiss();
                            teamGraphViewRank();
                        }
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        // do nothing
                    }
                });
    }

    //Graph View

    private void teamGraphView() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(currentTeam.name + ": Prestige History")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing?
                    }
                })
                .setView(getLayoutInflater().inflate(R.layout.graphview, null));
        AlertDialog dialog = builder.create();
        dialog.show();
        DataPoint[] data = new DataPoint[simLeague.leagueHistory.size()];
        GraphView graph = dialog.findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        String[] yearLabels = new String[simLeague.leagueHistory.size()];
        for(int i = 0; i < simLeague.leagueHistory.size(); i++) {
            series.appendData(new DataPoint((seasonStart+i), Integer.parseInt( currentTeam.teamHistory.get(i).split(":")[2].split(" ")[1])), true, i+1, false);
            yearLabels[i] = Integer.toString(i+seasonStart);
        }
        graph.addSeries(series);

        if(yearLabels.length > 1) {
            StaticLabelsFormatter years = new StaticLabelsFormatter(graph);
            years.setHorizontalLabels(yearLabels);
            graph.getGridLabelRenderer().setLabelFormatter(years);
            graph.getGridLabelRenderer().setNumHorizontalLabels(4);
        }
        graph.getViewport().setScalable(true);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMaxY(simLeague.teamList.get(0).teamPrestige+10);
        graph.getViewport().setMinY(0);
    }

    //Graph View

    private void teamGraphViewRank() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(currentTeam.name + ": Rankings History")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing?
                    }
                })
                .setView(getLayoutInflater().inflate(R.layout.graphview, null));
        AlertDialog dialog = builder.create();
        dialog.show();
        DataPoint[] data = new DataPoint[simLeague.leagueHistory.size()];
        GraphView graph = dialog.findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        String[] yearLabels = new String[simLeague.leagueHistory.size()];
        for(int i = 0; i < simLeague.leagueHistory.size(); i++) {
            series.appendData(new DataPoint((seasonStart+i), 120 - Integer.parseInt( currentTeam.teamHistory.get(i).split("#")[1].split(" ")[0])), true, i+1, false);
            yearLabels[i] = Integer.toString(i+seasonStart);
        }
        graph.addSeries(series);

        String[] rankLabels = new String[121];
        for(int i = 120; i >= 0; i--) {
            rankLabels[120-i] = Integer.toString(i);
        }

        if(yearLabels.length > 1) {
            StaticLabelsFormatter years = new StaticLabelsFormatter(graph);
            years.setHorizontalLabels(yearLabels);
            years.setVerticalLabels(rankLabels);
            graph.getGridLabelRenderer().setLabelFormatter(years);
            graph.getGridLabelRenderer().setNumHorizontalLabels(4);
            graph.getGridLabelRenderer().setNumVerticalLabels(6);
        }
        graph.getViewport().setScalable(true);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMaxY(120);
        graph.getViewport().setMinY(0);
    }

    //Coach History
    private void showCoachHistoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Coach History: " + currentTeam.HC.get(0).name)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing?
                    }
                })
                .setView(getLayoutInflater().inflate(R.layout.team_rankings_dialog, null));
        final AlertDialog dialog = builder.create();
        dialog.show();

        String[] selection = {"Team History", "Graph View: Prestige", "Graph View: Rankings"};
        Spinner teamHistSpinner = dialog.findViewById(R.id.spinnerTeamRankings);
        final ArrayAdapter<String> teamHistAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, selection);
        teamHistAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        teamHistSpinner.setAdapter(teamHistAdapter);

        final ListView teamHistoryList = dialog.findViewById(R.id.listViewTeamRankings);

        teamHistSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0) {
                            TeamHistoryListArrayAdapter teamHistoryAdapter =
                                    new TeamHistoryListArrayAdapter(MainActivity.this, currentTeam.HC.get(0).getCoachHistory());
                            teamHistoryList.setAdapter(teamHistoryAdapter);
                        } else if (position == 1) {
                            dialog.dismiss();
                            coachGraphView();
                        } else if (position == 2) {
                            dialog.dismiss();
                            coachGraphViewRank();
                        }
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        // do nothing
                    }
                });
    }

    private void coachGraphView() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(currentTeam.HC.get(0).name + ": Prestige History")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing?
                    }
                })
                .setView(getLayoutInflater().inflate(R.layout.graphview, null));
        AlertDialog dialog = builder.create();
        dialog.show();
        DataPoint[] data = new DataPoint[currentTeam.HC.get(0).history.size()];
        GraphView graph = dialog.findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        String[] yearLabels = new String[currentTeam.HC.get(0).history.size()];
        for(int i = 0; i < currentTeam.HC.get(0).history.size(); i++) {
            if(!currentTeam.HC.get(0).history.get(i).equals("")) {
                series.appendData(new DataPoint(Integer.parseInt(currentTeam.HC.get(0).history.get(i).split(": ")[0]), Integer.parseInt(currentTeam.HC.get(0).history.get(i).split("Prs: ")[1].split(" ")[0])), true, i + 1, false);
                yearLabels[i] = currentTeam.HC.get(0).history.get(i).split(":")[0];
            }
        }
        graph.addSeries(series);

        if(yearLabels.length > 1) {
            StaticLabelsFormatter years = new StaticLabelsFormatter(graph);
            years.setHorizontalLabels(yearLabels);
            graph.getGridLabelRenderer().setLabelFormatter(years);
            graph.getGridLabelRenderer().setNumHorizontalLabels(4);
        }
        graph.getViewport().setScalable(true);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMaxY(simLeague.teamList.get(0).teamPrestige+10);
        graph.getViewport().setMinY(0);
    }

    private void coachGraphViewRank() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(currentTeam.HC.get(0).name + ": Rankings History")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing?
                    }
                })
                .setView(getLayoutInflater().inflate(R.layout.graphview, null));
        AlertDialog dialog = builder.create();
        dialog.show();
        DataPoint[] data = new DataPoint[currentTeam.HC.get(0).history.size()];
        GraphView graph = dialog.findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        String[] yearLabels = new String[currentTeam.HC.get(0).history.size()];
        for(int i = 0; i < currentTeam.HC.get(0).history.size(); i++) {
            if(!currentTeam.HC.get(0).history.get(i).equals("")) {
                series.appendData(new DataPoint(Integer.parseInt(currentTeam.HC.get(0).history.get(i).split(": ")[0]), 120 - Integer.parseInt(currentTeam.HC.get(0).history.get(i).split("#")[1].split(" ")[0])), true, i + 1, false);
                yearLabels[i] = currentTeam.HC.get(0).history.get(i).split(":")[0];
            }
        }
        graph.addSeries(series);

        String[] rankLabels = new String[121];
        for(int i = 120; i >= 0; i--) {
            rankLabels[120-i] = Integer.toString(i);
        }

        if(yearLabels.length > 1) {
            StaticLabelsFormatter years = new StaticLabelsFormatter(graph);
            years.setHorizontalLabels(yearLabels);
            years.setVerticalLabels(rankLabels);
            graph.getGridLabelRenderer().setLabelFormatter(years);
            graph.getGridLabelRenderer().setNumHorizontalLabels(4);
            graph.getGridLabelRenderer().setNumVerticalLabels(6);
        }
        graph.getViewport().setScalable(true);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMaxY(120);
        graph.getViewport().setMinY(0);
    }

    //Team Stats Rankings
    private void showTeamRankingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Team Statistical Rankings")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing?
                    }
                })
                .setView(getLayoutInflater().inflate(R.layout.team_rankings_dialog, null));
        AlertDialog dialog = builder.create();
        dialog.show();

        ArrayList<String> rankings = new ArrayList<>();// = simLeague.getTeamRankingsStr(0);
        String[] rankingsSelection =
                {"Poll Votes", "Prestige", "Strength of Schedule", "Strength of Wins", "Points Per Game", "Opp Points Per Game",
                        "Yards Per Game", "Opp Yards Per Game", "Pass Yards Per Game", "Rush Yards Per Game",
                        "Opp Pass YPG", "Opp Rush YPG", "TO Differential", "Off Talent", "Def Talent", "Recruiting Class"};
        Spinner teamRankingsSpinner = dialog.findViewById(R.id.spinnerTeamRankings);
        ArrayAdapter<String> teamRankingsSpinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, rankingsSelection);
        teamRankingsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        teamRankingsSpinner.setAdapter(teamRankingsSpinnerAdapter);

        final ListView teamRankingsList = dialog.findViewById(R.id.listViewTeamRankings);
        final TeamRankingsListArrayAdapter teamRankingsAdapter =
                new TeamRankingsListArrayAdapter(this, rankings, userTeam.name);
        teamRankingsList.setAdapter(teamRankingsAdapter);

        teamRankingsSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        ArrayList<String> rankings = simLeague.getTeamRankingsStr(position);

                        teamRankingsAdapter.setUserTeamStrRep(userTeam.name);
                        if (position == 15)
                            teamRankingsAdapter.setUserTeamStrRep(userTeam.strRepWithPrestige());

                        teamRankingsAdapter.clear();
                        teamRankingsAdapter.addAll(rankings);
                        teamRankingsAdapter.notifyDataSetChanged();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        // do nothing
                    }
                });
    }

    //Player Stats Rankings
    private void showPlayerRankingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Player Statistical Rankings")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing?
                    }
                })
                .setView(getLayoutInflater().inflate(R.layout.team_rankings_dialog, null));
        AlertDialog dialog = builder.create();
        dialog.show();

        ArrayList<String> rankings = new ArrayList<>();
        String[] rankingsSelection =
                {"Passer Rating", "Passing Yards", "Passing TDs", "Interceptions Thrown", "Pass Comp PCT", "Rushing Yards", "Rushing TDs", "Receptions", "Receiving Yards", "Receiving TDs",
                        "Tackles", "Sacks", "Fumbles Recovered", "Interceptions", "Field Goals Made", "Field Goal Pct", "Kickoff Return Yards", "Kickoff Return TDs", "Punt Return Yards", "Punt Return TDs",
                        "Coach - Overall", "Coach - Career Score"
                };
        Spinner playerRankingssSpinner = dialog.findViewById(R.id.spinnerTeamRankings);
        ArrayAdapter<String> playerRankingssSpinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, rankingsSelection);
        playerRankingssSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        playerRankingssSpinner.setAdapter(playerRankingssSpinnerAdapter);

        final ListView playerRankingssList = dialog.findViewById(R.id.listViewTeamRankings);
        final PlayerRankingsList playerRankingssAdapter =
                new PlayerRankingsList(this, rankings, userTeam.abbr);
        playerRankingssList.setAdapter(playerRankingssAdapter);

        playerRankingssSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        ArrayList<String> rankings = simLeague.getPlayerRankStr(position);
                        if (position == 22) {
                            playerRankingssAdapter.setUserTeamStrRep(userTeam.abbr);
                        } else {
                            playerRankingssAdapter.setUserTeamStrRep(userTeam.abbr);
                        }
                        playerRankingssAdapter.clear();
                        playerRankingssAdapter.addAll(rankings);
                        playerRankingssAdapter.notifyDataSetChanged();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        // do nothing
                    }
                });
    }

    //Bowl Games Schedule
    private void showBowlCCGDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Post-Season Games")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing?
                    }
                })
                .setView(getLayoutInflater().inflate(R.layout.bowl_ccg_dialog, null));
        AlertDialog dialog = builder.create();
        dialog.show();

        String[] selection = {"Conf Championships", "Bowl Games"};
        Spinner bowlCCGSpinner = dialog.findViewById(R.id.spinnerBowlCCG);
        ArrayAdapter<String> bowlCCGadapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, selection);
        bowlCCGadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bowlCCGSpinner.setAdapter(bowlCCGadapter);

        final TextView bowlCCGscores = dialog.findViewById(R.id.textViewBowlCCGDialog);

        bowlCCGSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0) {
                            bowlCCGscores.setText(simLeague.getCCGsStr());
                        } else {
                            bowlCCGscores.setText(simLeague.getBowlGameWatchStr());
                        }
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        // do nothing
                    }
                });
    }

    //Awards
    private void heismanCeremony() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Post Season Awards")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing?
                    }
                })
                .setView(getLayoutInflater().inflate(R.layout.team_rankings_dialog, null));
        AlertDialog dialog = builder.create();
        dialog.show();

        String[] selection;
        if (simLeague.currentWeek < 13) {
            selection = new String[1];
            selection[0] = "Player of the Year";
        } else {
            selection = new String[15];
            selection[0] = "Player of the Year";
            selection[1] = "Coach of the Year";
            selection[2] = "Freshman of the Year";
            selection[3] = "All-American Team";
            selection[4] = "All-Freshman Team";
            for (int i = 0; i < confStart; ++i) {
                selection[i + 5] = simLeague.conferences.get(i).confName + " All-Conf Team";
            }
        }

        Spinner potySpinner = dialog.findViewById(R.id.spinnerTeamRankings);
        ArrayAdapter<String> potyAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, selection);
        potyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        potySpinner.setAdapter(potyAdapter);

        final ListView potyList = dialog.findViewById(R.id.listViewTeamRankings);

        // Get all american and all conf
        final String[] coachAwardList = simLeague.getCoachAwardStr().split(">");
        final String[] freshmanAwardList = simLeague.getFreshmanCeremonyStr().split(">");
        final String[] allAmericans = simLeague.getAllAmericanStr().split(">");
        final String[] allFreshman = simLeague.getAllFreshmanStr().split(">");
        final String[][] allConference = new String[confStart][];
        for (int i = 0; i < confStart; ++i) {
            allConference[i] = simLeague.getAllConfStr(i).split(">");
        }


        potySpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0) {
                            potyList.setAdapter(new SeasonAwardsListArrayAdapter(MainActivity.this, simLeague.getHeismanCeremonyStr().split(">"), userTeam.abbr));
                        } else if (position == 1) {
                            potyList.setAdapter(new SeasonAwardsListArrayAdapter(MainActivity.this, coachAwardList, userTeam.abbr));
                        } else if (position == 2) {
                            potyList.setAdapter(new SeasonAwardsListArrayAdapter(MainActivity.this, freshmanAwardList, userTeam.abbr));
                        } else if (position == 3) {
                            potyList.setAdapter(new SeasonAwardsListArrayAdapter(MainActivity.this, allAmericans, userTeam.abbr));
                        } else if (position == 4) {
                            potyList.setAdapter(new SeasonAwardsListArrayAdapter(MainActivity.this, allFreshman, userTeam.abbr));
                        } else {
                            potyList.setAdapter(new SeasonAwardsListArrayAdapter(MainActivity.this, allConference[position - 5], userTeam.abbr));
                        }
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        // do nothing
                    }
                });
    }

    private void importData() {
        isExternalStorageReadable();

        //ALERT DIALOG - ROSTER or COACH

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("What type of custom data would you like to import?")
                .setPositiveButton("Coach File", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loadData = "coach";
                        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                        intent.setType("text/plain");
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        startActivityForResult(intent, READ_REQUEST_CODE);
                    }
                })
                .setNegativeButton("Roster File", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loadData = "roster";
                        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                        intent.setType("text/plain");
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        startActivityForResult(intent, READ_REQUEST_CODE);
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //Save File Dialog
    private void saveLeague() {
        AlertDialog.Builder save = new AlertDialog.Builder(this);
        save.setTitle("Choose Save File to Overwrite:");
        final String[] fileInfos = getSaveFileInfos();
        SaveFilesListArrayAdapter saveFilesAdapter = new SaveFilesListArrayAdapter(this, fileInfos);
        save.setAdapter(saveFilesAdapter, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                final int itemy = item;
                // Do something with the selection
                if (fileInfos[itemy].equals("EMPTY")) {
                    // Empty file, don't show dialog confirmation
                    saveLeagueFile = new File(getFilesDir(), "saveFile" + itemy + ".cfb");
                    simLeague.saveLeague(saveLeagueFile);
                    Toast.makeText(MainActivity.this, "Saved league!",
                            Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                } else {
                    // Ask for confirmation to overwrite file
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("Are you sure you want to overwrite this save file?\n\n" + fileInfos[itemy])
                            .setPositiveButton("Yes, Overwrite", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Actually go back to main menu
                                    saveLeagueFile = new File(getFilesDir(), "saveFile" + itemy + ".cfb");
                                    simLeague.saveLeague(saveLeagueFile);
                                    Toast.makeText(MainActivity.this, "Saved league!",
                                            Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Do nothing
                                    dialog.dismiss();
                                }
                            });
                    AlertDialog dialog2 = builder.create();
                    dialog2.show();
                    TextView textView = dialog2.findViewById(android.R.id.message);
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                }
            }
        });
        save.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
            }
        });
        AlertDialog popup = save.create();
        popup.show();
    }

    //Get Save Files from Storage
    private String[] getSaveFileInfos() {
        String[] infos = new String[10];
        String fileInfo;
        File saveFile;
        for (int i = 0; i < 10; ++i) {
            saveFile = new File(getFilesDir(), "saveFile" + i + ".cfb");
            if (saveFile.exists()) {
                try {
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(saveFile));
                    fileInfo = bufferedReader.readLine();
                    infos[i] = fileInfo.substring(0, fileInfo.length() - 1); //gets rid of % at end
                } catch (FileNotFoundException ex) {
                    System.out.println(
                            "Unable to open file");
                } catch (IOException ex) {
                    System.out.println(
                            "Error reading file");
                }
            } else {
                infos[i] = "EMPTY";
            }
        }
        return infos;
    }

    //Exit Current Game
    private void exitMainActivity() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Are you sure you want to return to main menu? Any progress from the beginning of the season will be lost.")
                .setPositiveButton("Yes, Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Actually go back to main menu
                        finish();
                        Intent myIntent = new Intent(MainActivity.this, Home.class);
                        MainActivity.this.startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    //IN-GAME DISPLAYS

    //Career Mode Options
    private void careerModeOptions() {
        AlertDialog.Builder mode = new AlertDialog.Builder(this);
        mode.setMessage("What difficulty would you like?\n\n" +
                "Normal: Choose any team to start your new career.\n\n" +
                "Challenge: Start at the bottom and work your way up.\n\n")
                .setTitle("Choose Difficulty:")
                .setPositiveButton("Challenge", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        newGameHardDialog();
                    }
                })
                .setNegativeButton("Normal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        newGameDialog();
                    }
                });
        mode.setCancelable(false);
        AlertDialog dialog = mode.create();
        dialog.show();
        TextView msgTxt = dialog.findViewById(android.R.id.message);
        msgTxt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
    }

    //Injury Report
    private void showInjuryReportDialog() {
        String[] injuries = userTeam.getInjuryReport();
        if (injuries != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Injury Report")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //do nothing?
                        }
                    })
                    .setNegativeButton("Set Lineup", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Manage Lineup
                            showTeamLineupDialog();
                        }
                    })
                    .setView(getLayoutInflater().inflate(R.layout.injury_report, null));
            AlertDialog dialog = builder.create();
            dialog.show();

            ListView injuryList = dialog.findViewById(R.id.listViewInjuryReport);
            injuryList.setAdapter(new PlayerStatsListArrayAdapter(this, injuries));

            CheckBox showInjuryReportCheckBox = dialog.findViewById(R.id.checkBoxInjuryReport);
            showInjuryReportCheckBox.setChecked(true);

            showInjuryReportCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    //do stuff
                    setShowInjuryReport(isChecked);
                }
            });
        }
    }

    private void setShowInjuryReport(boolean show) {
        showInjuryReport = show;
    }

    //Pre-Season Goals
    private void seasonGoals() {
        simLeague.updateTeamTalentRatings();
        simLeague.setTeamBenchMarks();

        goals = "";
        int confPos = 0;

        for (int i = 0; i < simLeague.conferences.size(); ++i) {
            Conference c = simLeague.conferences.get(i);
            if (c.confName.equals(userTeam.conference)) {
                for (int x = 0; x < c.confTeams.size(); x++) {
                    if (c.confTeams.get(x).name.equals(userTeam.name)) {
                        confPos = x + 1;
                        break;
                    }
                }
            }
        }

        goals = "Welcome to the " + (seasonStart + userTeam.teamHistory.size()) + " College Football season!\n\n";
        goals += "This season your team is projected to finish ranked #" + userTeam.projectedPoll + "!\n\n";

        if (userTeam.projectedPoll > 100) {
            goals += "Despite being projected at #" + userTeam.projectedPoll + ", your goal is to finish in the Top 100.\n\n";
        }

        goals += "In conference play, your team is expected to finish " + userTeam.getRankStr(confPos) + " in the " + userTeam.conference + " conference.\n\n";

        goals += "Based on your schedule, your team is projected to finish with a record of " + userTeam.projectedWins + " - " + (12 - userTeam.projectedWins) + ".\n\n";

        if (simLeague.bonusTeam1 != null && simLeague.bonusTeam1.name.equals(userTeam.name) || simLeague.bonusTeam2 != null && simLeague.bonusTeam2.name.equals(userTeam.name) || simLeague.bonusTeam3 != null && simLeague.bonusTeam3.name.equals(userTeam.name)) {
            goals += "Your team's training facilities were upgraded over the off-season! Prestige has increased!\n\n";
        }

        if (simLeague.penalizedTeam1 != null && simLeague.penalizedTeam1.name.equals(userTeam.name) || simLeague.penalizedTeam2 != null && simLeague.penalizedTeam2.name.equals(userTeam.name)) {
            goals += "Your team had a minor infraction over the off-season and lost some Prestige.\n\n";
        }
        if (simLeague.penalizedTeam3 != null && simLeague.penalizedTeam3.name.equals(userTeam.name) || simLeague.penalizedTeam4 != null && simLeague.penalizedTeam4.name.equals(userTeam.name) || simLeague.penalizedTeam5 != null && simLeague.penalizedTeam5.name.equals(userTeam.name)) {
            goals += "Your team was penalized heavily for off-season issues by the College Athletic Administration and will lose Prestige and suffer a post-season bowl ban this year.\n\n";
        }

        simLeague.newsStories.get(simLeague.currentWeek).add("Season Goals>" + goals);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(goals + "\n\nNote: You can always review your season goals in the Pre-Season News.")
                .setTitle((seasonStart + userTeam.teamHistory.size()) + " Season Goals")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setNegativeButton("SAVE PROGRESS", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        saveLeague();
                    }
                });
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        TextView textView = dialog.findViewById(android.R.id.message);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

    }

    //Pre-Season Options
    //Redshirts, Set Budgets, etc.
    private void preseasonOptions() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("This will let you redshirt and set budgets in the future")
                .setTitle((seasonStart + userTeam.teamHistory.size()) + " Pre-Season")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setNegativeButton("SAVE PROGRESS", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        saveLeague();
                    }
                });
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        TextView textView = dialog.findViewById(android.R.id.message);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
    }

    //mid-season summary
    private void midseasonSummary() {
        String string = "";
        simLeague.midSeasonProgression();
        string = userTeam.midseasonUserProgression();
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(string)
                .setTitle("Mid-Season Progress Report")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
        TextView textView = dialog.findViewById(android.R.id.message);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
    }

    //End of Season Summary
    private void seasonSummary() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(simLeague.seasonSummaryStr() + "\n\nNote: You can always review your season summary in the Off-Season News.")
                .setTitle((seasonStart + userTeam.teamHistory.size()) + " Season Summary")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setNegativeButton("All Prestige Changes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        showPrestigeChange();
                    }
                });
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        TextView textView = dialog.findViewById(android.R.id.message);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

        simLeague.newsStories.get(simLeague.currentWeek + 1).add("Season Summary>" + simLeague.seasonSummaryStr());
    }

    //Show Prestige Change
    private void showPrestigeChange() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Prestige Rankings")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setView(getLayoutInflater().inflate(R.layout.simple_list_dialog, null));
        AlertDialog dialog = builder.create();
        dialog.show();

        final ListView teamRankingsList = dialog.findViewById(R.id.listViewDialog);
        final TeamRankingsListArrayAdapter teamRankingsAdapter =
                new TeamRankingsListArrayAdapter(this, simLeague.getTeamRankingsStr(1), userTeam.name);
        teamRankingsList.setAdapter(teamRankingsAdapter);
    }

    //Contract Status Dialog
    private void contractDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(userTeam.contractString)
                .setTitle((seasonStart + userTeam.teamHistory.size()) + " Contract Status")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setNegativeButton("View Coaching News", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showNewsStoriesDialog();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
        TextView textView = dialog.findViewById(android.R.id.message);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
    }

    //Job Offers Dialog when fired or resignation from previous team
    private void jobOffers(HeadCoach headCoach) {
        userHC = headCoach;
        int ratOvr = userHC.ratOvr;
        if (ratOvr < 40) ratOvr = 40;
        String oldTeam = userHC.team.name;
        updateHeaderBar();
        //get user team from list dialog
        final ArrayList<String> teamsArray = simLeague.getCoachListStrFired((ratOvr - 10), oldTeam);
        final ArrayList<Team> coachList = simLeague.getCoachListFired((ratOvr - 10), oldTeam);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Job Offers Available:");
        builder.setCancelable(false);
        final String[] teams = teamsArray.toArray(new String[teamsArray.size()]);
        builder.setItems(teams, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                userTeam.userControlled = false;
                userTeam.HC.clear();
                simLeague.coachHiringSingleTeam(userTeam);
                simLeague.newJobtransfer(coachList.get(item).name);
                userTeam = simLeague.userTeam;
                userTeamStr = userTeam.name;
                currentTeam = userTeam;
                userTeam.HC.clear();
                userTeam.HC.add(userHC);
                userTeam.fired = false;
                userHC.contractYear = 0;
                userHC.contractLength = 6;
                userHC.baselinePrestige = userTeam.teamPrestige;
                simLeague.newsStories.get(simLeague.currentWeek + 1).add("Coaching Hire: " + currentTeam.name + ">After an extensive search for a new head coach, " + currentTeam.name + " has hired " + userHC.name +
                        " to lead the team.");
                updateHeaderBar();
                examineTeam(currentTeam.name);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    //Job offers from other teams
    private void promotions(HeadCoach headCoach) {
        userHC = headCoach;
        if (userHC.promotionCandidate) {

            int ratOvr = userHC.ratOvr;
            if (ratOvr < 40) ratOvr = 40;
            double offers = 2;
            String oldTeam = userHC.team.name;
            updateHeaderBar();
            //get user team from list dialog
            final ArrayList<String> teamsArray = simLeague.getCoachPromotionListStr((ratOvr - 10), offers, oldTeam);
            final ArrayList<Team> coachList = simLeague.getCoachPromotionList((ratOvr - 10), offers, oldTeam);
            if (coachList.isEmpty()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Job Offers")
                        .setMessage("No job offers available.")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();

            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Job Offers Available:")
                        .setPositiveButton("Decline Offers", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                final String[] teams = teamsArray.toArray(new String[teamsArray.size()]);
                builder.setCancelable(false);
                builder.setItems(teams, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // Do something with the selection
                        userTeam.userControlled = false;
                        userTeam.HC.clear();
                        simLeague.coachHiringSingleTeam(userTeam);
                        simLeague.newJobtransfer(coachList.get(item).name);
                        userTeam = simLeague.userTeam;
                        userTeamStr = userTeam.name;
                        currentTeam = userTeam;
                        userTeam.HC.clear();
                        userTeam.HC.add(userHC);
                        userTeam.fired = false;
                        userHC.contractYear = 0;
                        userHC.contractLength = 6;
                        userHC.baselinePrestige = userTeam.teamPrestige;
                        simLeague.newsStories.get(simLeague.currentWeek + 1).add("Coaching Hire: " + currentTeam.name + ">After an extensive search for a new head coach, " + currentTeam.name + " has hired " + userHC.name +
                                " to lead the team.");
                        simLeague.coachCarousel();
                        updateHeaderBar();
                        examineTeam(currentTeam.name);
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Job Offers")
                    .setMessage("No job offers available. You did not perform well enough to be considered a coaching candidate at other schools.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    //Choose ANY team
    private void switchTeams(HeadCoach headCoach) {
        userHC = headCoach;
        updateHeaderBar();
        //get user team from list dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose your new team:");
        final String[] teams = simLeague.getTeamListStr();
        builder.setItems(teams, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                userTeam.userControlled = false;
                userTeam.HC.clear();
                simLeague.coachHiringSingleTeam(userTeam);
                simLeague.newJobtransfer(simLeague.teamList.get(item).name);
                userTeam = simLeague.userTeam;
                userTeamStr = userTeam.name;
                currentTeam = userTeam;
                userTeam.HC.clear();
                userTeam.HC.add(userHC);
                userTeam.fired = false;
                userHC.contractYear = 0;
                userHC.contractLength = 6;
                userHC.baselinePrestige = userTeam.teamPrestige;
                updateHeaderBar();
                examineTeam(currentTeam.name);
            }
        });
        builder.setCancelable(false);
        AlertDialog alert = builder.create();
        alert.show();
    }

    //Conference Realignment Update
    private void conferenceRealignment() {
        if (simLeague.confRealignment) {
            simLeague.conferenceRealignment();
            if (simLeague.countRealignment > 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage(simLeague.newsRealignment)
                        .setTitle((seasonStart + userTeam.teamHistory.size()) + " Conference Realignment News")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
                TextView textView = dialog.findViewById(android.R.id.message);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            }
        }
    }

    //Promotions & Relegations Update
    private void promotionRelegation() {
        if (simLeague.enableProRel) {
            simLeague.promotionRelegation();
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage(simLeague.newsRealignment)
                    .setTitle((seasonStart + userTeam.teamHistory.size()) + " Promotion/Relegation Update")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
            TextView textView = dialog.findViewById(android.R.id.message);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        }
    }

    //Television Contract News
    private void newsTV() {
        StringBuilder update = new StringBuilder();
        for(int i = 0; i < simLeague.newsTV.size(); ++i) {
            update.append(simLeague.newsTV.get(i) + "\n\n");
        }

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage(update)
                    .setTitle((seasonStart + userTeam.teamHistory.size()) + " Network Contract Updates")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setNegativeButton("Prestige Rankings", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            showPrestigeChange();
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
            TextView textView = dialog.findViewById(android.R.id.message);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
    }

    //Transfers Dialog
    private void transfers() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(simLeague.userTransfers)
                .setTitle((seasonStart + userTeam.teamHistory.size()) + " Transfers")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setNegativeButton("View All Transfers", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                        builder1.setMessage(simLeague.sumTransfers)
                                .setTitle((seasonStart + userTeam.teamHistory.size()) + " Transfers")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                        AlertDialog dialog1 = builder1.create();
                        dialog1.show();
                        TextView textView1 = dialog1.findViewById(android.R.id.message);
                        textView1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
        TextView textView = dialog.findViewById(android.R.id.message);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

    }

    //Recruiting Begins
    private void beginRecruiting() {
        simLeague.recruitPlayers();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(userTeam.abbr + " Players Leaving")
                .setPositiveButton("Begin Recruiting", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        simLeague.currentWeek = 0;
                        saveLeagueFile = new File(getFilesDir(), "saveLeagueRecruiting.cfb");
                        simLeague.saveLeague(saveLeagueFile);

                        //Get String of user team's players and such
                        StringBuilder sb = new StringBuilder();
                        userTeam.sortPlayers();
                        sb.append(userTeam.conference + "," + userTeam.name + "," + userTeam.abbr + "," + userTeam.getUserRecruitLevel() + "," + userTeam.HC.get(0).ratTalent + "%\n");
                        sb.append(userTeam.getPlayerInfoSaveFile());
                        sb.append("END_TEAM_INFO%\n");
                        sb.append(userTeam.getRecruitsInfoSaveFile());

                        //Start Recruiting Activity
                        finish();
                        Intent myIntent = new Intent(MainActivity.this, RecruitingActivity.class);
                        myIntent.putExtra("USER_TEAM_INFO", sb.toString());
                        MainActivity.this.startActivity(myIntent);
                    }
                })
                .setNegativeButton("Back", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                })
                .setView(getLayoutInflater().inflate(R.layout.team_rankings_dialog, null));
        AlertDialog dialog = builder.create();
        dialog.show();
        String[] spinnerSelection = {"Players Leaving", "Pro Mock Draft"};
        Spinner beginRecruitingSpinner = dialog.findViewById(R.id.spinnerTeamRankings);
        ArrayAdapter<String> beginRecruitingSpinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, spinnerSelection);
        beginRecruitingSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        beginRecruitingSpinner.setAdapter(beginRecruitingSpinnerAdapter);

        final ListView playerList = dialog.findViewById(R.id.listViewTeamRankings);
        final PlayerStatsListArrayAdapter playerStatsAdapter =
                new PlayerStatsListArrayAdapter(this, userTeam.getGradPlayersList());
        final MockDraftListArrayAdapter mockDraftAdapter =
                new MockDraftListArrayAdapter(this, simLeague.getMockDraftPlayersList(), userTeam.name);
        playerList.setAdapter(playerStatsAdapter);

        beginRecruitingSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0) {
                            // Players Leaving
                            playerList.setAdapter(playerStatsAdapter);
                        } else {
                            // Mock Draft
                            playerList.setAdapter(mockDraftAdapter);
                        }
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        // do nothing
                    }
                });

    }

    //Recruiting Score
    private void showRecruitingClassDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setTitle("Recruiting Class Rankings")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        seasonGoals();
                    }
                })
                .setView(getLayoutInflater().inflate(R.layout.simple_list_dialog, null));
        AlertDialog dialog = builder.create();
        dialog.show();

        final ListView teamRankingsList = dialog.findViewById(R.id.listViewDialog);
        final TeamRankingsListArrayAdapter teamRankingsAdapter =
                new TeamRankingsListArrayAdapter(this, simLeague.getTeamRankingsStr(15), userTeam.strRepWithPrestige());
        teamRankingsList.setAdapter(teamRankingsAdapter);
    }


    //CUSTOM DATA


    private void customLeague(Uri uri) {
        try {
            File conferences = new File(getFilesDir(), "conferences.txt");
            File teams = new File(getFilesDir(), "teams.txt");
            File bowls = new File(getFilesDir(), "bowls.txt");
            String line;
            InputStream inputStream = getContentResolver().openInputStream(uri);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            //First ignore the save file info
            line = null;
            line = reader.readLine();
            //Next get league history
            sb.append("[START_CONFERENCES]\n");
            while ((line = reader.readLine()) != null && !line.equals("[END_CONFERENCES]")) {
                sb.append(line + "\n");
            }
            sb.append("[END_CONFERENCES]\n");

            // Actually write to the file
            try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(conferences)))) {
                writer.write(sb.toString());
            } catch (Exception e) {
            }
            StringBuilder sb1 = new StringBuilder();

            //teams
            sb1.append("[START_TEAMS]\n");
            while ((line = reader.readLine()) != null && !line.equals("[END_TEAMS]")) {
                sb1.append(line + "\n");
            }
            sb1.append("[END_TEAMS]\n");
            // Actually write to the file
            try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(teams)))) {
                writer.write(sb1.toString());
            } catch (Exception e) {
            }

            StringBuilder sb2 = new StringBuilder();

            line = null;
            line = reader.readLine();
            //Next get league history
            sb2.append(line + "\n");
            sb2.append("[END_BOWL_NAMES]\n");

            // Actually write to the file
            try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(bowls)))) {
                writer.write(sb2.toString());
            } catch (Exception e) {
            }
            // Always close files.
            reader.close();
        } catch (Exception e) {
            Toast.makeText(MainActivity.this, "Error! Bad URL or unable to read file.", Toast.LENGTH_SHORT).show();
        }
    }

    // Checks if external storage is available for read and write *//*
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    //* Checks if external storage is available to at least read *//*
    private boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    //* Creates external Save directory *//*

    public File getExtSaveDir(Context context, String cfbCoach) {
        // Get the directory for the app's private pictures directory.
        File file = new File(context.getExternalFilesDir(
                Environment.DIRECTORY_DOCUMENTS), cfbCoach);
        if (!file.mkdirs()) {
            Log.e(cfbCoach, "Directory not created");
        }
        return file;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {

        // The ACTION_OPEN_DOCUMENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.

        if (requestCode == READ_REQUEST_CODE && resultCode == Home.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            // provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            if (resultData != null) {
                dataUri = null;
                dataUri = resultData.getData();
                try {
                    if (loadData.equals("coach")) {
                        readCoachFile(dataUri);
                    } else if (loadData.equals("roster")) {
                        readRosterFile(dataUri);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void readCoachFile(Uri uri) throws IOException {
        String line;
        InputStream inputStream = getContentResolver().openInputStream(uri);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        line = reader.readLine();
        //First ignore the save file info
        while ((line = reader.readLine()) != null && !line.equals("END_COACHES")) {
            String[] fileSplit = line.split(",");
            for (int i = 0; i < simLeague.teamList.size(); ++i) {
                if (fileSplit[0].equals(simLeague.teamList.get(i).name)) {
                    if (fileSplit.length > 2) {
                        simLeague.teamList.get(i).HC.clear();
                        simLeague.teamList.get(i).newCustomHeadCoach(fileSplit[1], Integer.parseInt(fileSplit[2]));
                    } else {
                        simLeague.teamList.get(i).HC.get(0).name = fileSplit[1];
                    }
                }
            }
        }
        reader.close();
        userTeam.setupUserCoach(userHC.name);
        currTab = 3;
        updatePlayerStats();
    }

    private void readRosterFile(Uri uri) throws IOException {
        boolean custom = false;

        //METHOD USED FOR CREATING NEW ROSTER FROM CUSTOM FILE
        for (int i = 0; i < simLeague.teamList.size(); ++i) {
            Team teamRoster = simLeague.teamList.get(i);
            teamRoster.teamQBs.clear();
            teamRoster.teamRBs.clear();
            teamRoster.teamWRs.clear();
            teamRoster.teamTEs.clear();
            teamRoster.teamOLs.clear();
            teamRoster.teamKs.clear();
            teamRoster.teamDLs.clear();
            teamRoster.teamLBs.clear();
            teamRoster.teamCBs.clear();
            teamRoster.teamSs.clear();
        }

        String line;
        InputStream inputStream = getContentResolver().openInputStream(uri);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        line = reader.readLine();
        //First ignore the save file info
        int qb, rb, wr, te, ol, k, dl, lb, cb, s;
        String teamX = "No Team";
        while ((line = reader.readLine()) != null && !line.equals("END_ROSTER")) {
            String[] fileSplit = line.split(",");


            //METHOD FOR CREATING NEW ROSTER WITH CUSTOM FILE
            for (int i = 0; i < simLeague.teamList.size(); ++i) {
                if (fileSplit[0].equals(simLeague.teamList.get(i).name)) {
                    teamX = simLeague.teamList.get(i).name;
                    Team teamRoster = simLeague.teamList.get(i);

                    if (fileSplit[2].equals("QB")) {
                        teamRoster.teamQBs.add(new PlayerQB(fileSplit[1], Integer.parseInt(fileSplit[3]), Integer.parseInt(fileSplit[4]), teamRoster, custom));
                    } else if (fileSplit[2].equals("RB")) {
                        teamRoster.teamRBs.add(new PlayerRB(fileSplit[1], Integer.parseInt(fileSplit[3]), Integer.parseInt(fileSplit[4]), teamRoster, custom));
                    } else if (fileSplit[2].equals("WR")) {
                        teamRoster.teamWRs.add(new PlayerWR(fileSplit[1], Integer.parseInt(fileSplit[3]), Integer.parseInt(fileSplit[4]), teamRoster, custom));
                    } else if (fileSplit[2].equals("TE")) {
                        teamRoster.teamTEs.add(new PlayerTE(fileSplit[1], Integer.parseInt(fileSplit[3]), Integer.parseInt(fileSplit[4]), teamRoster, custom));
                    } else if (fileSplit[2].equals("OL")) {
                        teamRoster.teamOLs.add(new PlayerOL(fileSplit[1], Integer.parseInt(fileSplit[3]), Integer.parseInt(fileSplit[4]), teamRoster, custom));
                    } else if (fileSplit[2].equals("DL")) {
                        teamRoster.teamDLs.add(new PlayerDL(fileSplit[1], Integer.parseInt(fileSplit[3]), Integer.parseInt(fileSplit[4]), teamRoster, custom));
                    } else if (fileSplit[2].equals("LB")) {
                        teamRoster.teamLBs.add(new PlayerLB(fileSplit[1], Integer.parseInt(fileSplit[3]), Integer.parseInt(fileSplit[4]), teamRoster, custom));
                    } else if (fileSplit[2].equals("CB")) {
                        teamRoster.teamCBs.add(new PlayerCB(fileSplit[1], Integer.parseInt(fileSplit[3]), Integer.parseInt(fileSplit[4]), teamRoster, custom));
                    } else if (fileSplit[2].equals("S")) {
                        teamRoster.teamSs.add(new PlayerS(fileSplit[1], Integer.parseInt(fileSplit[3]), Integer.parseInt(fileSplit[4]), teamRoster, custom));
                    } else if (fileSplit[2].equals("K")) {
                        teamRoster.teamKs.add(new PlayerK(fileSplit[1], Integer.parseInt(fileSplit[3]), Integer.parseInt(fileSplit[4]), teamRoster, custom));
                    }
                }
            }

        }
        reader.close();

        for (int i = 0; i < simLeague.teamList.size(); ++i) {
            Team teamRoster = simLeague.teamList.get(i);
            if (teamRoster.getAllPlayers().isEmpty()) {
                teamRoster.newRoster(teamRoster.minQBs, teamRoster.minRBs, teamRoster.minWRs, teamRoster.minTEs, teamRoster.minOLs, teamRoster.minKs, teamRoster.minDLs, teamRoster.minLBs, teamRoster.minCBs, teamRoster.minSs);
            }
        }

        for (int i = 0; i < simLeague.teamList.size(); ++i) {
            Team teamRoster = simLeague.teamList.get(i);
            teamRoster.recruitWalkOns();
        }
        simLeague.updateTeamTalentRatings();
        simLeague.newsStories.get(0).remove(3);
        simLeague.newsStories.get(0).remove(3);
        simLeague.topRecruits();
        currTab = 3;
        updatePlayerStats();
    }


    //MISC STUFF

    public boolean isNameValid(String name) {
        if (name.split(" ").length < 2) {
            return false;
        }
        return !(name.contains(",") || name.contains(">") || name.contains("%") || name.contains("\\"));
    }

    @Override
    public void onBackPressed() {
        exitMainActivity();
    }

    //GAME EDITOR V2
/*
    public void gameEditorV2() {
        final List<String> teamEditor = new ArrayList<>();
        final List<String> confEditor = new ArrayList<>();
        final Conference editConference = new Conference("Name", simLeague);
        final Team editTeam = new Team("Name", "abbr", "Conf", 50, 2, simLeague);

        AlertDialog.Builder GameEditor = new AlertDialog.Builder(this);
        GameEditor.setTitle("Game Universe Editor")
                .setView(getLayoutInflater().inflate(R.layout.game_editor_full, null));
        final AlertDialog dialog = GameEditor.create();
        dialog.show();

        final Spinner confList = dialog.findViewById(R.id.confList);
        final Spinner teamList = dialog.findViewById(R.id.teamList);
        final EditText changeNameEditText = dialog.findViewById(R.id.editTextChangeName);
        final EditText changeAbbrEditText = dialog.findViewById(R.id.editTextChangeAbbr);
        final EditText changeConfEditText = dialog.findViewById(R.id.editTextChangeConf);
        final EditText changeHCEditText = dialog.findViewById(R.id.editTextChangeHC);

        for (int i = 0; i < confStart; i++) {
            confEditor.add(simLeague.conferences.get(i).confName);
        }
        for (int i = 0; i < simLeague.conferences.get(0).confTeams.size(); i++) {
            teamEditor.add(simLeague.conferences.get(0).confTeams.get(i).name);
        }

        changeNameEditText.setText(currentTeam.name);  //updated from userTeam to currentTeam
        changeAbbrEditText.setText(currentTeam.abbr);   //updated from userTeam to currentTeam
        changeConfEditText.setText(currentConference.confName);   //updated from userTeam to currentTeam
        changeHCEditText.setText(currentTeam.HC.get(0).name);   //change Head Coach Name

        ArrayAdapter<String> editorAdaptorConf = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, confEditor);
        editorAdaptorConf.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        confList.setAdapter(editorAdaptorConf);
        confList.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        editConference = simLeague.conferences.get(position);
                        for (int i = 0; i < simLeague.conferences.get(position).confTeams.size(); i++) {
                            teamEditor.add(simLeague.conferences.get(position).confTeams.get(i).name);
                        }
                        teamList.notify();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

        ArrayAdapter<String> editorAdaptorTeam = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, teamEditor);
        editorAdaptorTeam.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        teamList.setAdapter(editorAdaptorTeam);
        teamList.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        Team tm = simLeague.conferences.get(position).confTeams.get(position);
                        changeNameEditText.setText(.name);  //updated from userTeam to currentTeam
                        changeAbbrEditText.setText(currentTeam.abbr);   //updated from userTeam to currentTeam
                        changeConfEditText.setText(currentConference.confName);   //updated from userTeam to currentTeam
                        changeHCEditText.setText(currentTeam.HC.get(0).name);   //change Head Coach Name
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });




        Button cancelChangeNameButton = dialog.findViewById(R.id.buttonCancelChangeName);
        Button okChangeNameButton = dialog.findViewById(R.id.buttonOkChangeName);

        cancelChangeNameButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                dialog.dismiss();
            }
        });

        okChangeNameButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                String newName = changeNameEditText.getText().toString().trim();
                String newAbbr = changeAbbrEditText.getText().toString().trim().toUpperCase();
                String newConf = changeConfEditText.getText().toString().trim();
                String newHC = changeHCEditText.getText().toString().trim();

                if (simLeague.isNameValid(newName) && simLeague.isAbbrValid(newAbbr) && simLeague.isNameValid(newConf) && isNameValid((newHC))) {
                    simLeague.changeAbbrHistoryRecords(currentTeam.abbr, newAbbr);
                    currentTeam.name = newName; //set new team name
                    currentTeam.abbr = newAbbr; //set new conference name
                    oldConf = currentConference.confName;
                    currentConference.confName = newConf;
                    currentTeam.HC.get(0).name = newHC;
                    simLeague.updateTeamConf(newConf, oldConf, currentConferenceID);  //update all other conf teams
                    getSupportActionBar().setTitle(season + " | " + userTeam.name);
                    Team rival = simLeague.findTeamAbbr(currentTeam.rivalTeam);  // Have to update rival's rival too!
                    rival.rivalTeam = currentTeam.abbr;
                    wantUpdateConf = true;
                    updateCurrConference();  //updates the UI
                    examineTeam(currentTeam.name);

                    dialog.dismiss();

                } else {
                    if (showToasts)
                        Toast.makeText(MainActivity.this, "Invalid name/abbr! Name not changed.",
                                Toast.LENGTH_SHORT).show();
                }

            }
        });
    }*/

    //allow the ability to enable editor to edit player names, positions, attributes, etc.
    private void playerEditorP() {

    }

    //DEBUG STUFF
    private void netTotalPrestige() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(simLeague.netTotalPrestige() + " Total Net Prestige Change")
                .setTitle((seasonStart + userTeam.teamHistory.size()) + " Prestige Change")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
        TextView textView = dialog.findViewById(android.R.id.message);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
    }
}