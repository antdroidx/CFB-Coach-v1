package Simulation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/* TEAM class

 */

public class Team {

    public League league;
    public String name;
    public String abbr;
    public String conference;
    public int location;
    public String rivalTeam;
    public boolean wonRivalryGame;
    public ArrayList<String> teamHistory;
    public ArrayList<String> userHistory;
    public ArrayList<String> hallOfFame;
    public LeagueRecords teamRecords;
    public boolean userControlled;
    public boolean showPopups;
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
    public ArrayList<Team> gameLossesAgainst;
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
    public int teamStrengthOfLosses;
    public int teamSOS;
    public int teamDiscipline;

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
    public int ratTransfer = 72;

    //prestige/talent improvements
    public int confPrestige;
    public int disciplinePts;

    public ArrayList<HeadCoach> HC;
    public boolean fired;
    public boolean newContract;
    public boolean retired;
    public String contractString;
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
    public ArrayList<PlayerDefense> teamDefense;
    //By year
    public ArrayList<Player> teamRSs;
    public ArrayList<Player> teamFRs;
    public ArrayList<Player> teamSOs;
    public ArrayList<Player> teamJRs;
    public ArrayList<Player> teamSRs;

    public int minQBs = 3;
    public int minRBs = 6;
    public int minWRs = 8;
    public int minTEs = 3;
    public int minOLs = 12;
    public int minKs = 2;
    public int minDLs = 10;
    public int minLBs = 8;
    public int minCBs = 8;
    public int minSs = 3;
    
    public int startersQB = 1;
    public int startersRB = 2;
    public int startersWR = 3;
    public int startersTE = 1;
    public int startersOL = 5;
    public int startersK = 1;
    public int startersDL = 4;
    public int startersLB = 3;
    public int startersCB = 3;
    public int startersS = 1;

    public int subQB = 0;
    public int subRB = 1;
    public int subWR = 2;
    public int subTE = 1;
    public int subOL = 2;
    public int subK = 0;
    public int subDL = 2;
    public int subLB = 1;
    public int subCB = 1;
    public int subS = 1;

    public int maxPlayers = 70;
    public int minRecruitStar = 5;
    public int maxStarRating = 10;
    int five = 84;
    int four = 78;
    int three = 68;
    int two = 58;

    public ArrayList<Player> playersLeaving;
    public ArrayList<Player> playersTransferring;
    public int dismissalChance = 3;

    public int[] recruitNeeds;
    public ArrayList<Player> playersInjured;
    public ArrayList<Player> playersRecovered;
    public ArrayList<Player> playersInjuredAll;
    public ArrayList<Player> playersSuspended;
    public ArrayList<Player> playersDis;

    public TeamStrategy teamStratOff;
    public TeamStrategy teamStratDef;
    public int teamStratOffNum;
    public int teamStratDefNum;

    public boolean confTVDeal;
    public boolean teamTVDeal;

    public HeadCoach headcoach;
    private static final int NFL_OVR = 90;
    private static final double NFL_CHANCE = 0.67;

    public int leagueOffTal;
    public int leagueDefTal;
    public int avgCP;
    public int confAvg;
    public int confLimit;

    /**
     * Creates new team, recruiting needed players and setting team stats to 0.
     *
     * @param name       name of the team
     * @param abbr       abbreviation of the team, 3 letters
     * @param conference conference the team is in
     * @param league     reference to the league object all must obey
     * @param prestige   prestige of that team, between 0-100
     */
    public Team(String name, String abbr, String conference, int prestige, String rivalTeamAbbr, int loc, League league) {
        this.league = league;
        userControlled = false;
        showPopups = true;
        teamHistory = new ArrayList<String>();
        userHistory = new ArrayList<String>();
        hallOfFame = new ArrayList<>();
        teamRecords = new LeagueRecords();
        playersInjuredAll = new ArrayList<>();
        location = loc;

        HC = new ArrayList<HeadCoach>();
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
        gameLossesAgainst = new ArrayList<Team>();
        gameWLSchedule = new ArrayList<String>();
        confChampion = "";
        semiFinalWL = "";
        natChampWL = "";

        teamPrestige = prestige;
        newRoster(minQBs, minRBs, minWRs, minTEs, minOLs, minKs, minDLs, minLBs, minCBs, minSs);

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
        disciplinePts = 0;
        teamDiscipline = getTeamDiscipline();

        teamPollScore = teamPrestige + getOffTalent() + getDefTalent();

        teamStratOff = new TeamStrategy();
        teamStratDef = new TeamStrategy();
        teamStratOffNum = getCPUOffense();
        teamStratDefNum = getCPUDefense();
        teamStratOff = getTeamStrategiesOff()[teamStratOffNum];
        teamStratDef = getTeamStrategiesDef()[teamStratDefNum];
        numRecruits = 30;
        playersLeaving = new ArrayList<>();
        playersTransferring = new ArrayList<>();
        recruitNeeds = new int[10];
        hallOfFame.add("");
    }


    /**
     * Constructor for team that is being loaded from file.
     *
     * @param loadStr String containing the team info that can be loaded
     */
    public Team(String loadStr, League league) {
        this.league = league;
        userControlled = false;
        showPopups = true;
        teamHistory = new ArrayList<String>();
        userHistory = new ArrayList<String>();
        hallOfFame = new ArrayList<>();
        teamRecords = new LeagueRecords();
        playersInjuredAll = new ArrayList<>();

        HC = new ArrayList<HeadCoach>();
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
        teamDefense = new ArrayList<PlayerDefense>();


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
        gameLossesAgainst = new ArrayList<Team>();
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
        disciplinePts = 0;
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
            location = Integer.parseInt(teamInfo[9]);
            if (teamInfo.length >= 14) {
                totalNCLosses = Integer.parseInt(teamInfo[10]);
                totalCCLosses = Integer.parseInt(teamInfo[11]);
                totalBowls = Integer.parseInt(teamInfo[12]);
                totalBowlLosses = Integer.parseInt(teamInfo[13]);
                if (teamInfo.length >= 17) {
                    teamStratOffNum = Integer.parseInt(teamInfo[13]);
                    teamStratDefNum = Integer.parseInt(teamInfo[15]);
                    showPopups = (Integer.parseInt(teamInfo[16]) == 1);
                    if (teamInfo.length >= 21) {
                        winStreak = new TeamStreak(Integer.parseInt(teamInfo[19]),
                                Integer.parseInt(teamInfo[20]),
                                Integer.parseInt(teamInfo[17]),
                                teamInfo[18]);
                        yearStartWinStreak = new TeamStreak(Integer.parseInt(teamInfo[19]),
                                Integer.parseInt(teamInfo[20]),
                                Integer.parseInt(teamInfo[17]),
                                teamInfo[18]);
                        if (teamInfo.length >= 23) {
                            teamTVDeal = Boolean.parseBoolean(teamInfo[21]);
                            confTVDeal = Boolean.parseBoolean(teamInfo[22]);
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
            if (!lines[1].split(",")[0].equals("HC")) evenYearHomeOpp = lines[1];
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
/*        teamStratOff = getTeamStrategiesOff()[teamStratOffNum];
        teamStratDef = getTeamStrategiesDef()[teamStratDefNum];*/
        teamDiscipline = getTeamDiscipline();

        numRecruits = 30;
        playersLeaving = new ArrayList<>();
        playersTransferring = new ArrayList<>();
        recruitNeeds = new int[10];
    }

    public void setupTeamBenchmark() {
        sortPlayers();
        confPrestige = league.conferences.get(league.getConfNumber(conference)).confPrestige;
        leagueOffTal = league.getAverageOffTalent();
        leagueDefTal = league.getAverageDefTalent();
        confAvg = league.averageConfPrestige();
        confLimit = (confPrestige - confAvg) / 3;
    }

    public void setupUserCoach(String name) {
        HC.get(0).name = name;
        HC.get(0).year = 0;
        HC.get(0).age = 35;
        HC.get(0).contractYear = 0;
        HC.get(0).contractLength = 6;
        HC.get(0).ratPot = 70;
        HC.get(0).ratOff = 70;
        HC.get(0).ratDef = 70;
        HC.get(0).ratTalent = 70;
        HC.get(0).ratDiscipline = 70;
        HC.get(0).ratOvr = (HC.get(0).ratOff + HC.get(0).ratDef + HC.get(0).ratTalent + HC.get(0).ratDiscipline) / 4;
        HC.get(0).offStrat = 0;
        HC.get(0).defStrat = 0;
    }

    /**
     * Recruits the needed amount of players at each position.
     * Rating of each player based on team prestige.
     * This is used when first creating a team.
     *
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
    public void newRoster(int qbNeeds, int rbNeeds, int wrNeeds, int teNeeds, int olNeeds, int kNeeds, int dlNeeds, int lbNeeds, int cbNeeds, int sNeeds) {
        //make team
        int stars = Math.round(teamPrestige / 10);
        int chance = 15;
        int num;

        for (int i = 0; i < qbNeeds; ++i) {
            //make QBs
            num = (int)(Math.random()*100);
            if (num < chance) {
                teamQBs.add(new PlayerQB(league.getRandName(), (int) (4 * Math.random() + 1), stars - 2, this));
            } else if (num > (100-chance)) {
                teamQBs.add(new PlayerQB(league.getRandName(), (int) (4 * Math.random() + 1), stars + 2, this));
            } else {
                teamQBs.add(new PlayerQB(league.getRandName(), (int) (4 * Math.random() + 1), stars, this));
            }
        }

        for (int i = 0; i < kNeeds; ++i) {
            num = (int)(Math.random()*100);
            if (num < chance) {
                teamKs.add(new PlayerK(league.getRandName(), (int) (4 * Math.random() + 1), stars - 2, this));
            } else if (num < (100 - chance)){
                teamKs.add(new PlayerK(league.getRandName(), (int) (4 * Math.random() + 1), stars + 2, this));
            } else {
                teamKs.add(new PlayerK(league.getRandName(), (int) (4 * Math.random() + 1), stars, this));
            }
        }

        for (int i = 0; i < rbNeeds; ++i) {
            //make RBs
            num = (int)(Math.random()*100);
            if (num < chance) {
                teamRBs.add(new PlayerRB(league.getRandName(), (int) (4 * Math.random() + 1), stars - 2, this));
            } else if (num > (100-chance)) {
                teamRBs.add(new PlayerRB(league.getRandName(), (int) (4 * Math.random() + 1), stars + 2, this));
            } else {
                teamRBs.add(new PlayerRB(league.getRandName(), (int) (4 * Math.random() + 1), stars, this));
            }
        }

        for (int i = 0; i < wrNeeds; ++i) {
            //make WRs
            num = (int)(Math.random()*100);
            if (num < chance) {
                teamWRs.add(new PlayerWR(league.getRandName(), (int) (4 * Math.random() + 1), stars - 2, this));
            } else if (num > (100-chance)) {
                teamWRs.add(new PlayerWR(league.getRandName(), (int) (4 * Math.random() + 1), stars + 2, this));
            } else {
                teamWRs.add(new PlayerWR(league.getRandName(), (int) (4 * Math.random() + 1), stars, this));
            }
        }

        for (int i = 0; i < teNeeds; ++i) {
            //make TEs
            num = (int)(Math.random()*100);
            if (num < chance) {
                teamTEs.add(new PlayerTE(league.getRandName(), (int) (4 * Math.random() + 1), stars - 2, this));
            } else if (num > (100-chance)) {
                teamTEs.add(new PlayerTE(league.getRandName(), (int) (4 * Math.random() + 1), stars + 2, this));
            } else {
                teamTEs.add(new PlayerTE(league.getRandName(), (int) (4 * Math.random() + 1), stars, this));
            }
        }

        for (int i = 0; i < olNeeds; ++i) {
            //make OLs
            num = (int)(Math.random()*100);
            if (num < chance) {
                teamOLs.add(new PlayerOL(league.getRandName(), (int) (4 * Math.random() + 1), stars - 2, this));
            } else if (num > (100-chance)){
                teamOLs.add(new PlayerOL(league.getRandName(), (int) (4 * Math.random() + 1), stars + 2, this));
            } else {
                teamOLs.add(new PlayerOL(league.getRandName(), (int) (4 * Math.random() + 1), stars, this));
            }
        }

        for (int i = 0; i < cbNeeds; ++i) {
            //make CBs
            num = (int)(Math.random()*100);
            if (num < chance) {
                teamCBs.add(new PlayerCB(league.getRandName(), (int) (4 * Math.random() + 1), stars - 2, this));
            } else if (num > (100-chance)) {
                teamCBs.add(new PlayerCB(league.getRandName(), (int) (4 * Math.random() + 1), stars + 2, this));
            } else {
                teamCBs.add(new PlayerCB(league.getRandName(), (int) (4 * Math.random() + 1), stars, this));
            }
        }

        for (int i = 0; i < dlNeeds; ++i) {
            //make DLs
            num = (int)(Math.random()*100);
            if (num < chance) {
                teamDLs.add(new PlayerDL(league.getRandName(), (int) (4 * Math.random() + 1), stars - 2, this));
            } else if (num > (100-chance)) {
                teamDLs.add(new PlayerDL(league.getRandName(), (int) (4 * Math.random() + 1), stars + 2, this));
            } else {
                teamDLs.add(new PlayerDL(league.getRandName(), (int) (4 * Math.random() + 1), stars, this));
            }
        }

        for (int i = 0; i < lbNeeds; ++i) {
            //make LBs
            num = (int)(Math.random()*100);
            if (num < chance) {
                teamLBs.add(new PlayerLB(league.getRandName(), (int) (4 * Math.random() + 1), stars - 2, this));
            } else if (num > (100-chance)){
                teamLBs.add(new PlayerLB(league.getRandName(), (int) (4 * Math.random() + 1), stars + 2, this));
            } else {
                teamLBs.add(new PlayerLB(league.getRandName(), (int) (4 * Math.random() + 1), stars, this));
            }
        }

        for (int i = 0; i < sNeeds; ++i) {
            //make Ss
            num = (int)(Math.random()*100);
            if (100 * Math.random() < 5 * chance) {
                teamSs.add(new PlayerS(league.getRandName(), (int) (4 * Math.random() + 1), stars - 2, this));
            } else if (num > (100-chance)) {
                teamSs.add(new PlayerS(league.getRandName(), (int) (4 * Math.random() + 1), stars + 2, this));
            } else {
                teamSs.add(new PlayerS(league.getRandName(), (int) (4 * Math.random() + 1), stars, this));
            }
        }

        //MAKE HEAD COACH
        int coachNum = 100*(int)Math.random();
        if (coachNum < 20) {
            HC.add(new HeadCoach(league.getRandName(), (int) (4 * Math.random() + 1), stars - 2, this));
        } else if (coachNum > 80) {
            HC.add(new HeadCoach(league.getRandName(), (int) (4 * Math.random() + 1), stars + 2, this));
        } else {
            HC.add(new HeadCoach(league.getRandName(), (int) (4 * Math.random() + 1), stars, this));
        }

        //done making players, sort them
        sortPlayers();
    }

    public void redshirtPlayers() {
        int redshirts = 0;
        int redshirtMax = HC.get(0).ratTalent/12;
        sortPlayers();
        groupPlayerStandingCSV();
        Collections.sort(teamFRs, new CompPlayer());


        for (Player p : teamFRs) {
            if (p instanceof PlayerQB) {
                for (int i = 0; i < teamQBs.size(); ++i) {
                    if (teamQBs.get(i) == p) {
                        if (i > 2 * startersQB && redshirts < redshirtMax) {
                            p.year = 0;
                            p.isRedshirt = true;
                            redshirts++;
                        }
                    }
                }
            }
            if (p instanceof PlayerRB) {
                for (int i = 0; i < teamRBs.size(); ++i) {
                    if (teamRBs.get(i) == p) {
                        if (i > 2 * startersRB && redshirts < redshirtMax) {
                            p.year = 0;
                            p.isRedshirt = true;
                            redshirts++;
                        }
                    }
                }
            }
            if (p instanceof PlayerWR) {
                for (int i = 0; i < teamWRs.size(); ++i) {
                    if (teamWRs.get(i) == p) {
                        if (i > 2 * startersWR && redshirts < redshirtMax) {
                            p.year = 0;
                            p.isRedshirt = true;
                            redshirts++;
                        }
                    }
                }
            }
            if (p instanceof PlayerTE) {
                for (int i = 0; i < teamTEs.size(); ++i) {
                    if (teamTEs.get(i) == p) {
                        if (i > 2 * startersTE && redshirts < redshirtMax) {
                            p.year = 0;
                            p.isRedshirt = true;
                            redshirts++;
                        }
                    }
                }
            }
            if (p instanceof PlayerOL) {
                for (int i = 0; i < teamOLs.size(); ++i) {
                    if (teamOLs.get(i) == p) {
                        if (i > 2 * startersOL && redshirts < redshirtMax) {
                            p.year = 0;
                            p.isRedshirt = true;
                            redshirts++;
                        }
                    }
                }
            }
            if (p instanceof PlayerK) {
                for (int i = 0; i < teamKs.size(); ++i) {
                    if (teamKs.get(i) == p) {
                        if (i > 2 * startersK && redshirts < redshirtMax) {
                            p.year = 0;
                            p.isRedshirt = true;
                            redshirts++;
                        }
                    }
                }
            }
            if (p instanceof PlayerDL) {
                for (int i = 0; i < teamDLs.size(); ++i) {
                    if (teamDLs.get(i) == p) {
                        if (i > 2 * startersDL && redshirts < redshirtMax) {
                            p.year = 0;
                            p.isRedshirt = true;
                            redshirts++;
                        }
                    }
                }
            }
            if (p instanceof PlayerLB) {
                for (int i = 0; i < teamLBs.size(); ++i) {
                    if (teamLBs.get(i) == p) {
                        if (i > 2 * startersLB && redshirts < redshirtMax) {
                            p.year = 0;
                            p.isRedshirt = true;
                            redshirts++;
                        }
                    }
                }
            }
            if (p instanceof PlayerCB) {
                for (int i = 0; i < teamCBs.size(); ++i) {
                    if (teamCBs.get(i) == p) {
                        if (i > 2 * startersCB && redshirts < redshirtMax) {
                            p.year = 0;
                            p.isRedshirt = true;
                            redshirts++;
                        }
                    }
                }
            }
            if (p instanceof PlayerS) {
                for (int i = 0; i < teamSs.size(); ++i) {
                    if (teamSs.get(i) == p) {
                        if (i > 2 * startersS && redshirts < redshirtMax) {
                            p.year = 0;
                            p.isRedshirt = true;
                            redshirts++;
                        }
                    }
                }
            }
        }

        recruitWalkOns();
    }

    public void newCustomHeadCoach(String coachName, int stars) {
        HC.add(new HeadCoach(coachName, 1, stars, this));
    }
    
    /**
     * Sorts players so that best players are higher in depth chart.
     */
    public void sortPlayers() {
        //sort players based on overall ratings to assemble best starting lineup
        Collections.sort(teamQBs, new CompPlayer());
        Collections.sort(teamRBs, new CompPlayer());
        Collections.sort(teamWRs, new CompPlayer());
        Collections.sort(teamTEs, new CompPlayer());
        Collections.sort(teamKs, new CompPlayer());
        Collections.sort(teamOLs, new CompPlayer());
        Collections.sort(teamDLs, new CompPlayer());
        Collections.sort(teamLBs, new CompPlayer());
        Collections.sort(teamCBs, new CompPlayer());
        Collections.sort(teamSs, new CompPlayer());

        Collections.sort(teamRSs, new CompPlayer());
        Collections.sort(teamFRs, new CompPlayer());
        Collections.sort(teamSOs, new CompPlayer());
        Collections.sort(teamJRs, new CompPlayer());
        Collections.sort(teamSRs, new CompPlayer());
    }

    /**
     * Updates poll score based on team stats.
     */
    public void updatePollScore() {
        updateStrengthOfWins();
        updateLossStrength();
        int offRating = offenseRating();
        int defRating = defenseRating();
        teamOffTalent = getOffTalent();
        teamDefTalent = getDefTalent();

        int preseasonBias = 6 - (wins + losses);
        if (preseasonBias < 0) preseasonBias = 0;
        teamPollScore =
                preseasonBias * (teamOffTalent + teamDefTalent + (teamPrestige/2) + (confPrestige/3)) +
                        (offRating + defRating + teamStrengthOfWins - teamStrengthOfLosses +
                        1000)/2;

        if ("CC".equals(confChampion)) {
            //bonus for winning conference
            teamPollScore += 25;
        }
        if ("NCW".equals(natChampWL)) {
            //bonus for winning champ game
            teamPollScore += 125;
        }
        if ("NCL".equals(natChampWL)) {
            //bonus for winning champ game
            teamPollScore += 35;
        }
    }

    public void updateSOS() {
        teamSOS = 0;
        for (int i = 0; i < 12; ++i) {
            Game g = gameSchedule.get(i);
            if (g.homeTeam == this) {
                teamSOS += totalTeamCount - g.awayTeam.rankTeamPollScore;
            } else {
                teamSOS += totalTeamCount - g.homeTeam.rankTeamPollScore;
            }
        }
    }

    /**
     * Updates strength of wins based on how opponents have fared.
     */
    public void updateStrengthOfWins() {
        teamStrengthOfWins = 0;
        int test = 0;
        for (int i = 0;  i < gameWinsAgainst.size(); ++i) {
            teamStrengthOfWins += (league.countTeam - gameWinsAgainst.get(i).rankTeamPollScore);
            }
    }


    public void updateLossStrength() {
        teamStrengthOfLosses = 0;
        for (int i = 0; i < gameLossesAgainst.size(); ++i) {
            teamStrengthOfLosses += gameLossesAgainst.get(i).rankTeamPollScore;
        }
    }

    public int offenseRating() {
        int offRating = 0;
        offRating = (league.countTeam - rankTeamPoints) + (league.countTeam - rankTeamYards);
        return offRating;
    }

    public int defenseRating() {
        int defRating = 0;
        defRating = (league.countTeam - rankTeamOppPoints) + (league.countTeam - rankTeamOppYards);
        return defRating;
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
     * Calculates offensive talent level of team.
     *
     * @return Offensive Talent Level
     */
    public int getOffTalent() {
        return (getQB(0).ratOvr * 5 +
                teamWRs.get(0).ratOvr + teamWRs.get(1).ratOvr + teamWRs.get(2).ratOvr +
                teamRBs.get(0).ratOvr + teamRBs.get(1).ratOvr + teamTEs.get(0).ratOvr +
                getCompositeOLPass() + getCompositeOLRush() + getOffSubTalent()) / 14;
    }
    
    public int getOffSubTalent() {
        return ((getQB(1).ratOvr + getRB(2).ratOvr + getWR(3).ratOvr + getWR(4).ratOvr + getTE(1).ratOvr + getOL(5).ratOvr + getOL(6).ratOvr)/7);
    }

    /**
     * Calculates defensive talent level of team.
     *
     * @return Defensive Talent Level
     */
    public int getDefTalent() {
        return (2*getRushDef() + 2*getPassDef() + getDefSubTalent()) / 5;
    }

    public int getDefSubTalent() {
        return ( (getDL(4).ratOvr + getDL(5).ratOvr + getLB(3).ratOvr + getCB(4).ratOvr + getCB(5).ratOvr + getS(1).ratOvr) / 6);
    }

    /**
     * Get the composite Football IQ of the team. Is used in game simulation.
     *
     * @return football iq of the team
     */
    public int getCompositeFootIQ() {
        int comp = 0;
        //Offense: 16
        comp += getQB(0).ratFootIQ * 5; //5
        comp += getRB(0).ratFootIQ + getRB(1).ratFootIQ; //2
        comp += getWR(0).ratFootIQ + getWR(1).ratFootIQ + getWR(2).ratFootIQ; //3
        comp += getTE(0).ratFootIQ; //1
        for (int i = 0; i < 5; ++i) { //5
            comp += getOL(i).ratFootIQ;
        }
        //Defense: 14
        comp += getS(0).ratFootIQ * 4; //4
        comp += getCB(0).ratFootIQ + getCB(1).ratFootIQ + getCB(2).ratFootIQ; //3
        for (int i = 0; i < 4; ++i) { //4
            comp += getDL(i).ratFootIQ;
        }
        for (int i = 0; i < 3; ++i) { //3
            comp += getLB(i).ratFootIQ;
        }

        //coach: 8
        comp += HC.get(0).ratDef * 4 + HC.get(0).ratOff * 4; //8

        //subs: 1
        comp += (getRB(2).ratFootIQ + getWR(3).ratFootIQ + getWR(4).ratFootIQ + getTE(1).ratFootIQ + getOL(5).ratFootIQ + getOL(6).ratFootIQ +
                getDL(4).ratFootIQ + getDL(5).ratFootIQ + getLB(3).ratFootIQ + getCB(4).ratFootIQ + 2*getS(1).ratFootIQ) / 12;

        return comp / 39;
    }

    /**
     * team disicipline rating
     */
    public int getTeamDiscipline() {
        int rating = 0;
        ArrayList<Player> roster = getAllPlayers();
        for (int i = 0; i < roster.size(); ++i) {
            rating += roster.get(i).personality;
        }
        return rating / roster.size();
    }


    /**
     * Get pass proficiency. The higher the more likely the team is to pass.
     *
     * @return integer of how good the team is at passing
     */
    public int getPassProf() {
        int avgWRs = (teamWRs.get(0).ratOvr + teamWRs.get(1).ratOvr + teamWRs.get(2).ratOvr + teamTEs.get(0).ratCatch) / 4;
        int avgSubs = (2*getWR(3).ratCatch + getTE(1).ratCatch + getRB(0).ratCatch + getRB(1).ratCatch + getRB(2).ratCatch)/ 6;

        return (2*getCompositeOLPass() + getQB(0).ratOvr*5 + avgWRs*4 + HC.get(0).ratOff*2 + avgSubs) / 14;
    }

    /**
     * Get run proficiency. The higher the more likely the team is to run.
     *
     * @return integer of how good the team is at rushing
     */
    public int getRushProf() {
        int avgRBs = (teamRBs.get(0).ratOvr + teamRBs.get(1).ratOvr) / 2;
        int QB = teamQBs.get(0).ratSpeed;
        int avgSub = getRB(2).ratOvr;

        return (3*getCompositeOLRush() + 4*avgRBs + QB + 2*HC.get(0).ratOff + avgSub) / 11;
    }

    /**
     * Get how good the team is at defending the pass
     *
     * @return integer of how good
     */
    public int getPassDef() {
        int avgCBs = (teamCBs.get(0).ratOvr + teamCBs.get(1).ratOvr + teamCBs.get(2).ratOvr) / 3;
        int avgLBs = (teamLBs.get(0).ratCoverage + teamLBs.get(1).ratCoverage + teamLBs.get(2).ratCoverage) / 3;
        int S = (teamSs.get(0).ratCoverage);
        int def = (3 * avgCBs + avgLBs + S) / 5;
        int avgSub = (getLB(3).ratCoverage + getCB(3).ratOvr*2 + getS(1).ratCoverage) / 4;


        return (def*4 + teamSs.get(0).ratOvr + getCompositeDLPass()*2 + 2*HC.get(0).ratDef + avgSub) / 10;
    }

    /**
     * Get how good the team is at defending the rush
     *
     * @return integer of how good
     */
    public int getRushDef() {
        return getCompositeDLRush();
    }

    /**
     * Get how good the OL is at defending the pass
     * Is the average of power and pass blocking.
     *
     * @return how good they are at blocking the pass.
     */
    public int getCompositeOLPass() {
        int compositeOL = 0;
        for (int i = 0; i < 5; ++i) {
            compositeOL += (teamOLs.get(i).ratStrength * 2 + teamOLs.get(i).ratPassBlock * 2 + teamOLs.get(i).ratAwareness) / 5;
        }
        compositeOL = compositeOL / 5;
        int avgSub = (getOL(5).ratOvr + getOL(6).ratOvr)/2;

        return (9*compositeOL + avgSub + 3*HC.get(0).ratOff) / 13;
    }

    /**
     * Get how good the OL is at defending the rush
     * Is the average of power and rush blocking.
     *
     * @return how good they are at blocking the rush.
     */
    public int getCompositeOLRush() {
        int compositeOL = 0;
        for (int i = 0; i < 5; ++i) {
            compositeOL += (teamOLs.get(i).ratStrength * 2 + teamOLs.get(i).ratRunBlock * 2 + teamOLs.get(i).ratAwareness) / 5;
        }
        compositeOL = compositeOL / 5;
        int compositeTE = teamTEs.get(0).ratRunBlock;

        int avgSub = (2*getOL(5).ratOvr + 2*getOL(6).ratOvr + getTE(1).ratRunBlock)/5;

        return (9*compositeOL + 2*compositeTE + 3*HC.get(0).ratOff + avgSub) / 15;
    }

    /**
     * Get how good the DL is at defending the pass.
     * Is the average of power and pass pressure.
     *
     * @return how good they are at putting pressure on passer.
     */
    public int getCompositeDLPass() {
        int compositeDL = 0;
        for (int i = 0; i < 4; ++i) {
            compositeDL += (teamDLs.get(i).ratStrength + teamDLs.get(i).ratPassRush) / 2;
        }
        compositeDL = compositeDL / 4;

        int avgSub = getDL(4).ratOvr + getDL(5).ratOvr;

        return (5*compositeDL + 2*HC.get(0).ratDef + avgSub) / 8;
    }

    /**
     * Get how good the DL is at defending the run.
     * Is the average of power and run stopping.
     *
     * @return how good they are at stopping the RB.
     */
    public int getCompositeDLRush() {
        int compositeDL = 0;
        int compositeLB = 0;
        int compositeS = 0;

        for (int i = 0; i < 4; ++i) {
            compositeDL += (teamDLs.get(i).ratStrength + teamDLs.get(i).ratRunStop) / 2;
        }
        compositeDL = compositeDL / 4;

        for (int i = 0; i < 3; ++i) {
            compositeLB += teamLBs.get(i).ratRunStop;
        }
        compositeLB = compositeLB / 3;

        compositeS += teamSs.get(0).ratRunStop;

        int avgSub = (2*getDL(4).ratOvr + 2*getDL(5).ratOvr + getLB(3).ratRunStop + getS(1).ratRunStop) / 6;
        return (4*compositeDL + 2*compositeLB + 2*compositeS + 2*HC.get(0).ratDef + avgSub) / 11;
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
            if (g.gameName.equals("Rivalry Game")) {
                rgameplayed = 1;
            }
        }

        // Don't add/subtract prestige if they are a blessed/cursed team from last season
        if (this != league.savePenalized && this != league.savePenalized2 && this != league.savePenalized3) {

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

        newPrestige += disciplinePts;

        //Sets the bounds for Prestige
        int confAvg = league.averageConfPrestige();
        int confLimit = (confPrestige - confAvg) / 3;
        if (newPrestige > 85 + confLimit) newPrestige = 85 + confLimit;
        if (newPrestige < 25 + confLimit) newPrestige = 25 + confLimit;
        if (newPrestige > 95) newPrestige = 95;
        if (newPrestige < 20) newPrestige = 20;

        int PrestigeScore[] = {newPrestige, rivalryPts, ccPts, ncwPts, nflPts, rgameplayed};
        return PrestigeScore;
    }


    /**
     * Advance season, hiring new coach if needed and calculating new prestige level.
     */
    public void advanceSeason() {
        int newPrestige[] = calcSeasonPrestige();
        teamPrestige = newPrestige[0];

        advanceSeasonPlayers();

        checkHallofFame();

        checkCareerRecords(league.leagueRecords);
        checkCareerRecords(teamRecords);
        if (league.userTeam == this) checkCareerRecords(league.userTeamRecords);
    }

    // OFF-SEASON HEAD COACH PROGRESSION
    // CAN BE FIRED OR EXTENDED CONTRACT HERE
    //
    public void advanceHC(LeagueRecords records, LeagueRecords teamRecords) {
        newContract = false;
        fired = false;
        retired = false;
        int newPrestige[] = calcSeasonPrestige();
        int avgOff = league.getAverageYards();

        int totalPDiff = newPrestige[0] - HC.get(0).baselinePrestige;
        HC.get(0).advanceSeason(totalPDiff, avgOff, leagueOffTal, leagueDefTal);

        teamRecords.checkRecord("Wins", HC.get(0).wins, abbr + ": " + HC.get(0).getInitialName(), league.getYear());
        teamRecords.checkRecord("National Championships", HC.get(0).natchamp, abbr + ": " + HC.get(0).getInitialName(), league.getYear());
        teamRecords.checkRecord("Conf Championships", HC.get(0).natchamp, abbr + ": " + HC.get(0).getInitialName(), league.getYear());
        teamRecords.checkRecord("Bowl Wins", HC.get(0).bowlwins, abbr + ": " + HC.get(0).getInitialName(), league.getYear());
        teamRecords.checkRecord("Coach Awards", HC.get(0).awards, abbr + ": " + HC.get(0).getInitialName(), league.getYear());

        records.checkRecord("Wins", HC.get(0).wins, abbr + ": " + HC.get(0).getInitialName(), league.getYear());
        records.checkRecord("National Championships", HC.get(0).natchamp, abbr + ": " + HC.get(0).getInitialName(), league.getYear());
        teamRecords.checkRecord("Conf Championships", HC.get(0).natchamp, abbr + ": " + HC.get(0).getInitialName(), league.getYear());
        records.checkRecord("Bowl Wins", HC.get(0).bowlwins, abbr + ": " + HC.get(0).getInitialName(), league.getYear());
        records.checkRecord("Coach Awards", HC.get(0).awards, abbr + ": " + HC.get(0).getInitialName(), league.getYear());

        coachContracts(totalPDiff, newPrestige[0], confLimit, avgCP);

        if (userControlled && totalPDiff > 2) {
            HC.get(0).promotionCandidate = true;
        }

    }



    public void coachContracts(int totalPDiff, int newPrestige, int confLimit, int avgCP) {
        int max = 78;
        int min = 63;
        Random rand = new Random();
        int retire = rand.nextInt((max - min) + 1) + min;

        //RETIREMENT
        if (HC.get(0).age > retire && !userControlled) {
            retired = true;
            String oldCoach = HC.get(0).name;
            int age = HC.get(0).age;
            fired = true;
            HC.remove(0);
            league.newsStories.get(league.currentWeek + 1).add(name + " Coaching Retirement>" + oldCoach + " has announced his retirement at the age of " + age +
                    ". His former team, " + name + " have not announced a new successor to replace the retired coach.");
        }

        if (!retired) {
            if (teamPrestige > (HC.get(0).baselinePrestige + 7) && teamPrestige < 76 && !userControlled && HC.get(0).age < 55 || teamPrestige > (HC.get(0).baselinePrestige + 7) && confPrestige < avgCP && teamPrestige < 80 && !userControlled && HC.get(0).age < 55) {
                league.newsStories.get(league.currentWeek + 1).add("Coaching Carousel Rumor Mill>After another successful season at " + name + ", head coach " + HC.get(0).name + " has moved to the top of" +
                        " many of the schools looking for a replacement at that position.");
                if (Math.random() > 0.50) {
                    league.coachStarList.add(HC.get(0));
                    league.coachStarPrevTeam.add(name + "," + teamPrestige + "," + confPrestige);
                }
            }
            //New Contracts or Firing
            if ((HC.get(0).contractYear) == HC.get(0).contractLength || natChampWL.equals("NCW") || natChampWL.equals("NCL") || (HC.get(0).contractYear + 1 == HC.get(0).contractLength && Math.random() < 0.33)) {
                if (totalPDiff > 15 || (natChampWL.equals("NCW"))) {
                    HC.get(0).contractLength = 6;
                    HC.get(0).contractYear = 0;
                    HC.get(0).baselinePrestige = (HC.get(0).baselinePrestige + 2 * teamPrestige) / 3;
                    newContract = true;
                    league.newsStories.get(league.currentWeek + 1).add("Long-Term Extension!>" + name + " has extended their head coach, " + HC.get(0).name +
                            " for 6 additional seasons for his successful tenue at the university.");
                } else if (totalPDiff > 10) {
                    HC.get(0).contractLength = 5;
                    HC.get(0).contractYear = 0;
                    HC.get(0).baselinePrestige = (HC.get(0).baselinePrestige + 2 * teamPrestige) / 3;
                    newContract = true;
                    league.newsStories.get(league.currentWeek + 1).add("New 5-Year Contract Awarded!>" + name + " has extended their head coach, " + HC.get(0).name +
                            " for 5 additional seasons for his successful tenue at the university.");
                } else if (totalPDiff > 7) {
                    HC.get(0).contractLength = 4;
                    HC.get(0).contractYear = 0;
                    HC.get(0).baselinePrestige = (HC.get(0).baselinePrestige + 2 * teamPrestige) / 3;
                    newContract = true;
                } else if (totalPDiff > 5 || (natChampWL.equals("NCL"))) {
                    if ((natChampWL.equals("NCL")) && HC.get(0).contractLength - HC.get(0).contractYear > 2) {

                    } else {
                        HC.get(0).contractLength = 3;
                        HC.get(0).contractYear = 0;
                        HC.get(0).baselinePrestige = (HC.get(0).baselinePrestige + 2 * teamPrestige) / 3;
                        newContract = true;
                    }
                } else if (totalPDiff < (0 - (HC.get(0).baselinePrestige / 10)) && newPrestige < (85+confLimit*.85) && !league.isCareerMode() && !userControlled) {
                    fired = true;
                    league.newsStories.get(league.currentWeek + 1).add("Coach Firing at " + name + ">" + name + " has fired their head coach, " + HC.get(0).name +
                            " after a disappointing tenure. The team is now searching for a new head coach.");
                    league.coachList.add(HC.get(0));
                    league.coachPrevTeam.add(name);
                    HC.remove(0);
                } else if (totalPDiff < (0 - (HC.get(0).baselinePrestige / 10)) && newPrestige < (85+confLimit*.85) && league.isCareerMode()) {
                    fired = true;
                    league.newsStories.get(league.currentWeek + 1).add("Coach Firing at " + name + ">" + name + " has fired their head coach, " + HC.get(0).name +
                            " after a disappointing tenure. The team is now searching for a new head coach.");
                    league.coachList.add(HC.get(0));
                    league.coachPrevTeam.add(name);
                    HC.remove(0);
                } else {
                    HC.get(0).contractLength = 2;
                    HC.get(0).contractYear = 0;
                    HC.get(0).baselinePrestige = (2 * HC.get(0).baselinePrestige + teamPrestige) / 3;
                    newContract = true;
                }
            }
        }
        if (userControlled) {
            if (newContract) {
                contractString = "Congratulations! You've been award with a new contract extension for " + HC.get(0).contractLength + " years!";
            } else if (fired) {
                contractString = "Due to your poor performance as head coach, the Athletic Director has terminated your contract and you are no longer Head Coach of this school.";
            } else {
                int[] newPres = calcSeasonPrestige();
                contractString = "You have " + (HC.get(0).contractLength - HC.get(0).contractYear)
                        + " years left on your contract. Your team prestige is currently at " + newPres[0] + " and your baseline " +
                        "prestige was " + HC.get(0).baselinePrestige;
            }
        }

    }

    public void promoteCoach() {
        //make team
        boolean promote = true;
        int stars = teamPrestige / 20 + 1;
        int chance = 20 - (teamPrestige - 20 * (teamPrestige / 20)); //between 0 and 20

        //MAKE HEAD COACH
        if (100 * Math.random() < 5 * chance) {
            HC.add(new HeadCoach(league.getRandName(), (int) (4 * Math.random() + 1), stars - 1, this, promote));
        } else {
            HC.add(new HeadCoach(league.getRandName(), (int) (4 * Math.random() + 1), stars, this, promote));
        }

        //done making players, sort them
        sortPlayers();
    }

    /**
     * Advance season for players. Removes seniors and develops underclassmen.
     */
    public void advanceSeasonPlayers() {

        int i = 0;
        while (i < teamQBs.size()) {
            if (teamQBs.get(i).year == 4 && !teamQBs.get(i).isTransfer || (teamQBs.get(i).year == 3 && teamQBs.get(i).ratOvr > NFL_OVR && Math.random() < NFL_CHANCE)) {
                playersLeaving.add(teamQBs.get(i));
                teamQBs.remove(i);
            } else {
                teamQBs.get(i).advanceSeason();
                i++;
            }
        }

        i = 0;
        while (i < teamRBs.size()) {
            if (teamRBs.get(i).year == 4  && !teamRBs.get(i).isTransfer || (teamRBs.get(i).year == 3 && teamRBs.get(i).ratOvr > NFL_OVR && Math.random() < NFL_CHANCE)) {
                playersLeaving.add(teamRBs.get(i));
                teamRBs.remove(i);
            } else {
                teamRBs.get(i).advanceSeason();
                i++;
            }
        }

        i = 0;
        while (i < teamWRs.size()) {
            if (teamWRs.get(i).year == 4  && !teamWRs.get(i).isTransfer || (teamWRs.get(i).year == 3 && teamWRs.get(i).ratOvr > NFL_OVR && Math.random() < NFL_CHANCE)) {
                playersLeaving.add(teamWRs.get(i));
                teamWRs.remove(i);
            } else {
                teamWRs.get(i).advanceSeason();
                i++;
            }
        }

        i = 0;
        while (i < teamTEs.size()) {
            if (teamTEs.get(i).year == 4  && !teamTEs.get(i).isTransfer || (teamTEs.get(i).year == 3 && teamTEs.get(i).ratOvr > NFL_OVR && Math.random() < NFL_CHANCE)) {
                playersLeaving.add(teamTEs.get(i));
                teamTEs.remove(i);
            } else {
                teamTEs.get(i).advanceSeason();
                i++;
            }
        }

        i = 0;
        while (i < teamOLs.size()) {
            if (teamOLs.get(i).year == 4  && !teamOLs.get(i).isTransfer || (teamOLs.get(i).year == 3 && teamOLs.get(i).ratOvr > NFL_OVR && Math.random() < NFL_CHANCE)) {
                playersLeaving.add(teamOLs.get(i));
                teamOLs.remove(i);
            } else {
                teamOLs.get(i).advanceSeason();
                i++;
            }
        }

        i = 0;
        while (i < teamKs.size()) {
            if (teamKs.get(i).year == 4 && !teamKs.get(i).isTransfer ) {
                playersLeaving.add(teamKs.get(i));
                teamKs.remove(i);
            } else {
                teamKs.get(i).advanceSeason();
                i++;
            }
        }

        i = 0;
        while (i < teamDLs.size()) {
            if (teamDLs.get(i).year == 4  && !teamDLs.get(i).isTransfer || (teamDLs.get(i).year == 3 && teamDLs.get(i).ratOvr > NFL_OVR && Math.random() < NFL_CHANCE)) {
                playersLeaving.add(teamDLs.get(i));
                teamDLs.remove(i);
            } else {
                teamDLs.get(i).advanceSeason();
                i++;
            }
        }

        i = 0;
        while (i < teamLBs.size()) {
            if (teamLBs.get(i).year == 4  && !teamLBs.get(i).isTransfer || (teamLBs.get(i).year == 3 && teamLBs.get(i).ratOvr > NFL_OVR && Math.random() < NFL_CHANCE)) {
                playersLeaving.add(teamLBs.get(i));
                teamLBs.remove(i);
            } else {
                teamLBs.get(i).advanceSeason();
                i++;
            }
        }

        i = 0;
        while (i < teamCBs.size()) {
            if (teamCBs.get(i).year == 4  && !teamCBs.get(i).isTransfer || (teamCBs.get(i).year == 3 && teamCBs.get(i).ratOvr > NFL_OVR && Math.random() < NFL_CHANCE)) {
                playersLeaving.add(teamCBs.get(i));
                teamCBs.remove(i);
            } else {
                teamCBs.get(i).advanceSeason();
                i++;
            }
        }

        i = 0;
        while (i < teamSs.size()) {
            if (teamSs.get(i).year == 4  && !teamSs.get(i).isTransfer || (teamSs.get(i).year == 3 && teamSs.get(i).ratOvr > NFL_OVR && Math.random() < NFL_CHANCE)) {
                playersLeaving.add(teamSs.get(i));
                teamSs.remove(i);
            } else {
                teamSs.get(i).advanceSeason();
                i++;
            }
        }
        resetStats();
        sortPlayers();
        getPlayersTransferring();
    }

    public void getPlayersTransferring() {

        // PLAYER TRANSFERS
        // Juniors/Seniors - rated 75+ who have not played more than 4 games total and are not starters on teams > 60
        int i;
        dismissalChance = Math.round((100-HC.get(0).ratDiscipline)/10);

        sortPlayers();
        i = 0;
        while (i < teamQBs.size() && teamQBs.size() > 1) {
            int chance = 1;
            if (Math.abs(teamQBs.get(i).region - location) > 2) ++chance;
            if (teamQBs.get(i).personality < 60) ++chance;
            if (teamQBs.get(i).careerGamesPlayed < 6) ++chance;
            if (teamQBs.get(i).year == 4) ++chance;
            if (teamQBs.get(i).personality > 85) ++chance;
            chance += teamQBs.get(i).troubledTimes;

            if (teamQBs.get(i).year > 2 && !teamQBs.get(i).isMedicalRS && teamQBs.get(i).ratOvr > ratTransfer && teamQBs.get(i) != teamQBs.get(0) && (int) (Math.random() * 10) < chance && !teamQBs.get(i).isTransfer || teamQBs.get(i).troubledTimes > Math.random()*dismissalChance) {
                teamQBs.get(i).isTransfer = true;
                if (teamQBs.get(i).troubledTimes > 0) {
                    league.newsStories.get(league.currentWeek+1).add(name + " Player Dismissed>Following several incidents, " + name + " has dimissed QB " + teamQBs.get(i).name + ". The player will have to sit out a year if he chooses to transfer to a new program.");
                    teamQBs.get(i).personality += (int)Math.random()*20;
                }
                if (teamQBs.get(i).personality > 85 && teamQBs.get(i).year==4) {
                    league.newsStories.get(league.currentWeek+1).add(name + " Grad Transfer>" + name + " QB " + teamQBs.get(i).name + " has announced that he will be leaving the school to attend Grad school elsewhere. He will start his search for a new school immediately.");
                }
                playersTransferring.add(teamQBs.get(i));
                league.transferQBs.add(teamQBs.get(i));
                league.tQBs.add(name);
                teamQBs.remove(i);
            }
            ++i;
        }

        i = 0;
        while (i < teamRBs.size() && teamRBs.size() > 2) {
            int chance = 0;
            if (Math.abs(teamRBs.get(i).region - location) > 2) ++chance;
            if (teamRBs.get(i).personality < 60) ++chance;
            if (teamRBs.get(i).careerGamesPlayed < 6) ++chance;
            if (teamRBs.get(i).year == 4) ++chance;
            chance += teamRBs.get(i).troubledTimes;
            if (teamRBs.get(i).personality > 85) ++chance;

            if (teamRBs.get(i).year > 2 && !teamRBs.get(i).isMedicalRS && teamRBs.get(i).ratOvr > ratTransfer && (int) (Math.random() * 10) < chance && !teamRBs.get(i).isTransfer || teamRBs.get(i).troubledTimes > Math.random()*dismissalChance) {
                if (teamRBs.get(i) != teamRBs.get(0) && teamRBs.get(i) != teamRBs.get(1)) {
                    teamRBs.get(i).isTransfer = true;
                    if (teamRBs.get(i).troubledTimes > 0) {
                        league.newsStories.get(league.currentWeek+1).add(name + " Player Dismissed>Following several incidents, " + name + " has dimissed RB " + teamRBs.get(i).name + ". The player will have to sit out a year if he chooses to transfer to a new program.");
                        teamRBs.get(i).personality += (int)Math.random()*15;
                    }
                    if (teamRBs.get(i).personality > 85 && teamRBs.get(i).year==4) {
                        league.newsStories.get(league.currentWeek+1).add(name + " Grad Transfer>" + name + " RB " + teamRBs.get(i).name + " has announced that he will be leaving the school to attend Grad school elsewhere. He will start his search for a new school immediately.");
                    }
                    playersTransferring.add(teamRBs.get(i));
                    league.transferRBs.add(teamRBs.get(i));
                    league.tRBs.add(name);
                    teamRBs.remove(i);
                }
            }
            ++i;
        }

        i = 0;
        while (i < teamWRs.size() && teamWRs.size() > 3) {
            int chance = 0;
            if (Math.abs(teamWRs.get(i).region - location) > 2) ++chance;
            if (teamWRs.get(i).personality < 60) ++chance;
            if (teamWRs.get(i).careerGamesPlayed < 6) ++chance;
            if (teamWRs.get(i).year == 4) ++chance;
            chance += teamWRs.get(i).troubledTimes;
            if (teamWRs.get(i).personality > 85) ++chance;

            if (teamWRs.get(i).year > 2 && !teamWRs.get(i).isMedicalRS && teamWRs.get(i).ratOvr > ratTransfer && (int) (Math.random() * 10) < chance && !teamWRs.get(i).isTransfer || teamWRs.get(i).troubledTimes > Math.random()*dismissalChance) {
                if (teamWRs.get(i) != teamWRs.get(0) && teamWRs.get(i) != teamWRs.get(1) && teamWRs.get(i) != teamWRs.get(2)) {
                    teamWRs.get(i).isTransfer = true;
                    if (teamWRs.get(i).troubledTimes > 0) {
                        league.newsStories.get(league.currentWeek+1).add(name + " Player Dismissed>Following several incidents, " + name + " has dimissed WR " + teamWRs.get(i).name + ". The player will have to sit out a year if he chooses to transfer to a new program.");
                        teamWRs.get(i).personality += (int)Math.random()*15;
                    }
                    if (teamWRs.get(i).personality > 85 && teamWRs.get(i).year==4) {
                        league.newsStories.get(league.currentWeek+1).add(name + " Grad Transfer>" + name + " WR " + teamWRs.get(i).name + " has announced that he will be leaving the school to attend Grad school elsewhere. He will start his search for a new school immediately.");
                    }
                    playersTransferring.add(teamWRs.get(i));
                    league.transferWRs.add(teamWRs.get(i));
                    league.tWRs.add(name);
                    teamWRs.remove(i);
                }
            }
            ++i;
        }

        i = 0;
        while (i < teamTEs.size() && teamTEs.size() > 1) {
            int chance = 0;
            if (Math.abs(teamTEs.get(i).region - location) > 2) ++chance;
            if (teamTEs.get(i).personality < 60) ++chance;
            if (teamTEs.get(i).careerGamesPlayed < 6) ++chance;
            if (teamTEs.get(i).year == 4) ++chance;
            chance += teamTEs.get(i).troubledTimes;
            if (teamTEs.get(i).personality > 85) ++chance;

            if (teamTEs.get(i).year > 2 && !teamTEs.get(i).isMedicalRS && teamTEs.get(i).ratOvr > ratTransfer && teamTEs.get(i) != teamTEs.get(0) && (int) (Math.random() * 10) < chance && !teamTEs.get(i).isTransfer || teamTEs.get(i).troubledTimes > Math.random()*dismissalChance) {
                teamTEs.get(i).isTransfer = true;
                if (teamTEs.get(i).troubledTimes > 0 ) {
                    league.newsStories.get(league.currentWeek+1).add(name + " Player Dismissed>Following several incidents, " + name + " has dimissed TE " + teamTEs.get(i).name + ". The player will have to sit out a year if he chooses to transfer to a new program.");
                    teamTEs.get(i).personality += (int)Math.random()*15;
                }
                if (teamTEs.get(i).personality > 85 && teamTEs.get(i).year==4) {
                    league.newsStories.get(league.currentWeek+1).add(name + " Grad Transfer>" + name + " TE " + teamTEs.get(i).name + " has announced that he will be leaving the school to attend Grad school elsewhere. He will start his search for a new school immediately.");
                }
                playersTransferring.add(teamTEs.get(i));
                league.transferTEs.add(teamTEs.get(i));
                league.tTEs.add(name);
                teamTEs.remove(i);
            }
            ++i;
        }

        i = 0;
        while (i < teamOLs.size() && teamOLs.size() > 5) {
            int chance = 0;
            if (Math.abs(teamOLs.get(i).region - location) > 2) ++chance;
            if (teamOLs.get(i).personality < 60) ++chance;
            if (teamOLs.get(i).careerGamesPlayed < 6) ++chance;
            if (teamOLs.get(i).year == 4) ++chance;
            chance += teamOLs.get(i).troubledTimes;
            if (teamOLs.get(i).personality > 85) ++chance;

            if (teamOLs.get(i).year > 2 && !teamOLs.get(i).isMedicalRS && teamOLs.get(i).ratOvr > ratTransfer && (int) (Math.random() * 10) < chance && !teamOLs.get(i).isTransfer || teamOLs.get(i).troubledTimes > Math.random()*dismissalChance) {
                if (teamOLs.get(i) != teamOLs.get(0) && teamOLs.get(i) != teamOLs.get(1) && teamOLs.get(i) != teamOLs.get(2) && teamOLs.get(i) != teamOLs.get(3) && teamOLs.get(i) != teamOLs.get(4)) {
                    teamOLs.get(i).isTransfer = true;
                    if (teamOLs.get(i).troubledTimes > 0) {
                        league.newsStories.get(league.currentWeek+1).add(name + " Player Dismissed>Following several incidents, " + name + " has dimissed OL " + teamOLs.get(i).name + ". The player will have to sit out a year if he chooses to transfer to a new program.");
                        teamOLs.get(i).personality += (int)Math.random()*15;
                    }
                    if (teamOLs.get(i).personality > 85 && teamOLs.get(i).year==4) {
                        league.newsStories.get(league.currentWeek+1).add(name + " Grad Transfer>" + name + " OL " + teamOLs.get(i).name + " has announced that he will be leaving the school to attend Grad school elsewhere. He will start his search for a new school immediately.");
                    }
                    playersTransferring.add(teamOLs.get(i));
                    league.transferOLs.add(teamOLs.get(i));
                    league.tOLs.add(name);
                    teamOLs.remove(i);
                }
            }
            ++i;
        }

        i = 0;
        while (i < teamKs.size() && teamKs.size() > 1) {
            int chance = 0;
            if (Math.abs(teamKs.get(i).region - location) > 2) ++chance;
            if (teamKs.get(i).personality < 60) ++chance;
            if (teamKs.get(i).careerGamesPlayed < 6) ++chance;
            if (teamKs.get(i).year == 4) ++chance;
            chance += teamKs.get(i).troubledTimes;
            if (teamKs.get(i).personality > 85) ++chance;

            if (teamKs.get(i).year > 2 && !teamKs.get(i).isMedicalRS && teamKs.get(i).ratOvr > ratTransfer && teamKs.get(i) != teamKs.get(0) && (int) (Math.random() * 10) < chance && !teamKs.get(i).isTransfer || teamKs.get(i).troubledTimes > Math.random()*dismissalChance) {
                teamKs.get(i).isTransfer = true;
                if (teamKs.get(i).troubledTimes > 0) {
                    league.newsStories.get(league.currentWeek+1).add(name + " Player Dismissed>Following several incidents, " + name + " has dimissed K " + teamKs.get(i).name + ". The player will have to sit out a year if he chooses to transfer to a new program.");
                    teamKs.get(i).personality += (int)Math.random()*15;
                }
                if (teamKs.get(i).personality > 85 && teamKs.get(i).year==4) {
                    league.newsStories.get(league.currentWeek+1).add(name + " Grad Transfer>" + name + " K " + teamKs.get(i).name + " has announced that he will be leaving the school to attend Grad school elsewhere. He will start his search for a new school immediately.");
                }
                playersTransferring.add(teamKs.get(i));
                league.transferKs.add(teamKs.get(i));
                league.tKs.add(name);
                teamKs.remove(i);
            }
            ++i;
        }

        i = 0;
        while (i < teamDLs.size() && teamDLs.size() > 4) {
            int chance = 0;
            if (Math.abs(teamDLs.get(i).region - location) > 2) ++chance;
            if (teamDLs.get(i).personality < 60) ++chance;
            if (teamDLs.get(i).careerGamesPlayed < 6) ++chance;
            if (teamDLs.get(i).year == 4) ++chance;
            chance += teamDLs.get(i).troubledTimes;
            if (teamDLs.get(i).personality > 85) ++chance;

            if (teamDLs.get(i).year > 2 && !teamDLs.get(i).isMedicalRS && teamDLs.get(i).ratOvr > ratTransfer && (int) (Math.random() * 10) < chance && !teamDLs.get(i).isTransfer || teamDLs.get(i).troubledTimes > Math.random()*dismissalChance) {
                if (teamDLs.get(i) != teamDLs.get(0) && teamDLs.get(i) != teamDLs.get(1) && teamDLs.get(i) != teamDLs.get(2) && teamDLs.get(i) != teamDLs.get(3)) {
                    teamDLs.get(i).isTransfer = true;
                    if (teamDLs.get(i).troubledTimes > 0) {
                        league.newsStories.get(league.currentWeek+1).add(name + " Player Dismissed>Following several incidents, " + name + " has dimissed DL " + teamDLs.get(i).name + ". The player will have to sit out a year if he chooses to transfer to a new program.");
                        teamDLs.get(i).personality += (int)Math.random()*15;
                    }
                    if (teamDLs.get(i).personality > 85 && teamDLs.get(i).year==4) {
                        league.newsStories.get(league.currentWeek+1).add(name + " Grad Transfer>" + name + " DL " + teamDLs.get(i).name + " has announced that he will be leaving the school to attend Grad school elsewhere. He will start his search for a new school immediately.");
                    }
                    playersTransferring.add(teamDLs.get(i));
                    league.transferDLs.add(teamDLs.get(i));
                    league.tDLs.add(name);
                    teamDLs.remove(i);
                }
            }
            ++i;
        }

        i = 0;
        while (i < teamLBs.size() && teamLBs.size() > 3) {
            int chance = 0;
            if (Math.abs(teamLBs.get(i).region - location) > 2) ++chance;
            if (teamLBs.get(i).personality < 60) ++chance;
            if (teamLBs.get(i).careerGamesPlayed < 6) ++chance;
            if (teamLBs.get(i).year == 4) ++chance;
            chance += teamLBs.get(i).troubledTimes;
            if (teamLBs.get(i).personality > 85) ++chance;

            if (teamLBs.get(i).year > 2 && !teamLBs.get(i).isMedicalRS && teamLBs.get(i).ratOvr > ratTransfer && (int) (Math.random() * 10) < chance && !teamLBs.get(i).isTransfer || teamLBs.get(i).troubledTimes > Math.random()*dismissalChance) {
                if (teamLBs.get(i) != teamLBs.get(0) && teamLBs.get(i) != teamLBs.get(1) && teamLBs.get(i) != teamLBs.get(2)) {
                    teamLBs.get(i).isTransfer = true;
                    if (teamLBs.get(i).troubledTimes > 0) {
                        league.newsStories.get(league.currentWeek+1).add(name + " Player Dismissed>Following several incidents, " + name + " has dimissed LB " + teamLBs.get(i).name + ". The player will have to sit out a year if he chooses to transfer to a new program.");
                        teamLBs.get(i).personality += (int)Math.random()*15;
                    }
                    if (teamLBs.get(i).personality > 85 && teamLBs.get(i).year==4) {
                        league.newsStories.get(league.currentWeek+1).add(name + " Grad Transfer>" + name + " LB " + teamLBs.get(i).name + " has announced that he will be leaving the school to attend Grad school elsewhere. He will start his search for a new school immediately.");
                    }
                    playersTransferring.add(teamLBs.get(i));
                    league.transferLBs.add(teamLBs.get(i));
                    league.tLBs.add(name);
                    teamLBs.remove(i);
                }
            }
            ++i;
        }

        i = 0;
        while (i < teamCBs.size() && teamCBs.size() > 3) {
            int chance = 0;
            if (Math.abs(teamCBs.get(i).region - location) > 2) ++chance;
            if (teamCBs.get(i).personality < 60) ++chance;
            if (teamCBs.get(i).careerGamesPlayed < 6) ++chance;
            if (teamCBs.get(i).year == 4) ++chance;
            chance += teamCBs.get(i).troubledTimes;
            if (teamCBs.get(i).personality > 85) ++chance;

            if (teamCBs.get(i).year > 2 && !teamCBs.get(i).isMedicalRS && teamCBs.get(i).ratOvr > ratTransfer && (int) (Math.random() * 10) < chance && !teamCBs.get(i).isTransfer || teamCBs.get(i).troubledTimes > Math.random()*dismissalChance) {
                if (teamCBs.get(i) != teamCBs.get(0) && teamCBs.get(i) != teamCBs.get(1) && teamCBs.get(i) != teamCBs.get(2) && teamCBs.get(i) != teamCBs.get(3)) {
                    teamCBs.get(i).isTransfer = true;
                    if (teamCBs.get(i).troubledTimes > 0) {
                        league.newsStories.get(league.currentWeek+1).add(name + " Player Dismissed>Following several incidents, " + name + " has dimissed CB " + teamCBs.get(i).name + ". The player will have to sit out a year if he chooses to transfer to a new program.");
                        teamCBs.get(i).personality += (int)Math.random()*15;
                    }
                    if (teamCBs.get(i).personality > 85 && teamCBs.get(i).year==4) {
                        league.newsStories.get(league.currentWeek+1).add(name + " Grad Transfer>" + name + " CB " + teamCBs.get(i).name + " has announced that he will be leaving the school to attend Grad school elsewhere. He will start his search for a new school immediately.");
                    }
                    playersTransferring.add(teamCBs.get(i));
                    league.transferCBs.add(teamCBs.get(i));
                    league.tCBs.add(name);
                    teamCBs.remove(i);
                }
            }
            ++i;
        }

        i = 0;
        while (i < teamSs.size() && teamSs.size() > 1) {
            int chance = 0;
            if (Math.abs(teamSs.get(i).region - location) > 2) ++chance;
            if (teamSs.get(i).personality < 60) ++chance;
            if (teamSs.get(i).careerGamesPlayed < 6) ++chance;
            if (teamSs.get(i).year == 4) ++chance;
            chance += teamSs.get(i).troubledTimes;
            if (teamSs.get(i).personality > 85) ++chance;

            if (teamSs.get(i).year > 2 && !teamSs.get(i).isMedicalRS && teamSs.get(i).ratOvr > ratTransfer && teamSs.get(i) != teamSs.get(0) && (int) (Math.random() * 10) < chance && !teamSs.get(i).isTransfer || teamSs.get(i).troubledTimes > Math.random()*dismissalChance) {
                teamSs.get(i).isTransfer = true;
                if (teamSs.get(i).troubledTimes > 0) {
                    league.newsStories.get(league.currentWeek+1).add(name + " Player Dismissed>Following several incidents, " + name + " has dimissed S " + teamSs.get(i).name + ". The player will have to sit out a year if he chooses to transfer to a new program.");
                    teamSs.get(i).personality += (int)Math.random()*15;
                }
                if (teamSs.get(i).personality > 85 && teamSs.get(i).year==4) {
                    league.newsStories.get(league.currentWeek+1).add(name + " Grad Transfer>" + name + " S " + teamSs.get(i).name + " has announced that he will be leaving the school to attend Grad school elsewhere. He will start his search for a new school immediately.");
                }
                playersTransferring.add(teamSs.get(i));
                league.transferSs.add(teamSs.get(i));
                league.tSs.add(name);
                teamSs.remove(i);
            }
            ++i;
        }
    }


    //GET FINAL RECRUITING NEEDS PRIOR TO RECRUITING
    public void CPUrecruiting() {
        
        int numTransfers = 0;
        if (!userControlled) {
            for (int i = 0; i < teamQBs.size(); ++i) {
                if (teamQBs.get(i).isTransfer) {
                    numTransfers++;
                }
            }
            int qb = minQBs - (teamQBs.size() - numTransfers);
            
            numTransfers = 0;
            for (int i = 0; i < teamRBs.size(); ++i) {
                if (teamRBs.get(i).isTransfer) {
                    numTransfers++;
                }
            }
            int rb = minRBs - (teamRBs.size() - numTransfers);

            numTransfers = 0;
            for (int i = 0; i < teamWRs.size(); ++i) {
                if (teamWRs.get(i).isTransfer) {
                    numTransfers++;
                }
            }
            int wr = minWRs - (teamWRs.size() - numTransfers);

            numTransfers = 0;
            for (int i = 0; i < teamTEs.size(); ++i) {
                if (teamTEs.get(i).isTransfer) {
                    numTransfers++;
                }
            }
            int te = minTEs - (teamTEs.size() - numTransfers);

            numTransfers = 0;
            for (int i = 0; i < teamOLs.size(); ++i) {
                if (teamOLs.get(i).isTransfer) {
                    numTransfers++;
                }
            }
            int ol = minOLs - (teamOLs.size() - numTransfers);

            numTransfers = 0;
            for (int i = 0; i < teamKs.size(); ++i) {
                if (teamKs.get(i).isTransfer) {
                    numTransfers++;
                }
            }
            int k = minKs - (teamKs.size() - numTransfers);

            numTransfers = 0;
            for (int i = 0; i < teamDLs.size(); ++i) {
                if (teamDLs.get(i).isTransfer) {
                    numTransfers++;
                }
            }
            int dl = minDLs - (teamDLs.size() - numTransfers);

            numTransfers = 0;
            for (int i = 0; i < teamLBs.size(); ++i) {
                if (teamLBs.get(i).isTransfer) {
                    numTransfers++;
                }
            }
            int lb = minLBs - (teamLBs.size() - numTransfers);

            numTransfers = 0;
            for (int i = 0; i < teamCBs.size(); ++i) {
                if (teamCBs.get(i).isTransfer) {
                    numTransfers++;
                }
            }
            int cb = minCBs - (teamCBs.size() - numTransfers);

            numTransfers = 0;
            for (int i = 0; i < teamSs.size(); ++i) {
                if (teamSs.get(i).isTransfer) {
                    numTransfers++;
                }
            }
            int s = minSs - (teamSs.size() - numTransfers);

            recruitPlayersFreshman(qb, rb, wr, te, ol, k, dl, lb, cb, s);
            resetStats();
        }
    }

    /**
     * Recruit freshman at each position.
     * This is used after each season.
     *
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
    public void recruitPlayersFreshman(int qbNeeds, int rbNeeds, int wrNeeds, int teNeeds, int olNeeds, int kNeeds, int dlNeeds, int lbNeeds, int cbNeeds, int sNeeds) {
        //make team
        int stars;
        int recruitChance;
        if (HC.get(0) != null) {
            recruitChance = HC.get(0).ratTalent-50;
        } else {
            recruitChance = teamPrestige-50;
        }

        for (int i = 0; i < qbNeeds; ++i) {
            stars = Math.round(teamPrestige / 10) + 1;
            if (stars < minRecruitStar) stars = minRecruitStar;
            if (recruitChance > Math.random()*50) {
                stars += Math.random()*(maxStarRating-stars);
            } else {
                stars -= Math.random()*(stars);
            }


            if (stars > 10) stars = 10;
            //make QBs
            teamQBs.add(new PlayerQB(league.getRandName(), 1, stars, this));
        }

        for (int i = 0; i < kNeeds; ++i) {
            stars = Math.round(teamPrestige / 10) + 1;
            if (stars < minRecruitStar) stars = minRecruitStar;
            if (recruitChance > Math.random()*50) {
                stars += Math.random()*(maxStarRating-stars);
            } else {
                stars -= Math.random()*(stars);
            }
            if (stars > 10) stars = 10;

            //make Ks
            teamKs.add(new PlayerK(league.getRandName(), 1, stars, this));
        }

        for (int i = 0; i < rbNeeds; ++i) {
            stars = Math.round(teamPrestige / 10) + 1;
            if (stars < minRecruitStar) stars = minRecruitStar;
            if (recruitChance > Math.random()*50) {
                stars += Math.random()*(maxStarRating-stars);
            } else {
                stars -= Math.random()*(stars);
            }
            if (stars > 10) stars = 10;

            //make RBs
            teamRBs.add(new PlayerRB(league.getRandName(), 1, stars, this));
        }

        for (int i = 0; i < wrNeeds; ++i) {
            stars = Math.round(teamPrestige / 10) + 1;
            if (stars < minRecruitStar) stars = minRecruitStar;
            if (recruitChance > Math.random()*50) {
                stars += Math.random()*(maxStarRating-stars);
            } else {
                stars -= Math.random()*(stars);
            }
            if (stars > 10) stars = 10;

            //make WRs
            teamWRs.add(new PlayerWR(league.getRandName(), 1, stars, this));
        }

        for (int i = 0; i < teNeeds; ++i) {
            stars = Math.round(teamPrestige / 10) + 1;
            if (stars < minRecruitStar) stars = minRecruitStar;
            if (recruitChance > Math.random()*50) {
                stars += Math.random()*(maxStarRating-stars);
            } else {
                stars -= Math.random()*(stars);
            }
            if (stars > 10) stars = 10;

            //make TEs
            teamTEs.add(new PlayerTE(league.getRandName(), 1, stars, this));
        }

        for (int i = 0; i < olNeeds; ++i) {
            stars = Math.round(teamPrestige / 10) + 1;
            if (stars < minRecruitStar) stars = minRecruitStar;
            if (recruitChance > Math.random()*50) {
                stars += Math.random()*(maxStarRating-stars);
            } else {
                stars -= Math.random()*(stars);
            }
            if (stars > 10) stars = 10;

            //make OLs
            teamOLs.add(new PlayerOL(league.getRandName(), 1, stars, this));
        }

        for (int i = 0; i < dlNeeds; ++i) {
            stars = Math.round(teamPrestige / 10) + 1;
            if (stars < minRecruitStar) stars = minRecruitStar;
            if (recruitChance > Math.random()*50) {
                stars += Math.random()*(maxStarRating-stars);
            } else {
                stars -= Math.random()*(stars);
            }
            if (stars > 10) stars = 10;

            //make DLs
            teamDLs.add(new PlayerDL(league.getRandName(), 1, stars, this));
        }

        for (int i = 0; i < lbNeeds; ++i) {
            stars = Math.round(teamPrestige / 10) + 1;
            if (stars < minRecruitStar) stars = minRecruitStar;
            if (recruitChance > Math.random()*50) {
                stars += Math.random()*(maxStarRating-stars);
            } else {
                stars -= Math.random()*(stars);
            }
            if (stars > 10) stars = 10;
            //make LBs
            teamLBs.add(new PlayerLB(league.getRandName(), 1, stars, this));
        }

        for (int i = 0; i < cbNeeds; ++i) {
            stars = Math.round(teamPrestige / 10) + 1;
            if (stars < minRecruitStar) stars = minRecruitStar;
            if (recruitChance > Math.random()*50) {
                stars += Math.random()*(maxStarRating-stars);
            } else {
                stars -= Math.random()*(stars);
            }
            if (stars > 10) stars = 10;

            //make CBs
            teamCBs.add(new PlayerCB(league.getRandName(), 1, stars, this));
        }

        for (int i = 0; i < sNeeds; ++i) {
            stars = Math.round(teamPrestige / 10) + 1;
            if (stars < minRecruitStar) stars = minRecruitStar;
            if (recruitChance > Math.random()*50) {
                stars += Math.random()*(maxStarRating-stars);
            } else {
                stars -= Math.random()*(stars);
            }
            if (stars > 10) stars = 10;

            //make Ss
            teamSs.add(new PlayerS(league.getRandName(), 1, stars, this));
        }

        //done making players, sort them
        sortPlayers();
    }

    /**
     * Recruits walk ons at each needed position.
     * This is used by user teams if there is a dearth at any position.
     */
    public void recruitWalkOns() {
        int star;
        //recruit walk ons (used for player teams who dont recruit all needs)
        
        //QUARTERBACKS
        int needs = minQBs - teamQBs.size();
        for (int i = 0; i < teamQBs.size(); ++i) {
            if (teamQBs.get(i).isRedshirt || teamQBs.get(i).isTransfer) {
                needs++;
            }
        }
        for (int i = 0; i < needs; ++i) {
            star = (int)Math.random()*3 + 1;
            teamQBs.add(new PlayerQB(league.getRandName(), 1, star, this));
        }

        //RUNNING BACKS
        needs = minRBs - teamRBs.size();
        for (int i = 0; i < teamRBs.size(); ++i) {
            if (teamRBs.get(i).isRedshirt || teamRBs.get(i).isTransfer) {
                needs++;
            }
        }
        for (int i = 0; i < needs; ++i) {
            star = (int)Math.random()*3 + 1;
            teamRBs.add(new PlayerRB(league.getRandName(), 1, star, this));
        }

        //WIDE RECEIVERS
        needs = minWRs - teamWRs.size();
        for (int i = 0; i < teamWRs.size(); ++i) {
            if (teamWRs.get(i).isRedshirt || teamWRs.get(i).isTransfer) {
                needs++;
            }
        }
        for (int i = 0; i < needs; ++i) {
            star = (int)Math.random()*3 + 1;
            teamWRs.add(new PlayerWR(league.getRandName(), 1, star, this));
        }

        //TIGHT ENDS
        needs = minTEs - teamTEs.size();
        for (int i = 0; i < teamTEs.size(); ++i) {
            if (teamTEs.get(i).isRedshirt || teamTEs.get(i).isTransfer) {
                needs++;
            }
        }
        for (int i = 0; i < needs; ++i) {
            star = (int)Math.random()*3 + 1;
            teamTEs.add(new PlayerTE(league.getRandName(), 1, star, this));
        }

        //OFFENSIVE LINE
        needs = minOLs - teamOLs.size();
        for (int i = 0; i < teamOLs.size(); ++i) {
            if (teamOLs.get(i).isRedshirt || teamOLs.get(i).isTransfer) {
                needs++;
            }
        }
        for (int i = 0; i < needs; ++i) {
            star = (int)Math.random()*3 + 1;
            teamOLs.add(new PlayerOL(league.getRandName(), 1, star, this));
        }
    
        //KICKERS
        needs = minKs - teamKs.size();
        for (int i = 0; i < teamKs.size(); ++i) {
            if (teamKs.get(i).isRedshirt || teamKs.get(i).isTransfer) {
                needs++;
            }
        }
        for (int i = 0; i < needs; ++i) {
            star = (int)Math.random()*3 + 1;
            teamKs.add(new PlayerK(league.getRandName(), 1, star, this));
        }

        //DEFENSIVE LINE
        needs = minDLs - teamDLs.size();
        for (int i = 0; i < teamDLs.size(); ++i) {
            if (teamDLs.get(i).isRedshirt || teamDLs.get(i).isTransfer) {
                needs++;
            }
        }
        for (int i = 0; i < needs; ++i) {
            star = (int)Math.random()*3 + 1;
            teamDLs.add(new PlayerDL(league.getRandName(), 1, star, this));
        }

        //LINEBACKERS
        needs = minLBs - teamLBs.size();
        for (int i = 0; i < teamLBs.size(); ++i) {
            if (teamLBs.get(i).isRedshirt || teamLBs.get(i).isTransfer) {
                needs++;
            }
        }
        for (int i = 0; i < needs; ++i) {
            star = (int)Math.random()*3 + 1;
            teamLBs.add(new PlayerLB(league.getRandName(), 1, star, this));
        }

        //CORNERBACKS
        needs = minCBs - teamCBs.size();
        for (int i = 0; i < teamCBs.size(); ++i) {
            if (teamCBs.get(i).isRedshirt || teamCBs.get(i).isTransfer) {
                needs++;
            }
        }
        for (int i = 0; i < needs; ++i) {
            star = (int)Math.random()*3 + 1;
            teamCBs.add(new PlayerCB(league.getRandName(), 1, star, this));
        }

        //SAFETY
        needs = minSs - teamSs.size();
        for (int i = 0; i < teamSs.size(); ++i) {
            if (teamSs.get(i).isRedshirt || teamSs.get(i).isTransfer) {
                needs++;
            }
        }
        for (int i = 0; i < needs; ++i) {
            star = (int)Math.random()*3 + 1;
            teamSs.add(new PlayerS(league.getRandName(), 1, star, this));
        }

        //done making players, sort them
        sortPlayers();
    }

    public int calcMaxRecruitRating() {
        int rating;
        rating = (int)Math.round(( maxStarRating + (double)((teamPrestige - 60)/10) ));
        if (rating < 2) rating = 2;
        if (rating > 10) rating = 10;

        return rating;
    }

    public PlayerQB[] getQBRecruits(int rating) {
        int adjNumRecruits = numRecruits;
        PlayerQB[] recruits = new PlayerQB[adjNumRecruits];
        int stars;

        for (int i = 0; i < adjNumRecruits; ++i) {
            stars = (int) (rating * (float) (adjNumRecruits - i / 2) / adjNumRecruits);
            recruits[i] = new PlayerQB(league.getRandName(), 1, stars, this);
        }
        Arrays.sort(recruits, new RecruitComparator());
        return recruits;
    }

    public PlayerRB[] getRBRecruits(int rating) {
        int adjNumRecruits = numRecruits;
        PlayerRB[] recruits = new PlayerRB[adjNumRecruits];
        int stars;

        for (int i = 0; i < adjNumRecruits; ++i) {
            stars = (int) (rating * (float) (adjNumRecruits - i / 2) / adjNumRecruits);
            recruits[i] = new PlayerRB(league.getRandName(), 1, stars, this);
        }
        Arrays.sort(recruits, new RecruitComparator());
        return recruits;
    }

    public PlayerWR[] getWRRecruits(int rating) {
        int adjNumRecruits = 2 * numRecruits;
        PlayerWR[] recruits = new PlayerWR[adjNumRecruits];
        int stars;

        for (int i = 0; i < adjNumRecruits; ++i) {
            stars = (int) (rating * (float) (adjNumRecruits - i / 2) / adjNumRecruits);
            recruits[i] = new PlayerWR(league.getRandName(), 1, stars, this);
        }
        Arrays.sort(recruits, new RecruitComparator());
        return recruits;
    }

    public PlayerTE[] getTERecruits(int rating) {
        int adjNumRecruits = numRecruits;
        PlayerTE[] recruits = new PlayerTE[adjNumRecruits];
        int stars;

        for (int i = 0; i < adjNumRecruits; ++i) {
            stars = (int) (rating * (float) (adjNumRecruits - i / 2) / adjNumRecruits);
            recruits[i] = new PlayerTE(league.getRandName(), 1, stars, this);
        }
        Arrays.sort(recruits, new RecruitComparator());
        return recruits;
    }

    public PlayerOL[] getOLRecruits(int rating) {
        int adjNumRecruits = 2 * numRecruits;
        PlayerOL[] recruits = new PlayerOL[adjNumRecruits];
        int stars;

        for (int i = 0; i < adjNumRecruits; ++i) {
            stars = (int) (rating * (float) (adjNumRecruits - i / 2) / adjNumRecruits);
            recruits[i] = new PlayerOL(league.getRandName(), 1, stars, this);
        }
        Arrays.sort(recruits, new RecruitComparator());
        return recruits;
    }

    public PlayerK[] getKRecruits(int rating) {
        int adjNumRecruits = numRecruits;
        PlayerK[] recruits = new PlayerK[adjNumRecruits];
        int stars;

        for (int i = 0; i < adjNumRecruits; ++i) {
            stars = (int) (rating * (float) (adjNumRecruits - i / 2) / adjNumRecruits);
            recruits[i] = new PlayerK(league.getRandName(), 1, stars, this);
        }
        Arrays.sort(recruits, new RecruitComparator());
        return recruits;
    }

    public PlayerDL[] getDLRecruits(int rating) {
        int adjNumRecruits = 2 * numRecruits;
        PlayerDL[] recruits = new PlayerDL[adjNumRecruits];
        int stars;

        for (int i = 0; i < adjNumRecruits; ++i) {
            stars = (int) (rating * (float) (adjNumRecruits - i / 2) / adjNumRecruits);
            recruits[i] = new PlayerDL(league.getRandName(), 1, stars, this);
        }
        Arrays.sort(recruits, new RecruitComparator());
        return recruits;
    }

    public PlayerLB[] getLBRecruits(int rating) {
        int adjNumRecruits = 2 * numRecruits;
        PlayerLB[] recruits = new PlayerLB[adjNumRecruits];
        int stars;

        for (int i = 0; i < adjNumRecruits; ++i) {
            stars = (int) (rating * (float) (adjNumRecruits - i / 2) / adjNumRecruits);
            recruits[i] = new PlayerLB(league.getRandName(), 1, stars, this);
        }
        Arrays.sort(recruits, new RecruitComparator());
        return recruits;
    }

    public PlayerCB[] getCBRecruits(int rating) {
        int adjNumRecruits = 2 * numRecruits;
        PlayerCB[] recruits = new PlayerCB[adjNumRecruits];
        int stars;

        for (int i = 0; i < adjNumRecruits; ++i) {
            stars = (int) (rating * (float) (adjNumRecruits - i / 2) / adjNumRecruits);
            recruits[i] = new PlayerCB(league.getRandName(), 1, stars, this);
        }
        Arrays.sort(recruits, new RecruitComparator());
        return recruits;
    }

    public PlayerS[] getSRecruits(int rating) {
        int adjNumRecruits = numRecruits;
        PlayerS[] recruits = new PlayerS[adjNumRecruits];
        int stars;

        for (int i = 0; i < adjNumRecruits; ++i) {
            stars = (int) (rating * (float) (adjNumRecruits - i / 2) / adjNumRecruits);
            recruits[i] = new PlayerS(league.getRandName(), 1, stars, this);
        }
        Arrays.sort(recruits, new RecruitComparator());
        return recruits;
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
     *
     * @param line       player to be recruited
     * @param isRedshirt whether that player should be recruited as a RS
     */
    private void recruitPlayerCSV(String line, boolean isRedshirt) {
        String[] playerInfo = line.split(",");
        int durability;
        if (playerInfo.length >= 11) durability = Integer.parseInt(playerInfo[10]);
        else durability = (int) (50 + 50 * Math.random());
        if (playerInfo[0].equals("QB")) {
            if (playerInfo.length >= 22)
                teamQBs.add(new PlayerQB(playerInfo[1], this,
                        Integer.parseInt(playerInfo[2]), Integer.parseInt(playerInfo[3]),
                        Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5]),
                        Integer.parseInt(playerInfo[6]), Integer.parseInt(playerInfo[7]), isRedshirt, durability,
                        Integer.parseInt(playerInfo[11]), Integer.parseInt(playerInfo[12]),
                        Integer.parseInt(playerInfo[13]), Integer.parseInt(playerInfo[14]),
                        Integer.parseInt(playerInfo[15]), Integer.parseInt(playerInfo[16]),
                        Integer.parseInt(playerInfo[17]), Integer.parseInt(playerInfo[18]),
                        Integer.parseInt(playerInfo[19]), Integer.parseInt(playerInfo[20]),
                        Integer.parseInt(playerInfo[21]), Integer.parseInt(playerInfo[22]),
                        Integer.parseInt(playerInfo[23]), Integer.parseInt(playerInfo[24]), Integer.parseInt(playerInfo[25]),
                        Integer.parseInt(playerInfo[26]), Boolean.parseBoolean(playerInfo[27]),
                        Integer.parseInt(playerInfo[28]), Integer.parseInt(playerInfo[29])));
            else
                teamQBs.add(new PlayerQB(playerInfo[1], this,
                        Integer.parseInt(playerInfo[2]), Integer.parseInt(playerInfo[3]),
                        Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5]),
                        Integer.parseInt(playerInfo[6]), Integer.parseInt(playerInfo[7]),
                        isRedshirt, durability, Integer.parseInt(playerInfo[11]),
                        Integer.parseInt(playerInfo[12]), Integer.parseInt(playerInfo[13])));
        } else if (playerInfo[0].equals("RB")) {
            if (playerInfo.length >= 20)
                teamRBs.add(new PlayerRB(playerInfo[1], this,
                        Integer.parseInt(playerInfo[2]), Integer.parseInt(playerInfo[3]),
                        Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5]),
                        Integer.parseInt(playerInfo[6]), Integer.parseInt(playerInfo[7]), isRedshirt, durability,
                        Integer.parseInt(playerInfo[11]), Integer.parseInt(playerInfo[12]),
                        Integer.parseInt(playerInfo[13]), Integer.parseInt(playerInfo[14]),
                        Integer.parseInt(playerInfo[15]), Integer.parseInt(playerInfo[16]),
                        Integer.parseInt(playerInfo[17]), Integer.parseInt(playerInfo[18]),
                        Integer.parseInt(playerInfo[19]), Integer.parseInt(playerInfo[20]),
                        Integer.parseInt(playerInfo[21]), Integer.parseInt(playerInfo[22]),
                        Integer.parseInt(playerInfo[23]), Boolean.parseBoolean(playerInfo[24]),
                        Integer.parseInt(playerInfo[25]), Integer.parseInt(playerInfo[26])));
            else
                teamRBs.add(new PlayerRB(playerInfo[1], this,
                        Integer.parseInt(playerInfo[2]), Integer.parseInt(playerInfo[3]),
                        Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5]),
                        Integer.parseInt(playerInfo[6]), Integer.parseInt(playerInfo[7]),
                        isRedshirt, durability, Integer.parseInt(playerInfo[11]),
                        Integer.parseInt(playerInfo[12]), Integer.parseInt(playerInfo[13])));
        } else if (playerInfo[0].equals("WR")) {
            if (playerInfo.length >= 22)
                teamWRs.add(new PlayerWR(playerInfo[1], this,
                        Integer.parseInt(playerInfo[2]), Integer.parseInt(playerInfo[3]),
                        Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5]),
                        Integer.parseInt(playerInfo[6]), Integer.parseInt(playerInfo[7]), isRedshirt, durability,
                        Integer.parseInt(playerInfo[11]), Integer.parseInt(playerInfo[12]),
                        Integer.parseInt(playerInfo[13]), Integer.parseInt(playerInfo[14]),
                        Integer.parseInt(playerInfo[15]), Integer.parseInt(playerInfo[16]),
                        Integer.parseInt(playerInfo[17]), Integer.parseInt(playerInfo[18]),
                        Integer.parseInt(playerInfo[19]), Integer.parseInt(playerInfo[20]),
                        Integer.parseInt(playerInfo[21]), Integer.parseInt(playerInfo[22]),
                        Boolean.parseBoolean(playerInfo[23]),Integer.parseInt(playerInfo[24]), Integer.parseInt(playerInfo[25])));
            else
                teamWRs.add(new PlayerWR(playerInfo[1], this,
                        Integer.parseInt(playerInfo[2]), Integer.parseInt(playerInfo[3]),
                        Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5]),
                        Integer.parseInt(playerInfo[6]), Integer.parseInt(playerInfo[7]),
                        isRedshirt, durability, Integer.parseInt(playerInfo[11]),
                        Integer.parseInt(playerInfo[12]), Integer.parseInt(playerInfo[13])));
        } else if (playerInfo[0].equals("TE")) {
            if (playerInfo.length >= 22)
                teamTEs.add(new PlayerTE(playerInfo[1], this,
                        Integer.parseInt(playerInfo[2]), Integer.parseInt(playerInfo[3]),
                        Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5]),
                        Integer.parseInt(playerInfo[6]), Integer.parseInt(playerInfo[7]), isRedshirt, durability,
                        Integer.parseInt(playerInfo[11]), Integer.parseInt(playerInfo[12]),
                        Integer.parseInt(playerInfo[13]), Integer.parseInt(playerInfo[14]),
                        Integer.parseInt(playerInfo[15]), Integer.parseInt(playerInfo[16]),
                        Integer.parseInt(playerInfo[17]), Integer.parseInt(playerInfo[18]),
                        Integer.parseInt(playerInfo[19]), Integer.parseInt(playerInfo[20]),
                        Integer.parseInt(playerInfo[21]), Integer.parseInt(playerInfo[22]),
                        Boolean.parseBoolean(playerInfo[23]),Integer.parseInt(playerInfo[24]), Integer.parseInt(playerInfo[25])));
            else
                teamTEs.add(new PlayerTE(playerInfo[1], this,
                        Integer.parseInt(playerInfo[2]), Integer.parseInt(playerInfo[3]),
                        Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5]),
                        Integer.parseInt(playerInfo[6]), Integer.parseInt(playerInfo[7]),
                        isRedshirt, durability, Integer.parseInt(playerInfo[11]),
                        Integer.parseInt(playerInfo[12]), Integer.parseInt(playerInfo[13])));
        } else if (playerInfo[0].equals("OL")) {
            if (playerInfo.length >= 16)
                teamOLs.add(new PlayerOL(playerInfo[1], this,
                        Integer.parseInt(playerInfo[2]), Integer.parseInt(playerInfo[3]),
                        Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5]),
                        Integer.parseInt(playerInfo[6]), Integer.parseInt(playerInfo[7]), isRedshirt, durability,
                        Integer.parseInt(playerInfo[11]), Integer.parseInt(playerInfo[12]),
                        Integer.parseInt(playerInfo[13]), Integer.parseInt(playerInfo[14]),
                        Integer.parseInt(playerInfo[15]), Integer.parseInt(playerInfo[16]),
                        Boolean.parseBoolean(playerInfo[17]),Integer.parseInt(playerInfo[18]), Integer.parseInt(playerInfo[19])));
            else
                teamOLs.add(new PlayerOL(playerInfo[1], this,
                        Integer.parseInt(playerInfo[2]), Integer.parseInt(playerInfo[3]),
                        Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5]),
                        Integer.parseInt(playerInfo[6]), Integer.parseInt(playerInfo[7]),
                        isRedshirt, durability, Integer.parseInt(playerInfo[11]),
                        Integer.parseInt(playerInfo[12]), Integer.parseInt(playerInfo[13])));
        } else if (playerInfo[0].equals("K")) {
            if (playerInfo.length >= 20)
                teamKs.add(new PlayerK(playerInfo[1], this,
                        Integer.parseInt(playerInfo[2]), Integer.parseInt(playerInfo[3]),
                        Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5]),
                        Integer.parseInt(playerInfo[6]), Integer.parseInt(playerInfo[7]), isRedshirt, durability,
                        Integer.parseInt(playerInfo[11]), Integer.parseInt(playerInfo[12]),
                        Integer.parseInt(playerInfo[13]), Integer.parseInt(playerInfo[14]),
                        Integer.parseInt(playerInfo[15]), Integer.parseInt(playerInfo[16]),
                        Integer.parseInt(playerInfo[17]), Integer.parseInt(playerInfo[18]),
                        Integer.parseInt(playerInfo[19]), Integer.parseInt(playerInfo[20]),
                        Boolean.parseBoolean(playerInfo[21]),Integer.parseInt(playerInfo[22]), Integer.parseInt(playerInfo[23])));
            else
                teamKs.add(new PlayerK(playerInfo[1], this,
                        Integer.parseInt(playerInfo[2]), Integer.parseInt(playerInfo[3]),
                        Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5]),
                        Integer.parseInt(playerInfo[6]), Integer.parseInt(playerInfo[7]),
                        isRedshirt, durability, Integer.parseInt(playerInfo[11]),
                        Integer.parseInt(playerInfo[12]), Integer.parseInt(playerInfo[13])));
        } else if (playerInfo[0].equals("DL")) {
            if (playerInfo.length >= 16)
                teamDLs.add(new PlayerDL(playerInfo[1], this,
                        Integer.parseInt(playerInfo[2]), Integer.parseInt(playerInfo[3]),
                        Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5]),
                        Integer.parseInt(playerInfo[6]), Integer.parseInt(playerInfo[7]), isRedshirt, durability,
                        Integer.parseInt(playerInfo[11]), Integer.parseInt(playerInfo[12]),
                        Integer.parseInt(playerInfo[13]), Integer.parseInt(playerInfo[14]),
                        Integer.parseInt(playerInfo[15]), Integer.parseInt(playerInfo[16]),
                        Integer.parseInt(playerInfo[17]), Integer.parseInt(playerInfo[18]),
                        Integer.parseInt(playerInfo[19]), Integer.parseInt(playerInfo[20]),
                        Boolean.parseBoolean(playerInfo[21]),Integer.parseInt(playerInfo[22]), Integer.parseInt(playerInfo[23])));
            else
                teamDLs.add(new PlayerDL(playerInfo[1], this,
                        Integer.parseInt(playerInfo[2]), Integer.parseInt(playerInfo[3]),
                        Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5]),
                        Integer.parseInt(playerInfo[6]), Integer.parseInt(playerInfo[7]),
                        isRedshirt, durability, Integer.parseInt(playerInfo[11]),
                        Integer.parseInt(playerInfo[12]), Integer.parseInt(playerInfo[13])));
        } else if (playerInfo[0].equals("LB")) {
            if (playerInfo.length >= 16)
                teamLBs.add(new PlayerLB(playerInfo[1], this,
                        Integer.parseInt(playerInfo[2]), Integer.parseInt(playerInfo[3]),
                        Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5]),
                        Integer.parseInt(playerInfo[6]), Integer.parseInt(playerInfo[7]), isRedshirt, durability,
                        Integer.parseInt(playerInfo[11]), Integer.parseInt(playerInfo[12]),
                        Integer.parseInt(playerInfo[13]), Integer.parseInt(playerInfo[14]),
                        Integer.parseInt(playerInfo[15]), Integer.parseInt(playerInfo[16]),
                        Integer.parseInt(playerInfo[17]), Integer.parseInt(playerInfo[18]),
                        Integer.parseInt(playerInfo[19]), Integer.parseInt(playerInfo[20]),
                        Boolean.parseBoolean(playerInfo[21]),Integer.parseInt(playerInfo[22]), Integer.parseInt(playerInfo[23])));
            else
                teamLBs.add(new PlayerLB(playerInfo[1], this,
                        Integer.parseInt(playerInfo[2]), Integer.parseInt(playerInfo[3]),
                        Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5]),
                        Integer.parseInt(playerInfo[6]), Integer.parseInt(playerInfo[7]),
                        isRedshirt, durability, Integer.parseInt(playerInfo[11]),
                        Integer.parseInt(playerInfo[12]), Integer.parseInt(playerInfo[13])));
        } else if (playerInfo[0].equals("CB")) {
            if (playerInfo.length >= 16)
                teamCBs.add(new PlayerCB(playerInfo[1], this,
                        Integer.parseInt(playerInfo[2]), Integer.parseInt(playerInfo[3]),
                        Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5]),
                        Integer.parseInt(playerInfo[6]), Integer.parseInt(playerInfo[7]), isRedshirt, durability,
                        Integer.parseInt(playerInfo[11]), Integer.parseInt(playerInfo[12]),
                        Integer.parseInt(playerInfo[13]), Integer.parseInt(playerInfo[14]),
                        Integer.parseInt(playerInfo[15]), Integer.parseInt(playerInfo[16]),
                        Integer.parseInt(playerInfo[17]), Integer.parseInt(playerInfo[18]),
                        Integer.parseInt(playerInfo[19]), Integer.parseInt(playerInfo[20]),
                        Integer.parseInt(playerInfo[21]), Integer.parseInt(playerInfo[22]),
                        Integer.parseInt(playerInfo[23]), Boolean.parseBoolean(playerInfo[24]),
                        Integer.parseInt(playerInfo[25]), Integer.parseInt(playerInfo[26])));
            else
                teamCBs.add(new PlayerCB(playerInfo[1], this,
                        Integer.parseInt(playerInfo[2]), Integer.parseInt(playerInfo[3]),
                        Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5]),
                        Integer.parseInt(playerInfo[6]), Integer.parseInt(playerInfo[7]),
                        isRedshirt, durability, Integer.parseInt(playerInfo[11]),
                        Integer.parseInt(playerInfo[12]), Integer.parseInt(playerInfo[13])));
        } else if (playerInfo[0].equals("S")) {
            if (playerInfo.length >= 16)
                teamSs.add(new PlayerS(playerInfo[1], this,
                        Integer.parseInt(playerInfo[2]), Integer.parseInt(playerInfo[3]),
                        Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5]),
                        Integer.parseInt(playerInfo[6]), Integer.parseInt(playerInfo[7]), isRedshirt, durability,
                        Integer.parseInt(playerInfo[11]), Integer.parseInt(playerInfo[12]),
                        Integer.parseInt(playerInfo[13]), Integer.parseInt(playerInfo[14]),
                        Integer.parseInt(playerInfo[15]), Integer.parseInt(playerInfo[16]),
                        Integer.parseInt(playerInfo[17]), Integer.parseInt(playerInfo[18]),
                        Integer.parseInt(playerInfo[19]), Integer.parseInt(playerInfo[20]),
                        Boolean.parseBoolean(playerInfo[21]),Integer.parseInt(playerInfo[22]), Integer.parseInt(playerInfo[23])));
            else
                teamSs.add(new PlayerS(playerInfo[1], this,
                        Integer.parseInt(playerInfo[2]), Integer.parseInt(playerInfo[3]),
                        Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5]),
                        Integer.parseInt(playerInfo[6]), Integer.parseInt(playerInfo[7]),
                        isRedshirt, durability, Integer.parseInt(playerInfo[11]),
                        Integer.parseInt(playerInfo[12]), Integer.parseInt(playerInfo[13])));
        } else if (playerInfo[0].equals("HC")) {
            if (playerInfo.length >= 16)
                HC.add(new HeadCoach(playerInfo[1], this,
                        Integer.parseInt(playerInfo[2]), Integer.parseInt(playerInfo[3]),
                        Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5]),
                        Integer.parseInt(playerInfo[6]), Integer.parseInt(playerInfo[7]),
                        Integer.parseInt(playerInfo[8]), Integer.parseInt(playerInfo[9]),
                        Integer.parseInt(playerInfo[10]), Integer.parseInt(playerInfo[11]),
                        Integer.parseInt(playerInfo[12]), Integer.parseInt(playerInfo[13]),
                        Integer.parseInt(playerInfo[14]), Integer.parseInt(playerInfo[15]),
                        Integer.parseInt(playerInfo[16]), Integer.parseInt(playerInfo[17]),
                        Integer.parseInt(playerInfo[18]), Integer.parseInt(playerInfo[19]),
                        Integer.parseInt(playerInfo[20]), Integer.parseInt(playerInfo[21]),
                        Integer.parseInt(playerInfo[22]), Integer.parseInt(playerInfo[23])));

            else
                HC.add(new HeadCoach(playerInfo[1], this,
                        Integer.parseInt(playerInfo[2]), Integer.parseInt(playerInfo[3]),
                        Integer.parseInt(playerInfo[4]), Integer.parseInt(playerInfo[5]),
                        Integer.parseInt(playerInfo[6]), Integer.parseInt(playerInfo[7]),
                        Integer.parseInt(playerInfo[8]), Integer.parseInt(playerInfo[9]),
                        Integer.parseInt(playerInfo[10]), Integer.parseInt(playerInfo[11]),
                        Integer.parseInt(playerInfo[12]), Integer.parseInt(playerInfo[13]),
                        Integer.parseInt(playerInfo[14]), Integer.parseInt(playerInfo[15]),
                        Integer.parseInt(playerInfo[16]), Integer.parseInt(playerInfo[17]),
                        Integer.parseInt(playerInfo[18]), Integer.parseInt(playerInfo[19]),
                        Integer.parseInt(playerInfo[20]), Integer.parseInt(playerInfo[21]),
                        Integer.parseInt(playerInfo[22]), Integer.parseInt(playerInfo[23])));
        }
    }


    /**
     * Gets the recruiting class strength.
     * Adds up all the ovrs of freshman
     *
     * @return class strength as a number
     */
    public int getRecruitingClassRat() {
        double classStrength = 0;
        int numFreshman = 0;
        ArrayList<Player> allPlayers = getAllPlayers();
        for (Player p : allPlayers) {
            if (p.year == 1 && p.ratOvr > 40 && !p.isRedshirt || p.year == 0 && p.ratOvr > 40) {
                int pRat;
                if (p.ratOvr > five) pRat = 5;
                else if (p.ratOvr > four) pRat = 4;
                else if (p.ratOvr > three) pRat = 3;
                else if (p.ratOvr > two) pRat = 2;
                else pRat = 1;

                classStrength += pRat * pRat;
                numFreshman++;
            }
        }
        if (numFreshman > 0)
            return  (int)(classStrength * (classStrength/numFreshman));
        else return 0;
    }

    public void getLeagueFreshman() {
        ArrayList<Player> teamPlayers = getAllPlayers();
        for (int p = 0; p < teamPlayers.size(); ++p) {
            if (teamPlayers.get(p).year == 1 && !teamPlayers.get(p).isRedshirt) {
                league.freshman.add(teamPlayers.get(p));
            }
            if (teamPlayers.get(p).year == 0) {
                league.redshirts.add(teamPlayers.get(p));
            }
        }
    }


    /**
     * For news stories or other info gathering, setup player groups by student standing
     * Run through each type of player, add them to the appropriate year
     */
    private void groupPlayerStandingCSV() {
        for (PlayerQB p : teamQBs) {
            if (p.year == 0) teamRSs.add(p);
            else if (p.year == 1) teamFRs.add(p);
            else if (p.year == 2) teamSOs.add(p);
            else if (p.year == 3) teamJRs.add(p);
            else if (p.year == 3) teamJRs.add(p);
            else if (p.year == 4) teamSRs.add(p);
        }
        for (PlayerRB p : teamRBs) {
            if (p.year == 0) teamRSs.add(p);
            else if (p.year == 1) teamFRs.add(p);
            else if (p.year == 2) teamSOs.add(p);
            else if (p.year == 3) teamJRs.add(p);
            else if (p.year == 4) teamSRs.add(p);
        }
        for (PlayerWR p : teamWRs) {
            if (p.year == 0) teamRSs.add(p);
            else if (p.year == 1) teamFRs.add(p);
            else if (p.year == 2) teamSOs.add(p);
            else if (p.year == 3) teamJRs.add(p);
            else if (p.year == 4) teamSRs.add(p);
        }
        for (PlayerTE p : teamTEs) {
            if (p.year == 0) teamRSs.add(p);
            else if (p.year == 1) teamFRs.add(p);
            else if (p.year == 2) teamSOs.add(p);
            else if (p.year == 3) teamJRs.add(p);
            else if (p.year == 4) teamSRs.add(p);
        }
        for (PlayerK p : teamKs) {
            if (p.year == 0) teamRSs.add(p);
            else if (p.year == 1) teamFRs.add(p);
            else if (p.year == 2) teamSOs.add(p);
            else if (p.year == 3) teamJRs.add(p);
            else if (p.year == 4) teamSRs.add(p);
        }
        for (PlayerOL p : teamOLs) {
            if (p.year == 0) teamRSs.add(p);
            else if (p.year == 1) teamFRs.add(p);
            else if (p.year == 2) teamSOs.add(p);
            else if (p.year == 3) teamJRs.add(p);
            else if (p.year == 4) teamSRs.add(p);
        }
        for (PlayerDL p : teamDLs) {
            if (p.year == 0) teamRSs.add(p);
            else if (p.year == 1) teamFRs.add(p);
            else if (p.year == 2) teamSOs.add(p);
            else if (p.year == 3) teamJRs.add(p);
            else if (p.year == 4) teamSRs.add(p);
        }
        for (PlayerLB p : teamLBs) {
            if (p.year == 0) teamRSs.add(p);
            else if (p.year == 1) teamFRs.add(p);
            else if (p.year == 2) teamSOs.add(p);
            else if (p.year == 3) teamJRs.add(p);
            else if (p.year == 4) teamSRs.add(p);
        }
        for (PlayerCB p : teamCBs) {
            if (p.year == 0) teamRSs.add(p);
            else if (p.year == 1) teamFRs.add(p);
            else if (p.year == 2) teamSOs.add(p);
            else if (p.year == 3) teamJRs.add(p);
            else if (p.year == 4) teamSRs.add(p);
        }
        for (PlayerS p : teamSs) {
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
        gameLossesAgainst = new ArrayList<Team>();
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
     * Updates team history.
     */
    public void updateTeamHistory() {
        int[] newPres = calcSeasonPrestige();
        String histYear;
        if (newPres[0] > teamPrestige)
            histYear = league.getYear() + ": #" + rankTeamPollScore + " " + name + " (" + wins + "-" + losses + ") "
                    + confChampion + " " + semiFinalWL + natChampWL + " Prs: " + newPres[0] + " (+" + (newPres[0] - teamPrestige) + ")";
        else
            histYear = league.getYear() + ": #" + rankTeamPollScore + " " + name + " (" + wins + "-" + losses + ") "
                    + confChampion + " " + semiFinalWL + natChampWL + " Prs: " + newPres[0] + " (" + (newPres[0] - teamPrestige) + ")";

        for (int i = 12; i < gameSchedule.size(); ++i) {
            Game g = gameSchedule.get(i);
            histYear += ">" + g.gameName + ": ";
            String[] gameSum = getGameSummaryStr(i);
            histYear += gameSum[1] + " " + gameSum[2];
        }

        teamHistory.add(histYear);
    }

    public void updateCoachHistory() {
        int[] newPres = calcSeasonPrestige();
        String histYear;
        if (newPres[0] > teamPrestige)
            histYear = league.getYear() + ": #" + rankTeamPollScore + " " + name + " (" + wins + "-" + losses + ") "
                    + confChampion + " " + semiFinalWL + natChampWL + " Prs: " + newPres[0] + " (+" + (newPres[0] - teamPrestige) + ")";
        else
            histYear = league.getYear() + ": #" + rankTeamPollScore + " " + name + " (" + wins + "-" + losses + ") "
                    + confChampion + " " + semiFinalWL + natChampWL + " Prs: " + newPres[0] + " (" + (newPres[0] - teamPrestige) + ")";

        for (int i = 12; i < gameSchedule.size(); ++i) {
            Game g = gameSchedule.get(i);
            histYear += ">" + g.gameName + ": ";
            String[] gameSum = getGameSummaryStr(i);
            histYear += gameSum[1] + " " + gameSum[2];
        }
        if (!HC.isEmpty()) {
            HC.get(0).history.add(histYear);
        }
    }

    /**
     * Gets the team history as a String array
     *
     * @return team history
     */
    public String[] getTeamHistoryList() {
        String[] hist = new String[teamHistory.size() + 6];
        hist[0] = "Location: " + league.getRegion(location);
        hist[1] = "Overall W-L: " + totalWins + "-" + totalLosses;
        hist[2] = "Conf Champ Record: " + totalCCs + "-" + totalCCLosses;
        hist[3] = "Bowl Game Record: " + totalBowls + "-" + totalBowlLosses;
        hist[4] = "National Champ Record: " + totalNCs + "-" + totalNCLosses;
        hist[5] = " ";
        for (int i = 0; i < teamHistory.size(); ++i) {
            hist[i + 6] = teamHistory.get(i);
        }
        return hist;
    }


    public void disciplinePlayer() {
        playersDis = new ArrayList<>();
        checkSuspensionPosition(teamQBs, startersQB + subQB);
        checkSuspensionPosition(teamRBs, startersRB + subRB);
        checkSuspensionPosition(teamWRs, startersWR + subWR);
        checkSuspensionPosition(teamTEs, startersTE + subTE);
        checkSuspensionPosition(teamOLs, startersOL + subOL);
        checkSuspensionPosition(teamKs, startersK + subK);
        checkSuspensionPosition(teamDLs, startersDL + subDL);
        checkSuspensionPosition(teamLBs, startersLB + subLB);
        checkSuspensionPosition(teamCBs, startersCB + subCB);
        checkSuspensionPosition(teamSs, startersS + subS);

        if (playersDis.size() > 0) {
            int randomPlayer = (int) (Math.random() * playersDis.size());
            suspendPlayer(playersDis.get(randomPlayer));
        }
    }

    public void suspendPlayer(Player player) {
        String[] issue = {"Academics", "Fighting", "DUI", "Skipping Class", "Skipping Practice", "Failed Drug Test", "Academics", "Excessive Partying", "Practice Brawl"};
        int duration = (int)(Math.random()*(65 - player.personality)/2);
        if (duration == 0) duration = 1;
        String description = issue[(int) (Math.random() * issue.length)];
        if (player.personality*Math.random() < Math.random()*HC.get(0).ratDiscipline) {
            player.isSuspended = true;
            player.ratPot -= duration / 1.5;
            player.ratFootIQ -= duration * 2;
            player.weeksSuspended = duration;
            player.troubledTimes++;
            if (player.ratOvr > 77) {
                player.team.league.newsStories.get(player.team.league.currentWeek + 1).add("Star Player Suspended>" + player.team.name + "'s star " + player.position + ", " + player.name + " was suspended from the team today. The team cited the reason as: " + description
                        + ". The player will be suspended for " + duration + " weeks.");
            }

            sortPlayers();

        }
    }

    private void checkSuspensionPosition(ArrayList<? extends Player> players, int numStarters) {
        int numInjured = 0;

        for (Player p : players) {
            if (p.injury != null && !p.isSuspended) {
                numInjured++;
            }
        }

        // Only injure if there are people left to injure
        if (numInjured < numStarters) {
            for (int i = 0; i < numStarters; ++i) {
                Player p = players.get(i);
                if (p.personality < 60) {
                    playersDis.add(p);
                }
            }
        }
    }

    public void updateSuspensions() {
        ArrayList<Player> allPlayers = getAllPlayers();
        for (int i = 0; i < allPlayers.size(); ++i ) {
            if (allPlayers.get(i).isSuspended) {
                allPlayers.get(i).weeksSuspended--;
                if (allPlayers.get(i).weeksSuspended < 1) {
                    allPlayers.get(i).isSuspended = false;
                    sortPlayers();
                }
            }
        }
    }

    /**
     * May injure players.
     * Guaranteed not to injure more than the amount of starters for each position.
     */
    public void checkForInjury() {
        playersInjured = new ArrayList<>();
        playersRecovered = new ArrayList<>();
        checkInjuryPosition(teamQBs, startersQB + subQB);
        checkInjuryPosition(teamRBs, startersRB + subRB);
        checkInjuryPosition(teamWRs, startersWR + subWR);
        checkInjuryPosition(teamTEs, startersTE + subTE);
        checkInjuryPosition(teamOLs, startersOL + subOL);
        checkInjuryPosition(teamKs, startersK + subK);
        checkInjuryPosition(teamDLs, startersDL + subDL);
        checkInjuryPosition(teamLBs, startersLB + subLB);
        checkInjuryPosition(teamCBs, startersCB + subCB);
        checkInjuryPosition(teamSs, startersS + subS);
        
    }

    private void checkInjuryPosition(ArrayList<? extends Player> players, int numStarters) {
        int numInjured = 0;

        for (Player p : players) {
            if (p.injury != null && !p.isSuspended && !p.isTransfer) {
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
                if (Math.random() < Math.pow(1 - (double) p.ratDur / 100, 3) && numInjured < numStarters) {
                    // injury!
                    p.injury = new Injury(p);
                    playersInjured.add(p);
                    playersInjuredAll.add(p);
                    numInjured++;
                }
            }
        }

        if (numInjured > 0) Collections.sort(players, new CompPlayer());
    }

    /**
     * Gets a list of all the players that were injured that week.
     *
     * @return list of players in string
     */
    public String[] getInjuryReport() {

        if (playersInjuredAll.size() > 0 || playersRecovered.size() > 0) {
            String[] injuries;

            if (playersRecovered.size() > 0)
                injuries = new String[playersInjuredAll.size() + playersRecovered.size() + 1];
            else injuries = new String[playersInjuredAll.size()];

            for (int i = 0; i < playersInjuredAll.size(); ++i) {
                injuries[i] = playersInjuredAll.get(i).getPosNameYrOvrPot_Str();
            }

            if (playersRecovered.size() > 0) {
                injuries[playersInjuredAll.size()] = "Players Recovered from Injuries:> ";
                for (int i = 0; i < playersRecovered.size(); ++i) {
                    injuries[playersInjuredAll.size() + i + 1] = playersRecovered.get(i).getPosNameYrOvrPot_Str();
                }
            }

            return injuries;
        } else return null;
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

    public HeadCoach getHC(int depth) {
        return HC.get(0);
    }

    public PlayerQB getQB(int depth) {
        if (depth < teamQBs.size() && depth >= 0) {
            return teamQBs.get(depth);
        } else {
            return teamQBs.get(0);
        }
    }

    public PlayerRB getRB(int depth) {
        if (depth < teamRBs.size() && depth >= 0) {
            return teamRBs.get(depth);
        } else {
            return teamRBs.get(0);
        }
    }

    public PlayerWR getWR(int depth) {
        if (depth < teamWRs.size() && depth >= 0) {
            return teamWRs.get(depth);
        } else {
            return teamWRs.get(0);
        }
    }

    public PlayerTE getTE(int depth) {
        if (depth < teamTEs.size() && depth >= 0) {
            return teamTEs.get(depth);
        } else {
            return teamTEs.get(0);
        }
    }

    public PlayerK getK(int depth) {
        if (depth < teamKs.size() && depth >= 0) {
            return teamKs.get(depth);
        } else {
            return teamKs.get(0);
        }
    }

    public PlayerOL getOL(int depth) {
        if (depth < teamOLs.size() && depth >= 0) {
            return teamOLs.get(depth);
        } else {
            return teamOLs.get(0);
        }
    }

    public PlayerDL getDL(int depth) {
        if (depth < teamDLs.size() && depth >= 0) {
            return teamDLs.get(depth);
        } else {
            return teamDLs.get(0);
        }
    }

    public PlayerLB getLB(int depth) {
        if (depth < teamLBs.size() && depth >= 0) {
            return teamLBs.get(depth);
        } else {
            return teamLBs.get(0);
        }
    }

    public PlayerCB getCB(int depth) {
        if (depth < teamCBs.size() && depth >= 0) {
            return teamCBs.get(depth);
        } else {
            return teamCBs.get(0);
        }
    }

    public PlayerS getS(int depth) {
        if (depth < teamSs.size() && depth >= 0) {
            return teamSs.get(depth);
        } else {
            return teamSs.get(0);
        }
    }




    /**
     * Get comma separated value of the team stats and their rankings.
     *
     * @return String of CSV stat,name,ranking
     */
    public String getTeamStatsStrCSV() {
        StringBuilder ts0 = new StringBuilder();

        ArrayList<Team> confTeams = new ArrayList<>();
        for (Conference c : league.conferences) {
            if (c.confName.equals(conference)) {
                confTeams.addAll(c.confTeams);
                Collections.sort(confTeams, new CompTeamConfWins());
                int confRank = 11;
                for (int i = 0; i < confTeams.size(); ++i) {
                    if (confTeams.get(i).equals(this)) {
                        confRank = i + 1;
                        break;
                    }
                }
                ts0.append(getConfWins() + "-" + getConfLosses() + ",");
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

        ts0.append(teamPoints / numGames() + ",");
        ts0.append("Points" + ",");
        ts0.append(getRankStr(rankTeamPoints) + "%\n");

        ts0.append(teamOppPoints / numGames() + ",");
        ts0.append("Opp Points" + ",");
        ts0.append(getRankStr(rankTeamOppPoints) + "%\n");

        ts0.append(teamYards / numGames() + ",");
        ts0.append("Yards" + ",");
        ts0.append(getRankStr(rankTeamYards) + "%\n");

        ts0.append(teamOppYards / numGames() + ",");
        ts0.append("Opp Yards" + ",");
        ts0.append(getRankStr(rankTeamOppYards) + "%\n");

        ts0.append(teamPassYards / numGames() + ",");
        ts0.append("Pass Yards" + ",");
        ts0.append(getRankStr(rankTeamPassYards) + "%\n");

        ts0.append(teamRushYards / numGames() + ",");
        ts0.append("Rush Yards" + ",");
        ts0.append(getRankStr(rankTeamRushYards) + "%\n");

        ts0.append(teamOppPassYards / numGames() + ",");
        ts0.append("Opp Pass YPG" + ",");
        ts0.append(getRankStr(rankTeamOppPassYards) + "%\n");

        ts0.append(teamOppRushYards / numGames() + ",");
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
     *
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
     *
     * @return String of season summary
     */
    public String seasonSummaryStr() {
        int prestigePts[] = calcSeasonPrestige();

        String summary = "Your team, " + name + ", finished the season ranked #" + rankTeamPollScore + " with " + wins + " wins and " + losses + " losses.";

        if (natChampWL.equals("NCW")) {
            summary += "\n\nYou won the National Championship! Recruits want to play for winners and you have proved that you are one. You gain " + prestigePts[3] + " prestige points!";
        }

        if (prestigePts[5] > 0) {
            if (wonRivalryGame) {
                summary += "\n\nCongrats on beating your rival, " + rivalTeam + "! You will gain " + prestigePts[1] + " prestige points in the off-season!";
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

        if (disciplinePts < 0) {
            summary += "\n\nYour team had some trouble with disciplinary violations this season and dropped " + disciplinePts + " prestige points.";
        } else if (disciplinePts > 0) {
            summary += "\n\nYour team stayed out of trouble this season and bonded together. Your team gained " + disciplinePts + " prestige points.";
        }

        if (this == league.savePenalized || this == league.savePenalized2 || this == league.savePenalized3) {
            summary += "\n\nYour team had penalties placed on it by the collegiate administration this season. Recruiting budgets were reduced due to this.";
        } else if ((prestigePts[0] - teamPrestige) > 0) {
            summary += "\n\nGreat job coach! You exceeded expectations and gained " + (prestigePts[0] - teamPrestige) + " prestige points! This will help your recruiting.";
        } else if ((prestigePts[0] - teamPrestige) < 0) {
            summary += "\n\nA bit of a down year, coach? You fell short of expectations and lost " + (teamPrestige - prestigePts[0]) + " prestige points. This will hurt your recruiting.";
        } else {
            summary += "\n\nWell, your team performed exactly how many expected. This won't hurt or help recruiting, but try to improve next year!";
        }

        summary += "\n\nCurrent Prestige: " + teamPrestige + " New Prestige: " + prestigePts[0];

        if (newContract && league.isCareerMode()) {
            summary += "\n\nCongratulations! You've been awarded with a contract extension of " + HC.get(0).contractLength + " years.";
        } else if (fired) {
            summary += "\n\nDue to failing to raise the team prestige during your contract length, you've been terminated from this position.";
        }
        return summary;
    }


    /**
     * Gets player name or detail strings for displaying in the roster tab via expandable list.
     * Should be separated by a '>' from left text and right text.
     *
     * @return list of players with their name,ovr,por,etc
     */
    public List<String> getPlayerStatsExpandListStr() {
        ArrayList<String> pList = new ArrayList<String>();

        pList.add(getHC(0).getHCString());

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
     *
     * @param playerStatsGroupHeaders list of players by name,overall,pot,etc
     * @return mapping of each player to their detail ratings
     */
    public Map<String, List<String>> getPlayerStatsExpandListMap(List<String> playerStatsGroupHeaders) {
        Map<String, List<String>> playerStatsMap = new LinkedHashMap<String, List<String>>();

        String ph; //player header
        ArrayList<String> pInfoList;


        //Head Coach
        ph = playerStatsGroupHeaders.get(0);
        playerStatsMap.put(ph, getHC(0).getDetailStatsList(numGames()));

        //QB
        ph = playerStatsGroupHeaders.get(1);
        playerStatsMap.put(ph, getQB(0).getDetailStatsList(numGames()));

        for (int i = 2; i < 4; ++i) {
            ph = playerStatsGroupHeaders.get(i);
            playerStatsMap.put(ph, getRB(i - 2).getDetailStatsList(numGames()));
        }

        for (int i = 4; i < 7; ++i) {
            ph = playerStatsGroupHeaders.get(i);
            playerStatsMap.put(ph, getWR(i - 4).getDetailStatsList(numGames()));
        }

        ph = playerStatsGroupHeaders.get(7);
        playerStatsMap.put(ph, getTE(0).getDetailStatsList(numGames()));

        for (int i = 8; i < 13; ++i) {
            ph = playerStatsGroupHeaders.get(i);
            playerStatsMap.put(ph, getOL(i - 8).getDetailStatsList(numGames()));
        }

        ph = playerStatsGroupHeaders.get(13);
        playerStatsMap.put(ph, getK(0).getDetailStatsList(numGames()));

        for (int i = 14; i < 18; ++i) {
            ph = playerStatsGroupHeaders.get(i);
            playerStatsMap.put(ph, getDL(i - 14).getDetailStatsList(numGames()));
        }
        for (int i = 18; i < 21; ++i) {
            ph = playerStatsGroupHeaders.get(i);
            playerStatsMap.put(ph, getLB(i - 18).getDetailStatsList(numGames()));
        }
        for (int i = 21; i < 24; ++i) {
            ph = playerStatsGroupHeaders.get(i);
            playerStatsMap.put(ph, getCB(i - 21).getDetailStatsList(numGames()));
        }
        ph = playerStatsGroupHeaders.get(24);
        playerStatsMap.put(ph, getS(0).getDetailStatsList(numGames()));

        //Bench
        ph = playerStatsGroupHeaders.get(25);
        ArrayList<String> benchStr = new ArrayList<>();
        for (int i = 1; i < teamQBs.size(); ++i) {
            benchStr.add(getQB(i).getPosNameYrOvrPot_Str());
        }
        for (int i = 2; i < teamRBs.size(); ++i) {
            benchStr.add(getRB(i).getPosNameYrOvrPot_Str());
        }
        for (int i = 3; i < teamWRs.size(); ++i) {
            benchStr.add(getWR(i).getPosNameYrOvrPot_Str());
        }
        for (int i = 1; i < teamTEs.size(); ++i) {
            benchStr.add(getTE(i).getPosNameYrOvrPot_Str());
        }
        for (int i = 5; i < teamOLs.size(); ++i) {
            benchStr.add(getOL(i).getPosNameYrOvrPot_Str());
        }
        for (int i = 1; i < teamKs.size(); ++i) {
            benchStr.add(getK(i).getPosNameYrOvrPot_Str());
        }
        for (int i = 4; i < teamDLs.size(); ++i) {
            benchStr.add(getDL(i).getPosNameYrOvrPot_Str());
        }
        for (int i = 3; i < teamLBs.size(); ++i) {
            benchStr.add(getLB(i).getPosNameYrOvrPot_Str());
        }
        for (int i = 3; i < teamCBs.size(); ++i) {
            benchStr.add(getCB(i).getPosNameYrOvrPot_Str());
        }
        for (int i = 1; i < teamSs.size(); ++i) {
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
     *
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

    /**
     * Get rank string of the user (no longer used?)
     *
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
     *
     * @return number of games played
     */
    public int numGames() {
        if (wins + losses > 0) {
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
     *
     * @return number of in-conf wins
     */
    public int getConfWins() {
        int confWins = 0;
        Game g;
        for (int i = 0; i < gameWLSchedule.size(); ++i) {
            g = gameSchedule.get(i);
            if (g.gameName.equals("Conference") || g.gameName.equals("Rivalry Game")) {
                // in conference game, see if was won
                if (g.homeTeam == this && g.homeScore > g.awayScore) {
                    confWins++;
                } else if (g.awayTeam == this && g.homeScore < g.awayScore) {
                    confWins++;
                }
            }
        }
        return confWins;
    }

    /**
     * Gets the number of in-conference losses, used for CCG rankings
     *
     * @return number of in-conf losses
     */
    public int getConfLosses() {
        int confLosses = 0;
        Game g;
        for (int i = 0; i < gameWLSchedule.size(); ++i) {
            g = gameSchedule.get(i);
            if (g.gameName.equals("Conference") || g.gameName.equals("Rivalry Game")) {
                // in conference game, see if was won
                if (g.homeTeam == this && g.homeScore < g.awayScore) {
                    confLosses++;
                } else if (g.awayTeam == this && g.homeScore > g.awayScore) {
                    confLosses++;
                }
            }
        }
        return confLosses;
    }

    //Team Names for main display spinner
    public String strRankTeamRecord() {
        return "#" + rankTeamPollScore + " " + name + " (" + wins + "-" + losses + ")";
    }

    /**
     * Str rep of team, no bowl results
     *
     * @return ranking abbr (w-l)
     */
    public String strRep() {
        return "[" + rankTeamPollScore + "] " + abbr;
    }

    /**
     * Str rep of team, with bowl results
     *
     * @return ranking abbr (w-l) BW
     */
    public String strRepWithBowlResults() {
        return /*"#" + rankTeamPollScore + " " + */name + " (" + wins + "-" + losses + ") " + confChampion + " " + semiFinalWL + natChampWL;
    }

    /**
     * Str rep of team, with prestige
     *
     * @return ranking abbr (Pres: XX)
     */
    public String strRepWithPrestige() {
        return /*"#" + rankTeamPollScore + " " + */name + " [" + teamPrestige + "]";
    }

    public String strTeamRecord() {
        return "(" + wins + "-" + losses + ")";
    }

    /**
     * Get what happened during the week for the team
     *
     * @return name W/L gameSum, new poll rank #1
     */
    public String weekSummaryStr() {
        int i = wins + losses - 1;
        Game g = gameSchedule.get(i);
        String gameSummary = gameWLSchedule.get(i) + " " + gameSummaryStr(g);
        String rivalryGameStr = "";
        if (g.gameName.equals("Rivalry Game")) {
            if (gameWLSchedule.get(i).equals("W")) rivalryGameStr = "Won against Rival!\n";
            else rivalryGameStr = "Lost against Rival!\n";
        }
        return rivalryGameStr + name + " " + gameSummary + "\nNew poll rank: #" + rankTeamPollScore + " " + name + " (" + wins + "-" + losses + ")";
    }

    /**
     * Gets the one-line summary of a game
     *
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
     *
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
     *
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
     *
     * @return string of everyone who is graduating sorted by position
     */
    public String getGraduatingPlayersStr() {
        StringBuilder sb = new StringBuilder();
        for (Player p : playersLeaving) {
            sb.append(p.getPosNameYrOvrPot_OneLine() + "\n");
        }
        return sb.toString();
    }

    public String[] getGradPlayersList() {
        String[] playersLeavingList = new String[playersLeaving.size()];
        for (int i = 0; i < playersLeavingList.length; ++i) {
            playersLeavingList[i] = playersLeaving.get(i).getPosNameYrOvrPot_Str();
        }
        for (int i = 0; i < playersTransferring.size(); ++i) {
            playersLeavingList[i] = playersTransferring.get(i).getPosNameYrOvrPotTra_Str();
        }
        return playersLeavingList;
    }




    /**
     * Save all the recruits into a string to be used by RecruitingActivity
     *
     * @return String of all the recruits
     */
    public String getRecruitsInfoSaveFile() {
        int rating = calcMaxRecruitRating();

        StringBuilder sb = new StringBuilder();
        PlayerQB[] qbs = getQBRecruits(rating);
        for (PlayerQB qb : qbs) {
            sb.append("QB," + qb.name + "," + qb.year + "," + qb.ratPot + "," + qb.ratFootIQ + "," +
                    qb.ratPassPow + "," + qb.ratPassAcc + "," + qb.ratEvasion + "," + qb.ratOvr + "," + qb.cost + "," + qb.ratDur + "," + qb.ratSpeed + "," + qb.region + "," + qb.personality + "%\n");
        }
        PlayerRB[] rbs = getRBRecruits(rating);
        for (PlayerRB rb : rbs) {
            sb.append("RB," + rb.name + "," + rb.year + "," + rb.ratPot + "," + rb.ratFootIQ + "," +
                    rb.ratRushPower + "," + rb.ratSpeed + "," + rb.ratEvasion + "," + rb.ratOvr + "," + rb.cost + "," + rb.ratDur + "," + rb.ratCatch + "," + rb.region + "," + rb.personality + "%\n");
        }
        PlayerWR[] wrs = getWRRecruits(rating);
        for (PlayerWR wr : wrs) {
            sb.append("WR," + wr.name + "," + wr.year + "," + wr.ratPot + "," + wr.ratFootIQ + "," +
                    wr.ratCatch + "," + wr.ratSpeed + "," + wr.ratEvasion + "," + wr.ratOvr + "," + wr.cost + "," + wr.ratDur + "," + wr.ratJump + "," + wr.region + "," + wr.personality + "%\n");
        }
        PlayerTE[] tes = getTERecruits(rating);
        for (PlayerTE te : tes) {
            sb.append("TE," + te.name + "," + te.year + "," + te.ratPot + "," + te.ratFootIQ + "," +
                    te.ratCatch + "," + te.ratRunBlock + "," + te.ratEvasion + "," + te.ratOvr + "," + te.cost + "," + te.ratDur + "," + te.ratSpeed + "," + te.region + "," + te.personality + "%\n");
        }
        PlayerK[] ks = getKRecruits(rating);
        for (PlayerK k : ks) {
            sb.append("K," + k.name + "," + k.year + "," + k.ratPot + "," + k.ratFootIQ + "," +
                    k.ratKickPow + "," + k.ratKickAcc + "," + k.ratKickFum + "," + k.ratOvr + "," + k.cost + "," + k.ratDur + "," + k.ratPressure + "," + k.region + "," + k.personality + "%\n");
        }
        PlayerOL[] ols = getOLRecruits(rating);
        for (PlayerOL ol : ols) {
            sb.append("OL," + ol.name + "," + ol.year + "," + ol.ratPot + "," + ol.ratFootIQ + "," +
                    ol.ratStrength + "," + ol.ratRunBlock + "," + ol.ratPassBlock + "," + ol.ratOvr + "," + ol.cost + "," + ol.ratDur + "," + ol.ratAwareness + "," + ol.region + "," + ol.personality + "%\n");
        }
        PlayerDL[] DLs = getDLRecruits(rating);
        for (PlayerDL DL : DLs) {
            sb.append("DL," + DL.name + "," + DL.year + "," + DL.ratPot + "," + DL.ratFootIQ + "," +
                    DL.ratStrength + "," + DL.ratRunStop + "," + DL.ratPassRush + "," + DL.ratOvr + "," + DL.cost + "," + DL.ratDur + "," + DL.ratTackle + "," + DL.region + "," + DL.personality + "%\n");
        }
        PlayerLB[] lbs = getLBRecruits(rating);
        for (PlayerLB lb : lbs) {
            sb.append("LB," + lb.name + "," + lb.year + "," + lb.ratPot + "," + lb.ratFootIQ + "," +
                    lb.ratCoverage + "," + lb.ratRunStop + "," + lb.ratTackle + "," + lb.ratOvr + "," + lb.cost + "," + lb.ratDur + "," + lb.ratSpeed  + "," + lb.region + "," + lb.personality + "%\n");
        }
        PlayerCB[] cbs = getCBRecruits(rating);
        for (PlayerCB cb : cbs) {
            sb.append("CB," + cb.name + "," + cb.year + "," + cb.ratPot + "," + cb.ratFootIQ + "," +
                    cb.ratCoverage + "," + cb.ratSpeed + "," + cb.ratTackle + "," + cb.ratOvr + "," + cb.cost + "," + cb.ratDur + "," + cb.ratJump + "," + cb.region + "," + cb.personality + "%\n");
        }
        PlayerS[] ss = getSRecruits(rating);
        for (PlayerS s : ss) {
            sb.append("S," + s.name + "," + s.year + "," + s.ratPot + "," + s.ratFootIQ + "," +
                    s.ratCoverage + "," + s.ratSpeed + "," + s.ratTackle + "," + s.ratOvr + "," + s.cost + "," + s.ratDur + "," + s.ratRunStop + "," + s.region + "," + s.personality + "%\n");
        }

        return sb.toString();
    }

    /**
     * Save all the current players into a string to be loaded from later
     *
     * @return string of all the players in csv form
     */
    public String getPlayerInfoSaveFile() {
        StringBuilder sb = new StringBuilder();
        for (HeadCoach hc : HC) {
            sb.append("HC," + hc.name + "," + hc.age + "," + hc.year + "," + hc.contractYear + "," + hc.contractLength + "," + hc.ratPot + "," +
                    hc.ratOff + "," + hc.ratDef + "," + hc.ratTalent + "," + hc.ratDiscipline + "," + hc.offStrat + "," + hc.defStrat + "," + hc.baselinePrestige + "," +
                    hc.wins + "," + hc.losses + "," + hc.bowlwins + "," + hc.bowllosses + "," + hc.confchamp + "," + hc.natchamp + "," + hc.allconference + "," +
                    hc.allamericans + "," + hc.confAward + "," + hc.awards + "%\n");
        }
        for (PlayerQB qb : teamQBs) {
            sb.append("QB," + qb.name + "," + qb.year + "," + qb.ratPot + "," + qb.ratFootIQ + "," +
                    qb.ratPassPow + "," + qb.ratPassAcc + "," + qb.ratEvasion + "," + qb.ratOvr + "," + qb.ratImprovement + "," + qb.ratDur + "," + qb.ratSpeed + "," +
                    qb.careerGamesPlayed + "," + qb.careerPassAtt + "," + qb.careerPassComp + "," + qb.careerTDs + "," + qb.careerInt + "," +
                    qb.careerPassYards + "," + qb.careerSacked + "," + qb.careerRushAtt + "," + qb.careerRushYards + "," + qb.careerRushTD + "," + qb.careerFumbles + "," + qb.careerHeismans +
                    "," + qb.careerAllAmerican + "," + qb.careerAllConference + "," + qb.careerWins + "," + qb.isTransfer + "," + qb.region + "," + qb.personality + "%\n");
        }
        for (PlayerRB rb : teamRBs) {
            sb.append("RB," + rb.name + "," + rb.year + "," + rb.ratPot + "," + rb.ratFootIQ + "," +
                    rb.ratRushPower + "," + rb.ratSpeed + "," + rb.ratEvasion + "," + rb.ratOvr + "," + rb.ratImprovement + "," + rb.ratDur + "," + rb.ratCatch + "," +
                    rb.careerGamesPlayed + "," + rb.careerRushAtt + "," + rb.careerRushYards + "," + rb.careerTDs + "," + rb.careerFumbles + "," + rb.careerReceptions + "," + rb.careerRecYards + "," + rb.careerRecTD + "," +
                    rb.careerHeismans + "," + rb.careerAllAmerican + "," + rb.careerAllConference + "," + rb.careerWins + "," + rb.isTransfer + "," + rb.region + "," + rb.personality + "%\n");
        }
        for (PlayerWR wr : teamWRs) {
            sb.append("WR," + wr.name + "," + wr.year + "," + wr.ratPot + "," + wr.ratFootIQ + "," +
                    wr.ratCatch + "," + wr.ratSpeed + "," + wr.ratEvasion + "," + wr.ratOvr + "," + wr.ratImprovement + "," + wr.ratDur + "," + wr.ratJump + "," +
                    wr.careerGamesPlayed + "," + wr.careerTargets + "," + wr.careerReceptions + "," + wr.careerRecYards + "," + wr.careerTD + "," +
                    wr.careerDrops + "," + wr.careerFumbles + "," + wr.careerHeismans + "," + wr.careerAllAmerican + "," + wr.careerAllConference + "," + wr.careerWins + "," + wr.isTransfer + "," + wr.region + "," + wr.personality + "%\n");
        }
        for (PlayerTE te : teamTEs) {
            sb.append("TE," + te.name + "," + te.year + "," + te.ratPot + "," + te.ratFootIQ + "," +
                    te.ratCatch + "," + te.ratRunBlock + "," + te.ratEvasion + "," + te.ratOvr + "," + te.ratImprovement + "," + te.ratDur + "," + te.ratSpeed + "," +
                    te.careerGamesPlayed + "," + te.careerTargets + "," + te.careerReceptions + "," + te.careerRecYards + "," + te.careerTD + "," +
                    te.careerDrops + "," + te.careerFumbles + "," + te.careerHeismans + "," + te.careerAllAmerican + "," + te.careerAllConference + "," + te.careerWins  + "," + te.isTransfer + "," + te.region + "," + te.personality + "%\n");
        }
        for (PlayerOL ol : teamOLs) {
            sb.append("OL," + ol.name + "," + ol.year + "," + ol.ratPot + "," + ol.ratFootIQ + "," +
                    ol.ratStrength + "," + ol.ratRunBlock + "," + ol.ratPassBlock + "," + ol.ratOvr + "," + ol.ratImprovement + "," + ol.ratDur + "," + ol.ratAwareness + "," +
                    ol.careerGamesPlayed + "," + ol.careerHeismans + "," + ol.careerAllAmerican + "," + ol.careerAllConference + "," + ol.careerWins  + "," + ol.isTransfer + "," + ol.region + "," + ol.personality + "%\n");
        }
        for (PlayerK k : teamKs) {
            sb.append("K," + k.name + "," + k.year + "," + k.ratPot + "," + k.ratFootIQ + "," +
                    k.ratKickPow + "," + k.ratKickAcc + "," + k.ratKickFum + "," + k.ratOvr + "," + k.ratImprovement + "," + k.ratDur + "," + k.ratPressure + "," +
                    k.careerGamesPlayed + "," + k.careerXPAtt + "," + k.careerXPMade + "," + k.careerFGAtt + "," + k.careerFGMade + "," +
                    k.careerHeismans + "," + k.careerAllAmerican + "," + k.careerAllConference + "," + k.careerWins  + "," + k.isTransfer + "," + k.region + "," + k.personality + "%\n");
        }
        for (PlayerDL dl : teamDLs) {
            sb.append("DL," + dl.name + "," + dl.year + "," + dl.ratPot + "," + dl.ratFootIQ + "," +
                    dl.ratStrength + "," + dl.ratRunStop + "," + dl.ratPassRush + "," + dl.ratOvr + "," + dl.ratImprovement + "," + dl.ratDur + "," + dl.ratTackle + "," +
                    dl.careerGamesPlayed + "," + dl.careerTackles + "," + dl.careerSacks + "," + dl.careerFumbles + "," + dl.careerInts + "," +
                    dl.careerHeismans + "," + dl.careerAllAmerican + "," + dl.careerAllConference + "," + dl.careerWins  + "," + dl.isTransfer + "," + dl.region + "," + dl.personality + "%\n");
        }
        for (PlayerLB lb : teamLBs) {
            sb.append("LB," + lb.name + "," + lb.year + "," + lb.ratPot + "," + lb.ratFootIQ + "," +
                    lb.ratCoverage + "," + lb.ratRunStop + "," + lb.ratTackle + "," + lb.ratOvr + "," + lb.ratImprovement + "," + lb.ratDur + "," + lb.ratSpeed + "," +
                    lb.careerGamesPlayed + "," + lb.careerTackles + "," + lb.careerSacks + "," + lb.careerFumbles + "," + lb.careerInts + "," +
                    lb.careerHeismans + "," + lb.careerAllAmerican + "," + lb.careerAllConference + "," + lb.careerWins  + "," + lb.isTransfer + "," + lb.region + "," + lb.personality + "%\n");
        }
        for (PlayerCB cb : teamCBs) {
            sb.append("CB," + cb.name + "," + cb.year + "," + cb.ratPot + "," + cb.ratFootIQ + "," +
                    cb.ratCoverage + "," + cb.ratSpeed + "," + cb.ratTackle + "," + cb.ratOvr + "," + cb.ratImprovement + "," + cb.ratDur + "," + cb.ratJump + "," +
                    cb.careerGamesPlayed + "," + cb.careerTackles + "," + cb.careerSacks + "," + cb.careerFumbles + "," + cb.careerInts + "," + cb.careerTargets + "," + cb.careerIncomplete + "," + cb.careerDefended + "," +
                    cb.careerHeismans + "," + cb.careerAllAmerican + "," + cb.careerAllConference + "," + cb.careerWins  + "," + cb.isTransfer + "," + cb.region + "," + cb.personality + "%\n");
        }
        for (PlayerS s : teamSs) {
            sb.append("S," + s.name + "," + s.year + "," + s.ratPot + "," + s.ratFootIQ + "," +
                    s.ratCoverage + "," + s.ratSpeed + "," + s.ratTackle + "," + s.ratOvr + "," + s.ratImprovement + "," + s.ratDur + "," + s.ratRunStop + "," +
                    s.careerGamesPlayed + "," + s.careerTackles + "," + s.careerSacks + "," + s.careerFumbles + "," + s.careerInts + "," +
                    s.careerHeismans + "," + s.careerAllAmerican + "," + s.careerAllConference + "," + s.careerWins  + "," + s.isTransfer + "," + s.region + "," + s.personality + "%\n");
        }

        return sb.toString();
    }


    /**
     * Set the starters for a particular position.
     *
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
                    teamQBs.add((PlayerQB) p);
                }
                Collections.sort(teamQBs, new CompPlayer());
                for (PlayerQB oldP : oldQBs) {
                    if (!teamQBs.contains(oldP)) teamQBs.add(oldP);
                }
                break;
            case 1:
                ArrayList<PlayerRB> oldRBs = new ArrayList<>();
                oldRBs.addAll(teamRBs);
                teamRBs.clear();
                for (Player p : starters) {
                    teamRBs.add((PlayerRB) p);
                }
                Collections.sort(teamRBs, new CompPlayer());
                for (PlayerRB oldP : oldRBs) {
                    if (!teamRBs.contains(oldP)) teamRBs.add(oldP);
                }
                break;
            case 2:
                ArrayList<PlayerWR> oldWRs = new ArrayList<>();
                oldWRs.addAll(teamWRs);
                teamWRs.clear();
                for (Player p : starters) {
                    teamWRs.add((PlayerWR) p);
                }
                Collections.sort(teamWRs, new CompPlayer());
                for (PlayerWR oldP : oldWRs) {
                    if (!teamWRs.contains(oldP)) teamWRs.add(oldP);
                }
                break;
            case 3:
                ArrayList<PlayerTE> oldTEs = new ArrayList<>();
                oldTEs.addAll(teamTEs);
                teamTEs.clear();
                for (Player p : starters) {
                    teamTEs.add((PlayerTE) p);
                }
                Collections.sort(teamTEs, new CompPlayer());
                for (PlayerTE oldP : oldTEs) {
                    if (!teamTEs.contains(oldP)) teamTEs.add(oldP);
                }
                break;
            case 4:
                ArrayList<PlayerOL> oldOLs = new ArrayList<>();
                oldOLs.addAll(teamOLs);
                teamOLs.clear();
                for (Player p : starters) {
                    teamOLs.add((PlayerOL) p);
                }
                Collections.sort(teamOLs, new CompPlayer());
                for (PlayerOL oldP : oldOLs) {
                    if (!teamOLs.contains(oldP)) teamOLs.add(oldP);
                }
                break;
            case 5:
                ArrayList<PlayerK> oldKs = new ArrayList<>();
                oldKs.addAll(teamKs);
                teamKs.clear();
                for (Player p : starters) {
                    teamKs.add((PlayerK) p);
                }
                Collections.sort(teamKs, new CompPlayer());
                for (PlayerK oldP : oldKs) {
                    if (!teamKs.contains(oldP)) teamKs.add(oldP);
                }
                break;
            case 6:
                ArrayList<PlayerDL> oldDLs = new ArrayList<>();
                oldDLs.addAll(teamDLs);
                teamDLs.clear();
                for (Player p : starters) {
                    teamDLs.add((PlayerDL) p);
                }
                Collections.sort(teamDLs, new CompPlayer());
                for (PlayerDL oldP : oldDLs) {
                    if (!teamDLs.contains(oldP)) teamDLs.add(oldP);
                }
                break;
            case 7:
                ArrayList<PlayerLB> oldLBs = new ArrayList<>();
                oldLBs.addAll(teamLBs);
                teamLBs.clear();
                for (Player p : starters) {
                    teamLBs.add((PlayerLB) p);
                }
                Collections.sort(teamLBs, new CompPlayer());
                for (PlayerLB oldP : oldLBs) {
                    if (!teamLBs.contains(oldP)) teamLBs.add(oldP);
                }
                break;
            case 8:
                ArrayList<PlayerCB> oldCBs = new ArrayList<>();
                oldCBs.addAll(teamCBs);
                teamCBs.clear();
                for (Player p : starters) {
                    teamCBs.add((PlayerCB) p);
                }
                Collections.sort(teamCBs, new CompPlayer());
                for (PlayerCB oldP : oldCBs) {
                    if (!teamCBs.contains(oldP)) teamCBs.add(oldP);
                }
                break;
            case 9:
                ArrayList<PlayerS> oldSs = new ArrayList<>();
                oldSs.addAll(teamSs);
                teamSs.clear();
                for (Player p : starters) {
                    teamSs.add((PlayerS) p);
                }
                Collections.sort(teamSs, new CompPlayer());
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
        if (teamQBs.get(0).ratSpeed >= 75 && HC.get(0).offStrat == 4) {
            return 4;
        } else if (teamQBs.get(0).ratSpeed < 75 && HC.get(0).offStrat == 4) {
            return 0;
        } else {
            if (HC.get(0).offStrat > 4) HC.get(0).offStrat = 0;
            return HC.get(0).offStrat;
        }
    }

    public int getCPUDefense() {
        if (HC.get(0).defStrat > 4) HC.get(0).defStrat = 0;
        return HC.get(0).defStrat;
    }

    /**
     * Generate all the offense team strategies that can be selected
     *
     * @return array of all the offense team strats
     */
    public TeamStrategy[] getTeamStrategiesOff() {
        TeamStrategy[] ts = new TeamStrategy[5];

        ts[0] = new TeamStrategy("Pro-Style",
                "Play a normal balanced offense.", 1, 0, 0, 1, 1, 0, 0, 1);

        ts[1] = new TeamStrategy("Smash Mouth",
                "Play a conservative run-heavy offense, setting up the passes as necessary.", 2, 2, -2, 1, 1, 1, 1, 0);

        ts[2] = new TeamStrategy("West Coast",
                "Passing game dictates the run game with short accurate passes.", 2, 0, 1, 0, 3, 1, -2, 1);

        ts[3] = new TeamStrategy("Spread",
                "Pass-heavy offense using many receivers with big play potential with risk.", 1, -2, 2, 0, 2, -1, 1, 1);

        ts[4] = new TeamStrategy("Read Option",
                "QB Option heavy offense, where QB options based on coverage and LB position.", 6, -1, 1, 1, 5, -1, 0, 0);

        return ts;
    }


    /**
     * Generate all the defense team strategies that can be selected
     *
     * @return array of all the defense team strats
     */
    public TeamStrategy[] getTeamStrategiesDef() {
        TeamStrategy[] ts = new TeamStrategy[5];

        ts[0] = new TeamStrategy("4-3 Man",
                "Play a standard 4-3 man-to-man balanced defense.", 1, 0, 0, 1, 1, 0, 0, 1);

        ts[1] = new TeamStrategy("4-6 Bear",
                "Focus on stopping the run. Will give up more big passing plays but will allow less runing yards and far less big plays from runing.", 2, 0, 2, 1, 1, -1, -1, 0);

        ts[2] = new TeamStrategy("Cover 0", "Play a pure man-to-man defense with no deep defenders.", 1, 1, 1, 1, 1, 2, -2, 1);

        ts[3] = new TeamStrategy("Cover 2",
                "Play a zone defense with safety help in the back against the pass, while LBs cover the run game. ", 2, 0, -1, 1, 3, 2, 0, 1);

        ts[4] = new TeamStrategy("Cover 3",
                "Play a zone defense to stop the big plays, but allows soft zone coverage underneath.", 3, 0, -2, 1, 7, 2, 2, 1);

        return ts;
    }


    /**
     * Checks all the players leaving to see if they should be inducted to the hall of fame.
     */
    public void checkHallofFame() {

        for (Player p : playersLeaving) {
            int allConf = p.careerAllConference + (p.wonAllConference ? 1 : 0);
            int allAmer = p.careerAllAmerican + (p.wonAllAmerican ? 1 : 0);
            int poty = p.careerHeismans + (p.wonHeisman ? 1 : 0);
            int score = 10*allConf + 25*allAmer + 50*poty;
            if ( score > 74 ) {
                // HOFer
                ArrayList<String> careerStats = p.getCareerStatsList();
                StringBuilder sb = new StringBuilder();
                sb.append(p.getPosNameYrOvr_Str() + "&");
                for (String s : careerStats) {
                    sb.append(s + "&");
                }
                hallOfFame.add(sb.toString());
                league.leagueHoF.add(sb.toString());
            }
        }
    }


    /**
     * Checks if any of the league records were broken by this team.
     */
    public void checkLeagueRecords(LeagueRecords records) {
        records.checkRecord("Team PPG", teamPoints / numGames(), abbr, league.getYear());
        records.checkRecord("Team Opp PPG", teamOppPoints / numGames(), abbr, league.getYear());
        records.checkRecord("Team YPG", teamYards / numGames(), abbr, league.getYear());
        records.checkRecord("Team Opp YPG", teamOppYards / numGames(), abbr, league.getYear());
        records.checkRecord("Team PPG", teamPoints / numGames(), abbr, league.getYear());
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
                records.checkRecord("Passes Defended", getCB(i).statsDefended, abbr + ": " + getCB(i).getInitialName(), league.getYear());
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
                records.checkRecord("Career Pass Yards", qb.statsPassYards + qb.careerPassYards, abbr + ": " + qb.getInitialName(), league.getYear() - 1);
                records.checkRecord("Career Pass TDs", qb.statsPassTD + qb.careerTDs, abbr + ": " + qb.getInitialName(), league.getYear() - 1);
                records.checkRecord("Career Ints Thrown", qb.statsInt + qb.careerInt, abbr + ": " + qb.getInitialName(), league.getYear() - 1);
                records.checkRecord("Career Rush Yards", qb.statsRushYards + qb.careerRushYards, abbr + ": " + qb.getInitialName(), league.getYear() - 1);
                records.checkRecord("Career Rush TDs", qb.statsRushTD + qb.careerRushTD, abbr + ": " + qb.getInitialName(), league.getYear() - 1);
                records.checkRecord("Career Fumbles Lost", qb.statsFumbles + qb.careerFumbles, abbr + ": " + qb.getInitialName(), league.getYear() - 1);
            } else if (p instanceof PlayerRB) {
                PlayerRB rb = (PlayerRB) p;
                records.checkRecord("Career Rush Yards", rb.statsRushYards + rb.careerRushYards, abbr + ": " + rb.getInitialName(), league.getYear() - 1);
                records.checkRecord("Career Rush TDs", rb.statsRushTD + rb.careerTDs, abbr + ": " + rb.getInitialName(), league.getYear() - 1);
                records.checkRecord("Career Fumbles Lost", rb.statsFumbles + rb.careerFumbles, abbr + ": " + rb.getInitialName(), league.getYear() - 1);
            } else if (p instanceof PlayerWR) {
                PlayerWR wr = (PlayerWR) p;
                records.checkRecord("Career Rec Yards", wr.statsRecYards + wr.careerRecYards, abbr + ": " + wr.getInitialName(), league.getYear() - 1);
                records.checkRecord("Career Rec TDs", wr.statsTD + wr.careerTD, abbr + ": " + wr.getInitialName(), league.getYear() - 1);
            } else if (p instanceof PlayerTE) {
                PlayerTE te = (PlayerTE) p;
                records.checkRecord("Career Rec Yards", te.statsRecYards + te.careerRecYards, abbr + ": " + te.getInitialName(), league.getYear() - 1);
                records.checkRecord("Career Rec TDs", te.statsRecTD + te.careerTD, abbr + ": " + te.getInitialName(), league.getYear() - 1);
            } else if (p instanceof PlayerDL) {
                PlayerDL dl = (PlayerDL) p;
                records.checkRecord("Career Tackles", dl.statsTackles + dl.careerTackles, abbr + ": " + dl.getInitialName(), league.getYear() - 1);
                records.checkRecord("Career Sacks", dl.statsSacks + dl.careerSacks, abbr + ": " + dl.getInitialName(), league.getYear() - 1);
                records.checkRecord("Career Fumbles Rec", dl.statsFumbles + dl.careerFumbles, abbr + ": " + dl.getInitialName(), league.getYear() - 1);
                records.checkRecord("Career Interceptions", dl.statsInts + dl.careerInts, abbr + ": " + dl.getInitialName(), league.getYear() - 1);
            } else if (p instanceof PlayerLB) {
                PlayerLB lb = (PlayerLB) p;
                records.checkRecord("Career Tackles", lb.statsTackles + lb.careerTackles, abbr + ": " + lb.getInitialName(), league.getYear() - 1);
                records.checkRecord("Career Sacks", lb.statsSacks + lb.careerSacks, abbr + ": " + lb.getInitialName(), league.getYear() - 1);
                records.checkRecord("Career Fumbles Rec", lb.statsFumbles + lb.careerFumbles, abbr + ": " + lb.getInitialName(), league.getYear() - 1);
                records.checkRecord("Career Interceptions", lb.statsInts + lb.careerInts, abbr + ": " + lb.getInitialName(), league.getYear() - 1);
            } else if (p instanceof PlayerCB) {
                PlayerCB cb = (PlayerCB) p;
                records.checkRecord("Career Tackles", cb.statsTackles + cb.careerTackles, abbr + ": " + cb.getInitialName(), league.getYear() - 1);
                records.checkRecord("Career Sacks", cb.statsSacks + cb.careerSacks, abbr + ": " + cb.getInitialName(), league.getYear() - 1);
                records.checkRecord("Career Fumbles Rec", cb.statsFumbles + cb.careerFumbles, abbr + ": " + cb.getInitialName(), league.getYear() - 1);
                records.checkRecord("Career Interceptions", cb.statsInts + cb.careerInts, abbr + ": " + cb.getInitialName(), league.getYear() - 1);
                records.checkRecord("Career Defended", cb.statsDefended + cb.careerDefended, abbr + ": " + cb.getInitialName(), league.getYear() - 1);
            } else if (p instanceof PlayerS) {
                PlayerS s = (PlayerS) p;
                records.checkRecord("Career Tackles", s.statsTackles + s.careerTackles, abbr + ": " + s.getInitialName(), league.getYear() - 1);
                records.checkRecord("Career Sacks", s.statsSacks + s.careerSacks, abbr + ": " + s.getInitialName(), league.getYear() - 1);
                records.checkRecord("Career Fumbles Rec", s.statsFumbles + s.careerFumbles, abbr + ": " + s.getInitialName(), league.getYear() - 1);
                records.checkRecord("Career Interceptions", s.statsInts + s.careerInts, abbr + ": " + s.getInitialName(), league.getYear() - 1);
            }
        }
    }

}
