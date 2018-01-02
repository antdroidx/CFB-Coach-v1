package Simulation;

import java.util.ArrayList;

/**
 * Created by ahngu on 10/9/2017.
 * <p>
 * TE should be able to catch the ball, and help block for rushes or passing game
 * Currently using the WR role as base
 */

public class PlayerTE extends Player {

    //RecCat affects how good he is at catching
    public int ratCatch;
    //BlkR Blocking for Rushes
    public int ratRunBlock;
    //RecEva affects how easily he can dodge tackles
    public int ratEvasion;
    //    public int ratSpeed;
    public int ratSpeed;

    //public Vector ratingsVector;

    //Stats
    public int statsTargets;
    public int statsReceptions;
    public int statsRecYards;
    public int statsRecTD;
    public int statsDrops;
    public int statsFumbles;

    public int careerTargets;
    public int careerReceptions;
    public int careerRecYards;
    public int careerTD;
    public int careerDrops;
    public int careerFumbles;

    public PlayerTE(String nm, Team t, int yr, int pot, int iq, int cat, int blk, int eva, boolean rs, int dur, int rsp, int reg, int trait) {
        team = t;
        name = nm;
        year = yr;
        gamesStarted = 0;
        gamesPlayed = 0;
        isInjured = false;
        ratOvr = (cat * 2 + blk * 2 + eva) / 5;
        ratPot = pot;
        ratFootIQ = iq;
        ratDur = dur;
        ratCatch = cat;
        ratRunBlock = blk;
        ratEvasion = eva;
        ratSpeed = rsp;
        isRedshirt = rs;
        if (isRedshirt) year = 0;
        region = reg;
        personality = trait;

        troubledTimes = 0;

        statsTargets = 0;
        statsReceptions = 0;
        statsRecYards = 0;
        statsRecTD = 0;
        statsDrops = 0;
        statsFumbles = 0;
        wonHeisman = false;
        wonAllAmerican = false;
        wonAllConference = false;
        statsWins = 0;

        careerTargets = 0;
        careerReceptions = 0;
        careerRecYards = 0;
        careerTD = 0;
        careerDrops = 0;
        careerFumbles = 0;
        careerGames = 0;
        careerHeismans = 0;
        careerAllAmerican = 0;
        careerAllConference = 0;
        careerWins = 0;

        position = "TE";
    }

    public PlayerTE(String nm, Team t, int yr, int pot, int iq, int cat, int blk, int eva, boolean rs, int dur, int spd,
                    int cGamesPlayed, int cTargets, int cReceptions, int cRecYards, int cTD, int cDrops, int cFumbles,
                    int cHeismans, int cAA, int cAC, int cWins, boolean transfer, int reg, int trait) {
        team = t;
        name = nm;
        year = yr;
        gamesStarted = 0;
        gamesPlayed = 0;
        isInjured = false;
        ratOvr = (cat * 2 + blk * 2 + eva + spd) / 6;
        ratPot = pot;
        ratFootIQ = iq;
        ratDur = dur;
        ratCatch = cat;
        ratRunBlock = blk;
        ratEvasion = eva;
        ratSpeed = spd;
        isRedshirt = rs;
        if (isRedshirt) year = 0;
        isTransfer = transfer;
        region = reg;
        personality = trait;

        troubledTimes = 0;

        statsTargets = 0;
        statsReceptions = 0;
        statsRecYards = 0;
        statsRecTD = 0;
        statsDrops = 0;
        statsFumbles = 0;
        wonHeisman = false;
        wonAllAmerican = false;
        wonAllConference = false;
        statsWins = 0;

        careerTargets = cTargets;
        careerReceptions = cReceptions;
        careerRecYards = cRecYards;
        careerTD = cTD;
        careerDrops = cDrops;
        careerFumbles = cFumbles;
        careerGames = cGamesPlayed;
        careerHeismans = cHeismans;
        careerAllAmerican = cAA;
        careerAllConference = cAC;
        careerWins = cWins;

        position = "TE";
    }

    public PlayerTE(String nm, int yr, int stars, Team t) {
        name = nm;
        year = yr;
        team = t;
        gamesStarted = 0;
        gamesPlayed = 0;
        isInjured = false;
        ratPot = (int) (attrBase + 50 * Math.random());
        ratFootIQ = (int) (attrBase + 50 * Math.random());
        ratDur = (int) (attrBase + 50 * Math.random());
        ratCatch = (int) (ratBase + year*yearFactor + stars*starFactor - ratTolerance*Math.random());
        ratRunBlock = (int) (ratBase + year*yearFactor + stars*starFactor - ratTolerance*Math.random());
        ratEvasion = (int) (ratBase + year*yearFactor + stars*starFactor - ratTolerance*Math.random());
        ratSpeed = (int) ((ratBase-15) + year*yearFactor + stars*starFactor - ratTolerance*Math.random());
        ratOvr = (ratCatch * 2 + ratRunBlock * 2 + ratEvasion + ratSpeed) / 6;
        region = (int)(Math.random()*5);
        personality = (int) (attrBase + 50 * Math.random());

        //cost = (int) (Math.pow((float) ratOvr - 55, 2) / 4) + 75 + (int) (Math.random() * 100) - 50;

        recruitTolerance = (int)((60 - team.teamPrestige)/teImportance);
        cost = (int)((Math.pow((float) ratOvr - costBaseRating, 2)/5) + (int)Math.random()*recruitTolerance);

        cost = (int)(cost/teImportance);

        double locFactor = Math.abs(team.location - region) - 2.5;
        cost = cost + (int)(Math.random()*(locFactor * locationDiscount));
        if (cost < 0) cost = (int)Math.random()*7+1;

        troubledTimes = 0;

        statsTargets = 0;
        statsReceptions = 0;
        statsRecYards = 0;
        statsRecTD = 0;
        statsDrops = 0;
        statsFumbles = 0;
        wonHeisman = false;
        wonAllAmerican = false;
        wonAllConference = false;
        statsWins = 0;

        careerTargets = 0;
        careerReceptions = 0;
        careerRecYards = 0;
        careerTD = 0;
        careerDrops = 0;
        careerFumbles = 0;
        careerGames = 0;
        careerHeismans = 0;
        careerAllAmerican = 0;
        careerAllConference = 0;
        careerWins = 0;

        position = "TE";
    }

    public PlayerTE(String nm, int yr, int stars, Team t, boolean custom) {
        name = nm;
        year = yr;
        team = t;
        gamesStarted = 0;
        gamesPlayed = 0;
        isInjured = false;
        ratPot = (int) (50 + 50 * Math.random());
        ratFootIQ = (int) (50 + 50 * Math.random());
        ratDur = (int) (50 + 50 * Math.random());
        ratCatch = (int) (ratBase + stars * customFactor - ratTolerance * Math.random());
        ratRunBlock = (int) (ratBase + stars * customFactor - ratTolerance * Math.random());
        ratEvasion = (int) (ratBase + stars * customFactor - ratTolerance * Math.random());
        ratSpeed = (int) ((ratBase-15) + stars * customFactor - ratTolerance * Math.random());
        ratOvr = (ratCatch * 2 + ratRunBlock * 2 + ratEvasion + ratSpeed) / 6;
        region = (int)(Math.random()*5);
        personality = (int) (50 + 50 * Math.random());

        troubledTimes = 0;

        statsTargets = 0;
        statsReceptions = 0;
        statsRecYards = 0;
        statsRecTD = 0;
        statsDrops = 0;
        statsFumbles = 0;
        wonHeisman = false;
        wonAllAmerican = false;
        wonAllConference = false;
        statsWins = 0;

        careerTargets = 0;
        careerReceptions = 0;
        careerRecYards = 0;
        careerTD = 0;
        careerDrops = 0;
        careerFumbles = 0;
        careerGames = 0;
        careerHeismans = 0;
        careerAllAmerican = 0;
        careerAllConference = 0;
        careerWins = 0;

        position = "TE";
    }

    @Override
    public void advanceSeason() {
        int oldOvr = ratOvr;
        progression = (ratPot * 3 + team.HC.get(0).ratTalent * 2 + team.HC.get(0).ratOff) / 6;
        int games = gamesStarted + (gamesPlayed-gamesStarted)/3;

        if (!isMedicalRS) {
            year++;
            if (wonAllConference) ratPot++;
            if (wonAllAmerican) ratPot++;
            if (year > 2 && games < 4) ratPot -= (int) (Math.random() * 15);

            ratFootIQ += (int) (Math.random() * (progression + games - 35)) / 10;
            ratCatch += (int) (Math.random() * (progression + games - 35)) / 10;
            ratRunBlock += (int) (Math.random() * (progression + games - 35)) / 10;
            ratEvasion += (int) (Math.random() * (progression + games - 35)) / 10;
            ratSpeed += (int) (Math.random() * (progression + games - 20)) / 10;
            if (Math.random() * 100 < progression) {
                //breakthrough
                ratCatch += (int) (Math.random() * (progression + games - 40)) / 10;
                ratRunBlock += (int) (Math.random() * (progression + games - 40)) / 10;
                ratEvasion += (int) (Math.random() * (progression + games - 40)) / 10;
                ratSpeed += (int) (Math.random() * (progression + games - 30)) / 10;
            }
        }
        
        ratOvr = (ratCatch * 2 + ratRunBlock * 2 + ratEvasion + ratSpeed) / 6;
        ratImprovement = ratOvr - oldOvr;
        //reset stats (keep career stats?)
        careerTargets += statsTargets;
        careerReceptions += statsReceptions;
        careerRecYards += statsRecYards;
        careerTD += statsRecTD;
        careerDrops += statsDrops;
        careerFumbles += statsFumbles;
        careerGames += gamesPlayed;
        careerWins += statsWins;

        if (wonHeisman) careerHeismans++;
        if (wonAllAmerican) careerAllAmerican++;
        if (wonAllConference) careerAllConference++;

        statsTargets = 0;
        statsReceptions = 0;
        statsRecYards = 0;
        statsRecTD = 0;
        statsDrops = 0;
        statsFumbles = 0;

        if (isTransfer) {
            isTransfer = false;
            year -= 1;
        }
    }

    @Override
    public int getHeismanScore() {
        return statsRecTD * 175 - statsFumbles * 100 - statsDrops * 50 + statsRecYards * 3 + team.confPrestige * 7;
    }

    @Override
    public ArrayList<String> getDetailStatsList(int games) {
        ArrayList<String> pStats = new ArrayList<>();
        pStats.add("TDs: " + statsRecTD + ">Fumbles: " + statsFumbles);
        pStats.add("Rec Yards: " + statsRecYards + " yds>Receptions: " + statsReceptions);
        pStats.add("Catch Percent: " + (100 * statsReceptions / (statsTargets + 1)) + ">Yards/Tgt: " + ((double) (10 * statsRecYards / (statsTargets + 1)) / 10) + " yds");
        pStats.add("Yds/Game: " + (statsRecYards / getGames()) + " yds/g>Drops: " + statsDrops);
        pStats.add("Games: " + gamesPlayed + " (" + statsWins + "-" + (gamesStarted - statsWins) + ")" + ">Durability: " + getLetterGrade(ratDur));
        pStats.add("Home Region: " + getRegion(region) + ">Personality: " + getLetterGrade(personality));
        pStats.add("Football IQ: " + getLetterGrade(ratFootIQ) + ">Catching: " + getLetterGrade(ratCatch));
        pStats.add("Blocking: " + getLetterGrade(ratRunBlock) + ">Evasion: " + getLetterGrade(ratEvasion));
        pStats.add("Speed: " + getLetterGrade(ratSpeed) + ">Nothing ");
        pStats.add(" > ");
        return pStats;
    }

    @Override
    public ArrayList<String> getDetailAllStatsList(int games) {
        ArrayList<String> pStats = new ArrayList<>();
        pStats.add("TDs: " + statsRecTD + ">Fumbles: " + statsFumbles);
        pStats.add("Rec Yards: " + statsRecYards + " yds>Receptions: " + statsReceptions);
        pStats.add("Catch Percent: " + (100 * statsReceptions / (statsTargets + 1)) + ">Yards/Tgt: " + ((double) (10 * statsRecYards / (statsTargets + 1)) / 10) + " yds");
        pStats.add("Yds/Game: " + (statsRecYards / getGames()) + " yds/g>Drops: " + statsDrops);
        pStats.add("Games: " + gamesPlayed + " (" + statsWins + "-" + (gamesStarted - statsWins) + ")" + ">Durability: " + ratDur);
        pStats.add("Home Region: " + getRegion(region) + ">Personality: " + personality);
        pStats.add("Football IQ: " + ratFootIQ + ">Catching: " + ratCatch);
        pStats.add("Blocking: " + ratRunBlock + ">Evasion: " + ratEvasion);
        pStats.add("Speed: " + ratSpeed + ">Nothing ");
        pStats.add("[B]CAREER STATS:");
        pStats.addAll(getCareerStatsList());
        return pStats;
    }

    @Override
    public ArrayList<String> getCareerStatsList() {
        ArrayList<String> pStats = new ArrayList<>();
        pStats.add("TDs: " + (statsRecTD + careerTD) + ">Fumbles: " + (statsFumbles + careerFumbles));
        pStats.add("Rec Yards: " + (statsRecYards + careerRecYards) + " yds>Receptions: " + (statsReceptions + careerReceptions));
        pStats.add("Catch Percent: " + (100 * (statsReceptions + careerReceptions) / (statsTargets + careerTargets + 1)) + ">Yards/Tgt: " + ((double) ((10 * statsRecYards + careerRecYards) / (statsTargets + careerTargets + 1)) / 10) + " yds");
        pStats.add("Yds/Game: " + ((statsRecYards + careerRecYards) / (getGames() + careerGames)) + " yds/g>Drops: " + (statsDrops + careerDrops));
        pStats.addAll(super.getCareerStatsList());
        return pStats;
    }

    @Override
    public String getInfoForLineup() {
        if (injury != null)
            return getInitialName() + " [" + getYrStr() + "] " + ratOvr + "/" + getLetterGrade(ratPot) + " " + injury.toString();
        return getInitialName() + " [" + getYrStr() + "] " + ratOvr + "/" + getLetterGrade(ratPot) + " (" +
                getLetterGrade(ratCatch) + ", " + getLetterGrade(ratRunBlock) + ", " + getLetterGrade(ratEvasion) + ", " + getLetterGrade(ratSpeed) + ")";
    }

}
