/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Simulation;

import java.util.ArrayList;

public class PlayerS extends Player {

    //CBCov affects how good he is at covering the pass
    public int ratCoverage;
    //CBSpd affects how good he is at not letting up deep passes
    public int ratSpeed;
    //CBTkl affects how good he is at tackling
    public int ratTackle;
    public int ratRunStop;

    //Stats
    public int statsTackles;
    public int statsSacks;
    public int statsFumbles;
    public int statsInts;

    public int careerTackles;
    public int careerSacks;
    public int careerFumbles;
    public int careerInts;

    public PlayerS(String nm, Team t, int yr, int pot, int iq, int cov, int spd, int tkl, boolean rs, int dur, int rstop, int reg, int trait) {
        team = t;
        name = nm;
        year = yr;
        gamesStarted = 0;
        gamesPlayed = 0;
        isInjured = false;
        ratOvr = (cov * 2 + spd + tkl + rstop) / 5;
        ratPot = pot;
        ratFootIQ = iq;
        ratDur = dur;
        ratCoverage = cov;
        ratSpeed = spd;
        ratTackle = tkl;
        ratRunStop = rstop;
        isRedshirt = rs;
        if (isRedshirt) year = 0;
        position = "S";
        region = reg;
        personality = trait;

        troubledTimes = 0;

        wonHeisman = false;
        wonAllAmerican = false;
        wonAllConference = false;
        statsWins = 0;

        careerGames = 0;
        careerHeismans = 0;
        careerAllAmerican = 0;
        careerAllConference = 0;
        careerWins = 0;

        statsTackles = 0;
        statsSacks = 0;
        statsFumbles = 0;
        statsInts = 0;

        careerTackles = 0;
        careerSacks = 0;
        careerFumbles = 0;
        careerInts = 0;
    }

    public PlayerS(String nm, Team t, int yr, int pot, int iq, int cov, int spd, int tkl, boolean rs, int dur, int rstop,
                   int cGamesPlayed, int cTackles, int cSacks, int cFumbles, int cInts, int cHeismans, int cAA, int cAC, int cWins, boolean transfer, int reg, int trait) {
        team = t;
        name = nm;
        year = yr;
        gamesStarted = 0;
        gamesPlayed = 0;
        isInjured = false;
        ratOvr = (cov * 2 + spd + tkl) / 4;
        ratPot = pot;
        ratFootIQ = iq;
        ratDur = dur;
        ratCoverage = cov;
        ratSpeed = spd;
        ratTackle = tkl;
        ratRunStop = rstop;
        isRedshirt = rs;
        if (isRedshirt) year = 0;
        isTransfer = transfer;
        region = reg;
        personality = trait;
        position = "S";
        troubledTimes = 0;

        wonHeisman = false;
        wonAllAmerican = false;
        wonAllConference = false;
        statsWins = 0;

        careerGames = cGamesPlayed;
        careerHeismans = cHeismans;
        careerAllAmerican = cAA;
        careerAllConference = cAC;
        careerWins = cWins;

        statsTackles = 0;
        statsSacks = 0;
        statsFumbles = 0;
        statsInts = 0;

        careerTackles = cTackles;
        careerSacks = cSacks;
        careerFumbles = cFumbles;
        careerInts = cInts;

    }

    public PlayerS(String nm, int yr, int stars, Team t) {
        name = nm;
        year = yr;
        team = t;
        gamesStarted = 0;
        gamesPlayed = 0;
        isInjured = false;
        ratPot = (int) (attrBase + 50 * Math.random());
        ratFootIQ = (int) (attrBase + 50 * Math.random());
        ratDur = (int) (attrBase + 50 * Math.random());
        ratCoverage = (int) (ratBase + year*yearFactor + stars*starFactor - ratTolerance*Math.random());
        ratSpeed = (int) ((ratBase-10) + year*yearFactor + stars*starFactor - ratTolerance*Math.random());
        ratTackle = (int) (ratBase + year*yearFactor + stars*starFactor - ratTolerance*Math.random());
        ratRunStop = (int) (ratBase + year*yearFactor + stars*starFactor - ratTolerance*Math.random());
        ratOvr = (ratCoverage * 2 + ratSpeed + ratTackle + ratRunStop) / 5;
        position = "S";
        region = (int)(Math.random()*5);
        personality = (int) (attrBase + 50 * Math.random());

        //cost = (int) (Math.pow((float) ratOvr - 55, 2) / 3.5) + 125 + (int) (Math.random() * 100) - 50;
        recruitTolerance = (int)((60 - team.teamPrestige)/sImportance);
        cost = (int)((Math.pow((float) ratOvr - costBaseRating, 2)/5) + (int)Math.random()*recruitTolerance);

        cost = (int)(cost/sImportance);

        double locFactor = Math.abs(team.location - region) - 2.5;
        cost = cost + (int)(Math.random()*(locFactor * locationDiscount));
        if (cost < 0) cost = (int)Math.random()*7+1;

        troubledTimes = 0;

        wonHeisman = false;
        wonAllAmerican = false;
        wonAllConference = false;
        statsWins = 0;

        careerGames = 0;
        careerHeismans = 0;
        careerAllAmerican = 0;
        careerAllConference = 0;
        careerWins = 0;

        statsTackles = 0;
        statsSacks = 0;
        statsFumbles = 0;
        statsInts = 0;

        careerTackles = 0;
        careerSacks = 0;
        careerFumbles = 0;
        careerInts = 0;
    }

    public PlayerS(String nm, int yr, int stars, Team t, boolean custom) {
        name = nm;
        year = yr;
        team = t;
        gamesStarted = 0;
        gamesPlayed = 0;
        isInjured = false;
        ratPot = (int) (attrBase + 50 * Math.random());
        ratFootIQ = (int) (attrBase + 50 * Math.random());
        ratDur = (int) (attrBase + 50 * Math.random());
        ratCoverage = (int) (ratBase + stars * customFactor - ratTolerance * Math.random());
        ratSpeed = (int) (ratBase + stars * customFactor - ratTolerance * Math.random());
        ratTackle = (int) (ratBase + stars * customFactor - ratTolerance * Math.random());
        ratRunStop = (int) (ratBase + stars * customFactor - ratTolerance * Math.random());
        ratOvr = (ratCoverage * 2 + ratSpeed + ratTackle + ratRunStop) / 5;
        position = "S";
        region = (int)(Math.random()*5);
        personality = (int) (attrBase + 50 * Math.random());

        troubledTimes = 0;

        wonHeisman = false;
        wonAllAmerican = false;
        wonAllConference = false;
        statsWins = 0;

        careerGames = 0;
        careerHeismans = 0;
        careerAllAmerican = 0;
        careerAllConference = 0;
        careerWins = 0;

        statsTackles = 0;
        statsSacks = 0;
        statsFumbles = 0;
        statsInts = 0;

        careerTackles = 0;
        careerSacks = 0;
        careerFumbles = 0;
        careerInts = 0;
    }
    
    @Override
    public void advanceSeason() {
        int oldOvr = ratOvr;
        progression = (ratPot * 3 + team.HC.get(0).ratTalent * 2 + team.HC.get(0).ratDef) / 6;
        int games = gamesStarted + (gamesPlayed-gamesStarted)/3;

        if (!isMedicalRS) {
            year++;
            if (wonAllConference) ratPot++;
            if (wonAllAmerican) ratPot++;
            if (year > 2 && games < 4) ratPot -= (int) (Math.random() * 15);

            ratFootIQ += (int) (Math.random() * (progression + games - 35)) / 10;
            ratCoverage += (int) (Math.random() * (progression + games - 35)) / 10;
            ratSpeed += (int) (Math.random() * (progression + games - 35)) / 10;
            ratTackle += (int) (Math.random() * (progression + games - 35)) / 10;
            ratRunStop += (int) (Math.random() * (progression + games - 35)) / 10;
            if (Math.random() * 100 < progression) {
                //breakthrough
                ratCoverage += (int) (Math.random() * (progression + games - 40)) / 10;
                ratSpeed += (int) (Math.random() * (progression + games - 40)) / 10;
                ratTackle += (int) (Math.random() * (progression + games - 40)) / 10;
                ratRunStop += (int) (Math.random() * (progression + games - 40)) / 10;
            }
        }
        
        ratOvr = (ratCoverage * 2 + ratSpeed + ratTackle + ratRunStop) / 5;
        ratImprovement = ratOvr - oldOvr;

        careerGames += gamesPlayed;
        careerWins += statsWins;

        careerTackles += statsTackles;
        careerSacks += statsSacks;
        careerFumbles += statsFumbles;
        careerInts += statsInts;

        statsTackles = 0;
        statsSacks = 0;
        statsFumbles = 0;
        statsInts = 0;

        if (wonHeisman) careerHeismans++;
        if (wonAllAmerican) careerAllAmerican++;
        if (wonAllConference) careerAllConference++;

        if (isTransfer) {
            isTransfer = false;
            year -= 1;
        }
    }

    @Override
    public int getHeismanScore() {
        return statsTackles * 40 + statsSacks * 750 + statsFumbles * 750 + statsInts * 750 + 10 * ratOvr - (team.teamOppYards * 2) - (team.teamOppPoints * 2) + team.confPrestige * 7;
    }

    @Override
    public ArrayList<String> getDetailStatsList(int games) {
        ArrayList<String> pStats = new ArrayList<>();
        pStats.add("Tackles: " + (statsTackles) + " >Sacks: " + (statsSacks));
        pStats.add("Fumbles: " + (statsFumbles) + " >Interceptions: " + (statsInts));
        pStats.add("Games: " + gamesPlayed + " (" + statsWins + "-" + (gamesStarted - statsWins) + ")" + ">Durability: " + getLetterGrade(ratDur));
        pStats.add("Home Region: " + getRegion(region) + ">Personality: " + getLetterGrade(personality));
        pStats.add("Football IQ: " + getLetterGrade(ratFootIQ) + ">Coverage: " + getLetterGrade(ratCoverage));
        pStats.add("Speed: " + getLetterGrade(ratSpeed) + ">Tackling: " + getLetterGrade(ratTackle));
        pStats.add("Run Stopping: " + getLetterGrade(ratRunStop) + ">Nothing");
        pStats.add(" > ");
        return pStats;
    }

    @Override
    public ArrayList<String> getDetailAllStatsList(int games) {
        ArrayList<String> pStats = new ArrayList<>();
        pStats.add("Tackles: " + (statsTackles) + " >Sacks: " + (statsSacks));
        pStats.add("Fumbles: " + (statsFumbles) + " >Interceptions: " + (statsInts));
        pStats.add("Games: " + gamesPlayed + " (" + statsWins + "-" + (gamesStarted - statsWins) + ")" + ">Durability: " + ratDur);
        pStats.add("Home Region: " + getRegion(region) + ">Personality: " + personality);
        pStats.add("Football IQ: " + ratFootIQ + ">Coverage: " + ratCoverage);
        pStats.add("Speed: " + ratSpeed + ">Tackling: " + ratTackle);
        pStats.add("Run Stopping: " + ratRunStop + ">Nothing");
        pStats.add("[B]CAREER STATS:");
        pStats.addAll(getCareerStatsList());
        return pStats;
    }

    @Override
    public ArrayList<String> getCareerStatsList() {
        ArrayList<String> pStats = new ArrayList<>();
        pStats.add("Tackles: " + (statsTackles + careerTackles) + " >Sacks: " + (statsSacks + careerSacks));
        pStats.add("Fumbles: " + (statsFumbles + careerFumbles) + " >Interceptions: " + (statsInts + careerInts));
        pStats.addAll(super.getCareerStatsList());
        return pStats;
    }


    @Override
    public String getInfoForLineup() {
        if (injury != null)
            return getInitialName() + " [" + getYrStr() + "] " + ratOvr + "/" + getLetterGrade(ratPot) + " " + injury.toString();
        return getInitialName() + " [" + getYrStr() + "] " + ratOvr + "/" + getLetterGrade(ratPot) + " (" +
                getLetterGrade(ratCoverage) + ", " + getLetterGrade(ratSpeed) + ", " + getLetterGrade(ratTackle) + ", " + getLetterGrade(ratRunStop) + ")";
    }

}
