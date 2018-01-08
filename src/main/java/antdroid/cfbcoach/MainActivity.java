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

import Simulation.Conference;
import Simulation.Game;
import Simulation.HeadCoach;
import Simulation.League;
import Simulation.Player;
import Simulation.PlayerCB;
import Simulation.PlayerDL;
import Simulation.PlayerK;
import Simulation.PlayerLB;
import Simulation.PlayerOL;
import Simulation.PlayerQB;
import Simulation.PlayerRB;
import Simulation.PlayerS;
import Simulation.PlayerTE;
import Simulation.PlayerWR;
import Simulation.Team;
import Simulation.TeamStrategy;

public class MainActivity extends AppCompatActivity {
    private static final int READ_REQUEST_CODE = 43;
    public String oldConf;
    public HeadCoach userHC;
    int season;
    League simLeague;
    Conference currentConference;
    Team currentTeam;
    int currentConferenceID;
    Team userTeam;
    File saveLeagueFile;
    String username;
    Uri dataUri;
    String loadData;

    List<String> teamList;
    List<String> confList;

    int currTab;
    String userTeamStr;
    Spinner examineTeamSpinner;
    ArrayAdapter<String> dataAdapterTeam;
    Spinner examineConfSpinner;
    ArrayAdapter<String> dataAdapterConf;
    ListView mainList;
    ExpandableListView expListPlayerStats;

    //recruiting
    int recruitingStage;
    int wantUpdateConf;
    boolean showToasts;
    boolean showInjuryReport;

    //Universe Settings
    int teamsStart = 12;
    int confStart = 10;
    int seasonStart = 2017;

    String saveLeagueFileStr;
    File customConfs;
    File customTeams;
    File customBowls;
    String customUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Set up list
        mainList = findViewById(R.id.mainList);
        expListPlayerStats = findViewById(R.id.playerStatsExpandList);

        /*
          Determine whether to load League or start new one
         */
        Bundle extras = getIntent().getExtras();
        boolean loadedLeague = false;
        if (extras != null) {
            String saveFileStr = extras.getString("SAVE_FILE");
            if (saveFileStr.contains("NEW_LEAGUE_DYNASTY")) {
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
                } else {
                    simLeague = new League(getString(R.string.league_player_names), getString(R.string.league_last_names), false);
                    season = seasonStart;
                }
            } else if (saveFileStr.contains("NEW_LEAGUE_CAREER")) {
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
                } else {
                    simLeague = new League(getString(R.string.league_player_names), getString(R.string.league_last_names), true);
                    season = seasonStart;
                }
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
                } else {
                    simLeague = new League(getString(R.string.league_player_names), getString(R.string.league_last_names), false);
                    season = seasonStart;
                }
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
                } else {
                    simLeague = new League(getString(R.string.league_player_names), getString(R.string.league_last_names), false);
                    season = seasonStart;
                }
            }
        } else {
            simLeague = new League(getString(R.string.league_player_names), getString(R.string.league_last_names), false);
            season = seasonStart;
        }

        recruitingStage = -1;
        wantUpdateConf = 2; // 0 and 1, don't update, 2 update
        showToasts = false;
        showInjuryReport = true;
        simLeague.setTeamBenchMarks();

        if (!loadedLeague) {
            // Set it to 1st team until one selected
            userTeam = simLeague.teamList.get(0);
            simLeague.userTeam = userTeam;
            userTeam.userControlled = true;
            userTeamStr = userTeam.name;
            currentTeam = userTeam;
            // Set toolbar text to '2017 Season' etc
            updateTeamUI();

            currentTeam = simLeague.teamList.get(0);
            currentConference = simLeague.conferences.get(0);

            if (simLeague.isCareerMode()) {
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
                AlertDialog dialog = mode.create();
                dialog.show();
                TextView msgTxt = dialog.findViewById(android.R.id.message);
                msgTxt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            } else {
                //get user team from list dialog
                newGameDialog();
            }
        } else {
            updateTeamUI();
        }

        final TextView currentTeamText = findViewById(R.id.currentTeamText);
        currentTeamText.setText(currentTeam.name + " (" + currentTeam.wins + "-" + currentTeam.losses + ")");

        //Set up spinner for examining team.
        examineConfSpinner = findViewById(R.id.examineConfSpinner);
        confList = new ArrayList<String>();
        for (int i = 0; i < confStart; i++) {
            confList.add(simLeague.conferences.get(i).confName);
        }
        dataAdapterConf = new ArrayAdapter<String>(this,
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
        teamList = new ArrayList<String>();
        for (int i = 0; i < teamsStart; i++) {
            teamList.add(simLeague.teamList.get(i).strRep());
        }

        dataAdapterTeam = new ArrayAdapter<String>(this,
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


        /*
          Set up "Play Week" button
         */
        final Button simGameButton = findViewById(R.id.simGameButton);
        simGameButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (recruitingStage == -1) {
                    // Perform action on click
                    if (simLeague.currentWeek == 16) {
                        userHC = userTeam.HC.get(0);
                        simLeague.advanceHC();
                        if (simLeague.isCareerMode()) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setMessage(userTeam.contractString)
                                    .setTitle((seasonStart + userTeam.teamHistory.size()) + " Contract Status")
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
                        simGameButton.setTextSize(12);
                        simGameButton.setText("Off-Season: Contracts");
                        simLeague.currentWeek++;
                    } else if (simLeague.currentWeek == 17 && userTeam.fired) {
                        newJobV2(userHC);
                        simGameButton.setTextSize(12);
                        simGameButton.setText("Off-Season: Coaching Changes");
                        simLeague.currentWeek++;
                        showNewsStoriesDialog();
                    } else if (simLeague.currentWeek == 17 && !userTeam.fired) {
                        promotions(userHC);
                        simGameButton.setTextSize(12);
                        simGameButton.setText("Off-Season: Coaching Changes");
                        simLeague.currentWeek++;
                    } else if (simLeague.currentWeek == 18) {
                        simLeague.coachCarousel();
                        simGameButton.setTextSize(12);
                        simGameButton.setText("Off-Season: Graduation");
                        simLeague.currentWeek++;
                        showNewsStoriesDialog();
                    } else if (simLeague.currentWeek == 19) {
                        userTeam.resetStats();
                        simLeague.advanceSeason();
                        simGameButton.setTextSize(12);
                        simGameButton.setText("Off-Season: Transfer Players");
                        simLeague.currentWeek++;
                    } else if (simLeague.currentWeek == 20) {
                        simLeague.transferPlayers();
                        showNewsStoriesDialog();
                        simGameButton.setTextSize(12);
                        simGameButton.setText("Off-Season: Transfer Players II");
                        simLeague.currentWeek++;
                    } else if (simLeague.currentWeek == 21) {
                        showNewsStoriesDialog();

                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage(simLeague.userTransfers)
                                .setTitle((seasonStart + userTeam.teamHistory.size()) + " Transfers")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                        TextView textView = dialog.findViewById(android.R.id.message);
                        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

                        simGameButton.setTextSize(12);
                        simGameButton.setText("Begin Recruiting");
                        simLeague.currentWeek++;
                    } else if (simLeague.currentWeek >= 22) {
                        recruitingStage = 0;
                        simLeague.curePlayers(); // get rid of all injuries
                        beginRecruiting();
                    } else {
                        int numGamesPlayed = userTeam.gameWLSchedule.size();
                        simLeague.playWeek();

                        // Get injury report if there are injuries and just played a game
                        if (simLeague.currentWeek != 15 && showInjuryReport && userTeam.gameWLSchedule.size() > numGamesPlayed)
                            showInjuryReportDialog();

                        if (simLeague.currentWeek == 15) {
                            // Show NCG summary and check league records
                            simLeague.checkLeagueRecords();
                            simLeague.updateHCHistory();
                            simLeague.updateTeamHistories();
                            simLeague.updateLeagueHistory();
                            simLeague.curePlayers(); // get rid of all injuries
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setMessage(simLeague.seasonSummaryStr())
                                    .setTitle((seasonStart + userTeam.teamHistory.size()) + " Season Summary")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                            TextView textView = dialog.findViewById(android.R.id.message);
                            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
                            simGameButton.setTextSize(12);
                            simGameButton.setText("Begin Off-Season");
                            simLeague.currentWeek++;
                        } else if (userTeam.gameWLSchedule.size() > numGamesPlayed) {
                            // Played a game, show summary
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
                        }

                        updateCurrTeam();
                        scrollToLatestGame();

                    }
                } else {
                    //in process of recruiting
                    beginRecruiting();
                }
            }
        });


        final Button homeButton = findViewById(R.id.buttonHome);
        final Button newsButton = findViewById(R.id.buttonNews);
        final Button teamStatsButton = findViewById(R.id.teamStatsButton);
        final Button playerStatsButton = findViewById(R.id.playerStatsButton);
        final Button teamScheduleButton = findViewById(R.id.teamScheduleButton);
        final Button scoresButton = findViewById(R.id.buttonScores);
        final Button standingsButton = findViewById(R.id.standingsButton);
        final Button rankingsButton = findViewById(R.id.rankingsButton);
        final Button depthchartButton = findViewById(R.id.buttonDepthChart);
        final Button strategyButton = findViewById(R.id.buttonStrategy);

        //News
        newsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                showNewsStoriesDialog();
            }
        });

        //Set up "Team Stats" Button
        teamStatsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                currTab = 0;
                updateTeamStats();
            }
        });

        //Set up "Player Stats" Button
        playerStatsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                if (simLeague.currentWeek < 19) {
                    currTab = 1;
                    updatePlayerStats();
                }
            }
        });

        //Set up "Schedule" Button
        teamScheduleButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                currTab = 2;
                updateSchedule();
            }
        });

        //Set up "Weekly Scoreboard" Button
        scoresButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                showWeeklyScores();
            }
        });


        //Set up "Conf Standings" Button
        standingsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                currTab = 3;
                updateStandings();
            }
        });

        //Set up "Polls" Button
        rankingsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                currTab = 4;
                updateRankings();
            }
        });

        //Set up "home button""
        homeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                currentTeam = userTeam;
                examineTeam(currentTeam.name);
            }
        });

        //Set up "depthchart"
        depthchartButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                currentTeam = userTeam;
                if (simLeague.currentWeek < 19) showTeamLineupDialog();
            }
        });

        //Set up "strategy"
        strategyButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                currentTeam = userTeam;
                showTeamStrategyDialog();
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
                String heismanTop5Str = simLeague.getTop5HeismanStr();
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(heismanTop5Str)
                        .setTitle("Player of the Year Watch")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //do nothing?
                            }
                        });
                AlertDialog dialog = builder.create();
                dialog.show();
                TextView textView = dialog.findViewById(android.R.id.message);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            } else {
                // Show dialog with All American spinner too
                heismanCeremony();
            }
        } else if (id == R.id.action_rankings) {
            /*
              Clicked Team Rankings in drop down menu
             */
            showTeamRankingsDialog();
        } else if (id == R.id.action_player_rankings) {

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
            saveLeague();
        } else if (id == R.id.action_return_main_menu) {
            /*
              Let user confirm that they actually do want to go to main menu
             */
            exitMainActivity();
        } else if (id == R.id.action_change_team_name) {
            /*
              Let user change their team name and abbr
             */
            changeTeamNameDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    public void examineTeam(String teamName) {
        wantUpdateConf = 0;
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
                if (c == currentConference) wantUpdateConf = 1;
                currentConference = c;
                examineConfSpinner.setSelection(i);
                break;
            }
        }

        teamList = new ArrayList<String>();
        dataAdapterTeam.clear();
        for (int i = 0; i < currentConference.confTeams.size(); i++) {
            teamList.add(currentConference.confTeams.get(i).strRep());
            dataAdapterTeam.add(teamList.get(i));
        }
        dataAdapterTeam.notifyDataSetChanged();

        for (int i = 0; i < currentConference.confTeams.size(); ++i) {
            String[] spinnerSplit = dataAdapterTeam.getItem(i).split(" ");
            if (spinnerSplit[1].equals(tempT.abbr)) {
                examineTeamSpinner.setSelection(i);
                currentTeam = tempT;
                TextView currentTeamText = findViewById(R.id.currentTeamText);
                currentTeamText.setText("#" + currentTeam.rankTeamPollScore +
                        " " + currentTeam.name + " (" + currentTeam.wins + "-" + currentTeam.losses + ") " +
                        currentTeam.confChampion + " " + currentTeam.semiFinalWL + currentTeam.natChampWL);
                if (currTab == 0) {
                    updateTeamStats();
                } else if (currTab == 1) {
                    updatePlayerStats();
                } else {
                    updateSchedule();
                }
                break;
            }
        }
    }

    private void updateCurrTeam() {
        teamList = new ArrayList<String>();
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
        if (currTab == 0) {
            updateTeamStats();
        } else if (currTab == 1) {
            updatePlayerStats();
        } else {
            updateSchedule();
        }
    }

    private void updateCurrConference() {
        confList.clear();
        for (int i = 0; i < confStart; i++) {
            confList.add(simLeague.conferences.get(i).confName);
        }
        dataAdapterConf.notifyDataSetChanged();

        if (wantUpdateConf >= 1) {
            teamList = new ArrayList<String>();
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
            wantUpdateConf++;
        }
    }

    private void updateTeamStats() {
        mainList.setVisibility(View.VISIBLE);
        expListPlayerStats.setVisibility(View.GONE);

        String[] teamStatsStr = currentTeam.getTeamStatsStrCSV().split("%\n");
        mainList.setAdapter(new TeamStatsListArrayAdapter(this, teamStatsStr));
    }

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

    private void scrollToLatestGame() {
        if (currTab == 2 && simLeague.currentWeek > 2) {
            mainList.setSelection(currentTeam.numGames() - 3);
        }

    }


    // Shows Conference Standings
    private void updateStandings() {
        mainList.setVisibility(View.VISIBLE);
        expListPlayerStats.setVisibility(View.GONE);

        ArrayList<String> standings;
        standings = simLeague.getConfStandings();

        final MainRankings teamRankings = new MainRankings(this, standings, userTeam.strRepWithBowlResults());
        mainList.setAdapter(teamRankings);
    }


    // Shows AP Polls
    private void updateRankings() {

        mainList.setVisibility(View.VISIBLE);
        expListPlayerStats.setVisibility(View.GONE);

        ArrayList<String> standings;
        standings = simLeague.getTeamRankingsStr(0);

        final MainRankings teamRankings = new MainRankings(this, standings, userTeam.strRepWithBowlResults());
        mainList.setAdapter(teamRankings);
    }


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
            selection = new String[14];
            selection[0] = "Player of the Year";
            selection[1] = "Coach of the Year";
            selection[2] = "Freshman of the Year";
            selection[3] = "All Americans";
            for (int i = 0; i < confStart; ++i) {
                selection[i + 4] = "All " + simLeague.conferences.get(i).confName + " Team";
            }
        }

        Spinner potySpinner = dialog.findViewById(R.id.spinnerTeamRankings);
        ArrayAdapter<String> potyAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, selection);
        potyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        potySpinner.setAdapter(potyAdapter);

        final ListView potyList = dialog.findViewById(R.id.listViewTeamRankings);

        // Get all american and all conf
        final String[] coachAwardList = simLeague.getCoachAwardStr().split(">");
        final String[] freshmanAwardList = simLeague.getFreshmanCeremonyStr().split(">");
        final String[] allAmericans = simLeague.getAllAmericanStr().split(">");
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
                        } else {
                            potyList.setAdapter(new SeasonAwardsListArrayAdapter(MainActivity.this, allConference[position - 4], userTeam.abbr));
                        }
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        // do nothing
                    }
                });
    }

    public void showGameDialog(Game g) {
        final String[] gameStr;
        if (g.hasPlayed) {
            // Show game sumamry dialog
            gameStr = g.getGameSummaryStr();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle(g.awayTeam.abbr + " @ " + g.homeTeam.abbr + ": " + g.gameName)
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
            final ArrayAdapter<String> boxMenu = new ArrayAdapter<String>(this,
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
            builder.setTitle(g.awayTeam.abbr + " @ " + g.homeTeam.abbr + ": " + g.gameName)
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
                final TeamStrategy[] tsOff = userTeam.getTeamStrategiesOff();
                final TeamStrategy[] tsDef = userTeam.getTeamStrategiesDef();
                int offStratNum = 0;
                int defStratNum = 0;

                String[] stratOffSelection = new String[tsOff.length];
                for (int i = 0; i < tsOff.length; ++i) {
                    stratOffSelection[i] = tsOff[i].getStratName();
                    if (stratOffSelection[i].equals(userTeam.teamStratOff.getStratName()))
                        offStratNum = i;
                }

                String[] stratDefSelection = new String[tsDef.length];
                for (int i = 0; i < tsDef.length; ++i) {
                    stratDefSelection[i] = tsDef[i].getStratName();
                    if (stratDefSelection[i].equals(userTeam.teamStratDef.getStratName()))
                        defStratNum = i;
                }

                // Offense Strategy Spinner
                Spinner stratOffSelectionSpinner = dialog.findViewById(R.id.spinnerScoutOffenseStrategy);
                ArrayAdapter<String> stratOffSpinnerAdapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, stratOffSelection);
                stratOffSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                stratOffSelectionSpinner.setAdapter(stratOffSpinnerAdapter);
                stratOffSelectionSpinner.setSelection(offStratNum);

                stratOffSelectionSpinner.setOnItemSelectedListener(
                        new AdapterView.OnItemSelectedListener() {
                            public void onItemSelected(
                                    AdapterView<?> parent, View view, int position, long id) {
                                userTeam.teamStratOff = tsOff[position];
                                userTeam.teamStratOffNum = position;
                            }

                            public void onNothingSelected(AdapterView<?> parent) {
                                // do nothing
                            }
                        });

                // Defense Spinner Adapter
                Spinner stratDefSelectionSpinner = dialog.findViewById(R.id.spinnerScoutDefenseStrategy);
                ArrayAdapter<String> stratDefSpinnerAdapter = new ArrayAdapter<String>(this,
                        android.R.layout.simple_spinner_item, stratDefSelection);
                stratDefSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                stratDefSelectionSpinner.setAdapter(stratDefSpinnerAdapter);
                stratDefSelectionSpinner.setSelection(defStratNum);

                stratDefSelectionSpinner.setOnItemSelectedListener(
                        new AdapterView.OnItemSelectedListener() {
                            public void onItemSelected(
                                    AdapterView<?> parent, View view, int position, long id) {
                                userTeam.teamStratDef = tsDef[position];
                                userTeam.teamStratDefNum = position;
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

    public void showTeamRankingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Team Rankings")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing?
                    }
                })
                .setView(getLayoutInflater().inflate(R.layout.team_rankings_dialog, null));
        AlertDialog dialog = builder.create();
        dialog.show();

        ArrayList<String> rankings = new ArrayList<String>();// = simLeague.getTeamRankingsStr(0);
        String[] rankingsSelection =
                {"Poll Votes", "Conference Standings", "Strength of Schedule", "Strength of Wins", "Points Per Game", "Opp Points Per Game",
                        "Yards Per Game", "Opp Yards Per Game", "Pass Yards Per Game", "Rush Yards Per Game",
                        "Opp Pass YPG", "Opp Rush YPG", "TO Differential", "Off Talent", "Def Talent", "Prestige", "Recruiting Class" };
        Spinner teamRankingsSpinner = dialog.findViewById(R.id.spinnerTeamRankings);
        ArrayAdapter<String> teamRankingsSpinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, rankingsSelection);
        teamRankingsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        teamRankingsSpinner.setAdapter(teamRankingsSpinnerAdapter);

        final ListView teamRankingsList = dialog.findViewById(R.id.listViewTeamRankings);
        final TeamRankingsListArrayAdapter teamRankingsAdapter =
                new TeamRankingsListArrayAdapter(this, rankings, userTeam.strRepWithBowlResults());
        teamRankingsList.setAdapter(teamRankingsAdapter);

        teamRankingsSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        ArrayList<String> rankings = simLeague.getTeamRankingsStr(position);
                        if (position == 16) {
                            teamRankingsAdapter.setUserTeamStrRep(userTeam.strRepWithPrestige());
                        } else {
                            teamRankingsAdapter.setUserTeamStrRep(userTeam.strRepWithBowlResults());
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

    public void showPlayerRankingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Player Rankings")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing?
                    }
                })
                .setView(getLayoutInflater().inflate(R.layout.team_rankings_dialog, null));
        AlertDialog dialog = builder.create();
        dialog.show();

        ArrayList<String> rankings = new ArrayList<String>();
        String[] rankingsSelection =
                {"Passer Rating", "Passing Yards", "Passing TDs", "Interceptions Thrown", "Pass Comp PCT", "Rushing Yards", "Rushing TDs", "Receptions", "Receiving Yards", "Receiving TDs",
                        "Tackles", "Sacks", "Fumbles Recovered", "Interceptions", "Field Goals Made", "Field Goal Pct",
                        "Coach - Overall", "QB - Overall", "RB - Overall", "WR - Overall", "TE - Overall", "OL - Overall", "K - Overall", "DL - Overall", "LB - Overall", "CB - Overall", "S - Overall",
                        "Coach - Career Score"
                };
        Spinner teamRankingsSpinner = dialog.findViewById(R.id.spinnerTeamRankings);
        ArrayAdapter<String> teamRankingsSpinnerAdapter = new ArrayAdapter<String>(this,
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
                        ArrayList<String> rankings = simLeague.getPlayerRankStr(position);
                        if (position == 21) {
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
    }


    public void showRecruitingClassDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Recruiting Class Rankings")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing?
                    }
                })
                .setView(getLayoutInflater().inflate(R.layout.simple_list_dialog, null));
        AlertDialog dialog = builder.create();
        dialog.show();

        final ListView teamRankingsList = dialog.findViewById(R.id.listViewDialog);
        final TeamRankingsListArrayAdapter teamRankingsAdapter =
                new TeamRankingsListArrayAdapter(this, simLeague.getTeamRankingsStr(16), userTeam.strRepWithPrestige());
        teamRankingsList.setAdapter(teamRankingsAdapter);
    }

    public void showLeagueHistoryDialog() {
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

        String[] historySelection = {"League History", "League Records", "Hall of Fame", "League Stats"};
        Spinner leagueHistorySpinner = dialog.findViewById(R.id.spinnerTeamRankings);
        ArrayAdapter<String> leagueHistorySpinnerAdapter = new ArrayAdapter<String>(this,
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
                                    new LeagueRecordsListArrayAdapter(MainActivity.this, simLeague.getLeagueRecordsStr().split("\n"), userTeam.abbr);
                            leagueHistoryList.setAdapter(leagueRecordsAdapter);
                        } else if (position == 2) {
                            HallOfFameListArrayAdapter hofAdapter = new HallOfFameListArrayAdapter(MainActivity.this, hofPlayers);
                            leagueHistoryList.setAdapter(hofAdapter);
                        } else if (position == 3) {
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

    public void showLeagueHistoryStats() {
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

        ArrayList<String> rankings = new ArrayList<String>();// = simLeague.getTeamRankingsStr(0);
        String[] rankingsSelection =
                {"National Championships", "Conference Championships", "Bowl Victories", "Total Wins", "Active Coach Career Score"};
        Spinner teamRankingsSpinner = dialog.findViewById(R.id.spinnerTeamRankings);
        ArrayAdapter<String> teamRankingsSpinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, rankingsSelection);
        teamRankingsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        teamRankingsSpinner.setAdapter(teamRankingsSpinnerAdapter);

        final ListView teamRankingsList = dialog.findViewById(R.id.listViewTeamRankings);
        final TeamRankingsListArrayAdapter teamRankingsAdapter =
                new TeamRankingsListArrayAdapter(this, rankings, userTeam.strRepWithBowlResults());
        teamRankingsList.setAdapter(teamRankingsAdapter);

        teamRankingsSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        ArrayList<String> rankings = simLeague.getLeagueHistoryStats(position);
                        if (position == 4) {
                            teamRankingsAdapter.setUserTeamStrRep(userTeam.strRepWithPrestige());
                        } else {
                            teamRankingsAdapter.setUserTeamStrRep(userTeam.strRepWithBowlResults());
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

    public void showTop25History() {
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
            final ArrayAdapter<String> top25Adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, selection);
            top25Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            top25hisSpinner.setAdapter(top25Adapter);
        } else {
            String[] selection = new String[simLeague.leagueHistory.size()];
            for (int i = 0; i < simLeague.leagueHistory.size(); ++i) {
                selection[i] = Integer.toString(seasonStart + i);
            }
            Spinner top25hisSpinner = dialog.findViewById(R.id.spinnerBowlCCG);
            final ArrayAdapter<String> top25Adapter = new ArrayAdapter<String>(this,
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

    public void showCoachHistoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Coach History: " + currentTeam.HC.get(0).name)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing?
                    }
                })
                .setView(getLayoutInflater().inflate(R.layout.team_rankings_dialog, null));
        AlertDialog dialog = builder.create();
        dialog.show();

        String[] selection = {"Team History"};
        Spinner teamHistSpinner = dialog.findViewById(R.id.spinnerTeamRankings);
        final ArrayAdapter<String> teamHistAdapter = new ArrayAdapter<String>(this,
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
                        }
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        // do nothing
                    }
                });
    }

    public void showCurrTeamHistoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Team History: " + currentTeam.name)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do nothing?
                    }
                })
                .setView(getLayoutInflater().inflate(R.layout.team_rankings_dialog, null));
        AlertDialog dialog = builder.create();
        dialog.show();

        String[] selection = {"Team History", "Team Records", "Hall of Fame"};
        Spinner teamHistSpinner = dialog.findViewById(R.id.spinnerTeamRankings);
        final ArrayAdapter<String> teamHistAdapter = new ArrayAdapter<String>(this,
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
                                    new LeagueRecordsListArrayAdapter(MainActivity.this, currentTeam.teamRecords.getRecordsStr().split("\n"), "---");
                            teamHistoryList.setAdapter(leagueRecordsAdapter);
                        } else {
                            HallOfFameListArrayAdapter hofAdapter = new HallOfFameListArrayAdapter(MainActivity.this, hofPlayers);
                            teamHistoryList.setAdapter(hofAdapter);
                        }
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        // do nothing
                    }
                });
    }


    public void showBowlCCGDialog() {
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
        ArrayAdapter<String> bowlCCGadapter = new ArrayAdapter<String>(this,
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

    public void showNewsStoriesDialog() {
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

        ArrayList<String> rankings = new ArrayList<String>();// = simLeague.getTeamRankingsStr(0);
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
        ArrayAdapter<String> weekSelectionSpinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, weekSelection);
        weekSelectionSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weekSelectionSpinner.setAdapter(weekSelectionSpinnerAdapter);
        if (simLeague.currentWeek > 15 && simLeague.currentWeek < 19) {
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
                            rankings.add("National Letter of Intention Day!>Today marks the first day of open recruitment. Teams are now allowed to sign incoming freshman to their schools.");
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

    /**
     * Dialog for coaches to select their team's strategy for offense and defense.
     */
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
        final TeamStrategy[] tsOff = userTeam.getTeamStrategiesOff();
        final TeamStrategy[] tsDef = userTeam.getTeamStrategiesDef();
        int offStratNum = 0;
        int defStratNum = 0;

        String[] stratOffSelection = new String[tsOff.length];
        for (int i = 0; i < tsOff.length; ++i) {
            stratOffSelection[i] = tsOff[i].getStratName();
            if (stratOffSelection[i].equals(userTeam.teamStratOff.getStratName())) offStratNum = i;
        }

        String[] stratDefSelection = new String[tsDef.length];
        for (int i = 0; i < tsDef.length; ++i) {
            stratDefSelection[i] = tsDef[i].getStratName();
            if (stratDefSelection[i].equals(userTeam.teamStratDef.getStratName())) defStratNum = i;
        }

        final TextView offStratDescription = dialog.findViewById(R.id.textOffenseStrategy);
        final TextView defStratDescription = dialog.findViewById(R.id.textDefenseStrategy);

        // Offense Strategy Spinner
        Spinner stratOffSelectionSpinner = dialog.findViewById(R.id.spinnerOffenseStrategy);
        ArrayAdapter<String> stratOffSpinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, stratOffSelection);
        stratOffSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stratOffSelectionSpinner.setAdapter(stratOffSpinnerAdapter);
        stratOffSelectionSpinner.setSelection(offStratNum);

        stratOffSelectionSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        offStratDescription.setText(tsOff[position].getStratDescription());
                        userTeam.teamStratOff = tsOff[position];
                        userTeam.teamStratOffNum = position;
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        // do nothing
                    }
                });

        // Defense Spinner Adapter
        Spinner stratDefSelectionSpinner = dialog.findViewById(R.id.spinnerDefenseStrategy);
        ArrayAdapter<String> stratDefSpinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, stratDefSelection);
        stratDefSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        stratDefSelectionSpinner.setAdapter(stratDefSpinnerAdapter);
        stratDefSelectionSpinner.setSelection(defStratNum);

        stratDefSelectionSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        defStratDescription.setText(tsDef[position].getStratDescription());
                        userTeam.teamStratDef = tsDef[position];
                        userTeam.teamStratDefNum = position;
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        // do nothing
                    }
                });

    }

    /**
     * Open dialog that allows users to change their name and/or abbr.
     */
    private void changeTeamNameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Settings / Editor")
                .setView(getLayoutInflater().inflate(R.layout.change_team_name_dialog, null));
        final AlertDialog dialog = builder.create();
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

        final CheckBox checkboxShowPopup = dialog.findViewById(R.id.checkboxShowPopups);
        checkboxShowPopup.setChecked(showToasts);

        final CheckBox checkboxShowInjury = dialog.findViewById(R.id.checkboxShowInjuryReport);
        checkboxShowInjury.setChecked(showInjuryReport);

        Button cancelChangeNameButton = dialog.findViewById(R.id.buttonCancelChangeName);
        Button okChangeNameButton = dialog.findViewById(R.id.buttonOkChangeName);
        Button changeTeamsButton = dialog.findViewById(R.id.buttonChangeTeams);

        cancelChangeNameButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                showToasts = checkboxShowPopup.isChecked();
                showInjuryReport = checkboxShowInjury.isChecked();
                userTeam.showPopups = showToasts;
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

                if (simLeague.isNameValid(newName) && simLeague.isAbbrValid(newAbbr) && simLeague.isNameValid(newConf) && simLeague.isNameValid((newHC))) {
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
                    //examineTeam(currentTeam.name);
                    wantUpdateConf = 2;
                    updateCurrConference();  //updates the UI

                } else {
                    if (showToasts)
                        Toast.makeText(MainActivity.this, "Invalid name/abbr! Name not changed.",
                                Toast.LENGTH_SHORT).show();
                }
                showToasts = checkboxShowPopup.isChecked();
                showInjuryReport = checkboxShowInjury.isChecked();
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
                        if (simLeague.isCareerMode()) newJob(userHC);
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

    /**
     * Show injury report.
     */
    public void showInjuryReportDialog() {
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

    public void setShowInjuryReport(boolean show) {
        showInjuryReport = show;
    }

    /**
     * Allow users to set lineups here.
     */
    private void showTeamLineupDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Set Team Lineup")
                .setView(getLayoutInflater().inflate(R.layout.team_lineup_dialog, null));
        final AlertDialog dialog = builder.create();
        dialog.show();

        final String[] positionSelection = {"QB (1 starter)", "RB (2 starters)", "WR (3 starters)", "TE (1 starter)", "OL (5 starters)",
                "K (1 starter)", "DL (4 starters)", "LB (3 starters)", "CB (3 starters)", "S (1 starter)"};
        final int[] positionNumberRequired = {1, 2, 3, 1, 5, 1, 4, 3, 3, 1};
        final Spinner teamLineupPositionSpinner = dialog.findViewById(R.id.spinnerTeamLineupPosition);
        ArrayAdapter<String> teamLineupPositionSpinnerAdapter = new ArrayAdapter<String>(this,
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

    /**
     * Update the lineup list to a new position.
     * Has checked boxes for all the starters.
     *
     * @param position               position looking at (0-7)
     * @param teamLineupAdapter      TeamLineupArrayAdapter storing the data
     * @param positionNumberRequired number of players required by the position (1 for QB, 3 for WR, etc)
     * @param positionPlayers        arraylist of players
     */
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

    /**
     * Examines a player on the bench of the team, so users can see all their stats.
     *
     * @param player to examine
     */
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

    /**
     * Checks to see if a player won any awards, for highlighting purposes.
     * Uses findBenchPlayer(..) to find the correct player.
     *
     * @param player as a string
     * @return 3 if POTY, 2 if AA, 1 if AC
     */
    public int checkAwardPlayer(String player) {
        Player p = currentTeam.findBenchPlayer(player);
        if (p == null) return 0;
        if (p.wonHeisman) return 3;
        if (p.wonAllAmerican) return 2;
        if (p.wonAllConference) return 1;
        return 0;
    }

    /**
     * Start Recruiting Activity, sending over the user team's players and budget.
     */
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
                        sb.append(userTeam.conference + "," + userTeam.name + "," + userTeam.abbr + "," + userTeam.teamPrestige + "," + userTeam.HC.get(0).ratTalent + "%\n");
                        sb.append(userTeam.getPlayerInfoSaveFile());
                        sb.append("END_TEAM_INFO%\n");
                        sb.append(userTeam.getRecruitsInfoSaveFile());

                        //Start Recruiting Activity
                        Intent myIntent = new Intent(MainActivity.this, RecruitingActivity.class);
                        myIntent.putExtra("USER_TEAM_INFO", sb.toString());
                        MainActivity.this.startActivity(myIntent);
                    }
                })
                .setView(getLayoutInflater().inflate(R.layout.team_rankings_dialog, null));
        AlertDialog dialog = builder.create();
        dialog.show();

        String[] spinnerSelection = {"Players Leaving", "Mock Draft"};
        Spinner beginRecruitingSpinner = dialog.findViewById(R.id.spinnerTeamRankings);
        ArrayAdapter<String> beginRecruitingSpinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, spinnerSelection);
        beginRecruitingSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        beginRecruitingSpinner.setAdapter(beginRecruitingSpinnerAdapter);

        final ListView playerList = dialog.findViewById(R.id.listViewTeamRankings);
        final PlayerStatsListArrayAdapter playerStatsAdapter =
                new PlayerStatsListArrayAdapter(this, userTeam.getGradPlayersList());
        final MockDraftListArrayAdapter mockDraftAdapter =
                new MockDraftListArrayAdapter(this, simLeague.getMockDraftPlayersList(), userTeam.strRep());
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

    @Override
    public void onBackPressed() {
        exitMainActivity();
    }

    /**
     * Exit the main activity. Show dialog to ask if they are sure they want to quit, and give info about saving.
     */
    private void exitMainActivity() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Are you sure you want to return to main menu? Any progress from the beginning of the season will be lost.")
                .setPositiveButton("Yes, Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Actually go back to main menu
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

    /**
     * Save League, show dialog to choose which save file to save onto.
     */
    private void saveLeague() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Save File to Overwrite:");
        final String[] fileInfos = getSaveFileInfos();
        SaveFilesListArrayAdapter saveFilesAdapter = new SaveFilesListArrayAdapter(this, fileInfos);
        builder.setAdapter(saveFilesAdapter, new DialogInterface.OnClickListener() {
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
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do nothing
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * Get info of the 5 save files for printing in the save file list
     */
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

    public void showWeeklyScores() {
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

        ArrayList<String> rankings = new ArrayList<String>();// = simLeague.getTeamRankingsStr(0);
        String[] weekSelection = new String[simLeague.currentWeek + 1];
        for (int i = 0; i < weekSelection.length; ++i) {
            if (i == 13) weekSelection[i] = "Conf Champ Week";
            else if (i == 14) weekSelection[i] = "Bowl Game Week";
            else if (i == 15) weekSelection[i] = "National Champ";
            else weekSelection[i] = "Week " + i;
        }
        Spinner weekSelectionSpinner = dialog.findViewById(R.id.spinnerTeamRankings);
        ArrayAdapter<String> weekSelectionSpinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, weekSelection);
        weekSelectionSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weekSelectionSpinner.setAdapter(weekSelectionSpinnerAdapter);
        weekSelectionSpinner.setSelection(simLeague.currentWeek);

        final ListView newsStoriesList = dialog.findViewById(R.id.listViewTeamRankings);
        final NewsStoriesListArrayAdapter newsStoriesAdapter = new NewsStoriesListArrayAdapter(this, rankings);
        newsStoriesList.setAdapter(newsStoriesAdapter);

        weekSelectionSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        ArrayList<String> rankings = simLeague.weeklyScores.get(position);
                        boolean isempty = false;
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


    public void updateTeamUI() {
        if (simLeague.isCareerMode()) {
            getSupportActionBar().setTitle(season + " | " + userTeam.name + " | Career Mode");
        } else {
            getSupportActionBar().setTitle(season + " | " + userTeam.name + " | Dynasty Mode");
        }
    }

    //Choose ANY team
    public void switchTeams(HeadCoach headCoach) {
        userHC = headCoach;
        updateTeamUI();
        //get user team from list dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose your new team:");
        final String[] teams = simLeague.getTeamListStr();
        builder.setItems(teams, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                userTeam.userControlled = false;
                userTeam.HC.clear();
                userTeam.newRoster(0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
                simLeague.newJobtransfer(simLeague.teamList.get(item).name);
                userTeam = simLeague.userTeam;
                userTeamStr = userTeam.name;
                currentTeam = userTeam;
                userTeam.HC.clear();
                userTeam.HC.add(userHC);
                userTeam.fired = false;
                userHC.contractYear = 0;
                userHC.contractLength = 4;
                userHC.baselinePrestige = userTeam.teamPrestige;
                updateTeamUI();
                examineTeam(currentTeam.name);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    //Only schools lower than Coach Rating will offer jobs
    public void newJob(HeadCoach headCoach) {
        userHC = headCoach;
        int ratOvr = userHC.ratOvr;
        if (ratOvr < 40) ratOvr = 40;
        int offers = (int) (Math.random() * 5);
        if (offers < 1) offers = 1;
        updateTeamUI();
        //get user team from list dialog
        final ArrayList<String> teamsArray = simLeague.getCoachListStr((ratOvr - 10), offers);
        final ArrayList<Team> coachList = simLeague.getCoachList((ratOvr - 10), offers);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Job Offers Available:");
        final String[] teams = teamsArray.toArray(new String[teamsArray.size()]);
        builder.setItems(teams, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                userTeam.userControlled = false;
                userTeam.HC.clear();
                userTeam.newRoster(0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
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
                updateTeamUI();
                examineTeam(currentTeam.name);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    //Only schools lower than Coach Rating will offer jobs
    //Version 2 will only have offers from schools with coaching vacancies and teams looking to improve
    public void newJobV2(HeadCoach headCoach) {
        userHC = headCoach;
        int ratOvr = userHC.ratOvr;
        if (ratOvr < 40) ratOvr = 40;
/*        int offers = (int) (Math.random() * 5);
        if (offers < 1) offers = 1;*/
        int offers = 8;  //skip every 'offers' teams during for loop
        String oldTeam = userHC.team.name;
        updateTeamUI();
        //get user team from list dialog
        final ArrayList<String> teamsArray = simLeague.getCoachListStrV2((ratOvr - 10), offers, oldTeam);
        final ArrayList<Team> coachList = simLeague.getCoachListV2((ratOvr - 10), offers, oldTeam);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Job Offers Available:");
        final String[] teams = teamsArray.toArray(new String[teamsArray.size()]);
        builder.setItems(teams, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                // Do something with the selection
                userTeam.userControlled = false;
                userTeam.HC.clear();
                userTeam.newRoster(0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
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
                updateTeamUI();
                examineTeam(currentTeam.name);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    //Job offers promotions
    public void promotions(HeadCoach headCoach) {
        userHC = headCoach;
        if (userHC.promotionCandidate) {

            int ratOvr = userHC.ratOvr;
            if (ratOvr < 40) ratOvr = 40;
            double offers = 2;
            String oldTeam = userHC.team.name;
            updateTeamUI();
            //get user team from list dialog
            final ArrayList<String> teamsArray = simLeague.getCoachPromotionListStr((ratOvr - 10), offers, oldTeam);
            final ArrayList<Team> coachList = simLeague.getCoachPromotionList((ratOvr - 10), offers, oldTeam);
            if (coachList.isEmpty()) {

                showNewsStoriesDialog();

            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Job Offers Available:")
                        .setPositiveButton("Decline Offers", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                showNewsStoriesDialog();

                            }
                        });
                final String[] teams = teamsArray.toArray(new String[teamsArray.size()]);
                builder.setItems(teams, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // Do something with the selection
                        userTeam.userControlled = false;
                        userTeam.HC.clear();
                        userTeam.newRoster(0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
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
                        updateTeamUI();
                        examineTeam(currentTeam.name);
                        showNewsStoriesDialog();

                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        } else {

            showNewsStoriesDialog();
        }
    }

    private void newGameHardDialog() {
        int offers = (int) (Math.random() * 5);
        if (offers < 1) offers = 1;
        final ArrayList<Team> coachList = simLeague.getCoachList(50, offers);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Job Offers Available:");
        final ArrayList<String> teamsArray = simLeague.getCoachListStr(50, offers);
        final String[] teams = teamsArray.toArray(new String[teamsArray.size()]);
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
                simLeague.preseasonNews();
                userHC = userTeam.HC.get(0);
                // Set toolbar text to '2017 Season' etc
                updateTeamUI();
                examineTeam(currentTeam.name);
            }
        });
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
                simLeague.preseasonNews();
                userHC = userTeam.HC.get(0);
                // Set toolbar text to '2017 Season' etc
                updateTeamUI();
                examineTeam(currentTeam.name);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


    private void userNameDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter Name:")
                .setView(getLayoutInflater().inflate(R.layout.username_dialog, null));
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
                if (simLeague.isNameValid((newHC))) {
                    userTeam.HC.get(0).name = newHC;
                    dialog.dismiss();
                } else {
                    if (showToasts)
                        Toast.makeText(MainActivity.this, "Invalid name/abbr! Name not changed.",
                                Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });
    }

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
    public boolean isExternalStorageReadable() {
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

    public void importData() {
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

    public void readCoachFile(Uri uri) throws IOException {
        String line;
        InputStream inputStream = getContentResolver().openInputStream(uri);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        line = reader.readLine();
        //First ignore the save file info
        while ((line = reader.readLine()) != null && !line.equals("END_COACHES")) {
            String[] fileSplit = line.split(",");
            for (int i = 0; i < simLeague.teamList.size(); ++i) {
                if (fileSplit[0].toString().equals(simLeague.teamList.get(i).name)) {
                    if (fileSplit.length > 2) {
                        simLeague.teamList.get(i).HC.clear();
                        simLeague.teamList.get(i).newCustomHeadCoach(fileSplit[1].toString(), Integer.parseInt(fileSplit[2]));
                    } else {
                        simLeague.teamList.get(i).HC.get(0).name = fileSplit[1].toString();
                    }
                }
            }
        }
        reader.close();
        userTeam.setupUserCoach(userHC.name);
    }

    public void readRosterFile(Uri uri) throws IOException {
        boolean custom = true;

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
        simLeague.newsStories.get(0).remove(2);
        simLeague.newsStories.get(0).remove(2);
        simLeague.topRecruits();
    }

}