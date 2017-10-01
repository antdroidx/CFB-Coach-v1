package CFBsimPack;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/* TEAM class

 */

public class Team {

    public League league;

    public String name;
    public String abbr;
    public String conference;
    public String rivalTeam;
    public boolean wonRivalryGame;
    public ArrayList<String> teamHistory;
    public ArrayList<String> hallOfFame;
    public boolean userControlled;
    public boolean showPopups;
    public int recruitMoney;
    public int numRecruits;

    public int wins;
    public int losses;
    public int totalWins;
    public int totalLosses;
    public int totalCCs;
    public int totalNCs;
    public int totalCCLosses;
    public int totalNCLosses;
    public int totalBowls;
    public int totalBowlLosses;
    public String evenYearHomeOpp;

    public TeamStreak winStreak;
    public TeamStreak yearStartWinStreak;

    //Game Log variables
    public ArrayList<Game> gameSchedule;
    public Game gameOOCSchedule0;
    public Game gameOOCSchedule4;
    public Game gameOOCSchedule9;
    public ArrayList<String> gameWLSchedule;
    public ArrayList<Team> gameWinsAgainst;
    public String confChampion;
    public String semiFinalWL;
    public String natChampWL;

    //Team stats
    public int teamPoints;
    public int teamOppPoints;
    public int teamYards;
    public int teamOppYards;
    public int teamPassYards;
    public int teamRushYards;
    public int teamOppPassYards;
    public int teamOppRushYards;
    public int teamTODiff;
    public int teamOffTalent;
    public int teamDefTalent;
    public int teamPrestige;
    public int teamPollScore;
    public int teamStrengthOfWins;

    public int rankTeamPoints;
    public int rankTeamOppPoints;
    public int rankTeamYards;
    public int rankTeamOppYards;
    public int rankTeamPassYards;
    public int rankTeamRushYards;
    public int rankTeamOppPassYards;
    public int rankTeamOppRushYards;
    public int rankTeamTODiff;
    public int rankTeamOffTalent;
    public int rankTeamDefTalent;
    public int rankTeamPrestige;
    public int rankTeamRecruitClass;
    public int rankTeamPollScore;
    public int rankTeamStrengthOfWins;

    //prestige/talent improvements
    public int diffPrestige;
    public int diffOffTalent;
    public int diffDefTalent;

    //players on team
    //offense
    public ArrayList<PlayerQB> teamQBs;
    public ArrayList<PlayerRB> teamRBs;
    public ArrayList<PlayerWR> teamWRs;
    public ArrayList<PlayerK> teamKs;
    public ArrayList<PlayerOL> teamOLs;
    //defense
    public ArrayList<PlayerF7> teamF7s;
    public ArrayList<PlayerS> teamSs;
    public ArrayList<PlayerCB> teamCBs;
    //By year
    public ArrayList<Player> teamRSs;
    public ArrayList<Player> teamFRs;
    public ArrayList<Player> teamSOs;
    public ArrayList<Player> teamJRs;
    public ArrayList<Player> teamSRs;

    public ArrayList<Player> playersLeaving;
    public ArrayList<Player> playersInjured;
    public ArrayList<Player> playersRecovered;
    public ArrayList<Player> playersInjuredAll;

    public TeamStrategy teamStratOff;
    public TeamStrategy teamStratDef;
    public int teamStratOffNum;
    public int teamStratDefNum;

    public boolean confTVDeal;
    public boolean teamTVDeal;

    private static final int NFL_OVR = 90;
    private static final double NFL_CHANCE = 0.5;

    /**
     * Creates new team, recruiting needed players and setting team stats to 0.
     * @param name name of the team
     * @param abbr abbreviation of the team, 3 letters
     * @param conference conference the team is in
     * @param league reference to the league object all must obey
     * @param prestige prestige of that team, between 0-100
     */
    public Team( String name, String abbr, String conference, League league, int prestige, String rivalTeamAbbr ) {
        this.league = league;
        userControlled = false;
        showPopups = true;
        teamHistory = new ArrayList<String>();
        hallOfFame = new ArrayList<>();
        playersInjuredAll = new ArrayList<>();

        teamQBs = new ArrayList<PlayerQB>();
        teamRBs = new ArrayList<PlayerRB>();
        teamWRs = new ArrayList<PlayerWR>();
        teamKs = new ArrayList<PlayerK>();
        teamOLs = new ArrayList<PlayerOL>();
        teamF7s = new ArrayList<PlayerF7>();
        teamSs = new ArrayList<PlayerS>();
        teamCBs = new ArrayList<PlayerCB>();

        teamRSs = new ArrayList<Player>();
        teamFRs = new ArrayList<Player>();
        teamSOs = new ArrayList<Player>();
        teamJRs = new ArrayList<Player>();
        teamSRs = new ArrayList<Player>();

        gameSchedule = new ArrayList<Game>();
        gameOOCSchedule0 = null;
        gameOOCSchedule4 = null;
        gameOOCSchedule9 = null;
        gameWinsAgainst = new ArrayList<Team>();
        gameWLSchedule = new ArrayList<String>();
        confChampion = "";
        semiFinalWL = "";
        natChampWL = "";

        teamPrestige = prestige;
        recruitPlayers(2, 4, 6, 2, 10, 2, 6, 14);

        //set stats
        totalWins = 0;
        totalLosses = 0;
        winStreak = new TeamStreak(league.getYear(), league.getYear(), 0, abbr);
        yearStartWinStreak = new TeamStreak(league.getYear(), league.getYear(), 0, abbr);
        totalCCs = 0;
        totalNCs = 0;
        totalCCLosses = 0;
        totalNCLosses = 0;
        totalBowls = 0;
        totalBowlLosses = 0;
        this.name = name;
        this.abbr = abbr;
        this.conference = conference;
        rivalTeam = rivalTeamAbbr;
        wonRivalryGame = false;
        teamPoints = 0;
        teamOppPoints = 0;
        teamYards = 0;
        teamOppYards = 0;
        teamPassYards = 0;
        teamRushYards = 0;
        teamOppPassYards = 0;
        teamOppRushYards = 0;
        teamTODiff = 0;
        teamOffTalent = getOffTalent();
        teamDefTalent = getDefTalent();

        teamPollScore = teamPrestige + getOffTalent() + getDefTalent();

        teamStratOff = new TeamStrategy();
        teamStratDef = new TeamStrategy();
        //Sets CPU Strategy!
        teamStratOffNum = getCPUOffense();
        teamStratDefNum = getCPUDefense();
        numRecruits = 30;
        playersLeaving = new ArrayList<>();
    }



    /**
     * Constructor for team that is being loaded from file.
     * @param loadStr String containing the team info that can be loaded
     */
    public Team( String loadStr, League league ) {
        this.league = league;
        userControlled = false;
        showPopups = true;
        teamHistory = new ArrayList<String>();
        hallOfFame = new ArrayList<>();
        playersInjuredAll = new ArrayList<>();

        teamQBs = new ArrayList<PlayerQB>();
        teamRBs = new ArrayList<PlayerRB>();
        teamWRs = new ArrayList<PlayerWR>();
        teamKs = new ArrayList<PlayerK>();
        teamOLs = new ArrayList<PlayerOL>();
        teamF7s = new ArrayList<PlayerF7>();
        teamSs = new ArrayList<PlayerS>();
        teamCBs = new ArrayList<PlayerCB>();

        teamRSs = new ArrayList<Player>();
        teamFRs = new ArrayList<Player>();
        teamSOs = new ArrayList<Player>();
        teamJRs = new ArrayList<Player>();
        teamSRs = new ArrayList<Player>();

        gameSchedule = new ArrayList<Game>();
        gameOOCSchedule0 = null;
        gameOOCSchedule4 = null;
        gameOOCSchedule9 = null;
        gameWinsAgainst = new ArrayList<Team>();
        gameWLSchedule = new ArrayList<String>();
        confChampion = "";
        semiFinalWL = "";
        natChampWL = "";

        //set stats
        teamPoints = 0;
        teamOppPoints = 0;
        teamYards = 0;
        teamOppYards = 0;
        teamPassYards = 0;
        teamRushYards = 0;
        teamOppPassYards = 0;
        teamOppRushYards = 0;
        teamTODiff = 0;
        teamOffTalent = 0;
        teamDefTalent = 0;
        teamPollScore = 0;
        teamStratOffNum = 1; // 1 is the default strats
        teamStratDefNum = 1;
        teamTVDeal = false;
        confTVDeal = false;

        // Actually load the team from the string
        String[] lines = loadStr.split("%");

        // Lines 0 is team info
        String[] teamInfo = lines[0].split(",");
        if (teamInfo.length >= 9) {
            conference = teamInfo[0];
            name = teamInfo[1];
            abbr = teamInfo[2];
            winStreak = new TeamStreak(league.getYear(), league.getYear(), 0, abbr);
            yearStartWinStreak = new TeamStreak(league.getYear(), league.getYear(), 0, abbr);
            teamPrestige = Integer.parseInt(teamInfo[3]);
            totalWins = Integer.parseInt(teamInfo[4]);
            totalLosses = Integer.parseInt(teamInfo[5]);
            totalCCs = Integer.parseInt(teamInfo[6]);
            totalNCs = Integer.parseInt(teamInfo[7]);
            rivalTeam = teamInfo[8];
            if (teamInfo.length >= 13) {
                totalNCLosses = Integer.parseInt(teamInfo[9]);
                totalCCLosses = Integer.parseInt(teamInfo[10]);
                totalBowls = Integer.parseInt(teamInfo[11]);
                totalBowlLosses = Integer.parseInt(teamInfo[12]);
                if (teamInfo.length >= 16) {
                    teamStratOffNum = Integer.parseInt(teamInfo[13]);
                    teamStratDefNum = Integer.parseInt(teamInfo[14]);
                    showPopups = (Integer.parseInt(teamInfo[15]) == 1);
                    if (teamInfo.length >= 20) {
                        winStreak = new TeamStreak(Integer.parseInt(teamInfo[18]),
                                Integer.parseInt(teamInfo[19]),
                                Integer.parseInt(teamInfo[16]),
                                teamInfo[17]);
                        yearStartWinStreak = new TeamStreak(Integer.parseInt(teamInfo[18]),
                                Integer.parseInt(teamInfo[19]),
                                Integer.parseInt(teamInfo[16]),
                                teamInfo[17]);
                        if (teamInfo.length >= 22){
                            teamTVDeal = Boolean.parseBoolean(teamInfo[20]);
                            confTVDeal = Boolean.parseBoolean(teamInfo[21]);
                        }
                    }
                }
            } else {
                totalCCLosses = 0;
                totalNCLosses = 0;
                totalBowls = 0;
                totalBowlLosses = 0;
            }
        }

        // Lines 1 is Team Home/Away Rotation
        int startOfPlayers = 2;
        if (!lines[1].split(",")[0].equals("QB")) evenYearHomeOpp = lines[1];
        else {
            startOfPlayers = 1;
        }

        // Rest of lines are player info
        for (int i = startOfPlayers; i < lines.length; ++i) {
            recruitPlayerCSV(lines[i], false);
        }

        // Group players by class standing (FRs, SOs, etc)
        groupPlayerStandingCSV();


        wonRivalryGame = false;
        teamStratOff = getTeamStrategiesOff()[teamStratOffNum];
        teamStratDef = getTeamStrategiesDef()[teamStratDefNum];


        numRecruits = 30;
        playersLeaving = new ArrayList<>();
    }

    /**
     * Gets the OffTalent, DefTalent, poll score
     */
    public void updateTalentRatings() {
        teamOffTalent = getOffTalent();
        teamDefTalent = getDefTalent();
        teamPollScore = teamPrestige + getOffTalent() + getDefTalent();
    }

    /**
     * Advance season, hiring new coach if needed and calculating new prestige level.
     */
    public void advanceSeason() {
        // subtract for rivalry first
        int oldPrestige = teamPrestige;
        if (this != league.saveBless && this != league.saveCurse) {
            // Don't add/subtract prestige if they are a blessed/cursed team from last season
            if (wonRivalryGame && (teamPrestige - league.findTeamAbbr(rivalTeam).teamPrestige < 20)) {
                teamPrestige += 3;
            } else if (!wonRivalryGame && (league.findTeamAbbr(rivalTeam).teamPrestige - teamPrestige < 20)) {
                teamPrestige -= 3;
            }
        }

        int expectedPollFinish = 100 - teamPrestige;
        int diffExpected = expectedPollFinish - rankTeamPollScore;
        oldPrestige = teamPrestige;

        if ((teamPrestige > 50) && diffExpected > 0) {
            teamPrestige = (int) Math.pow(teamPrestige, 1 + (float) diffExpected / 1500);// + diffExpected/2500);
        }
        if ((teamPrestige > 50) && diffExpected < 0) {
            teamPrestige = (int) Math.pow(teamPrestige, 1 + (float) diffExpected / 2000);// + diffExpected/2500);
        }

        if ((teamPrestige < 50) && diffExpected > 0) {
            teamPrestige = (int) Math.pow(teamPrestige, 1 + (float) diffExpected / 1750);// + diffExpected/2500);
        }
        if ((teamPrestige < 50) && diffExpected < 0) {
            teamPrestige = (int) Math.pow(teamPrestige, 1 + (float) diffExpected / 2000);// + diffExpected/2500);
        }

        if (rankTeamPollScore == 1) {
                // NCW
                teamPrestige += 3;
        }


        if (teamPrestige > 99) teamPrestige = 99;
        if (teamPrestige < 10) teamPrestige = 10;
/*
        if (league.findTeamAbbr(rivalTeam).userControlled && league.isHardMode()) {
            // My rival is the user team, lock my prestige if it is Hard Mode
            Team rival = league.findTeamAbbr(rivalTeam);
            if (teamPrestige < rival.teamPrestige - 10) {
                teamPrestige = rival.teamPrestige - 10;
            }
        } else if (userControlled && league.isHardMode()) {
            // I am the user team, lock my rivals prestige
            Team rival = league.findTeamAbbr(rivalTeam);
            if (rival.teamPrestige < teamPrestige - 10) {
                rival.teamPrestige = teamPrestige - 10;
            }
        } */

        diffPrestige = teamPrestige - oldPrestige;

        if (userControlled) checkHallofFame();

        checkCareerRecords(league.leagueRecords);
        if (league.userTeam == this) checkCareerRecords(league.userTeamRecords);

        advanceSeasonPlayers();

        //Sets CPU Strategy!
        if(!userControlled){
            teamStratOffNum = getCPUOffense();
            teamStratDefNum = getCPUDefense();
        }

    }

    /**
     * Checks all the players leaving to see if they should be inducted to the hall of fame.
     */
    public void checkHallofFame() {
        // hofScore = gamesPlayed + 5*allConf + 15*allAmer + 50*POTY
        // Need 50 to get in
        for (Player p : playersLeaving) {
            int gms = p.gamesPlayed + p.careerGamesPlayed;
            int allConf = p.careerAllConference + (p.wonAllConference ? 1 : 0);
            int allAmer = p.careerAllAmerican + (p.wonAllAmerican ? 1 : 0);
            int poty = p.careerHeismans + (p.wonHeisman ? 1 : 0);
            if (gms/2 + 5*allConf + 15*allAmer + 50*poty > 50) {
                // HOFer
                ArrayList<String> careerStats = p.getCareerStatsList();
                StringBuilder sb = new StringBuilder();
                sb.append(p.getPosNameYrOvr_Str() + "&");
                for (String s : careerStats) {
                    sb.append(s + "&");
                }
                hallOfFame.add(sb.toString());
            }
        }
    }

    /**
     * Checks if any of the league records were broken by this team.
     */
    public void checkLeagueRecords(LeagueRecords records) {
        records.checkRecord("Team PPG", teamPoints/numGames(), abbr, league.getYear());
        records.checkRecord("Team Opp PPG", teamOppPoints/numGames(), abbr, league.getYear());
        records.checkRecord("Team YPG", teamYards/numGames(), abbr, league.getYear());
        records.checkRecord("Team Opp YPG", teamOppYards/numGames(), abbr, league.getYear());
        records.checkRecord("Team PPG", teamPoints/numGames(), abbr, league.getYear());
        records.checkRecord("Team TO Diff", teamTODiff, abbr, league.getYear());

        for (int i = 0; i < teamQBs.size(); ++i) {
            if (getQB(i).gamesPlayed > 6) {
                records.checkRecord("Pass Yards", getQB(i).statsPassYards, abbr + " " + getQB(i).getInitialName(), league.getYear());
                records.checkRecord("Pass TDs", getQB(i).statsTD, abbr + " " + getQB(i).getInitialName(), league.getYear());
                records.checkRecord("Interceptions", getQB(i).statsInt, abbr + " " + getQB(i).getInitialName(), league.getYear());
                records.checkRecord("Comp Percent", (100 * getQB(i).statsPassComp) / (getQB(i).statsPassAtt + 1), abbr + " " + getQB(i).getInitialName(), league.getYear());
            }
        }


        for (int i = 0; i < teamRBs.size(); ++i) {
            if (getRB(i).gamesPlayed > 6) {
                records.checkRecord("Rush Yards", getRB(i).statsRushYards, abbr + " " + getRB(i).getInitialName(), league.getYear());
                records.checkRecord("Rush TDs", getRB(i).statsTD, abbr + " " + getRB(i).getInitialName(), league.getYear());
                records.checkRecord("Rush Fumbles", getRB(i).statsFumbles, abbr + " " + getRB(i).getInitialName(), league.getYear());
            }
        }

        for (int i = 0; i < teamWRs.size(); ++i) {
            if (getWR(i).gamesPlayed > 6) {
                records.checkRecord("Rec Yards", getWR(i).statsRecYards, abbr + " " + getWR(i).getInitialName(), league.getYear());
                records.checkRecord("Rec TDs", getWR(i).statsTD, abbr + " " + getWR(i).getInitialName(), league.getYear());
                records.checkRecord("Catch Percent", (100 * getWR(i).statsReceptions) / (getWR(i).statsTargets + 1), abbr + " " + getWR(i).getInitialName(), league.getYear());
            }
        }

    }

    /**
     * Checks the career records for all the leaving players. Must be done after playersLeaving is populated.
     */
    public void checkCareerRecords(LeagueRecords records) {
        for (Player p : playersLeaving) {
            if (p instanceof PlayerQB) {
                PlayerQB qb = (PlayerQB) p;
                records.checkRecord("Career Pass Yards", qb.statsPassYards+qb.careerPassYards, abbr + " " + qb.getInitialName(), league.getYear()-1);
                records.checkRecord("Career Pass TDs", qb.statsTD+qb.careerTDs, abbr + " " + qb.getInitialName(), league.getYear()-1);
                records.checkRecord("Career Interceptions", qb.statsInt+qb.careerInt, abbr + " " + qb.getInitialName(), league.getYear()-1);
            }
            else if (p instanceof PlayerRB) {
                PlayerRB rb = (PlayerRB) p;
                records.checkRecord("Career Rush Yards", rb.statsRushYards+rb.careerRushYards, abbr + " " + rb.getInitialName(), league.getYear()-1);
                records.checkRecord("Career Rush TDs", rb.statsTD+rb.careerTDs, abbr + " " + rb.getInitialName(), league.getYear()-1);
                records.checkRecord("Career Rush Fumbles", rb.statsFumbles+rb.careerFumbles, abbr + " " + rb.getInitialName(), league.getYear()-1);
            }
            else if (p instanceof PlayerWR) {
                PlayerWR wr = (PlayerWR) p;
                records.checkRecord("Career Rec Yards", wr.statsRecYards+wr.careerRecYards, abbr + " " + wr.getInitialName(), league.getYear()-1);
                records.checkRecord("Career Rec TDs", wr.statsTD+wr.careerTD, abbr + " " + wr.getInitialName(), league.getYear()-1);
            }
        }
    }

    public void getPlayersLeaving() {
        if (playersLeaving.isEmpty()) {
            int i = 0;

            // Juniors more likely to leave in Hard mode and if you won NCG
            double hardBonus = 0;
            if (userControlled && league.isHardMode()) hardBonus = 0.15;
            if (natChampWL.equals("NCW")) {
                hardBonus += 0.2;
            }

            while (i < teamQBs.size()) {
                if (teamQBs.get(i).year == 4 || (teamQBs.get(i).year == 3 && teamQBs.get(i).ratOvr > NFL_OVR && Math.random() < NFL_CHANCE + hardBonus)) {
                    playersLeaving.add(teamQBs.get(i));
                }
                ++i;
            }

            i = 0;
            while (i < teamRBs.size()) {
                if (teamRBs.get(i).year == 4 || (teamRBs.get(i).year == 3 && teamRBs.get(i).ratOvr > NFL_OVR && Math.random() < NFL_CHANCE + hardBonus)) {
                    playersLeaving.add(teamRBs.get(i));
                }
                ++i;
            }

            i = 0;
            while (i < teamWRs.size()) {
                if (teamWRs.get(i).year == 4 || (teamWRs.get(i).year == 3 && teamWRs.get(i).ratOvr > NFL_OVR && Math.random() < NFL_CHANCE + hardBonus)) {
                    playersLeaving.add(teamWRs.get(i));
                }
                ++i;
            }

            i = 0;
            while (i < teamKs.size()) {
                if (teamKs.get(i).year == 4) {
                    playersLeaving.add(teamKs.get(i));
                }
                ++i;
            }

            i = 0;
            while (i < teamOLs.size()) {
                if (teamOLs.get(i).year == 4 || (teamOLs.get(i).year == 3 && teamOLs.get(i).ratOvr > NFL_OVR && Math.random() < NFL_CHANCE + hardBonus)) {
                    playersLeaving.add(teamOLs.get(i));
                }
                ++i;
            }

            i = 0;
            while (i < teamSs.size()) {
                if (teamSs.get(i).year == 4 || (teamSs.get(i).year == 3 && teamSs.get(i).ratOvr > NFL_OVR && Math.random() < NFL_CHANCE + hardBonus)) {
                    playersLeaving.add(teamSs.get(i));
                }
                ++i;
            }

            i = 0;
            while (i < teamCBs.size()) {
                if (teamCBs.get(i).year == 4 || (teamCBs.get(i).year == 3 && teamCBs.get(i).ratOvr > NFL_OVR && Math.random() < NFL_CHANCE + hardBonus)) {
                    playersLeaving.add(teamCBs.get(i));
                }
                ++i;
            }

            i = 0;
            while (i < teamF7s.size()) {
                if (teamF7s.get(i).year == 4 || (teamF7s.get(i).year == 3 && teamF7s.get(i).ratOvr > NFL_OVR && Math.random() < NFL_CHANCE + hardBonus)) {
                    playersLeaving.add(teamF7s.get(i));
                }
                ++i;
            }
        }
    }

    /**
     * Advance season for players. Removes seniors and develops underclassmen.
     */
    public void advanceSeasonPlayers() {
        int qbNeeds=0, rbNeeds=0, wrNeeds=0, kNeeds=0, olNeeds=0, sNeeds=0, cbNeeds=0, f7Needs=0;
        if (playersLeaving.isEmpty()) {
            int i = 0;
            while (i < teamQBs.size()) {
                if (teamQBs.get(i).year == 4 || (teamQBs.get(i).year == 3 && teamQBs.get(i).ratOvr > NFL_OVR && Math.random() < NFL_CHANCE)) {
                    teamQBs.remove(i);
                    qbNeeds++;
                } else {
                    teamQBs.get(i).advanceSeason();
                    i++;
                }
            }

            i = 0;
            while (i < teamRBs.size()) {
                if (teamRBs.get(i).year == 4 || (teamRBs.get(i).year == 3 && teamRBs.get(i).ratOvr > NFL_OVR && Math.random() < NFL_CHANCE)) {
                    teamRBs.remove(i);
                    rbNeeds++;
                } else {
                    teamRBs.get(i).advanceSeason();
                    i++;
                }
            }

            i = 0;
            while (i < teamWRs.size()) {
                if (teamWRs.get(i).year == 4 || (teamWRs.get(i).year == 3 && teamWRs.get(i).ratOvr > NFL_OVR && Math.random() < NFL_CHANCE)) {
                    teamWRs.remove(i);
                    wrNeeds++;
                } else {
                    teamWRs.get(i).advanceSeason();
                    i++;
                }
            }

            i = 0;
            while (i < teamKs.size()) {
                if (teamKs.get(i).year == 4) {
                    teamKs.remove(i);
                    kNeeds++;
                } else {
                    teamKs.get(i).advanceSeason();
                    i++;
                }
            }

            i = 0;
            while (i < teamOLs.size()) {
                if (teamOLs.get(i).year == 4 || (teamOLs.get(i).year == 3 && teamOLs.get(i).ratOvr > NFL_OVR && Math.random() < NFL_CHANCE)) {
                    teamOLs.remove(i);
                    olNeeds++;
                } else {
                    teamOLs.get(i).advanceSeason();
                    i++;
                }
            }

            i = 0;
            while (i < teamSs.size()) {
                if (teamSs.get(i).year == 4 || (teamSs.get(i).year == 3 && teamSs.get(i).ratOvr > NFL_OVR && Math.random() < NFL_CHANCE)) {
                    teamSs.remove(i);
                    sNeeds++;
                } else {
                    teamSs.get(i).advanceSeason();
                    i++;
                }
            }

            i = 0;
            while (i < teamCBs.size()) {
                if (teamCBs.get(i).year == 4 || (teamCBs.get(i).year == 3 && teamCBs.get(i).ratOvr > NFL_OVR && Math.random() < NFL_CHANCE)) {
                    teamCBs.remove(i);
                    cbNeeds++;
                } else {
                    teamCBs.get(i).advanceSeason();
                    i++;
                }
            }

            i = 0;
            while (i < teamF7s.size()) {
                if (teamF7s.get(i).year == 4 || (teamF7s.get(i).year == 3 && teamF7s.get(i).ratOvr > NFL_OVR && Math.random() < NFL_CHANCE)) {
                    teamF7s.remove(i);
                    f7Needs++;
                } else {
                    teamF7s.get(i).advanceSeason();
                    i++;
                }
            }

            if (!userControlled) {
                recruitPlayersFreshman(qbNeeds, rbNeeds, wrNeeds, kNeeds, olNeeds, sNeeds, cbNeeds, f7Needs);
                resetStats();
            }

        } else {
            // Just remove the players that are in playersLeaving
            int i = 0;
            while (i < teamQBs.size()) {
                if (playersLeaving.contains(teamQBs.get(i))) {
                    teamQBs.remove(i);
                    qbNeeds++;
                } else {
                    teamQBs.get(i).advanceSeason();
                    i++;
                }
            }

            i = 0;
            while (i < teamRBs.size()) {
                if (playersLeaving.contains(teamRBs.get(i))) {
                    teamRBs.remove(i);
                    rbNeeds++;
                } else {
                    teamRBs.get(i).advanceSeason();
                    i++;
                }
            }

            i = 0;
            while (i < teamWRs.size()) {
                if (playersLeaving.contains(teamWRs.get(i))) {
                    teamWRs.remove(i);
                    wrNeeds++;
                } else {
                    teamWRs.get(i).advanceSeason();
                    i++;
                }
            }

            i = 0;
            while (i < teamKs.size()) {
                if (playersLeaving.contains(teamKs.get(i))) {
                    teamKs.remove(i);
                    kNeeds++;
                } else {
                    teamKs.get(i).advanceSeason();
                    i++;
                }
            }

            i = 0;
            while (i < teamOLs.size()) {
                if (playersLeaving.contains(teamOLs.get(i))) {
                    teamOLs.remove(i);
                    olNeeds++;
                } else {
                    teamOLs.get(i).advanceSeason();
                    i++;
                }
            }

            i = 0;
            while (i < teamSs.size()) {
                if (playersLeaving.contains(teamSs.get(i))) {
                    teamSs.remove(i);
                    sNeeds++;
                } else {
                    teamSs.get(i).advanceSeason();
                    i++;
                }
            }

            i = 0;
            while (i < teamCBs.size()) {
                if (playersLeaving.contains(teamCBs.get(i))) {
                    teamCBs.remove(i);
                    cbNeeds++;
                } else {
                    teamCBs.get(i).advanceSeason();
                    i++;
                }
            }

            i = 0;
            while (i < teamF7s.size()) {
                if (playersLeaving.contains(teamF7s.get(i))) {
                    teamF7s.remove(i);
                    f7Needs++;
                } else {
                    teamF7s.get(i).advanceSeason();
                    i++;
                }
            }

            if (!userControlled) {
                recruitPlayersFreshman(qbNeeds, rbNeeds, wrNeeds, kNeeds, olNeeds, sNeeds, cbNeeds, f7Needs);
                resetStats();
            }
        }
    }

    /**
     * Recruits the needed amount of players at each position.
     * Rating of each player based on team prestige.
     * This is used when first creating a team.
     * @param qbNeeds
     * @param rbNeeds
     * @param wrNeeds
     * @param kNeeds
     * @param olNeeds
     * @param sNeeds
     * @param cbNeeds
     * @param f7Needs
     */
    public void recruitPlayers( int qbNeeds, int rbNeeds, int wrNeeds, int kNeeds,
                                int olNeeds, int sNeeds, int cbNeeds, int f7Needs ) {
        //make team
        int stars = teamPrestige/20 + 1;
        int chance = 20 - (teamPrestige - 20*( teamPrestige/20 )); //between 0 and 20

        for( int i = 0; i < qbNeeds; ++i ) {
            //make QBs
            if ( 100*Math.random() < 5*chance ) {
                teamQBs.add( new PlayerQB(league.getRandName(), (int)(4*Math.random() + 1), stars-1, this) );
            } else {
                teamQBs.add( new PlayerQB(league.getRandName(), (int)(4*Math.random() + 1), stars, this) );
            }
        }

        for( int i = 0; i < kNeeds; ++i ) {
            //make Ks
            if ( 100*Math.random() < 5*chance ) {
                teamKs.add( new PlayerK(league.getRandName(), (int)(4*Math.random() + 1), stars-1, this) );
            } else {
                teamKs.add( new PlayerK(league.getRandName(), (int)(4*Math.random() + 1), stars, this) );
            }
        }

        for( int i = 0; i < rbNeeds; ++i ) {
            //make RBs
            if ( 100*Math.random() < 5*chance ) {
                teamRBs.add( new PlayerRB(league.getRandName(), (int)(4*Math.random() + 1), stars-1, this) );
            } else {
                teamRBs.add( new PlayerRB(league.getRandName(), (int)(4*Math.random() + 1), stars, this) );
            }
        }

        for( int i = 0; i < wrNeeds; ++i ) {
            //make WRs
            if ( 100*Math.random() < 5*chance ) {
                teamWRs.add( new PlayerWR(league.getRandName(), (int)(4*Math.random() + 1), stars-1, this) );
            } else {
                teamWRs.add( new PlayerWR(league.getRandName(), (int)(4*Math.random() + 1), stars, this) );
            }
        }

        for( int i = 0; i < olNeeds; ++i ) {
            //make OLs
            if ( 100*Math.random() < 5*chance ) {
                teamOLs.add( new PlayerOL(league.getRandName(), (int)(4*Math.random() + 1), stars-1, this) );
            } else {
                teamOLs.add( new PlayerOL(league.getRandName(), (int)(4*Math.random() + 1), stars, this) );
            }
        }

        for( int i = 0; i < cbNeeds; ++i ) {
            //make CBs
            if ( 100*Math.random() < 5*chance ) {
                teamCBs.add( new PlayerCB(league.getRandName(), (int)(4*Math.random() + 1), stars-1, this) );
            } else {
                teamCBs.add( new PlayerCB(league.getRandName(), (int)(4*Math.random() + 1), stars, this) );
            }
        }

        for( int i = 0; i < f7Needs; ++i ) {
            //make F7s
            if ( 100*Math.random() < 5*chance ) {
                teamF7s.add( new PlayerF7(league.getRandName(), (int)(4*Math.random() + 1), stars-1, this) );
            } else {
                teamF7s.add( new PlayerF7(league.getRandName(), (int)(4*Math.random() + 1), stars, this) );
            }
        }

        for( int i = 0; i < sNeeds; ++i ) {
            //make Ss
            if ( 100*Math.random() < 5*chance ) {
                teamSs.add( new PlayerS(league.getRandName(), (int)(4*Math.random() + 1), stars-1, this) );
            } else {
                teamSs.add( new PlayerS(league.getRandName(), (int)(4*Math.random() + 1), stars, this) );
            }
        }

        //done making players, sort them
        sortPlayers();
    }

    /**
     * Recruit freshman at each position.
     * This is used after each season.
     * @param qbNeeds
     * @param rbNeeds
     * @param wrNeeds
     * @param kNeeds
     * @param olNeeds
     * @param sNeeds
     * @param cbNeeds
     * @param f7Needs
     */
    public void recruitPlayersFreshman( int qbNeeds, int rbNeeds, int wrNeeds, int kNeeds,
                                        int olNeeds, int sNeeds, int cbNeeds, int f7Needs ) {
        //make team
        int stars = teamPrestige/20 + 1;
        int chance = 20 - (teamPrestige - 20*( teamPrestige/20 )); //between 0 and 20

        double starsBonusChance = 0.15;
        double starsBonusDoubleChance = 0.05;

        for( int i = 0; i < qbNeeds; ++i ) {
            // Add some randomness so that players with higher stars can be recruited
            stars = teamPrestige/20 + 1;
            if ( 100*Math.random() < 5*chance ) stars = stars - 1;
            if (Math.random() < starsBonusChance) stars = stars + 1;
            else if (Math.random() < starsBonusDoubleChance) stars = stars + 2;
            if (stars > 5) stars = 5;

            //make QBs
            teamQBs.add(new PlayerQB(league.getRandName(), 1, stars, this));
        }

        for( int i = 0; i < kNeeds; ++i ) {
            // Add some randomness so that players with higher stars can be recruited
            stars = teamPrestige/20 + 1;
            if ( 100*Math.random() < 5*chance ) stars = stars - 1;
            if (Math.random() < starsBonusChance) stars = stars + 1;
            else if (Math.random() < starsBonusDoubleChance) stars = stars + 2;
            if (stars > 5) stars = 5;

            //make Ks
            teamKs.add( new PlayerK(league.getRandName(), 1, stars, this) );
        }

        for( int i = 0; i < rbNeeds; ++i ) {
            // Add some randomness so that players with higher stars can be recruited
            stars = teamPrestige/20 + 1;
            if ( 100*Math.random() < 5*chance ) stars = stars - 1;
            if (Math.random() < starsBonusChance) stars = stars + 1;
            else if (Math.random() < starsBonusDoubleChance) stars = stars + 2;
            if (stars > 5) stars = 5;

            //make RBs
            teamRBs.add( new PlayerRB(league.getRandName(), 1, stars, this) );
        }

        for( int i = 0; i < wrNeeds; ++i ) {
            // Add some randomness so that players with higher stars can be recruited
            stars = teamPrestige/20 + 1;
            if ( 100*Math.random() < 5*chance ) stars = stars - 1;
            if (Math.random() < starsBonusChance) stars = stars + 1;
            else if (Math.random() < starsBonusDoubleChance) stars = stars + 2;
            if (stars > 5) stars = 5;

            //make WRs
            teamWRs.add( new PlayerWR(league.getRandName(), 1, stars, this) );
        }

        for( int i = 0; i < olNeeds; ++i ) {
            // Add some randomness so that players with higher stars can be recruited
            stars = teamPrestige/20 + 1;
            if ( 100*Math.random() < 5*chance ) stars = stars - 1;
            if (Math.random() < starsBonusChance) stars = stars + 1;
            else if (Math.random() < starsBonusDoubleChance) stars = stars + 2;
            if (stars > 5) stars = 5;

            //make OLs
            teamOLs.add( new PlayerOL(league.getRandName(), 1, stars, this) );
        }

        for( int i = 0; i < cbNeeds; ++i ) {
            // Add some randomness so that players with higher stars can be recruited
            stars = teamPrestige/20 + 1;
            if ( 100*Math.random() < 5*chance ) stars = stars - 1;
            if (Math.random() < starsBonusChance) stars = stars + 1;
            else if (Math.random() < starsBonusDoubleChance) stars = stars + 2;
            if (stars > 5) stars = 5;

            //make CBs
            teamCBs.add( new PlayerCB(league.getRandName(), 1, stars, this) );
        }

        for( int i = 0; i < f7Needs; ++i ) {
            // Add some randomness so that players with higher stars can be recruited
            stars = teamPrestige/20 + 1;
            if ( 100*Math.random() < 5*chance ) stars = stars - 1;
            if (Math.random() < starsBonusChance) stars = stars + 1;
            else if (Math.random() < starsBonusDoubleChance) stars = stars + 2;
            if (stars > 5) stars = 5;

            //make F7s
            teamF7s.add( new PlayerF7(league.getRandName(), 1, stars, this) );
        }

        for( int i = 0; i < sNeeds; ++i ) {
            // Add some randomness so that players with higher stars can be recruited
            stars = teamPrestige/20 + 1;
            if ( 100*Math.random() < 5*chance ) stars = stars - 1;
            if (Math.random() < starsBonusChance) stars = stars + 1;
            else if (Math.random() < starsBonusDoubleChance) stars = stars + 2;
            if (stars > 5) stars = 5;

            //make Ss
            teamSs.add( new PlayerS(league.getRandName(), 1, stars, this) );
        }

        //done making players, sort them
        sortPlayers();
    }

    /**
     * Recruits walk ons at each needed position.
     * This is used by user teams if there is a dearth at any position.
     */
    public void recruitWalkOns() {
        //recruit walk ons (used for player teams who dont recruit all needs)
        int needs = 2 - teamQBs.size();
        for( int i = 0; i < needs; ++i ) {
            //make QBs
            teamQBs.add( new PlayerQB(league.getRandName(), 1, 2, this) );
        }

        needs = 4 - teamRBs.size();
        for( int i = 0; i < needs; ++i ) {
            //make RBs
            teamRBs.add( new PlayerRB(league.getRandName(), 1, 2, this) );
        }

        needs = 6 - teamWRs.size();
        for( int i = 0; i < needs; ++i ) {
            //make WRs
            teamWRs.add( new PlayerWR(league.getRandName(), 1, 2, this) );
        }

        needs = 10 - teamOLs.size();
        for( int i = 0; i < needs; ++i ) {
            //make OLs
            teamOLs.add( new PlayerOL(league.getRandName(), 1, 2, this) );
        }

        needs = 2 - teamKs.size();
        for( int i = 0; i < needs; ++i ) {
            //make Ks
            teamKs.add( new PlayerK(league.getRandName(), 1, 2, this) );
        }

        needs = 2 - teamSs.size();
        for( int i = 0; i < needs; ++i ) {
            //make Ss
            teamSs.add( new PlayerS(league.getRandName(), 1, 2, this) );
        }

        needs = 6 - teamCBs.size();
        for( int i = 0; i < needs; ++i ) {
            //make Ss
            teamCBs.add( new PlayerCB(league.getRandName(), 1, 2, this) );
        }

        needs = 14 - teamF7s.size();
        for( int i = 0; i < needs; ++i ) {
            //make Ss
            teamF7s.add( new PlayerF7(league.getRandName(), 1, 2, this) );
        }

        //done making players, sort them
        sortPlayers();
    }

    /**
     * Recruit all players given in a string
     */
    public void recruitPlayersFromStr(String playersStr) {
        String[] players = playersStr.split("%\n");
        String currLine = players[0];
        int i = 0;
        while (!currLine.equals("END_RECRUITS")) {
            recruitPlayerCSV(currLine, false);
            currLine = players[++i];
        }

        // Recruit Walk-ons before redshirts so that they don't affect position needs
        recruitWalkOns();

        currLine = players[++i]; // skip over END_RECRUITS line
        while (!currLine.equals("END_REDSHIRTS")) {
            recruitPlayerCSV(currLine, true);
            currLine = players[++i];
        }

    }

    /**
     * Recruit player given a CSV string
     * @param line player to be recruited
     * @param isRedshirt whether that player should be recruited as a RS
     */
    private void recruitPlayerCSV(String line, boolean isRedshirt) {
        String[] playerInfo = line.split(",");
        int durability;
        if (playerInfo.length >= 11) durability = Integer.parseInt(playerInfo[10]);
        else durability = (int) (50 + 50*Math.random());
        if (playerInfo[0].equals("QB")) {
            if (playerInfo.length >= 22)
                teamQBs.add( new PlayerQB(playerInfo[1], this,
                        Integer.parseInt(playerInfo[2]), Integer.parseInt(playerInfo[3]),
                        Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5]),
                        Integer.parseInt(playerInfo[6]), Integer.parseInt(playerInfo[7]), isRedshirt, durability,
                        Integer.parseInt(playerInfo[11]), Integer.parseInt(playerInfo[12]),
                        Integer.parseInt(playerInfo[13]), Integer.parseInt(playerInfo[14]),
                        Integer.parseInt(playerInfo[15]), Integer.parseInt(playerInfo[16]),
                        Integer.parseInt(playerInfo[17]), Integer.parseInt(playerInfo[18]),
                        Integer.parseInt(playerInfo[19]), Integer.parseInt(playerInfo[20]), Integer.parseInt(playerInfo[21]))
                        );
            else
                teamQBs.add( new PlayerQB(playerInfo[1], this,
                                Integer.parseInt(playerInfo[2]), Integer.parseInt(playerInfo[3]),
                                Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5]),
                                Integer.parseInt(playerInfo[6]), Integer.parseInt(playerInfo[7]), isRedshirt, durability)
                );
        } else if (playerInfo[0].equals("RB")) {
            if (playerInfo.length >= 20)
                teamRBs.add( new PlayerRB(playerInfo[1], this,
                        Integer.parseInt(playerInfo[2]), Integer.parseInt(playerInfo[3]),
                        Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5]),
                        Integer.parseInt(playerInfo[6]), Integer.parseInt(playerInfo[7]), isRedshirt, durability,
                        Integer.parseInt(playerInfo[11]), Integer.parseInt(playerInfo[12]),
                        Integer.parseInt(playerInfo[13]), Integer.parseInt(playerInfo[14]),
                        Integer.parseInt(playerInfo[15]), Integer.parseInt(playerInfo[16]),
                        Integer.parseInt(playerInfo[17]), Integer.parseInt(playerInfo[18]), Integer.parseInt(playerInfo[19])));
            else
                teamRBs.add( new PlayerRB(playerInfo[1], this,
                        Integer.parseInt(playerInfo[2]), Integer.parseInt(playerInfo[3]),
                        Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5]),
                        Integer.parseInt(playerInfo[6]), Integer.parseInt(playerInfo[7]), isRedshirt, durability));
        } else if (playerInfo[0].equals("WR")) {
            if (playerInfo.length >= 22)
                teamWRs.add( new PlayerWR(playerInfo[1], this,
                        Integer.parseInt(playerInfo[2]), Integer.parseInt(playerInfo[3]),
                        Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5]),
                        Integer.parseInt(playerInfo[6]), Integer.parseInt(playerInfo[7]), isRedshirt, durability,
                        Integer.parseInt(playerInfo[11]), Integer.parseInt(playerInfo[12]),
                        Integer.parseInt(playerInfo[13]), Integer.parseInt(playerInfo[14]),
                        Integer.parseInt(playerInfo[15]), Integer.parseInt(playerInfo[16]),
                        Integer.parseInt(playerInfo[17]), Integer.parseInt(playerInfo[18]),
                        Integer.parseInt(playerInfo[19]), Integer.parseInt(playerInfo[20]), Integer.parseInt(playerInfo[21]))
                );
            else
                teamWRs.add( new PlayerWR(playerInfo[1], this,
                        Integer.parseInt(playerInfo[2]), Integer.parseInt(playerInfo[3]),
                        Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5]),
                        Integer.parseInt(playerInfo[6]), Integer.parseInt(playerInfo[7]), isRedshirt, durability)
                );
        } else if (playerInfo[0].equals("OL")) {
            if (playerInfo.length >= 16)
                teamOLs.add( new PlayerOL(playerInfo[1], this,
                        Integer.parseInt(playerInfo[2]), Integer.parseInt(playerInfo[3]),
                        Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5]),
                        Integer.parseInt(playerInfo[6]), Integer.parseInt(playerInfo[7]), isRedshirt, durability,
                        Integer.parseInt(playerInfo[11]), Integer.parseInt(playerInfo[12]),
                        Integer.parseInt(playerInfo[13]), Integer.parseInt(playerInfo[14]), Integer.parseInt(playerInfo[15])));
            else
                teamOLs.add( new PlayerOL(playerInfo[1], this,
                        Integer.parseInt(playerInfo[2]), Integer.parseInt(playerInfo[3]),
                        Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5]),
                        Integer.parseInt(playerInfo[6]), Integer.parseInt(playerInfo[7]), isRedshirt, durability));
        } else if (playerInfo[0].equals("K")) {
            if (playerInfo.length >= 20)
                teamKs.add( new PlayerK(playerInfo[1], this,
                        Integer.parseInt(playerInfo[2]), Integer.parseInt(playerInfo[3]),
                        Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5]),
                        Integer.parseInt(playerInfo[6]), Integer.parseInt(playerInfo[7]), isRedshirt, durability,
                        Integer.parseInt(playerInfo[11]), Integer.parseInt(playerInfo[12]),
                        Integer.parseInt(playerInfo[13]), Integer.parseInt(playerInfo[14]),
                        Integer.parseInt(playerInfo[15]), Integer.parseInt(playerInfo[16]),
                        Integer.parseInt(playerInfo[17]), Integer.parseInt(playerInfo[18]), Integer.parseInt(playerInfo[19])));
            else
                teamKs.add( new PlayerK(playerInfo[1], this,
                        Integer.parseInt(playerInfo[2]), Integer.parseInt(playerInfo[3]),
                        Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5]),
                        Integer.parseInt(playerInfo[6]), Integer.parseInt(playerInfo[7]), isRedshirt, durability));
        } else if (playerInfo[0].equals("S")) {
            if (playerInfo.length >= 16)
                teamSs.add( new PlayerS(playerInfo[1], this,
                        Integer.parseInt(playerInfo[2]), Integer.parseInt(playerInfo[3]),
                        Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5]),
                        Integer.parseInt(playerInfo[6]), Integer.parseInt(playerInfo[7]), isRedshirt, durability,
                        Integer.parseInt(playerInfo[11]), Integer.parseInt(playerInfo[12]),
                        Integer.parseInt(playerInfo[13]), Integer.parseInt(playerInfo[14]), Integer.parseInt(playerInfo[15])));
            else
                teamSs.add( new PlayerS(playerInfo[1], this,
                        Integer.parseInt(playerInfo[2]), Integer.parseInt(playerInfo[3]),
                        Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5]),
                        Integer.parseInt(playerInfo[6]), Integer.parseInt(playerInfo[7]), isRedshirt, durability));
        } else if (playerInfo[0].equals("CB")) {
            if (playerInfo.length >= 16)
                teamCBs.add( new PlayerCB(playerInfo[1], this,
                        Integer.parseInt(playerInfo[2]), Integer.parseInt(playerInfo[3]),
                        Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5]),
                        Integer.parseInt(playerInfo[6]), Integer.parseInt(playerInfo[7]), isRedshirt, durability,
                        Integer.parseInt(playerInfo[11]), Integer.parseInt(playerInfo[12]),
                        Integer.parseInt(playerInfo[13]), Integer.parseInt(playerInfo[14]), Integer.parseInt(playerInfo[15])));
            else
                teamCBs.add( new PlayerCB(playerInfo[1], this,
                        Integer.parseInt(playerInfo[2]), Integer.parseInt(playerInfo[3]),
                        Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5]),
                        Integer.parseInt(playerInfo[6]), Integer.parseInt(playerInfo[7]), isRedshirt, durability));
        } else if (playerInfo[0].equals("F7")) {
            if (playerInfo.length >= 16)
                teamF7s.add( new PlayerF7(playerInfo[1], this,
                        Integer.parseInt(playerInfo[2]), Integer.parseInt(playerInfo[3]),
                        Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5]),
                        Integer.parseInt(playerInfo[6]), Integer.parseInt(playerInfo[7]), isRedshirt, durability,
                        Integer.parseInt(playerInfo[11]), Integer.parseInt(playerInfo[12]),
                        Integer.parseInt(playerInfo[13]), Integer.parseInt(playerInfo[14]), Integer.parseInt(playerInfo[15])));
            else
                teamF7s.add( new PlayerF7(playerInfo[1], this,
                        Integer.parseInt(playerInfo[2]), Integer.parseInt(playerInfo[3]),
                        Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5]),
                        Integer.parseInt(playerInfo[6]), Integer.parseInt(playerInfo[7]), isRedshirt, durability));
        }
    }

    /**
     * For news stories or other info gathering, setup player groups by student standing
     * Run through each type of player, add them to the appropriate year
     */
    private void groupPlayerStandingCSV() {
        for (PlayerQB p : teamQBs){
            if (p.year == 0) teamRSs.add(p);
            else if (p.year == 1) teamFRs.add(p);
            else if (p.year == 2) teamSOs.add(p);
            else if (p.year == 3) teamJRs.add(p);
            else if (p.year == 4) teamSRs.add(p);
        }
        for (PlayerRB p : teamRBs){
            if (p.year == 0) teamRSs.add(p);
            else if (p.year == 1) teamFRs.add(p);
            else if (p.year == 2) teamSOs.add(p);
            else if (p.year == 3) teamJRs.add(p);
            else if (p.year == 4) teamSRs.add(p);
        }
        for (PlayerWR p : teamWRs){
            if (p.year == 0) teamRSs.add(p);
            else if (p.year == 1) teamFRs.add(p);
            else if (p.year == 2) teamSOs.add(p);
            else if (p.year == 3) teamJRs.add(p);
            else if (p.year == 4) teamSRs.add(p);
        }
        for (PlayerK p : teamKs){
            if (p.year == 0) teamRSs.add(p);
            else if (p.year == 1) teamFRs.add(p);
            else if (p.year == 2) teamSOs.add(p);
            else if (p.year == 3) teamJRs.add(p);
            else if (p.year == 4) teamSRs.add(p);
        }
        for (PlayerOL p : teamOLs){
            if (p.year == 0) teamRSs.add(p);
            else if (p.year == 1) teamFRs.add(p);
            else if (p.year == 2) teamSOs.add(p);
            else if (p.year == 3) teamJRs.add(p);
            else if (p.year == 4) teamSRs.add(p);
        }
        for (PlayerS p : teamSs){
            if (p.year == 0) teamRSs.add(p);
            else if (p.year == 1) teamFRs.add(p);
            else if (p.year == 2) teamSOs.add(p);
            else if (p.year == 3) teamJRs.add(p);
            else if (p.year == 4) teamSRs.add(p);
        }
        for (PlayerCB p : teamCBs){
            if (p.year == 0) teamRSs.add(p);
            else if (p.year == 1) teamFRs.add(p);
            else if (p.year == 2) teamSOs.add(p);
            else if (p.year == 3) teamJRs.add(p);
            else if (p.year == 4) teamSRs.add(p);
        }
        for (PlayerF7 p : teamF7s){
            if (p.year == 0) teamRSs.add(p);
            else if (p.year == 1) teamFRs.add(p);
            else if (p.year == 2) teamSOs.add(p);
            else if (p.year == 3) teamJRs.add(p);
            else if (p.year == 4) teamSRs.add(p);
        }
    }

    /**
     * Resets all team stats to 0.
     */
    public void resetStats() {
        //reset stats
        gameSchedule = new ArrayList<Game>();
        gameOOCSchedule0 = null;
        gameOOCSchedule4 = null;
        gameOOCSchedule9 = null;
        gameWinsAgainst = new ArrayList<Team>();
        gameWLSchedule = new ArrayList<String>();
        confChampion = "";
        semiFinalWL = "";
        natChampWL = "";
        wins = 0;
        losses = 0;

        //set stats
        teamPoints = 0;
        teamOppPoints = 0;
        teamYards = 0;
        teamOppYards = 0;
        teamPassYards = 0;
        teamRushYards = 0;
        teamOppPassYards = 0;
        teamOppRushYards = 0;
        teamTODiff = 0;

    }

    /**
     * Updates poll score based on team stats.
     */
    public void updatePollScore() {
        updateStrengthOfWins();
        int preseasonBias = 7 - (wins + losses); // change wins + losses to -
        if (preseasonBias < 0) preseasonBias = 0;
        teamPollScore = (wins*200 + 3*(teamPoints-teamOppPoints) +
                (teamYards-teamOppYards)/40 +
                3*(preseasonBias)*(teamPrestige + getOffTalent() + getDefTalent()) +
                teamStrengthOfWins)/8; // change 10 to 7 on SOW
        if ( "CC".equals(confChampion) ) {
            //bonus for winning conference
            teamPollScore += 40;
        }
        if ( "NCW".equals(natChampWL) ) {
            //bonus for winning champ game
            teamPollScore += 100;
        }
        if ( losses == 0 ) {
            teamPollScore += 30;
        } else if ( losses == 1 ) {
            teamPollScore += 15;
        }

        teamOffTalent = getOffTalent();
        teamDefTalent = getDefTalent();
    }

    /**
     * Updates team history.
     */
    public void updateTeamHistory() {
        String histYear = league.getYear() + ": #" + rankTeamPollScore + " " + name + " (" + wins + "-" + losses + ") "
                + confChampion + " " + semiFinalWL + natChampWL;

        for (int i = 12; i < gameSchedule.size(); ++i) {
            Game g = gameSchedule.get(i);
            histYear += ">" + g.gameName + ": ";
            String[] gameSum = getGameSummaryStr(i);
            histYear += gameSum[1] + " " + gameSum[2];
        }

        teamHistory.add(histYear);
    }

    /**
     * Gets the team history as a String array
     * @return team history
     */
    public String[] getTeamHistoryList() {
        String[] hist = new String[teamHistory.size()+5];
        hist[0] = "Overall W-L: " + totalWins + "-" + totalLosses;
        hist[1] = "Conf Champ Record: " + totalCCs + "-" + totalCCLosses;
        hist[2] = "Bowl Game Record: " + totalBowls + "-" + totalBowlLosses;
        hist[3] = "National Champ Record: " + totalNCs + "-" + totalNCLosses;
        hist[4] = " ";
        for (int i = 0; i < teamHistory.size(); ++i) {
            hist[i+5] = teamHistory.get(i);
        }
        return hist;
    }

    /**
     * Gets a string of the entire team history
     */
    public String getTeamHistoryStr() {
        String hist = "";
        hist += "Overall W-L: " + totalWins + "-" + totalLosses + "\n";
        hist += "Conf Champ Record: " + totalCCs + "-" + totalCCLosses + "\n";
        hist += "Bowl Game Record: " + totalBowls + "-" + totalBowlLosses + "\n";
        hist += "National Champ Record: " + totalNCs + "-" + totalNCLosses + "\n";
        hist += "\nYear by year summary:\n";
        for (int i = 0; i < teamHistory.size(); ++i) {
            hist += teamHistory.get(i) + "\n";
        }
        return hist;
    }

    /**
     * Updates strength of wins based on how opponents have fared.
     */
    public void updateStrengthOfWins() {
        int strWins = 0;
        for ( int i = 0; i < 12; ++i ) {
            Game g = gameSchedule.get(i);
            if (g.homeTeam == this) {
                strWins += Math.pow(100 - g.awayTeam.rankTeamPollScore,2); //Changed 60 to 100
            } else {
                strWins += Math.pow(100 - g.homeTeam.rankTeamPollScore,2); //changed 60 to 100
            }
        }
        teamStrengthOfWins = strWins/50;
        for (Team t : gameWinsAgainst) {
            teamStrengthOfWins += Math.pow(t.wins,2);
        }
    }

    /**
     * Sorts players so that best players are higher in depth chart.
     */
    public void sortPlayers() {
        //sort players based on overall ratings to assemble best starting lineup
        Collections.sort(teamQBs, new PlayerComparator());
        Collections.sort(teamRBs, new PlayerComparator());
        Collections.sort(teamWRs, new PlayerComparator());
        Collections.sort(teamKs, new PlayerComparator());
        Collections.sort(teamOLs, new PlayerComparator());
        Collections.sort(teamCBs, new PlayerComparator());
        Collections.sort(teamSs, new PlayerComparator());
        Collections.sort(teamF7s, new PlayerComparator());

        Collections.sort(teamRSs, new PlayerComparator());
        Collections.sort(teamFRs, new PlayerComparator());
        Collections.sort(teamSOs, new PlayerComparator());
        Collections.sort(teamJRs, new PlayerComparator());
        Collections.sort(teamSRs, new PlayerComparator());
    }

    /**
     * May injure players.
     * Guaranteed not to injure more than the amount of starters for each position.
     */
    public void checkForInjury() {
        playersInjured = new ArrayList<>();
        playersRecovered = new ArrayList<>();
        if (league.isHardMode()) {
            checkInjuryPosition(teamQBs, 1);
            checkInjuryPosition(teamRBs, 2);
            checkInjuryPosition(teamWRs, 3);
            checkInjuryPosition(teamOLs, 5);
            checkInjuryPosition(teamKs, 1);
            checkInjuryPosition(teamSs, 1);
            checkInjuryPosition(teamCBs, 3);
            checkInjuryPosition(teamF7s, 1);
        }
    }

    private void checkInjuryPosition(ArrayList<? extends Player> players, int numStarters) {
        int numInjured = 0;

        for (Player p : players) {
            if (p.injury != null) {
                p.injury.advanceGame();
                numInjured++;
                if (p.injury == null) {
                    playersRecovered.add(p);
                    playersInjuredAll.remove(p);
                }
            }
        }

        // Only injure if there are people left to injure
        if (numInjured < numStarters) {
            for (int i = 0; i < numStarters; ++i) {
                Player p = players.get(i);
                if (Math.random() < Math.pow(1 - (double)p.ratDur/100, 3) && numInjured < numStarters) {
                    // injury!
                    p.injury = new Injury(p);
                    playersInjured.add(p);
                    playersInjuredAll.add(p);
                    numInjured++;
                }
            }
        }

        if (numInjured > 0) Collections.sort(players, new PlayerComparator());
    }

    /**
     * Gets a list of all the players that were injured that week.
     * @return list of players in string
     */
    public String[] getInjuryReport() {
        if (playersInjured.size() > 0 || playersRecovered.size() > 0) {
            String[] injuries;

            if (playersRecovered.size() > 0) injuries = new String[playersInjured.size() + playersRecovered.size() + 1];
            else injuries = new String[playersInjured.size()];

            for (int i = 0; i < playersInjured.size(); ++i) {
                injuries[i] = playersInjured.get(i).getPosNameYrOvrPot_Str();
            }

            if (playersRecovered.size() > 0) {
                injuries[ playersInjured.size() ] = "Players Recovered from Injuries:> ";
                for (int i = 0; i < playersRecovered.size(); ++i) {
                    injuries[ playersInjured.size() + i + 1 ] = playersRecovered.get(i).getPosNameYrOvrPot_Str();
                }
            }

            return injuries;
        }
        else return null;
    }

    /**
     * Get rid of all injuries
     */
    public void curePlayers() {
        curePlayersPosition(teamQBs);
        curePlayersPosition(teamRBs);
        curePlayersPosition(teamWRs);
        curePlayersPosition(teamOLs);
        curePlayersPosition(teamKs);
        curePlayersPosition(teamSs);
        curePlayersPosition(teamCBs);
        curePlayersPosition(teamF7s);
        sortPlayers();
    }

    private void curePlayersPosition(ArrayList<? extends Player> players) {
        for (Player p : players) {
            p.injury = null;
            p.isInjured = false;
        }
    }

    /**
     * Calculates offensive talent level of team.
     * @return Offensive Talent Level
     */
    public int getOffTalent() {
        return ( getQB(0).ratOvr*5 +
                teamWRs.get(0).ratOvr + teamWRs.get(1).ratOvr + teamWRs.get(2).ratOvr +
                teamRBs.get(0).ratOvr + teamRBs.get(1).ratOvr +
                getCompositeOLPass() + getCompositeOLRush() ) / 12;
    }

    /**
     * Calculates defensive talent level of team.
     * @return Defensive Talent Level
     */
    public int getDefTalent() {
        return ( getRushDef() + getPassDef() ) / 2;
    }

    /**
     * Get the composite Football IQ of the team. Is used in game simulation.
     * @return football iq of the team
     */
    public int getCompositeFootIQ() {
        int comp = 0;
        comp += getQB(0).ratFootIQ * 5;
        comp += getRB(0).ratFootIQ + getRB(1).ratFootIQ;
        comp += getWR(0).ratFootIQ + getWR(1).ratFootIQ + getWR(2).ratFootIQ;
        for (int i = 0; i < 5; ++i) {
            comp += getOL(i).ratFootIQ/5;
        }
        comp += getS(0).ratFootIQ * 5;
        comp += getCB(0).ratFootIQ + getCB(1).ratFootIQ + getCB(2).ratFootIQ;
        for (int i = 0; i < 7; ++i) {
            comp += getF7(i).ratFootIQ/7;
        }
        return comp / 20;
    }

    /**
     * Gets the recruiting class strength.
     * Adds up all the ovrs of freshman
     * @return class strength as a number
     */
    public int getRecruitingClassRat() {
        int classStrength = 0;
        int numFreshman = 0;
        ArrayList<Player> allPlayers = getAllPlayers();
        for (Player p : allPlayers) {
            if (p.year == 1 && p.ratOvr > 65) {
                // Is freshman
                classStrength += p.ratOvr - 30;
                numFreshman++;
            }
        }

        if (numFreshman > 0)
            return classStrength * (classStrength/numFreshman) / 100;
        else return 0;
    }

    public ArrayList<Player> getAllPlayers() {
        ArrayList<Player> allPlayersList = new ArrayList<>();
        allPlayersList.addAll(teamQBs);
        allPlayersList.addAll(teamRBs);
        allPlayersList.addAll(teamWRs);
        allPlayersList.addAll(teamOLs);
        allPlayersList.addAll(teamKs);
        allPlayersList.addAll(teamSs);
        allPlayersList.addAll(teamCBs);
        allPlayersList.addAll(teamF7s);
        return allPlayersList;
    }

    public PlayerQB getQB(int depth) {
        if ( depth < teamQBs.size() && depth >= 0 ) {
            return teamQBs.get(depth);
        } else {
            return teamQBs.get(0);
        }
    }

    public PlayerRB getRB(int depth) {
        if ( depth < teamRBs.size() && depth >= 0 ) {
            return teamRBs.get(depth);
        } else {
            return teamRBs.get(0);
        }
    }

    public PlayerWR getWR(int depth) {
        if ( depth < teamWRs.size() && depth >= 0 ) {
            return teamWRs.get(depth);
        } else {
            return teamWRs.get(0);
        }
    }

    public PlayerK getK(int depth) {
        if ( depth < teamKs.size() && depth >= 0 ) {
            return teamKs.get(depth);
        } else {
            return teamKs.get(0);
        }
    }

    public PlayerOL getOL(int depth) {
        if ( depth < teamOLs.size() && depth >= 0 ) {
            return teamOLs.get(depth);
        } else {
            return teamOLs.get(0);
        }
    }

    public PlayerS getS(int depth) {
        if ( depth < teamSs.size() && depth >= 0 ) {
            return teamSs.get(depth);
        } else {
            return teamSs.get(0);
        }
    }

    public PlayerCB getCB(int depth) {
        if ( depth < teamCBs.size() && depth >= 0 ) {
            return teamCBs.get(depth);
        } else {
            return teamCBs.get(0);
        }
    }

    public PlayerF7 getF7(int depth) {
        if ( depth < teamF7s.size() && depth >= 0 ) {
            return teamF7s.get(depth);
        } else {
            return teamF7s.get(0);
        }
    }

    /**
     * Get pass proficiency. The higher the more likely the team is to pass.
     * @return integer of how good the team is at passing
     */
    public int getPassProf() {
        int avgWRs = ( teamWRs.get(0).ratOvr + teamWRs.get(1).ratOvr + teamWRs.get(2).ratOvr)/3;
        return (getCompositeOLPass() + getQB(0).ratOvr*2 + avgWRs)/4;
    }

    /**
     * Get run proficiency. The higher the more likely the team is to run.
     * @return integer of how good the team is at rushing
     */
    public int getRushProf() {
        int avgRBs = ( teamRBs.get(0).ratOvr + teamRBs.get(1).ratOvr )/2;
        return (getCompositeOLRush() + avgRBs )/2;
    }

    /**
     * Get how good the team is at defending the pass
     * @return integer of how good
     */
    public int getPassDef() {
        int avgCBs = ( teamCBs.get(0).ratOvr + teamCBs.get(1).ratOvr + teamCBs.get(2).ratOvr)/3;
        return (avgCBs*3 + teamSs.get(0).ratOvr + getCompositeF7Pass()*2)/6;
    }

    /**
     * Get how good the team is at defending the rush
     * @return integer of how good
     */
    public int getRushDef() {
        return getCompositeF7Rush();
    }

    /**
     * Get how good the OL is at defending the pass
     * Is the average of power and pass blocking.
     * @return how good they are at blocking the pass.
     */
    public int getCompositeOLPass() {
        int compositeOL = 0;
        for ( int i = 0; i < 5; ++i ) {
            compositeOL += (teamOLs.get(i).ratOLPow + teamOLs.get(i).ratOLBkP)/2;
        }
        return compositeOL / 5;
    }

    /**
     * Get how good the OL is at defending the rush
     * Is the average of power and rush blocking.
     * @return how good they are at blocking the rush.
     */
    public int getCompositeOLRush() {
        int compositeOL = 0;
        for ( int i = 0; i < 5; ++i ) {
            compositeOL += (teamOLs.get(i).ratOLPow + teamOLs.get(i).ratOLBkR)/2;
        }
        return compositeOL / 5;
    }

    /**
     * Get how good the F7 is at defending the pass.
     * Is the average of power and pass pressure.
     * @return how good they are at putting pressure on passer.
     */
    public int getCompositeF7Pass() {
        int compositeF7 = 0;
        for ( int i = 0; i < 7; ++i ) {
            compositeF7 += (teamF7s.get(i).ratF7Pow + teamF7s.get(i).ratF7Pas)/2;
        }
        return compositeF7 / 7;
    }

    /**
     * Get how good the F7 is at defending the run.
     * Is the average of power and run stopping.
     * @return how good they are at stopping the RB.
     */
    public int getCompositeF7Rush() {
        int compositeF7 = 0;
        for ( int i = 0; i < 7; ++i ) {
            compositeF7 += (teamF7s.get(i).ratF7Pow + teamF7s.get(i).ratF7Rsh)/2;
        }
        return compositeF7 / 7;
    }

    /**
     * Get comma separated value of the team stats and their rankings.
     * @return String of CSV stat,name,ranking
     */
    public String getTeamStatsStrCSV() {
        StringBuilder ts0 = new StringBuilder();

        ArrayList<Team> confTeams = new ArrayList<>();
        for (Conference c : league.conferences) {
            if (c.confName.equals(conference)) {
                confTeams.addAll(c.confTeams);
                Collections.sort(confTeams, new TeamCompConfWins());
                int confRank = 11;
                for (int i = 0; i < confTeams.size(); ++i) {
                    if (confTeams.get(i).equals(this)) {
                        confRank = i+1;
                        break;
                    }
                }
                ts0.append(getConfWins()+"-"+getConfLosses() + ",");
                ts0.append("Conf W-L" + ",");
                ts0.append(getRankStr(confRank) + "%\n");
            }
        }

        ts0.append(teamPollScore + ",");
        ts0.append("AP Votes" + ",");
        ts0.append(getRankStr(rankTeamPollScore) + "%\n");

        ts0.append(teamStrengthOfWins + ",");
        ts0.append("SOS" + ",");
        ts0.append(getRankStr(rankTeamStrengthOfWins) + "%\n");

        ts0.append(teamPoints/numGames() + ",");
        ts0.append("Points" + ",");
        ts0.append(getRankStr(rankTeamPoints) + "%\n");

        ts0.append(teamOppPoints/numGames() + ",");
        ts0.append("Opp Points" + ",");
        ts0.append(getRankStr(rankTeamOppPoints) + "%\n");

        ts0.append(teamYards/numGames() + ",");
        ts0.append("Yards" + ",");
        ts0.append(getRankStr(rankTeamYards) + "%\n");

        ts0.append(teamOppYards/numGames() + ",");
        ts0.append("Opp Yards" + ",");
        ts0.append(getRankStr(rankTeamOppYards) + "%\n");

        ts0.append(teamPassYards/numGames() + ",");
        ts0.append("Pass Yards" + ",");
        ts0.append(getRankStr(rankTeamPassYards) + "%\n");

        ts0.append(teamRushYards/numGames() + ",");
        ts0.append("Rush Yards" + ",");
        ts0.append(getRankStr(rankTeamRushYards) + "%\n");

        ts0.append(teamOppPassYards/numGames() + ",");
        ts0.append("Opp Pass YPG" + ",");
        ts0.append(getRankStr(rankTeamOppPassYards) + "%\n");

        ts0.append(teamOppRushYards/numGames() + ",");
        ts0.append("Opp Rush YPG" + ",");
        ts0.append(getRankStr(rankTeamOppRushYards) + "%\n");

        if (teamTODiff > 0) ts0.append("+" + teamTODiff + ",");
        else ts0.append(teamTODiff + ",");
        ts0.append("TO Diff" + ",");
        ts0.append(getRankStr(rankTeamTODiff) + "%\n");

        ts0.append(teamOffTalent + ",");
        ts0.append("Off Talent" + ",");
        ts0.append(getRankStr(rankTeamOffTalent) + "%\n");

        ts0.append(teamDefTalent + ",");
        ts0.append("Def Talent" + ",");
        ts0.append(getRankStr(rankTeamDefTalent) + "%\n");

        ts0.append(teamPrestige + ",");
        ts0.append("Prestige" + ",");
        ts0.append(getRankStr(rankTeamPrestige) + "%\n");

        ts0.append(getRecruitingClassRat() + ",");
        ts0.append("Recruit Class" + ",");
        ts0.append(getRankStr(rankTeamRecruitClass) + "%\n");

        return ts0.toString();
    }

    /**
     * Get the game summary of a played game.
     * [gameName, score summary, who they played]
     * @param gameNumber number of the game desired
     * @return array of name, score, who was played
     */
    public String[] getGameSummaryStr(int gameNumber) {
        String[] gs = new String[3];
        Game g = gameSchedule.get(gameNumber);
        gs[0] = g.gameName;
        if (gameNumber < gameWLSchedule.size()) {
            gs[1] = gameWLSchedule.get(gameNumber) + " " + gameSummaryStrScore(g);
            if (g.numOT > 0) gs[1] += " (" + g.numOT + "OT)";
        } else {
            gs[1] = "---";
        }
        gs[2] = gameSummaryStrOpponent(g);
        return gs;
    }

    /**
     * Get a summary of your team's season.
     * Tells how they finished, if they beat/fell short of expecations, and if they won rivalry game.
     * @return String of season summary
     */
    public String seasonSummaryStr() {
        String summary = "Your team, " + name + ", finished the season ranked #" + rankTeamPollScore + " with " + wins + " wins and " + losses + " losses.";
        int expectedPollFinish = 100 - teamPrestige;
        int diffExpected = expectedPollFinish - rankTeamPollScore;
        int oldPrestige = teamPrestige;
        int newPrestige = oldPrestige;
        if ( teamPrestige > 45 || diffExpected > 0 ) {
            newPrestige = (int)Math.pow(teamPrestige, 1 + (float)diffExpected/1500);// + diffExpected/2500);
        }

        if ( natChampWL.equals("NCW") ) {
            summary += "\n\nYou won the National Championship! Recruits want to play for winners and you have proved that you are one. You gain +3 prestige!";
        }

        if ((newPrestige - oldPrestige) > 0) {
            summary += "\n\nGreat job coach! You exceeded expectations and gained " + (newPrestige - oldPrestige) + " prestige! This will help your recruiting.";
        } else if ((newPrestige - oldPrestige) < 0) {
            summary += "\n\nA bit of a down year, coach? You fell short expectations and lost " + (oldPrestige - newPrestige) + " prestige. This will hurt your recruiting.";
        } else {
            summary += "\n\nWell, your team performed exactly how many expected. This won't hurt or help recruiting, but try to improve next year!";
        }

        if ( wonRivalryGame && (teamPrestige - league.findTeamAbbr(rivalTeam).teamPrestige < 20) ) {
            summary += "\n\nFuture recruits were impressed that you won your rivalry game. You gained 2 prestige.";
        } else if (!wonRivalryGame && (league.findTeamAbbr(rivalTeam).teamPrestige - teamPrestige < 20 || name.equals("American Samoa"))) {
            summary += "\n\nSince you couldn't win your rivalry game, recruits aren't excited to attend your school. You lost 2 prestige.";
        } else if ( wonRivalryGame ) {
            summary += "\n\nGood job winning your rivalry game, but it was expected given the state of their program. You gain no prestige for this.";
        } else {
            summary += "\n\nYou lost your rivalry game, but this was expected given your rebuilding program. You lost no prestige for this.";
        }

        return summary;
    }

    /**
     * Gets player name or detail strings for displaying in the roster tab via expandable list.
     * Should be separated by a '>' from left text and right text.
     * @return list of players with their name,ovr,por,etc
     */
    public List<String> getPlayerStatsExpandListStr() {
        ArrayList<String> pList = new ArrayList<String>();

        pList.add(getQB(0).getPosNameYrOvrPot_Str());

        for (int i = 0; i < 2; ++i) {
            pList.add(getRB(i).getPosNameYrOvrPot_Str());
        }

        for (int i = 0; i < 3; ++i) {
            pList.add(getWR(i).getPosNameYrOvrPot_Str());
        }

        for (int i = 0; i < 5; ++i) {
            pList.add(getOL(i).getPosNameYrOvrPot_Str());
        }

        pList.add(getK(0).getPosNameYrOvrPot_Str());

        pList.add(getS(0).getPosNameYrOvrPot_Str());

        for (int i = 0; i < 3; ++i) {
            pList.add(getCB(i).getPosNameYrOvrPot_Str());
        }

        for (int i = 0; i < 7; ++i) {
            pList.add(getF7(i).getPosNameYrOvrPot_Str());
        }

        pList.add("BENCH > BENCH");

        return pList;
    }

    /**
     * Creates the map needed for making the expandable list view used in the player stats.
     * @param playerStatsGroupHeaders list of players by name,overall,pot,etc
     * @return mapping of each player to their detail ratings
     */
    public Map<String, List<String>> getPlayerStatsExpandListMap(List<String> playerStatsGroupHeaders) {
        Map<String, List<String>> playerStatsMap = new LinkedHashMap<String, List<String>>();

        String ph; //player header
        ArrayList<String> pInfoList;

        //QB
        ph = playerStatsGroupHeaders.get(0);
        playerStatsMap.put(ph, getQB(0).getDetailStatsList(numGames()));

        for (int i = 1; i < 3; ++i) {
            ph = playerStatsGroupHeaders.get(i);
            playerStatsMap.put(ph, getRB(i - 1).getDetailStatsList(numGames()));
        }

        for (int i = 3; i < 6; ++i) {
            ph = playerStatsGroupHeaders.get(i);
            playerStatsMap.put(ph, getWR(i - 3).getDetailStatsList(numGames()));
        }

        for (int i = 6; i < 11; ++i) {
            ph = playerStatsGroupHeaders.get(i);
            playerStatsMap.put(ph, getOL(i - 6).getDetailStatsList(numGames()));
        }

        ph = playerStatsGroupHeaders.get(11);
        playerStatsMap.put(ph, getK(0).getDetailStatsList(numGames()));

        ph = playerStatsGroupHeaders.get(12);
        playerStatsMap.put(ph, getS(0).getDetailStatsList(numGames()));

        for (int i = 13; i < 16; ++i) {
            ph = playerStatsGroupHeaders.get(i);
            playerStatsMap.put(ph, getCB(i - 13).getDetailStatsList(numGames()));
        }

        for (int i = 16; i < 23; ++i) {
            ph = playerStatsGroupHeaders.get(i);
            playerStatsMap.put(ph, getF7(i - 16).getDetailStatsList(numGames()));
        }

        //Bench
        ph = playerStatsGroupHeaders.get(23);
        ArrayList<String> benchStr = new ArrayList<>();
        for ( int i = 1; i < teamQBs.size(); ++i) {
            benchStr.add( getQB(i).getPosNameYrOvrPot_Str() );
        }
        for ( int i = 2; i < teamRBs.size(); ++i) {
            benchStr.add( getRB(i).getPosNameYrOvrPot_Str() );
        }
        for ( int i = 3; i < teamWRs.size(); ++i) {
            benchStr.add( getWR(i).getPosNameYrOvrPot_Str() );
        }
        for ( int i = 5; i < teamOLs.size(); ++i) {
            benchStr.add( getOL(i).getPosNameYrOvrPot_Str() );
        }
        for ( int i = 1; i < teamKs.size(); ++i) {
            benchStr.add( getK(i).getPosNameYrOvrPot_Str() );
        }
        for ( int i = 1; i < teamSs.size(); ++i) {
            benchStr.add(getS(i).getPosNameYrOvrPot_Str());
        }
        for ( int i = 3; i < teamCBs.size(); ++i) {
            benchStr.add( getCB(i).getPosNameYrOvrPot_Str() );
        }
        for ( int i = 7; i < teamF7s.size(); ++i) {
            benchStr.add( getF7(i).getPosNameYrOvrPot_Str() );
        }
        playerStatsMap.put(ph, benchStr);

        return playerStatsMap;
    }

    public Player findBenchPlayer(String line) {
        for (Player p : teamQBs) {
            if (p.getPosNameYrOvrPot_Str().equals(line)) return p;
        }
        for (Player p : teamRBs) {
            if (p.getPosNameYrOvrPot_Str().equals(line)) return p;
        }
        for (Player p : teamWRs) {
            if (p.getPosNameYrOvrPot_Str().equals(line)) return p;
        }
        for (Player p : teamOLs) {
            if (p.getPosNameYrOvrPot_Str().equals(line)) return p;
        }
        for (Player p : teamKs) {
            if (p.getPosNameYrOvrPot_Str().equals(line)) return p;
        }
        for (Player p : teamSs) {
            if (p.getPosNameYrOvrPot_Str().equals(line)) return p;
        }
        for (Player p : teamCBs) {
            if (p.getPosNameYrOvrPot_Str().equals(line)) return p;
        }
        for (Player p : teamF7s) {
            if (p.getPosNameYrOvrPot_Str().equals(line)) return p;
        }
        return null;
    }

    /**
     * Gets rank str, i.e. 12 -> 12th, 3 -> 3rd
     * @param num ranking
     * @return string of the ranking with correct ending
     */
    public String getRankStr(int num) {
        if (num == 11) {
            return "11th";
        } else if (num == 12) {
            return "12th";
        } else if (num == 13) {
            return "13th";
        } else if (num%10 == 1) {
            return num + "st";
        } else if (num%10 == 2) {
            return num + "nd";
        } else if (num%10 == 3){
            return num + "rd";
        } else {
            return num + "th";
        }
    }

    /**
     * Get rank string of the user (no longer used?)
     * @param num ranking
     * @return ranking with correct ending
     */
    public String getRankStrStarUser(int num) {
        if (true) {
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
        } else {
            if (num == 11) {
                return "** 11th **";
            } else if (num == 12) {
                return "** 12th **";
            } else if (num == 13) {
                return "** 13th **";
            } else if (num % 10 == 1) {
                return "** " + num + "st **";
            } else if (num % 10 == 2) {
                return "** " + num + "nd **";
            } else if (num % 10 == 3) {
                return "** " + num + "rd **";
            } else {
                return "** " + num + "th **";
            }
        }
    }

    /**
     * Gets the number of games played so far
     * @return number of games played
     */
    public int numGames() {
        if ( wins + losses > 0 ) {
            return wins + losses;
        } else return 1;
    }

    public String getStrAbbrWL() {
        return name + " (" + wins + "-" + losses + ")";
    }

    public String getStrAbbrWL_2Lines() {
        return name + "\n(" + wins + "-" + losses + ")";
    }

    /**
     * Gets the number of in-conference wins, used for CCG rankings
     * @return number of in-conf wins
     */
    public int getConfWins() {
        int confWins = 0;
        Game g;
        for (int i = 0; i < gameWLSchedule.size(); ++i) {
            g = gameSchedule.get(i);
            if ( g.gameName.equals("Conference") || g.gameName.equals("Rivalry Game") ) {
                // in conference game, see if was won
                if ( g.homeTeam == this && g.homeScore > g.awayScore ) {
                    confWins++;
                } else if ( g.awayTeam == this && g.homeScore < g.awayScore ) {
                    confWins++;
                }
            }
        }
        return confWins;
    }

    /**
     * Gets the number of in-conference losses, used for CCG rankings
     * @return number of in-conf losses
     */
    public int getConfLosses() {
        int confLosses = 0;
        Game g;
        for (int i = 0; i < gameWLSchedule.size(); ++i) {
            g = gameSchedule.get(i);
            if ( g.gameName.equals("Conference") || g.gameName.equals("Rivalry Game") ) {
                // in conference game, see if was won
                if ( g.homeTeam == this && g.homeScore < g.awayScore ) {
                    confLosses++;
                } else if ( g.awayTeam == this && g.homeScore > g.awayScore ) {
                    confLosses++;
                }
            }
        }
        return confLosses;
    }

    //Team Names for main display spinner
    public String mainTeam() {
        return "["+ rankTeamPollScore + "]" + name + "(" + wins + "-" + losses +")";
    }
    /**
     * Str rep of team, no bowl results
     * @return ranking abbr (w-l)
     */
    public String strRep() {
        return "#" + rankTeamPollScore + " " + abbr + " (" + wins + "-" + losses + ")";  //changing to name messes up home screen order
    }
    /**
     * Str rep of team, with bowl results
     * @return ranking abbr (w-l) BW
     */
    public String strRepWithBowlResults() {
        return /*"#" + rankTeamPollScore + " " +*/ name + " (" + wins + "-" + losses + ") " + confChampion + " " + semiFinalWL + natChampWL;
    }

    /**
     * Str rep of team, with prestige
     * @return ranking abbr (Pres: XX)
     */
    public String strRepWithPrestige() {
        return /*"#" + rankTeamPollScore + " " + */name + " [" + teamPrestige + "]";
    }

    /**
     * Get what happened during the week for the team
     * @return name W/L gameSum, new poll rank #1
     */
    public String weekSummaryStr() {
        int i = wins + losses - 1;
        Game g = gameSchedule.get(i);
        String gameSummary = gameWLSchedule.get(i) + " " + gameSummaryStr(g);
        String rivalryGameStr = "";
        if (g.gameName.equals("Rivalry Game")) {
            if ( gameWLSchedule.get(i).equals("W") ) rivalryGameStr = "Won against Rival!\n";
            else rivalryGameStr = "Lost against Rival!\n";
        }
        return rivalryGameStr + name + " " + gameSummary + "\nNew poll rank: #" + rankTeamPollScore + " " + name + " (" + wins + "-" + losses + ")";
    }

    /**
     * Gets the one-line summary of a game
     * @param g Game to get summary from
     * @return 31 - 43 @ GEO #60   POP UP MSG
     */
    public String gameSummaryStr(Game g) {
        if (g.homeTeam == this) {
            return g.homeScore + " - " + g.awayScore + " vs " + g.awayTeam.name + " #" + g.awayTeam.rankTeamPollScore;
        } else {
            return g.awayScore + " - " + g.homeScore + " @ " + g.homeTeam.name + " #" + g.homeTeam.rankTeamPollScore;
        }
    }

    /**
     * Get just the score of the game
     * @param g Game to get score from
     * @return "myTeamScore - otherTeamScore"
     */
    public String gameSummaryStrScore(Game g) {
        if (g.homeTeam == this) {
            return g.homeScore + " - " + g.awayScore;
        } else {
            return g.awayScore + " - " + g.homeScore;
        }
    }

    /**
     * Get the vs/@ part of the game summary
     * @param g Game to get from
     * @return vs OPP #45
     */
    public String gameSummaryStrOpponent(Game g) {
        if (g.homeTeam == this) {
            return "vs " + g.awayTeam.name + " #" + g.awayTeam.rankTeamPollScore;
        } else {
            return "@ " + g.homeTeam.name + " #" + g.homeTeam.rankTeamPollScore;
        }
    }

    /**
     * Get String of who all is graduating from the team
     * @return string of everyone who is graduating sorted by position
     */
    public String getGraduatingPlayersStr() {
        StringBuilder sb = new StringBuilder();
        for (Player p : playersLeaving) {
            sb.append(p.getPosNameYrOvrPot_OneLine() +"\n");
        }
        return sb.toString();
    }

    public String[] getGradPlayersList() {
        String[] playersLeavingList = new String[playersLeaving.size()];
        for (int i = 0; i < playersLeavingList.length; ++i) {
            playersLeavingList[i] = playersLeaving.get(i).getPosNameYrOvrPot_Str();
        }
        return playersLeavingList;
    }

    /**
     * Get string of the current team needs (not used anymore?)
     * @return String of all the position needs
     */
    public String getTeamNeeds() {
        StringBuilder needs = new StringBuilder();
        needs.append("\t\t"+(2-teamQBs.size())+ "QBs, ");
        needs.append((4-teamRBs.size())+ "RBs, ");
        needs.append((6-teamWRs.size())+ "WRs, ");
        needs.append((2-teamKs.size())+ "Ks\n");
        needs.append("\t\t"+(10-teamOLs.size())+ "OLs, ");
        needs.append((2-teamSs.size())+ "Ss, ");
        needs.append((6-teamCBs.size())+ "CBs, ");
        needs.append((14-teamF7s.size())+ "F7s");
        return needs.toString();
    }

    public PlayerQB[] getQBRecruits() {
        PlayerQB[] recruits = new PlayerQB[numRecruits];
        int stars;
        for (int i = 0; i < numRecruits; ++i) {
            stars = (int)(5*(float)(numRecruits - i/2)/numRecruits);
            recruits[i] = new PlayerQB(league.getRandName(), 1, stars, this);
        }
        Arrays.sort(recruits, new PlayerComparator());
        return recruits;
    }

    public PlayerRB[] getRBRecruits() {
        int numRBrecruits = 2*numRecruits;
        PlayerRB[] recruits = new PlayerRB[numRBrecruits];
        int stars;
        for (int i = 0; i < numRBrecruits; ++i) {
            stars = (int)(5*(float)(numRBrecruits - i/2)/numRBrecruits);
            recruits[i] = new PlayerRB(league.getRandName(), 1, stars, this);
        }
        Arrays.sort(recruits, new PlayerComparator());
        return recruits;
    }

    public PlayerWR[] getWRRecruits() {
        int adjNumRecruits = 2*numRecruits;
        PlayerWR[] recruits = new PlayerWR[adjNumRecruits];
        int stars;
        for (int i = 0; i < adjNumRecruits; ++i) {
            stars = (int)(5*(float)(adjNumRecruits - i/2)/adjNumRecruits);
            recruits[i] = new PlayerWR(league.getRandName(), 1, stars, this);
        }
        Arrays.sort(recruits, new PlayerComparator());
        return recruits;
    }

    public PlayerOL[] getOLRecruits() {
        int adjNumRecruits = 3*numRecruits;
        PlayerOL[] recruits = new PlayerOL[adjNumRecruits];
        int stars;
        for (int i = 0; i < adjNumRecruits; ++i) {
            stars = (int)(5*(float)(adjNumRecruits - i/2)/adjNumRecruits);
            recruits[i] = new PlayerOL(league.getRandName(), 1, stars, this);
        }
        Arrays.sort(recruits, new PlayerComparator());
        return recruits;
    }

    public PlayerK[] getKRecruits() {
        int adjNumRecruits = numRecruits;
        PlayerK[] recruits = new PlayerK[adjNumRecruits];
        int stars;
        for (int i = 0; i < adjNumRecruits; ++i) {
            stars = (int)(5*(float)(adjNumRecruits - i/2)/adjNumRecruits);
            recruits[i] = new PlayerK(league.getRandName(), 1, stars, this);
        }
        Arrays.sort(recruits, new PlayerComparator());
        return recruits;
    }

    public PlayerS[] getSRecruits() {
        int adjNumRecruits = numRecruits;
        PlayerS[] recruits = new PlayerS[adjNumRecruits];
        int stars;
        for (int i = 0; i < adjNumRecruits; ++i) {
            stars = (int)(5*(float)(adjNumRecruits - i/2)/adjNumRecruits);
            recruits[i] = new PlayerS(league.getRandName(), 1, stars, this);
        }
        Arrays.sort(recruits, new PlayerComparator());
        return recruits;
    }

    public PlayerCB[] getCBRecruits() {
        int adjNumRecruits = 2*numRecruits;
        PlayerCB[] recruits = new PlayerCB[adjNumRecruits];
        int stars;
        for (int i = 0; i < adjNumRecruits; ++i) {
            stars = (int)(5*(float)(adjNumRecruits - i/2)/adjNumRecruits);
            recruits[i] = new PlayerCB(league.getRandName(), 1, stars, this);
        }
        Arrays.sort(recruits, new PlayerComparator());
        return recruits;
    }

    public PlayerF7[] getF7Recruits() {
        int adjNumRecruits = 3*numRecruits;
        PlayerF7[] recruits = new PlayerF7[adjNumRecruits];
        int stars;
        for (int i = 0; i < adjNumRecruits; ++i) {
            stars = (int)(5*(float)(adjNumRecruits - i/2)/adjNumRecruits);
            recruits[i] = new PlayerF7(league.getRandName(), 1, stars, this);
        }
        Arrays.sort(recruits, new PlayerComparator());
        return recruits;
    }

    /**
     * Save all the recruits into a string to be used by RecruitingActivity
     * @return String of all the recruits
     */
    public String getRecruitsInfoSaveFile() {
        StringBuilder sb = new StringBuilder();
        PlayerQB[] qbs = getQBRecruits();
        for (PlayerQB qb : qbs) {
            sb.append("QB," + qb.name + "," + qb.year + "," + qb.ratPot + "," + qb.ratFootIQ + "," +
                    qb.ratPassPow + "," + qb.ratPassAcc + "," + qb.ratPassEva + "," + qb.ratOvr + "," + qb.cost + "," + qb.ratDur + "%\n");
        }
        PlayerRB[] rbs = getRBRecruits();
        for (PlayerRB rb : rbs) {
            sb.append("RB," + rb.name + "," + rb.year + "," + rb.ratPot + "," + rb.ratFootIQ + "," +
                    rb.ratRushPow + "," + rb.ratRushSpd + "," + rb.ratRushEva + "," + rb.ratOvr + "," + rb.cost + "," + rb.ratDur + "%\n");
        }
        PlayerWR[] wrs = getWRRecruits();
        for (PlayerWR wr : wrs) {
            sb.append("WR," + wr.name + "," + wr.year + "," + wr.ratPot + "," + wr.ratFootIQ + "," +
                    wr.ratRecCat + "," + wr.ratRecSpd + "," + wr.ratRecEva + "," + wr.ratOvr + "," + wr.cost + "," + wr.ratDur + "%\n");
        }
        PlayerK[] ks = getKRecruits();
        for (PlayerK k : ks) {
            sb.append("K," + k.name + "," + k.year + "," + k.ratPot + "," + k.ratFootIQ + "," +
                    k.ratKickPow + "," + k.ratKickAcc + "," + k.ratKickFum + "," + k.ratOvr + "," + k.cost + "," + k.ratDur + "%\n");
        }
        PlayerOL[] ols = getOLRecruits();
        for (PlayerOL ol : ols) {
            sb.append("OL," + ol.name + "," + ol.year + "," + ol.ratPot + "," + ol.ratFootIQ + "," +
                    ol.ratOLPow + "," + ol.ratOLBkR + "," + ol.ratOLBkP + "," + ol.ratOvr + "," + ol.cost + "," + ol.ratDur + "%\n");
        }
        PlayerS[] ss = getSRecruits();
        for (PlayerS s : ss) {
            sb.append("S," + s.name + "," + s.year + "," + s.ratPot + "," + s.ratFootIQ + "," +
                    s.ratSCov + "," + s.ratSSpd + "," + s.ratSTkl + "," + s.ratOvr + "," + s.cost + "," + s.ratDur + "%\n");
        }
        PlayerCB[] cbs = getCBRecruits();
        for (PlayerCB cb : cbs) {
            sb.append("CB," + cb.name + "," + cb.year + "," + cb.ratPot + "," + cb.ratFootIQ + "," +
                    cb.ratCBCov + "," + cb.ratCBSpd + "," + cb.ratCBTkl + "," + cb.ratOvr + "," + cb.cost + "," + cb.ratDur + "%\n");
        }
        PlayerF7[] f7s = getF7Recruits();
        for (PlayerF7 f7 : f7s) {
            sb.append("F7," + f7.name + "," + f7.year + "," + f7.ratPot + "," + f7.ratFootIQ + "," +
                    f7.ratF7Pow + "," + f7.ratF7Rsh + "," + f7.ratF7Pas + "," + f7.ratOvr + "," + f7.cost + "," + f7.ratDur + "%\n");
        }
        return sb.toString();
    }

    /**
     * Save all the current players into a string to be loaded from later
     * @return string of all the players in csv form
     */
    public String getPlayerInfoSaveFile() {
        StringBuilder sb = new StringBuilder();
        for (PlayerQB qb : teamQBs) {
            sb.append("QB," + qb.name + "," + qb.year + "," + qb.ratPot + "," + qb.ratFootIQ + "," +
                    qb.ratPassPow + "," + qb.ratPassAcc + "," + qb.ratPassEva + "," + qb.ratOvr + "," + qb.ratImprovement + "," + qb.ratDur + "," +
                    qb.careerGamesPlayed + "," + qb.careerPassAtt + "," + qb.careerPassComp + "," + qb.careerTDs + "," + qb.careerInt + "," +
                    qb.careerPassYards + "," + qb.careerSacked + "," + qb.careerHeismans + "," + qb.careerAllAmerican + "," + qb.careerAllConference + "," + qb.careerWins + "%\n");
        }
        for (PlayerRB rb : teamRBs) {
            sb.append("RB," + rb.name + "," + rb.year + "," + rb.ratPot + "," + rb.ratFootIQ + "," +
                    rb.ratRushPow + "," + rb.ratRushSpd + "," + rb.ratRushEva + "," + rb.ratOvr + "," + rb.ratImprovement + "," + rb.ratDur + "," +
                    rb.careerGamesPlayed + "," + rb.careerRushAtt + "," + rb.careerRushYards + "," + rb.careerTDs + "," + rb.careerFumbles + "," +
                    rb.careerHeismans + "," + rb.careerAllAmerican + "," + rb.careerAllConference + "," + rb.careerWins + "%\n");
        }
        for (PlayerWR wr : teamWRs) {
            sb.append("WR," + wr.name + "," + wr.year + "," + wr.ratPot + "," + wr.ratFootIQ + "," +
                    wr.ratRecCat + "," + wr.ratRecSpd + "," + wr.ratRecEva + "," + wr.ratOvr + "," + wr.ratImprovement + "," + wr.ratDur + "," +
                    wr.careerGamesPlayed + "," + wr.careerTargets + "," + wr.careerReceptions + "," + wr.careerRecYards + "," + wr.careerTD + "," +
                    wr.careerDrops + "," + wr.careerFumbles + "," + wr.careerHeismans + "," + wr.careerAllAmerican + "," + wr.careerAllConference + "," + wr.careerWins + "%\n");
        }
        for (PlayerK k : teamKs) {
            sb.append("K," + k.name + "," + k.year + "," + k.ratPot + "," + k.ratFootIQ + "," +
                    k.ratKickPow + "," + k.ratKickAcc + "," + k.ratKickFum + "," + k.ratOvr + "," + k.ratImprovement + "," + k.ratDur + "," +
                    k.careerGamesPlayed + "," + k.careerXPAtt + "," + k.careerXPMade + "," + k.careerFGAtt + "," + k.careerFGMade + "," +
                    k.careerHeismans + "," + k.careerAllAmerican + "," + k.careerAllConference + "," + k.careerWins + "%\n");
        }
        for (PlayerOL ol : teamOLs) {
            sb.append("OL," + ol.name + "," + ol.year + "," + ol.ratPot + "," + ol.ratFootIQ + "," +
                    ol.ratOLPow + "," + ol.ratOLBkR + "," + ol.ratOLBkP + "," + ol.ratOvr + "," + ol.ratImprovement + "," + ol.ratDur + "," +
                    ol.careerGamesPlayed + "," + ol.careerHeismans + "," + ol.careerAllAmerican + "," + ol.careerAllConference + "," + ol.careerWins + "%\n");
        }
        for (PlayerS s : teamSs) {
            sb.append("S," + s.name + "," + s.year + "," + s.ratPot + "," + s.ratFootIQ + "," +
                    s.ratSCov + "," + s.ratSSpd + "," + s.ratSTkl + "," + s.ratOvr + "," + s.ratImprovement + "," + s.ratDur + "," +
                    s.careerGamesPlayed + "," + s.careerHeismans + "," + s.careerAllAmerican + "," + s.careerAllConference + "," + s.careerWins + "%\n");
        }
        for (PlayerCB cb : teamCBs) {
            sb.append("CB," + cb.name + "," + cb.year + "," + cb.ratPot + "," + cb.ratFootIQ + "," +
                    cb.ratCBCov + "," + cb.ratCBSpd + "," + cb.ratCBTkl + "," + cb.ratOvr + "," + cb.ratImprovement + "," + cb.ratDur + "," +
                    cb.careerGamesPlayed + "," + cb.careerHeismans + "," + cb.careerAllAmerican + "," + cb.careerAllConference + "," + cb.careerWins + "%\n");
        }
        for (PlayerF7 f7 : teamF7s) {
            sb.append("F7," + f7.name + "," + f7.year + "," + f7.ratPot + "," + f7.ratFootIQ + "," +
                    f7.ratF7Pow + "," + f7.ratF7Rsh + "," + f7.ratF7Pas + "," + f7.ratOvr + "," + f7.ratImprovement + "," + f7.ratDur + "," +
                    f7.careerGamesPlayed + "," + f7.careerHeismans + "," + f7.careerAllAmerican + "," + f7.careerAllConference + "," + f7.careerWins + "%\n");
        }
        return sb.toString();
    }

    /**
     * Generate all the offense team strategies that can be selected
     * @return array of all the offense team strats
     */
    public TeamStrategy[] getTeamStrategiesOff() {
        TeamStrategy[] ts = new TeamStrategy[3];

        ts[0] = new TeamStrategy("Aggressive",
                "Play a more aggressive offense. Will pass with lower completion percentage and higher chance of interception." +
                        " However, catches will go for more yards.", -1, 2, 3, 1);

        ts[1] = new TeamStrategy("No Preference",
                "Will play a normal offense with no bonus either way, but no penalties either.", 0, 0, 0, 0);

        ts[2] = new TeamStrategy("Conservative",
                "Play a more conservative offense, running a bit more and passing slightly less. Passes are more accurate but shorter." +
                        " Rushes are more likely to gain yards but less likely to break free for big plays.", 1, -2, -3, -1);

        return ts;
    }

    // Generate CPU Strategy
    public int getCPUOffense() {
        int OP, OR, OS = 0;
        OP = getPassProf();
        OR = getRushProf();
        if(OP > (OR + 2)) {
            OS = 0;
        } else if(OR > (OP + 2)) {
            OS = 2;
        } else OS = 1;
        return OS;
    }

    public int getCPUDefense() {
        int DP, DR, DS = 0;
        DP = getPassProf();
        DR = getRushProf();
        if(DR > (DP + 2)) {
            DS = 0;
        } else if(DP > (DR + 2)) {
            DS = 2;
        } else DS = 1;
        return DS;
    }
    /**
     * Generate all the defense team strategies that can be selected
     * @return array of all the defense team strats
     */
    public TeamStrategy[] getTeamStrategiesDef() {
        TeamStrategy[] ts = new TeamStrategy[3];

        ts[0] = new TeamStrategy("Stack the Box",
                "Focus on stopping the run. Will give up more big passing plays but will allow less rushing yards and far less big plays from rushing.", 1, 0, -1, -1);

        ts[1] = new TeamStrategy("No Preference",
                "Will play a normal defense with no bonus either way, but no penalties either.", 0, 0, 0, 0);

        ts[2] = new TeamStrategy("Nickel Zone",
                "Focus on stopping the pass. Will give up less yards on catches and will be more likely to intercept passes, " +
                        "but will allow more rushing yards.", -1, 0, 1, 1);

        return ts;
    }


    /**
     * Set the starters for a particular position.
     * @param starters new starters to be set
     * @param position position, 0 - 7
     */
    public void setStarters(ArrayList<Player> starters, int position) {
        switch (position) {
            case 0:
                ArrayList<PlayerQB> oldQBs = new ArrayList<>();
                oldQBs.addAll(teamQBs);
                teamQBs.clear();
                for (Player p : starters) {
                    teamQBs.add( (PlayerQB) p );
                }
                Collections.sort(teamQBs, new PlayerComparator());
                for (PlayerQB oldP : oldQBs) {
                    if (!teamQBs.contains(oldP)) teamQBs.add(oldP);
                }
                break;
            case 1:
                ArrayList<PlayerRB> oldRBs = new ArrayList<>();
                oldRBs.addAll(teamRBs);
                teamRBs.clear();
                for (Player p : starters) {
                    teamRBs.add( (PlayerRB) p );
                }
                Collections.sort(teamRBs, new PlayerComparator());
                for (PlayerRB oldP : oldRBs) {
                    if (!teamRBs.contains(oldP)) teamRBs.add(oldP);
                }
                break;
            case 2:
                ArrayList<PlayerWR> oldWRs = new ArrayList<>();
                oldWRs.addAll(teamWRs);
                teamWRs.clear();
                for (Player p : starters) {
                    teamWRs.add( (PlayerWR) p );
                }
                Collections.sort(teamWRs, new PlayerComparator());
                for (PlayerWR oldP : oldWRs) {
                    if (!teamWRs.contains(oldP)) teamWRs.add(oldP);
                }
                break;
            case 3:
                ArrayList<PlayerOL> oldOLs = new ArrayList<>();
                oldOLs.addAll(teamOLs);
                teamOLs.clear();
                for (Player p : starters) {
                    teamOLs.add( (PlayerOL) p );
                }
                Collections.sort(teamOLs, new PlayerComparator());
                for (PlayerOL oldP : oldOLs) {
                    if (!teamOLs.contains(oldP)) teamOLs.add(oldP);
                }
                break;
            case 4:
                ArrayList<PlayerK> oldKs = new ArrayList<>();
                oldKs.addAll(teamKs);
                teamKs.clear();
                for (Player p : starters) {
                    teamKs.add( (PlayerK) p );
                }
                Collections.sort(teamKs, new PlayerComparator());
                for (PlayerK oldP : oldKs) {
                    if (!teamKs.contains(oldP)) teamKs.add(oldP);
                }
                break;
            case 5:
                ArrayList<PlayerS> oldSs = new ArrayList<>();
                oldSs.addAll(teamSs);
                teamSs.clear();
                for (Player p : starters) {
                    teamSs.add( (PlayerS) p );
                }
                Collections.sort(teamSs, new PlayerComparator());
                for (PlayerS oldP : oldSs) {
                    if (!teamSs.contains(oldP)) teamSs.add(oldP);
                }
                break;
            case 6:
                ArrayList<PlayerCB> oldCBs = new ArrayList<>();
                oldCBs.addAll(teamCBs);
                teamCBs.clear();
                for (Player p : starters) {
                    teamCBs.add( (PlayerCB) p );
                }
                Collections.sort(teamCBs, new PlayerComparator());
                for (PlayerCB oldP : oldCBs) {
                    if (!teamCBs.contains(oldP)) teamCBs.add(oldP);
                }
                break;
            case 7:
                ArrayList<PlayerF7> oldF7s = new ArrayList<>();
                oldF7s.addAll(teamF7s);
                teamF7s.clear();
                for (Player p : starters) {
                    teamF7s.add( (PlayerF7) p );
                }
                Collections.sort(teamF7s, new PlayerComparator());
                for (PlayerF7 oldP : oldF7s) {
                    if (!teamF7s.contains(oldP)) teamF7s.add(oldP);
                }
                break;
        }

        // Set ranks so that Off/Def Talent rankings are updated
        league.setTeamRanks();
    }

    /**
     * Add one gamePlayed to all the starters.
     * The number of games played affects how much players improve.
     */
    public void addGamePlayedPlayers(boolean wonGame) {
        addGamePlayedList(teamQBs, 1, wonGame);
        addGamePlayedList(teamRBs, 2, wonGame);
        addGamePlayedList(teamWRs, 3, wonGame);
        addGamePlayedList(teamOLs, 5, wonGame);
        addGamePlayedList(teamKs, 1, wonGame);
        addGamePlayedList(teamSs, 1, wonGame);
        addGamePlayedList(teamCBs, 3, wonGame);
        addGamePlayedList(teamF7s, 7, wonGame);
    }

    private void addGamePlayedList(ArrayList<? extends Player> playerList, int starters, boolean wonGame) {
        for (int i = 0; i < starters; ++i) {
            playerList.get(i).gamesPlayed++;
            if (wonGame) playerList.get(i).statsWins++;
        }
    }
}

/**
 * Comparator used to sort players by overall
 */
class PlayerComparator implements Comparator<Player> {
    @Override
    public int compare( Player a, Player b ) {
        if (!a.isInjured && !b.isInjured) {
            // If both players aren't injured
            if (a.year > 0 && b.year > 0) {
                // If both players aren't redshirted
                if (a.ratOvr > b.ratOvr) return -1;
                else if (a.ratOvr == b.ratOvr)
                    return a.ratPot > b.ratPot ? -1 : a.ratPot == b.ratPot ? 0 : 1;
                else return 1;
            } else if (a.year > 0)
                return -1;
            else if (b.year > 0)
                return 1;
            else
                return a.ratOvr > b.ratOvr ? -1 : a.ratOvr == b.ratOvr ? 0 : 1;
        } else if (!a.isInjured) {
            return -1;
        } else if (!b.isInjured) {
            return  1;
        } else {
            return a.ratOvr > b.ratOvr ? -1 : a.ratOvr == b.ratOvr ? 0 : 1;
        }
    }
}

/**
 * Comparator used to sort players by position, QB-RB-WR-OL-K-S-CB-F7
 */
class PlayerPositionComparator implements Comparator<Player> {
    @Override
    public int compare( Player a, Player b ) {
        int aPos = Player.getPosNumber(a.position);
        int bPos = Player.getPosNumber(b.position);
        return aPos < bPos ? -1 : aPos == bPos ? 0 : 1;
    }
}
