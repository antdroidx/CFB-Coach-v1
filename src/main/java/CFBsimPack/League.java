package CFBsimPack;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import antdroid.cfbcoach.MainActivity;

/**
 * League class. Has 6 conferences of 10 teams each.
 * @author Achi
 */
public class League {
    //Lists of conferences/teams
    public ArrayList<String[]> leagueHistory;
    public ArrayList<String> heismanHistory;
    public ArrayList<Conference> conferences;
    public ArrayList<Team> teamList;
    public ArrayList<String> nameList;
    public ArrayList<String> lastNameList;
    public ArrayList< ArrayList<String> > newsStories;

    public LeagueRecords leagueRecords;
    public LeagueRecords userTeamRecords;
    public TeamStreak longestWinStreak;
    public TeamStreak yearStartLongestWinStreak;
    public TeamStreak longestActiveWinStreak;

    // News Story Variables
    public Team saveBless;
    public Team saveBless2;
    public Team saveBless3;
    public Team saveBless4;
    public Team saveBless5;
    public Team saveCurse;
    public Team saveCurse2;
    public Team saveCurse3;
    public Team saveCurse4;
    public Team saveCurse5;
    public boolean blessDevelopingStory;
    public int blessDevelopingWeek;
    public int blessDevelopingCase;
    public boolean curseDevelopingStory;
    public int curseDevelopingWeek;
    public int curseDevelopingCase;
    public String storyFullName;
    public String storyFirstName;
    public String storyLastName;

    //Current week, 1-14
    public int currentWeek;
    public int randgm;
    public int randconf;
    int seasonStart = 2017;
    int countTeam = 120;

    //Bowl Games
    public boolean hasScheduledBowls;
    public Game semiG14;
    public Game semiG23;
    public Game ncg;
    public Game[] bowlGames;
    public int countBG = 14;
    public Game weeklyScores;

    //User Team
    public Team userTeam;

    boolean heismanDecided;
    Player heisman;
    ArrayList<Player> heismanCandidates;
    private String heismanWinnerStrFull;

    ArrayList<Player> allAmericans;
    private String allAmericanStr;

    public String[] bowlNames = {"Rose Bowl", "Orange Bowl", "Sugar Bowl", "Fiesta Bowl", "Peach Bowl",
            "Liberty Bowl", "Cotton Bowl", "Holiday Bowl", "Citrus Bowl", "Alamo Bowl", "Aloha Bowl", "Independence Bowl", "Vegas Bowl","Cactus Bowl"};


    /**
     * Creates League, sets up Conferences, reads team names and conferences from file.
     * Also schedules games for every team.
     */
    public League(String namesCSV, String lastNamesCSV) {
        heismanDecided = false;
        hasScheduledBowls = false;
        bowlGames = new Game[countBG];
        leagueHistory = new ArrayList<String[]>();
        heismanHistory = new ArrayList<String>();
        currentWeek = 0;
        conferences = new ArrayList<Conference>();
        conferences.add(new Conference("ACC", this));
        conferences.add(new Conference("American", this));
        conferences.add(new Conference("Big Ten", this));
        conferences.add(new Conference("Big 12", this));
        conferences.add(new Conference("Conf USA", this));
        conferences.add(new Conference("MAC", this));
        conferences.add(new Conference("Mt West", this));
        conferences.add(new Conference("Pac-12", this));
        conferences.add(new Conference("SEC", this));
        conferences.add(new Conference("Sun Belt", this));
        allAmericans = new ArrayList<Player>();

        // Initialize new stories lists
        newsStories = new ArrayList< ArrayList<String> >();
        for (int i = 0; i < 16; ++i) {
            newsStories.add( new ArrayList<String>() );
        }
        newsStories.get(0).add("New Season!>Ready for the new season, coach? Whether the National Championship is " +
                "on your mind, or just a winning season, good luck!");

        leagueRecords = new LeagueRecords();
        userTeamRecords = new LeagueRecords();
        longestWinStreak = new TeamStreak(getYear(), getYear(), 0, "XXX");
        yearStartLongestWinStreak = new TeamStreak(getYear(), getYear(), 0, "XXX");
        longestActiveWinStreak = new TeamStreak(getYear(), getYear(), 0, "XXX");

        // Read first names from file
        nameList = new ArrayList<String>();
        String[] namesSplit = namesCSV.split(",");
        for (String n : namesSplit) {
            nameList.add(n.trim());
        }

        // Read last names from file
        lastNameList = new ArrayList<String>();
        namesSplit = lastNamesCSV.split(",");
        for (String n : namesSplit) {
            lastNameList.add(n.trim());
        }

        //Set up conference
        // FUTURE: READ FROM XML OR CSV FILE

        //ACC - done + 12
        conferences.get(0).confTeams.add( new Team( "Clemson", "CLEM", "ACC", this, 90, "WAKE" ));
        conferences.get(0).confTeams.add( new Team( "Duke", "DUKE", "ACC", this, 60, "UNC" ));
        conferences.get(0).confTeams.add( new Team( "Florida St", "FSU", "ACC", this, 70, "MIA" ));
        conferences.get(0).confTeams.add( new Team( "Georgia Tech", "GT", "ACC", this, 48, "NCST" ));
        conferences.get(0).confTeams.add( new Team( "Louisville", "LOUI", "ACC", this, 75, "PITT" ));
        conferences.get(0).confTeams.add( new Team( "Miami", "MIA", "ACC", this, 73, "FSU" ));
        conferences.get(0).confTeams.add( new Team( "North Carolina", "UNC", "ACC", this, 47, "DUKE" ));
        conferences.get(0).confTeams.add( new Team( "NC State", "NCST", "ACC", this, 60, "GT" ));
        conferences.get(0).confTeams.add( new Team( "Pittsburgh", "PITT", "ACC", this, 65, "LOUI" ));
        conferences.get(0).confTeams.add( new Team( "Wake Forest", "WAKE", "ACC", this, 55, "CLEM" ));
        conferences.get(0).confTeams.add( new Team( "Virginia", "VIR", "ACC", this, 48, "VTEC" ));
        conferences.get(0).confTeams.add( new Team( "Virginia Tech", "VTEC", "ACC", this, 73, "VIR" ));

        //American - done x 12
        conferences.get(1).confTeams.add( new Team( "Boston College", "BC", "American", this, 38, "TEM" ));
        conferences.get(1).confTeams.add( new Team( "Central Florida", "UCF", "American", this, 60, "USF" ));
        conferences.get(1).confTeams.add( new Team( "Cinncinati", "CINN", "American", this, 46, "MEMP" ));
        conferences.get(1).confTeams.add( new Team( "Maryland", "MARY", "American", this, 49, "RUT" ));
        conferences.get(1).confTeams.add( new Team( "Memphis", "MEMP", "American", this, 67, "CINN" ));
        conferences.get(1).confTeams.add( new Team( "Notre Dame", "ND", "American", this, 73, "WVU" ));
        conferences.get(1).confTeams.add( new Team( "Rutgers", "RUT", "American", this, 40, "MARY" ));
        conferences.get(1).confTeams.add( new Team( "South Florida", "USF", "American", this, 68, "UCF" ));
        conferences.get(1).confTeams.add( new Team( "Syracuse", "SYR", "American", this, 47, "UCON" ));
        conferences.get(1).confTeams.add( new Team( "Temple", "TEM", "American", this, 46, "BC" ));
        conferences.get(1).confTeams.add( new Team( "UConn", "UCON", "American", this, 37, "SYR" ));
        conferences.get(1).confTeams.add( new Team( "West Virginia", "WVU", "American", this, 71, "ND" ));

        //Big Ten - done x 12
        conferences.get(2).confTeams.add( new Team( "Illinois", "ILL", "Big Ten", this, 45, "IND" ));
        conferences.get(2).confTeams.add( new Team( "Indiana", "IND", "Big Ten", this, 42, "ILL" ));
        conferences.get(2).confTeams.add( new Team( "Iowa", "IOW", "Big Ten", this, 63, "NEB" ));
        conferences.get(2).confTeams.add( new Team( "Michigan", "MIC", "Big Ten", this, 85, "OSU" ));
        conferences.get(2).confTeams.add( new Team( "Michigan St", "MSU", "Big Ten", this, 65, "PSU" ));
        conferences.get(2).confTeams.add( new Team( "Minnesota", "MIN", "Big Ten", this, 60, "WIS" ));
        conferences.get(2).confTeams.add( new Team( "Nebraska", "NEB", "Big Ten", this, 72, "IOW" ));
        conferences.get(2).confTeams.add( new Team( "Northwestern", "NWU", "Big Ten", this, 55, "PUR" ));
        conferences.get(2).confTeams.add( new Team( "Ohio State", "OSU", "Big Ten", this, 85, "MIC" ));
        conferences.get(2).confTeams.add( new Team( "Penn State", "PSU", "Big Ten", this, 84, "MSU" ));
        conferences.get(2).confTeams.add( new Team( "Purdue", "PUR", "Big Ten", this, 52, "NWU" ));
        conferences.get(2).confTeams.add( new Team( "Wisconsin", "WIS", "Big Ten", this, 78, "MIN" ));

        //Big 12 - done x 12
        conferences.get(3).confTeams.add( new Team( "Baylor", "BAY", "Big 12", this, 52, "TCU" ));
        conferences.get(3).confTeams.add( new Team( "Houston", "HOU", "Big 12", this, 58, "TTEC" ));
        conferences.get(3).confTeams.add( new Team( "Iowa State", "ISU", "Big 12", this, 49, "MIZ" ));
        conferences.get(3).confTeams.add( new Team( "Texas Tech", "TTEC", "Big 12", this, 51, "HOU" ));
        conferences.get(3).confTeams.add( new Team( "Kansas", "KAN", "Big 12", this, 35, "KSU" ));
        conferences.get(3).confTeams.add( new Team( "Kansas St", "KSU", "Big 12", this, 72, "KAN" ));
        conferences.get(3).confTeams.add( new Team( "Oklahoma", "OKL", "Big 12", this, 87, "TEX" ));
        conferences.get(3).confTeams.add( new Team( "Oklahoma St", "OKST", "Big 12", this, 74, "TXAM" ));
        conferences.get(3).confTeams.add( new Team( "Texas", "TEX", "Big 12", this, 65, "OKL" ));
        conferences.get(3).confTeams.add( new Team( "Texas AM", "TXAM", "Big 12", this, 71, "OKST" ));
        conferences.get(3).confTeams.add( new Team( "TCU", "TCU", "Big 12", this, 75, "BAY" ));
        conferences.get(3).confTeams.add( new Team( "Missouri", "MIZ", "Big 12", this, 52, "ISU" ));

        //Conf USA x 12
        conferences.get(4).confTeams.add( new Team( "Army", "ARMY", "Conf USA", this, 40, "NAVY" ));
        conferences.get(4).confTeams.add( new Team( "East Carolina", "ECU", "Conf USA", this, 30, "WKU" ));
        conferences.get(4).confTeams.add( new Team( "Florida Atl", "FAU", "Conf USA", this, 38, "FIU" ));
        conferences.get(4).confTeams.add( new Team( "Florida Intl", "FIU", "Conf USA", this, 38, "FAU" ));
        conferences.get(4).confTeams.add( new Team( "LA Tech", "LTEC", "Conf USA", this, 47, "TUL" ));
        conferences.get(4).confTeams.add( new Team( "Marshall", "MARS", "Conf USA", this, 48, "SMU" ));
        conferences.get(4).confTeams.add( new Team( "Navy", "NAVY", "Conf USA", this, 55, "ARMY" ));
        conferences.get(4).confTeams.add( new Team( "Southern Meth", "SMU", "Conf USA", this, 48, "MARS" ));
        conferences.get(4).confTeams.add( new Team( "Southern Miss", "SMIS", "Conf USA", this, 41, "UAB" ));
        conferences.get(4).confTeams.add( new Team( "Tulane", "TUL", "Conf USA", this, 36, "LTEC" ));
        conferences.get(4).confTeams.add( new Team( "UAB", "UAB", "Conf USA", this, 32, "SMIS" ));
        conferences.get(4).confTeams.add( new Team( "Western Kentucky", "WKU", "Conf USA", this, 43, "ECU" ));

        // MAC - x 12
        conferences.get(5).confTeams.add( new Team( "Akron", "AKR", "MAC", this, 30, "KNST" ));
        conferences.get(5).confTeams.add( new Team( "Ball St", "BAL", "MAC", this, 28, "NIU" ));
        conferences.get(5).confTeams.add( new Team( "Bowling Green", "BG", "MAC", this, 28, "TOL" ));
        conferences.get(5).confTeams.add( new Team( "Buffalo", "BUF", "MAC", this, 34, "EMU" ));
        conferences.get(5).confTeams.add( new Team( "Central Mich", "CMU", "MAC", this, 37, "WMU" ));
        conferences.get(5).confTeams.add( new Team( "Eastern Mich", "EMU", "MAC", this, 39, "BUF" ));
        conferences.get(5).confTeams.add( new Team( "Kent State", "KNST", "MAC", this, 32, "KNST" ));
        conferences.get(5).confTeams.add( new Team( "Miami OH", "MiOH", "MAC", this, 37, "OHIO" ));
        conferences.get(5).confTeams.add( new Team( "Northern Illinois", "NIU", "MAC", this, 45, "BAL" ));
        conferences.get(5).confTeams.add( new Team( "Ohio", "OHIO", "MAC", this, 45, "MiOH" ));
        conferences.get(5).confTeams.add( new Team( "Toledo", "TOL", "MAC", this, 39, "BG" ));
        conferences.get(5).confTeams.add( new Team( "Western Mich", "WMU", "MAC", this, 47, "CMU" ));
        
        //Mt West x 12
        conferences.get(6).confTeams.add( new Team( "Air Force", "AF", "Mt West", this, 42, "HAW" ));
        conferences.get(6).confTeams.add( new Team( "Boise State", "BOIS", "Mt West", this, 66, "SDSU" ));
        conferences.get(6).confTeams.add( new Team( "BYU", "BYU", "Mt West", this, 38, "UTST" ));
        conferences.get(6).confTeams.add( new Team( "Colorado St", "CSU", "Mt West", this, 47, "WYO" ));
        conferences.get(6).confTeams.add( new Team( "Fresno St", "FRES", "Mt West", this, 37, "SJSU" ));
        conferences.get(6).confTeams.add( new Team( "Hawaii", "HAW", "Mt West", this, 35, "AF" ));
        conferences.get(6).confTeams.add( new Team( "Nevada", "NEV", "Mt West", this, 35, "NMEX" ));
        conferences.get(6).confTeams.add( new Team( "New Mexico", "NMEX", "Mt West", this, 40, "NEV" ));
        conferences.get(6).confTeams.add( new Team( "San Diego State", "SDSU", "Mt West", this, 73, "BOIS" ));
        conferences.get(6).confTeams.add( new Team( "San Jose St", "SJSU", "Mt West", this, 31, "FRES" ));
        conferences.get(6).confTeams.add( new Team( "Utah State", "UTST", "Mt West", this, 43, "BYU" ));
        conferences.get(6).confTeams.add( new Team( "Wyoming", "WYOM", "Mt West", this, 49, "CSU" ));

        //Pac-12 - done x 12
        conferences.get(7).confTeams.add( new Team( "Arizona", "ARIZ", "Pac-12", this, 49, "ASU" ));
        conferences.get(7).confTeams.add( new Team( "Arizona State", "ASU", "Pac-12", this, 58, "ARIZ" ));
        conferences.get(7).confTeams.add( new Team( "California", "CAL", "Pac-12", this, 58, "STAN" ));
        conferences.get(7).confTeams.add( new Team( "Colorado", "COL", "Pac-12", this, 68, "UTAH" ));
        conferences.get(7).confTeams.add( new Team( "Oregon", "OREG", "Pac-12", this, 71, "ORST" ));
        conferences.get(7).confTeams.add( new Team( "Oregon State", "ORST", "Pac-12", this, 40, "OREG" ));
        conferences.get(7).confTeams.add( new Team( "Stanford", "STAN", "Pac-12", this, 75, "CAL" ));
        conferences.get(7).confTeams.add( new Team( "UCLA", "UCLA", "Pac-12", this, 67, "USC" ));
        conferences.get(7).confTeams.add( new Team( "USC", "USC", "Pac-12", this, 83, "UCLA" ));
        conferences.get(7).confTeams.add( new Team( "Washington", "WASH", "Pac-12", this, 83, "WSU" ));
        conferences.get(7).confTeams.add( new Team( "Wash State", "WSU", "Pac-12", this, 73, "WASH" ));
        conferences.get(7).confTeams.add( new Team( "Utah", "UTAH", "Pac-12", this, 71, "COL" ));

        //SEC - done x 12
        conferences.get(8).confTeams.add( new Team( "Alabama", "BAMA", "SEC", this, 95, "AUB" ));
        conferences.get(8).confTeams.add( new Team( "Arkansas", "ARK", "SEC", this, 50, "LSU" ));
        conferences.get(8).confTeams.add( new Team( "Auburn", "AUB", "SEC", this, 72, "BAMA" ));
        conferences.get(8).confTeams.add( new Team( "Florida", "FLOR", "SEC", this, 71, "UGA" ));
        conferences.get(8).confTeams.add( new Team( "Georgia", "UGA", "SEC", this, 82, "FLOR" ));
        conferences.get(8).confTeams.add( new Team( "Kentucky", "UK", "SEC", this, 59, "SC" ));
        conferences.get(8).confTeams.add( new Team( "Louisiana St", "LSU", "SEC", this, 74, "ARK" ));
        conferences.get(8).confTeams.add( new Team( "Ole Miss", "MISS", "SEC", this, 54, "MSST" ));
        conferences.get(8).confTeams.add( new Team( "Mississippi St", "MSST", "SEC", this, 70, "MISS" ));
        conferences.get(8).confTeams.add( new Team( "South Carolina", "SC", "SEC", this, 50, "UK" ));
        conferences.get(8).confTeams.add( new Team( "Tennessee", "TENN", "SEC", this, 70, "VAND" ));
        conferences.get(8).confTeams.add( new Team( "Vanderbilt", "VAND", "SEC", this, 45, "TENN" ));

        //Sun Belt
        conferences.get(9).confTeams.add( new Team( "Appalachian State", "APP", "Sun Belt", this, 44, "ODOM" ));
        conferences.get(9).confTeams.add( new Team( "LA Monroe", "LMON", "Sun Belt", this, 32, "TROY" ));
        conferences.get(9).confTeams.add( new Team( "Mid Tenn State", "MTSU", "Sun Belt", this, 39, "GASO" ));
        conferences.get(9).confTeams.add( new Team( "Troy", "TROY", "Sun Belt", this, 45, "LMON" ));
        conferences.get(9).confTeams.add( new Team( "TX El Paso", "UTEP", "Sun Belt", this, 30, "UTSA" ));
        conferences.get(9).confTeams.add( new Team( "North Texas", "NTEX", "Sun Belt", this, 35, "TXST" ));
        conferences.get(9).confTeams.add( new Team( "Tulsa", "TULS", "Sun Belt", this, 40, "RICE" ));
        conferences.get(9).confTeams.add( new Team( "Rice", "RICE", "Sun Belt", this, 33, "TULS" ));
        conferences.get(9).confTeams.add( new Team( "Texas St", "TXST", "Sun Belt", this, 30, "NTEX" ));
        conferences.get(9).confTeams.add( new Team( "Georgia Southern", "GASO", "Sun Belt", this, 32, "MTSU" ));
        conferences.get(9).confTeams.add( new Team( "Old Dominion", "ODOM", "Sun Belt", this, 35, "APP" ));
        conferences.get(9).confTeams.add( new Team( "TX San Antonio", "UTSA", "Sun Belt", this, 45, "UTEP" ));


        //set teamList
        teamList = new ArrayList<Team>();
        for (int i = 0; i < conferences.size(); ++i ) {
            for (int j = 0; j < conferences.get(i).confTeams.size(); ++j ) {
                teamList.add( conferences.get(i).confTeams.get(j) );
            }
        }

        //set up schedule
        Random rand = new Random();
        int max = 8;
        int min = 5;
        //randgm = (int)Math.random()*((max - min)+1) + min;
        randgm = rand.nextInt((max - min)+1) + min;
        int maxc = 1;
        int minc = 0;
        randconf = rand.nextInt((maxc - minc)+1) + minc;

        for (int i = 0; i < conferences.size(); ++i ) {
            conferences.get(i).setUpSchedule();
        }
        for (int i = 0; i < conferences.size(); ++i ) {
            conferences.get(i).setUpOOCSchedule();
        }
        for (int i = 0; i < conferences.size(); ++i ) {
            conferences.get(i).insertOOCSchedule();
        }
    }

    /* LOAD A SAVE FILE
     * Create League from saved file.
     * @param saveFile file that league is saved in
     */
    public League(File saveFile, String namesCSV, String lastNamesCSV) {
        heismanDecided = false;
        hasScheduledBowls = false;
        blessDevelopingStory = false;
        curseDevelopingStory = false;
        bowlGames = new Game[countBG];
        // This will reference one line at a time
        String line = null;
        currentWeek = 0;

        leagueRecords = new LeagueRecords();
        userTeamRecords = new LeagueRecords();
        longestWinStreak = new TeamStreak(seasonStart, seasonStart, 0, "XXX");
        yearStartLongestWinStreak = new TeamStreak(seasonStart, seasonStart, 0, "XXX");
        longestActiveWinStreak = new TeamStreak(seasonStart, seasonStart, 0, "XXX");

        try {
            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader( new FileReader(saveFile) );

            //First ignore the save file info
            line = bufferedReader.readLine();

            //Next get league history
            leagueHistory = new ArrayList<String[]>();
            while((line = bufferedReader.readLine()) != null && !line.equals("END_LEAGUE_HIST")) {
                leagueHistory.add(line.split("%"));
            }

            //Next get heismans
            heismanHistory = new ArrayList<String>();
            while((line = bufferedReader.readLine()) != null && !line.equals("END_HEISMAN_HIST")) {
                heismanHistory.add(line);
            }

            //Next make all the conferences & teams
            conferences = new ArrayList<Conference>();
            teamList = new ArrayList<Team>();

            while((line = bufferedReader.readLine()) != null && !line.equals("END_CONFERENCES")) {
                conferences.add(new Conference(line, this));
            }

            allAmericans = new ArrayList<Player>();
            String[] splits;
            for(int i = 0; i < countTeam; ++i) { //Do for every team
                StringBuilder sbTeam = new StringBuilder();
                while((line = bufferedReader.readLine()) != null && !line.equals("END_PLAYERS")) {
                    sbTeam.append(line);
                }
                Team t = new Team(sbTeam.toString(), this);
                conferences.get( getConfNumber(t.conference) ).confTeams.add(t);
                teamList.add(t);
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
            while((line = bufferedReader.readLine()) != null && !line.equals("END_USER_TEAM")) {
                userTeam.teamHistory.add(line);
            }

            // Set up blessed and cursed teams for Week 0 news stories
            StringBuilder sbBless = new StringBuilder();
            while((line = bufferedReader.readLine()) != null && !line.equals("END_BLESS_TEAM")) {
                sbBless.append(line);
            }
            if (!sbBless.toString().equals("NULL")) {
                saveBless = findTeamAbbr(sbBless.toString());
                saveBless.sortPlayers();
                findTeamAbbr(saveBless.rivalTeam).sortPlayers();
            } else {saveBless = null;
            }

            StringBuilder sbBless2 = new StringBuilder();
            while((line = bufferedReader.readLine()) != null && !line.equals("END_BLESS2_TEAM")) {
                sbBless2.append(line);
            }
            if (!sbBless2.toString().equals("NULL")) {
                saveBless2 = findTeamAbbr(sbBless2.toString());
                saveBless2.sortPlayers();
                findTeamAbbr(saveBless2.rivalTeam).sortPlayers();
            } else {saveBless2 = null;
            }

            StringBuilder sbBless3 = new StringBuilder();
            while((line = bufferedReader.readLine()) != null && !line.equals("END_BLESS3_TEAM")) {
                sbBless3.append(line);
            }
            if (!sbBless3.toString().equals("NULL")) {
                saveBless3 = findTeamAbbr(sbBless3.toString());
                saveBless3.sortPlayers();
                findTeamAbbr(saveBless3.rivalTeam).sortPlayers();
            } else {saveBless3 = null;
            }

            StringBuilder sbBless4 = new StringBuilder();
            while((line = bufferedReader.readLine()) != null && !line.equals("END_BLESS4_TEAM")) {
                sbBless4.append(line);
            }
            if (!sbBless4.toString().equals("NULL")) {
                saveBless4 = findTeamAbbr(sbBless4.toString());
                saveBless4.sortPlayers();
                findTeamAbbr(saveBless4.rivalTeam).sortPlayers();
            } else {saveBless4 = null;
            }

            StringBuilder sbBless5 = new StringBuilder();
            while((line = bufferedReader.readLine()) != null && !line.equals("END_BLESS5_TEAM")) {
                sbBless5.append(line);
            }
            if (!sbBless5.toString().equals("NULL")) {
                saveBless5 = findTeamAbbr(sbBless5.toString());
                saveBless5.sortPlayers();
                findTeamAbbr(saveBless5.rivalTeam).sortPlayers();
            } else {saveBless5 = null;
            }



            StringBuilder sbCurse = new StringBuilder();
            while((line = bufferedReader.readLine()) != null && !line.equals("END_CURSE_TEAM")) {
                sbCurse.append(line);
            }
            if (!sbCurse.toString().equals("NULL")) {
                saveCurse = findTeamAbbr(sbCurse.toString());
                saveCurse.sortPlayers();
                findTeamAbbr(saveCurse.rivalTeam).sortPlayers();
            } else {saveCurse = null;}

            StringBuilder sbCurse2 = new StringBuilder();
            while((line = bufferedReader.readLine()) != null && !line.equals("END_CURSE2_TEAM")) {
                sbCurse2.append(line);
            }
            if (!sbCurse2.toString().equals("NULL")) {
                saveCurse2 = findTeamAbbr(sbCurse2.toString());
                saveCurse2.sortPlayers();
                findTeamAbbr(saveCurse2.rivalTeam).sortPlayers();
            } else {saveCurse2 = null;}

            StringBuilder sbCurse3 = new StringBuilder();
            while((line = bufferedReader.readLine()) != null && !line.equals("END_CURSE3_TEAM")) {
                sbCurse3.append(line);
            }
            if (!sbCurse3.toString().equals("NULL")) {
                saveCurse3 = findTeamAbbr(sbCurse3.toString());
                saveCurse3.sortPlayers();
                findTeamAbbr(saveCurse3.rivalTeam).sortPlayers();
            } else {saveCurse3 = null;}

            StringBuilder sbCurse4 = new StringBuilder();
            while((line = bufferedReader.readLine()) != null && !line.equals("END_CURSE4_TEAM")) {
                sbCurse4.append(line);
            }
            if (!sbCurse4.toString().equals("NULL")) {
                saveCurse4 = findTeamAbbr(sbCurse4.toString());
                saveCurse4.sortPlayers();
                findTeamAbbr(saveCurse4.rivalTeam).sortPlayers();
            } else {saveCurse4 = null;}

            StringBuilder sbCurse5 = new StringBuilder();
            while((line = bufferedReader.readLine()) != null && !line.equals("END_CURSE5_TEAM")) {
                sbCurse5.append(line);
            }
            if (!sbCurse5.toString().equals("NULL")) {
                saveCurse5 = findTeamAbbr(sbCurse5.toString());
                saveCurse5.sortPlayers();
                findTeamAbbr(saveCurse5.rivalTeam).sortPlayers();
            } else {saveCurse5 = null;}

            String[] record;
            while((line = bufferedReader.readLine()) != null && !line.equals("END_LEAGUE_RECORDS")) {
                record = line.split(",");
                if (!record[1].equals("-1"))
                    leagueRecords.checkRecord(record[0], Integer.parseInt(record[1]), record[2], Integer.parseInt(record[3]));
            }

            while((line = bufferedReader.readLine()) != null && !line.equals("END_LEAGUE_WIN_STREAK")) {
                record = line.split(",");
                longestWinStreak = new TeamStreak(
                        Integer.parseInt(record[2]), Integer.parseInt(record[3]), Integer.parseInt(record[0]), record[1]);
                yearStartLongestWinStreak = new TeamStreak(
                        Integer.parseInt(record[2]), Integer.parseInt(record[3]), Integer.parseInt(record[0]), record[1]);
            }

            while((line = bufferedReader.readLine()) != null && !line.equals("END_USER_TEAM_RECORDS")) {
                record = line.split(",");
                if (!record[1].equals("-1"))
                    userTeamRecords.checkRecord(record[0], Integer.parseInt(record[1]), record[2], Integer.parseInt(record[3]));
            }

            while((line = bufferedReader.readLine()) != null && !line.equals("END_HALL_OF_FAME")) {
                userTeam.hallOfFame.add(line);
            }

            // Always close files.
            bufferedReader.close();


            // Read first names from file
            nameList = new ArrayList<String>();
            String[] namesSplit = namesCSV.split(",");
            for (String n : namesSplit) {
                nameList.add(n.trim());
            }

            // Read last names from file
            lastNameList = new ArrayList<String>();
            namesSplit = lastNamesCSV.split(",");
            for (String n : namesSplit) {
                lastNameList.add(n.trim());
            }

            //Get longest active win streak
            updateLongestActiveWinStreak();

            //set up schedule
           for (int i = 0; i < conferences.size(); ++i ) {
                conferences.get(i).setUpSchedule();
            }

            Random rand = new Random();
            int max = 8;
            int min = 5;
            //randgm = (int)Math.random()*((max - min)+1) + min;
            randgm = rand.nextInt((max - min)+1) + min;
            int maxc = 1;
            int minc = 0;
            randconf = rand.nextInt((maxc - minc)+1) + minc;

            for (int i = 0; i < conferences.size(); ++i ) {
                conferences.get(i).setUpOOCSchedule();
            }
            for (int i = 0; i < conferences.size(); ++i ) {
                conferences.get(i).insertOOCSchedule();
            }

            // Initialize new stories lists
            newsStories = new ArrayList< ArrayList<String> >();
            for (int i = 0; i < 16; ++i) {
                newsStories.add(new ArrayList<String>());
            }
            newsStories.get(0).add("New Season!>Ready for the new season, coach? Whether the National Championship is " +
                    "on your mind, or just a winning season, good luck!");


            if (saveBless != null) {
                newsStories.get(0).add("Off-Season Coaching Hires:>New Coaching hires at the following schools will add new look and will hopefully bring more prestige to the university: \n" + saveBless.name + ": " + getRandName() + "\n" + saveBless2.name + ": " + getRandName() + "\n" + saveBless3.name + ": " + getRandName() + "\n" + saveBless4.name + ": " + getRandName() + "\n" + saveBless5.name + ": " + getRandName() + "\n");
            }
            if (saveCurse != null) {
                newsStories.get(0).add("Major Infraction: " + saveCurse.name + ">An administrative probe has determined that booster " + getRandName() + " has tampered with several recruits. In addition, academic records at  " + saveCurse.name + " have been suspect over the past couple years. The will ineligible for bowl games or the playoffs this season. The team prestige has dropped and recruiting will be more challenging.");
            }
            if (saveCurse2 != null) {
                newsStories.get(0).add("Minor Infraction: " + saveCurse2.name + ">Investigations have led to the discovery that " + saveCurse2.name + "'s head coach " + getRandName() + " was found violating recruiting policies over the past off-season. The team prestige has dropped.");
            }
            if (saveCurse3 != null) {
                newsStories.get(0).add("Incidental Infraction: " + saveCurse3.name + ">Several players from " + saveCurse3.name + " were arrested in non-football related activities this past off-season. The team prestige has dropped.");
            }
            if (saveCurse4 != null) {
                newsStories.get(0).add("Incidental Infraction: " + saveCurse4.name + ">An independent investigation determined " + saveCurse4.name + " assistant " + getRandName() + " violated recruiting regulations during the off-season. The team prestige has dropped.");
            }
            if (saveCurse5 != null) {
                newsStories.get(0).add("Incidental Infraction: " + saveCurse5.name + ">Newspapers are reporting " + saveCurse5.name + "'s head recruiter " + getRandName() + " contacted several recruits during a recruiting dead period. The team prestige has dropped.");
            }


        }
        catch(FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file");
        }
        catch(IOException ex) {
            System.out.println(
                    "Error reading file");
        }
    }

    /**
     * Get conference number from string
     * @param conf conference name
     * @return int of number 0-5
     */
    public int getConfNumber(String conf) {
        if (conf.equals(conferences.get(0).confName)) return 0;
        if (conf.equals(conferences.get(1).confName)) return 1;
        if (conf.equals(conferences.get(2).confName)) return 2;
        if (conf.equals(conferences.get(3).confName)) return 3;
        if (conf.equals(conferences.get(4).confName)) return 4;
        if (conf.equals(conferences.get(5).confName)) return 5;
        if (conf.equals(conferences.get(6).confName)) return 6;
        if (conf.equals(conferences.get(7).confName)) return 7;
        if (conf.equals(conferences.get(8).confName)) return 8;
        if (conf.equals(conferences.get(9).confName)) return 9;
        return 0;
    }

    /**
     * Plays week. If normal week, handled by conferences. If bowl week, handled here.
     */
    public void playWeek() {
        if ( currentWeek <= 12 ) {
            for (int i = 0; i < conferences.size(); ++i) {
                conferences.get(i).playWeek();
                //
                //GET WEEKLY SCOREBOARD - WIP
                //weekScores.add(i, weeklyScores.getResults());
                //idea - create a csv to collect weekly scores and then display in the main activity somewhere.
            }
        }

        if ( currentWeek == 12 ) {
            //bowl week
            for (int i = 0; i < teamList.size(); ++i) {
                teamList.get(i).updatePollScore();
            }
            Collections.sort( teamList, new TeamCompPoll() );

            schedBowlGames();
        } else if ( currentWeek == 13 ) {
            ArrayList<Player> heismans = getHeisman();
            heismanHistory.add(heismans.get(0).position + " " + heismans.get(0).getInitialName() + " [" + heismans.get(0).getYrStr() + "], "
                    + heismans.get(0).team.abbr + " (" + heismans.get(0).team.wins + "-" + heismans.get(0).team.losses + ")");
            playBowlGames();
        } else if ( currentWeek == 14 ) {
            ncg.playGame();
            if ( ncg.homeScore > ncg.awayScore ) {
                ncg.homeTeam.semiFinalWL = "";
                ncg.awayTeam.semiFinalWL = "";
                ncg.homeTeam.natChampWL = "NCW";
                ncg.awayTeam.natChampWL = "NCL";
                ncg.homeTeam.totalNCs++;
                ncg.awayTeam.totalNCLosses++;
                newsStories.get(15).add(
                        ncg.homeTeam.name + " wins the National Championship!>" +
                                ncg.homeTeam.strRep() + " defeats " + ncg.awayTeam.strRep() +
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
                newsStories.get(15).add(
                        ncg.awayTeam.name + " wins the National Championship!>" +
                                ncg.awayTeam.strRep() + " defeats " + ncg.homeTeam.strRep() +
                                " in the national championship game " + ncg.awayScore + " to " + ncg.homeScore + "." +
                                " Congratulations " + ncg.awayTeam.name + "!"
                );
            }
        }

        setTeamRanks();
        updateLongestActiveWinStreak();
        currentWeek++;
    }

    /**
     * Schedules bowl games based on team rankings.
     */
    public void schedBowlGames() {
        //bowl week
        for (int i = 0; i < teamList.size(); ++i) {
            teamList.get(i).updatePollScore();
            if (saveCurse != null && teamList.get(i).abbr == saveCurse.abbr) {
                teamList.get(i).teamPollScore = 0;
            }
        }
        Collections.sort( teamList, new TeamCompPoll() );

        //semifinals
        semiG14 = new Game( teamList.get(0), teamList.get(3), "Semis, 1v4" );
        teamList.get(0).gameSchedule.add(semiG14);
        teamList.get(3).gameSchedule.add(semiG14);

        semiG23 = new Game( teamList.get(1), teamList.get(2), "Semis, 2v3" );
        teamList.get(1).gameSchedule.add(semiG23);
        teamList.get(2).gameSchedule.add(semiG23);

        //other bowl games

        bowlGames[0] = new Game( teamList.get(4), teamList.get(6), bowlNames[0] );
        teamList.get(4).gameSchedule.add(bowlGames[0]);
        teamList.get(6).gameSchedule.add(bowlGames[0]);

        bowlGames[1] = new Game( teamList.get(5), teamList.get(7), bowlNames[1] );
        teamList.get(5).gameSchedule.add(bowlGames[1]);
        teamList.get(7).gameSchedule.add(bowlGames[1]);

        bowlGames[2] = new Game( teamList.get(8), teamList.get(14), bowlNames[2] );
        teamList.get(8).gameSchedule.add(bowlGames[2]);
        teamList.get(14).gameSchedule.add(bowlGames[2]);

        bowlGames[3] = new Game( teamList.get(9), teamList.get(15), bowlNames[3] );
        teamList.get(9).gameSchedule.add(bowlGames[3]);
        teamList.get(15).gameSchedule.add(bowlGames[3]);

        bowlGames[4] = new Game( teamList.get(10), teamList.get(11), bowlNames[4] );
        teamList.get(10).gameSchedule.add(bowlGames[4]);
        teamList.get(11).gameSchedule.add(bowlGames[4]);

        bowlGames[5] = new Game( teamList.get(12), teamList.get(13), bowlNames[5] );
        teamList.get(12).gameSchedule.add(bowlGames[5]);
        teamList.get(13).gameSchedule.add(bowlGames[5]);

        bowlGames[6] = new Game( teamList.get(16), teamList.get(20), bowlNames[6] );
        teamList.get(16).gameSchedule.add(bowlGames[6]);
        teamList.get(20).gameSchedule.add(bowlGames[6]);

        bowlGames[7] = new Game( teamList.get(17), teamList.get(21), bowlNames[7] );
        teamList.get(17).gameSchedule.add(bowlGames[7]);
        teamList.get(21).gameSchedule.add(bowlGames[7]);

        bowlGames[8] = new Game( teamList.get(18), teamList.get(22), bowlNames[8] );
        teamList.get(18).gameSchedule.add(bowlGames[8]);
        teamList.get(22).gameSchedule.add(bowlGames[8]);

        bowlGames[9] = new Game( teamList.get(19), teamList.get(23), bowlNames[9] );
        teamList.get(19).gameSchedule.add(bowlGames[9]);
        teamList.get(23).gameSchedule.add(bowlGames[9]);

        bowlGames[10] = new Game( teamList.get(24), teamList.get(28), bowlNames[10] );
        teamList.get(24).gameSchedule.add(bowlGames[10]);
        teamList.get(28).gameSchedule.add(bowlGames[10]);

        bowlGames[11] = new Game( teamList.get(25), teamList.get(30), bowlNames[11] );
        teamList.get(25).gameSchedule.add(bowlGames[11]);
        teamList.get(30).gameSchedule.add(bowlGames[11]);

        bowlGames[12] = new Game( teamList.get(26), teamList.get(29), bowlNames[12] );
        teamList.get(26).gameSchedule.add(bowlGames[12]);
        teamList.get(29).gameSchedule.add(bowlGames[12]);

        bowlGames[13] = new Game( teamList.get(27), teamList.get(31), bowlNames[13] );
        teamList.get(27).gameSchedule.add(bowlGames[13]);
        teamList.get(31).gameSchedule.add(bowlGames[13]);
        hasScheduledBowls = true;
    }

    /**
     * Actually plays each bowl game.
     */

    public void playBowlGames() {
        for (Game g : bowlGames) {
            playBowl(g);
        }

        semiG14.playGame();
        semiG23.playGame();
        Team semi14winner;
        Team semi23winner;
        if ( semiG14.homeScore > semiG14.awayScore ) {
            semiG14.homeTeam.semiFinalWL = "SFW";
            semiG14.awayTeam.semiFinalWL = "SFL";
            semiG14.awayTeam.totalBowlLosses++;
            semiG14.homeTeam.totalBowls++;
            semi14winner = semiG14.homeTeam;
            newsStories.get(14).add(
                    semiG14.homeTeam.name + " wins the " + semiG14.gameName +"!>" +
                            semiG14.homeTeam.strRep() + " defeats " + semiG14.awayTeam.strRep() +
                            " in the semifinals, winning " + semiG14.homeScore + " to " + semiG14.awayScore + ". " +
                            semiG14.homeTeam.name + " advances to the National Championship!"

            );
        } else {
            semiG14.homeTeam.semiFinalWL = "SFL";
            semiG14.awayTeam.semiFinalWL = "SFW";
            semiG14.homeTeam.totalBowlLosses++;
            semiG14.awayTeam.totalBowls++;
            semi14winner = semiG14.awayTeam;
            newsStories.get(14).add(
                    semiG14.awayTeam.name + " wins the " + semiG14.gameName +"!>" +
                            semiG14.awayTeam.strRep() + " defeats " + semiG14.homeTeam.strRep() +
                            " in the semifinals, winning " + semiG14.awayScore + " to " + semiG14.homeScore + ". " +
                            semiG14.awayTeam.name + " advances to the National Championship!"

            );
        }
        if ( semiG23.homeScore > semiG23.awayScore ) {
            semiG23.homeTeam.semiFinalWL = "SFW";
            semiG23.awayTeam.semiFinalWL = "SFL";
            semiG23.homeTeam.totalBowls++;
            semiG23.awayTeam.totalBowlLosses++;
            semi23winner = semiG23.homeTeam;
            newsStories.get(14).add(
                    semiG23.homeTeam.name + " wins the " + semiG23.gameName +"!>" +
                            semiG23.homeTeam.strRep() + " defeats " + semiG23.awayTeam.strRep() +
                            " in the semifinals, winning " + semiG23.homeScore + " to " + semiG23.awayScore + ". " +
                            semiG23.homeTeam.name + " advances to the National Championship!"

            );
        } else {
            semiG23.homeTeam.semiFinalWL = "SFL";
            semiG23.awayTeam.semiFinalWL = "SFW";
            semiG23.awayTeam.totalBowls++;
            semiG23.homeTeam.totalBowlLosses++;
            semi23winner = semiG23.awayTeam;
            newsStories.get(14).add(
                    semiG23.awayTeam.name + " wins the " + semiG23.gameName +"!>" +
                            semiG23.awayTeam.strRep() + " defeats " + semiG23.homeTeam.strRep() +
                            " in the semifinals, winning " + semiG23.awayScore + " to " + semiG23.homeScore + ". " +
                            semiG23.awayTeam.name + " advances to the National Championship!"

            );
        }

        //schedule NCG
        ncg = new Game( semi14winner, semi23winner, "NCG" );
        semi14winner.gameSchedule.add( ncg );
        semi23winner.gameSchedule.add(ncg);

    }

    /**
     * Plays a particular bowl game
     * @param g bowl game to be played
     */
    private void playBowl(Game g) {
        g.playGame();
        if ( g.homeScore > g.awayScore ) {
            g.homeTeam.semiFinalWL = "BW";
            g.awayTeam.semiFinalWL = "BL";
            g.homeTeam.totalBowls++;
            g.awayTeam.totalBowlLosses++;
            newsStories.get(14).add(
                    g.homeTeam.name + " wins the " + g.gameName +"!>" +
                            g.homeTeam.strRep() + " defeats " + g.awayTeam.strRep() +
                            " in the " + g.gameName + ", winning " + g.homeScore + " to " + g.awayScore + "."
            );
        } else {
            g.homeTeam.semiFinalWL = "BL";
            g.awayTeam.semiFinalWL = "BW";
            g.homeTeam.totalBowlLosses++;
            g.awayTeam.totalBowls++;
            newsStories.get(14).add(
                    g.awayTeam.name + " wins the " + g.gameName +"!>" +
                            g.awayTeam.strRep() + " defeats " + g.homeTeam.strRep() +
                            " in the " + g.gameName + ", winning " + g.awayScore + " to " + g.homeScore + "."
            );
        }
    }

    /**
     * At the end of the year, record the top 10 teams for the League's History.
     */
    public void updateLeagueHistory() {
        //update league history
        Collections.sort( teamList, new TeamCompPoll() );
        String[] yearTop25 = new String[25];
        Team tt;
        for (int i = 0; i < 25; ++i) {
            tt = teamList.get(i);
            yearTop25[i] = tt.abbr + " (" + tt.wins + "-" + tt.losses + ")";
        }
        leagueHistory.add(yearTop25);
    }


    //Advances season for each team and sets up schedules for the new year.

    public void advanceSeason() {
        currentWeek = 0;
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).advanceSeason();
        }


        //COACH HIRES & INFRACTIONS

        // Hire Coach for avg team
        int blessNumber = (int)(Math.random()*21);
        Team blessTeam = teamList.get(35 + blessNumber);
        int coach = (int)(Math.random()*17);
        blessTeam.teamPrestige += coach + 4;
        saveBless = blessTeam;
        if (blessTeam.teamPrestige > 99) {blessTeam.teamPrestige = 99;}

        // Hire Coach for medicore team
        blessNumber = (int)(Math.random()*21);
        Team blessTeam2 = teamList.get(55 + blessNumber);
        coach = (int)(Math.random()*19);
        blessTeam2.teamPrestige += coach + 4;
        saveBless2 = blessTeam2;
        if (blessTeam2.teamPrestige > 99) {blessTeam2.teamPrestige = 99;}

        // Hire Coach for very bad team
        blessNumber = (int)(Math.random()*25);
        if (blessNumber > 20) blessNumber = 20;
        Team blessTeam3 = teamList.get(75 + blessNumber);
        coach = (int)(Math.random()*20);
        blessTeam3.teamPrestige += coach + 4;
        saveBless3 = blessTeam3;
        if (blessTeam3.teamPrestige > 99) {blessTeam3.teamPrestige = 99;}

        // Hire Coach for very bad team
        blessNumber = (int)(Math.random()*12);
        if (blessNumber > 10) blessNumber = 10;
        Team blessTeam4 = teamList.get(99 + blessNumber);
        coach = (int)(Math.random()*21);
        blessTeam4.teamPrestige += coach + 4;
        saveBless4 = blessTeam4;
        if (blessTeam4.teamPrestige > 99) {blessTeam4.teamPrestige = 99;}

        // Hire Coach for very bad team
        blessNumber = (int)(Math.random()*12);
        if (blessNumber > 10) blessNumber = 10;
        Team blessTeam5 = teamList.get(109 + blessNumber);
        coach = (int)(Math.random()*23);
        blessTeam5.teamPrestige += coach + 4;
        saveBless5 = blessTeam5;
        if (blessTeam5.teamPrestige > 99) {blessTeam5.teamPrestige = 99;}

        /// INFRACTIONS TIME

        //Major Infraction to a good team
        int x = (int)(Math.random()*11);
        if (x > 7 ){
            int curseNumber = (int)(Math.random()*25);
            Team curseTeam = teamList.get(curseNumber);
            if (curseTeam.teamPrestige > 70) {
                curseTeam.teamPrestige -= (25 + x);
                saveCurse = curseTeam;
            }  else saveCurse = null;
        } else saveCurse = null;

        //Minor infraction to an avg team
        x = (int)(Math.random()*11);
        if (x > 6 ) {
            int curseNumber = (int) (Math.random()*45);
            Team curseTeam2 = teamList.get(curseNumber);
                curseTeam2.teamPrestige -= (10 + x);
                saveCurse2 = curseTeam2;
        } else saveCurse2 = null;

        //Discipline a team
        x = (int)(Math.random()*11);
        if (x > 6 ) {
            int curseNumber = (int) (Math.random()*55);
            Team curseTeam3 = teamList.get(curseNumber);
            curseTeam3.teamPrestige -= (6 + x);
            saveCurse3 = curseTeam3;
        } else saveCurse3 = null;

        //Discipline a team
        x = (int)(Math.random()*11);
        if (x > 5 ) {
            int curseNumber = (int) (Math.random()*75);
            Team curseTeam4 = teamList.get(curseNumber);
            curseTeam4.teamPrestige -= (3 + x);
            saveCurse4 = curseTeam4;
        } else saveCurse4 = null;

        //Discipline a team
        x = (int)(Math.random()*11);
        if (x > 5 ) {
            int curseNumber = (int) (Math.random()*85);
            Team curseTeam5 = teamList.get(curseNumber);
            curseTeam5.teamPrestige -= (5);
            saveCurse5 = curseTeam5;
        } else saveCurse5 = null;

        //FINISH INFRACTIONS


        advanceSeasonWinStreaks();

        for (int c = 0; c < conferences.size(); ++c) {
            conferences.get(c).robinWeek = 0;
            conferences.get(c).week = 0;
        }

        hasScheduledBowls = false;
    }

    /**
     * Check the longest win streak. If the given streak is longer, replace.
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
    public void updateLongestActiveWinStreak() {
        for (Team t : teamList) {
            if (t.winStreak.getStreakLength() > longestActiveWinStreak.getStreakLength()) {
                longestActiveWinStreak = t.winStreak;
            }
        }
    }

    /**
     * Advance season for win streaks, so no save-load whackiness.
     */
    public void advanceSeasonWinStreaks() {
        yearStartLongestWinStreak = longestWinStreak;
        for (Team t : teamList) {
            t.yearStartWinStreak = t.winStreak;
        }
    }

    /**
     * Change the team abbr of the lognest win streak if the user changed it
     * @param oldAbbr old abbreviation
     * @param newAbbr new abbreviation
     */
    public void changeAbbrWinStreaks(String oldAbbr, String newAbbr) {
        if (longestWinStreak.getTeam().equals(oldAbbr)) {
            longestWinStreak.changeAbbr(newAbbr);
        }
        if (yearStartLongestWinStreak.getTeam().equals(oldAbbr)) {
            yearStartLongestWinStreak.changeAbbr(newAbbr);
        }
    }

    /**
     * Changes all the abbrs to new abbr, in records and histories.
     * @param oldAbbr
     * @param newAbbr
     */
    public void changeAbbrHistoryRecords(String oldAbbr, String newAbbr) {
        // check records and win streaks
        leagueRecords.changeAbbrRecords(userTeam.abbr, newAbbr);
        userTeamRecords.changeAbbrRecords(userTeam.abbr, newAbbr);
        changeAbbrWinStreaks(userTeam.abbr, newAbbr);
        userTeam.winStreak.changeAbbr(newAbbr);
        userTeam.yearStartWinStreak.changeAbbr(newAbbr);

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
        }
        userTeam.checkLeagueRecords(userTeamRecords);
    }
    /**
     * Gets all the league records, including the longest win streak
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
     * Gets the current year, starting from 2017
     * @return the current year
     */
    public int getYear() {
        return seasonStart + leagueHistory.size();
    }

    /**
     * Gets rid of all injuries
     */
    public void curePlayers() {
        for (Team t : teamList) {
            t.curePlayers();
        }
    }

    /**
     * Updates team history for each team.
     */
    public void updateTeamHistories() {
        for ( int i = 0; i < teamList.size(); ++i ) {
            teamList.get(i).updateTeamHistory();
        }
    }

    /**
     * Update all teams off talent, def talent, etc
     */
    public void updateTeamTalentRatings() {
        for (Team t : teamList) {
            t.updateTalentRatings();
        }
    }

    /**
     * Gets a random player name.
     * @return random name
     */
    public String getRandName() {
        int fn = (int) (Math.random() * nameList.size());
        int ln = (int) (Math.random() * lastNameList.size());
        return nameList.get(fn) + " " + lastNameList.get(ln);
    }

    /**
     * Updates poll scores for each team and updates their ranking.
     */
    public void setTeamRanks() {
        //get team ranks for PPG, YPG, etc
        for (int i = 0; i < teamList.size(); ++i) {
            teamList.get(i).updatePollScore();
        }

        Collections.sort( teamList, new TeamCompPoll() );
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).rankTeamPollScore = t+1;
        }

        for (int i = 0; i < teamList.size(); ++i) {
            teamList.get(i).updateSOS();
        }

        Collections.sort( teamList, new TeamCompSoS() );
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).rankTeamSOS = t+1;
        }

        Collections.sort( teamList, new TeamCompSoW() );
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).rankTeamStrengthOfWins = t+1;
        }

        Collections.sort( teamList, new TeamCompPPG() );
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).rankTeamPoints = t+1;
        }

        Collections.sort( teamList, new TeamCompOPPG() );
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).rankTeamOppPoints = t+1;
        }

        Collections.sort( teamList, new TeamCompYPG() );
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).rankTeamYards = t+1;
        }

        Collections.sort( teamList, new TeamCompOYPG() );
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).rankTeamOppYards = t+1;
        }

        Collections.sort( teamList, new TeamCompPYPG() );
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).rankTeamPassYards = t+1;
        }

        Collections.sort( teamList, new TeamCompRYPG() );
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).rankTeamRushYards = t+1;
        }

        Collections.sort( teamList, new TeamCompOPYPG() );
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).rankTeamOppPassYards = t+1;
        }

        Collections.sort( teamList, new TeamCompORYPG() );
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).rankTeamOppRushYards = t+1;
        }

        Collections.sort( teamList, new TeamCompTODiff() );
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).rankTeamTODiff= t+1;
        }

        Collections.sort( teamList, new TeamCompOffTalent() );
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).rankTeamOffTalent = t+1;
        }

        Collections.sort( teamList, new TeamCompDefTalent() );
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).rankTeamDefTalent = t+1;
        }

        Collections.sort( teamList, new TeamCompPrestige() );
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).rankTeamPrestige = t+1;
        }

        if (currentWeek == 0) {
            Collections.sort(teamList, new TeamCompRecruitClass());
            for (int t = 0; t < teamList.size(); ++t) {
                teamList.get(t).rankTeamRecruitClass = t + 1;
            }
        }

    }

    /**
     * Calculates who wins the Heisman.
     * @return Heisman Winner
     */
    public ArrayList<Player> getHeisman() {
        heisman = null;
        int heismanScore = 0;
        int tempScore = 0;
        ArrayList<Player> heismanCandidates = new ArrayList<Player>();
        for ( int i = 0; i < teamList.size(); ++i ) {
            //qb
            for (int qb = 0; qb < teamList.get(i).teamQBs.size(); ++qb) {
                heismanCandidates.add(teamList.get(i).teamQBs.get(qb));
                tempScore = teamList.get(i).teamQBs.get(qb).getHeismanScore() + teamList.get(i).wins * 100;
                if (tempScore > heismanScore) {
                    heisman = teamList.get(i).teamQBs.get(qb);
                    heismanScore = tempScore;
                }
            }

            //rb
            for (int rb = 0; rb < teamList.get(i).teamRBs.size(); ++rb) {
                heismanCandidates.add( teamList.get(i).teamRBs.get(rb) );
                tempScore = teamList.get(i).teamRBs.get(rb).getHeismanScore() + teamList.get(i).wins*100;
                if ( tempScore > heismanScore ) {
                    heisman = teamList.get(i).teamRBs.get(rb);
                    heismanScore = tempScore;
                }
            }

            //wr
            for (int wr = 0; wr < teamList.get(i).teamWRs.size(); ++wr) {
                heismanCandidates.add( teamList.get(i).teamWRs.get(wr) );
                tempScore = teamList.get(i).teamWRs.get(wr).getHeismanScore() + teamList.get(i).wins*100;
                if ( tempScore > heismanScore ) {
                    heisman = teamList.get(i).teamWRs.get(wr);
                    heismanScore = tempScore;
                }
            }
        }
        Collections.sort( heismanCandidates, new PlayerHeismanComp() );

        return heismanCandidates;
    }

    /**
     * Get string of the top 5 heisman candidates. If the heisman is already decided, get the ceremony str.
     * @return string of top 5 players and their stats
     */
    public String getTop5HeismanStr() {
        if (heismanDecided) {
            return getHeismanCeremonyStr();
        } else {
            ArrayList<Player> heismanCandidates = getHeisman();
            //full results string
            String heismanTop5 = "";
            for (int i = 0; i < 5; ++i) {
                Player p = heismanCandidates.get(i);
                heismanTop5 += (i + 1) + ". " + p.team.abbr + "(" + p.team.wins + "-" + p.team.losses + ")" + " - ";
                if (p instanceof PlayerQB) {
                    PlayerQB pqb = (PlayerQB) p;
                    heismanTop5 += " QB " + pqb.name + " [" + pqb.getYrStr() +
                            "]\n \t\t(" + pqb.statsTD + " TDs, " + pqb.statsInt + " Int, " + pqb.statsPassYards + " Yds)\n\n";
                } else if (p instanceof PlayerRB) {
                    PlayerRB prb = (PlayerRB) p;
                    heismanTop5 += " RB " + prb.name + " [" + prb.getYrStr() +
                            "]\n \t\t(" + prb.statsTD + " TDs, " + prb.statsFumbles + " Fum, " + prb.statsRushYards + " Yds)\n\n";
                } else if (p instanceof PlayerWR) {
                    PlayerWR pwr = (PlayerWR) p;
                    heismanTop5 += " WR " + pwr.name + " [" + pwr.getYrStr() +
                            "]\n \t\t(" + pwr.statsTD + " TDs, " + pwr.statsFumbles + " Fum, " + pwr.statsRecYards + " Yds)\n\n";
                }
            }
            return heismanTop5;
        }
    }

    /**
     * Perform the heisman ceremony. Congratulate winner and give top 5 vote getters.
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
                heismanTop5 += (i + 1) + ". " + p.team.abbr + "(" + p.team.wins + "-" + p.team.losses + ")" + " - ";
                if (p instanceof PlayerQB) {
                    PlayerQB pqb = (PlayerQB) p;
                    heismanTop5 += " QB " + pqb.getInitialName() + ": " + p.getHeismanScore() + " votes\n\t("
                            + pqb.statsTD + " TDs, " + pqb.statsInt + " Int, " + pqb.statsPassYards + " Yds)\n\n";
                } else if (p instanceof PlayerRB) {
                    PlayerRB prb = (PlayerRB) p;
                    heismanTop5 += " RB " + prb.getInitialName() + ": " + p.getHeismanScore() + " votes\n\t("
                            + prb.statsTD + " TDs, " + prb.statsFumbles + " Fum, " + prb.statsRushYards + " Yds)\n\n";
                } else if (p instanceof PlayerWR) {
                    PlayerWR pwr = (PlayerWR) p;
                    heismanTop5 += " WR " + pwr.getInitialName() + ": " + p.getHeismanScore() + " votes\n\t("
                            + pwr.statsTD + " TDs, " + pwr.statsFumbles + " Fum, " + pwr.statsRecYards + " Yds)\n\n";
                }
            }
            String heismanStats = "";
            String heismanWinnerStr = "";
            if (heisman instanceof PlayerQB) {
                //qb heisman
                PlayerQB heisQB = (PlayerQB) heisman;
                heismanWinnerStr = "Congratulations to the Player of the Year, " + heisQB.team.abbr +
                        " QB " + heisQB.name + " [" + heisman.getYrStr() + "], who had " +
                        heisQB.statsTD + " TDs, just " + heisQB.statsInt + " interceptions, and " +
                        heisQB.statsPassYards + " passing yards. He led " + heisQB.team.name +
                        " to a " + heisQB.team.wins + "-" + heisQB.team.losses + " record and a #" + heisQB.team.rankTeamPollScore +
                        " poll ranking.";
                heismanStats = heismanWinnerStr + "\n\nFull Results:" + heismanTop5;
            } else if (heisman instanceof PlayerRB) {
                //rb heisman
                PlayerRB heisRB = (PlayerRB) heisman;
                heismanWinnerStr = "Congratulations to the Player of the Year, " + heisRB.team.abbr +
                        " RB " + heisRB.name + " [" + heisman.getYrStr() + "], who had " +
                        heisRB.statsTD + " TDs, just " + heisRB.statsFumbles + " fumbles, and " +
                        heisRB.statsRushYards + " rushing yards. He led " + heisRB.team.name +
                        " to a " + heisRB.team.wins + "-" + heisRB.team.losses + " record and a #" + heisRB.team.rankTeamPollScore +
                        " poll ranking.";
                heismanStats = heismanWinnerStr + "\n\nFull Results:" + heismanTop5;
            } else if (heisman instanceof PlayerWR) {
                //wr heisman
                PlayerWR heisWR = (PlayerWR) heisman;
                heismanWinnerStr = "Congratulations to the Player of the Year, " + heisWR.team.abbr +
                        " WR " + heisWR.name + " [" + heisman.getYrStr() + "], who had " +
                        heisWR.statsTD + " TDs, just " + heisWR.statsFumbles + " fumbles, and " +
                        heisWR.statsRecYards + " receiving yards. He led " + heisWR.team.name +
                        " to a " + heisWR.team.wins + "-" + heisWR.team.losses + " record and a #" + heisWR.team.rankTeamPollScore +
                        " poll ranking.";
                heismanStats = heismanWinnerStr + "\n\nFull Results:" + heismanTop5;
            }

            // Add news story
            if (putNewsStory) {
                newsStories.get(13).add(heisman.name + " is the Player of the Year!>" + heismanWinnerStr);
                heismanWinnerStrFull = heismanStats;
            }

            return heismanStats;

        } else {
            return heismanWinnerStrFull;
        }
    }

    /**
     * Gets All Americans, best of all conference teams
     * @return string list of all americans
     */
    public String getAllAmericanStr() {
        if (allAmericans.isEmpty()) {
            ArrayList<PlayerQB> qbs = new ArrayList<>();
            ArrayList<PlayerRB> rbs = new ArrayList<>();
            ArrayList<PlayerWR> wrs = new ArrayList<>();
            ArrayList<PlayerOL> ols = new ArrayList<>();
            ArrayList<PlayerK> ks = new ArrayList<>();
            ArrayList<PlayerS> ss = new ArrayList<>();
            ArrayList<PlayerCB> cbs = new ArrayList<>();
            ArrayList<PlayerF7> f7s = new ArrayList<>();

            for (Conference c : conferences) {
                c.getAllConfPlayers();
                qbs.add((PlayerQB) c.allConfPlayers.get(0));
                rbs.add((PlayerRB) c.allConfPlayers.get(1));
                rbs.add((PlayerRB) c.allConfPlayers.get(2));
                wrs.add((PlayerWR) c.allConfPlayers.get(3));
                wrs.add((PlayerWR) c.allConfPlayers.get(4));
                wrs.add((PlayerWR) c.allConfPlayers.get(5));
                for (int i = 6; i < 11; ++i) {
                    ols.add((PlayerOL) c.allConfPlayers.get(i));
                }
                ks.add((PlayerK) c.allConfPlayers.get(11));
                ss.add((PlayerS) c.allConfPlayers.get(12));
                for (int i = 13; i < 16; ++i) {
                    cbs.add((PlayerCB) c.allConfPlayers.get(i));
                }
                for (int i = 16; i < 23; ++i) {
                    f7s.add((PlayerF7) c.allConfPlayers.get(i));
                }
            }

            Collections.sort(qbs, new PlayerHeismanComp());
            Collections.sort(rbs, new PlayerHeismanComp());
            Collections.sort(wrs, new PlayerHeismanComp());
            Collections.sort(ols, new PlayerHeismanComp());
            Collections.sort(ks, new PlayerHeismanComp());
            Collections.sort(ss, new PlayerHeismanComp());
            Collections.sort(cbs, new PlayerHeismanComp());
            Collections.sort(f7s, new PlayerHeismanComp());

            allAmericans.add(qbs.get(0));
            qbs.get(0).wonAllAmerican = true;
            allAmericans.add(rbs.get(0));
            rbs.get(0).wonAllAmerican = true;
            allAmericans.add(rbs.get(1));
            rbs.get(1).wonAllAmerican = true;
            for (int i = 0; i < 3; ++i) {
                allAmericans.add(wrs.get(i));
                wrs.get(i).wonAllAmerican = true;
            }
            for (int i = 0; i < 5; ++i) {
                allAmericans.add(ols.get(i));
                ols.get(i).wonAllAmerican = true;
            }
            allAmericans.add(ks.get(0));
            ks.get(0).wonAllAmerican = true;
            allAmericans.add(ss.get(0));
            ss.get(0).wonAllAmerican = true;
            for (int i = 0; i < 3; ++i) {
                allAmericans.add(cbs.get(i));
                cbs.get(i).wonAllAmerican = true;
            }
            for (int i = 0; i < 7; ++i) {
                allAmericans.add(f7s.get(i));
                f7s.get(i).wonAllAmerican = true;
            }
        }

        StringBuilder allAmerican = new StringBuilder();
        for (int i = 0; i < allAmericans.size(); ++i) {
            Player p = allAmericans.get(i);
            allAmerican.append(p.team.abbr + "(" + p.team.wins + "-" + p.team.losses + ")" + " - ");
            if (p instanceof PlayerQB) {
                PlayerQB pqb = (PlayerQB) p;
                allAmerican.append(" QB " + pqb.name + " [" + pqb.getYrStr() + "]\n \t\t" +
                        pqb.statsTD + " TDs, " + pqb.statsInt + " Int, " + pqb.statsPassYards + " Yds\n");
            } else if (p instanceof PlayerRB) {
                PlayerRB prb = (PlayerRB) p;
                allAmerican.append(" RB " + prb.name + " [" + prb.getYrStr() + "]\n \t\t" +
                        prb.statsTD + " TDs, " + prb.statsFumbles + " Fum, " + prb.statsRushYards + " Yds\n");
            } else if (p instanceof PlayerWR) {
                PlayerWR pwr = (PlayerWR) p;
                allAmerican.append(" WR " + pwr.name + " [" + pwr.getYrStr() + "]\n \t\t" +
                        pwr.statsTD + " TDs, " + pwr.statsFumbles + " Fum, " + pwr.statsRecYards + " Yds\n");
            } else if (p instanceof PlayerK) {
                PlayerK pk = (PlayerK) p;
                allAmerican.append(" K " + pk.name + " [" + pk.getYrStr() + "]\n \t\t" +
                        "FGs: " + pk.statsFGMade + "/" + pk.statsFGAtt + ", XPs: " + pk.statsXPMade + "/" + pk.statsXPAtt + "\n");
            } else {
                allAmerican.append(" " + p.position + " " + p.name + " [" + p.getYrStr() + "]\n");
            }
            allAmerican.append(" \t\tOverall: " + p.ratOvr + ", Potential: " + p.getLetterGrade(p.ratPot) + "\n\n>");
        }

        // Go through all the all conf players to get the all americans
        return allAmerican.toString();
    }

    /**
     * Get a string list of all conference team of choice
     * @param confNum which conference
     * @return string of the conference team
     */
    public String getAllConfStr(int confNum) {
        ArrayList<Player> allConfPlayers = conferences.get(confNum).getAllConfPlayers();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < allConfPlayers.size(); ++i) {
            Player p = allConfPlayers.get(i);
            sb.append(p.team.abbr + "(" + p.team.wins + "-" + p.team.losses + ")" + " - ");
            if (p instanceof PlayerQB) {
                PlayerQB pqb = (PlayerQB) p;
                sb.append(" QB " + pqb.name + " [" + pqb.getYrStr() + "]\n \t\t" +
                        pqb.statsTD + " TDs, " + pqb.statsInt + " Int, " + pqb.statsPassYards + " Yds\n");
            } else if (p instanceof PlayerRB) {
                PlayerRB prb = (PlayerRB) p;
                sb.append(" RB " + prb.name + " [" + prb.getYrStr() + "]\n \t\t" +
                        prb.statsTD + " TDs, " + prb.statsFumbles + " Fum, " + prb.statsRushYards + " Yds\n");
            } else if (p instanceof PlayerWR) {
                PlayerWR pwr = (PlayerWR) p;
                sb.append(" WR " + pwr.name + " [" + pwr.getYrStr() + "]\n \t\t" +
                        pwr.statsTD + " TDs, " + pwr.statsFumbles + " Fum, " + pwr.statsRecYards + " Yds\n");
            } else if (p instanceof PlayerK) {
                PlayerK pk = (PlayerK) p;
                sb.append(" K " + pk.name + " [" + pk.getYrStr() + "]\n \t\t" +
                        "FGs: " + pk.statsFGMade + "/" + pk.statsFGAtt + ", XPs: " + pk.statsXPMade + "/" + pk.statsXPAtt + "\n");
            } else {
                sb.append(" " + p.position + " " + p.name + " [" + p.getYrStr() + "]\n");
            }
            sb.append(" \t\tOverall: " + p.ratOvr + ", Potential: " + p.getLetterGrade(p.ratPot) + "\n\n>");
        }

        return sb.toString();
    }

    /**
     * Set the players leaving for each team.
     */
    public void getPlayersLeaving() {
        for (Team t : teamList) {
            t.getPlayersLeaving();
        }
    }

    /**
     * Get a mock draft of all players who are leaving, sorted by overall.
     * @return array of string reps of the players
     */
    public String[] getMockDraftPlayersList() {
        ArrayList<Player> allPlayersLeaving = new ArrayList<>();
        for (Team t : teamList) {
            for (Player p : t.playersLeaving) {
                if (p.ratOvr > 80 && !p.position.equals("K")) allPlayersLeaving.add(p);
            }
        }

        Collections.sort(allPlayersLeaving, new PlayerComparator());

        // Get 100 players (first 4 rounds)
        ArrayList<Player> NFLPlayers = new ArrayList<>(128);
        for (int i = 0; i < 128; ++i) {
            NFLPlayers.add(allPlayersLeaving.get(i));
        }

        String[] nflPlayers = new String[ NFLPlayers.size() ];
        for (int i = 0; i < nflPlayers.length; ++i) {
            nflPlayers[i] = NFLPlayers.get(i).getMockDraftStr();
        }

        return nflPlayers;
    }


    /**
     * Get list of all the teams and their rankings based on selection
     * @param selection stat to sort by, 0-13
     * @return list of the teams: ranking,str rep,stat
     */
    public ArrayList<String> getTeamRankingsStr(int selection) {
        /*
        0 = poll score
        1 = conf standings
        2 = sos
        3 = points
        4 = opp points
        5 = yards
        6 = opp yards
        7 = pass yards
        8 = rush yards
        9 = opp pass yards
        10 = opp rush yards
        11 = TO diff
        12 = off talent
        13 = def talent
        14 = prestige
         */
        ArrayList<Team> teams = teamList; //(ArrayList<Team>) teamList.clone();
        ArrayList<String> rankings = new ArrayList<String>();
        Team t;
        switch (selection) {
            case 0: Collections.sort( teams, new TeamCompPoll() );
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i + 1) + "," + t.strRepWithBowlResults() + "," + t.teamPollScore);
                }
                break;
            case 1: return getConfStandings();
            case 2: Collections.sort( teams, new TeamCompSoS() );
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i+1) + "," + t.strRepWithBowlResults() + "," + t.teamSOS);
                }
                break;
            case 3: Collections.sort( teams, new TeamCompSoW() );
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i+1) + "," + t.strRepWithBowlResults() + "," + t.teamStrengthOfWins);
                }
                break;
            case 4: Collections.sort( teams, new TeamCompPPG() );
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i+1) + "," + t.strRepWithBowlResults() + "," + (t.teamPoints/t.numGames()));
                }
                break;
            case 5: Collections.sort( teams, new TeamCompOPPG() );
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i+1) + "," + t.strRepWithBowlResults() + "," + (t.teamOppPoints/t.numGames()));
                }
                break;
            case 6: Collections.sort( teams, new TeamCompYPG() );
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i+1) + "," + t.strRepWithBowlResults() + "," + (t.teamYards/t.numGames()));
                }
                break;
            case 7: Collections.sort( teams, new TeamCompOYPG() );
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i+1) + "," + t.strRepWithBowlResults() + "," + (t.teamOppYards/t.numGames()));
                }
                break;
            case 8: Collections.sort( teams, new TeamCompPYPG() );
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i+1) + "," + t.strRepWithBowlResults() + "," + (t.teamPassYards/t.numGames()));
                }
                break;
            case 9: Collections.sort( teams, new TeamCompRYPG() );
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i+1) + "," + t.strRepWithBowlResults() + "," + (t.teamRushYards/t.numGames()));
                }
                break;
            case 10: Collections.sort( teams, new TeamCompOPYPG() );
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i+1) + "," + t.strRepWithBowlResults() + "," + (t.teamOppPassYards/t.numGames()));
                }
                break;
            case 11: Collections.sort( teams, new TeamCompORYPG() );
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i+1) + "," + t.strRepWithBowlResults() + "," + (t.teamOppRushYards/t.numGames()));
                }
                break;
            case 12: Collections.sort( teams, new TeamCompTODiff() );
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    if (t.teamTODiff > 0) rankings.add(t.getRankStrStarUser(i+1) + "," + t.strRepWithBowlResults() + ",+" + t.teamTODiff);
                    else rankings.add(t.getRankStrStarUser(i+1) + "," + t.strRepWithBowlResults() + "," + t.teamTODiff);
                }
                break;
            case 13: Collections.sort( teams, new TeamCompOffTalent() );
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i+1) + "," + t.strRepWithBowlResults() + "," + t.teamOffTalent);
                }
                break;
            case 14: Collections.sort( teams, new TeamCompDefTalent() );
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i+1) + "," + t.strRepWithBowlResults() + "," + t.teamDefTalent);
                }
                break;
            case 15: Collections.sort( teams, new TeamCompPrestige() );
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i + 1) + "," + t.strRepWithBowlResults() + "," + t.teamPrestige);
                }
                break;
            case 16: Collections.sort( teams, new TeamCompRecruitClass() );
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i + 1) + "," + t.strRepWithPrestige() + "," + t.getRecruitingClassRat());
                }
                break;
            default: Collections.sort( teams, new TeamCompPoll() );
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i+1) + "," + t.strRepWithBowlResults() + "," + t.teamPollScore);
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
            Collections.sort(confTeams, new TeamCompConfWins());
            confStandings.add(" ,"+c.confName+" Conference, ");
            Team t;
            for (int i = 0; i < confTeams.size(); ++i) {
                t = confTeams.get(i);
                confStandings.add(t.getRankStrStarUser(i+1) + "," + t.strRepWithBowlResults() + "," + t.getConfWins()+"-"+t.getConfLosses());
            }
            confTeams.clear();
        }
        return confStandings;
    }

    /**
     * Get String of the league's history, year by year.
     * Saves the NCG winner and the POTY.
     * @return list of the league's history.
     */
    public String getLeagueHistoryStr() {
        String hist = "";
        for (int i = 0; i < leagueHistory.size(); ++i) {
            hist += (seasonStart+i) + ":\n";
            hist += "\tChampions: " + leagueHistory.get(i)[0] + "\n";
            hist += "\tPOTY: " + heismanHistory.get(i) + "\n%";
        }
        return hist;
    }

    public String getLeagueTop25History(int year) {
        String hist = "";
        hist += (seasonStart+year) + ":\n";
        for (int i = 0; i < 25; ++i) {
            hist += "#" + i +" " + leagueHistory.get(year)[i] + "\n";
        }
        return hist;
    }

    /**
     * Get list of teams and their prestige, used for selecting when a new game is started
     * @return array of all the teams
     */
    public String[] getTeamListStr() {
        String[] teams = new String[teamList.size()];
        for (int i = 0; i < teamList.size(); ++i){
            teams[i] = teamList.get(i).conference + ":  " + teamList.get(i).name + "  [" + teamList.get(i).teamPrestige + "]";
        }
        return teams;
    }

    /**
     * Get list of all bowl games and their predicted teams
     * @return string of all the bowls and their predictions
     */
    public String getBowlGameWatchStr() {
        //if bowls arent scheduled yet, give predictions
        if (!hasScheduledBowls) {

            for (int i = 0; i < teamList.size(); ++i) {
                teamList.get(i).updatePollScore();
            }
            Collections.sort(teamList, new TeamCompPoll());

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

            sb.append(bowlNames[0]+":\n\t\t");
            t1 = teamList.get(4);
            t2 = teamList.get(6);
            sb.append(t1.strRep() + " vs " + t2.strRep() + "\n\n");

            sb.append(bowlNames[1]+":\n\t\t");
            t1 = teamList.get(5);
            t2 = teamList.get(7);
            sb.append(t1.strRep() + " vs " + t2.strRep() + "\n\n");

            sb.append(bowlNames[2]+":\n\t\t");
            t1 = teamList.get(8);
            t2 = teamList.get(14);
            sb.append(t1.strRep() + " vs " + t2.strRep() + "\n\n");

            sb.append(bowlNames[3]+":\n\t\t");
            t1 = teamList.get(9);
            t2 = teamList.get(15);
            sb.append(t1.strRep() + " vs " + t2.strRep() + "\n\n");

            sb.append(bowlNames[4]+":\n\t\t");
            t1 = teamList.get(10);
            t2 = teamList.get(11);
            sb.append(t1.strRep() + " vs " + t2.strRep() + "\n\n");

            sb.append(bowlNames[5]+":\n\t\t");
            t1 = teamList.get(12);
            t2 = teamList.get(13);
            sb.append(t1.strRep() + " vs " + t2.strRep() + "\n\n");

            for (int i = 6; i < 10; ++i) {
                sb.append(bowlNames[i]+":\n\t\t");
                t1 = teamList.get(10 + i);
                t2 = teamList.get(14 + i);
                sb.append(t1.strRep() + " vs " + t2.strRep() + "\n\n");
            }

            sb.append(bowlNames[10]+":\n\t\t");
            t1 = teamList.get(24);
            t2 = teamList.get(28);
            sb.append(t1.strRep() + " vs " + t2.strRep() + "\n\n");

            sb.append(bowlNames[11]+":\n\t\t");
            t1 = teamList.get(25);
            t2 = teamList.get(30);
            sb.append(t1.strRep() + " vs " + t2.strRep() + "\n\n");

            sb.append(bowlNames[12]+":\n\t\t");
            t1 = teamList.get(26);
            t2 = teamList.get(29);
            sb.append(t1.strRep() + " vs " + t2.strRep() + "\n\n");

            sb.append(bowlNames[13]+":\n\t\t");
            t1 = teamList.get(27);
            t2 = teamList.get(31);
            sb.append(t1.strRep() + " vs " + t2.strRep() + "\n\n");

            return sb.toString();

        } else {
            // Games have already been scheduled, give actual teams
            StringBuilder sb = new StringBuilder();

            sb.append("Semifinal 1v4:\n");
            sb.append(getGameSummaryBowl(semiG14));

            sb.append("\n\nSemifinal 2v3:\n");
            sb.append(getGameSummaryBowl(semiG23));

            for (int i = 0; i < bowlGames.length; ++i) {
                sb.append("\n\n"+bowlNames[i]+":\n");
                sb.append(getGameSummaryBowl(bowlGames[i]));
            }

            return sb.toString();
        }
    }

    /**
     * Get string of what happened in a particular bowl
     * @param g Bowl game to be examined
     * @return string of its summary, ALA W 24 - 40 @ GEO, etc
     */
    public String getGameSummaryBowl(Game g) {
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
     * Get a list of all the CCGs and their teams
     * @return
     */
    public String getCCGsStr() {
        StringBuilder sb = new StringBuilder();
        for (Conference c : conferences) {
            sb.append(c.getCCGStr()+"\n\n");
        }
        return sb.toString();
    }

    /**
     * Find team based on a name
     * @param name team name
     * @return reference to the Team object
     */
    public Team findTeam(String name) {
        for (int i = 0; i < teamList.size(); i++){
            if (teamList.get(i).strRep().equals(name)) {
                return teamList.get(i);
            }
        }
        return teamList.get(0);
    }

    /**
     * Find team based on a abbr
     * @param abbr team abbr
     * @return reference to the Team object
     */
    public Team findTeamAbbr(String abbr) {
        for (int i = 0; i < teamList.size(); i++){
            if (teamList.get(i).abbr.equals(abbr)) {
                return teamList.get(i);
            }
        }
        return teamList.get(0);
    }

    /**
     * Find conference based on a name
     * @param name conf name
     * @return reference to the Conference object
     */
    public Conference findConference(String name) {
        for (int i = 0; i < teamList.size(); i++){
            if (conferences.get(i).confName.equals(name)) {
                return conferences.get(i);
            }
        }
        return conferences.get(0);
    }

    public void updateTeamConf(String newConf, String oldConf, int x) {
        for (int i = 0; i  < teamList.size(); i++) {
            if (teamList.get(i).conference.equals(oldConf)) {
                teamList.get(i).conference = newConf;
            }
        }
            conferences.get(x).confName = newConf;
    }

    /**
     * See if team name is in use, or has illegal characters.
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

    /**
     * Get summary of what happened in the NCG
     * @return string of summary
     */
    public String ncgSummaryStr() {
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
     * @return ncgSummary, userTeam's summary
     */
    public String seasonSummaryStr() {
        setTeamRanks();
        StringBuilder sb = new StringBuilder();
        sb.append(ncgSummaryStr());
        sb.append("\n\n" + userTeam.seasonSummaryStr());
        sb.append("\n\n" + leagueRecords.brokenRecordsStr(getYear(), userTeam.abbr));
        return sb.toString();
    }

    /**
     * Save League in a file.
     * @param saveFile file to be overwritten
     * @return true if successful
     */
    public boolean saveLeague(File saveFile) {
        StringBuilder sb = new StringBuilder();

        // Save information about the save file, user team info
            sb.append((seasonStart + leagueHistory.size()) + ": " + userTeam.abbr + " (" + (userTeam.totalWins - userTeam.wins) + "-" + (userTeam.totalLosses - userTeam.losses) + ") " +
                    userTeam.totalCCs + " CCs, " + userTeam.totalNCs + " NCs>\n");


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
            sb.append(conferences.get(i).confName + "\n");
        }
        sb.append("END_CONFERENCES\n");

        // Save information about each team like W-L records, as well as all the players
        for (Team t : teamList) {
            sb.append(t.conference + "," + t.name + "," + t.abbr + "," + t.teamPrestige + "," +
                    (t.totalWins - t.wins) + "," + (t.totalLosses - t.losses) + "," + t.totalCCs + "," + t.totalNCs + "," + t.rivalTeam + "," +
                    t.totalNCLosses + "," + t.totalCCLosses + "," + t.totalBowls + "," + t.totalBowlLosses + "," +
                    t.teamStratOffNum + "," + t.teamStratDefNum + "," + (t.showPopups ? 1 : 0) + "," +
                    t.yearStartWinStreak.getStreakCSV() + "," +  t.teamTVDeal + "," + t.confTVDeal + "%" + t.evenYearHomeOpp + "%\n");
            sb.append(t.getPlayerInfoSaveFile());
            sb.append("END_PLAYERS\n");
        }

        // Save history of the user's team of the W-L and bowl results each year
        sb.append(userTeam.name + "\n");
        for (String s : userTeam.teamHistory) {
            sb.append(s + "\n");
        }
        sb.append("END_USER_TEAM\n");

        // Save who was blessed and cursed this year for news stories the following year
        if (saveBless != null) {
            sb.append(saveBless.abbr + "\n");
            sb.append("END_BLESS_TEAM\n");
        } else {
            sb.append("NULL\n");
            sb.append("END_BLESS_TEAM\n");
        }
        if (saveBless2 != null) {
            sb.append(saveBless2.abbr + "\n");
            sb.append("END_BLESS2_TEAM\n");
        } else {
            sb.append("NULL\n");
            sb.append("END_BLESS2_TEAM\n");
        }
        if (saveBless3 != null) {
            sb.append(saveBless3.abbr + "\n");
            sb.append("END_BLESS3_TEAM\n");
        } else {
            sb.append("NULL\n");
            sb.append("END_BLESS3_TEAM\n");
        }
        if (saveBless4 != null) {
            sb.append(saveBless4.abbr + "\n");
            sb.append("END_BLESS4_TEAM\n");
        } else {
            sb.append("NULL\n");
            sb.append("END_BLESS4_TEAM\n");
        }
        if (saveBless5 != null) {
            sb.append(saveBless5.abbr + "\n");
            sb.append("END_BLESS5_TEAM\n");
        } else {
            sb.append("NULL\n");
            sb.append("END_BLESS5_TEAM\n");
        }
        if (saveCurse != null) {
            sb.append(saveCurse.abbr + "\n");
            sb.append("END_CURSE_TEAM\n");
        } else {
            sb.append("NULL\n");
            sb.append("END_CURSE_TEAM\n");
        }
        if (saveCurse2 != null) {
            sb.append(saveCurse2.abbr + "\n");
            sb.append("END_CURSE2_TEAM\n");
        } else {
            sb.append("NULL\n");
            sb.append("END_CURSE2_TEAM\n");
        }
        if (saveCurse3 != null) {
            sb.append(saveCurse3.abbr + "\n");
            sb.append("END_CURSE3_TEAM\n");
        } else {
            sb.append("NULL\n");
            sb.append("END_CURSE3_TEAM\n");
        }
        if (saveCurse4 != null) {
            sb.append(saveCurse4.abbr + "\n");
            sb.append("END_CURSE4_TEAM\n");
        } else {
            sb.append("NULL\n");
            sb.append("END_CURSE4_TEAM\n");
        }
        if (saveCurse5 != null) {
            sb.append(saveCurse5.abbr + "\n");
            sb.append("END_CURSE5_TEAM\n");
        } else {
            sb.append("NULL\n");
            sb.append("END_CURSE5_TEAM\n");
        }

        // Save league records
        sb.append(leagueRecords.getRecordsStr());
        sb.append("END_LEAGUE_RECORDS\n");

        sb.append(yearStartLongestWinStreak.getStreakCSV());
        sb.append("\nEND_LEAGUE_WIN_STREAK\n");

        // Save user team records
        sb.append(userTeamRecords.getRecordsStr());
        sb.append("END_USER_TEAM_RECORDS\n");

        // Save all the Hall of Famers
        for (String s : userTeam.hallOfFame) {
            sb.append(s + "\n");
        }
        sb.append("END_HALL_OF_FAME\n");




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

class PlayerHeismanComp implements Comparator<Player> {
    @Override
    public int compare( Player a, Player b ) {
        return a.getHeismanScore() > b.getHeismanScore() ? -1 : a.getHeismanScore() == b.getHeismanScore() ? 0 : 1;
    }
}

class TeamCompPoll implements Comparator<Team> {
    @Override
    public int compare( Team a, Team b ) {
        return a.teamPollScore > b.teamPollScore ? -1 : a.teamPollScore == b.teamPollScore ? 0 : 1;
    }
}

class TeamCompSoW implements Comparator<Team> {
    @Override
    public int compare( Team a, Team b ) {
        return a.teamStrengthOfWins > b.teamStrengthOfWins ? -1 : a.teamStrengthOfWins == b.teamStrengthOfWins ? 0 : 1;
    }
}


class TeamCompSoS implements Comparator<Team> {
    @Override
    public int compare( Team a, Team b ) {
        return a.teamSOS > b.teamSOS ? -1 : a.teamSOS == b.teamSOS ? 0 : 1;
    }
}

class TeamCompPPG implements Comparator<Team> {
    @Override
    public int compare( Team a, Team b ) {
        return a.teamPoints/a.numGames() > b.teamPoints/b.numGames() ? -1 : a.teamPoints/a.numGames() == b.teamPoints/b.numGames() ? 0 : 1;
    }
}

class TeamCompOPPG implements Comparator<Team> {
    @Override
    public int compare( Team a, Team b ) {
        return a.teamOppPoints/a.numGames() < b.teamOppPoints/b.numGames() ? -1 : a.teamOppPoints/a.numGames() == b.teamOppPoints/b.numGames() ? 0 : 1;
    }
}

class TeamCompYPG implements Comparator<Team> {
    @Override
    public int compare( Team a, Team b ) {
        return a.teamYards/a.numGames() > b.teamYards/b.numGames() ? -1 : a.teamYards/a.numGames() == b.teamYards/b.numGames() ? 0 : 1;
    }
}

class TeamCompOYPG implements Comparator<Team> {
    @Override
    public int compare( Team a, Team b ) {
        return a.teamOppYards/a.numGames() < b.teamOppYards/b.numGames() ? -1 : a.teamOppYards/a.numGames() == b.teamOppYards/b.numGames() ? 0 : 1;
    }
}

class TeamCompOPYPG implements Comparator<Team> {
    @Override
    public int compare( Team a, Team b ) {
        return a.teamOppPassYards/a.numGames() < b.teamOppPassYards/b.numGames() ? -1 : a.teamOppPassYards/a.numGames() == b.teamOppPassYards/b.numGames() ? 0 : 1;
    }
}

class TeamCompORYPG implements Comparator<Team> {
    @Override
    public int compare( Team a, Team b ) {
        return a.teamOppRushYards/a.numGames() < b.teamOppRushYards/b.numGames() ? -1 : a.teamOppRushYards/a.numGames() == b.teamOppRushYards/b.numGames() ? 0 : 1;
    }
}

class TeamCompPYPG implements Comparator<Team> {
    @Override
    public int compare( Team a, Team b ) {
        return a.teamPassYards/a.numGames() > b.teamPassYards/b.numGames() ? -1 : a.teamPassYards/a.numGames() == b.teamPassYards/b.numGames() ? 0 : 1;
    }
}

class TeamCompRYPG implements Comparator<Team> {
    @Override
    public int compare( Team a, Team b ) {
        return a.teamRushYards/a.numGames() > b.teamRushYards/b.numGames() ? -1 : a.teamRushYards/a.numGames() == b.teamRushYards/b.numGames() ? 0 : 1;
    }
}

class TeamCompTODiff implements Comparator<Team> {
    @Override
    public int compare( Team a, Team b ) {
        return a.teamTODiff > b.teamTODiff ? -1 : a.teamTODiff == b.teamTODiff ? 0 : 1;
    }
}

class TeamCompOffTalent implements Comparator<Team> {
    @Override
    public int compare( Team a, Team b ) {
        return a.teamOffTalent > b.teamOffTalent ? -1 : a.teamOffTalent == b.teamOffTalent ? 0 : 1;
    }
}

class TeamCompDefTalent implements Comparator<Team> {
    @Override
    public int compare( Team a, Team b ) {
        return a.teamDefTalent > b.teamDefTalent ? -1 : a.teamDefTalent == b.teamDefTalent ? 0 : 1;
    }
}

class TeamCompPrestige implements Comparator<Team> {
    @Override
    public int compare( Team a, Team b ) {
        return a.teamPrestige > b.teamPrestige ? -1 : a.teamPrestige == b.teamPrestige ? 0 : 1;
    }
}

class TeamCompRecruitClass implements Comparator<Team> {
    @Override
    public int compare( Team a, Team b ) {
        return a.getRecruitingClassRat() > b.getRecruitingClassRat() ? -1 : a.getRecruitingClassRat() == b.getRecruitingClassRat() ? 0 : 1;
    }
}






