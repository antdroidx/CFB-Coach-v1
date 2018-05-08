package simulation;

import java.util.ArrayList;
import java.util.Collections;

import comparator.CompCoachScore;
import comparator.CompConfTeamRanking;
import comparator.CompPlayerHeisman;
import comparator.CompTeamConfWins;
import comparator.CompTeamPoll;
import positions.HeadCoach;
import positions.Player;
import positions.PlayerQB;
import positions.PlayerRB;
import positions.PlayerWR;
import positions.PlayerTE;
import positions.PlayerOL;
import positions.PlayerDL;
import positions.PlayerLB;
import positions.PlayerCB;
import positions.PlayerS;
import positions.PlayerK;

public class Conference {

    public String confName;
    public int confPrestige;
    public int confRelegateMin;
    public int confPromoteMin;
    public boolean confTV;
    public int confTVContract;
    public int confTVBonus;
    public String TV;

    public final ArrayList<Team> confTeams;
    private boolean evenYear;

    private final League league;

    private Game ccg;

    public int week;
    public int robinWeek;
    private final int teamCount = 12;
    private final double promotionFactor = 1.15;
    private final double relegationFactor = 0.85;

    public final ArrayList<Player> allConfPlayers;

    /**
     * Sets up Conference with empty list of teams.
     *
     * @param name
     * @param league
     */
    public Conference(String name, League league, Boolean deal, int length, int terms) {
        confName = name;
        confTeams = new ArrayList<>();
        this.league = league;
        week = 0;
        allConfPlayers = new ArrayList<>();

        confTV = deal;
        confTVContract = length;
        confTVBonus =terms;
        TV = getTVName();
    }

    /**
     * Sets up Conference from save file
     *
     */
    public Conference(String save, League league) {
        String[] data = save.split(",");
        confName = data[0];
        confTeams = new ArrayList<>();
        this.league = league;
        week = 0;
        allConfPlayers = new ArrayList<>();

        //Old Save Compatibility
        if(data.length > 1) {
            confTV = Boolean.parseBoolean(data[1]);
            confTVContract = Integer.parseInt(data[2]);
            confTVBonus = Integer.parseInt(data[3]);
            TV = data[4];
        } else {
            //Old Save Compatibility
            confTV = false;
            confTVContract = 0;
            confTVBonus = 0;
            TV = getTVName();
        }


    }

    private String getTVName() {
        String name;

        int t = (int)(Math.random() * 4);

        if (t== 0) {
            name = confName + " Multimedia";
        } else if(t == 1) {
            name = confName + " Sports";
        } else if (t == 2) {
            name = confName + " Network";
        } else if (t == 3) {
            name = confName + " Channel";
        } else {
            name = confName + " Network";
        }

        return name;
    }

    //Conference Television
    public void reviewConfTVDeal() {
        updateConfPrestige();
        if(confTV && confTVContract <= 2) {
            confTVprofitSharing();
            negotiateConfTV();
            confTVExpiring();
        } else if (confTV) {
            confTVprofitSharing();
        } else {
            negotiateConfTV();
        }

    }

    //Check for Contracts and Negotiate a new deal if no deal is already in place
    private void negotiateConfTV() {
        if(!confTV || confTV && confTVContract <= 1) {
            if (Math.random() * confPrestige*1.5 < Math.random()*confPrestige && confPrestige > league.getAverageConfPrestige()) {
                confTV = true;
                confTVContract = (int) (Math.random() * 5) + 5;
                confTVBonus = confPrestige / 20;

                league.newsStories.get(league.currentWeek+1).add(TV + " TV Contract>A new television contract has been worked out with the "
                        + confName + " conference. The new television contract is for " + confTVContract + " years starting next season, and will provide bonuses of up to " + confTVBonus + " prestige points every season to each team.");

                league.newsTV.add(TV + " TV Contract:\n\tA new TV contract has been worked out with the "
                        + confName + " conference. The new television contract is for " + confTVContract + " years starting next season, and will provide bonuses of up to " + confTVBonus + " prestige points every season to each team.");
                league.updateTV = true;
            } else if (Math.random() * confPrestige*2.5 < Math.random()*confPrestige) {
                confTV = true;
                confTVContract = (int) (Math.random() * 5) + 5;
                confTVBonus = confPrestige / 20;

                league.newsStories.get(league.currentWeek+1).add(TV + " TV Contract>A new television contract has been worked out with the "
                        + confName + " conference. The new television contract is for " + confTVContract + " years, and will provide bonuses of up to " + confTVBonus + " prestige points every season to each team.");

                league.newsTV.add(TV + " Contract:\n\tA new television contract has been worked out with the "
                        + confName + " conference. The new television contract is for " + confTVContract + " years, and will provide bonuses of up to " + confTVBonus + " prestige points every season to each team.");
                league.updateTV = true;

            }
        }
    }

    //Provide television incentives to colleges
    private void confTVprofitSharing() {
        if(confTV) {
            confTVContract--;
            int yearBonus = (int)(Math.random()*confTVBonus+1);
            for(int t = 0; t < confTeams.size(); t++) {
                confTeams.get(t).teamPrestige += yearBonus;
                if (yearBonus > 1) confTeams.get(t).HC.get(0).baselinePrestige += yearBonus/2;  //make the coach's job slightly more challenging
                else confTeams.get(t).HC.get(0).baselinePrestige += Math.random()*2;
            }
            league.newsStories.get(league.currentWeek+1).add(TV + " Annual Distribution>Each member of the " + confName + " Conference will be receiving an additional " + yearBonus + " prestige bonus this off-season as part of their network contract. The current contract will expire in " + confTVContract + " years.");

            league.newsTV.add(TV + " Annual Profit Sharing:\n\t+" + yearBonus + " prestige bonus.\nCurrent contract will expire in " + confTVContract + " years.");
            league.updateTV = true;

        }
    }

    //Notify expiration of contract with no renewal
    private void confTVExpiring() {
        if (confTVContract <= 0) {
            confTV = false;
            confTVContract = 0;
            confTVBonus = 0;
            league.newsStories.get(league.currentWeek+1).add(TV + " TV Contract Expires>The parent company of the " + confName + " Network and the " + confName + " conference were unable to come to an agreement on a new contract. The contract is now expired and will have to wait until the end of next season for renegotiations to begin again.");

            league.newsTV.add(TV + " Contract Expires:\n\tThe parent company of the " + confName + " Network and the " + confName + " conference were unable to come to an agreement on a new contract. The contract is now expired and will have to wait until the end of next season for renegotiations to begin again.");
            league.updateTV = true;
        }
    }

    /**
     * Sets up schedule for in-conference games using round robin scheduling.
     */
    public void setUpSchedule() {
        //schedule in conf matchups
        robinWeek = 0;
        evenYear = (league.leagueHistory.size() % 2 == 0);

        // Set up int arrays for each team's home/away rotation.
        // Theoretically every year you should change off between having 5 home games and 4 away games in conference
        if (league.leagueHistory.size() == 0 || confTeams.get(0).evenYearHomeOpp == null) {
            int[][] evenHomeGames = new int[12][];
            evenHomeGames[0] = new int[]{11, 7, 3, 10, 6};
            evenHomeGames[1] = new int[]{8, 4, 0, 9};
            evenHomeGames[2] = new int[]{9, 5, 1, 8, 10};
            evenHomeGames[3] = new int[]{6, 2, 10, 7};
            evenHomeGames[4] = new int[]{7, 3, 11, 6};
            evenHomeGames[5] = new int[]{4, 0, 8, 11};
            evenHomeGames[6] = new int[]{5, 1, 9, 2};
            evenHomeGames[7] = new int[]{2, 10, 6, 5, 1};
            evenHomeGames[8] = new int[]{3, 11, 7, 0};
            evenHomeGames[9] = new int[]{0, 8, 4, 3, 5};
            evenHomeGames[10] = new int[]{1, 9, 5, 4};
            evenHomeGames[11] = new int[]{10, 6, 2, 1, 3};


            for (int x = 0; x < evenHomeGames.length; x++) {
                StringBuilder sb = new StringBuilder();
                for (int y = 0; y < evenHomeGames[x].length; y++) {
                    sb.append(confTeams.get(evenHomeGames[x][y]).abbr + ",");
                }
                confTeams.get(x).evenYearHomeOpp = sb.toString();
            }
        }
        for (int r = 0; r < 9; ++r) {
            for (int g = 0; g < ((teamCount + 1) / 2); ++g) {
                Team a = confTeams.get((robinWeek + g) % (teamCount - 1));
                Team b;
                if (g == 0) {
                    b = confTeams.get((teamCount - 1));
                } else {
                    b = confTeams.get(((teamCount - 1) - g + robinWeek) % (teamCount - 1));
                }

                Game gm;

                // Check whether it's an even year and if team B appears in team A's even year home game list, or if it's not an even year and team A appears in team B's list

                if ((evenYear && a.evenYearHomeOpp.contains(b.abbr)) || (evenYear && !b.evenYearHomeOpp.contains(a.abbr)) || (!evenYear && !a.evenYearHomeOpp.contains((b.abbr))) || (!evenYear && b.evenYearHomeOpp.contains((a.abbr)))) {
                    if (a.rivalTeam.equals(b.abbr)) {
                        gm = new Game(a, b, "Rivalry Game");
                    } else {
                        gm = new Game(a, b, "Conference");
                    }
                }

                // Basically check all the reverse scenarios above, anything that would cause B to be the home team.
                else if ((evenYear && b.evenYearHomeOpp.contains(a.abbr) || (evenYear && !a.evenYearHomeOpp.contains(b.abbr)) || (!evenYear && a.evenYearHomeOpp.contains((b.abbr))) || (!evenYear && !b.evenYearHomeOpp.contains(a.abbr)))) {
                    if (a.rivalTeam.equals(b.abbr)) {
                        gm = new Game(b, a, "Rivalry Game");
                    } else {
                        gm = new Game(b, a, "Conference");
                    }
                } else { // I'm 99.9% sure all scenarios and possibilities are covered above, but lets not break the game if I'm wrong
                    if (a.rivalTeam.equals(b.abbr)) {
                        gm = new Game(b, a, "Rivalry Game");
                    } else {
                        gm = new Game(b, a, "Conference");
                    }
                }
                a.gameSchedule.add(gm);
                b.gameSchedule.add(gm);

                if (a.userControlled == true)
                    System.out.println("User Controlled Home Schedule: " + a.evenYearHomeOpp);
            }
            robinWeek++;
        }


    }

    /**
     * Sets up schedule for OOC games, which are played in 3 weeks.
     */
    public void setUpOOCSchedule() {
        //schedule OOC games
        int confNum = -1;
        if (league.conferences.get(league.randconf).confName.equals(confName)) {
            confNum = league.randconf;
        } else if (league.conferences.get(1 + 2 * league.randconf).confName.equals(confName)) {
            confNum = 1 + 2 * league.randconf;
        } else if (league.conferences.get(2 + 3 * league.randconf).confName.equals(confName)) {
            confNum = 2 + 3 * league.randconf;
        } else if (league.conferences.get(3 + 4 * league.randconf).confName.equals(confName)) {
            confNum = 3 + 4 * league.randconf;
        } else if (league.conferences.get(4 + 5 * league.randconf).confName.equals(confName)) {
            confNum = 4 + 5 * league.randconf;
        }

        if (confNum != -1 && league.randconf == 1) {
            for (int offsetOOC = 5; offsetOOC < 11; ++offsetOOC) {
                ArrayList<Team> availTeams = new ArrayList<>();
                int selConf = confNum + offsetOOC * 2;
                if (selConf == 11) selConf = 0;
                if (selConf == 13) selConf = 2;
                if (selConf == 15) selConf = 4;
                if (selConf == 17) selConf = 6;
                if (selConf == 19) selConf = 8;
                if (selConf == 21) selConf = 0;
                if (selConf == 23) selConf = 2;
                if (selConf == 25) selConf = 4;
                if (selConf == 27) selConf = 6;
                if (selConf == 29) selConf = 8;
                if (selConf == 31) selConf = 0;
                if (selConf == 33) selConf = 2;
                if (selConf == 35) selConf = 4;
                if (selConf == 37) selConf = 6;
                if (selConf == 39) selConf = 8;
                scheduleOOC(offsetOOC, selConf, availTeams);
            }
        } else if (confNum != -1 && league.randconf == 0) {
            for (int offsetOOC = 5; offsetOOC < 11; ++offsetOOC) {
                ArrayList<Team> availTeams = new ArrayList<>();
                int selConf = confNum + offsetOOC;
                if (selConf == 10) selConf = 5;
                if (selConf == 11) selConf = 6;
                if (selConf == 12) selConf = 7;
                if (selConf == 13) selConf = 8;
                if (selConf == 14) selConf = 9;
                scheduleOOC(offsetOOC, selConf, availTeams);
            }
        }

    }

    private void scheduleOOC(int offsetOOC, int selConf, ArrayList<Team> availTeams) {
        for (int i = 0; i < teamCount; ++i) {
            availTeams.add(league.conferences.get(selConf).confTeams.get(i));
        }
        for (int i = 0; i < teamCount; ++i) {
            int selTeam = (int) (availTeams.size() * Math.random());
            Team a = confTeams.get(i);
            Team b = availTeams.get(selTeam);

            Game gm;
            if (Math.random() > 0.5) {
                gm = new Game(a, b, a.conference + " vs " + b.conference);
            } else {
                gm = new Game(b, a, b.conference + " vs " + a.conference);
            }

            if (offsetOOC == league.randgm) {
                a.gameOOCSchedule0 = gm;
                b.gameOOCSchedule0 = gm;
                availTeams.remove(selTeam);
            } else if (offsetOOC == league.randgm + 1) {
                a.gameOOCSchedule1 = gm;
                b.gameOOCSchedule1 = gm;
                availTeams.remove(selTeam);
            } else if (offsetOOC == league.randgm + 2) {
                a.gameOOCSchedule2 = gm;
                b.gameOOCSchedule2 = gm;
                availTeams.remove(selTeam);
            }
        }
    }

    /**
     * Inserts the OOC games in the 1st, 5th, and 10th weeks. changed to 1, 2, 3 weeks
     */
    public void insertOOCSchedule() {
        for (int i = 0; i < confTeams.size(); ++i) {
            confTeams.get(i).gameSchedule.add(0, confTeams.get(i).gameOOCSchedule0);
            confTeams.get(i).gameSchedule.add(1, confTeams.get(i).gameOOCSchedule1);
            confTeams.get(i).gameSchedule.add(2, confTeams.get(i).gameOOCSchedule2);
        }
    }

    /**
     * Plays week for each team. If CCG week, play the CCG.
     */
    public void playWeek() {
        if (week == 12) {
            playConfChamp();
        } else {
            for (int i = 0; i < confTeams.size(); ++i) {
                confTeams.get(i).gameSchedule.get(week).playGame();
            }
            if (week == 11) schedConfChamp();
            week++;
        }

    }

    public void newsMatchups() {
        if (week >= 12) {
            return;
        } else {
            for (int i = 0; i < confTeams.size(); ++i) {
                confTeams.get(i).gameSchedule.get(week).addUpcomingGames(confTeams.get(i));
            }
        }
    }

    public void newsNSMatchups() {
        if (week >= 12) {
            return;
        } else {
            for (int i = 0; i < confTeams.size(); ++i) {
                confTeams.get(i).gameSchedule.get(week).addNewSeasonGames(confTeams.get(i));
            }
        }
    }

    public void updateConfPrestige() {
        int CP = 0;
        for (int i = 0; i < confTeams.size(); ++i) {
            CP += confTeams.get(i).teamPrestige;
        }
        confPrestige = CP / (confTeams.size());

        confPromoteMin = (int)(CP/confTeams.size() * promotionFactor);
        confRelegateMin = (int)(CP/confTeams.size() * relegationFactor);

    }

    public void updateConfRankings() {
        Collections.sort(confTeams, new CompConfTeamRanking());
    }

    /**
     * Schedule the CCG based on team rankings.
     */
    private void schedConfChamp() {
        // Play CCG between top 2 teams
        for (int i = 0; i < confTeams.size(); ++i) {
            confTeams.get(i).updatePollScore();
        }
        Collections.sort(confTeams, new CompTeamConfWins());

        int winsFirst = confTeams.get(0).getConfWins();
        Team t = confTeams.get(0);
        int i = 0;
        ArrayList<Team> teamTB = new ArrayList<>();
        while (t.getConfWins() == winsFirst) {
            teamTB.add(t);
            ++i;
            t = confTeams.get(i);
        }
        if (teamTB.size() > 2) {
            // ugh 3 way tiebreaker
            Collections.sort(teamTB, new CompTeamPoll());
            for (int j = 0; j < teamTB.size(); ++j) {
                confTeams.set(j, teamTB.get(j));
            }

        }

        int winsSecond = confTeams.get(1).getConfWins();
        t = confTeams.get(1);
        i = 1;
        teamTB.clear();
        while (t.getConfWins() == winsSecond) {
            teamTB.add(t);
            ++i;
            t = confTeams.get(i);
        }
        if (teamTB.size() > 2) {
            // ugh 3 way tiebreaker
            Collections.sort(teamTB, new CompTeamPoll());
            for (int j = 0; j < teamTB.size(); ++j) {
                confTeams.set(1 + j, teamTB.get(j));
            }

        }

        ccg = new Game(confTeams.get(0), confTeams.get(1), confName + " CCG");
        confTeams.get(0).gameSchedule.add(ccg);
        confTeams.get(1).gameSchedule.add(ccg);
        league.newsStories.get(week + 1).add("Upcoming: " + confName + " Championship Game>" + confTeams.get(0).strRankTeamRecord() + " will host " + confTeams.get(1).strRankTeamRecord() + " in the conference championship game next week.");
    }

    /**
     * Play the CCG. Add the "CC" tag to the winner.
     */
    private void playConfChamp() {
        // Play CCG between top 2 teams
        ccg.playGame();
        if (ccg.homeScore > ccg.awayScore) {
            confTeams.get(0).confChampion = "CC";
            confTeams.get(0).totalCCs++;
            confTeams.get(1).totalCCLosses++;
            confTeams.get(0).HC.get(0).confchamp++;
            confTeams.get(1).HC.get(0).bowllosses++;
            league.newsStories.get(13).add(
                    ccg.homeTeam.name + " wins the " + confName + "!>" +
                            ccg.homeTeam.strRep() + " took care of business in the conference championship against " + ccg.awayTeam.strRep() +
                            ", winning at home with a score of " + ccg.homeScore + " to " + ccg.awayScore + "."
            );
        } else {
            confTeams.get(1).confChampion = "CC";
            confTeams.get(1).totalCCs++;
            confTeams.get(0).totalCCLosses++;
            confTeams.get(1).HC.get(0).confchamp++;
            confTeams.get(0).HC.get(0).bowllosses++;
            league.newsStories.get(13).add(
                    ccg.awayTeam.name + " wins the " + confName + "!>" +
                            ccg.awayTeam.strRep() + " surprised many in the conference championship against " + ccg.homeTeam.strRep() +
                            ", winning on the road with a score of " + ccg.awayScore + " to " + ccg.homeScore + "."
            );
        }
        Collections.sort(confTeams, new CompTeamPoll());
    }

    /**
     * String of who is playing in the CCG and the result if available.
     *
     * @return conf champ summary
     */
    public String getCCGStr() {
        if (ccg == null) {
            // Give prediction, find top 2 teams
            Team team1 = null, team2 = null;
            int score1 = 0, score2 = 0;
            for (int i = confTeams.size() - 1; i >= 0; --i) { //count backwards so higher ranked teams are predicted
                Team t = confTeams.get(i);
                if (t.getConfWins() >= score1) {
                    score2 = score1;
                    score1 = t.getConfWins();
                    team2 = team1;
                    team1 = t;
                } else if (t.getConfWins() > score2) {
                    score2 = t.getConfWins();
                    team2 = t;
                }
            }
            return confName + " Conference Championship:\n\t\t" +
                    team1.strRep() + " vs " + team2.strRep();
        } else {
            if (!ccg.hasPlayed) {
                return confName + " Conference Championship:\n\t\t" +
                        ccg.homeTeam.strRep() + " vs " + ccg.awayTeam.strRep();
            } else {
                StringBuilder sb = new StringBuilder();
                Team winner, loser;
                sb.append(confName + " Conference Championship:\n");
                if (ccg.homeScore > ccg.awayScore) {
                    winner = ccg.homeTeam;
                    loser = ccg.awayTeam;
                    sb.append(winner.strRep() + " W ");
                    sb.append(ccg.homeScore + "-" + ccg.awayScore + " ");
                    sb.append("vs " + loser.strRep());
                    return sb.toString();
                } else {
                    winner = ccg.awayTeam;
                    loser = ccg.homeTeam;
                    sb.append(winner.strRep() + " W ");
                    sb.append(ccg.awayScore + "-" + ccg.homeScore + " ");
                    sb.append("at " + loser.strRep());
                    return sb.toString();
                }
            }
        }
    }

    /**
     * Get the allConfPlayers by sorting all the conf's players by their Heisman score
     * Should be only called after week 13
     */
    public ArrayList<Player> getAllConfPlayers() {
        if (allConfPlayers.isEmpty()) {
            ArrayList<HeadCoach> hc = new ArrayList<>();
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


            for (Team t : confTeams) {
                hc.addAll(t.HC);
                qbs.addAll(t.teamQBs);
                rbs.addAll(t.teamRBs);
                wrs.addAll(t.teamWRs);
                tes.addAll(t.teamTEs);
                ols.addAll(t.teamOLs);
                ks.addAll(t.teamKs);
                dls.addAll(t.teamDLs);
                lbs.addAll(t.teamLBs);
                cbs.addAll(t.teamCBs);
                ss.addAll(t.teamSs);

            }

            Collections.sort(hc, new CompCoachScore());
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

            allConfPlayers.add(hc.get(0));
            hc.get(0).wonAllConference = true;
            hc.get(0).confAward++;
            allConfPlayers.add(qbs.get(0));
            qbs.get(0).wonAllConference = true;
            qbs.get(0).team.HC.get(0).allconference++;
            allConfPlayers.add(rbs.get(0));
            rbs.get(0).wonAllConference = true;
            rbs.get(0).team.HC.get(0).allconference++;
            allConfPlayers.add(rbs.get(1));
            rbs.get(1).wonAllConference = true;
            rbs.get(1).team.HC.get(0).allconference++;
            for (int i = 0; i < 3; ++i) {
                allConfPlayers.add(wrs.get(i));
                wrs.get(i).wonAllConference = true;
                wrs.get(i).team.HC.get(0).allconference++;
            }
            allConfPlayers.add(tes.get(0));
            tes.get(0).wonAllConference = true;
            tes.get(0).team.HC.get(0).allconference++;

            for (int i = 0; i < 5; ++i) {
                allConfPlayers.add(ols.get(i));
                ols.get(i).wonAllConference = true;
                ols.get(i).team.HC.get(0).allconference++;
            }

            allConfPlayers.add(ks.get(0));
            ks.get(0).wonAllConference = true;
            ks.get(0).team.HC.get(0).allconference++;

            for (int i = 0; i < 4; ++i) {
                allConfPlayers.add(dls.get(i));
                dls.get(i).wonAllConference = true;
                dls.get(i).team.HC.get(0).allconference++;
            }
            for (int i = 0; i < 3; ++i) {
                allConfPlayers.add(lbs.get(i));
                lbs.get(i).wonAllConference = true;
                lbs.get(i).team.HC.get(0).allconference++;
            }
            for (int i = 0; i < 3; ++i) {
                allConfPlayers.add(cbs.get(i));
                cbs.get(i).wonAllConference = true;
                cbs.get(i).team.HC.get(0).allconference++;
            }
            for (int i = 0; i < 2; ++i) {
                allConfPlayers.add(ss.get(i));
                ss.get(i).wonAllConference = true;
                ss.get(i).team.HC.get(0).allconference++;
            }
        }

        return allConfPlayers;
    }

}