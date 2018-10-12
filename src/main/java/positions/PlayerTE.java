package positions;

import java.util.ArrayList;

import simulation.Team;

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

    //Size Config
    private final int hAvg = 76;
    private final int hMax = 3;
    private final int hMin = -2;
    private final int wAvg = 254;
    private final int wMax = 30;
    private final int wMin = -20;

    public PlayerTE(Team t, String nm, int yr, int reg, int trait, int iq, int scout, boolean transfer, boolean wasRS, int pot, int dur, boolean rs, int cat, int blk, int eva, int spd, int h, int w) {
        position = "TE";

        team = t;
        name = nm;
        year = yr;

        ratPot = pot;
        ratFootIQ = iq;
        ratDur = dur;
        ratCatch = cat;
        ratRunBlock = blk;
        ratEvasion = eva;
        ratSpeed = spd;
        isRedshirt = rs;
        wasRedshirt = wasRS;

        region = reg;
        personality = trait;
        recruitRating = scout;
        height = h;
        weight = w;
        ratOvr = getOverall();

        resetSeasonStats();
        resetCareerStats();
    }


    public PlayerTE(Team t, String nm, int yr, int reg, int trait, int iq, int scout, boolean transfer, boolean wasRS, boolean wo, int pot, int dur, boolean rs, int cGamesPlayed, int cWins, int cHeismans, int cAA, int cAC, int cTF, int cAF,
                    int cat, int blk, int eva, int spd, int h, int w, int cTargets, int cReceptions, int cRecYards, int cTD, int cDrops, int cFumbles) {
        position = "TE";
        team = t;
        name = nm;
        year = yr;

        ratPot = pot;
        ratFootIQ = iq;
        ratDur = dur;
        ratCatch = cat;
        ratRunBlock = blk;
        ratEvasion = eva;
        ratSpeed = spd;
        isRedshirt = rs;
        wasRedshirt = wasRS;

        isTransfer = transfer;
        isWalkOn = wo;
        region = reg;
        personality = trait;
        recruitRating = scout;
        height = h;
        weight = w;
        ratOvr = getOverall();

        resetSeasonStats();

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
        careerTopFreshman = cTF;
        careerAllFreshman = cAF;
        careerWins = cWins;
    }

    public PlayerTE(String nm, int yr, int stars, Team t) {
        position = "TE";
        height = hAvg + (int) (Math.random() * ((hMax - hMin) + 1)) + hMin;
        weight = wAvg + (int) (Math.random() * ((wMax - wMin) + 1)) + wMin;
        name = nm;
        year = yr;
        team = t;

        wasRedshirt = getWasRedshirtStatus();

        ratPot = (int) (attrBase + 50 * Math.random());
        ratFootIQ = (int) (attrBase + 50 * Math.random());
        ratDur = (int) (attrBase + 50 * Math.random());
        ratCatch = (int) (ratBase + year * yearFactor + stars * starFactor - ratTolerance * Math.random());
        ratRunBlock = (int) (ratBase + year * yearFactor + stars * starFactor - ratTolerance * Math.random());
        ratEvasion = (int) (ratBase + year * yearFactor + stars * starFactor - ratTolerance * Math.random());
        ratSpeed = (int) ((ratBase - 10) + year * yearFactor + stars * starFactor - ratTolerance * Math.random());
        ratOvr = getOverall();
        region = (int) (Math.random() * 5);
        personality = (int) (attrBase + 50 * Math.random());

        recruitRating = getScoutingGrade();

        recruitTolerance = (int) ((60 - team.teamPrestige) / teImportance);
        cost = getInitialCost();
        cost = (int) (cost / teImportance);
        cost = getLocationCost();
        if (cost < 0) cost = (int) Math.random() * 5 + 1;

        resetSeasonStats();
        resetCareerStats();
    }

    public PlayerTE(String nm, int yr, int stars, Team t, boolean custom) {
        position = "TE";
        height = hAvg + (int) (Math.random() * ((hMax - hMin) + 1)) + hMin;
        weight = wAvg + (int) (Math.random() * ((wMax - wMin) + 1)) + wMin;
        name = nm;
        year = yr;
        team = t;

        wasRedshirt = getWasRedshirtStatus();

        ratPot = (int) (50 + 50 * Math.random());
        ratFootIQ = (int) (50 + 50 * Math.random());
        ratDur = (int) (50 + 50 * Math.random());
        ratCatch = (int) (ratBase + stars * customFactor - ratTolerance * Math.random());
        ratRunBlock = (int) (ratBase + stars * customFactor - ratTolerance * Math.random());
        ratEvasion = (int) (ratBase + stars * customFactor - ratTolerance * Math.random());
        ratSpeed = (int) ((ratBase - 10) + stars * customFactor - ratTolerance * Math.random());
        ratOvr = getOverall();
        region = (int) (Math.random() * 5);
        personality = (int) (50 + 50 * Math.random());

        if (custom) isWalkOn = true;
        recruitRating = getScoutingGrade();
        resetSeasonStats();
        resetCareerStats();
    }

    public void midSeasonProgression() {
        final int ratOvrStart = ratOvr;
        progression = getProgressionOff();
        double games = getMidSeasonBonus();

        ratFootIQ += (int) (Math.random() * games);
        ratCatch += (int) (Math.random() * games);
        ratRunBlock += (int) (Math.random() * games);
        ratEvasion += (int) (Math.random() * games);
        ratSpeed += (int) (Math.random() * games);

        ratOvr = getOverall();
        ratImprovement = ratOvr - ratOvrStart;
    }


    @Override
    public void advanceSeason() {
        double games = getGamesBonus();

        if (!isMedicalRS) {
            if (wonAllConference) ratPot += (int) Math.random() * allConfPotBonus;
            if (wonAllAmerican) ratPot += (int) Math.random() * allAmericanBonus;
            if (wonAllFreshman) ratPot += (int) Math.random() * allFreshmanBonus;
            if (wonTopFreshman) ratPot += (int) Math.random() * topBonus;
            if (wonHeisman) ratPot += (int) Math.random() * topBonus;
            progression = getProgressionOff();

            if (year > 2 && games < minGamesPot) ratPot -= (int) (Math.random() * 15);

            ratFootIQ += (int) (Math.random() * (progression + games - endseason)) / endseasonFactor;
            ratCatch += (int) (Math.random() * (progression + games - endseason)) / endseasonFactor;
            ratRunBlock += (int) (Math.random() * (progression + games - endseason)) / endseasonFactor;
            ratEvasion += (int) (Math.random() * (progression + games - endseason)) / endseasonFactor;
            ratSpeed += ((int) (Math.random() * (progression + games - endseason)) / endseasonFactor) / 1.5;
            if (Math.random() * 100 < progression) {
                //breakthrough
                ratCatch += (int) (Math.random() * (progression + games - endseasonBonus)) / endseasonFactor;
                ratRunBlock += (int) (Math.random() * (progression + games - endseasonBonus)) / endseasonFactor;
                ratEvasion += (int) (Math.random() * (progression + games - endseasonBonus)) / endseasonFactor;
                ratSpeed += ((int) (Math.random() * (progression + games - endseasonBonus)) / endseasonFactor) / 1.5;
            }

            durabilityProgression();

        }

        ratOvr = getOverall();
        ratImprovement = ratOvr - ratOvrStart;
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
        if (wonAllFreshman) careerAllFreshman++;
        if (wonTopFreshman) careerTopFreshman++;

        if (isTransfer || isRedshirt || isMedicalRS) {
            isTransfer = false;
            wasRedshirt = true;
        } else {
            year++;
        }

    }

    private void resetSeasonStats() {
        gamesStarted = 0;
        gamesPlayed = 0;
        isInjured = false;
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
        wonAllFreshman = false;
        wonTopFreshman = false;

        statsWins = 0;

    }

    private void resetCareerStats() {
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
        careerAllFreshman = 0;
        careerTopFreshman = 0;
        careerWins = 0;
    }


    @Override
    public int getHeismanScore() {
        return statsRecTD * 250 - statsFumbles * 75 + statsReceptions * 2 - statsDrops * 25 + statsRecYards * 2 + ratOvr * 10 + getConfPrestigeBonus();
    }

    @Override
    public int getCareerScore() {
        return statsRecTD * 250 - statsFumbles * 75 + statsReceptions * 2 - statsDrops * 25 + statsRecYards * 2 + ratOvr * 10 +
                careerTD * 250 - careerFumbles * 75 + careerReceptions * 2 - careerDrops * 25 + careerRecYards * 2 + ratOvr * 10 * year;
    }

    @Override
    public ArrayList<String> getDetailStatsList(int games) {
        ArrayList<String> pStats = new ArrayList<>();
        pStats.add("Height " + getHeight() + ">Weight: " + getWeight());
        pStats.add("TDs: " + statsRecTD + ">Fumbles: " + statsFumbles);
        pStats.add("Rec Yards: " + statsRecYards + " yds>Receptions: " + statsReceptions);
        pStats.add("Catch Percent: " + (100 * statsReceptions / (statsTargets + 1)) + ">Yards/Tgt: " + ((double) (10 * statsRecYards / (statsTargets + 1)) / 10) + " yds");
        pStats.add("Yds/Game: " + (statsRecYards / getGames()) + " yds/g>Drops: " + statsDrops);
        pStats.add("Games: " + gamesPlayed + " (" + statsWins + "-" + (gamesStarted - statsWins) + ")" + "> ");
        pStats.add("Catching: " + getLetterGrade(ratCatch) + ">Rec Speed: " + getLetterGrade(ratSpeed));
        pStats.add("Evasion: " + getLetterGrade(ratEvasion) + ">Run Block: " + getLetterGrade(ratRunBlock));
        pStats.add("Durability: " + getLetterGrade(ratDur) + ">Football IQ: " + getLetterGrade(ratFootIQ));
        pStats.add("Home Region: " + getRegion(region) + ">Personality: " + getLetterGrade(personality));
        pStats.add("Scout Grade: " + getScoutingGradeString() + " > " + getStatus());
        pStats.add(" > ");
        return pStats;
    }

    @Override
    public ArrayList<String> getDetailAllStatsList(int games) {
        ArrayList<String> pStats = new ArrayList<>();
        pStats.add("Height " + getHeight() + ">Weight: " + getWeight());
        pStats.add("TDs: " + statsRecTD + ">Fumbles: " + statsFumbles);
        pStats.add("Rec Yards: " + statsRecYards + " yds>Receptions: " + statsReceptions);
        pStats.add("Catch Percent: " + (100 * statsReceptions / (statsTargets + 1)) + ">Yards/Tgt: " + ((double) (10 * statsRecYards / (statsTargets + 1)) / 10) + " yds");
        pStats.add("Yds/Game: " + (statsRecYards / getGames()) + " yds/g>Drops: " + statsDrops);
        pStats.add("Games: " + gamesPlayed + " (" + statsWins + "-" + (gamesStarted - statsWins) + ")" + "> ");
        pStats.add("Catching: " + getLetterGrade(ratCatch) + ">Rec Speed: " + getLetterGrade(ratSpeed));
        pStats.add("Evasion: " + getLetterGrade(ratEvasion) + ">Run Block: " + getLetterGrade(ratRunBlock));
        pStats.add("Durability: " + getLetterGrade(ratDur) + ">Football IQ: " + getLetterGrade(ratFootIQ));
        pStats.add("Home Region: " + getRegion(region) + ">Personality: " + getLetterGrade(personality));
        pStats.add("Scout Grade: " + getScoutingGradeString() + " > " + getStatus());
        pStats.add(" > ");
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
            return getInitialName() + " [" + getYrStr() + "] " + ratOvr + "/" + getPotRating(ratPot, ratOvr, year, team.HC.get(0).ratTalent) + " " + injury.toString();
        return getInitialName() + " [" + getYrStr() + "] " + ratOvr + "/" + getPotRating(ratPot, ratOvr, year, team.HC.get(0).ratTalent) + " (" +
                getLetterGrade(ratCatch) + ", " + getLetterGrade(ratRunBlock) + ", " + getLetterGrade(ratEvasion) + ", " + getLetterGrade(ratSpeed) + ")";
    }

    public int getOverall() {
        int ovr;
        ovr = (ratCatch * 2 + ratRunBlock * 2 + ratEvasion + ratSpeed) / 6;
        return ovr;
    }

}
