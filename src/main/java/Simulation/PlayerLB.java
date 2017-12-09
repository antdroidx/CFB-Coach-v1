package Simulation;

import java.util.ArrayList;

/**
 * Created by ahngu on 10/9/2017.
 * <p>
 * Linebacker needs to stop the run and defend TE and most importantly, tackle.
 * <p>
 * Currently using the F7 role as base
 */

public class PlayerLB extends Player {

    //Coverage against TEs
    public int ratCoverage;
    //OLBkR affects how well he defends running plays
    public int ratRunStop;
    //LB Tackle Ability
    public int ratTackle;
    //public int ratSpeed;
    public int ratSpeed;


    //Stats
    public int statsTackles;
    public int statsSacks;
    public int statsFumbles;
    public int statsInts;

    public int careerTackles;
    public int careerSacks;
    public int careerFumbles;
    public int careerInts;

    public PlayerLB(String nm, Team t, int yr, int pot, int iq, int cov, int rsh, int tkl, boolean rs, int dur, int spd, int reg, int trait) {
        team = t;
        name = nm;
        year = yr;
        gamesPlayed = 0;
        isInjured = false;
        ratOvr = (cov + 2 * rsh + 2 * tkl + spd) / 6;
        ratPot = pot;
        ratFootIQ = iq;
        ratDur = dur;
        ratCoverage = cov;
        ratRunStop = rsh;
        ratTackle = tkl;
        ratSpeed = spd;
        isRedshirt = rs;
        if (isRedshirt) year = 0;
        position = "LB";
        region = reg;
        personality = trait;
        cost = (int) (Math.pow((float) ratOvr - 55, 2) / 4) + 70 + (int) (Math.random() * 100) - 50;
        troubledTimes = 0;

        wonHeisman = false;
        wonAllAmerican = false;
        wonAllConference = false;
        statsWins = 0;

        careerGamesPlayed = 0;
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

    public PlayerLB(String nm, Team t, int yr, int pot, int iq, int cov, int rsh, int tkl, boolean rs, int dur, int spd,
                    int cGamesPlayed, int cTackles, int cSacks, int cFumbles, int cInts, int cHeismans, int cAA, int cAC, int cWins, boolean transfer, int reg, int trait) {
        team = t;
        name = nm;
        year = yr;
        gamesPlayed = 0;
        isInjured = false;
        ratOvr = (cov + rsh * 2 + tkl * 2) / 5;
        ratPot = pot;
        ratFootIQ = iq;
        ratDur = dur;
        ratCoverage = cov;
        ratRunStop = rsh;
        ratTackle = tkl;
        ratSpeed = spd;
        isRedshirt = rs;
        if (isRedshirt) year = 0;
        isTransfer = transfer;
        region = reg;
        personality = trait;
        position = "LB";
        troubledTimes = 0;

        cost = (int) (Math.pow((float) ratOvr - 55, 2) / 4) + 70 + (int) (Math.random() * 100) - 50;

        wonHeisman = false;
        wonAllAmerican = false;
        wonAllConference = false;
        statsWins = 0;

        careerGamesPlayed = cGamesPlayed;
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

     public PlayerLB(String nm, int yr, int stars, Team t) {
        name = nm;
        year = yr;
        team = t;
        gamesPlayed = 0;
        isInjured = false;
        ratPot = (int) (attrBase + 50 * Math.random());
        ratFootIQ = (int) (attrBase + 50 * Math.random());
        ratDur = (int) (attrBase + 50 * Math.random());
        ratCoverage = (int) (ratBase + year*yearFactor + stars*starFactor - ratTolerance*Math.random());
        ratRunStop = (int) (ratBase + year*yearFactor + stars*starFactor - ratTolerance*Math.random());
        ratTackle = (int) (ratBase + year*yearFactor + stars*starFactor - ratTolerance*Math.random());
        ratSpeed = (int) ((ratBase-15) + year*yearFactor + stars*starFactor - ratTolerance*Math.random());
        ratOvr = (ratCoverage + ratRunStop * 2 + ratTackle * 2 + ratSpeed) / 6;
        position = "LB";
        region = (int)(Math.random()*5);
        personality = (int) (attrBase + 50 * Math.random());
        cost = (int) (Math.pow((float) ratOvr - 55, 2) / 4) + 70 + (int) (Math.random() * 100) - 50;

        double locFactor = Math.abs(team.location - region) - 2.5;
        cost = cost + (int)(Math.random()*(locFactor * 10));
         troubledTimes = 0;

        wonHeisman = false;
        wonAllAmerican = false;
        wonAllConference = false;
        statsWins = 0;

        careerGamesPlayed = 0;
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

    public PlayerLB(String nm, int yr, int stars, Team t, boolean custom) {
        name = nm;
        year = yr;
        team = t;
        gamesPlayed = 0;
        isInjured = false;
        ratPot = (int) (attrBase + 50 * Math.random());
        ratFootIQ = (int) (attrBase + 50 * Math.random());
        ratDur = (int) (attrBase + 50 * Math.random());
        ratCoverage = (int) (ratBase + stars * customFactor - ratTolerance * Math.random());
        ratRunStop = (int) (ratBase + stars * customFactor - ratTolerance * Math.random());
        ratTackle = (int) (ratBase + stars * customFactor - ratTolerance * Math.random());
        ratSpeed = (int) ((ratBase-15) + stars * customFactor - ratTolerance * Math.random());
        ratOvr = (ratCoverage + ratRunStop * 2 + ratTackle * 2 + ratSpeed) / 6;
        position = "LB";
        region = (int)(Math.random()*5);
        personality = (int) (attrBase + 50 * Math.random());
        cost = (int) (Math.pow((float) ratOvr - 55, 2) / 4) + 70 + (int) (Math.random() * 100) - 50;

        double locFactor = Math.abs(team.location - region) - 2.5;
        cost = cost + (int)(Math.random()*(locFactor * 10));
        troubledTimes = 0;

        wonHeisman = false;
        wonAllAmerican = false;
        wonAllConference = false;
        statsWins = 0;

        careerGamesPlayed = 0;
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
    public int getHeismanScore() {
        return statsTackles * 30 + statsSacks * 550 + statsFumbles * 550 + statsInts * 500 + 15 * ratOvr - (2 * team.teamOppYards) - (2 * team.teamOppPoints) + team.confPrestige * 7;
    }

    @Override
    public void advanceSeason() {
        int oldOvr = ratOvr;
        progression = (ratPot * 3 + team.HC.get(0).ratTalent * 2 + team.HC.get(0).ratDef) / 6;

        if (!isMedicalRS) {
            year++;
            if (wonAllConference) ratPot++;
            if (wonAllAmerican) ratPot++;
            if (year > 2 && gamesPlayed < 4) ratPot -= (int) (Math.random() * 15);

            ratFootIQ += (int) (Math.random() * (progression + gamesPlayed - 35)) / 10;
            ratCoverage += (int) (Math.random() * (progression + gamesPlayed - 35)) / 10;
            ratRunStop += (int) (Math.random() * (progression + gamesPlayed - 35)) / 10;
            ratTackle += (int) (Math.random() * (progression + gamesPlayed - 35)) / 10;
            ratSpeed += (int) (Math.random() * (progression + gamesPlayed - 35)) / 10;
            if (Math.random() * 100 < progression) {
                //breakthrough
                ratCoverage += (int) (Math.random() * (progression + gamesPlayed - 40)) / 10;
                ratRunStop += (int) (Math.random() * (progression + gamesPlayed - 40)) / 10;
                ratTackle += (int) (Math.random() * (progression + gamesPlayed - 40)) / 10;
                ratSpeed += (int) (Math.random() * (progression + gamesPlayed - 40)) / 10;
            }
        }
        ratOvr = (ratCoverage + ratRunStop * 2 + ratTackle * 2 + ratSpeed) / 6;
        ratImprovement = ratOvr - oldOvr;

        careerGamesPlayed += gamesPlayed;
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
    public ArrayList<String> getDetailStatsList(int games) {
        ArrayList<String> pStats = new ArrayList<>();
        pStats.add("Tackles: " + (statsTackles) + " >Sacks: " + (statsSacks));
        pStats.add("Fumbles: " + (statsFumbles) + " >Interceptions: " + (statsInts));
        pStats.add("Games: " + gamesPlayed + " (" + statsWins + "-" + (gamesPlayed - statsWins) + ")" + ">Durability: " + getLetterGrade(ratDur));
        pStats.add("Home Region: " + getRegion(region) + ">Personality: " + getLetterGrade(personality));
        pStats.add("Football IQ: " + getLetterGrade(ratFootIQ) + ">Coverage: " + getLetterGrade(ratCoverage));
        pStats.add("Run Stop: " + getLetterGrade(ratRunStop) + ">Tackling: " + getLetterGrade(ratTackle));
        pStats.add("Speed: " + getLetterGrade(ratSpeed) + ">Nothing ");
        pStats.add(" > ");
        return pStats;
    }

    @Override
    public ArrayList<String> getDetailAllStatsList(int games) {
        ArrayList<String> pStats = new ArrayList<>();
        pStats.add("Tackles: " + (statsTackles) + " >Sacks: " + (statsSacks));
        pStats.add("Fumbles: " + (statsFumbles) + " >Interceptions: " + (statsInts));
        pStats.add("Games: " + gamesPlayed + " (" + statsWins + "-" + (gamesPlayed - statsWins) + ")" + ">Durability: " + ratDur);
        pStats.add("Home Region: " + getRegion(region) + ">Personality: " + personality);
        pStats.add("Football IQ: " + ratFootIQ + ">Coverage: " + ratCoverage);
        pStats.add("Run Stop: " + ratRunStop + ">Tackling: " + ratTackle);
        pStats.add("Speed: " + ratSpeed + ">Nothing ");
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
                getLetterGrade(ratCoverage) + ", " + getLetterGrade(ratRunStop) + ", " + getLetterGrade(ratTackle) + ", " + getLetterGrade(ratSpeed) + ")";
    }

}
