package Simulation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;
import java.util.Random;

/**
 * League class. Has 6 conferences of 10 teams each.
 *
 * @author Achi
 */
public class League {
    //Lists of conferences/teams
    public ArrayList<String[]> leagueHistory;
    public ArrayList<String> UserHistory;
    public ArrayList<String> heismanHistory;
    public ArrayList<Conference> conferences;
    public ArrayList<Team> teamList;
    public ArrayList<HeadCoach> coachList;
    public ArrayList<String> coachPrevTeam;
    public ArrayList<HeadCoach> coachStarList;
    public ArrayList<String> coachStarPrevTeam;
    public ArrayList<String> nameList;
    public ArrayList<String> lastNameList;
    public ArrayList<ArrayList<String>> newsStories;
    public ArrayList<ArrayList<String>> weeklyScores;

    public LeagueRecords leagueRecords;
    public LeagueRecords userTeamRecords;
    public TeamStreak longestWinStreak;
    public TeamStreak yearStartLongestWinStreak;
    public TeamStreak longestActiveWinStreak;

    // News Story Variables
    public Team saveLucky;
    public Team saveLucky2;
    public Team saveLucky3;
    public Team saveLucky4;
    public Team saveLucky5;
    public Team savePenalized;
    public Team savePenalized2;
    public Team savePenalized3;
    public Team savePenalized4;
    public Team savePenalized5;

    //Current week, 1-14
    public int currentWeek;
    public int randgm;
    public int randconf;
    int seasonStart = 2017;
    int countTeam = 120;

    int seasonWeeks = 20;

    //Bowl Games
    public boolean hasScheduledBowls;
    public Game semiG14;
    public Game semiG23;
    public Game ncg;
    public Game[] bowlGames;
    public int countBG = 18;

    //User Team
    public Team userTeam;

    boolean heismanDecided;
    Player heisman;
    Player coachWinner;
    ArrayList<Player> heismanCandidates;
    private String heismanWinnerStrFull;

    ArrayList<Player> allAmericans;
    private String allAmericanStr;

    public String[] bowlNames = {"Rose Bowl", "Orange Bowl", "Sugar Bowl", "Fiesta Bowl", "Peach Bowl", "Cotton Bowl", "Citrus Bowl", "Gator Bowl", "Cactus Bowl", "Alamo Bowl", "Holiday Bowl", "Sun Bowl",
            "Liberty Bowl", "Independence Bowl", "Vegas Bowl", "Military Bowl", "Aloha Bowl", "Humanitarian Bowl"};

    private boolean careerMode;

    /**
     * Creates League, sets up Conferences, reads team names and conferences from file.
     * Also schedules games for every team.
     */
    public League(String namesCSV, String lastNamesCSV, boolean career) {
        careerMode = career;
        heismanDecided = false;
        hasScheduledBowls = false;
        bowlGames = new Game[countBG];
        leagueHistory = new ArrayList<String[]>();
        UserHistory = new ArrayList<>();
        heismanHistory = new ArrayList<String>();
        coachList = new ArrayList<>();
        coachPrevTeam = new ArrayList<>();
        coachStarList = new ArrayList<>();
        coachStarPrevTeam = new ArrayList<>();

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
        ArrayList<Team> coachList = new ArrayList<>();
        coachPrevTeam = new ArrayList<>();
        coachStarList = new ArrayList<>();
        coachStarPrevTeam = new ArrayList<>();


        // Initialize new stories lists
        newsStories = new ArrayList<ArrayList<String>>();
        weeklyScores = new ArrayList<ArrayList<String>>();
        for (int i = 0; i < seasonWeeks; ++i) {
            newsStories.add(new ArrayList<String>());
            weeklyScores.add(new ArrayList<String>());
        }
        newsStories.get(0).add("New Season!>Ready for the new season, coach? Whether the National Championship is " +
                "on your mind, or just a winning season, good luck!");

        weeklyScores.get(0).add("Scores:>No games this week.");

        leagueRecords = new LeagueRecords();
        userTeamRecords = new LeagueRecords();
        longestWinStreak = new TeamStreak(getYear(), getYear(), 0, "XXX");
        yearStartLongestWinStreak = new TeamStreak(getYear(), getYear(), 0, "XXX");
        longestActiveWinStreak = new TeamStreak(getYear(), getYear(), 0, "XXX");

        // Read first names from file
        nameList = new ArrayList<String>();
        String[] namesSplit = namesCSV.split(",");
        for (String n : namesSplit) {
            if (isNameValid(n.trim()))
                nameList.add(n.trim());
        }

        // Read last names from file
        lastNameList = new ArrayList<String>();
        namesSplit = lastNamesCSV.split(",");
        for (String n : namesSplit) {
            if (isNameValid(n.trim()))
                lastNameList.add(n.trim());
        }

        //Set up conference
        // FUTURE: READ FROM XML OR CSV FILE

        //ACC - done + 12
        conferences.get(0).confTeams.add(new Team("Clemson", "CLEM", "ACC", 85, "WAKE", this));
        conferences.get(0).confTeams.add(new Team("Duke", "DUKE", "ACC", 53, "UNC", this));
        conferences.get(0).confTeams.add(new Team("Florida St", "FSU", "ACC", 62, "MIA", this));
        conferences.get(0).confTeams.add(new Team("Georgia Tech", "GT", "ACC", 48, "NCST", this));
        conferences.get(0).confTeams.add(new Team("Louisville", "LOUI", "ACC", 72, "PITT", this));
        conferences.get(0).confTeams.add(new Team("Miami", "MIA", "ACC", 77, "FSU", this));
        conferences.get(0).confTeams.add(new Team("North Carolina", "UNC", "ACC", 46, "DUKE", this));
        conferences.get(0).confTeams.add(new Team("NC State", "NCST", "ACC", 70, "GT", this));
        conferences.get(0).confTeams.add(new Team("Pittsburgh", "PITT", "ACC", 64, "LOUI", this));
        conferences.get(0).confTeams.add(new Team("Wake Forest", "WAKE", "ACC", 52, "CLEM", this));
        conferences.get(0).confTeams.add(new Team("Virginia", "VIR", "ACC", 50, "VTEC", this));
        conferences.get(0).confTeams.add(new Team("Virginia Tech", "VTEC", "ACC", 73, "VIR", this));

        //American - done x 12
        conferences.get(1).confTeams.add(new Team("Boston College", "BC", "American", 40, "TEM", this));
        conferences.get(1).confTeams.add(new Team("Central Florida", "UCF", "American", 66, "USF", this));
        conferences.get(1).confTeams.add(new Team("Cinncinati", "CINN", "American", 42, "MEMP", this));
        conferences.get(1).confTeams.add(new Team("Maryland", "MARY", "American", 45, "RUT", this));
        conferences.get(1).confTeams.add(new Team("Memphis", "MEMP", "American", 66, "CINN", this));
        conferences.get(1).confTeams.add(new Team("Notre Dame", "ND", "American", 78, "WVU", this));
        conferences.get(1).confTeams.add(new Team("Rutgers", "RUT", "American", 42, "MARY", this));
        conferences.get(1).confTeams.add(new Team("South Florida", "USF", "American", 70, "UCF", this));
        conferences.get(1).confTeams.add(new Team("Syracuse", "SYR", "American", 48, "UCON", this));
        conferences.get(1).confTeams.add(new Team("Temple", "TEM", "American", 43, "BC", this));
        conferences.get(1).confTeams.add(new Team("UConn", "UCON", "American", 40, "SYR", this));
        conferences.get(1).confTeams.add(new Team("West Virginia", "WVU", "American", 70, "ND", this));

        //Big Ten - done x 12
        conferences.get(2).confTeams.add(new Team("Illinois", "ILL", "Big Ten", 40, "IND", this));
        conferences.get(2).confTeams.add(new Team("Indiana", "IND", "Big Ten", 40, "ILL", this));
        conferences.get(2).confTeams.add(new Team("Iowa", "IOW", "Big Ten", 64, "NEB", this));
        conferences.get(2).confTeams.add(new Team("Michigan", "MIC", "Big Ten", 78, "OSU", this));
        conferences.get(2).confTeams.add(new Team("Michigan St", "MSU", "Big Ten", 74, "PSU", this));
        conferences.get(2).confTeams.add(new Team("Minnesota", "MIN", "Big Ten", 60, "WIS", this));
        conferences.get(2).confTeams.add(new Team("Nebraska", "NEB", "Big Ten", 64, "IOW", this));
        conferences.get(2).confTeams.add(new Team("Northwestern", "NWU", "Big Ten", 60, "PUR", this));
        conferences.get(2).confTeams.add(new Team("Ohio State", "OSU", "Big Ten", 80, "MIC", this));
        conferences.get(2).confTeams.add(new Team("Penn State", "PSU", "Big Ten", 82, "MSU", this));
        conferences.get(2).confTeams.add(new Team("Purdue", "PUR", "Big Ten", 50, "NWU", this));
        conferences.get(2).confTeams.add(new Team("Wisconsin", "WIS", "Big Ten", 79, "MIN", this));

        //Big 12 - done x 12
        conferences.get(3).confTeams.add(new Team("Baylor", "BAY", "Big 12", 42, "TCU", this));
        conferences.get(3).confTeams.add(new Team("Houston", "HOU", "Big 12", 57, "TTEC", this));
        conferences.get(3).confTeams.add(new Team("Iowa State", "ISU", "Big 12", 65, "MIZ", this));
        conferences.get(3).confTeams.add(new Team("Texas Tech", "TTEC", "Big 12", 50, "HOU", this));
        conferences.get(3).confTeams.add(new Team("Kansas", "KAN", "Big 12", 30, "KSU", this));
        conferences.get(3).confTeams.add(new Team("Kansas St", "KSU", "Big 12", 66, "KAN", this));
        conferences.get(3).confTeams.add(new Team("Oklahoma", "OKL", "Big 12", 80, "TEX", this));
        conferences.get(3).confTeams.add(new Team("Oklahoma St", "OKST", "Big 12", 76, "TXAM", this));
        conferences.get(3).confTeams.add(new Team("Texas", "TEX", "Big 12", 68, "OKL", this));
        conferences.get(3).confTeams.add(new Team("Texas AM", "TXAM", "Big 12", 70, "OKST", this));
        conferences.get(3).confTeams.add(new Team("TCU", "TCU", "Big 12", 77, "BAY", this));
        conferences.get(3).confTeams.add(new Team("Missouri", "MIZ", "Big 12", 50, "ISU", this));

        //Conf USA x 12
        conferences.get(4).confTeams.add(new Team("Army", "ARMY", "Conf USA", 43, "NAVY", this));
        conferences.get(4).confTeams.add(new Team("East Carolina", "ECU", "Conf USA", 32, "WKU", this));
        conferences.get(4).confTeams.add(new Team("Florida Atl", "FAU", "Conf USA", 47, "FIU", this));
        conferences.get(4).confTeams.add(new Team("Florida Intl", "FIU", "Conf USA", 45, "FAU", this));
        conferences.get(4).confTeams.add(new Team("LA Tech", "LTEC", "Conf USA", 43, "TUL", this));
        conferences.get(4).confTeams.add(new Team("Marshall", "MARS", "Conf USA", 47, "SMU", this));
        conferences.get(4).confTeams.add(new Team("Navy", "NAVY", "Conf USA", 50, "ARMY", this));
        conferences.get(4).confTeams.add(new Team("Southern Meth", "SMU", "Conf USA", 47, "MARS", this));
        conferences.get(4).confTeams.add(new Team("Southern Miss", "SMIS", "Conf USA", 42, "UAB", this));
        conferences.get(4).confTeams.add(new Team("Tulane", "TUL", "Conf USA", 38, "LTEC", this));
        conferences.get(4).confTeams.add(new Team("UAB", "UAB", "Conf USA", 40, "SMIS", this));
        conferences.get(4).confTeams.add(new Team("Western Kentucky", "WKU", "Conf USA", 46, "ECU", this));

        // MAC - x 12
        conferences.get(5).confTeams.add(new Team("Akron", "AKR", "MAC", 35, "KNST", this));
        conferences.get(5).confTeams.add(new Team("Ball St", "BAL", "MAC", 28, "NIU", this));
        conferences.get(5).confTeams.add(new Team("Bowling Green", "BG", "MAC", 30, "TOL", this));
        conferences.get(5).confTeams.add(new Team("Buffalo", "BUF", "MAC", 34, "EMU", this));
        conferences.get(5).confTeams.add(new Team("Central Mich", "CMU", "MAC", 40, "WMU", this));
        conferences.get(5).confTeams.add(new Team("Eastern Mich", "EMU", "MAC", 38, "BUF", this));
        conferences.get(5).confTeams.add(new Team("Kent State", "KNST", "MAC", 30, "KNST", this));
        conferences.get(5).confTeams.add(new Team("Miami OH", "MiOH", "MAC", 34, "OHIO", this));
        conferences.get(5).confTeams.add(new Team("Northern Illinois", "NIU", "MAC", 44, "BAL", this));
        conferences.get(5).confTeams.add(new Team("Ohio", "OHIO", "MAC", 44, "MiOH", this));
        conferences.get(5).confTeams.add(new Team("Toledo", "TOL", "MAC", 44, "BG", this));
        conferences.get(5).confTeams.add(new Team("Western Mich", "WMU", "MAC", 44, "CMU", this));

        //Mt West x 12
        conferences.get(6).confTeams.add(new Team("Air Force", "AF", "Mt West", 42, "HAW", this));
        conferences.get(6).confTeams.add(new Team("Boise State", "BOIS", "Mt West", 65, "SDSU", this));
        conferences.get(6).confTeams.add(new Team("BYU", "BYU", "Mt West", 32, "UTST", this));
        conferences.get(6).confTeams.add(new Team("Colorado St", "CSU", "Mt West", 47, "WYO", this));
        conferences.get(6).confTeams.add(new Team("Fresno St", "FRES", "Mt West", 40, "SJSU", this));
        conferences.get(6).confTeams.add(new Team("Hawaii", "HAW", "Mt West", 35, "AF", this));
        conferences.get(6).confTeams.add(new Team("Nevada", "NEV", "Mt West", 35, "NMEX", this));
        conferences.get(6).confTeams.add(new Team("New Mexico", "NMEX", "Mt West", 38, "NEV", this));
        conferences.get(6).confTeams.add(new Team("San Diego State", "SDSU", "Mt West", 65, "BOIS", this));
        conferences.get(6).confTeams.add(new Team("San Jose St", "SJSU", "Mt West", 29, "FRES", this));
        conferences.get(6).confTeams.add(new Team("Utah State", "UTST", "Mt West", 44, "BYU", this));
        conferences.get(6).confTeams.add(new Team("Wyoming", "WYOM", "Mt West", 47, "CSU", this));

        //Pac-12 - done x 12
        conferences.get(7).confTeams.add(new Team("Arizona", "ARIZ", "Pac-12", 60, "ASU", this));
        conferences.get(7).confTeams.add(new Team("Arizona State", "ASU", "Pac-12", 62, "ARIZ", this));
        conferences.get(7).confTeams.add(new Team("California", "CAL", "Pac-12", 55, "STAN", this));
        conferences.get(7).confTeams.add(new Team("Colorado", "COL", "Pac-12", 55, "UTAH", this));
        conferences.get(7).confTeams.add(new Team("Oregon", "OREG", "Pac-12", 64, "ORST", this));
        conferences.get(7).confTeams.add(new Team("Oregon State", "ORST", "Pac-12", 40, "OREG", this));
        conferences.get(7).confTeams.add(new Team("Stanford", "STAN", "Pac-12", 75, "CAL", this));
        conferences.get(7).confTeams.add(new Team("UCLA", "UCLA", "Pac-12", 64, "USC", this));
        conferences.get(7).confTeams.add(new Team("USC", "USC", "Pac-12", 80, "UCLA", this));
        conferences.get(7).confTeams.add(new Team("Washington", "WASH", "Pac-12", 80, "WSU", this));
        conferences.get(7).confTeams.add(new Team("Wash State", "WSU", "Pac-12", 73, "WASH", this));
        conferences.get(7).confTeams.add(new Team("Utah", "UTAH", "Pac-12", 67, "COL", this));

        //SEC - done x 12
        conferences.get(8).confTeams.add(new Team("Alabama", "BAMA", "SEC", 93, "AUB", this));
        conferences.get(8).confTeams.add(new Team("Arkansas", "ARK", "SEC", 50, "LSU", this));
        conferences.get(8).confTeams.add(new Team("Auburn", "AUB", "SEC", 75, "BAMA", this));
        conferences.get(8).confTeams.add(new Team("Florida", "FLOR", "SEC", 66, "UGA", this));
        conferences.get(8).confTeams.add(new Team("Georgia", "UGA", "SEC", 82, "FLOR", this));
        conferences.get(8).confTeams.add(new Team("Kentucky", "UK", "SEC", 55, "SC", this));
        conferences.get(8).confTeams.add(new Team("LSU", "LSU", "SEC", 75, "ARK", this));
        conferences.get(8).confTeams.add(new Team("Ole Miss", "MISS", "SEC", 55, "MSST", this));
        conferences.get(8).confTeams.add(new Team("Mississippi St", "MSST", "SEC", 73, "MISS", this));
        conferences.get(8).confTeams.add(new Team("South Carolina", "SC", "SEC", 53, "UK", this));
        conferences.get(8).confTeams.add(new Team("Tennessee", "TENN", "SEC", 66, "VAND", this));
        conferences.get(8).confTeams.add(new Team("Vanderbilt", "VAND", "SEC", 47, "TENN", this));

        //Sun Belt
        conferences.get(9).confTeams.add(new Team("Appalachian State", "APP", "Sun Belt", 42, "ODOM", this));
        conferences.get(9).confTeams.add(new Team("LA Monroe", "LMON", "Sun Belt", 38, "TROY", this));
        conferences.get(9).confTeams.add(new Team("Mid Tenn State", "MTSU", "Sun Belt", 39, "GASO", this));
        conferences.get(9).confTeams.add(new Team("Troy", "TROY", "Sun Belt", 46, "LMON", this));
        conferences.get(9).confTeams.add(new Team("UT El Paso", "UTEP", "Sun Belt", 30, "UTSA", this));
        conferences.get(9).confTeams.add(new Team("North Texas", "NTEX", "Sun Belt", 43, "TXST", this));
        conferences.get(9).confTeams.add(new Team("Tulsa", "TULS", "Sun Belt", 39, "RICE", this));
        conferences.get(9).confTeams.add(new Team("Rice", "RICE", "Sun Belt", 33, "TULS", this));
        conferences.get(9).confTeams.add(new Team("Texas St", "TXST", "Sun Belt", 30, "NTEX", this));
        conferences.get(9).confTeams.add(new Team("Georgia Southern", "GASO", "Sun Belt", 30, "MTSU", this));
        conferences.get(9).confTeams.add(new Team("Old Dominion", "ODOM", "Sun Belt", 35, "APP", this));
        conferences.get(9).confTeams.add(new Team("UT San Antonio", "UTSA", "Sun Belt", 43, "UTEP", this));

        //set teamList
        teamList = new ArrayList<Team>();
        for (int i = 0; i < conferences.size(); ++i) {
            for (int j = 0; j < conferences.get(i).confTeams.size(); ++j) {
                teamList.add(conferences.get(i).confTeams.get(j));
                teamList.get(i).teamStratOffNum = teamList.get(i).getCPUOffense();
                teamList.get(i).teamStratDefNum = teamList.get(i).getCPUDefense();
                teamList.get(i).teamStratOff = teamList.get(i).getTeamStrategiesOff()[teamList.get(i).teamStratOffNum];
                teamList.get(i).teamStratDef = teamList.get(i).getTeamStrategiesDef()[teamList.get(i).teamStratDefNum];
            }
        }

        //set up schedule
        Random rand = new Random();
        int max = 8;
        int min = 5;
        randgm = rand.nextInt((max - min) + 1) + min;
        int maxc = 1;
        int minc = 0;
        randconf = rand.nextInt((maxc - minc) + 1) + minc;

        for (int i = 0; i < conferences.size(); ++i) {
            conferences.get(i).setUpSchedule();
        }
        for (int i = 0; i < conferences.size(); ++i) {
            conferences.get(i).setUpOOCSchedule();
        }
        for (int i = 0; i < conferences.size(); ++i) {
            conferences.get(i).insertOOCSchedule();
        }

        newsStories.get(0).add("Conference Prestige>The latest surveys are in. The " + getYear() + " prestige ratings for each conference are:\n\n" +
                conferences.get(0).confName + ":  " + conferences.get(0).confPrestige + "\n" +
                conferences.get(1).confName + ":  " + conferences.get(1).confPrestige + "\n" +
                conferences.get(2).confName + ":  " + conferences.get(2).confPrestige + "\n" +
                conferences.get(3).confName + ":  " + conferences.get(3).confPrestige + "\n" +
                conferences.get(4).confName + ":  " + conferences.get(4).confPrestige + "\n" +
                conferences.get(5).confName + ":  " + conferences.get(5).confPrestige + "\n" +
                conferences.get(6).confName + ":  " + conferences.get(6).confPrestige + "\n" +
                conferences.get(7).confName + ":  " + conferences.get(7).confPrestige + "\n" +
                conferences.get(8).confName + ":  " + conferences.get(8).confPrestige + "\n" +
                conferences.get(9).confName + ":  " + conferences.get(9).confPrestige + "\n");
    }

    /**
     * Creates a CUSTOM League Universe
     */


    //public League(String namesCSV, String lastNamesCSV, boolean career, File customConf, File customTeams) {
    public League(String namesCSV, String lastNamesCSV, boolean career, File customConf, File customTeams, File customBowl) {
        String line = null;
        careerMode = career;
        heismanDecided = false;
        hasScheduledBowls = false;
        bowlGames = new Game[countBG];
        leagueHistory = new ArrayList<String[]>();
        UserHistory = new ArrayList<>();
        heismanHistory = new ArrayList<String>();
        coachList = new ArrayList<>();
        coachPrevTeam = new ArrayList<>();
        coachStarList = new ArrayList<>();
        coachStarPrevTeam = new ArrayList<>();
        currentWeek = 0;
        conferences = new ArrayList<Conference>();

        try {
            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(new FileReader(customConf));

            //First ignore the save file info
            line = bufferedReader.readLine();
            while ((line = bufferedReader.readLine()) != null && !line.equals("[END_CONFERENCES]")) {
                conferences.add(new Conference(line.toString(), this));
            }
        } catch (FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file");
        } catch (IOException ex) {
            System.out.println(
                    "Error reading file");
        }

        allAmericans = new ArrayList<Player>();
        ArrayList<Team> coachList = new ArrayList<>();
        coachPrevTeam = new ArrayList<>();
        coachStarList = new ArrayList<>();
        coachStarPrevTeam = new ArrayList<>();


        // Initialize new stories lists
        newsStories = new ArrayList<ArrayList<String>>();
        weeklyScores = new ArrayList<ArrayList<String>>();
        for (int i = 0; i < seasonWeeks; ++i) {
            newsStories.add(new ArrayList<String>());
            weeklyScores.add(new ArrayList<String>());
        }
        newsStories.get(0).add("New Season!>Ready for the new season, coach? Whether the National Championship is " +
                "on your mind, or just a winning season, good luck!");

        weeklyScores.get(0).add("Scores:>No games this week.");

        leagueRecords = new LeagueRecords();
        userTeamRecords = new LeagueRecords();
        longestWinStreak = new TeamStreak(getYear(), getYear(), 0, "XXX");
        yearStartLongestWinStreak = new TeamStreak(getYear(), getYear(), 0, "XXX");
        longestActiveWinStreak = new TeamStreak(getYear(), getYear(), 0, "XXX");

        // Read first names from file
        nameList = new ArrayList<String>();
        String[] namesSplit = namesCSV.split(",");
        for (String n : namesSplit) {
            if (isNameValid(n.trim()))
                nameList.add(n.trim());
        }

        // Read last names from file
        lastNameList = new ArrayList<String>();
        namesSplit = lastNamesCSV.split(",");
        for (String n : namesSplit) {
            if (isNameValid(n.trim()))
                lastNameList.add(n.trim());
        }

        //Set up conference teams
        try {
            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(new FileReader(customTeams));

            //First ignore the save file info
            line = bufferedReader.readLine();
            while ((line = bufferedReader.readLine()) != null && !line.equals("[END_TEAMS]")) {
                for (int c = 0; c < conferences.size(); ++c) {
                    while ((line = bufferedReader.readLine()) != null && !line.equals("[END_CONF]")) {
                        line.replace("\"", "\\\"");
                        String[] filesSplit = line.split(", ");
                        String tmName = filesSplit[0];
                        String tmAbbr = filesSplit[1];
                        String tmConf = filesSplit[2];
                        int tmPres = Integer.parseInt(filesSplit[3]);
                        String tmRival = filesSplit[4];
                        conferences.get(c).confTeams.add(new Team(tmName, tmAbbr, tmConf, tmPres, tmRival, this));
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
        teamList = new ArrayList<Team>();
        for (int i = 0; i < conferences.size(); ++i) {
            for (int j = 0; j < conferences.get(i).confTeams.size(); ++j) {
                teamList.add(conferences.get(i).confTeams.get(j));
                teamList.get(i).teamStratOffNum = teamList.get(i).getCPUOffense();
                teamList.get(i).teamStratDefNum = teamList.get(i).getCPUDefense();
                teamList.get(i).teamStratOff = teamList.get(i).getTeamStrategiesOff()[teamList.get(i).teamStratOffNum];
                teamList.get(i).teamStratDef = teamList.get(i).getTeamStrategiesDef()[teamList.get(i).teamStratDefNum];
            }
        }

        //set up schedule
        Random rand = new Random();
        int max = 8;
        int min = 5;
        randgm = rand.nextInt((max - min) + 1) + min;
        int maxc = 1;
        int minc = 0;
        randconf = rand.nextInt((maxc - minc) + 1) + minc;

        for (int i = 0; i < conferences.size(); ++i) {
            conferences.get(i).setUpSchedule();
        }
        for (int i = 0; i < conferences.size(); ++i) {
            conferences.get(i).setUpOOCSchedule();
        }
        for (int i = 0; i < conferences.size(); ++i) {
            conferences.get(i).insertOOCSchedule();
        }

        newsStories.get(0).add("Conference Prestige>The latest surveys are in. The " + getYear() + " prestige ratings for each conference are:\n\n" +
                conferences.get(0).confName + ":  " + conferences.get(0).confPrestige + "\n" +
                conferences.get(1).confName + ":  " + conferences.get(1).confPrestige + "\n" +
                conferences.get(2).confName + ":  " + conferences.get(2).confPrestige + "\n" +
                conferences.get(3).confName + ":  " + conferences.get(3).confPrestige + "\n" +
                conferences.get(4).confName + ":  " + conferences.get(4).confPrestige + "\n" +
                conferences.get(5).confName + ":  " + conferences.get(5).confPrestige + "\n" +
                conferences.get(6).confName + ":  " + conferences.get(6).confPrestige + "\n" +
                conferences.get(7).confName + ":  " + conferences.get(7).confPrestige + "\n" +
                conferences.get(8).confName + ":  " + conferences.get(8).confPrestige + "\n" +
                conferences.get(9).confName + ":  " + conferences.get(9).confPrestige + "\n");


        //Create new Bowl Game Names from TXT
        try {
            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(new FileReader(customBowl));

            //First ignore the save file info
            line = bufferedReader.readLine();
            line.replaceAll("\"", "\\\"");
            for (int b = 0; b < bowlNames.length; ++b) {
                String[] filesSplit = line.split(", ");
                bowlNames[b] = filesSplit[b].toString();
            }

        } catch (FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file");
        } catch (IOException ex) {
            System.out.println(
                    "Error reading file");
        }

    }


    /* LOAD A SAVE FILE
     * Create League from saved file.
     * @param saveFile file that league is saved in
     */
    public League(File saveFile, String namesCSV, String lastNamesCSV) {
        heismanDecided = false;
        hasScheduledBowls = false;
        bowlGames = new Game[countBG];
        // This will reference one line at a time
        String line = null;
        currentWeek = 0;

        leagueRecords = new LeagueRecords();
        userTeamRecords = new LeagueRecords();
        UserHistory = new ArrayList<>();
        longestWinStreak = new TeamStreak(seasonStart, seasonStart, 0, "XXX");
        yearStartLongestWinStreak = new TeamStreak(seasonStart, seasonStart, 0, "XXX");
        longestActiveWinStreak = new TeamStreak(seasonStart, seasonStart, 0, "XXX");
        coachList = new ArrayList<>();
        coachPrevTeam = new ArrayList<>();
        coachStarList = new ArrayList<>();
        coachStarPrevTeam = new ArrayList<>();

        try {
            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(new FileReader(saveFile));

            //First ignore the save file info
            line = bufferedReader.readLine();
            // Game Mode
            careerMode = line.substring(line.length() - 9, line.length()).equals("[CAREER]%");

            //Next get league history
            leagueHistory = new ArrayList<String[]>();
            while ((line = bufferedReader.readLine()) != null && !line.equals("END_LEAGUE_HIST")) {
                leagueHistory.add(line.split("%"));
            }

            //Next get heismans
            heismanHistory = new ArrayList<String>();
            while ((line = bufferedReader.readLine()) != null && !line.equals("END_HEISMAN_HIST")) {
                heismanHistory.add(line);
            }

            //Next make all the conferences & teams
            conferences = new ArrayList<Conference>();
            teamList = new ArrayList<Team>();

            while ((line = bufferedReader.readLine()) != null && !line.equals("END_CONFERENCES")) {
                conferences.add(new Conference(line, this));
            }

            allAmericans = new ArrayList<Player>();
            String[] splits;
            for (int i = 0; i < countTeam; ++i) { //Do for every team
                StringBuilder sbTeam = new StringBuilder();
                while ((line = bufferedReader.readLine()) != null && !line.equals("END_PLAYERS")) {
                    sbTeam.append(line);
                }
                Team t = new Team(sbTeam.toString(), this);
                conferences.get(getConfNumber(t.conference)).confTeams.add(t);
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
            while ((line = bufferedReader.readLine()) != null && !line.equals("END_USER_TEAM")) {
                UserHistory.add(line);
            }

            while ((line = bufferedReader.readLine()) != null && !line.equals("END_TEAM_HISTORY")) {
                for (int i = 0; i < countTeam; ++i) { //Do for every team
                    while ((line = bufferedReader.readLine()) != null && !line.equals("END_TEAM")) {
                        teamList.get(i).teamHistory.add(line);
                    }
                }
            }

            // Set up lucky and penalized teams for Week 0 news stories
            StringBuilder sbLucky = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null && !line.equals("END_LUCKY_TEAM")) {
                sbLucky.append(line);
            }
            if (!sbLucky.toString().equals("NULL")) {
                saveLucky = findTeamAbbr(sbLucky.toString());
                saveLucky.sortPlayers();
                findTeamAbbr(saveLucky.rivalTeam).sortPlayers();
            } else {
                saveLucky = null;
            }

            StringBuilder sbLucky2 = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null && !line.equals("END_LUCKY2_TEAM")) {
                sbLucky2.append(line);
            }
            if (!sbLucky2.toString().equals("NULL")) {
                saveLucky2 = findTeamAbbr(sbLucky2.toString());
                saveLucky2.sortPlayers();
                findTeamAbbr(saveLucky2.rivalTeam).sortPlayers();
            } else {
                saveLucky2 = null;
            }

            StringBuilder sbLucky3 = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null && !line.equals("END_LUCKY3_TEAM")) {
                sbLucky3.append(line);
            }
            if (!sbLucky3.toString().equals("NULL")) {
                saveLucky3 = findTeamAbbr(sbLucky3.toString());
                saveLucky3.sortPlayers();
                findTeamAbbr(saveLucky3.rivalTeam).sortPlayers();
            } else {
                saveLucky3 = null;
            }


            StringBuilder sbPenalized = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null && !line.equals("END_PENALIZED_TEAM")) {
                sbPenalized.append(line);
            }
            if (!sbPenalized.toString().equals("NULL")) {
                savePenalized = findTeamAbbr(sbPenalized.toString());
                savePenalized.sortPlayers();
                findTeamAbbr(savePenalized.rivalTeam).sortPlayers();
            } else {
                savePenalized = null;
            }

            StringBuilder sbPenalized2 = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null && !line.equals("END_PENALIZED2_TEAM")) {
                sbPenalized2.append(line);
            }
            if (!sbPenalized2.toString().equals("NULL")) {
                savePenalized2 = findTeamAbbr(sbPenalized2.toString());
                savePenalized2.sortPlayers();
                findTeamAbbr(savePenalized2.rivalTeam).sortPlayers();
            } else {
                savePenalized2 = null;
            }

            StringBuilder sbPenalized3 = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null && !line.equals("END_PENALIZED3_TEAM")) {
                sbPenalized3.append(line);
            }
            if (!sbPenalized3.toString().equals("NULL")) {
                savePenalized3 = findTeamAbbr(sbPenalized3.toString());
                savePenalized3.sortPlayers();
                findTeamAbbr(savePenalized3.rivalTeam).sortPlayers();
            } else {
                savePenalized3 = null;
            }

            StringBuilder sbPenalized4 = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null && !line.equals("END_PENALIZED4_TEAM")) {
                sbPenalized4.append(line);
            }
            if (!sbPenalized4.toString().equals("NULL")) {
                savePenalized4 = findTeamAbbr(sbPenalized4.toString());
                savePenalized4.sortPlayers();
                findTeamAbbr(savePenalized4.rivalTeam).sortPlayers();
            } else {
                savePenalized4 = null;
            }

            StringBuilder sbPenalized5 = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null && !line.equals("END_PENALIZED5_TEAM")) {
                sbPenalized5.append(line);
            }
            if (!sbPenalized5.toString().equals("NULL")) {
                savePenalized5 = findTeamAbbr(sbPenalized5.toString());
                savePenalized5.sortPlayers();
                findTeamAbbr(savePenalized5.rivalTeam).sortPlayers();
            } else {
                savePenalized5 = null;
            }
            while ((line = bufferedReader.readLine()) != null && !line.equals("END_BOWL_NAMES")) {
                for (int b = 0; b < bowlNames.length; ++b) {
                    String[] filesSplit = line.split(",");
                    bowlNames[b] = filesSplit[b];
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
                for (int i = 0; i < countTeam; ++i) { //Do for every team
                    while ((line = bufferedReader.readLine()) != null && !line.equals("END_TEAM")) {
                        record = line.split(",");
                        if (!record[1].equals("-1"))
                            teamList.get(i).teamRecords.checkRecord(record[0], Integer.parseInt(record[1]), record[2], Integer.parseInt(record[3]));
                    }
                }
            }

            while ((line = bufferedReader.readLine()) != null && !line.equals("END_HALL_OF_FAME")) {
                for (int i = 0; i < countTeam; ++i) { //Do for every team
                    while ((line = bufferedReader.readLine()) != null && !line.equals("END_TEAM")) {
                        teamList.get(i).hallOfFame.add(line);
                    }
                }
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
            for (int i = 0; i < conferences.size(); ++i) {
                conferences.get(i).setUpSchedule();
            }

            Random rand = new Random();
            int max = 8;
            int min = 5;
            randgm = rand.nextInt((max - min) + 1) + min;
            int maxc = 1;
            int minc = 0;
            randconf = rand.nextInt((maxc - minc) + 1) + minc;

            for (int i = 0; i < conferences.size(); ++i) {
                conferences.get(i).setUpOOCSchedule();
            }
            for (int i = 0; i < conferences.size(); ++i) {
                conferences.get(i).insertOOCSchedule();
            }

            // Initialize new stories lists
            newsStories = new ArrayList<ArrayList<String>>();
            weeklyScores = new ArrayList<ArrayList<String>>();
            for (int i = 0; i < seasonWeeks; ++i) {
                newsStories.add(new ArrayList<String>());
                weeklyScores.add(new ArrayList<String>());
            }
            newsStories.get(0).add("New Season!>Ready for the new season, coach? Whether the National Championship is " +
                    "on your mind, or just a winning season, good luck!");
            weeklyScores.get(0).add("Scores:>No games this week.");

            newsStories.get(0).add("Conference Prestige>The latest surveys are in. The " + getYear() + " prestige ratings for each conference are:\n\n" +
                    conferences.get(0).confName + ":  " + conferences.get(0).confPrestige + "\n" +
                    conferences.get(1).confName + ":  " + conferences.get(1).confPrestige + "\n" +
                    conferences.get(2).confName + ":  " + conferences.get(2).confPrestige + "\n" +
                    conferences.get(3).confName + ":  " + conferences.get(3).confPrestige + "\n" +
                    conferences.get(4).confName + ":  " + conferences.get(4).confPrestige + "\n" +
                    conferences.get(5).confName + ":  " + conferences.get(5).confPrestige + "\n" +
                    conferences.get(6).confName + ":  " + conferences.get(6).confPrestige + "\n" +
                    conferences.get(7).confName + ":  " + conferences.get(7).confPrestige + "\n" +
                    conferences.get(8).confName + ":  " + conferences.get(8).confPrestige + "\n" +
                    conferences.get(9).confName + ":  " + conferences.get(9).confPrestige + "\n");

            if (saveLucky != null || saveLucky2 != null || saveLucky3 != null || saveLucky4 != null || saveLucky5 != null) {
                newsStories.get(0).add("Off-Season Coaching Hires:>New Coaching hires at the following schools will add new look and will hopefully bring more prestige to the university: \n\n" +
                        saveLucky.name + ": " + getRandName() + "\n" + saveLucky2.name + ": " + getRandName() + "\n" + saveLucky3.name + ": " + getRandName() + "\n" + saveLucky4.name + ": " + getRandName() + "\n" + saveLucky5.name + ": " + getRandName() + "\n");
            }
            if (savePenalized != null) {
                newsStories.get(0).add("Major Infraction: " + savePenalized.name + ">An administrative probe has determined that booster " + savePenalized.HC.get(0).name +
                        " has tampered with several recruits. In addition, academic records at  " + savePenalized.name + " have been suspect over the past couple years. The team will ineligible for bowl games or the playoffs this season. The team prestige has dropped and recruiting will be more challenging.");
            }
            if (savePenalized2 != null) {
                newsStories.get(0).add("Minor Infraction: " + savePenalized2.name + ">Investigations have led to the discovery that " + savePenalized2.name + "'s head coach " + savePenalized2.HC.get(0).name +
                        " was found violating recruiting policies over the past off-season. The team will ineligible for bowl games or the playoffs this season. The team prestige has dropped.");
            }
            if (savePenalized3 != null) {
                newsStories.get(0).add("Incidental Infraction: " + savePenalized3.name + ">Several players from " + savePenalized3.name + " were arrested in non-football related activities this past off-season. The team prestige has dropped.");
            }
            if (savePenalized4 != null) {
                newsStories.get(0).add("Incidental Infraction: " + savePenalized4.name + ">An independent investigation determined " + savePenalized4.name + " assistant " + getRandName() + " violated recruiting regulations during the off-season. The team prestige has dropped.");
            }
            if (savePenalized5 != null) {
                newsStories.get(0).add("Incidental Infraction: " + savePenalized5.name + ">Newspapers are reporting " + savePenalized5.name + "'s head recruiter " + getRandName() + " contacted several recruits during a recruiting dead period. The team prestige has dropped.");
            }


        } catch (FileNotFoundException ex) {
            System.out.println(
                    "Unable to open file");
        } catch (IOException ex) {
            System.out.println(
                    "Error reading file");
        }
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

    /**
     * Get conference nmber from string
     *
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

        if (currentWeek <= 12) {
            for (int i = 0; i < conferences.size(); ++i) {
                conferences.get(i).playWeek();
            }
        }

        if (currentWeek == 12) {
            //bowl week
            for (int i = 0; i < teamList.size(); ++i) {
                teamList.get(i).updatePollScore();
            }
            Collections.sort(teamList, new TeamCompPoll());

            schedBowlGames();
        } else if (currentWeek == 13) {
            ArrayList<Player> heismans = getHeisman();
            heismanHistory.add(heismans.get(0).position + " " + heismans.get(0).getInitialName() + " [" + heismans.get(0).getYrStr() + "], "
                    + heismans.get(0).team.abbr + " (" + heismans.get(0).team.wins + "-" + heismans.get(0).team.losses + ")");
            playBowlGames();
        } else if (currentWeek == 14) {
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
                ncg.awayTeam.HC.get(0).bowlwins++;
                ncg.awayTeam.HC.get(0).natchamp++;
                ncg.homeTeam.HC.get(0).bowllosses++;
                newsStories.get(15).add(
                        ncg.awayTeam.name + " wins the National Championship!>" +
                                ncg.awayTeam.strRep() + " defeats " + ncg.homeTeam.strRep() +
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

        //focus on the best player at a position this year each week
        playerSpotlight();

        coachingHotSeat();


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
            //bowl ban!
            if (savePenalized != null && Objects.equals(teamList.get(i).abbr, savePenalized.abbr)) {
                teamList.get(i).teamPollScore = 0;
            }
            if (savePenalized2 != null && Objects.equals(teamList.get(i).abbr, savePenalized2.abbr)) {
                teamList.get(i).teamPollScore = 0;
            }
            //minimum wins for bowl eligibility
            if (teamList.get(i).wins < 7) {
                teamList.get(i).teamPollScore = 0;
            }
        }
        Collections.sort(teamList, new TeamCompPoll());

        //semifinals
        semiG14 = new Game(teamList.get(0), teamList.get(3), "Semis, 1v4");
        teamList.get(0).gameSchedule.add(semiG14);
        teamList.get(3).gameSchedule.add(semiG14);

        semiG23 = new Game(teamList.get(1), teamList.get(2), "Semis, 2v3");
        teamList.get(1).gameSchedule.add(semiG23);
        teamList.get(2).gameSchedule.add(semiG23);

        newsStories.get(currentWeek + 1).add("Playoff Teams Announced!>" + teamList.get(0).getStrAbbrWL() + " will play " + teamList.get(3).getStrAbbrWL() +
                " , while " + teamList.get(1).getStrAbbrWL() + " will play " + teamList.get(2).getStrAbbrWL() + " in next week's College Football Playoff semi-final round. The winners will compete for this year's National Title!");

        //other bowl games

        bowlGames[0] = new Game(teamList.get(4), teamList.get(6), bowlNames[0]);
        teamList.get(4).gameSchedule.add(bowlGames[0]);
        teamList.get(6).gameSchedule.add(bowlGames[0]);

        newsStories.get(currentWeek + 1).add(bowlGames[0].gameName + " Selection Announced:>" + teamList.get(4).strRankTeamRecord() + " will play " + teamList.get(6).strRankTeamRecord() +
                " in next week's bowl game. ");

        bowlGames[1] = new Game(teamList.get(5), teamList.get(7), bowlNames[1]);
        teamList.get(5).gameSchedule.add(bowlGames[1]);
        teamList.get(7).gameSchedule.add(bowlGames[1]);

        newsStories.get(currentWeek + 1).add(bowlGames[1].gameName + " Selection Announced:>" + teamList.get(5).strRankTeamRecord() + " will play " + teamList.get(7).strRankTeamRecord() +
                " in next week's bowl game. ");

        bowlGames[2] = new Game(teamList.get(8), teamList.get(14), bowlNames[2]);
        teamList.get(8).gameSchedule.add(bowlGames[2]);
        teamList.get(14).gameSchedule.add(bowlGames[2]);

        newsStories.get(currentWeek + 1).add(bowlGames[2].gameName + " Selection Announced:>" + teamList.get(8).strRankTeamRecord() + " will play " + teamList.get(14).strRankTeamRecord() +
                " in next week's bowl game. ");

        bowlGames[3] = new Game(teamList.get(9), teamList.get(15), bowlNames[3]);
        teamList.get(9).gameSchedule.add(bowlGames[3]);
        teamList.get(15).gameSchedule.add(bowlGames[3]);

        newsStories.get(currentWeek + 1).add(bowlGames[3].gameName + " Selection Announced:>" + teamList.get(9).strRankTeamRecord() + " will play " + teamList.get(15).strRankTeamRecord() +
                " in next week's bowl game. ");

        bowlGames[4] = new Game(teamList.get(10), teamList.get(11), bowlNames[4]);
        teamList.get(10).gameSchedule.add(bowlGames[4]);
        teamList.get(11).gameSchedule.add(bowlGames[4]);

        newsStories.get(currentWeek + 1).add(bowlGames[4].gameName + " Selection Announced:>" + teamList.get(10).strRankTeamRecord() + " will play " + teamList.get(11).strRankTeamRecord() +
                " in next week's bowl game. ");

        bowlGames[5] = new Game(teamList.get(12), teamList.get(13), bowlNames[5]);
        teamList.get(12).gameSchedule.add(bowlGames[5]);
        teamList.get(13).gameSchedule.add(bowlGames[5]);

        newsStories.get(currentWeek + 1).add(bowlGames[5].gameName + " Selection Announced:>" + teamList.get(12).strRankTeamRecord() + " will play " + teamList.get(13).strRankTeamRecord() +
                " in next week's bowl game. ");

        bowlGames[6] = new Game(teamList.get(16), teamList.get(20), bowlNames[6]);
        teamList.get(16).gameSchedule.add(bowlGames[6]);
        teamList.get(20).gameSchedule.add(bowlGames[6]);

        newsStories.get(currentWeek + 1).add(bowlGames[6].gameName + " Selection Announced:>" + teamList.get(16).strRankTeamRecord() + " will play " + teamList.get(20).strRankTeamRecord() +
                " in next week's bowl game. ");

        bowlGames[7] = new Game(teamList.get(17), teamList.get(21), bowlNames[7]);
        teamList.get(17).gameSchedule.add(bowlGames[7]);
        teamList.get(21).gameSchedule.add(bowlGames[7]);

        newsStories.get(currentWeek + 1).add(bowlGames[7].gameName + " Selection Announced:>" + teamList.get(17).strRankTeamRecord() + " will play " + teamList.get(21).strRankTeamRecord() +
                " in next week's bowl game. ");

        bowlGames[8] = new Game(teamList.get(18), teamList.get(22), bowlNames[8]);
        teamList.get(18).gameSchedule.add(bowlGames[8]);
        teamList.get(22).gameSchedule.add(bowlGames[8]);

        newsStories.get(currentWeek + 1).add(bowlGames[8].gameName + " Selection Announced:>" + teamList.get(18).strRankTeamRecord() + " will play " + teamList.get(22).strRankTeamRecord() +
                " in next week's bowl game. ");

        bowlGames[9] = new Game(teamList.get(19), teamList.get(23), bowlNames[9]);
        teamList.get(19).gameSchedule.add(bowlGames[9]);
        teamList.get(23).gameSchedule.add(bowlGames[9]);

        newsStories.get(currentWeek + 1).add(bowlGames[9].gameName + " Selection Announced:>" + teamList.get(19).strRankTeamRecord() + " will play " + teamList.get(23).strRankTeamRecord() +
                " in next week's bowl game. ");

        bowlGames[10] = new Game(teamList.get(24), teamList.get(28), bowlNames[10]);
        teamList.get(24).gameSchedule.add(bowlGames[10]);
        teamList.get(28).gameSchedule.add(bowlGames[10]);

        newsStories.get(currentWeek + 1).add(bowlGames[10].gameName + " Selection Announced:>" + teamList.get(24).strRankTeamRecord() + " will play " + teamList.get(28).strRankTeamRecord() +
                " in next week's bowl game. ");

        bowlGames[11] = new Game(teamList.get(25), teamList.get(30), bowlNames[11]);
        teamList.get(25).gameSchedule.add(bowlGames[11]);
        teamList.get(30).gameSchedule.add(bowlGames[11]);

        newsStories.get(currentWeek + 1).add(bowlGames[11].gameName + " Selection Announced:>" + teamList.get(25).strRankTeamRecord() + " will play " + teamList.get(30).strRankTeamRecord() +
                " in next week's bowl game. ");

        bowlGames[12] = new Game(teamList.get(26), teamList.get(29), bowlNames[12]);
        teamList.get(26).gameSchedule.add(bowlGames[12]);
        teamList.get(29).gameSchedule.add(bowlGames[12]);

        newsStories.get(currentWeek + 1).add(bowlGames[12].gameName + " Selection Announced:>" + teamList.get(26).strRankTeamRecord() + " will play " + teamList.get(29).strRankTeamRecord() +
                " in next week's bowl game. ");

        bowlGames[13] = new Game(teamList.get(27), teamList.get(31), bowlNames[13]);
        teamList.get(27).gameSchedule.add(bowlGames[13]);
        teamList.get(31).gameSchedule.add(bowlGames[13]);
        hasScheduledBowls = true;

        newsStories.get(currentWeek + 1).add(bowlGames[13].gameName + " Selection Announced:>" + teamList.get(27).strRankTeamRecord() + " will play " + teamList.get(31).strRankTeamRecord() +
                " in next week's bowl game. ");

        bowlGames[14] = new Game(teamList.get(32), teamList.get(36), bowlNames[14]);
        teamList.get(32).gameSchedule.add(bowlGames[14]);
        teamList.get(36).gameSchedule.add(bowlGames[14]);

        newsStories.get(currentWeek + 1).add(bowlGames[14].gameName + " Selection Announced:>" + teamList.get(32).strRankTeamRecord() + " will play " + teamList.get(36).strRankTeamRecord() +
                " in next week's bowl game. ");

        bowlGames[15] = new Game(teamList.get(33), teamList.get(38), bowlNames[15]);
        teamList.get(33).gameSchedule.add(bowlGames[15]);
        teamList.get(38).gameSchedule.add(bowlGames[15]);

        newsStories.get(currentWeek + 1).add(bowlGames[15].gameName + " Selection Announced:>" + teamList.get(33).strRankTeamRecord() + " will play " + teamList.get(38).strRankTeamRecord() +
                " in next week's bowl game. ");

        bowlGames[16] = new Game(teamList.get(34), teamList.get(37), bowlNames[16]);
        teamList.get(34).gameSchedule.add(bowlGames[16]);
        teamList.get(37).gameSchedule.add(bowlGames[16]);

        newsStories.get(currentWeek + 1).add(bowlGames[16].gameName + " Selection Announced:>" + teamList.get(34).strRankTeamRecord() + " will play " + teamList.get(37).strRankTeamRecord() +
                " in next week's bowl game. ");

        bowlGames[17] = new Game(teamList.get(35), teamList.get(39), bowlNames[17]);
        teamList.get(35).gameSchedule.add(bowlGames[17]);
        teamList.get(39).gameSchedule.add(bowlGames[17]);

        newsStories.get(currentWeek + 1).add(bowlGames[17].gameName + " Selection Announced:>" + teamList.get(35).strRankTeamRecord() + " will play " + teamList.get(39).strRankTeamRecord() +
                " in next week's bowl game. ");

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
        if (semiG14.homeScore > semiG14.awayScore) {
            semiG14.homeTeam.semiFinalWL = "SFW";
            semiG14.awayTeam.semiFinalWL = "SFL";
            semiG14.awayTeam.totalBowlLosses++;
            semiG14.homeTeam.totalBowls++;
            semiG14.homeTeam.HC.get(0).bowlwins++;
            semiG14.awayTeam.HC.get(0).bowllosses++;
            semi14winner = semiG14.homeTeam;
            newsStories.get(14).add(
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
            newsStories.get(14).add(
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
            newsStories.get(14).add(
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
            newsStories.get(14).add(
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
    }

    /**
     * Plays a particular bowl game
     *
     * @param g bowl game to be played
     */
    private void playBowl(Game g) {
        g.playGame();
        if (g.homeScore > g.awayScore) {
            g.homeTeam.semiFinalWL = "BW";
            g.awayTeam.semiFinalWL = "BL";
            g.homeTeam.totalBowls++;
            g.awayTeam.totalBowlLosses++;
            g.homeTeam.HC.get(0).bowlwins++;
            g.awayTeam.HC.get(0).bowllosses++;
            newsStories.get(14).add(
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
            newsStories.get(14).add(
                    g.awayTeam.name + " wins the " + g.gameName + "!>" +
                            g.awayTeam.strRep() + " defeats " + g.homeTeam.strRep() +
                            " in the " + g.gameName + ", winning " + g.awayScore + " to " + g.homeScore + "."
            );
        }
    }

    /**
     * At the end of the year, record the top 25 teams for the League's History.
     */
    public void updateLeagueHistory() {
        //update league history
        Collections.sort(teamList, new TeamCompPoll());
        String[] yearTop25 = new String[25];
        Team tt;
        for (int i = 0; i < 25; ++i) {
            tt = teamList.get(i);
            yearTop25[i] = tt.name + " (" + tt.wins + "-" + tt.losses + ")";
        }
        leagueHistory.add(yearTop25);
    }


    public void advanceHC() {
        coachList.clear();
        coachPrevTeam.clear();
        coachStarList.clear();
        coachStarPrevTeam.clear();
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).advanceHC();
        }
    }

    //Advances season for each team and sets up schedules for the new year.

    public void advanceSeason() {
        currentWeek = 0;
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).advanceSeason();
        }


        //COACH HIRES & INFRACTIONS

/*        // Hire Coach for avg team
        int luckyNumber = (int)(Math.random()*21);
        Team luckyTeam = teamList.get(35 + luckyNumber);
        int coach = (int)(Math.random()*17);
        luckyTeam.teamPrestige += coach + 4;
        saveLucky = luckyTeam;
        if (luckyTeam.teamPrestige > 99) {luckyTeam.teamPrestige = 99;}

        // Hire Coach for medicore team
        luckyNumber = (int)(Math.random()*20);
        Team luckyTeam2 = teamList.get(56 + luckyNumber);
        coach = (int)(Math.random()*19);
        luckyTeam2.teamPrestige += coach + 4;
        saveLucky2 = luckyTeam2;
        if (luckyTeam2.teamPrestige > 99) {luckyTeam2.teamPrestige = 99;}

        // Hire Coach for very bad team
        luckyNumber = (int)(Math.random()*25);
        if (luckyNumber > 20) luckyNumber = 20;
        Team luckyTeam3 = teamList.get(75 + luckyNumber);
        coach = (int)(Math.random()*20);
        luckyTeam3.teamPrestige += coach + 4;
        saveLucky3 = luckyTeam3;
        if (luckyTeam3.teamPrestige > 99) {luckyTeam3.teamPrestige = 99;}

        // Hire Coach for very bad team
        luckyNumber = (int)(Math.random()*12);
        if (luckyNumber > 10) luckyNumber = 10;
        Team luckyTeam4 = teamList.get(98 + luckyNumber);
        coach = (int)(Math.random()*21);
        luckyTeam4.teamPrestige += coach + 4;
        saveLucky4 = luckyTeam4;
        if (luckyTeam4.teamPrestige > 99) {luckyTeam4.teamPrestige = 99;}

        // Hire Coach for very bad team
        luckyNumber = (int)(Math.random()*12);
        if (luckyNumber > 10) luckyNumber = 10;
        Team luckyTeam5 = teamList.get(109 + luckyNumber);
        coach = (int)(Math.random()*23);
        luckyTeam5.teamPrestige += coach + 4;
        saveLucky5 = luckyTeam5;
        if (luckyTeam5.teamPrestige > 99) {luckyTeam5.teamPrestige = 99;}*/

        saveLucky = null;
        saveLucky2 = null;
        saveLucky3 = null;
        saveLucky4 = null;
        saveLucky5 = null;

        /// INFRACTIONS TIME

        //Major Infraction to a good team
        int x = (int) (Math.random() * 11);
        if (x > 7) {
            int penalizedNumber = (int) (Math.random() * 25);
            Team penalizedTeam = teamList.get(penalizedNumber);
            if (penalizedTeam.teamPrestige > 70 && penalizedTeam.HC.get(0).ratDiscipline < (Math.random() * 100)) {
                penalizedTeam.teamPrestige -= (25 + x);
                savePenalized = penalizedTeam;
                penalizedTeam.HC.get(0).ratDiscipline -= 20;
            } else {
                savePenalized = null;
                penalizedTeam.HC.get(0).ratDiscipline += 15;
            }
        } else savePenalized = null;

        //Minor infraction to an avg team
        x = (int) (Math.random() * 11);
        if (x > 7) {
            int penalizedNumber = (int) (Math.random() * 55);
            Team penalizedTeam2 = teamList.get(penalizedNumber);
            if (penalizedTeam2.HC.get(0).ratDiscipline < (Math.random() * 100)) {
                penalizedTeam2.teamPrestige -= (10 + x);
                savePenalized2 = penalizedTeam2;
                penalizedTeam2.HC.get(0).ratDiscipline -= 10;
            } else {
                savePenalized2 = null;
                penalizedTeam2.HC.get(0).ratDiscipline += 7;
            }
        } else savePenalized2 = null;

        //Discipline a team
        x = (int) (Math.random() * 11);
        if (x > 6) {
            int penalizedNumber = (int) (Math.random() * 60);
            Team penalizedTeam3 = teamList.get(penalizedNumber);
            if (penalizedTeam3.HC.get(0).ratDiscipline < (Math.random() * 100)) {
                penalizedTeam3.teamPrestige -= (6 + x);
                penalizedTeam3.HC.get(0).ratDiscipline -= 8;
                savePenalized3 = penalizedTeam3;
            } else {
                savePenalized3 = null;
                penalizedTeam3.HC.get(0).ratDiscipline += 5;
            }
        } else savePenalized3 = null;

        //Discipline a team
        x = (int) (Math.random() * 11);
        if (x > 5) {
            int penalizedNumber = (int) (Math.random() * 75);
            Team penalizedTeam4 = teamList.get(penalizedNumber);
            if (penalizedTeam4.HC.get(0).ratDiscipline < (Math.random() * 100)) {
                penalizedTeam4.teamPrestige -= (5 + x);
                penalizedTeam4.HC.get(0).ratDiscipline -= 5;
                savePenalized4 = penalizedTeam4;
            } else {
                savePenalized4 = null;
                penalizedTeam4.HC.get(0).ratDiscipline += 5;
            }
        } else savePenalized4 = null;

        //Discipline a team
        x = (int) (Math.random() * 11);
        if (x > 5) {
            int penalizedNumber = (int) (Math.random() * 85);
            Team penalizedTeam5 = teamList.get(penalizedNumber);
            if (penalizedTeam5.HC.get(0).ratDiscipline < (Math.random() * 100)) {
                penalizedTeam5.teamPrestige -= (3 + x);
                penalizedTeam5.HC.get(0).ratDiscipline -= 5;
                savePenalized5 = penalizedTeam5;
            } else {
                savePenalized = null;
                penalizedTeam5.HC.get(0).ratDiscipline += 5;
            }
        } else savePenalized5 = null;

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
     *
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
     * Gets the current year, starting from 2017
     *
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
        for (int i = 0; i < teamList.size(); ++i) {
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

    /**
     * Updates poll scores for each team and updates their ranking.
     */
    public void setTeamRanks() {
        //get team ranks for PPG, YPG, etc
        for (int i = 0; i < teamList.size(); ++i) {
            teamList.get(i).updatePollScore();
        }

        Collections.sort(teamList, new TeamCompPoll());
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).rankTeamPollScore = t + 1;
        }

        for (int i = 0; i < teamList.size(); ++i) {
            teamList.get(i).updateSOS();
        }

        Collections.sort(teamList, new TeamCompSoS());
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).rankTeamSOS = t + 1;
        }

        Collections.sort(teamList, new TeamCompSoW());
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).rankTeamStrengthOfWins = t + 1;
        }

        Collections.sort(teamList, new TeamCompPPG());
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).rankTeamPoints = t + 1;
        }

        Collections.sort(teamList, new TeamCompOPPG());
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).rankTeamOppPoints = t + 1;
        }

        Collections.sort(teamList, new TeamCompYPG());
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).rankTeamYards = t + 1;
        }

        Collections.sort(teamList, new TeamCompOYPG());
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).rankTeamOppYards = t + 1;
        }

        Collections.sort(teamList, new TeamCompPYPG());
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).rankTeamPassYards = t + 1;
        }

        Collections.sort(teamList, new TeamCompRYPG());
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).rankTeamRushYards = t + 1;
        }

        Collections.sort(teamList, new TeamCompOPYPG());
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).rankTeamOppPassYards = t + 1;
        }

        Collections.sort(teamList, new TeamCompORYPG());
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).rankTeamOppRushYards = t + 1;
        }

        Collections.sort(teamList, new TeamCompTODiff());
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).rankTeamTODiff = t + 1;
        }

        Collections.sort(teamList, new TeamCompOffTalent());
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).rankTeamOffTalent = t + 1;
        }

        Collections.sort(teamList, new TeamCompDefTalent());
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).rankTeamDefTalent = t + 1;
        }

        Collections.sort(teamList, new TeamCompPrestige());
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).rankTeamPrestige = t + 1;
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
     *
     * @return Heisman Winner
     */
    public ArrayList<Player> getHeisman() {
        heisman = null;
        int heismanScore = 0;
        int tempScore = 0;
        ArrayList<Player> heismanCandidates = new ArrayList<Player>();
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
        Collections.sort(heismanCandidates, new PlayerHeismanComp());

        return heismanCandidates;
    }

    /**
     * Get string of the top 5 heisman candidates. If the heisman is already decided, get the ceremony str.
     *
     * @return string of top 5 players and their stats
     */
    public String getTop5HeismanStr() {
        if (heismanDecided) {
            return getHeismanCeremonyStr();
        } else {
            ArrayList<Player> heismanCandidates = getHeisman();
            //full results string
            String heismanTop5 = "";
            for (int i = 0; i < 10; ++i) {
                Player p = heismanCandidates.get(i);
                heismanTop5 += (i + 1) + ". " + p.team.abbr + "(" + p.team.wins + "-" + p.team.losses + ")" + " - ";
                if (p instanceof PlayerQB) {
                    PlayerQB pqb = (PlayerQB) p;
                    heismanTop5 += " QB " + pqb.name + " [" + pqb.getYrStr() +
                            "]\n \t\t(" + pqb.statsPassTD + " TDs, " + pqb.statsInt + " Int, " + pqb.statsPassYards + " Yds, "
                            + pqb.statsRushTD + " TDs, " + pqb.statsFumbles + " Fum, " + pqb.statsRushYards + " Yds)\n\n";
                } else if (p instanceof PlayerRB) {
                    PlayerRB prb = (PlayerRB) p;
                    heismanTop5 += " RB " + prb.name + " [" + prb.getYrStr() +
                            "]\n \t\t(" + prb.statsRushTD + " TDs, " + prb.statsFumbles + " Fum, " + prb.statsRushYards + " Yds)\n\n";
                } else if (p instanceof PlayerWR) {
                    PlayerWR pwr = (PlayerWR) p;
                    heismanTop5 += " WR " + pwr.name + " [" + pwr.getYrStr() +
                            "]\n \t\t(" + pwr.statsTD + " TDs, " + pwr.statsFumbles + " Fum, " + pwr.statsRecYards + " Yds)\n\n";
                } else if (p instanceof PlayerTE) {
                    PlayerTE pte = (PlayerTE) p;
                    heismanTop5 += " WR " + pte.name + " [" + pte.getYrStr() +
                            "]\n \t\t(" + pte.statsRecTD + " TDs, " + pte.statsFumbles + " Fum, " + pte.statsRecYards + " Yds)\n\n";
                } else if (p instanceof PlayerDL) {
                    PlayerDL pdl = (PlayerDL) p;
                    heismanTop5 += " DL " + pdl.name + " [" + pdl.getYrStr() +
                            "]\n \t\t(" + pdl.statsTackles + " Tkl, " + pdl.statsSacks + " Sacks, " + pdl.statsFumbles + " Fum)\n\n";
                } else if (p instanceof PlayerLB) {
                    PlayerLB plb = (PlayerLB) p;
                    heismanTop5 += " LB " + plb.name + " [" + plb.getYrStr() +
                            "]\n \t\t(" + plb.statsTackles + " Tkl, " + plb.statsFumbles + " Fum, " + plb.statsInts + " Int)\n\n";
                } else if (p instanceof PlayerCB) {
                    PlayerCB pcb = (PlayerCB) p;
                    heismanTop5 += " CB " + pcb.name + " [" + pcb.getYrStr() +
                            "]\n \t\t(" + pcb.statsTackles + " Tkl, " + pcb.statsDefended + " Def, " + pcb.statsInts + " Int)\n\n";
                } else if (p instanceof PlayerS) {
                    PlayerS ps = (PlayerS) p;
                    heismanTop5 += " S " + ps.name + " [" + ps.getYrStr() +
                            "]\n \t\t(" + ps.statsTackles + " Tkl, " + ps.statsFumbles + " Fum, " + ps.statsInts + " Int)\n\n";
                }

            }
            return heismanTop5;
        }
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
                heismanTop5 += (i + 1) + ". " + p.team.abbr + "(" + p.team.wins + "-" + p.team.losses + ")" + " - ";
                if (p instanceof PlayerQB) {
                    PlayerQB pqb = (PlayerQB) p;
                    heismanTop5 += " QB " + pqb.getInitialName() + ": " + p.getHeismanScore() + " votes\n\t("
                            + pqb.statsPassTD + " TDs, " + pqb.statsInt + " Int, " + pqb.statsPassYards + " Yds, "
                            + pqb.statsRushTD + " TDs, " + pqb.statsFumbles + " Fum, " + pqb.statsRushYards + " Yds)\n\n";
                } else if (p instanceof PlayerRB) {
                    PlayerRB prb = (PlayerRB) p;
                    heismanTop5 += " RB " + prb.getInitialName() + ": " + p.getHeismanScore() + " votes\n\t("
                            + prb.statsRushTD + " TDs, " + prb.statsFumbles + " Fum, " + prb.statsRushYards + " Yds)\n\n";
                } else if (p instanceof PlayerWR) {
                    PlayerWR pwr = (PlayerWR) p;
                    heismanTop5 += " WR " + pwr.getInitialName() + ": " + p.getHeismanScore() + " votes\n\t("
                            + pwr.statsTD + " TDs, " + pwr.statsFumbles + " Fum, " + pwr.statsRecYards + " Yds)\n\n";
                } else if (p instanceof PlayerTE) {
                    PlayerTE pte = (PlayerTE) p;
                    heismanTop5 += " TE " + pte.getInitialName() + ": " + p.getHeismanScore() + " votes\n\t("
                            + pte.statsRecTD + " TDs, " + pte.statsFumbles + " Fum, " + pte.statsRecYards + " Yds)\n\n";
                } else if (p instanceof PlayerDL) {
                    PlayerDL pdl = (PlayerDL) p;
                    heismanTop5 += " DL " + pdl.getInitialName() + ": " + p.getHeismanScore() + " votes\n\t("
                            + pdl.statsTackles + " Tkl, " + pdl.statsSacks + " Sacks, " + pdl.statsFumbles + " Fum)\n\n";
                } else if (p instanceof PlayerLB) {
                    PlayerLB plb = (PlayerLB) p;
                    heismanTop5 += " LB " + plb.getInitialName() + ": " + p.getHeismanScore() + " votes\n\t("
                            + plb.statsTackles + " Tkl, " + plb.statsFumbles + " Fum, " + plb.statsInts + " Int)\n\n";
                } else if (p instanceof PlayerCB) {
                    PlayerCB pcb = (PlayerCB) p;
                    heismanTop5 += " CB " + pcb.getInitialName() + ": " + p.getHeismanScore() + " votes\n\t("
                            + pcb.statsTackles + " Tkl, " + pcb.statsDefended + " Def, " + pcb.statsInts + " Int)\n\n";
                } else if (p instanceof PlayerS) {
                    PlayerS ps = (PlayerS) p;
                    heismanTop5 += " S " + ps.getInitialName() + ": " + p.getHeismanScore() + " votes\n\t("
                            + ps.statsTackles + " Tkl, " + ps.statsFumbles + " Fum, " + ps.statsInts + " Int)\n\n";
                }
            }

            String heismanStats = "";
            String heismanWinnerStr = "";
            if (heisman instanceof PlayerQB) {
                //qb heisman
                PlayerQB heisQB = (PlayerQB) heisman;
                heismanWinnerStr = "Congratulations to the Player of the Year, " + heisQB.team.abbr +
                        " QB " + heisQB.name + " [" + heisman.getYrStr() + "], who had " +
                        heisQB.statsPassTD + " TDs, just " + heisQB.statsInt + " interceptions, and " +
                        heisQB.statsPassYards + " passing yards. In addition, he ran for " + heisQB.statsRushYards + "yards and scored " + heisQB.statsRushTD + " touchdowns. He led " + heisQB.team.name +
                        " to a " + heisQB.team.wins + "-" + heisQB.team.losses + " record and a #" + heisQB.team.rankTeamPollScore +
                        " poll ranking.";
                heismanStats = heismanWinnerStr + "\n\nFull Results:" + heismanTop5;
            } else if (heisman instanceof PlayerRB) {
                //rb heisman
                PlayerRB heisRB = (PlayerRB) heisman;
                heismanWinnerStr = "Congratulations to the Player of the Year, " + heisRB.team.abbr +
                        " RB " + heisRB.name + " [" + heisman.getYrStr() + "], who had " +
                        heisRB.statsRushTD + " TDs, just " + heisRB.statsFumbles + " fumbles, and " +
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
            } else if (heisman instanceof PlayerTE) {
                //te heisman
                PlayerTE heisTE = (PlayerTE) heisman;
                heismanWinnerStr = "Congratulations to the Player of the Year, " + heisTE.team.abbr +
                        " TE " + heisTE.name + " [" + heisman.getYrStr() + "], who had " +
                        heisTE.statsRecTD + " TDs, just " + heisTE.statsFumbles + " fumbles, and " +
                        heisTE.statsRecYards + " receiving yards. He led " + heisTE.team.name +
                        " to a " + heisTE.team.wins + "-" + heisTE.team.losses + " record and a #" + heisTE.team.rankTeamPollScore +
                        " poll ranking.";
                heismanStats = heismanWinnerStr + "\n\nFull Results:" + heismanTop5;
            } else if (heisman instanceof PlayerDL) {
                //dl heisman
                PlayerDL heisDL = (PlayerDL) heisman;
                heismanWinnerStr = "Congratulations to the Player of the Year, " + heisDL.team.abbr +
                        " DL " + heisDL.name + " [" + heisman.getYrStr() + "], who had " +
                        heisDL.statsTackles + " tackles, " + heisDL.statsSacks + " sacks, and forced " + heisDL.statsFumbles + " fumbles. He led " + heisDL.team.name +
                        " to a " + heisDL.team.wins + "-" + heisDL.team.losses + " record and a #" + heisDL.team.rankTeamPollScore +
                        " poll ranking.";
                heismanStats = heismanWinnerStr + "\n\nFull Results:" + heismanTop5;
            } else if (heisman instanceof PlayerLB) {
                //wr heisman
                PlayerLB heisLB = (PlayerLB) heisman;
                heismanWinnerStr = "Congratulations to the Player of the Year, " + heisLB.team.abbr +
                        " LB " + heisLB.name + " [" + heisman.getYrStr() + "], who had " +
                        heisLB.statsTackles + " tackles, " + heisLB.statsSacks + " sacks, and recovered " + heisLB.statsFumbles + " fumbles, and " +
                        heisLB.statsInts + " inteceptions. He led " + heisLB.team.name +
                        " to a " + heisLB.team.wins + "-" + heisLB.team.losses + " record and a #" + heisLB.team.rankTeamPollScore +
                        " poll ranking.";
                heismanStats = heismanWinnerStr + "\n\nFull Results:" + heismanTop5;
            } else if (heisman instanceof PlayerCB) {
                //wr heisman
                PlayerCB heisCB = (PlayerCB) heisman;
                heismanWinnerStr = "Congratulations to the Player of the Year, " + heisCB.team.abbr +
                        " CB " + heisCB.name + " [" + heisman.getYrStr() + "], who had " +
                        heisCB.statsTackles + " tackles, defended " + heisCB.statsDefended + " passes, and " +
                        heisCB.statsInts + " inteceptions. He led " + heisCB.team.name +
                        " to a " + heisCB.team.wins + "-" + heisCB.team.losses + " record and a #" + heisCB.team.rankTeamPollScore +
                        " poll ranking.";
                heismanStats = heismanWinnerStr + "\n\nFull Results:" + heismanTop5;
            } else if (heisman instanceof PlayerS) {
                //wr heisman
                PlayerS heisS = (PlayerS) heisman;
                heismanWinnerStr = "Congratulations to the Player of the Year, " + heisS.team.abbr +
                        " S " + heisS.name + " [" + heisman.getYrStr() + "], who had " +
                        heisS.statsTackles + " tackles, " + heisS.statsSacks + " sacks, and recovered " + heisS.statsFumbles + " fumbles, and " +
                        heisS.statsInts + " inteceptions. He led " + heisS.team.name +
                        " to a " + heisS.team.wins + "-" + heisS.team.losses + " record and a #" + heisS.team.rankTeamPollScore +
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

            Collections.sort(qbs, new PlayerHeismanComp());
            Collections.sort(rbs, new PlayerHeismanComp());
            Collections.sort(wrs, new PlayerHeismanComp());
            Collections.sort(tes, new PlayerHeismanComp());
            Collections.sort(ols, new PlayerHeismanComp());
            Collections.sort(ks, new PlayerHeismanComp());
            Collections.sort(dls, new PlayerHeismanComp());
            Collections.sort(lbs, new PlayerHeismanComp());
            Collections.sort(cbs, new PlayerHeismanComp());
            Collections.sort(ss, new PlayerHeismanComp());

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
            allAmericans.add(ss.get(0));
            ss.get(0).wonAllAmerican = true;
            ss.get(0).team.HC.get(0).allamericans++;
        }

        StringBuilder allAmerican = new StringBuilder();
        for (int i = 0; i < allAmericans.size(); ++i) {
            Player p = allAmericans.get(i);
            allAmerican.append(p.team.abbr + "(" + p.team.wins + "-" + p.team.losses + ")" + " - ");
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
                        pwr.statsTD + " TDs, " + pwr.statsFumbles + " Fum, " + pwr.statsRecYards + " Yds\n");
            } else if (p instanceof PlayerTE) {
                PlayerTE pte = (PlayerTE) p;
                allAmerican.append(" TE " + pte.name + " [" + pte.getYrStr() + "]\n \t\t" +
                        pte.statsRecTD + " TDs, " + pte.statsFumbles + " Fum, " + pte.statsRecYards + " Yds\n");
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
            allAmerican.append(" \t\tOverall: " + p.ratOvr + ", Potential: " + p.getLetterGrade(p.ratPot) + "\n\n>");
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
                sb.append(" HC " + hc.name + "\n \t\tAge: :" + hc.age + " Season " + hc.year + "\n");
            } else if (p instanceof PlayerQB) {
                PlayerQB pqb = (PlayerQB) p;
                sb.append(" QB " + pqb.name + " [" + pqb.getYrStr() + "]\n \t\t" +
                        pqb.statsPassTD + " TDs, " + pqb.statsInt + " Int, " + pqb.statsPassYards + " Pass Yds, " + pqb.statsRushYards + "Rush Yds\n");
            } else if (p instanceof PlayerRB) {
                PlayerRB prb = (PlayerRB) p;
                sb.append(" RB " + prb.name + " [" + prb.getYrStr() + "]\n \t\t" +
                        prb.statsRushTD + " TDs, " + prb.statsFumbles + " Fum, " + prb.statsRushYards + " Yds\n");
            } else if (p instanceof PlayerWR) {
                PlayerWR pwr = (PlayerWR) p;
                sb.append(" WR " + pwr.name + " [" + pwr.getYrStr() + "]\n \t\t" +
                        pwr.statsTD + " TDs, " + pwr.statsFumbles + " Fum, " + pwr.statsRecYards + " Yds\n");
            } else if (p instanceof PlayerTE) {
                PlayerTE pte = (PlayerTE) p;
                sb.append(" TE " + pte.name + " [" + pte.getYrStr() + "]\n \t\t" +
                        pte.statsRecTD + " TDs, " + pte.statsFumbles + " Fum, " + pte.statsRecYards + " Yds\n");
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
     *
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

        String[] nflPlayers = new String[NFLPlayers.size()];
        for (int i = 0; i < nflPlayers.length; ++i) {
            nflPlayers[i] = NFLPlayers.get(i).getMockDraftStr();
        }

        return nflPlayers;
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
        ArrayList<Team> teams = teamList; //(ArrayList<Team>) teamList.clone();
        ArrayList<String> rankings = new ArrayList<String>();
        Team t;
        switch (selection) {
            case 0:
                Collections.sort(teams, new TeamCompPoll());
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i + 1) + "," + t.strRepWithBowlResults() + "," + t.teamPollScore);
                }
                break;
            case 1:
                return getConfStandings();
            case 2:
                Collections.sort(teams, new TeamCompSoS());
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i + 1) + "," + t.strRepWithBowlResults() + "," + t.teamSOS);
                }
                break;
            case 3:
                Collections.sort(teams, new TeamCompSoW());
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i + 1) + "," + t.strRepWithBowlResults() + "," + t.teamStrengthOfWins);
                }
                break;
            case 4:
                Collections.sort(teams, new TeamCompPPG());
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i + 1) + "," + t.strRepWithBowlResults() + "," + (t.teamPoints / t.numGames()));
                }
                break;
            case 5:
                Collections.sort(teams, new TeamCompOPPG());
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i + 1) + "," + t.strRepWithBowlResults() + "," + (t.teamOppPoints / t.numGames()));
                }
                break;
            case 6:
                Collections.sort(teams, new TeamCompYPG());
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i + 1) + "," + t.strRepWithBowlResults() + "," + (t.teamYards / t.numGames()));
                }
                break;
            case 7:
                Collections.sort(teams, new TeamCompOYPG());
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i + 1) + "," + t.strRepWithBowlResults() + "," + (t.teamOppYards / t.numGames()));
                }
                break;
            case 8:
                Collections.sort(teams, new TeamCompPYPG());
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i + 1) + "," + t.strRepWithBowlResults() + "," + (t.teamPassYards / t.numGames()));
                }
                break;
            case 9:
                Collections.sort(teams, new TeamCompRYPG());
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i + 1) + "," + t.strRepWithBowlResults() + "," + (t.teamRushYards / t.numGames()));
                }
                break;
            case 10:
                Collections.sort(teams, new TeamCompOPYPG());
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i + 1) + "," + t.strRepWithBowlResults() + "," + (t.teamOppPassYards / t.numGames()));
                }
                break;
            case 11:
                Collections.sort(teams, new TeamCompORYPG());
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i + 1) + "," + t.strRepWithBowlResults() + "," + (t.teamOppRushYards / t.numGames()));
                }
                break;
            case 12:
                Collections.sort(teams, new TeamCompTODiff());
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    if (t.teamTODiff > 0)
                        rankings.add(t.getRankStrStarUser(i + 1) + "," + t.strRepWithBowlResults() + ",+" + t.teamTODiff);
                    else
                        rankings.add(t.getRankStrStarUser(i + 1) + "," + t.strRepWithBowlResults() + "," + t.teamTODiff);
                }
                break;
            case 13:
                Collections.sort(teams, new TeamCompOffTalent());
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i + 1) + "," + t.strRepWithBowlResults() + "," + t.teamOffTalent);
                }
                break;
            case 14:
                Collections.sort(teams, new TeamCompDefTalent());
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i + 1) + "," + t.strRepWithBowlResults() + "," + t.teamDefTalent);
                }
                break;
            case 15:
                Collections.sort(teams, new TeamCompPrestige());
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i + 1) + "," + t.strRepWithBowlResults() + "," + t.teamPrestige);
                }
                break;
            case 16:
                Collections.sort(teams, new TeamCompRecruitClass());
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i + 1) + "," + t.strRepWithPrestige() + "," + t.getRecruitingClassRat());
                }
                break;
            default:
                Collections.sort(teams, new TeamCompPoll());
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i + 1) + "," + t.strRepWithBowlResults() + "," + t.teamPollScore);
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
            confStandings.add(" ," + c.confName + " Conference, ");
            Team t;
            for (int i = 0; i < confTeams.size(); ++i) {
                t = confTeams.get(i);
                confStandings.add(t.getRankStrStarUser(i + 1) + "," + t.strRepWithBowlResults() + "," + t.getConfWins() + "-" + t.getConfLosses());
            }
            confTeams.clear();
        }
        return confStandings;
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
            hist += "\tPOTY: " + heismanHistory.get(i) + "\n%";
        }
        return hist;
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

    //Get Coaching Job Offers
    public ArrayList<String> getCoachListStr(int rating, int offers) {
        ArrayList<String> teams = new ArrayList<>();
        ArrayList<Team> teamVacancies = new ArrayList<>();
        for (int i = 0; i < teamList.size(); i += offers) {
            if (teamList.get(i).teamPrestige < rating) {
                teamVacancies.add(new Team(teamList.get(i).name, teamList.get(i).abbr, teamList.get(i).conference, teamList.get(i).teamPrestige, teamList.get(i).rivalTeam, this));
                teams.add(new String(teamList.get(i).conference + ":  " + teamList.get(i).name + "  [" + teamList.get(i).teamPrestige + "]"));
            }
        }
        return teams;
    }

    //Get Coach Job Offers List for Team Transfer
    public ArrayList<Team> getCoachList(int rating, int offers) {
        ArrayList<Team> teamVacancies = new ArrayList<>();
        for (int i = 0; i < teamList.size(); i += offers) {
            if (teamList.get(i).teamPrestige < rating) {
                teamVacancies.add(new Team(teamList.get(i).name, teamList.get(i).abbr, teamList.get(i).conference, teamList.get(i).teamPrestige, teamList.get(i).rivalTeam, this));
            }
        }
        return teamVacancies;
    }

    //Get Coaching Job Offers
    public ArrayList<String> getCoachListStrV2(int rating, int offers, String oldTeam) {
        ArrayList<String> teams = new ArrayList<>();
        ArrayList<Team> teamVacancies = new ArrayList<>();
        for (int i = 0; i < teamList.size(); ++i) {
            if (teamList.get(i).teamPrestige < rating && teamList.get(i).HC.isEmpty() && teamList.get(i).name != oldTeam) {
                teamVacancies.add(new Team(teamList.get(i).name, teamList.get(i).abbr, teamList.get(i).conference, teamList.get(i).teamPrestige, teamList.get(i).rivalTeam, this));
                teams.add(new String(teamList.get(i).conference + ":  " + teamList.get(i).name + "  [" + teamList.get(i).teamPrestige + "]"));
            }
        }

        if (teamVacancies.isEmpty()) {
            for (int i = 0; i < teamList.size(); i += offers) {
                if (teamList.get(i).teamPrestige < rating && teamList.get(i).name != oldTeam) {
                    teamVacancies.add(new Team(teamList.get(i).name, teamList.get(i).abbr, teamList.get(i).conference, teamList.get(i).teamPrestige, teamList.get(i).rivalTeam, this));
                    teams.add(new String(teamList.get(i).conference + ":  " + teamList.get(i).name + "  [" + teamList.get(i).teamPrestige + "]"));
                }
            }
        }
        return teams;
    }

    //Get Coach Job Offers List for Team Transfer
    public ArrayList<Team> getCoachListV2(int rating, int offers, String oldTeam) {
        ArrayList<Team> teamVacancies = new ArrayList<>();
        for (int i = 0; i < teamList.size(); ++i) {
            if (teamList.get(i).teamPrestige < rating && teamList.get(i).HC.isEmpty() && teamList.get(i).name != oldTeam) {
                teamVacancies.add(new Team(teamList.get(i).name, teamList.get(i).abbr, teamList.get(i).conference, teamList.get(i).teamPrestige, teamList.get(i).rivalTeam, this));
            }
        }

        if (teamVacancies.isEmpty()) {
            for (int i = 0; i < teamList.size(); i += offers) {
                if (teamList.get(i).teamPrestige < rating && teamList.get(i).name != oldTeam) {
                    teamVacancies.add(new Team(teamList.get(i).name, teamList.get(i).abbr, teamList.get(i).conference, teamList.get(i).teamPrestige, teamList.get(i).rivalTeam, this));
                }
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

    /**
     * Get list of all bowl games and their predicted teams
     *
     * @return string of all the bowls and their predictions
     */
    public String getBowlGameWatchStr() {
        //if bowls arent scheduled yet, give predictions
        if (!hasScheduledBowls) {

            for (int i = 0; i < teamList.size(); ++i) {
                teamList.get(i).updatePollScore();
                if (teamList.get(i) == savePenalized) teamList.get(i).teamPollScore = 0;
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

            sb.append(bowlNames[0] + ":\n\t\t");
            t1 = teamList.get(4);
            t2 = teamList.get(6);
            sb.append(t1.strRep() + " vs " + t2.strRep() + "\n\n");

            sb.append(bowlNames[1] + ":\n\t\t");
            t1 = teamList.get(5);
            t2 = teamList.get(7);
            sb.append(t1.strRep() + " vs " + t2.strRep() + "\n\n");

            sb.append(bowlNames[2] + ":\n\t\t");
            t1 = teamList.get(8);
            t2 = teamList.get(14);
            sb.append(t1.strRep() + " vs " + t2.strRep() + "\n\n");

            sb.append(bowlNames[3] + ":\n\t\t");
            t1 = teamList.get(9);
            t2 = teamList.get(15);
            sb.append(t1.strRep() + " vs " + t2.strRep() + "\n\n");

            sb.append(bowlNames[4] + ":\n\t\t");
            t1 = teamList.get(10);
            t2 = teamList.get(11);
            sb.append(t1.strRep() + " vs " + t2.strRep() + "\n\n");

            sb.append(bowlNames[5] + ":\n\t\t");
            t1 = teamList.get(12);
            t2 = teamList.get(13);
            sb.append(t1.strRep() + " vs " + t2.strRep() + "\n\n");

            for (int i = 6; i < 10; ++i) {
                sb.append(bowlNames[i] + ":\n\t\t");
                t1 = teamList.get(10 + i);
                t2 = teamList.get(14 + i);
                sb.append(t1.strRep() + " vs " + t2.strRep() + "\n\n");
            }

            sb.append(bowlNames[10] + ":\n\t\t");
            t1 = teamList.get(24);
            t2 = teamList.get(28);
            sb.append(t1.strRep() + " vs " + t2.strRep() + "\n\n");

            sb.append(bowlNames[11] + ":\n\t\t");
            t1 = teamList.get(25);
            t2 = teamList.get(30);
            sb.append(t1.strRep() + " vs " + t2.strRep() + "\n\n");

            sb.append(bowlNames[12] + ":\n\t\t");
            t1 = teamList.get(26);
            t2 = teamList.get(29);
            sb.append(t1.strRep() + " vs " + t2.strRep() + "\n\n");

            sb.append(bowlNames[13] + ":\n\t\t");
            t1 = teamList.get(27);
            t2 = teamList.get(31);
            sb.append(t1.strRep() + " vs " + t2.strRep() + "\n\n");

            sb.append(bowlNames[14] + ":\n\t\t");
            t1 = teamList.get(32);
            t2 = teamList.get(36);
            sb.append(t1.strRep() + " vs " + t2.strRep() + "\n\n");

            sb.append(bowlNames[15] + ":\n\t\t");
            t1 = teamList.get(33);
            t2 = teamList.get(38);
            sb.append(t1.strRep() + " vs " + t2.strRep() + "\n\n");

            sb.append(bowlNames[16] + ":\n\t\t");
            t1 = teamList.get(34);
            t2 = teamList.get(37);
            sb.append(t1.strRep() + " vs " + t2.strRep() + "\n\n");

            sb.append(bowlNames[17] + ":\n\t\t");
            t1 = teamList.get(35);
            t2 = teamList.get(39);
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
                sb.append("\n\n" + bowlNames[i] + ":\n");
                sb.append(getGameSummaryBowl(bowlGames[i]));
            }

            return sb.toString();
        }
    }

    /**
     * Get string of what happened in a particular bowl
     *
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

    /**
     * Get summary of what happened in the NCG
     *
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
     *
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
     *
     * @param saveFile file to be overwritten
     * @return true if successful
     */
    public boolean saveLeague(File saveFile) {
        StringBuilder sb = new StringBuilder();

        // Save information about the save file, user team info
        if (isCareerMode()) {
            sb.append((seasonStart + leagueHistory.size()) + ": " + userTeam.abbr + " (" + (userTeam.totalWins - userTeam.wins) + "-" + (userTeam.totalLosses - userTeam.losses) + ") " +
                    userTeam.totalCCs + " CCs, " + userTeam.totalNCs + " NCs>[CAREER]%\n");
        } else {
            sb.append((seasonStart + leagueHistory.size()) + ": " + userTeam.abbr + " (" + (userTeam.totalWins - userTeam.wins) + "-" + (userTeam.totalLosses - userTeam.losses) + ") " +
                    userTeam.totalCCs + " CCs, " + userTeam.totalNCs + " NCs>[DYNASTY]%\n");
        }


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
                    t.yearStartWinStreak.getStreakCSV() + "," + t.teamTVDeal + "," + t.confTVDeal + "%" + t.evenYearHomeOpp + "%\n");
            sb.append(t.getPlayerInfoSaveFile());
            sb.append("END_PLAYERS\n");
        }

        // Save history of the user's team of the W-L and bowl results each year
        sb.append(userTeam.name + "\n");
        for (String s : UserHistory) {
            sb.append(s + "\n");
        }
        sb.append("END_USER_TEAM\n");
        //Every Team Year-by-Year History
        for (Team t : teamList) {
            for (String s : t.teamHistory) {
                sb.append(s + "\n");
            }
            sb.append("END_TEAM\n");
        }
        sb.append("END_TEAM_HISTORY\n");

        // Save who was luckyed and penalizedd this year for news stories the following year
        if (saveLucky != null) {
            sb.append(saveLucky.abbr + "\n");
            sb.append("END_LUCKY_TEAM\n");
        } else {
            sb.append("NULL\n");
            sb.append("END_LUCKY_TEAM\n");
        }
        if (saveLucky2 != null) {
            sb.append(saveLucky2.abbr + "\n");
            sb.append("END_LUCKY2_TEAM\n");
        } else {
            sb.append("NULL\n");
            sb.append("END_LUCKY2_TEAM\n");
        }
        if (saveLucky3 != null) {
            sb.append(saveLucky3.abbr + "\n");
            sb.append("END_LUCKY3_TEAM\n");
        } else {
            sb.append("NULL\n");
            sb.append("END_LUCKY3_TEAM\n");
        }
        if (savePenalized != null) {
            sb.append(savePenalized.abbr + "\n");
            sb.append("END_PENALIZED_TEAM\n");
        } else {
            sb.append("NULL\n");
            sb.append("END_PENALIZED_TEAM\n");
        }
        if (savePenalized2 != null) {
            sb.append(savePenalized2.abbr + "\n");
            sb.append("END_PENALIZED2_TEAM\n");
        } else {
            sb.append("NULL\n");
            sb.append("END_PENALIZED2_TEAM\n");
        }
        if (savePenalized3 != null) {
            sb.append(savePenalized3.abbr + "\n");
            sb.append("END_PENALIZED3_TEAM\n");
        } else {
            sb.append("NULL\n");
            sb.append("END_PENALIZED3_TEAM\n");
        }
        if (savePenalized4 != null) {
            sb.append(savePenalized4.abbr + "\n");
            sb.append("END_PENALIZED4_TEAM\n");
        } else {
            sb.append("NULL\n");
            sb.append("END_PENALIZED4_TEAM\n");
        }
        if (savePenalized5 != null) {
            sb.append(savePenalized5.abbr + "\n");
            sb.append("END_PENALIZED5_TEAM\n");
        } else {
            sb.append("NULL\n");
            sb.append("END_PENALIZED5_TEAM\n");
        }

        for (int b = 0; b < bowlNames.length; ++b) {
            sb.append(bowlNames[b] + ",");
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

        // Save all the Hall of Famers
        for (Team t : teamList) {
            for (String s : t.hallOfFame) {
                sb.append(s + "\n");
                sb.append("END_TEAM\n");
            }
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

    //News on opening weekend
    public void preseasonNews() {
        coachingHotSeat();
        //Add Big Games of the Week
        for (int i = 0; i < conferences.size(); ++i) {
            conferences.get(i).newsNSMatchups();
        }
    }

    //Committee News
    public void cfbPlayoffsNews() {
        setTeamRanks();
        ArrayList<Team> teams = teamList;
        Collections.sort(teams, new TeamCompPoll());

        if (currentWeek == 8) {
            newsStories.get(currentWeek + 1).add("Committee Announces First Playoff Rankings>The College Football Playoffs Committee has set ther initial rankings for this season's playoffs. The first look at the playoffs have " +
                    teams.get(0).name + " at the top of the list. The rest of the playoff order looks like this:\n\n" + "1. " + teams.get(0).getStrAbbrWL() + "\n" + "2. " + teams.get(1).getStrAbbrWL() + "\n" + "3. " +
                    teams.get(2).getStrAbbrWL() + "\n" + "4. " + teams.get(3).getStrAbbrWL() + "\n" + "5. " + teams.get(4).getStrAbbrWL() + "\n" + "6. " + teams.get(5).getStrAbbrWL() + "\n" + "7. " +
                    teams.get(6).getStrAbbrWL() + "\n" + "8. " + teams.get(7).getStrAbbrWL() + "\n");
        }
        if (currentWeek > 8 && currentWeek < 12) {
            newsStories.get(currentWeek + 1).add("Committee Updates Rankings>The College Football Playoff Committee has updated their Playoff Rankings. The order looks like this: \n\n" + "1. " + teams.get(0).getStrAbbrWL() +
                    "\n" + "2. " + teams.get(1).getStrAbbrWL() + "\n" + "3. " + teams.get(2).getStrAbbrWL() + "\n" + "4. " + teams.get(3).getStrAbbrWL() + "\n" + "5. " + teams.get(4).getStrAbbrWL() + "\n" + "6. " +
                    teams.get(5).getStrAbbrWL() + "\n" + "7. " + teams.get(6).getStrAbbrWL() + "\n" + "8. " + teams.get(7).getStrAbbrWL() + "\n");
        }
    }

    //Player Spotlight
    public void playerSpotlight() {
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
                    " to a record of " + WR.get(0).team.strTeamRecord() + ". The receiver has caught " + WR.get(0).statsReceptions + " for " + WR.get(0).statsRecYards + " yards this year. He's found the end zone " + WR.get(0).statsTD +
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


    public ArrayList<PlayerQB> rankQB() {
        heisman = null;
        int heismanScore = 0;
        int tempScore = 0;
        ArrayList<PlayerQB> heismanCandidates = new ArrayList<PlayerQB>();
        for (int i = 0; i < teamList.size(); ++i) {
            for (int qb = 0; qb < teamList.get(i).teamQBs.size(); ++qb) {
                heismanCandidates.add(teamList.get(i).teamQBs.get(qb));
                tempScore = teamList.get(i).teamQBs.get(qb).getHeismanScore() + teamList.get(i).wins * 100 + teamList.get(i).confPrestige * 10;
                if (tempScore > heismanScore) {
                    heisman = teamList.get(i).teamQBs.get(qb);
                    heismanScore = tempScore;
                }
            }
        }
        Collections.sort(heismanCandidates, new PlayerHeismanComp());
        return heismanCandidates;
    }

    public ArrayList<PlayerRB> rankRB() {
        heisman = null;
        int heismanScore = 0;
        int tempScore = 0;
        ArrayList<PlayerRB> heismanCandidates = new ArrayList<PlayerRB>();
        for (int i = 0; i < teamList.size(); ++i) {
            for (int rb = 0; rb < teamList.get(i).teamRBs.size(); ++rb) {
                heismanCandidates.add(teamList.get(i).teamRBs.get(rb));
                tempScore = teamList.get(i).teamRBs.get(rb).getHeismanScore() + teamList.get(i).wins * 100 + teamList.get(i).confPrestige * 10;
                if (tempScore > heismanScore) {
                    heisman = teamList.get(i).teamRBs.get(rb);
                    heismanScore = tempScore;
                }
            }
        }
        Collections.sort(heismanCandidates, new PlayerHeismanComp());
        return heismanCandidates;
    }

    public ArrayList<PlayerWR> rankWR() {
        heisman = null;
        int heismanScore = 0;
        int tempScore = 0;
        ArrayList<PlayerWR> heismanCandidates = new ArrayList<PlayerWR>();
        for (int i = 0; i < teamList.size(); ++i) {
            for (int wr = 0; wr < teamList.get(i).teamWRs.size(); ++wr) {
                heismanCandidates.add(teamList.get(i).teamWRs.get(wr));
                tempScore = teamList.get(i).teamWRs.get(wr).getHeismanScore() + teamList.get(i).wins * 100 + teamList.get(i).confPrestige * 10;
                if (tempScore > heismanScore) {
                    heisman = teamList.get(i).teamWRs.get(wr);
                    heismanScore = tempScore;
                }
            }
        }
        Collections.sort(heismanCandidates, new PlayerHeismanComp());
        return heismanCandidates;
    }


    public ArrayList<PlayerTE> rankTE() {
        heisman = null;
        int heismanScore = 0;
        int tempScore = 0;
        ArrayList<PlayerTE> heismanCandidates = new ArrayList<PlayerTE>();
        for (int i = 0; i < teamList.size(); ++i) {
            for (int te = 0; te < teamList.get(i).teamTEs.size(); ++te) {
                heismanCandidates.add(teamList.get(i).teamTEs.get(te));
                tempScore = teamList.get(i).teamTEs.get(te).getHeismanScore() + teamList.get(i).wins * 100 + teamList.get(i).confPrestige * 10;
                if (tempScore > heismanScore) {
                    heisman = teamList.get(i).teamTEs.get(te);
                    heismanScore = tempScore;
                }
            }
        }
        Collections.sort(heismanCandidates, new PlayerHeismanComp());
        return heismanCandidates;
    }

    public ArrayList<PlayerOL> rankOL() {
        heisman = null;
        int heismanScore = 0;
        int tempScore = 0;
        ArrayList<PlayerOL> heismanCandidates = new ArrayList<PlayerOL>();
        for (int i = 0; i < teamList.size(); ++i) {
            for (int te = 0; te < teamList.get(i).teamOLs.size(); ++te) {
                heismanCandidates.add(teamList.get(i).teamOLs.get(te));
                tempScore = teamList.get(i).teamOLs.get(te).getHeismanScore() + teamList.get(i).wins * 100 + teamList.get(i).confPrestige * 10;
                if (tempScore > heismanScore) {
                    heisman = teamList.get(i).teamOLs.get(te);
                    heismanScore = tempScore;
                }
            }
        }
        Collections.sort(heismanCandidates, new PlayerHeismanComp());
        return heismanCandidates;
    }

    public ArrayList<PlayerK> rankK() {
        heisman = null;
        int heismanScore = 0;
        int tempScore = 0;
        ArrayList<PlayerK> heismanCandidates = new ArrayList<PlayerK>();
        for (int i = 0; i < teamList.size(); ++i) {
            for (int te = 0; te < teamList.get(i).teamKs.size(); ++te) {
                heismanCandidates.add(teamList.get(i).teamKs.get(te));
                tempScore = teamList.get(i).teamKs.get(te).getHeismanScore() + teamList.get(i).wins * 100 + teamList.get(i).confPrestige * 10;
                if (tempScore > heismanScore) {
                    heisman = teamList.get(i).teamKs.get(te);
                    heismanScore = tempScore;
                }
            }
        }
        Collections.sort(heismanCandidates, new PlayerHeismanComp());
        return heismanCandidates;
    }

    public ArrayList<PlayerDL> rankDL() {
        heisman = null;
        int heismanScore = 0;
        int tempScore = 0;
        ArrayList<PlayerDL> heismanCandidates = new ArrayList<PlayerDL>();
        for (int i = 0; i < teamList.size(); ++i) {
            for (int dl = 0; dl < teamList.get(i).teamDLs.size(); ++dl) {
                heismanCandidates.add(teamList.get(i).teamDLs.get(dl));
                tempScore = teamList.get(i).teamDLs.get(dl).getHeismanScore() + teamList.get(i).wins * 100 + teamList.get(i).confPrestige * 10;
                if (tempScore > heismanScore) {
                    heisman = teamList.get(i).teamDLs.get(dl);
                    heismanScore = tempScore;
                }
            }
        }
        Collections.sort(heismanCandidates, new PlayerHeismanComp());
        return heismanCandidates;
    }

    public ArrayList<PlayerLB> rankLB() {
        heisman = null;
        int heismanScore = 0;
        int tempScore = 0;
        ArrayList<PlayerLB> heismanCandidates = new ArrayList<PlayerLB>();
        for (int i = 0; i < teamList.size(); ++i) {
            for (int lb = 0; lb < teamList.get(i).teamLBs.size(); ++lb) {
                heismanCandidates.add(teamList.get(i).teamLBs.get(lb));
                tempScore = teamList.get(i).teamLBs.get(lb).getHeismanScore() + teamList.get(i).wins * 100 + teamList.get(i).confPrestige * 10;
                if (tempScore > heismanScore) {
                    heisman = teamList.get(i).teamLBs.get(lb);
                    heismanScore = tempScore;
                }
            }
        }
        Collections.sort(heismanCandidates, new PlayerHeismanComp());
        return heismanCandidates;
    }

    public ArrayList<PlayerCB> rankCB() {
        heisman = null;
        int heismanScore = 0;
        int tempScore = 0;
        ArrayList<PlayerCB> heismanCandidates = new ArrayList<PlayerCB>();
        for (int i = 0; i < teamList.size(); ++i) {
            for (int cb = 0; cb < teamList.get(i).teamCBs.size(); ++cb) {
                heismanCandidates.add(teamList.get(i).teamCBs.get(cb));
                tempScore = teamList.get(i).teamCBs.get(cb).getHeismanScore() + teamList.get(i).wins * 100 + teamList.get(i).confPrestige * 10;
                if (tempScore > heismanScore) {
                    heisman = teamList.get(i).teamCBs.get(cb);
                    heismanScore = tempScore;
                }
            }
        }
        Collections.sort(heismanCandidates, new PlayerHeismanComp());
        return heismanCandidates;
    }

    public ArrayList<PlayerS> rankS() {
        heisman = null;
        int heismanScore = 0;
        int tempScore = 0;
        ArrayList<PlayerS> heismanCandidates = new ArrayList<PlayerS>();
        for (int i = 0; i < teamList.size(); ++i) {
            for (int s = 0; s < teamList.get(i).teamSs.size(); ++s) {
                heismanCandidates.add(teamList.get(i).teamSs.get(s));
                tempScore = teamList.get(i).teamSs.get(s).getHeismanScore() + teamList.get(i).wins * 100 + teamList.get(i).confPrestige * 10;
                if (tempScore > heismanScore) {
                    heisman = teamList.get(i).teamSs.get(s);
                    heismanScore = tempScore;
                }
            }
        }
        Collections.sort(heismanCandidates, new PlayerHeismanComp());
        return heismanCandidates;
    }

    public ArrayList<HeadCoach> rankHC() {
        heisman = null;
        ArrayList<HeadCoach> coachCandidates = new ArrayList<HeadCoach>();
        for (int i = 0; i < teamList.size(); ++i) {
            coachCandidates.add(teamList.get(i).HC.get(0));
        }
        Collections.sort(coachCandidates, new CoachScoreComp());
        return coachCandidates;
    }

    public String getCoachAwardStr() {
        ArrayList<HeadCoach> coachCandidates = rankHC();
        coachWinner = coachCandidates.get(0);
        String coachAwardTopList = "";
        for (int i = 0; i < 5; ++i) {
            Player p = coachCandidates.get(i);
            HeadCoach hc = (HeadCoach) p;
            coachAwardTopList += (i + 1) + ". " + hc.name + ": " + ((HeadCoach) p).getCoachScore() + " votes\n";
            coachAwardTopList += p.team.name + " (" + p.team.wins + "-" + p.team.losses + ")" + "\n\n";
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

        return coachStats;
    }

    //PLAYER RANKINGS STUFF

    public ArrayList<String> getPlayerRankStr(int selection) {
        int rankNum = 120;
        ArrayList<PlayerQB> pQB = new ArrayList<PlayerQB>();
        for (int i = 0; i < teamList.size(); ++i) {
            for (int p = 0; p < teamList.get(i).teamQBs.size(); ++p) {
                pQB.add(teamList.get(i).teamQBs.get(p));
            }
        }
        ArrayList<PlayerRB> pRB = new ArrayList<PlayerRB>();
        for (int i = 0; i < teamList.size(); ++i) {
            for (int p = 0; p < teamList.get(i).teamRBs.size(); ++p) {
                pRB.add(teamList.get(i).teamRBs.get(p));
            }
        }
        ArrayList<PlayerWR> pWR = new ArrayList<PlayerWR>();
        for (int i = 0; i < teamList.size(); ++i) {
            for (int p = 0; p < teamList.get(i).teamWRs.size(); ++p) {
                pWR.add(teamList.get(i).teamWRs.get(p));
            }
        }
        ArrayList<PlayerTE> pTE = new ArrayList<PlayerTE>();
        for (int i = 0; i < teamList.size(); ++i) {
            for (int p = 0; p < teamList.get(i).teamTEs.size(); ++p) {
                pTE.add(teamList.get(i).teamTEs.get(p));
            }
        }
        ArrayList<PlayerOL> pOL = new ArrayList<PlayerOL>();
        for (int i = 0; i < teamList.size(); ++i) {
            for (int p = 0; p < teamList.get(i).teamOLs.size(); ++p) {
                pOL.add(teamList.get(i).teamOLs.get(p));
            }
        }
        ArrayList<PlayerK> pK = new ArrayList<PlayerK>();
        for (int i = 0; i < teamList.size(); ++i) {
            for (int p = 0; p < teamList.get(i).teamKs.size(); ++p) {
                pK.add(teamList.get(i).teamKs.get(p));
            }
        }
        ArrayList<PlayerDL> pDL = new ArrayList<PlayerDL>();
        for (int i = 0; i < teamList.size(); ++i) {
            for (int p = 0; p < teamList.get(i).teamDLs.size(); ++p) {
                pDL.add(teamList.get(i).teamDLs.get(p));
            }
        }
        ArrayList<PlayerLB> pLB = new ArrayList<PlayerLB>();
        for (int i = 0; i < teamList.size(); ++i) {
            for (int p = 0; p < teamList.get(i).teamLBs.size(); ++p) {
                pLB.add(teamList.get(i).teamLBs.get(p));
            }
        }
        ArrayList<PlayerCB> pCB = new ArrayList<PlayerCB>();
        for (int i = 0; i < teamList.size(); ++i) {
            for (int p = 0; p < teamList.get(i).teamCBs.size(); ++p) {
                pCB.add(teamList.get(i).teamCBs.get(p));
            }
        }
        ArrayList<PlayerS> pS = new ArrayList<PlayerS>();
        for (int i = 0; i < teamList.size(); ++i) {
            for (int p = 0; p < teamList.get(i).teamSs.size(); ++p) {
                pS.add(teamList.get(i).teamSs.get(p));
            }
        }
        ArrayList<HeadCoach> HC = new ArrayList<>();
        for (int i = 0; i < teamList.size(); ++i) {
            HC.add(teamList.get(i).HC.get(0));
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

        ArrayList<String> rankings = new ArrayList<String>();
        switch (selection) {
            case 0:
                Collections.sort(pQB, new PlayerPassRatingComp());
                for (int i = 0; i < rankNum; ++i) {
                    rankings.add((i + 1) + ". ," + pQB.get(i).name + " (" + pQB.get(i).team.abbr + ")," + pQB.get(i).getPasserRating());
                }
                break;
            case 1:
                Collections.sort(pQB, new PlayerPassYardsComp());
                for (int i = 0; i < rankNum; ++i) {
                    rankings.add((i + 1) + ". ," + pQB.get(i).name + " (" + pQB.get(i).team.abbr + ")," + pQB.get(i).statsPassYards);
                }
                break;
            case 2:
                Collections.sort(pQB, new PlayerPassTDsComp());
                for (int i = 0; i < rankNum; ++i) {
                    rankings.add((i + 1) + ". ," + pQB.get(i).name + " (" + pQB.get(i).team.abbr + ")," + pQB.get(i).statsPassTD);
                }
                break;
            case 3:
                Collections.sort(pQB, new PlayerPassIntsComp());
                for (int i = 0; i < rankNum; ++i) {
                    rankings.add((i + 1) + ". ," + pQB.get(i).name + " (" + pQB.get(i).team.abbr + ")," + pQB.get(i).statsInt);
                }
                break;
            case 4:
                Collections.sort(pQB, new PlayerPassPctComp());
                for (int i = 0; i < rankNum; ++i) {
                    rankings.add((i + 1) + ". ," + pQB.get(i).name + " (" + pQB.get(i).team.abbr + ")," + pQB.get(i).getPassPCT() + "%");
                }
                break;
            case 5:
                Collections.sort(pRB, new PlayerRushYardsComp());
                for (int i = 0; i < rankNum; ++i) {
                    rankings.add((i + 1) + ". ," + pRB.get(i).name + " (" + pRB.get(i).team.abbr + ")," + pRB.get(i).statsRushYards);
                }
                break;
            case 6:
                Collections.sort(pRB, new PlayerRushTDsComp());
                for (int i = 0; i < rankNum; ++i) {
                    rankings.add((i + 1) + ". ," + pRB.get(i).name + " (" + pRB.get(i).team.abbr + ")," + pRB.get(i).statsRushTD);
                }
                break;
            case 7:
                Collections.sort(pWR, new PlayerReceptionsComp());
                for (int i = 0; i < rankNum; ++i) {
                    rankings.add((i + 1) + ". ," + pWR.get(i).name + " (" + pWR.get(i).team.abbr + ")," + pWR.get(i).statsReceptions);
                }
                break;
            case 8:
                Collections.sort(pWR, new PlayerRecYardsComp());
                for (int i = 0; i < rankNum; ++i) {
                    rankings.add((i + 1) + ". ," + pWR.get(i).name + " (" + pWR.get(i).team.abbr + ")," + pWR.get(i).statsRecYards);
                }
                break;
            case 9:
                Collections.sort(pWR, new PlayerRecTDsComp());
                for (int i = 0; i < rankNum; ++i) {
                    rankings.add((i + 1) + ". ," + pWR.get(i).name + " (" + pWR.get(i).team.abbr + ")," + pWR.get(i).statsTD);
                }
                break;
            case 10:
                Collections.sort(def, new PlayerTacklesComp());
                for (int i = 0; i < rankNum; ++i) {
                    rankings.add((i + 1) + ". ," + def.get(i).name + " (" + def.get(i).team.abbr + ")," + def.get(i).tackles);
                }
                break;
            case 11:
                Collections.sort(def, new PlayerSacksComp());
                for (int i = 0; i < rankNum; ++i) {
                    rankings.add((i + 1) + ". ," + def.get(i).name + " (" + def.get(i).team.abbr + ")," + def.get(i).sacks);
                }
                break;
            case 12:
                Collections.sort(def, new PlayerFumblesRecComp());
                for (int i = 0; i < rankNum; ++i) {
                    rankings.add((i + 1) + ". ," + def.get(i).name + " (" + def.get(i).team.abbr + ")," + def.get(i).fumbles);
                }
                break;
            case 13:
                Collections.sort(def, new PlayerInterceptionsComp());
                for (int i = 0; i < rankNum; ++i) {
                    rankings.add((i + 1) + ". ," + def.get(i).name + " (" + def.get(i).team.abbr + ")," + def.get(i).interceptions);
                }
                break;

            case 14:
                Collections.sort(HC, new CoachScoreComp());
                for (int i = 0; i < rankNum; ++i) {
                    rankings.add((i + 1) + ". ," + HC.get(i).name + " (" + HC.get(i).team.abbr + ")," + HC.get(i).getCoachScore());
                }
                break;
            case 15:
                Collections.sort(pQB, new PlayerHeismanComp());
                for (int i = 0; i < rankNum; ++i) {
                    rankings.add((i + 1) + ". ," + pQB.get(i).name + " (" + pQB.get(i).team.abbr + ")," + pQB.get(i).getHeismanScore());
                }
                break;
            case 16:
                Collections.sort(pRB, new PlayerHeismanComp());
                for (int i = 0; i < rankNum; ++i) {
                    rankings.add((i + 1) + ". ," + pRB.get(i).name + " (" + pRB.get(i).team.abbr + ")," + pRB.get(i).getHeismanScore());
                }
                break;
            case 17:
                Collections.sort(pWR, new PlayerHeismanComp());
                for (int i = 0; i < rankNum; ++i) {
                    rankings.add((i + 1) + ". ," + pWR.get(i).name + " (" + pWR.get(i).team.abbr + ")," + pWR.get(i).getHeismanScore());
                }
                break;
            case 18:
                Collections.sort(pTE, new PlayerHeismanComp());
                for (int i = 0; i < rankNum; ++i) {
                    rankings.add((i + 1) + ". ," + pTE.get(i).name + " (" + pTE.get(i).team.abbr + ")," + pTE.get(i).getHeismanScore());
                }
                break;
            case 19:
                Collections.sort(pOL, new PlayerHeismanComp());
                for (int i = 0; i < rankNum; ++i) {
                    rankings.add((i + 1) + ". ," + pOL.get(i).name + " (" + pOL.get(i).team.abbr + ")," + pOL.get(i).getHeismanScore());
                }
                break;
            case 20:
                Collections.sort(pK, new PlayerHeismanComp());
                for (int i = 0; i < rankNum; ++i) {
                    rankings.add((i + 1) + ". ," + pK.get(i).name + " (" + pK.get(i).team.abbr + ")," + pK.get(i).getHeismanScore());
                }
                break;
            case 21:
                Collections.sort(pDL, new PlayerHeismanComp());
                for (int i = 0; i < rankNum; ++i) {
                    rankings.add((i + 1) + ". ," + pDL.get(i).name + " (" + pDL.get(i).team.abbr + ")," + pDL.get(i).getHeismanScore());
                }
                break;
            case 22:
                Collections.sort(pLB, new PlayerHeismanComp());
                for (int i = 0; i < rankNum; ++i) {
                    rankings.add((i + 1) + ". ," + pLB.get(i).name + " (" + pLB.get(i).team.abbr + ")," + pLB.get(i).getHeismanScore());
                }
                break;
            case 23:
                Collections.sort(pCB, new PlayerHeismanComp());
                for (int i = 0; i < rankNum; ++i) {
                    rankings.add((i + 1) + ". ," + pCB.get(i).name + " (" + pCB.get(i).team.abbr + ")," + pCB.get(i).getHeismanScore());
                }
                break;
            case 24:
                Collections.sort(pS, new PlayerHeismanComp());
                for (int i = 0; i < rankNum; ++i) {
                    rankings.add((i + 1) + ". ," + pS.get(i).name + " (" + pS.get(i).team.abbr + ")," + pS.get(i).getHeismanScore());
                }
                break;
        }
        return rankings;
    }

    public int getAverageYards() {
        int average = 0;
        for (int i = 0; i < teamList.size(); ++i) {
            average += teamList.get(i).teamYards;
        }
        average = average / teamList.size();
        return average;
    }

    public int getAverageOffTalent() {
        int average = 0;
        for (int i = 0; i < teamList.size(); ++i) {
            average += teamList.get(i).getOffTalent();
        }
        average = average / teamList.size();
        return average;
    }

    public int getAverageDefTalent() {
        int average = 0;
        for (int i = 0; i < teamList.size(); ++i) {
            average += teamList.get(i).getDefTalent();
        }
        average = average / teamList.size();
        return average;
    }

    //Coaching Discipline Opportunities
    public void disciplineAction() {
        int t = 0;
        //randomly chooose 5 teams from teamlist
        //disciplinary action
        //use coach pts to fix
        //if not, coach score goes down
        //team loses prestige

    }

    //COACHING CAROUSEL HIRING METHOD
    //THIS METHOD TAKES THE COACH LIST CREATED AFTER FIRING AND PUTS IT INTO A POPULATION FOR TEAMS WITH NO COACHES TO HIRE FROM

    public void coachCarousel() {

        //Rising Star Coaches
        for (int i = 0; i < coachStarList.size(); ++i) {
            String[] coachSplit = coachStarPrevTeam.get(i).split(",");
            String tmName = coachSplit[0];
            int tmPres = Integer.parseInt(coachSplit[1]);
            for (int t = 0; t < teamList.size(); ++t) {
                if (teamList.get(t).HC.isEmpty() && (coachStarList.get(i).ratOvr - 5) >= teamList.get(t).teamPrestige && teamList.get(t).name != tmName && teamList.get(t).teamPrestige >= tmPres) {
                    teamList.get(t).HC.add(coachStarList.get(i));
                    newsStories.get(currentWeek + 1).add("Coaching Hire: " + teamList.get(t).name + ">Rising star head coach " + teamList.get(t).HC.get(0).name + " has announced his departure from " +
                     coachStarPrevTeam.get(i) + " after being selected by " + teamList.get(t).name + " as their new head coach. His previous track record has had him on the top list of many schools.");

                    for (int j = 0; j < teamList.size(); ++j) {
                        if (teamList.get(j).name == tmName) {
                            teamList.get(j).HC.remove(0);
                            teamList.get(t).newRoster(0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
                            newsStories.get(currentWeek + 1).add("Coaching Hire: " + teamList.get(t).name + ">Following the departure of coach, " + teamList.get(t).name +
                            " has promoted assistant coach " + teamList.get(t).HC.get(0).name + " to the head coaching job at the school.");
                        }
                    }

                    break;
                }
            }
        }

        //Coaches who were fired
        for (int i = 0; i < coachList.size(); ++i) {
            for (int t = 0; t < teamList.size(); ++t) {
                if (teamList.get(t).HC.isEmpty() && (coachList.get(i).ratOvr + 5) >= teamList.get(t).teamPrestige && teamList.get(t).name != coachPrevTeam.get(i)) {
                    teamList.get(t).HC.add(coachList.get(i));
                    newsStories.get(currentWeek + 1).add("Coaching Hire: " + teamList.get(t).name + ">After an extensive search for a new head coach, " + teamList.get(t).name + " has hired " + teamList.get(t).HC.get(0).name +
                            " to lead the team. Coach " + teamList.get(t).HC.get(0).name + " previously coached at " + coachPrevTeam.get(i) + ", before being let go this past season.");
                    break;
                }
            }
        }
        for (int t = 0; t < teamList.size(); ++t) {
            if (teamList.get(t).HC.isEmpty()) {
                teamList.get(t).newRoster(0, 0, 0, 0, 0, 0, 0, 0, 0, 0);
                newsStories.get(currentWeek + 1).add("Coaching Hire: " + teamList.get(t).name + ">After an extensive search for a new head coach, " + teamList.get(t).name + " has hired " + teamList.get(t).HC.get(0).name +
                        " to lead the team. Coach " + teamList.get(t).HC.get(0).name + " was previously unemployed, but the team is willing to take its chances on a new face.");
            }
        }
    }

    //Coaching Hot Seat News
    public void coachingHotSeat() {
        if (currentWeek == 0) {
            for (int i = 0; i < teamList.size(); ++i) {
                if (teamList.get(i).HC.get(0).baselinePrestige < teamList.get(i).teamPrestige && teamList.get(i).HC.get(0).contractYear == teamList.get(i).HC.get(0).contractLength) {
                    newsStories.get(0).add("Coaching Hot Seat: " + teamList.get(i).name + ">Head Coach " + teamList.get(i).HC.get(0).name + " has struggled over the course of his current contract with " +
                            teamList.get(i).name + " and has failed to raise the team prestige. Because this is his final contract year, the team will be evaluating whether to continue with the coach at the end of " +
                            "this season. He'll remain on the hot seat throughout this year.");
                } else if (teamList.get(i).HC.get(0).baselinePrestige > teamList.get(i).teamPrestige + 8 && teamList.get(i).teamPrestige < 75) {
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
}

class CoachScoreComp implements Comparator<HeadCoach> {
    @Override
    public int compare(HeadCoach a, HeadCoach b) {
        return a.getCoachScore() > b.getCoachScore() ? -1 : a.getCoachScore() == b.getCoachScore() ? 0 : 1;
    }
}

class PlayerHeismanComp implements Comparator<Player> {
    @Override
    public int compare(Player a, Player b) {
        return a.getHeismanScore() > b.getHeismanScore() ? -1 : a.getHeismanScore() == b.getHeismanScore() ? 0 : 1;
    }
}

class PlayerPassRatingComp implements Comparator<PlayerQB> {
    @Override
    public int compare(PlayerQB a, PlayerQB b) {
        return a.getPasserRating() > b.getPasserRating() ? -1 : a.getPasserRating() == b.getPasserRating() ? 0 : 1;
    }
}

class PlayerPassYardsComp implements Comparator<PlayerQB> {
    @Override
    public int compare(PlayerQB a, PlayerQB b) {
        return a.statsPassYards > b.statsPassYards ? -1 : a.statsPassYards == b.statsPassYards ? 0 : 1;
    }
}

class PlayerPassTDsComp implements Comparator<PlayerQB> {
    @Override
    public int compare(PlayerQB a, PlayerQB b) {
        return a.statsPassTD > b.statsPassTD ? -1 : a.statsPassTD == b.statsPassTD ? 0 : 1;
    }
}

class PlayerPassIntsComp implements Comparator<PlayerQB> {
    @Override
    public int compare(PlayerQB a, PlayerQB b) {
        return a.statsInt > b.statsInt ? -1 : a.statsInt == b.statsInt ? 0 : 1;
    }
}

class PlayerPassPctComp implements Comparator<PlayerQB> {
    @Override
    public int compare(PlayerQB a, PlayerQB b) {
        return a.getPassPCT() > b.getPassPCT() ? -1 : a.getPassPCT() == b.getPassPCT() ? 0 : 1;
    }
}

class PlayerRushYardsComp implements Comparator<PlayerRB> {
    @Override
    public int compare(PlayerRB a, PlayerRB b) {
        return a.statsRushYards > b.statsRushYards ? -1 : a.statsRushYards == b.statsRushYards ? 0 : 1;
    }
}

class PlayerRushTDsComp implements Comparator<PlayerRB> {
    @Override
    public int compare(PlayerRB a, PlayerRB b) {
        return a.statsRushTD > b.statsRushTD ? -1 : a.statsRushTD == b.statsRushTD ? 0 : 1;
    }
}

class PlayerRecYardsComp implements Comparator<PlayerWR> {
    @Override
    public int compare(PlayerWR a, PlayerWR b) {
        return a.statsRecYards > b.statsRecYards ? -1 : a.statsRecYards == b.statsRecYards ? 0 : 1;
    }
}

class PlayerReceptionsComp implements Comparator<PlayerWR> {
    @Override
    public int compare(PlayerWR a, PlayerWR b) {
        return a.statsReceptions > b.statsReceptions ? -1 : a.statsReceptions == b.statsReceptions ? 0 : 1;
    }
}

class PlayerRecTDsComp implements Comparator<PlayerWR> {
    @Override
    public int compare(PlayerWR a, PlayerWR b) {
        return a.statsTD > b.statsTD ? -1 : a.statsTD == b.statsTD ? 0 : 1;
    }
}

class PlayerTacklesComp implements Comparator<PlayerDefense> {
    @Override
    public int compare(PlayerDefense a, PlayerDefense b) {
        return a.tackles > b.tackles ? -1 : a.tackles == b.tackles ? 0 : 1;
    }
}

class PlayerSacksComp implements Comparator<PlayerDefense> {
    @Override
    public int compare(PlayerDefense a, PlayerDefense b) {
        return a.sacks > b.sacks ? -1 : a.sacks == b.sacks ? 0 : 1;
    }
}

class PlayerFumblesRecComp implements Comparator<PlayerDefense> {
    @Override
    public int compare(PlayerDefense a, PlayerDefense b) {
        return a.fumbles > b.fumbles ? -1 : a.fumbles == b.fumbles ? 0 : 1;
    }
}

class PlayerInterceptionsComp implements Comparator<PlayerDefense> {
    @Override
    public int compare(PlayerDefense a, PlayerDefense b) {
        return a.interceptions > b.interceptions ? -1 : a.interceptions == b.interceptions ? 0 : 1;
    }
}

class TeamCompPoll implements Comparator<Team> {
    @Override
    public int compare(Team a, Team b) {
        return a.teamPollScore > b.teamPollScore ? -1 : a.teamPollScore == b.teamPollScore ? 0 : 1;
    }
}

class TeamCompSoW implements Comparator<Team> {
    @Override
    public int compare(Team a, Team b) {
        return a.teamStrengthOfWins > b.teamStrengthOfWins ? -1 : a.teamStrengthOfWins == b.teamStrengthOfWins ? 0 : 1;
    }
}


class TeamCompSoS implements Comparator<Team> {
    @Override
    public int compare(Team a, Team b) {
        return a.teamSOS > b.teamSOS ? -1 : a.teamSOS == b.teamSOS ? 0 : 1;
    }
}

class TeamCompPPG implements Comparator<Team> {
    @Override
    public int compare(Team a, Team b) {
        return a.teamPoints / a.numGames() > b.teamPoints / b.numGames() ? -1 : a.teamPoints / a.numGames() == b.teamPoints / b.numGames() ? 0 : 1;
    }
}

class TeamCompOPPG implements Comparator<Team> {
    @Override
    public int compare(Team a, Team b) {
        return a.teamOppPoints / a.numGames() < b.teamOppPoints / b.numGames() ? -1 : a.teamOppPoints / a.numGames() == b.teamOppPoints / b.numGames() ? 0 : 1;
    }
}

class TeamCompYPG implements Comparator<Team> {
    @Override
    public int compare(Team a, Team b) {
        return a.teamYards / a.numGames() > b.teamYards / b.numGames() ? -1 : a.teamYards / a.numGames() == b.teamYards / b.numGames() ? 0 : 1;
    }
}

class TeamCompOYPG implements Comparator<Team> {
    @Override
    public int compare(Team a, Team b) {
        return a.teamOppYards / a.numGames() < b.teamOppYards / b.numGames() ? -1 : a.teamOppYards / a.numGames() == b.teamOppYards / b.numGames() ? 0 : 1;
    }
}

class TeamCompOPYPG implements Comparator<Team> {
    @Override
    public int compare(Team a, Team b) {
        return a.teamOppPassYards / a.numGames() < b.teamOppPassYards / b.numGames() ? -1 : a.teamOppPassYards / a.numGames() == b.teamOppPassYards / b.numGames() ? 0 : 1;
    }
}

class TeamCompORYPG implements Comparator<Team> {
    @Override
    public int compare(Team a, Team b) {
        return a.teamOppRushYards / a.numGames() < b.teamOppRushYards / b.numGames() ? -1 : a.teamOppRushYards / a.numGames() == b.teamOppRushYards / b.numGames() ? 0 : 1;
    }
}

class TeamCompPYPG implements Comparator<Team> {
    @Override
    public int compare(Team a, Team b) {
        return a.teamPassYards / a.numGames() > b.teamPassYards / b.numGames() ? -1 : a.teamPassYards / a.numGames() == b.teamPassYards / b.numGames() ? 0 : 1;
    }
}

class TeamCompRYPG implements Comparator<Team> {
    @Override
    public int compare(Team a, Team b) {
        return a.teamRushYards / a.numGames() > b.teamRushYards / b.numGames() ? -1 : a.teamRushYards / a.numGames() == b.teamRushYards / b.numGames() ? 0 : 1;
    }
}

class TeamCompTODiff implements Comparator<Team> {
    @Override
    public int compare(Team a, Team b) {
        return a.teamTODiff > b.teamTODiff ? -1 : a.teamTODiff == b.teamTODiff ? 0 : 1;
    }
}

class TeamCompOffTalent implements Comparator<Team> {
    @Override
    public int compare(Team a, Team b) {
        return a.teamOffTalent > b.teamOffTalent ? -1 : a.teamOffTalent == b.teamOffTalent ? 0 : 1;
    }
}

class TeamCompDefTalent implements Comparator<Team> {
    @Override
    public int compare(Team a, Team b) {
        return a.teamDefTalent > b.teamDefTalent ? -1 : a.teamDefTalent == b.teamDefTalent ? 0 : 1;
    }
}

class TeamCompPrestige implements Comparator<Team> {
    @Override
    public int compare(Team a, Team b) {
        return a.teamPrestige > b.teamPrestige ? -1 : a.teamPrestige == b.teamPrestige ? 0 : 1;
    }
}

class TeamCompRecruitClass implements Comparator<Team> {
    @Override
    public int compare(Team a, Team b) {
        return a.getRecruitingClassRat() > b.getRecruitingClassRat() ? -1 : a.getRecruitingClassRat() == b.getRecruitingClassRat() ? 0 : 1;
    }
}





