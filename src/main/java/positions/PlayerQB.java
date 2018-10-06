package positions;

import java.util.ArrayList;

import simulation.Team;

/**
 * Class for QB player.
 *
 * @author Achi
 */
public class PlayerQB extends Player {

    //PassPow affects how far he can throw
    public int ratPassPow;
    //PassAcc affects how accurate his passes are
    public int ratPassAcc;
    //PassEva (evasiveness) affects how easily he can dodge sacks
    public int ratEvasion;
    public int ratSpeed;

    //Stats
    public int statsPassAtt;
    public int statsPassComp;
    public int statsPassTD;
    public int statsInt;
    public int statsPassYards;
    public int statsSacked;
    public int statsRushAtt;
    public int statsRushYards;
    public int statsRushTD;
    public int statsFumbles;

    public int careerPassAtt;
    public int careerPassComp;
    public int careerTDs;
    public int careerInt;
    public int careerPassYards;
    public int careerSacked;
    public int careerRushAtt;
    public int careerRushYards;
    public int careerRushTD;
    public int careerFumbles;

    //Size Config
    private final int hAvg = 75;
    private final int hMax = 3;
    private final int hMin = -4;
    private final int wAvg = 223;
    private final int wMax = 30;
    private final int wMin = -35;

    public PlayerQB(Team t, String nm, int yr, int reg, int trait, int iq, int scout, boolean transfer, boolean wasRS, int pot, int dur, boolean rs, int pow, int acc, int eva, int spd, int h, int w) {
        position = "QB";
        team = t;
        name = nm;
        year = yr;
        ratPot = pot;
        ratFootIQ = iq;
        ratDur = dur;
        ratPassPow = pow;
        ratPassAcc = acc;
        ratEvasion = eva;
        ratSpeed = spd;
        isRedshirt = rs;
        if (isRedshirt) year = 0;
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

    public PlayerQB(Team t, String nm, int yr, int reg, int trait, int iq, int scout, boolean transfer, boolean wasRS, boolean wo, int pot, int dur, boolean rs, int cGamesPlayed, int cWins, int cHeismans, int cAA, int cAC, int cTF, int cAF,
                    int pow, int acc, int eva, int spd, int h, int w, int cPassAtt, int cPassComp, int cTDs, int cInt, int cPassYards, int cSacked,
                    int cRushAtt, int cRushYards, int cRushTD, int cFumbles) {

        position = "QB";
        team = t;
        name = nm;
        year = yr;
        ratPot = pot;
        ratFootIQ = iq;
        ratDur = dur;
        ratPassPow = pow;
        ratPassAcc = acc;
        ratEvasion = eva;
        ratSpeed = spd;
        isRedshirt = rs;
        if (isRedshirt) year = 0;
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

        careerPassAtt = cPassAtt;
        careerPassComp = cPassComp;
        careerTDs = cTDs;
        careerInt = cInt;
        careerPassYards = cPassYards;
        careerSacked = cSacked;
        careerRushAtt = cRushAtt;
        careerRushYards = cRushYards;
        careerRushTD = cRushTD;
        careerFumbles = cFumbles;

        careerGames = cGamesPlayed;
        careerHeismans = cHeismans;
        careerAllAmerican = cAA;
        careerAllConference = cAC;
        careerTopFreshman = cTF;
        careerAllFreshman = cAF;
        careerWins = cWins;

    }

    public PlayerQB(String nm, int yr, int stars, Team t) {
        position = "QB";
        height = hAvg + (int) (Math.random() * ((hMax - hMin) + 1)) + hMin;
        weight = wAvg + (int) (Math.random() * ((wMax - wMin) + 1)) + wMin;
        name = nm;
        year = yr;
        team = t;

        ratPot = (int) (attrBase + 50 * Math.random());
        ratFootIQ = (int) (attrBase + 50 * Math.random());
        ratDur = (int) (attrBase + 50 * Math.random());
        ratPassPow = (int) (ratBase + year * yearFactor + stars * starFactor - ratTolerance * Math.random());
        ratPassAcc = (int) (ratBase + year * yearFactor + stars * starFactor - ratTolerance * Math.random());
        ratEvasion = (int) (ratBase + year * yearFactor + stars * starFactor - ratTolerance * Math.random());
        ratSpeed = (int) ((ratBase - 5) + year * yearFactor + stars * starFactor - ratTolerance * Math.random());
        ratOvr = getOverall();
        region = (int) (Math.random() * 5);
        personality = (int) (attrBase + 50 * Math.random());

        recruitRating = getScoutingGrade();

        recruitTolerance = (int) ((60 - team.teamPrestige) / qbImportance);
        cost = getInitialCost();
        cost = (int) (cost / qbImportance);

        cost = getLocationCost();
        if (cost < 0) cost = (int) Math.random() * 5 + 1;

        resetSeasonStats();
        resetCareerStats();

    }

    public PlayerQB(String nm, int yr, int stars, Team t, Boolean custom) {
        position = "QB";
        height = hAvg + (int) (Math.random() * ((hMax - hMin) + 1)) + hMin;
        weight = wAvg + (int) (Math.random() * ((wMax - wMin) + 1)) + wMin;
        name = nm;
        year = yr;
        team = t;

        ratPot = (int) (attrBase + 50 * Math.random());
        ratFootIQ = (int) (attrBase + 50 * Math.random());
        ratDur = (int) (attrBase + 50 * Math.random());
        ratPassPow = (int) (ratBase + stars * customFactor - ratTolerance * Math.random());
        ratPassAcc = (int) (ratBase + stars * customFactor - ratTolerance * Math.random());
        ratEvasion = (int) (ratBase + stars * customFactor - ratTolerance * Math.random());
        ratSpeed = (int) ((ratBase - 5) + stars * customFactor - ratTolerance * Math.random());
        ratOvr = getOverall();
        region = (int) (Math.random() * 5);
        personality = (int) (attrBase + 50 * Math.random());

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
        ratPassPow += (int) (Math.random() * games);
        ratPassAcc += (int) (Math.random() * games);
        ratEvasion += (int) (Math.random() * games);
        ratSpeed += (int) (Math.random() * games);

        ratOvr = getOverall();
        ratImprovement = ratOvr - ratOvrStart;
    }

    @Override
    public void advanceSeason() {
        double games = getGamesBonus();

        if (!isMedicalRS) {
            year++;
            if (wonAllConference) ratPot += (int) Math.random() * allConfPotBonus;
            if (wonAllAmerican) ratPot += (int) Math.random() * allAmericanBonus;
            if (wonAllFreshman) ratPot += (int) Math.random() * allFreshmanBonus;
            if (wonTopFreshman) ratPot += (int) Math.random() * topBonus;
            if (wonHeisman) ratPot += (int) Math.random() * topBonus;

            progression = getProgressionOff();

            if (year > 2 && games < minGamesPot) ratPot -= (int) (Math.random() * 15);

            ratFootIQ += (int) (Math.random() * (progression + games - endseason)) / endseasonFactor;
            ratPassPow += (int) (Math.random() * (progression + games - endseason)) / endseasonFactor;
            ratPassAcc += (int) (Math.random() * (progression + games - endseason)) / endseasonFactor;
            ratEvasion += (int) (Math.random() * (progression + games - endseason)) / endseasonFactor;
            ratSpeed += ((int) (Math.random() * (progression + games - endseason)) / endseasonFactor) / 1.5;

            if (Math.random() * 100 < progression) {
                //breakthrough
                ratPassPow += (int) (Math.random() * (progression + games - endseasonBonus)) / endseasonFactor;
                ratPassAcc += (int) (Math.random() * (progression + games - endseasonBonus)) / endseasonFactor;
                ratEvasion += (int) (Math.random() * (progression + games - endseasonBonus)) / endseasonFactor;
                ratSpeed += ((int) (Math.random() * (progression + games - endseasonBonus)) / endseasonFactor) / 1.5;
            }

            durabilityProgression();

        }

        ratOvr = getOverall();
        ratImprovement = ratOvr - ratOvrStart;

        careerPassAtt += statsPassAtt;
        careerPassComp += statsPassComp;
        careerTDs += statsPassTD;
        careerInt += statsInt;
        careerPassYards += statsPassYards;
        careerSacked += statsSacked;
        careerRushAtt += statsRushAtt;
        careerRushYards += statsRushYards;
        careerRushTD += statsRushTD;
        careerFumbles += statsFumbles;
        careerGames += gamesPlayed;
        careerWins += statsWins;

        if (wonHeisman) careerHeismans++;
        if (wonAllAmerican) careerAllAmerican++;
        if (wonAllConference) careerAllConference++;
        if (wonAllFreshman) careerAllFreshman++;
        if (wonTopFreshman) careerTopFreshman++;

        if (isTransfer) {
            isTransfer = false;
            year -= 1;
        }

        if (isRedshirt) wasRedshirt = true;
    }

    private void resetSeasonStats() {
        troubledTimes = 0;
        gamesStarted = 0;
        gamesPlayed = 0;
        statsPassAtt = 0;
        statsPassComp = 0;
        statsPassTD = 0;
        statsInt = 0;
        statsPassYards = 0;
        statsSacked = 0;
        statsRushAtt = 0;
        statsRushYards = 0;
        statsRushTD = 0;
        statsFumbles = 0;
        statsWins = 0;

        wonHeisman = false;
        wonAllAmerican = false;
        wonAllConference = false;
        wonAllFreshman = false;
        wonTopFreshman = false;
        isInjured = false;

    }

    private void resetCareerStats() {
        careerPassAtt = 0;
        careerPassComp = 0;
        careerTDs = 0;
        careerInt = 0;
        careerPassYards = 0;
        careerSacked = 0;
        careerRushAtt = 0;
        careerRushYards = 0;
        careerRushTD = 0;
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
        return statsPassTD * 150 - statsInt * 215 + statsPassYards + statsRushTD * 140 + 3 * statsRushYards + ratOvr * 10 + getConfPrestigeBonus();
    }


    //Career score for HoF :: target - 35000?
    @Override
    public int getCareerScore() {
        return careerTDs * 150 - careerInt * 215 + careerPassYards + careerRushTD * 150 + 3 * careerRushYards + ratOvr * 10 * year +
                statsPassTD * 150 - statsInt * 215 + statsPassYards + statsRushTD * 150 + 3 * statsRushYards + ratOvr * 10;
    }

    public double getPasserRating() {
        if (statsPassAtt < 1) {
            return 0;
        } else {
            double rating = (((8.4 * statsPassYards) + (300 * statsPassTD) + (100 * statsPassComp) - (200 * statsInt)) / statsPassAtt);
            return rating;
        }
    }

    public double getPassPCT() {
        if (statsPassAtt < 1) {
            return 0;
        } else {
            double rating = 100 * statsPassComp / (statsPassAtt);
            return rating;
        }
    }

    public double getCareerPassPCT() {
        if (careerPassAtt + statsPassAtt < 1) {
            return 0;
        } else {
            double rating = (100 * (statsPassComp + careerPassComp) / (statsPassAtt + careerPassAtt));
            return rating;
        }
    }

    public double getCareerPasserRating() {
        if (statsPassAtt + careerPassAtt < 1) {
            return 0;
        } else {
            double rating = (((8.4 * (statsPassYards + careerPassYards)) + (300 * (statsPassTD + careerTDs)) + (100 * (statsPassComp + careerPassComp)) - (200 * (statsInt + careerInt))) / (statsPassAtt + careerPassAtt));
            return rating;
        }
    }


    @Override
    public ArrayList<String> getDetailStatsList(int games) {
        ArrayList<String> pStats = new ArrayList<>();
        pStats.add("Height " + getHeight() + ">Weight: " + getWeight());
        pStats.add("Passer Rating " + df2.format(getPasserRating()) + ">Comp Percent: " + df2.format(getPassPCT()) + "%");
        pStats.add("Touchdowns: " + statsPassTD + ">Interceptions: " + statsInt);
        pStats.add("Pass Yards: " + statsPassYards + " yds>Yards/Att: " + df2.format(((double) (10 * statsPassYards / (statsPassAtt + 1)) / 10)) + " yds");
        pStats.add("Yds/Game: " + (statsPassYards / getGames()) + " yds/g>Sacks: " + statsSacked);
        pStats.add("Rush Yards: " + (statsRushYards) + ">Rush TDs: " + statsRushTD);
        pStats.add("Fumbles: " + statsFumbles + "> Games: " + gamesPlayed + " (" + statsWins + "-" + (gamesStarted - statsWins) + ")");
        pStats.add("Pass Strength: " + getLetterGrade(ratPassPow) + ">Accuracy: " + getLetterGrade(ratPassAcc));
        pStats.add("Speed: " + getLetterGrade(ratSpeed) + ">Evasion: " + getLetterGrade(ratEvasion));
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
        pStats.add("Passer Rating " + df2.format(getPasserRating()) + ">Comp Percent: " + df2.format(getPassPCT()) + "%");
        pStats.add("Touchdowns: " + statsPassTD + ">Interceptions: " + statsInt);
        pStats.add("Pass Yards: " + statsPassYards + " yds>Yards/Att: " + df2.format(((double) (10 * statsPassYards / (statsPassAtt + 1)) / 10)) + " yds");
        pStats.add("Yds/Game: " + (statsPassYards / getGames()) + " yds/g>Sacks: " + statsSacked);
        pStats.add("Rush Yards: " + (statsRushYards) + ">Rush TDs: " + statsRushTD);
        pStats.add("Fumbles: " + statsFumbles + "> Games: " + gamesPlayed + " (" + statsWins + "-" + (gamesStarted - statsWins) + ")");
        pStats.add("Pass Strength: " + getLetterGrade(ratPassPow) + ">Accuracy: " + getLetterGrade(ratPassAcc));
        pStats.add("Speed: " + getLetterGrade(ratSpeed) + ">Evasion: " + getLetterGrade(ratEvasion));
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
        pStats.add("Passer Rating " + df2.format(getCareerPasserRating()) + ">Comp Percent: " + df2.format(getCareerPassPCT()) + "%");
        pStats.add("Touchdowns: " + (statsPassTD + careerTDs) + ">Interceptions: " + (statsInt + careerInt));
        pStats.add("Pass Yards: " + (statsPassYards + careerPassYards) + " yds>Yards/Att: " + df2.format(((double) (10 * (statsPassYards + careerPassYards) / (statsPassAtt + careerPassAtt + 1)) / 10)) + " yds");
        pStats.add("Yds/Game: " + ((statsPassYards + careerPassYards) / (getGames() + careerGames)) + " yds/g>Sacks: " + (statsSacked + careerSacked));
        pStats.add("Rush Yards: " + (statsRushYards + careerRushYards) + ">Rush TDs: " + (statsRushTD + careerRushTD));
        pStats.addAll(super.getCareerStatsList());
        return pStats;
    }

    @Override
    public String getInfoForLineup() {
        if (injury != null)
            return getInitialName() + " [" + getYrStr() + "] " + ratOvr + "/" + getPotRating(ratPot, ratOvr, year, team.HC.get(0).ratTalent) + " " + injury.toString();
        return getInitialName() + " [" + getYrStr() + "] " + ratOvr + "/" + getPotRating(ratPot, ratOvr, year, team.HC.get(0).ratTalent) + " (" +
                getLetterGrade(ratPassPow) + ", " + getLetterGrade(ratPassAcc) + ", " + getLetterGrade(ratEvasion) + ", " + getLetterGrade(ratSpeed) + ")";
    }

    public int getOverall() {
        int ovr;
        ovr = (ratPassPow * 3 + ratPassAcc * 4 + ratEvasion + ratSpeed) / 9;
        return ovr;
    }

}
