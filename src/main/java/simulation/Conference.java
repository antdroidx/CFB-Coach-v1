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
import positions.PlayerCB;
import positions.PlayerDL;
import positions.PlayerK;
import positions.PlayerLB;
import positions.PlayerOL;
import positions.PlayerQB;
import positions.PlayerRB;
import positions.PlayerS;
import positions.PlayerTE;
import positions.PlayerWR;

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

    private final League league;

    private Game ccg;
    public final ArrayList<Division> divisions;
    public int[] oocWeeks = {0,1,2};

    private final double promotionFactor = 1.15;
    private final double relegationFactor = 0.75;
    public final int minConfTeams = 8;

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
        allConfPlayers = new ArrayList<>();
        divisions = new ArrayList<Division>();


        confTV = deal;
        confTVContract = length;
        confTVBonus = terms;
        TV = getTVName();

    }

    /**
     * Sets up Conference from save file
     */
    public Conference(String save, League league) {
        String[] data = save.split(",");
        confName = data[0];
        confTeams = new ArrayList<>();
        this.league = league;
        allConfPlayers = new ArrayList<>();
        divisions = new ArrayList<Division>();


        //Old Save Compatibility
        if (data.length > 1) {
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

    public String getTVName() {
        String name;

        int t = (int) (Math.random() * 4);

        if (t == 0) {
            name = confName + " Multimedia";
        } else if (t == 1) {
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
        if (confTV && confTVContract <= 2) {
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
        if (!confTV || confTV && confTVContract <= 1) {
            if (Math.random() * confPrestige * 1.5 < Math.random() * confPrestige && confPrestige > league.getAverageConfPrestige()) {
                confTV = true;
                confTVContract = (int) (Math.random() * 5) + 5;
                confTVBonus = confPrestige / 16;

                league.newsStories.get(league.currentWeek + 1).add(TV + " TV Contract>A new television contract has been worked out with the "
                        + confName + " conference. The new television contract is for " + confTVContract + " years starting next season, and will provide bonuses of up to " + confTVBonus + " prestige points every season to each team.");

                league.newsTV.add(TV + " TV Contract:\n\tA new TV contract has been worked out with the "
                        + confName + " conference. The new television contract is for " + confTVContract + " years starting next season, and will provide bonuses of up to " + confTVBonus + " prestige points every season to each team.");
                league.updateTV = true;
            } else if (Math.random() * confPrestige * 4 < Math.random() * confPrestige) {
                confTV = true;
                confTVContract = (int) (Math.random() * 5) + 5;
                confTVBonus = confPrestige / 20;

                league.newsStories.get(league.currentWeek + 1).add(TV + " TV Contract>A new television contract has been worked out with the "
                        + confName + " conference. The new television contract is for " + confTVContract + " years, and will provide bonuses of up to " + confTVBonus + " prestige points every season to each team.");

                league.newsTV.add(TV + " Contract:\n\tA new television contract has been worked out with the "
                        + confName + " conference. The new television contract is for " + confTVContract + " years, and will provide bonuses of up to " + confTVBonus + " prestige points every season to each team.");
                league.updateTV = true;

            }
        }
    }

    //Provide television incentives to colleges
    private void confTVprofitSharing() {
        if (confTV && confTVContract > 0) {
            confTVContract--;
            int yearBonus = (int) (Math.random() * confTVBonus + 1);
            for (int t = 0; t < confTeams.size(); t++) {
                confTeams.get(t).teamPrestige += yearBonus;
                if (yearBonus > 1)
                    confTeams.get(t).HC.get(0).baselinePrestige += yearBonus / 2;  //make the coach's job slightly more challenging
                else confTeams.get(t).HC.get(0).baselinePrestige += Math.random() * 2;
            }
            league.newsStories.get(league.currentWeek + 1).add(TV + " Annual Distribution>Each member of the " + confName + " Conference will be receiving an additional " + yearBonus + " prestige bonus this off-season as part of their network contract. The current contract will expire in " + confTVContract + " years.");

            league.newsTV.add(TV + " Annual Profit Sharing:\n\t+" + yearBonus + " prestige bonus.\nCurrent contract will expire in " + confTVContract + " years.");
            league.updateTV = true;
            if(confTVContract <= 0) confTV = false;

        }
    }

    //Notify expiration of contract with no renewal
    private void confTVExpiring() {
        if (confTVContract <= 0) {
            confTV = false;
            confTVContract = 0;
            confTVBonus = 0;
            league.newsStories.get(league.currentWeek + 1).add(TV + " TV Contract Expires>The parent company of the " + confName + " Network and the " + confName + " conference were unable to come to an agreement on a new contract. The contract is now expired and will have to wait until the end of next season for renegotiations to begin again.");

            league.newsTV.add(TV + " Contract Expires:\n\tThe parent company of the " + confName + " Network and the " + confName + " conference were unable to come to an agreement on a new contract. The contract is now expired and will have to wait until the end of next season for renegotiations to begin again.");
            league.updateTV = true;
        }
    }

    /**
     * Sorts teams into their division
     */
    public void setDivisionTeams(){
        if(confTeams.size() >= 12) {
            String divAName = divisions.get(0).divName;
            for (int i = 0; i < confTeams.size(); i++) {
                if (confTeams.get(i).division.equals(divAName)) {
                    divisions.get(0).divTeams.add(confTeams.get(i));
                } else {
                    divisions.get(1).divTeams.add(confTeams.get(i));
                }
            }
        }
    }

    /**
     * sets up division schedule round robin style
     */
    /**
     * Sets up schedule for in-conference games using round robin scheduling.
     */
    public void setUpDivisionSchedule() {
        //schedule in conf matchups
        int robinWeek = 0;
        int robinCounter = 1;
        int confWeeks = 5;

        for (int d = 0; d < 2; d++) {
            int divSize = divisions.get(d).divTeams.size();
            if(divSize % 2 != 0) {
                divisions.get(d).divTeams.add(new Team("BYE", "BYE", "BYE", 0, "BYE", 0, league));
                divSize++;
                confWeeks++;
            } else {
                Team b = new Team("BYE", "BYE", "BYE", 0, "BYE", 0, league);
                for (int g = 0; g < divSize; ++g) {
                    Team a = divisions.get(d).divTeams.get(g);
                    a.gameSchedule.add(new Game(a, b, "BYE WEEK"));
                }
            }
            for (int r = 0; r < confWeeks; ++r) {
                for (int g = 0; g < divSize/2; ++g) {
                    Team a = divisions.get(d).divTeams.get((robinWeek + g) % (divSize-1));
                    Team b;
                    if (g == 0) {
                        b = divisions.get(d).divTeams.get(divSize-1);
                    } else {
                        b = divisions.get(d).divTeams.get((divSize-1 - g + robinWeek) % (divSize-1));
                    }

                    Game gm;

                    if (r % 2 == 0) {
                        gm = new Game(a, b, "Division");
                    } else {
                        gm = new Game(b, a, "Division");
                    }

                    a.gameSchedule.add(gm);
                    b.gameSchedule.add(gm);

                }
                robinWeek += robinCounter;
            }
        }
    }

    /**
     * Sets up cross divisional games
     */
    public void setUpCrossDivisionSchedule(){
        int year = league.leagueHistory.size();
        int n = year%6;
        int teams = confTeams.size()/2;
        for(int i = 0; i < teams; i++){
            Game gm;
            for(int k = 0; k < 4; k++) {
                Team teamA = divisions.get(0).divTeams.get(i);
                Team teamB = divisions.get(1).divTeams.get((n + i + k) % teams);
                if(k > 1) {
                    gm = new Game(teamA,teamB,"Conference");
                }
                else{
                    gm = new Game(teamB,teamA,"Conference");
                }
                teamA.gameSchedule.add(gm);
                teamB.gameSchedule.add(gm);
            }
        }
    }

    /**
     * Sets up schedule for in-conference games using round robin scheduling.
     */
    public void setUpSchedule() {
        //schedule in conf matchups
        int robinWeek = 0;
        int robinCounter = 1;
        int confWeeks = 9;
        int confSize = confTeams.size() - 1;

        if(confTeams.size() > 11) {
            robinCounter = 3;
        }
        if(confName.contains("Independent")) confWeeks = 0;
        if(league.enableUnivProRel) confWeeks = 12;


        for (int r = 0; r < confWeeks; ++r) {
            for (int g = 0; g < (confTeams.size()/ 2); ++g) {
                Team a = confTeams.get((robinWeek + g) % confSize);
                Team b;
                if (g == 0) {
                    b = confTeams.get(confSize);
                } else {
                    b = confTeams.get((confSize - g + robinWeek) % confSize);
                }

                Game gm;

                    if (r%2 == 0 && confSize > 10) {
                            gm = new Game(a, b, "Conference");
                    } else {
                            gm = new Game(b, a, "Conference");
                    }

                a.gameSchedule.add(gm);
                b.gameSchedule.add(gm);

            }
            robinWeek += robinCounter;
        }
    }

    /**
     * Inserts the OOC games in the 1st, 5th, and 10th weeks. changed to 1, 2, 3 weeks
     */
    public void insertOOCSchedule() {
        for (int i = 0; i < confTeams.size(); ++i) {
            confTeams.get(i).gameSchedule.add(oocWeeks[0], confTeams.get(i).gameOOCSchedule0);
            confTeams.get(i).gameSchedule.add(oocWeeks[1], confTeams.get(i).gameOOCSchedule1);
            confTeams.get(i).gameSchedule.add(oocWeeks[2], confTeams.get(i).gameOOCSchedule2);
        }
    }

    /**
     * Plays week for each team. If CCG week, play the CCG.
     */
    public void playWeek() {
        if (league.currentWeek == 12) {
            if(confTeams.size() >= minConfTeams)
            playConfChamp();
        } else {
            for (int i = 0; i < confTeams.size(); ++i) {
                confTeams.get(i).gameSchedule.get(league.currentWeek).playGame();
            }
            if (league.currentWeek == 11) schedConfChamp();
        }

    }

    public void newsMatchups() {
        if (league.currentWeek >= 11) {
            return;
        } else {
            for (int i = 0; i < confTeams.size(); ++i) {
                confTeams.get(i).gameSchedule.get(league.currentWeek+1).addUpcomingGames(confTeams.get(i));

            }
        }
    }

    public void newsNSMatchups() {
        if (league.currentWeek >= 12) {
            return;
        } else {
            for (int i = 0; i < confTeams.size(); ++i) {
                confTeams.get(i).gameSchedule.get(league.currentWeek).addNewSeasonGames(confTeams.get(i));
            }
        }
    }

    public void updateConfPrestige() {
        int CP = 0;
        for (int i = 0; i < confTeams.size(); ++i) {
            CP += confTeams.get(i).teamPrestige;
        }
        confPrestige = CP / (confTeams.size());

        confPromoteMin = (int) (CP / confTeams.size() * promotionFactor);
        confRelegateMin = (int) (CP / confTeams.size() * relegationFactor);

    }

    public void updateConfRankings() {
        Collections.sort(confTeams, new CompConfTeamRanking());
    }

    /**
     * Schedule the CCG based on team rankings.
     */
    private void schedConfChamp() {
        // Play CCG between top 2 teams
        if (confTeams.size() >= minConfTeams) {
            for (int i = 0; i < confTeams.size(); ++i) {
                confTeams.get(i).updatePollScore();
            }

            ArrayList<Team> teams = new ArrayList<>();
            for (int i = 0; i < confTeams.size(); ++i) {
                if (league.penalizedTeams.get(0) != confTeams.get(i) && league.penalizedTeams.get(1) != confTeams.get(i))
                    teams.add(confTeams.get(i));
            }


            Collections.sort(teams, new CompTeamConfWins());

            int winsFirst = teams.get(0).getConfWins();
            Team t = teams.get(0);
            int i = 0;
            ArrayList<Team> teamTB = new ArrayList<>();
            while (t.getConfWins() == winsFirst) {
                teamTB.add(t);
                ++i;
                t = teams.get(i);
            }
            if (teamTB.size() > 2) {
                // ugh 3 way tiebreaker
                Collections.sort(teamTB, new CompTeamPoll());
                for (int j = 0; j < teamTB.size(); ++j) {
                    teams.set(j, teamTB.get(j));
                }

            }

            int winsSecond = teams.get(1).getConfWins();
            t = teams.get(1);
            i = 1;
            teamTB.clear();
            while (t.getConfWins() == winsSecond) {
                teamTB.add(t);
                ++i;
                t = teams.get(i);
            }
            if (teamTB.size() > 2) {
                // ugh 3 way tiebreaker
                Collections.sort(teamTB, new CompTeamPoll());
                for (int j = 0; j < teamTB.size(); ++j) {
                    teams.set(1 + j, teamTB.get(j));
                }

            }

            ccg = new Game(teams.get(0), teams.get(1), confName + " CCG");
            teams.get(0).gameSchedule.add(ccg);
            teams.get(1).gameSchedule.add(ccg);
            league.newsStories.get(league.currentWeek + 1).add("Upcoming: " + confName + " Championship Game>" + teams.get(0).strRankTeamRecord() + " will host " + teams.get(1).strRankTeamRecord() + " in the conference championship game next week.");
        }
    }

    /**
     * Play the CCG. Add the "CC" tag to the winner.
     */
    private void playConfChamp() {
        // Play CCG between top 2 teams
        ccg.playGame();
        if (ccg.homeScore > ccg.awayScore) {
            ccg.homeTeam.confChampion = "CC";
            ccg.homeTeam.totalCCs++;
            ccg.awayTeam.totalCCLosses++;
            ccg.homeTeam.HC.get(0).confchamp++;
            league.newsStories.get(13).add(
                    ccg.homeTeam.name + " wins the " + confName + "!>" +
                            ccg.homeTeam.strRep() + " took care of business in the conference championship against " + ccg.awayTeam.strRep() +
                            ", winning at home with a score of " + ccg.homeScore + " to " + ccg.awayScore + "."
            );
        } else {
            ccg.awayTeam.confChampion = "CC";
            ccg.awayTeam.totalCCs++;
            ccg.homeTeam.totalCCLosses++;
            ccg.awayTeam.HC.get(0).confchamp++;
            league.newsStories.get(13).add(
                    ccg.awayTeam.name + " wins the " + confName + "!>" +
                            ccg.awayTeam.strRep() + " surprised many in the conference championship against " + ccg.homeTeam.strRep() +
                            ", winning on the road with a score of " + ccg.awayScore + " to " + ccg.homeScore + "."
            );
        }
        Collections.sort(confTeams, new CompTeamPoll());
        Collections.sort(confTeams, new CompTeamConfWins());
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

    public String getConfSaveString() {
        return (confName + "," + confTV + "," + confTVContract + "," + confTVBonus + "," + TV + "\n");
    }

}