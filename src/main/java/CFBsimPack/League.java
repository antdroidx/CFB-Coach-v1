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
    public Team saveCurse;
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

    //Bowl Games
    public boolean hasScheduledBowls;
    public Game semiG14;
    public Game semiG23;
    public Game ncg;
    public Game[] bowlGames;

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

    public static final String[] donationNames = {"Mark Eeslee", "Lee Sin", "Brent Uttwipe", "Gabriel Kemble",
            "Jon Stupak", "Kiergan Ren", "Dean Steinkuhler", "Declan Greally", "Parks Wilson", "Darren Ryder"};

    private boolean isHardMode;

    /**
     * Creates League, sets up Conferences, reads team names and conferences from file.
     * Also schedules games for every team.
     */
    public League(String namesCSV, String lastNamesCSV, boolean difficulty) {
        isHardMode = difficulty;
        heismanDecided = false;
        hasScheduledBowls = false;
        bowlGames = new Game[14];
        leagueHistory = new ArrayList<String[]>();
        heismanHistory = new ArrayList<String>();
        currentWeek = 0;
        conferences = new ArrayList<Conference>();
        conferences.add( new Conference("ACC", this) );
        conferences.add( new Conference("American", this) );
        conferences.add( new Conference("Big Ten", this) );
        conferences.add( new Conference("Big 12", this) );
        conferences.add( new Conference("Conf USA", this) );
        conferences.add( new Conference("MAC", this) );
        conferences.add( new Conference("Mt West", this) );
        conferences.add( new Conference("PAC-10", this) );
        conferences.add( new Conference("SEC", this) );
        conferences.add( new Conference("Sun Belt", this) );
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

        //Set up conferences
        //ACC - done
        conferences.get(0).confTeams.add( new Team( "Clemson", "CLEM", "ACC", this, 90, "WAKE" ));
        conferences.get(0).confTeams.add( new Team( "Duke", "DUKE", "ACC", this, 45, "UNC" ));
        conferences.get(0).confTeams.add( new Team( "Florida St", "FSU", "ACC", this, 74, "MIA" ));
        conferences.get(0).confTeams.add( new Team( "Louisville", "LOUI", "ACC", this, 77, "NCST" ));
        conferences.get(0).confTeams.add( new Team( "Miami", "MIA", "ACC", this, 70, "FSU" ));
        conferences.get(0).confTeams.add( new Team( "North Carolina", "UNC", "ACC", this, 62, "DUKE" ));
        conferences.get(0).confTeams.add( new Team( "NC State", "NCST", "ACC", this, 61, "LOUI" ));
        conferences.get(0).confTeams.add( new Team( "Pittsburgh", "PITT", "ACC", this, 72, "VTEC" ));
        conferences.get(0).confTeams.add( new Team( "Wake Forest", "WAKE", "ACC", this, 42, "CLEM" ));
        conferences.get(0).confTeams.add( new Team( "Virginia Tech", "VTEC", "ACC", this, 70, "PITT" ));

        //American - done
        conferences.get(1).confTeams.add( new Team( "Boston College", "BC", "American", this, 27, "TEM" ));
        conferences.get(1).confTeams.add( new Team( "Central Florida", "UCF", "American", this, 50, "USF" ));
        conferences.get(1).confTeams.add( new Team( "Cinncinati", "CINN", "American", this, 57, "MEMP" ));
        conferences.get(1).confTeams.add( new Team( "Memphis", "MEMP", "American", this, 69, "CINN" ));
        conferences.get(1).confTeams.add( new Team( "Notre Dame", "ND", "American", this, 70, "WVU" ));
        conferences.get(1).confTeams.add( new Team( "Rutgers", "RUT", "American", this, 54, "SYR" ));
        conferences.get(1).confTeams.add( new Team( "South Florida", "USF", "American", this, 73, "UCF" ));
        conferences.get(1).confTeams.add( new Team( "Syracuse", "SYR", "American", this, 49, "RUT" ));
        conferences.get(1).confTeams.add( new Team( "Temple", "TEM", "American", this, 58, "BC" ));
        conferences.get(1).confTeams.add( new Team( "West Virginia", "WVU", "American", this, 73, "ND" ));

        //Big Ten - done
        conferences.get(2).confTeams.add( new Team( "Iowa", "IOW", "Big Ten", this, 60, "NEB" ));
        conferences.get(2).confTeams.add( new Team( "Michigan", "MIC", "Big Ten", this, 85, "OSU" ));
        conferences.get(2).confTeams.add( new Team( "Michigan St", "MSU", "Big Ten", this, 65, "PSU" ));
        conferences.get(2).confTeams.add( new Team( "Minnesota", "MIN", "Big Ten", this, 54, "WIS" ));
        conferences.get(2).confTeams.add( new Team( "Nebraska", "NEB", "Big Ten", this, 73, "IOW" ));
        conferences.get(2).confTeams.add( new Team( "Northwestern", "NWU", "Big Ten", this, 55, "PUR" ));
        conferences.get(2).confTeams.add( new Team( "Ohio State", "OSU", "Big Ten", this, 86, "MIC" ));
        conferences.get(2).confTeams.add( new Team( "Penn State", "PSU", "Big Ten", this, 84, "MSU" ));
        conferences.get(2).confTeams.add( new Team( "Purdue", "PUR", "Big Ten", this, 47, "NWU" ));
        conferences.get(2).confTeams.add( new Team( "Wisconsin", "WIS", "Big Ten", this, 74, "MIN" ));

        //Big 12 - done
        conferences.get(3).confTeams.add( new Team( "Baylor", "BAY", "Big 12", this, 54, "TCU" ));
        conferences.get(3).confTeams.add( new Team( "Texas Tech", "TTEC", "Big 12", this, 51, "MIZ" ));
        conferences.get(3).confTeams.add( new Team( "Kansas", "KAN", "Big 12", this, 29, "KSU" ));
        conferences.get(3).confTeams.add( new Team( "Kansas St", "KSU", "Big 12", this, 70, "KAN" ));
        conferences.get(3).confTeams.add( new Team( "Oklahoma", "OKL", "Big 12", this, 90, "TEX" ));
        conferences.get(3).confTeams.add( new Team( "Oklahoma St", "OKST", "Big 12", this, 77, "TXAM" ));
        conferences.get(3).confTeams.add( new Team( "Texas", "TEX", "Big 12", this, 73, "OKL" ));
        conferences.get(3).confTeams.add( new Team( "Texas AM", "TXAM", "Big 12", this, 74, "OKST" ));
        conferences.get(3).confTeams.add( new Team( "TCU", "TCU", "Big 12", this, 76, "BAY" ));
        conferences.get(3).confTeams.add( new Team( "Missouri", "MIZ", "Big 12", this, 63, "TTEC" ));

        //Conf USA
        conferences.get(4).confTeams.add( new Team( "Army", "ARMY", "Conf USA", this, 46, "NAVY" ));
        conferences.get(4).confTeams.add( new Team( "Florida Atl", "FAU", "Conf USA", this, 48, "FIU" ));
        conferences.get(4).confTeams.add( new Team( "Florida Intl", "FIU", "Conf USA", this, 47, "FAU" ));
        conferences.get(4).confTeams.add( new Team( "Houston", "HOU", "Conf USA", this, 61, "UTEP" ));
        conferences.get(4).confTeams.add( new Team( "Marshall", "MARS", "Conf USA", this, 45, "SMU" ));
        conferences.get(4).confTeams.add( new Team( "Navy", "NAVY", "Conf USA", this, 67, "ARMY" ));
        conferences.get(4).confTeams.add( new Team( "Southern Meth", "SMU", "Conf USA", this, 53, "MARS" ));
        conferences.get(4).confTeams.add( new Team( "UTEP", "UTEP", "Conf USA", this, 47, "HOU" ));
        conferences.get(4).confTeams.add( new Team( "UAB", "UAB", "Conf USA", this, 50, "WKU" ));
        conferences.get(4).confTeams.add( new Team( "Western Kentucky", "WKU", "Conf USA", this, 60, "UAB" ));

        // MAC -
        conferences.get(5).confTeams.add( new Team( "Central Mich", "CMU", "MAC", this, 43, "WMU" ));
        conferences.get(5).confTeams.add( new Team( "Eastern Mich", "EMU", "MAC", this, 39, "MiOH" ));
        conferences.get(5).confTeams.add( new Team( "Illinois", "ILL", "MAC", this, 47, "IND" ));
        conferences.get(5).confTeams.add( new Team( "Indiana", "IND", "MAC", this, 30, "ILL" ));
        conferences.get(5).confTeams.add( new Team( "Iowa State", "ISU", "MAC", this, 60, "NIU" ));
        conferences.get(5).confTeams.add( new Team( "Maryland", "MARY", "MAC", this, 51, "VIR" ));
        conferences.get(5).confTeams.add( new Team( "Miami OH", "MiOH", "MAC", this, 45, "EMU" ));
        conferences.get(5).confTeams.add( new Team( "Northern Illinios", "NIU", "MAC", this, 48, "ISU" ));
        conferences.get(5).confTeams.add( new Team( "Virginia", "VIR", "MAC", this, 56, "MARY" ));
        conferences.get(5).confTeams.add( new Team( "Western Mich", "WMU", "MAC", this, 65, "CMU" ));

        //Mt West
        conferences.get(6).confTeams.add( new Team( "Boise State", "BOIS", "Mt West", this, 73, "SDSU" ));
        conferences.get(6).confTeams.add( new Team( "BYU", "BYU", "Mt West", this, 47, "UTAH" ));
        conferences.get(6).confTeams.add( new Team( "Colorado", "COL", "Mt West", this, 71, "CSU" ));
        conferences.get(6).confTeams.add( new Team( "Colorado St", "CSU", "Mt West", this, 50, "COL" ));
        conferences.get(6).confTeams.add( new Team( "Fresno St", "FRES", "Mt West", this, 47, "HAW" ));
        conferences.get(6).confTeams.add( new Team( "Hawaii", "HAW", "Mt West", this, 42, "FRES" ));
        conferences.get(6).confTeams.add( new Team( "San Diego State", "SDSU", "Mt West", this, 75, "BOIS" ));
        conferences.get(6).confTeams.add( new Team( "Utah", "UTAH", "Mt West", this, 72, "BYU" ));
        conferences.get(6).confTeams.add( new Team( "Utah State", "UTSt", "Mt West", this, 48, "WYOM" ));
        conferences.get(6).confTeams.add( new Team( "Wyoming", "WYOM", "Mt West", this, 49, "UTSt" ));

        //PAC-10 - done
        conferences.get(7).confTeams.add( new Team( "Arizona", "ARIZ", "PAC-10", this, 60, "ASU" ));
        conferences.get(7).confTeams.add( new Team( "Arizona State", "ASU", "PAC-10", this, 65, "ARIZ" ));
        conferences.get(7).confTeams.add( new Team( "California", "CAL", "PAC-10", this, 60, "STAN" ));
        conferences.get(7).confTeams.add( new Team( "Oregon", "OREG", "PAC-10", this, 72, "ORST" ));
        conferences.get(7).confTeams.add( new Team( "Oregon State", "ORST", "PAC-10", this, 40, "OREG" ));
        conferences.get(7).confTeams.add( new Team( "Stanford", "STAN", "PAC-10", this, 76, "CAL" ));
        conferences.get(7).confTeams.add( new Team( "UCLA", "UCLA", "PAC-10", this, 71, "USC" ));
        conferences.get(7).confTeams.add( new Team( "USC", "USC", "PAC-10", this, 87, "UCLA" ));
        conferences.get(7).confTeams.add( new Team( "Washington", "WASH", "PAC-10", this, 84, "WSU" ));
        conferences.get(7).confTeams.add( new Team( "Wash State", "WSU", "PAC-10", this, 74, "WASH" ));

        //SEC - done
        conferences.get(8).confTeams.add( new Team( "Alabama", "BAMA", "SEC", this, 95, "AUB" ));
        conferences.get(8).confTeams.add( new Team( "Arkansas", "ARK", "SEC", this, 50, "LSU" ));
        conferences.get(8).confTeams.add( new Team( "Auburn", "AUB", "SEC", this, 72, "BAMA" ));
        conferences.get(8).confTeams.add( new Team( "Florida", "FLOR", "SEC", this, 70, "UGA" ));
        conferences.get(8).confTeams.add( new Team( "Georgia", "UGA", "SEC", this, 80, "FLOR" ));
        conferences.get(8).confTeams.add( new Team( "Kentucky", "UK", "SEC", this, 55, "TENN" ));
        conferences.get(8).confTeams.add( new Team( "Louisiana St", "LSU", "SEC", this, 77, "ARK" ));
        conferences.get(8).confTeams.add( new Team( "Ole Miss", "MISS", "SEC", this, 60, "MSST" ));
        conferences.get(8).confTeams.add( new Team( "Mississippi St", "MSST", "SEC", this, 73, "MISS" ));
        conferences.get(8).confTeams.add( new Team( "Tennessee", "TENN", "SEC", this, 74, "UK" ));

        //Sun Belt
        conferences.get(9).confTeams.add( new Team( "Appalachian State", "APP", "Sun Belt", this, 47, "GT" ));
        conferences.get(9).confTeams.add( new Team( "Georgia Tech", "GT", "Sun Belt", this, 30, "APP" ));
        conferences.get(9).confTeams.add( new Team( "East Carolina", "ECU", "Sun Belt", this, 47, "SC" ));
        conferences.get(9).confTeams.add( new Team( "LA Monroe", "LMON", "Sun Belt", this, 43, "LTEC" ));
        conferences.get(9).confTeams.add( new Team( "LA Tech", "LTEC", "Sun Belt", this, 46, "LMON" ));
        conferences.get(9).confTeams.add( new Team( "Mid Tenn State", "MTSU", "Sun Belt", this, 51, "VAND" ));
        conferences.get(9).confTeams.add( new Team( "Southern Miss", "SMIS", "Sun Belt", this, 53, "TROY" ));
        conferences.get(9).confTeams.add( new Team( "South Carolina", "SC", "Sun Belt", this, 61, "ECU" ));
        conferences.get(9).confTeams.add( new Team( "Troy", "TROY", "Sun Belt", this, 40, "SMIS" ));
        conferences.get(9).confTeams.add( new Team( "Vanderbilt", "VAND", "Sun Belt", this, 30, "MTSU" ));


        //set teamList
        teamList = new ArrayList<Team>();
        for (int i = 0; i < conferences.size(); ++i ) {
            for (int j = 0; j < conferences.get(i).confTeams.size(); ++j ) {
                teamList.add( conferences.get(i).confTeams.get(j) );
            }
        }

        //set up schedule
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

    /**
     * Create League from saved file.
     * @param saveFile file that league is saved in
     */
    public League(File saveFile, String namesCSV, String lastNamesCSV) {
        heismanDecided = false;
        hasScheduledBowls = false;
        blessDevelopingStory = false;
        curseDevelopingStory = false;
        bowlGames = new Game[14];
        // This will reference one line at a time
        String line = null;
        currentWeek = 0;

        leagueRecords = new LeagueRecords();
        userTeamRecords = new LeagueRecords();
        longestWinStreak = new TeamStreak(2017, 2017, 0, "XXX");
        yearStartLongestWinStreak = new TeamStreak(2017, 2017, 0, "XXX");
        longestActiveWinStreak = new TeamStreak(2017, 2017, 0, "XXX");

        try {
            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader( new FileReader(saveFile) );

            //First ignore the save file info
            line = bufferedReader.readLine();
            // [EASY]%    [HARD]%
            isHardMode = line.substring(line.length() - 7, line.length()).equals("[HARD]%");

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

            //Next make all the teams
            conferences = new ArrayList<Conference>();
            teamList = new ArrayList<Team>();

            conferences.add( new Conference("ACC", this) );
            conferences.add( new Conference("American", this) );
            conferences.add( new Conference("Conf USA", this) );
            conferences.add( new Conference("Big Ten", this) );
            conferences.add( new Conference("Big 12", this) );
            conferences.add( new Conference("MAC", this) );
            conferences.add( new Conference("Mt West", this) );
            conferences.add( new Conference("PAC-10", this) );
            conferences.add( new Conference("SEC", this) );
            conferences.add( new Conference("SunBelt", this) );
            allAmericans = new ArrayList<Player>();
            String[] splits;
            for(int i = 0; i < 100; ++i) { //Do for every team (60)
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

            StringBuilder sbCurse = new StringBuilder();
            while((line = bufferedReader.readLine()) != null && !line.equals("END_CURSE_TEAM")) {
                sbCurse.append(line);
            }
            if (!sbCurse.toString().equals("NULL")) {
                saveCurse = findTeamAbbr(sbCurse.toString());
                saveCurse.sortPlayers();
                findTeamAbbr(saveCurse.rivalTeam).sortPlayers();
            } else {saveCurse = null;}

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

            // Set up offseason news to be randomly added to Week 0, set up names to be used if random names are needed, and make first and last names available for later in the story

            if (saveBless != null){
                storyFullName = getRandName();
                storyFirstName = storyFullName.replaceAll(" .*", "");
                storyLastName = storyFullName.replaceAll(".* ", "");
                String storyPlayer;

                for(int i = 0; i < 1; i++){
                    switch((int)(Math.random() * 8)) { //Change the number Math.random is multiplied by to the number of cases (so last case # + 1)
                        case 0:
                            //Hired a shiny new coach who used to play for the school (feed those Vol fans wishing for Peyton something to dream on)
                            newsStories.get(0).add("Blue Chip hire for Bad Break University>" + saveBless.name + " announced the hire of alumnus and former professional coach " + getRandName() + ", today. It was long rumored that the highly touted coach considered the position a \"dream job\", but talks between the two didn't heat up until this offseason. The hire certainly helps boost the prestige of the University's football program, which has fallen on hard times as of late.");
                            break;

                        case 1:
                            //Rich Sports Apparel CEO alum giving flashy uniforms to the school
                            newsStories.get(0).add("Fashion Speaks Louder Than Words>Renowned sports apparel mogul and " + saveBless.name + " alumnus " + storyFullName + " has declared war on boring uniforms. " + storyLastName + " has pledged his company's services to \"ensure that the university's football team never wears the same uniform twice.\" Recruits are already abuzz on social media declaring their newly found interest in playing for the school.");
                            break;

                        case 2:
                            //Get the first defensive player that isn't a Freshman and use their name to describe being on the 'CFB News' top play for 50 days
                            // Only using the first 4 of the front 7, we'll just assume it's a 3-4 defense and those are their linebackers
                            // If no suitable players, decrement i and retry the loop for a usable story
                            String playerLastName;
                            if (saveBless.getS(0).year >= 2) storyPlayer = saveBless.getS(0).name;
                            else if (saveBless.getCB(0).year >= 2)
                                storyPlayer = saveBless.getCB(0).name;
                            else if (saveBless.getF7(0).year >= 2)
                                storyPlayer = saveBless.getF7(0).name;
                            else if (saveBless.getCB(1).year >= 2)
                                storyPlayer = saveBless.getCB(1).name;
                            else if (saveBless.getF7(1).year >= 2)
                                storyPlayer = saveBless.getF7(1).name;
                            else if (saveBless.getCB(2).year >= 2)
                                storyPlayer = saveBless.getCB(2).name;
                            else if (saveBless.getF7(2).year >= 2)
                                storyPlayer = saveBless.getF7(2).name;
                            else if (saveBless.getF7(3).year >= 2)
                                storyPlayer = saveBless.getF7(3).name;
                            else{
                                System.out.println("No suitable defensive players, new bless story");
                                i--;
                                break;
                            }

                            playerLastName = storyPlayer.replaceAll(".* ", ""); // For referencing last name all news like
                            newsStories.get(0).add("The Hit That Keeps On Giving>For the 50th consecutive day, " + saveBless.name + " star " + storyPlayer + "'s explosive hit against " + saveBless.rivalTeam + " sits atop the CFB News Top Plays list. " + playerLastName + " credits Coach " + getRandName() + " with providing him the inspiration to stay in the weight room late and think clearly during plays. During its reign, \"The Hit\" has dethroned and outlasted the " + teamList.get((int) (Math.random() * 60)).name + " Baseball Team's \"Puppies in the Park\" viral video, and " + teamList.get((int) (Math.random() * 60)).name + "'s Make-A-Wish TD on the Top Plays list.");
                            break;

                        case 3:
                            // Free Prestige, is it in you? Set developing story for blessed team to true and add a story
                            newsStories.get(0).add(saveBless.name + " Nutrition Dept. Electro-Lighting Up the Field>Nutrition and Sport Science graduate students at " + saveBless.name + " are helping their team gain the upper hand on the field with their own work in the lab. At a press conference held today outside the team's practice field, the university announced the first production run of it's own sports enhancement drink. The drink is expected to be available in stores mid-season with an initial offering of three flavors: \"Berry Blitz\", \"Hail Cherry\", and \"The Man-Go Route.\" Despite recent struggles by " + saveBless.name + " on the football field, the school is hoping to boost it's overall image with this move.");
                            blessDevelopingStory = true;
                            blessDevelopingWeek = ((int) (Math.random() * 4)) + 5; // Print a new story "mid-season" (random week between 6 and 10)
                            blessDevelopingCase = 1;
                            System.out.println("Check news in week " + (blessDevelopingWeek + 1) + " for " + saveBless.name + "'s story development");
                            break;

                        case 4:
                            //Inspired by that time Kliff did the stanky leg in a circle of Tech players)
                            newsStories.get(0).add("Just Call Him Coach Dougie>When a cell phone recording of Coach " + storyFullName + " out dancing his players at the end of a Spring practice was uploaded to the internet, " + storyLastName + " thought nothing of it. When it hit one million views over night, Coach took notice. In response to wild popularity his moves have received, " + storyLastName + " has made it a new tradition at " + saveBless.name + " to have a dance off with all prospective recruits, much to the delight of fans and students, who have turned out in large numbers to watch the competitions.");
                            break;


                        case 5:
                            //Originally pitched as a story about the team partying hard, and getting positive recruiting as a result, before homecoming despite losing the game, I couldn't think of a good way to turn that into a preseason story directly. As a result this one is inspired by that.
                            //And if the sarcasm/humor in this story isn't obvious, I've done something wrong
                            newsStories.get(0).add("Breaking News: College Athletes Love Partying>A stunning development out of " + saveBless.name + " today, as it's being reported that college athletes, and athletic recruits for that matter, love to \"party and just have a good time.\" This surprising development grew out of reports on social media that " + saveBless.name + " fall practices were drawing large crowds. These stories eventually led to more reports that full fledged parties, complete with beer and music, were taking place after each practice. Recruits have been abuzz on social media declaring their intent to check out these parties and their desire to play for a school that \"knows how to have a good time.\" Coaching staffs around the country are scratching their collective heads in bewilderment while " + saveBless.name + " enjoys their sudden and unexpected recruiting boon.");
                            break;

                        case 6:
                            //Increase academic standards have yielded an overall stronger group of recruits for saveBless.name
                            newsStories.get(0).add("Success in the Classroom Spilling Onto the Field>" + saveBless.name + " find themselves in an unusual, but agreeable, position. The university, which has been slowly increasing admissions standards and taking strives to improve it's academic offerings, has begun to find itself counted among the Top 100 schools nationwide. As a result, the football Program program is finding itself with a new breed of recruit: smarter, more driven, and more capable of learning complex schemes. " + saveBless.name + " also finds itself with increased overall attention, as its name is beginning to have association with some of the most academically rigorous institutions in the country. As the school's mission to improve its academic standing continues, it stands to reason that it will enjoy an increased level of prestige.");
                            break;

                        case 7:
                            //TV Deal -- Each conf will have it's own network name and a story about a network getting a TV deal can't be posted more than once (same for individual tv deals)
                            //If the conference doesn't get a TV deal, saveBless will get an individual deal which alters the story if/when the conference gets their own network

                            String networkName; // Name of conf TV network

                            switch(getConfNumber(saveBless.conference)){ //Set name using switch method fed by conference's number
                                case 8:
                                    networkName = "SEC Sports";
                                    break;
                                case 2:
                                    networkName = "Big Ten Network";
                                    break;
                                case 0:
                                    networkName = "The ACC Network";
                                    break;
                                case 3:
                                    networkName = "Big 12 Championship Channel";
                                    break;
                                case 7:
                                    networkName = "PAC-10 Network";
                                    break;
                                case 1:
                                    networkName = "American Sports";
                                    break;
                                case 5:
                                    networkName = "MAC Messenger";
                                    break;
                                case 6:
                                    networkName = "Mountain Sports";
                                    break;
                                case 4:
                                    networkName = "Conf USA Network";
                                    break;
                                case 9:
                                    networkName = "SunBelt Sports";
                                    break;
                                default:
                                    networkName = (saveBless.conference + " Collegiate Sports Network");
                                    break;
                            }

                            //Setup variables to check for individual Team TV deals
                            int teamTVDeals = 0;
                            ArrayList<String> tvDealTeams = new ArrayList<String>();

                            String memberSchoolsWithTV;

                            for (int ttv = 0; ttv < conferences.get(getConfNumber(saveBless.conference)).confTeams.size(); ttv++){ //Check each team in the conference
                                if(conferences.get(getConfNumber(saveBless.conference)).confTeams.get(ttv).teamTVDeal){ //To see if they have an individual deal
                                    teamTVDeals++; //If they do, increment teamTVDeals
                                    tvDealTeams.add(conferences.get(getConfNumber(saveBless.conference)).confTeams.get(ttv).name); //and add their name to the list of teams with a deal
                                }
                            }

                            if (tvDealTeams.size() == 1){ //Set up string for story about teams with individual deals being negotiated with
                                memberSchoolsWithTV = "member school " + tvDealTeams.get(0);
                            }
                            else{
                                memberSchoolsWithTV = "member schools ";
                                for(int tdt = 0; tdt < tvDealTeams.size(); tdt++){
                                    if(tdt == tvDealTeams.size()-1) { //If the last team in the list
                                        memberSchoolsWithTV += " and ";
                                    }
                                    if (tvDealTeams.size() == 2){ //If just two teams, no need for a comma (SchoolX and SchoolY)
                                        memberSchoolsWithTV += (tvDealTeams.get(tdt) + " ");
                                    }
                                    else{ //Need commas
                                        if(tdt == tvDealTeams.size()-1){//Last team in the list, no need for a comma (, and SchoolZ)
                                            memberSchoolsWithTV += (tvDealTeams.get(tdt) + " ");
                                        }
                                        else{ //A team at the start or middle of a list 3 or larger (School W, SchoolX, SchoolY, and)
                                            //WE OXFORD COMMA NOW
                                            memberSchoolsWithTV += (tvDealTeams.get(tdt) + ", ");
                                        }
                                    }
                                }
                            }


                            //Lets write some news -- Start by checking if the conf has a deal, if every team does not have their own deal, and a 20% chance
                            if(Math.random() <= 0 && !findTeamAbbr(saveBless.abbr).confTVDeal && teamTVDeals == 0){ //If no teams have individual deals and the conference has no network, 20% a conference network is formed
                                //Conference TV Deal -- Plus 5 prestige to the conference's member schools
                                newsStories.get(0).add(saveBless.conference + " Announces Launch of New TV Network>In a joint press conference between conference officials and all member schools, The " + saveBless.conference + " Conference announced the launch of it's new TV network " + networkName + ". The network, which was largely spearheaded by member school " + saveBless.name + ", is expected to increase the revenue and recruiting range of member schools in ways previously unseen by the conference. The channel goes live next week with the first broadcast expected to be the morning matchup between " + saveBless.gameSchedule.get(0).homeTeam.name + " and " + saveBless.gameSchedule.get(0).awayTeam.name + ".");

                                //Add prestige and set confTVDeal to true for each team so that the next time this comes around, a duplicate story won't be posted about a network being formed (and prestige is only granted once)
                                for(int ctv = 0; ctv < conferences.get(getConfNumber(saveBless.conference)).confTeams.size(); ctv++){
                                    conferences.get(getConfNumber((saveBless.conference))).confTeams.get(ctv).confTVDeal = true;
                                    conferences.get(getConfNumber((saveBless.conference))).confTeams.get(ctv).teamPrestige += 5;
                                }
                            }

                            //We didn't use the above story, so check to see if it failed because a Team TV Deal Exists
                            else if(Math.random() <= 0.20 && !findTeamAbbr(saveBless.abbr).confTVDeal && teamTVDeals != 0){ //If 20% chance, no conf tv deal, but someone in the conf has an individual deal

                                //Conference TV Deal -- But there had to be negotiations with the teams that already had TV deals -- Plus prestige to the conference members
                                //Teams that already have individual deals don't get the bonus prestige (or maybe get less) cause they were already on TV
                                newsStories.get(0).add(saveBless.conference + " Announces New Network After Lengthy Negotiations>After negotiations that went long into the night, The " + saveBless.conference + " Conference released a statement today detailing the launch of " + networkName + ", the conference's first exclusive TV network. Sources familiar with the situation explained that the agreement was delayed by negotiations with " + memberSchoolsWithTV + " who had previously inked TV deals of their own for team specific networks. Specific details were not released, but Conference Comissioner " + storyFullName + " did go on record as saying the only TV network for all conference teams will be " + networkName + " moving forward.");

                                //Add prestige and set confTVDeal to true for each team so that the next time this comes around, a duplicate story won't be posted about a network being formed (and prestige is only granted once)
                                for(int ctv = 0; ctv < conferences.get(getConfNumber(saveBless.conference)).confTeams.size(); ctv++) {
                                    conferences.get(getConfNumber((saveBless.conference))).confTeams.get(ctv).confTVDeal = true;
                                    if(tvDealTeams.contains(conferences.get(getConfNumber((saveBless.conference))).confTeams.get(ctv).name)){
                                        //Give nothing to saveBless cause they already got a fat chunk of prestige
                                        if(conferences.get(getConfNumber((saveBless.conference))).confTeams.get(ctv).name.equals(saveBless.name)) conferences.get(getConfNumber((saveBless.conference))).confTeams.get(ctv).teamPrestige += 0;
                                        else conferences.get(getConfNumber((saveBless.conference))).confTeams.get(ctv).teamPrestige += 1; // Your TV network already existed, so this whole conference TV network is whatever to you
                                    }
                                    else {
                                        conferences.get(getConfNumber((saveBless.conference))).confTeams.get(ctv).teamPrestige += 5; // Welcome to the Small Screen, bay bee!
                                    }
                                }

                            }

                            //We didn't use either of the above stories, so now see if it's just the 20% chance that failed
                            else if(!findTeamAbbr(saveBless.abbr).confTVDeal && !findTeamAbbr(saveBless.abbr).teamTVDeal){ //If no conf tv deal and no team tv deal -- Congrats, the saveBless.name Sports Network is born!
                                if(saveBless.abbr.equals("USC") || saveBless.abbr.equals("UCLA")){//Different story for LA or Hollywood cause...well they're LA and Hollywood and they're inking a TV deal
                                    String holOrUlaTVName; //Different channel names for different schools
                                    if(saveBless.abbr.equals("USC")){ //Hollywood St. Sportsnet -- LA Fans know and loathe the inspiration for this one
                                        holOrUlaTVName = "USC announced today, that it will launch it's own exclusive athletics channel 'USC Sportsnet'.";
                                    }
                                    else{ // ULA All Access, cause it's alliterative and catchy
                                        holOrUlaTVName = "UCLA announced that 'UCLA All Access', a channel dedicated exclusively to hosting UCLA Athletics content, is set to begin broadcasting.";
                                    }
                                    newsStories.get(0).add(saveBless.name + " Takes Advantage of Non-Athletic Local Talents>In a move that left everyone around the country wondering \"what took so long?\", " + holOrUlaTVName + " The channel goes live next week, with the first broadcast expected to be " + findTeamAbbr(saveBless.abbr).gameSchedule.get(0).awayTeam.name + " at " + findTeamAbbr(saveBless.abbr).gameSchedule.get(0).homeTeam.name + ". " + saveBless.name + " cited the wealth of local talent and expertise in the broadcasting industry as major catalysts for getting the network off the ground and on the air.");
                                }
                                else {//Any other school
                                    newsStories.get(0).add(saveBless.name + " Does Its Best USC Impression>In a move that's expected to greatly boost the national awareness and recruiting reach of " + saveBless.name + ", the university announced that it's ready to go live with its first foray into national broadcasting in the form of a university specific athletics television network. The school has remained hush on many details surrounding the network, but the channel is expected to go live next week when a name and availability will be announced at the conclusion of the final fall practice of the year.");
                                    findTeamAbbr(saveBless.abbr).teamTVDeal = true;
                                }
                            }

                            else { //There's a Conf TV deal, or a Team TV deal, or both, or the chance to form the Conf network failed, so, pull another story
                                i--;
                                break;
                            }
                            break;


                        default:
                            i--;
                            System.out.println("Error in selecting bless story (got default case), retrying...");
                            break;
                    }
                }

            }

            if (saveCurse != null) {
                storyFullName = getRandName();
                storyFirstName = storyFullName.replaceAll(" .*", "");
                storyLastName = storyFullName.replaceAll(".* ", "");
                String storyPlayer;

                for (int i = 0; i < 1; i++){
                    switch((int)(Math.random() * 7)) { //Change the number Math.random is multiplied by to the number of cases (so last case # + 1)
                        case 0:
                            //Team broke the rules, placed on probation and it's harder to recruit (-prestige)
                            newsStories.get(0).add(saveCurse.name + " Rocked by Infractions Scandal!>After an investigation during the offseason, " + saveCurse.name + " has been placed on probation and assigned on-campus vistation limits for recruits. Athletic Director " + storyFullName + " released a statment vowing that the institution would work to repair the damage done to its prestige.");
                            break;

                        case 1:
                            //Sleepover w/ star recruit
                            newsStories.get(0).add(saveCurse.name + " Coach Redefines \"Strange Bed Fellows\">" + saveCurse.name + " Head Coach " + storyFullName + " has landed in hot water after he was discovered at the home of a Class of " + (getYear() + 2) + " recruit, having a sleepover. Family of the recruit, who's name has been withheld, state that Coach " + storyLastName + " and the recruit \"watched GetPix and chilled.\" Despite a lack of charges against " + storyLastName + ", the university has placed an indefinite suspension to the coach's recruiting travel privileges, pending an internal investigation.");
                            break;

                        case 2:
                            //Get the first offensive position player that isn't a Freshman and is good enough to be a decent starter and use their name to describe them pulling a reverse catfish...literally
                            //If no suitable players, decrement i and break so that the loop can try again to find a good story
                            String playerGFSchool;
                            if (saveCurse.getQB(0).year >= 2 && saveCurse.getQB(0).ratOvr > 85)
                                storyPlayer = saveCurse.getQB(0).name;
                            else if (saveCurse.getRB(0).year >= 2 && saveCurse.getRB(0).ratOvr > 79)
                                storyPlayer = saveCurse.getRB(0).name;
                            else if (saveCurse.getWR(0).year >= 2 && saveCurse.getWR(0).ratOvr > 79)
                                storyPlayer = saveCurse.getWR(0).name;
                            else if (saveCurse.getRB(1).year >= 2 && saveCurse.getRB(1).ratOvr > 79)
                                storyPlayer = saveCurse.getRB(1).name;
                            else if (saveCurse.getWR(1).year >= 2 && saveCurse.getWR(1).ratOvr > 79)
                                storyPlayer = saveCurse.getWR(1).name;
                            else if (saveCurse.getWR(2).year >= 2 && saveCurse.getWR(2).ratOvr > 79)
                                storyPlayer = saveCurse.getWR(2).name;
                            else {
                                i--;
                                break;
                            }

                            //If the cursed team is Indiana, the gf's school was American Samoa and vice versa, otherwise gf school is random (and potentially the same as cursed school)
                            if (saveCurse.abbr.equals("KAN")) playerGFSchool = "Indiana";
                            else if (saveCurse.abbr.equals("IND")) playerGFSchool = "Kansas";
                            else playerGFSchool = teamList.get((int) (Math.random() * 60)).name;
                            String storyPlayerLast = storyPlayer.replaceAll(".* ", "");

                            newsStories.get(0).add(saveCurse.name + " Star Demonstrates The Rare \"Reverse Catfish\">After winning the nation's heart by finishing out the " + (getYear() - 1) + " season despite losing his girlfriend to a freak fishing accident, " + saveCurse.name + " star " + storyPlayer + " now faces intense scrutiny from national media for allegedly making the whole thing up. " + storyPlayerLast + " originally claimed his girlfriend was a student at " + playerGFSchool + ", until internet message board users discovered a private blog run by the player that revealed the truth; the girlfriend was fake, and her name was actually the name of his pet catfish. The university's athletics department officially declined to comment, citing an ongoing internal investigation.");
                            break;

                        case 3:
                            //Hazing rituals by upperclassmen reported by underclassmen and scared off recruits -- Curse Developing #1

                            //Figure out if the recruit scared off was a RS (if the cursed team is rivals with the user team) or a FR
                            String starRSOrFR;

                            //Does the cursed team's rivals have an RS players and is the best RS an overall better player than the best FR
                            if (findTeamAbbr(saveCurse.rivalTeam).teamRSs.size() > 0 && findTeamAbbr(saveCurse.rivalTeam).teamFRs.size() > 0 &&
                                    (findTeamAbbr(saveCurse.rivalTeam).teamRSs.get(0).ratOvr >= findTeamAbbr(saveCurse.rivalTeam).teamFRs.get(0).ratOvr)) {
                                // If so, the scared off player will be the RS
                                starRSOrFR = ("highly sought after recruit and current " + findTeamAbbr(saveCurse.rivalTeam).name + " redshirt freshman " + findTeamAbbr(saveCurse.rivalTeam).teamRSs.get(0).name);
                            } else { // Grab the best Freshman on the rival's team
                                if (findTeamAbbr(saveCurse.rivalTeam).teamFRs.size() > 0 )
                                    starRSOrFR = (findTeamAbbr(saveCurse.rivalTeam).name + "'s star freshman recruit " + findTeamAbbr(saveCurse.rivalTeam).teamFRs.get(0).name);
                                else starRSOrFR = (findTeamAbbr(saveCurse.rivalTeam).name + "'s star freshman recruit " + getRandName());
                            }

                            //Now that we know what recruit was scared off to the rival team
                            Player srCurseTeam;
                            if (saveCurse.teamSRs.size() > 0) srCurseTeam = saveCurse.teamSRs.get(0);
                            else srCurseTeam = saveCurse.teamQBs.get(0);
                            newsStories.get(0).add("A New Kind of Summer Haze>" + saveCurse.name + " Senior " + srCurseTeam.position + " " + srCurseTeam.name + " stepped forward today, as the ringleader of a group of upperclassmen responsible for the extreme hazing of several of the program's underclassmen, including several non-player students. It was revealed earlier this year that " + starRSOrFR + " flipped his commitment from " + saveCurse.name + " after being contacted on social media by members of the group and told to \"prepare\" for the hazing he would face leading up to Spring Practice. There is currently no word on what punishment Coach " + storyFullName + " will hand out to the group.");
                            curseDevelopingStory = true;
                            curseDevelopingWeek = 0; // Print a new story after the week 1 games about the lack of punishment by coach
                            curseDevelopingCase = 1; // First developing curse story
                            break;

                        case 4:
                            //Coach angers boosters -- Inspired a little bit by Sark, a little bit by "I'M A MAN, I'M 40", and also a little bit by how much Chip hated the political game
                            newsStories.get(0).add("Coach Tries, Fails, to Shield Team from Booster Politics>" + saveCurse.name + " Head Coach " + storyFullName + " is making headlines this week for launching into an expletive filled tirade directed at athletics boosters at a private \"Boosters Only\" event. " + storyLastName + " was set off when a particular booster asked for star quarterback " + saveCurse.getQB(0).name + "'s phone number and began chastising the audience for \"caring too much about a bunch of kids playing football.\" Athletic Director " + getRandName() + " released a statement stating \"The Athletics Department appreciates the support of all fans of all " + saveCurse.name + " sports, and we will be working with " + storyFirstName + " to help him understand that.\"");
                            break;

                        case 5:
                            //Huge team fight that coaches were slow to dispel and now is bleeding over into the season -- Curse Developing #2

                            String saveCurseGameOneOpp = null;

                            //Get cursed team's first opponent of the year
                            if (saveCurse.gameSchedule.get(0).homeTeam == saveCurse){
                                saveCurseGameOneOpp = saveCurse.gameSchedule.get(0).awayTeam.name;
                            }
                            else if (saveCurse.gameSchedule.get(0).awayTeam == saveCurse) {
                                saveCurseGameOneOpp = saveCurse.gameSchedule.get(0).homeTeam.name;
                            }

                            newsStories.get(0).add("Collegiate Boxing Returns to " + saveCurse.name + "'s Locker Room>Sources inside the football program at " + saveCurse.name + " have reported that an offseason dispute between between starting quarterback " + saveCurse.teamQBs.get(0).name + " and top wide receiver " + saveCurse.teamWRs.get(1).name + " was left to fester over the summer and finally came to blows this afternoon. When contacted for comment, Head Coach " + storyFullName + " said only that he was \"aware of an issue within the team\" and that he will be \"looking into the matter further.\" " + saveCurse.name + " kicks their season off against " + saveCurseGameOneOpp + " next Saturday.");
                            curseDevelopingStory = true;
                            curseDevelopingWeek = 0;
                            curseDevelopingCase = 2; //Developing story will be about performance of QB and WR in Week 1

                            break;

                        case 6:
                            //Academic Scandal -- Uni was falsifying grades (you-can't-do-that clap, clap, clapclapclap)
                            newsStories.get(0).add(saveCurse.name + "'s Reputation Shaken by Fake Grading Scandal>" + saveCurse.name + " has announced the suspension of several university administrators pending an internal investigation into the falsification of grades for student athletes that were on the border of academic eligibility. Third party investigators uncovered the grading scheme after being asked to look into why several graduating players could not read the instructions provided with their Wonderlic Tests. Recruits from as far out as the class of " + (getYear() + 2) + " have rescinded verbal commitments, citing their desire to explore their options further. Currently, no academic or athletic sactions have been announced for the school.");
                            break;

                        default:
                            i--;
                            System.out.println("Error in selecting curse story (got default case), retrying...");
                            break;
                    }
                }

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
     * Gets whether it is hard mode.
     * Returns true is hard, false if normal.
     * @return difficulty
     */
    public boolean isHardMode() {
        return isHardMode;
    }

    /**
     * Get conference nmber from string
     * @param conf conference name
     * @return int of number 0-5
     */
    public int getConfNumber(String conf) {
        if (conf.equals("ACC")) return 0;
        if (conf.equals("American")) return 1;
        if (conf.equals("Big Ten")) return 2;
        if (conf.equals("Big 12")) return 3;
        if (conf.equals("Conf USA")) return 4;
        if (conf.equals("MAC")) return 5;
        if (conf.equals("Mt West")) return 6;
        if (conf.equals("PAC-10")) return 7;
        if (conf.equals("SEC")) return 8;
        if (conf.equals("Sun Belt")) return 9;
        return 0;
    }

    /**
     * Plays week. If normal week, handled by conferences. If bowl week, handled here.
     */
    public void playWeek() {
        if ( currentWeek <= 12 ) {
            for (int i = 0; i < conferences.size(); ++i) {
                conferences.get(i).playWeek();
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

        // If there was a developing story and it's time for that story to print, print it based on which story was triggered
        if (blessDevelopingStory){

            switch (blessDevelopingCase) { // Which story was triggered?

                case 1: //Sports Drink Development
                    if (blessDevelopingWeek == currentWeek) { // Is it time to print this yet?
                        if (findTeamAbbr(saveBless.abbr).rankTeamPollScore > 49) {
                            //Looks like the "secret stuff" didn't do much -- Maybe it was just water all along?
                            newsStories.get(blessDevelopingWeek + 1).add(saveBless.name + " Still Thirsty For Wins>Despite the much talked about launch of their new Sport Enhancement Drink, " + saveBless.name + " still find themselves struggling to make the most of the talent available to them and break free from the bottom of the polls. With their eyes set on improvement in the years to come, all " + saveBless.abbr + " fans can do now is weather the drought.");
                        } //Electrolytes: saveBless.name currentYear MVP
                        else if (findTeamAbbr(saveBless.abbr).rankTeamPollScore < 41 && findTeamAbbr(saveBless.abbr).rankTeamPollScore > 20) {
                            newsStories.get(blessDevelopingWeek + 1).add("Success a Refreshing Change for " + saveBless.name + ">On the heels of a successful first week of sales for their new Sports Enhancement Drink, " + saveBless.name + "'s has much to celebrate as they seem to have found the light at the end of the tunnel. In less than a season, the program's fortunes have turned, both financially and in the polls, begging the question: What are they putting in those sports drinks?");
                        } //The team is suddenly in the top 20 and wondering if they had it in themselves all along. The school is rolling in $$$$
                        else if (findTeamAbbr(saveBless.abbr).rankTeamPollScore <= 20) {
                            newsStories.get(blessDevelopingWeek + 1).add(saveBless.name + " Being Propelled to New Heights>In the middle of a football season that is smashing all expectations, " + saveBless.name + " is managing to smash a few sales records, as well. Crediting both realms of success to the school's Sports Nutrition program, Athletic Director " + getRandName() + " praised the work of the program's graduate researchers in developing a world class sport enhancement drink, while also announcing the product's expansion into two new flavors.");
                        }
                    }
                    break;

                default:
                    //Oh man I'm not good with computer how did we get here?
                    break;
            }
        }

        if (curseDevelopingStory){
            switch (curseDevelopingCase){ //Which story was triggered?
                case 1: //Lack of punishment for hazing underclassmen
                    Player srCurseTeam;
                    if (saveCurse.teamSRs.size() > 0) srCurseTeam = saveCurse.teamSRs.get(0);
                    else srCurseTeam = saveCurse.teamQBs.get(0);
                    if (curseDevelopingWeek == currentWeek) { //Print this story when the time comes, but from the user's perspective, print it in the week prior to the current week (add it to week 1 when the player sees week 2 -- Once games are played, the week is advanced)
                        //No one missed playing time, no word from Coach
                        newsStories.get(curseDevelopingWeek+1).add(saveCurse.name + " Hazing Scandal Update>After last week's report on the " + saveCurse.name + " hazing scandal, the college football world waited to see what punishments would be handed out to " + srCurseTeam.name + " and other implicated but unnamed players. With Week 1 officially in the books we have an answer: Nothing. Based on the final fall practice depth chart, no players missed playing time or starting status (" + srCurseTeam.name.replace("*. ","") + " played every down he was available for). Coach " + storyLastName + " has remained silent on the issue.");
                    }
                    else if (curseDevelopingWeek+1 == currentWeek) { //Populate this story into Week 2 --before-- the games are played.

                        if (Math.random() < .5) { //"The boy's suffered enough"
                            newsStories.get(curseDevelopingWeek+2).add("No Punishment for Group in Hazing Scandal>Last week, it was reported that " + saveCurse.name + " Head Coach " + storyFullName + " had not commented on the hazing scandal that resulted in lost recruits for the program. Today, " + storyLastName + " revealed that this was no mistake, and that there will be no punishment for those involved. In a brief statement released today by the program, " + storyLastName + " is quoted as saying that he considers this matter closed and that he believes the public scrutiny " + srCurseTeam.name + " faced after admitting to being the ringleader of the hazing group was \"punishment enough.\"");
                        }
                        else { //Time to make amends
                            newsStories.get(curseDevelopingWeek+2).add("Punishment Announced for " + saveCurse.name + " Upperclassmen>In a statement released through its Athletics Department today, " +saveCurse.name+" Head Coach " + storyFullName + " announced that he had spoken with each member of the team privately and determined who the upperclassmen responsible for the over-the-top hazing occurring within the program were. Not wishing to draw further scrutiny to individual players, " +storyLastName+" stated that the group of players would be responsible for identifying the best way to give back to the local community and carrying out whatever volunteer work was necessary to see the project through to completion.");
                        }
                    }
                    break;
                case 2: //QB and WR had a fight, how did the first game go? -- More scenarios to be added later
                    if(curseDevelopingWeek == currentWeek){
                        PlayerQB cursedQB;
                        PlayerWR cursedWR;
                        PlayerWR cursedWR2;
                        PlayerWR cursedWR3;

                        if(saveCurse.gameSchedule.get(0).homeTeam == saveCurse){
                            cursedQB = findTeamAbbr(saveCurse.abbr).gameSchedule.get(0).homeTeam.getQB(0);
                            cursedWR = findTeamAbbr(saveCurse.abbr).gameSchedule.get(0).homeTeam.getWR(0);
                            cursedWR2 = findTeamAbbr(saveCurse.abbr).gameSchedule.get(0).homeTeam.getWR(1);
                            cursedWR3 = findTeamAbbr(saveCurse.abbr).gameSchedule.get(0).homeTeam.getWR(2);
                        }
                        else{
                            cursedQB = findTeamAbbr(saveCurse.abbr).gameSchedule.get(0).awayTeam.getQB(0);
                            cursedWR = findTeamAbbr(saveCurse.abbr).gameSchedule.get(0).awayTeam.getWR(0);
                            cursedWR2 = findTeamAbbr(saveCurse.abbr).gameSchedule.get(0).awayTeam.getWR(1);
                            cursedWR3 = findTeamAbbr(saveCurse.abbr).gameSchedule.get(0).awayTeam.getWR(2);
                        }
                        if (100*cursedQB.statsPassComp/Math.max(1,cursedQB.statsPassAtt) > 60 && cursedWR.statsTargets > cursedWR2.statsTargets && cursedWR.statsTargets > cursedWR3.statsTargets) {
                            if (findTeam((saveCurse.abbr)).wins == 0) {
                                //QB and WR still in sync, but the team lost
                                newsStories.get(curseDevelopingWeek + 1).add(saveCurse.name + " Locker Room Scuffle Affects Week 1 Performance>Despite managing to find their sync after a locker room altercation last week, quarterback " + cursedQB.name + " and wide receiver " + cursedWR.name + " still left a lasting negative impression in the minds and performance of their teammates. " + cursedQB.name + "'s " + cursedQB.statsPassYards + " yards and " + cursedWR.name + "'s " + cursedWR.statsReceptions + " receptions could not bring the rest of the team out of the funk that eventually saw them drop their season opener.");
                                break;
                            } else {
                                //Hey, boys will be boys, right? Team won and QB/WR still hooked up for a good game
                                newsStories.get(curseDevelopingWeek + 1).add("Water Under the Bridge at " + saveCurse.name + ">" + saveCurse.name + " didn't appear to remember the locker room altercation from last week nor the media attention it garnered as " + cursedQB.name + " threw for " + cursedQB.statsPassYards + " yards and " + cursedWR.name + " caught " + cursedWR.statsReceptions + " balls to help lift " + saveCurse.name + " in their season opener. " + cursedQB.name.replaceAll(".* ", "") + " still looked to his favorite target more than any other receiver, but all is still not perfectly well within the program.");
                                break;
                            }
                        }
                        else if(100*cursedQB.statsPassComp/Math.max(1,cursedQB.statsPassAtt) > 59){
                            //QB had a good game but didn't hit his old favorite more than other WRs
                            newsStories.get(curseDevelopingWeek+1).add("Team Unrest Continues at " + saveCurse.name + ">Quarterback " + cursedQB.name + " looked good in his season opener, throwing " + cursedQB.statsPassComp + " completions for " + cursedQB.statsPassYards + " yards, primarily to receivers not named " + cursedWR.name + ". Sources within the program have remained quiet since last week's locker room scuffle between the once tight QB-WR duo, but one thing is clear: " + cursedQB.name.replaceAll(".* ","") + " has not forgotten.");
                            break;
                        }
                        else if(100*cursedQB.statsPassComp/Math.max(1,cursedQB.statsPassAtt) > 44 && 100*cursedQB.statsPassComp/Math.max(1,cursedQB.statsPassAtt) < 60){
                            //QB was pretty much a non-factor
                            String winOrLoss;
                            if(findTeam((saveCurse.abbr)).wins == 0) winOrLoss = "loss";
                            else winOrLoss = "win";

                            newsStories.get(curseDevelopingWeek+1).add(cursedQB.name + " a Non-Factor in Season Opener>On the heels of a locker room fight that dominated national media and brought the leadership capabilities of the " + saveCurse.name + " coaching staff into question, starting quarterback " + cursedQB.name + " opened his season without much fanfare. Or much of anything. " + cursedQB.name.replaceAll(".* ","") + " threw for " + cursedQB.statsPassYards + " yards on " + cursedQB.statsPassComp + " for " + cursedQB.statsPassAtt + " passing, managing to look perfectly average in a season opening " + winOrLoss + ".");
                            break;
                        }

                        else{
                            //QB had a bad game and the team lost
                            if(findTeam((saveCurse.abbr)).wins == 0){
                                newsStories.get(curseDevelopingWeek + 1).add(cursedQB.name + " Fails to Find Rhythm in Season Opener>Just over a week after reports surfaced of a locker room fight between " + saveCurse.name + " quarterback " + cursedQB.name + " and his favorite target, wide receiver " + cursedWR.name + ", the two are back in the media spotlight for a lackluster performance in a season opening loss. Looking visibly disoriented and confused at times, " + cursedQB.name.replaceAll(".* ", "") + " went just " + cursedQB.statsPassComp + " for " + cursedQB.statsPassAtt + " passing, and failed to achieve any consistency in a losing effort.");
                                break;
                            }
                            else{ //QB looked crappy but the team managed to pull one out anyway
                                newsStories.get(curseDevelopingWeek+1).add(saveCurse.name + " Manage a Win Despite Poor QB Play>Appearing to still be caught up in the locker room drama of last week, " + cursedQB.name + " had to get by with a little help from his friends. Going " + cursedQB.statsPassComp + " for " + cursedQB.statsPassAtt + " while looking lost at times, " + cursedQB.name.replaceAll(".* ","") + "'s performance left a lot to be desired. Still, the damage done was not enough to surrender a loss, and " + saveCurse.name + " marches into Week 2 with a 1-0 record.");
                                break;
                            }

                        }
                    }
                    break; // this break happens every week that isn't curseDevelopingWeek


                default:
                    //We really shouldn't be here, turn back now!
                    break;

            }


        }

        currentWeek++;
    }

    /**
     * Schedules bowl games based on team rankings.
     */
    public void schedBowlGames() {
        //bowl week
        for (int i = 0; i < teamList.size(); ++i) {
            teamList.get(i).updatePollScore();
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
        //bowlNames = {"Lilac Bowl", "Apple Bowl", "Salty Bowl", "Salsa Bowl", "Mango Bowl",
        //             "Patriot Bowl", "Salad Bowl", "Frost Bowl", "Tropical Bowl", "Music Bowl"};
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

        bowlGames[13] = new Game( teamList.get(27), teamList.get(32), bowlNames[13] );
        teamList.get(27).gameSchedule.add(bowlGames[13]);
        teamList.get(32).gameSchedule.add(bowlGames[13]);
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
        String[] yearTop10 = new String[10];
        Team tt;
        for (int i = 0; i < 10; ++i) {
            tt = teamList.get(i);
            yearTop10[i] = tt.abbr + " (" + tt.wins + "-" + tt.losses + ")";
        }
        leagueHistory.add(yearTop10);
    }

    /**
     * Advances season for each team and sets up schedules for the new year.
     */
    public void advanceSeason() {
        currentWeek = 0;
        //updateTeamHistories();
        for (int t = 0; t < teamList.size(); ++t) {
            teamList.get(t).advanceSeason();
        }

        // Bless a random team with lots of prestige
        int blessNumber = (int)(Math.random()*9);
        Team blessTeam = teamList.get(50 + blessNumber);
//        if (!blessTeam.userControlled && !blessTeam.name.equals("American Samoa")) {
        blessTeam.teamPrestige += 25;
        saveBless = blessTeam;
        if (blessTeam.teamPrestige > 99) blessTeam.teamPrestige = 99;
//        }
//        else saveBless = null;

        //Curse a good team
        int curseNumber = (int)(Math.random()*7);
        Team curseTeam = teamList.get(3 + curseNumber);
        if (/*!curseTeam.userControlled && */curseTeam.teamPrestige > 80) {
            curseTeam.teamPrestige -= 25;
            saveCurse = curseTeam;
        }
        else saveCurse = null;

        // Advance win streaks
        advanceSeasonWinStreaks();

        for (int c = 0; c < conferences.size(); ++c) {
            conferences.get(c).robinWeek = 0;
            conferences.get(c).week = 0;
        }

        //set up schedule (not needed anymore?)
        for (int i = 0; i < conferences.size(); ++i ) {
            conferences.get(i).setUpSchedule();
        }
        for (int i = 0; i < conferences.size(); ++i ) {
            conferences.get(i).setUpOOCSchedule();
        }
        for (int i = 0; i < conferences.size(); ++i ) {
            conferences.get(i).insertOOCSchedule();
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
        return 2017 + leagueHistory.size();
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
        //       if (Math.random() > 0.0025) {
        int fn = (int) (Math.random() * nameList.size());
        int ln = (int) (Math.random() * lastNameList.size());
        return nameList.get(fn) + " " + lastNameList.get(ln);
        //      } else {
        //          return donationNames[ (int)(Math.random()*donationNames.length) ];
        //      }
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
                if (p.ratOvr > 85 && !p.position.equals("K")) allPlayersLeaving.add(p);
            }
        }

        Collections.sort(allPlayersLeaving, new PlayerComparator());

        // Get 64 players (first 2 rounds)
        ArrayList<Player> top64Players = new ArrayList<>(100);
        for (int i = 0; i < 100; ++i) {
            top64Players.add(allPlayersLeaving.get(i));
        }

        String[] nflPlayers = new String[ top64Players.size() ];
        for (int i = 0; i < nflPlayers.length; ++i) {
            nflPlayers[i] = top64Players.get(i).getMockDraftStr();
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
            case 2: Collections.sort( teams, new TeamCompSoW() );
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i+1) + "," + t.strRepWithBowlResults() + "," + t.teamStrengthOfWins);
                }
                break;
            case 3: Collections.sort( teams, new TeamCompPPG() );
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i+1) + "," + t.strRepWithBowlResults() + "," + (t.teamPoints/t.numGames()));
                }
                break;
            case 4: Collections.sort( teams, new TeamCompOPPG() );
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i+1) + "," + t.strRepWithBowlResults() + "," + (t.teamOppPoints/t.numGames()));
                }
                break;
            case 5: Collections.sort( teams, new TeamCompYPG() );
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i+1) + "," + t.strRepWithBowlResults() + "," + (t.teamYards/t.numGames()));
                }
                break;
            case 6: Collections.sort( teams, new TeamCompOYPG() );
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i+1) + "," + t.strRepWithBowlResults() + "," + (t.teamOppYards/t.numGames()));
                }
                break;
            case 7: Collections.sort( teams, new TeamCompPYPG() );
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i+1) + "," + t.strRepWithBowlResults() + "," + (t.teamPassYards/t.numGames()));
                }
                break;
            case 8: Collections.sort( teams, new TeamCompRYPG() );
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i+1) + "," + t.strRepWithBowlResults() + "," + (t.teamRushYards/t.numGames()));
                }
                break;
            case 9: Collections.sort( teams, new TeamCompOPYPG() );
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i+1) + "," + t.strRepWithBowlResults() + "," + (t.teamOppPassYards/t.numGames()));
                }
                break;
            case 10: Collections.sort( teams, new TeamCompORYPG() );
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i+1) + "," + t.strRepWithBowlResults() + "," + (t.teamOppRushYards/t.numGames()));
                }
                break;
            case 11: Collections.sort( teams, new TeamCompTODiff() );
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    if (t.teamTODiff > 0) rankings.add(t.getRankStrStarUser(i+1) + "," + t.strRepWithBowlResults() + ",+" + t.teamTODiff);
                    else rankings.add(t.getRankStrStarUser(i+1) + "," + t.strRepWithBowlResults() + "," + t.teamTODiff);
                }
                break;
            case 12: Collections.sort( teams, new TeamCompOffTalent() );
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i+1) + "," + t.strRepWithBowlResults() + "," + t.teamOffTalent);
                }
                break;
            case 13: Collections.sort( teams, new TeamCompDefTalent() );
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i+1) + "," + t.strRepWithBowlResults() + "," + t.teamDefTalent);
                }
                break;
            case 14: Collections.sort( teams, new TeamCompPrestige() );
                for (int i = 0; i < teams.size(); ++i) {
                    t = teams.get(i);
                    rankings.add(t.getRankStrStarUser(i + 1) + "," + t.strRepWithBowlResults() + "," + t.teamPrestige);
                }
                break;
            case 15: Collections.sort( teams, new TeamCompRecruitClass() );
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
            hist += (2017+i) + ":\n";
            hist += "\tChampions: " + leagueHistory.get(i)[0] + "\n";
            hist += "\tPOTY: " + heismanHistory.get(i) + "\n%";
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
            teams[i] = teamList.get(i).conference + ": " +
                    teamList.get(i).name + ", Pres: " + teamList.get(i).teamPrestige;
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
            t2 = teamList.get(32);
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

    /**
     * See if team name is in use, or has illegal characters.
     * @param name team name
     * @return true if valid, false if not
     */
    public boolean isNameValid(String name) {
        if (name.length() == 0) {
            return false;
        }

        if (name.contains(",") || name.contains(">") || name.contains("%") || name.contains("\\")) {
            // Illegal character!
            return false;
        }

        for (int i = 0; i < teamList.size(); i++) {
            // compare using all lower case so no dumb duplicates
            if (teamList.get(i).name.toLowerCase().equals(name.toLowerCase()) &&
                    !teamList.get(i).userControlled) {
                return false;
            }
        }

        return true;
    }

    /**
     * See if team abbr is in use, or has illegal characters, or is not 3 characters
     * @param abbr new abbr
     * @return true if valid, false if not
     */
    public boolean isAbbrValid(String abbr) {
        if (abbr.length() > 3 || abbr.length() == 0) {
            // Only 3 letter abbr allowed
            return false;
        }

        if (abbr.contains(",") || abbr.contains(">") || abbr.contains("%") || abbr.contains("\\") || abbr.contains(" ")) {
            // Illegal character!
            return false;
        }

        for (int i = 0; i < teamList.size(); i++) {
            if (teamList.get(i).abbr.equals(abbr) &&
                    !teamList.get(i).userControlled) {
                return false;
            }
        }

        return true;
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
        if (isHardMode) {
            sb.append((2017 + leagueHistory.size()) + ": " + userTeam.abbr + " (" + (userTeam.totalWins - userTeam.wins) + "-" + (userTeam.totalLosses - userTeam.losses) + ") " +
                    userTeam.totalCCs + " CCs, " + userTeam.totalNCs + " NCs>[HARD]%\n");
        } else {
            sb.append((2017 + leagueHistory.size()) + ": " + userTeam.abbr + " (" + (userTeam.totalWins - userTeam.wins) + "-" + (userTeam.totalLosses - userTeam.losses) + ") " +
                    userTeam.totalCCs + " CCs, " + userTeam.totalNCs + " NCs>[EASY]%\n");
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
        if (saveCurse != null) {
            sb.append(saveCurse.abbr + "\n");
            sb.append("END_CURSE_TEAM\n");
        } else {
            sb.append("NULL\n");
            sb.append("END_CURSE_TEAM\n");
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






