package CFBsimPack;

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
    public Playbook playbook;
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
    public Game gameOOCSchedule1;
    public Game gameOOCSchedule2;
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
    public int teamSOS;

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
    public int rankTeamSOS;
    public int teamCount = 12;
    public int totalTeamCount = teamCount * 10;

    //prestige/talent improvements
    public int diffPrestige;
    public int diffOffTalent;
    public int diffDefTalent;
    public int newPrestige;
    //public int confPrestige;

    //players on team
    //offense
    public ArrayList<PlayerQB> teamQBs;
    public ArrayList<PlayerRB> teamRBs;
    public ArrayList<PlayerWR> teamWRs;
    public ArrayList<PlayerTE> teamTEs;
    public ArrayList<PlayerK> teamKs;
    public ArrayList<PlayerOL> teamOLs;
    //defense
    public ArrayList<PlayerDL> teamDLs;
    public ArrayList<PlayerLB> teamLBs;
    public ArrayList<PlayerCB> teamCBs;
    public ArrayList<PlayerS> teamSs;
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
    private static final double NFL_CHANCE = 0.67;

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
        teamTEs = new ArrayList<PlayerTE>();
        teamKs = new ArrayList<PlayerK>();
        teamOLs = new ArrayList<PlayerOL>();
        teamDLs = new ArrayList<PlayerDL>();
        teamLBs = new ArrayList<PlayerLB>();
        teamCBs = new ArrayList<PlayerCB>();
        teamSs = new ArrayList<PlayerS>();


        teamRSs = new ArrayList<Player>();
        teamFRs = new ArrayList<Player>();
        teamSOs = new ArrayList<Player>();
        teamJRs = new ArrayList<Player>();
        teamSRs = new ArrayList<Player>();

        gameSchedule = new ArrayList<Game>();
        gameOOCSchedule0 = null;
        gameOOCSchedule1 = null;
        gameOOCSchedule2 = null;
        gameWinsAgainst = new ArrayList<Team>();
        gameWLSchedule = new ArrayList<String>();
        confChampion = "";
        semiFinalWL = "";
        natChampWL = "";

        teamPrestige = prestige;
        recruitPlayers(2, 4, 6, 2, 10, 2, 8, 6, 6, 2);

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
        teamStratOffNum = getCPUOffense();
        teamStratDefNum = getCPUDefense();
        teamStratOff = getTeamStrategiesOff()[teamStratOffNum];
        teamStratDef = getTeamStrategiesDef()[teamStratDefNum];
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
        teamTEs = new ArrayList<PlayerTE>();
        teamKs = new ArrayList<PlayerK>();
        teamOLs = new ArrayList<PlayerOL>();
        teamDLs = new ArrayList<PlayerDL>();
        teamLBs = new ArrayList<PlayerLB>();
        teamCBs = new ArrayList<PlayerCB>();
        teamSs = new ArrayList<PlayerS>();


        teamRSs = new ArrayList<Player>();
        teamFRs = new ArrayList<Player>();
        teamSOs = new ArrayList<Player>();
        teamJRs = new ArrayList<Player>();
        teamSRs = new ArrayList<Player>();

        gameSchedule = new ArrayList<Game>();
        gameOOCSchedule0 = null;
        gameOOCSchedule1 = null;
        gameOOCSchedule2 = null;
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
        teamStratOffNum = 0;
        teamStratDefNum = 0;
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

        int newPrestige[] = calcSeasonPrestige();
        teamPrestige = newPrestige[0];

        if (userControlled) checkHallofFame();

        checkCareerRecords(league.leagueRecords);
        if (league.userTeam == this) checkCareerRecords(league.userTeamRecords);

        advanceSeasonPlayers();

    }

    //Calculates Prestige Change at end of season
    public int[] calcSeasonPrestige() {
        int expectedPollFinish = 100 - teamPrestige;
        int diffExpected = expectedPollFinish - rankTeamPollScore;
        int newPrestige = teamPrestige;
        int rivalryPts = 0;
        int players = 0;
        int ccPts = 0;
        int ncwPts = 0;
        int nflPts = 0;
        int rgameplayed = 0;

        Game g;
        for (int i = 0; i < gameWLSchedule.size(); ++i) {
            g = gameSchedule.get(i);
            if (g.gameName.equals("Rivalry Game") ) {
                rgameplayed = 1;
            }
        }

        // Don't add/subtract prestige if they are a blessed/cursed team from last season
        if (this != league.saveBless && this != league.saveBless2 && this != league.saveBless3 && this != league.saveBless4 && this != league.saveBless5
                && this != league.saveCurse && this != league.saveCurse2 && this != league.saveCurse3 && this != league.saveCurse4 && this != league.saveCurse5) {

            //RIVALRY POINTS!
            //Only call it if there's not a huge difference in team strength
            if (Math.abs(teamPrestige - league.findTeamAbbr(rivalTeam).teamPrestige) < 20 && rgameplayed == 1) {
                if (teamPrestige >= 75) {
                    //Good Teams: favorite teams get no points, only penalties
                    if (wonRivalryGame && (teamPrestige - league.findTeamAbbr(rivalTeam).teamPrestige) > 0) {
                        newPrestige += 0;
                        rivalryPts += 0;
                    } else if (!wonRivalryGame && (teamPrestige - league.findTeamAbbr(rivalTeam).teamPrestige) > 0) {
                        newPrestige -= 2;
                        rivalryPts -= 2;
                        //If you're the underdog, you get points, unless you lose ;)
                    } else if (wonRivalryGame && (teamPrestige - league.findTeamAbbr(rivalTeam).teamPrestige) < 0) {
                        newPrestige += 1;
                        rivalryPts += 1;
                    } else if (!wonRivalryGame && (teamPrestige - league.findTeamAbbr(rivalTeam).teamPrestige) < 0) {
                        newPrestige -= 1;
                        rivalryPts -= 1;
                    }
                } else if (teamPrestige >= 50 && teamPrestige < 75) {
                    //Average Teams: If you're the favorite, you get some points.
                    if (wonRivalryGame && (teamPrestige - league.findTeamAbbr(rivalTeam).teamPrestige) > 0) {
                        newPrestige += 1;
                        rivalryPts += 1;
                    } else if (!wonRivalryGame && (teamPrestige - league.findTeamAbbr(rivalTeam).teamPrestige) > 0) {
                        newPrestige -= 1;
                        rivalryPts -= 1;
                        //If you're the underdog, you get more points, unless you lose ;)
                    } else if (wonRivalryGame && (teamPrestige - league.findTeamAbbr(rivalTeam).teamPrestige) < 0) {
                        newPrestige += 2;
                        rivalryPts += 2;
                    } else if (!wonRivalryGame && (teamPrestige - league.findTeamAbbr(rivalTeam).teamPrestige) < 0) {
                        newPrestige -= 1;
                        rivalryPts -= 1;
                    }
                } else {
                    //Bad Teams: If you're the favorite, you get some points.
                    if (wonRivalryGame && (teamPrestige - league.findTeamAbbr(rivalTeam).teamPrestige) > 0) {
                        newPrestige += 1;
                        rivalryPts += 1;
                    } else if (!wonRivalryGame && (teamPrestige - league.findTeamAbbr(rivalTeam).teamPrestige) > 0) {
                        newPrestige -= 1;
                        rivalryPts -= 1;
                        //If you're the underdog, you get more points, unless you lose ;)
                    } else if (wonRivalryGame && (teamPrestige - league.findTeamAbbr(rivalTeam).teamPrestige) < 0) {
                        newPrestige += 2;
                        rivalryPts += 2;
                    } else if (!wonRivalryGame && (teamPrestige - league.findTeamAbbr(rivalTeam).teamPrestige) < 0) {
                        newPrestige -= 0;
                        rivalryPts -= 0;
                    }
                }
            }

            //Team Prestige change for every team based on ranking
            if ((teamPrestige >= 80) && diffExpected > 0) {
                newPrestige = (int) Math.pow(newPrestige, 1 + (float) diffExpected / 2250); //Top teams: gain little prestige if success
            } else if ((teamPrestige >= 80) && diffExpected < 0) {
                newPrestige = (int) Math.pow(newPrestige, 1 + (float) diffExpected / 1650); //Top teams: lose significant prestige for poor seasons

            } else if ((teamPrestige < 80 && teamPrestige >= 60) && diffExpected > 0) {
                newPrestige = (int) Math.pow(newPrestige, 1 + (float) diffExpected / 1850); //Above avg teams: gain prestige faster
            } else if ((teamPrestige < 80 && teamPrestige >= 60) && diffExpected < 0) {
                newPrestige = (int) Math.pow(newPrestige, 1 + (float) diffExpected / 2000); //Above avg team: lose prestige at normal rate

            } else if ((teamPrestige < 60 && teamPrestige >= 40) && diffExpected > 0) {
                newPrestige = (int) Math.pow(newPrestige, 1 + (float) diffExpected / 1750); //below avg teams: gain prestige fastest
            } else if ((teamPrestige < 60 && teamPrestige >= 40) && diffExpected < 0) {
                newPrestige = (int) Math.pow(newPrestige, 1 + (float) diffExpected / 2200); //below avg teams: lose prestige slower

            } else if ((teamPrestige < 40) && diffExpected > 0) {
                newPrestige = (int) Math.pow(newPrestige, 1 + (float) diffExpected / 2000); //Bad teams: gain prestige at normal rate
            } else if ((teamPrestige < 40) && diffExpected < 0) {
                newPrestige = (int) Math.pow(newPrestige, 1 + (float) diffExpected / 3500); //Bad teams: can't get much worse!
            }
        }

            //National Title Winner
            if (rankTeamPollScore == 1) {
                newPrestige += 3;
                ncwPts += 3;
            }

                //bonus for winning conference
            if ("CC".equals(confChampion)) {
                newPrestige += 1;
                ccPts += 1;
            }

            //Bonus for NFL Prospects if team is medicore
            if (teamPrestige < 50) {
                getPlayersLeaving();
                for (int i = 0; i < playersLeaving.size(); ++i) {
                    if (playersLeaving.get(i).ratOvr > 90 && !playersLeaving.get(i).position.equals("K")) {
                        players++;
                    }
                }
                if (players > 2) {
                    newPrestige += 2;
                    nflPts += 2;
                }
                if (players > 0) {
                    newPrestige += 2;
                    nflPts += 2;
                }
            }

                //Sets the bounds for Prestige
                if (newPrestige > 95) newPrestige = 95;
                if (newPrestige < 25) newPrestige = 25;

        int PrestigeScore[] = {newPrestige, rivalryPts, ccPts, ncwPts, nflPts, rgameplayed};
        return PrestigeScore;
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
                records.checkRecord("Pass Yards", getQB(i).statsPassYards, abbr + ": " + getQB(i).getInitialName(), league.getYear());
                records.checkRecord("Pass TDs", getQB(i).statsPassTD, abbr + ": " + getQB(i).getInitialName(), league.getYear());
                records.checkRecord("Ints Thrown", getQB(i).statsInt, abbr + ": " + getQB(i).getInitialName(), league.getYear());
                records.checkRecord("Comp Percent", (100 * getQB(i).statsPassComp) / (getQB(i).statsPassAtt + 1), abbr + ": " + getQB(i).getInitialName(), league.getYear());
                records.checkRecord("Rush Yards", getQB(i).statsRushYards, abbr + ": " + getQB(i).getInitialName(), league.getYear());
                records.checkRecord("Rush TDs", getQB(i).statsRushTD, abbr + ": " + getQB(i).getInitialName(), league.getYear());
                records.checkRecord("Fumbles Lost", getQB(i).statsFumbles, abbr + ": " + getQB(i).getInitialName(), league.getYear());
            }
        }


        for (int i = 0; i < teamRBs.size(); ++i) {
            if (getRB(i).gamesPlayed > 6) {
                records.checkRecord("Rush Yards", getRB(i).statsRushYards, abbr + ": " + getRB(i).getInitialName(), league.getYear());
                records.checkRecord("Rush TDs", getRB(i).statsRushTD, abbr + ": " + getRB(i).getInitialName(), league.getYear());
                records.checkRecord("Fumbles Lost", getRB(i).statsFumbles, abbr + ": " + getRB(i).getInitialName(), league.getYear());
            }
        }

        for (int i = 0; i < teamWRs.size(); ++i) {
            if (getWR(i).gamesPlayed > 6) {
                records.checkRecord("Rec Yards", getWR(i).statsRecYards, abbr + ": " + getWR(i).getInitialName(), league.getYear());
                records.checkRecord("Rec TDs", getWR(i).statsTD, abbr + ": " + getWR(i).getInitialName(), league.getYear());
                records.checkRecord("Catch Percent", (100 * getWR(i).statsReceptions) / (getWR(i).statsTargets + 1), abbr + ": " + getWR(i).getInitialName(), league.getYear());
            }
        }

        for (int i = 0; i < teamTEs.size(); ++i) {
            if (getTE(i).gamesPlayed > 6) {
                records.checkRecord("Rec Yards", getTE(i).statsRecYards, abbr + ": " + getTE(i).getInitialName(), league.getYear());
                records.checkRecord("Rec TDs", getTE(i).statsRecTD, abbr + ": " + getTE(i).getInitialName(), league.getYear());
                records.checkRecord("Catch Percent", (100 * getTE(i).statsReceptions) / (getTE(i).statsTargets + 1), abbr + ": " + getTE(i).getInitialName(), league.getYear());
            }
        }
        for (int i = 0; i < teamDLs.size(); ++i) {
            if (getDL(i).gamesPlayed > 6) {
                records.checkRecord("Tackles", getDL(i).statsTackles, abbr + ": " + getDL(i).getInitialName(), league.getYear());
                records.checkRecord("Sacks", getDL(i).statsSacks, abbr + ": " + getDL(i).getInitialName(), league.getYear());
                records.checkRecord("Fumbles Recovered", getDL(i).statsFumbles, abbr + ": " + getDL(i).getInitialName(), league.getYear());
                records.checkRecord("Interceptions", getDL(i).statsInts, abbr + ": " + getDL(i).getInitialName(), league.getYear());
            }
        }
        for (int i = 0; i < teamLBs.size(); ++i) {
            if (getLB(i).gamesPlayed > 6) {
                records.checkRecord("Tackles", getLB(i).statsTackles, abbr + ": " + getLB(i).getInitialName(), league.getYear());
                records.checkRecord("Sacks", getLB(i).statsSacks, abbr + ": " + getLB(i).getInitialName(), league.getYear());
                records.checkRecord("Fumbles Recovered", getLB(i).statsFumbles, abbr + ": " + getLB(i).getInitialName(), league.getYear());
                records.checkRecord("Interceptions", getLB(i).statsInts, abbr + ": " + getLB(i).getInitialName(), league.getYear());
            }
        }
        for (int i = 0; i < teamCBs.size(); ++i) {
            if (getCB(i).gamesPlayed > 6) {
                records.checkRecord("Tackles", getCB(i).statsTackles, abbr + ": " + getCB(i).getInitialName(), league.getYear());
                records.checkRecord("Sacks", getCB(i).statsSacks, abbr + ": " + getCB(i).getInitialName(), league.getYear());
                records.checkRecord("Fumbles Recovered", getCB(i).statsFumbles, abbr + ": " + getCB(i).getInitialName(), league.getYear());
                records.checkRecord("Interceptions", getCB(i).statsInts, abbr + ": " + getCB(i).getInitialName(), league.getYear());
            }
        }
        for (int i = 0; i < teamSs.size(); ++i) {
            if (getS(i).gamesPlayed > 6) {
                records.checkRecord("Tackles", getS(i).statsTackles, abbr + ": " + getS(i).getInitialName(), league.getYear());
                records.checkRecord("Sacks", getS(i).statsSacks, abbr + ": " + getS(i).getInitialName(), league.getYear());
                records.checkRecord("Fumbles Recovered", getS(i).statsFumbles, abbr + ": " + getS(i).getInitialName(), league.getYear());
                records.checkRecord("Interceptions", getS(i).statsInts, abbr + ": " + getS(i).getInitialName(), league.getYear());
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
                records.checkRecord("Career Pass Yards", qb.statsPassYards+qb.careerPassYards, abbr + ": " + qb.getInitialName(), league.getYear()-1);
                records.checkRecord("Career Pass TDs", qb.statsPassTD +qb.careerTDs, abbr + ": " + qb.getInitialName(), league.getYear()-1);
                records.checkRecord("Career Ints Thrown", qb.statsInt+qb.careerInt, abbr + ": " + qb.getInitialName(), league.getYear()-1);
                records.checkRecord("Career Rush Yards", qb.statsRushYards+qb.careerRushYards, abbr + ": " + qb.getInitialName(), league.getYear()-1);
                records.checkRecord("Career Rush TDs", qb.statsRushTD +qb.careerRushTD, abbr + ": " + qb.getInitialName(), league.getYear()-1);
                records.checkRecord("Career Fumbles Lost", qb.statsFumbles+qb.careerFumbles, abbr + ": " + qb.getInitialName(), league.getYear()-1);
            }
            else if (p instanceof PlayerRB) {
                PlayerRB rb = (PlayerRB) p;
                records.checkRecord("Career Rush Yards", rb.statsRushYards+rb.careerRushYards, abbr + ": " + rb.getInitialName(), league.getYear()-1);
                records.checkRecord("Career Rush TDs", rb.statsRushTD +rb.careerTDs, abbr + ": " + rb.getInitialName(), league.getYear()-1);
                records.checkRecord("Career Fumbles Lost", rb.statsFumbles+rb.careerFumbles, abbr + ": " + rb.getInitialName(), league.getYear()-1);
            }
            else if (p instanceof PlayerWR) {
                PlayerWR wr = (PlayerWR) p;
                records.checkRecord("Career Rec Yards", wr.statsRecYards+wr.careerRecYards, abbr + ": " + wr.getInitialName(), league.getYear()-1);
                records.checkRecord("Career Rec TDs", wr.statsTD+wr.careerTD, abbr + ": " + wr.getInitialName(), league.getYear()-1);
            }
            else if (p instanceof PlayerTE) {
                PlayerTE te = (PlayerTE) p;
                records.checkRecord("Career Rec Yards", te.statsRecYards+te.careerRecYards, abbr + ": " + te.getInitialName(), league.getYear()-1);
                records.checkRecord("Career Rec TDs", te.statsRecTD +te.careerTD, abbr + ": " + te.getInitialName(), league.getYear()-1);
            }
            else if (p instanceof PlayerDL) {
                PlayerDL dl = (PlayerDL) p;
                    records.checkRecord("Career Tackles", dl.statsTackles+dl.careerTackles , abbr + ": " + dl.getInitialName(), league.getYear());
                    records.checkRecord("Career Sacks", dl.statsSacks+dl.careerSacks, abbr + ": " + dl.getInitialName(), league.getYear());
                    records.checkRecord("Career Fumbles Rec", dl.statsFumbles+dl.careerFumbles, abbr + ": " + dl.getInitialName(), league.getYear());
                    records.checkRecord("Career Interceptions", dl.statsInts+dl.careerInts, abbr + ": " + dl.getInitialName(), league.getYear());
            }
            else if (p instanceof PlayerLB) {
                PlayerLB lb = (PlayerLB) p;
                records.checkRecord("Career Tackles", lb.statsTackles+lb.careerTackles , abbr + ": " + lb.getInitialName(), league.getYear());
                records.checkRecord("Career Sacks", lb.statsSacks+lb.careerSacks, abbr + ": " + lb.getInitialName(), league.getYear());
                records.checkRecord("Career Fumbles Rec", lb.statsFumbles+lb.careerFumbles, abbr + ": " + lb.getInitialName(), league.getYear());
                records.checkRecord("Career Interceptions", lb.statsInts+lb.careerInts, abbr + ": " + lb.getInitialName(), league.getYear());
            }
            else if (p instanceof PlayerCB) {
                PlayerCB cb = (PlayerCB) p;
                records.checkRecord("Career Tackles", cb.statsTackles+cb.careerTackles , abbr + ": " + cb.getInitialName(), league.getYear());
                records.checkRecord("Career Sacks", cb.statsSacks+cb.careerSacks, abbr + ": " + cb.getInitialName(), league.getYear());
                records.checkRecord("Career Fumbles Rec", cb.statsFumbles+cb.careerFumbles, abbr + ": " + cb.getInitialName(), league.getYear());
                records.checkRecord("Career Interceptions", cb.statsInts+cb.careerInts, abbr + ": " + cb.getInitialName(), league.getYear());
            }
            else if (p instanceof PlayerS) {
                PlayerS s = (PlayerS) p;
                records.checkRecord("Career Tackles", s.statsTackles+s.careerTackles , abbr + ": " + s.getInitialName(), league.getYear());
                records.checkRecord("Career Sacks", s.statsSacks+s.careerSacks, abbr + ": " + s.getInitialName(), league.getYear());
                records.checkRecord("Career Fumbles Rec", s.statsFumbles+s.careerFumbles, abbr + ": " + s.getInitialName(), league.getYear());
                records.checkRecord("Career Interceptions", s.statsInts+s.careerInts, abbr + ": " + s.getInitialName(), league.getYear());
            }
        }
    }

    public void getPlayersLeaving() {
        if (playersLeaving.isEmpty()) {
            int i = 0;

            // Juniors more likely to leave if you won NCG
            double champsBonus = 0;
            if (natChampWL.equals("NCW")) {
                champsBonus += 0.2;
            }
            if (natChampWL.equals("NCL")) {
                champsBonus += 0.15;
            }
            if (natChampWL.equals("SFL || SFW")) {
                champsBonus += 0.1;
            }

            while (i < teamQBs.size()) {
                if (teamQBs.get(i).year == 4 || (teamQBs.get(i).year == 3 && teamQBs.get(i).ratOvr > NFL_OVR && Math.random() < NFL_CHANCE + champsBonus)) {
                    playersLeaving.add(teamQBs.get(i));
                }
                ++i;
            }

            i = 0;
            while (i < teamRBs.size()) {
                if (teamRBs.get(i).year == 4 || (teamRBs.get(i).year == 3 && teamRBs.get(i).ratOvr > NFL_OVR && Math.random() < NFL_CHANCE + champsBonus)) {
                    playersLeaving.add(teamRBs.get(i));
                }
                ++i;
            }

            i = 0;
            while (i < teamWRs.size()) {
                if (teamWRs.get(i).year == 4 || (teamWRs.get(i).year == 3 && teamWRs.get(i).ratOvr > NFL_OVR && Math.random() < NFL_CHANCE + champsBonus)) {
                    playersLeaving.add(teamWRs.get(i));
                }
                ++i;
            }

            i = 0;
            while (i < teamTEs.size()) {
                if (teamTEs.get(i).year == 4 || (teamTEs.get(i).year == 3 && teamTEs.get(i).ratOvr > NFL_OVR && Math.random() < NFL_CHANCE + champsBonus)) {
                    playersLeaving.add(teamTEs.get(i));
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
                if (teamOLs.get(i).year == 4 || (teamOLs.get(i).year == 3 && teamOLs.get(i).ratOvr > NFL_OVR && Math.random() < NFL_CHANCE + champsBonus)) {
                    playersLeaving.add(teamOLs.get(i));
                }
                ++i;
            }

            i = 0;
            while (i < teamDLs.size()) {
                if (teamDLs.get(i).year == 4 || (teamDLs.get(i).year == 3 && teamDLs.get(i).ratOvr > NFL_OVR && Math.random() < NFL_CHANCE + champsBonus)) {
                    playersLeaving.add(teamDLs.get(i));
                }
                ++i;
            }

            i = 0;
            while (i < teamLBs.size()) {
                if (teamLBs.get(i).year == 4 || (teamLBs.get(i).year == 3 && teamLBs.get(i).ratOvr > NFL_OVR && Math.random() < NFL_CHANCE + champsBonus)) {
                    playersLeaving.add(teamLBs.get(i));
                }
                ++i;
            }

            i = 0;
            while (i < teamCBs.size()) {
                if (teamCBs.get(i).year == 4 || (teamCBs.get(i).year == 3 && teamCBs.get(i).ratOvr > NFL_OVR && Math.random() < NFL_CHANCE + champsBonus)) {
                    playersLeaving.add(teamCBs.get(i));
                }
                ++i;
            }
            
            i = 0;
            while (i < teamSs.size()) {
                if (teamSs.get(i).year == 4 || (teamSs.get(i).year == 3 && teamSs.get(i).ratOvr > NFL_OVR && Math.random() < NFL_CHANCE + champsBonus)) {
                    playersLeaving.add(teamSs.get(i));
                }
                ++i;
            }
        }
    }

    /**
     * Advance season for players. Removes seniors and develops underclassmen.
     */
    public void advanceSeasonPlayers() {
        int qbNeeds=0, rbNeeds=0, wrNeeds=0, teNeeds=0, olNeeds=0, kNeeds=0, dlNeeds=0, lbNeeds=0, cbNeeds=0, sNeeds=0;
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
            while (i < teamTEs.size()) {
                if (teamTEs.get(i).year == 4 || (teamTEs.get(i).year == 3 && teamTEs.get(i).ratOvr > NFL_OVR && Math.random() < NFL_CHANCE)) {
                    teamTEs.remove(i);
                    teNeeds++;
                } else {
                    teamTEs.get(i).advanceSeason();
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
            while (i < teamDLs.size()) {
                if (teamDLs.get(i).year == 4 || (teamDLs.get(i).year == 3 && teamDLs.get(i).ratOvr > NFL_OVR && Math.random() < NFL_CHANCE)) {
                    teamDLs.remove(i);
                    dlNeeds++;
                } else {
                    teamDLs.get(i).advanceSeason();
                    i++;
                }
            }

            i = 0;
            while (i < teamLBs.size()) {
                if (teamLBs.get(i).year == 4 || (teamLBs.get(i).year == 3 && teamLBs.get(i).ratOvr > NFL_OVR && Math.random() < NFL_CHANCE)) {
                    teamLBs.remove(i);
                    lbNeeds++;
                } else {
                    teamLBs.get(i).advanceSeason();
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
            while (i < teamSs.size()) {
                if (teamSs.get(i).year == 4 || (teamSs.get(i).year == 3 && teamSs.get(i).ratOvr > NFL_OVR && Math.random() < NFL_CHANCE)) {
                    teamSs.remove(i);
                    sNeeds++;
                } else {
                    teamSs.get(i).advanceSeason();
                    i++;
                }
            }

            if (!userControlled) {
                recruitPlayersFreshman(qbNeeds, rbNeeds, wrNeeds, teNeeds, kNeeds, olNeeds, dlNeeds, lbNeeds, cbNeeds, sNeeds);
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
            while (i < teamTEs.size()) {
                if (playersLeaving.contains(teamTEs.get(i))) {
                    teamTEs.remove(i);
                    teNeeds++;
                } else {
                    teamTEs.get(i).advanceSeason();
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
            while (i < teamDLs.size()) {
                if (playersLeaving.contains(teamDLs.get(i))) {
                    teamDLs.remove(i);
                    dlNeeds++;
                } else {
                    teamDLs.get(i).advanceSeason();
                    i++;
                }
            }

            i = 0;
            while (i < teamLBs.size()) {
                if (playersLeaving.contains(teamLBs.get(i))) {
                    teamLBs.remove(i);
                    lbNeeds++;
                } else {
                    teamLBs.get(i).advanceSeason();
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
            while (i < teamSs.size()) {
                if (playersLeaving.contains(teamSs.get(i))) {
                    teamSs.remove(i);
                    sNeeds++;
                } else {
                    teamSs.get(i).advanceSeason();
                    i++;
                }
            }
            
            if (!userControlled) {
                recruitPlayersFreshman(qbNeeds, rbNeeds, wrNeeds, teNeeds, olNeeds, kNeeds, dlNeeds, lbNeeds, cbNeeds, sNeeds);
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
     * @param dlNeeds
     * @param teNeeds
     * @param lbNeeds
     */
    public void recruitPlayers( int qbNeeds, int rbNeeds, int wrNeeds, int teNeeds, int olNeeds, int kNeeds, int dlNeeds, int lbNeeds, int cbNeeds, int sNeeds) {
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
        
        for( int i = 0; i < teNeeds; ++i ) {
            //make TEs
            if ( 100*Math.random() < 5*chance ) {
                teamTEs.add( new PlayerTE(league.getRandName(), (int)(4*Math.random() + 1), stars-1, this) );
            } else {
                teamTEs.add( new PlayerTE(league.getRandName(), (int)(4*Math.random() + 1), stars, this) );
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

        for( int i = 0; i < dlNeeds; ++i ) {
            //make DLs
            if ( 100*Math.random() < 5*chance ) {
                teamDLs.add( new PlayerDL(league.getRandName(), (int)(4*Math.random() + 1), stars-1, this) );
            } else {
                teamDLs.add( new PlayerDL(league.getRandName(), (int)(4*Math.random() + 1), stars, this) );
            }
        }

        for( int i = 0; i < lbNeeds; ++i ) {
            //make LBs
            if ( 100*Math.random() < 5*chance ) {
                teamLBs.add( new PlayerLB(league.getRandName(), (int)(4*Math.random() + 1), stars-1, this) );
            } else {
                teamLBs.add( new PlayerLB(league.getRandName(), (int)(4*Math.random() + 1), stars, this) );
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
     * @param dlNeeds
     * @param teNeeds
     * @param lbNeeds
     */
    public void recruitPlayersFreshman( int qbNeeds, int rbNeeds, int wrNeeds, int teNeeds, int olNeeds, int kNeeds, int dlNeeds, int lbNeeds, int cbNeeds, int sNeeds) {
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

        for( int i = 0; i < teNeeds; ++i ) {
            // Add some randomness so that players with higher stars can be recruited
            stars = teamPrestige/20 + 1;
            if ( 100*Math.random() < 5*chance ) stars = stars - 1;
            if (Math.random() < starsBonusChance) stars = stars + 1;
            else if (Math.random() < starsBonusDoubleChance) stars = stars + 2;
            if (stars > 5) stars = 5;

            //make TEs
            teamTEs.add( new PlayerTE(league.getRandName(), 1, stars, this) );
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

        for( int i = 0; i < dlNeeds; ++i ) {
            // Add some randomness so that players with higher stars can be recruited
            stars = teamPrestige/20 + 1;
            if ( 100*Math.random() < 5*chance ) stars = stars - 1;
            if (Math.random() < starsBonusChance) stars = stars + 1;
            else if (Math.random() < starsBonusDoubleChance) stars = stars + 2;
            if (stars > 5) stars = 5;

            //make DLs
            teamDLs.add( new PlayerDL(league.getRandName(), 1, stars, this) );
        }

        for( int i = 0; i < lbNeeds; ++i ) {
            // Add some randomness so that players with higher stars can be recruited
            stars = teamPrestige/20 + 1;
            if ( 100*Math.random() < 5*chance ) stars = stars - 1;
            if (Math.random() < starsBonusChance) stars = stars + 1;
            else if (Math.random() < starsBonusDoubleChance) stars = stars + 2;
            if (stars > 5) stars = 5;

            //make LBs
            teamLBs.add( new PlayerLB(league.getRandName(), 1, stars, this) );
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

        needs = 2 - teamTEs.size();
        for( int i = 0; i < needs; ++i ) {
            //make TEs
            teamTEs.add( new PlayerTE(league.getRandName(), 1, 2, this) );
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

        needs = 8 - teamDLs.size();
        for( int i = 0; i < needs; ++i ) {
            //make DLs
            teamDLs.add( new PlayerDL(league.getRandName(), 1, 2, this) );
        }

        needs = 6 - teamLBs.size();
        for( int i = 0; i < needs; ++i ) {
            //make LBs
            teamLBs.add( new PlayerLB(league.getRandName(), 1, 2, this) );
        }

        needs = 6 - teamCBs.size();
        for( int i = 0; i < needs; ++i ) {
            //make Ss
            teamCBs.add( new PlayerCB(league.getRandName(), 1, 2, this) );
        }

        needs = 2 - teamSs.size();
        for( int i = 0; i < needs; ++i ) {
            //make Ss
            teamSs.add( new PlayerS(league.getRandName(), 1, 2, this) );
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
                teamQBs.add(new PlayerQB(playerInfo[1], this,
                        Integer.parseInt(playerInfo[2]), Integer.parseInt(playerInfo[3]),
                        Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5]),
                        Integer.parseInt(playerInfo[6]), Integer.parseInt(playerInfo[7]),
                         isRedshirt, durability,
                        Integer.parseInt(playerInfo[11]), Integer.parseInt(playerInfo[12]),
                        Integer.parseInt(playerInfo[13]), Integer.parseInt(playerInfo[14]),
                        Integer.parseInt(playerInfo[15]), Integer.parseInt(playerInfo[16]),
                        Integer.parseInt(playerInfo[17]), Integer.parseInt(playerInfo[18]),
                        Integer.parseInt(playerInfo[19]), Integer.parseInt(playerInfo[20]),
                        Integer.parseInt(playerInfo[21]), Integer.parseInt(playerInfo[22]),
                        Integer.parseInt(playerInfo[23]), Integer.parseInt(playerInfo[24]),
                        Integer.parseInt(playerInfo[25]), Integer.parseInt(playerInfo[26])));
            else
                teamQBs.add( new PlayerQB(playerInfo[1], this,
                                Integer.parseInt(playerInfo[2]), Integer.parseInt(playerInfo[3]),
                                Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5]),
                                Integer.parseInt(playerInfo[6]), Integer.parseInt(playerInfo[7]),
                                isRedshirt, durability, Integer.parseInt(playerInfo[11])));
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
                        Integer.parseInt(playerInfo[19]), Integer.parseInt(playerInfo[20]), Integer.parseInt(playerInfo[21])));
            else
                teamWRs.add( new PlayerWR(playerInfo[1], this,
                        Integer.parseInt(playerInfo[2]), Integer.parseInt(playerInfo[3]),
                        Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5]),
                        Integer.parseInt(playerInfo[6]), Integer.parseInt(playerInfo[7]), isRedshirt, durability));
        } else if (playerInfo[0].equals("TE")) {
            if (playerInfo.length >= 22)
                teamTEs.add( new PlayerTE(playerInfo[1], this,
                        Integer.parseInt(playerInfo[2]), Integer.parseInt(playerInfo[3]),
                        Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5]),
                        Integer.parseInt(playerInfo[6]), Integer.parseInt(playerInfo[7]), isRedshirt, durability,
                        Integer.parseInt(playerInfo[11]), Integer.parseInt(playerInfo[12]),
                        Integer.parseInt(playerInfo[13]), Integer.parseInt(playerInfo[14]),
                        Integer.parseInt(playerInfo[15]), Integer.parseInt(playerInfo[16]),
                        Integer.parseInt(playerInfo[17]), Integer.parseInt(playerInfo[18]),
                        Integer.parseInt(playerInfo[19]), Integer.parseInt(playerInfo[20]),
                        Integer.parseInt(playerInfo[21]),Integer.parseInt(playerInfo[21])));
            else
                teamTEs.add( new PlayerTE(playerInfo[1], this,
                        Integer.parseInt(playerInfo[2]), Integer.parseInt(playerInfo[3]),
                        Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5]),
                        Integer.parseInt(playerInfo[6]), Integer.parseInt(playerInfo[7]),
                        isRedshirt, durability, Integer.parseInt(playerInfo[11])));
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
        } else if (playerInfo[0].equals("DL")) {
            if (playerInfo.length >= 16)
                teamDLs.add(new PlayerDL(playerInfo[1], this,
                        Integer.parseInt(playerInfo[2]), Integer.parseInt(playerInfo[3]),
                        Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5]),
                        Integer.parseInt(playerInfo[6]), Integer.parseInt(playerInfo[7]), isRedshirt, durability,
                        Integer.parseInt(playerInfo[11]), Integer.parseInt(playerInfo[12]),
                        Integer.parseInt(playerInfo[13]), Integer.parseInt(playerInfo[14]),
                        Integer.parseInt(playerInfo[15]), Integer.parseInt(playerInfo[16]),
                        Integer.parseInt(playerInfo[17]), Integer.parseInt(playerInfo[18]), Integer.parseInt(playerInfo[19])));
         else
                teamDLs.add( new PlayerDL(playerInfo[1], this,
                        Integer.parseInt(playerInfo[2]), Integer.parseInt(playerInfo[3]),
                        Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5]),
                        Integer.parseInt(playerInfo[6]), Integer.parseInt(playerInfo[7]), isRedshirt, durability));
        } else if (playerInfo[0].equals("LB")) {
            if (playerInfo.length >= 16)
            teamLBs.add( new PlayerLB(playerInfo[1], this,
                    Integer.parseInt(playerInfo[2]), Integer.parseInt(playerInfo[3]),
                    Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5]),
                    Integer.parseInt(playerInfo[6]), Integer.parseInt(playerInfo[7]), isRedshirt, durability,
                    Integer.parseInt(playerInfo[11]), Integer.parseInt(playerInfo[12]),
                    Integer.parseInt(playerInfo[13]), Integer.parseInt(playerInfo[14]),
                    Integer.parseInt(playerInfo[15]), Integer.parseInt(playerInfo[16]),
                    Integer.parseInt(playerInfo[17]), Integer.parseInt(playerInfo[18]),
                    Integer.parseInt(playerInfo[19]), Integer.parseInt(playerInfo[20])));
        else
            teamLBs.add( new PlayerLB(playerInfo[1], this,
                    Integer.parseInt(playerInfo[2]), Integer.parseInt(playerInfo[3]),
                    Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5]),
                    Integer.parseInt(playerInfo[6]), Integer.parseInt(playerInfo[7]),
                    isRedshirt, durability, Integer.parseInt(playerInfo[11])));
        } else if (playerInfo[0].equals("CB")) {
        if (playerInfo.length >= 16)
            teamCBs.add( new PlayerCB(playerInfo[1], this,
                    Integer.parseInt(playerInfo[2]), Integer.parseInt(playerInfo[3]),
                    Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5]),
                    Integer.parseInt(playerInfo[6]), Integer.parseInt(playerInfo[7]), isRedshirt, durability,
                    Integer.parseInt(playerInfo[11]), Integer.parseInt(playerInfo[12]),
                    Integer.parseInt(playerInfo[13]), Integer.parseInt(playerInfo[14]),
                    Integer.parseInt(playerInfo[15]), Integer.parseInt(playerInfo[16]),
                    Integer.parseInt(playerInfo[17]), Integer.parseInt(playerInfo[18]), Integer.parseInt(playerInfo[19])));
        else
            teamCBs.add( new PlayerCB(playerInfo[1], this,
                    Integer.parseInt(playerInfo[2]), Integer.parseInt(playerInfo[3]),
                    Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5]),
                    Integer.parseInt(playerInfo[6]), Integer.parseInt(playerInfo[7]), isRedshirt, durability));
        } else if (playerInfo[0].equals("S")) {
            if (playerInfo.length >= 16)
                teamSs.add(new PlayerS(playerInfo[1], this,
                        Integer.parseInt(playerInfo[2]), Integer.parseInt(playerInfo[3]),
                        Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5]),
                        Integer.parseInt(playerInfo[6]), Integer.parseInt(playerInfo[7]), isRedshirt, durability,
                        Integer.parseInt(playerInfo[11]), Integer.parseInt(playerInfo[12]),
                        Integer.parseInt(playerInfo[13]), Integer.parseInt(playerInfo[14]),
                        Integer.parseInt(playerInfo[15]), Integer.parseInt(playerInfo[16]),
                        Integer.parseInt(playerInfo[17]), Integer.parseInt(playerInfo[18]), Integer.parseInt(playerInfo[19])));
            else
                teamSs.add(new PlayerS(playerInfo[1], this,
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
        for (PlayerTE p : teamTEs){
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
        for (PlayerDL p : teamDLs){
            if (p.year == 0) teamRSs.add(p);
            else if (p.year == 1) teamFRs.add(p);
            else if (p.year == 2) teamSOs.add(p);
            else if (p.year == 3) teamJRs.add(p);
            else if (p.year == 4) teamSRs.add(p);
        }
        for (PlayerLB p : teamLBs){
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
        for (PlayerS p : teamSs){
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
        gameOOCSchedule1 = null;
        gameOOCSchedule2 = null;
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
        teamPollScore = (wins*223 + 3*(teamPoints-teamOppPoints) +
                (teamYards-teamOppYards)/40 + 1*teamStrengthOfWins/4 +
                3*(preseasonBias)*(teamPrestige + getOffTalent() + getDefTalent()) +
                teamStrengthOfWins)/10;
        if ( "CC".equals(confChampion) ) {
            //bonus for winning conference
            teamPollScore += 25;
        }
        if ( "NCW".equals(natChampWL) ) {
            //bonus for winning champ game
            teamPollScore += 125;
        }
        if ( "NCL".equals(natChampWL) ) {
            //bonus for winning champ game
            teamPollScore += 25;
        }
        if ( losses == 0 ) {
            teamPollScore += 50;
        } else if ( losses == 1 ) {
            teamPollScore += 25;
        }
       if (wins == 0) {
            teamPollScore -= 10;
        }

        teamOffTalent = getOffTalent();
        teamDefTalent = getDefTalent();
    }

    /**
     * Updates team history.
     */
    public void updateTeamHistory() {
        String histYear = league.getYear() + ": #" + rankTeamPollScore + " " + name + " (" + wins + "-" + losses + ") "
                + confChampion + " " + semiFinalWL + natChampWL + " Prs: " + teamPrestige;

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
                strWins += Math.pow(totalTeamCount - g.awayTeam.rankTeamPollScore,2);
            } else {
                strWins += Math.pow(totalTeamCount - g.homeTeam.rankTeamPollScore,2);
            }
        }
        teamStrengthOfWins = strWins/50;
        for (Team t : gameWinsAgainst) {
            teamStrengthOfWins += Math.pow(t.wins,2);
        }
    }

    public void updateSOS() {
        teamSOS = 0;
        for ( int i = 0; i < 12; ++i ) {
            Game g = gameSchedule.get(i);
            if (g.homeTeam == this) {
                teamSOS += totalTeamCount - g.awayTeam.rankTeamPollScore;
            } else {
                teamSOS += totalTeamCount - g.homeTeam.rankTeamPollScore;
            }
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
        Collections.sort(teamTEs, new PlayerComparator());
        Collections.sort(teamKs, new PlayerComparator());
        Collections.sort(teamOLs, new PlayerComparator());
        Collections.sort(teamDLs, new PlayerComparator());
        Collections.sort(teamLBs, new PlayerComparator());
        Collections.sort(teamCBs, new PlayerComparator());
        Collections.sort(teamSs, new PlayerComparator());

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
            checkInjuryPosition(teamQBs, 1);
            checkInjuryPosition(teamRBs, 2);
            checkInjuryPosition(teamWRs, 3);
            checkInjuryPosition(teamTEs, 1);
            checkInjuryPosition(teamOLs, 5);
            checkInjuryPosition(teamKs, 1);
            checkInjuryPosition(teamDLs, 4);
            checkInjuryPosition(teamLBs, 3);
            checkInjuryPosition(teamCBs, 3);
            checkInjuryPosition(teamSs, 1);
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
        curePlayersPosition(teamTEs);
        curePlayersPosition(teamOLs);
        curePlayersPosition(teamKs);
        curePlayersPosition(teamDLs);
        curePlayersPosition(teamLBs);
        curePlayersPosition(teamCBs);
        curePlayersPosition(teamSs);
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
                teamRBs.get(0).ratOvr + teamRBs.get(1).ratOvr + teamTEs.get(0).ratOvr +
                getCompositeOLPass() + getCompositeOLRush() ) / 13;
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
        comp += getTE(0).ratFootIQ;
        for (int i = 0; i < 5; ++i) {
            comp += getOL(i).ratFootIQ/5;
        }
        comp += getS(0).ratFootIQ * 4;
        comp += getCB(0).ratFootIQ + getCB(1).ratFootIQ + getCB(2).ratFootIQ;
        for (int i = 0; i < 4; ++i) {
            comp += getDL(i).ratFootIQ/4;
        }
        for (int i = 0; i < 3; ++i) {
            comp += getLB(i).ratFootIQ;
        }
        return comp / 23;
    }

    /**
     * Gets the recruiting class strength.
     * Adds up all the ovrs of freshman
     * @return class strength as a number
     */
    public int getRecruitingClassRat() {
        int classStrength = 0;
        int numFreshman = 0;
        int numRedshirt = 0;
        ArrayList<Player> allPlayers = getAllPlayers();
        for (Player p : allPlayers) {
            if (p.year == 1 && p.ratOvr > 65 && !p.isRedshirt) {
                // Is freshman
                classStrength += p.ratOvr - 30;
                numFreshman++;
            }
            if (p.year == 0 && p.ratOvr > 65) {
                classStrength += p.ratOvr - 35;
                numRedshirt++;
            }
      }

        if (numFreshman > 0 || numRedshirt > 0)
            return classStrength * (classStrength/(numFreshman + numRedshirt)) / 100;
        else return 0;
    }

    public ArrayList<Player> getAllPlayers() {
        ArrayList<Player> allPlayersList = new ArrayList<>();
        allPlayersList.addAll(teamQBs);
        allPlayersList.addAll(teamRBs);
        allPlayersList.addAll(teamWRs);
        allPlayersList.addAll(teamTEs);
        allPlayersList.addAll(teamOLs);
        allPlayersList.addAll(teamKs);
        allPlayersList.addAll(teamDLs);
        allPlayersList.addAll(teamLBs);
        allPlayersList.addAll(teamCBs);
        allPlayersList.addAll(teamSs);
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

    public PlayerTE getTE(int depth) {
        if ( depth < teamTEs.size() && depth >= 0 ) {
            return teamTEs.get(depth);
        } else {
            return teamTEs.get(0);
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
    
    public PlayerDL getDL(int depth) {
        if ( depth < teamDLs.size() && depth >= 0 ) {
            return teamDLs.get(depth);
        } else {
            return teamDLs.get(0);
        }
    }
    
    public PlayerLB getLB(int depth) {
        if ( depth < teamLBs.size() && depth >= 0 ) {
            return teamLBs.get(depth);
        } else {
            return teamLBs.get(0);
        }
    }
    
    public PlayerCB getCB(int depth) {
        if ( depth < teamCBs.size() && depth >= 0 ) {
            return teamCBs.get(depth);
        } else {
            return teamCBs.get(0);
        }
    }
    
    public PlayerS getS(int depth) {
        if ( depth < teamSs.size() && depth >= 0 ) {
            return teamSs.get(depth);
        } else {
            return teamSs.get(0);
        }
    }

    
    /**
     * Get pass proficiency. The higher the more likely the team is to pass.
     * @return integer of how good the team is at passing
     */
    public int getPassProf() {
        int avgWRs = ( teamWRs.get(0).ratOvr + teamWRs.get(1).ratOvr + teamWRs.get(2).ratOvr + teamTEs.get(0).ratCatch)/4;
        return (getCompositeOLPass() + getQB(0).ratOvr*2 + avgWRs)/4;
    }

    /**
     * Get run proficiency. The higher the more likely the team is to run.
     * @return integer of how good the team is at rushing
     */
    public int getRushProf() {
        int avgRBs = ( teamRBs.get(0).ratOvr + teamRBs.get(1).ratOvr )/2;
        int QB = teamQBs.get(0).ratSpeed;
        return (3*getCompositeOLRush() + 3*avgRBs + QB)/7;
    }

    /**
     * Get how good the team is at defending the pass
     * @return integer of how good
     */
    public int getPassDef() {
        int avgCBs = ( teamCBs.get(0).ratOvr + teamCBs.get(1).ratOvr + teamCBs.get(2).ratOvr)/3;
        int avgLBs = (teamLBs.get(0).ratCoverage + teamLBs.get(1).ratCoverage + teamLBs.get(2).ratCoverage)/ 3;
        int def = (2*avgCBs + avgLBs) / 3;
        return (def*3 + teamSs.get(0).ratOvr + getCompositeDLPass()*2)/6;
    }

    /**
     * Get how good the team is at defending the rush
     * @return integer of how good
     */
    public int getRushDef() {
        return getCompositeDLRush();
    }

    /**
     * Get how good the OL is at defending the pass
     * Is the average of power and pass blocking.
     * @return how good they are at blocking the pass.
     */
    public int getCompositeOLPass() {
        int compositeOL = 0;
        for ( int i = 0; i < 5; ++i ) {
            compositeOL += (teamOLs.get(i).ratStrength + teamOLs.get(i).ratPassBlock)/2;
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
            compositeOL += (teamOLs.get(i).ratStrength + teamOLs.get(i).ratRunBlock)/2;
        }
        int compositeTE = teamTEs.get(0).ratRunBlock;
        return (2*(compositeOL) + compositeTE)/11;
    }

    /**
     * Get how good the DL is at defending the pass.
     * Is the average of power and pass pressure.
     * @return how good they are at putting pressure on passer.
     */
    public int getCompositeDLPass() {
        int compositeDL = 0;
        for ( int i = 0; i < 4; ++i ) {
            compositeDL += (teamDLs.get(i).ratStrength + teamDLs.get(i).ratPassRush)/2;
        }
        return compositeDL / 4;
    }

    /**
     * Get how good the DL is at defending the run.
     * Is the average of power and run stopping.
     * @return how good they are at stopping the RB.
     */
    public int getCompositeDLRush() {
        int compositeDL = 0;
        int compositeLB = 0;
        for ( int i = 0; i < 4; ++i ) {
            compositeDL += (teamDLs.get(i).ratStrength + teamDLs.get(i).ratRunStop)/2;
        }
        for ( int i = 0; i < 3; ++i ) {
            compositeLB += teamLBs.get(i).ratRunStop;
        }
        return (2*compositeDL + compositeLB)/11;
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

        ts0.append(teamSOS + ",");
        ts0.append("Strength of Schedule" + ",");
        ts0.append(getRankStr(rankTeamSOS) + "%\n");

        ts0.append(teamStrengthOfWins + ",");
        ts0.append("Strength of Wins" + ",");
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
        int prestigePts[] = calcSeasonPrestige();

        String summary = "Your team, " + name + ", finished the season ranked #" + rankTeamPollScore + " with " + wins + " wins and " + losses + " losses.";

        if ( natChampWL.equals("NCW") ) {
            summary += "\n\nYou won the National Championship! Recruits want to play for winners and you have proved that you are one. You gain " + prestigePts[3] + " prestige points!";
        }

        if (prestigePts[5] > 0) {
            if (wonRivalryGame) {
                summary += "\n\nCongrats on beating your rival, " + rivalTeam + "! You will gain of " + prestigePts[1] + " prestige points in the off-season!";
            } else {
                summary += "\n\nUnfortunately you lost to your rival, " + rivalTeam + "! You lose " + prestigePts[1] + " prestige points this off-season!";
            }
        }

        if (prestigePts[2] > 0) {
            summary += "\n\nSince you won your conference championship, your team gained " + prestigePts[2] + " prestige points!";
        }
        if (prestigePts[4] > 0) {
            summary += "\n\nYou have Pro caliber talent going to the draft. Since your school isn't expected to have such talents, you will see a gain of " + prestigePts[4] + " prestige points in the off-season!";
        }

        if (this == league.saveBless || this == league.saveBless2 || this == league.saveBless3 || this == league.saveBless4 || this == league.saveBless5) {
            summary += "\n\nThere was a major shake-up on your coaching staff this season. This fresh start hopefully will guide you to success in the future!";
        } else if (this == league.saveCurse || this == league.saveCurse2 || this == league.saveCurse3 || this == league.saveCurse4 || this == league.saveCurse5) {
            summary += "\n\n our team had penalties placed on it by the collegiate administration this season. Recruiting budgets were reduced due to this.";
        } else if ((prestigePts[0] - teamPrestige) > 0) {
            summary += "\n\nGreat job coach! You exceeded expectations and gained " + (prestigePts[0] - teamPrestige) + " prestige points! This will help your recruiting.";
        } else if ((prestigePts[0] - teamPrestige) < 0) {
            summary += "\n\nA bit of a down year, coach? You fell short expectations and lost " + (teamPrestige - prestigePts[0]) + " prestige points. This will hurt your recruiting.";
        } else {
            summary += "\n\nWell, your team performed exactly how many expected. This won't hurt or help recruiting, but try to improve next year!";
        }

        summary += "\n\nCurrent Prestige: " + teamPrestige + " New Prestige: " + prestigePts[0];

        return summary;
    }

/*
    public String coachingChange() {
        int rankGoal = 100 - teamPrestige;
        int x = (int)Math.random()*100;
        String returnName = name;
        if (teamPrestige <= 60 && ((rankGoal - rankTeamPollScore) < (teamPrestige/1.5)) && x > 66) {
            teamPrestige += (int)Math.random()*21;
            return "YES";
        }
        return "NO";
    }*/



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
        
        pList.add(getTE(0).getPosNameYrOvrPot_Str());

        for (int i = 0; i < 5; ++i) {
            pList.add(getOL(i).getPosNameYrOvrPot_Str());
        }

        pList.add(getK(0).getPosNameYrOvrPot_Str());
        
        for (int i = 0; i < 4; ++i) {
            pList.add(getDL(i).getPosNameYrOvrPot_Str());
        }

        for (int i = 0; i < 3; ++i) {
            pList.add(getLB(i).getPosNameYrOvrPot_Str());
        }
        
        for (int i = 0; i < 3; ++i) {
            pList.add(getCB(i).getPosNameYrOvrPot_Str());
        }
        
        pList.add(getS(0).getPosNameYrOvrPot_Str());

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
        
        ph = playerStatsGroupHeaders.get(6);
        playerStatsMap.put(ph, getTE(0).getDetailStatsList(numGames()));
        
        for (int i = 7; i < 12; ++i) {
            ph = playerStatsGroupHeaders.get(i);
            playerStatsMap.put(ph, getOL(i - 7).getDetailStatsList(numGames()));
        }

        ph = playerStatsGroupHeaders.get(12);
        playerStatsMap.put(ph, getK(0).getDetailStatsList(numGames()));
        
        for (int i = 13; i < 17; ++i) {
            ph = playerStatsGroupHeaders.get(i);
            playerStatsMap.put(ph, getDL(i - 13).getDetailStatsList(numGames()));
        }
        for (int i = 17; i < 20; ++i) {
            ph = playerStatsGroupHeaders.get(i);
            playerStatsMap.put(ph, getLB(i - 17).getDetailStatsList(numGames()));
        }
        for (int i = 20; i < 23; ++i) {
            ph = playerStatsGroupHeaders.get(i);
            playerStatsMap.put(ph, getCB(i - 20).getDetailStatsList(numGames()));
        }
        ph = playerStatsGroupHeaders.get(23);
        playerStatsMap.put(ph, getS(0).getDetailStatsList(numGames()));

        //Bench
        ph = playerStatsGroupHeaders.get(24);
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
        for ( int i = 1; i < teamTEs.size(); ++i) {
            benchStr.add( getTE(i).getPosNameYrOvrPot_Str() );
        }
        for ( int i = 5; i < teamOLs.size(); ++i) {
            benchStr.add( getOL(i).getPosNameYrOvrPot_Str() );
        }
        for ( int i = 1; i < teamKs.size(); ++i) {
            benchStr.add( getK(i).getPosNameYrOvrPot_Str() );
        }
        for ( int i = 4; i < teamDLs.size(); ++i) {
            benchStr.add( getDL(i).getPosNameYrOvrPot_Str() );
        }
        for ( int i = 3; i < teamLBs.size(); ++i) {
            benchStr.add( getLB(i).getPosNameYrOvrPot_Str() );
        }
        for ( int i = 3; i < teamCBs.size(); ++i) {
            benchStr.add( getCB(i).getPosNameYrOvrPot_Str() );
        }
        for ( int i = 1; i < teamSs.size(); ++i) {
            benchStr.add(getS(i).getPosNameYrOvrPot_Str());
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
        for (Player p : teamTEs) {
            if (p.getPosNameYrOvrPot_Str().equals(line)) return p;
        }
        for (Player p : teamOLs) {
            if (p.getPosNameYrOvrPot_Str().equals(line)) return p;
        }
        for (Player p : teamKs) {
            if (p.getPosNameYrOvrPot_Str().equals(line)) return p;
        }
        for (Player p : teamDLs) {
            if (p.getPosNameYrOvrPot_Str().equals(line)) return p;
        }
        for (Player p : teamLBs) {
            if (p.getPosNameYrOvrPot_Str().equals(line)) return p;
        }
        for (Player p : teamCBs) {
            if (p.getPosNameYrOvrPot_Str().equals(line)) return p;
        }
        for (Player p : teamSs) {
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
        return "["+ rankTeamPollScore + "]" + name;
    }
    /**
     * Str rep of team, no bowl results
     * @return ranking abbr (w-l)
     */
    public String strRep() {
        return "[" + rankTeamPollScore + "] " + abbr;
    }
    /**
     * Str rep of team, with bowl results
     * @return ranking abbr (w-l) BW
     */
    public String strRepWithBowlResults() {
        return /*"#" + rankTeamPollScore + " " + */name + " (" + wins + "-" + losses + ") " + confChampion + " " + semiFinalWL + natChampWL;
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
            return g.awayScore + " - " + g.homeScore + " at " + g.homeTeam.name + " #" + g.homeTeam.rankTeamPollScore;
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
            return "at " + g.homeTeam.name + " #" + g.homeTeam.rankTeamPollScore;
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
        needs.append((2-teamTEs.size())+ "TEs, ");
        needs.append((10-teamOLs.size())+ "OLs, ");
        needs.append("\t\t"+(2-teamKs.size())+ "Ks\n");
        needs.append((8-teamDLs.size())+ "DLs");
        needs.append((6-teamLBs.size())+ "LBs");
        needs.append((6-teamCBs.size())+ "CBs, ");
        needs.append((2-teamSs.size())+ "Ss, ");

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
        int adjNumRecruits = numRecruits;
        PlayerWR[] recruits = new PlayerWR[adjNumRecruits];
        int stars;
        for (int i = 0; i < adjNumRecruits; ++i) {
            stars = (int)(5*(float)(adjNumRecruits - i/2)/adjNumRecruits);
            recruits[i] = new PlayerWR(league.getRandName(), 1, stars, this);
        }
        Arrays.sort(recruits, new PlayerComparator());
        return recruits;
    }
    
    public PlayerTE[] getTERecruits() {
        int adjNumRecruits = 2*numRecruits;
        PlayerTE[] recruits = new PlayerTE[adjNumRecruits];
        int stars;
        for (int i = 0; i < adjNumRecruits; ++i) {
            stars = (int)(5*(float)(adjNumRecruits - i/2)/adjNumRecruits);
            recruits[i] = new PlayerTE(league.getRandName(), 1, stars, this);
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
    
    public PlayerDL[] getDLRecruits() {
        int adjNumRecruits = 3*numRecruits;
        PlayerDL[] recruits = new PlayerDL[adjNumRecruits];
        int stars;
        for (int i = 0; i < adjNumRecruits; ++i) {
            stars = (int)(5*(float)(adjNumRecruits - i/2)/adjNumRecruits);
            recruits[i] = new PlayerDL(league.getRandName(), 1, stars, this);
        }
        Arrays.sort(recruits, new PlayerComparator());
        return recruits;
    }
    
    public PlayerLB[] getLBRecruits() {
        int adjNumRecruits = 2*numRecruits;
        PlayerLB[] recruits = new PlayerLB[adjNumRecruits];
        int stars;
        for (int i = 0; i < adjNumRecruits; ++i) {
            stars = (int)(5*(float)(adjNumRecruits - i/2)/adjNumRecruits);
            recruits[i] = new PlayerLB(league.getRandName(), 1, stars, this);
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
    

    /**
     * Save all the recruits into a string to be used by RecruitingActivity
     * @return String of all the recruits
     */
    public String getRecruitsInfoSaveFile() {
        StringBuilder sb = new StringBuilder();
        PlayerQB[] qbs = getQBRecruits();
        for (PlayerQB qb : qbs) {
            sb.append("QB," + qb.name + "," + qb.year + "," + qb.ratPot + "," + qb.ratFootIQ + "," +
                    qb.ratPassPow + "," + qb.ratPassAcc + "," + qb.ratEvasion + "," + qb.ratOvr + "," + qb.cost + "," + qb.ratDur + "," + qb.ratSpeed + "%\n");
        }
        PlayerRB[] rbs = getRBRecruits();
        for (PlayerRB rb : rbs) {
            sb.append("RB," + rb.name + "," + rb.year + "," + rb.ratPot + "," + rb.ratFootIQ + "," +
                    rb.ratRushPower + "," + rb.ratSpeed + "," + rb.ratEvasion + "," + rb.ratOvr + "," + rb.cost + "," + rb.ratDur + "%\n");
        }
        PlayerWR[] wrs = getWRRecruits();
        for (PlayerWR wr : wrs) {
            sb.append("WR," + wr.name + "," + wr.year + "," + wr.ratPot + "," + wr.ratFootIQ + "," +
                    wr.ratCatch + "," + wr.ratSpeed + "," + wr.ratEvasion + "," + wr.ratOvr + "," + wr.cost + "," + wr.ratDur + "%\n");
        }
        PlayerTE[] tes = getTERecruits();
        for (PlayerTE te : tes) {
            sb.append("TE," + te.name + "," + te.year + "," + te.ratPot + "," + te.ratFootIQ + "," +
                    te.ratCatch + "," + te.ratRunBlock + "," + te.ratEvasion + "," +te.ratOvr + "," + te.cost + "," + te.ratDur  + "," + te.ratSpeed +  "%\n");
        }
        PlayerK[] ks = getKRecruits();
        for (PlayerK k : ks) {
            sb.append("K," + k.name + "," + k.year + "," + k.ratPot + "," + k.ratFootIQ + "," +
                    k.ratKickPow + "," + k.ratKickAcc + "," + k.ratKickFum + "," + k.ratOvr + "," + k.cost + "," + k.ratDur + "%\n");
        }
        PlayerOL[] ols = getOLRecruits();
        for (PlayerOL ol : ols) {
            sb.append("OL," + ol.name + "," + ol.year + "," + ol.ratPot + "," + ol.ratFootIQ + "," +
                    ol.ratStrength + "," + ol.ratRunBlock + "," + ol.ratPassBlock + "," + ol.ratOvr + "," + ol.cost + "," + ol.ratDur + "%\n");
        }
        PlayerDL[] DLs = getDLRecruits();
        for (PlayerDL DL : DLs) {
            sb.append("DL," + DL.name + "," + DL.year + "," + DL.ratPot + "," + DL.ratFootIQ + "," +
                    DL.ratStrength + "," + DL.ratRunStop + "," + DL.ratPassRush + "," + DL.ratOvr + "," + DL.cost + "," + DL.ratDur + "%\n");
        }
        PlayerLB[] lbs = getLBRecruits();
        for (PlayerLB lb : lbs) {
            sb.append("LB," + lb.name + "," + lb.year + "," + lb.ratPot + "," + lb.ratFootIQ + "," +
                    lb.ratCoverage + "," + lb.ratRunStop + "," + lb.ratTackle +"," + lb.ratOvr + "," + lb.cost + "," + lb.ratDur +  "," + lb.ratSpeed +  "%\n");
        }
        PlayerCB[] cbs = getCBRecruits();
        for (PlayerCB cb : cbs) {
            sb.append("CB," + cb.name + "," + cb.year + "," + cb.ratPot + "," + cb.ratFootIQ + "," +
                    cb.ratCoverage + "," + cb.ratSpeed + "," + cb.ratTackle + "," + cb.ratOvr + "," + cb.cost + "," + cb.ratDur + "%\n");
        }
        PlayerS[] ss = getSRecruits();
        for (PlayerS s : ss) {
            sb.append("S," + s.name + "," + s.year + "," + s.ratPot + "," + s.ratFootIQ + "," +
                    s.ratCoverage + "," + s.ratSpeed + "," + s.ratTackle + "," + s.ratOvr + "," + s.cost + "," + s.ratDur + "%\n");
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
                    qb.ratPassPow + "," + qb.ratPassAcc + "," + qb.ratEvasion + "," + qb.ratSpeed + "," + qb.ratOvr + "," + qb.ratImprovement + "," + qb.ratDur + "," +
                    qb.careerGamesPlayed + "," + qb.careerPassAtt + "," + qb.careerPassComp + "," + qb.careerTDs + "," + qb.careerInt + "," +
                    qb.careerPassYards + "," + qb.careerSacked + "," + qb.careerRushAtt + "," + qb.careerRushYards + "," + qb.careerRushTD + "," + qb.careerFumbles + "," + qb.careerHeismans +
                    "," + qb.careerAllAmerican + "," + qb.careerAllConference + "," + qb.careerWins + "%\n");
        }
        for (PlayerRB rb : teamRBs) {
            sb.append("RB," + rb.name + "," + rb.year + "," + rb.ratPot + "," + rb.ratFootIQ + "," +
                    rb.ratRushPower + "," + rb.ratSpeed + "," + rb.ratEvasion + "," + rb.ratOvr + "," + rb.ratImprovement + "," + rb.ratDur + "," +
                    rb.careerGamesPlayed + "," + rb.careerRushAtt + "," + rb.careerRushYards + "," + rb.careerTDs + "," + rb.careerFumbles + "," +
                    rb.careerHeismans + "," + rb.careerAllAmerican + "," + rb.careerAllConference + "," + rb.careerWins + "%\n");
        }
        for (PlayerWR wr : teamWRs) {
            sb.append("WR," + wr.name + "," + wr.year + "," + wr.ratPot + "," + wr.ratFootIQ + "," +
                    wr.ratCatch + "," + wr.ratSpeed + "," + wr.ratEvasion + "," + wr.ratOvr + "," + wr.ratImprovement + "," + wr.ratDur + "," +
                    wr.careerGamesPlayed + "," + wr.careerTargets + "," + wr.careerReceptions + "," + wr.careerRecYards + "," + wr.careerTD + "," +
                    wr.careerDrops + "," + wr.careerFumbles + "," + wr.careerHeismans + "," + wr.careerAllAmerican + "," + wr.careerAllConference + "," + wr.careerWins + "%\n");
        }
        for (PlayerTE te : teamTEs) {
            sb.append("TE," + te.name + "," + te.year + "," + te.ratPot + "," + te.ratFootIQ + "," +
                    te.ratCatch + "," + te.ratRunBlock + "," + te.ratEvasion + "," + te.ratSpeed + "," + te.ratOvr + "," + te.ratImprovement + "," + te.ratDur + "," +
                    te.careerGamesPlayed + "," + te.careerTargets + "," + te.careerReceptions + "," + te.careerRecYards + "," + te.careerTD + "," +
                    te.careerDrops + "," + te.careerFumbles + "," + te.careerHeismans + "," + te.careerAllAmerican + "," + te.careerAllConference + "," + te.careerWins + "%\n");
        }
        for (PlayerOL ol : teamOLs) {
            sb.append("OL," + ol.name + "," + ol.year + "," + ol.ratPot + "," + ol.ratFootIQ + "," +
                    ol.ratStrength + "," + ol.ratRunBlock + "," + ol.ratPassBlock + "," + ol.ratOvr + "," + ol.ratImprovement + "," + ol.ratDur + "," +
                    ol.careerGamesPlayed + "," + ol.careerHeismans + "," + ol.careerAllAmerican + "," + ol.careerAllConference + "," + ol.careerWins + "%\n");
        }
        for (PlayerK k : teamKs) {
            sb.append("K," + k.name + "," + k.year + "," + k.ratPot + "," + k.ratFootIQ + "," +
                    k.ratKickPow + "," + k.ratKickAcc + "," + k.ratKickFum + "," + k.ratOvr + "," + k.ratImprovement + "," + k.ratDur + "," +
                    k.careerGamesPlayed + "," + k.careerXPAtt + "," + k.careerXPMade + "," + k.careerFGAtt + "," + k.careerFGMade + "," +
                    k.careerHeismans + "," + k.careerAllAmerican + "," + k.careerAllConference + "," + k.careerWins + "%\n");
        }
        for (PlayerDL dl : teamDLs) {
            sb.append("DL," + dl.name + "," + dl.year + "," + dl.ratPot + "," + dl.ratFootIQ + "," +
                    dl.ratStrength + "," + dl.ratRunStop + "," + dl.ratPassRush + "," + dl.ratOvr + "," + dl.ratImprovement + "," + dl.ratDur + "," +
                    dl.careerGamesPlayed + "," + dl.careerTackles + "," + dl.careerSacks + "," + dl.careerFumbles + "," + dl.careerInts + "," +
                    dl.careerHeismans + "," + dl.careerAllAmerican + "," + dl.careerAllConference + "," + dl.careerWins + "%\n");
        }
        for (PlayerLB lb : teamLBs) {
            sb.append("LB," + lb.name + "," + lb.year + "," + lb.ratPot + "," + lb.ratFootIQ + "," +
                    lb.ratCoverage + "," + lb.ratRunStop + "," + lb.ratTackle + "," + lb.ratSpeed + "," + lb.ratOvr + "," + lb.ratImprovement + "," + lb.ratDur + "," +
                    lb.careerGamesPlayed + "," + lb.careerTackles + "," + lb.careerSacks + "," + lb.careerFumbles + "," + lb.careerInts + "," +
                    lb.careerHeismans + "," + lb.careerAllAmerican + "," + lb.careerAllConference + "," + lb.careerWins + "%\n");
        }
        for (PlayerCB cb : teamCBs) {
            sb.append("CB," + cb.name + "," + cb.year + "," + cb.ratPot + "," + cb.ratFootIQ + "," +
                    cb.ratCoverage + "," + cb.ratSpeed + "," + cb.ratTackle + "," + cb.ratOvr + "," + cb.ratImprovement + "," + cb.ratDur + "," +
                    cb.careerGamesPlayed + "," + cb.careerTackles + "," + cb.careerSacks + "," + cb.careerFumbles + "," + cb.careerInts + "," +
                    cb.careerHeismans + "," + cb.careerAllAmerican + "," + cb.careerAllConference + "," + cb.careerWins + "%\n");
        }
        for (PlayerS s : teamSs) {
            sb.append("S," + s.name + "," + s.year + "," + s.ratPot + "," + s.ratFootIQ + "," +
                    s.ratCoverage + "," + s.ratSpeed + "," + s.ratTackle + "," + s.ratOvr + "," + s.ratImprovement + "," + s.ratDur + "," +
                    s.careerGamesPlayed + "," + s.careerTackles + "," + s.careerSacks + "," + s.careerFumbles + "," + s.careerInts + "," +
                    s.careerHeismans + "," + s.careerAllAmerican + "," + s.careerAllConference + "," + s.careerWins + "%\n");
        }

        return sb.toString();
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
                ArrayList<PlayerTE> oldTEs = new ArrayList<>();
                oldTEs.addAll(teamTEs);
                teamTEs.clear();
                for (Player p : starters) {
                    teamTEs.add( (PlayerTE) p );
                }
                Collections.sort(teamTEs, new PlayerComparator());
                for (PlayerTE oldP : oldTEs) {
                    if (!teamTEs.contains(oldP)) teamTEs.add(oldP);
                }
                break;
            case 4:
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
            case 5:
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
            case 6:
                ArrayList<PlayerDL> oldDLs = new ArrayList<>();
                oldDLs.addAll(teamDLs);
                teamDLs.clear();
                for (Player p : starters) {
                    teamDLs.add( (PlayerDL) p );
                }
                Collections.sort(teamDLs, new PlayerComparator());
                for (PlayerDL oldP : oldDLs) {
                    if (!teamDLs.contains(oldP)) teamDLs.add(oldP);
                }
                break;
            case 7:
                ArrayList<PlayerLB> oldLBs = new ArrayList<>();
                oldLBs.addAll(teamLBs);
                teamLBs.clear();
                for (Player p : starters) {
                    teamLBs.add( (PlayerLB) p );
                }
                Collections.sort(teamLBs, new PlayerComparator());
                for (PlayerLB oldP : oldLBs) {
                    if (!teamLBs.contains(oldP)) teamLBs.add(oldP);
                }
                break;
            case 8:
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
            case 9:
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
        addGamePlayedList(teamTEs, 1, wonGame);
        addGamePlayedList(teamOLs, 5, wonGame);
        addGamePlayedList(teamKs, 1, wonGame);
        addGamePlayedList(teamDLs, 4, wonGame);
        addGamePlayedList(teamLBs, 3, wonGame);
        addGamePlayedList(teamCBs, 3, wonGame);
        addGamePlayedList(teamSs, 1, wonGame);
    }

    private void addGamePlayedList(ArrayList<? extends Player> playerList, int starters, boolean wonGame) {
        for (int i = 0; i < starters; ++i) {
            playerList.get(i).gamesPlayed++;
            if (wonGame) playerList.get(i).statsWins++;
        }
    }


    // Generate CPU Strategy
    //
    public int getCPUOffense() {
        int OP, OR;
        OP = getPassProf();
        OR = getRushProf();
        if(OP > (OR + 6)) {
            return 3;
        } else if(OP > (OR + 4)) {
            return 2;
        } else if(OR > (OP + 6) && teamQBs.get(0).ratSpeed > 75) {
            return 4;
        } else if(OR > (OR + 87)) {
            return 1;
        } else {
            return 0;
        }
    }

    public int getCPUDefense() {
        int DP, DR;
        DP = getPassProf();
        DR = getRushProf();
        if(DR > (DP + 7)) {
            return 3;
        } else if(DR > (DP + 4)) {
            return 2;
        } else if(DP > (DR + 4)) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Generate all the offense team strategies that can be selected
     * @return array of all the offense team strats
     */
    public TeamStrategy[] getTeamStrategiesOff() {
        TeamStrategy[] ts = new TeamStrategy[5];

        ts[0] = new TeamStrategy("Pro-Style",
                "Play a normal balanced offense.", 1, 0, 0, 1, 1, 0, 0, 1);

        ts[1] = new TeamStrategy("Smash Mouth",
                "Play a conservative run-heavy offense, setting up the passes as necessary.", 3, 2, -1, 1, 2, 2, 0, 0);

        ts[2] = new TeamStrategy("West Coast",
                "Passing game dictates the run game with short accurate passes.", 2, 2, 0, 0, 3, 1, -1, 1);

        ts[3] = new TeamStrategy("Spread",
                "Pass-heavy offense using many receivers with big play potential with risk.", 3, -1, 1, 0, 7, -1, 3, 1);

        ts[4] = new TeamStrategy("Read Option",
                "QB Option heavy offense, where QB options based on coverage and LB position.", 6, -1, 1, 1, 5, -1, 1, 0);


        return ts;
    }



    /**
     * Generate all the defense team strategies that can be selected
     * @return array of all the defense team strats
     */
    public TeamStrategy[] getTeamStrategiesDef() {
        TeamStrategy[] ts = new TeamStrategy[4];

        ts[0] = new TeamStrategy("4-3",
                "Play a standard 4-3 man-to-man balanced defense.", 1, 0, 0, 1, 1, 0 ,0, 1);

        ts[1] = new TeamStrategy("46 Bear Defense",
                "Focus on stopping the run. Will give up more big passing plays but will allow less runing yards and far less big plays from runing.",  2, 0, 2, 1, 1, -1 ,-1, 0);

        ts[2] = new TeamStrategy("Cover 2",
                "Play a zone defense with safety help in the back against the pass, while LBs cover the run game. ",  2, 0, -1, 1, 3, 1 ,0, 1);

        ts[3] = new TeamStrategy("Cover 3",
                "Play a zone defense to stop the big plays, but allows soft zone coverage underneath.", 3, 0, -2, 1, 7, 2 ,1, 1);

        return ts;
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
 * Comparator used to sort players by position, QB-RB-WR-OL-K-S-CB-DL
 */
class PlayerPositionComparator implements Comparator<Player> {
    @Override
    public int compare(Player a, Player b) {
        int aPos = Player.getPosNumber(a.position);
        int bPos = Player.getPosNumber(b.position);
        return aPos < bPos ? -1 : aPos == bPos ? 0 : 1;
    }
}