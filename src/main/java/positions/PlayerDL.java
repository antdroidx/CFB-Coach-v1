package positions;

import java.util.ArrayList;

import simulation.Team;

/**
 * Created by ahngu on 10/9/2017.
 * <p>
 * I imagine the DL will be a 1:1 swap of the F7 category
 */

public class PlayerDL extends Player {

    //OLPow affects how strong he is against OL
    public int ratStrength;
    //OLBkR affects how well he defends running plays
    public int ratRunStop;
    //OLBkP affects how well he defends passing plays
    public int ratPassRush;
    public int ratTackle;

    //Stats
    public int statsTackles;
    public int statsSacks;
    public int statsFumbles;
    public int statsInts;

    public int careerTackles;
    public int careerSacks;
    public int careerFumbles;
    public int careerInts;

    //Size Config
    private final int hAvg = 75;
    private final int hMax = 5;
    private final int hMin = -5;
    private final int wAvg = 290;
    private final int wMax = 60;
    private final int wMin = -50;

    public PlayerDL(Team t, String nm, int yr, int reg, int trait, int iq, int scout, boolean transfer, boolean wasRS, int pot, int dur, boolean rs, int pow, int rsh, int pas, int tkl, int h, int w) {
        team = t;
        name = nm;
        year = yr;
        gamesStarted = 0;
        gamesPlayed = 0;
        isInjured = false;
        ratPot = pot;
        ratFootIQ = iq;
        ratDur = dur;
        ratStrength = pow;
        ratRunStop = rsh;
        ratPassRush = pas;
        ratTackle = tkl;
        isRedshirt = rs;
        wasRedshirt = wasRS;

        position = "DL";
        homeState = reg;
        personality = trait;
        recruitRating = scout;
        height = h;
        weight = w;
        ratOvr = getOverall();

        troubledTimes = 0;

        wonHeisman = false;
        wonAllAmerican = false;
        wonAllConference = false;
        wonAllFreshman = false;
        wonTopFreshman = false;

        statsWins = 0;

        careerGames = 0;
        careerHeismans = 0;
        careerAllAmerican = 0;
        careerAllConference = 0;
        careerAllFreshman = 0;
        careerTopFreshman = 0;
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


    public PlayerDL(Team t, String nm, int yr, int reg, int trait, int iq, int scout, boolean transfer, boolean wasRS, boolean wo, int pot, int dur, boolean rs, int cGamesPlayed, int cWins, int cHeismans, int cAA, int cAC, int cTF, int cAF,
                    int pow, int rsh, int pas, int tkl, int h, int w, int cTackles, int cSacks, int cFumbles, int cInts) {
        position = "DL";
        team = t;
        name = nm;
        year = yr;

        ratPot = pot;
        ratFootIQ = iq;
        ratDur = dur;
        ratStrength = pow;
        ratRunStop = rsh;
        ratPassRush = pas;
        ratTackle = tkl;
        isRedshirt = rs;
        wasRedshirt = wasRS;

        isTransfer = transfer;
        isWalkOn = wo;
        homeState = reg;
        personality = trait;
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

        careerTackles = cTackles;
        careerSacks = cSacks;
        careerFumbles = cFumbles;
        careerInts = cInts;
    }

    public PlayerDL(String nm, int yr, int stars, Team t) {
        position = "DL";
        height = hAvg + (int) (Math.random() * ((hMax - hMin) + 1)) + hMin;
        weight = wAvg + (int) (Math.random() * ((wMax - wMin) + 1)) + wMin;
        name = nm;
        year = yr;
        team = t;

        wasRedshirt = getWasRedshirtStatus();

        createGenericAttributes();

        ratStrength = (int) (ratBase + year * yearFactor + stars * starFactor - ratTolerance * Math.random());
        ratRunStop = (int) (ratBase + year * yearFactor + stars * starFactor - ratTolerance * Math.random());
        ratPassRush = (int) (ratBase + year * yearFactor + stars * starFactor - ratTolerance * Math.random());
        ratTackle = (int) (ratBase + year * yearFactor + stars * starFactor - ratTolerance * Math.random());
        ratOvr = getOverall();

        recruitRating = getScoutingGrade();

        recruitTolerance = (int) ((60 - team.teamPrestige) / dlImportance);
        cost = getInitialCost();
        cost = (int) (cost / dlImportance);
        cost = getLocationCost();
        if (cost < 0) cost = (int) Math.random() * 5 + 1;

        resetSeasonStats();
        resetCareerStats();

    }

    public PlayerDL(String nm, int yr, int stars, Team t, boolean custom) {
        position = "DL";
        height = hAvg + (int) (Math.random() * ((hMax - hMin) + 1)) + hMin;
        weight = wAvg + (int) (Math.random() * ((wMax - wMin) + 1)) + wMin;
        name = nm;
        year = yr;
        team = t;

        wasRedshirt = getWasRedshirtStatus();

        createGenericAttributes();

        ratStrength = (int) (ratBase + stars * customFactor - ratTolerance * Math.random());
        ratRunStop = (int) (ratBase + stars * customFactor - ratTolerance * Math.random());
        ratPassRush = (int) (ratBase + stars * customFactor - ratTolerance * Math.random());
        ratTackle = (int) (ratBase + stars * customFactor - ratTolerance * Math.random());
        ratOvr = getOverall();

        if (custom) isWalkOn = true;
        recruitRating = getScoutingGrade();

        resetSeasonStats();
        resetCareerStats();
    }


    public void midSeasonProgression() {
        final int ratOvrStart = ratOvr;
        progression = getProgressionDef();
        double games = getMidSeasonBonus();

        ratFootIQ += (int) (Math.random() * games);
        ratStrength += (int) (Math.random() * games);
        ratRunStop += (int) (Math.random() * games);
        ratPassRush += (int) (Math.random() * games);
        ratTackle += (int) (Math.random() * games);

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
            progression = getProgressionDef();

            if (year > 2 && games < minGamesPot) ratPot -= (int) (Math.random() * 15);

            ratFootIQ += (int) (Math.random() * (progression + games - endseason)) / endseasonFactor;
            ratStrength += (int) (Math.random() * (progression + games - endseason)) / endseasonFactor;
            ratRunStop += (int) (Math.random() * (progression + games - endseason)) / endseasonFactor;
            ratPassRush += (int) (Math.random() * (progression + games - endseason)) / endseasonFactor;
            ratTackle += (int) (Math.random() * (progression + games - endseason)) / endseasonFactor;
            if (Math.random() * 100 < progression) {
                //breakthrough
                ratStrength += (int) (Math.random() * (progression + games - endseasonBonus)) / endseasonFactor;
                ratRunStop += (int) (Math.random() * (progression + games - endseasonBonus)) / endseasonFactor;
                ratPassRush += (int) (Math.random() * (progression + games - endseasonBonus)) / endseasonFactor;
                ratTackle += (int) (Math.random() * (progression + games - endseasonBonus)) / endseasonFactor;
            }

            durabilityProgression();

        }

        ratOvr = getOverall();
        ratImprovement = ratOvr - ratOvrStart;

        careerGames += gamesPlayed;
        careerWins += statsWins;

        careerTackles += statsTackles;
        careerSacks += statsSacks;
        careerFumbles += statsFumbles;
        careerInts += statsInts;

        addSeasonAwards();
        checkRedshirt();
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
        statsTackles = 0;
        statsSacks = 0;
        statsFumbles = 0;
        statsInts = 0;
    }

    private void resetCareerStats() {
        careerGames = 0;
        careerHeismans = 0;
        careerAllAmerican = 0;
        careerAllConference = 0;
        careerAllFreshman = 0;
        careerTopFreshman = 0;
        careerWins = 0;

        careerTackles = 0;
        careerSacks = 0;
        careerFumbles = 0;
        careerInts = 0;
    }


    @Override
    public int getHeismanScore() {
        return statsTackles * 25 + statsSacks * 425 + statsFumbles * 425 + statsInts * 425 + ratOvr * 10 + getConfPrestigeBonus();
    }

    @Override
    public int getCareerScore() {
        return statsTackles * 25 + statsSacks * 425 + statsFumbles * 425 + statsInts * 425 + ratOvr * 10 + team.teamPrestige * 4 +
                careerTackles * 25 + careerSacks * 425 + careerFumbles * 425 + careerInts * 425 + ratOvr * year * 10;
    }

    @Override
    public ArrayList<String> getDetailAllStatsList() {
        ArrayList<String> pStats = stringPlayerInfo();
        pStats.add(" > ");
        pStats.add("[B]PLAYER RATINGS");
        ArrayList<String> attributes = stringPlayerAttributes();
        for(String a : attributes) {
            pStats.add(a);
        }
        pStats.add("Strength: " + getLetterGrade(ratStrength) + ">Run Stop: " + getLetterGrade(ratRunStop));
        pStats.add("Tackling: " + getLetterGrade(ratTackle) + ">Pass Rush: " + getLetterGrade(ratPassRush));
        pStats.add(" > ");
        pStats.add("[B]SEASON STATS");


        pStats.add("Tackles: " + (statsTackles) + " >Sacks: " + (statsSacks));
        pStats.add("Fumbles: " + (statsFumbles) + " >Interceptions: " + (statsInts));
        pStats.add("Games: " + gamesPlayed + " (" + statsWins + "-" + (gamesStarted - statsWins) + ")" + "> ");

        pStats.add(" > ");
        pStats.add("[B]CAREER STATS");
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
            return getInitialName() + " [" + getYrStr() + "] " + ratOvr + "/" + getPotRating(team.HC.get(0).ratTalent) + " " + injury.toString();
        return getInitialName() + " [" + getYrStr() + "] " + ratOvr + "/" + getPotRating(team.HC.get(0).ratTalent) + " (" +
                getLetterGrade(ratStrength) + ", " + getLetterGrade(ratRunStop) + ", " + getLetterGrade(ratPassRush) + ", " + getLetterGrade(ratTackle) + ")";
    }

    public int getOverall() {
        int ovr;
        ovr = (ratStrength * 3 + ratRunStop + ratPassRush + ratTackle) / 6;
        return ovr;
    }

}
