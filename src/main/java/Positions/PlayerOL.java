package Positions;

import java.util.ArrayList;

import Simulation.Team;

/**
 * Class for the OL player. 5 on field at a time.
 *
 * @author Achi
 */
public class PlayerOL extends Player {

    //OLPow affects how strong he is against defending DL
    public int ratStrength;
    //OLBkR affects how well he blocks for running plays
    public int ratRunBlock;
    //OLBkP affects how well he blocks for passing plays
    public int ratPassBlock;
    public int ratAwareness;

    public int statsSacksAllowed;
    public int statsRushYards;
    public int statsPassYards;

    //Size Config
    private final int hAvg = 76;
    private final int hMax = 5;
    private final int hMin = -3;
    private final int wAvg = 310;
    private final int wMax = 40;
    private final int wMin = -35;

    public PlayerOL(Team t, String nm, int yr, int reg, int trait, int iq, int scout, boolean transfer, boolean wasRS, int pot, int dur, boolean rs, int pow, int bkr, int bkp, int awr, int h, int w) {
        position = "OL";
        team = t;
        name = nm;
        year = yr;
        ratPot = pot;
        ratFootIQ = iq;
        ratDur = dur;
        ratStrength = pow;
        ratRunBlock = bkr;
        ratPassBlock = bkp;
        ratAwareness = awr;
        isRedshirt = rs;
        if (isRedshirt) year = 0;
        wasRedshirt = wasRS;
        height = h;
        weight = w;
        ratOvr = getOverall();

        region = reg;
        personality = trait;
        recruitRating = scout;

        resetSeasonStats();
        resetCareerStats();
    }


    public PlayerOL(Team t, String nm, int yr, int reg, int trait, int iq, int scout, boolean transfer, boolean wasRS, boolean wo, int pot, int dur, boolean rs,int cGamesPlayed, int cWins, int cHeismans, int cAA, int cAC, int cTF, int cAF,
                    int pow, int bkr, int bkp, int awr, int h, int w) {
        position = "OL";
        team = t;
        name = nm;
        year = yr;
        ratPot = pot;
        ratFootIQ = iq;
        ratDur = dur;
        ratStrength = pow;
        ratRunBlock = bkr;
        ratPassBlock = bkp;
        ratAwareness = awr;
        isRedshirt = rs;
        if (isRedshirt) year = 0;
        wasRedshirt = wasRS;

        isTransfer = transfer;
        isWalkOn = wo;
        region = reg;
        personality = trait;
        troubledTimes = 0;
        recruitRating = scout;
        height = h;
        weight = w;
        ratOvr = getOverall();

        resetSeasonStats();

        careerGames = cGamesPlayed;
        careerHeismans = cHeismans;
        careerAllAmerican = cAA;
        careerAllConference = cAC;
        careerTopFreshman = cTF;
        careerAllFreshman = cAF;
        careerWins = cWins;
    }

    public PlayerOL(String nm, int yr, int stars, Team t) {
        position = "OL";
        height = hAvg + 	(int)(Math.random() * ((hMax - hMin) + 1)) + hMin;
        weight = wAvg + 	(int)(Math.random() * ((wMax - wMin) + 1)) + wMin;
        name = nm;
        year = yr;
        team = t;

        ratPot = (int) (attrBase + 50 * Math.random());
        ratFootIQ = (int) (attrBase + 50 * Math.random());
        ratDur = (int) (attrBase + 50 * Math.random());
        ratStrength = (int) (ratBase + year*yearFactor + stars*starFactor - ratTolerance*Math.random());
        ratRunBlock = (int) (ratBase + year*yearFactor + stars*starFactor - ratTolerance*Math.random());
        ratPassBlock = (int) (ratBase + year*yearFactor + stars*starFactor - ratTolerance*Math.random());
        ratAwareness = (int) (ratBase + year*yearFactor + stars*starFactor - ratTolerance*Math.random());
        ratOvr = getOverall();
        region = (int)(Math.random()*5);
        personality = (int) (attrBase + 50 * Math.random());

        recruitRating = getScoutingGrade();

        recruitTolerance = (int)((60 - team.teamPrestige)/olImportance);
        cost = (int)((Math.pow((float) ratOvr - costBaseRating, 2)/5) + (int)Math.random()*recruitTolerance);

        cost = (int)(cost/olImportance);

        double locFactor = Math.abs(team.location - region) - 2.5;
        cost = cost + (int)(Math.random()*(locFactor * locationDiscount));
        if (cost < 0) cost = (int)Math.random()*7+1;

        resetSeasonStats();
        resetCareerStats();

    }

    public PlayerOL(String nm, int yr, int stars, Team t, boolean custom) {
        position = "OL";
        height = hAvg + 	(int)(Math.random() * ((hMax - hMin) + 1)) + hMin;
        weight = wAvg + 	(int)(Math.random() * ((wMax - wMin) + 1)) + wMin;
        name = nm;
        year = yr;
        team = t;

        ratPot = (int) (attrBase + 50 * Math.random());
        ratFootIQ = (int) (attrBase + 50 * Math.random());
        ratDur = (int) (attrBase + 50 * Math.random());
        ratStrength = (int) (ratBase + stars * customFactor - ratTolerance * Math.random());
        ratRunBlock = (int) (ratBase + stars * customFactor - ratTolerance * Math.random());
        ratPassBlock = (int) (ratBase + stars * customFactor - ratTolerance * Math.random());
        ratAwareness = (int) (ratBase + stars * customFactor - ratTolerance * Math.random());
        ratOvr = getOverall();
        region = (int)(Math.random()*5);
        personality = (int) (attrBase + 50 * Math.random());

        if(custom) isWalkOn = true;
        recruitRating = getScoutingGrade();

        resetSeasonStats();
        resetCareerStats();
    }

    public void midSeasonProgression() {
        final int ratOvrStart = ratOvr;
        progression = getProgressionOff();
        double games = getMidSeasonBonus();

        ratFootIQ += (int) (Math.random() * games);
        ratStrength += (int) (Math.random() * games);
        ratRunBlock += (int) (Math.random() * games);
        ratPassBlock += (int) (Math.random() * games);
        ratAwareness += (int) (Math.random() * games);

        ratOvr = getOverall();
        ratImprovement = ratOvr - ratOvrStart;
    }


    @Override
    public void advanceSeason() {
        double games = getGamesBonus();

        if (!isMedicalRS) {
            year++;
            if (wonAllConference) ratPot += (int)Math.random()*allConfPotBonus;
            if (wonAllAmerican) ratPot += (int)Math.random()*allAmericanBonus;
            if (wonAllFreshman) ratPot += (int)Math.random()*allFreshmanBonus;
            if (wonTopFreshman) ratPot += (int)Math.random()*topBonus;
            if (wonHeisman) ratPot += (int)Math.random()*topBonus;
            progression = getProgressionOff();

            if (year > 2 && games < minGamesPot) ratPot -= (int) (Math.random() * 15);

            ratFootIQ += (int) (Math.random() * (progression + games - endseason)) / endseasonFactor;
            ratStrength += (int) (Math.random() * (progression + games - endseason)) / endseasonFactor;
            ratRunBlock += (int) (Math.random() * (progression + games - endseason)) / endseasonFactor;
            ratPassBlock += (int) (Math.random() * (progression + games - endseason)) / endseasonFactor;
            ratAwareness += (int) (Math.random() * (progression + games - endseason)) / endseasonFactor;
            if (Math.random() * 100 < progression) {
                //breakthrough
                ratStrength += (int) (Math.random() * (progression + games - endseasonBonus)) / endseasonFactor;
                ratRunBlock += (int) (Math.random() * (progression + games - endseasonBonus)) / endseasonFactor;
                ratPassBlock += (int) (Math.random() * (progression + games - endseasonBonus)) / endseasonFactor;
                ratAwareness += (int) (Math.random() * (progression + games - endseasonBonus)) / endseasonFactor;
            }
        }

        ratOvr = getOverall();
        ratImprovement = ratOvr - ratOvrStart;

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
        gamesStarted = 0;
        gamesPlayed = 0;
        isInjured = false;
        troubledTimes = 0;

        wonHeisman = false;
        wonAllAmerican = false;
        wonAllConference = false;
        wonAllFreshman = false;
        wonTopFreshman = false;
        statsWins = 0;
    }

    private void resetCareerStats() {
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
        return ratOvr*50 + getConfPrestigeBonus();
    }

    @Override
    public int getCareerScore() {
        return ratOvr*(year)*50;
    }

    @Override
    public ArrayList<String> getDetailStatsList(int games) {
        ArrayList<String> pStats = new ArrayList<>();
        pStats.add("Height " + getHeight() + ">Weight: " + getWeight());
        pStats.add("Games: " + gamesPlayed + " (" + statsWins + "-" + (gamesStarted - statsWins) + ")" + "> ");
        pStats.add("Strength: " + getLetterGrade(ratStrength) + ">Run Block: " + getLetterGrade(ratRunBlock));
        pStats.add("Awareness: " + getLetterGrade(ratAwareness) + ">Pass Block: " + getLetterGrade(ratPassBlock));
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
        pStats.add("Games: " + gamesPlayed + " (" + statsWins + "-" + (gamesStarted - statsWins) + ")" + "> ");
        pStats.add("Strength: " + getLetterGrade(ratStrength) + ">Run Block: " + getLetterGrade(ratRunBlock));
        pStats.add("Awareness: " + getLetterGrade(ratAwareness) + ">Pass Block: " + getLetterGrade(ratPassBlock));
        pStats.add("Durability: " + getLetterGrade(ratDur) + ">Football IQ: " + getLetterGrade(ratFootIQ));
        pStats.add("Home Region: " + getRegion(region) + ">Personality: " + getLetterGrade(personality));
        pStats.add("Scout Grade: " + getScoutingGradeString() + " > " + getStatus());
        pStats.add(" > ");
        pStats.add("[B]CAREER STATS:");
        pStats.addAll(getCareerStatsList());
        return pStats;
    }

    @Override
    public String getInfoForLineup() {
        if (injury != null)
            return getInitialName() + " [" + getYrStr() + "] " + ratOvr + "/" + getPotRating(ratPot, ratOvr, year, team.HC.get(0).ratTalent) + " " + injury.toString();
        return getInitialName() + " [" + getYrStr() + "] " + ratOvr + "/" + getPotRating(ratPot, ratOvr, year, team.HC.get(0).ratTalent) + " (" +
                getLetterGrade(ratStrength) + ", " + getLetterGrade(ratRunBlock) + ", " + getLetterGrade(ratPassBlock) + ", " + getLetterGrade(ratAwareness) + ")";
    }

    public int getOverall() {
        int ovr;
        ovr = (ratStrength * 3 + ratRunBlock * 2 + ratPassBlock * 2 + ratAwareness) / 8;
        return ovr;
    }

}
