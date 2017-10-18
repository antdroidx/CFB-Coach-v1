package CFBsimPack;

import java.io.Serializable;
import java.util.Collections;
import java.util.Random;


/**
 * Class for storing games. Has all stats for the game.
 * Also is responsible for simming the game.
 *
 * @author Achi
 */
public class Game implements Serializable {

    public League league;

    public Team homeTeam;
    public Team awayTeam;

    public boolean hasPlayed;

    public String gameName;

    public int homeScore;
    public int[] homeQScore;
    public int awayScore;
    public int[] awayQScore;
    public int homeYards;
    public int awayYards;

    public int numOT;
    public int homeTOs;
    public int awayTOs;

    public int[] HomeQBStats;
    public int[] AwayQBStats;

    public int[] HomeRB1Stats;
    public int[] HomeRB2Stats;
    public int[] AwayRB1Stats;
    public int[] AwayRB2Stats;

    public int[] HomeWR1Stats;
    public int[] HomeWR2Stats;
    public int[] HomeWR3Stats;
    public int[] AwayWR1Stats;
    public int[] AwayWR2Stats;
    public int[] AwayWR3Stats;

    public int[] HomeTEStats;
    public int[] AwayTEStats;

    public int[] HomeKStats;
    public int[] AwayKStats;

    public int[] HomeDL1Stats;
    public int[] HomeDL2Stats;
    public int[] HomeDL3Stats;
    public int[] HomeDL4Stats;
    public int[] AwayDL1Stats;
    public int[] AwayDL2Stats;
    public int[] AwayDL3Stats;
    public int[] AwayDL4Stats;

    public int[] HomeLB1Stats;
    public int[] HomeLB2Stats;
    public int[] HomeLB3Stats;
    public int[] AwayLB1Stats;
    public int[] AwayLB2Stats;
    public int[] AwayLB3Stats;

    public int[] HomeCB1Stats;
    public int[] HomeCB2Stats;
    public int[] HomeCB3Stats;
    public int[] AwayCB1Stats;
    public int[] AwayCB2Stats;
    public int[] AwayCB3Stats;

    public int[] HomeSStats;
    public int[] AwaySStats;


    // Store reference to players, in case starters are changed later		
    private PlayerQB homeQB;
    private PlayerRB[] homeRBs;
    private PlayerWR[] homeWRs;
    private PlayerTE homeTE;
    private PlayerK homeK;
    private PlayerDL[] homeDLs;
    private PlayerLB[] homeLBs;
    private PlayerCB[] homeCBs;
    private PlayerS homeS;

    private PlayerQB awayQB;
    private PlayerRB[] awayRBs;
    private PlayerWR[] awayWRs;
    private PlayerTE awayTE;
    private PlayerK awayK;
    private PlayerDL[] awayDLs;
    private PlayerLB[] awayLBs;
    private PlayerCB[] awayCBs;
    private PlayerS awayS;

    String gameEventLog;
    String tdInfo;

    //private variables used when simming games
    private int gameTime;
    private boolean gamePoss; //1 if home, 0 if away
    private int gameYardLine;
    private int gameDown;
    private int gameYardsNeed;
    private boolean playingOT;
    private boolean bottomOT;

    private int timePerPlay = 21;

    Random rand = new Random();

    /**
     * Create game with a name (likely a bowl game).
     *
     * @param home
     * @param away
     * @param name
     */
    public Game(Team home, Team away, String name) {
        homeTeam = home;
        awayTeam = away;

        gameName = name;

        homeScore = 0;
        homeQScore = new int[10];
        awayScore = 0;
        awayQScore = new int[10];
        numOT = 0;

        homeTOs = 0;
        awayTOs = 0;

        //initialize arrays, set everything to zero
        HomeQBStats = new int[10];
        AwayQBStats = new int[10];

        HomeRB1Stats = new int[4];
        HomeRB2Stats = new int[4];
        AwayRB1Stats = new int[4];
        AwayRB2Stats = new int[4];

        HomeWR1Stats = new int[6];
        HomeWR2Stats = new int[6];
        HomeWR3Stats = new int[6];
        AwayWR1Stats = new int[6];
        AwayWR2Stats = new int[6];
        AwayWR3Stats = new int[6];

        HomeTEStats = new int[6];
        AwayTEStats = new int[6];

        HomeKStats = new int[6];
        AwayKStats = new int[6];

        HomeDL1Stats = new int[4];
        HomeDL2Stats = new int[4];
        HomeDL3Stats = new int[4];
        HomeDL4Stats = new int[4];
        AwayDL1Stats = new int[4];
        AwayDL2Stats = new int[4];
        AwayDL3Stats = new int[4];
        AwayDL4Stats = new int[4];

        HomeLB1Stats = new int[4];
        HomeLB2Stats = new int[4];
        HomeLB3Stats = new int[4];
        AwayLB1Stats = new int[4];
        AwayLB2Stats = new int[4];
        AwayLB3Stats = new int[4];

        HomeCB1Stats = new int[4];
        HomeCB2Stats = new int[4];
        HomeCB3Stats = new int[4];
        AwayCB1Stats = new int[4];
        AwayCB2Stats = new int[4];
        AwayCB3Stats = new int[4];

        HomeSStats = new int[4];
        AwaySStats = new int[4];

        //playGame();
        hasPlayed = false;

        if (gameName.equals("Conference") && (homeTeam.rivalTeam.equals(awayTeam.abbr) || awayTeam.rivalTeam.equals(homeTeam.abbr))) {
            // Rivalry game!
            gameName = "Rivalry Game";
        }
    }

    /**
     * Create a game without a name (regular season game).
     *
     * @param home home team playing
     * @param away away team playing
     */
    public Game(Team home, Team away) {
        homeTeam = home;
        awayTeam = away;
        numOT = 0;
        homeTOs = 0;
        awayTOs = 0;

        gameName = "";

        homeScore = 0;
        homeQScore = new int[10];
        awayScore = 0;
        awayQScore = new int[10];

        //initialize arrays, set everything to zero
        HomeQBStats = new int[10];
        AwayQBStats = new int[10];

        HomeRB1Stats = new int[4];
        HomeRB2Stats = new int[4];
        AwayRB1Stats = new int[4];
        AwayRB2Stats = new int[4];

        HomeWR1Stats = new int[6];
        HomeWR2Stats = new int[6];
        HomeWR3Stats = new int[6];
        AwayWR1Stats = new int[6];
        AwayWR2Stats = new int[6];
        AwayWR3Stats = new int[6];

        HomeTEStats = new int[6];
        AwayTEStats = new int[6];

        HomeKStats = new int[6];
        AwayKStats = new int[6];

        HomeDL1Stats = new int[4];
        HomeDL2Stats = new int[4];
        HomeDL3Stats = new int[4];
        HomeDL4Stats = new int[4];
        AwayDL1Stats = new int[4];
        AwayDL2Stats = new int[4];
        AwayDL3Stats = new int[4];
        AwayDL4Stats = new int[4];

        HomeLB1Stats = new int[4];
        HomeLB2Stats = new int[4];
        HomeLB3Stats = new int[4];
        AwayLB1Stats = new int[4];
        AwayLB2Stats = new int[4];
        AwayLB3Stats = new int[4];

        HomeCB1Stats = new int[4];
        HomeCB2Stats = new int[4];
        HomeCB3Stats = new int[4];
        AwayCB1Stats = new int[4];
        AwayCB2Stats = new int[4];
        AwayCB3Stats = new int[4];

        HomeSStats = new int[4];
        AwaySStats = new int[4];

        //playGame();
        hasPlayed = false;

    }

    /**
     * Gets the game summary, along with all the stats and the game log, to be used by the UI.
     *
     * @return A String array of the left, center, right, and logs.
     */
    public String[] getGameSummaryStr() {
        /**
         * [0] is left side
         * [1] is center
         * [2] is right
         * [3] is bottom (game log)
         */
        String[] gameSum = new String[4];
        StringBuilder gameL = new StringBuilder();
        StringBuilder gameC = new StringBuilder();
        StringBuilder gameR = new StringBuilder();

        gameL.append("\nPoints\nYards\nPass Yards\nRush Yards\nTOs\n\n");
        gameC.append("#" + awayTeam.rankTeamPollScore + " " + awayTeam.abbr + "\n" + awayScore + "\n" + awayYards + " yds\n" +
                getPassYards(true) + " pyds\n" + getRushYards(true) + " ryds\n" + awayTOs + " TOs\n\n");
        gameR.append("#" + homeTeam.rankTeamPollScore + " " + homeTeam.abbr + "\n" + homeScore + "\n" + homeYards + " yds\n" +
                getPassYards(false) + " pyds\n" + getRushYards(false) + " ryds\n" + homeTOs + " TOs\n\n");

        gameL.append("\n");
        gameC.append("\n");
        gameR.append("\n");
        gameL.append("OFFENSE\n");
        gameC.append(awayTeam.teamStratOff.getStratName() + "\n");
        gameR.append(homeTeam.teamStratOff.getStratName() + "\n");
        gameL.append("\n");
        gameC.append("\n");
        gameR.append("\n");

        gameL.append("- - -\n");
        gameC.append("Passing\n");
        gameR.append("- - -\n");

        //Quarterbacks
        gameL.append("\n");
        gameC.append("\n");
        gameR.append("\n");
        gameL.append("QB\nYr Ovr\nTD/Int\nPass Yards\nComp/Att\nRush Yards\nTD/Fum\nSacks\n");
        gameC.append(awayQB.getInitialName() + "\n");
        gameC.append(awayQB.getYrStr() + " " + awayQB.ratOvr + "\n");
        gameC.append(AwayQBStats[2] + "/" + AwayQBStats[3] + "\n"); //td/int
        gameC.append(AwayQBStats[4] + " yds\n"); //pass yards
        gameC.append(AwayQBStats[0] + "/" + AwayQBStats[1] + "\n"); //pass comp/att
        gameC.append(AwayQBStats[7] + "yds\n"); //rush
        gameC.append(AwayQBStats[8] + "/" + AwayQBStats[9] + "\n"); //rush td/fum
        gameC.append(AwayQBStats[5] + " sacks\n"); //rush td/fum
        gameR.append(homeQB.getInitialName() + "\n");
        gameR.append(homeQB.getYrStr() + " " + homeQB.ratOvr + "\n");
        gameR.append(HomeQBStats[2] + "/" + HomeQBStats[3] + "\n"); //td/int
        gameR.append(HomeQBStats[4] + " yds\n"); //pass yards
        gameR.append(HomeQBStats[0] + "/" + HomeQBStats[1] + "\n"); //pass comp/att
        gameR.append(HomeQBStats[7] + " yds/\n"); //rushing
        gameR.append(HomeQBStats[8] + "/" + HomeQBStats[9] + "\n"); //rush td/fum
        gameR.append(HomeQBStats[5] + " sacks\n"); //rush td/fum
        gameL.append("\n");
        gameC.append("\n");
        gameR.append("\n");
        gameL.append("\n");
        gameC.append("\n");
        gameR.append("\n");

        gameL.append("- - -\n");
        gameC.append("Rushing\n");
        gameR.append("- - -\n");

        //Running Backs
        gameL.append("\n");
        gameC.append("\n");
        gameR.append("\n");
        gameL.append("RB\nYr Ovr\nTD/Fum\nRush Yards\nYds/Att\n");
        gameC.append(awayRBs[0].getInitialName() + "\n");
        gameC.append(awayRBs[0].getYrStr() + " " + awayRBs[0].ratOvr + "\n");
        gameC.append(AwayRB1Stats[2] + "/" + AwayRB1Stats[3] + "\n"); //td/fum
        gameC.append(AwayRB1Stats[1] + " yds\n"); //rush yards
        gameC.append(((double) (10 * AwayRB1Stats[1] / AwayRB1Stats[0]) / 10) + "\n");
        gameR.append(homeRBs[0].getInitialName() + "\n");
        gameR.append(homeRBs[0].getYrStr() + " " + homeRBs[0].ratOvr + "\n");
        gameR.append(HomeRB1Stats[2] + "/" + HomeRB1Stats[3] + "\n"); //td/fum
        gameR.append(HomeRB1Stats[1] + " yds\n"); //rush yards
        gameR.append(((double) (10 * HomeRB1Stats[1] / HomeRB1Stats[0]) / 10) + "\n");
        gameL.append("\n");
        gameC.append("\n");
        gameR.append("\n");
        gameL.append("RB\nYr Ovr\nTD/Fum\nRush Yards\nYds/Att\n");
        gameC.append(awayRBs[1].getInitialName() + "\n");
        gameC.append(awayRBs[1].getYrStr() + " " + awayRBs[1].ratOvr + "\n");
        gameC.append(AwayRB2Stats[2] + "/" + AwayRB2Stats[3] + "\n"); //td/fum
        gameC.append(AwayRB2Stats[1] + " yds\n"); //rush yards
        gameC.append(((double) (10 * AwayRB2Stats[1] / AwayRB2Stats[0]) / 10) + "\n");
        gameR.append(homeRBs[1].getInitialName() + "\n");
        gameR.append(homeRBs[1].getYrStr() + " " + homeRBs[1].ratOvr + "\n");
        gameR.append(HomeRB2Stats[2] + "/" + HomeRB2Stats[3] + "\n"); //td/fum
        gameR.append(HomeRB2Stats[1] + " yds\n"); //rush yards
        gameR.append(((double) (10 * HomeRB2Stats[1] / HomeRB2Stats[0]) / 10) + "\n");
        gameL.append("\n");
        gameC.append("\n");
        gameR.append("\n");
        gameL.append("\n");
        gameC.append("\n");
        gameR.append("\n");

        gameL.append("- - -\n");
        gameC.append("Receiving\n");
        gameR.append("- - -\n");
        //WR
        gameL.append("\n");
        gameC.append("\n");
        gameR.append("\n");
        gameL.append("WR\nYr Ovr\nTD/Fum\nRec Yards\nRec/Tgts\n");
        gameC.append(awayWRs[0].getInitialName() + "\n");
        gameC.append(awayWRs[0].getYrStr() + " " + awayWRs[0].ratOvr + "\n");
        gameC.append(AwayWR1Stats[3] + "/" + AwayWR1Stats[5] + "\n"); //td/fum
        gameC.append(AwayWR1Stats[2] + " yds\n"); //rec yards
        gameC.append(AwayWR1Stats[0] + "/" + AwayWR1Stats[1] + "\n"); //rec/targets
        gameR.append(homeWRs[0].getInitialName() + "\n");
        gameR.append(homeWRs[0].getYrStr() + " " + homeWRs[0].ratOvr + "\n");
        gameR.append(HomeWR1Stats[3] + "/" + HomeWR1Stats[5] + "\n"); //td/fum
        gameR.append(HomeWR1Stats[2] + " yds\n"); //rec yards
        gameR.append(HomeWR1Stats[0] + "/" + HomeWR1Stats[1] + "\n"); //rec/targets
        gameL.append("\n");
        gameC.append("\n");
        gameR.append("\n");
        gameL.append("WR\nYr Ovr\nTD/Fum\nRec Yards\nRec/Tgts\n");
        gameC.append(awayWRs[1].getInitialName() + "\n");
        gameC.append(awayWRs[1].getYrStr() + " " + awayWRs[1].ratOvr + "\n");
        gameC.append(AwayWR2Stats[3] + "/" + AwayWR2Stats[5] + "\n"); //td/fum
        gameC.append(AwayWR2Stats[2] + " yds\n"); //rec yards
        gameC.append(AwayWR2Stats[0] + "/" + AwayWR2Stats[1] + "\n"); //rec/targets
        gameR.append(homeWRs[1].getInitialName() + "\n");
        gameR.append(homeWRs[1].getYrStr() + " " + homeWRs[1].ratOvr + "\n");
        gameR.append(HomeWR2Stats[3] + "/" + HomeWR2Stats[5] + "\n"); //td/fum
        gameR.append(HomeWR2Stats[2] + " yds\n"); //rec yards
        gameR.append(HomeWR2Stats[0] + "/" + HomeWR2Stats[1] + "\n"); //rec/targets
        gameL.append("\n");
        gameC.append("\n");
        gameR.append("\n");
        gameL.append("WR\nYr Ovr\nTD/Fum\nRec Yards\nRec/Tgts\n");
        gameC.append(awayWRs[2].getInitialName() + "\n");
        gameC.append(awayWRs[2].getYrStr() + " " + awayWRs[2].ratOvr + "\n");
        gameC.append(AwayWR3Stats[3] + "/" + AwayWR3Stats[5] + "\n"); //td/fum
        gameC.append(AwayWR3Stats[2] + " yds\n"); //rec yards
        gameC.append(AwayWR3Stats[0] + "/" + AwayWR3Stats[1] + "\n"); //rec/targets
        gameR.append(homeWRs[2].getInitialName() + "\n");
        gameR.append(homeWRs[2].getYrStr() + " " + homeWRs[2].ratOvr + "\n");
        gameR.append(HomeWR3Stats[3] + "/" + HomeWR3Stats[5] + "\n"); //td/fum
        gameR.append(HomeWR3Stats[2] + " yds\n"); //rec yards
        gameR.append(HomeWR3Stats[0] + "/" + HomeWR3Stats[1] + "\n"); //rec/targets
        gameL.append("\n");
        gameC.append("\n");
        gameR.append("\n");
        //TE
        gameL.append("TE\nYr Ovr\nTD/Fum\nRec Yards\nRec/Tgts\n");
        gameC.append(awayTE.getInitialName() + "\n");
        gameC.append(awayTE.getYrStr() + " " + awayTE.ratOvr + "\n");
        gameC.append(AwayTEStats[3] + "/" + AwayTEStats[5] + "\n"); //td/fum
        gameC.append(AwayTEStats[2] + " yds\n"); //rec yards
        gameC.append(AwayTEStats[0] + "/" + AwayTEStats[1] + "\n"); //rec/targets
        gameR.append(homeTE.getInitialName() + "\n");
        gameR.append(homeTE.getYrStr() + " " + homeTE.ratOvr + "\n");
        gameR.append(HomeTEStats[3] + "/" + HomeTEStats[5] + "\n"); //td/fum
        gameR.append(HomeTEStats[2] + " yds\n"); //rec yards
        gameR.append(HomeTEStats[0] + "/" + HomeTEStats[1] + "\n"); //rec/targets
        gameL.append("\n");
        gameC.append("\n");
        gameR.append("\n");
        gameL.append("\n");
        gameC.append("\n");
        gameR.append("\n");

        gameL.append("DEFENSE\n");
        gameC.append(awayTeam.teamStratDef.getStratName() + "\n");
        gameR.append(homeTeam.teamStratDef.getStratName() + "\n");


        //Def Lineman
        gameL.append("\n");
        gameC.append("\n");
        gameR.append("\n");
        gameL.append("DL\nYr Ovr\nTckl/Sack\nFum/Int\n");
        gameC.append(awayDLs[0].getInitialName() + "\n");
        gameC.append(awayDLs[0].getYrStr() + " " + awayDLs[0].ratOvr + "\n");
        gameC.append(AwayDL1Stats[0] + "/" + AwayDL1Stats[1] + "\n" + AwayDL1Stats[2] + "/" + AwayDL1Stats[3] + "\n");
        gameR.append(homeDLs[0].getInitialName() + "\n");
        gameR.append(homeDLs[0].getYrStr() + " " + homeDLs[0].ratOvr + "\n");
        gameR.append(HomeDL1Stats[0] + "/" + HomeDL1Stats[1] + "\n" + HomeDL1Stats[2] + "/" + HomeDL1Stats[3] + "\n");
        gameL.append("\n");
        gameC.append("\n");
        gameR.append("\n");

        gameL.append("DL\nYr Ovr\nTckl/Sack\nFum/Int\n");
        gameC.append(awayDLs[1].getInitialName() + "\n");
        gameC.append(awayDLs[1].getYrStr() + " " + awayDLs[1].ratOvr + "\n");
        gameC.append(AwayDL2Stats[0] + "/" + AwayDL2Stats[1] + "\n" + AwayDL2Stats[2] + "/" + AwayDL2Stats[3] + "\n");
        gameR.append(homeDLs[1].getInitialName() + "\n");
        gameR.append(homeDLs[1].getYrStr() + " " + homeDLs[1].ratOvr + "\n");
        gameR.append(HomeDL2Stats[0] + "/" + HomeDL2Stats[1] + "\n" + HomeDL2Stats[2] + "/" + HomeDL2Stats[3] + "\n");
        gameL.append("\n");
        gameC.append("\n");
        gameR.append("\n");

        gameL.append("DL\nYr Ovr\nTckl/Sack\nFum/Int\n");
        gameC.append(awayDLs[2].getInitialName() + "\n");
        gameC.append(awayDLs[2].getYrStr() + " " + awayDLs[2].ratOvr + "\n");
        gameC.append(AwayDL3Stats[0] + "/" + AwayDL3Stats[1] + "\n" + AwayDL3Stats[2] + "/" + AwayDL3Stats[3] + "\n");
        gameR.append(homeDLs[2].getInitialName() + "\n");
        gameR.append(homeDLs[2].getYrStr() + " " + homeDLs[2].ratOvr + "\n");
        gameR.append(HomeDL3Stats[0] + "/" + HomeDL3Stats[1] + "\n" + HomeDL3Stats[2] + "/" + HomeDL3Stats[3] + "\n");
        gameL.append("\n");
        gameC.append("\n");
        gameR.append("\n");

        gameL.append("DL\nYr Ovr\nTckl/Sack\nFum/Int\n");
        gameC.append(awayDLs[3].getInitialName() + "\n");
        gameC.append(awayDLs[3].getYrStr() + " " + awayDLs[3].ratOvr + "\n");
        gameC.append(AwayDL4Stats[0] + "/" + AwayDL4Stats[1] + "\n" + AwayDL4Stats[2] + "/" + AwayDL4Stats[3] + "\n");
        gameR.append(homeDLs[3].getInitialName() + "\n");
        gameR.append(homeDLs[3].getYrStr() + " " + homeDLs[3].ratOvr + "\n");
        gameR.append(HomeDL4Stats[0] + "/" + HomeDL4Stats[1] + "\n" + HomeDL4Stats[2] + "/" + HomeDL4Stats[3] + "\n");

        //Linebacker
        gameL.append("\n");
        gameC.append("\n");
        gameR.append("\n");
        gameL.append("LB\nYr Ovr\nTckl/Sack\nFum/Int\n");
        gameC.append(awayLBs[0].getInitialName() + "\n");
        gameC.append(awayLBs[0].getYrStr() + " " + awayLBs[0].ratOvr + "\n");
        gameC.append(AwayLB1Stats[0] + "/" + AwayLB1Stats[1] + "\n" + AwayLB1Stats[2] + "/" + AwayLB1Stats[3] + "\n");
        gameR.append(homeLBs[0].getInitialName() + "\n");
        gameR.append(homeLBs[0].getYrStr() + " " + homeLBs[0].ratOvr + "\n");
        gameR.append(HomeLB1Stats[0] + "/" + HomeLB1Stats[1] + "\n" + HomeLB1Stats[2] + "/" + HomeLB1Stats[3] + "\n");
        gameL.append("\n");
        gameC.append("\n");
        gameR.append("\n");

        gameL.append("LB\nYr Ovr\nTckl/Sack\nFum/Int\n");
        gameC.append(awayLBs[1].getInitialName() + "\n");
        gameC.append(awayLBs[1].getYrStr() + " " + awayLBs[1].ratOvr + "\n");
        gameC.append(AwayLB2Stats[0] + "/" + AwayLB2Stats[1] + "\n" + AwayLB2Stats[2] + "/" + AwayLB2Stats[3] + "\n");
        gameR.append(homeLBs[1].getInitialName() + "\n");
        gameR.append(homeLBs[1].getYrStr() + " " + homeLBs[1].ratOvr + "\n");
        gameR.append(HomeLB2Stats[0] + "/" + HomeLB2Stats[1] + "\n" + HomeLB2Stats[2] + "/" + HomeLB2Stats[3] + "\n");
        gameL.append("\n");
        gameC.append("\n");
        gameR.append("\n");

        gameL.append("LB\nYr Ovr\nTckl/Sack\nFum/Int\n");
        gameC.append(awayLBs[2].getInitialName() + "\n");
        gameC.append(awayLBs[2].getYrStr() + " " + awayLBs[2].ratOvr + "\n");
        gameC.append(AwayLB3Stats[0] + "/" + AwayLB3Stats[1] + "\n" + AwayLB3Stats[2] + "/" + AwayLB3Stats[3] + "\n");
        gameR.append(homeLBs[2].getInitialName() + "\n");
        gameR.append(homeLBs[2].getYrStr() + " " + homeLBs[2].ratOvr + "\n");
        gameR.append(HomeLB3Stats[0] + "/" + HomeLB3Stats[1] + "\n" + HomeLB3Stats[2] + "/" + HomeLB3Stats[3] + "\n");

        //CB
        gameL.append("\n");
        gameC.append("\n");
        gameR.append("\n");
        gameL.append("CB\nYr Ovr\nTckl/Sack\nFum/Int\n");
        gameC.append(awayCBs[0].getInitialName() + "\n");
        gameC.append(awayCBs[0].getYrStr() + " " + awayCBs[0].ratOvr + "\n");
        gameC.append(AwayCB1Stats[0] + "/" + AwayCB1Stats[1] + "\n" + AwayCB1Stats[2] + "/" + AwayCB1Stats[3] + "\n");
        gameR.append(homeCBs[0].getInitialName() + "\n");
        gameR.append(homeCBs[0].getYrStr() + " " + homeCBs[0].ratOvr + "\n");
        gameR.append(HomeCB1Stats[0] + "/" + HomeCB1Stats[1] + "\n" + HomeCB1Stats[2] + "/" + HomeCB1Stats[3] + "\n");
        gameL.append("\n");
        gameC.append("\n");
        gameR.append("\n");
        gameL.append("CB\nYr Ovr\nTckl/Sack\nFum/Int\n");
        gameC.append(awayCBs[1].getInitialName() + "\n");
        gameC.append(awayCBs[1].getYrStr() + " " + awayCBs[1].ratOvr + "\n");
        gameC.append(AwayCB2Stats[0] + "/" + AwayCB2Stats[1] + "\n" + AwayCB2Stats[2] + "/" + AwayCB2Stats[3] + "\n");
        gameR.append(homeCBs[1].getInitialName() + "\n");
        gameR.append(homeCBs[1].getYrStr() + " " + homeCBs[1].ratOvr + "\n");
        gameR.append(HomeCB2Stats[0] + "/" + HomeCB2Stats[1] + "\n" + HomeCB2Stats[2] + "/" + HomeCB2Stats[3] + "\n");
        gameL.append("\n");
        gameC.append("\n");
        gameR.append("\n");
        gameL.append("CB\nYr Ovr\nTckl/Sack\nFum/Int\n");
        gameC.append(awayCBs[2].getInitialName() + "\n");
        gameC.append(awayCBs[2].getYrStr() + " " + awayCBs[1].ratOvr + "\n");
        gameC.append(AwayCB3Stats[0] + "/" + AwayCB3Stats[1] + "\n" + AwayCB3Stats[2] + "/" + AwayCB3Stats[3] + "\n");
        gameR.append(homeCBs[2].getInitialName() + "\n");
        gameR.append(homeCBs[2].getYrStr() + " " + homeCBs[1].ratOvr + "\n");
        gameR.append(HomeCB3Stats[0] + "/" + HomeCB3Stats[1] + "\n" + HomeCB3Stats[2] + "/" + HomeCB3Stats[3] + "\n");
        //Safety
        gameL.append("\n");
        gameC.append("\n");
        gameR.append("\n");
        gameL.append("S\nYr Ovr\nTckl/Sack\nFum/Int\n");
        gameC.append(awayS.getInitialName() + "\n");
        gameC.append(awayS.getYrStr() + " " + awayS.ratOvr + "\n");
        gameC.append(AwaySStats[0] + "/" + AwaySStats[1] + "\n" + AwaySStats[2] + "/" + AwaySStats[3] + "\n");
        gameR.append(homeS.getInitialName() + "\n");
        gameR.append(homeS.getYrStr() + " " + homeS.ratOvr + "\n");
        gameR.append(HomeSStats[0] + "/" + HomeSStats[1] + "\n" + HomeSStats[2] + "/" + HomeSStats[3] + "\n");
        gameL.append("\n");
        gameC.append("\n");
        gameR.append("\n");
        gameL.append("\n");
        gameC.append("\n");
        gameR.append("\n");

        gameL.append("- - -\n");
        gameC.append("KICKING\n");
        gameR.append("- - -\n");
        gameL.append("\n");
        gameC.append("\n");
        gameR.append("\n");

        //Kickers
        gameL.append("\n");
        gameC.append("\n");
        gameR.append("\n");
        gameL.append("K\nYr Ovr\nFGM/FGA\nXPM/XPA\n");
        gameC.append(awayK.getInitialName() + "\n");
        gameC.append(awayK.getYrStr() + " " + awayK.ratOvr + "\n");
        gameC.append(AwayKStats[2] + "/" + AwayKStats[3] + " FG\n" + AwayKStats[0] + "/" + AwayKStats[1] + " XP\n");
        gameR.append(homeK.getInitialName() + "\n");
        gameR.append(homeK.getYrStr() + " " + homeK.ratOvr + "/" + homeK.ratPot + "\n");
        gameR.append(HomeKStats[2] + "/" + HomeKStats[3] + " FG\n" + HomeKStats[0] + "/" + HomeKStats[1] + " XP\n");
        gameL.append("\n");
        gameC.append("\n");
        gameR.append("\n");

        gameSum[0] = gameL.toString();
        gameSum[1] = gameC.toString();
        gameSum[2] = gameR.toString();
        gameSum[3] = gameEventLog;

        return gameSum;

    }

    /**
     * Gets a scouting summary of a game that is yet to be played.
     *
     * @return a String array with the left, right, center, and "SCOUTING REPORT"
     */
    public String[] getGameScoutStr() {
        /**
         * [0] is left side
         * [1] is center
         * [2] is right
         * [3] is bottom (will be empty for scouting)
         */
        String[] gameSum = new String[4];
        StringBuilder gameL = new StringBuilder();
        StringBuilder gameC = new StringBuilder();
        StringBuilder gameR = new StringBuilder();

        gameL.append("Ranking\nRecord\nPPG\nOpp PPG\nYPG\nOpp YPG\n" +
                "\nPass YPG\nRush YPG\nOpp PYPG\nOpp RYPG\n\nOff Talent\nDef Talent\nPrestige\nOffense\nDefense");
        int g = awayTeam.numGames();
        Team t = awayTeam;
        gameC.append("#" + t.rankTeamPollScore + " " + t.abbr + "\n" + t.wins + "-" + t.losses + "\n" +
                t.teamPoints / g + " (" + t.rankTeamPoints + ")\n" + t.teamOppPoints / g + " (" + t.rankTeamOppPoints + ")\n" +
                t.teamYards / g + " (" + t.rankTeamYards + ")\n" + t.teamOppYards / g + " (" + t.rankTeamOppYards + ")\n\n" +
                t.teamPassYards / g + " (" + t.rankTeamPassYards + ")\n" + t.teamRushYards / g + " (" + t.rankTeamRushYards + ")\n" +
                t.teamOppPassYards / g + " (" + t.rankTeamOppPassYards + ")\n" + t.teamOppRushYards / g + " (" + t.rankTeamOppRushYards + ")\n\n" +
                t.teamOffTalent + " (" + t.rankTeamOffTalent + ")\n" + t.teamDefTalent + " (" + t.rankTeamDefTalent + ")\n" +
                t.teamPrestige + " (" + t.rankTeamPrestige + ")\n" + t.teamStratOff.getStratName() + "\n" + t.teamStratDef.getStratName() + "\n");
        g = homeTeam.numGames();
        t = homeTeam;
        gameR.append("#" + t.rankTeamPollScore + " " + t.abbr + "\n" + t.wins + "-" + t.losses + "\n" +
                t.teamPoints / g + " (" + t.rankTeamPoints + ")\n" + t.teamOppPoints / g + " (" + t.rankTeamOppPoints + ")\n" +
                t.teamYards / g + " (" + t.rankTeamYards + ")\n" + t.teamOppYards / g + " (" + t.rankTeamOppYards + ")\n\n" +
                t.teamPassYards / g + " (" + t.rankTeamPassYards + ")\n" + t.teamRushYards / g + " (" + t.rankTeamRushYards + ")\n" +
                t.teamOppPassYards / g + " (" + t.rankTeamOppPassYards + ")\n" + t.teamOppRushYards / g + " (" + t.rankTeamOppRushYards + ")\n\n" +
                t.teamOffTalent + " (" + t.rankTeamOffTalent + ")\n" + t.teamDefTalent + " (" + t.rankTeamDefTalent + ")\n" +
                t.teamPrestige + " (" + t.rankTeamPrestige + ")\n" + t.teamStratOff.getStratName() + "\n" + t.teamStratDef.getStratName() + "\n");

        gameSum[0] = gameL.toString();
        gameSum[1] = gameC.toString();
        gameSum[2] = gameR.toString();

        StringBuilder gameScout = new StringBuilder();
        if (awayTeam.playersInjuredAll != null && !awayTeam.playersInjuredAll.isEmpty()) {
            Collections.sort(awayTeam.playersInjuredAll, new PlayerPositionComparator());
            gameScout.append("\n" + awayTeam.abbr + " Injury Report:\n");
            for (Player p : awayTeam.playersInjuredAll) {
                gameScout.append(p.getPosNameYrOvrPot_OneLine() + "\n");
            }
        }
        if (homeTeam.playersInjuredAll != null && !homeTeam.playersInjuredAll.isEmpty()) {
            Collections.sort(homeTeam.playersInjuredAll, new PlayerPositionComparator());
            gameScout.append("\n" + homeTeam.abbr + " Injury Report:\n");
            for (Player p : homeTeam.playersInjuredAll) {
                gameScout.append(p.getPosNameYrOvrPot_OneLine() + "\n");
            }
        }

        gameSum[3] = gameScout.toString();

        return gameSum;
    }

    /**
     * Gets the amount of pass yards by a certain team for this game.
     *
     * @param ha home/away bool, false for home
     * @return number of pass yards by specified team
     */
    public int getPassYards(boolean ha) {
        //ha = home/away, false for home, true for away
        if (!ha) return HomeQBStats[4];
        else return AwayQBStats[4];
    }

    /**
     * Gets the amount of rush yards by a certain team for this game.
     *
     * @param ha home/away bool, false for home
     * @return number of rush yards by speicifed team
     */
    public int getRushYards(boolean ha) {
        //ha = home/away, false for home, true for away
        if (!ha) return HomeRB1Stats[1] + HomeRB2Stats[1] + HomeQBStats[7];
        else return AwayRB1Stats[1] + AwayRB2Stats[1] + AwayQBStats[7];
    }

    /**
     * Gets home field advantage, which is a +3 bonus to certain things for the home team
     *
     * @return 3 if for home, 0 for away
     */
    private int getHFadv() {
        //home field advantage
        int footIQadv = (homeTeam.getCompositeFootIQ() - awayTeam.getCompositeFootIQ()) / 5;
        if (footIQadv > 3) footIQadv = 3;
        if (footIQadv < -3) footIQadv = -3;
        if (gamePoss) {
            return 3 + footIQadv;
        } else {
            return -footIQadv;
        }
    }

    /**
     * Gets the even prefix for events that happen in the log.
     * Will be of type ALA 3 - 3 GEO, Time: Q2 X:XX, 1st and goal at 95 yard line.
     *
     * @return String of the event prefix.
     */
    private String getEventPrefix() {
        String possStr;
        if (gamePoss) possStr = homeTeam.abbr;
        else possStr = awayTeam.abbr;
        String yardsNeedAdj = "" + gameYardsNeed;
        if (gameYardLine + gameYardsNeed >= 100) yardsNeedAdj = "Goal";
        int gameDownAdj;
        if (gameDown > 4) {
            gameDownAdj = 4;
        } else {
            gameDownAdj = gameDown;
        }
        return "\n\n" + homeTeam.abbr + " " + homeScore + " - " + awayScore + " " + awayTeam.abbr + ", Time: " + convGameTime() +
                "\n\t" + possStr + " " + gameDownAdj + " and " + yardsNeedAdj + " at " + gameYardLine + " yard line." + "\n";
    }

    /**
     * Converts the game time, which is 0-3600, into readable time, i.e. Q1 12:50
     *
     * @return String of the converted game time
     */
    private String convGameTime() {
        if (!playingOT) {
            int qNum = (3600 - gameTime) / 900 + 1;
            int minTime;
            int secTime;
            String secStr;
            if (gameTime <= 0 && numOT <= 0) { // Prevent Q5 1X:XX from displaying in the game log
                return "0:00 Q4";
            } else {
                minTime = (gameTime - 900 * (4 - qNum)) / 60;
                secTime = (gameTime - 900 * (4 - qNum)) - 60 * minTime;
                if (secTime < 10) secStr = "0" + secTime;
                else secStr = "" + secTime;
                return minTime + ":" + secStr + " Q" + qNum;
            }
        } else {
            if (!bottomOT) {
                return "TOP OT" + numOT;
            } else {
                return "BOT OT" + numOT;
            }
        }
    }

    /**
     * Plays game. Home team starts with ball, run plays till time expires.
     */
    public void playGame() {
        if (!hasPlayed) {
            gameEventLog = "LOG: #" + awayTeam.rankTeamPollScore + " " + awayTeam.abbr + " (" + awayTeam.wins + "-" + awayTeam.losses + ") @ #" +
                    homeTeam.rankTeamPollScore + " " + homeTeam.abbr + " (" + homeTeam.wins + "-" + homeTeam.losses + ")" + "\n" +
                    "---------------------------------------------------------\n\n" +
                    awayTeam.abbr + " Off Strategy: " + awayTeam.teamStratOff.getStratName() + "\n" +
                    awayTeam.abbr + " Def Strategy: " + awayTeam.teamStratDef.getStratName() + "\n" +
                    homeTeam.abbr + " Off Strategy: " + homeTeam.teamStratOff.getStratName() + "\n" +
                    homeTeam.abbr + " Def Strategy: " + homeTeam.teamStratDef.getStratName() + "\n";
            //probably establish some home field advantage before playing
            gameTime = 3600;
            gameDown = 1;
            gamePoss = true;
            gameYardsNeed = 10;
            gameYardLine = 20;

            // Regulation
            while (gameTime > 0) {
                //play ball!
                if (gamePoss) runPlay(homeTeam, awayTeam);
                else runPlay(awayTeam, homeTeam);
            }

            // Add last play
            if (homeScore != awayScore) {
                gameEventLog += getEventPrefix() + "Time has expired! The game is over.";
            } else {
                gameEventLog += getEventPrefix() + "OVERTIME!\nTie game at 0:00, overtime begins!";
            }

            //Overtime (if needed)
            if (gameTime <= 0 && homeScore == awayScore) {
                playingOT = true;
                gamePoss = false;
                gameYardLine = 75;
                numOT++;
                gameTime = -1;
                gameDown = 1;
                gameYardsNeed = 10;

                while (playingOT) {
                    if (gamePoss) runPlay(homeTeam, awayTeam);
                    else runPlay(awayTeam, homeTeam);
                }
            }

            //game over, add wins
            if (homeScore > awayScore) {
                homeTeam.wins++;
                homeTeam.totalWins++;
                homeTeam.gameWLSchedule.add("W");
                awayTeam.losses++;
                awayTeam.totalLosses++;
                awayTeam.gameWLSchedule.add("L");
                homeTeam.gameWinsAgainst.add(awayTeam);
                homeTeam.winStreak.addWin(homeTeam.league.getYear());
                homeTeam.league.checkLongestWinStreak(homeTeam.winStreak);
                awayTeam.winStreak.resetStreak(awayTeam.league.getYear());
            } else {
                homeTeam.losses++;
                homeTeam.totalLosses++;
                homeTeam.gameWLSchedule.add("L");
                awayTeam.wins++;
                awayTeam.totalWins++;
                awayTeam.gameWLSchedule.add("W");
                awayTeam.gameWinsAgainst.add(homeTeam);
                awayTeam.winStreak.addWin(awayTeam.league.getYear());
                awayTeam.league.checkLongestWinStreak(awayTeam.winStreak);
                homeTeam.winStreak.resetStreak(homeTeam.league.getYear());
            }

            // Add points/opp points
            homeTeam.addGamePlayedPlayers(homeScore > awayScore);
            awayTeam.addGamePlayedPlayers(awayScore > homeScore);

            homeTeam.teamPoints += homeScore;
            awayTeam.teamPoints += awayScore;

            homeTeam.teamOppPoints += awayScore;
            awayTeam.teamOppPoints += homeScore;

            homeYards = getPassYards(false) + getRushYards(false);
            awayYards = getPassYards(true) + getRushYards(true);

            homeTeam.teamYards += homeYards;
            awayTeam.teamYards += awayYards;

            homeTeam.teamOppYards += awayYards;
            awayTeam.teamOppYards += homeYards;

            homeTeam.teamOppPassYards += getPassYards(true);
            awayTeam.teamOppPassYards += getPassYards(false);
            homeTeam.teamOppRushYards += getRushYards(true);
            awayTeam.teamOppRushYards += getRushYards(false);

            homeTeam.teamTODiff += awayTOs - homeTOs;
            awayTeam.teamTODiff += homeTOs - awayTOs;

            hasPlayed = true;

            addNewsStory();

            if (homeTeam.rivalTeam.equals(awayTeam.abbr) || awayTeam.rivalTeam.equals(homeTeam.abbr)) {
                if (homeScore > awayScore) {
                    homeTeam.wonRivalryGame = true;
                } else {
                    awayTeam.wonRivalryGame = true;
                }
            }

            // Set reference to right players
            homeQB = homeTeam.getQB(0);
            homeRBs = new PlayerRB[2];
            for (int i = 0; i < 2; ++i) {
                homeRBs[i] = homeTeam.getRB(i);
            }
            homeWRs = new PlayerWR[3];
            for (int i = 0; i < 3; ++i) {
                homeWRs[i] = homeTeam.getWR(i);
            }
            homeTE = homeTeam.getTE(0);

            homeK = homeTeam.getK(0);

            homeDLs = new PlayerDL[4];
            for (int i = 0; i < 4; ++i) {
                homeDLs[i] = homeTeam.getDL(i);
            }
            homeLBs = new PlayerLB[3];
            for (int i = 0; i < 3; ++i) {
                homeLBs[i] = homeTeam.getLB(i);
            }
            homeCBs = new PlayerCB[3];
            for (int i = 0; i < 3; ++i) {
                homeCBs[i] = homeTeam.getCB(i);
            }
            homeS = homeTeam.getS(0);

            awayQB = awayTeam.getQB(0);
            awayRBs = new PlayerRB[2];
            for (int i = 0; i < 2; ++i) {
                awayRBs[i] = awayTeam.getRB(i);
            }
            awayWRs = new PlayerWR[3];
            for (int i = 0; i < 3; ++i) {
                awayWRs[i] = awayTeam.getWR(i);
            }
            awayTE = awayTeam.getTE(0);
            awayK = awayTeam.getK(0);

            awayDLs = new PlayerDL[4];
            for (int i = 0; i < 4; ++i) {
                awayDLs[i] = awayTeam.getDL(i);
            }
            awayLBs = new PlayerLB[3];
            for (int i = 0; i < 3; ++i) {
                awayLBs[i] = awayTeam.getLB(i);
            }
            awayCBs = new PlayerCB[3];
            for (int i = 0; i < 3; ++i) {
                awayCBs[i] = awayTeam.getCB(i);
            }
            awayS = awayTeam.getS(0);

            homeTeam.checkForInjury();
            awayTeam.checkForInjury();

        }
    }

    /**
     * Add news story to the league depending on the games outcome.
     * Only if it is big upset, or undefeated team goes down, or there are 3+ OTs.
     */
    public void addNewsStory() {

        //Weekly Scoreboard Update
        homeTeam.league.weeklyScores.get(homeTeam.league.currentWeek+1).add(gameName + ">#" + awayTeam.rankTeamPollScore +" " + awayTeam.name + " " + awayScore + "\n" + "#" + homeTeam.rankTeamPollScore + " " + homeTeam.name +  " " +homeScore);

        //homeTeam.league.weeklyScores.get(homeTeam.league.currentWeek+1).add("#" + awayTeam.rankTeamPollScore +" " + awayTeam.name + " at " + "#" + homeTeam.rankTeamPollScore + " " + homeTeam.name + ">" +  awayTeam.name + " " + awayScore + "\n" +  homeTeam.name + " " +homeScore);


        if (numOT >= 3) {
            // Thriller in OT
            Team winner, loser;
            int winScore, loseScore;
            if (awayScore > homeScore) {
                winner = awayTeam;
                loser = homeTeam;
                winScore = awayScore;
                loseScore = homeScore;
            } else {
                winner = homeTeam;
                loser = awayTeam;
                winScore = homeScore;
                loseScore = awayScore;
            }

            homeTeam.league.newsStories.get(homeTeam.league.currentWeek + 1).add(
                    numOT + "OT Thriller!>" + winner.strRep() + " and " + loser.strRep() + " played an absolutely thrilling game " +
                            "that went to " + numOT + " overtimes, with " + winner.name + " finally emerging victorious " + winScore + " to " + loseScore + ".");
        } else if (homeScore > awayScore && awayTeam.losses == 1 && awayTeam.league.currentWeek > 5) {
            // 5-0 or better team given first loss
            awayTeam.league.newsStories.get(homeTeam.league.currentWeek + 1).add(
                    "Undefeated no more! " + awayTeam.name + " suffers first loss!" +
                            ">" + homeTeam.strRep() + " hands " + awayTeam.strRep() +
                            " their first loss of the season, winning " + homeScore + " to " + awayScore + ".");
        } else if (awayScore > homeScore && homeTeam.losses == 1 && homeTeam.league.currentWeek > 5) {
            // 5-0 or better team given first loss
            homeTeam.league.newsStories.get(homeTeam.league.currentWeek + 1).add(
                    "Undefeated no more! " + homeTeam.name + " suffers first loss!" +
                            ">" + awayTeam.strRep() + " hands " + homeTeam.strRep() +
                            " their first loss of the season, winning " + awayScore + " to " + homeScore + ".");
        } else if (awayScore > homeScore && homeTeam.rankTeamPollScore < 20 &&
                (awayTeam.rankTeamPollScore - homeTeam.rankTeamPollScore) > 20) {
            // Upset!
            awayTeam.league.newsStories.get(awayTeam.league.currentWeek + 1).add(
                    "Upset! " + awayTeam.strRep() + " beats " + homeTeam.strRep() +
                            ">#" + awayTeam.rankTeamPollScore + " " + awayTeam.name + " was able to pull off the upset on the road against #" +
                            homeTeam.rankTeamPollScore + " " + homeTeam.name + ", winning " + awayScore + " to " + homeScore + ".");
        } else if (homeScore > awayScore && awayTeam.rankTeamPollScore < 20 &&
                (homeTeam.rankTeamPollScore - awayTeam.rankTeamPollScore) > 20) {
            // Upset!
            homeTeam.league.newsStories.get(homeTeam.league.currentWeek + 1).add(
                    "Upset! " + homeTeam.strRep() + " beats " + awayTeam.strRep() +
                            ">#" + homeTeam.rankTeamPollScore + " " + homeTeam.name + " was able to pull off the upset at home against #" +
                            awayTeam.rankTeamPollScore + " " + awayTeam.name + ", winning " + homeScore + " to " + awayScore + ".");
        }

    }

    /**
     * Run play. Type of play run determined by offensive strengths and type of situation.
     *
     * @param offense offense running the play
     * @param defense defense defending the play
     */
    private void runPlay(Team offense, Team defense) {

        if (gameDown > 4) {
            if (!playingOT) {
                //Log the turnover on downs, reset down and distance, give possession to the defense, exit this runPlay()
                gameEventLog += getEventPrefix() + "TURNOVER ON DOWNS!\n" + offense.abbr + " failed to convert on " + (gameDown - 1) + "th down. " + defense.abbr + " takes over possession on downs.";

                //Turn over on downs, change possession, set to first down and 10 yards to go
                gamePoss = !gamePoss;
                gameDown = 1;
                gameYardsNeed = 10;
                //and flip which direction the ball is moving in
                gameYardLine = 100 - gameYardLine;

            } else {
                //OT is over for the offense, log the turnover on downs, run resetForOT().
                gameEventLog += getEventPrefix() + "TURNOVER ON DOWNS!\n" + offense.abbr + " failed to convert on " + (gameDown - 1) + "th down in OT and their possession is over.";
                resetForOT();

            }
        } else {
            double preferPass = (offense.getPassProf() - defense.getPassDef()) / 100 + Math.random() * offense.teamStratOff.getPassPref();       //STRATEGIES
            double preferRush = (offense.getRushProf() - defense.getRushDef()) / 100 + Math.random() * offense.teamStratOff.getRunPref();

            // If it's 1st and Goal to go, adjust yards needed to reflect distance for a TD so that play selection reflects actual yards to go
            // If we don't do this, gameYardsNeed may be higher than the actually distance for a TD and suboptimal plays may be chosen
            if (gameDown == 1 && gameYardLine >= 91) gameYardsNeed = 100 - gameYardLine;

            //Under 30 seconds to play, check that the team with the ball is trailing or tied, do something based on the score difference
            if (gameTime <= 30 && !playingOT && ((gamePoss && (awayScore >= homeScore)) || (!gamePoss && (homeScore >= awayScore)))) {
                //Down by 3 or less, or tied, and you have the ball
                if (((gamePoss && (awayScore - homeScore) <= 3) || (!gamePoss && (homeScore - awayScore) <= 3)) && gameYardLine > 60) {
                    //last second FGA
                    fieldGoalAtt(offense, defense);
                } else {
                    //hail mary
                    passingPlay(offense, defense);
                }
            } else if (gameDown >= 4) {
                if (((gamePoss && (awayScore - homeScore) > 3) || (!gamePoss && (homeScore - awayScore) > 3)) && gameTime < 300) {
                    //go for it since we need 7 to win -- This also forces going for it if down by a TD in BOT OT
                    if (gameYardsNeed < 3) {
                        rushingPlay(offense, defense);
                    } else {
                        passingPlay(offense, defense);
                    }
                } else {
                    //4th down
                    if (gameYardsNeed < 3) {
                        if (gameYardLine > 65) {
                            //fga
                            fieldGoalAtt(offense, defense);
                        } else if (gameYardLine > 55) {
                            // run play, go for it!
                            rushingPlay(offense, defense);
                        } else {
                            //punt
                            puntPlay(offense);
                        }
                    } else if (gameYardLine > 60) {
                        //fga
                        fieldGoalAtt(offense, defense);
                    } else {
                        //punt
                        puntPlay(offense);
                    }
                }
            } else if ((gameDown == 3 && gameYardsNeed > 4) || ((gameDown == 1 || gameDown == 2) && (preferPass >= preferRush))) {
                // pass play
                passingPlay(offense, defense);
            } else {
                //run play
                rushingPlay(offense, defense);
            }
        }


    }

    /**
     * Give ball to correct team and reset yard line/down for new team.
     * In top OT frame, give the other team the ball, reset down, distance and yard line, and move to bottom OT frame
     * In bottom OT frame, if score is tied, keep playing.
     * If not, the game is over.
     */
    private void resetForOT() {
        if (bottomOT && homeScore == awayScore) {
            gameYardLine = 75;
            gameYardsNeed = 10;
            gameDown = 1;
            numOT++;
            gamePoss = (numOT % 2) == 0;
            gameTime = -1;
            bottomOT = false;
            //runPlay( awayTeam, homeTeam );
        } else if (!bottomOT) {
            gamePoss = !gamePoss;
            gameYardLine = 75;
            gameYardsNeed = 10;
            gameDown = 1;
            gameTime = -1;
            bottomOT = true;
            //runPlay( homeTeam, awayTeam );
        } else {
            // game is not tied after both teams had their chance
            playingOT = false;
        }
    }

    /**
     * Passing play.
     *
     * @param offense throwing the ball
     * @param defense defending the pass
     */
    private void passingPlay(Team offense, Team defense) {
        boolean playWR = true;
        int playerDL;
        int playerLB;
        int[] selRBStats;
        int[] selWRStats;
        int[] selTEStats;
        int[] selDLStats;
        int[] selLBStats;
        int[] selCBStats;
        int[] selSStats;
        int[] selLB2Stats;
        PlayerWR selWR;
        PlayerDL selDL;
        PlayerLB selLB;
        PlayerLB selLB2;
        PlayerCB selCB;

        PlayerS selS = defense.getS(0);
        PlayerTE selTE = offense.getTE(0);

        //choose WR to throw to, better WRs more often
        double WR1pref = Math.pow(offense.getWR(0).ratOvr, 1) * Math.random();
        double WR2pref = Math.pow(offense.getWR(1).ratOvr, 1) * Math.random();
        double WR3pref = Math.pow(offense.getWR(2).ratOvr, 1) * Math.random();
        double TEpref = Math.pow(((offense.getTE(0).ratCatch + offense.getTE(0).ratSpeed)/2), 1) * Math.random()*.67;

        //To implement - half back catches/screens/etc.
        double RB1pref = Math.pow((offense.getRB(0).ratOvr), 1) * Math.random();
        double RB2pref = Math.pow((offense.getRB(1).ratOvr), 1) * Math.random();

        double DL1pref = Math.pow(defense.getDL(0).ratPassRush, 1) * Math.random();
        double DL2pref = Math.pow(defense.getDL(1).ratPassRush, 1) * Math.random();
        double DL3pref = Math.pow(defense.getDL(2).ratPassRush, 1) * Math.random();
        double DL4pref = Math.pow(defense.getDL(3).ratPassRush, 1) * Math.random();

        double LB1pref = Math.pow(defense.getLB(0).ratCoverage, 1) * Math.random();
        double LB2pref = Math.pow(defense.getLB(1).ratCoverage, 1) * Math.random();
        double LB3pref = Math.pow(defense.getLB(2).ratCoverage, 1) * Math.random();

        //Choose the DL involved in play
        if (DL1pref > DL2pref && DL1pref > DL3pref && DL1pref > DL4pref) {
            selDL = defense.getDL(0);
            playerDL = 0;
        } else if (DL2pref > DL1pref && DL2pref > DL3pref && DL2pref > DL4pref) {
            selDL = defense.getDL(1);
            playerDL = 1;
        } else if (DL3pref > DL1pref && DL3pref > DL2pref && DL3pref > DL4pref) {
            selDL = defense.getDL(2);
            playerDL = 2;
        } else {
            selDL = defense.getDL(3);
            playerDL = 3;
        }

        //Choose LB involved in play
        if (LB1pref > LB2pref && LB1pref > LB3pref) {
            selLB = defense.getLB(0);
            selLB2 = defense.getLB(1);
            playerLB = 0;
        } else if (LB2pref > LB1pref && LB2pref > LB3pref) {
            selLB = defense.getLB(1);
            selLB2 = defense.getLB(2);
            playerLB = 1;
        } else {
            selLB = defense.getLB(2);
            selLB2 = defense.getLB(0);
            playerLB = 2;
        }

        //Choose WR and CB involved in play
        if (WR1pref > WR2pref && WR1pref > WR3pref) {
            selWR = offense.getWR(0);
            selCB = defense.getCB(0);
            if (gamePoss) {
                selWRStats = HomeWR1Stats;
                selCBStats = AwayCB1Stats;
            } else {
                selWRStats = AwayWR1Stats;
                selCBStats = HomeCB1Stats;
            }
        } else if (WR2pref > WR1pref && WR2pref > WR3pref) {
            selWR = offense.getWR(1);
            selCB = defense.getCB(1);
            if (gamePoss) {
                selWRStats = HomeWR2Stats;
                selCBStats = AwayCB2Stats;
            } else {
                selWRStats = AwayWR2Stats;
                selCBStats = HomeCB2Stats;
            }
        } else {
            selWR = offense.getWR(2);
            selCB = defense.getCB(2);
            if (gamePoss) {
                selWRStats = HomeWR3Stats;
                selCBStats = AwayCB3Stats;
            } else {
                selWRStats = AwayWR3Stats;
                selCBStats = HomeCB3Stats;
            }
        }

        //Choose appropriate team stats
        if (gamePoss) {
            selTEStats = HomeTEStats;
            selSStats = AwaySStats;

            if (playerDL == 0) {
                selDLStats = AwayDL1Stats;
            } else if (playerDL == 1) {
                selDLStats = AwayDL2Stats;
            } else if (playerDL == 2) {
                selDLStats = AwayDL3Stats;
            } else {
                selDLStats = AwayDL4Stats;
            }

            if (playerLB == 0) {
                selLBStats = AwayLB1Stats;
                selLB2Stats = AwayLB2Stats;
            } else if (playerLB == 1) {
                selLBStats = AwayLB2Stats;
                selLB2Stats = AwayLB3Stats;
            } else {
                selLBStats = AwayLB3Stats;
                selLB2Stats = AwayLB1Stats;
            }

        } else {

            selTEStats = AwayTEStats;
            selSStats = HomeSStats;

            if (playerDL == 0) {
                selDLStats = HomeDL1Stats;
            } else if (playerDL == 1) {
                selDLStats = HomeDL2Stats;
            } else if (playerDL == 2) {
                selDLStats = HomeDL3Stats;
            } else {
                selDLStats = HomeDL4Stats;
            }

            if (playerLB == 0) {
                selLBStats = HomeLB1Stats;
                selLB2Stats = AwayLB2Stats;
            } else if (playerLB == 1) {
                selLBStats = HomeLB2Stats;
                selLB2Stats = AwayLB3Stats;

            } else {
                selLBStats = HomeLB3Stats;
                selLB2Stats = AwayLB1Stats;
            }
        }

        //Choose the Catch Target
        if (TEpref > WR1pref && TEpref > WR2pref & TEpref > WR3pref) {
            passingPlayTE(offense, defense, selTE, selDL, selLB, selLB2, selCB, selS, selTEStats, selDLStats, selLBStats, selLB2Stats, selCBStats, selSStats);
        } else {
            passingPlayWR(offense, defense, selWR, selDL, selLB, selLB2, selCB, selS, selWRStats, selDLStats, selLBStats, selLB2Stats, selCBStats, selSStats);
        }

    }

    /**
     * Passing play to WRs.
     *
     * @param offense throwing the ball
     * @param defense defending the pass
     */
    private void passingPlayWR(Team offense, Team defense, PlayerWR selWR, PlayerDL selDL, PlayerLB selLB, PlayerLB selLB2, PlayerCB selCB, PlayerS selS, int[] selWRStats, int[] selDLStats, int[] selLBStats, int[] selLB2Stats, int[] selCBStats, int[] selSStats) {
        int yardsGain = 0;
        boolean gotTD = false;
        boolean gotFumble = false;

        //get how much pressure there is on qb, check if sack
        int pressureOnQB = defense.getCompositeDLPass() * 2 - offense.getCompositeOLPass() - getHFadv() + (defense.teamStratDef.getRunProtection() - offense.teamStratDef.getRunProtection());  //STRATEGIES
        if (Math.random() * 100 < pressureOnQB / 8) {
            //sacked!
            String defender;
            double DL = selDL.ratPassRush * Math.random() * 100;
            double LB = selLB2.ratOvr * Math.random() * 60;
            double S = selS.ratOvr * Math.random() * 25;

            if (DL >= LB && DL >= S) {
                selDLStats[0]++;
                selDLStats[1]++;
                selDL.statsTackles++;
                selDL.statsSacks++;
                defender = ("DL " + selDL.name);
            } else if (LB > DL && LB >= S) {
                selLB2Stats[0]++;
                selLB2Stats[1]++;
                selLB2.statsTackles++;
                selLB2.statsSacks++;
                defender = ("LB " + selLB2.name);
            } else {
                selSStats[0]++;
                selSStats[1]++;
                selS.statsTackles++;
                selS.statsSacks++;
                defender = ("S " + selS.name);
            }

            qbSack(offense, defense, defender);

            return;
        }

        //check for int
        double intChance = (pressureOnQB + defense.getS(0).ratOvr - (offense.getQB(0).ratPassAcc + offense.getQB(0).ratFootIQ + 100) / 3) / 18    //STRATEGIES
                - offense.teamStratOff.getPassProtection() + defense.teamStratDef.getPassProtection();
        if (intChance < 0.015) intChance = 0.015;
        if (100 * Math.random() < intChance) {
            //Interception
            double cb = selCB.ratCoverage * Math.random() * 100;
            double s = selS.ratCoverage * Math.random() * 70;
            double lb = selLB.ratCoverage * Math.random() * 35;
            String defender;

            if (cb >= s) {
                selCBStats[3]++;
                selCB.statsInts++;
                defender = ("CB " + selCB.name);
            } else if (s > cb && s >= lb) {
                selSStats[3]++;
                selS.statsInts++;
                defender = ("S " + selS.name);
            } else {
                selLBStats[3]++;
                selLB.statsInts++;
                defender = ("LB " + selLB.name);
            }

            qbInterception(offense, defender);

            return;
        }

        //throw ball, check for completion
        double completion = (getHFadv() + normalize(offense.getQB(0).ratPassAcc) + normalize(selWR.ratCatch)
                - normalize(selCB.ratCoverage)) / 2 + 18.25 - pressureOnQB / 16.8 + offense.teamStratOff.getPassProtection() - defense.teamStratDef.getPassProtection();   //STRATEGIES
        if (100 * Math.random() < completion) {
            if (100 * Math.random() < (100 - selWR.ratCatch) / 3) {
                //drop
                gameDown++;
                selWRStats[4]++;
                selWR.statsDrops++;
                passAttempt(offense, selWR, selWRStats, yardsGain);
                //Drop ball = inc pass, so run time for the play, stop clock until next play, move on (aka return;)
                gameTime -= timePerPlay * Math.random();
                return;

            } else {
                //no drop
                yardsGain = (int) ((normalize(offense.getQB(0).ratPassPow) + normalize(selWR.ratSpeed) - normalize(selCB.ratSpeed)) * Math.random() / 4.8 //STRATEGIES
                        + offense.teamStratOff.getPassPotential() - defense.teamStratDef.getPassPotential());
                //see if receiver can get yards after catch
                double escapeChance = (normalize(selWR.ratEvasion) * 3 - selCB.ratTackle - selS.ratTackle) * Math.random()   //STRATEGIES
                        + offense.teamStratOff.getPassPotential() - defense.teamStratDef.getPassPotential();
                if (escapeChance > 92 || Math.random() > 0.95) {
                    yardsGain += 3 + (selWR.ratSpeed * Math.random() / 4);
                }
                if (escapeChance > 80 && Math.random() < (0.1 + (offense.teamStratOff.getPassPotential() - defense.teamStratDef.getPassPotential()) / 200)) {  //STRATEGIES
                    //wr escapes for TD
                    yardsGain += 100;
                }

                //add yardage
                gameYardLine += yardsGain;
                if (gameYardLine >= 100) { //TD!
                    yardsGain -= gameYardLine - 100;
                    gameYardLine = 100 - yardsGain;
                    addPointsQuarter(6);
                    passingTD(offense, selWR, selWRStats, yardsGain);
                    gotTD = true;
                } else {
                    //check for fumble
                    double fumChance = (selS.ratTackle + selCB.ratTackle) / 2;
                    if (100 * Math.random() < fumChance / 50) {
                        //Fumble!
                        gotFumble = true;
                    }
                }

                if (!gotTD && !gotFumble) {
                    //check downs if there wasnt fumble or TD
                    gameYardsNeed -= yardsGain;

                    if (escapeChance <= 70) {
                        selCBStats[0]++;
                        selCB.statsTackles++;
                    } else if (escapeChance > 70 && escapeChance < 85) {
                        selSStats[0]++;
                        selS.statsTackles++;
                    } else {
                        selLBStats[0]++;
                        selLB.statsTackles++;
                    }

                    if (gameYardsNeed <= 0) {
                        // Only set new down and distance if there wasn't a TD
                        gameDown = 1;
                        gameYardsNeed = 10;
                    } else gameDown++;
                }

                //stats management
                passCompletion(offense, defense, selWR, selWRStats, yardsGain);
            }

        } else {
            //no completion, advance downs
            passAttempt(offense, selWR, selWRStats, yardsGain);
            gameDown++;
            //Incomplete pass stops the clock, so just run time for how long the play took, then move on
            gameTime -= timePerPlay * Math.random();
            return;
        }

        passAttempt(offense, selWR, selWRStats, yardsGain);


        if (gotFumble) {
            String defender;
            if (selS.ratTackle * Math.random() > selCB.ratTackle * Math.random()) {
                selSStats[0]++;
                selSStats[2]++;
                selS.statsTackles++;
                selS.statsFumbles++;
                defender = ("S " + selS.name);
            } else {
                selCBStats[0]++;
                selCBStats[2]++;
                selCB.statsTackles++;
                selCB.statsFumbles++;
                defender = ("CB " + selCB.name);
            }

            selWRStats[5]++;
            selWR.statsFumbles++;

            gameEventLog += getEventPrefix() + "TURNOVER!\n" + offense.abbr + " receiver " + selWR.name + " fumbled the ball after a catch. It was recovered by " + defender + ".";


            if (gamePoss) { // home possession
                homeTOs++;
            } else {
                awayTOs++;
            }
            if (!playingOT) {
                gameDown = 1;
                gameYardsNeed = 10;
                gamePoss = !gamePoss;
                gameYardLine = 100 - gameYardLine;
                gameTime -= timePerPlay * Math.random();
                return;
            } else {
                resetForOT();
                return;
            }
        }

        if (gotTD) {
            gameTime -= timePerPlay * Math.random();
            kickXP(offense, defense);
            if (!playingOT) kickOff(offense);
            else resetForOT();
            return;
        }

        gameTime -= timePerPlay + timePerPlay * Math.random();
    }

    private void passingPlayTE(Team offense, Team defense, PlayerTE selTE, PlayerDL selDL, PlayerLB selLB, PlayerLB selLB2, PlayerCB selCB, PlayerS selS, int[] selTEStats, int[] selDLStats, int[] selLBStats, int[] selLB2Stats, int[] selCBStats, int[] selSStats) {
        int yardsGain = 0;
        boolean gotTD = false;
        boolean gotFumble = false;

        //get how much pressure there is on qb, check if sack
        int pressureOnQB = defense.getCompositeDLPass() * 2 - offense.getCompositeOLPass() - getHFadv() + (defense.teamStratDef.getPassProtection() - offense.teamStratDef.getPassProtection());   //STRATEGIES
        if (Math.random() * 100 < pressureOnQB / 8) {
            //sacked!
            String defender;
            double DL = selDL.ratPassRush * Math.random() * 100;
            double LB = selLB2.ratOvr * Math.random() * 65;
            double S = selS.ratOvr * Math.random() * 50;
            double CB = selCB.ratOvr * Math.random() * 25;
            if (DL >= S && DL >= CB && DL >= LB) {
                selDLStats[0]++;
                selDLStats[1]++;
                selDL.statsTackles++;
                selDL.statsSacks++;
                defender = ("DL " + selDL.name);
            } else if (LB > DL && LB >= S && LB >= CB) {
                selLB2Stats[0]++;
                selLB2Stats[1]++;
                selLB2.statsTackles++;
                selLB2.statsSacks++;
                defender = ("LB " + selLB2.name);
            } else if (S > DL && S > LB && S >= CB) {
                selSStats[0]++;
                selSStats[1]++;
                selS.statsTackles++;
                selS.statsSacks++;
                defender = ("S " + selDL.name);
            } else {
                selCBStats[0]++;
                selCBStats[1]++;
                selCB.statsTackles++;
                selCB.statsSacks++;
                defender = ("CB " + selCB.name);
            }

            qbSack(offense, defense, defender);
            return;
        } /*else if (Math.random() * 100 < pressureOnQB / 9) {
            RushPlayQB(offense, defense, selQB, selTE, selDL, selLB, selCB, selS, selQBStats, selDLStats, selLBStats, selCBStats, selSStats);
        }*/

        //check for int
        double intChance = (pressureOnQB + defense.getS(0).ratOvr - (offense.getQB(0).ratPassAcc + offense.getQB(0).ratFootIQ + 100) / 3) / 18
                - offense.teamStratOff.getPassProtection() + defense.teamStratDef.getPassProtection();   //STRATEGIES
        if (intChance < 0.015) intChance = 0.015;
        if (100 * Math.random() < intChance) {
            //Interception
            double lb = selLB.ratCoverage * Math.random() * 100;
            double s = selS.ratCoverage * Math.random() * 75;
            double cb = selCB.ratCoverage * Math.random() * 25;
            String defender;

            if (lb >= s & lb >= cb) {
                selLBStats[3]++;
                selLB.statsInts++;
                defender = ("LB " + selLB.name);
            } else if (s > lb && s >= cb) {
                selSStats[3]++;
                selS.statsInts++;
                defender = ("S " + selS.name);
            } else {
                selCBStats[3]++;
                selCB.statsInts++;
                defender = ("CB " + selCB.name);
            }

            qbInterception(offense, defender);
            return;
        }

        //throw ball, check for completion
        double completion = (getHFadv() + normalize(offense.getQB(0).ratPassAcc) + normalize(selTE.ratCatch)
                - normalize(selLB.ratCoverage)) / 2 + 18.25 - pressureOnQB / 16.8 + offense.teamStratOff.getPassProtection() - defense.teamStratDef.getPassProtection();  //STRATEGIES
        if (100 * Math.random() < completion) {
            if (100 * Math.random() < (100 - selTE.ratCatch) / 3) {
                //drop
                gameDown++;
                selTEStats[4]++;
                selTE.statsDrops++;
                passAttemptTE(offense, selTE, selTEStats, yardsGain);
                //Drop ball = inc pass, so run time for the play, stop clock until next play, move on (aka return;)
                gameTime -= timePerPlay * Math.random();
                return;

            } else {
                //no drop
                yardsGain = (int) ((normalize(offense.getQB(0).ratPassPow) + normalize(selTE.ratSpeed) - normalize(selLB.ratSpeed)) * Math.random() / 4.8 //STRATEGIES
                        + offense.teamStratOff.getPassPotential() - defense.teamStratDef.getPassPotential());
                //see if receiver can get yards after catch
                double escapeChance = (normalize(selTE.ratEvasion) * 3 - selLB.ratTackle - defense.getS(0).ratOvr) * Math.random()  //STRATEGIES
                        + offense.teamStratOff.getPassPotential() - defense.teamStratDef.getPassPotential();
                if (escapeChance > 92 || Math.random() > 0.95) {
                    yardsGain += 3 + (selTE.ratSpeed * Math.random() / 4);
                }
                if (escapeChance > 75 && Math.random() < (0.1 + (offense.teamStratOff.getPassPotential() - defense.teamStratDef.getPassPotential()) / 200)) {  //STRATEGIES
                    //wr escapes for TD
                    yardsGain += 100;
                }

                //add yardage
                gameYardLine += yardsGain;
                if (gameYardLine >= 100) { //TD!
                    yardsGain -= gameYardLine - 100;
                    gameYardLine = 100 - yardsGain;
                    addPointsQuarter(6);
                    passingTDTE(offense, selTE, selTEStats, yardsGain);
                    gotTD = true;
                } else {
                    //check for fumble
                    double fumChance = (defense.getS(0).ratTackle + selLB.ratTackle) / 2;
                    if (100 * Math.random() < fumChance / 50) {
                        //Fumble!
                        gotFumble = true;
                    }
                }

                if (!gotTD && !gotFumble) {
                    //check downs if there wasnt fumble or TD
                    gameYardsNeed -= yardsGain;

                    if (escapeChance <= 80) {
                        selLBStats[0]++;
                        selLB.statsTackles++;
                    } else if (escapeChance > 80 && escapeChance < 95) {
                        selSStats[0]++;
                        selS.statsTackles++;
                    } else {
                        selCBStats[0]++;
                        selCB.statsTackles++;
                    }

                    if (gameYardsNeed <= 0) {
                        // Only set new down and distance if there wasn't a TD
                        gameDown = 1;
                        gameYardsNeed = 10;
                    } else gameDown++;
                }

                //stats management
                passCompletionTE(offense, defense, selTE, selTEStats, yardsGain);
            }

        } else {
            //no completion, advance downs
            passAttemptTE(offense, selTE, selTEStats, yardsGain);
            gameDown++;
            //Incomplete pass stops the clock, so just run time for how long the play took, then move on
            gameTime -= timePerPlay * Math.random();
            return;
        }

        passAttemptTE(offense, selTE, selTEStats, yardsGain);


        if (gotFumble) {

            selTEStats[5]++;
            selTE.statsFumbles++;
            String defender;
            if (selS.ratTackle * Math.random() > selLB.ratTackle * Math.random()) {
                selSStats[0]++;
                selSStats[2]++;
                selS.statsTackles++;
                selS.statsFumbles++;
                defender = ("S " + selS.name);
            } else if (selLB.ratTackle * Math.random() > (selCB.ratTackle * Math.random()) * 0.5) {
                selLBStats[0]++;
                selLBStats[2]++;
                selLB.statsTackles++;
                selLB.statsFumbles++;
                defender = ("LB " + selLB.name);
            } else {
                selCBStats[0]++;
                selCBStats[2]++;
                selCB.statsTackles++;
                selCB.statsFumbles++;
                defender = ("CB " + selCB.name);
            }

            gameEventLog += getEventPrefix() + "TURNOVER!\n" + offense.abbr + " TE " + selTE.name + " fumbled the ball after a catch. It was recovered by " + defender + ".";


            if (gamePoss) { // home possession
                homeTOs++;
            } else {
                awayTOs++;
            }
            if (!playingOT) {
                gameDown = 1;
                gameYardsNeed = 10;
                gamePoss = !gamePoss;
                gameYardLine = 100 - gameYardLine;
                gameTime -= timePerPlay * Math.random();
                return;
            } else {
                resetForOT();
                return;
            }
        }

        if (gotTD) {
            gameTime -= timePerPlay * Math.random();
            kickXP(offense, defense);
            if (!playingOT) kickOff(offense);
            else resetForOT();
            return;
        }

        gameTime -= 17 + timePerPlay * Math.random();
    }

    //*************//
    //RUSHING PLAYS**
    //*************//

    /**
     * Rushing Play using running backs.
     *
     * @param offense running the ball
     * @param defense defending the run
     */
    private void rushingPlay(Team offense, Team defense) {
        boolean gotTD = false;
        int[] selQBStats;
        int[] selRBStats;
        int[] selDLStats;
        int[] selLBStats;
        int[] selCBStats;
        int[] selSStats;
        PlayerQB selQB = offense.getQB(0);
        PlayerRB selRB;
        PlayerDL selDL;
        PlayerLB selLB;
        PlayerS selS;
        PlayerCB selCB;

        int playerRB;
        int playerDL;
        int playerLB;
        int playerCB;

        PlayerTE selTE = offense.getTE(0);
        selS = defense.getS(0);

        double RB1pref = Math.pow(offense.getRB(0).ratOvr, 1.5) * Math.random();
        double RB2pref = Math.pow(offense.getRB(1).ratOvr, 1.5) * Math.random();
        double QBpref = Math.pow(offense.getQB(0).ratSpeed, 1.5) * Math.random();

        double DL1pref = Math.pow(defense.getDL(0).ratRunStop, 1) * Math.random();
        double DL2pref = Math.pow(defense.getDL(1).ratRunStop, 1) * Math.random();
        double DL3pref = Math.pow(defense.getDL(2).ratRunStop, 1) * Math.random();
        double DL4pref = Math.pow(defense.getDL(3).ratRunStop, 1) * Math.random();

        double LB1pref = Math.pow(defense.getLB(0).ratRunStop, 1) * Math.random();
        double LB2pref = Math.pow(defense.getLB(1).ratRunStop, 1) * Math.random();
        double LB3pref = Math.pow(defense.getLB(2).ratRunStop, 1) * Math.random();

        double CB1pref = Math.pow(defense.getCB(0).ratTackle, 1) * Math.random();
        double CB2pref = Math.pow(defense.getCB(1).ratTackle, 1) * Math.random();
        double CB3pref = Math.pow(defense.getCB(2).ratTackle, 1) * Math.random();

        if (RB1pref > RB2pref) {
            selRB = offense.getRB(0);
            playerRB = 0;
        } else {
            selRB = offense.getRB(1);
            playerRB = 1;
        }

        if (DL1pref > DL2pref && DL1pref > DL3pref && DL1pref > DL4pref) {
            selDL = defense.getDL(0);
            playerDL = 0;
        } else if (DL2pref > DL1pref && DL2pref > DL3pref && DL2pref > DL4pref) {
            selDL = defense.getDL(1);
            playerDL = 1;
        } else if (DL3pref > DL1pref && DL3pref > DL2pref && DL3pref > DL4pref) {
            selDL = defense.getDL(2);
            playerDL = 2;
        } else {
            selDL = defense.getDL(3);
            playerDL = 3;
        }

        if (LB1pref > LB2pref && LB1pref > LB3pref) {
            selLB = defense.getLB(0);
            playerLB = 0;
        } else if (LB2pref > LB1pref && LB2pref > LB3pref) {
            selLB = defense.getLB(1);
            playerLB = 1;
        } else {
            selLB = defense.getLB(2);
            playerLB = 2;
        }

        if (CB1pref > CB2pref && CB1pref > CB3pref) {
            selCB = defense.getCB(0);
            playerCB = 0;
        } else if (CB2pref > CB1pref && CB2pref > CB3pref) {
            selCB = defense.getCB(1);
            playerCB = 1;
        } else {
            selCB = defense.getCB(2);
            playerCB = 2;
        }

        if (gamePoss) {
            selQBStats = HomeQBStats;
            selSStats = AwaySStats;
            if (playerRB == 0) {
                selRBStats = HomeRB1Stats;
            } else {
                selRBStats = HomeRB2Stats;
            }

            if (playerDL == 0) {
                selDLStats = AwayDL1Stats;
            } else if (playerDL == 1) {
                selDLStats = AwayDL2Stats;
            } else if (playerDL == 2) {
                selDLStats = AwayDL3Stats;
            } else {
                selDLStats = AwayDL4Stats;
            }

            if (playerLB == 0) {
                selLBStats = AwayLB1Stats;
            } else if (playerLB == 1) {
                selLBStats = AwayLB2Stats;
            } else {
                selLBStats = AwayLB3Stats;
            }

            if (playerCB == 0) {
                selCBStats = AwayCB1Stats;
            } else if (playerCB == 1) {
                selCBStats = AwayCB2Stats;
            } else {
                selCBStats = AwayCB3Stats;
            }
        } else {
            selQBStats = AwayQBStats;
            selSStats = HomeSStats;

            if (playerRB == 0) {
                selRBStats = AwayRB1Stats;
            } else {
                selRBStats = AwayRB2Stats;
            }

            if (playerDL == 0) {
                selDLStats = HomeDL1Stats;
            } else if (playerDL == 1) {
                selDLStats = HomeDL2Stats;
            } else if (playerDL == 2) {
                selDLStats = HomeDL3Stats;
            } else {
                selDLStats = HomeDL4Stats;
            }

            if (playerLB == 0) {
                selLBStats = HomeLB1Stats;
            } else if (playerLB == 1) {
                selLBStats = HomeLB2Stats;
            } else {
                selLBStats = HomeLB3Stats;
            }

            if (playerCB == 0) {
                selCBStats = HomeCB1Stats;
            } else if (playerCB == 1) {
                selCBStats = HomeCB2Stats;
            } else {
                selCBStats = HomeCB3Stats;
            }
        }

        if (offense.teamStratOffNum == 4 && QBpref > RB1pref && QBpref > RB2pref) {
            RushPlayQB(offense, defense, selQB, selTE, selDL, selLB, selCB, selS, selQBStats, selDLStats, selLBStats, selCBStats, selSStats);

        } else if (QBpref * 0.2 > RB1pref && QBpref * 0.2 > RB2pref) {
            RushPlayQB(offense, defense, selQB, selTE, selDL, selLB, selCB, selS, selQBStats, selDLStats, selLBStats, selCBStats, selSStats);

        } else {
            RushPlayRB(offense, defense, selRB, selTE, selDL, selLB, selCB, selS, selRBStats, selDLStats, selLBStats, selCBStats, selSStats);
        }

    }

    private void RushPlayRB(Team offense, Team defense, PlayerRB selRB, PlayerTE selTE, PlayerDL selDL, PlayerLB selLB, PlayerCB selCB, PlayerS selS, int[] selRBStats, int[] selDLStats, int[] selLBStats, int[] selCBStats, int[] selSStats) {
        boolean gotTD;
        gotTD = false;

        int blockAdv = offense.getCompositeOLRush() - defense.getCompositeDLRush() + (offense.teamStratDef.getRunProtection() - defense.teamStratDef.getRunProtection());  //STRATEGIES
        int blockAdvOutside = selTE.ratRunBlock - selLB.ratRunStop + (offense.teamStratDef.getRunUsage() - defense.teamStratDef.getRunUsage());
        int yardsGain = (int) ((selRB.ratSpeed + blockAdv + blockAdvOutside + getHFadv()) * Math.random() / 10 + (double) offense.teamStratOff.getRunPotential() / 2 - (double) defense.teamStratDef.getRunPotential() / 2);  //STRATEGIES

        if (yardsGain < 2) {
            yardsGain += selRB.ratRushPower / 20 - 3 - (double) defense.teamStratDef.getRunPotential() / 2;  //STRATEGIES
        } else {
            //break free from tackles
            if (Math.random() < (0.28 + (offense.teamStratOff.getRunPotential() - (double) defense.teamStratDef.getRunPotential() / 2) / 50)) {  //STRATEGIES
                yardsGain += (selRB.ratEvasion - blockAdvOutside) / 5 * Math.random();
            }
        }

        //add yardage
        gameYardLine += yardsGain;

        if (gameYardLine >= 100) { //TD!
            addPointsQuarter(6);
            yardsGain -= gameYardLine - 100;
            gameYardLine = 100 - yardsGain;

            selRBStats[2]++;
            selRB.statsRushTD++;

            if (gamePoss) { // home possession
                homeScore += 6;
            } else {
                awayScore += 6;
            }
            tdInfo = offense.abbr + " RB " + selRB.name + " rushed " + yardsGain + " yards for a TD.";
            //offense.teamPoints += 6;
            //defense.teamOppPoints += 6;

            gotTD = true;
        }

        //check downs if there wasn't TD
        if (!gotTD) {
            //check downs
            gameYardsNeed -= yardsGain;
            if (gameYardsNeed <= 0) {
                // Only set new down and distance if there wasn't a TD
                gameDown = 1;
                gameYardsNeed = 10;
            } else gameDown++;
        }

        if (yardsGain < 2) {
            selDLStats[0]++;
            selDL.statsTackles++;
        } else if (yardsGain >= 2 && yardsGain < 9) {
            selLBStats[0]++;
            selLB.statsTackles++;
        } else {
            if (selCB.ratTackle * Math.random() * 50 >= selS.ratTackle * Math.random() * 100) {
                selCBStats[0]++;
                selCB.statsTackles++;
            } else {
                selSStats[0]++;
                selS.statsTackles++;
            }
        }
        //stats management
        rushAttempt(offense, defense, selRB, selRBStats, yardsGain);

        if (gotTD) {
            gameTime -= 5 + timePerPlay * Math.random(); // Clock stops for the TD, just burn time for the play
            kickXP(offense, defense);
            if (!playingOT) kickOff(offense);
            else resetForOT();
        } else {
            gameTime -= timePerPlay + timePerPlay * Math.random();
            //check for fumble
            double fumChance = ((defense.getS(0).ratTackle + selLB.ratTackle) / 2 + defense.getCompositeDLRush() - getHFadv()) / 2 + offense.teamStratOff.getRunProtection();  //STRATEGIES
            if (100 * Math.random() < fumChance / 50) {
                //Fumble!

                selRBStats[3]++;
                selRB.statsFumbles++;
                if (gamePoss) {
                    homeTOs++;
                } else {
                    awayTOs++;
                }

                String defender;
                double cb = selCB.ratTackle * Math.random() * 20;
                double s = selS.ratTackle * Math.random() * 70;
                double lb = selLB.ratTackle * Math.random() * 100;

                if (cb > s && cb > lb) {
                    selCBStats[0]++;
                    selCB.statsTackles++;
                    selCBStats[2]++;
                    selCB.statsFumbles++;
                    defender = ("CB " + selCB.name);
                } else if (s >= cb && s > lb) {
                    selSStats[0]++;
                    selS.statsTackles++;
                    selSStats[2]++;
                    selS.statsFumbles++;
                    defender = ("S " + selS.name);
                } else {
                    selLBStats[0]++;
                    selLB.statsTackles++;
                    selLBStats[2]++;
                    selLB.statsFumbles++;
                    defender = ("LB " + selLB.name);
                }

                gameEventLog += getEventPrefix() + "TURNOVER!\n" + offense.abbr + " RB " + selRB.name + " fumbled the ball while rushing and recovered by " + defender + ".";

                if (!playingOT) {
                    gameDown = 1;
                    gameYardsNeed = 10;
                    gamePoss = !gamePoss;
                    gameYardLine = 100 - gameYardLine;
                } else resetForOT();
            }
        }
    }

    private void RushPlayQB(Team offense, Team defense, PlayerQB selQB, PlayerTE selTE, PlayerDL selDL, PlayerLB selLB, PlayerCB selCB, PlayerS selS, int[] selQBStats, int[] selDLStats, int[] selLBStats, int[] selCBStats, int[] selSStats) {
        boolean gotTD;
        gotTD = false;

        int blockAdv = offense.getCompositeOLRush() - defense.getCompositeDLRush() + (offense.teamStratDef.getRunProtection() - defense.teamStratDef.getRunProtection());  //STRATEGIES
        int blockAdvOutside = selTE.ratRunBlock - selLB.ratRunStop + (offense.teamStratDef.getRunUsage() - defense.teamStratDef.getRunUsage());
        int yardsGain = (int) ((selQB.ratSpeed + blockAdv + blockAdvOutside + getHFadv()) * Math.random() / 10 + (double) offense.teamStratOff.getRunPotential() / 2 - (double) defense.teamStratDef.getRunPotential() / 2);  //STRATEGIES

        if (yardsGain < 2) {
            yardsGain += selQB.ratEvasion / 20 - 3 - (double) defense.teamStratDef.getRunPotential() / 2;  //STRATEGIES
        } else {
            //break free from tackles
            if (Math.random() < (0.20 + (offense.teamStratOff.getRunPotential() - (double) defense.teamStratDef.getRunPotential() / 2) / 50)) {  //STRATEGIES
                yardsGain += (selQB.ratEvasion - blockAdvOutside) / 5 * Math.random();
            }
        }

        //add yardage
        gameYardLine += yardsGain;

        if (gameYardLine >= 100) { //TD!
            addPointsQuarter(6);
            yardsGain -= gameYardLine - 100;
            gameYardLine = 100 - yardsGain;

            selQBStats[8]++;
            selQB.statsRushTD++;

            if (gamePoss) { // home possession
                homeScore += 6;
            } else {
                awayScore += 6;
            }

            tdInfo = offense.abbr + " QB " + selQB.name + " rushed " + yardsGain + " yards for a TD.";
            //offense.teamPoints += 6;
            //defense.teamOppPoints += 6;

            gotTD = true;
        }

        //check downs if there wasn't TD
        if (!gotTD) {
            //check downs
            gameYardsNeed -= yardsGain;
            if (gameYardsNeed <= 0) {
                // Only set new down and distance if there wasn't a TD
                gameDown = 1;
                gameYardsNeed = 10;
            } else gameDown++;
        }

        if (yardsGain < 2) {
            selDLStats[0]++;
            selDL.statsTackles++;
        } else if (yardsGain >= 2 && yardsGain < 9) {
            selLBStats[0]++;
            selLB.statsTackles++;
        } else {
            if (selCB.ratTackle * Math.random() * 50 >= selS.ratTackle * Math.random() * 100) {
                selCBStats[0]++;
                selCB.statsTackles++;
            } else {
                selSStats[0]++;
                selS.statsTackles++;
            }
        }
        //stats management
        rushAttemptQB(offense, defense, selQB, selQBStats, yardsGain);

        if (gotTD) {
            gameTime -= 5 + timePerPlay * Math.random(); // Clock stops for the TD, just burn time for the play
            kickXP(offense, defense);
            if (!playingOT) kickOff(offense);
            else resetForOT();
        } else {
            gameTime -= timePerPlay + timePerPlay * Math.random();
            //check for fumble
            double fumChance = ((defense.getS(0).ratTackle + selLB.ratTackle) / 2 + defense.getCompositeDLRush() - getHFadv()) / 2 + offense.teamStratOff.getRunProtection();  //STRATEGIES
            if (95 * Math.random() < fumChance / 50) {
                //Fumble!

                selQBStats[8]++;
                selQB.statsFumbles++;

                if (gamePoss) {
                    homeTOs++;
                } else {
                    awayTOs++;
                }

                String defender;
                double cb = selCB.ratTackle * Math.random() * 20;
                double s = selS.ratTackle * Math.random() * 70;
                double lb = selLB.ratTackle * Math.random() * 100;

                if (cb > s && cb > lb) {
                    selCBStats[0]++;
                    selCB.statsTackles++;
                    selCBStats[2]++;
                    selCB.statsFumbles++;
                    defender = ("CB " + selCB.name);
                } else if (s >= cb && s > lb) {
                    selSStats[0]++;
                    selS.statsTackles++;
                    selSStats[2]++;
                    selS.statsFumbles++;
                    defender = ("S " + selS.name);
                } else {
                    selLBStats[0]++;
                    selLB.statsTackles++;
                    selLBStats[2]++;
                    selLB.statsFumbles++;
                    defender = ("LB " + selLB.name);
                }


                gameEventLog += getEventPrefix() + "TURNOVER!\n" + offense.abbr + " QB " + selQB.name + " fumbled the ball while rushing and recovered by " + defender + ".";

                if (!playingOT) {
                    gameDown = 1;
                    gameYardsNeed = 10;
                    gamePoss = !gamePoss;
                    gameYardLine = 100 - gameYardLine;
                } else resetForOT();
            }
        }
    }


    /**
     * Attempt a field goal using the offense's kicker.
     * If successful, add 3 points and kick off.
     * If not, turn the ball over.
     *
     * @param offense kicking the ball
     * @param defense defending the kick
     */
    private void fieldGoalAtt(Team offense, Team defense) {
        double fgDistRatio = Math.pow((110 - gameYardLine) / 50, 2);
        double fgAccRatio = Math.pow((110 - gameYardLine) / 50, 1.25);
        double fgDistChance = (getHFadv() + offense.getK(0).ratKickPow - fgDistRatio * 80);
        double fgAccChance = (getHFadv() + offense.getK(0).ratKickAcc - fgAccRatio * 80);
        if (fgDistChance > 20 && fgAccChance * Math.random() > 15) {
            // made the fg
            if (gamePoss) { // home possession
                homeScore += 3;
                HomeKStats[3]++;
                HomeKStats[2]++;
            } else {
                awayScore += 3;
                AwayKStats[3]++;
                AwayKStats[2]++;
            }
            gameEventLog += getEventPrefix() + offense.abbr + " K " + offense.getK(0).name + " made the " + (110 - gameYardLine) + " yard FG.";
            addPointsQuarter(3);
            offense.getK(0).statsFGMade++;
            offense.getK(0).statsFGAtt++;
            if (!playingOT) kickOff(offense);
            else resetForOT();

        } else {
            //miss
            gameEventLog += getEventPrefix() + offense.abbr + " K " + offense.getK(0).name + " missed the " + (110 - gameYardLine) + " yard FG.";
            offense.getK(0).statsFGAtt++;
            if (!playingOT) {
                gameYardLine = Math.max(100 - gameYardLine, 20); //Misses inside the 20 = defense takes over on the 20
                gameDown = 1;
                gameYardsNeed = 10;
                if (gamePoss) { // home possession
                    HomeKStats[3]++;
                } else {
                    AwayKStats[3]++;
                }
                gamePoss = !gamePoss;
            } else resetForOT();


        }

        gameTime -= 20;

    }

    /**
     * Kick the extra point after the touchdown.
     * Will decide to go for 2 under certain circumstances.
     *
     * @param offense going for the point after
     * @param defense defending the point after
     */
    private void kickXP(Team offense, Team defense) {
        // No XP/2pt try if the TD puts the bottom OT offense ahead (aka wins the game)
        if (playingOT && bottomOT && (((numOT % 2 == 0) && awayScore > homeScore) || ((numOT % 2 != 0) && homeScore > awayScore))) {
            gameEventLog += getEventPrefix() + " " + tdInfo + "\n" + offense.abbr + " wins on a walk-off touchdown!";
        }
        // If a TD is scored as time expires, there is no XP/2pt if the score difference is greater than 2
        else if (!playingOT && gameTime <= 0 && ((homeScore - awayScore > 2) || (awayScore - homeScore > 2))) {
            //Walkoff TD!
            if ((Math.abs(homeScore - awayScore) < 7) && ((gamePoss && homeScore > awayScore) || (!gamePoss && awayScore > homeScore)))
                gameEventLog += getEventPrefix() + " " + tdInfo + "\n" + offense.abbr + " wins on a walk-off touchdown!";
                //Just rubbing in the win or saving some pride
            else gameEventLog += getEventPrefix() + " " + tdInfo;
        } else {
            if ((numOT >= 3) || (((gamePoss && (awayScore - homeScore) == 2) || (!gamePoss && (homeScore - awayScore) == 2)) && gameTime < 300)) {
                //go for 2
                boolean successConversion = false;
                if (Math.random() <= 0.50) {
                    //rushing
                    int blockAdv = offense.getCompositeOLRush() - defense.getCompositeDLRush();
                    int yardsGain = (int) ((offense.getRB(0).ratSpeed + blockAdv) * Math.random() / 6);
                    if (yardsGain > 5) {
                        successConversion = true;
                        if (gamePoss) { // home possession
                            homeScore += 2;
                        } else {
                            awayScore += 2;
                        }
                        addPointsQuarter(2);
                        gameEventLog += getEventPrefix() + " " + tdInfo + " " + offense.getRB(0).name + " rushed for the 2pt conversion.";
                    } else {
                        gameEventLog += getEventPrefix() + " " + tdInfo + " " + offense.getRB(0).name + " stopped at the line of scrimmage, failed the 2pt conversion.";
                    }
                } else {
                    int pressureOnQB = defense.getCompositeDLPass() * 2 - offense.getCompositeOLPass();
                    double completion = (normalize(offense.getQB(0).ratPassAcc) + offense.getWR(0).ratCatch - defense.getCB(0).ratCoverage) / 2 + 25 - pressureOnQB / 20;
                    if (100 * Math.random() < completion) {
                        successConversion = true;
                        if (gamePoss) { // home possession
                            homeScore += 2;
                        } else {
                            awayScore += 2;
                        }
                        addPointsQuarter(2);
                        gameEventLog += getEventPrefix() + " " + tdInfo + " " + offense.getQB(0).name + " completed the pass to " + offense.getWR(0).name + " for the 2pt conversion.";
                    } else {
                        gameEventLog += getEventPrefix() + " " + tdInfo + " " + offense.getQB(0).name + "'s pass incomplete to " + offense.getWR(0).name + " for the failed 2pt conversion.";
                    }
                }

            } else {
                //kick XP
                if (Math.random() * 100 < 23 + offense.getK(0).ratKickAcc && Math.random() > 0.01) {
                    //made XP
                    if (gamePoss) { // home possession
                        homeScore += 1;
                        HomeKStats[0]++;
                        HomeKStats[1]++;
                    } else {
                        awayScore += 1;
                        AwayKStats[0]++;
                        AwayKStats[1]++;
                    }
                    gameEventLog += getEventPrefix() + " " + tdInfo + " " + offense.getK(0).name + " made the XP.";
                    addPointsQuarter(1);
                    offense.getK(0).statsXPMade++;
                } else {
                    gameEventLog += getEventPrefix() + " " + tdInfo + " " + offense.getK(0).name + " missed the XP.";
                    // missed XP
                    if (gamePoss) { // home possession
                        HomeKStats[1]++;
                    } else {
                        AwayKStats[1]++;
                    }
                }
                offense.getK(0).statsXPAtt++;
            }
        }
    }

    /**
     * Kick the ball off, turning the ball over to the other team.
     *
     * @param offense kicking the ball off
     */
    private void kickOff(Team offense) {
        if (gameTime <= 0) return;
        else {
            //Decide whether to onside kick. Only if losing but within 8 points with < 3 min to go
            if (gameTime < 180 && ((gamePoss && (awayScore - homeScore) <= 8 && (awayScore - homeScore) > 0)
                    || (!gamePoss && (homeScore - awayScore) <= 8 && (homeScore - awayScore) > 0))) {
                // Yes, do onside
                if (offense.getK(0).ratKickFum * Math.random() > 60 || Math.random() < 0.1) {
                    //Success!
                    gameEventLog += getEventPrefix() + offense.abbr + " K " + offense.getK(0).name + " successfully executes onside kick! " + offense.abbr + " has possession!";
                } else {
                    // Fail
                    gameEventLog += getEventPrefix() + offense.abbr + " K " + offense.getK(0).name + " failed the onside kick and lost possession.";
                    gamePoss = !gamePoss;
                }
                gameYardLine = 50;
                gameDown = 1;
                gameYardsNeed = 10;

                gameTime -= 4 + 5 * Math.random(); //Onside kicks are very fast, unless there's a weird fight for the ball. Chance to burn a lot of time, odds are you'll burn a little time.
            } else {
                // Just regular kick off
                gameYardLine = (int) (100 - (offense.getK(0).ratKickPow + 20 - 40 * Math.random()));    //FUTURE IMPLEMENTATION: WR speed affect Kick return
                if (gameYardLine <= 0) gameYardLine = 25;
                gameDown = 1;
                gameYardsNeed = 10;
                gamePoss = !gamePoss;
            }

            gameTime -= timePerPlay * Math.random();
        }
    }

    /**
     * Kick the ball off following a safety, turning the ball over to the other team.
     * Safety free kicks happen from the 20 instead of the 35, so start the kicker from further back.
     *
     * @param offense kicking the ball off
     */
    private void freeKick(Team offense) {
        if (gameTime <= 0) return;
        else {
            //Decide whether to onside kick. Only if losing but within 8 points with < 3 min to go
            if (gameTime < 180 && ((gamePoss && (awayScore - homeScore) <= 8 && (awayScore - homeScore) > 0)
                    || (!gamePoss && (homeScore - awayScore) <= 8 && (homeScore - awayScore) > 0))) {
                // Yes, do onside
                if (offense.getK(0).ratKickFum * Math.random() > 60 || Math.random() < 0.1) {
                    //Success!
                    gameEventLog += getEventPrefix() + offense.abbr + " K " + offense.getK(0).name + " successfully executes onside kick! " + offense.abbr + " has possession!";
                    gameYardLine = 35;
                    gameDown = 1;
                    gameYardsNeed = 10;
                } else {
                    // Fail
                    gameEventLog += getEventPrefix() + offense.abbr + " K " + offense.getK(0).name + " failed the onside kick and lost possession.";
                    gamePoss = !gamePoss;
                    gameYardLine = 65;
                    gameDown = 1;
                    gameYardsNeed = 10;
                }

                gameTime -= 4 + 4 * Math.random(); //Onside kicks are very fast, unless there's a weird fight for the ball. Chance to burn a lot of time, odds are you'll burn a little time.
            } else {
                // Just regular kick off
                gameYardLine = (int) (115 - (offense.getK(0).ratKickPow + 20 - 40 * Math.random()));
                if (gameYardLine <= 0) gameYardLine = 25;
                gameDown = 1;
                gameYardsNeed = 10;
                gamePoss = !gamePoss;
                gameTime -= timePerPlay * Math.random();

            }
        }
    }

    /**
     * Punt the ball if it is a 4th down and decided not to go for it.
     * Will turnover possession.
     *
     * @param offense kicking the punt
     */
    private void puntPlay(Team offense) {
        gameYardLine = (int) (100 - (gameYardLine + offense.getK(0).ratKickPow / 3 + 20 - 10 * Math.random()));
        if (gameYardLine < 0) {
            gameYardLine = 20;
        }
        gameDown = 1;
        gameYardsNeed = 10;
        gamePoss = !gamePoss;

        gameTime -= timePerPlay + timePerPlay * Math.random();
    }

    /**
     * Sack the offense's QB. If it is past 0 yard line, call for a safety.
     *
     * @param offense offense that is gettign sacked
     */
    private void qbSack(Team offense, Team defense, String defender) {
        int sackloss = (3 + (int) (Math.random() * (normalize(defense.getCompositeDLPass()) - normalize(offense.getCompositeOLPass())) / 2));
        if (sackloss < 3) sackloss = 3;
        offense.getQB(0).statsSacked++;
        //offense.getQB(0).statsRushYards -= sackloss;
        gameYardsNeed += sackloss;
        gameYardLine -= sackloss;

        if (gamePoss) { // home possession
            HomeQBStats[6]++;
            HomeQBStats[7] -= sackloss;
            HomeQBStats[5]++;
            homeYards -= sackloss;
            homeTeam.teamRushYards -= sackloss;
            awayTeam.teamOppRushYards -= sackloss;
        } else {
            AwayQBStats[6]++;
            AwayQBStats[7] -= sackloss;
            AwayQBStats[5]++;
            awayYards -= sackloss;
            awayTeam.teamRushYards -= sackloss;
            homeTeam.teamOppRushYards -= sackloss;
        }

        if (gameYardLine < 0) {
            // Safety!
            // Eat some time up for the play that was run, stop it once play is over
            gameTime -= 10 * Math.random();
            safety(defender);
            return; // Run safety then get out of qbSack (safety() will take care of free kick)
        } else {

            gameEventLog += getEventPrefix() + "SACK!\n" + " QB " + offense.getQB(0).name +
                    " was sacked for a loss of " + sackloss + " by " + defender + ".";
        }


        gameDown++; // Advance gameDown after checking for Safety, otherwise game log reports Safety occurring one down later than it did

        //Similar amount of time as rushing, minus some in-play time -- sacks are faster (usually)
        gameTime -= timePerPlay + timePerPlay * Math.random();
    }

    /**
     * Perform safety. Will add 2 to the correct team and give the ball over via a free kick.
     */
    private void safety(String defender) {
        //addPointsQuarter(2);
        if (gamePoss) {
            awayScore += 2;
            gameEventLog += getEventPrefix() + "SAFETY!\n" + homeTeam.abbr + " QB " + homeTeam.getQB(0).name +
                    " was tackled in the endzone by " + defender + "! Result is a safety and " + awayTeam.abbr + " will get possession.";
            freeKick(homeTeam);
        } else {
            homeScore += 2;
            gameEventLog += getEventPrefix() + "SAFETY!\n" + awayTeam.abbr + " QB " + awayTeam.getQB(0).name +
                    " was tackled in the endzone by " + defender + "! Result is a safety and " + homeTeam.abbr + " will get possession.";
            freeKick(awayTeam);
        }
    }

    /**
     * Perform an interception on the offense's QB. Will turn the ball over and add the needed stats.
     *
     * @param offense offense that has been intercepted.
     */
    private void qbInterception(Team offense, String defender) {
        if (gamePoss) { // home possession
            HomeQBStats[3]++;
            HomeQBStats[1]++;
            homeTOs++;
        } else {
            AwayQBStats[3]++;
            AwayQBStats[1]++;
            awayTOs++;
        }
        offense.getQB(0).statsInt++;

        //Log the event before decreasing the time, in keeping with the standard of other logged plays (TD, Fumble, etc.)
        gameEventLog += getEventPrefix() + "TURNOVER!\n" + offense.abbr + " QB " + offense.getQB(0).name + " was intercepted by " + defender + ".";
        //Clock stops after a pick, so just run time off the clock for the play that occurred
        //NOTE: If the ability to run an interception back is ever added, this should be changed to be more time
        gameTime -= timePerPlay * Math.random();
        if (!playingOT) {
            gameDown = 1;
            gameYardsNeed = 10;
            gamePoss = !gamePoss;
            gameYardLine = 100 - gameYardLine;
        } else resetForOT();
    }

    /**
     * Passing touchdown stat tracking. Add 6 points and yards for QB/WR.
     *
     * @param offense    offense who got the TD
     * @param selWR      WR who caught the TD
     * @param selWRStats stats array for that WR
     * @param yardsGain  number of yards gained by the TD
     */
    private void passingTD(Team offense, PlayerWR selWR, int[] selWRStats, int yardsGain) {
        if (gamePoss) { // home possession
            homeScore += 6;
            HomeQBStats[2]++;
            selWRStats[3]++;
        } else {
            awayScore += 6;
            AwayQBStats[2]++;
            selWRStats[3]++;
        }
        tdInfo = offense.abbr + " QB " + offense.getQB(0).name + " threw a " + yardsGain + " yard TD to WR " + selWR.name + ".";
        offense.getQB(0).statsPassTD++;
        selWR.statsTD++;
    }

    /**
     * Pass completion stat tracking. used for team mostly.
     *
     * @param offense    offense who threw the pass
     * @param defense    defense who defended the pass
     * @param selWR      WR who caught the pass
     * @param selWRStats stat array for that WR
     * @param yardsGain  number of yards gained by that pass
     */
    private void passCompletion(Team offense, Team defense, PlayerWR selWR, int[] selWRStats, int yardsGain) {
        offense.getQB(0).statsPassComp++;
        offense.getQB(0).statsPassYards += yardsGain;
        selWR.statsReceptions++;
        selWR.statsRecYards += yardsGain;
        offense.teamPassYards += yardsGain;

        if (gamePoss) { // home possession
            HomeQBStats[0]++;
            selWRStats[0]++;
        } else {
            AwayQBStats[0]++;
            selWRStats[0]++;
        }
    }

    /**
     * Stat tracking for a pass attempt (not necessarily completion). Used for QB mostly.
     *
     * @param offense    offense who threw the pass
     * @param selWR      WR who tries to catch the pass
     * @param selWRStats stat array for that WR
     * @param yardsGain  yards
     */
    private void passAttempt(Team offense, PlayerWR selWR, int[] selWRStats, int yardsGain) {
        offense.getQB(0).statsPassAtt++;
        selWR.statsTargets++;

        if (gamePoss) { // home possession
            homeYards += yardsGain;
            HomeQBStats[4] += yardsGain;
            HomeQBStats[1]++;
            selWRStats[2] += yardsGain;
            selWRStats[1]++;
        } else {
            awayYards += yardsGain;
            AwayQBStats[4] += yardsGain;
            AwayQBStats[1]++;
            selWRStats[2] += yardsGain;
            selWRStats[1]++;
        }
    }


    /**
     * Passing touchdown stat tracking. Add 6 points and yards for QB/WR.
     *
     * @param offense    offense who got the TD
     * @param selTE      TE who caught the TD
     * @param selTEStats stats array for that WR
     * @param yardsGain  number of yards gained by the TD
     */
    private void passingTDTE(Team offense, PlayerTE selTE, int[] selTEStats, int yardsGain) {
        if (gamePoss) { // home possession
            homeScore += 6;
            HomeQBStats[2]++;
            selTEStats[3]++;
        } else {
            awayScore += 6;
            AwayQBStats[2]++;
            selTEStats[3]++;
        }
        tdInfo = offense.abbr + " QB " + offense.getQB(0).name + " threw a " + yardsGain + " yard TD to TE " + selTE.name + ".";
        offense.getQB(0).statsPassTD++;
        selTE.statsRecTD++;
    }

    /**
     * Pass completion stat tracking. used for team mostly.
     *
     * @param offense    offense who threw the pass
     * @param defense    defense who defended the pass
     * @param selTE      TE who caught the pass
     * @param selTEStats stat array for that WR
     * @param yardsGain  number of yards gained by that pass
     */
    private void passCompletionTE(Team offense, Team defense, PlayerTE selTE, int[] selTEStats, int yardsGain) {
        offense.getQB(0).statsPassComp++;
        offense.getQB(0).statsPassYards += yardsGain;
        selTE.statsReceptions++;
        selTE.statsRecYards += yardsGain;
        offense.teamPassYards += yardsGain;

        if (gamePoss) { // home possession
            HomeQBStats[0]++;
            selTEStats[0]++;
        } else {
            AwayQBStats[0]++;
            selTEStats[0]++;
        }
    }

    /**
     * Stat tracking for a pass attempt (not necessarily completion). Used for QB mostly.
     *
     * @param offense    offense who threw the pass
     * @param selTE      TE who tries to catch the pass
     * @param selTEStats stat array for that WR
     * @param yardsGain  yards
     */
    private void passAttemptTE(Team offense, PlayerTE selTE, int[] selTEStats, int yardsGain) {
        offense.getQB(0).statsPassAtt++;
        selTE.statsTargets++;

        if (gamePoss) { // home possession
            homeYards += yardsGain;
            HomeQBStats[4] += yardsGain;
            HomeQBStats[1]++;
            selTEStats[2] += yardsGain;
            selTEStats[1]++;
        } else {
            awayYards += yardsGain;
            AwayQBStats[4] += yardsGain;
            AwayQBStats[1]++;
            selTEStats[2] += yardsGain;
            selTEStats[1]++;
        }
    }

    /**
     * Rush attempt stat tracking
     *
     * @param offense   offense who ran the run play
     * @param defense   defense who defended the play
     * @param selRB     RB who ran the ball
     * @param yardsGain yards gained by the rush
     */
    private void rushAttempt(Team offense, Team defense, PlayerRB selRB, int[] selRBStats, int yardsGain) {
        selRB.statsRushAtt++;
        selRB.statsRushYards += yardsGain;

        selRBStats[0]++;
        selRBStats[1] += yardsGain;
        if (gamePoss) { // home possession
            homeYards += yardsGain;
            homeTeam.teamRushYards += yardsGain;
            awayTeam.teamOppRushYards += yardsGain;
        } else {
            awayYards += yardsGain;
            awayTeam.teamRushYards += yardsGain;
            homeTeam.teamOppRushYards += yardsGain;

        }
    }

    private void rushAttemptQB(Team offense, Team defense, PlayerQB selQB, int[] selQBStats, int yardsGain) {
        offense.getQB(0).statsRushAtt++;
        offense.getQB(0).statsRushYards += yardsGain;

        if (gamePoss) { // home possession
            homeYards += yardsGain;
            HomeQBStats[6]++;
            HomeQBStats[7] += yardsGain;
            homeTeam.teamRushYards += yardsGain;
            awayTeam.teamOppRushYards -= yardsGain;
        } else {
            awayYards += yardsGain;
            AwayQBStats[6]++;
            AwayQBStats[7] += yardsGain;
            awayTeam.teamRushYards += yardsGain;
            homeTeam.teamOppRushYards -= yardsGain;
        }
    }

    /**
     * Add points to the correct quarter (not used anymore?)
     *
     * @param points points added
     */
    private void addPointsQuarter(int points) {
        if (gamePoss) {
            //home poss
            if (gameTime > 2700) {
                homeQScore[0] += points;
            } else if (gameTime > 1800) {
                homeQScore[1] += points;
            } else if (gameTime > 900) {
                homeQScore[2] += points;
            } else if (numOT == 0) {
                homeQScore[3] += points;
            } else {
                if (3 + numOT < 10) homeQScore[3 + numOT] += points;
                else homeQScore[9] += points;
            }
        } else {
            //away
            if (gameTime > 2700) {
                awayQScore[0] += points;
            } else if (gameTime > 1800) {
                awayQScore[1] += points;
            } else if (gameTime > 900) {
                awayQScore[2] += points;
            } else if (numOT == 0) {
                awayQScore[3] += points;
            } else {
                if (3 + numOT < 10) awayQScore[3 + numOT] += points;
                else awayQScore[9] += points;
            }
        }
    }

    /**
     * Normalize a rating to make it more even. 80->90, 90->95, etc
     *
     * @param rating rating to be normalized
     * @return normalized rating
     */
    private int normalize(int rating) {
        return (100 + rating) / 2;
    }
}
