package recruiting;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import antdroid.cfbcoach.MainActivity;
import antdroid.cfbcoach.R;

public class RecruitingActivity extends AppCompatActivity {

    // Variables use during recruiting
    private String teamName;
    public int recruitingBudget;
    private final Random rand = new Random();
    private int HCtalent;

    public ArrayList<String> playersRecruited;
    private ArrayList<String> playersGraduating;
    private ArrayList<String> teamQBs;
    private ArrayList<String> teamRBs;
    private ArrayList<String> teamWRs;
    private ArrayList<String> teamTEs;
    private ArrayList<String> teamOLs;
    private ArrayList<String> teamKs;
    private ArrayList<String> teamDLs;
    private ArrayList<String> teamLBs;
    private ArrayList<String> teamCBs;
    private ArrayList<String> teamSs;

    public ArrayList<String> teamPlayers; //all players

    private ArrayList<String> availQBs;
    private ArrayList<String> availRBs;
    private ArrayList<String> availWRs;
    private ArrayList<String> availTEs;
    private ArrayList<String> availOLs;
    private ArrayList<String> availKs;
    private ArrayList<String> availDLs;
    private ArrayList<String> availLBs;
    private ArrayList<String> availCBs;
    private ArrayList<String> availSs;
    private ArrayList<String> availAll;
    private ArrayList<String> avail50;

    private ArrayList<String> removeList;

    private ArrayList<String> west;
    private ArrayList<String> midwest;
    private ArrayList<String> central;
    private ArrayList<String> east;
    private ArrayList<String> south;

    private int needQBs;
    private int needRBs;
    private int needWRs;
    private int needTEs;
    private int needOLs;
    private int needKs;
    private int needDLs;
    private int needLBs;
    private int needCBs;
    private int needSs;

    private final int minPlayers = 50;
    private final int minQBs = 3;
    private final int minRBs = 5;
    private final int minWRs = 8;
    private final int minTEs = 3;
    private final int minOLs = 11;
    private final int minKs = 2;
    private final int minDLs = 10;
    private final int minLBs = 7;
    private final int minCBs = 7;
    private final int minSs = 5;

    public final int maxPlayers = 70;
    private final double recruitOffBoard = 0.935;

    private final int five = 84;
    private final int four = 78;
    private final int three = 68;
    private final int two = 58;

    int height;
    int weight;

    // Whether to show pop ups every recruit
    private boolean showPopUp;
    private boolean autoFilter;

    // Keep track of which position is selected in spinner
    private String currentPosition;

    // Android Components to keep track of
    private TextView budgetText;
    private Spinner positionSpinner;
    private ExpandableListView recruitList;
    private ArrayList<String> positions;
    private ArrayAdapter dataAdapterPosition;
    private ExpandableListAdapterRecruiting expListAdapter;
    public Map<String, List<String>> playersInfo;
    public List<String> players;

    public final String[] states = {"AS","AZ","CA","HI","ID","MT","NV","OR","UT","WA","CO","KS","MO","NE","NM","ND","OK","SD","TX","WY","IL","IN","IA","KY","MD","MI","MN","OH","TN","WI","CT","DE","ME","MA","NH","NJ","NY","PA","RI","VT","AL","AK","FL","GA","LA","MS","NC","SC","VA","WV"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruiting);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Init all the ArrayLists
        playersRecruited = new ArrayList<>();
        playersGraduating = new ArrayList<>();
        teamQBs = new ArrayList<>();
        teamRBs = new ArrayList<>();
        teamWRs = new ArrayList<>();
        teamTEs = new ArrayList<>();
        teamOLs = new ArrayList<>();
        teamKs = new ArrayList<>();
        teamDLs = new ArrayList<>();
        teamLBs = new ArrayList<>();
        teamCBs = new ArrayList<>();
        teamSs = new ArrayList<>();
        teamPlayers = new ArrayList<>();
        availQBs = new ArrayList<>();
        availRBs = new ArrayList<>();
        availWRs = new ArrayList<>();
        availTEs = new ArrayList<>();
        availOLs = new ArrayList<>();
        availKs = new ArrayList<>();
        availDLs = new ArrayList<>();
        availLBs = new ArrayList<>();
        availCBs = new ArrayList<>();
        availSs = new ArrayList<>();

        avail50 = new ArrayList<>();
        availAll = new ArrayList<>();
        west = new ArrayList<>();
        midwest = new ArrayList<>();
        central = new ArrayList<>();
        east = new ArrayList<>();
        south = new ArrayList<>();

        // Get User Team's player info and team info for recruiting
        Bundle extras = getIntent().getExtras();
        String userTeamStr = "";
        if (extras != null) {
            userTeamStr = extras.getString("USER_TEAM_INFO");
        }

        // Parse through string
        String[] lines = userTeamStr.split("%\n");
        final String[] teamInfo = lines[0].split(",");
        teamName = teamInfo[1];
        recruitingBudget = Integer.parseInt(teamInfo[3]) * 15;
        if (teamInfo[4].isEmpty()) {
            HCtalent = 70;
        } else {
            HCtalent = Integer.parseInt(teamInfo[4]);
        }
        getSupportActionBar().setTitle(teamName + " | Recruiting");

        showPopUp = true;
        autoFilter = true;

        // First get user team's roster info
        String[] playerInfo;
        int i = 1;
        while (!lines[i].equals("END_TEAM_INFO")) {
            playerInfo = lines[i].split(",");
            if (playerInfo[2].equals("5")) {
                // Graduating player
                playersGraduating.add(getReadablePlayerInfo(lines[i]));
            } else {
                teamPlayers.add(getReadablePlayerInfo(lines[i]));
                if (playerInfo[0].equals("QB")) {
                    teamQBs.add(getReadablePlayerInfo(lines[i]));
                } else if (playerInfo[0].equals("RB")) {
                    teamRBs.add(getReadablePlayerInfo(lines[i]));
                } else if (playerInfo[0].equals("WR")) {
                    teamWRs.add(getReadablePlayerInfo(lines[i]));
                } else if (playerInfo[0].equals("TE")) {
                    teamTEs.add(getReadablePlayerInfo(lines[i]));
                } else if (playerInfo[0].equals("OL")) {
                    teamOLs.add(getReadablePlayerInfo(lines[i]));
                } else if (playerInfo[0].equals("K")) {
                    teamKs.add(getReadablePlayerInfo(lines[i]));
                } else if (playerInfo[0].equals("DL")) {
                    teamDLs.add(getReadablePlayerInfo(lines[i]));
                } else if (playerInfo[0].equals("LB")) {
                    teamLBs.add(getReadablePlayerInfo(lines[i]));
                } else if (playerInfo[0].equals("CB")) {
                    teamCBs.add(getReadablePlayerInfo(lines[i]));
                } else if (playerInfo[0].equals("S")) {
                    teamSs.add(getReadablePlayerInfo(lines[i]));
                }
            }
            ++i; // go to next line
        }

        sortTeam();


        // Add extra money if your team was fleeced
        int recBonus = (minPlayers - teamPlayers.size()) * 25;
        int coachBonus = HCtalent * 3;
        recruitingBudget += recBonus + coachBonus;

        // Next get recruits info
        ++i;
        while (i < lines.length) {
            playerInfo = lines[i].split(",");
            availAll.add(lines[i]);
            int region = Integer.parseInt(playerInfo[3])/10;
            if (region == 0) {
                west.add(lines[i]);
            }
            if (region == 1) {
                midwest.add(lines[i]);
            }
            if (region == 2) {
                central.add(lines[i]);
            }
            if (region == 3) {
                east.add(lines[i]);
            }
            if (region == 4) {
                south.add(lines[i]);
            }

            if (playerInfo[0].equals("QB")) {
                availQBs.add(lines[i]);
            } else if (playerInfo[0].equals("RB")) {
                availRBs.add(lines[i]);
            } else if (playerInfo[0].equals("WR")) {
                availWRs.add(lines[i]);
            } else if (playerInfo[0].equals("TE")) {
                availTEs.add(lines[i]);
            } else if (playerInfo[0].equals("K")) {
                availKs.add(lines[i]);
            } else if (playerInfo[0].equals("OL")) {
                availOLs.add(lines[i]);
            } else if (playerInfo[0].equals("DL")) {
                availDLs.add(lines[i]);
            } else if (playerInfo[0].equals("LB")) {
                availLBs.add(lines[i]);
            } else if (playerInfo[0].equals("CB")) {
                availCBs.add(lines[i]);
            } else if (playerInfo[0].equals("S")) {
                availSs.add(lines[i]);
            }
            ++i;
        }


        // Sort to get top 100 overall players
        Collections.sort(availAll, new CompRecruitScoutGrade());
        Collections.sort(west, new CompRecruitScoutGrade());
        Collections.sort(midwest, new CompRecruitScoutGrade());
        Collections.sort(central, new CompRecruitScoutGrade());
        Collections.sort(east, new CompRecruitScoutGrade());
        Collections.sort(south, new CompRecruitScoutGrade());

        avail50 = new ArrayList<>(availAll.subList(0, 49));

        // Get needs for each position
        updatePositionNeeds();

        /*
          Assign components to private variables for easier access later
         */
        budgetText = findViewById(R.id.textRecBudget);
        String budgetStr = "Budget: $" + recruitingBudget;
        budgetText.setText(budgetStr);

        /*
          Set up spinner for examining choosing position to recruit
         */
        positionSpinner = findViewById(R.id.spinnerRec);
        positions = new ArrayList<>();
        positions.add("Top 50 Recruits");
        positions.add("All Players");
        positions.add("QB (Need: " + needQBs + ")");
        positions.add("RB (Need: " + needRBs + ")");
        positions.add("WR (Need: " + needWRs + ")");
        positions.add("TE (Need: " + needTEs + ")");
        positions.add("OL (Need: " + needOLs + ")");
        positions.add("K (Need: " + needKs + ")");
        positions.add("DL (Need: " + needDLs + ")");
        positions.add("LB (Need: " + needLBs + ")");
        positions.add("CB (Need: " + needCBs + ")");
        positions.add("S (Need: " + needSs + ")");
        positions.add("West (" + west.size() + ")");
        positions.add("Midwest (" + midwest.size() + ")");
        positions.add("Central (" + central.size() + ")");
        positions.add("East (" + east.size() + ")");
        positions.add("South (" + east.size() + ")");

        dataAdapterPosition = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, positions);
        dataAdapterPosition.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        positionSpinner.setAdapter(dataAdapterPosition);
        positionSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        currentPosition = parent.getItemAtPosition(position).toString();
                        updateForNewPosition(position);
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

        /*
          Set up the "Done" button for returning back to MainActivity
         */
        Button doneRecrutingButton = findViewById(R.id.buttonDoneRecruiting);
        doneRecrutingButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                exitRecruiting();
            }
        });

        final Switch filterSwitch = findViewById(R.id.filterSwitch);
        filterSwitch.setText("Filter Unaffordable");
        filterSwitch.setChecked(autoFilter);
        filterSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                autoFilter = isChecked;
            }
        });

        /*
          Set up the "Roster" button for displaying dialog of all players in roster
         */
        Button viewRosterButton = findViewById(R.id.buttonRecRoster);
        viewRosterButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Make dialog
                makeRosterDialog();
            }
        });

        /*
          Set up expandable list view
         */
        recruitList = findViewById(R.id.recruitExpandList);
        setPlayerList("QB");
        setPlayerInfoMap("QB");
        expListAdapter = new ExpandableListAdapterRecruiting(this);
        recruitList.setAdapter(expListAdapter);

        /*
          Set up "Expand All / Collapse All" button
         */
        final Button buttonExpandAll = findViewById(R.id.buttonRecruitExpandCollapse);
        buttonExpandAll.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RecruitingActivity.this);
                builder.setTitle("DISPLAY OPTIONS");
                String filter = "Enable Auto-Remove Unaffordable Players";
                if (autoFilter) filter = "Disable Auto-Remove Unaffordable Players";
                final String[] sels = {"Expand All", "Collapse All", "Sort by Grade", "Sort by Cost", filter};
                builder.setItems(sels, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // Do something with the selection
                        if (item == 0) {
                            // Expand everyone
                            for (int i = 0; i < players.size(); ++i) {
                                recruitList.expandGroup(i, false);
                            }
                        } else if (item == 1) {
                            // Collapse everyone
                            for (int i = 0; i < players.size(); ++i) {
                                recruitList.collapseGroup(i);
                            }
                        } else if (item == 2) {
                            sortByGrade();
                            expListAdapter.notifyDataSetChanged();
                        } else if (item == 3) {
                            sortByCost();
                            expListAdapter.notifyDataSetChanged();
                        } else if (item == 4) {
                            autoFilter = !autoFilter;
                            filterSwitch.setChecked(autoFilter);
                        }

                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

    }

    //Create Roster Screen
    private void makeRosterDialog() {
        String rosterStr = getRosterStr();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(rosterStr)
                .setTitle(teamName + " Roster | Team Size: " + (teamPlayers.size() + playersRecruited.size()))
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Dismiss dialog
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
        TextView msgTxt = dialog.findViewById(android.R.id.message);
        msgTxt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
    }

    private String getRosterStr() {
        updatePositionNeeds();
        StringBuilder sb = new StringBuilder();
        String stbn = ""; //ST for starter, BN for bench
        String p = ""; //player string

        sb.append("QBs (Need: " + needQBs + ")\n");
        appendPlayers(sb, teamQBs, 1);

        sb.append("\nRBs (Need: " + needRBs + ")\n");
        appendPlayers(sb, teamRBs, 2);

        sb.append("\nWRs (Need: " + needWRs + ")\n");
        appendPlayers(sb, teamWRs, 3);

        sb.append("\nTEs (Need: " + needTEs + ")\n");
        appendPlayers(sb, teamTEs, 1);

        sb.append("\nOLs (Need: " + needOLs + ")\n");
        appendPlayers(sb, teamOLs, 5);

        sb.append("\nKs (Need: " + needKs + ")\n");
        appendPlayers(sb, teamKs, 1);

        sb.append("\nDLs (Need: " + needDLs + ")\n");
        appendPlayers(sb, teamDLs, 4);

        sb.append("\nLBs (Need: " + needLBs + ")\n");
        appendPlayers(sb, teamLBs, 3);

        sb.append("\nCBs (Need: " + needCBs + ")\n");
        appendPlayers(sb, teamCBs, 3);

        sb.append("\nSs (Need: " + needSs + ")\n");
        appendPlayers(sb, teamSs, 2);

        return sb.toString();
    }

    private void appendPlayers(StringBuilder sb, ArrayList<String> players, int numStart) {
        String p, stbn;
        for (int i = 0; i < players.size(); ++i) {
            if (i > numStart - 1) stbn = "BN";
            else stbn = "ST";
            p = players.get(i);
            sb.append("\t" + stbn + " " + p + "\n");
        }
    }

    private String getReadablePlayerInfoPos(String p) {
        String[] pi = p.split(",");
        return pi[0] + " " + pi[1] + " " + getYrStr(pi[2]) + " " + pi[11] + " Ovr";
    }


    //FILTERS & SORTINGS


    //UPDATE DATA AFTER CHOOSING FILTER
    private void updateForNewPosition(int position) {
        if (position > 1 && position < 12) {
            String[] splitty = currentPosition.split(" ");
            setPlayerList(splitty[0]);
            setPlayerInfoMap(splitty[0]);
            expListAdapter.notifyDataSetChanged();
        } else {
            // See top 100 recruits
            if (position == 0) {
                players = avail50;
            } else if (position == 12) {
                players = west;
            } else if (position == 13) {
                players = midwest;
            } else if (position == 14) {
                players = central;
            } else if (position == 15) {
                players = east;
            } else if (position == 16) {
                players = south;
            } else {
                players = availAll;
            }

            playersInfo = new LinkedHashMap<>();
            for (String p : players) {
                ArrayList<String> pInfoList = new ArrayList<>();
                pInfoList.add(getPlayerDetails(p, p.split(",")[0]));
                playersInfo.put(p.substring(0, p.length() - 2), pInfoList);
            }
            expListAdapter.notifyDataSetChanged();
        }
    }

    //Get Players
    private void setPlayerList(String pos) {
        if (pos.equals("QB")) {
            players = availQBs;
        } else if (pos.equals("RB")) {
            players = availRBs;
        } else if (pos.equals("WR")) {
            players = availWRs;
        } else if (pos.equals("TE")) {
            players = availTEs;
        } else if (pos.equals("OL")) {
            players = availOLs;
        } else if (pos.equals("K")) {
            players = availKs;
        } else if (pos.equals("DL")) {
            players = availDLs;
        } else if (pos.equals("LB")) {
            players = availLBs;
        } else if (pos.equals("CB")) {
            players = availCBs;
        } else if (pos.equals("S")) {
            players = availSs;
        }
    }

    //Player General Display
    private void setPlayerInfoMap(String pos) {
        playersInfo = new LinkedHashMap<>();
        for (String p : players) {
            ArrayList<String> pInfoList = new ArrayList<>();
            pInfoList.add(getPlayerDetails(p, pos));
            playersInfo.put(p.substring(0, p.length() - 2), pInfoList);
        }
    }

    //Player Attributes (Profile)
    private String getPlayerDetails(String player, String pos) {
        String[] ps = player.split(",");
        if (pos.equals("QB")) {
            return "Home State: " + getRegion(Integer.parseInt(ps[3])) +
                    "\nPass Strength: " + getGrade(ps[13]) +
                    "\nPass Accuracy: " + getGrade(ps[14]) +
                    "\nEvasion: " + getGrade(ps[15]) +
                    "\nSpeed: " + getGrade(ps[16]);
        } else if (pos.equals("RB")) {
            return "Home State: " + getRegion(Integer.parseInt(ps[3])) +
                    "\nPower: " + getGrade(ps[13]) +
                    "\nSpeed: " + getGrade(ps[14]) +
                    "\nEvasion: " + getGrade(ps[15]) +
                    "\nCatching: " + getGrade(ps[16]);
        } else if (pos.equals("WR")) {
            return "Home State: " + getRegion(Integer.parseInt(ps[3])) +
                    "\nCatching: " + getGrade(ps[13]) +
                    "\nSpeed: " + getGrade(ps[14]) +
                    "\nEvasion: " + getGrade(ps[15]) +
                    "\nJumping: " + getGrade(ps[16]);
        } else if (pos.equals("TE")) {
            return "Home State: " + getRegion(Integer.parseInt(ps[3])) +
                    "\nCatching: " + getGrade(ps[13]) +
                    "\nRush Blk: " + getGrade(ps[14]) +
                    "\nEvasion: " + getGrade(ps[15]) +
                    "\nSpeed: " + getGrade(ps[16]);
        } else if (pos.equals("OL")) {
            return "Home State: " + getRegion(Integer.parseInt(ps[3])) +
                    "\nStrength: " + getGrade(ps[13]) +
                    "\nRush Blk: " + getGrade(ps[14]) +
                    "\nPass Blk: " + getGrade(ps[15]) +
                    "\nAwareness: " + getGrade(ps[16]);
        } else if (pos.equals("K")) {
            return "Home State: " + getRegion(Integer.parseInt(ps[3])) +
                    "\nKick Power: " + getGrade(ps[13]) +
                    "\nAccuracy: " + getGrade(ps[14]) +
                    "\nClumsiness: " + getGrade(ps[15]) +
                    "\nPressure: " + getGrade(ps[16]);
        } else if (pos.equals("DL")) {
            return "Home State: " + getRegion(Integer.parseInt(ps[3])) +
                    "\nStrength: " + getGrade(ps[13]) +
                    "\nRun Stop: " + getGrade(ps[14]) +
                    "\nPass Press: " + getGrade(ps[15]) +
                    "\nTackling: " + getGrade(ps[16]);
        } else if (pos.equals("LB")) {
            return "Home State: " + getRegion(Integer.parseInt(ps[3])) +
                    "\nCoverage: " + getGrade(ps[13]) +
                    "\nRun Stop: " + getGrade(ps[14]) +
                    "\nTackling: " + getGrade(ps[15]) +
                    "\nSpeed: " + getGrade(ps[16]);
        } else if (pos.equals("CB")) {
            return "Home State: " + getRegion(Integer.parseInt(ps[3])) +

                    "\nCoverage: " + getGrade(ps[13]) +
                    "\nSpeed: " + getGrade(ps[14]) +
                    "\nTackling: " + getGrade(ps[15]) +
                    "\nJumping: " + getGrade(ps[16]);
        } else if (pos.equals("S")) {
            return "Home State: " + getRegion(Integer.parseInt(ps[3])) +

                    "\nCoverage: " + getGrade(ps[13]) +
                    "\nSpeed: " + getGrade(ps[14]) +
                    "\nTackling: " + getGrade(ps[15]) +
                    "\nRun Stop: " + getGrade(ps[16]);
        }
        return "ERROR";
    }

    //FILTER OUT UNAFFORDABLE PLAYERS
    private void removeUnaffordableRecruits() {
        removeUnaffordable(players);
        removeUnaffordable(avail50);
        removeUnaffordable(availAll);
        removeUnaffordable(availQBs);
        removeUnaffordable(availRBs);
        removeUnaffordable(availWRs);
        removeUnaffordable(availTEs);
        removeUnaffordable(availOLs);
        removeUnaffordable(availKs);
        removeUnaffordable(availDLs);
        removeUnaffordable(availLBs);
        removeUnaffordable(availCBs);
        removeUnaffordable(availSs);
        removeUnaffordable(west);
        removeUnaffordable(midwest);
        removeUnaffordable(central);
        removeUnaffordable(east);
        removeUnaffordable(south);

        dataAdapterPosition.notifyDataSetChanged();
    }

    private void removeUnaffordable(List<String> list) {
        int i = 0;
        while (i < list.size()) {
            if (getRecruitCost(list.get(i)) > recruitingBudget) {
                // Can't afford him
                list.remove(i);
            } else {
                ++i;
            }
        }
    }

    //REMOVE RECRUITS OFF THE BOARD
    private void removeRecruits() {
        removeRecruits(players);
        removeRecruitBoard(removeList);
    }

    private void removeRecruits(List<String> list) {
        removeList = new ArrayList<>();
        for (int i = 0; i < list.size(); ++i) {
            if (Math.random() > recruitOffBoard) {
                removeList.add(list.get(i));
                list.remove(i);
            }
        }
    }

    private void removeRecruitBoard(List<String> list) {

        for (int i = 0; i < list.size(); i++) {

            String player = list.get(i);
            // Remove the player from the top 100 list
            avail50.remove(player);
            availAll.remove(player);
            west.remove(player);
            midwest.remove(player);
            central.remove(player);
            east.remove(player);
            south.remove(player);

            String[] ps = player.split(",");
            if (ps[0].equals("QB")) {
                availQBs.remove(player);
            } else if (ps[0].equals("RB")) {
                availRBs.remove(player);
            } else if (ps[0].equals("WR")) {
                availWRs.remove(player);
            } else if (ps[0].equals("TE")) {
                availTEs.remove(player);
            } else if (ps[0].equals("OL")) {
                availOLs.remove(player);
            } else if (ps[0].equals("K")) {
                availKs.remove(player);
            } else if (ps[0].equals("DL")) {
                availDLs.remove(player);
            } else if (ps[0].equals("LB")) {
                availLBs.remove(player);
            } else if (ps[0].equals("CB")) {
                availCBs.remove(player);
            } else if (ps[0].equals("S")) {
                availSs.remove(player);
            }
        }

        dataAdapterPosition.notifyDataSetChanged();
    }


    //Update Position Spinner & Team Needs
    private void updatePositionNeeds() {
        // Get needs for each position
        needQBs = minQBs - teamQBs.size();
        needRBs = minRBs - teamRBs.size();
        needWRs = minWRs - teamWRs.size();
        needTEs = minTEs - teamTEs.size();
        needOLs = minOLs - teamOLs.size();
        needKs = minKs - teamKs.size();
        needDLs = minDLs - teamDLs.size();
        needLBs = minLBs - teamLBs.size();
        needCBs = minCBs - teamCBs.size();
        needSs = minSs - teamSs.size();

        if (dataAdapterPosition != null) {
            positions = new ArrayList<>();
            positions.add("Top 50 Recruits");
            positions.add("All Players");
            positions.add("QB (Need: " + needQBs + ")");
            positions.add("RB (Need: " + needRBs + ")");
            positions.add("WR (Need: " + needWRs + ")");
            positions.add("TE (Need: " + needTEs + ")");
            positions.add("OL (Need: " + needOLs + ")");
            positions.add("K (Need: " + needKs + ")");
            positions.add("DL (Need: " + needDLs + ")");
            positions.add("LB (Need: " + needLBs + ")");
            positions.add("CB (Need: " + needCBs + ")");
            positions.add("S (Need: " + needSs + ")");
            positions.add("West (" + west.size() + ")");
            positions.add("Midwest (" + midwest.size() + ")");
            positions.add("Central (" + central.size() + ")");
            positions.add("East (" + east.size() + ")");
            positions.add("South (" + south.size() + ")");

            dataAdapterPosition.clear();
            for (String p : positions) {
                dataAdapterPosition.add(p);
            }
            dataAdapterPosition.notifyDataSetChanged();
        }
    }


    //SORT - GRADE(Default)
    private void sortByGrade() {
        Collections.sort(availAll, new CompRecruitScoutGrade());
        Collections.sort(avail50, new CompRecruitScoutGrade());
        Collections.sort(availQBs, new CompRecruitScoutGrade());
        Collections.sort(availRBs, new CompRecruitScoutGrade());
        Collections.sort(availWRs, new CompRecruitScoutGrade());
        Collections.sort(availTEs, new CompRecruitScoutGrade());
        Collections.sort(availOLs, new CompRecruitScoutGrade());
        Collections.sort(availKs, new CompRecruitScoutGrade());
        Collections.sort(availDLs, new CompRecruitScoutGrade());
        Collections.sort(availLBs, new CompRecruitScoutGrade());
        Collections.sort(availCBs, new CompRecruitScoutGrade());
        Collections.sort(availSs, new CompRecruitScoutGrade());
        Collections.sort(west, new CompRecruitScoutGrade());
        Collections.sort(midwest, new CompRecruitScoutGrade());
        Collections.sort(central, new CompRecruitScoutGrade());
        Collections.sort(east, new CompRecruitScoutGrade());
        Collections.sort(south, new CompRecruitScoutGrade());

    }

    //SORT - COST
    private void sortByCost() {
        Collections.sort(availAll, new CompRecruitCost());
        Collections.sort(avail50, new CompRecruitCost());
        Collections.sort(availQBs, new CompRecruitCost());
        Collections.sort(availRBs, new CompRecruitCost());
        Collections.sort(availWRs, new CompRecruitCost());
        Collections.sort(availTEs, new CompRecruitCost());
        Collections.sort(availOLs, new CompRecruitCost());
        Collections.sort(availKs, new CompRecruitCost());
        Collections.sort(availDLs, new CompRecruitCost());
        Collections.sort(availLBs, new CompRecruitCost());
        Collections.sort(availCBs, new CompRecruitCost());
        Collections.sort(availSs, new CompRecruitCost());
        Collections.sort(west, new CompRecruitCost());
        Collections.sort(midwest, new CompRecruitCost());
        Collections.sort(central, new CompRecruitCost());
        Collections.sort(east, new CompRecruitCost());
        Collections.sort(south, new CompRecruitCost());

    }


    //RECRUIT PLAYER
    public void recruitPlayerDialog(String p, int pos, List<Integer> groupsExp) {
        final String player = p;
        final int groupPosition = pos;
        final List<Integer> groupsExpanded = groupsExp;
        int moneyNeeded = getRecruitCost(player);
        if (recruitingBudget >= moneyNeeded) {

            if (showPopUp) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Confirm Recruiting");
                builder.setMessage("Your team roster is at " + (teamPlayers.size() + playersRecruited.size()) + " (Max: 70).\n\nAre you sure you want to recruit " + getReadablePlayerInfoDisplay(player) + " for $" + moneyNeeded + "?");
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                recruitList.collapseGroup(groupPosition);
                                for (int i = groupPosition + 1; i < players.size(); ++i) {
                                    if (recruitList.isGroupExpanded(i)) {
                                        groupsExpanded.add(i);
                                    }
                                    recruitList.collapseGroup(i);
                                }

                                recruitPlayer(player);

                                expListAdapter.notifyDataSetChanged();

                                dialog.dismiss();
                            }
                        });

                builder.setNeutralButton("Yes, Don't Show",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                recruitList.collapseGroup(groupPosition);
                                for (int i = groupPosition + 1; i < players.size(); ++i) {
                                    if (recruitList.isGroupExpanded(i)) {
                                        groupsExpanded.add(i);
                                    }
                                    recruitList.collapseGroup(i);
                                }

                                recruitPlayer(player);
                                setShowPopUp(false);

                                expListAdapter.notifyDataSetChanged();

                                dialog.dismiss();
                            }
                        });

                builder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // not successful
                                recruitList.collapseGroup(groupPosition);
                                for (int i = groupPosition + 1; i < players.size(); ++i) {
                                    if (recruitList.isGroupExpanded(i)) {
                                        groupsExpanded.add(i);
                                    }
                                    recruitList.collapseGroup(i);
                                }

                                recruitList.expandGroup(groupPosition);
                                expListAdapter.notifyDataSetChanged();
/*                                for (int group : groupsExpanded) {
                                    recruitList.expandGroup(group);
                                }*/
                                dialog.cancel();
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();

                TextView msgTxt = dialog.findViewById(android.R.id.message);
                msgTxt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

            } else {
                // Don't show pop up dialog
                recruitList.collapseGroup(groupPosition);
                for (int i = groupPosition + 1; i < players.size(); ++i) {
                    if (recruitList.isGroupExpanded(i)) {
                        groupsExpanded.add(i);
                    }
                    recruitList.collapseGroup(i);
                }

                recruitPlayer(player);

                expListAdapter.notifyDataSetChanged();
/*                for (int group : groupsExpanded) {
                    recruitList.expandGroup(group - 1);
                }*/
            }

        } else {
            recruitList.collapseGroup(groupPosition);
            for (int i = groupPosition + 1; i < players.size(); ++i) {
                if (recruitList.isGroupExpanded(i)) {
                    groupsExpanded.add(i);
                }
                recruitList.collapseGroup(i);
            }
            Toast.makeText(this, "Not enough money!",
                    Toast.LENGTH_SHORT).show();
            recruitList.expandGroup(groupPosition);
            expListAdapter.notifyDataSetChanged();
/*            for (int group : groupsExpanded) {
                recruitList.expandGroup(group);
            }*/
        }
    }

    private void recruitPlayer(String player) {
        int moneyNeeded = getRecruitCost(player);
        recruitingBudget -= moneyNeeded;
        budgetText.setText("Budget: $" + recruitingBudget);

        // Remove the player from the top 100 list
        avail50.remove(player);
        availAll.remove(player);
        west.remove(player);
        midwest.remove(player);
        central.remove(player);
        east.remove(player);
        south.remove(player);
        playersRecruited.add(player);

        // Also need to add recruited player to correct team list and remove from avail list
        String[] ps = player.split(",");
        if (ps[0].equals("QB")) {
            availQBs.remove(player);
            teamQBs.add(getReadablePlayerInfo(player));
        } else if (ps[0].equals("RB")) {
            availRBs.remove(player);
            teamRBs.add(getReadablePlayerInfo(player));
        } else if (ps[0].equals("WR")) {
            availWRs.remove(player);
            teamWRs.add(getReadablePlayerInfo(player));
        } else if (ps[0].equals("TE")) {
            availTEs.remove(player);
            teamTEs.add(getReadablePlayerInfo(player));
        } else if (ps[0].equals("OL")) {
            availOLs.remove(player);
            teamOLs.add(getReadablePlayerInfo(player));
        } else if (ps[0].equals("K")) {
            availKs.remove(player);
            teamKs.add(getReadablePlayerInfo(player));
        } else if (ps[0].equals("DL")) {
            availDLs.remove(player);
            teamDLs.add(getReadablePlayerInfo(player));
        } else if (ps[0].equals("LB")) {
            availLBs.remove(player);
            teamLBs.add(getReadablePlayerInfo(player));
        } else if (ps[0].equals("CB")) {
            availCBs.remove(player);
            teamCBs.add(getReadablePlayerInfo(player));
        } else if (ps[0].equals("S")) {
            availSs.remove(player);
            teamSs.add(getReadablePlayerInfo(player));
        }
        sortTeam();

        players.remove(player);

        Toast.makeText(this, "Recruited " + ps[0] + " " + ps[1],
                Toast.LENGTH_SHORT).show();

        if (autoFilter) removeUnaffordableRecruits();
        removeRecruits();
        updatePositionNeeds();
    }


    //PLAYER INFO FOR RECRUIT/REDSHIRTING DIALOG
    private String getReadablePlayerInfoDisplay(String p) {
        String[] pi = p.split(",");
        return pi[6] + "-Star " + pi[0] + " " + pi[1];
    }

    //SCOUT PLAYER - created by Achi Jones - never used
    private boolean scoutPlayer(String player) {
        int scoutCost = getRecruitCost(player) / 10;
        if (scoutCost < 10) scoutCost = 10;

        if (recruitingBudget >= scoutCost) {
            recruitingBudget -= scoutCost;
            budgetText.setText("Budget: $" + recruitingBudget);

            // Check availAll first
            if (availAll.contains(player)) {
                int posTop = availAll.indexOf(player);
                availAll.set(posTop, player.substring(0, player.length() - 1) + "1");
            }
            if (avail50.contains(player)) {
                int posTop = avail50.indexOf(player);
                avail50.set(posTop, player.substring(0, player.length() - 1) + "1");
            }


            // Next check all the position lists
            String[] ps = player.split(",");
            if (ps[0].equals("QB") && availQBs.contains(player)) {
                availQBs.set(availQBs.indexOf(player), player.substring(0, player.length() - 1) + "1");
            } else if (ps[0].equals("RB") && availRBs.contains(player)) {
                availRBs.set(availRBs.indexOf(player), player.substring(0, player.length() - 1) + "1");
            } else if (ps[0].equals("WR") && availWRs.contains(player)) {
                availWRs.set(availWRs.indexOf(player), player.substring(0, player.length() - 1) + "1");
            } else if (ps[0].equals("TE") && availTEs.contains(player)) {
                availTEs.set(availTEs.indexOf(player), player.substring(0, player.length() - 1) + "1");
            } else if (ps[0].equals("OL") && availOLs.contains(player)) {
                availOLs.set(availOLs.indexOf(player), player.substring(0, player.length() - 1) + "1");
            } else if (ps[0].equals("K") && availKs.contains(player)) {
                availKs.set(availKs.indexOf(player), player.substring(0, player.length() - 1) + "1");
            } else if (ps[0].equals("DL") && availDLs.contains(player)) {
                availDLs.set(availDLs.indexOf(player), player.substring(0, player.length() - 1) + "1");
            } else if (ps[0].equals("LB") && availLBs.contains(player)) {
                availLBs.set(availLBs.indexOf(player), player.substring(0, player.length() - 1) + "1");
            } else if (ps[0].equals("CB") && availCBs.contains(player)) {
                availCBs.set(availCBs.indexOf(player), player.substring(0, player.length() - 1) + "1");
            } else if (ps[0].equals("S") && availSs.contains(player)) {
                availSs.set(availSs.indexOf(player), player.substring(0, player.length() - 1) + "1");
            }

            Toast.makeText(this, "Scouted " + ps[0] + " " + ps[1], Toast.LENGTH_SHORT).show();

            expListAdapter.notifyDataSetChanged();

            return true;

        } else {
            Toast.makeText(this, "Not enough money!",
                    Toast.LENGTH_SHORT).show();

            return false;
        }
    }

    private void sortTeam() {
        Collections.sort(teamQBs, new CompRecruitTeamRosterOvr());
        Collections.sort(teamRBs, new CompRecruitTeamRosterOvr());
        Collections.sort(teamWRs, new CompRecruitTeamRosterOvr());
        Collections.sort(teamTEs, new CompRecruitTeamRosterOvr());
        Collections.sort(teamOLs, new CompRecruitTeamRosterOvr());
        Collections.sort(teamKs, new CompRecruitTeamRosterOvr());
        Collections.sort(teamDLs, new CompRecruitTeamRosterOvr());
        Collections.sort(teamLBs, new CompRecruitTeamRosterOvr());
        Collections.sort(teamCBs, new CompRecruitTeamRosterOvr());
        Collections.sort(teamSs, new CompRecruitTeamRosterOvr());
    }

    //PLAYER DISPLAY INFO
    private String getReadablePlayerInfo(String p) {
        String[] pi = p.split(",");
        String improveStr = "";
        String transfer = "";
        if (pi[7].equals("true")) transfer = " (Transfer)";
        if (!playersRecruited.contains(p)) {
            improveStr = "(+" + pi[13] + ")";
            return pi[1] + " " + getYrStr(pi[2]) + "  Ovr: " + pi[12] + " " + improveStr + transfer;
        } else {
            improveStr = " (Recruit)";
            return pi[1] + " " + getYrStr(pi[2]) + "  Ovr: " + pi[11] + " " + improveStr + transfer;
        }
    }

    /**
     * Converts player string into '$500 QB A. Name, Overall: 89' or similar
     */
    public String getPlayerNameCost(String player) {
        String[] ps = player.split(",");
        return "$" + ps[12] + " " + ps[0] + " " + ps[1] + ">Grade: " + getStarGrade(ps[6]);
    }

    /**
     * Used for parsing through string to get cost
     */
    public int getRecruitCost(String p) {
        String[] pSplit = p.split(",");
        return Integer.parseInt(pSplit[12]);
    }

    /**
     * Convert year from number to String, i.e. 3 -> Junior
     */
    private String getYrStr(String yr) {
        if (yr.equals("1")) {
            return "[Fr]";
        } else if (yr.equals("2")) {
            return "[So]";
        } else if (yr.equals("3")) {
            return "[Jr]";
        } else if (yr.equals("4")) {
            return "[Sr]";
        } else if (yr.equals("5")) {
            return "[Sr]";
        }
        return "[XX]";
    }

    public String getGrade(String num) {
        int pRat = (Integer.parseInt(num));
        if (pRat > five) return " * * * * *";
        else if (pRat > four) return " * * * *";
        else if (pRat > three) return " * * *";
        else if (pRat > two) return " * *";
        else return " *";
    }

    public String getStarGrade(String num) {
        int pRat = (Integer.parseInt(num));
        if (pRat == 5) return " * * * * *";
        if (pRat == 4) return " * * * *  ";
        if (pRat == 3) return " * * *    ";
        if (pRat == 2) return " * *      ";
        if (pRat == 1) return " *        ";

        else return "??";
    }

    public String getRegion(int region) {
        return states[region];
    }

    public String getHeight(int height) {

        int feet = height / 12;
        int leftover = height % 12;

        return feet + "'' " + leftover + "\"";
    }

    public String getWeight(int weight) {
        return weight + " lbs";
    }


    /**
     * Exit the recruiting activity. Called when the "Done" button is pressed or when user presses back button.
     */
    private void exitRecruiting() {
        StringBuilder sb = new StringBuilder();
        sb.append("Are you sure you are done recruiting? Any unfilled positions will be filled by walk-ons.\n\n");
        for (int i = 2; i < positions.size() - 4; ++i) {
            sb.append("\t\t" + positions.get(i) + "\n");
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(RecruitingActivity.this);
        builder.setMessage(sb.toString())
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Send info about what recruits were selected back
                        Intent myIntent = new Intent(RecruitingActivity.this, MainActivity.class);
                        myIntent.putExtra("SAVE_FILE", "DONE_RECRUITING");
                        myIntent.putExtra("RECRUITS", getRecruitsStr());
                        RecruitingActivity.this.startActivity(myIntent);
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Gets all the recruits in a string to send back to MainActivity to be added to user team
     */
    private String getRecruitsStr() {
        StringBuilder sb = new StringBuilder();

        for (String p : playersRecruited) {
            sb.append(p + "%\n");
        }
        sb.append("END_RECRUITS%\n");

        return sb.toString();
    }

    @Override
    public void onBackPressed() {
        exitRecruiting();
    }


    private void setShowPopUp(boolean tf) {
        showPopUp = tf;
    }


    //MAIN UI FOR PLAYER DATA IN RECRUITING SCREEN
    class ExpandableListAdapterRecruiting extends BaseExpandableListAdapter {

        private final Activity context;

        ExpandableListAdapterRecruiting(Activity context) {
            this.context = context;
        }

        public String getChild(int groupPosition, int childPosition) {
            return playersInfo.get(players.get(groupPosition).substring(0, players.get(groupPosition).length() - 2)).get(childPosition);
        }

        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }


        public View getChildView(final int groupPosition, final int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            final String playerDetail = getChild(groupPosition, childPosition);
            final String playerCSV = getGroup(groupPosition);
            LayoutInflater inflater = context.getLayoutInflater();

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.child_recruit, null);
            }

            // Set up Text for player details
            final TextView details = convertView.findViewById(R.id.textRecruitDetails);
            final TextView potential = convertView.findViewById(R.id.textRecruitPotential);

            details.setText(playerDetail);
            potential.setText("Height: " + getHeight(Integer.parseInt(playerCSV.split(",")[17])) + "\nWeight: " + getWeight(Integer.parseInt(playerCSV.split(",")[18])) + "\nFootball IQ: " + getGrade(playerCSV.split(",")[5]) + "\n" +
                    "Personality: " + getGrade(playerCSV.split(",")[4]) + "\n" +
                    "Durability: " + getGrade(playerCSV.split(",")[10]));

            // Set up Recruit and Redshirt buttons to display the right price
            Button recruitPlayerButton = convertView.findViewById(R.id.buttonRecruitPlayer);

            if (teamPlayers.size() + playersRecruited.size() < maxPlayers) {
                recruitPlayerButton.setText("Recruit: $" + getRecruitCost(playerCSV));
            } else recruitPlayerButton.setVisibility(View.INVISIBLE);

            // Set up button for recruiting player
            recruitPlayerButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Save who is currently expanded
                    if (teamPlayers.size() + playersRecruited.size() < maxPlayers) {
                        List<Integer> groupsExpanded = new ArrayList<>();
                        recruitPlayerDialog(playerCSV, groupPosition, groupsExpanded);
                    }
                }
            });

            return convertView;
        }

        public int getChildrenCount(int groupPosition) {
            return playersInfo.get(players.get(groupPosition).substring(0, players.get(groupPosition).length() - 2)).size();
        }

        public String getGroup(int groupPosition) {
            return players.get(groupPosition);
        }

        public int getGroupCount() {
            return players.size();
        }

        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            String[] playerCost = getPlayerNameCost(getGroup(groupPosition)).split(">");
            String playerLeft = playerCost[0];
            String playerRight = playerCost[1];
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.group_recruit,
                        null);
            }
            TextView itemL = convertView.findViewById(R.id.textRecruitLeft);
            itemL.setTypeface(null, Typeface.BOLD);
            itemL.setText(playerLeft);
            TextView itemR = convertView.findViewById(R.id.textRecruitRight);
            itemR.setTypeface(null, Typeface.BOLD);
            itemR.setText(playerRight);
            return convertView;
        }

        public boolean hasStableIds() {
            return true;
        }

        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

    }

}
