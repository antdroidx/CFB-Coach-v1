package antdroid.cfbcoach;

import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.os.Environment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;

import CFBsimPack.Player;
import CFBsimPack.TeamStrategy;
import antdroid.cfbcoach.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Collections;

import CFBsimPack.Conference;
import CFBsimPack.Game;
import CFBsimPack.League;
import CFBsimPack.Team;

public class MainActivity extends AppCompatActivity {

    int season;
    League simLeague;
    Conference currentConference;
    Team currentTeam;
    public String oldConf, newConf;
    int currentConferenceID;
    Team userTeam;
    File saveLeagueFile;

    List<String> teamList;
    List<String> confList;

    int currTab;
    String userTeamStr;
    Spinner examineTeamSpinner;
    ArrayAdapter<String> dataAdapterTeam;
    Spinner examineConfSpinner;
    ArrayAdapter<String> dataAdapterConf;
    ArrayList<String> scoreboard;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Set up list
        mainList = (ListView) findViewById(R.id.mainList);
        expListPlayerStats = (ExpandableListView) findViewById(R.id.playerStatsExpandList);

        /**
         * Determine whether to load League or start new one
         */
        Bundle extras = getIntent().getExtras();
        boolean loadedLeague = false;
        if (extras != null) {
            String saveFileStr = extras.getString("SAVE_FILE");
            if (saveFileStr.equals("NEW_LEAGUE")) {
                simLeague = new League(getString(R.string.league_player_names), getString(R.string.league_last_names));
                season = seasonStart;
            } else if (saveFileStr.equals("DONE_RECRUITING")) {
                File saveFile = new File(getFilesDir(), "saveLeagueRecruiting.cfb");
                if (saveFile.exists()) {
                    simLeague = new League(saveFile, getString(R.string.league_player_names), getString(R.string.league_last_names));
                    userTeam = simLeague.userTeam;
                    userTeamStr = userTeam.name;
                    userTeam.recruitPlayersFromStr(extras.getString("RECRUITS"));
                    simLeague.updateTeamTalentRatings();
                    simLeague.updateTeamTalentRatings();
                    season = simLeague.getYear();
                    currentTeam = userTeam;
                    loadedLeague = true;
                } else {
                    simLeague = new League(getString(R.string.league_player_names), getString(R.string.league_last_names));
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
                    simLeague = new League(getString(R.string.league_player_names), getString(R.string.league_last_names));
                    season = seasonStart;
                }
            }
        } else {
            simLeague = new League(getString(R.string.league_player_names), getString(R.string.league_last_names));
            season = seasonStart;
        }

        recruitingStage = -1;
        wantUpdateConf = 2; // 0 and 1, don't update, 2 update
        showToasts = false;
        showInjuryReport = true;

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
            //get user team from list dialog
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
                    // set rankings so that not everyone is rank #0
                    simLeague.setTeamRanks();
                    // Set toolbar text to '2017 Season' etc
                    updateTeamUI();
                    examineTeam(currentTeam.name);
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        } else {
            // Set toolbar text to '2017 Season' etc
            updateTeamUI();
        }

        final TextView currentTeamText = (TextView) findViewById(R.id.currentTeamText);
        currentTeamText.setText(currentTeam.name + " (" + currentTeam.wins + "-" + currentTeam.losses + ")");

        //Set up spinner for examining team.
        examineConfSpinner = (Spinner) findViewById(R.id.examineConfSpinner);
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

        examineTeamSpinner = (Spinner) findViewById(R.id.examineTeamSpinner);
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
                        //heh
                        //updateCurrTeam();
                    }
                });


        /**
         * Set up "Play Week" button
         */
        final Button simGameButton = (Button) findViewById(R.id.simGameButton);
        simGameButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (recruitingStage == -1) {
                    // Perform action on click
                    if (simLeague.currentWeek == 15) {
                        recruitingStage = 0;
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
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setMessage(simLeague.seasonSummaryStr())
                                    .setTitle((seasonStart + userTeam.teamHistory.size()) + " Season Summary")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            //do nothing?
                                        }
                                    });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                            TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
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
                            simGameButton.setText("Play Week");
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
                        } else {
                            simGameButton.setTextSize(14);
                            simGameButton.setText("Begin Recruiting");
                            simLeague.curePlayers(); // get rid of all injuries
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

        final Button homeButton = (Button) findViewById(R.id.buttonHome);
        final Button newsButton = (Button) findViewById(R.id.buttonNews);
        final Button teamStatsButton = (Button) findViewById(R.id.teamStatsButton);
        final Button playerStatsButton = (Button) findViewById(R.id.playerStatsButton);
        final Button teamScheduleButton = (Button) findViewById(R.id.teamScheduleButton);
        final Button standingsButton = (Button) findViewById(R.id.standingsButton);
        final Button rankingsButton = (Button) findViewById(R.id.rankingsButton);
        final Button depthchartButton = (Button) findViewById(R.id.buttonDepthChart);
        final Button strategyButton = (Button) findViewById(R.id.buttonStrategy);

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
                currTab = 1;
                updatePlayerStats();
            }
        });

        //Set up "Schedule" Button
        teamScheduleButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                currTab = 2;
                updateSchedule();
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
                //currTab = 3;
                currentTeam = userTeam;
                showTeamLineupDialog();
            }
        });

        //Set up "strategy"
        strategyButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                //currTab = 3;
                currentTeam = userTeam;
                showTeamStrategyDialog();
            }
        });

        if (loadedLeague) {
            // Set rankings so that not everyone is rank #0
            simLeague.setTeamRanks();
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
            /**
             * Clicked Heisman watch in drop down menu
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
                TextView textView = (TextView) dialog.findViewById(android.R.id.message);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            } else {
                // Show dialog with All American spinner too
                heismanCeremony();
            }
        } else if (id == R.id.action_rankings) {
            /**
             * Clicked Team Rankings in drop down menu
             */
            showTeamRankingsDialog();
        } else if (id == R.id.action_current_team_history) {
            /**
             * Current selected team history
             */
            showCurrTeamHistoryDialog();
        } else if (id == R.id.action_league_history) {
            /**
             * Clicked League History in drop down menu
             */
            showLeagueHistoryDialog();
         } else if (id == R.id.action_top_25_history) {
             /**
              * Clicked Top 25 History
              */
             showTop25History();
        } else if (id == R.id.action_team_history) {
            /**
             * Clicked User Team History in drop down menu
             */
            showUserTeamHistoryDialog();
        } else if (id == R.id.action_ccg_bowl_watch) {
            /**
             * Clicked CCG / Bowl Watch in drop down menu
             */
            showBowlCCGDialog();
        } else if (id == R.id.action_save_league) {
            /**
             * Clicked Save League in drop down menu
             */
            saveLeague();
        } else if (id == R.id.action_return_main_menu) {
            /**
             * Let user confirm that they actually do want to go to main menu
             */
            exitMainActivity();
        } else if (id == R.id.action_change_team_name) {
            /**
             * Let user change their team name and abbr
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
                TextView currentTeamText = (TextView) findViewById(R.id.currentTeamText);
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
        TextView currentTeamText = (TextView) findViewById(R.id.currentTeamText);
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
            selection = new String[12];
            selection[0] = "Player of the Year";
            selection[1] = "All Americans";
            for (int i = 0; i < confStart; ++i) {
                selection[i + 2] = "All " + simLeague.conferences.get(i).confName + " Team";
            }
        }

        Spinner potySpinner = (Spinner) dialog.findViewById(R.id.spinnerTeamRankings);
        ArrayAdapter<String> potyAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, selection);
        potyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        potySpinner.setAdapter(potyAdapter);

        final ListView potyList = (ListView) dialog.findViewById(R.id.listViewTeamRankings);

        // Get all american and all conf
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
                            potyList.setAdapter(new SeasonAwardsListArrayAdapter(MainActivity.this, allAmericans, userTeam.abbr));
                        } else {
                            potyList.setAdapter(new SeasonAwardsListArrayAdapter(MainActivity.this, allConference[position - 2], userTeam.abbr));
                        }
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        // do nothing
                    }
                });
    }

    public void showGameDialog(Game g) {
        String[] gameStr;
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
            final TextView gameAwayScore = (TextView) dialog.findViewById(R.id.gameDialogScoreAway);
            final TextView gameHomeScore = (TextView) dialog.findViewById(R.id.gameDialogScoreHome);
            final TextView gameAwayScoreName = (TextView) dialog.findViewById(R.id.gameDialogScoreAwayName);
            final TextView gameHomeScoreName = (TextView) dialog.findViewById(R.id.gameDialogScoreHomeName);
            gameAwayScore.setText(g.awayScore + "");
            gameHomeScore.setText(g.homeScore + "");
            gameAwayScoreName.setText(g.awayTeam.getStrAbbrWL_2Lines());
            gameHomeScoreName.setText(g.homeTeam.getStrAbbrWL_2Lines());


            final TextView awayTeam = (TextView) dialog.findViewById(R.id.teamAway);
            final TextView awayQT1 = (TextView) dialog.findViewById(R.id.awayQT1);
            final TextView awayQT2 = (TextView) dialog.findViewById(R.id.awayQT2);
            final TextView awayQT3 = (TextView) dialog.findViewById(R.id.awayQT3);
            final TextView awayQT4 = (TextView) dialog.findViewById(R.id.awayQT4);
            final TextView awayOT = (TextView) dialog.findViewById(R.id.awayOT);
            final TextView homeTeam = (TextView) dialog.findViewById(R.id.teamHome);
            final TextView homeQT1 = (TextView) dialog.findViewById(R.id.homeQT1);
            final TextView homeQT2 = (TextView) dialog.findViewById(R.id.homeQT2);
            final TextView homeQT3 = (TextView) dialog.findViewById(R.id.homeQT3);
            final TextView homeQT4 = (TextView) dialog.findViewById(R.id.homeQT4);
            final TextView homeOT = (TextView) dialog.findViewById(R.id.homeOT);
            final TextView scoreOT = (TextView) dialog.findViewById(R.id.scoreOT);


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


            final TextView gameDialogScoreDashName = (TextView) dialog.findViewById(R.id.gameDialogScoreDashName);
            if (g.numOT > 0) {
                gameDialogScoreDashName.setText(g.numOT + "OT");
            } else gameDialogScoreDashName.setText("@");

            final TextView gameL = (TextView) dialog.findViewById(R.id.gameDialogLeft);
            gameL.setText(gameStr[0]);
            final TextView gameC = (TextView) dialog.findViewById(R.id.gameDialogCenter);
            gameC.setText(gameStr[1]);
            final TextView gameR = (TextView) dialog.findViewById(R.id.gameDialogRight);
            gameR.setText(gameStr[2]);
            final TextView gameB = (TextView) dialog.findViewById(R.id.gameDialogBottom);
            gameB.setText(gameStr[3] + "\n\n");
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

            final TextView gameL = (TextView) dialog.findViewById(R.id.gameScoutDialogLeft);
            gameL.setText(gameStr[0]);
            final TextView gameC = (TextView) dialog.findViewById(R.id.gameScoutDialogCenter);
            gameC.setText(gameStr[1]);
            final TextView gameR = (TextView) dialog.findViewById(R.id.gameScoutDialogRight);
            gameR.setText(gameStr[2]);
            final TextView gameB = (TextView) dialog.findViewById(R.id.gameScoutDialogBottom);
            gameB.setText(gameStr[3]);

            // Set up spinners to choose strategy, if the game involves the user team
            if (g.awayTeam == userTeam || g.homeTeam == userTeam) {

                // Set text to show user team's abbr
                TextView textScoutOffenseStrategy = (TextView) dialog.findViewById(R.id.textScoutOffenseStrategy);
                TextView textScoutDefenseStrategy = (TextView) dialog.findViewById(R.id.textScoutDefenseStrategy);
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
                Spinner stratOffSelectionSpinner = (Spinner) dialog.findViewById(R.id.spinnerScoutOffenseStrategy);
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
                Spinner stratDefSelectionSpinner = (Spinner) dialog.findViewById(R.id.spinnerScoutDefenseStrategy);
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
                Spinner stratOffSelectionSpinner = (Spinner) dialog.findViewById(R.id.spinnerScoutOffenseStrategy);
                Spinner stratDefSelectionSpinner = (Spinner) dialog.findViewById(R.id.spinnerScoutDefenseStrategy);
                stratOffSelectionSpinner.setVisibility(View.GONE);
                stratDefSelectionSpinner.setVisibility(View.GONE);

                TextView textScoutOffenseStrategy = (TextView) dialog.findViewById(R.id.textScoutOffenseStrategy);
                TextView textScoutDefenseStrategy = (TextView) dialog.findViewById(R.id.textScoutDefenseStrategy);
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
                        "Opp Pass YPG", "Opp Rush YPG", "TO Differential", "Off Talent", "Def Talent", "Prestige", "Recruiting Class"};
        Spinner teamRankingsSpinner = (Spinner) dialog.findViewById(R.id.spinnerTeamRankings);
        ArrayAdapter<String> teamRankingsSpinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, rankingsSelection);
        teamRankingsSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        teamRankingsSpinner.setAdapter(teamRankingsSpinnerAdapter);

        final ListView teamRankingsList = (ListView) dialog.findViewById(R.id.listViewTeamRankings);
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

        final ListView teamRankingsList = (ListView) dialog.findViewById(R.id.listViewDialog);
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

        String[] historySelection = {"League History", "League Records"};
        Spinner leagueHistorySpinner = (Spinner) dialog.findViewById(R.id.spinnerTeamRankings);
        ArrayAdapter<String> leagueHistorySpinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, historySelection);
        leagueHistorySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        leagueHistorySpinner.setAdapter(leagueHistorySpinnerAdapter);

        final ListView leagueHistoryList = (ListView) dialog.findViewById(R.id.listViewTeamRankings);

        leagueHistorySpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        if (position == 1) {
                            final LeagueRecordsListArrayAdapter leagueRecordsAdapter =
                                    new LeagueRecordsListArrayAdapter(MainActivity.this, simLeague.getLeagueRecordsStr().split("\n"), userTeam.abbr);
                            leagueHistoryList.setAdapter(leagueRecordsAdapter);
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
            Spinner top25hisSpinner = (Spinner) dialog.findViewById(R.id.spinnerBowlCCG);
            final ArrayAdapter<String> top25Adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, selection);
            top25Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            top25hisSpinner.setAdapter(top25Adapter);
        } else {
            String[] selection = new String[simLeague.leagueHistory.size()];
                for (int i = 0; i < simLeague.leagueHistory.size(); ++i) {
                    selection[i] = Integer.toString(seasonStart + i);
                }
            Spinner top25hisSpinner = (Spinner) dialog.findViewById(R.id.spinnerBowlCCG);
            final ArrayAdapter<String> top25Adapter = new ArrayAdapter<String>(this,
                    android.R.layout.simple_spinner_item, selection);
            top25Adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            top25hisSpinner.setAdapter(top25Adapter);

            final TextView top25his = (TextView) dialog.findViewById(R.id.textViewBowlCCGDialog);

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

public void showUserTeamHistoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("My Team History")
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
        Spinner teamHistSpinner = (Spinner) dialog.findViewById(R.id.spinnerTeamRankings);
        final ArrayAdapter<String> teamHistAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, selection);
        teamHistAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        teamHistSpinner.setAdapter(teamHistAdapter);

        final ListView teamHistoryList = (ListView) dialog.findViewById(R.id.listViewTeamRankings);

        final String[] hofPlayers = new String[userTeam.hallOfFame.size()];
        for (int i = 0; i < userTeam.hallOfFame.size(); ++i) {
            hofPlayers[i] = userTeam.hallOfFame.get(i);
        }

        teamHistSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        if (position == 0) {
                            TeamHistoryListArrayAdapter teamHistoryAdapter =
                                    new TeamHistoryListArrayAdapter(MainActivity.this, userTeam.getTeamHistoryList());
                            teamHistoryList.setAdapter(teamHistoryAdapter);
                        } else if (position == 1) {
                            LeagueRecordsListArrayAdapter leagueRecordsAdapter =
                                    new LeagueRecordsListArrayAdapter(MainActivity.this, simLeague.userTeamRecords.getRecordsStr().split("\n"), "---");
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

    public void showCurrTeamHistoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Team History")
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
        Spinner teamHistSpinner = (Spinner) dialog.findViewById(R.id.spinnerTeamRankings);
        final ArrayAdapter<String> teamHistAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, selection);
        teamHistAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        teamHistSpinner.setAdapter(teamHistAdapter);

        final ListView teamHistoryList = (ListView) dialog.findViewById(R.id.listViewTeamRankings);

        teamHistSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                            TeamHistoryListArrayAdapter teamHistoryAdapter =
                                    new TeamHistoryListArrayAdapter(MainActivity.this, currentTeam.getTeamHistoryList());
                            teamHistoryList.setAdapter(teamHistoryAdapter);
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
        Spinner bowlCCGSpinner = (Spinner) dialog.findViewById(R.id.spinnerBowlCCG);
        ArrayAdapter<String> bowlCCGadapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, selection);
        bowlCCGadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        bowlCCGSpinner.setAdapter(bowlCCGadapter);

        final TextView bowlCCGscores = (TextView) dialog.findViewById(R.id.textViewBowlCCGDialog);

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
            if (i == 13) weekSelection[i] = "Conf Champ Week";
            else if (i == 14) weekSelection[i] = "Bowl Game Week";
            else if (i == 15) weekSelection[i] = "National Champ";
            else weekSelection[i] = "Week " + i;
        }
        Spinner weekSelectionSpinner = (Spinner) dialog.findViewById(R.id.spinnerTeamRankings);
        ArrayAdapter<String> weekSelectionSpinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, weekSelection);
        weekSelectionSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weekSelectionSpinner.setAdapter(weekSelectionSpinnerAdapter);
        weekSelectionSpinner.setSelection(simLeague.currentWeek);

        final ListView newsStoriesList = (ListView) dialog.findViewById(R.id.listViewTeamRankings);
        final NewsStoriesListArrayAdapter newsStoriesAdapter = new NewsStoriesListArrayAdapter(this, rankings);
        newsStoriesList.setAdapter(newsStoriesAdapter);

        weekSelectionSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        ArrayList<String> rankings = simLeague.newsStories.get(position);
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

        final TextView offStratDescription = (TextView) dialog.findViewById(R.id.textOffenseStrategy);
        final TextView defStratDescription = (TextView) dialog.findViewById(R.id.textDefenseStrategy);

        // Offense Strategy Spinner
        Spinner stratOffSelectionSpinner = (Spinner) dialog.findViewById(R.id.spinnerOffenseStrategy);
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
        Spinner stratDefSelectionSpinner = (Spinner) dialog.findViewById(R.id.spinnerDefenseStrategy);
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

        final EditText changeNameEditText = (EditText) dialog.findViewById(R.id.editTextChangeName);
        changeNameEditText.setText(currentTeam.name);  //updated from userTeam to currentTeam
        final EditText changeAbbrEditText = (EditText) dialog.findViewById(R.id.editTextChangeAbbr);
        changeAbbrEditText.setText(currentTeam.abbr);   //updated from userTeam to currentTeam
        final EditText changeConfEditText = (EditText) dialog.findViewById(R.id.editTextChangeConf);
        changeConfEditText.setText(currentConference.confName);   //updated from userTeam to currentTeam

        final TextView invalidNameText = (TextView) dialog.findViewById(R.id.textViewChangeName);
        final TextView invalidAbbrText = (TextView) dialog.findViewById(R.id.textViewChangeAbbr);
        final TextView invalidConfText = (TextView) dialog.findViewById(R.id.textViewChangeConf);

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
                    invalidNameText.setText("Name already in use or has illegal characters!");
                } else {
                    invalidNameText.setText("");
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                newConf = s.toString().trim();
                if (!simLeague.isNameValid(newConf)) {
                    invalidNameText.setText("Name already in use or has illegal characters!");
                } else {
                    invalidNameText.setText("");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                newConf = s.toString().trim();
                if (!simLeague.isNameValid(newConf)) {
                    invalidNameText.setText("Name already in use or has illegal characters!");
                } else {
                    invalidNameText.setText("");
                }
            }
        });

        final CheckBox checkboxShowPopup = (CheckBox) dialog.findViewById(R.id.checkboxShowPopups);
        checkboxShowPopup.setChecked(showToasts);

        final CheckBox checkboxShowInjury = (CheckBox) dialog.findViewById(R.id.checkboxShowInjuryReport);
        checkboxShowInjury.setChecked(showInjuryReport);

        Button cancelChangeNameButton = (Button) dialog.findViewById(R.id.buttonCancelChangeName);
        Button okChangeNameButton = (Button) dialog.findViewById(R.id.buttonOkChangeName);

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

                if (simLeague.isNameValid(newName) && simLeague.isAbbrValid(newAbbr) && simLeague.isNameValid(newConf)) {
                    simLeague.changeAbbrHistoryRecords(currentTeam.abbr, newAbbr);
                    currentTeam.name = newName; //set new team name
                    currentTeam.abbr = newAbbr; //set new conference name
                    oldConf = currentConference.confName;
                    currentConference.confName = newConf;
                    simLeague.updateTeamConf(newConf, oldConf, currentConferenceID);  //update all other conf teams
                    getSupportActionBar().setTitle( season + " | " + userTeam.name);
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

            ListView injuryList = (ListView) dialog.findViewById(R.id.listViewInjuryReport);
            injuryList.setAdapter(new PlayerStatsListArrayAdapter(this, injuries));

            CheckBox showInjuryReportCheckBox = (CheckBox) dialog.findViewById(R.id.checkBoxInjuryReport);
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

        final String[] positionSelection = {"QB (1 starter)", "RB (2 starters)", "WR (3 starters)","TE (1 starter)", "OL (5 starters)",
                "K (1 starter)", "DL (4 starters)", "LB (3 starters)", "CB (3 starters)", "S (1 starter)"};
        final int[] positionNumberRequired = {1, 2, 3, 1, 5, 1, 4, 3, 3, 1};
        final Spinner teamLineupPositionSpinner = (Spinner) dialog.findViewById(R.id.spinnerTeamLineupPosition);
        ArrayAdapter<String> teamLineupPositionSpinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, positionSelection);
        teamLineupPositionSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        teamLineupPositionSpinner.setAdapter(teamLineupPositionSpinnerAdapter);

        // Text to show what each attr is
        final TextView textLineupPositionDescription = (TextView) dialog.findViewById(R.id.textViewLineupPositionDescription);

        // List of team's players for selected position
        final ArrayList<Player> positionPlayers = new ArrayList<>();
        positionPlayers.addAll(userTeam.teamQBs);

        final ListView teamPositionList = (ListView) dialog.findViewById(R.id.listViewTeamLineup);
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

        Button saveLineupsButton = (Button) dialog.findViewById(R.id.buttonSaveLineups);
        Button doneWithLineupsButton = (Button) dialog.findViewById(R.id.buttonDoneWithLineups);

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
                textLineupPositionDescription.setText("Name [Yr] Ovr/Pot (Str, Acc, Eva)");
                positionPlayers.addAll(userTeam.teamQBs);
                break;
            case 1:
                textLineupPositionDescription.setText("Name [Yr] Ovr/Pot (Pow, Spd, Eva)");
                positionPlayers.addAll(userTeam.teamRBs);
                break;
            case 2:
                textLineupPositionDescription.setText("Name [Yr] Ovr/Pot (Cat, Spd, Eva)");
                positionPlayers.addAll(userTeam.teamWRs);
                break;
            case 3:
                textLineupPositionDescription.setText("Name [Yr] Ovr/Pot (Cat, RunBlk, Eva)");
                positionPlayers.addAll(userTeam.teamTEs);
                break;
            case 4:
                textLineupPositionDescription.setText("Name [Yr] Ovr/Pot (Str, RunBlk, PassBlk)");
                positionPlayers.addAll(userTeam.teamOLs);
                break;
            case 5:
                textLineupPositionDescription.setText("Name [Yr] Ovr/Pot (KStr, KAcc, Clum)");
                positionPlayers.addAll(userTeam.teamKs);
                break;
            case 6:
                textLineupPositionDescription.setText("Name [Yr] Ovr/Pot (Str, RunDef, PassDef)");
                positionPlayers.addAll(userTeam.teamDLs);
                break;
            case 7:
                textLineupPositionDescription.setText("Name [Yr] Ovr/Pot (Cov, RunDef, Tack)");
                positionPlayers.addAll(userTeam.teamLBs);
                break;
            case 8:
                textLineupPositionDescription.setText("Name [Yr] Ovr/Pot (Cov, Spd, Tack)");
                positionPlayers.addAll(userTeam.teamCBs);
                break;
            case 9:
                textLineupPositionDescription.setText("Name [Yr] Ovr/Pot (Cov, Spd, Tack)");
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
        if (p == null) p = currentTeam.getQB(0);
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
        simLeague.getPlayersLeaving();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(userTeam.abbr + " Players Leaving")
                .setPositiveButton("Begin Recruiting", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        simLeague.updateTeamHistories();
                        simLeague.updateLeagueHistory();
                        userTeam.resetStats();
                        simLeague.advanceSeason();
                        saveLeagueFile = new File(getFilesDir(), "saveLeagueRecruiting.cfb");
                        simLeague.saveLeague(saveLeagueFile);

                        //Get String of user team's players and such
                        StringBuilder sb = new StringBuilder();
                        userTeam.sortPlayers();
                        sb.append(userTeam.conference + "," + userTeam.name + "," + userTeam.abbr + "," + userTeam.teamPrestige + "%\n");
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
        Spinner beginRecruitingSpinner = (Spinner) dialog.findViewById(R.id.spinnerTeamRankings);
        ArrayAdapter<String> beginRecruitingSpinnerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, spinnerSelection);
        beginRecruitingSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        beginRecruitingSpinner.setAdapter(beginRecruitingSpinnerAdapter);

        final ListView playerList = (ListView) dialog.findViewById(R.id.listViewTeamRankings);
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
                    TextView textView = (TextView) dialog2.findViewById(android.R.id.message);
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

    
    public void updateTeamUI() {
        getSupportActionBar().setTitle(season + " | " + userTeam.name);
    }
}

