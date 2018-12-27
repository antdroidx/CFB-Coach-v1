package simulation;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import comparator.CompCoachCareer;
import comparator.CompCoachCareerPrestige;
import comparator.CompCoachOvr;
import comparator.CompCoachScore;
import comparator.CompConfPrestige;
import comparator.CompKickRetTD;
import comparator.CompKickRetYards;
import comparator.CompNFLTalent;
import comparator.CompPlayer;
import comparator.CompPlayerFGMade;
import comparator.CompPlayerFGpct;
import comparator.CompPlayerFumblesRec;
import comparator.CompPlayerHeisman;
import comparator.CompPlayerInterceptions;
import comparator.CompPlayerPassInts;
import comparator.CompPlayerPassPCT;
import comparator.CompPlayerPassRating;
import comparator.CompPlayerPassTDs;
import comparator.CompPlayerPassYards;
import comparator.CompPlayerRecTDs;
import comparator.CompPlayerRecYards;
import comparator.CompPlayerReceptions;
import comparator.CompPlayerRushTDs;
import comparator.CompPlayerRushYards;
import comparator.CompPlayerSacks;
import comparator.CompPlayerTackles;
import comparator.CompPuntRetTDs;
import comparator.CompPuntRetYards;
import comparator.CompTeamBowls;
import comparator.CompTeamBudget;
import comparator.CompTeamCC;
import comparator.CompTeamChemistry;
import comparator.CompTeamConfWins;
import comparator.CompTeamDefTalent;
import comparator.CompTeamDisciplineScore;
import comparator.CompTeamFacilities;
import comparator.CompTeamNC;
import comparator.CompTeamOPPG;
import comparator.CompTeamOPYPG;
import comparator.CompTeamORYPG;
import comparator.CompTeamOYPG;
import comparator.CompTeamOffTalent;
import comparator.CompTeamPPG;
import comparator.CompTeamPYPG;
import comparator.CompTeamPoll;
import comparator.CompTeamPrestige;
import comparator.CompTeamProjPoll;
import comparator.CompTeamRYPG;
import comparator.CompTeamRecruitClass;
import comparator.CompTeamSoS;
import comparator.CompTeamSoW;
import comparator.CompTeamTODiff;
import comparator.CompTeamWins;
import comparator.CompTeamYPG;
import positions.HeadCoach;
import positions.Player;
import positions.PlayerCB;
import positions.PlayerDL;
import positions.PlayerDefense;
import positions.PlayerK;
import positions.PlayerLB;
import positions.PlayerOL;
import positions.PlayerOffense;
import positions.PlayerQB;
import positions.PlayerRB;
import positions.PlayerReturner;
import positions.PlayerS;
import positions.PlayerTE;
import positions.PlayerWR;

public class League {
    public String saveVer = "v1.3.16";

    public ArrayList<String[]> leagueHistory;
    private ArrayList<String> heismanHistory;
    public ArrayList<String> leagueHoF;
    public ArrayList<Conference> conferences;
    public ArrayList<Team> teamList;
    public ArrayList<HeadCoach> coachList;
    public ArrayList<HeadCoach> coachStarList;
    private ArrayList<HeadCoach> coachFreeAgents;
    private ArrayList<String> nameList;
    private ArrayList<String> lastNameList;
    public ArrayList<ArrayList<String>> newsStories;
    public ArrayList<ArrayList<String>> weeklyScores;
    private ArrayList<String> teamDiscipline;
    private double disciplineChance = 0.15;

    public LeagueRecords leagueRecords;
    public LeagueRecords userTeamRecords;
    private TeamStreak longestWinStreak;
    private TeamStreak yearStartLongestWinStreak;
    private TeamStreak longestActiveWinStreak;

    // News Story Variables

    //League Stats
    public int leagueOffTal;
    public int leagueDefTal;
    public int confAvg;
    public double leagueChemistry;

    //Current week, 1-14
    public int currentWeek;

    //Bowl Games
    private boolean hasScheduledBowls;
    private Game semiG14;
    private Game semiG23;
    private Game ncg;
    private Game[] bowlGames;
    private Game[] cfpGames;
    public int playoffWeek;

    //User Team
    public Team userTeam;

    //Freshman Team
    private ArrayList<PlayerQB> fQBs;
    private ArrayList<PlayerRB> fRBs;
    private ArrayList<PlayerWR> fWRs;
    private ArrayList<PlayerTE> fTEs;
    private ArrayList<PlayerK> fKs;
    private ArrayList<PlayerOL> fOLs;
    private ArrayList<PlayerDL> fDLs;
    private ArrayList<PlayerLB> fLBs;
    private ArrayList<PlayerCB> fCBs;
    private ArrayList<PlayerS> fSs;
    //Transfer List
    public ArrayList<PlayerQB> transferQBs;
    public ArrayList<PlayerRB> transferRBs;
    public ArrayList<PlayerWR> transferWRs;
    public ArrayList<PlayerTE> transferTEs;
    public ArrayList<PlayerK> transferKs;
    public ArrayList<PlayerOL> transferOLs;
    public ArrayList<PlayerDL> transferDLs;
    public ArrayList<PlayerLB> transferLBs;
    public ArrayList<PlayerCB> transferCBs;
    public ArrayList<PlayerS> transferSs;
    public String userTransfers;
    public String sumTransfers;
    private ArrayList<String> transfersList;
    public ArrayList<Player> freshmen;
    public ArrayList<Player> redshirts;
    public String[] bowlNames;

    //Game Options
    public boolean fullGameLog;
    public boolean showPotential;
    public boolean confRealignment;
    public boolean enableUnivProRel;
    public boolean enableTV;
    public boolean neverRetire;
    public boolean expPlayoffs;
    public boolean advancedRealignment;
    public int countRealignment;
    public String newsRealignment;
    public boolean updateTV;
    public ArrayList<String> newsTV;
    public ArrayList<Team> playoffTeams;
    public String postseason;

    private final DecimalFormat df2 = new DecimalFormat(".##");
    private final int seasonStart = 2018;
    int countTeam = 130; //default roster automatically calculates this number when using custom data or loaded saves
    private final int seasonWeeks = 30;
    public int regSeasonWeeks = 13; //original = 13 will change dynamically based on team/conference structure
    private final double confRealignmentChance = .25; //chance of event .25
    private final double realignmentChance = .225; //chance of invite .33
    private boolean heismanDecided;
    private Player heisman;
    private Player defPOTY;
    private Player coachWinner;
    private Player freshman;
    private ArrayList<Player> heismanCandidates;
    private ArrayList<Player> defPOTYCandidates;
    private ArrayList<Player> freshmanCandidates;
    private ArrayList<Player> allAmericans;
    private ArrayList<Player> allFreshman;
    private String heismanWinnerStrFull;
    private String defPOTYWinnerStrFull;
    private String freshmanWinnerStrFull;
    private String coachWinnerStrFull;
    public boolean careerMode;

    private final String[] proTeams = {"New England", "Buffalo", "New Jersey", "Miami", "Pittsburgh", "Baltimore", "Cincinnati", "Cleveland", "Jacksonville", "Indianapolis", "Houston", "Tennessee", "Kansas City", "Oakland", "Anaheim", "Denver",
            "New York", "Philadelphia", "Dallas", "Washington", "Minnesota", "Chicago", "Green Bay", "Detroit", "New Orleans", "Carolina", "Tampa Bay", "Atlanta", "Seattle", "Los Angeles", "San Francisco", "Arizona"};

    public final String[] states = {"AS", "AZ", "CA", "HI", "ID", "MT", "NV", "OR", "UT", "WA", "CO", "KS", "MO", "NE", "NM", "ND", "OK", "SD", "TX", "WY", "IL", "IN", "IA", "KY", "MD", "MI", "MN", "OH", "TN", "WI", "CT", "DE", "ME", "MA", "NH", "NJ", "NY", "PA", "RI", "VT", "AL", "AK", "FL", "GA", "LA", "MS", "NC", "SC", "VA", "WV"};

    public final String bowlNamesText = "Carnation Bowl, Mandarin Bowl, Honey Bowl, Fiesta Bowl, Necatrine Bowl, Polyester Bowl, Lemon-Lime Bowl, Aligator Bowl, Desert Bowl, Fort Bowl, Vacation Bowl, Star Bowl, Bell Bowl, Freedom Bowl, Casino Bowl, American Bowl, Island Bowl, Philantropy Bowl, Steak Bowl, Camping Bowl, Spud Bowl, Music Bowl, New Orleans Bowl, Cowboy Bowl, Santa Fe Bowl, Burrito Bowl, Mexico Bowl, Chick Bowl, Empire Bowl, Rainbow Bowl, Mushroom Bowl, Coffee Bowl";

    public String[] teamsFCS = {"Alabama State", "Albany", "Cal-Poly", "Central Arkansas", "Chattanooga", "Columbia", "Dayton", "Deleware", "Eastern Wash", "Eastern Tenn", "Eastern Ken", "Harvard", "Yale", "Princeton", "Grambling", "Georgetown", "Idaho", "Idaho State", "James Madison", "Maine", "Miss Valley", "Montana", "New Hampshire", "North Dakota", "North Dakota St", "South Dakota", "South Dakota St", "Northern Arizona", "Northern Colorado", "Portland", "Rhode Island", "Sacramento", "Southern", "Southern TX", "Western llinois", "Vanilla", "Youngstown"};

    public String[] confNamesNew = {"Big 8", "National", "Constitution", "Colonial", "Contintental"};


    /**
     * Creates League, sets up Conferences, reads team names and conferences from file.
     * Also schedules games for every team.
     */
    public League(String namesCSV, String lastNamesCSV, String confText, String teamText, String bowlText, boolean randomize, boolean equalize) {
        careerMode = true;
        showPotential = false;
        confRealignment = true;
        enableTV = true;
        enableUnivProRel = false;
        neverRetire = false;
        setupCommonInitalizers();

        //set up names database from xml
        setupNamesDB(namesCSV, lastNamesCSV);

        //Set up bowls from XML
        bowlNames = new String[bowlText.split(", ").length];
        for (int b = 0; b < bowlText.split(", ").length; ++b) {
            bowlNames[b] = bowlText.split(", ")[b];
        }

        //Set up conferences from XML
        String[] confSplit = confText.split("%");
        int confDiv = 0;
        for (String n : confSplit) {
            conferences.add(new Conference(n.split(",")[0], this, false, 0, 0));
            conferences.get(confDiv).divisions.add(new Division(n.split(",")[1], this));
            conferences.get(confDiv).divisions.add(new Division(n.split(",")[2], this));
            confDiv++;
        }


        //Set up teams from XML
        int x = 0;
        int c = 0;
        if (!randomize && !equalize) {
            for (int t = 0; t < teamText.split("%").length; t++) {
                if (teamText.split("%")[t].contains("[END_CONF]")) {
                    x = 0;
                    c++;
                } else {
                    String[] teamID = teamText.split("%")[t].split(",");
                    conferences.get(c).confTeams.add(new Team(teamID[0].substring(1), teamID[1], teamID[2], Integer.parseInt(teamID[3]), teamID[4], Integer.parseInt(teamID[5]), this));
                    x++;
                }
            }
        } else if (randomize) {
            for (int t = 0; t < teamText.split("%").length; t++) {
                int tmPres = 0;
                if (c < 5) tmPres = (int) (Math.random() * 35) + 60;
                else tmPres = (int) (Math.random() * 35) + 25;
                if (teamText.split("%")[t].contains("[END_CONF]")) {
                    x = 0;
                    c++;
                } else {
                    String[] teamID = teamText.split("%")[t].split(",");
                    conferences.get(c).confTeams.add(new Team(teamID[0].substring(1), teamID[1], teamID[2], tmPres, teamID[4], Integer.parseInt(teamID[5]), this));
                    x++;
                }
            }
        } else {
            for (int t = 0; t < teamText.split("%").length; t++) {
                int tmPres = 60;
                if (teamText.split("%")[t].contains("[END_CONF]")) {
                    x = 0;
                    c++;
                } else {
                    String[] teamID = teamText.split("%")[t].split(",");
                    conferences.get(c).confTeams.add(new Team(teamID[0].substring(1), teamID[1], teamID[2], tmPres, teamID[4], Integer.parseInt(teamID[5]), this));
                    x++;
                }
            }
        }

        //set teamList
        for (int i = 0; i < conferences.size(); ++i) {
            for (int j = 0; j < conferences.get(i).confTeams.size(); ++j) {
                teamList.add(conferences.get(i).confTeams.get(j));
                teamList.get(i).playbookOffNum = teamList.get(i).getCPUOffense();
                teamList.get(i).playbookDefNum = teamList.get(i).getCPUDefense();
                teamList.get(i).playbookOff = teamList.get(i).getPlaybookOff()[teamList.get(i).playbookOffNum];
                teamList.get(i).playbookDef = teamList.get(i).getPlaybookDef()[teamList.get(i).playbookDefNum];
            }
        }

        setupSeason();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Creates a CUSTOM League Universe
     */
    public League(String namesCSV, String lastNamesCSV, File customConf, File customTeams, File customBowl, boolean randomize, boolean equalize) {
        careerMode = true;
        showPotential = false;
        confRealignment = true;
        enableTV = true;
        enableUnivProRel = false;
        neverRetire = false;

        setupCommonInitalizers();
        setupNamesDB(namesCSV, lastNamesCSV);

        String line = null;

        try {
            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(new FileReader(customConf));

            //First ignore the save file info
            line = bufferedReader.readLine();
            while ((line = bufferedReader.readLine()) != null && !line.equals("[END_CONFERENCES]")) {
                conferences.add(new Conference(line, this, false, 0, 0));
            }
        } catch (FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file");
        } catch (IOException ex) {
            System.out.println(
                    "Error reading file");
        }


        //Set up conference teams
        try {
            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(new FileReader(customTeams));

            //First ignore the save file info
            line = bufferedReader.readLine();
            countTeam = 0;
            while ((line = bufferedReader.readLine()) != null && !line.contains("[END_TEAMS]")) {
                for (int c = 0; c < conferences.size(); ++c) {
                    while ((line = bufferedReader.readLine()) != null && !line.contains("[END_CONF]")) {
                        String[] filesSplit = line.split(", ");
                        if (filesSplit.length > 1) {
                            line.replace("\"", "\\\"");

                            String tmName = filesSplit[0];
                            String tmAbbr = filesSplit[1];
                            String tmConf = filesSplit[2];
                            int tmPres = Integer.parseInt(filesSplit[3]);
                            if (randomize) {
                                if (c < 5) tmPres = (int) (Math.random() * 35) + 60;
                                else tmPres = (int) (Math.random() * 35) + 25;
                            }
                            if (equalize) {
                                tmPres = 60;
                            }
                            String tmRival = filesSplit[4];
                            int tmLoc = Integer.parseInt(filesSplit[5]);
                            conferences.get(c).confTeams.add(new Team(tmName, tmAbbr, tmConf, tmPres, tmRival, tmLoc, this));
                        } else {
                            filesSplit = line.split(",");
                            String tmName = filesSplit[0];
                            String tmAbbr = filesSplit[1];
                            String tmConf = filesSplit[2];
                            int tmPres = Integer.parseInt(filesSplit[3]);
                            if (randomize) {
                                if (c < 5) tmPres = (int) (Math.random() * 35) + 60;
                                else tmPres = (int) (Math.random() * 35) + 25;
                            }
                            if (equalize) {
                                tmPres = 60;
                            }
                            String tmRival = filesSplit[4];
                            int tmLoc = Integer.parseInt(filesSplit[5]);
                            conferences.get(c).confTeams.add(new Team(tmName, tmAbbr, tmConf, tmPres, tmRival, tmLoc, this));
                        }
                        countTeam++;
                    }
                }
            }
        } catch (FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file");
        } catch (IOException ex) {
            System.out.println(
                    "Error reading file");
        }

        //set teamList
        for (int i = 0; i < conferences.size(); ++i) {
            for (int j = 0; j < conferences.get(i).confTeams.size(); ++j) {
                teamList.add(conferences.get(i).confTeams.get(j));
                teamList.get(i).playbookOffNum = teamList.get(i).getCPUOffense();
                teamList.get(i).playbookDefNum = teamList.get(i).getCPUDefense();
                teamList.get(i).playbookOff = teamList.get(i).getPlaybookOff()[teamList.get(i).playbookOffNum];
                teamList.get(i).playbookDef = teamList.get(i).getPlaybookDef()[teamList.get(i).playbookDefNum];
            }
        }

        //Create new Bowl Game Names from TXT
        try {
            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(new FileReader(customBowl));

            //First ignore the save file info
            line = bufferedReader.readLine();
            String[] filesSplit = line.split(", ");
            if (filesSplit.length > 1) {
                bowlNames = new String[filesSplit.length];
                line.replaceAll("\"", "\\\"");
                for (int b = 0; b < filesSplit.length; ++b) {
                    bowlNames[b] = filesSplit[b];
                }
            } else {
                filesSplit = line.split(",");
                bowlNames = new String[filesSplit.length];
                for (int b = 0; b < filesSplit.length; ++b) {
                    bowlNames[b] = filesSplit[b];
                }
            }

        } catch (FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file");
        } catch (IOException ex) {
            System.out.println(
                    "Error reading file");
        }

        if (bowlNames.length != bowlNamesText.split(",").length) {

            String[] bowlTemp = bowlNames.clone();

            bowlNames = new String[bowlNamesText.split(",").length];

            for (int b = 0; b < bowlTemp.length; b++) {
                bowlNames[b] = bowlTemp[b];
            }

            for (int b = bowlTemp.length; b < bowlNamesText.split(",").length; b++) {
                bowlNames[b] = bowlNamesText.split(",")[b];
            }
        }

        //Rename conference to Independent if too small
        for (int c = 0; c < conferences.size(); c++) {
            if (conferences.get(c).confTeams.size() < conferences.get(c).minConfTeams) {
                conferences.get(c).confName = "Independent";
                for (int i = 0; i < conferences.get(c).confTeams.size(); i++) {
                    conferences.get(c).confTeams.get(i).conference = "Independent";
                }
            }
        }

        setupSeason();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * LOAD A SAVE FILE
     * Create League from saved file.
     *
     * @param saveFile file that league is saved in
     */
    public League(File saveFile, String namesCSV, String lastNamesCSV) {
        setupCommonInitalizers();
        setupNamesDB(namesCSV, lastNamesCSV);

        String line = null;

        try {
            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(new FileReader(saveFile));

            //First ignore the save file info
            line = bufferedReader.readLine();
            // Game Mode
            //careerMode = line.substring(line.length() - 4, line.length()).equals("[C]%");

            //Team Count
            if (line.split(">").length > 1) {
                if (!line.split(">")[1].contains("["))
                    countTeam = Integer.parseInt(line.split(">")[1]);
            }


            //Next get league history
            while ((line = bufferedReader.readLine()) != null && !line.equals("END_LEAGUE_HIST")) {
                leagueHistory.add(line.split("%"));
            }

            //Next get heismans
            while ((line = bufferedReader.readLine()) != null && !line.equals("END_HEISMAN_HIST")) {
                heismanHistory.add(line);
            }

            //Next make all the conferences & teams
            while ((line = bufferedReader.readLine()) != null && !line.equals("END_CONFERENCES")) {
                conferences.add(new Conference(line, this));
            }

            for (int i = 0; i < countTeam; ++i) { //Do for every team
                StringBuilder sbTeam = new StringBuilder();
                while ((line = bufferedReader.readLine()) != null && !line.equals("END_PLAYERS")) {
                    sbTeam.append(line);
                }
                Team t = new Team(sbTeam.toString(), this);
                conferences.get(getConfNumber(t.conference)).confTeams.add(t);
                teamList.add(t);
                teamList.get(i).playbookOffNum = teamList.get(i).getCPUOffense();
                teamList.get(i).playbookDefNum = teamList.get(i).getCPUDefense();
                teamList.get(i).playbookOff = teamList.get(i).getPlaybookOff()[teamList.get(i).playbookOffNum];
                teamList.get(i).playbookDef = teamList.get(i).getPlaybookDef()[teamList.get(i).playbookDefNum];
            }

            //Set up user team
            if ((line = bufferedReader.readLine()) != null) {
                for (Team t : teamList) {
                    if (t.name.equals(line)) {
                        userTeam = t;
                        userTeam.userControlled = true;
                    }
                }
            }
            //Team History
            while ((line = bufferedReader.readLine()) != null && !line.equals("END_TEAM_HISTORY")) {
                for (int i = 0; i < teamList.size(); ++i) { //Do for every team
                    while ((line = bufferedReader.readLine()) != null && !line.equals("END_TEAM")) {
                        teamList.get(i).teamHistory.add(line);
                    }
                }
            }
            //Coach History
            while ((line = bufferedReader.readLine()) != null && !line.equals("END_COACH_HISTORY")) {
                for (int i = 0; i < teamList.size(); ++i) { //Do for every team
                    while ((line = bufferedReader.readLine()) != null && !line.equals("END_COACH")) {
                        teamList.get(i).HC.get(0).history.add(line);
                    }
                }
            }


            while ((line = bufferedReader.readLine()) != null && !line.equals("END_BOWL_NAMES")) {
                String[] filesSplit = line.split(",");
                bowlNames = new String[filesSplit.length];
                for (int b = 0; b < filesSplit.length; ++b) {
                    bowlNames[b] = filesSplit[b];
                }
            }

            //fix bowl names
            if (bowlNames.length != bowlNamesText.split(",").length) {

                String[] bowlTemp = bowlNames.clone();

                bowlNames = new String[bowlNamesText.split(",").length];

                for (int b = 0; b < bowlTemp.length; b++) {
                    bowlNames[b] = bowlTemp[b];
                }

                for (int b = bowlTemp.length; b < bowlNamesText.split(",").length; b++) {
                    bowlNames[b] = bowlNamesText.split(",")[b];
                }
            }

            //fix bowl null
            if (bowlNames.length > 0 && bowlNames[0] == null) {

                bowlNames = new String[bowlNamesText.split(",").length];

                for (int b = 0; b < bowlNamesText.split(",").length; b++) {
                    bowlNames[b] = bowlNamesText.split(",")[b];
                }
            }

            String[] record;
            while ((line = bufferedReader.readLine()) != null && !line.equals("END_LEAGUE_RECORDS")) {
                record = line.split(",");
                if (!record[1].equals("-1"))
                    leagueRecords.checkRecord(record[0], Integer.parseInt(record[1]), record[2], Integer.parseInt(record[3]));
            }

            while ((line = bufferedReader.readLine()) != null && !line.equals("END_LEAGUE_WIN_STREAK")) {
                record = line.split(",");
                longestWinStreak = new TeamStreak(
                        Integer.parseInt(record[2]), Integer.parseInt(record[3]), Integer.parseInt(record[0]), record[1]);
                yearStartLongestWinStreak = new TeamStreak(
                        Integer.parseInt(record[2]), Integer.parseInt(record[3]), Integer.parseInt(record[0]), record[1]);
            }

            while ((line = bufferedReader.readLine()) != null && !line.equals("END_TEAM_RECORDS")) {
                for (int i = 0; i < teamList.size(); ++i) { //Do for every team
                    while ((line = bufferedReader.readLine()) != null && !line.equals("END_TEAM")) {
                        record = line.split(",");
                        if (!record[1].equals("-1"))
                            teamList.get(i).teamRecords.checkRecord(record[0], Integer.parseInt(record[1]), record[2], Integer.parseInt(record[3]));
                    }
                }
            }

            while ((line = bufferedReader.readLine()) != null && !line.equals("END_LEAGUE_HALL_OF_FAME")) {
                leagueHoF.add(line);
                String[] fileSplit = line.split(":");
                for (int i = 0; i < teamList.size(); ++i) {
                    if (teamList.get(i).name.equals(fileSplit[0])) {
                        teamList.get(i).hallOfFame.add(line);
                    }
                }
            }
            int coachFA = 0;
            while ((line = bufferedReader.readLine()) != null && !line.equals("END_COACHES")) {
                String h[] = line.split("%");
                int cPres = 0;
                if (h.length == 1) {
                    String c[] = line.split(",");
                    if (c.length > 21) cPres = Integer.parseInt(c[21]);
                    coachFreeAgents.add(new HeadCoach(c[0], Integer.parseInt(c[1]), Integer.parseInt(c[2]), Integer.parseInt(c[3]), Integer.parseInt(c[4]), Integer.parseInt(c[5]), Integer.parseInt(c[6]), Integer.parseInt(c[7])
                            , Integer.parseInt(c[8]), Integer.parseInt(c[9]), Integer.parseInt(c[10]), Integer.parseInt(c[11]), Integer.parseInt(c[12]), Integer.parseInt(c[13]), Integer.parseInt(c[14]), Integer.parseInt(c[15])
                            , Integer.parseInt(c[16]), Integer.parseInt(c[17]), Integer.parseInt(c[18]), Integer.parseInt(c[19]), Integer.parseInt(c[20]), cPres));
                } else if (h.length == 2) {
                    String c[] = h[0].split(",");
                    if (c.length > 21) cPres = Integer.parseInt(c[21]);
                    coachFreeAgents.add(new HeadCoach(c[0], Integer.parseInt(c[1]), Integer.parseInt(c[2]), Integer.parseInt(c[3]), Integer.parseInt(c[4]), Integer.parseInt(c[5]), Integer.parseInt(c[6]), Integer.parseInt(c[7])
                            , Integer.parseInt(c[8]), Integer.parseInt(c[9]), Integer.parseInt(c[10]), Integer.parseInt(c[11]), Integer.parseInt(c[12]), Integer.parseInt(c[13]), Integer.parseInt(c[14]), Integer.parseInt(c[15])
                            , Integer.parseInt(c[16]), Integer.parseInt(c[17]), Integer.parseInt(c[18]), Integer.parseInt(c[19]), Integer.parseInt(c[20]), cPres));
                    while ((line = bufferedReader.readLine()) != null && !line.equals("END_FREE_AGENT")) {
                        coachFreeAgents.get(coachFA).history.add(line);
                    }
                    coachFA++;
                }
            }


            while ((line = bufferedReader.readLine()) != null && !line.equals("END_GAME_LOG")) {
                fullGameLog = Boolean.parseBoolean(line);
            }

            while ((line = bufferedReader.readLine()) != null && !line.equals("END_HIDE_POTENTIAL")) {
                showPotential = Boolean.parseBoolean(line);
            }

            while ((line = bufferedReader.readLine()) != null && !line.equals("END_CONF_REALIGNMENT")) {
                confRealignment = Boolean.parseBoolean(line);
            }

            while ((line = bufferedReader.readLine()) != null && !line.equals("END_ENABLE_TV")) {
                enableTV = Boolean.parseBoolean(line);
            }

            while ((line = bufferedReader.readLine()) != null && !line.equals("END_PRO_REL")) {
                enableUnivProRel = Boolean.parseBoolean((line));
            }

            while ((line = bufferedReader.readLine()) != null && !line.equals("END_POPUPS")) {
                userTeam.showPopups = Boolean.parseBoolean((line));
            }

            while ((line = bufferedReader.readLine()) != null && !line.equals("END_NEVER_RETIRE")) {
                neverRetire = Boolean.parseBoolean((line));
            }

            while ((line = bufferedReader.readLine()) != null && !line.equals("END_CAREER_MODE")) {
                careerMode = Boolean.parseBoolean((line));
            }

            while ((line = bufferedReader.readLine()) != null && !line.equals("END_EXP_PLAYOFFS")) {
                expPlayoffs = Boolean.parseBoolean((line));
            }

            while ((line = bufferedReader.readLine()) != null && !line.equals("END_ADV_CONF_REALIGNMENT")) {
                advancedRealignment = Boolean.parseBoolean(line);
            }

            if (enableUnivProRel) {
                confRealignment = false;
                advancedRealignment = false;
            }

            // Always close files.
            bufferedReader.close();


        } catch (FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file");
        } catch (IOException ex) {
            System.out.println(
                    "Error reading file");
        }

        //Get longest active win streak
        updateLongestActiveWinStreak();


        setupSeason();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * LOAD A SAVE FILE & FIX IT
     * Create League from saved file.
     *
     * @param saveFile file that league is saved in
     */
    public League(File saveFile, String namesCSV, String lastNamesCSV, boolean fixSave) {
        setupCommonInitalizers();
        setupNamesDB(namesCSV, lastNamesCSV);

        String line = null;

        try {
            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(new FileReader(saveFile));

            //First ignore the save file info
            line = bufferedReader.readLine();
            // Game Mode
            //careerMode = line.substring(line.length() - 4, line.length()).equals("[C]%");

            //Team Count
            if (line.split(">").length > 1) {
                if (!line.split(">")[1].contains("["))
                    countTeam = Integer.parseInt(line.split(">")[1]);
            }


            //Next get league history
            while ((line = bufferedReader.readLine()) != null && !line.equals("END_LEAGUE_HIST")) {
                leagueHistory.add(line.split("%"));
            }

            //Next get heismans
            while ((line = bufferedReader.readLine()) != null && !line.equals("END_HEISMAN_HIST")) {
                heismanHistory.add(line);
            }

            //Next make all the conferences & teams
            while ((line = bufferedReader.readLine()) != null && !line.equals("END_CONFERENCES")) {
                conferences.add(new Conference(line, this));
            }

            for (int i = 0; i < countTeam; ++i) { //Do for every team
                StringBuilder sbTeam = new StringBuilder();
                while ((line = bufferedReader.readLine()) != null && !line.equals("END_PLAYERS")) {
                    sbTeam.append(line);
                }
                Team t = new Team(sbTeam.toString(), this);
                conferences.get(getConfNumber(t.conference)).confTeams.add(t);
                teamList.add(t);
                teamList.get(i).playbookOffNum = teamList.get(i).getCPUOffense();
                teamList.get(i).playbookDefNum = teamList.get(i).getCPUDefense();
                teamList.get(i).playbookOff = teamList.get(i).getPlaybookOff()[teamList.get(i).playbookOffNum];
                teamList.get(i).playbookDef = teamList.get(i).getPlaybookDef()[teamList.get(i).playbookDefNum];
            }

            //Set up user team
            if ((line = bufferedReader.readLine()) != null) {
                for (Team t : teamList) {
                    if (t.name.equals(line)) {
                        userTeam = t;
                        userTeam.userControlled = true;
                    }
                }
            }
            //Team History
            while ((line = bufferedReader.readLine()) != null && !line.equals("END_TEAM_HISTORY")) {
                for (int i = 0; i < teamList.size(); ++i) { //Do for every team
                    while ((line = bufferedReader.readLine()) != null && !line.equals("END_TEAM")) {
                        teamList.get(i).teamHistory.add(line);
                    }
                }
            }
            //Coach History
            while ((line = bufferedReader.readLine()) != null && !line.equals("END_COACH_HISTORY")) {
                for (int i = 0; i < teamList.size(); ++i) { //Do for every team
                    while ((line = bufferedReader.readLine()) != null && !line.equals("END_COACH")) {
                        teamList.get(i).HC.get(0).history.add(line);
                    }
                }
            }


            while ((line = bufferedReader.readLine()) != null && !line.equals("END_BOWL_NAMES")) {
                String[] filesSplit = line.split(",");
                bowlNames = new String[filesSplit.length];
                for (int b = 0; b < filesSplit.length; ++b) {
                    bowlNames[b] = filesSplit[b];
                }
            }

            //fix bowl names
            if (bowlNames.length != bowlNamesText.split(",").length) {

                String[] bowlTemp = bowlNames.clone();

                bowlNames = new String[bowlNamesText.split(",").length];

                for (int b = 0; b < bowlTemp.length; b++) {
                    bowlNames[b] = bowlTemp[b];
                }

                for (int b = bowlTemp.length; b < bowlNamesText.split(",").length; b++) {
                    bowlNames[b] = bowlNamesText.split(",")[b];
                }
            }

            String[] record;
            while ((line = bufferedReader.readLine()) != null && !line.equals("END_LEAGUE_RECORDS")) {
                record = line.split(",");
                if (!record[1].equals("-1"))
                    leagueRecords.checkRecord(record[0], Integer.parseInt(record[1]), record[2], Integer.parseInt(record[3]));
            }

            while ((line = bufferedReader.readLine()) != null && !line.equals("END_LEAGUE_WIN_STREAK")) {
                record = line.split(",");
                longestWinStreak = new TeamStreak(
                        Integer.parseInt(record[2]), Integer.parseInt(record[3]), Integer.parseInt(record[0]), record[1]);
                yearStartLongestWinStreak = new TeamStreak(
                        Integer.parseInt(record[2]), Integer.parseInt(record[3]), Integer.parseInt(record[0]), record[1]);
            }

            while ((line = bufferedReader.readLine()) != null && !line.equals("END_TEAM_RECORDS")) {
                for (int i = 0; i < teamList.size(); ++i) { //Do for every team
                    while ((line = bufferedReader.readLine()) != null && !line.equals("END_TEAM")) {
                        record = line.split(",");
                        if (!record[1].equals("-1"))
                            teamList.get(i).teamRecords.checkRecord(record[0], Integer.parseInt(record[1]), record[2], Integer.parseInt(record[3]));
                    }
                }
            }

            while ((line = bufferedReader.readLine()) != null && !line.equals("END_LEAGUE_HALL_OF_FAME")) {
                leagueHoF.add(line);
                String[] fileSplit = line.split(":");
                for (int i = 0; i < teamList.size(); ++i) {
                    if (teamList.get(i).name.equals(fileSplit[0])) {
                        teamList.get(i).hallOfFame.add(line);
                    }
                }
            }
            int coachFA = 0;
            while ((line = bufferedReader.readLine()) != null && !line.equals("END_COACHES")) {
                String h[] = line.split("%");
                int cPres = 0;
                if (h.length == 1) {
                    String c[] = line.split(",");
                    if (c.length > 21) cPres = Integer.parseInt(c[21]);
                    coachFreeAgents.add(new HeadCoach(c[0], Integer.parseInt(c[1]), Integer.parseInt(c[2]), Integer.parseInt(c[3]), Integer.parseInt(c[4]), Integer.parseInt(c[5]), Integer.parseInt(c[6]), Integer.parseInt(c[7])
                            , Integer.parseInt(c[8]), Integer.parseInt(c[9]), Integer.parseInt(c[10]), Integer.parseInt(c[11]), Integer.parseInt(c[12]), Integer.parseInt(c[13]), Integer.parseInt(c[14]), Integer.parseInt(c[15])
                            , Integer.parseInt(c[16]), Integer.parseInt(c[17]), Integer.parseInt(c[18]), Integer.parseInt(c[19]), Integer.parseInt(c[20]), cPres));
                } else if (h.length == 2) {
                    String c[] = h[0].split(",");
                    if (c.length > 21) cPres = Integer.parseInt(c[21]);
                    coachFreeAgents.add(new HeadCoach(c[0], Integer.parseInt(c[1]), Integer.parseInt(c[2]), Integer.parseInt(c[3]), Integer.parseInt(c[4]), Integer.parseInt(c[5]), Integer.parseInt(c[6]), Integer.parseInt(c[7])
                            , Integer.parseInt(c[8]), Integer.parseInt(c[9]), Integer.parseInt(c[10]), Integer.parseInt(c[11]), Integer.parseInt(c[12]), Integer.parseInt(c[13]), Integer.parseInt(c[14]), Integer.parseInt(c[15])
                            , Integer.parseInt(c[16]), Integer.parseInt(c[17]), Integer.parseInt(c[18]), Integer.parseInt(c[19]), Integer.parseInt(c[20]), cPres));
                    while ((line = bufferedReader.readLine()) != null && !line.equals("END_FREE_AGENT")) {
                        coachFreeAgents.get(coachFA).history.add(line);
                    }
                    coachFA++;
                }
            }


            while ((line = bufferedReader.readLine()) != null && !line.equals("END_GAME_LOG")) {
                fullGameLog = Boolean.parseBoolean(line);
            }

            while ((line = bufferedReader.readLine()) != null && !line.equals("END_HIDE_POTENTIAL")) {
                showPotential = Boolean.parseBoolean(line);
            }

            while ((line = bufferedReader.readLine()) != null && !line.equals("END_CONF_REALIGNMENT")) {
                confRealignment = Boolean.parseBoolean(line);
            }

            while ((line = bufferedReader.readLine()) != null && !line.equals("END_ENABLE_TV")) {
                enableTV = Boolean.parseBoolean(line);
            }

            while ((line = bufferedReader.readLine()) != null && !line.equals("END_PRO_REL")) {
                enableUnivProRel = Boolean.parseBoolean((line));
            }

            while ((line = bufferedReader.readLine()) != null && !line.equals("END_POPUPS")) {
                userTeam.showPopups = Boolean.parseBoolean((line));
            }

            while ((line = bufferedReader.readLine()) != null && !line.equals("END_NEVER_RETIRE")) {
                neverRetire = Boolean.parseBoolean((line));
            }

            while ((line = bufferedReader.readLine()) != null && !line.equals("END_CAREER_MODE")) {
                careerMode = Boolean.parseBoolean((line));
            }

            while ((line = bufferedReader.readLine()) != null && !line.equals("END_EXP_PLAYOFFS")) {
                expPlayoffs = Boolean.parseBoolean((line));
            }

            if (enableUnivProRel) {
                confRealignment = false;
            }

            // Always close files.
            bufferedReader.close();


        } catch (FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file");
        } catch (IOException ex) {
            System.out.println(
                    "Error reading file");
        }

        //Get longest active win streak
        updateLongestActiveWinStreak();

        //fix team divisions
        for (int c = 0; c < conferences.size(); c++) {
            for (int i = 0; i < conferences.get(c).confTeams.size(); i++) {
                conferences.get(c).divisions.add(new Division("A", this));
                conferences.get(c).divisions.add(new Division("B", this));
                if (i % 2 == 0) {
                    conferences.get(c).confTeams.get(i).division = conferences.get(c).divisions.get(1).divName;
                    conferences.get(c).divisions.get(0).divTeams.add(conferences.get(c).confTeams.get(i));
                } else {
                    conferences.get(c).confTeams.get(i).division = conferences.get(c).divisions.get(2).divName;
                    conferences.get(c).divisions.get(1).divTeams.add(conferences.get(c).confTeams.get(i));
                }
            }
        }

        setupSeason();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * IMPORT A SAVE FILE
     * Create League from saved file.
     **/
    public League(InputStream inputStream, String namesCSV, String lastNamesCSV) {
        setupCommonInitalizers();
        setupNamesDB(namesCSV, lastNamesCSV);

        String line = null;

        try {
            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            //First ignore the save file info
            line = bufferedReader.readLine();
            // Game Mode
            //careerMode = line.substring(line.length() - 4, line.length()).equals("[C]%");
            //Team Count
            if (line.split(">").length > 1) {
                countTeam = Integer.parseInt(line.split(">")[1]);
            }


            //Next get league history

            while ((line = bufferedReader.readLine()) != null && !line.equals("END_LEAGUE_HIST")) {
                leagueHistory.add(line.split("%"));
            }

            //Next get heismans
            while ((line = bufferedReader.readLine()) != null && !line.equals("END_HEISMAN_HIST")) {
                heismanHistory.add(line);
            }

            //Next make all the conferences & teams
            while ((line = bufferedReader.readLine()) != null && !line.equals("END_CONFERENCES")) {
                conferences.add(new Conference(line, this));
            }

            for (int i = 0; i < countTeam; ++i) { //Do for every team
                StringBuilder sbTeam = new StringBuilder();
                while ((line = bufferedReader.readLine()) != null && !line.equals("END_PLAYERS")) {
                    sbTeam.append(line);
                }
                Team t = new Team(sbTeam.toString(), this);
                conferences.get(getConfNumber(t.conference)).confTeams.add(t);
                teamList.add(t);
                teamList.get(i).playbookOffNum = teamList.get(i).getCPUOffense();
                teamList.get(i).playbookDefNum = teamList.get(i).getCPUDefense();
                teamList.get(i).playbookOff = teamList.get(i).getPlaybookOff()[teamList.get(i).playbookOffNum];
                teamList.get(i).playbookDef = teamList.get(i).getPlaybookDef()[teamList.get(i).playbookDefNum];
            }

            //Set up user team
            if ((line = bufferedReader.readLine()) != null) {
                for (Team t : teamList) {
                    if (t.name.equals(line)) {
                        userTeam = t;
                        userTeam.userControlled = true;
                    }
                }
            }
            //Team History
            while ((line = bufferedReader.readLine()) != null && !line.equals("END_TEAM_HISTORY")) {
                for (int i = 0; i < teamList.size(); ++i) { //Do for every team
                    while ((line = bufferedReader.readLine()) != null && !line.equals("END_TEAM")) {
                        teamList.get(i).teamHistory.add(line);
                    }
                }
            }
            //Coach History
            while ((line = bufferedReader.readLine()) != null && !line.equals("END_COACH_HISTORY")) {
                for (int i = 0; i < teamList.size(); ++i) { //Do for every team
                    while ((line = bufferedReader.readLine()) != null && !line.equals("END_COACH")) {
                        teamList.get(i).HC.get(0).history.add(line);
                    }
                }
            }

            while ((line = bufferedReader.readLine()) != null && !line.equals("END_BOWL_NAMES")) {
                String[] filesSplit = line.split(",");
                bowlNames = new String[filesSplit.length];

                for (int b = 0; b < filesSplit.length; ++b) {
                    bowlNames[b] = filesSplit[b];
                }
            }

            //fix bowl names
            if (bowlNames.length != bowlNamesText.split(",").length) {

                String[] bowlTemp = bowlNames.clone();

                bowlNames = new String[bowlNamesText.split(",").length];

                for (int b = 0; b < bowlTemp.length; b++) {
                    bowlNames[b] = bowlTemp[b];
                }

                for (int b = bowlTemp.length; b < bowlNamesText.split(",").length; b++) {
                    bowlNames[b] = bowlNamesText.split(",")[b];
                }
            }


            String[] record;
            while ((line = bufferedReader.readLine()) != null && !line.equals("END_LEAGUE_RECORDS")) {
                record = line.split(",");
                if (!record[1].equals("-1"))
                    leagueRecords.checkRecord(record[0], Integer.parseInt(record[1]), record[2], Integer.parseInt(record[3]));
            }

            while ((line = bufferedReader.readLine()) != null && !line.equals("END_LEAGUE_WIN_STREAK")) {
                record = line.split(",");
                longestWinStreak = new TeamStreak(
                        Integer.parseInt(record[2]), Integer.parseInt(record[3]), Integer.parseInt(record[0]), record[1]);
                yearStartLongestWinStreak = new TeamStreak(
                        Integer.parseInt(record[2]), Integer.parseInt(record[3]), Integer.parseInt(record[0]), record[1]);
            }

            while ((line = bufferedReader.readLine()) != null && !line.equals("END_TEAM_RECORDS")) {
                for (int i = 0; i < teamList.size(); ++i) { //Do for every team
                    while ((line = bufferedReader.readLine()) != null && !line.equals("END_TEAM")) {
                        record = line.split(",");
                        if (!record[1].equals("-1"))
                            teamList.get(i).teamRecords.checkRecord(record[0], Integer.parseInt(record[1]), record[2], Integer.parseInt(record[3]));
                    }
                }
            }

            while ((line = bufferedReader.readLine()) != null && !line.equals("END_LEAGUE_HALL_OF_FAME")) {
                leagueHoF.add(line);
                String[] fileSplit = line.split(":");
                for (int i = 0; i < teamList.size(); ++i) {
                    if (teamList.get(i).name.equals(fileSplit[0])) {
                        teamList.get(i).hallOfFame.add(line);
                    }
                }
            }
            int coachFA = 0;
            while ((line = bufferedReader.readLine()) != null && !line.equals("END_COACHES")) {
                String h[] = line.split("%");
                int cPres = 0;
                if (h.length == 1) {
                    String c[] = line.split(",");
                    if (c.length > 21) cPres = Integer.parseInt(c[21]);
                    coachFreeAgents.add(new HeadCoach(c[0], Integer.parseInt(c[1]), Integer.parseInt(c[2]), Integer.parseInt(c[3]), Integer.parseInt(c[4]), Integer.parseInt(c[5]), Integer.parseInt(c[6]), Integer.parseInt(c[7])
                            , Integer.parseInt(c[8]), Integer.parseInt(c[9]), Integer.parseInt(c[10]), Integer.parseInt(c[11]), Integer.parseInt(c[12]), Integer.parseInt(c[13]), Integer.parseInt(c[14]), Integer.parseInt(c[15])
                            , Integer.parseInt(c[16]), Integer.parseInt(c[17]), Integer.parseInt(c[18]), Integer.parseInt(c[19]), Integer.parseInt(c[20]), cPres));
                } else if (h.length == 2) {
                    String c[] = h[0].split(",");
                    if (c.length > 21) cPres = Integer.parseInt(c[21]);
                    coachFreeAgents.add(new HeadCoach(c[0], Integer.parseInt(c[1]), Integer.parseInt(c[2]), Integer.parseInt(c[3]), Integer.parseInt(c[4]), Integer.parseInt(c[5]), Integer.parseInt(c[6]), Integer.parseInt(c[7])
                            , Integer.parseInt(c[8]), Integer.parseInt(c[9]), Integer.parseInt(c[10]), Integer.parseInt(c[11]), Integer.parseInt(c[12]), Integer.parseInt(c[13]), Integer.parseInt(c[14]), Integer.parseInt(c[15])
                            , Integer.parseInt(c[16]), Integer.parseInt(c[17]), Integer.parseInt(c[18]), Integer.parseInt(c[19]), Integer.parseInt(c[20]), cPres));
                    while ((line = bufferedReader.readLine()) != null && !line.equals("END_FREE_AGENT")) {
                        coachFreeAgents.get(coachFA).history.add(line);
                    }
                    coachFA++;
                }
            }

            fullGameLog = Boolean.parseBoolean(bufferedReader.readLine());
            showPotential = Boolean.parseBoolean(bufferedReader.readLine());
            confRealignment = Boolean.parseBoolean(bufferedReader.readLine());
            enableTV = Boolean.parseBoolean(bufferedReader.readLine());
            enableUnivProRel = Boolean.parseBoolean((bufferedReader.readLine()));
            userTeam.showPopups = Boolean.parseBoolean((bufferedReader.readLine()));
            neverRetire = Boolean.parseBoolean((bufferedReader.readLine()));
            careerMode = Boolean.parseBoolean((bufferedReader.readLine()));
            expPlayoffs = Boolean.parseBoolean((bufferedReader.readLine()));
            advancedRealignment = Boolean.parseBoolean((bufferedReader.readLine()));

            if (enableUnivProRel) {
                confRealignment = false;
                advancedRealignment = false;
            }

            // Always close files.
            bufferedReader.close();


        } catch (FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file");
        } catch (IOException ex) {
            System.out.println(
                    "Error reading file");
        }

        //fix team divions
        for (int c = 0; c < conferences.size(); c++) {
            for (int i = 0; i < conferences.get(c).confTeams.size(); i++) {
                conferences.get(c).divisions.add(new Division("A", this));
                conferences.get(c).divisions.add(new Division("B", this));
                if (i % 2 == 0) {
                    conferences.get(c).confTeams.get(i).division = conferences.get(c).divisions.get(1).divName;
                    conferences.get(c).divisions.get(0).divTeams.add(conferences.get(c).confTeams.get(i));
                } else {
                    conferences.get(c).confTeams.get(i).division = conferences.get(c).divisions.get(2).divName;
                    conferences.get(c).divisions.get(1).divTeams.add(conferences.get(c).confTeams.get(i));
                }
            }
        }

        //Get longest active win streak
        updateLongestActiveWinStreak();

        setupSeason();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    //Initialize all common variables for each game type
    private void setupCommonInitalizers() {

        nameList = new ArrayList<>();
        lastNameList = new ArrayList<>();
        heismanDecided = false;
        hasScheduledBowls = false;
        bowlGames = new Game[bowlNamesText.split(",").length];
        bowlNames = new String[bowlNamesText.split(",").length];
        cfpGames = new Game[15];
        playoffTeams = new ArrayList<>();
        leagueHistory = new ArrayList<>();
        heismanHistory = new ArrayList<>();
        leagueHoF = new ArrayList<>();
        coachList = new ArrayList<>();
        coachStarList = new ArrayList<>();
        coachFreeAgents = new ArrayList<>();

        conferences = new ArrayList<>();
        teamList = new ArrayList<>();

        allAmericans = new ArrayList<>();
        allFreshman = new ArrayList<>();

        transferQBs = new ArrayList<>();
        transferRBs = new ArrayList<>();
        transferWRs = new ArrayList<>();
        transferTEs = new ArrayList<>();
        transferKs = new ArrayList<>();
        transferOLs = new ArrayList<>();
        transferDLs = new ArrayList<>();
        transferLBs = new ArrayList<>();
        transferCBs = new ArrayList<>();
        transferSs = new ArrayList<>();

        freshmen = new ArrayList<>();
        redshirts = new ArrayList<>();

        leagueRecords = new LeagueRecords();
        userTeamRecords = new LeagueRecords();
        longestWinStreak = new TeamStreak(getYear(), getYear(), 0, "XXX");
        yearStartLongestWinStreak = new TeamStreak(getYear(), getYear(), 0, "XXX");
        longestActiveWinStreak = new TeamStreak(getYear(), getYear(), 0, "XXX");
    }

    //Set Up Season variables
    private void setupSeason() {

        int numOddConf = 0;
        int largeOddConf = 0;
        for (int i = 0; i < conferences.size(); i++) {
            if(conferences.get(i).confTeams.size() % 2 != 0) {
                numOddConf++;
                advancedRealignment = true;
/*                if(conferences.get(i).confTeams.size() >= 13) {
                    largeOddConf++;
                }*/
            }
        }
        if (numOddConf > 0) regSeasonWeeks++;
        if (largeOddConf > 0) regSeasonWeeks++;

        //set up schedule
        for (int i = 0; i < conferences.size(); ++i) {
            conferences.get(i).setUpSchedule();
        }

        //decide OOC schedule
        for (int r = 0; r < regSeasonWeeks; r++) {
            int j = 0;
            int k = 0;

            for (int c = 0; c < conferences.size(); c++) {
                if (r < conferences.get(c).oocGames && conferences.get(c).confTeams.size() >= conferences.get(c).minConfTeams) {
                    boolean scheduled = false;
                    k = k + (int) (Math.random() * 4);
                    while (!scheduled) {
                        int week = (j + r + k) % (regSeasonWeeks - 1);
                        if (!conferences.get(c).oocWeeks.contains(week)) {
                            conferences.get(c).oocWeeks.add(week);
                            for (int t = 0; t < conferences.get(c).confTeams.size(); t++) {
                                conferences.get(c).confTeams.get(t).oocWeeks.add(week);
                            }
                            scheduled = true;
                        } else {
                            k = k + 2;
                        }
                    }
                    j++;
                } else if (conferences.get(c).confTeams.size() < conferences.get(c).minConfTeams && r < conferences.get(c).oocGames) {
                    for (int t = 0; t < conferences.get(c).confTeams.size(); t++) {
                        conferences.get(c).confTeams.get(t).oocWeeks.add(r);
                    }
                }
            }
        }

        //setup FCS Team Database

        //get list of team names
        ArrayList<String> leagueTeams = new ArrayList<>();
        for(int i = 0; i < teamList.size(); i++) {
            leagueTeams.add(teamList.get(i).name);
        }

        for(int i = 0; i < teamsFCS.length; i++) {
            if(leagueTeams.contains(teamsFCS[i])) teamsFCS[i] = "FCS Team";
        }

        //Setup OOC v3 Scheduling
        if (!enableUnivProRel) {
            for (int week = 0; week < (regSeasonWeeks-1); week++) {

                ArrayList<Team> availTeams = new ArrayList<>();
                for (int t = 0; t < teamList.size(); t++) {
                    if (teamList.get(t).oocWeeks.contains(week)) {
                        availTeams.add(teamList.get(t));
                    }
                }

                while (availTeams.size() > 0) {
                    int selTeamA = (int) (availTeams.size() * Math.random());
                    Team a = availTeams.get(selTeamA);

                    ArrayList<Team> availTeamsB = new ArrayList<>();
                    for (int k = 0; k < availTeams.size(); k++) {
                        if (!availTeams.get(k).conference.equals(a.conference) && !a.oocTeams.contains(availTeams.get(k))) {
                            availTeamsB.add(availTeams.get(k));
                        }
                    }
                    Team b;

                    if (availTeamsB.isEmpty()) {
                        b = new Team(teamsFCS[(int) (teamsFCS.length * Math.random())], "FCS", "FCS Division", (int) (Math.random() * 40), "FCS1", 0, this, true);
                    } else {
                        int selTeamB = (int) (availTeamsB.size() * Math.random());
                        b = availTeamsB.get(selTeamB);
                    }

                    Game gm;
                    gm = new Game(a, b, "OOC");

                    //Log.d("league", "setupSeason: " + a.name + " vs " + b.name);

                    if (!a.conference.contains("Independent") && !a.conference.contains("FCS")) {
                        a.gameSchedule.add(week, gm);
                    }
                    if (!b.conference.contains("Independent") && !b.conference.contains("FCS"))  {
                        b.gameSchedule.add(week, gm);
                    }

                    if (a.conference.contains("Independent")) {
                        a.gameSchedule.add(gm);
                    }
                    if (b.conference.contains("Independent")) {
                        b.gameSchedule.add(gm);
                    }

                    a.oocTeams.add(b);
                    b.oocTeams.add(a);

                    availTeams.remove(a);
                    availTeams.remove(b);
                }

            }


            if(numOddConf > 0) {
                for (int c = 0; c < conferences.size(); c++) {
                    if (conferences.get(c).confTeams.size() < conferences.get(c).minConfTeams) {
                        Team bye = new Team("BYE", "BYE", "BYE", 0, "BYE", 0, this);
                        bye.rankTeamPollScore = teamList.size();
                        for (int g = 0; g < conferences.get(c).confTeams.size(); ++g) {
                            Team a = conferences.get(c).confTeams.get(g);
                            a.gameSchedule.add(new Game(a, bye, "BYE WEEK"));
                        }
                    }
                }
            }

            if(largeOddConf > 0) {
                for (int c = 0; c < conferences.size(); c++) {
                    if (conferences.get(c).confTeams.size() < conferences.get(c).minConfTeams) {
                        Team bye = new Team("BYE", "BYE", "BYE", 0, "BYE", 0, this);
                        bye.rankTeamPollScore = teamList.size();
                        for (int g = 0; g < conferences.get(c).confTeams.size(); ++g) {
                            Team a = conferences.get(c).confTeams.get(g);
                            a.gameSchedule.add(new Game(a, bye, "BYE WEEK"));
                        }
                    }
                }
            }

        }

        confAvg = getAverageConfPrestige();

        // Initialize new stories lists
        newsStories = new ArrayList<>();
        weeklyScores = new ArrayList<>();
        for (int i = 0; i < seasonWeeks; ++i) {
            newsStories.add(new ArrayList<String>());
            weeklyScores.add(new ArrayList<String>());
        }
        newsStories.get(0).add("New Season!>Ready for the new season, coach? Whether the National Championship is " +
                "on your mind, or just a winning season, good luck!");
        weeklyScores.get(0).add("Scores:>No games this week.");

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < conferences.size(); i++) {
            if (conferences.get(i).confTeams.size() >= conferences.get(i).minConfTeams) {
                sb.append(conferences.get(i).confName + ":  " + conferences.get(i).confPrestige + "\n");
            }
        }

        newsStories.get(0).add("Conference Prestige>The latest surveys are in. The " + getYear() + " prestige ratings for each conference are:\n\n" + sb);

        penalizeTeams();

        sb = new StringBuilder();
        for (int i = 0; i < teamList.size(); i++) {
            if (teamList.get(i).bowlBan) {
                sb.append(teamList.get(i).name + "\n");
            }
        }

        if (sb.length() > 0)
            newsStories.get(0).add("Post-Season Ban!>These teams have seen numerous violations pile up and have lost the patiences of the College Football Administration. These teams will see reduced scholarships (loss of prestige), and post-season bans!\n\n" + sb);

        sb = new StringBuilder();
        for (int i = 0; i < teamList.size(); i++) {
            if (teamList.get(i).penalized) {
                sb.append(teamList.get(i).name + "\n");
            }
        }

        if (sb.length() > 0)
            newsStories.get(0).add("Minor Infractions!>The following teams have been fined by the College Football Administration for a minor infractions related to discplinary concerns surrounding the school:\n\n" + sb);

        upgradeFacilities();

        sb = new StringBuilder();
        for (int i = 0; i < teamList.size(); i++) {
            if (teamList.get(i).facilityUpgrade) {
                sb.append(teamList.get(i).name + " : Level " + teamList.get(i).teamFacilities + "\n");
            }
        }

        if (sb.length() > 0)
            newsStories.get(0).add("Upgraded Facilities!>The following teams upgraded their team training facilities this off-season:\n\n" + sb);

    }

    /**
     * Gets whether it is hard mode.
     * Returns true is hard, false if normal.
     *
     * @return difficulty
     */
    public boolean isCareerMode() {
        return careerMode;
    }

    //Set Up Database of Names
    private void setupNamesDB(String namesCSV, String lastNamesCSV) {
        // Read first names from file
        String[] namesSplit = namesCSV.split(",");
        for (String n : namesSplit) {
            if (isNameValid(n.trim()))
                nameList.add(n.trim());
        }

        // Read last names from file
        namesSplit = lastNamesCSV.split(",");
        for (String n : namesSplit) {
            if (isNameValid(n.trim()))
                lastNameList.add(n.trim());
        }
    }

    /**
     * Gets a random player name.
     *
     * @return random name
     */
    public String getRandName() {
        String name;
        int fn = (int) (Math.random() * nameList.size());
        int ln = (int) (Math.random() * lastNameList.size());
        name = nameList.get(fn) + " " + lastNameList.get(ln);
        return name;
    }

    //Set Up Team Benchmarks for Goals
    public void setTeamBenchMarks() {
        setTeamRanks();

        for (int i = 0; i < teamList.size(); ++i) {
            teamList.get(i).setupTeamBenchmark();
        }

        for (int i = 0; i < teamList.size(); ++i) {
            teamList.get(i).projectTeamWins();
            teamList.get(i).projectPollRank();
        }

        Collections.sort(teamList, new CompTeamProjPoll());
        for (int i = 0; i < teamList.size(); ++i) {
            teamList.get(i).projectedPollRank = i + 1;
        }

        leagueOffTal = getAverageOffTalent();
        leagueDefTal = getAverageDefTalent();
        leagueChemistry = getAverageTeamChemistry();
    }

    /**
     * Gets the current year, starting from 2017
     *
     * @return the current year
     */
    public int getYear() {
        return seasonStart + leagueHistory.size();
    }

    //Return homeState name
    public String getRegion(int region) {
        String location;
        if (region == 0) location = "West";
        else if (region == 1) location = "Midwest";
        else if (region == 2) location = "Central";
        else if (region == 3) location = "East";
        else location = "South";
        return location;
    }

    /**
     * Get list of teams and their prestige, used for selecting when a new game is started
     *
     * @return array of all the teams
     */
    public String[] getTeamListStr() {
        String[] teams = new String[teamList.size()];
        for (int i = 0; i < teamList.size(); ++i) {
            teams[i] = teamList.get(i).conference + ":  " + teamList.get(i).name + "  [" + teamList.get(i).teamPrestige + "]";
        }
        return teams;
    }

    /**
     * Find team based on a name
     *
     * @param name team name
     * @return reference to the Team object
     */
    public Team findTeam(String name) {
        for (int i = 0; i < teamList.size(); i++) {
            if (teamList.get(i).strRep().equals(name)) {
                return teamList.get(i);
            }
        }
        return teamList.get(0);
    }

    /**
     * Find team based on a abbr
     *
     * @param abbr team abbr
     * @return reference to the Team object
     */
    public Team findTeamAbbr(String abbr) {
        for (int i = 0; i < teamList.size(); i++) {
            if (teamList.get(i).abbr.equals(abbr)) {
                return teamList.get(i);
            }
        }
        return teamList.get(0);
    }

    /**
     * Find conference based on a name
     *
     * @param name conf name
     * @return reference to the Conference object
     */
    public Conference findConference(String name) {
        for (int i = 0; i < teamList.size(); i++) {
            if (conferences.get(i).confName.equals(name)) {
                return conferences.get(i);
            }
        }
        return conferences.get(0);
    }

    public void updateTeamConf(String newConf, String oldConf, int x) {
        for (int i = 0; i < teamList.size(); i++) {
            if (teamList.get(i).conference.equals(oldConf)) {
                teamList.get(i).conference = newConf;
            }
        }
        conferences.get(x).confName = newConf;
    }

    /**
     * Get conference nmber from string
     *
     * @param conf conference name
     * @return int of number 0-5
     */
    public int getConfNumber(String conf) {
        boolean complete = false;
        int i = 0;
        while (complete == false) {
            if (conf.equals(conferences.get(i).confName)) {
                return i;
            } else i++;
        }
        return i;
    }

    /**
     * Update all teams off talent, def talent, etc
     */
    public void updateTeamTalentRatings() {
        for (Team t : teamList) {
            t.updateTalentRatings();
        }
        for (Conference c : conferences) {
            c.updateConfRankings();
        }
    }

    //Return League Average Off Yards
    public int getAverageYards() {
        int average = 0;
        for (int i = 0; i < teamList.size(); ++i) {
            average += teamList.get(i).teamYards;
        }
        average = average / teamList.size();
        return average;
    }

    //Return league average offensive talent
    public int getAverageOffTalent() {
        int average = 0;
        for (int i = 0; i < teamList.size(); ++i) {
            average += teamList.get(i).getOffTalent();
        }
        average = average / teamList.size();
        return average;
    }

    //Return league average defensive talent
    public int getAverageDefTalent() {
        int average = 0;
        for (int i = 0; i < teamList.size(); ++i) {
            average += teamList.get(i).getDefTalent();
        }
        average = average / teamList.size();
        return average;
    }

    //Determine League Average Conference Prestige
    public int getAverageConfPrestige() {
        int avgPrestige = 0;
        for (int i = 0; i < conferences.size(); ++i) {
            conferences.get(i).updateConfPrestige();
        }
        for (int i = 0; i < conferences.size(); ++i) {
            avgPrestige += conferences.get(i).confPrestige;
        }
        return avgPrestige / conferences.size();
    }

    //get League Avg Chemistry
    public double getAverageTeamChemistry() {
        double avg = 0;
        for (int i = 0; i < teamList.size(); ++i) {
            avg += teamList.get(i).teamChemistry;
        }
        return avg / teamList.size();
    }


    //News on opening weekend
    public void preseasonNews() {
        coachingHotSeat();

        topRecruits();

        //Add Big Games of the Week
        for (int i = 0; i < conferences.size(); ++i) {
            conferences.get(i).newsNSMatchups();
        }
    }

    //Get a list of Top Recruits for News
    public void topRecruits() {
        for (int i = 0; i < teamList.size(); ++i) {
            if (teamList.get(i) != userTeam) {
                teamList.get(i).redshirtCPUPlayers();
            }
            teamList.get(i).getLeagueFreshman();
        }
        Collections.sort(freshmen, new CompPlayer());
        Collections.sort(redshirts, new CompPlayer());

        StringBuilder newsFreshman = new StringBuilder();
        for (int i = 0; i < 25; ++i) {
            newsFreshman.append((i + 1) + ". " + freshmen.get(i).position + " " + freshmen.get(i).name + ", " + freshmen.get(i).team.name + " : Ovr: " + freshmen.get(i).ratOvr + "\n\n");
        }
        StringBuilder newsRedshirts = new StringBuilder();
        for (int i = 0; i < 25; ++i) {
            newsRedshirts.append((i + 1) + ". " + redshirts.get(i).position + " " + redshirts.get(i).name + ", " + redshirts.get(i).team.name + " : Ovr: " + freshmen.get(i).ratOvr + "\n\n");
        }

        newsStories.get(0).add("Impact Freshmen>This year's top freshmen who are expected to play right away:\n\n" + newsFreshman);
        newsStories.get(0).add("Top Incoming Redshirted Recruits>The following list is this year's top redshirts. Their respective teams decided to sit them out this season, in hopes of progressing their talent further for next year.\n\n" + newsRedshirts);
    }

    public void penalizeTeams() {


        //Infractions v2
        //Based closer to NCAA Football series
        //If Discipline score drops below threshold, penalties ensue

        for (int i = 0; i < teamList.size(); i++) {
            if (teamList.get(i).teamDisciplineScore < 30 && !teamList.get(i).recentPenalty) {
                teamList.get(i).penalized = true;
                teamList.get(i).recentPenalty = true;
                teamList.get(i).teamPrestige -= teamList.get(i).teamPrestige * 0.15;
                teamList.get(i).teamBudget -= teamList.get(i).teamBudget * 0.15;
            } else if (teamList.get(i).teamDisciplineScore <= 0) {
                teamList.get(i).bowlBan = true;
                teamList.get(i).teamPrestige -= teamList.get(i).teamPrestige * 0.35;
                teamList.get(i).teamBudget -= teamList.get(i).teamBudget * 0.35;
                teamList.get(i).teamDisciplineScore = 50;
                teamList.get(i).penalized = false;
                teamList.get(i).recentPenalty = false;
            }
        }
    }

    public void upgradeFacilities() {


        //Team Facilities Upgrade -- if teams have enough cash, they will spend on this. helps progression of players
        int baselineCost = 15000;
        for (int i = 0; i < teamList.size(); i++) {
            if (teamList.get(i).teamBudget > baselineCost * (teamList.get(i).teamFacilities + 1)) {
                //spend cash, upgrade facilities
                teamList.get(i).teamBudget -= baselineCost * (teamList.get(i).teamFacilities + 1);
                teamList.get(i).facilityUpgrade = true;
                teamList.get(i).teamFacilities++;
            }
        }
    }

    //Simulates Each Game Week
    public void playWeek() {
        //focus on the best player at a position this year each week
        playerSpotlight();

        updateSuspensions();

        disciplineAction();

        //Clear "next week" scoreboard blank data
        weeklyScores.get(currentWeek+1).clear();

        if (currentWeek <= regSeasonWeeks-1) {
            for (int i = 0; i < conferences.size(); ++i) {
                conferences.get(i).playWeek();
            }
        }

        if (currentWeek == regSeasonWeeks-1) {
            //bowl week
            for (int i = 0; i < teamList.size(); ++i) {
                teamList.get(i).updatePollScore();
            }
            Collections.sort(teamList, new CompTeamPoll());

            if (expPlayoffs) expandedPlayoff();
                //else schedBowlGames();
            else bowlScheduleLogic();

        } else if (currentWeek == regSeasonWeeks) {
            ArrayList<Player> heismans = getHeisman();
            heismanHistory.add(heismans.get(0).position + " " + heismans.get(0).getInitialName() + " [" + heismans.get(0).getYrStr() + "], "
                    + heismans.get(0).team.abbr + " (" + heismans.get(0).team.wins + "-" + heismans.get(0).team.losses + ")>" +
                    defPOTYCandidates.get(0).position + " " + defPOTYCandidates.get(0).getInitialName() + " [" + defPOTYCandidates.get(0).getYrStr() + "], "
                    + defPOTYCandidates.get(0).team.abbr + " (" + defPOTYCandidates.get(0).team.wins + "-" + defPOTYCandidates.get(0).team.losses + ")");

            if (expPlayoffs) playExpPlayoffSweet16();
            else playBowlWeek1();

        } else if (currentWeek == regSeasonWeeks+1) {
            if (expPlayoffs) playExpPlayoffQTF();
            else playBowlWeek2();

        } else if (currentWeek == regSeasonWeeks+2) {
            if (expPlayoffs) playExpPlayoffSemi();
            else playBowlWeek3();

        } else if (currentWeek == regSeasonWeeks+3) {
            ncg.playGame();
            if (ncg.homeScore > ncg.awayScore) {
                ncg.homeTeam.semiFinalWL = "";
                ncg.awayTeam.semiFinalWL = "";
                ncg.homeTeam.natChampWL = "NCW";
                ncg.awayTeam.natChampWL = "NCL";
                ncg.homeTeam.totalNCs++;
                ncg.awayTeam.totalNCLosses++;
                ncg.homeTeam.HC.get(0).bowlwins++;
                ncg.homeTeam.HC.get(0).natchamp++;
                ncg.awayTeam.HC.get(0).bowllosses++;
                newsStories.get(currentWeek + 1).add(
                        ncg.homeTeam.name + " wins the National Championship!>" +
                                ncg.homeTeam.name + " defeats " + ncg.awayTeam.name +
                                " in the national championship game " + ncg.homeScore + " to " + ncg.awayScore + "." +
                                " Congratulations " + ncg.homeTeam.name + "!"
                );

            } else {
                ncg.homeTeam.semiFinalWL = "";
                ncg.awayTeam.semiFinalWL = "";
                ncg.awayTeam.natChampWL = "NCW";
                ncg.homeTeam.natChampWL = "NCL";
                ncg.awayTeam.totalNCs++;
                ncg.homeTeam.totalNCLosses++;
                ncg.awayTeam.HC.get(0).bowlwins++;
                ncg.awayTeam.HC.get(0).natchamp++;
                ncg.homeTeam.HC.get(0).bowllosses++;
                newsStories.get(currentWeek + 1).add(
                        ncg.awayTeam.name + " wins the National Championship!>" +
                                ncg.awayTeam.name + " defeats " + ncg.homeTeam.name +
                                " in the national championship game " + ncg.awayScore + " to " + ncg.homeScore + "." +
                                " Congratulations " + ncg.awayTeam.name + "!"
                );
            }
        }
        //add news regarding CFB Playoff Committee
        cfbPlayoffsNews();

        //add upcoming matches of the week
        for (int i = 0; i < conferences.size(); ++i) {
            conferences.get(i).newsMatchups();
        }


        coachingHotSeat();


        setTeamRanks();
        updateLongestActiveWinStreak();

        currentWeek++;
    }


    //Coaching Discipline Opportunities
    private void disciplineAction() {
        teamDiscipline = new ArrayList<>();
        String news = "";
        for (int t = 0; t < teamList.size(); ++t) {
            if (Math.random() < disciplineChance) {
                int teamDis = teamList.get(t).getTeamDiscipline();
                if ((int) (Math.random() * (100 - teamDis)) > (int) (Math.random() * teamList.get(t).HC.get(0).ratDiscipline)) {
                    teamList.get(t).HC.get(0).ratDiscipline -= (int) (Math.random() * 5);
                    teamList.get(t).disciplinePts -= (int) (Math.random() * 3);
                    teamDiscipline.add(teamList.get(t).name);
                    teamList.get(t).disciplinePlayer();
                    teamList.get(t).teamDisciplineScore -= ((int) (Math.random() * 7) + 3);
                    teamList.get(t).teamBudget -= ((int) (Math.random() * 10 * 100));
                } else {
                    teamList.get(t).HC.get(0).ratDiscipline += (int) (Math.random() * 3);
                    teamList.get(t).teamDisciplineScore += (int) (Math.random() * 4);
                    if (teamList.get(t).teamDisciplineScore > 99)
                        teamList.get(t).teamDisciplineScore = 99;
                }
            }
        }
        for (int i = 0; i < teamDiscipline.size(); ++i) {
            news += "\n" + teamDiscipline.get(i);
        }
        newsStories.get(currentWeek + 1).add("In-Season Disciplinary Action>The following teams have had issues with discipline in the past week:\n" + news);
    }

    private void updateSuspensions() {
        for (int i = 0; i < teamList.size(); ++i) {
            teamList.get(i).updateSuspensions();
        }
    }

    //Player Spotlight
    private void playerSpotlight() {
        ArrayList<PlayerQB> QB = rankQB();
        ArrayList<PlayerRB> RB = rankRB();
        ArrayList<PlayerWR> WR = rankWR();
        ArrayList<PlayerDL> DL = rankDL();
        ArrayList<PlayerLB> LB = rankLB();
        ArrayList<PlayerCB> CB = rankCB();
        ArrayList<PlayerS> S = rankS();
        if (currentWeek == 5) {
            newsStories.get(currentWeek + 1).add("Player Spotlight>" + S.get(0).getYrStr() + " safety, " + S.get(0).name + ", has been cleaning up in the back this year helping " + S.get(0).team.name +
                    " to a record of " + S.get(0).team.strTeamRecord() + ". The safety has made " + S.get(0).statsTackles + " tackles and sacked the QB " + S.get(0).statsSacks + " times this year. In coverage, he's recovered " +
                    S.get(0).statsFumbles + " fumbles and intercepted opposing QBs " + S.get(0).statsInts + " times this year. Look for him to be in the year end running for Player of the Year.");
        } else if (currentWeek == 6) {
            newsStories.get(currentWeek + 1).add("Player Spotlight>" + QB.get(0).getYrStr() + " quarterback, " + QB.get(0).name + ", is one of the top players at his position in the nation this year. He has led " + QB.get(0).team.name +
                    " to a record of " + QB.get(0).team.strTeamRecord() + ". He has passed for " + QB.get(0).statsPassYards + " yards this season, and thrown " + QB.get(0).statsPassTD + " touchdowns. " +
                    "He's also carried the ball for " + QB.get(0).statsRushYards + " yards this season. Look for him to be in the year end running for Player of the Year.");
        } else if (currentWeek == 7) {
            newsStories.get(currentWeek + 1).add("Player Spotlight>" + WR.get(0).getYrStr() + " wide receiver, " + WR.get(0).name + ", has been flying pass defensive coverages this year helping " + WR.get(0).team.name +
                    " to a record of " + WR.get(0).team.strTeamRecord() + ". The receiver has caught " + WR.get(0).statsReceptions + " for " + WR.get(0).statsRecYards + " yards this year. He's found the end zone " + WR.get(0).statsRecTD +
                    " times. Look for him to be in the year end running for Player of the Year.");
        } else if (currentWeek == 8) {
            newsStories.get(currentWeek + 1).add("Player Spotlight>" + LB.get(0).getYrStr() + " linebacker, " + LB.get(0).name + ", has been blowing up offenses this year helping " + LB.get(0).team.name +
                    " to a record of " + LB.get(0).team.strTeamRecord() + ". The linebacker has made " + LB.get(0).statsTackles + " tackles and sacked the QB " + LB.get(0).statsSacks + " times this year. In coverage, he's recovered " +
                    LB.get(0).statsFumbles + " fumbles and intercepted opposing QBs " + LB.get(0).statsInts + " times this year. Look for him to be in the year end running for Player of the Year.");
        } else if (currentWeek == 9) {
            newsStories.get(currentWeek + 1).add("Player Spotlight>" + DL.get(0).getYrStr() + " defensive lineman, " + DL.get(0).name + ", has been disrupting offensive lines this year helping " + DL.get(0).team.name +
                    " to a record of " + DL.get(0).team.strTeamRecord() + ". The lineman has made " + DL.get(0).statsTackles + " tackles and sacked the QB " + DL.get(0).statsSacks + " times this year.He's also recovered " +
                    DL.get(0).statsFumbles + " fumbles this year. Look for him to be in the year end running for Player of the Year.");
        } else if (currentWeek == 10) {
            newsStories.get(currentWeek + 1).add("Player Spotlight>" + RB.get(0).getYrStr() + " running back, " + RB.get(0).name + ", has been finding holes in opposing defenses this season for " + RB.get(0).team.name +
                    " as they compiled a record of " + RB.get(0).team.strTeamRecord() + ". The running back has rushed for " + RB.get(0).statsRushYards + " yards and scored " + RB.get(0).statsRushTD + " times this year. " +
                    "In the passing game, he's caught " + RB.get(0).statsReceptions + " for " + RB.get(0).statsRecYards + " and scored " + RB.get(0).statsRecTD + " touchdowns in the air this year. " +
                    "Look for him to be in the year end running for Player of the Year.");
        } else if (currentWeek == 11) {
            newsStories.get(currentWeek + 1).add("Player Spotlight>" + CB.get(0).getYrStr() + " cornerback, " + CB.get(0).name + ", has been shutting down opposing receivers this year helping " + CB.get(0).team.name +
                    " to a record of " + CB.get(0).team.strTeamRecord() + ". The corner has made " + CB.get(0).statsTackles + " tackles and sacked the QB " + CB.get(0).statsSacks + " times this year. In coverage, he's recovered " +
                    CB.get(0).statsFumbles + " fumbles and intercepted opposing QBs " + CB.get(0).statsInts + " times this year. Look for him to be in the year end running for Player of the Year.");
        }
    }

    //Committee News
    private void cfbPlayoffsNews() {
        setTeamRanks();
        ArrayList<Team> teams = teamList;
        Collections.sort(teams, new CompTeamPoll());

        if (currentWeek == 8) {
            newsStories.get(currentWeek + 1).add("Committee Announces First Playoff Rankings>The College Football Playoffs Committee has set ther initial rankings for this season's playoffs. The first look at the playoffs have " +
                    teams.get(0).name + " at the top of the list. The rest of the playoff order looks like this:\n\n" + "1. " + teams.get(0).getStrAbbrWL() + "\n" + "2. " + teams.get(1).getStrAbbrWL() + "\n" + "3. " +
                    teams.get(2).getStrAbbrWL() + "\n" + "4. " + teams.get(3).getStrAbbrWL() + "\n" + "5. " + teams.get(4).getStrAbbrWL() + "\n" + "6. " + teams.get(5).getStrAbbrWL() + "\n" + "7. " +
                    teams.get(6).getStrAbbrWL() + "\n" + "8. " + teams.get(7).getStrAbbrWL() + "\n");
        }
        if (currentWeek > 8 && currentWeek < regSeasonWeeks-1) {
            newsStories.get(currentWeek + 1).add("Committee Updates Rankings>The College Football Playoff Committee has updated their Playoff Rankings. The order looks like this: \n\n" + "1. " + teams.get(0).getStrAbbrWL() +
                    "\n" + "2. " + teams.get(1).getStrAbbrWL() + "\n" + "3. " + teams.get(2).getStrAbbrWL() + "\n" + "4. " + teams.get(3).getStrAbbrWL() + "\n" + "5. " + teams.get(4).getStrAbbrWL() + "\n" + "6. " +
                    teams.get(5).getStrAbbrWL() + "\n" + "7. " + teams.get(6).getStrAbbrWL() + "\n" + "8. " + teams.get(7).getStrAbbrWL() + "\n");
        }
    }


    /**
     * Calculates who wins the Heisman.
     *
     * @return Heisman Winner
     */
    private ArrayList<Player> getHeisman() {
        heisman = null;
        int heismanScore = 0;
        int tempScore = 0;
        ArrayList<Player> heismanCandidates = new ArrayList<>();
        for (int i = 0; i < teamList.size(); ++i) {
            //qb
            for (int qb = 0; qb < teamList.get(i).teamQBs.size(); ++qb) {
                heismanCandidates.add(teamList.get(i).teamQBs.get(qb));
                tempScore = teamList.get(i).teamQBs.get(qb).getHeismanScore() + teamList.get(i).wins * 100 + teamList.get(i).confPrestige * 10;
                if (tempScore > heismanScore) {
                    heisman = teamList.get(i).teamQBs.get(qb);
                    heismanScore = tempScore;
                }
            }

            //rb
            for (int rb = 0; rb < teamList.get(i).teamRBs.size(); ++rb) {
                heismanCandidates.add(teamList.get(i).teamRBs.get(rb));
                tempScore = teamList.get(i).teamRBs.get(rb).getHeismanScore() + teamList.get(i).wins * 100 + teamList.get(i).confPrestige * 10;
                if (tempScore > heismanScore) {
                    heisman = teamList.get(i).teamRBs.get(rb);
                    heismanScore = tempScore;
                }
            }

            //wr
            for (int wr = 0; wr < teamList.get(i).teamWRs.size(); ++wr) {
                heismanCandidates.add(teamList.get(i).teamWRs.get(wr));
                tempScore = teamList.get(i).teamWRs.get(wr).getHeismanScore() + teamList.get(i).wins * 100 + teamList.get(i).confPrestige * 10;
                if (tempScore > heismanScore) {
                    heisman = teamList.get(i).teamWRs.get(wr);
                    heismanScore = tempScore;
                }
            }

            //te
            for (int te = 0; te < teamList.get(i).teamTEs.size(); ++te) {
                heismanCandidates.add(teamList.get(i).teamTEs.get(te));
                tempScore = teamList.get(i).teamTEs.get(te).getHeismanScore() + teamList.get(i).wins * 100 + teamList.get(i).confPrestige * 10;
                if (tempScore > heismanScore) {
                    heisman = teamList.get(i).teamTEs.get(te);
                    heismanScore = tempScore;
                }
            }
        }
        Collections.sort(heismanCandidates, new CompPlayerHeisman());

        return heismanCandidates;
    }

    /**
     * Perform the heisman ceremony. Congratulate winner and give top 5 vote getters.
     *
     * @return string of the heisman ceremony.
     */
    public String getHeismanCeremonyStr() {
        boolean putNewsStory = false;
        if (!heismanDecided) {
            heismanDecided = true;
            heismanCandidates = getHeisman();
            heisman = heismanCandidates.get(0);
            heisman.wonHeisman = true;
            putNewsStory = true;
            //full results string
            String heismanTop5 = "\n";
            for (int i = 0; i < 5; ++i) {
                Player p = heismanCandidates.get(i);
                if (p instanceof PlayerQB) {
                    PlayerQB pqb = (PlayerQB) p;
                    heismanTop5 += (i + 1) + ". QB " + pqb.name + ": " + p.getHeismanScore() + " votes\n\t\t"
                            + pqb.team.strRankTeamRecord() + " - " + pqb.getYrStr() + "\n\t\t"
                            + pqb.statsPassTD + " TDs, " + pqb.statsInt + " Int, " + pqb.statsPassYards + " Yds, "
                            + df2.format(pqb.getPasserRating()) + " QBR\n\n";
                } else if (p instanceof PlayerRB) {
                    PlayerRB prb = (PlayerRB) p;
                    heismanTop5 += (i + 1) + ". RB " + prb.name + ": " + p.getHeismanScore() + " votes\n\t\t"
                            + prb.team.strRankTeamRecord() + " - " + prb.getYrStr() + "\n\t\t"
                            + prb.statsRushTD + " TDs, " + prb.statsFumbles + " Fum, " + prb.statsRushYards + " Yds\n\n";
                } else if (p instanceof PlayerWR) {
                    PlayerWR pwr = (PlayerWR) p;
                    heismanTop5 += (i + 1) + ". WR " + pwr.name + ": " + p.getHeismanScore() + " votes\n\t\t"
                            + pwr.team.strRankTeamRecord() + " - " + pwr.getYrStr() + "\n\t\t"
                            + pwr.statsRecTD + " TDs, " + pwr.statsFumbles + " Fum, " + pwr.statsRecYards + " Yds\n\n";
                } else if (p instanceof PlayerTE) {
                    PlayerTE pte = (PlayerTE) p;
                    heismanTop5 += (i + 1) + ". TE " + pte.name + ": " + p.getHeismanScore() + " votes\n\t\t"
                            + pte.team.strRankTeamRecord() + " - " + pte.getYrStr() + "\n\t\t"
                            + pte.statsRecTD + " TDs, " + pte.statsFumbles + " Fum, " + pte.statsRecYards + " Yds\n\n";
                }
            }

            String heismanStats = "";
            String heismanWinnerStr = "";
            if (heisman instanceof PlayerQB) {
                //qb heisman
                PlayerQB heisQB = (PlayerQB) heisman;
                heismanWinnerStr = "Congratulations to the Offensive Player of the Year, " + heisQB.team.name +
                        " QB " + heisQB.name + " [" + heisman.getYrStr() + "], who had " +
                        heisQB.statsPassTD + " TDs, just " + heisQB.statsInt + " interceptions, and " +
                        heisQB.statsPassYards + " passing yards. In addition, he ran for " + heisQB.statsRushYards + " rushing yards and scored " + heisQB.statsRushTD + " touchdowns. He led " + heisQB.team.name +
                        " to a " + heisQB.team.wins + "-" + heisQB.team.losses + " record and a #" + heisQB.team.rankTeamPollScore +
                        " poll ranking.";
                heismanStats = heismanWinnerStr + "\n\nFull Results:" + heismanTop5;
            } else if (heisman instanceof PlayerRB) {
                //rb heisman
                PlayerRB heisRB = (PlayerRB) heisman;
                heismanWinnerStr = "Congratulations to the Offensive Player of the Year, " + heisRB.team.name +
                        " RB " + heisRB.name + " [" + heisman.getYrStr() + "], who had " +
                        heisRB.statsRushTD + " TDs, just " + heisRB.statsFumbles + " fumbles, and " +
                        heisRB.statsRushYards + " rushing yards. He led " + heisRB.team.name +
                        " to a " + heisRB.team.wins + "-" + heisRB.team.losses + " record and a #" + heisRB.team.rankTeamPollScore +
                        " poll ranking.";
                heismanStats = heismanWinnerStr + "\n\nFull Results:" + heismanTop5;
            } else if (heisman instanceof PlayerWR) {
                //wr heisman
                PlayerWR heisWR = (PlayerWR) heisman;
                heismanWinnerStr = "Congratulations to the Offensive Player of the Year, " + heisWR.team.name +
                        " WR " + heisWR.name + " [" + heisman.getYrStr() + "], who had " +
                        heisWR.statsRecTD + " TDs, just " + heisWR.statsFumbles + " fumbles, and " +
                        heisWR.statsRecYards + " receiving yards. He led " + heisWR.team.name +
                        " to a " + heisWR.team.wins + "-" + heisWR.team.losses + " record and a #" + heisWR.team.rankTeamPollScore +
                        " poll ranking.";
                heismanStats = heismanWinnerStr + "\n\nFull Results:" + heismanTop5;
            } else if (heisman instanceof PlayerTE) {
                //te heisman
                PlayerTE heisTE = (PlayerTE) heisman;
                heismanWinnerStr = "Congratulations to the Offensive Player of the Year, " + heisTE.team.name +
                        " TE " + heisTE.name + " [" + heisman.getYrStr() + "], who had " +
                        heisTE.statsRecTD + " TDs, just " + heisTE.statsFumbles + " fumbles, and " +
                        heisTE.statsRecYards + " receiving yards. He led " + heisTE.team.name +
                        " to a " + heisTE.team.wins + "-" + heisTE.team.losses + " record and a #" + heisTE.team.rankTeamPollScore +
                        " poll ranking.";
                heismanStats = heismanWinnerStr + "\n\nFull Results:" + heismanTop5;
            }

            // Add news story
            if (putNewsStory) {
                newsStories.get(currentWeek + 1).add(heisman.name + " is the Offensive Player of the Year!>" + heismanWinnerStr);
                heismanWinnerStrFull = heismanStats;
            }

            return heismanStats;
        } else {
            return heismanWinnerStrFull;
        }
    }

    /**
     * Calculates who wins the Heisman.
     *
     * @return Heisman Winner
     */
    private ArrayList<Player> getDefPOTY() {
        heisman = null;
        int heismanScore = 0;
        int tempScore = 0;
        ArrayList<Player> heismanCandidates = new ArrayList<>();
        for (int i = 0; i < teamList.size(); ++i) {
            //dl
            for (int dl = 0; dl < teamList.get(i).teamDLs.size(); ++dl) {
                heismanCandidates.add(teamList.get(i).teamDLs.get(dl));
                tempScore = teamList.get(i).teamDLs.get(dl).getHeismanScore() + teamList.get(i).wins * 100 + teamList.get(i).confPrestige * 10;
                if (tempScore > heismanScore) {
                    heisman = teamList.get(i).teamDLs.get(dl);
                    heismanScore = tempScore;
                }
            }

            //lb
            for (int lb = 0; lb < teamList.get(i).teamLBs.size(); ++lb) {
                heismanCandidates.add(teamList.get(i).teamLBs.get(lb));
                tempScore = teamList.get(i).teamLBs.get(lb).getHeismanScore() + teamList.get(i).wins * 100 + teamList.get(i).confPrestige * 10;
                if (tempScore > heismanScore) {
                    heisman = teamList.get(i).teamLBs.get(lb);
                    heismanScore = tempScore;
                }
            }

            //cb
            for (int cb = 0; cb < teamList.get(i).teamCBs.size(); ++cb) {
                heismanCandidates.add(teamList.get(i).teamCBs.get(cb));
                tempScore = teamList.get(i).teamCBs.get(cb).getHeismanScore() + teamList.get(i).wins * 100 + teamList.get(i).confPrestige * 10;
                if (tempScore > heismanScore) {
                    heisman = teamList.get(i).teamCBs.get(cb);
                    heismanScore = tempScore;
                }
            }

            //s
            for (int s = 0; s < teamList.get(i).teamSs.size(); ++s) {
                heismanCandidates.add(teamList.get(i).teamSs.get(s));
                tempScore = teamList.get(i).teamSs.get(s).getHeismanScore() + teamList.get(i).wins * 100 + teamList.get(i).confPrestige * 10;
                if (tempScore > heismanScore) {
                    heisman = teamList.get(i).teamSs.get(s);
                    heismanScore = tempScore;
                }
            }
        }
        Collections.sort(heismanCandidates, new CompPlayerHeisman());

        return heismanCandidates;
    }

    /**
     * Perform the heisman ceremony. Congratulate winner and give top 5 vote getters.
     *
     * @return string of the heisman ceremony.
     */
    public String getDefensePOTYStr() {
        boolean putNewsStory = false;
        if (!heismanDecided) {
            defPOTYCandidates = getDefPOTY();
            defPOTY = defPOTYCandidates.get(0);
            defPOTY.wonHeisman = true;
            putNewsStory = true;
            //full results string
            String heismanTop5 = "\n";
            for (int i = 0; i < 5; ++i) {
                Player p = defPOTYCandidates.get(i);
                if (p instanceof PlayerDL) {
                    PlayerDL pdl = (PlayerDL) p;
                    heismanTop5 += (i + 1) + ". DL " + pdl.name + ": " + p.getHeismanScore() + " votes\n\t\t"
                            + pdl.team.strRankTeamRecord() + " - " + pdl.getYrStr() + "\n\t\t"
                            + pdl.statsTackles + " Tkl, " + pdl.statsSacks + " Sacks, " + pdl.statsFumbles + " Fum\n\n";
                } else if (p instanceof PlayerLB) {
                    PlayerLB plb = (PlayerLB) p;
                    heismanTop5 += (i + 1) + ". LB " + plb.name + ": " + p.getHeismanScore() + " votes\n\t\t"
                            + plb.team.strRankTeamRecord() + " - " + plb.getYrStr() + "\n\t\t"
                            + plb.statsTackles + " Tkl, " + plb.statsFumbles + " Fum, " + plb.statsInts + " Int\n\n";
                } else if (p instanceof PlayerCB) {
                    PlayerCB pcb = (PlayerCB) p;
                    heismanTop5 += (i + 1) + ". CB " + pcb.name + ": " + p.getHeismanScore() + " votes\n\t\t"
                            + pcb.team.strRankTeamRecord() + " - " + pcb.getYrStr() + "\n\t\t"
                            + pcb.statsTackles + " Tkl, " + pcb.statsDefended + " Def, " + pcb.statsInts + " Int\n\n";
                } else if (p instanceof PlayerS) {
                    PlayerS ps = (PlayerS) p;
                    heismanTop5 += (i + 1) + ". S " + ps.name + ": " + p.getHeismanScore() + " votes\n\t\t"
                            + ps.team.strRankTeamRecord() + " - " + ps.getYrStr() + "\n\t\t"
                            + ps.statsTackles + " Tkl, " + ps.statsFumbles + " Fum, " + ps.statsInts + " Int\n\n";
                }
            }

            String heismanStats = "";
            String heismanWinnerStr = "";
            if (defPOTY instanceof PlayerDL) {
                //dl heisman
                PlayerDL heisDL = (PlayerDL) defPOTY;
                heismanWinnerStr = "Congratulations to the Defensive Player of the Year, " + heisDL.team.name +
                        " DL " + heisDL.name + " [" + defPOTY.getYrStr() + "], who had " +
                        heisDL.statsTackles + " tackles, " + heisDL.statsSacks + " sacks, and forced " + heisDL.statsFumbles + " fumbles. He led " + heisDL.team.name +
                        " to a " + heisDL.team.wins + "-" + heisDL.team.losses + " record and a #" + heisDL.team.rankTeamPollScore +
                        " poll ranking.";
                heismanStats = heismanWinnerStr + "\n\nFull Results:" + heismanTop5;
            } else if (defPOTY instanceof PlayerLB) {
                //wr heisman
                PlayerLB heisLB = (PlayerLB) defPOTY;
                heismanWinnerStr = "Congratulations to the Defensive Player of the Year, " + heisLB.team.name +
                        " LB " + heisLB.name + " [" + defPOTY.getYrStr() + "], who had " +
                        heisLB.statsTackles + " tackles, " + heisLB.statsSacks + " sacks, and recovered " + heisLB.statsFumbles + " fumbles, and " +
                        heisLB.statsInts + " inteceptions. He led " + heisLB.team.name +
                        " to a " + heisLB.team.wins + "-" + heisLB.team.losses + " record and a #" + heisLB.team.rankTeamPollScore +
                        " poll ranking.";
                heismanStats = heismanWinnerStr + "\n\nFull Results:" + heismanTop5;
            } else if (defPOTY instanceof PlayerCB) {
                //wr heisman
                PlayerCB heisCB = (PlayerCB) defPOTY;
                heismanWinnerStr = "Congratulations to the Defensive Player of the Year, " + heisCB.team.name +
                        " CB " + heisCB.name + " [" + defPOTY.getYrStr() + "], who had " +
                        heisCB.statsTackles + " tackles, defended " + heisCB.statsDefended + " passes, and " +
                        heisCB.statsInts + " inteceptions. He led " + heisCB.team.name +
                        " to a " + heisCB.team.wins + "-" + heisCB.team.losses + " record and a #" + heisCB.team.rankTeamPollScore +
                        " poll ranking.";
                heismanStats = heismanWinnerStr + "\n\nFull Results:" + heismanTop5;
            } else if (defPOTY instanceof PlayerS) {
                //wr heisman
                PlayerS heisS = (PlayerS) defPOTY;
                heismanWinnerStr = "Congratulations to the Defensive Player of the Year, " + heisS.team.name +
                        " S " + heisS.name + " [" + defPOTY.getYrStr() + "], who had " +
                        heisS.statsTackles + " tackles, " + heisS.statsSacks + " sacks, and recovered " + heisS.statsFumbles + " fumbles, and " +
                        heisS.statsInts + " inteceptions. He led " + heisS.team.name +
                        " to a " + heisS.team.wins + "-" + heisS.team.losses + " record and a #" + heisS.team.rankTeamPollScore +
                        " poll ranking.";
                heismanStats = heismanWinnerStr + "\n\nFull Results:" + heismanTop5;
            }

            // Add news story
            if (putNewsStory) {
                newsStories.get(currentWeek + 1).add(defPOTY.name + " is the Defensive Player of the Year!>" + heismanWinnerStr);
                defPOTYWinnerStrFull = heismanStats;
            }

            return heismanStats;
        } else {
            return defPOTYWinnerStrFull;
        }
    }

    private ArrayList<Player> getTopFreshman() {
        freshman = null;
        ArrayList<Player> freshmanCandidates = new ArrayList<>();
        fQBs = new ArrayList<>();
        fRBs = new ArrayList<>();
        fWRs = new ArrayList<>();
        fTEs = new ArrayList<>();
        fKs = new ArrayList<>();
        fOLs = new ArrayList<>();
        fDLs = new ArrayList<>();
        fLBs = new ArrayList<>();
        fCBs = new ArrayList<>();
        fSs = new ArrayList<>();

        for (int i = 0; i < teamList.size(); ++i) {
            //qb
            for (int qb = 0; qb < teamList.get(i).teamQBs.size(); ++qb) {
                if (teamList.get(i).teamQBs.get(qb).year == 1) {
                    freshmanCandidates.add(teamList.get(i).teamQBs.get(qb));
                    fQBs.add(teamList.get(i).teamQBs.get(qb));
                }
            }

            //rb
            for (int rb = 0; rb < teamList.get(i).teamRBs.size(); ++rb) {
                if (teamList.get(i).teamRBs.get(rb).year == 1) {
                    freshmanCandidates.add(teamList.get(i).teamRBs.get(rb));
                    fRBs.add(teamList.get(i).teamRBs.get(rb));
                }
            }

            //wr
            for (int wr = 0; wr < teamList.get(i).teamWRs.size(); ++wr) {
                if (teamList.get(i).teamWRs.get(wr).year == 1) {
                    freshmanCandidates.add(teamList.get(i).teamWRs.get(wr));
                    fWRs.add(teamList.get(i).teamWRs.get(wr));
                }
            }

            //te
            for (int te = 0; te < teamList.get(i).teamTEs.size(); ++te) {
                if (teamList.get(i).teamTEs.get(te).year == 1) {
                    freshmanCandidates.add(teamList.get(i).teamTEs.get(te));
                    fTEs.add(teamList.get(i).teamTEs.get(te));
                }
            }

            //ol
            for (int ol = 0; ol < teamList.get(i).teamOLs.size(); ++ol) {
                if (teamList.get(i).teamOLs.get(ol).year == 1) {
                    //freshmanCandidates.add(teamList.get(i).teamOLs.get(ol));
                    fOLs.add(teamList.get(i).teamOLs.get(ol));
                }
            }

            //k
            for (int k = 0; k < teamList.get(i).teamKs.size(); ++k) {
                if (teamList.get(i).teamKs.get(k).year == 1) {
                    freshmanCandidates.add(teamList.get(i).teamKs.get(k));
                    fKs.add(teamList.get(i).teamKs.get(k));
                }
            }

            
            //dl
            for (int dl = 0; dl < teamList.get(i).teamDLs.size(); ++dl) {
                if (teamList.get(i).teamDLs.get(dl).year == 1) {
                    freshmanCandidates.add(teamList.get(i).teamDLs.get(dl));
                    fDLs.add(teamList.get(i).teamDLs.get(dl));
                }
            }

            //lb
            for (int lb = 0; lb < teamList.get(i).teamLBs.size(); ++lb) {
                if (teamList.get(i).teamLBs.get(lb).year == 1) {
                    freshmanCandidates.add(teamList.get(i).teamLBs.get(lb));
                    fLBs.add(teamList.get(i).teamLBs.get(lb));
                }
            }

            //cb
            for (int cb = 0; cb < teamList.get(i).teamCBs.size(); ++cb) {
                if (teamList.get(i).teamCBs.get(cb).year == 1) {
                    freshmanCandidates.add(teamList.get(i).teamCBs.get(cb));
                    fCBs.add(teamList.get(i).teamCBs.get(cb));
                }
            }

            //s
            for (int s = 0; s < teamList.get(i).teamSs.size(); ++s) {
                if (teamList.get(i).teamSs.get(s).year == 1) {
                    freshmanCandidates.add(teamList.get(i).teamSs.get(s));
                    fSs.add(teamList.get(i).teamSs.get(s));
                }
            }
        }
        Collections.sort(freshmanCandidates, new CompPlayerHeisman());
        Collections.sort(fQBs, new CompPlayerHeisman());
        Collections.sort(fRBs, new CompPlayerHeisman());
        Collections.sort(fWRs, new CompPlayerHeisman());
        Collections.sort(fTEs, new CompPlayerHeisman());
        Collections.sort(fOLs, new CompPlayerHeisman());
        Collections.sort(fKs, new CompPlayerHeisman());
        Collections.sort(fDLs, new CompPlayerHeisman());
        Collections.sort(fLBs, new CompPlayerHeisman());
        Collections.sort(fCBs, new CompPlayerHeisman());
        Collections.sort(fSs, new CompPlayerHeisman());

        return freshmanCandidates;
    }

    /**
     * Gets All Freshman
     *
     * @return string list of all americans
     */
    public String getAllFreshmanStr() {
        if (allFreshman.isEmpty()) {

            if (fQBs.size() > 0) {
                allFreshman.add(fQBs.get(0));
                fQBs.get(0).wonAllFreshman = true;
                fQBs.get(0).team.HC.get(0).allamericans++;
            }

            if (fRBs.size() > 1) {
                allFreshman.add(fRBs.get(0));
                fRBs.get(0).wonAllFreshman = true;
                fRBs.get(0).team.HC.get(0).allamericans++;

                allFreshman.add(fRBs.get(1));
                fRBs.get(1).wonAllFreshman = true;
                fRBs.get(1).team.HC.get(0).allamericans++;

            }

            if (fWRs.size() > 2) {
                for (int i = 0; i < 3; ++i) {
                    allFreshman.add(fWRs.get(i));
                    fWRs.get(i).wonAllFreshman = true;
                    fWRs.get(i).team.HC.get(0).allamericans++;

                }
            }

            if (fTEs.size() > 0) {
                allFreshman.add(fTEs.get(0));
                fTEs.get(0).wonAllFreshman = true;
                fTEs.get(0).team.HC.get(0).allamericans++;

            }

            if (fOLs.size() > 4) {
                for (int i = 0; i < 5; ++i) {
                    allFreshman.add(fOLs.get(i));
                    fOLs.get(i).wonAllFreshman = true;
                    fOLs.get(i).team.HC.get(0).allamericans++;
                }
            }

            if (fKs.size() > 0) {
                allFreshman.add(fKs.get(0));
                fKs.get(0).wonAllFreshman = true;
                fKs.get(0).team.HC.get(0).allamericans++;
            }

            if (fDLs.size() > 3) {
                for (int i = 0; i < 4; ++i) {
                    allFreshman.add(fDLs.get(i));
                    fDLs.get(i).wonAllFreshman = true;
                    fDLs.get(i).team.HC.get(0).allamericans++;
                }
            }

            if (fLBs.size() > 2) {
                for (int i = 0; i < 3; ++i) {
                    allFreshman.add(fLBs.get(i));
                    fLBs.get(i).wonAllFreshman = true;
                    fLBs.get(i).team.HC.get(0).allamericans++;
                }
            }

            if (fCBs.size() > 2) {
                for (int i = 0; i < 3; ++i) {
                    allFreshman.add(fCBs.get(i));
                    fCBs.get(i).wonAllFreshman = true;
                    fCBs.get(i).team.HC.get(0).allamericans++;
                }
            }
            if (fSs.size() > 1) {
                for (int i = 0; i < 2; ++i) {
                    allFreshman.add(fSs.get(i));
                    fSs.get(i).wonAllFreshman = true;
                    fSs.get(i).team.HC.get(0).allamericans++;
                }
            }
        }

        StringBuilder allFreshmanTeam = new StringBuilder();
        for (int i = 0; i < allFreshman.size(); ++i) {
            Player p = allFreshman.get(i);
            allFreshmanTeam.append(p.team.abbr + " (" + p.team.wins + "-" + p.team.losses + ")" + " - ");
            if (p instanceof PlayerQB) {
                PlayerQB pqb = (PlayerQB) p;
                allFreshmanTeam.append(" QB " + pqb.name + " [" + pqb.getYrStr() + "]\n \t\t" +
                        pqb.statsPassTD + " TDs, " + pqb.statsInt + " Int, " + pqb.statsPassYards + " Yds\n");
            } else if (p instanceof PlayerRB) {
                PlayerRB prb = (PlayerRB) p;
                allFreshmanTeam.append(" RB " + prb.name + " [" + prb.getYrStr() + "]\n \t\t" +
                        prb.statsRushTD + " TDs, " + prb.statsFumbles + " Fum, " + prb.statsRushYards + " Yds\n");
            } else if (p instanceof PlayerWR) {
                PlayerWR pwr = (PlayerWR) p;
                allFreshmanTeam.append(" WR " + pwr.name + " [" + pwr.getYrStr() + "]\n \t\t" +
                        pwr.statsRecTD + " TDs, " + pwr.statsReceptions + " Rec, " + pwr.statsRecYards + " Yds\n");
            } else if (p instanceof PlayerTE) {
                PlayerTE pte = (PlayerTE) p;
                allFreshmanTeam.append(" TE " + pte.name + " [" + pte.getYrStr() + "]\n \t\t" +
                        pte.statsRecTD + " TDs, " + pte.statsReceptions + " Rec, " + pte.statsRecYards + " Yds\n");
            } else if (p instanceof PlayerOL) {
                PlayerOL pol = (PlayerOL) p;
                allFreshmanTeam.append(" OL " + pol.name + " [" + pol.getYrStr() + "]\n \t\t" +
                        df2.format(pol.getYardsPerRush()) + " YPR, " + df2.format(pol.getYardsPerPass()) + " YPP, " + pol.statsSacksAllowed + " Sacks\n");
            } else if (p instanceof PlayerK) {
                PlayerK pk = (PlayerK) p;
                allFreshmanTeam.append(" K " + pk.name + " [" + pk.getYrStr() + "]\n \t\t" +
                        "FGs: " + pk.statsFGMade + "/" + pk.statsFGAtt + ", XPs: " + pk.statsXPMade + "/" + pk.statsXPAtt + "\n");
            } else if (p instanceof PlayerDL) {
                PlayerDL pdl = (PlayerDL) p;
                allFreshmanTeam.append(" DL " + pdl.name + " [" + pdl.getYrStr() + "]\n \t\t" +
                        pdl.statsTackles + " Tkl, " + pdl.statsSacks + " Sacks, " + pdl.statsFumbles + " Fum\n");
            } else if (p instanceof PlayerLB) {
                PlayerLB plb = (PlayerLB) p;
                allFreshmanTeam.append(" LB " + plb.name + " [" + plb.getYrStr() + "]\n \t\t" +
                        plb.statsTackles + " Tkl, " + plb.statsSacks + " Sacks, " + plb.statsFumbles + " Fum, " + plb.statsInts + " Int\n");
            } else if (p instanceof PlayerCB) {
                PlayerCB pcb = (PlayerCB) p;
                allFreshmanTeam.append(" CB " + pcb.name + " [" + pcb.getYrStr() + "]\n \t\t" +
                        pcb.statsTackles + " Tkl, " + pcb.statsSacks + " Sacks, " + pcb.statsDefended + " Def, " + pcb.statsInts + " Int\n");
            } else if (p instanceof PlayerS) {
                PlayerS ps = (PlayerS) p;
                allFreshmanTeam.append(" S " + ps.name + " [" + ps.getYrStr() + "]\n \t\t" +
                        ps.statsTackles + " Tkl, " + ps.statsSacks + " Sacks, " + ps.statsFumbles + " Fum, " + ps.statsInts + " Int\n");
            } else {
                allFreshmanTeam.append(" " + p.position + " " + p.name + " [" + p.getYrStr() + "]\n");
            }
            allFreshmanTeam.append(" \t\tOverall: " + p.ratOvr + "\n\n>");
        }

        return allFreshmanTeam.toString();
    }

    /**
     * Perform the heisman ceremony. Congratulate winner and give top 5 vote getters.
     *
     * @return string of the heisman ceremony.
     */
    public String getFreshmanCeremonyStr() {
        boolean putNewsStory = false;
        if (!heismanDecided) {
            freshmanCandidates = getTopFreshman();
            freshman = freshmanCandidates.get(0);
            freshman.wonTopFreshman = true;
            putNewsStory = true;
            //full results string
            String freshmanTop5 = "\n";
            for (int i = 0; i < 5; ++i) {
                Player p = freshmanCandidates.get(i);
                if (p instanceof PlayerQB) {
                    PlayerQB pqb = (PlayerQB) p;
                    freshmanTop5 += (i + 1) + ". QB " + pqb.name + ": " + p.getHeismanScore() + " votes\n\t\t"
                            + pqb.team.strRankTeamRecord() + "\n\t\t"
                            + pqb.statsPassTD + " TDs, " + pqb.statsInt + " Int, " + pqb.statsPassYards + " Yds, "
                            + df2.format(pqb.getPasserRating()) + " QBR\n\n";
                } else if (p instanceof PlayerRB) {
                    PlayerRB prb = (PlayerRB) p;
                    freshmanTop5 += (i + 1) + ". RB " + prb.name + ": " + p.getHeismanScore() + " votes\n\t\t"
                            + prb.team.strRankTeamRecord() + "\n\t\t"
                            + prb.statsRushTD + " TDs, " + prb.statsFumbles + " Fum, " + prb.statsRushYards + " Yds\n\n";
                } else if (p instanceof PlayerWR) {
                    PlayerWR pwr = (PlayerWR) p;
                    freshmanTop5 += (i + 1) + ". WR " + pwr.name + ": " + p.getHeismanScore() + " votes\n\t\t"
                            + pwr.team.strRankTeamRecord() + "\n\t\t"
                            + pwr.statsRecTD + " TDs, " + pwr.statsFumbles + " Fum, " + pwr.statsRecYards + " Yds\n\n";
                } else if (p instanceof PlayerTE) {
                    PlayerTE pte = (PlayerTE) p;
                    freshmanTop5 += (i + 1) + ". TE " + pte.name + ": " + p.getHeismanScore() + " votes\n\t\t"
                            + pte.team.strRankTeamRecord() + "\n\t\t"
                            + pte.statsRecTD + " TDs, " + pte.statsFumbles + " Fum, " + pte.statsRecYards + " Yds\n\n";
                } else if (p instanceof PlayerDL) {
                    PlayerDL pdl = (PlayerDL) p;
                    freshmanTop5 += (i + 1) + ". DL " + pdl.name + ": " + p.getHeismanScore() + " votes\n\t\t"
                            + pdl.team.strRankTeamRecord() + "\n\t\t"
                            + pdl.statsTackles + " Tkl, " + pdl.statsSacks + " Sacks, " + pdl.statsFumbles + " Fum\n\n";
                } else if (p instanceof PlayerLB) {
                    PlayerLB plb = (PlayerLB) p;
                    freshmanTop5 += (i + 1) + ". LB " + plb.name + ": " + p.getHeismanScore() + " votes\n\t\t"
                            + plb.team.strRankTeamRecord() + "\n\t\t"
                            + plb.statsTackles + " Tkl, " + plb.statsFumbles + " Fum, " + plb.statsInts + " Int\n\n";
                } else if (p instanceof PlayerCB) {
                    PlayerCB pcb = (PlayerCB) p;
                    freshmanTop5 += (i + 1) + ". CB " + pcb.name + ": " + p.getHeismanScore() + " votes\n\t\t"
                            + pcb.team.strRankTeamRecord() + "\n\t\t"
                            + pcb.statsTackles + " Tkl, " + pcb.statsDefended + " Def, " + pcb.statsInts + " Int\n\n";
                } else if (p instanceof PlayerS) {
                    PlayerS ps = (PlayerS) p;
                    freshmanTop5 += (i + 1) + ". S " + ps.name + ": " + p.getHeismanScore() + " votes\n\t\t"
                            + ps.team.strRankTeamRecord() + "\n\t\t"
                            + ps.statsTackles + " Tkl, " + ps.statsFumbles + " Fum, " + ps.statsInts + " Int\n\n";
                }
            }

            String freshmanStats = "";
            String freshmanWinnerStr = "";
            if (freshman instanceof PlayerQB) {
                //qb freshman
                PlayerQB heisQB = (PlayerQB) freshman;
                freshmanWinnerStr = "Congratulations to the Freshman Player of the Year, " + heisQB.team.abbr +
                        " QB " + heisQB.name + " [" + freshman.getYrStr() + "], who had " +
                        heisQB.statsPassTD + " TDs, just " + heisQB.statsInt + " interceptions, and " +
                        heisQB.statsPassYards + " passing yards. In addition, he ran for " + heisQB.statsRushYards + "yards and scored " + heisQB.statsRushTD + " touchdowns. He led " + heisQB.team.name +
                        " to a " + heisQB.team.wins + "-" + heisQB.team.losses + " record and a #" + heisQB.team.rankTeamPollScore +
                        " poll ranking.";
                freshmanStats = freshmanWinnerStr + "\n\nFull Results:" + freshmanTop5;
            } else if (freshman instanceof PlayerRB) {
                //rb freshman
                PlayerRB heisRB = (PlayerRB) freshman;
                freshmanWinnerStr = "Congratulations to the Freshman Player of the Year, " + heisRB.team.abbr +
                        " RB " + heisRB.name + " [" + freshman.getYrStr() + "], who had " +
                        heisRB.statsRushTD + " TDs, just " + heisRB.statsFumbles + " fumbles, and " +
                        heisRB.statsRushYards + " rushing yards. He led " + heisRB.team.name +
                        " to a " + heisRB.team.wins + "-" + heisRB.team.losses + " record and a #" + heisRB.team.rankTeamPollScore +
                        " poll ranking.";
                freshmanStats = freshmanWinnerStr + "\n\nFull Results:" + freshmanTop5;
            } else if (freshman instanceof PlayerWR) {
                //wr freshman
                PlayerWR heisWR = (PlayerWR) freshman;
                freshmanWinnerStr = "Congratulations to the Freshman Player of the Year, " + heisWR.team.abbr +
                        " WR " + heisWR.name + " [" + freshman.getYrStr() + "], who had " +
                        heisWR.statsRecTD + " TDs, just " + heisWR.statsFumbles + " fumbles, and " +
                        heisWR.statsRecYards + " receiving yards. He led " + heisWR.team.name +
                        " to a " + heisWR.team.wins + "-" + heisWR.team.losses + " record and a #" + heisWR.team.rankTeamPollScore +
                        " poll ranking.";
                freshmanStats = freshmanWinnerStr + "\n\nFull Results:" + freshmanTop5;
            } else if (freshman instanceof PlayerTE) {
                //te freshman
                PlayerTE heisTE = (PlayerTE) freshman;
                freshmanWinnerStr = "Congratulations to the Freshman Player of the Year, " + heisTE.team.abbr +
                        " TE " + heisTE.name + " [" + freshman.getYrStr() + "], who had " +
                        heisTE.statsRecTD + " TDs, just " + heisTE.statsFumbles + " fumbles, and " +
                        heisTE.statsRecYards + " receiving yards. He led " + heisTE.team.name +
                        " to a " + heisTE.team.wins + "-" + heisTE.team.losses + " record and a #" + heisTE.team.rankTeamPollScore +
                        " poll ranking.";
                freshmanStats = freshmanWinnerStr + "\n\nFull Results:" + freshmanTop5;
            } else if (freshman instanceof PlayerDL) {
                //dl freshman
                PlayerDL heisDL = (PlayerDL) freshman;
                freshmanWinnerStr = "Congratulations to the Freshman Player of the Year, " + heisDL.team.abbr +
                        " DL " + heisDL.name + " [" + freshman.getYrStr() + "], who had " +
                        heisDL.statsTackles + " tackles, " + heisDL.statsSacks + " sacks, and forced " + heisDL.statsFumbles + " fumbles. He led " + heisDL.team.name +
                        " to a " + heisDL.team.wins + "-" + heisDL.team.losses + " record and a #" + heisDL.team.rankTeamPollScore +
                        " poll ranking.";
                freshmanStats = freshmanWinnerStr + "\n\nFull Results:" + freshmanTop5;
            } else if (freshman instanceof PlayerLB) {
                //wr freshman
                PlayerLB heisLB = (PlayerLB) freshman;
                freshmanWinnerStr = "Congratulations to the Freshman Player of the Year, " + heisLB.team.abbr +
                        " LB " + heisLB.name + " [" + freshman.getYrStr() + "], who had " +
                        heisLB.statsTackles + " tackles, " + heisLB.statsSacks + " sacks, and recovered " + heisLB.statsFumbles + " fumbles, and " +
                        heisLB.statsInts + " inteceptions. He led " + heisLB.team.name +
                        " to a " + heisLB.team.wins + "-" + heisLB.team.losses + " record and a #" + heisLB.team.rankTeamPollScore +
                        " poll ranking.";
                freshmanStats = freshmanWinnerStr + "\n\nFull Results:" + freshmanTop5;
            } else if (freshman instanceof PlayerCB) {
                //wr freshman
                PlayerCB heisCB = (PlayerCB) freshman;
                freshmanWinnerStr = "Congratulations to the Freshman Player of the Year, " + heisCB.team.abbr +
                        " CB " + heisCB.name + " [" + freshman.getYrStr() + "], who had " +
                        heisCB.statsTackles + " tackles, defended " + heisCB.statsDefended + " passes, and " +
                        heisCB.statsInts + " inteceptions. He led " + heisCB.team.name +
                        " to a " + heisCB.team.wins + "-" + heisCB.team.losses + " record and a #" + heisCB.team.rankTeamPollScore +
                        " poll ranking.";
                freshmanStats = freshmanWinnerStr + "\n\nFull Results:" + freshmanTop5;
            } else if (freshman instanceof PlayerS) {
                //wr freshman
                PlayerS heisS = (PlayerS) freshman;
                freshmanWinnerStr = "Congratulations to the Freshman Player of the Year, " + heisS.team.abbr +
                        " S " + heisS.name + " [" + freshman.getYrStr() + "], who had " +
                        heisS.statsTackles + " tackles, " + heisS.statsSacks + " sacks, and recovered " + heisS.statsFumbles + " fumbles, and " +
                        heisS.statsInts + " inteceptions. He led " + heisS.team.name +
                        " to a " + heisS.team.wins + "-" + heisS.team.losses + " record and a #" + heisS.team.rankTeamPollScore +
                        " poll ranking.";
                freshmanStats = freshmanWinnerStr + "\n\nFull Results:" + freshmanTop5;
            }

            // Add news story
            if (putNewsStory) {
                newsStories.get(currentWeek + 1).add(freshman.name + " is the Freshman Player of the Year!>" + freshmanWinnerStr);
                freshmanWinnerStrFull = freshmanStats;
            }

            return freshmanStats;
        } else {
            return freshmanWinnerStrFull;
        }
    }

    public String getCoachAwardStr() {
        if (!heismanDecided) {
            ArrayList<HeadCoach> coachCandidates = rankHC();
            coachWinner = coachCandidates.get(0);
            String coachAwardTopList = "";
            for (int i = 0; i < 5; ++i) {
                Player p = coachCandidates.get(i);
                HeadCoach hc = (HeadCoach) p;
                coachAwardTopList += (i + 1) + ". " + hc.name + ": " + ((HeadCoach) p).getCoachScore() + " votes\n";
                coachAwardTopList += p.team.name + " (" + p.team.wins + "-" + p.team.losses + ")  Overall: " + hc.ratOvr + "\n\n";
            }
            String coachStats = "";
            String coachWinnerStr = "";
            coachWinnerStr = "Congratulations to the Coach of the Year, " + coachWinner.name + "!\n\nHe led " + coachWinner.team.name +
                    " to a " + coachWinner.team.wins + "-" + coachWinner.team.losses + " record and a #" + coachWinner.team.rankTeamPollScore +
                    " poll ranking.";
            coachStats = coachWinnerStr + "\n\nFull Results:\n\n" + coachAwardTopList;

            newsStories.get(currentWeek + 1).add("Coach of the Year Announced>This year's top head coach award was given to " + coachWinner.name +
                    " of " + coachWinner.team.name + ".");

            coachCandidates.get(0).awards++;
            coachWinnerStrFull = coachStats;
            coachCandidates.get(0).wonTopHC = true;


            return coachStats;
        } else {
            return coachWinnerStrFull;
        }
    }

    /**
     * Gets All Americans, best of all conference teams
     *
     * @return string list of all americans
     */
    public String getAllAmericanStr() {
        if (allAmericans.isEmpty()) {
            ArrayList<PlayerQB> qbs = new ArrayList<>();
            ArrayList<PlayerRB> rbs = new ArrayList<>();
            ArrayList<PlayerWR> wrs = new ArrayList<>();
            ArrayList<PlayerTE> tes = new ArrayList<>();
            ArrayList<PlayerOL> ols = new ArrayList<>();
            ArrayList<PlayerK> ks = new ArrayList<>();
            ArrayList<PlayerDL> dls = new ArrayList<>();
            ArrayList<PlayerLB> lbs = new ArrayList<>();
            ArrayList<PlayerCB> cbs = new ArrayList<>();
            ArrayList<PlayerS> ss = new ArrayList<>();

            for (Conference c : conferences) {
                c.getAllConfPlayers();
                qbs.add((PlayerQB) c.allConfPlayers.get(1));
                rbs.add((PlayerRB) c.allConfPlayers.get(2));
                rbs.add((PlayerRB) c.allConfPlayers.get(3));
                wrs.add((PlayerWR) c.allConfPlayers.get(4));
                wrs.add((PlayerWR) c.allConfPlayers.get(5));
                wrs.add((PlayerWR) c.allConfPlayers.get(6));
                tes.add((PlayerTE) c.allConfPlayers.get(7));
                for (int i = 8; i < 13; ++i) {
                    ols.add((PlayerOL) c.allConfPlayers.get(i));
                }
                ks.add((PlayerK) c.allConfPlayers.get(13));
                for (int i = 14; i < 17; ++i) {
                    dls.add((PlayerDL) c.allConfPlayers.get(i));
                }
                for (int i = 18; i < 21; ++i) {
                    lbs.add((PlayerLB) c.allConfPlayers.get(i));
                }
                for (int i = 21; i < 24; ++i) {
                    cbs.add((PlayerCB) c.allConfPlayers.get(i));
                }
                ss.add((PlayerS) c.allConfPlayers.get(24));
            }

            Collections.sort(qbs, new CompPlayerHeisman());
            Collections.sort(rbs, new CompPlayerHeisman());
            Collections.sort(wrs, new CompPlayerHeisman());
            Collections.sort(tes, new CompPlayerHeisman());
            Collections.sort(ols, new CompPlayerHeisman());
            Collections.sort(ks, new CompPlayerHeisman());
            Collections.sort(dls, new CompPlayerHeisman());
            Collections.sort(lbs, new CompPlayerHeisman());
            Collections.sort(cbs, new CompPlayerHeisman());
            Collections.sort(ss, new CompPlayerHeisman());

            allAmericans.add(qbs.get(0));
            qbs.get(0).wonAllAmerican = true;
            qbs.get(0).team.HC.get(0).allamericans++;
            allAmericans.add(rbs.get(0));
            rbs.get(0).wonAllAmerican = true;
            rbs.get(0).team.HC.get(0).allamericans++;
            allAmericans.add(rbs.get(1));
            rbs.get(1).wonAllAmerican = true;
            rbs.get(1).team.HC.get(0).allamericans++;
            for (int i = 0; i < 3; ++i) {
                allAmericans.add(wrs.get(i));
                wrs.get(i).wonAllAmerican = true;
                wrs.get(i).team.HC.get(0).allamericans++;
            }
            allAmericans.add(tes.get(0));
            tes.get(0).wonAllAmerican = true;
            tes.get(0).team.HC.get(0).allamericans++;
            for (int i = 0; i < 5; ++i) {
                allAmericans.add(ols.get(i));
                ols.get(i).wonAllAmerican = true;
                ols.get(i).team.HC.get(0).allamericans++;
            }
            allAmericans.add(ks.get(0));
            ks.get(0).wonAllAmerican = true;
            ks.get(0).team.HC.get(0).allamericans++;
            for (int i = 0; i < 4; ++i) {
                allAmericans.add(dls.get(i));
                dls.get(i).wonAllAmerican = true;
                dls.get(i).team.HC.get(0).allamericans++;
            }
            for (int i = 0; i < 3; ++i) {
                allAmericans.add(lbs.get(i));
                lbs.get(i).wonAllAmerican = true;
                lbs.get(i).team.HC.get(0).allamericans++;
            }
            for (int i = 0; i < 3; ++i) {
                allAmericans.add(cbs.get(i));
                cbs.get(i).wonAllAmerican = true;
                cbs.get(i).team.HC.get(0).allamericans++;
            }
            for (int i = 0; i < 2; ++i) {
                allAmericans.add(ss.get(i));
                ss.get(i).wonAllAmerican = true;
                ss.get(i).team.HC.get(0).allamericans++;
            }

        }

        StringBuilder allAmerican = new StringBuilder();
        for (int i = 0; i < allAmericans.size(); ++i) {
            Player p = allAmericans.get(i);
            allAmerican.append(p.team.abbr + " (" + p.team.wins + "-" + p.team.losses + ")" + " - ");
            if (p instanceof PlayerQB) {
                PlayerQB pqb = (PlayerQB) p;
                allAmerican.append(" QB " + pqb.name + " [" + pqb.getYrStr() + "]\n \t\t" +
                        pqb.statsPassTD + " TDs, " + pqb.statsInt + " Int, " + pqb.statsPassYards + " Yds\n");
            } else if (p instanceof PlayerRB) {
                PlayerRB prb = (PlayerRB) p;
                allAmerican.append(" RB " + prb.name + " [" + prb.getYrStr() + "]\n \t\t" +
                        prb.statsRushTD + " TDs, " + prb.statsFumbles + " Fum, " + prb.statsRushYards + " Yds\n");
            } else if (p instanceof PlayerWR) {
                PlayerWR pwr = (PlayerWR) p;
                allAmerican.append(" WR " + pwr.name + " [" + pwr.getYrStr() + "]\n \t\t" +
                        pwr.statsRecTD + " TDs, " + pwr.statsReceptions + " Rec, " + pwr.statsRecYards + " Yds\n");
            } else if (p instanceof PlayerTE) {
                PlayerTE pte = (PlayerTE) p;
                allAmerican.append(" TE " + pte.name + " [" + pte.getYrStr() + "]\n \t\t" +
                        pte.statsRecTD + " TDs, " + pte.statsReceptions + " Rec, " + pte.statsRecYards + " Yds\n");
            } else if (p instanceof PlayerOL) {
                PlayerOL pol = (PlayerOL) p;
                allAmerican.append(" OL " + pol.name + " [" + pol.getYrStr() + "]\n \t\t" +
                        df2.format(pol.getYardsPerRush()) + " YPR, " + df2.format(pol.getYardsPerPass()) + " YPP, " + pol.statsSacksAllowed + " Sacks\n");
            } else if (p instanceof PlayerK) {
                PlayerK pk = (PlayerK) p;
                allAmerican.append(" K " + pk.name + " [" + pk.getYrStr() + "]\n \t\t" +
                        "FGs: " + pk.statsFGMade + "/" + pk.statsFGAtt + ", XPs: " + pk.statsXPMade + "/" + pk.statsXPAtt + "\n");
            } else if (p instanceof PlayerDL) {
                PlayerDL pdl = (PlayerDL) p;
                allAmerican.append(" DL " + pdl.name + " [" + pdl.getYrStr() + "]\n \t\t" +
                        pdl.statsTackles + " Tkl, " + pdl.statsSacks + " Sacks, " + pdl.statsFumbles + " Fum\n");
            } else if (p instanceof PlayerLB) {
                PlayerLB plb = (PlayerLB) p;
                allAmerican.append(" LB " + plb.name + " [" + plb.getYrStr() + "]\n \t\t" +
                        plb.statsTackles + " Tkl, " + plb.statsSacks + " Sacks, " + plb.statsFumbles + " Fum, " + plb.statsInts + " Int\n");
            } else if (p instanceof PlayerCB) {
                PlayerCB pcb = (PlayerCB) p;
                allAmerican.append(" CB " + pcb.name + " [" + pcb.getYrStr() + "]\n \t\t" +
                        pcb.statsTackles + " Tkl, " + pcb.statsSacks + " Sacks, " + pcb.statsDefended + " Def, " + pcb.statsInts + " Int\n");
            } else if (p instanceof PlayerS) {
                PlayerS ps = (PlayerS) p;
                allAmerican.append(" S " + ps.name + " [" + ps.getYrStr() + "]\n \t\t" +
                        ps.statsTackles + " Tkl, " + ps.statsSacks + " Sacks, " + ps.statsFumbles + " Fum, " + ps.statsInts + " Int\n");
            } else {
                allAmerican.append(" " + p.position + " " + p.name + " [" + p.getYrStr() + "]\n");
            }
            allAmerican.append(" \t\tOverall: " + p.ratOvr + "\n\n>");
        }


        // Go through all the all conf players to get the all americans
        return allAmerican.toString();
    }

    /**
     * Get a string list of all conference team of choice
     *
     * @param confNum which conference
     * @return string of the conference team
     */
    public String getAllConfStr(int confNum) {
        ArrayList<Player> allConfPlayers = conferences.get(confNum).getAllConfPlayers();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < allConfPlayers.size(); ++i) {
            Player p = allConfPlayers.get(i);
            sb.append(p.team.abbr + " (" + p.team.wins + "-" + p.team.losses + ")" + " - ");
            if (p instanceof HeadCoach) {
                HeadCoach hc = (HeadCoach) p;
                sb.append(" HC " + hc.name + "\n \t\tAge: " + hc.age + " Season " + hc.year + "\n");
            } else if (p instanceof PlayerQB) {
                PlayerQB pqb = (PlayerQB) p;
                sb.append(" QB " + pqb.name + " [" + pqb.getYrStr() + "]\n \t\t" +
                        pqb.statsPassTD + " TDs, " + pqb.statsInt + " Int, " + pqb.statsPassYards + " Pass Yds\n");
            } else if (p instanceof PlayerRB) {
                PlayerRB prb = (PlayerRB) p;
                sb.append(" RB " + prb.name + " [" + prb.getYrStr() + "]\n \t\t" +
                        prb.statsRushTD + " TDs, " + prb.statsFumbles + " Fum, " + prb.statsRushYards + " Yds\n");
            } else if (p instanceof PlayerWR) {
                PlayerWR pwr = (PlayerWR) p;
                sb.append(" WR " + pwr.name + " [" + pwr.getYrStr() + "]\n \t\t" +
                        pwr.statsRecTD + " TDs, " + pwr.statsReceptions + " Rec, " + pwr.statsRecYards + " Yds\n");
            } else if (p instanceof PlayerTE) {
                PlayerTE pte = (PlayerTE) p;
                sb.append(" TE " + pte.name + " [" + pte.getYrStr() + "]\n \t\t" +
                        pte.statsRecTD + " TDs, " + pte.statsReceptions + " Rec, " + pte.statsRecYards + " Yds\n");
            } else if (p instanceof PlayerOL) {
                PlayerOL pol = (PlayerOL) p;
                sb.append(" OL " + pol.name + " [" + pol.getYrStr() + "]\n \t\t" +
                        df2.format(pol.getYardsPerRush()) + " YPR, " + df2.format(pol.getYardsPerPass()) + " YPP, " + pol.statsSacksAllowed + " Sacks\n");
            } else if (p instanceof PlayerK) {
                PlayerK pk = (PlayerK) p;
                sb.append(" K " + pk.name + " [" + pk.getYrStr() + "]\n \t\t" +
                        "FGs: " + pk.statsFGMade + "/" + pk.statsFGAtt + ", XPs: " + pk.statsXPMade + "/" + pk.statsXPAtt + "\n");
            } else if (p instanceof PlayerDL) {
                PlayerDL pdl = (PlayerDL) p;
                sb.append(" DL " + pdl.name + " [" + pdl.getYrStr() + "]\n \t\t" +
                        pdl.statsTackles + " Tkl, " + pdl.statsSacks + " Sacks, " + pdl.statsFumbles + " Fum\n");
            } else if (p instanceof PlayerLB) {
                PlayerLB plb = (PlayerLB) p;
                sb.append(" LB " + plb.name + " [" + plb.getYrStr() + "]\n \t\t" +
                        plb.statsTackles + " Tkl, " + plb.statsSacks + " Sacks, " + plb.statsFumbles + " Fum, " + plb.statsInts + " Int\n");
            } else if (p instanceof PlayerCB) {
                PlayerCB pcb = (PlayerCB) p;
                sb.append(" CB " + pcb.name + " [" + pcb.getYrStr() + "]\n \t\t" +
                        pcb.statsTackles + " Tkl, " + pcb.statsDefended + " Def, " + pcb.statsFumbles + " Fum, " + pcb.statsInts + " Int\n");
            } else if (p instanceof PlayerS) {
                PlayerS ps = (PlayerS) p;
                sb.append(" S " + ps.name + " [" + ps.getYrStr() + "]\n \t\t" +
                        ps.statsTackles + " Tkl, " + ps.statsSacks + " Sacks, " + ps.statsFumbles + " Fum, " + ps.statsInts + " Int\n");
            } else {
                sb.append(" " + p.position + " " + p.name + " [" + p.getYrStr() + "]\n");
            }
            if (p instanceof HeadCoach) {
                sb.append(" \t\tOverall: " + ((HeadCoach) p).getHCOverall() + "\n\n>");
            } else {
                sb.append(" \t\tOverall: " + p.ratOvr + "\n\n>");
            }
        }

        return sb.toString();
    }

    /**
     * Get a list of all the CCGs and their teams
     *
     * @return
     */
    public String getCCGsStr() {
        StringBuilder sb = new StringBuilder();
        for (Conference c : conferences) {
            sb.append(c.getCCGStr() + "\n\n");
        }
        return sb.toString();
    }

    /**
     * Get list of all bowl games and their predicted teams
     *
     * @return string of all the bowls and their predictions
     */
    public String getBowlGameWatchStr() {
        //if bowls arent scheduled yet, give predictions
        if (!hasScheduledBowls) {

            if (expPlayoffs) {
                getPlayoffTeams();
                return postseason;
            } else {
                setTeamRanks();
                for (int i = 0; i < teamList.size(); ++i) {
                    teamList.get(i).updatePollScore();
                    if (teamList.get(i).bowlBan)
                        teamList.get(i).teamPollScore = 0;
                }
                Collections.sort(teamList, new CompTeamPoll());

                StringBuilder sb = new StringBuilder();
                Team t1;
                Team t2;

                sb.append("Semifinal 1v4:\n\t\t");
                t1 = teamList.get(0);
                t2 = teamList.get(3);
                sb.append(t1.strRep() + " vs " + t2.strRep() + "\n\n");

                sb.append("Semifinal 2v3:\n\t\t");
                t1 = teamList.get(1);
                t2 = teamList.get(2);
                sb.append(t1.strRep() + " vs " + t2.strRep() + "\n\n");

                int t = 4;
                for (int g = 0; g < bowlNames.length; g++) {
                    sb.append(bowlNames[g] + ":\n\t\t");
                    t1 = teamList.get(t);
                    t2 = teamList.get(t + 4);
                    sb.append(t1.strRep() + " vs " + t2.strRep() + "\n\n");
                    t++;
                    if (t % 8 == 0) t = t + 8;
                }

                return sb.toString();

            }
        } else {

            if (expPlayoffs) {

                return postseason;

            } else {
                // Games have already been scheduled, give actual teams
                StringBuilder sb = new StringBuilder();

                sb.append("Semifinal 1v4:\n");
                sb.append(getGameSummaryBowl(semiG14));

                sb.append("\n\nSemifinal 2v3:\n");
                sb.append(getGameSummaryBowl(semiG23));

                for (int i = 0; i < bowlGames.length; ++i) {
                    if (bowlGames[i] != null) {
                        sb.append("\n\n" + bowlNames[i] + ":\n");
                        sb.append(getGameSummaryBowl(bowlGames[i]));
                    }
                }

                return sb.toString();
            }

        }
    }

    //
    // EXPANDED PLAYOFFS MODE
    //

    public void getPlayoffTeams() {
        playoffTeams.clear();
        for (int i = 0; i < teamList.size(); ++i) {
            teamList.get(i).updatePollScore();
            //bowl ban!
            if (teamList.get(i).bowlBan) teamList.get(i).teamPollScore = 0;

            //minimum wins for bowl eligibility
            if (teamList.get(i).wins < 7) teamList.get(i).teamPollScore = 0;

        }
        Collections.sort(teamList, new CompTeamPoll());

        if (currentWeek > 13) {
            for (int i = 0; i < teamList.size(); i++) {
                if (teamList.get(i).confChampion.equals("CC")) {
                    playoffTeams.add(teamList.get(i));
                }
            }
            int x = 0;
            for (int i = 0; i < teamList.size(); i++) {
                if (!teamList.get(i).confChampion.equals("CC")) {
                    playoffTeams.add(teamList.get(i));
                    x++;
                    if (x >= 16 - playoffTeams.size()) break;
                }
            }
        } else {
            for (int i = 0; i < conferences.size(); i++) {
                Collections.sort(conferences.get(i).confTeams, new CompTeamConfWins());
                playoffTeams.add(conferences.get(i).confTeams.get(0));
            }

            int x = 0;
            for (int i = 0; i < teamList.size(); i++) {
                if (!playoffTeams.contains(teamList.get(i))) {
                    playoffTeams.add(teamList.get(i));
                    x++;
                    if (x >= 6) break;
                }
            }
        }

        Collections.sort(playoffTeams, new CompTeamPoll());

        StringBuilder sb = new StringBuilder();
        sb.append("The following teams are expected to make it to the Football Playoffs!\n\n");
        int i = 1;
        for (Team t : playoffTeams) {
            sb.append(i + ". " + t.strRankTeamRecord() + " [" + t.conference + "]\n");
            i++;
        }
        postseason = sb.toString();
    }

    public void expandedPlayoff() {
        hasScheduledBowls = true;
        playoffWeek = 1;
        getPlayoffTeams();

        for (int i = 0; i < 8; i++) {
            cfpGames[i] = new Game(playoffTeams.get(i), playoffTeams.get(15 - i), "Sweet 16");
            playoffTeams.get(i).gameSchedule.add(cfpGames[i]);
            playoffTeams.get(15 - i).gameSchedule.add(cfpGames[i]);
            newsStories.get(currentWeek + 1).add("Upcoming Sweet 16 Playoff Games!>" + playoffTeams.get(i).getStrAbbrWL() + " will battle with " + playoffTeams.get(15 - i).getStrAbbrWL() +
                    " in the " + getYear() + " Sweet 16 round of the Playoffs!");
            weeklyScores.get(currentWeek + 2).add(cfpGames[i].gameName + ">#" + cfpGames[i].awayTeam.rankTeamPollScore + " " + cfpGames[i].awayTeam.name + "\n" + "#" + cfpGames[i].homeTeam.rankTeamPollScore + " " + cfpGames[i].homeTeam.name);

        }

        //Heal teams
        for (int i = 0; i < teamList.size(); i++) {
            teamList.get(i).postSeasonHealing(1);
        }
    }


    public void expPlayoffSchdQT() {
        Collections.sort(playoffTeams, new CompTeamPoll());

        for (int i = 8; i < 12; i++) {
            cfpGames[i] = new Game(playoffTeams.get(i - 8), playoffTeams.get(15 - i), "Elite 8");
            playoffTeams.get(i - 8).gameSchedule.add(cfpGames[i]);
            playoffTeams.get(15 - i).gameSchedule.add(cfpGames[i]);
            newsStories.get(currentWeek + 1).add("Elite 8 Match-up Announced!>" + playoffTeams.get(i - 8).getStrAbbrWL() + " will take on " + playoffTeams.get(15 - i).getStrAbbrWL() +
                    " in the " + getYear() + " Elite 8!");
            weeklyScores.get(currentWeek + 2).add(cfpGames[i].gameName + ">#" + cfpGames[i].awayTeam.rankTeamPollScore + " " + cfpGames[i].awayTeam.name + "\n" + "#" + cfpGames[i].homeTeam.rankTeamPollScore + " " + cfpGames[i].homeTeam.name);
        }
    }

    public void expPlayoffSchdSemi() {
        Collections.sort(playoffTeams, new CompTeamPoll());

        for (int i = 12; i < 14; i++) {
            cfpGames[i] = new Game(playoffTeams.get(i - 12), playoffTeams.get(15 - i), "Final Four");
            playoffTeams.get(i - 12).gameSchedule.add(cfpGames[i]);
            playoffTeams.get(15 - i).gameSchedule.add(cfpGames[i]);
            newsStories.get(currentWeek + 1).add("Final Four Announced!>" + playoffTeams.get(i - 12).getStrAbbrWL() + " and " + playoffTeams.get(15 - i).getStrAbbrWL() +
                    " will play each other in the " + getYear() + " Final Four!");
            weeklyScores.get(currentWeek + 2).add(cfpGames[i].gameName + ">#" + cfpGames[i].awayTeam.rankTeamPollScore + " " + cfpGames[i].awayTeam.name + "\n" + "#" + cfpGames[i].homeTeam.rankTeamPollScore + " " + cfpGames[i].homeTeam.name);
        }
    }

    public void expPlayoffSchFinals() {
        Collections.sort(playoffTeams, new CompTeamPoll());

        ncg = new Game(playoffTeams.get(0), playoffTeams.get(1), "Championship");
        playoffTeams.get(0).gameSchedule.add(ncg);
        playoffTeams.get(1).gameSchedule.add(ncg);
        newsStories.get(currentWeek + 1).add("The Upcoming National Title Game!>" + playoffTeams.get(0).getStrAbbrWL() + " and " + playoffTeams.get(1).getStrAbbrWL() +
                " are the last two teams left in the " + getYear() + " College Football Playoffs. These teams will compete next weekend for the National Title!");
        weeklyScores.get(currentWeek + 2).add(ncg.gameName + ">#" + ncg.awayTeam.rankTeamPollScore + " " + ncg.awayTeam.name + "\n" + "#" + ncg.homeTeam.rankTeamPollScore + " " + ncg.homeTeam.name);
    }

    public void playExpPlayoffSweet16() {
        playoffWeek = 1;
        for (int i = 0; i < 8; i++) {
            playPlayoff(cfpGames[i]);
        }
        expPlayoffSchdQT();
    }

    public void playExpPlayoffQTF() {
        playoffWeek = 2;
        for (int i = 8; i < 12; i++) {
            playPlayoff(cfpGames[i]);
        }
        expPlayoffSchdSemi();
    }

    public void playExpPlayoffSemi() {
        playoffWeek = 3;
        for (int i = 12; i < 14; i++) {
            playPlayoff(cfpGames[i]);
        }
        expPlayoffSchFinals();
    }

    private void playPlayoff(Game g) {
        g.playGame();

        if (playoffWeek == 1) {
            if (g.homeScore > g.awayScore) {
                g.homeTeam.sweet16 = "S16W";
                g.awayTeam.sweet16 = "S16L";
                g.homeTeam.totalBowls++;
                g.awayTeam.totalBowlLosses++;
                g.homeTeam.HC.get(0).bowlwins++;
                g.awayTeam.HC.get(0).bowllosses++;
                newsStories.get(currentWeek + 1).add(
                        g.homeTeam.name + " wins the " + g.gameName + "!>" +
                                g.homeTeam.strRep() + " defeats " + g.awayTeam.strRep() +
                                " in the " + g.gameName + ", winning " + g.homeScore + " to " + g.awayScore + "."
                );
                playoffTeams.remove(g.awayTeam);
            } else {
                g.homeTeam.sweet16 = "S16L";
                g.awayTeam.sweet16 = "S16W";
                g.homeTeam.totalBowlLosses++;
                g.awayTeam.totalBowls++;
                g.awayTeam.HC.get(0).bowlwins++;
                g.homeTeam.HC.get(0).bowllosses++;
                newsStories.get(currentWeek + 1).add(
                        g.awayTeam.name + " wins the " + g.gameName + "!>" +
                                g.awayTeam.strRep() + " defeats " + g.homeTeam.strRep() +
                                " in the " + g.gameName + ", winning " + g.awayScore + " to " + g.homeScore + "."
                );
                playoffTeams.remove(g.homeTeam);
            }
        }

        if (playoffWeek == 2) {
            g.homeTeam.sweet16 = "";
            g.awayTeam.sweet16 = "";
            if (g.homeScore > g.awayScore) {
                g.homeTeam.qtFinalWL = "QTW";
                g.awayTeam.qtFinalWL = "QTL";
                g.homeTeam.totalBowls++;
                g.awayTeam.totalBowlLosses++;
                g.homeTeam.HC.get(0).bowlwins++;
                g.awayTeam.HC.get(0).bowllosses++;
                newsStories.get(currentWeek + 1).add(
                        g.homeTeam.name + " wins the " + g.gameName + "!>" +
                                g.homeTeam.strRep() + " defeats " + g.awayTeam.strRep() +
                                " in the " + g.gameName + ", winning " + g.homeScore + " to " + g.awayScore + "."
                );
                playoffTeams.remove(g.awayTeam);
            } else {
                g.homeTeam.qtFinalWL = "QTL";
                g.awayTeam.qtFinalWL = "QTW";
                g.homeTeam.totalBowlLosses++;
                g.awayTeam.totalBowls++;
                g.awayTeam.HC.get(0).bowlwins++;
                g.homeTeam.HC.get(0).bowllosses++;
                newsStories.get(currentWeek + 1).add(
                        g.awayTeam.name + " wins the " + g.gameName + "!>" +
                                g.awayTeam.strRep() + " defeats " + g.homeTeam.strRep() +
                                " in the " + g.gameName + ", winning " + g.awayScore + " to " + g.homeScore + "."
                );
                playoffTeams.remove(g.homeTeam);
            }
        }

        if (playoffWeek == 3) {
            g.homeTeam.qtFinalWL = "";
            g.awayTeam.qtFinalWL = "";
            if (g.homeScore > g.awayScore) {
                g.homeTeam.semiFinalWL = "SFW";
                g.awayTeam.semiFinalWL = "SFL";
                g.homeTeam.totalBowls++;
                g.awayTeam.totalBowlLosses++;
                g.homeTeam.HC.get(0).bowlwins++;
                g.awayTeam.HC.get(0).bowllosses++;
                newsStories.get(currentWeek + 1).add(
                        g.homeTeam.name + " wins the " + g.gameName + "!>" +
                                g.homeTeam.strRep() + " defeats " + g.awayTeam.strRep() +
                                " in the " + g.gameName + ", winning " + g.homeScore + " to " + g.awayScore + "."
                );
                playoffTeams.remove(g.awayTeam);
            } else {
                g.homeTeam.semiFinalWL = "SFL";
                g.awayTeam.semiFinalWL = "SFW";
                g.homeTeam.totalBowlLosses++;
                g.awayTeam.totalBowls++;
                g.awayTeam.HC.get(0).bowlwins++;
                g.homeTeam.HC.get(0).bowllosses++;
                newsStories.get(currentWeek + 1).add(
                        g.awayTeam.name + " wins the " + g.gameName + "!>" +
                                g.awayTeam.strRep() + " defeats " + g.homeTeam.strRep() +
                                " in the " + g.gameName + ", winning " + g.awayScore + " to " + g.homeScore + "."
                );
                playoffTeams.remove(g.homeTeam);
            }
        }

    }

    /////////////////////////

    /* New Bowl Scheduling Logic
    Only teams qualify make it to bowl games. bowl games max out at total bowl length. if not enough teams qualify, bowl size shrinks.
     */


    private ArrayList<Team> getQualifiedTeams() {
        ArrayList<Team> bowlTeams = new ArrayList<>();

        for (int i = 0; i < teamList.size(); ++i) {
            if (!teamList.get(i).bowlBan && teamList.get(i).wins > 6)
                bowlTeams.add(teamList.get(i));
        }

        return bowlTeams;
    }

    private void bowlScheduleLogic() {
        setTeamRanks();
        ArrayList<Team> bowlTeams = getQualifiedTeams();
        Collections.sort(bowlTeams, new CompTeamPoll());

        int bowlCount = (bowlTeams.size() - 4) / 2;
        if (bowlCount > bowlNames.length) bowlCount = bowlNames.length;

        //semifinals
        semiG14 = new Game(bowlTeams.get(0), bowlTeams.get(3), "Semis, 1v4");
        bowlTeams.get(0).gameSchedule.add(semiG14);
        bowlTeams.get(3).gameSchedule.add(semiG14);

        semiG23 = new Game(bowlTeams.get(1), bowlTeams.get(2), "Semis, 2v3");
        bowlTeams.get(1).gameSchedule.add(semiG23);
        bowlTeams.get(2).gameSchedule.add(semiG23);

        newsStories.get(currentWeek + 1).add("Playoff Teams Announced!>" + bowlTeams.get(0).getStrAbbrWL() + " will play " + bowlTeams.get(3).getStrAbbrWL() +
                " , while " + bowlTeams.get(1).getStrAbbrWL() + " will play " + bowlTeams.get(2).getStrAbbrWL() + " in next week's College Football Playoff semi-final round. The winners will compete for this year's National Title!");

        weeklyScores.get(currentWeek + 4).add(semiG14.gameName + ">#" + semiG14.awayTeam.rankTeamPollScore + " " + semiG14.awayTeam.name + "\n" + "#" + semiG14.homeTeam.rankTeamPollScore + " " + semiG14.homeTeam.name);
        weeklyScores.get(currentWeek + 4).add(semiG23.gameName + ">#" + semiG23.awayTeam.rankTeamPollScore + " " + semiG23.awayTeam.name + "\n" + "#" + semiG23.homeTeam.rankTeamPollScore + " " + semiG23.homeTeam.name);


        //schedule bowl games teams ranked #5-12
        int g = 0; //game #
        int r = 1; //rounds #
        int t = 0; //team #

        while (bowlCount / 4 >= r) {
            for (int i = t + 4; i < t + 8; i++) {
                bowlGames[g] = new Game(bowlTeams.get(i), bowlTeams.get(i + 4), bowlNames[g]);
                bowlTeams.get(i).gameSchedule.add(bowlGames[g]);
                bowlTeams.get(i + 4).gameSchedule.add(bowlGames[g]);
                newsStories.get(currentWeek + 1).add(bowlGames[g].gameName + " Announced!>" + "#" + bowlTeams.get(i).rankTeamPollScore + " " + bowlTeams.get(i).getStrAbbrWL() + " will compete with " + "#" + bowlTeams.get(i + 4).rankTeamPollScore + " " + bowlTeams.get(i + 4).getStrAbbrWL() +
                        " in the " + getYear() + " " + bowlGames[g].gameName + "!");
                if(g < 6) weeklyScores.get(currentWeek + 4).add(bowlGames[g].gameName + ">#" + bowlGames[g].awayTeam.rankTeamPollScore + " " + bowlGames[g].awayTeam.name + "\n" + "#" + bowlGames[g].homeTeam.rankTeamPollScore + " " + bowlGames[g].homeTeam.name);
                else if(g < 16) weeklyScores.get(currentWeek + 3).add(bowlGames[g].gameName + ">#" + bowlGames[g].awayTeam.rankTeamPollScore + " " + bowlGames[g].awayTeam.name + "\n" + "#" + bowlGames[g].homeTeam.rankTeamPollScore + " " + bowlGames[g].homeTeam.name );
                else weeklyScores.get(currentWeek + 2).add(bowlGames[g].gameName + ">#" + bowlGames[g].awayTeam.rankTeamPollScore + " " + bowlGames[g].awayTeam.name + "\n" + "#" + bowlGames[g].homeTeam.rankTeamPollScore + " " + bowlGames[g].homeTeam.name);

                g++;
            }
            t = t + 8;
            r++;
        }

        hasScheduledBowls = true;

        //Heal Bowl Team Players
        int weekGame = (bowlCount) / 2;

        for (int i = 0; i < 16; i++) {
            teamList.get(i).postSeasonHealing(3);
        }
        for (int i = 16; i < 36; i++) {
            teamList.get(i).postSeasonHealing(2);
        }
        for (int i = 36; i < teamList.size(); i++) {
            teamList.get(i).postSeasonHealing(1);
        }

    }

    /**
     * Actually plays each bowl game.
     */


    private void playBowlWeek1() {
        for (int g = 16; g < bowlGames.length; g++) {
            if(bowlGames[g] != null) playBowl(bowlGames[g]);
        }
    }

    private void playBowlWeek2() {
        for (int g = 6; g < bowlGames.length; g++) {
            if(bowlGames[g] != null) playBowl(bowlGames[g]);
        }
    }

    private void playBowlWeek3() {
        for (int g = 0; g < bowlGames.length; g++) {
            if(bowlGames[g] != null) playBowl(bowlGames[g]);
        }

        semiG14.playGame();
        semiG23.playGame();
        Team semi14winner;
        Team semi23winner;
        if (semiG14.homeScore > semiG14.awayScore) {
            semiG14.homeTeam.semiFinalWL = "SFW";
            semiG14.awayTeam.semiFinalWL = "SFL";
            semiG14.awayTeam.totalBowlLosses++;
            semiG14.homeTeam.totalBowls++;
            semiG14.homeTeam.HC.get(0).bowlwins++;
            semiG14.awayTeam.HC.get(0).bowllosses++;
            semi14winner = semiG14.homeTeam;
            newsStories.get(currentWeek + 1).add(
                    semiG14.homeTeam.name + " wins the " + semiG14.gameName + "!>" +
                            semiG14.homeTeam.strRep() + " defeats " + semiG14.awayTeam.strRep() +
                            " in the semifinals, winning " + semiG14.homeScore + " to " + semiG14.awayScore + ". " +
                            semiG14.homeTeam.name + " advances to the National Championship!"

            );
        } else {
            semiG14.homeTeam.semiFinalWL = "SFL";
            semiG14.awayTeam.semiFinalWL = "SFW";
            semiG14.homeTeam.totalBowlLosses++;
            semiG14.awayTeam.totalBowls++;
            semiG14.awayTeam.HC.get(0).bowlwins++;
            semiG14.homeTeam.HC.get(0).bowllosses++;
            semi14winner = semiG14.awayTeam;
            newsStories.get(currentWeek + 1).add(
                    semiG14.awayTeam.name + " wins the " + semiG14.gameName + "!>" +
                            semiG14.awayTeam.strRep() + " defeats " + semiG14.homeTeam.strRep() +
                            " in the semifinals, winning " + semiG14.awayScore + " to " + semiG14.homeScore + ". " +
                            semiG14.awayTeam.name + " advances to the National Championship!"

            );
        }
        if (semiG23.homeScore > semiG23.awayScore) {
            semiG23.homeTeam.semiFinalWL = "SFW";
            semiG23.awayTeam.semiFinalWL = "SFL";
            semiG23.homeTeam.totalBowls++;
            semiG23.awayTeam.totalBowlLosses++;
            semiG23.homeTeam.HC.get(0).bowlwins++;
            semiG23.awayTeam.HC.get(0).bowllosses++;
            semi23winner = semiG23.homeTeam;
            newsStories.get(currentWeek + 1).add(
                    semiG23.homeTeam.name + " wins the " + semiG23.gameName + "!>" +
                            semiG23.homeTeam.strRep() + " defeats " + semiG23.awayTeam.strRep() +
                            " in the semifinals, winning " + semiG23.homeScore + " to " + semiG23.awayScore + ". " +
                            semiG23.homeTeam.name + " advances to the National Championship!"

            );
        } else {
            semiG23.homeTeam.semiFinalWL = "SFL";
            semiG23.awayTeam.semiFinalWL = "SFW";
            semiG23.awayTeam.totalBowls++;
            semiG23.homeTeam.totalBowlLosses++;
            semiG23.awayTeam.HC.get(0).bowlwins++;
            semiG23.homeTeam.HC.get(0).bowllosses++;
            semi23winner = semiG23.awayTeam;
            newsStories.get(currentWeek + 1).add(
                    semiG23.awayTeam.name + " wins the " + semiG23.gameName + "!>" +
                            semiG23.awayTeam.strRep() + " defeats " + semiG23.homeTeam.strRep() +
                            " in the semifinals, winning " + semiG23.awayScore + " to " + semiG23.homeScore + ". " +
                            semiG23.awayTeam.name + " advances to the National Championship!"

            );
        }

        //schedule NCG
        ncg = new Game(semi14winner, semi23winner, "NCG");
        semi14winner.gameSchedule.add(ncg);
        semi23winner.gameSchedule.add(ncg);
        newsStories.get(currentWeek + 1).add("Upcoming National Title Game!>" + semi14winner.getStrAbbrWL() + " will compete with " + semi23winner.getStrAbbrWL() +
                " for the " + getYear() + " College Football National Title!");
        weeklyScores.get(currentWeek + 2).add(ncg.gameName + ">#" + ncg.awayTeam.rankTeamPollScore + " " + ncg.awayTeam.name + "\n" + "#" + ncg.homeTeam.rankTeamPollScore + " " + ncg.homeTeam.name);

    }

    /**
     * Plays a particular bowl game
     *
     * @param g bowl game to be played
     */
    private void playBowl(Game g) {
        if(!g.hasPlayed) {
            g.playGame();
            if (g.homeScore > g.awayScore) {
                g.homeTeam.semiFinalWL = "BW";
                g.awayTeam.semiFinalWL = "BL";
                g.homeTeam.totalBowls++;
                g.awayTeam.totalBowlLosses++;
                g.homeTeam.HC.get(0).bowlwins++;
                g.awayTeam.HC.get(0).bowllosses++;
                newsStories.get(currentWeek + 1).add(
                        g.homeTeam.name + " wins the " + g.gameName + "!>" +
                                g.homeTeam.strRep() + " defeats " + g.awayTeam.strRep() +
                                " in the " + g.gameName + ", winning " + g.homeScore + " to " + g.awayScore + "."
                );
            } else {
                g.homeTeam.semiFinalWL = "BL";
                g.awayTeam.semiFinalWL = "BW";
                g.homeTeam.totalBowlLosses++;
                g.awayTeam.totalBowls++;
                g.awayTeam.HC.get(0).bowlwins++;
                g.homeTeam.HC.get(0).bowllosses++;
                newsStories.get(currentWeek + 1).add(
                        g.awayTeam.name + " wins the " + g.gameName + "!>" +
                                g.awayTeam.strRep() + " defeats " + g.homeTeam.strRep() +
                                " in the " + g.gameName + ", winning " + g.awayScore + " to " + g.homeScore + "."
                );
            }
        }
    }

    /**
     * Get string of what happened in a particular bowl
     *
     * @param g Bowl game to be examined
     * @return string of its summary, ALA W 24 - 40 @ GEO, etc
     */
    private String getGameSummaryBowl(Game g) {
        StringBuilder sb = new StringBuilder();
        Team winner, loser;
        if (!g.hasPlayed) {
            return g.homeTeam.strRep() + " vs " + g.awayTeam.strRep();
        } else {
            if (g.homeScore > g.awayScore) {
                winner = g.homeTeam;
                loser = g.awayTeam;
                sb.append(winner.strRep() + " W ");
                sb.append(g.homeScore + "-" + g.awayScore + " ");
                sb.append("vs " + loser.strRep());
                return sb.toString();
            } else {
                winner = g.awayTeam;
                loser = g.homeTeam;
                sb.append(winner.strRep() + " W ");
                sb.append(g.awayScore + "-" + g.homeScore + " ");
                sb.append("@ " + loser.strRep());
                return sb.toString();
            }
        }
    }


    /**
     * Get summary of what happened in the NCG
     *
     * @return string of summary
     */
    private String ncgSummaryStr() {
        // Give summary of what happened in the NCG
        if (ncg.homeScore > ncg.awayScore) {
            return ncg.homeTeam.name + " (" + ncg.homeTeam.wins + "-" + ncg.homeTeam.losses + ") won the National Championship, " +
                    "winning against " + ncg.awayTeam.name + " (" + ncg.awayTeam.wins + "-" + ncg.awayTeam.losses + ") in the NCG " +
                    ncg.homeScore + "-" + ncg.awayScore + ".";
        } else {
            return ncg.awayTeam.name + " (" + ncg.awayTeam.wins + "-" + ncg.awayTeam.losses + ") won the National Championship, " +
                    "winning against " + ncg.homeTeam.name + " (" + ncg.homeTeam.wins + "-" + ncg.homeTeam.losses + ") in the NCG " +
                    ncg.awayScore + "-" + ncg.homeScore + ".";
        }
    }

    /**
     * Get summary of season.
     *
     * @return ncgSummary, userTeam's summary
     */
    public void enterOffseason() {
        for (Team t : teamList) {
            t.enterOffSeason();
        }
    }

    public String seasonSummaryStr() {
        setTeamRanks();
        StringBuilder sb = new StringBuilder();
        sb.append(ncgSummaryStr());
        sb.append("\n\n" + userTeam.seasonSummaryStr());
        sb.append("\n\n" + leagueRecords.brokenRecordsStr(getYear(), userTeam.abbr));
        return sb.toString();
    }


    public void advanceHC() {
        coachList.clear();
        coachStarList.clear();
        Collections.sort(teamList, new CompTeamPrestige());
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).advanceHC(leagueRecords, teamList.get(t).teamRecords);
            teamList.get(t).checkFacilitiesUpgradeBonus();
        }
    }

    public void updateHCHistory() {
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).updateCoachHistory();
        }

    }

    //New Hard Game
    public ArrayList<Team> getCoachList() {
        ArrayList<Team> teamVacancies = new ArrayList<>();

        for (int c = 0; c < conferences.size(); c++) {
            teamVacancies.add(conferences.get(c).confTeams.get(10));
            teamVacancies.add(conferences.get(c).confTeams.get(11));
        }
        return teamVacancies;
    }

    //Get Coach Job Offers List if fired or quit
    public ArrayList<Team> getCoachListFired(int rating, String oldTeam) {
        ArrayList<Team> teamVacancies = new ArrayList<>();
        for (int i = 0; i < teamList.size(); ++i) {
            if (teamList.get(i).getMinCoachHireReq() < rating && teamList.get(i).HC.isEmpty() && teamList.get(i).name != oldTeam) {
                teamVacancies.add(teamList.get(i));
            }
        }

        if (teamVacancies.isEmpty()) {
            teamVacancies = getCoachList();
        }
        return teamVacancies;
    }

    //Get Coach Job Offers List for Team Transfer
    public ArrayList<Team> getCoachPromotionList(int rating, double offers, String oldTeam) {
        ArrayList<Team> teamVacancies = new ArrayList<>();
        for (int i = 0; i < teamList.size(); ++i) {
            if (teamList.get(i).getMinCoachHireReq() < rating && teamList.get(i).HC.isEmpty() && teamList.get(i).name != oldTeam && offers > 0.50) {
                teamVacancies.add(teamList.get(i));
            }
        }

        return teamVacancies;
    }

    //Transferring Jobs
    public void newJobtransfer(String coachTeam) {
        for (int i = 0; i < teamList.size(); ++i) {
            if (teamList.get(i).name.equals(coachTeam)) {
                teamList.get(i).userControlled = true;
                userTeam = teamList.get(i);
            }
        }
    }

    //COACHING CAROUSEL HIRING METHOD
    //THIS METHOD TAKES THE COACH LIST CREATED AFTER FIRING AND PUTS IT INTO A POPULATION FOR TEAMS WITH NO COACHES TO HIRE FROM
    public void coachCarousel() {
        Collections.sort(teamList, new CompTeamPrestige());
        //Rising Star Coaches
        for (int i = 0; i < coachStarList.size(); ++i) {
            String tmName = coachStarList.get(i).team.name;
            int tmPres = coachStarList.get(i).team.teamPrestige;
            int cPres = coachStarList.get(i).team.confPrestige;

            for (int t = 0; t < teamList.size(); ++t) {
                if (teamList.get(t).HC.isEmpty() && coachStarList.get(i).ratOvr >= teamList.get(t).getMinCoachHireReq() && teamList.get(t).name != tmName && Math.random() > 0.66) {
                    if (teamList.get(t).teamPrestige > tmPres && teamList.get(t).confPrestige > cPres || teamList.get(t).teamPrestige > tmPres + 5 || teamList.get(t).confPrestige + 10 > cPres) {
                        teamList.get(t).HC.add(coachStarList.get(i));
                        teamList.get(t).HC.get(0).contractLength = 6;
                        teamList.get(t).HC.get(0).contractYear = 0;
                        teamList.get(t).HC.get(0).baselinePrestige = teamList.get(t).teamPrestige;
                        newsStories.get(currentWeek + 1).add("Rising Star Coach Hired: " + teamList.get(t).name + ">Rising star head coach " + teamList.get(t).HC.get(0).name + " has announced his departure from " +
                                tmName + " after being selected by " + teamList.get(t).name + " as their new head coach. His previous track record has had him on the top list of many schools.");

                        for (int j = 0; j < teamList.size(); ++j) {
                            if (teamList.get(j).name.equals(tmName)) {
                                teamList.get(j).HC.remove(0);
                                if (Math.random() > 0.20) {
                                    teamList.get(j).promoteCoach();
                                    teamList.get(j).HC.get(0).history.add("");
                                    newsStories.get(currentWeek + 1).add("Replacement Promoted: " + teamList.get(j).name + ">" + teamList.get(j).name +
                                            " hopes to continue their recent success, despite the recent loss of coach " + teamList.get(t).HC.get(0).name + ". The team has promoted his assistant coach " + teamList.get(j).HC.get(0).name + " to the head coaching job at the school.");
                                }
                            }
                        }
                        break;
                    }
                }
            }
        }

        //Coaches who were fired
        Collections.sort(coachList, new CompCoachOvr());
        for (int i = 0; i < coachList.size(); ++i) {
            for (int t = 0; t < teamList.size(); ++t) {
                if (teamList.get(t).HC.isEmpty() && coachList.get(i).ratOvr >= teamList.get(t).getMinCoachHireReq() && teamList.get(t).name != coachList.get(i).team.name && Math.random() > 0.60) {
                    teamList.get(t).HC.add(coachList.get(i));
                    teamList.get(t).HC.get(0).contractLength = 6;
                    teamList.get(t).HC.get(0).contractYear = 0;
                    teamList.get(t).HC.get(0).baselinePrestige = teamList.get(t).teamPrestige;
                    newsStories.get(currentWeek + 1).add("Coaching Switch: " + teamList.get(t).name + ">After an extensive search for a new head coach, " + teamList.get(t).name + " has hired " + teamList.get(t).HC.get(0).name +
                            " to lead the team. Coach " + teamList.get(t).HC.get(0).name + " previously coached at " + coachList.get(i).team.name + ", before being let go this past season.");
                    coachList.remove(i);
                    break;
                }
            }
        }

        //Coaches who were fired previous years
        Collections.sort(coachFreeAgents, new CompCoachOvr());
        for (int i = 0; i < coachFreeAgents.size(); ++i) {
            for (int t = 0; t < teamList.size(); ++t) {
                if (teamList.get(t).HC.isEmpty() && coachFreeAgents.get(i).ratOvr >= teamList.get(t).getMinCoachHireReq() && Math.random() < 0.60) {
                    teamList.get(t).HC.add(coachFreeAgents.get(i));
                    teamList.get(t).HC.get(0).contractLength = 6;
                    teamList.get(t).HC.get(0).contractYear = 0;
                    teamList.get(t).HC.get(0).baselinePrestige = teamList.get(t).teamPrestige;
                    newsStories.get(currentWeek + 1).add("Return to the Sidelines: " + teamList.get(t).name + ">After an extensive search for a new head coach, " + teamList.get(t).name + " has hired " + teamList.get(t).HC.get(0).name +
                            " to lead the team. Coach " + teamList.get(t).HC.get(0).name + " has been out of football, but is returning this season!");
                    coachFreeAgents.remove(i);
                    break;
                }
            }
        }


        //Assistants Promoted
        for (int t = 0; t < teamList.size(); ++t) {
            if (teamList.get(t).HC.isEmpty()) {
                teamList.get(t).promoteCoach();
                teamList.get(t).HC.get(0).history.add("");
                newsStories.get(currentWeek + 1).add("Coaching Promotion: " + teamList.get(t).name + ">Following the departure of their previous head coach, " + teamList.get(t).name + " has promoted assistant " + teamList.get(t).HC.get(0).name +
                        " to lead the team.");
            }
        }
    }

    //Hiring method for teams that get poached
    public void coachHiringSingleTeam(Team school) {
        //Rising Star Coaches
        for (int i = 0; i < coachStarList.size(); ++i) {
            String tmName = coachStarList.get(i).team.name;
            int tmPres = coachStarList.get(i).team.teamPrestige;
            int cPres = coachStarList.get(i).team.confPrestige;
            if (coachStarList.get(i).ratOvr >= school.getMinCoachHireReq() && school.name != tmName && Math.random() > 0.60) {
                if (school.teamPrestige > tmPres && school.confPrestige > cPres || school.teamPrestige > tmPres + 5 || school.confPrestige + 10 > cPres) {
                    school.HC.add(coachStarList.get(i));
                    school.HC.get(0).contractLength = 6;
                    school.HC.get(0).contractYear = 0;
                    school.HC.get(0).baselinePrestige = school.teamPrestige;
                    newsStories.get(currentWeek + 1).add("Rising Star Coach Hired: " + school.name + ">Rising star head coach " + school.HC.get(0).name + " has announced his departure from " +
                            tmName + " after being selected by " + school.name + " as their new head coach. His previous track record has had him on the top list of many schools.");

                    for (int j = 0; j < teamList.size(); ++j) {
                        if (teamList.get(j).name.equals(tmName)) {
                            teamList.get(j).HC.remove(0);
                            if (Math.random() > 0.25) {
                                teamList.get(j).promoteCoach();
                                teamList.get(j).HC.get(0).history.add("");
                                newsStories.get(currentWeek + 1).add("Replacement Promoted: " + teamList.get(j).name + ">" + teamList.get(j).name +
                                        " hopes to continue their recent success, despite the recent loss of coach " + school.HC.get(0).name + ". The team has promoted his assistant coach " + teamList.get(j).HC.get(0).name + " to the head coaching job at the school.");
                            } else {
                                coachHiringSingleTeam(teamList.get(j));
                            }
                        }
                    }
                    break;
                }
            }
        }

        if (school.HC.isEmpty()) {
            //Coaches who were fired
            Collections.sort(coachList, new CompCoachOvr());
            for (int i = 0; i < coachList.size(); ++i) {
                if (school.HC.isEmpty() && coachList.get(i).ratOvr + 5 >= school.getMinCoachHireReq() && school.name != coachList.get(i).team.name && Math.random() > 0.45) {
                    school.HC.add(coachList.get(i));
                    school.HC.get(0).contractLength = 6;
                    school.HC.get(0).contractYear = 0;
                    school.HC.get(0).baselinePrestige = school.teamPrestige;
                    newsStories.get(currentWeek + 1).add("Coaching Change: " + school.name + ">After an extensive search for a new head coach, " + school.name + " has hired " + school.HC.get(0).name +
                            " to lead the team. Coach " + school.HC.get(0).name + " previously coached at " + coachList.get(i).team.name + ", before being let go this past season.");
                    coachList.remove(i);
                    break;
                }
            }
        }

        if (school.HC.isEmpty()) {
            //Coaches who were fired previous years
            Collections.sort(coachFreeAgents, new CompCoachOvr());
            for (int i = 0; i < coachFreeAgents.size(); ++i) {
                if (school.HC.isEmpty() && coachFreeAgents.get(i).ratOvr >= school.getMinCoachHireReq() && Math.random() < 0.65) {
                    school.HC.add(coachFreeAgents.get(i));
                    school.HC.get(0).contractLength = 6;
                    school.HC.get(0).contractYear = 0;
                    school.HC.get(0).baselinePrestige = school.teamPrestige;
                    newsStories.get(currentWeek + 1).add("Back to Coaching: " + school.name + ">After an extensive search for a new head coach, " + school.name + " has hired " + school.HC.get(0).name +
                            " to lead the team. Coach " + school.HC.get(0).name + " has been out of football, but is returning this upcoming season!");
                    coachFreeAgents.remove(i);
                    break;
                }
            }
        }

        if (school.HC.isEmpty()) {
            school.promoteCoach();
            school.HC.get(0).history.add("");
            newsStories.get(currentWeek + 1).add("Coaching Promotion: " + school.name + ">Following the departure of their previous head coach, " + school.name + " has promoted assistant " + school.HC.get(0).name +
                    " to lead the team.");
        }
    }


    //Coaching Hot Seat News
    private void coachingHotSeat() {
        if (currentWeek == 0) {
            for (int i = 0; i < teamList.size(); ++i) {
                if (teamList.get(i).HC.get(0).baselinePrestige < teamList.get(i).teamPrestige && teamList.get(i).HC.get(0).contractYear == teamList.get(i).HC.get(0).contractLength) {
                    newsStories.get(0).add("Coaching Hot Seat: " + teamList.get(i).name + ">Head Coach " + teamList.get(i).HC.get(0).name + " has struggled over the course of his current contract with " +
                            teamList.get(i).name + " and has failed to raise the team prestige. Because this is his final contract year, the team will be evaluating whether to continue with the coach at the end of " +
                            "this season. He'll remain on the hot seat throughout this year.");
                } else if (teamList.get(i).teamPrestige > (teamList.get(i).HC.get(0).baselinePrestige + 10) && teamList.get(i).teamPrestige < teamList.get((int) (teamList.size() * 0.35)).teamPrestige) {
                    newsStories.get(0).add("Coaching Rising Star: " + teamList.get(i).HC.get(0).name + ">" + teamList.get(i).name + " head coach " + teamList.get(i).HC.get(0).name +
                            " has been building a strong program and if he continues this path, he'll be on the top of the wishlist at a major program in the future.");
                }
            }
        } else if (currentWeek == 7) {
            for (int i = 0; i < teamList.size(); ++i) {
                if (teamList.get(i).HC.get(0).baselinePrestige < teamList.get(i).teamPrestige && teamList.get(i).HC.get(0).contractYear == teamList.get(i).HC.get(0).contractLength && teamList.get(i).rankTeamPollScore > (100 - teamList.get(i).HC.get(0).baselinePrestige)) {
                    newsStories.get(currentWeek + 1).add("Coaching Hot Seat: " + teamList.get(i).name + ">Head Coach " + teamList.get(i).HC.get(0).name + " future is in jeopardy at  " +
                            teamList.get(i).name + ". The coach has failed to get out of the hot seat this season with disappointing losses and failing to live up to the school's standards.");
                }
            }
        }
    }

    //Transfer players from the available transfer lists
    public void transferPlayers() {
        Collections.sort(teamList, new CompTeamPoll());
        int rand;
        Random random = new Random();
        int max = teamList.size() - 1;
        int min = 0;

        //Transfer List Summary Builder
        transfersList = new ArrayList<>();

        //User Transfer Pop Up Dialog Builder
        userTransfers = "";
        StringBuilder tOut = new StringBuilder();
        StringBuilder tIn = new StringBuilder();
        tOut.append("Transfers Out:\n\n");
        tIn.append("Transfers In:\n\n");


        for (int loc = 1; loc < 4; ++loc) {

            for (int i = 0; i < transferQBs.size(); ++i) {
                rand = random.nextInt((max - min) + 1) + min;
                for (int t = rand; t < teamList.size() - rand; ++t) {
                    if (teamList.get(t).teamQBs.size() < 1 || teamList.get(t).teamQBs.get(0).ratOvr < transferQBs.get(i).ratOvr) {
                        if (Math.abs(teamList.get(t).location - transferQBs.get(i).getRegion()) < loc) {
                            int qbTransfers = 0;
                            for (int x = 0; x < teamList.get(t).teamQBs.size(); ++x) {
                                if (teamList.get(t).teamQBs.get(x).isTransfer) qbTransfers++;
                            }
                            if (qbTransfers == 0) {
                                newsStories.get(currentWeek + 1).add(teamList.get(t).name + " Transfer News>" + transferQBs.get(i).getYrStr() + " QB " + transferQBs.get(i).name + "(" + transferQBs.get(i).ratOvr + ") has announced his transfer to " + teamList.get(t).name + ". He was previously enrolled at " +
                                        transferQBs.get(i).team.abbr + " .");
                                if (teamList.get(t).equals(userTeam) && transferQBs.get(i).isTransfer) {
                                    tIn.append(transferQBs.get(i).position + " " + transferQBs.get(i).name + ", " + transferQBs.get(i).getYrStr() + "  Ovr: " + transferQBs.get(i).ratOvr + " (" + transferQBs.get(i).team.abbr + ")\n\n");
                                }
                                if (teamList.get(t).equals(userTeam) && transferQBs.get(i).isGradTransfer) {
                                    tIn.append(transferQBs.get(i).position + " " + transferQBs.get(i).name + ", " + transferQBs.get(i).getYrStr() + " [Grad]" + "  Ovr: " + transferQBs.get(i).ratOvr + " (" + transferQBs.get(i).team.abbr + ")\n\n");
                                }
                                if (transferQBs.get(i).team.abbr.equals(userTeam.abbr)) {
                                    tOut.append(transferQBs.get(i).position + " " + transferQBs.get(i).name + ", " + transferQBs.get(i).getYrStr() + "  Ovr: " + transferQBs.get(i).ratOvr + " (" + teamList.get(t).name + ")\n\n");
                                }

                                transfersList.add(transferQBs.get(i).ratOvr + " " + transferQBs.get(i).position + " " + transferQBs.get(i).name + " [" + transferQBs.get(i).getTransferStatus() + "] " + teamList.get(t).name + " (" + transferQBs.get(i).team.abbr + ")");
                                teamList.get(t).teamQBs.add(transferQBs.get(i));
                                transferQBs.remove(i);
                                break;
                            }
                        }
                    }
                }
            }

            for (int i = 0; i < transferRBs.size(); ++i) {
                rand = random.nextInt((max - min) + 1) + min;
                for (int t = rand; t < teamList.size() - rand; ++t) {
                    if (teamList.get(t).teamRBs.size() < 2 || teamList.get(t).teamRBs.get(0).ratOvr < transferRBs.get(i).ratOvr) {
                        if (Math.abs(teamList.get(t).location - transferRBs.get(i).getRegion()) < loc) {
                            newsStories.get(currentWeek + 1).add(teamList.get(t).name + " Transfer News>" + transferRBs.get(i).getYrStr() + " RB " + transferRBs.get(i).name + "(" + transferRBs.get(i).ratOvr + ") has announced his transfer to " + teamList.get(t).name + ". He was previously enrolled at " +
                                    transferRBs.get(i).team.abbr + " .");
                            if (teamList.get(t).equals(userTeam) && transferRBs.get(i).isTransfer) {
                                tIn.append(transferRBs.get(i).position + " " + transferRBs.get(i).name + ", " + transferRBs.get(i).getYrStr() + "  Ovr: " + transferRBs.get(i).ratOvr + " (" + transferRBs.get(i).team.abbr + ")\n\n");
                            }
                            if (teamList.get(t).equals(userTeam) && transferRBs.get(i).isGradTransfer) {
                                tIn.append(transferRBs.get(i).position + " " + transferRBs.get(i).name + ", " + transferRBs.get(i).getYrStr() + " [Grad]" + "  Ovr: " + transferRBs.get(i).ratOvr + " (" + transferRBs.get(i).team.abbr + ")\n\n");
                            }
                            if (transferRBs.get(i).team.abbr.equals(userTeam.abbr)) {
                                tOut.append(transferRBs.get(i).position + " " + transferRBs.get(i).name + ", " + transferRBs.get(i).getYrStr() + "  Ovr: " + transferRBs.get(i).ratOvr + " (" + teamList.get(t).name + ")\n\n");
                            }

                            transfersList.add(transferRBs.get(i).ratOvr + " " + transferRBs.get(i).position + " " + transferRBs.get(i).name + " [" + transferRBs.get(i).getTransferStatus() + "] " + teamList.get(t).name + " (" + transferRBs.get(i).team.abbr + ")");
                            teamList.get(t).teamRBs.add(transferRBs.get(i));
                            transferRBs.remove(i);
                            break;
                        }
                    }
                }
            }

            for (int i = 0; i < transferWRs.size(); ++i) {
                rand = random.nextInt((max - min) + 1) + min;
                for (int t = rand; t < teamList.size() - rand; ++t) {
                    if (teamList.get(t).teamWRs.size() < 3 || teamList.get(t).teamWRs.get(0).ratOvr < transferWRs.get(i).ratOvr) {
                        if (Math.abs(teamList.get(t).location - transferWRs.get(i).getRegion()) < 1) {
                            newsStories.get(currentWeek + 1).add(teamList.get(t).name + " Transfer News>" + transferWRs.get(i).getYrStr() + " WR " + transferWRs.get(i).name + "(" + transferWRs.get(i).ratOvr + ") has announced his transfer to " + teamList.get(t).name + ". He was previously enrolled at " +
                                    transferWRs.get(i).team.abbr + " .");
                            if (teamList.get(t).equals(userTeam) && transferWRs.get(i).isTransfer) {
                                tIn.append(transferWRs.get(i).position + " " + transferWRs.get(i).name + ", " + transferWRs.get(i).getYrStr() + "  Ovr: " + transferWRs.get(i).ratOvr + " (" + transferWRs.get(i).team.abbr + ")\n\n");
                            }
                            if (teamList.get(t).equals(userTeam) && transferWRs.get(i).isGradTransfer) {
                                tIn.append(transferWRs.get(i).position + " " + transferWRs.get(i).name + ", " + transferWRs.get(i).getYrStr() + " [Grad]" + "  Ovr: " + transferWRs.get(i).ratOvr + " (" + transferWRs.get(i).team.abbr + ")\n\n");
                            }
                            if (transferWRs.get(i).team.abbr.equals(userTeam.abbr)) {
                                tOut.append(transferWRs.get(i).position + " " + transferWRs.get(i).name + ", " + transferWRs.get(i).getYrStr() + "  Ovr: " + transferWRs.get(i).ratOvr + " (" + teamList.get(t).name + ")\n\n");
                            }

                            transfersList.add(transferWRs.get(i).ratOvr + " " + transferWRs.get(i).position + " " + transferWRs.get(i).name + " [" + transferWRs.get(i).getTransferStatus() + "] " + teamList.get(t).name + " (" + transferWRs.get(i).team.abbr + ")");
                            teamList.get(t).teamWRs.add(transferWRs.get(i));
                            transferWRs.remove(i);
                            break;
                        }
                    }
                }
            }

            for (int i = 0; i < transferTEs.size(); ++i) {
                rand = random.nextInt((max - min) + 1) + min;
                for (int t = rand; t < teamList.size() - rand; ++t) {
                    if (teamList.get(t).teamTEs.size() < 1 || teamList.get(t).teamTEs.get(0).ratOvr < transferTEs.get(i).ratOvr) {
                        if (Math.abs(teamList.get(t).location - transferTEs.get(i).getRegion()) < loc) {
                            newsStories.get(currentWeek + 1).add(teamList.get(t).name + " Transfer News>" + transferTEs.get(i).getYrStr() + " TE " + transferTEs.get(i).name + "(" + transferTEs.get(i).ratOvr + ") has announced his transfer to " + teamList.get(t).name + ". He was previously enrolled at " +
                                    transferTEs.get(i).team.abbr + " .");
                            if (teamList.get(t).equals(userTeam) && transferTEs.get(i).isTransfer) {
                                tIn.append(transferTEs.get(i).position + " " + transferTEs.get(i).name + ", " + transferTEs.get(i).getYrStr() + "  Ovr: " + transferTEs.get(i).ratOvr + " (" + transferTEs.get(i).team.abbr + ")\n\n");
                            }
                            if (teamList.get(t).equals(userTeam) && transferTEs.get(i).isGradTransfer) {
                                tIn.append(transferTEs.get(i).position + " " + transferTEs.get(i).name + ", " + transferTEs.get(i).getYrStr() + " [Grad]" + "  Ovr: " + transferTEs.get(i).ratOvr + " (" + transferTEs.get(i).team.abbr + ")\n\n");
                            }
                            if (transferTEs.get(i).team.abbr.equals(userTeam.abbr)) {
                                tOut.append(transferTEs.get(i).position + " " + transferTEs.get(i).name + ", " + transferTEs.get(i).getYrStr() + "  Ovr: " + transferTEs.get(i).ratOvr + " (" + teamList.get(t).name + ")\n\n");
                            }

                            transfersList.add(transferTEs.get(i).ratOvr + " " + transferTEs.get(i).position + " " + transferTEs.get(i).name + " [" + transferTEs.get(i).getTransferStatus() + "] " + teamList.get(t).name + " (" + transferTEs.get(i).team.abbr + ")");
                            teamList.get(t).teamTEs.add(transferTEs.get(i));
                            transferTEs.remove(i);
                            break;
                        }
                    }
                }
            }

            for (int i = 0; i < transferOLs.size(); ++i) {
                rand = random.nextInt((max - min) + 1) + min;
                for (int t = rand; t < teamList.size() - rand; ++t) {
                    if (teamList.get(t).teamOLs.size() < 5 || teamList.get(t).teamOLs.get(0).ratOvr < transferOLs.get(i).ratOvr && Math.abs(teamList.get(t).location - transferOLs.get(i).getRegion()) < loc) {
                        newsStories.get(currentWeek + 1).add(teamList.get(t).name + " Transfer News>" + transferOLs.get(i).getYrStr() + " OL " + transferOLs.get(i).name + "(" + transferOLs.get(i).ratOvr + ") has announced his transfer to " + teamList.get(t).name + ". He was previously enrolled at " +
                                transferOLs.get(i).team.abbr + " .");
                        if (teamList.get(t).equals(userTeam) && transferOLs.get(i).isTransfer) {
                            tIn.append(transferOLs.get(i).position + " " + transferOLs.get(i).name + ", " + transferOLs.get(i).getYrStr() + "  Ovr: " + transferOLs.get(i).ratOvr + " (" + transferOLs.get(i).team.abbr + ")\n\n");
                        }
                        if (teamList.get(t).equals(userTeam) && transferOLs.get(i).isGradTransfer) {
                            tIn.append(transferOLs.get(i).position + " " + transferOLs.get(i).name + ", " + transferOLs.get(i).getYrStr() + " [Grad]" + "  Ovr: " + transferOLs.get(i).ratOvr + " (" + transferOLs.get(i).team.abbr + ")\n\n");
                        }
                        if (transferOLs.get(i).team.abbr.equals(userTeam.abbr)) {
                            tOut.append(transferOLs.get(i).position + " " + transferOLs.get(i).name + ", " + transferOLs.get(i).getYrStr() + "  Ovr: " + transferOLs.get(i).ratOvr + " (" + teamList.get(t).name + ")\n\n");
                        }

                        transfersList.add(transferOLs.get(i).ratOvr + " " + transferOLs.get(i).position + " " + transferOLs.get(i).name + " [" + transferOLs.get(i).getTransferStatus() + "] " + teamList.get(t).name + " (" + transferOLs.get(i).team.abbr + ")");
                        teamList.get(t).teamOLs.add(transferOLs.get(i));
                        transferOLs.remove(i);
                        break;

                    }
                }
            }

            for (int i = 0; i < transferKs.size(); ++i) {
                rand = random.nextInt((max - min) + 1) + min;
                for (int t = rand; t < teamList.size() - rand; ++t) {
                    if (teamList.get(t).teamKs.size() < 1 || teamList.get(t).teamKs.get(0).ratOvr < transferKs.get(i).ratOvr) {
                        if (Math.abs(teamList.get(t).location - transferKs.get(i).getRegion()) < loc) {
                            newsStories.get(currentWeek + 1).add(teamList.get(t).name + " Transfer News>" + transferKs.get(i).getYrStr() + " K " + transferKs.get(i).name + "(" + transferKs.get(i).ratOvr + ") has announced his transfer to " + teamList.get(t).name + ". He was previously enrolled at " +
                                    transferKs.get(i).team.abbr + " .");
                            if (teamList.get(t).equals(userTeam) && transferKs.get(i).isTransfer) {
                                tIn.append(transferKs.get(i).position + " " + transferKs.get(i).name + ", " + transferKs.get(i).getYrStr() + "  Ovr: " + transferKs.get(i).ratOvr + " (" + transferKs.get(i).team.abbr + ")\n\n");
                            }
                            if (teamList.get(t).equals(userTeam) && transferKs.get(i).isGradTransfer) {
                                tIn.append(transferKs.get(i).position + " " + transferKs.get(i).name + ", " + transferKs.get(i).getYrStr() + " [Grad]" + "  Ovr: " + transferKs.get(i).ratOvr + " (" + transferKs.get(i).team.abbr + ")\n\n");
                            }
                            if (transferKs.get(i).team.abbr.equals(userTeam.abbr)) {
                                tOut.append(transferKs.get(i).position + " " + transferKs.get(i).name + ", " + transferKs.get(i).getYrStr() + "  Ovr: " + transferKs.get(i).ratOvr + " (" + teamList.get(t).name + ")\n\n");
                            }

                            transfersList.add(transferKs.get(i).ratOvr + " " + transferKs.get(i).position + " " + transferKs.get(i).name + " [" + transferKs.get(i).getTransferStatus() + "] " + teamList.get(t).name + " (" + transferKs.get(i).team.abbr + ")");
                            teamList.get(t).teamKs.add(transferKs.get(i));
                            transferKs.remove(i);
                            break;
                        }
                    }
                }
            }

            for (int i = 0; i < transferDLs.size(); ++i) {
                rand = random.nextInt((max - min) + 1) + min;
                for (int t = rand; t < teamList.size() - rand; ++t) {
                    if (teamList.get(t).teamDLs.size() < 4 || teamList.get(t).teamDLs.get(0).ratOvr < transferDLs.get(i).ratOvr) {
                        if (Math.abs(teamList.get(t).location - transferDLs.get(i).getRegion()) < loc) {
                            newsStories.get(currentWeek + 1).add(teamList.get(t).name + " Transfer News>" + transferDLs.get(i).getYrStr() + " DL " + transferDLs.get(i).name + "(" + transferDLs.get(i).ratOvr + ") has announced his transfer to " + teamList.get(t).name + ". He was previously enrolled at " +
                                    transferDLs.get(i).team.abbr + " .");
                            if (teamList.get(t).equals(userTeam) && transferDLs.get(i).isTransfer) {
                                tIn.append(transferDLs.get(i).position + " " + transferDLs.get(i).name + ", " + transferDLs.get(i).getYrStr() + "  Ovr: " + transferDLs.get(i).ratOvr + " (" + transferDLs.get(i).team.abbr + ")\n\n");
                            }
                            if (teamList.get(t).equals(userTeam) && transferDLs.get(i).isGradTransfer) {
                                tIn.append(transferDLs.get(i).position + " " + transferDLs.get(i).name + ", " + transferDLs.get(i).getYrStr() + " [Grad]" + "  Ovr: " + transferDLs.get(i).ratOvr + " (" + transferDLs.get(i).team.abbr + ")\n\n");
                            }
                            if (transferDLs.get(i).team.abbr.equals(userTeam.abbr)) {
                                tOut.append(transferDLs.get(i).position + " " + transferDLs.get(i).name + ", " + transferDLs.get(i).getYrStr() + "  Ovr: " + transferDLs.get(i).ratOvr + " (" + teamList.get(t).name + ")\n\n");
                            }

                            transfersList.add(transferDLs.get(i).ratOvr + " " + transferDLs.get(i).position + " " + transferDLs.get(i).name + " [" + transferDLs.get(i).getTransferStatus() + "] " + teamList.get(t).name + " (" + transferDLs.get(i).team.abbr + ")");
                            teamList.get(t).teamDLs.add(transferDLs.get(i));
                            transferDLs.remove(i);
                            break;
                        }
                    }
                }
            }

            for (int i = 0; i < transferLBs.size(); ++i) {
                rand = random.nextInt((max - min) + 1) + min;
                for (int t = rand; t < teamList.size() - rand; ++t) {
                    if (teamList.get(t).teamLBs.size() < 3 || teamList.get(t).teamLBs.get(0).ratOvr < transferLBs.get(i).ratOvr) {
                        if (Math.abs(teamList.get(t).location - transferLBs.get(i).getRegion()) < loc) {
                            newsStories.get(currentWeek + 1).add(teamList.get(t).name + " Transfer News>" + transferLBs.get(i).getYrStr() + " LB " + transferLBs.get(i).name + "(" + transferLBs.get(i).ratOvr + ") has announced his transfer to " + teamList.get(t).name + ". He was previously enrolled at " +
                                    transferLBs.get(i).team.abbr + " .");
                            if (teamList.get(t).equals(userTeam) && transferLBs.get(i).isTransfer) {
                                tIn.append(transferLBs.get(i).position + " " + transferLBs.get(i).name + ", " + transferLBs.get(i).getYrStr() + "  Ovr: " + transferLBs.get(i).ratOvr + " (" + transferLBs.get(i).team.abbr + ")\n\n");
                            }
                            if (teamList.get(t).equals(userTeam) && transferLBs.get(i).isGradTransfer) {
                                tIn.append(transferLBs.get(i).position + " " + transferLBs.get(i).name + ", " + transferLBs.get(i).getYrStr() + " [Grad]" + "  Ovr: " + transferLBs.get(i).ratOvr + " (" + transferLBs.get(i).team.abbr + ")\n\n");
                            }
                            if (transferLBs.get(i).team.abbr.equals(userTeam.abbr)) {
                                tOut.append(transferLBs.get(i).position + " " + transferLBs.get(i).name + ", " + transferLBs.get(i).getYrStr() + "  Ovr: " + transferLBs.get(i).ratOvr + " (" + teamList.get(t).name + ")\n\n");
                            }

                            transfersList.add(transferLBs.get(i).ratOvr + " " + transferLBs.get(i).position + " " + transferLBs.get(i).name + " [" + transferLBs.get(i).getTransferStatus() + "] " + teamList.get(t).name + " (" + transferLBs.get(i).team.abbr + ")");
                            teamList.get(t).teamLBs.add(transferLBs.get(i));
                            transferLBs.remove(i);
                            break;
                        }
                    }
                }
            }

            for (int i = 0; i < transferCBs.size(); ++i) {
                rand = random.nextInt((max - min) + 1) + min;
                for (int t = rand; t < teamList.size() - rand; ++t) {
                    if (teamList.get(t).teamCBs.size() < 3 || teamList.get(t).teamCBs.get(0).ratOvr < transferCBs.get(i).ratOvr) {
                        if (Math.abs(teamList.get(t).location - transferCBs.get(i).getRegion()) < loc) {
                            newsStories.get(currentWeek + 1).add(teamList.get(t).name + " Transfer News>" + transferCBs.get(i).getYrStr() + " CB " + transferCBs.get(i).name + "(" + transferCBs.get(i).ratOvr + ") has announced his transfer to " + teamList.get(t).name + ". He was previously enrolled at " +
                                    transferCBs.get(i).team.abbr + " .");
                            if (teamList.get(t).equals(userTeam) && transferCBs.get(i).isTransfer) {
                                tIn.append(transferCBs.get(i).position + " " + transferCBs.get(i).name + ", " + transferCBs.get(i).getYrStr() + "  Ovr: " + transferCBs.get(i).ratOvr + " (" + transferCBs.get(i).team.abbr + ")\n\n");
                            }
                            if (teamList.get(t).equals(userTeam) && transferCBs.get(i).isGradTransfer) {
                                tIn.append(transferCBs.get(i).position + " " + transferCBs.get(i).name + ", " + transferCBs.get(i).getYrStr() + " [Grad]" + "  Ovr: " + transferCBs.get(i).ratOvr + " (" + transferCBs.get(i).team.abbr + ")\n\n");
                            }
                            if (transferCBs.get(i).team.abbr.equals(userTeam.abbr)) {
                                tOut.append(transferCBs.get(i).position + " " + transferCBs.get(i).name + ", " + transferCBs.get(i).getYrStr() + "  Ovr: " + transferCBs.get(i).ratOvr + " (" + teamList.get(t).name + ")\n\n");
                            }

                            transfersList.add(transferCBs.get(i).ratOvr + " " + transferCBs.get(i).position + " " + transferCBs.get(i).name + " [" + transferCBs.get(i).getTransferStatus() + "] " + teamList.get(t).name + " (" + transferCBs.get(i).team.abbr + ")");
                            teamList.get(t).teamCBs.add(transferCBs.get(i));
                            transferCBs.remove(i);
                            break;
                        }
                    }
                }
            }

            for (int i = 0; i < transferSs.size(); ++i) {
                rand = random.nextInt((max - min) + 1) + min;
                for (int t = rand; t < teamList.size() - rand; ++t) {
                    if (teamList.get(t).teamSs.size() < 1 || teamList.get(t).teamSs.get(0).ratOvr < transferSs.get(i).ratOvr) {
                        if (Math.abs(teamList.get(t).location - transferSs.get(i).getRegion()) < loc) {
                            newsStories.get(currentWeek + 1).add(teamList.get(t).name + " Transfer News>" + transferSs.get(i).getYrStr() + " S " + transferSs.get(i).name + "(" + transferSs.get(i).ratOvr + ") has announced his transfer to " + teamList.get(t).name + ". He was previously enrolled at " +
                                    transferSs.get(i).team.abbr + " .");
                            if (teamList.get(t).equals(userTeam) && transferSs.get(i).isTransfer) {
                                tIn.append(transferSs.get(i).position + " " + transferSs.get(i).name + ", " + transferSs.get(i).getYrStr() + "  Ovr: " + transferSs.get(i).ratOvr + " (" + transferSs.get(i).team.abbr + ")\n\n");
                            }
                            if (teamList.get(t).equals(userTeam) && transferSs.get(i).isGradTransfer) {
                                tIn.append(transferSs.get(i).position + " " + transferSs.get(i).name + ", " + transferSs.get(i).getYrStr() + " [Grad]" + "  Ovr: " + transferSs.get(i).ratOvr + " (" + transferSs.get(i).team.abbr + ")\n\n");
                            }
                            if (transferSs.get(i).team.abbr.equals(userTeam.abbr)) {
                                tOut.append(transferSs.get(i).position + " " + transferSs.get(i).name + ", " + transferSs.get(i).getYrStr() + "  Ovr: " + transferSs.get(i).ratOvr + " (" + teamList.get(t).name + ")\n\n");
                            }

                            transfersList.add(transferSs.get(i).ratOvr + " " + transferSs.get(i).position + " " + transferSs.get(i).name + " [" + transferSs.get(i).getTransferStatus() + "] " + teamList.get(t).name + " (" + transferSs.get(i).team.abbr + ")");
                            teamList.get(t).teamSs.add(transferSs.get(i));
                            transferSs.remove(i);
                            break;
                        }
                    }
                }
            }
        }

        //The remaining user players transfer to FCS/Div II Football

        for (int i = 0; i < transferQBs.size(); ++i) {
            if(Math.random() > .50) {
                if (transferQBs.get(i).team.abbr.equals(userTeam.abbr)) {
                    tOut.append(transferQBs.get(i).position + " " + transferQBs.get(i).name + ", " + transferQBs.get(i).getYrStr() + "  Ovr: " + transferQBs.get(i).ratOvr + " (FCS)\n\n");
                }
            } else {
                transferQBs.get(i).team.teamQBs.add(transferQBs.get(i));
            }
        }
        for (int i = 0; i < transferRBs.size(); ++i) {
            if (Math.random() > .50) {
                if (transferRBs.get(i).team.abbr.equals(userTeam.abbr)) {
                    tOut.append(transferRBs.get(i).position + " " + transferRBs.get(i).name + ", " + transferRBs.get(i).getYrStr() + "  Ovr: " + transferRBs.get(i).ratOvr + " (FCS)\n\n");
                }
            } else {
                transferRBs.get(i).team.teamRBs.add(transferRBs.get(i));
            }
        }
        for (int i = 0; i < transferWRs.size(); ++i) {
            if (Math.random() > .50) {
                if (transferWRs.get(i).team.abbr.equals(userTeam.abbr)) {
                    tOut.append(transferWRs.get(i).position + " " + transferWRs.get(i).name + ", " + transferWRs.get(i).getYrStr() + "  Ovr: " + transferWRs.get(i).ratOvr + " (FCS)\n\n");
                }
            } else {
                transferWRs.get(i).team.teamWRs.add(transferWRs.get(i));
            }
        }
        for (int i = 0; i < transferTEs.size(); ++i) {
            if (Math.random() > .50) {
                if (transferTEs.get(i).team.abbr.equals(userTeam.abbr)) {
                    tOut.append(transferTEs.get(i).position + " " + transferTEs.get(i).name + ", " + transferTEs.get(i).getYrStr() + "  Ovr: " + transferTEs.get(i).ratOvr + " (FCS)\n\n");
                }
            } else {
                transferTEs.get(i).team.teamTEs.add(transferTEs.get(i));
            }
        }
        for (int i = 0; i < transferOLs.size(); ++i) {
            if (Math.random() > .50) {
                if (transferOLs.get(i).team.abbr.equals(userTeam.abbr)) {
                    tOut.append(transferOLs.get(i).position + " " + transferOLs.get(i).name + ", " + transferOLs.get(i).getYrStr() + "  Ovr: " + transferOLs.get(i).ratOvr + " (FCS)\n\n");
                }
            } else {
                transferOLs.get(i).team.teamOLs.add(transferOLs.get(i));
            }
        }
        for (int i = 0; i < transferKs.size(); ++i) {
            if (Math.random() > .50) {
                if (transferKs.get(i).team.abbr.equals(userTeam.abbr)) {
                    tOut.append(transferKs.get(i).position + " " + transferKs.get(i).name + ", " + transferKs.get(i).getYrStr() + "  Ovr: " + transferKs.get(i).ratOvr + " (FCS)\n\n");
                }
            } else {
                transferKs.get(i).team.teamKs.add(transferKs.get(i));
            }
        }
        for (int i = 0; i < transferDLs.size(); ++i) {
            if (Math.random() > .50) {
                if (transferDLs.get(i).team.abbr.equals(userTeam.abbr)) {
                    tOut.append(transferDLs.get(i).position + " " + transferDLs.get(i).name + ", " + transferDLs.get(i).getYrStr() + "  Ovr: " + transferDLs.get(i).ratOvr + " (FCS)\n\n");
                }
            } else {
                transferDLs.get(i).team.teamDLs.add(transferDLs.get(i));
            }
        }
        for (int i = 0; i < transferLBs.size(); ++i) {
            if (Math.random() > .50) {
                if (transferLBs.get(i).team.abbr.equals(userTeam.abbr)) {
                    tOut.append(transferLBs.get(i).position + " " + transferLBs.get(i).name + ", " + transferLBs.get(i).getYrStr() + "  Ovr: " + transferLBs.get(i).ratOvr + " (FCS)\n\n");
                }
            } else {
                transferLBs.get(i).team.teamLBs.add(transferLBs.get(i));
            }
        }
        for (int i = 0; i < transferCBs.size(); ++i) {
            if (Math.random() > .50) {
                if (transferCBs.get(i).team.abbr.equals(userTeam.abbr)) {
                    tOut.append(transferCBs.get(i).position + " " + transferCBs.get(i).name + ", " + transferCBs.get(i).getYrStr() + "  Ovr: " + transferCBs.get(i).ratOvr + " (FCS)\n\n");
                }
            } else {
                transferCBs.get(i).team.teamCBs.add(transferCBs.get(i));
            }
        }
        for (int i = 0; i < transferSs.size(); ++i) {
            if (Math.random() > .50) {
                if (transferSs.get(i).team.abbr.equals(userTeam.abbr)) {
                    tOut.append(transferSs.get(i).position + " " + transferSs.get(i).name + ", " + transferSs.get(i).getYrStr() + "  Ovr: " + transferSs.get(i).ratOvr + " (FCS)\n\n");
                }
            } else {
                transferSs.get(i).team.teamSs.add(transferSs.get(i));
            }
        }

        //Sort Teams
        for (int i = 0; i < teamList.size(); ++i) {
            teamList.get(i).sortPlayers();
        }
        
        userTransfers = tIn + "\n" + tOut;
        newsStories.get(currentWeek + 1).add("User Team Transfers>" + userTransfers);

        Collections.sort(transfersList, Collections.<String>reverseOrder());
        StringBuilder transferSum = new StringBuilder();

        for (int i = 0; i < transfersList.size(); ++i) {
            transferSum.append(transfersList.get(i) + "\n\n");
        }

        newsStories.get(currentWeek + 1).add("Transfers Summary>" + transferSum);
        sumTransfers = "" + transferSum;

    }

    /*
Conference Realigment v2:

Every conference creates a bucket of teams that are not meeting minimum prestige threshold and a bucket of exceeding threshold (if lower tierd conference).

Then conferences can see if they want to add them to their list if the teams meet the new conference list... chosen at random.

*/
    public void conferenceRealignmentV2() {
        ArrayList<Team> demoteTeamList = new ArrayList<>();
        ArrayList<Team> promoteTeamList = new ArrayList<>();
        newsRealignment = "";
        countRealignment = 0;

        if (Math.random() < confRealignmentChance) {
            ArrayList<Conference> confList = conferences;
            Collections.sort(confList, new CompConfPrestige());

            //find conferences for independents
            for (int c = 0; c < confList.size(); c++) {
                if (confList.get(c).confTeams.size() < confList.get(c).minConfTeams) {
                    for (int i = 0; i < confList.get(c).confTeams.size(); i++) {
                        if (confList.get(c).confTeams.get(i).teamPrestige < confList.get(c).confRelegateMin) {
                            demoteTeamList.add(confList.get(c).confTeams.get(i));
                        }
                    }
                }
            }

            //Smaller Conferences Will Try to Expand Their Empire...
            for (int c = 0; c < conferences.size(); c++) {
                if (conferences.get(c).confTeams.size() < 16) {
                    Conference conf = conferences.get(c);
                    ArrayList<Team> qualified = new ArrayList<>();
                    for (int i = 0; i < demoteTeamList.size(); i++) {
                        if (demoteTeamList.get(i).teamPrestige > conf.confRelegateMin && Math.random() < realignmentChance * 2 && Math.abs(demoteTeamList.get(i).location - conf.confTeams.get(0).location) < 2) {
                            qualified.add(demoteTeamList.get(i));
                        }
                        if (qualified.size() >= 2) {
                            final Team teamA = qualified.get(0);
                            final Team teamB = qualified.get(1);
                            conferences.get(getConfNumber(teamA.conference)).confTeams.remove(teamA);
                            conferences.get(getConfNumber(teamB.conference)).confTeams.remove(teamB);
                            teamA.conference = conf.confName;
                            teamB.conference = conf.confName;
                            conf.confTeams.add(teamA);
                            conf.confTeams.add(teamB);

                            //break the news
                            newsStories.get(currentWeek + 2).add("Conference Addition!>The " + conf.confName + " conference announced today they will be adding " + teamA.name + " and " + teamB.name + " from the " + conf.confName + " to their conference next season!");

                            newsRealignment += ("The " + conf.confName + " conference announced today they will be adding " + teamA.name + " and " + teamB.name + " from the " + conf.confName + " to their conference next season!\n\n");
                            countRealignment++;
                            demoteTeamList.remove(teamA);
                            demoteTeamList.remove(teamB);
                        }
                    }
                }
            }

            //find the teams that are doing too well for their conference level
            for (int c = confList.size() / 2; c < confList.size(); c++) {
                if (confList.get(c).confTeams.size() >= confList.get(c).minConfTeams) {
                    for (int i = 0; i < confList.get(c).confTeams.size(); i++) {
                        if (confList.get(c).confTeams.get(i).teamPrestige > confList.get(c).confPromoteMin) {
                            promoteTeamList.add(confList.get(c).confTeams.get(i));
                        }
                    }
                } else {
                    for (int i = 0; i < confList.get(c).confTeams.size(); i++) {
                        promoteTeamList.add(confList.get(c).confTeams.get(i));
                    }
                }
            }

            //Sort Prestige
            Collections.sort(promoteTeamList, new CompTeamPrestige());

            //Bigger Conferences will extend Invites
            for (int i = 0; i < promoteTeamList.size(); i++) {
                int randomConf = (int) (Math.random() * (confList.size() / 2));
                if (promoteTeamList.get(i).teamPrestige > confList.get(randomConf).confPromoteMin && confList.get(randomConf).confTeams.size() >= confList.get(randomConf).minConfTeams) {
                    for (int k = confList.get(randomConf).confTeams.size() - 1; k >= 0; k--) {
                        if (confList.get(randomConf).confTeams.get(k).teamPrestige < confList.get(randomConf).confRelegateMin) {
                            if (Math.random() < realignmentChance && Math.abs(confList.get(randomConf).confTeams.get(k).location - promoteTeamList.get(i).location) < 2) {

                                final String teamAconf = confList.get(randomConf).confName;
                                final String teamBconf = promoteTeamList.get(i).conference;
                                final Team teamA = confList.get(randomConf).confTeams.get(k);
                                final Team teamB = promoteTeamList.get(i);

                                //transfer conf data
                                teamB.conference = teamAconf;
                                teamA.conference = teamBconf;

                                //remove + transfer teams
                                confList.get(getConfNumber(teamAconf)).confTeams.remove(teamA);
                                confList.get(getConfNumber(teamBconf)).confTeams.remove(teamB);

                                confList.get(getConfNumber(teamAconf)).confTeams.add(teamB);
                                confList.get(getConfNumber(teamBconf)).confTeams.add(teamA);

                                //Remove some prestige from demoted teams
                                teamA.teamPrestige -= (int) Math.random() * 4;


                                //break the news
                                newsStories.get(currentWeek + 2).add("Conference Realignment News>The " + teamAconf + " conference announced today they will be adding " + teamB.name + " to their conference next season! The " + teamBconf + " conference has agreed to add " + teamA.name + " as part of the realignment.");

                                newsRealignment += ("The " + teamAconf + " announced today they will be adding " + teamB.name + " to their conference next season! The " + teamBconf + " conference has agreed to add " + teamA.name + " as part of the realignment.\n\n");
                                countRealignment++;
                                promoteTeamList.remove(teamA);
                                break;
                            }
                        }

                    }

                }
            }
        }


        if (advancedRealignment && Math.random() < confRealignmentChance) {
            ArrayList<Conference> confList = conferences;
            Collections.sort(confList, new CompConfPrestige());


            //find the teams do not meet conference threshold
            for (int c = 0; c < confList.size(); c++) {
                if (confList.get(c).confTeams.size() >= confList.get(c).minConfTeams) {
                    for (int i = 0; i < confList.get(c).confTeams.size(); i++) {
                        if (confList.get(c).confTeams.get(i).teamPrestige < confList.get(c).confRelegateMin) {
                            demoteTeamList.add(confList.get(c).confTeams.get(i));
                        }
                    }
                } else {
                    for (int i = 0; i < confList.get(c).confTeams.size(); i++) {
                        demoteTeamList.add(confList.get(c).confTeams.get(i));
                    }
                }
            }

            //Smaller Conferences Will Try to Expand Their Empire...
            for (int c = 0; c < conferences.size(); c++) {
                if (conferences.get(c).confTeams.size() < 14 && conferences.get(c).confTeams.size() >= conferences.get(c).minConfTeams)  {
                    Conference conf = conferences.get(c);
                    for (int i = 0; i < demoteTeamList.size(); i++) {
                        if (demoteTeamList.get(i).teamPrestige > conf.confPromoteMin && Math.random() < realignmentChance && Math.abs(demoteTeamList.get(i).location - conf.confTeams.get(0).location) < 2) {
                            final Team teamA = demoteTeamList.get(i);
                            final String oldConf = teamA.conference;
                            if(conferences.get(getConfNumber(teamA.conference)).confTeams.size() > conferences.get(getConfNumber(teamA.conference)).minConfTeams) {
                                conferences.get(getConfNumber(teamA.conference)).confTeams.remove(teamA);
                                teamA.conference = conf.confName;
                                conf.confTeams.add(teamA);

                                //break the news
                                newsStories.get(currentWeek + 2).add("Conference Growth!>The " + conf.confName + " conference announced today they will be adding " + teamA.name + " to their conference next season! " + teamA.name + " used to part of the " + oldConf + " Conference.");

                                newsRealignment += ("The " + conf.confName + " conference announced today they will be adding " + teamA.name + " to their conference next season! " + teamA.name + " used to part of the " + oldConf + " Conference.\n\n");
                                countRealignment++;
                                demoteTeamList.remove(teamA);
                            }
                        }
                    }
                }
            }

            //Let's kick people out
            int indConf = 0;
            boolean indSpace = false;
            for (Conference c : conferences) {
                if (c.confName.equals("Independent") && c.confTeams.size() < c.minConfTeams) {
                    indConf = getConfNumber(c.confName);
                    indSpace = true;
                }
            }
            if (indSpace) {
                Conference indy = conferences.get(indConf);
                for (int i = 0; i < demoteTeamList.size(); i++) {
                    if (Math.random() < realignmentChance && indy.confTeams.size() < (indy.minConfTeams-1) && !demoteTeamList.get(i).conference.equals("Independent")) {
                        final Team teamA = demoteTeamList.get(i);
                        final String oldConf = teamA.conference;

                        if(conferences.get(getConfNumber(teamA.conference)).confTeams.size() > conferences.get(getConfNumber(teamA.conference)).minConfTeams) {
                            conferences.get(getConfNumber(teamA.conference)).confTeams.remove(teamA);
                            teamA.conference = indy.confName;
                            indy.confTeams.add(teamA);

                            //break the news
                            newsStories.get(currentWeek + 2).add("Conference and Team Part Ways!>The " + oldConf + " conference announced today they will be removing " + teamA.name + " from their conference next season! " + teamA.name + " will become and Independent school until picked up by a new conference.");

                            newsRealignment += ("The " + oldConf + " conference announced today they will be removing " + teamA.name + " from their conference next season! " + teamA.name + " will become and Independent school until picked up by a new conference\n\n");
                            countRealignment++;
                            demoteTeamList.remove(teamA);
                        }
                    }
                }
            }


            //Promote FCS School
            if(Math.random() < realignmentChance) {
                Conference indy = conferences.get(indConf);
                if(conferences.get(indConf).confTeams.size() < indy.minConfTeams-1) {
                    String fcsName;
                    Boolean named = false;
                    int nameTest = 0;
                    while(!named) {
                        fcsName = teamsFCS[(int) (teamsFCS.length * Math.random())];
                        for(int i = 0; i < teamList.size(); i++) {
                            if(teamList.get(i).name.equals(fcsName)) {
                                nameTest++;
                            }
                        }
                        if(nameTest == 0) {
                            named = true;
                        }
                    }

                    Team FCS = new Team(teamsFCS[(int) (teamsFCS.length * Math.random())], "FCS", "Independent", (int) (Math.random() * 35), "A", (int)(Math.random()*6), this);
                    FCS.abbr = FCS.name.substring(0,2);
                    FCS.rankTeamPollScore = teamList.size();
                    teamList.add(FCS);
                    indy.confTeams.add(FCS);

                    //break the news
                    newsStories.get(currentWeek + 2).add("Div II School Promoted to Div I!>Today is a special day at " + FCS.name + ". They have been promoted to Division I College Football today, and will be listed as an Independent team!");

                    newsRealignment += (FCS.name + " have been promoted to Division I College Football today, and will be listed as an Independent team!\n\n");
                    countRealignment++;
                }
            }

            //Create New Conference


        }


        for (Conference c : conferences) {
            if (c.confTeams.size() < c.minConfTeams) {
                c.confPrestige = 0;
            }
        }

        Collections.sort(conferences, new CompConfPrestige());
    }

    //Universal Pro/Rel system - uses EVERY conferences. This is a giant Premier League + Championship + League 1 + League 2 + National League + Feeder Leagues FA-like system.
    public void convertUnivProRel() {

        //Reorder Every Team by Rank
        Collections.sort(teamList, new CompTeamPrestige());

        //check if there is an even number of teams
        if(teamList.size() % 2 != 0) {
            teamList.add(new Team(teamsFCS[(int) (teamsFCS.length * Math.random())], "FCS", "FCS Division", (int) (Math.random() * 40), "FCS1", (int)(Math.random()*6), this, true));
        }

        //Clear all Conferences
        for (int i = 0; i < conferences.size(); i++) {
            conferences.get(i).confTeams.clear();
            conferences.get(i).confName = getRankStr(i + 1) + " Tier";
            conferences.get(i).TV = conferences.get(i).getTVName();
        }

        int teamsPerConf = teamList.size() / conferences.size();
        if (teamsPerConf % 2 != 0) teamsPerConf++;  //check if even # of teams per conf

        int c = 0, next = 0;
        for (int t = 0; t < teamList.size(); t++) {
            if (c == conferences.size() - 1) {

            } else if (next == teamsPerConf) {
                next = 0;
                c++;
            }
            teamList.get(t).conference = conferences.get(c).confName;
            teamList.get(t).division = "A";
            teamList.get(t).gameSchedule.clear();
            conferences.get(c).confTeams.add(teamList.get(t));
            next++;
        }

        setupSeason();
        updateTeamTalentRatings();
        setTeamBenchMarks();
        setTeamRanks();
        Collections.sort(teamList, new CompTeamPrestige());
    }

    //Universal Promotion/Relegation System - just like in Soccer. Can be enabled. Disabled by default.
    public void universalProRel() {

        //Get list of Promoted & Relegated Teams
        ArrayList<Team> promotedTeams = new ArrayList<>();
        ArrayList<Team> relegatedTeams = new ArrayList<>();

        int confSize = 0;
        for (int c = 0; c < conferences.size(); c++) {
            if (conferences.get(c).confTeams.size() >= conferences.get(c).minConfTeams) {
                confSize++;
            }
        }
        for (int i = 1; i < confSize; ++i) {
            for (int t = 0; t < conferences.get(i).confTeams.size(); t++) {
                if (conferences.get(i).confTeams.get(t).gameSchedule.size() > 12 && conferences.get(i).confTeams.get(t).gameSchedule.get(12).gameName.contains("CCG"))
                    promotedTeams.add(conferences.get(i).confTeams.get(t));
            }
        }

        for (int i = 0; i < confSize; ++i) {
            int size = conferences.get(i).confTeams.size();
            relegatedTeams.add(conferences.get(i).confTeams.get(size - 1));
            relegatedTeams.add(conferences.get(i).confTeams.get(size - 2));
        }

        //Remove more prestige from teams
        for (int i = 0; i < relegatedTeams.size(); i++) {
            relegatedTeams.get(i).teamPrestige -= 3;
        }

        StringBuilder string = new StringBuilder();
        for (int i = 0; i < confSize - 1; ++i) {

            Conference PConf = conferences.get(i);
            Conference RConf = conferences.get(i + 1);
            string.append("[ " + PConf.confName + " || " + RConf.confName + " ]\n");

            //change team conferences
            promotedTeams.get(2 * i).conference = PConf.confName;
            promotedTeams.get(2 * i + 1).conference = PConf.confName;
            relegatedTeams.get(2 * i).conference = RConf.confName;
            relegatedTeams.get(2 * i + 1).conference = RConf.confName;

            //Remove teams from Conferences
            PConf.confTeams.remove(relegatedTeams.get(2 * i));
            PConf.confTeams.remove(relegatedTeams.get(2 * i + 1));
            RConf.confTeams.remove(promotedTeams.get(2 * i));
            RConf.confTeams.remove(promotedTeams.get(2 * i + 1));

            //Add teams to Conferences
            PConf.confTeams.add(promotedTeams.get(2 * i));
            PConf.confTeams.add(promotedTeams.get(2 * i + 1));
            RConf.confTeams.add(relegatedTeams.get(2 * i));
            RConf.confTeams.add(relegatedTeams.get(2 * i + 1));

            //calculate Conf Prestige
            PConf.updateConfPrestige();
            RConf.updateConfPrestige();

            //break the news
            string.append("Promoted to " + PConf.confName + " Conference (" + PConf.confPrestige + ")\n");
            string.append(" + " + promotedTeams.get(2 * i).name + " (" + promotedTeams.get(2 * i).teamPrestige + ")\n" + " + " + promotedTeams.get(2 * i + 1).name + " (" + promotedTeams.get(2 * i + 1).teamPrestige + ")\n");
            string.append("Relegated to " + RConf.confName + " Conference\n");
            string.append(" - " + relegatedTeams.get(2 * i).name + " (" + relegatedTeams.get(2 * i).teamPrestige + ")\n" + " - " + relegatedTeams.get(2 * i + 1).name + " (" + relegatedTeams.get(2 * i + 1).teamPrestige + ")\n");
            string.append("\n");

        }

        //post news in News
        newsRealignment = string.toString();
        newsStories.get(currentWeek + 2).add("Promotion/Relegation Update>" + newsRealignment);
    }

    //Mock Draft String
    public String[] getMockDraftPlayersList() {
        ArrayList<Player> allPlayersLeaving = new ArrayList<>();
        for (Team t : teamList) {
            for (Player p : t.playersLeaving) {
                if (p.ratOvr > 70) allPlayersLeaving.add(p);
            }
        }

        Collections.sort(allPlayersLeaving, new CompNFLTalent());
        ArrayList<Player> NFLPlayers = new ArrayList<>();

        if (allPlayersLeaving.size() > 224) {
            for (int i = 0; i < 224; ++i) {
                NFLPlayers.add(allPlayersLeaving.get(i));
            }
        } else {
            // Get 100 players (first 4 rounds)
            for (int i = 0; i < 96; ++i) {
                NFLPlayers.add(allPlayersLeaving.get(i));
            }
        }

        List<String> nfl = Arrays.asList(proTeams);
        Collections.shuffle(nfl);

        String[] nflPlayers = new String[NFLPlayers.size()];
        int n = 0, r = 1;
        for (int i = 0; i < nflPlayers.length; ++i) {
            nflPlayers[i] = NFLPlayers.get(i).getMockDraftStr(r, n + 1, nfl.get(n));
            n++;
            if (n >= nfl.size()) {
                n = 0;
                r++;
            }
        }

        return nflPlayers;
    }

    //CPU Recruiting - Tells each team to recruit players
    public void recruitPlayers() {
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).CPUrecruiting();
        }
    }

    //Mid-Season - Give players who have been playing games a bonus
    public void midSeasonProgression() {
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).midSeasonProgression();
        }
    }

    //advances team players
    public void advanceSeason() {
        for (int t = 0; t < teamList.size(); ++t) {

            teamList.get(t).advanceTeamPlayers();
        }

        advanceSeasonWinStreaks();

        if (enableTV) newsTV = new ArrayList<>();
        for (int c = 0; c < conferences.size(); ++c) {
            if (enableTV) conferences.get(c).reviewConfTVDeal();
        }

    }


    /**
     * Check the longest win streak. If the given streak is longer, replace.
     *
     * @param streak streak to check
     */
    public void checkLongestWinStreak(TeamStreak streak) {
        if (streak.getStreakLength() > longestWinStreak.getStreakLength()) {
            longestWinStreak = new TeamStreak(streak.getStartYear(), streak.getEndYear(), streak.getStreakLength(), streak.getTeam());
        }
    }

    /**
     * Gets the longest active win streak.
     */
    private void updateLongestActiveWinStreak() {
        for (Team t : teamList) {
            if (t.winStreak.getStreakLength() > longestActiveWinStreak.getStreakLength()) {
                longestActiveWinStreak = t.winStreak;
            }
        }
    }

    /**
     * Advance season for win streaks, so no save-load whackiness.
     */
    private void advanceSeasonWinStreaks() {
        yearStartLongestWinStreak = longestWinStreak;
    }

    /**
     * Change the team abbr of the lognest win streak if the user changed it
     *
     * @param oldAbbr old abbreviation
     * @param newAbbr new abbreviation
     */
    private void changeAbbrWinStreaks(String oldAbbr, String newAbbr) {
        if (longestWinStreak.getTeam().equals(oldAbbr)) {
            longestWinStreak.changeAbbr(newAbbr);
        }
        if (yearStartLongestWinStreak.getTeam().equals(oldAbbr)) {
            yearStartLongestWinStreak.changeAbbr(newAbbr);
        }
    }

    /**
     * Changes all the abbrs to new abbr, in records and histories.
     *
     * @param oldAbbr
     * @param newAbbr
     */
    public void changeAbbrHistoryRecords(String oldAbbr, String newAbbr) {
        // check records and win streaks
        leagueRecords.changeAbbrRecords(userTeam.abbr, newAbbr);
        userTeamRecords.changeAbbrRecords(userTeam.abbr, newAbbr);
        changeAbbrWinStreaks(userTeam.abbr, newAbbr);
        userTeam.winStreak.changeAbbr(newAbbr);

        // check league and POTY history
        for (String[] yr : leagueHistory) {
            for (int i = 0; i < yr.length; ++i) {
                if (yr[i].split(" ")[0].equals(oldAbbr)) {
                    yr[i] = newAbbr + " " + yr[i].split(" ")[1];
                }
            }
        }

        for (int i = 0; i < heismanHistory.size(); ++i) {
            String p = heismanHistory.get(i);
            if (p.split(" ")[4].equals(oldAbbr)) {
                heismanHistory.set(i,
                        p.split(" ")[0] + " " +
                                p.split(" ")[1] + " " +
                                p.split(" ")[2] + " " +
                                p.split(" ")[3] + " " +
                                newAbbr + " " +
                                p.split(" ")[5]);
            }
        }

    }

    /**
     * Checks if any of the league records were broken by teams.
     */
    public void checkLeagueRecords() {
        for (Team t : teamList) {
            t.checkLeagueRecords(leagueRecords);
            t.checkLeagueRecords(t.teamRecords);
        }
        userTeam.checkLeagueRecords(userTeamRecords);
    }

    /**
     * Gets all the league records, including the longest win streak
     *
     * @return string of all the records, csv
     */
    public String getLeagueRecordsStr() {
        String winStreakStr = "Longest Win Streak," + longestWinStreak.getStreakLength() + "," +
                longestWinStreak.getTeam() + "," + longestWinStreak.getStartYear() + "-" + longestWinStreak.getEndYear() + "\n";
        String activeWinStreakStr = "Active Win Streak," + longestActiveWinStreak.getStreakLength() + "," +
                longestActiveWinStreak.getTeam() + "," + longestActiveWinStreak.getStartYear() + "-" + longestActiveWinStreak.getEndYear() + "\n";
        return winStreakStr + activeWinStreakStr + leagueRecords.getRecordsStr();
    }

    /**
     * At the end of the year, record the top 25 teams for the League's History.
     */
    public void updateLeagueHistory() {
        //update league history
        Collections.sort(teamList, new CompTeamPoll());
        String[] yearTop25 = new String[25];
        Team tt;
        for (int i = 0; i < 25; ++i) {
            tt = teamList.get(i);
            yearTop25[i] = tt.name + " (" + tt.wins + "-" + tt.losses + ")";
        }
        leagueHistory.add(yearTop25);
    }

    public String getLeagueTop25History(int year) {
        String hist = "";
        hist += (seasonStart + year) + " Top 25 Rankings:\n";
        for (int i = 0; i < 25; ++i) {
            if (i < 9) {
                hist += "\t 0" + (i + 1) + ":  " + leagueHistory.get(year)[i] + "\n";
            } else {
                hist += "\t " + (i + 1) + ":  " + leagueHistory.get(year)[i] + "\n";
            }

        }
        return hist;
    }

    /**
     * Get String of the league's history, year by year.
     * Saves the NCG winner and the POTY.
     *
     * @return list of the league's history.
     */
    public String getLeagueHistoryStr() {
        String hist = "";
        for (int i = 0; i < leagueHistory.size(); ++i) {
            hist += (seasonStart + i) + ":\n";
            hist += "\tChampions: " + leagueHistory.get(i)[0] + "\n";
            hist += "\tOff: " + heismanHistory.get(i).split(">")[0] + "\n";
            if (heismanHistory.get(i).split(">").length > 1)
                hist += "\tDef: " + heismanHistory.get(i).split(">")[1] + "\n%";
            else hist += "%";
        }
        return hist;
    }


    /**
     * Updates team history for each team.
     */
    public void updateTeamHistories() {
        for (int i = 0; i < teamList.size(); ++i) {
            teamList.get(i).updateTeamHistory();
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Updates poll scores for each team and updates their ranking.
     */
    public void setTeamRanks() {
        //get team ranks for PPG, YPG, etc
        for (int i = 0; i < teamList.size(); ++i) {
            teamList.get(i).updatePollScore();
        }

        Collections.sort(teamList, new CompTeamPoll());
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).rankTeamPollScore = t + 1;
        }

        for (int i = 0; i < teamList.size(); ++i) {
            teamList.get(i).updateSOS();
        }

        Collections.sort(teamList, new CompTeamSoS());
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).rankTeamSOS = t + 1;
        }

        Collections.sort(teamList, new CompTeamSoW());
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).rankTeamStrengthOfWins = t + 1;
        }

        Collections.sort(teamList, new CompTeamPPG());
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).rankTeamPoints = t + 1;
        }

        Collections.sort(teamList, new CompTeamOPPG());
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).rankTeamOppPoints = t + 1;
        }

        Collections.sort(teamList, new CompTeamYPG());
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).rankTeamYards = t + 1;
        }

        Collections.sort(teamList, new CompTeamOYPG());
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).rankTeamOppYards = t + 1;
        }

        Collections.sort(teamList, new CompTeamPYPG());
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).rankTeamPassYards = t + 1;
        }

        Collections.sort(teamList, new CompTeamRYPG());
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).rankTeamRushYards = t + 1;
        }

        Collections.sort(teamList, new CompTeamOPYPG());
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).rankTeamOppPassYards = t + 1;
        }

        Collections.sort(teamList, new CompTeamORYPG());
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).rankTeamOppRushYards = t + 1;
        }

        Collections.sort(teamList, new CompTeamTODiff());
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).rankTeamTODiff = t + 1;
        }

        Collections.sort(teamList, new CompTeamOffTalent());
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).rankTeamOffTalent = t + 1;
        }

        Collections.sort(teamList, new CompTeamDefTalent());
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).rankTeamDefTalent = t + 1;
        }

        Collections.sort(teamList, new CompTeamPrestige());
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).rankTeamPrestige = t + 1;
        }

        Collections.sort(teamList, new CompTeamDisciplineScore());
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).rankTeamDisciplineScore = t + 1;
        }

        Collections.sort(teamList, new CompTeamBudget());
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).rankTeamBudget = t + 1;
        }

        Collections.sort(teamList, new CompTeamFacilities());
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).rankTeamFacilities = t + 1;
        }

        Collections.sort(teamList, new CompTeamChemistry());
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).rankTeamChemistry = t + 1;
        }

        if (currentWeek == 0) {
            Collections.sort(teamList, new CompTeamRecruitClass());
            for (int t = 0; t < teamList.size(); ++t) {
                teamList.get(t).rankTeamRecruitClass = t + 1;
            }
        }
    }

    /**
     * Get list of all the teams and their rankings based on selection
     *
     * @param selection stat to sort by, 0-13
     * @return list of the teams: ranking,str rep,stat
     */
    public ArrayList<String> getTeamRankingsStr(int selection) {
        /*
         */
        ArrayList<Team> teams = teamList;

        ArrayList<HeadCoach> HC = new ArrayList<>();
        for (int i = 0; i < teamList.size(); ++i) {
            if (teamList.get(i).HC.size() > 0) HC.add(teamList.get(i).HC.get(0));
        }

        ArrayList<String> rankings = new ArrayList<>();
        Team t;
        switch (selection) {
            case 0:
                Collections.sort(teams, new CompTeamPoll());
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i + 1) + "," + t.name + "," + df2.format(t.teamPollScore));
                }
                break;
            case 1:
                Collections.sort(teams, new CompTeamPrestige());
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    if (currentWeek > 15 && (t.teamPrestige - t.teamPrestigeStart) > 0)
                        rankings.add(t.getRankStrStarUser(i + 1) + "," + t.name + "," + t.teamPrestige + "  (+" + (t.teamPrestige - t.teamPrestigeStart) + ")");
                    else if (currentWeek > 15 && (t.teamPrestige - t.teamPrestigeStart) < 0)
                        rankings.add(t.getRankStrStarUser(i + 1) + "," + t.name + "," + t.teamPrestige + "  (" + (t.teamPrestige - t.teamPrestigeStart) + ")");
                    else
                        rankings.add(t.getRankStrStarUser(i + 1) + "," + t.name + "," + t.teamPrestige);
                }
                break;
            case 2:
                Collections.sort(teams, new CompTeamSoS());
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i + 1) + "," + t.name + "," + t.teamSOS);
                }
                break;
            case 3:
                Collections.sort(teams, new CompTeamSoW());
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i + 1) + "," + t.name + "," + t.teamStrengthOfWins);
                }
                break;
            case 4:
                Collections.sort(teams, new CompTeamPPG());
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i + 1) + "," + t.name + "," + df2.format((float) t.teamPoints / t.numGames()));
                }
                break;
            case 5:
                Collections.sort(teams, new CompTeamOPPG());
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i + 1) + "," + t.name + "," + df2.format((float) t.teamOppPoints / t.numGames()));
                }
                break;
            case 6:
                Collections.sort(teams, new CompTeamYPG());
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i + 1) + "," + t.name + "," + df2.format((float) t.teamYards / t.numGames()));
                }
                break;
            case 7:
                Collections.sort(teams, new CompTeamOYPG());
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i + 1) + "," + t.name + "," + df2.format((float) t.teamOppYards / t.numGames()));
                }
                break;
            case 8:
                Collections.sort(teams, new CompTeamPYPG());
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i + 1) + "," + t.name + "," + df2.format((float) t.teamPassYards / t.numGames()));
                }
                break;
            case 9:
                Collections.sort(teams, new CompTeamRYPG());
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i + 1) + "," + t.name + "," + df2.format((float) t.teamRushYards / t.numGames()));
                }
                break;
            case 10:
                Collections.sort(teams, new CompTeamOPYPG());
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i + 1) + "," + t.name + "," + df2.format((float) t.teamOppPassYards / t.numGames()));
                }
                break;
            case 11:
                Collections.sort(teams, new CompTeamORYPG());
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i + 1) + "," + t.name + "," + df2.format((float) t.teamOppRushYards / t.numGames()));
                }
                break;
            case 12:
                Collections.sort(teams, new CompTeamTODiff());
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    if (t.teamTODiff > 0)
                        rankings.add(t.getRankStrStarUser(i + 1) + "," + t.name + ",+" + t.teamTODiff);
                    else
                        rankings.add(t.getRankStrStarUser(i + 1) + "," + t.name + "," + t.teamTODiff);
                }
                break;
            case 13:
                Collections.sort(teams, new CompTeamOffTalent());
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i + 1) + "," + t.name + "," + df2.format(t.teamOffTalent));
                }
                break;
            case 14:
                Collections.sort(teams, new CompTeamDefTalent());
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i + 1) + "," + t.name + "," + df2.format(t.teamDefTalent));
                }
                break;
            case 15:
                Collections.sort(teams, new CompTeamChemistry());
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i + 1) + "," + t.name + "," + df2.format(t.getTeamChemistry()));
                }
                break;
            case 16:
                Collections.sort(teams, new CompTeamRecruitClass());
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i + 1) + "," + t.strRepWithPrestige() + "," + df2.format(t.getRecruitingClassRat()));
                }
                break;
            case 17:
                Collections.sort(teams, new CompTeamDisciplineScore());
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i + 1) + "," + t.name + "," + (t.teamDisciplineScore) + "%");
                }
                break;
            case 18:
                Collections.sort(teams, new CompTeamBudget());
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i + 1) + "," + t.name + ",$" + t.teamBudget);
                }
                break;
            case 19:
                Collections.sort(teams, new CompTeamFacilities());
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i + 1) + "," + t.name + ",L" + t.teamFacilities);
                }
                break;
            case 20:
                Collections.sort(HC, new CompCoachOvr());
                for (int i = 0; i < HC.size(); ++i) {
                    rankings.add((i + 1) + ". ," + HC.get(i).team.name + "," + HC.get(i).getHCOverall());
                }
                break;
            case 21:
                Collections.sort(HC, new CompCoachScore());
                for (int i = 0; i < HC.size(); ++i) {
                    rankings.add((i + 1) + ". ," + HC.get(i).team.name + "," + HC.get(i).getCoachScore());
                }
                break;
            default:
                Collections.sort(teams, new CompTeamPoll());
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i + 1) + "," + t.name + "," + t.teamPollScore);
                }
                break;
        }

        return rankings;
    }

    public ArrayList<String> getLeagueHistoryStats(int selection) {
        /*
         */
        ArrayList<Team> teams = teamList;
        ArrayList<String> rankings = new ArrayList<>();
        Team t;

        ArrayList<HeadCoach> HC = new ArrayList<>();
        for (int i = 0; i < teamList.size(); ++i) {
            if (teamList.get(i).HC.size() > 0) HC.add(teamList.get(i).HC.get(0));
        }

        switch (selection) {
            case 0:
                Collections.sort(teams, new CompTeamNC());
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i + 1) + "," + t.name + "," + t.totalNCs);
                }
                break;
            case 1:
                Collections.sort(teams, new CompTeamCC());
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i + 1) + "," + t.name + "," + t.totalCCs);
                }
                break;
            case 2:
                Collections.sort(teams, new CompTeamBowls());
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i + 1) + "," + t.name + "," + t.totalBowls);
                }
                break;
            case 3:
                Collections.sort(teams, new CompTeamWins());
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i + 1) + "," + t.name + "," + (t.totalWins + t.wins));
                }
                break;
            case 4:
                Collections.sort(HC, new CompCoachCareer());
                for (int i = 0; i < HC.size(); ++i) {
                    rankings.add((i + 1) + ". ," + HC.get(i).name + " (" + HC.get(i).team.abbr + ")," + HC.get(i).getCoachCareerScore());
                }
                break;
            case 5:
                Collections.sort(HC, new CompCoachCareerPrestige());
                for (int i = 0; i < HC.size(); ++i) {
                    rankings.add((i + 1) + ". ," + HC.get(i).name + " (" + HC.get(i).team.abbr + ")," + HC.get(i).cumulativePrestige);
                }
                break;
        }
        return rankings;
    }


    /**
     * Get conference standings in an list of Strings.
     * Must be CSV form: Rank,Team,Num
     */
    public ArrayList<String> getConfStandings() {
        ArrayList<String> confStandings = new ArrayList<>();
        ArrayList<Team> confTeams = new ArrayList<>();
        for (Conference c : conferences) {
            confTeams.addAll(c.confTeams);
            Collections.sort(confTeams, new CompTeamConfWins());
            confStandings.add(" ," + c.confName + " Conference, , ");
            Team t;
            for (int i = 0; i < confTeams.size(); ++i) {
                t = confTeams.get(i);
                confStandings.add(t.getRankStrStarUser(i + 1) + "," + t.strConfStandings() + "," + t.strTeamRecord() + "," + t.getConfWins() + "-" + t.getConfLosses());
            }
            confTeams.clear();
            confStandings.add(" , , , ");
        }
        return confStandings;
    }

    public ArrayList<String> getTeamRankings() {
        ArrayList<Team> teams = teamList; //(ArrayList<Team>) teamList.clone();
        ArrayList<String> rankings = new ArrayList<>();
        Team t;
        Collections.sort(teams, new CompTeamPoll());
        for (int i = 0; i < teams.size(); ++i) {
            t = teams.get(i);
            rankings.add(t.getRankStrStarUser(i + 1) + "," + t.name + "," + t.strTeamRecord() + "," + df2.format(t.teamPollScore));
        }
        return rankings;
    }

    private ArrayList<PlayerQB> rankQB() {
        heisman = null;
        ArrayList<PlayerQB> heismanCandidates = new ArrayList<>();
        for (int i = 0; i < teamList.size(); ++i) {
            for (int qb = 0; qb < teamList.get(i).teamQBs.size(); ++qb) {
                heismanCandidates.add(teamList.get(i).teamQBs.get(qb));
            }
        }
        Collections.sort(heismanCandidates, new CompPlayerHeisman());
        return heismanCandidates;
    }

    private ArrayList<PlayerRB> rankRB() {
        heisman = null;
        ArrayList<PlayerRB> heismanCandidates = new ArrayList<>();
        for (int i = 0; i < teamList.size(); ++i) {
            for (int rb = 0; rb < teamList.get(i).teamRBs.size(); ++rb) {
                heismanCandidates.add(teamList.get(i).teamRBs.get(rb));
            }
        }
        Collections.sort(heismanCandidates, new CompPlayerHeisman());
        return heismanCandidates;
    }

    private ArrayList<PlayerWR> rankWR() {
        heisman = null;
        ArrayList<PlayerWR> heismanCandidates = new ArrayList<>();
        for (int i = 0; i < teamList.size(); ++i) {
            for (int wr = 0; wr < teamList.get(i).teamWRs.size(); ++wr) {
                heismanCandidates.add(teamList.get(i).teamWRs.get(wr));
            }
        }
        Collections.sort(heismanCandidates, new CompPlayerHeisman());
        return heismanCandidates;
    }

    private ArrayList<PlayerTE> rankTE() {
        heisman = null;
        ArrayList<PlayerTE> heismanCandidates = new ArrayList<>();
        for (int i = 0; i < teamList.size(); ++i) {
            for (int te = 0; te < teamList.get(i).teamTEs.size(); ++te) {
                heismanCandidates.add(teamList.get(i).teamTEs.get(te));
            }
        }
        Collections.sort(heismanCandidates, new CompPlayerHeisman());
        return heismanCandidates;
    }

    private ArrayList<PlayerOL> rankOL() {
        heisman = null;
        ArrayList<PlayerOL> heismanCandidates = new ArrayList<>();
        for (int i = 0; i < teamList.size(); ++i) {
            for (int te = 0; te < teamList.get(i).teamOLs.size(); ++te) {
                heismanCandidates.add(teamList.get(i).teamOLs.get(te));
            }
        }
        Collections.sort(heismanCandidates, new CompPlayerHeisman());
        return heismanCandidates;
    }

    private ArrayList<PlayerK> rankK() {
        heisman = null;
        ArrayList<PlayerK> heismanCandidates = new ArrayList<>();
        for (int i = 0; i < teamList.size(); ++i) {
            for (int te = 0; te < teamList.get(i).teamKs.size(); ++te) {
                heismanCandidates.add(teamList.get(i).teamKs.get(te));
            }
        }
        Collections.sort(heismanCandidates, new CompPlayerHeisman());
        return heismanCandidates;
    }

    private ArrayList<PlayerDL> rankDL() {
        heisman = null;
        ArrayList<PlayerDL> heismanCandidates = new ArrayList<>();
        for (int i = 0; i < teamList.size(); ++i) {
            for (int dl = 0; dl < teamList.get(i).teamDLs.size(); ++dl) {
                heismanCandidates.add(teamList.get(i).teamDLs.get(dl));
            }
        }
        Collections.sort(heismanCandidates, new CompPlayerHeisman());
        return heismanCandidates;
    }

    private ArrayList<PlayerLB> rankLB() {
        heisman = null;
        ArrayList<PlayerLB> heismanCandidates = new ArrayList<>();
        for (int i = 0; i < teamList.size(); ++i) {
            for (int lb = 0; lb < teamList.get(i).teamLBs.size(); ++lb) {
                heismanCandidates.add(teamList.get(i).teamLBs.get(lb));
            }
        }
        Collections.sort(heismanCandidates, new CompPlayerHeisman());
        return heismanCandidates;
    }

    private ArrayList<PlayerCB> rankCB() {
        heisman = null;
        ArrayList<PlayerCB> heismanCandidates = new ArrayList<>();
        for (int i = 0; i < teamList.size(); ++i) {
            for (int cb = 0; cb < teamList.get(i).teamCBs.size(); ++cb) {
                heismanCandidates.add(teamList.get(i).teamCBs.get(cb));
            }
        }
        Collections.sort(heismanCandidates, new CompPlayerHeisman());
        return heismanCandidates;
    }

    private ArrayList<PlayerS> rankS() {
        heisman = null;
        ArrayList<PlayerS> heismanCandidates = new ArrayList<>();
        for (int i = 0; i < teamList.size(); ++i) {
            for (int s = 0; s < teamList.get(i).teamSs.size(); ++s) {
                heismanCandidates.add(teamList.get(i).teamSs.get(s));
            }
        }
        Collections.sort(heismanCandidates, new CompPlayerHeisman());
        return heismanCandidates;
    }

    private ArrayList<HeadCoach> rankHC() {
        heisman = null;
        ArrayList<HeadCoach> coachCandidates = new ArrayList<>();
        for (int i = 0; i < teamList.size(); ++i) {
            if (teamList.get(i).HC.get(0) != null) {
                coachCandidates.add(teamList.get(i).HC.get(0));
            }
        }
        Collections.sort(coachCandidates, new CompCoachScore());
        return coachCandidates;
    }


    //PLAYER RANKINGS STUFF

    public ArrayList<String> getPlayerRankStr(int selection) {
        int rankNum = 0;
        ArrayList<PlayerQB> pQB = new ArrayList<>();
        for (int i = 0; i < teamList.size(); ++i) {
            for (int p = 0; p < teamList.get(i).teamQBs.size(); ++p) {
                if (teamList.get(i).teamQBs.get(p).statsPassAtt >= (10 * currentWeek)) {
                    rankNum++;
                    pQB.add(teamList.get(i).teamQBs.get(p));
                }
            }
        }
        ArrayList<PlayerRB> pRB = new ArrayList<>();
        for (int i = 0; i < teamList.size(); ++i) {
            for (int p = 0; p < teamList.get(i).teamRBs.size(); ++p) {
                pRB.add(teamList.get(i).teamRBs.get(p));
            }
        }
        ArrayList<PlayerWR> pWR = new ArrayList<>();
        for (int i = 0; i < teamList.size(); ++i) {
            for (int p = 0; p < teamList.get(i).teamWRs.size(); ++p) {
                pWR.add(teamList.get(i).teamWRs.get(p));
            }
        }
        ArrayList<PlayerTE> pTE = new ArrayList<>();
        for (int i = 0; i < teamList.size(); ++i) {
            for (int p = 0; p < teamList.get(i).teamTEs.size(); ++p) {
                pTE.add(teamList.get(i).teamTEs.get(p));
            }
        }
        ArrayList<PlayerOL> pOL = new ArrayList<>();
        for (int i = 0; i < teamList.size(); ++i) {
            for (int p = 0; p < teamList.get(i).teamOLs.size(); ++p) {
                pOL.add(teamList.get(i).teamOLs.get(p));
            }
        }
        ArrayList<PlayerK> pK = new ArrayList<>();
        for (int i = 0; i < teamList.size(); ++i) {
            for (int p = 0; p < teamList.get(i).teamKs.size(); ++p) {
                pK.add(teamList.get(i).teamKs.get(p));
            }
        }
        ArrayList<PlayerDL> pDL = new ArrayList<>();
        for (int i = 0; i < teamList.size(); ++i) {
            for (int p = 0; p < teamList.get(i).teamDLs.size(); ++p) {
                pDL.add(teamList.get(i).teamDLs.get(p));
            }
        }
        ArrayList<PlayerLB> pLB = new ArrayList<>();
        for (int i = 0; i < teamList.size(); ++i) {
            for (int p = 0; p < teamList.get(i).teamLBs.size(); ++p) {
                pLB.add(teamList.get(i).teamLBs.get(p));
            }
        }
        ArrayList<PlayerCB> pCB = new ArrayList<>();
        for (int i = 0; i < teamList.size(); ++i) {
            for (int p = 0; p < teamList.get(i).teamCBs.size(); ++p) {
                pCB.add(teamList.get(i).teamCBs.get(p));
            }
        }
        ArrayList<PlayerS> pS = new ArrayList<>();
        for (int i = 0; i < teamList.size(); ++i) {
            for (int p = 0; p < teamList.get(i).teamSs.size(); ++p) {
                pS.add(teamList.get(i).teamSs.get(p));
            }
        }
        ArrayList<HeadCoach> HC = new ArrayList<>();
        for (int i = 0; i < teamList.size(); ++i) {
            if (teamList.get(i).HC.size() > 0) HC.add(teamList.get(i).HC.get(0));
        }

        ArrayList<PlayerOffense> off = new ArrayList<>();
        for (int i = 0; i < teamList.size(); ++i) {
            for (int p = 0; p < teamList.get(i).teamQBs.size(); ++p) {
                off.add(new PlayerOffense(teamList.get(i),
                        teamList.get(i).teamQBs.get(p).name,
                        "QB",
                        teamList.get(i).teamQBs.get(p).year,
                        teamList.get(i).teamQBs.get(p).statsRushYards,
                        teamList.get(i).teamQBs.get(p).statsRushTD,
                        0, 0, 0,
                        teamList.get(i).teamQBs.get(p).statsFumbles));
            }
        }
        for (int i = 0; i < teamList.size(); ++i) {
            for (int p = 0; p < teamList.get(i).teamRBs.size(); ++p) {
                off.add(new PlayerOffense(teamList.get(i),
                        teamList.get(i).teamRBs.get(p).name,
                        "RB",
                        teamList.get(i).teamRBs.get(p).year,
                        teamList.get(i).teamRBs.get(p).statsRushYards,
                        teamList.get(i).teamRBs.get(p).statsRushTD,
                        teamList.get(i).teamRBs.get(p).statsReceptions,
                        teamList.get(i).teamRBs.get(p).statsRecYards,
                        teamList.get(i).teamRBs.get(p).statsRecTD,
                        teamList.get(i).teamRBs.get(p).statsFumbles));
            }
        }
        for (int i = 0; i < teamList.size(); ++i) {
            for (int p = 0; p < teamList.get(i).teamWRs.size(); ++p) {
                off.add(new PlayerOffense(teamList.get(i),
                        teamList.get(i).teamWRs.get(p).name,
                        "WR",
                        teamList.get(i).teamWRs.get(p).year,
                        0, 0,
                        teamList.get(i).teamWRs.get(p).statsReceptions,
                        teamList.get(i).teamWRs.get(p).statsRecYards,
                        teamList.get(i).teamWRs.get(p).statsRecTD,
                        teamList.get(i).teamWRs.get(p).statsFumbles));
            }
        }
        for (int i = 0; i < teamList.size(); ++i) {
            for (int p = 0; p < teamList.get(i).teamTEs.size(); ++p) {
                off.add(new PlayerOffense(teamList.get(i),
                        teamList.get(i).teamTEs.get(p).name,
                        "TE",
                        teamList.get(i).teamTEs.get(p).year,
                        0, 0,
                        teamList.get(i).teamTEs.get(p).statsReceptions,
                        teamList.get(i).teamTEs.get(p).statsRecYards,
                        teamList.get(i).teamTEs.get(p).statsRecTD,
                        teamList.get(i).teamTEs.get(p).statsFumbles));
            }
        }

        ArrayList<PlayerDefense> def = new ArrayList<>();
        for (int i = 0; i < teamList.size(); ++i) {
            for (int p = 0; p < teamList.get(i).teamDLs.size(); ++p) {
                def.add(new PlayerDefense(teamList.get(i),
                        teamList.get(i).teamDLs.get(p).name,
                        "DL",
                        teamList.get(i).teamDLs.get(p).year,
                        teamList.get(i).teamDLs.get(p).statsTackles,
                        teamList.get(i).teamDLs.get(p).statsSacks,
                        teamList.get(i).teamDLs.get(p).statsFumbles,
                        teamList.get(i).teamDLs.get(p).statsInts));
            }
            for (int p = 0; p < teamList.get(i).teamLBs.size(); ++p) {
                def.add(new PlayerDefense(teamList.get(i),
                        teamList.get(i).teamLBs.get(p).name,
                        "LB",
                        teamList.get(i).teamLBs.get(p).year,
                        teamList.get(i).teamLBs.get(p).statsTackles,
                        teamList.get(i).teamLBs.get(p).statsSacks,
                        teamList.get(i).teamLBs.get(p).statsFumbles,
                        teamList.get(i).teamLBs.get(p).statsInts));
            }
            for (int p = 0; p < teamList.get(i).teamCBs.size(); ++p) {
                def.add(new PlayerDefense(teamList.get(i),
                        teamList.get(i).teamCBs.get(p).name,
                        "CB",
                        teamList.get(i).teamCBs.get(p).year,
                        teamList.get(i).teamCBs.get(p).statsTackles,
                        teamList.get(i).teamCBs.get(p).statsSacks,
                        teamList.get(i).teamCBs.get(p).statsFumbles,
                        teamList.get(i).teamCBs.get(p).statsInts));
            }
            for (int p = 0; p < teamList.get(i).teamSs.size(); ++p) {
                def.add(new PlayerDefense(teamList.get(i),
                        teamList.get(i).teamSs.get(p).name,
                        "S",
                        teamList.get(i).teamSs.get(p).year,
                        teamList.get(i).teamSs.get(p).statsTackles,
                        teamList.get(i).teamSs.get(p).statsSacks,
                        teamList.get(i).teamSs.get(p).statsFumbles,
                        teamList.get(i).teamSs.get(p).statsInts));
            }
        }

        ArrayList<PlayerReturner> returner = new ArrayList<>();
        int retNum = 0;
        for (int i = 0; i < teamList.size(); ++i) {
            for (int p = 0; p < teamList.get(i).teamRBs.size(); ++p) {
                if (teamList.get(i).teamRBs.get(p).statsKickRets > 0 || teamList.get(i).teamRBs.get(p).statsPuntRets > 0) {
                    returner.add(new PlayerReturner(teamList.get(i).abbr,
                            teamList.get(i).teamRBs.get(p).name,
                            "RB",
                            teamList.get(i).teamRBs.get(p).statsKickRets,
                            teamList.get(i).teamRBs.get(p).statsKickRetYards,
                            teamList.get(i).teamRBs.get(p).statsKickRetTDs,
                            teamList.get(i).teamRBs.get(p).statsPuntRets,
                            teamList.get(i).teamRBs.get(p).statsPuntRetYards,
                            teamList.get(i).teamRBs.get(p).statsPuntRetTDs));
                    retNum++;
                }
            }
            for (int p = 0; p < teamList.get(i).teamWRs.size(); ++p) {
                if (teamList.get(i).teamWRs.get(p).statsKickRets > 0 || teamList.get(i).teamWRs.get(p).statsPuntRets > 0) {
                    returner.add(new PlayerReturner(teamList.get(i).abbr,
                            teamList.get(i).teamWRs.get(p).name,
                            "WR",
                            teamList.get(i).teamWRs.get(p).statsKickRets,
                            teamList.get(i).teamWRs.get(p).statsKickRetYards,
                            teamList.get(i).teamWRs.get(p).statsKickRetTDs,
                            teamList.get(i).teamWRs.get(p).statsPuntRets,
                            teamList.get(i).teamWRs.get(p).statsPuntRetYards,
                            teamList.get(i).teamWRs.get(p).statsPuntRetTDs));
                    retNum++;
                }
            }
            for (int p = 0; p < teamList.get(i).teamCBs.size(); ++p) {
                if (teamList.get(i).teamCBs.get(p).statsKickRets > 0 || teamList.get(i).teamCBs.get(p).statsPuntRets > 0) {
                    returner.add(new PlayerReturner(teamList.get(i).abbr,
                            teamList.get(i).teamCBs.get(p).name,
                            "CB",
                            teamList.get(i).teamCBs.get(p).statsKickRets,
                            teamList.get(i).teamCBs.get(p).statsKickRetYards,
                            teamList.get(i).teamCBs.get(p).statsKickRetTDs,
                            teamList.get(i).teamCBs.get(p).statsPuntRets,
                            teamList.get(i).teamCBs.get(p).statsPuntRetYards,
                            teamList.get(i).teamCBs.get(p).statsPuntRetTDs));
                    retNum++;
                }
            }
        }


        ArrayList<String> rankings = new ArrayList<>();
        switch (selection) {
            case 0:
                Collections.sort(pQB, new CompPlayerPassRating());
                for (int i = 0; i < rankNum; ++i) {
                    rankings.add((i + 1) + ". ," + pQB.get(i).name + "," + pQB.get(i).team.abbr + "," + df2.format(pQB.get(i).getPasserRating()));
                }
                break;
            case 1:
                Collections.sort(pQB, new CompPlayerPassYards());
                for (int i = 0; i < rankNum; ++i) {
                    rankings.add((i + 1) + ". ," + pQB.get(i).name + "," + pQB.get(i).team.abbr + "," + pQB.get(i).statsPassYards);
                }
                break;
            case 2:
                Collections.sort(pQB, new CompPlayerPassTDs());
                for (int i = 0; i < rankNum; ++i) {
                    rankings.add((i + 1) + ". ," + pQB.get(i).name + "," + pQB.get(i).team.abbr + "," + pQB.get(i).statsPassTD);
                }
                break;
            case 3:
                Collections.sort(pQB, new CompPlayerPassInts());
                for (int i = 0; i < rankNum; ++i) {
                    rankings.add((i + 1) + ". ," + pQB.get(i).name + "," + pQB.get(i).team.abbr + "," + pQB.get(i).statsInt);
                }
                break;
            case 4:
                Collections.sort(pQB, new CompPlayerPassPCT());
                for (int i = 0; i < rankNum; ++i) {
                    rankings.add((i + 1) + ". ," + pQB.get(i).name + "," + pQB.get(i).team.abbr + "," + df2.format(pQB.get(i).getPassPCT()) + "%");
                }
                break;
            case 5:
                Collections.sort(off, new CompPlayerRushYards());
                for (int i = 0; i < rankNum; ++i) {
                    rankings.add((i + 1) + ". ," + off.get(i).position + " " + off.get(i).name + "," + off.get(i).team.abbr + "," + off.get(i).rushYards);
                }
                break;
            case 6:
                Collections.sort(off, new CompPlayerRushTDs());
                for (int i = 0; i < rankNum; ++i) {
                    rankings.add((i + 1) + ". ," + off.get(i).position + " " + off.get(i).name + "," + off.get(i).team.abbr + "," + off.get(i).rushTDs);
                }
                break;
            case 7:
                Collections.sort(off, new CompPlayerReceptions());
                for (int i = 0; i < rankNum; ++i) {
                    rankings.add((i + 1) + ". ," + off.get(i).position + " " + off.get(i).name + "," + off.get(i).team.abbr + "," + off.get(i).receptions);
                }
                break;
            case 8:
                Collections.sort(off, new CompPlayerRecYards());
                for (int i = 0; i < rankNum; ++i) {
                    rankings.add((i + 1) + ". ," + off.get(i).position + " " + off.get(i).name + "," + off.get(i).team.abbr + "," + off.get(i).receptionYards);
                }
                break;
            case 9:
                Collections.sort(off, new CompPlayerRecTDs());
                for (int i = 0; i < rankNum; ++i) {
                    rankings.add((i + 1) + ". ," + off.get(i).position + " " + off.get(i).name + "," + off.get(i).team.abbr + "," + off.get(i).receptionTDs);
                }
                break;
            case 10:
                Collections.sort(def, new CompPlayerTackles());
                for (int i = 0; i < rankNum; ++i) {
                    rankings.add((i + 1) + ". ," + def.get(i).position + " " + def.get(i).name + "," + def.get(i).team.abbr + "," + def.get(i).tackles);
                }
                break;
            case 11:
                Collections.sort(def, new CompPlayerSacks());
                for (int i = 0; i < rankNum; ++i) {
                    rankings.add((i + 1) + ". ," + def.get(i).position + " " + def.get(i).name + "," + def.get(i).team.abbr + "," + def.get(i).sacks);
                }
                break;
            case 12:
                Collections.sort(def, new CompPlayerFumblesRec());
                for (int i = 0; i < rankNum; ++i) {
                    rankings.add((i + 1) + ". ," + def.get(i).position + " " + def.get(i).name + "," + def.get(i).team.abbr + "," + def.get(i).fumbles);
                }
                break;
            case 13:
                Collections.sort(def, new CompPlayerInterceptions());
                for (int i = 0; i < rankNum; ++i) {
                    rankings.add((i + 1) + ". ," + def.get(i).position + " " + def.get(i).name + "," + def.get(i).team.abbr + "," + def.get(i).interceptions);
                }
                break;
            case 14:
                Collections.sort(pK, new CompPlayerFGMade());
                for (int i = 0; i < rankNum; ++i) {
                    rankings.add((i + 1) + ". ," + pK.get(i).name + "," + pK.get(i).team.abbr + "," + pK.get(i).statsFGMade);
                }
                break;
            case 15:
                Collections.sort(pK, new CompPlayerFGpct());
                for (int i = 0; i < rankNum; ++i) {
                    rankings.add((i + 1) + ". ," + pK.get(i).name + "," + pK.get(i).team.abbr + "," + pK.get(i).getFGpct() + "%");
                }
                break;
            case 16:
                Collections.sort(returner, new CompKickRetYards());
                for (int i = 0; i < retNum; ++i) {
                    rankings.add((i + 1) + ". ," + returner.get(i).name + "," + returner.get(i).team + "," + returner.get(i).kYards);
                }
                break;
            case 17:
                Collections.sort(returner, new CompKickRetTD());
                for (int i = 0; i < retNum; ++i) {
                    rankings.add((i + 1) + ". ," + returner.get(i).name + "," + returner.get(i).team + "," + returner.get(i).kTD);
                }
                break;
            case 18:
                Collections.sort(returner, new CompPuntRetYards());
                for (int i = 0; i < retNum; ++i) {
                    rankings.add((i + 1) + ". ," + returner.get(i).name + "," + returner.get(i).team + "," + returner.get(i).pYards);
                }
                break;
            case 19:
                Collections.sort(returner, new CompPuntRetTDs());
                for (int i = 0; i < retNum; ++i) {
                    rankings.add((i + 1) + ". ," + returner.get(i).name + "," + returner.get(i).team + "," + returner.get(i).pTD);
                }
                break;
            case 20:
                Collections.sort(HC, new CompCoachOvr());
                for (int i = 0; i < HC.size(); ++i) {
                    rankings.add((i + 1) + ". ," + HC.get(i).name + "," + HC.get(i).team.abbr + "," + HC.get(i).getHCOverall());
                }
                break;
            case 21:
                Collections.sort(HC, new CompCoachScore());
                for (int i = 0; i < HC.size(); ++i) {
                    rankings.add((i + 1) + ". ," + HC.get(i).name + "," + HC.get(i).team.abbr + "," + HC.get(i).getCoachScore());
                }
                break;
        }
        return rankings;
    }

    public ArrayList<String> getAwardsWatch(int selection) {
        int rankNum = 40;
        ArrayList<String> rankings = new ArrayList<>();
        switch (selection) {
            case 0:
                ArrayList<HeadCoach> HC = rankHC();
                Collections.sort(HC, new CompCoachScore());
                for (int i = 0; i < HC.size(); ++i) {
                    rankings.add((i + 1) + ". ," + HC.get(i).name + " (" + HC.get(i).team.abbr + ")," + HC.get(i).getCoachScore());
                }
                break;
            case 1:
                ArrayList<PlayerQB> pQB = rankQB();
                Collections.sort(pQB, new CompPlayerHeisman());
                for (int i = 0; i < rankNum; ++i) {
                    rankings.add((i + 1) + ". ," + pQB.get(i).name + " (" + pQB.get(i).team.abbr + ")," + pQB.get(i).getHeismanScore());
                }
                break;
            case 2:
                ArrayList<PlayerRB> pRB = rankRB();
                Collections.sort(pRB, new CompPlayerHeisman());
                for (int i = 0; i < rankNum; ++i) {
                    rankings.add((i + 1) + ". ," + pRB.get(i).name + " (" + pRB.get(i).team.abbr + ")," + pRB.get(i).getHeismanScore());
                }
                break;
            case 3:
                ArrayList<PlayerWR> pWR = rankWR();
                Collections.sort(pWR, new CompPlayerHeisman());
                for (int i = 0; i < rankNum; ++i) {
                    rankings.add((i + 1) + ". ," + pWR.get(i).name + " (" + pWR.get(i).team.abbr + ")," + pWR.get(i).getHeismanScore());
                }
                break;
            case 4:
                ArrayList<PlayerTE> pTE = rankTE();
                Collections.sort(pTE, new CompPlayerHeisman());
                for (int i = 0; i < rankNum; ++i) {
                    rankings.add((i + 1) + ". ," + pTE.get(i).name + " (" + pTE.get(i).team.abbr + ")," + pTE.get(i).getHeismanScore());
                }
                break;
            case 5:
                ArrayList<PlayerOL> pOL = rankOL();
                Collections.sort(pOL, new CompPlayerHeisman());
                for (int i = 0; i < rankNum; ++i) {
                    rankings.add((i + 1) + ". ," + pOL.get(i).name + " (" + pOL.get(i).team.abbr + ")," + pOL.get(i).getHeismanScore());
                }
                break;
            case 6:
                ArrayList<PlayerK> pK = rankK();
                Collections.sort(pK, new CompPlayerHeisman());
                for (int i = 0; i < rankNum; ++i) {
                    rankings.add((i + 1) + ". ," + pK.get(i).name + " (" + pK.get(i).team.abbr + ")," + pK.get(i).getHeismanScore());
                }
                break;
            case 7:
                ArrayList<PlayerDL> pDL = rankDL();
                Collections.sort(pDL, new CompPlayerHeisman());
                for (int i = 0; i < rankNum; ++i) {
                    rankings.add((i + 1) + ". ," + pDL.get(i).name + " (" + pDL.get(i).team.abbr + ")," + pDL.get(i).getHeismanScore());
                }
                break;
            case 8:
                ArrayList<PlayerLB> pLB = rankLB();
                Collections.sort(pLB, new CompPlayerHeisman());
                for (int i = 0; i < rankNum; ++i) {
                    rankings.add((i + 1) + ". ," + pLB.get(i).name + " (" + pLB.get(i).team.abbr + ")," + pLB.get(i).getHeismanScore());
                }
                break;
            case 9:
                ArrayList<PlayerCB> pCB = rankCB();
                Collections.sort(pCB, new CompPlayerHeisman());
                for (int i = 0; i < rankNum; ++i) {
                    rankings.add((i + 1) + ". ," + pCB.get(i).name + " (" + pCB.get(i).team.abbr + ")," + pCB.get(i).getHeismanScore());
                }
                break;
            case 10:
                ArrayList<PlayerS> pS = rankS();
                Collections.sort(pS, new CompPlayerHeisman());
                for (int i = 0; i < rankNum; ++i) {
                    rankings.add((i + 1) + ". ," + pS.get(i).name + " (" + pS.get(i).team.abbr + ")," + pS.get(i).getHeismanScore());
                }
                break;
        }
        return rankings;
    }


    /**
     * See if team name is in use, or has illegal characters.
     *
     * @param name team name
     * @return true if valid, false if not
     */
    public boolean isNameValid(String name) {
        if (name.length() == 0) {
            return false;
        }
        return !(name.contains(",") || name.contains(">") || name.contains("%") || name.contains("\\"));
    }

    /**
     * See if team abbr is in use, or has illegal characters, or is not 3 characters
     *
     * @param abbr new abbr
     * @return true if valid, false if not
     */
    public boolean isAbbrValid(String abbr) {
        if (abbr.length() > 4 || abbr.length() == 0) {
            // Only 4 letter abbr allowed
            return false;
        }

        return !(abbr.contains(",") || abbr.contains(">") || abbr.contains("%") || abbr.contains("\\") || abbr.contains(" "));

    }

    public String getRankStr(int num) {
        if (num == 11) {
            return "11th";
        } else if (num == 12) {
            return "12th";
        } else if (num == 13) {
            return "13th";
        } else if (num % 10 == 1) {
            return num + "st";
        } else if (num % 10 == 2) {
            return num + "nd";
        } else if (num % 10 == 3) {
            return num + "rd";
        } else {
            return num + "th";
        }
    }

    public int getAvgCoachTal() {
        int avg = 0;
        for (int i = 0; i < teamList.size(); i++) {
            if (teamList.get(i) != userTeam && teamList.get(i).HC.size() > 0)
                avg += teamList.get(i).HC.get(0).ratTalent;
        }
        return avg / (teamList.size() - 1);
    }

    public int getAvgCoachDis() {
        int avg = 0;
        for (int i = 0; i < teamList.size(); i++) {
            if (teamList.get(i) != userTeam && teamList.get(i).HC.size() > 0)
                avg += teamList.get(i).HC.get(0).ratDiscipline;
        }
        return avg / (teamList.size() - 1);
    }

    public int getAvgCoachOff() {
        int avg = 0;
        for (int i = 0; i < teamList.size(); i++) {
            if (teamList.get(i) != userTeam && teamList.get(i).HC.size() > 0)
                avg += teamList.get(i).HC.get(0).ratOff;
        }
        return avg / (teamList.size() - 1);
    }

    public int getAvgCoachDef() {
        int avg = 0;
        for (int i = 0; i < teamList.size(); i++) {
            if (teamList.get(i) != userTeam && teamList.get(i).HC.size() > 0)
                avg += teamList.get(i).HC.get(0).ratDef;
        }
        return avg / (teamList.size() - 1);
    }


    public String getFreeAgentCoachSave() {
        for (HeadCoach h : coachFreeAgents) {
            h.age++;
        }
        //Adding to the Available Coach List Pool
        for (HeadCoach h : coachList) {
            h.ratPot = h.ratPot + (int) Math.random() * 15;
            coachFreeAgents.add(h);
        }
        Collections.sort(coachFreeAgents, new CompCoachOvr());
        StringBuilder sb = new StringBuilder();

        for (HeadCoach h : coachFreeAgents) {
            if (h.age < 56) {
                sb.append(h.name + "," + (h.age) + "," + h.year + "," + h.ratPot + "," + (h.ratOff + (int) Math.random() * 3) + "," + (h.ratDef + (int) Math.random() * 3) + "," + (h.ratTalent + (int) Math.random() * 3) + "," + (h.ratDiscipline + (int) Math.random() * 3)
                        + "," + h.offStrat + "," + h.defStrat + "," + h.baselinePrestige + "," + h.wins + "," + h.losses + "," + h.bowlwins + "," + h.bowllosses + "," + h.confchamp + "," + h.natchamp + "," + h.allconference
                        + "," + h.allamericans + "," + h.confAward + "," + h.awards + "% " + "\n");
                for (String s : h.history) {
                    sb.append(s + "\n");
                }
                sb.append("END_FREE_AGENT\n");
            }
        }

        return sb.toString();
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Save League in a file.
     *
     * @param saveFile file to be overwritten
     * @return true if successful
     */
    public boolean saveLeague(File saveFile) {

        //Need method for saving mid-season

        StringBuilder sb = new StringBuilder();

        // Save information about the save file, user team info
        sb.append((seasonStart + leagueHistory.size()) + ": " + userTeam.HC.get(0).getInitialName() + ", " + userTeam.abbr + " (" + (userTeam.HC.get(0).wins - userTeam.wins) + "-" + (userTeam.HC.get(0).losses - userTeam.losses) + ") " +
                userTeam.HC.get(0).confchamp + " CC, " + userTeam.HC.get(0).natchamp + " NC [" + userTeam.HC.get(0).getCoachCareerScore() + "]>" + teamList.size() + ">" + saveVer + "%\n");

        // Save league history of who was #1 each year
        for (int i = 0; i < leagueHistory.size(); ++i) {
            for (int j = 0; j < leagueHistory.get(i).length; ++j) {
                sb.append(leagueHistory.get(i)[j] + "%");
            }
            sb.append("\n");
        }
        sb.append("END_LEAGUE_HIST\n");

        // Save POTY history of who won each year
        // Go through leagueHist size in case they save after the Heisman Ceremony
        for (int i = 0; i < leagueHistory.size(); ++i) {
            sb.append(heismanHistory.get(i) + "\n");
        }
        sb.append("END_HEISMAN_HIST\n");


        //Save Conference Names
        for (int i = 0; i < conferences.size(); ++i) {
            sb.append(conferences.get(i).getConfSaveString());
        }
        sb.append("END_CONFERENCES\n");

        // Save information about each team like W-L records, as well as all the players
        for (Team t : teamList) {
            sb.append(t.getTeamSaveString());
            sb.append(t.getPlayerInfoSaveFile());
            sb.append("END_PLAYERS\n");
        }

        // Save history of the user's team of the W-L and bowl results each year
        sb.append(userTeam.name + "\n");
        sb.append("END_USER_TEAM\n");
        //Every Team Year-by-Year History
        for (Team t : teamList) {
            for (String s : t.teamHistory) {
                sb.append(s + "\n");
            }
            sb.append("END_TEAM\n");
        }
        sb.append("END_TEAM_HISTORY\n");


        for (Team t : teamList) {
            for (String s : t.HC.get(0).history) {
                sb.append(s + "\n");
            }
            sb.append("END_COACH\n");
        }
        sb.append("END_COACH_HISTORY\n");

        for (String bowlName : bowlNames) {
            sb.append(bowlName + ",");
        }
        sb.append("\nEND_BOWL_NAMES\n");

        // Save league records
        sb.append(leagueRecords.getRecordsStr());
        sb.append("END_LEAGUE_RECORDS\n");

        sb.append(yearStartLongestWinStreak.getStreakCSV());
        sb.append("\nEND_LEAGUE_WIN_STREAK\n");

        // Save user team records
        for (Team t : teamList) {
            sb.append(t.teamRecords.getRecordsStr());
            sb.append("END_TEAM\n");
        }
        sb.append("END_TEAM_RECORDS\n");

        for (String s : leagueHoF) {
            sb.append(s + "\n");
        }
        sb.append("END_LEAGUE_HALL_OF_FAME\n");

        sb.append(getFreeAgentCoachSave());
        sb.append("END_COACHES\n");


        sb.append(fullGameLog + "\n");
        sb.append("END_GAME_LOG\n");
        sb.append(showPotential + "\n");
        sb.append("END_HIDE_POTENTIAL\n");
        sb.append(confRealignment + "\n");
        sb.append("END_CONF_REALIGNMENT\n");
        sb.append(enableTV + "\n");
        sb.append("END_ENABLE_TV\n");
        sb.append(enableUnivProRel + "\n");
        sb.append("END_PRO_REL\n");
        sb.append(userTeam.showPopups + "\n");
        sb.append("END_POPUPS\n");
        sb.append(neverRetire + "\n");
        sb.append("END_NEVER_RETIRE\n");
        sb.append(careerMode + "\n");
        sb.append("END_CAREER_MODE\n");
        sb.append(expPlayoffs + "\n");
        sb.append("END_EXP_PLAYOFFS\n");
        sb.append(advancedRealignment + "\n");
        sb.append("END_ADV_CONF_REALIGNMENT\n");

        sb.append("\nEND_SAVE_FILE");

        // Actually write to the file
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(saveFile), "utf-8"))) {
            writer.write(sb.toString());
            return true;
        } catch (Exception e) {
            return false;
        }
    }


}
