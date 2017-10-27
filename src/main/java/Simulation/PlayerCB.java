package Simulation;

import java.util.ArrayList;

public class PlayerCB extends Player {

    //CBCov affects how good he is at covering the pass
    public int ratCoverage;
    //CBSpd affects how good he is at not letting up deep passes
    public int ratSpeed;
    //CBTkl affects how good he is at tackling
    public int ratTackle;
    public int ratJump;

    //Stats
    public int statsTackles;
    public int statsSacks;
    public int statsFumbles;
    public int statsInts;
    public int statsTargets;
    public int statsIncomplete;
    public int statsDefended;

    public int careerTackles;
    public int careerSacks;
    public int careerFumbles;
    public int careerInts;
    public int careerTargets;
    public int careerIncomplete;
    public int careerDefended;

    public PlayerCB(String nm, Team t, int yr, int pot, int iq, int cov, int spd, int tkl, boolean rs, int dur, int jmp) {
        team = t;
        name = nm;
        year = yr;
        gamesPlayed = 0;
        isInjured = false;
        ratOvr = (cov * 2 + spd + tkl) / 4;
        ratPot = pot;
        ratFootIQ = iq;
        ratDur = dur;
        ratCoverage = cov;
        ratSpeed = spd;
        ratTackle = tkl;
        ratJump = jmp;
        isRedshirt = rs;
        if (isRedshirt) year = 0;
        position = "CB";

        cost = (int) (Math.pow((float) ratOvr - 55, 2) / 4.5) + 50 + (int) (Math.random() * 100) - 50;

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
        statsTargets = 0;
        statsIncomplete = 0;
        statsDefended = 0;

        careerTackles = 0;
        careerSacks = 0;
        careerFumbles = 0;
        careerInts = 0;
        careerTargets = 0;
        careerDefended = 0;
    }

    public PlayerCB(String nm, Team t, int yr, int pot, int iq, int cov, int spd, int tkl, boolean rs, int dur, int jmp,
                    int cGamesPlayed, int cTackles, int cSacks, int cFumbles, int cInts, int cTar, int cInc, int cDef, int cHeismans, int cAA, int cAC, int cWins) {
        team = t;
        name = nm;
        year = yr;
        gamesPlayed = 0;
        isInjured = false;
        ratOvr = (cov * 2 + spd + tkl + jmp) / 5;
        ratPot = pot;
        ratFootIQ = iq;
        ratDur = dur;
        ratCoverage = cov;
        ratSpeed = spd;
        ratTackle = tkl;
        ratJump = jmp;
        isRedshirt = rs;
        if (isRedshirt) year = 0;
        position = "CB";

        cost = (int) (Math.pow((float) ratOvr - 55, 2) / 4.5) + 50 + (int) (Math.random() * 100) - 50;

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
        statsTargets = 0;
        statsIncomplete = 0;
        statsDefended = 0;

        careerTackles = cTackles;
        careerSacks = cSacks;
        careerFumbles = cFumbles;
        careerInts = cInts;
        careerTargets = cTar;
        careerIncomplete = cInc;
        careerDefended = cDef;
    }

    public PlayerCB(String nm, int yr, int stars, Team t) {
        name = nm;
        year = yr;
        team = t;
        gamesPlayed = 0;
        isInjured = false;
        ratPot = (int) (50 + 50 * Math.random());
        ratFootIQ = (int) (50 + 50 * Math.random());
        ratDur = (int) (50 + 50 * Math.random());
        ratCoverage = (int) (60 + year * 5 + stars * 5 - 25 * Math.random());
        ratSpeed = (int) (60 + year * 5 + stars * 5 - 25 * Math.random());
        ratTackle = (int) (60 + year * 5 + stars * 5 - 25 * Math.random());
        ratJump = (int) (60 + year * 5 + stars * 5 - 25 * Math.random());
        ratOvr = (ratCoverage * 2 + ratSpeed + ratTackle + ratJump) / 5;
        position = "CB";

        cost = (int) (Math.pow((float) ratOvr - 55, 2) / 4.5) + 50 + (int) (Math.random() * 100) - 50;

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
        statsTargets = 0;
        statsIncomplete = 0;
        statsDefended = 0;

        careerTackles = 0;
        careerSacks = 0;
        careerFumbles = 0;
        careerInts = 0;
        careerTargets = 0;
        careerIncomplete = 0;
        careerDefended = 0;
    }

    @Override
    public int getHeismanScore() {
        return statsTackles * 35 + statsSacks * 500 + statsFumbles * 500 + statsInts * 500 + statsDefended * 45 + statsIncomplete * 15 - statsTargets * 10 + ratCoverage * 14 + ratSpeed * 5 + ratJump * 2 - (1 * team.teamOppYards) - (2 * team.teamOppPoints) + team.confPrestige * 7;
    }

    @Override
    public void advanceSeason() {
        int oldOvr = ratOvr;
        progression = (ratPot * 3 + team.HC.get(0).ratTalent * 2 + team.HC.get(0).ratOff) / 6;


        if (!isMedicalRS) {
            year++;
            if (wonAllConference) ratPot++;
            if (wonAllAmerican) ratPot++;
            if (year > 2 && gamesPlayed < 4) ratPot -= (int) (Math.random() * 15);
            // old: ratPot - 25
            // new ratPot + gamesPlayed - 35
            ratFootIQ += (int) (Math.random() * (progression + gamesPlayed - 35)) / 10;
            ratCoverage += (int) (Math.random() * (progression + gamesPlayed - 35)) / 10;
            ratSpeed += (int) (Math.random() * (progression + gamesPlayed - 35)) / 10;
            ratTackle += (int) (Math.random() * (progression + gamesPlayed - 35)) / 10;
            ratJump += (int) (Math.random() * (progression + gamesPlayed - 25)) / 10;
            if (Math.random() * 100 < progression) {
                //breakthrough
                ratCoverage += (int) (Math.random() * (progression + gamesPlayed - 40)) / 10;
                ratSpeed += (int) (Math.random() * (progression + gamesPlayed - 40)) / 10;
                ratTackle += (int) (Math.random() * (progression + gamesPlayed - 40)) / 10;
                ratJump += (int) (Math.random() * (progression + gamesPlayed - 30)) / 10;
            }
        }
        ratOvr = (ratCoverage * 2 + ratSpeed + ratTackle + ratJump) / 5;
        ratImprovement = ratOvr - oldOvr;

        careerGamesPlayed += gamesPlayed;
        careerWins += statsWins;

        careerTackles += statsTackles;
        careerSacks += statsSacks;
        careerFumbles += statsFumbles;
        careerInts += statsInts;
        careerTargets += statsTargets;
        careerIncomplete += statsIncomplete;
        careerDefended += statsDefended;

        statsTackles = 0;
        statsSacks = 0;
        statsFumbles = 0;
        statsInts = 0;
        statsTargets = 0;
        statsIncomplete = 0;
        statsDefended = 0;

        if (wonHeisman) careerHeismans++;
        if (wonAllAmerican) careerAllAmerican++;
        if (wonAllConference) careerAllConference++;

    }

    @Override
    public ArrayList<String> getDetailStatsList(int games) {
        ArrayList<String> pStats = new ArrayList<>();
        pStats.add("Tackles: " + (statsTackles) + " >Sacks: " + (statsSacks));
        pStats.add("Fumbles: " + (statsFumbles) + " >Interceptions: " + (statsInts));
        pStats.add("Defended: " + statsDefended + ">Shutdown Pct: " + (100 * (statsIncomplete) / (statsTargets + 1)) + "%");
        pStats.add("Games: " + gamesPlayed + " (" + statsWins + "-" + (gamesPlayed - statsWins) + ")" + ">Durability: " + getLetterGrade(ratDur));
        pStats.add("Football IQ: " + getLetterGrade(ratFootIQ) + ">Coverage: " + getLetterGrade(ratCoverage));
        pStats.add("Speed: " + getLetterGrade(ratSpeed) + ">Tackling: " + getLetterGrade(ratTackle));
        pStats.add("Jumping: " + getLetterGrade(ratJump) + ">Nothing");
        pStats.add(" > ");
        return pStats;
    }

    @Override
    public ArrayList<String> getDetailAllStatsList(int games) {
        ArrayList<String> pStats = new ArrayList<>();
        pStats.add("Tackles: " + (statsTackles) + " >Sacks: " + (statsSacks));
        pStats.add("Fumbles: " + (statsFumbles) + " >Interceptions: " + (statsInts));
        pStats.add("Defended: " + statsDefended + ">Shutdown Pct: " + (100 * (statsIncomplete) / (statsTargets + 1)) + "%");
        pStats.add("Games: " + gamesPlayed + " (" + statsWins + "-" + (gamesPlayed - statsWins) + ")" + ">Durability: " + getLetterGrade(ratDur));
        pStats.add("Football IQ: " + getLetterGrade(ratFootIQ) + ">Coverage: " + getLetterGrade(ratCoverage));
        pStats.add("Speed: " + getLetterGrade(ratSpeed) + ">Tackling: " + getLetterGrade(ratTackle));
        pStats.add("Jumping: " + getLetterGrade(ratJump) + ">Nothing");
        pStats.add("[B]CAREER STATS:");
        pStats.addAll(getCareerStatsList());
        return pStats;
    }

    @Override
    public ArrayList<String> getCareerStatsList() {
        ArrayList<String> pStats = new ArrayList<>();
        pStats.add("Tackles: " + (statsTackles + careerTackles) + " >Sacks: " + (statsSacks + careerSacks));
        pStats.add("Fumbles: " + (statsFumbles + careerFumbles) + " >Interceptions: " + (statsInts + careerInts));
        pStats.add("Defended: " + (statsDefended + careerDefended) + ">Shutdown Pct: " + (100 * (statsIncomplete + careerIncomplete) / (statsTargets + careerTargets + 1)) + "%");
        pStats.addAll(super.getCareerStatsList());
        return pStats;
    }


    @Override
    public String getInfoForLineup() {
        if (injury != null)
            return getInitialName() + " [" + getYrStr() + "] " + ratOvr + "/" + getLetterGrade(ratPot) + " " + injury.toString();
        return getInitialName() + " [" + getYrStr() + "] " + ratOvr + "/" + getLetterGrade(ratPot) + " (" +
                getLetterGrade(ratCoverage) + ", " + getLetterGrade(ratSpeed) + ", " + getLetterGrade(ratTackle) + ", " + getLetterGrade(ratJump) + ")";
    }

}
