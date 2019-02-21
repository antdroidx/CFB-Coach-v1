package antdroid.cfbcoach;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
    private HeadCoach userHC;
    private int season;
    private League simLeague;
    private Conference currentConference;
    private Team currentTeam;
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

    private ArrayList<Team> jobList;
    private int jobType;
    private boolean jobListSet;

    private boolean wantUpdateConf;
    private boolean showToasts;
    private boolean showInjuryReport;
    private boolean redshirtComplete;
    private boolean budgetsComplete;
    private boolean newGame;
    private boolean skipRetirementQ;
    private boolean reincarnate;

    //Universe Settings
    private final int seasonStart = 2018;
    private final int retireAge = 67;

    String saveLeagueFileStr;
    private File customConfs;
    private File customTeams;
    private File customBowls;
    private String customUri;

    private final DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
    private final DecimalFormat df2 = new DecimalFormat("#.##");

    public int theme;

    private boolean loadedLeague = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        theme =  Integer.parseInt(extras.get("Theme").toString());
        if(theme == 1) setTheme(R.style.AppThemeLight);
        else setTheme(R.style.AppTheme);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Set up list
        mainList = findViewById(R.id.mainList);
        expListPlayerStats = findViewById(R.id.playerStatsExpandList);
        jobList = new ArrayList<>();

        //Load Data
        loadGame(extras);


        wantUpdateConf = true; // 0 and 1, don't update, 2 update
        showToasts = true;
        showInjuryReport = true;

        try {
            if (!loadedLeague) {
                // Set it to 1st team until one selected
                userTeam = simLeague.teamList.get(0);
                simLeague.userTeam = userTeam;
                userTeam.userControlled = true;
                userTeamStr = userTeam.name;
                currentTeam = userTeam;
                currentTeam = simLeague.teamList.get(0);
                currentConference = simLeague.conferences.get(0);

                String saveFileStr = extras.getString("SAVE_FILE");
                if (saveFileStr.contains("CUSTOM")) importDataPrompt();
                else careerModeOptions();
            }

        } catch (Exception ex) {
            System.out.println(
                    "Error reading file");
            ex.printStackTrace();
            crash();
            return;
        }

        // Set toolbar text
        updateHeaderBar();

        //Team Name Display
        final TextView currentTeamText = findViewById(R.id.currentTeamText);
        currentTeamText.setText(currentTeam.name + " (" + currentTeam.wins + "-" + currentTeam.losses + ")");

        //Set up spinner for examining team.
        examineConfSpinner = findViewById(R.id.examineConfSpinner);
        confList = new ArrayList<>();
        for (int i = 0; i < simLeague.conferences.size(); i++) {
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
                        updateCurrConference();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        //heh
                    }
                });

        examineTeamSpinner = findViewById(R.id.examineTeamSpinner);
        teamList = new ArrayList<>();
        for (int i = 0; i < simLeague.teamList.size(); i++) {
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
                //updatePlayerStats();
                viewRoster();
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
        Button depthchartButton = findViewById(R.id.buttonDepthChart);
        if (!redshirtComplete) {
            depthchartButton.setText("REDSHIRT");
            depthchartButton.setBackgroundColor(Color.RED);
        }

        depthchartButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                currentTeam = userTeam;
                if (!redshirtComplete) redshirtDialog();
                else depthChartDialog();
            }
        });

        //Strategy/Playbook
        final Button strategyButton = findViewById(R.id.buttonStrategy);
        strategyButton.setBackgroundColor(0XFF607D8B);
        strategyButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                currentTeam = userTeam;
                showTeamStrategyDialog();
            }
        });

        //Simulate Week Button
        final Button simGameButton = findViewById(R.id.simGameButton);
        simGameButton.setText("START SEASON");
        simGameButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                simulateWeek();
            }
        });


        if (loadedLeague) {
            // Set rankings so that not everyone is rank #0
            simLeague.setTeamRanks();
            examineTeam(userTeam.name);
            showToasts = userTeam.showPopups;
        }

        if (simLeague.getYear() != seasonStart) {
            // Only show recruiting classes if not season 1
            showRecruitingClassDialog();
        }
    }

    private void loadGame(Bundle extras) {
        if (extras != null) {
            String saveFileStr = extras.getString("SAVE_FILE");

            //NEW DYNASTY GAME
            if (saveFileStr.contains("NEW_LEAGUE")) {
                //NEW DYNASTY GAME WITH CUSTOM DATABASE
                if (saveFileStr.contains("CUSTOM")) {
                    newGame = true;
                    String[] filesSplit = saveFileStr.split(",");
                    this.customUri = filesSplit[1];
                    customConfs = new File(getFilesDir(), "conferences.txt");
                    customTeams = new File(getFilesDir(), "teams.txt");
                    customBowls = new File(getFilesDir(), "bowls.txt");
                    Uri uri = Uri.parse(customUri);
                    customLeague(uri);
                    if (saveFileStr.contains("RANDOM"))
                        simLeague = new League(getString(R.string.league_player_names), getString(R.string.league_last_names), customConfs, customTeams, customBowls, true, false, this);
                    else if (saveFileStr.contains("EQUALIZE"))
                        simLeague = new League(getString(R.string.league_player_names), getString(R.string.league_last_names), customConfs, customTeams, customBowls, false, true, this);
                    else
                        simLeague = new League(getString(R.string.league_player_names), getString(R.string.league_last_names), customConfs, customTeams, customBowls, false, false, this);
                    season = seasonStart;

                    //NEW DYNASTY DEFAULT DATABASE
                } else if (saveFileStr.contains("RANDOM")) {
                    simLeague = new League(getString(R.string.league_player_names), getString(R.string.league_last_names), getString(R.string.conferences), getString(R.string.teams), getString(R.string.bowls), true, false);
                    season = seasonStart;

                } else if (saveFileStr.contains("EQUALIZE")) {
                    simLeague = new League(getString(R.string.league_player_names), getString(R.string.league_last_names), getString(R.string.conferences), getString(R.string.teams), getString(R.string.bowls), false, true);
                    season = seasonStart;

                } else {
                    simLeague = new League(getString(R.string.league_player_names), getString(R.string.league_last_names), getString(R.string.conferences), getString(R.string.teams), getString(R.string.bowls), false, false);
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
                //Import Save
            } else if (saveFileStr.contains("IMPORT"))  {
                String[] filesSplit = saveFileStr.split(",");
                Uri uri = Uri.parse(filesSplit[1]);
                InputStream inputStream = null;
                try {
                    inputStream = getContentResolver().openInputStream(uri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                simLeague = new League(inputStream, getString(R.string.league_player_names), getString(R.string.league_last_names), this);
                userTeam = simLeague.userTeam;
                userTeamStr = userTeam.name;
                simLeague.updateTeamTalentRatings();
                season = simLeague.getYear();
                currentTeam = userTeam;
                loadedLeague = true;

                //LOADS A SAVE GAME
            } else if (saveFileStr.contains("FIX")) {
                File saveFile = new File(getFilesDir(), saveFileStr.split(",")[0]);
                simLeague = new League(saveFile, getString(R.string.league_player_names), getString(R.string.league_last_names), true, this);
                userTeam = simLeague.userTeam;
                userTeamStr = userTeam.name;
                simLeague.updateTeamTalentRatings();
                season = simLeague.getYear();
                currentTeam = userTeam;
                loadedLeague = true;
                if(season == seasonStart) seasonGoals();
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
                    if(season == seasonStart) seasonGoals();
                }
            }
        } else {
            //STARTS A NEW GAME WITH NO EXTRAS - NOT USED CURRENTLY
            simLeague = new League(getString(R.string.league_player_names), getString(R.string.league_last_names), getString(R.string.conferences), getString(R.string.teams), getString(R.string.bowls), false, false);
            season = seasonStart;
        }
    }

    //Update Header Bar
    private void updateHeaderBar() {
        if (simLeague.isCareerMode()) {
            getSupportActionBar().setTitle(season + " | " + userTeam.name);
        } else {
            getSupportActionBar().setTitle(season + " | " + userTeam.name);
        }
    }

    private void selectTeam() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose your team:");
        final String[] teams = simLeague.getTeamListStr();
        builder.setItems(teams, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                simLeague.teamList.get(item).HC.get(0).team = null;
                simLeague.coachFreeAgents.add(simLeague.teamList.get(item).HC.get(0));
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
                    dialog.dismiss();
                    setupCoachStyle();
                } else {
                    if (showToasts)
                        Toast.makeText(MainActivity.this, "Invalid name/abbr! Name not changed.",
                                Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setupCoachStyle() {

        String[] coachChoice = {"Balanced", "Defensive-Minded", "Offensive-Minded", "Graduate Assistant (HARD)"};
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder
                .setTitle("Choose your Coaching Style")
                .setSingleChoiceItems(coachChoice, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(i == 0) {
                            setupCoachBal();
                            if (!newGame) seasonGoals();
                        }
                        if(i == 1) {
                            setupCoachDef();
                            if (!newGame) seasonGoals();
                        }
                        if(i == 2) {
                            setupCoachOff();
                            if (!newGame) seasonGoals();
                        }
                        if(i == 3) {
                            setupCoachHard();
                            if (!newGame) seasonGoals();
                        }
                        dialogInterface.dismiss();
                    }
                });

        final AlertDialog dialog = builder.create();
        builder.setCancelable(false);
        dialog.show();

    }

    private void setupCoachOff() {
        userTeam.getHC(0).ratOff = simLeague.getAvgCoachOff()+5;
        userTeam.getHC(0).ratDef = simLeague.getAvgCoachDef()-5;
        userTeam.getHC(0).ratOvr = userTeam.getHC(0).getHCOverall();
        viewRoster();
    }

    private void setupCoachDef() {
        userTeam.getHC(0).ratOff = simLeague.getAvgCoachOff()-5;
        userTeam.getHC(0).ratDef = simLeague.getAvgCoachDef()+5;
        userTeam.getHC(0).ratOvr = userTeam.getHC(0).getHCOverall();
        viewRoster();
    }

    private void setupCoachBal() {
        userTeam.getHC(0).ratOff = simLeague.getAvgCoachOff();
        userTeam.getHC(0).ratDef = simLeague.getAvgCoachDef();
        userTeam.getHC(0).ratOvr = userTeam.getHC(0).getHCOverall();
        viewRoster();
    }

    private void setupCoachHard() {
        userTeam.getHC(0).ratOff = 50;
        userTeam.getHC(0).ratDef = 50;
        userTeam.getHC(0).ratDiscipline = 60;
        userTeam.getHC(0).ratTalent = 50;
        userTeam.getHC(0).ratOvr = userTeam.getHC(0).getHCOverall();
        viewRoster();
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
            } else if (spinnerSplit.length == 3 && (spinnerSplit[1] + " " + spinnerSplit[2]).equals(tempT.name)) {
                examineTeamSpinner.setSelection(i);
                currentTeam = tempT;
                TextView currentTeamText = findViewById(R.id.currentTeamText);
                currentTeamText.setText("#" + currentTeam.rankTeamPollScore +
                        " " + currentTeam.name + " (" + currentTeam.wins + "-" + currentTeam.losses + ") " +
                        currentTeam.confChampion + " " + currentTeam.semiFinalWL + currentTeam.natChampWL);
                break;
            } else if (spinnerSplit.length == 4 && (spinnerSplit[1] + " " + spinnerSplit[2] + " " + spinnerSplit[3]).equals(tempT.name)) {
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
            viewRoster();
        }

    }

    private void updateCurrTeam() {
        teamList = new ArrayList<>();
        dataAdapterTeam.clear();
        for (int i = 0; i < currentConference.confTeams.size() ; i++) {
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
            viewRoster();
        }

    }

    private void updateCurrConference() {
        confList.clear();
        for (int i = 0; i < simLeague.conferences.size(); i++) {
            confList.add(simLeague.conferences.get(i).confName);
        }
        dataAdapterConf.notifyDataSetChanged();

        if (wantUpdateConf) {
            teamList = new ArrayList<>();
            dataAdapterTeam.clear();
            for (int i = 0; i < currentConference.confTeams.size() ; i++) {
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
            else if (i == simLeague.regSeasonWeeks) weekSelection[i] = "Conf Champ Week"; //was 13
            else if (i == simLeague.regSeasonWeeks+1) weekSelection[i] = "Bowl Week 1";
            else if (i == simLeague.regSeasonWeeks+2) weekSelection[i] = "Bowl Week 2";
            else if (i == simLeague.regSeasonWeeks+3) weekSelection[i] = "Bowl Week 3";
            else if (i == simLeague.regSeasonWeeks+4) weekSelection[i] = "National Champ";
            else if (i == simLeague.regSeasonWeeks+5) weekSelection[i] = "Off-Season News";
            else if (i == simLeague.regSeasonWeeks+6) weekSelection[i] = "Coaching Contracts";
            else if (i == simLeague.regSeasonWeeks+7) weekSelection[i] = "Off-Season News";
            else if (i == simLeague.regSeasonWeeks+8) weekSelection[i] = "Coach Hirings";
            else if (i == simLeague.regSeasonWeeks+9) weekSelection[i] = "Roster News";
            else if (i == simLeague.regSeasonWeeks+10) weekSelection[i] = "Transfer News";
            else if (i == simLeague.regSeasonWeeks+11) weekSelection[i] = "Recruiting News";
            else weekSelection[i] = "Week " + i;
        }
        Spinner weekSelectionSpinner = dialog.findViewById(R.id.spinnerTeamRankings);
        ArrayAdapter<String> weekSelectionSpinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, weekSelection);
        weekSelectionSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weekSelectionSpinner.setAdapter(weekSelectionSpinnerAdapter);
        if (simLeague.currentWeek > simLeague.regSeasonWeeks+5 && simLeague.currentWeek < simLeague.regSeasonWeeks+7) {
            weekSelectionSpinner.setSelection(simLeague.currentWeek);
        } else {
            weekSelectionSpinner.setSelection(simLeague.currentWeek);
        }

        final ListView newsStoriesList = dialog.findViewById(R.id.listViewTeamRankings);
        final NewsStories newsStoriesAdapter = new NewsStories(this, rankings);
        newsStoriesList.setAdapter(newsStoriesAdapter);

        weekSelectionSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        ArrayList<String> rankings = simLeague.newsStories.get(position);
                        boolean isempty = false;
                        if (simLeague.currentWeek == simLeague.regSeasonWeeks+11 && rankings.size() == 0) {
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
        mainList.setAdapter(new TeamStatsList(this, teamStatsStr));
    }

    //Player Stats
    //Roster 2.0
    private void viewRoster() {
        mainList.setVisibility(View.VISIBLE);
        expListPlayerStats.setVisibility(View.GONE);

        ArrayList<String> roster;
        roster = currentTeam.getRoster();

        final TeamRoster teamRoster = new TeamRoster(this, roster, this, simLeague.currentWeek);
        mainList.setAdapter(teamRoster);
    }

    //Open Player Profile
    public void examinePlayer(String player) {
        Player p = currentTeam.findTeamPlayer(player);
        if (p == null) {

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            ArrayList<String> pStatsList = p.getDetailAllStatsList();

            String[] pStatsArray = pStatsList.toArray(new String[pStatsList.size()]);
            PlayerProfile pStatsAdapter = new PlayerProfile(this, pStatsArray);
            builder.setAdapter(pStatsAdapter, null)
                    .setTitle(p.position + " " + p.name + ", " + p.getYrStr())
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //do nothing
                        }
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    public void openPlayerProfile(String player, String teamAbbr) {
        Team tempTeam = simLeague.findTeamAbbr(teamAbbr);
        Player p = tempTeam.findTeamPlayer(player);
        if (p == null) {

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            ArrayList<String> pStatsList = p.getDetailAllStatsList();

            String[] pStatsArray = pStatsList.toArray(new String[pStatsList.size()]);
            PlayerProfile pStatsAdapter = new PlayerProfile(this, pStatsArray);
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
    }

    //Open Coach Profile from Database
    public void examineCoachDB(String player) {
        final HeadCoach p = findCoachProfile(player);
        if (p == null) {

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            ArrayList<String> pStatsList;
            if(p.team == null) {
                pStatsList = p.getCareerStatsList();
            } else {
                pStatsList = p.getDetailAllStatsList();
            }

            String[] pStatsArray = pStatsList.toArray(new String[pStatsList.size()]);
            PlayerProfile pStatsAdapter = new PlayerProfile(this, pStatsArray);
            builder.setAdapter(pStatsAdapter, null)
                    .setTitle(p.position + " " + p.name)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //do nothing
                        }
                    })
                    .setNeutralButton("Coach History", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            showCoachHistoryDialog(p);
                        }
                    });

            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }

    public HeadCoach findCoachProfile(String name) {
        HeadCoach p = null;
        String[] nameSplit = name.split(" ");
        String nameHC = nameSplit[0] + " " + nameSplit[1];

        for(int i = 0; i < simLeague.teamList.size(); i++) {
            if(simLeague.teamList.get(i).HC.size() > 0 && simLeague.teamList.get(i).HC.get(0).name.equals(nameHC)) return simLeague.teamList.get(i).HC.get(0);
        }

        for(int i = 0; i < simLeague.coachDatabase.size(); i++) {
            if(simLeague.coachDatabase.get(i).name.equals(nameHC)) return simLeague.coachDatabase.get(i);
        }

        return p;
    }

    //Player Awards for Bio
    public int checkAwardPlayer(String player) {
        Player p = currentTeam.findTeamPlayer(player);
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
        mainList.setAdapter(new GameScheduleList(this, this, currentTeam, games));
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
            selection[0] = "menu >> Game Summary";
            selection[1] = "menu >> Offense Stats";
            selection[2] = "menu >> Defense Stats";
            selection[3] = "menu >> Special Teams Stats";
            selection[4] = "menu >> Game Play Log";

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
        if (simLeague.currentWeek + 2 <= simLeague.regSeasonWeeks+5 && simLeague.currentWeek+1 <= simLeague.regSeasonWeeks) dbSize = simLeague.currentWeek + 2;
        else dbSize = simLeague.regSeasonWeeks+5;

        String[] weekSelection = new String[dbSize];
        for (int i = 0; i < weekSelection.length; ++i) {
            if (i == simLeague.regSeasonWeeks) weekSelection[i] = "Conf Champ Week";
            else if (i == simLeague.regSeasonWeeks+1 && !simLeague.expPlayoffs) weekSelection[i] = "Bowl Week 1";
            else if (i == simLeague.regSeasonWeeks+2 && !simLeague.expPlayoffs) weekSelection[i] = "Bowl Week 2";
            else if (i == simLeague.regSeasonWeeks+3 && !simLeague.expPlayoffs) weekSelection[i] = "Bowl Week 3";
            else if (i == simLeague.regSeasonWeeks+1) weekSelection[i] = "Sweet 16";
            else if (i == simLeague.regSeasonWeeks+2) weekSelection[i] = "Elite 8";
            else if (i == simLeague.regSeasonWeeks+3) weekSelection[i] = "Final Four";
            else if (i == simLeague.regSeasonWeeks+4) weekSelection[i] = "National Champ";
            else weekSelection[i] = "Week " + i;
        }

        Spinner weekSelectionSpinner = dialog.findViewById(R.id.spinnerTeamRankings);
        ArrayAdapter<String> weekSelectionSpinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, weekSelection);
        weekSelectionSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weekSelectionSpinner.setAdapter(weekSelectionSpinnerAdapter);

        int psweek = simLeague.currentWeek;
        if(psweek > simLeague.regSeasonWeeks+4) psweek = simLeague.regSeasonWeeks+4;
        if(simLeague.currentWeek+2 <= simLeague.regSeasonWeeks) weekSelectionSpinner.setSelection(dbSize - 2);
        else weekSelectionSpinner.setSelection(psweek);

        final ListView newsStoriesList = dialog.findViewById(R.id.listViewTeamRankings);
        final NewsStories weeklyScoresAdapter = new NewsStories(this, rankings);
        newsStoriesList.setAdapter(weeklyScoresAdapter);

        weekSelectionSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        ArrayList<String> scores = simLeague.weeklyScores.get(position);
                        boolean isempty = false;
                        if (scores.size() == 0) {
                            isempty = true;
                            scores.add(" > ");
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
    private void depthChartDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set Team Lineup")
                .setView(getLayoutInflater().inflate(R.layout.team_lineup_dialog, null));
        final AlertDialog dialog = builder.create();
        dialog.show();

        final String[] positionSelection = {"Quarterbacks", "Running Backs", "Wide Receivers", "Tight Ends", "Off Linemen",
                "Kickers", "Def Linemen", "Linebackers", "Cornerbacks", "Safeties"};
        final int[] positionNumberRequired = {userTeam.startersQB, userTeam.startersRB, userTeam.startersWR, userTeam.startersTE, userTeam.startersOL, userTeam.startersK, userTeam.startersDL, userTeam.startersLB, userTeam.startersCB, userTeam.startersS};
        final Spinner teamLineupPositionSpinner = dialog.findViewById(R.id.spinnerTeamLineupPosition);
        ArrayAdapter<String> teamLineupPositionSpinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, positionSelection);
        teamLineupPositionSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        teamLineupPositionSpinner.setAdapter(teamLineupPositionSpinnerAdapter);

        final TextView minPlayersText = dialog.findViewById(R.id.textMinPlayers);

        // Text to show what each attr is
        final TextView textLineupPositionDescription = dialog.findViewById(R.id.textViewLineupPositionDescription);

        // List of team's players for selected position
        final ArrayList<Player> positionPlayers = new ArrayList<>();
        positionPlayers.addAll(userTeam.teamQBs);

        final ListView teamPositionList = dialog.findViewById(R.id.listViewTeamLineup);
        final DepthChart teamLineupAdapter = new DepthChart(this, positionPlayers, 1);
        teamPositionList.setAdapter(teamLineupAdapter);

        teamLineupPositionSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {

                        minPlayersText.setText("Starters: " + positionNumberRequired[position]);

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
    private void updateLineupList(int position, DepthChart teamLineupAdapter, int[] positionNumberRequired,
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

    //Depth Chart
    private void redshirtDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Redshirt Players")
                .setView(getLayoutInflater().inflate(R.layout.team_lineup_dialog, null));
        final AlertDialog dialog = builder.create();
        dialog.show();

        final String[] positionSelection = {"Quarterbacks", "Running Backs", "Wide Receivers", "Tight Ends", "Off Linemen",
                "Kickers", "Def Linemen", "Linebackers", "Cornerbacks", "Safeties"};
        final int[] positionNumberRequired = {userTeam.minQBs, userTeam.minRBs, userTeam.minWRs, userTeam.minTEs, userTeam.minOLs, userTeam.minKs, userTeam.minDLs, userTeam.minLBs, userTeam.minCBs, userTeam.minSs};
        final Spinner teamLineupPositionSpinner = dialog.findViewById(R.id.spinnerTeamLineupPosition);
        ArrayAdapter<String> teamLineupPositionSpinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, positionSelection);
        teamLineupPositionSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        teamLineupPositionSpinner.setAdapter(teamLineupPositionSpinnerAdapter);

        final TextView minPlayersText = dialog.findViewById(R.id.textMinPlayers);

        // Text to show what each attr is
        final TextView textLineupPositionDescription = dialog.findViewById(R.id.textViewLineupPositionDescription);

        // List of team's players for selected position
        final ArrayList<Player> positionPlayers = new ArrayList<>();
        positionPlayers.addAll(userTeam.teamQBs);

        final ListView teamPositionList = dialog.findViewById(R.id.listViewTeamLineup);
        final RedshirtAdapter redshirtSelector = new RedshirtAdapter(this, positionPlayers, 1);
        teamPositionList.setAdapter(redshirtSelector);

        teamLineupPositionSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {

                        minPlayersText.setText("Min Active: " + positionNumberRequired[position] + " Current Active: " + userTeam.getActivePlayers(position));
                        redshirtLineup(position, redshirtSelector, positionNumberRequired, positionPlayers, textLineupPositionDescription);
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        // do nothing
                    }
                });

        Button saveLineupsButton = dialog.findViewById(R.id.buttonSaveLineups);
        saveLineupsButton.setText("REDSHIRT PLAYERS");
        Button doneWithLineupsButton = dialog.findViewById(R.id.buttonDoneWithLineups);

        doneWithLineupsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Button depthchartButton = findViewById(R.id.buttonDepthChart);
                if (!redshirtComplete) depthchartButton.setText("SET REDSHIRTS");
                dialog.dismiss();
                updateCurrTeam();
            }
        });

        saveLineupsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Save the lineup that player set for the position
                int positionSpinner = teamLineupPositionSpinner.getSelectedItemPosition();
                // Set starters to new selection

                if (redshirtSelector.playersSelected.size() + userTeam.countRedshirts() - redshirtSelector.playersRemoved.size() < 10) {

                    userTeam.setRedshirts(redshirtSelector.playersSelected, redshirtSelector.playersRemoved, positionSpinner);
                    redshirtSelector.playersSelected.clear();
                    redshirtSelector.playersRemoved.clear();

                    // Update list to show the change
                    redshirtLineup(positionSpinner, redshirtSelector, positionNumberRequired, positionPlayers, textLineupPositionDescription);
                    minPlayersText.setText("Min Active: " + positionNumberRequired[positionSpinner] + " Current Active: " + userTeam.getActivePlayers(positionSpinner));

                    Toast.makeText(MainActivity.this, "Set redshirts for " + positionSelection[positionSpinner] + "! You currently have " + userTeam.countRedshirts() + " (Max: 9) redshirted players.",
                            Toast.LENGTH_SHORT).show();

                } else {

                    Toast.makeText(MainActivity.this, "A maximum of 9 players can be redshirted each season. You have exceeded this! You currently have " + userTeam.countRedshirts() + " redshirted players.",
                            Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    //Depth Chart Lineup Setup
    private void redshirtLineup(int position, RedshirtAdapter redshirtSelector, int[] positionNumberRequired,
                                ArrayList<Player> positionPlayers, TextView textLineupPositionDescription) {
        redshirtSelector.playersRequired = positionNumberRequired[position];
        redshirtSelector.playersSelected.clear();
        redshirtSelector.players.clear();
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
        redshirtSelector.notifyDataSetChanged();
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
        if(simLeague.currentWeek == 0 && !redshirtComplete) {
            simGameButton.setTextSize(12);
            simGameButton.setText("Play Week " + (simLeague.currentWeek + 1));
            redshirtComplete = true;
            userTeam.recruitWalkOns();
            simLeague.preseasonNews();
            Button depthchartButton = findViewById(R.id.buttonDepthChart);
            depthchartButton.setBackgroundColor(0XFF607D8B);
            depthchartButton.setText("DEPTH CHART");
        } else if (simLeague.currentWeek <= simLeague.regSeasonWeeks+3) {
            int numGamesPlayed = userTeam.gameWLSchedule.size();

            simLeague.playWeek();

            if (simLeague.currentWeek == simLeague.regSeasonWeeks/2) {
                midseasonSummary();
            }

            // Get injury report if there are injuries and just played a game
            if (userTeam.gameWLSchedule.size() > numGamesPlayed) {
                if (showInjuryReport)
                    showInjuryReportDialog();
                if (showToasts)
                    Toast.makeText(MainActivity.this, userTeam.weekSummaryStr(simLeague.currentWeek),
                            Toast.LENGTH_SHORT).show();
            }

            // Show notification for being invited/not invited to bowl or CCG
            if (simLeague.currentWeek >= simLeague.regSeasonWeeks-1) {
                if (!userTeam.gameSchedule.get(userTeam.gameSchedule.size() - 1).hasPlayed) {
                    String weekGameName = userTeam.gameSchedule.get(userTeam.gameSchedule.size() - 1).gameName;
                    if (weekGameName.equals("NCG")) {
                        if (showToasts)
                            Toast.makeText(MainActivity.this, "Congratulations! " + userTeam.name + " was invited to the National Championship Game!",
                                    Toast.LENGTH_SHORT).show();
                    } else {
                        if (showToasts && simLeague.expPlayoffs)
                            Toast.makeText(MainActivity.this, "Congratulations! " + userTeam.name + " was invited to the " +
                                            weekGameName + "!",
                                    Toast.LENGTH_SHORT).show();
                        else {
                            if (showToasts && simLeague.currentWeek == simLeague.regSeasonWeeks)
                                Toast.makeText(MainActivity.this, "Congratulations! " + userTeam.name + " was invited to the " +
                                                weekGameName + "!",
                                        Toast.LENGTH_SHORT).show();
                        }
                    }
                } else if (simLeague.currentWeek == simLeague.regSeasonWeeks-1) {
                    if (showToasts)
                        Toast.makeText(MainActivity.this, userTeam.name + " was not invited to the Conference Championship.",
                                Toast.LENGTH_SHORT).show();
                } else if (simLeague.currentWeek == simLeague.regSeasonWeeks) {
                    if (showToasts && simLeague.expPlayoffs)
                        Toast.makeText(MainActivity.this, userTeam.name + " did not make the College Football Playoffs.",
                                Toast.LENGTH_SHORT).show();
                    if (showToasts && !simLeague.expPlayoffs)
                        Toast.makeText(MainActivity.this, userTeam.name + " was not invited to a bowl game.",
                                Toast.LENGTH_SHORT).show();
                }
            }

            if (simLeague.currentWeek < simLeague.regSeasonWeeks-1) {
                simGameButton.setTextSize(14);
                simGameButton.setText("Play Week " + (simLeague.currentWeek + 1));
            } else if (simLeague.currentWeek == simLeague.regSeasonWeeks-1) {
                simGameButton.setTextSize(11);
                simGameButton.setText("Play Conf Championships");
                examineTeam(currentTeam.name);
            } else if (simLeague.currentWeek == simLeague.regSeasonWeeks) {
                heismanCeremony();
                simGameButton.setTextSize(12);
                if (simLeague.expPlayoffs) simGameButton.setText("Play Sweet 16");
                else simGameButton.setText("Play Bowl Week 1");
                examineTeam(currentTeam.name);
            } else if (simLeague.currentWeek == simLeague.regSeasonWeeks+1) {
                simGameButton.setTextSize(12);
                if (simLeague.expPlayoffs) simGameButton.setText("Play Elite 8");
                else simGameButton.setText("Play Bowl Week 2");
                examineTeam(currentTeam.name);
            } else if (simLeague.currentWeek == simLeague.regSeasonWeeks+2) {
                simGameButton.setTextSize(12);
                if (simLeague.expPlayoffs) simGameButton.setText("Play Final Four");
                else simGameButton.setText("Play Bowl Week 3");
                examineTeam(currentTeam.name);
            } else if (simLeague.currentWeek == simLeague.regSeasonWeeks+3) {
                simGameButton.setTextSize(10);
                simGameButton.setText("Play National Championship");
            } else if (simLeague.currentWeek == simLeague.regSeasonWeeks+4) {
                simGameButton.setTextSize(10);
                simGameButton.setText("Season Summary");
                showNewsStoriesDialog();
            }


            updateCurrTeam();
            scrollToLatestGame();

            //Off-Season
        } else if (simLeague.currentWeek == simLeague.regSeasonWeeks+4) {
            // Show NCG summary and check league records
            simLeague.enterOffseason();
            simLeague.checkLeagueRecords();
            seasonSummary();
            simLeague.updateHCHistory();
            simLeague.updateTeamHistories();
            simLeague.updateLeagueHistory();
            simLeague.currentWeek++;
            simGameButton.setTextSize(12);
            simGameButton.setText("Off-Season: Contracts");

        } else if (simLeague.currentWeek == simLeague.regSeasonWeeks+5) {
            userHC = userTeam.HC.get(0);

            simLeague.advanceHC();
            if (simLeague.isCareerMode()) contractDialog();
            //conf realignment
            conferenceRealignment();
            //Promotion/Relegation!
            universalProRel();
            simGameButton.setTextSize(12);
            if (simLeague.isCareerMode())
                simGameButton.setText("Off-Season: Job Offers");
            else simGameButton.setText("Off-Season: Continue");
            simLeague.currentWeek++;

            //DEBUG STUFF
            //coachProgressData();

        } else if (simLeague.currentWeek == simLeague.regSeasonWeeks+6 && userTeam.fired) {
            if (simLeague.isCareerMode()) jobOffers(userHC);
            simLeague.currentWeek++;
            simGameButton.setTextSize(12);
            simGameButton.setText("Off-Season: Coaching Changes");

        } else if (simLeague.currentWeek == simLeague.regSeasonWeeks+6 && !userTeam.fired) {
            userHC = userTeam.HC.get(0);
            if (simLeague.isCareerMode()) promotions(userHC);
            simLeague.currentWeek++;
            simGameButton.setTextSize(12);
            simGameButton.setText("Off-Season: Coaching Changes");

        } else if (simLeague.currentWeek == simLeague.regSeasonWeeks+7) {
            simLeague.coachCarousel();
            simGameButton.setTextSize(12);
            simLeague.currentWeek++;
            simGameButton.setText("Off-Season: Graduation");
            showNewsStoriesDialog();

        } else if (simLeague.currentWeek == simLeague.regSeasonWeeks+8) {
            simLeague.advanceSeason();
            simLeague.currentWeek++;
            if (simLeague.updateTV) newsTV();
            simGameButton.setTextSize(12);
            simGameButton.setText("Off-Season: Transfer List");

        } else if (simLeague.currentWeek == simLeague.regSeasonWeeks+9) {
            simLeague.transferPlayers();
            showNewsStoriesDialog(); //shows significant transfer options
            simLeague.currentWeek++;
            simGameButton.setTextSize(12);
            simGameButton.setText("Off-Season: Complete Transfers");

        } else if (simLeague.currentWeek == simLeague.regSeasonWeeks+10) {
            transfers(); //displays list of transfers
            simLeague.currentWeek++;
            simGameButton.setTextSize(12);
            simGameButton.setText("Begin Recruiting");

        } else if (simLeague.currentWeek >= simLeague.regSeasonWeeks+11) {
            beginRecruiting();

        }

        if(userTeam.disciplineAction) disciplineSetup();
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
            if (simLeague.currentWeek < simLeague.regSeasonWeeks) {

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
                final TeamRankingsList teamRankingsAdapter =
                        new TeamRankingsList(this, rankings, userTeam.abbr);
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
        } else if (id == R.id.action_player_rankings && simLeague.currentWeek < simLeague.regSeasonWeeks+7) {

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
        } else if (id == R.id.action_coach_DB) {
            /*
              Clicked User Team History in drop down menu
             */
            showCoachDatabase();
        } else if (id == R.id.action_ccg_bowl_watch) {
            /*
              Clicked CCG / Bowl Watch in drop down menu
             */
            showBowlCCGDialog();
/*        } else if (id == R.id.action_import) {
            importData();*/

        } else if (id == R.id.action_save_league) {
            /*
              Clicked Save League in drop down menu
             */
            if (simLeague.getYear() != seasonStart && simLeague.currentWeek < simLeague.regSeasonWeeks-6) {
                saveLeague();
            } else if (simLeague.currentWeek > simLeague.regSeasonWeeks-7) {
                Toast.makeText(MainActivity.this, "Save Function Disabled. Save only available in prior to Week 6.",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Save Function disabled during initial season.",
                        Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.action_export_league) {
            /*
              Clicked Save League in drop down menu
             */
            exportData();

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

        final CheckBox checkboxShowPotential = dialog.findViewById(R.id.checkboxShowPotential);
        checkboxShowPotential.setChecked(simLeague.showPotential);

        final CheckBox checkboxGameLog = dialog.findViewById(R.id.checkboxShowFullGameLog);
        checkboxGameLog.setChecked(simLeague.fullGameLog);

        final CheckBox checkboxCareerMode = dialog.findViewById(R.id.checkboxCareerMode);
        checkboxCareerMode.setChecked(simLeague.isCareerMode());

        final CheckBox checkboxNeverRetire = dialog.findViewById(R.id.checkboxNeverRetire);
        checkboxNeverRetire.setChecked(simLeague.neverRetire);

        final CheckBox checkboxTV = dialog.findViewById(R.id.checkboxTV);
        checkboxTV.setChecked(simLeague.enableTV);

        final CheckBox checkboxPlayoffs = dialog.findViewById(R.id.checkboxPlayoffs);
        final TextView textPlayoffs = dialog.findViewById(R.id.textPlayoffs);
        if(simLeague.currentWeek < simLeague.regSeasonWeeks) {
            checkboxPlayoffs.setChecked(simLeague.expPlayoffs);
        } else {
            textPlayoffs.setVisibility(View.INVISIBLE);
            checkboxPlayoffs.setVisibility(View.INVISIBLE);
        }

        final CheckBox checkboxRealignment = dialog.findViewById(R.id.checkboxConfRealignment);
        if(simLeague.enableUnivProRel) {
            final TextView textRealignment = dialog.findViewById(R.id.textConfRealignment);
            textRealignment.setVisibility(View.INVISIBLE);
            checkboxRealignment.setVisibility(View.INVISIBLE);
        }
        checkboxRealignment.setChecked(simLeague.confRealignment);

        final CheckBox checkboxAdvRealignment = dialog.findViewById(R.id.checkboxAdvConfRealignment);
        if(simLeague.enableUnivProRel) {
            final TextView textAdvRealignment = dialog.findViewById(R.id.textAdvConfRealignment);
            textAdvRealignment.setVisibility(View.INVISIBLE);
            checkboxAdvRealignment.setVisibility(View.INVISIBLE);
        }
        checkboxAdvRealignment.setChecked(simLeague.advancedRealignment);

        final CheckBox checkboxProRelegation = dialog.findViewById(R.id.checkboxProRelegation);
        //checkboxProRelegation.setChecked(simLeague.enableUnivProRel);
        checkboxProRelegation.setVisibility(View.INVISIBLE);
        final TextView textProRel = dialog.findViewById(R.id.textEnableProRel);
        textProRel.setVisibility(View.INVISIBLE);

        checkboxAdvRealignment.setOnClickListener(new View.OnClickListener() {
                                                      @Override
                                                      public void onClick(View view) {
                                                          if (checkboxAdvRealignment.isChecked()) {
                                                              checkboxProRelegation.setChecked(false);
                                                              checkboxRealignment.setChecked(true);
                                                          }

                                                          if(simLeague.regSeasonWeeks > 13) checkboxAdvRealignment.setChecked(true);
                                                      }
                                                  }
        );

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
                                                             checkboxAdvRealignment.setChecked(false);
                                                         }
                                                     }
                                                 }
        );

        Button cancelButton = dialog.findViewById(R.id.buttonCancelSettings);
        Button okButton = dialog.findViewById(R.id.buttonOkSettings);
        Button changeTeamsButton = dialog.findViewById(R.id.buttonChangeTeams);
        if (userTeam.getHC(0).age >= 68 && !simLeague.neverRetire) changeTeamsButton.setText("RETIRE");
        if (simLeague.currentWeek < simLeague.regSeasonWeeks+6) changeTeamsButton.setVisibility(View.INVISIBLE);

        Button gameEditorButton = dialog.findViewById(R.id.buttonGameEditor);
        Button fixBowlButton = dialog.findViewById(R.id.buttonFixBowls);

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
                gameEditorV2();

            }
        });

        fixBowlButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Confirmation");
                builder.setMessage("Are you sure you want to restore bowl names to game default names?");
                builder.setCancelable(false);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Perform action on click
                        fixBowlNames();
                        Toast.makeText(getApplicationContext(), "Bowl Names Replaced!", Toast.LENGTH_SHORT).show();
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

        okButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                showToasts = checkboxShowPopup.isChecked();
                showInjuryReport = checkboxShowInjury.isChecked();
                simLeague.showPotential = checkboxShowPotential.isChecked();
                simLeague.fullGameLog = checkboxGameLog.isChecked();
                simLeague.careerMode = checkboxCareerMode.isChecked();
                simLeague.neverRetire = checkboxNeverRetire.isChecked();
                simLeague.confRealignment = checkboxRealignment.isChecked();
                simLeague.advancedRealignment = checkboxAdvRealignment.isChecked();
                simLeague.expPlayoffs = checkboxPlayoffs.isChecked();
                simLeague.enableTV = checkboxTV.isChecked();
                if (simLeague.enableUnivProRel) {
                    simLeague.confRealignment = false;
                    simLeague.advancedRealignment = false;
                }
                userTeam.showPopups = showToasts;
                dialog.dismiss();

            }
        });

        changeTeamsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
                if (userTeam.getHC(0).age > retireAge && !simLeague.neverRetire) {
                    retirementQuestion();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Confirmation");
                    builder.setMessage("Are you sure you want to resign from this position?");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Perform action on click
                            userHC = userTeam.HC.get(0);
                            //selectNewTeam();
                            if (simLeague.isCareerMode()) jobOffers(userHC);
                            else selectNewTeam(userHC);
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

        String[] historySelection = {"League History", "League Records", "League Stats", "Hall of Fame", "Coach Database"};
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
                            final LeagueRecordsList leagueRecordsAdapter =
                                    new LeagueRecordsList(MainActivity.this, simLeague.getLeagueRecordsStr().split("\n"), userTeam.abbr, userTeam.name);
                            leagueHistoryList.setAdapter(leagueRecordsAdapter);
                        } else if (position == 2) {
                            showLeagueHistoryStats();
                        } else if (position == 3) {
                            HallofFameList hofAdapter = new HallofFameList(MainActivity.this, hofPlayers, userTeam.name);
                            leagueHistoryList.setAdapter(hofAdapter);
                        } else if (position == 4) {
                            showCoachDatabase();
                        } else {
                            final LeagueHistoryList leagueHistoryAdapter =
                                    new LeagueHistoryList(MainActivity.this, simLeague.getLeagueHistoryStr().split("%"), userTeam.abbr);
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

        ArrayList<String> rankings = new ArrayList<>();
        String[] rankingsSelection =
                {"National Championships", "Conference Championships", "Bowl Victories", "Total Wins"};
        Spinner teamRankingsSpinner = dialog.findViewById(R.id.spinnerTeamRankings);
        ArrayAdapter<String> teamRankingsSpinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, rankingsSelection);
        teamRankingsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        teamRankingsSpinner.setAdapter(teamRankingsSpinnerAdapter);

        final ListView teamRankingsList = dialog.findViewById(R.id.listViewTeamRankings);
        final TeamRankingsList teamRankingsAdapter =
                new TeamRankingsList(this, rankings, userTeam.name);
        teamRankingsList.setAdapter(teamRankingsAdapter);

        teamRankingsSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        ArrayList<String> rankings = simLeague.getLeagueHistoryStats(position);
                        teamRankingsAdapter.setUserTeamStrRep(userTeam.name);
                        teamRankingsAdapter.clear();
                        teamRankingsAdapter.addAll(rankings);
                        teamRankingsAdapter.notifyDataSetChanged();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        // do nothing
                    }
                });
    }

    //League History Stats
    private void showCoachDatabase() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Coach Database")
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
                {"National Championships", "Conference Championships", "Bowl Victories", "Total Wins", "Winning PCT", "Coach of the Year", "Conf Coach of Year", "All-Americans", "All-Conference", "Coach Career Score", "Coach Accumulated Prestige"};
        Spinner teamRankingsSpinner = dialog.findViewById(R.id.spinnerTeamRankings);
        ArrayAdapter<String> teamRankingsSpinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, rankingsSelection);
        teamRankingsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        teamRankingsSpinner.setAdapter(teamRankingsSpinnerAdapter);

        final ListView teamRankingsList = dialog.findViewById(R.id.listViewTeamRankings);
        final CoachDatabase coachDatabase =
                new CoachDatabase(this, rankings, userTeam.name, this);
        teamRankingsList.setAdapter(coachDatabase);

        teamRankingsSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        ArrayList<String> rankings = simLeague.getCoachDatabase(position);
                        coachDatabase.setupUserHC(userTeam.HC.get(0).name + " (" + userTeam.abbr + ")");
                        coachDatabase.clear();
                        coachDatabase.addAll(rankings);
                        coachDatabase.notifyDataSetChanged();
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
                            TeamHistoryList teamHistoryAdapter =
                                    new TeamHistoryList(MainActivity.this, currentTeam.getTeamHistoryList());
                            teamHistoryList.setAdapter(teamHistoryAdapter);
                        } else if (position == 1) {
                            LeagueRecordsList leagueRecordsAdapter =
                                    new LeagueRecordsList(MainActivity.this, currentTeam.teamRecords.getRecordsStr().split("\n"), "---", "---");
                            teamHistoryList.setAdapter(leagueRecordsAdapter);
                        } else if (position == 2) {
                            HallofFameList hofAdapter = new HallofFameList(MainActivity.this, hofPlayers, userTeam.name);
                            teamHistoryList.setAdapter(hofAdapter);
                        } else if (position == 3) {
                            dialog.dismiss();
                            teamPrestigeGraphView();
                        } else if (position == 4) {
                            dialog.dismiss();
                            teamRankingGraphView();
                        }
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        // do nothing
                    }
                });
    }

    //Graph View

    private void teamPrestigeGraphView() {

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
        for (int i = 0; i < simLeague.leagueHistory.size(); i++) {
            series.appendData(new DataPoint((seasonStart + i), Integer.parseInt(currentTeam.teamHistory.get(i).split(":")[2].split(" ")[1])), true, i + 1, false);
            yearLabels[i] = Integer.toString(i + seasonStart);
        }
        graph.addSeries(series);

        if (yearLabels.length > 1) {
            StaticLabelsFormatter years = new StaticLabelsFormatter(graph);
            years.setHorizontalLabels(yearLabels);
            graph.getGridLabelRenderer().setLabelFormatter(years);
            graph.getGridLabelRenderer().setNumHorizontalLabels(4);
        }
        graph.getViewport().setScalable(true);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMaxY(simLeague.teamList.get(0).teamPrestige + 10);
        graph.getViewport().setMinY(0);
    }

    //Graph View

    private void teamRankingGraphView() {

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
        for (int i = 0; i < simLeague.leagueHistory.size(); i++) {
            series.appendData(new DataPoint((seasonStart + i), simLeague.teamList.size() - Integer.parseInt(currentTeam.teamHistory.get(i).split("#")[1].split(" ")[0])), true, i + 1, false);
            yearLabels[i] = Integer.toString(i + seasonStart);
        }
        graph.addSeries(series);

        String[] rankLabels = new String[simLeague.teamList.size()+1];
        for (int i = simLeague.teamList.size(); i >= 0; i--) {
            rankLabels[simLeague.teamList.size() - i] = Integer.toString(i);
        }

        if (yearLabels.length > 1) {
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
        graph.getViewport().setMaxY(simLeague.teamList.size());
        graph.getViewport().setMinY(0);
    }

    //Coach History
    private void showCoachHistoryDialog(HeadCoach p) {
        final HeadCoach hc = p;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Coach History: " + hc.name)
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
                            TeamHistoryList teamHistoryAdapter =
                                    new TeamHistoryList(MainActivity.this, hc.getCoachHistory());
                            teamHistoryList.setAdapter(teamHistoryAdapter);
                        } else if (position == 1) {
                            coachGraphView(hc);
                        } else if (position == 2) {
                            coachGraphViewRank(hc);
                        }
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        // do nothing
                    }
                });
    }

    private void coachGraphView(HeadCoach hc) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(hc.name + ": Prestige History")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing?
                    }
                })
                .setView(getLayoutInflater().inflate(R.layout.graphview, null));
        AlertDialog dialog = builder.create();
        dialog.show();

        DataPoint[] data = new DataPoint[hc.history.size()];
        GraphView graph = dialog.findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        String[] yearLabels = new String[hc.history.size()];
        for (int i = 0; i < hc.history.size(); i++) {
            if (!hc.history.get(i).equals("")) {
                series.appendData(new DataPoint(Integer.parseInt(hc.history.get(i).split(": ")[0]), Integer.parseInt(hc.history.get(i).split("Prs: ")[1].split(" ")[0])), true, i + 1, false);
                yearLabels[i] = hc.history.get(i).split(":")[0];
            }
        }
        graph.addSeries(series);

        if (yearLabels.length > 1) {
            StaticLabelsFormatter years = new StaticLabelsFormatter(graph);
            years.setHorizontalLabels(yearLabels);
            graph.getGridLabelRenderer().setLabelFormatter(years);
            graph.getGridLabelRenderer().setNumHorizontalLabels(4);
        }
        graph.getViewport().setScalable(true);
        graph.getViewport().setScrollable(true);
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMaxY(simLeague.teamList.get(0).teamPrestige + 10);
        graph.getViewport().setMinY(0);
    }

    private void coachGraphViewRank(HeadCoach hc) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(hc.name + ": Rankings History")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing?
                    }
                })
                .setView(getLayoutInflater().inflate(R.layout.graphview, null));
        AlertDialog dialog = builder.create();
        dialog.show();
        DataPoint[] data = new DataPoint[hc.history.size()];
        GraphView graph = dialog.findViewById(R.id.graph);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>();
        String[] yearLabels = new String[hc.history.size()];
        for (int i = 0; i < hc.history.size(); i++) {
            if (!hc.history.get(i).equals("")) {
                series.appendData(new DataPoint(Integer.parseInt(hc.history.get(i).split(": ")[0]), simLeague.teamList.size() - Integer.parseInt(hc.history.get(i).split("#")[1].split(" ")[0])), true, i + 1, false);
                yearLabels[i] = hc.history.get(i).split(":")[0];
            }
        }
        graph.addSeries(series);

        String[] rankLabels = new String[simLeague.teamList.size()+1];
        for (int i = simLeague.teamList.size(); i >= 0; i--) {
            rankLabels[simLeague.teamList.size() - i] = Integer.toString(i);
        }

        if (yearLabels.length > 1) {
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
        graph.getViewport().setMaxY(simLeague.teamList.size());
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

        ArrayList<String> rankings = new ArrayList<>();
        String[] rankingsSelection =
                {"Power Index", "Prestige", "Strength of Schedule", "Strength of Wins", "Points Per Game", "Opp Points Per Game",
                        "Yards Per Game", "Opp Yards Per Game", "Pass Yards Per Game", "Rush Yards Per Game",
                        "Opp Pass YPG", "Opp Rush YPG", "TO Differential", "Off Talent", "Def Talent", "Team Chemistry", "Recruiting Class", "Discipline Score", "Team Budget", "Team Facilities", "Coach - Overall", "Coach Score"};
        Spinner teamRankingsSpinner = dialog.findViewById(R.id.spinnerTeamRankings);
        ArrayAdapter<String> teamRankingsSpinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, rankingsSelection);
        teamRankingsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        teamRankingsSpinner.setAdapter(teamRankingsSpinnerAdapter);

        final ListView teamRankingsList = dialog.findViewById(R.id.listViewTeamRankings);
        final TeamRankingsList teamRankingsAdapter =
                new TeamRankingsList(this, rankings, userTeam.name);
        teamRankingsList.setAdapter(teamRankingsAdapter);

        teamRankingsSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        ArrayList<String> rankings = simLeague.getTeamRankingsStr(position);

                        teamRankingsAdapter.setUserTeamStrRep(userTeam.name);
                        if (position == 16)
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
                        "Coach - Overall", "Coach - Season Score"
                };
        Spinner playerRankingssSpinner = dialog.findViewById(R.id.spinnerTeamRankings);
        ArrayAdapter<String> playerRankingssSpinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, rankingsSelection);
        playerRankingssSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        playerRankingssSpinner.setAdapter(playerRankingssSpinnerAdapter);

        final ListView playerRankingssList = dialog.findViewById(R.id.listViewTeamRankings);
        final PlayerRankingsList playerRankingssAdapter =
                new PlayerRankingsList(this, rankings, userTeam.abbr, this);
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

        String[] selection = {"Conf Championships", "Post-Season"};
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
        if (simLeague.currentWeek < simLeague.regSeasonWeeks) {
            selection = new String[1];
            selection[0] = "Offensive Player of the Year";
        } else {
            selection = new String[6 + simLeague.conferences.size()];
            selection[0] = "Offensive Player of the Year";
            selection[1] = "Defensive Player of the Year";
            selection[2] = "Coach of the Year";
            selection[3] = "Freshman of the Year";
            selection[4] = "All-American Team";
            selection[5] = "All-Freshman Team";
            for (int i = 0; i < simLeague.conferences.size(); ++i) {
                selection[i + 6] = simLeague.conferences.get(i).confName + " All-Conf Team";
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
        final String[] defAwardList = simLeague.getDefensePOTYStr().split(">");
        final String[] freshmanAwardList = simLeague.getFreshmanCeremonyStr().split(">");
        final String[] allAmericans = simLeague.getAllAmericanStr().split(">");
        final String[] allFreshman = simLeague.getAllFreshmanStr().split(">");
        final String[][] allConference = new String[simLeague.conferences.size()][];
        for (int i = 0; i < simLeague.conferences.size(); ++i) {
            allConference[i] = simLeague.getAllConfStr(i).split(">");
        }


        potySpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0) {
                            potyList.setAdapter(new SeasonAwardsList(MainActivity.this, simLeague.getHeismanCeremonyStr().split(">"), userTeam.abbr));
                        } else if (position == 1) {
                            potyList.setAdapter(new SeasonAwardsList(MainActivity.this, defAwardList, userTeam.abbr));
                        } else if (position == 2) {
                            potyList.setAdapter(new SeasonAwardsList(MainActivity.this, coachAwardList, userTeam.abbr));
                        } else if (position == 3) {
                            potyList.setAdapter(new SeasonAwardsList(MainActivity.this, freshmanAwardList, userTeam.abbr));
                        } else if (position == 4) {
                            potyList.setAdapter(new SeasonAwardsList(MainActivity.this, allAmericans, userTeam.abbr));
                        } else if (position == 5) {
                            potyList.setAdapter(new SeasonAwardsList(MainActivity.this, allFreshman, userTeam.abbr));
                        } else {
                            potyList.setAdapter(new SeasonAwardsList(MainActivity.this, allConference[position - 6], userTeam.abbr));
                        }
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        // do nothing
                    }
                });
    }

    private void importDataPrompt() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Do you want to import Coach/Player data?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        importData();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (newGame) {
                            newGame = false;
                            careerModeOptions();
                        }
                    }
                });
        AlertDialog dialog = builder.create();
        builder.setCancelable(false);
        dialog.show();
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
                        intent.setType("text/*");
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        startActivityForResult(intent, READ_REQUEST_CODE);
                        importMoreDataPrompt();
                    }
                })
                .setNegativeButton("Roster File", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loadData = "roster";
                        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                        intent.setType("text/*");
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        startActivityForResult(intent, READ_REQUEST_CODE);
                        importMoreDataPrompt();
                    }
                })
                .setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        importMoreDataPrompt();
                    }
                });
        AlertDialog dialog = builder.create();
        builder.setCancelable(false);
        dialog.show();

    }

    private void importMoreDataPrompt() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Do you want to import more data?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        importData();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (newGame) {
                            newGame = false;
                            careerModeOptions();
                        }
                    }
                });
        AlertDialog dialog = builder.create();
        builder.setCancelable(false);
        dialog.show();
    }

    //Save File Dialog
    private void saveLeague() {
        AlertDialog.Builder save = new AlertDialog.Builder(this);
        save.setTitle("Choose Save File to Overwrite:");
        final String[] fileInfos = getSaveFileInfos();
        SaveFilesList saveFilesAdapter = new SaveFilesList(this, fileInfos);
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
        String[] infos = new String[20];
        String fileInfo;
        File saveFile;
        for (int i = 0; i < 20; ++i) {
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

    //Export Save File

    private void exportData() {
        //WORK IN PROGRESS
        exportSave();
    }


    //Exit Current Game
    public void exitMainActivity() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Are you sure you want to return to main menu? Any progress from the beginning of the season will be lost.")
                .setPositiveButton("Yes, Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Actually go back to main menu
                        finish();
                        Intent myIntent = new Intent(MainActivity.this, Home.class);
                        myIntent.putExtra("Theme", theme);
                        MainActivity.this.startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                    }
                })
                .setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    //IN-GAME DISPLAYS

    //New Game Options
    private void careerModeOptions() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New Game Options")
                .setView(getLayoutInflater().inflate(R.layout.settings_menu, null));
        final AlertDialog dialog = builder.create();
        builder.setCancelable(false);
        dialog.show();

        final CheckBox checkboxShowPopup = dialog.findViewById(R.id.checkboxShowPopups);
        checkboxShowPopup.setChecked(showToasts);

        final CheckBox checkboxShowInjury = dialog.findViewById(R.id.checkboxShowInjuryReport);
        checkboxShowInjury.setChecked(showInjuryReport);

        final CheckBox checkboxShowPotential = dialog.findViewById(R.id.checkboxShowPotential);
        checkboxShowPotential.setChecked(simLeague.showPotential);

        final CheckBox checkboxGameLog = dialog.findViewById(R.id.checkboxShowFullGameLog);
        checkboxGameLog.setChecked(simLeague.fullGameLog);

        final CheckBox checkboxCareerMode = dialog.findViewById(R.id.checkboxCareerMode);
        checkboxCareerMode.setChecked(simLeague.isCareerMode());

        final CheckBox checkboxNeverRetire = dialog.findViewById(R.id.checkboxNeverRetire);
        checkboxNeverRetire.setChecked(simLeague.neverRetire);

        final CheckBox checkboxRealignment = dialog.findViewById(R.id.checkboxConfRealignment);
        checkboxRealignment.setChecked(simLeague.confRealignment);

        final CheckBox checkboxAdvRealignment = dialog.findViewById(R.id.checkboxAdvConfRealignment);
        checkboxAdvRealignment.setChecked(simLeague.advancedRealignment);

        final CheckBox checkboxPlayoffs = dialog.findViewById(R.id.checkboxPlayoffs);
        checkboxPlayoffs.setChecked(simLeague.expPlayoffs);

        final CheckBox checkboxProRelegation = dialog.findViewById(R.id.checkboxProRelegation);
        checkboxProRelegation.setChecked(simLeague.enableUnivProRel);

        final CheckBox checkboxTV = dialog.findViewById(R.id.checkboxTV);
        checkboxTV.setChecked(simLeague.enableTV);

        checkboxAdvRealignment.setOnClickListener(new View.OnClickListener() {
                                                   @Override
                                                   public void onClick(View view) {
                                                       if (checkboxAdvRealignment.isChecked()) {
                                                           checkboxProRelegation.setChecked(false);
                                                           checkboxRealignment.setChecked(true);
                                                       }

                                                       if(simLeague.regSeasonWeeks > 13) checkboxAdvRealignment.setChecked(true);
                                                   }
                                               }
        );

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
                                                             checkboxAdvRealignment.setChecked(false);
                                                         }
                                                     }
                                                 }
        );

        Button cancelButton = dialog.findViewById(R.id.buttonCancelSettings);
        cancelButton.setVisibility(View.INVISIBLE);
        Button changeTeamsButton = dialog.findViewById(R.id.buttonChangeTeams);
        changeTeamsButton.setVisibility(View.INVISIBLE);
        Button gameEditorButton = dialog.findViewById(R.id.buttonGameEditor);
        gameEditorButton.setVisibility(View.INVISIBLE);
        Button fixBowlButton = dialog.findViewById(R.id.buttonFixBowls);
        fixBowlButton.setVisibility(View.INVISIBLE);
        Button okButton = dialog.findViewById(R.id.buttonOkSettings);
        okButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                showToasts = checkboxShowPopup.isChecked();
                showInjuryReport = checkboxShowInjury.isChecked();
                simLeague.showPotential = checkboxShowPotential.isChecked();
                simLeague.fullGameLog = checkboxGameLog.isChecked();
                simLeague.careerMode = checkboxCareerMode.isChecked();
                simLeague.neverRetire = checkboxNeverRetire.isChecked();
                simLeague.enableUnivProRel = checkboxProRelegation.isChecked();
                simLeague.confRealignment = checkboxRealignment.isChecked();
                simLeague.advancedRealignment = checkboxAdvRealignment.isChecked();
                simLeague.expPlayoffs = checkboxPlayoffs.isChecked();
                simLeague.enableTV = checkboxTV.isChecked();
                if (simLeague.enableUnivProRel) {
                    simLeague.confRealignment = false;
                    universalProRelAction();
                }
                userTeam.showPopups = showToasts;
                selectTeam();
                dialog.dismiss();

            }
        });

    }

    private void universalProRelAction() {

        // Perform action on click
        simLeague.enableUnivProRel = true;
        simLeague.confRealignment = false;
        simLeague.convertUnivProRel();
        updateCurrConference();
        updateCurrTeam();
        examineTeam(userTeam.name);

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
                            depthChartDialog();
                        }
                    })
                    .setView(getLayoutInflater().inflate(R.layout.injury_report, null));
            AlertDialog dialog = builder.create();
            dialog.show();

            ListView injuryList = dialog.findViewById(R.id.listViewInjuryReport);
            injuryList.setAdapter(new PlayerProfile(this, injuries));

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

        goals = "Welcome to the " + simLeague.getYear() + " College Football season!\n\n";
        goals += "This season your team is projected to finish ranked #" + userTeam.projectedPollRank + "!\n\n";

        int num = (int)(simLeague.teamList.size()*.875);
        if (userTeam.projectedPollRank > 100) {
            goals += "Despite being projected at #" + userTeam.projectedPollRank + ", your goal is to finish in the Top + " + num + ".\n\n";
        }

        goals += "In conference play, your team is expected to finish " + userTeam.getRankStr(confPos) + " in the " + userTeam.conference + " conference.\n\n";

        int games = 0;
        for(Game g : userTeam.gameSchedule) {
            if (g.gameName.equals("OOC") || g.gameName.equals("Conference") || g.gameName.equals("Division")) {
                games++;
            }
        }

        goals += "Based on your schedule, your team is projected to finish with a record of " + userTeam.projectedWins + " - " + (games - userTeam.projectedWins) + ".\n\n";

        if (simLeague.getYear() > seasonStart) {
            if (userTeam.bowlBan) {
                goals += "Your team was penalized heavily for off-season issues by the College Athletic Administration and will lose Prestige and suffer a post-season bowl ban this year.\n\n";
            }
            if (userTeam.penalized) {
                goals += "Your team had a minor infraction over the off-season and lost some Prestige.\n\n";
            }
        }

        if (simLeague.getYear() > seasonStart) {
            if (userTeam.facilityUpgrade) {
                goals += "Your team upgraded the training facilities this off-season to Level " + userTeam.teamFacilities + " which added an additional " + userTeam.teamFacilities + " prestige points!\n\n";
            }
        }

        simLeague.newsStories.get(simLeague.currentWeek).add("Season Goals>" + goals);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(goals + "\nNote: You can always review your season goals in the Pre-Season News.")
                .setTitle(simLeague.getYear() + " Season Goals")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        if(simLeague.getYear() != seasonStart)
                builder.setNegativeButton("SAVE PROGRESS", new DialogInterface.OnClickListener() {
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
                .setTitle(simLeague.getYear() + " Pre-Season")
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

    public void showSuspensions() {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(userTeam.suspensionNews)
                .setTitle("DISCIPLINARY ACTION")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        TextView textView = dialog.findViewById(android.R.id.message);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        userTeam.suspension = false;
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
                .setTitle(simLeague.getYear() + " Season Summary")
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
        final TeamRankingsList teamRankingsAdapter =
                new TeamRankingsList(this, simLeague.getTeamRankingsStr(1), userTeam.name);
        teamRankingsList.setAdapter(teamRankingsAdapter);
    }

    //Contract Status Dialog
    private void contractDialog() {

        if (simLeague.isCareerMode()) {
            if (userHC.age > retireAge) {
                userHC.retirement = true;
            }
        }

        if (userHC.retirement && !skipRetirementQ && !simLeague.neverRetire) {
            retirementQuestion();
            skipRetirementQ = true;

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage(userTeam.contractString)
                    .setTitle(simLeague.getYear() + " Contract Status")
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
    }

    //Job Offers Dialog when fired or resignation from previous team
    private void jobOffers(HeadCoach headCoach) {
        jobType  = 1;
        jobListSet = true;
        jobList.clear();

        userHC = headCoach;
        int ratOvr = userHC.getHCOverall();
        if (ratOvr < 40) ratOvr = 40;
        String oldTeam = "NO TEAM";
        if(userHC.team != null) oldTeam = userHC.team.name;
        updateHeaderBar();
        //get user team from list dialog
        jobList = simLeague.getCoachListFired(ratOvr, oldTeam);
        String[] teams = setJobTeamList(jobList);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Job Offers Available:");
        builder.setCancelable(false);
        builder.setItems(teams, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                viewTeam(jobList, item);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    //Job offers from other teams
    private void promotions(HeadCoach headCoach) {
        jobType  = 2;
        jobListSet = true;
        jobList.clear();

        userHC = headCoach;
        if (userHC.promotionCandidate) {

            int ratOvr = userHC.getHCOverall();
            if (ratOvr < 40) ratOvr = 40;
            double offers = 2;
            String oldTeam = "NO TEAM";
            if(userHC.team != null) oldTeam = userHC.team.name;
            updateHeaderBar();
            //get user team from list dialog
            jobList = simLeague.getCoachPromotionList(ratOvr, offers, oldTeam);

            String[] teams = setJobTeamList(jobList);

            if (jobList.isEmpty()) {
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
                builder.setCancelable(false);
                builder.setItems(teams, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // Do something with the selection
                        viewTeam(jobList, item);
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

    //Choose ANY team (manually change from Options Menu)
    private void selectNewTeam(HeadCoach headCoach) {
        jobType  = 0;
        jobListSet = true;
        jobList.clear();

        userHC = headCoach;
        updateHeaderBar();
        //get user team from list dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose your new team:");
        jobList = simLeague.teamList;

        final String[] teams = simLeague.getTeamListStr();
        builder.setItems(teams, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                //changeTeams(coachList, item);
                //updateHeaderBar();
                //examineTeam(currentTeam.name);
                viewTeam(jobList, item);
            }
        });
        builder.setCancelable(false);
        AlertDialog alert = builder.create();
        alert.show();
    }

    //Make a list of Team Names for the Team Selection Window
    private String[] setJobTeamList(ArrayList<Team> jobListTemp) {
        String[] temp = new String[jobListTemp.size()];

        for(int i=0; i < jobListTemp.size(); i++) {
            temp[i] = jobListTemp.get(i).name + "\n Prestige: #" + jobListTemp.get(i).rankTeamPrestige + "  Off: " + df2.format(jobListTemp.get(i).teamOffTalent) + "  Def: " + df2.format(jobListTemp.get(i).teamDefTalent);
        }

        return temp;
    }

    //View Team prior to choosing
    private void viewTeam(final ArrayList<Team> teamList, final int item) {
        String[] teamRoster = teamList.get(item).getTeamRosterString();

        AlertDialog.Builder roster = new AlertDialog.Builder(this);
        roster.setTitle(teamList.get(item).name + " Team Roster" +
                        "\nPres: #" + teamList.get(item).rankTeamPrestige + " | Off: " + df2.format(teamList.get(item).teamOffTalent) + " | Def: " + df2.format(teamList.get(item).teamDefTalent));
        roster.setNeutralButton("Decline", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if(jobType == 2)  promotions(userHC);
                else if (jobType == 1) jobOffers(userHC);
                else selectNewTeam(userHC);

            }
        });
        roster.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                changeTeams(jobList, item);
                if(jobType == 2) simLeague.coachCarousel();
            }
        });

        StringBuilder sb = new StringBuilder();
        for(String s : teamRoster) {
            if(s != null) sb.append(s +"\n");
        }
        roster.setMessage(sb);


        roster.setCancelable(false);
        AlertDialog teamWindow = roster.create();
        teamWindow.show();
    }

    private void viewTeam2() {
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
                            TeamHistoryList teamHistoryAdapter =
                                    new TeamHistoryList(MainActivity.this, currentTeam.getTeamHistoryList());
                            teamHistoryList.setAdapter(teamHistoryAdapter);
                        } else if (position == 1) {
                            LeagueRecordsList leagueRecordsAdapter =
                                    new LeagueRecordsList(MainActivity.this, currentTeam.teamRecords.getRecordsStr().split("\n"), "---", "---");
                            teamHistoryList.setAdapter(leagueRecordsAdapter);
                        } else if (position == 2) {
                            HallofFameList hofAdapter = new HallofFameList(MainActivity.this, hofPlayers, userTeam.name);
                            teamHistoryList.setAdapter(hofAdapter);
                        } else if (position == 3) {
                            dialog.dismiss();
                            teamPrestigeGraphView();
                        } else if (position == 4) {
                            dialog.dismiss();
                            teamRankingGraphView();
                        }
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        // do nothing
                    }
                });
    }

    //Method to actually switch teams
    private void changeTeams(ArrayList<Team> teamList, int item) {
        userTeam.userControlled = false;
        userTeam.HC.clear();
        simLeague.coachHiringSingleTeam(userTeam);
        simLeague.newJobtransfer(teamList.get(item).name);
        userTeam = simLeague.userTeam;
        userTeamStr = userTeam.name;
        currentTeam = userTeam;
        userTeam.HC.clear();

        if(reincarnate) {
            userTeam.setupUserCoach(userHC.name);
            userHC = userTeam.HC.get(0);
            reincarnate = false;
            userNameDialog();
        } else {
            userTeam.HC.add(userHC);
        }

        userHC.team = userTeam;
        userTeam.fired = false;
        userHC.contractYear = 0;
        userHC.contractLength = 6;
        userHC.baselinePrestige = userTeam.teamPrestige;
        simLeague.newsStories.get(simLeague.currentWeek + 1).add("Coaching Hire: " + currentTeam.name + ">After an extensive search for a new head coach, " + currentTeam.name + " has hired " + userHC.name +
                " to lead the team.");
        updateHeaderBar();
        examineTeam(currentTeam.name);
    }



    //Conference Realignment Update
    private void conferenceRealignment() {
        if (simLeague.confRealignment) {
            simLeague.conferenceRealignmentV2();
            if (simLeague.countRealignment > 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage(simLeague.newsRealignment)
                        .setTitle(simLeague.getYear() + " Conference Realignment News")
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
    private void universalProRel() {
        if (simLeague.enableUnivProRel) {
            simLeague.universalProRel();
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage(simLeague.newsRealignment)
                    .setTitle(simLeague.getYear() + " Promotion/Relegation Update")
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
        for (int i = 0; i < simLeague.newsTV.size(); ++i) {
            update.append(simLeague.newsTV.get(i) + "\n\n");
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(update)
                .setTitle(simLeague.getYear() + " Network Contract Updates")
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
                .setTitle(simLeague.getYear() + " Transfers")
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
                                .setTitle(simLeague.getYear() + " Transfers")
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
                        sb.append(userTeam.conference + "," + userTeam.name + "," + userTeam.abbr + "," + userTeam.getUserRecruitBudget() + "," + userTeam.HC.get(0).ratTalent + "%\n");
                        sb.append(userTeam.getPlayerInfoSaveFile());
                        sb.append("END_TEAM_INFO%\n");
                        sb.append(userTeam.getRecruitsInfoSaveFile());

                        //Start Recruiting Activity
                        finish();
                        Intent myIntent = new Intent(MainActivity.this, RecruitingActivity.class);
                        myIntent.putExtra("USER_TEAM_INFO", sb.toString());
                        myIntent.putExtra("Theme", theme);
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
        final PlayerProfile playerStatsAdapter =
                new PlayerProfile(this, userTeam.getGradPlayersList());
        final MockDraft mockDraftAdapter =
                new MockDraft(this, simLeague.getMockDraftPlayersList(), userTeam.name);
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
        final TeamRankingsList teamRankingsAdapter =
                new TeamRankingsList(this, simLeague.getTeamRankingsStr(16), userTeam.strRepWithPrestige());
        teamRankingsList.setAdapter(teamRankingsAdapter);
    }

    //Retirement vs Eternal
    private void retirementQuestion() {
        String string = "";
        string = "You have reached that time in your life when you need to decide to hang it up and retire or continue on. " +
                "At this point, if you choose to continue, your ability to increase skill ratings will be much more challenging. " +
                "You may also retire and end your career. " +
                "Finally, you can choose to reincarnate yourself as a fresh new head coach in his 30s in this same universe!";
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(string)
                .setTitle("Retirement Age")
                .setPositiveButton("Continue Career", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (skipRetirementQ) contractDialog();
                        dialog.dismiss();

                    }
                })
                .setNeutralButton("Reincarnate", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        reincarnation();
                        dialog.dismiss();

                    }
                })
                .setNegativeButton("Retire", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        retire();
                        dialog.dismiss();

                    }
                });
        builder.setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
        TextView textView = dialog.findViewById(android.R.id.message);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
    }

    private void retire() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("A brief look back...")
                .setPositiveButton("EXIT GAME", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        exitMainActivity();
                    }
                })
                .setView(getLayoutInflater().inflate(R.layout.team_rankings_dialog, null));
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        dialog.show();

        String[] selection = {"Team History"};
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
                            TeamHistoryList teamHistoryAdapter =
                                    new TeamHistoryList(MainActivity.this, currentTeam.HC.get(0).getCoachHistory());
                            teamHistoryList.setAdapter(teamHistoryAdapter);
                        }
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        // do nothing
                    }
                });
    }

    private void reincarnation() {
        userHC = userTeam.getHC(0);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Coach History: " + currentTeam.HC.get(0).name)
                .setPositiveButton("Use Same Team", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        userHC.retired = true;
                        userHC.team = null;
                        simLeague.coachFreeAgents.add(userHC);
                        userTeam.setupUserCoach(userHC.name);
                        newGame = true;
                        userNameDialog();
                        dialog.dismiss();

                    }
                })
                .setNeutralButton("Pick New Team", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        reincarnate = true;
                        userHC.retired = true;
                        userHC.team = null;
                        simLeague.coachFreeAgents.add(userHC);

                        jobOffers(userHC);

                        newGame = true;

                        dialog.dismiss();

                    }
                })
                .setView(getLayoutInflater().inflate(R.layout.team_rankings_dialog, null));
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        dialog.show();

        String[] selection = {"Team History"};
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
                            TeamHistoryList teamHistoryAdapter =
                                    new TeamHistoryList(MainActivity.this, currentTeam.HC.get(0).getCoachHistory());
                            teamHistoryList.setAdapter(teamHistoryAdapter);
                        }
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        // do nothing
                    }
                });

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
            while ((line = reader.readLine()) != null && !line.contains("[END_CONFERENCES]")) {
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
            while ((line = reader.readLine()) != null && !line.contains("[END_TEAMS]")) {
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

    public File getExtSaveDir(Context context, String fileName) {
        File file = new File(context.getExternalFilesDir(
                Environment.DIRECTORY_DOCUMENTS), fileName);
        if (!file.mkdirs()) {
            Log.e(fileName, "Directory not created");
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

            //Name checker
            if (fileSplit.length > 1) {
                if (fileSplit[1].split(" ").length > 1) {

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
            }
        }
        reader.close();
        currTab = 3;
        viewRoster();
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
        //First ignore the save file info;
        while ((line = reader.readLine()) != null && !line.equals("END_ROSTER")) {
            String[] fileSplit = line.split(",");

            //Name checker
            if (fileSplit.length > 1) {
                if (fileSplit[1].split(" ").length > 1) {

                    //METHOD FOR CREATING NEW ROSTER WITH CUSTOM FILE
                    for (int i = 0; i < simLeague.teamList.size(); ++i) {
                        if (fileSplit[0].equals(simLeague.teamList.get(i).name)) {
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
            }

        }
        reader.close();

        for (int i = 0; i < simLeague.teamList.size(); ++i) {
            Team teamRoster = simLeague.teamList.get(i);
            if (teamRoster.getAllPlayers().isEmpty()) {
                teamRoster.newRoster(teamRoster.minQBs, teamRoster.minRBs, teamRoster.minWRs, teamRoster.minTEs, teamRoster.minOLs, teamRoster.minKs, teamRoster.minDLs, teamRoster.minLBs, teamRoster.minCBs, teamRoster.minSs, true);
            } else {
                teamRoster.newRoster(teamRoster.minQBs - teamRoster.teamQBs.size(), teamRoster.minRBs - teamRoster.teamRBs.size(), teamRoster.minWRs - teamRoster.teamWRs.size(),
                        teamRoster.minTEs - teamRoster.teamTEs.size(), teamRoster.minOLs - teamRoster.teamOLs.size(), teamRoster.minKs - teamRoster.teamKs.size(),
                        teamRoster.minDLs - teamRoster.teamDLs.size(), teamRoster.minLBs - teamRoster.teamLBs.size(), teamRoster.minCBs - teamRoster.teamCBs.size(), teamRoster.minSs - teamRoster.teamSs.size(), false);
            }
        }

        currTab = 3;

        simLeague.updateTeamTalentRatings();
        viewRoster();
    }

    //EXPORT DATA

    private void exportSave() {
        // Empty file, don't show dialog confirmation
        isExternalStorageReadable();
        isExternalStorageWritable();
        saveLeagueFile = new File(getExtSaveDir(this,"CFBCOACH"), "CFB_SAVE.txt");
        simLeague.saveLeague(saveLeagueFile);
        Toast.makeText(MainActivity.this, "Exported Save to Storage", Toast.LENGTH_SHORT).show();
    }

    //Export Save File
    private void exportTeams() {
        // Empty file, don't show dialog confirmation
        isExternalStorageReadable();
        isExternalStorageWritable();
        saveLeagueFile = new File(getExtSaveDir(this,"CFBCOACH"), "CFB_TEAMS.txt");
        simLeague.saveLeague(saveLeagueFile);
        Toast.makeText(MainActivity.this, "Saved league!", Toast.LENGTH_SHORT).show();
    }

    //Export Save File
    private void exportBowlNames() {
        // Empty file, don't show dialog confirmation
        isExternalStorageReadable();
        isExternalStorageWritable();
        saveLeagueFile = new File(getExtSaveDir(this,"CFBCOACH"), "CFB_BOWLS.txt");
        simLeague.saveLeague(saveLeagueFile);
        Toast.makeText(MainActivity.this, "Saved league!", Toast.LENGTH_SHORT).show();
    }

    //Export Save File
    private void exportPlayers() {
        // Empty file, don't show dialog confirmation
        isExternalStorageReadable();
        isExternalStorageWritable();
        saveLeagueFile = new File(getExtSaveDir(this,"CFBCOACH"), "CFB_PLAYERS.txt");
        simLeague.saveLeague(saveLeagueFile);
        Toast.makeText(MainActivity.this, "Saved league!", Toast.LENGTH_SHORT).show();
    }

    //Export Save File
    private void exportConferences() {
        // Empty file, don't show dialog confirmation
        isExternalStorageReadable();
        isExternalStorageWritable();
        saveLeagueFile = new File(getExtSaveDir(this,"CFBCOACH"), "CFB_CONF.txt");
        simLeague.saveLeague(saveLeagueFile);
        Toast.makeText(MainActivity.this, "Saved league!", Toast.LENGTH_SHORT).show();
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

    public void gameEditorV2() {
        currentTeam = userTeam;
        currentConference = simLeague.conferences.get(simLeague.getConfNumber(userTeam.conference));



        AlertDialog.Builder GameEditor = new AlertDialog.Builder(this);
        GameEditor.setTitle("Game Universe Editor v2 (BETA)")
                .setView(getLayoutInflater().inflate(R.layout.game_editor_full, null));
        final AlertDialog dialog = GameEditor.create();
        dialog.show();

        //setup window
        final List<String> teamEditor = new ArrayList<>();
        final List<String> confEditor = new ArrayList<>();
        final Spinner confList = dialog.findViewById(R.id.confList);
        final Spinner teamList = dialog.findViewById(R.id.teamList);
        final EditText changeNameEditText = dialog.findViewById(R.id.editTextChangeName);
        final EditText changeAbbrEditText = dialog.findViewById(R.id.editTextChangeAbbr);
        final EditText changeRivalEditText = dialog.findViewById(R.id.editRival);
        changeRivalEditText.setVisibility(View.INVISIBLE);

        final EditText changeConfEditText = dialog.findViewById(R.id.editTextChangeConf);
        final EditText changeHCEditText = dialog.findViewById(R.id.editTextChangeHC);
        final EditText changePrestigeEditText = dialog.findViewById(R.id.editPrestige);

        final ArrayAdapter<String> editorAdaptorConf = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, confEditor);
        editorAdaptorConf.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        confList.setAdapter(editorAdaptorConf);
        final ArrayAdapter<String> editorAdaptorTeam = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, teamEditor);
        editorAdaptorTeam.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        teamList.setAdapter(editorAdaptorTeam);

        Button cancelChangeNameButton = dialog.findViewById(R.id.buttonCancelChangeName);
        cancelChangeNameButton.setText("BACK");
        Button okChangeNameButton = dialog.findViewById(R.id.buttonOkChangeName);
        okChangeNameButton.setText("UPDATE");

        //fill in default data
        for (int i = 0; i < simLeague.conferences.size(); i++) {
            confEditor.add(simLeague.conferences.get(i).confName);
        }
        for (int i = 0; i < simLeague.conferences.get(simLeague.getConfNumber(currentTeam.conference)).confTeams.size(); i++) {
            teamEditor.add(simLeague.conferences.get(simLeague.getConfNumber(currentTeam.conference)).confTeams.get(i).name);
        }
        editorAdaptorConf.notifyDataSetChanged();
        editorAdaptorTeam.notifyDataSetChanged();

        //setup spinner data
        confList.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {

                        changeNameEditText.clearComposingText();
                        changeAbbrEditText.clearComposingText();
                        changeRivalEditText.clearComposingText();
                        changeConfEditText.clearComposingText();
                        changeHCEditText.clearComposingText();
                        changePrestigeEditText.clearComposingText();

                        currentConference = simLeague.conferences.get(position);
                        teamEditor.clear();
                        for (int i = 0; i < currentConference.confTeams.size(); i++) {
                            teamEditor.add(currentConference.confTeams.get(i).name);
                        }

                        editorAdaptorConf.notifyDataSetChanged();
                        editorAdaptorTeam.notifyDataSetChanged();
                        teamList.performClick();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

        teamList.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        Team tm = currentConference.confTeams.get(position);
                        currentTeam = tm;
                        currentConference = simLeague.conferences.get(simLeague.getConfNumber(currentTeam.conference));

                            changeNameEditText.setText(currentTeam.name);
                            changeAbbrEditText.setText(currentTeam.abbr);
                            changeRivalEditText.setText(currentTeam.division);
                            changeConfEditText.setText(currentConference.confName);
                            changeHCEditText.setText(currentTeam.HC.get(0).name);
                            changePrestigeEditText.setText(Integer.toString(currentTeam.teamPrestige));
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });


        cancelChangeNameButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                getSupportActionBar().setTitle(season + " | " + userTeam.name);
                updateCurrConference();  //updates the UI
                examineTeam(userTeam.name);
                dialog.dismiss();
                }
        });

        okChangeNameButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                String newName = changeNameEditText.getText().toString().trim();
                String newAbbr = changeAbbrEditText.getText().toString().trim().toUpperCase();
                String newRival = changeRivalEditText.getText().toString().trim().toUpperCase();
                String newConf = changeConfEditText.getText().toString().trim();
                String newHC = changeHCEditText.getText().toString().trim();
                int newPrestige = Integer.parseInt( changePrestigeEditText.getText().toString().trim() );

                if (simLeague.isNameValid(newName) && simLeague.isAbbrValid(newAbbr) && simLeague.isNameValid(newConf) && isNameValid((newHC))) {
                    simLeague.changeAbbrHistoryRecords(currentTeam.abbr, newAbbr);

                    if (newName != currentTeam.name) {
                        currentTeam.name = newName; //set new team name
                        teamEditor.clear();
                        for (int i = 0; i < currentConference.confTeams.size(); i++) {
                            teamEditor.add(currentConference.confTeams.get(i).name);
                        }
                        editorAdaptorTeam.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this, "Updated Team Name", Toast.LENGTH_SHORT).show();
                    }

                    if (newAbbr != currentTeam.abbr) currentTeam.abbr = newAbbr; //set new team Abbr

                    if (newHC != currentTeam.HC.get(0).name) currentTeam.HC.get(0).name = newHC; //set new HC name

                    if (newPrestige != currentTeam.teamPrestige) currentTeam.teamPrestige = newPrestige;

                    if (newConf != currentConference.confName) {
                        String oldConf = currentConference.confName;
                        currentConference.confName = newConf;
                        simLeague.updateTeamConf(newConf, oldConf, simLeague.getConfNumber(currentConference.confName));  //update all other conf teams

                        confEditor.clear();
                        for (int i = 0; i < simLeague.conferences.size(); i++) {
                            confEditor.add(simLeague.conferences.get(i).confName);
                        }
                        editorAdaptorConf.notifyDataSetChanged();
                        editorAdaptorTeam.notifyDataSetChanged();
                        Toast.makeText(MainActivity.this, "Updated Conference Name", Toast.LENGTH_SHORT).show();

                    }

/*                    if (newRival != currentTeam.division) {
                        currentTeam.division = newRival;
                    }*/


                    wantUpdateConf = true;
                } else {
                    if (showToasts)
                        Toast.makeText(MainActivity.this, "Invalid name/abbr! Name not changed.",
                                Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void fixBowlNames() {
        String[] bowls = simLeague.bowlNamesText.split(",");
        simLeague.bowlNames = new String[bowls.length];
        for(int i = 0; i < bowls.length; i++) {
            simLeague.bowlNames[i] = bowls[i];
        }
    }

    //allow the ability to enable editor to edit player names, positions, attributes, etc.
    private void playerEditor() {

    }

    public void userHallofFame() {
        //Retirement Hall of Fame

    }

    private void disciplineSetup() {
        userTeam.suspendPlayerSetup(this);
    }

    public void disciplineAction(final Player player, final String issue, final int gamesA, final int gamesB) {


        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Disicpline Action Required");
        builder.setMessage(player.position + " " + player.name + " (" + player.ratOvr + ") violated a team policy related to " + issue + ".\n\nHow do you want to proceed?");
        builder.setCancelable(false);
        builder.setPositiveButton("Suspend " + gamesA + " Games", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Perform action on click
                userTeam.disciplineAction(player, issue, gamesA, 2);
                dialog.dismiss();
                if (userTeam.suspension) showSuspensions();

            }
        });
        builder.setNegativeButton("Suspend " + gamesB + " Games", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Perform action on click
                userTeam.disciplineAction(player, issue, gamesB, 1);
                dialog.dismiss();
                if (userTeam.suspension) showSuspensions();

            }
        });
        builder.setNeutralButton("Ignore", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Perform action on click
                userTeam.disciplineAction(player, issue, gamesA, 3);
                dialog.dismiss();
                if (userTeam.suspension) showSuspensions();

            }
        });
        builder.show();
        userTeam.disciplineAction = false;
    }

    private void transferPlayer(ArrayList<String> players) {

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Select to Accept Transfer Request");
        builder.setCancelable(false);
    }

    public void crash() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("The DATABASE is invalid or corrupt. Please check for formatting or spelling errors.")
                .setPositiveButton("Exit to Main Screen", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Actually go back to main menu
                        finish();
                        Intent myIntent = new Intent(MainActivity.this, Home.class);
                        myIntent.putExtra("Theme", theme);
                        MainActivity.this.startActivity(myIntent);
                    }
                })
                .setCancelable(false);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

}