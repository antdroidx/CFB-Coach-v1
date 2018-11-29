package positions;

import java.util.ArrayList;

import simulation.Team;

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

    public int statsKickRets;
    public int statsKickRetYards;
    public int statsKickRetTDs;
    public int statsPuntRets;
    public int statsPuntRetYards;
    public int statsPuntRetTDs;
    public int statsRetGames;

    public int careerKickRets;
    public int careerKickRetYards;
    public int careerKickRetTDs;
    public int careerPuntRets;
    public int careerPuntRetYards;
    public int careerPuntRetTDs;
    private int careerRetGames;

    //Size Config
    private final int hAvg = 71;
    private final int hMax = 4;
    private final int hMin = -4;
    private final int wAvg = 193;
    private final int wMax = 30;
    private final int wMin = -40;

    public PlayerCB(Team t, String nm, int yr, int reg, int trait, int iq, int scout, boolean transfer, boolean wasRS, int pot, int dur, boolean rs, int cov, int spd, int tkl, int jmp, int h, int w) {
        position = "CB";
        team = t;
        name = nm;
        year = yr;
        gamesStarted = 0;
        gamesPlayed = 0;
        isInjured = false;
        ratPot = pot;
        ratFootIQ = iq;
        ratDur = dur;
        ratCoverage = cov;
        ratSpeed = spd;
        ratTackle = tkl;
        ratJump = jmp;
        isRedshirt = rs;
        wasRedshirt = wasRS;

        homeState = reg;
        personality = trait;
        recruitRating = scout;
        height = h;
        weight = w;
        ratOvr = getOverall();

        resetSeasonStats();
        resetCareerStats();
    }

    public PlayerCB(Team t, String nm, int yr, int reg, int trait, int iq, int scout, boolean transfer, boolean wasRS, boolean wo, int pot, int dur, boolean rs, int cGamesPlayed, int cWins, int cHeismans, int cAA, int cAC, int cTF, int cAF,
                    int cov, int spd, int tkl, int jmp, int h, int w, int cTackles, int cSacks, int cFumbles, int cInts, int cTar, int cInc, int cDef, int kret, int kyds, int ktd, int pret, int pyds, int ptd) {
        position = "CB";
        team = t;
        name = nm;
        year = yr;

        ratPot = pot;
        ratFootIQ = iq;
        ratDur = dur;
        ratCoverage = cov;
        ratSpeed = spd;
        ratTackle = tkl;
        ratJump = jmp;
        isRedshirt = rs;
        wasRedshirt = wasRS;

        isTransfer = transfer;
        isWalkOn = wo;
        homeState = reg;
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

        careerTackles = cTackles;
        careerSacks = cSacks;
        careerFumbles = cFumbles;
        careerInts = cInts;
        careerTargets = cTar;
        careerIncomplete = cInc;
        careerDefended = cDef;

        careerKickRets = kret;
        careerKickRetYards = kyds;
        careerKickRetTDs = ktd;
        careerPuntRets = pret;
        careerPuntRetYards = pyds;
        careerPuntRetTDs = ptd;
    }

    public PlayerCB(String nm, int yr, int stars, Team t) {
        position = "CB";
        height = hAvg + (int) (Math.random() * ((hMax - hMin) + 1)) + hMin;
        weight = wAvg + (int) (Math.random() * ((wMax - wMin) + 1)) + wMin;
        name = nm;
        year = yr;
        team = t;

        wasRedshirt = getWasRedshirtStatus();

        createGenericAttributes();

        ratCoverage = (int) (ratBase + year * yearFactor + stars * starFactor - ratTolerance * Math.random());
        ratSpeed = (int) (ratBase + year * yearFactor + stars * starFactor - ratTolerance * Math.random());
        ratTackle = (int) ((ratBase - 10) + year * yearFactor + stars * starFactor - ratTolerance * Math.random());
        ratJump = (int) (ratBase + year * yearFactor + stars * starFactor - ratTolerance * Math.random());
        ratOvr = getOverall();

        recruitRating = getScoutingGrade();

        recruitTolerance = (int) ((60 - team.teamPrestige) / cbImportance);
        cost = getInitialCost();
        cost = (int) (cost / cbImportance);
        cost = getLocationCost();
        if (cost < 0) cost = (int) Math.random() * 5 + 1;

        resetSeasonStats();
        resetCareerStats();
    }

    public PlayerCB(String nm, int yr, int stars, Team t, boolean custom) {
        position = "CB";
        height = hAvg + (int) (Math.random() * ((hMax - hMin) + 1)) + hMin;
        weight = wAvg + (int) (Math.random() * ((wMax - wMin) + 1)) + wMin;
        name = nm;
        year = yr;
        team = t;

        wasRedshirt = getWasRedshirtStatus();

        createGenericAttributes();

        ratCoverage = (int) (ratBase + stars * customFactor - ratTolerance * Math.random());
        ratSpeed = (int) (ratBase + stars * customFactor - ratTolerance * Math.random());
        ratTackle = (int) (ratBase + stars * customFactor - ratTolerance * Math.random());
        ratJump = (int) (ratBase + stars * customFactor - ratTolerance * Math.random());
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
        ratCoverage += (int) (Math.random() * games);
        ratSpeed += (int) (Math.random() * games);
        ratTackle += (int) (Math.random() * games);
        ratJump += (int) (Math.random() * games);

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
            ratCoverage += (int) (Math.random() * (progression + games - endseason)) / endseasonFactor;
            ratSpeed += (int) (Math.random() * (progression + games - endseason)) / endseasonFactor;
            ratTackle += (int) (Math.random() * (progression + games - endseason)) / endseasonFactor;
            ratJump += (int) (Math.random() * (progression + games - endseason)) / endseasonFactor;
            if (Math.random() * 100 < progression) {
                //breakthrough
                ratCoverage += (int) (Math.random() * (progression + games - endseasonBonus)) / endseasonFactor;
                ratSpeed += (int) (Math.random() * (progression + games - endseasonBonus)) / endseasonFactor;
                ratTackle += (int) (Math.random() * (progression + games - endseasonBonus)) / endseasonFactor;
                ratJump += (int) (Math.random() * (progression + games - endseasonBonus)) / endseasonFactor;
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
        careerTargets += statsTargets;
        careerIncomplete += statsIncomplete;
        careerDefended += statsDefended;

        careerKickRets += statsKickRets;
        careerKickRetYards += statsKickRetYards;
        careerKickRetTDs += statsKickRetTDs;
        careerPuntRets += statsPuntRets;
        careerPuntRetYards += statsPuntRetYards;
        careerPuntRetTDs += statsPuntRetTDs;
        careerRetGames += statsRetGames;

        if (wonHeisman) careerHeismans++;
        if (wonAllAmerican) careerAllAmerican++;
        if (wonAllConference) careerAllConference++;
        if (wonAllFreshman) careerAllFreshman++;
        if (wonTopFreshman) careerTopFreshman++;

        if (isTransfer || isRedshirt || isMedicalRS) {
            isTransfer = false;
            isRedshirt = false;
            isMedicalRS = false;
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
        statsTargets = 0;
        statsIncomplete = 0;
        statsDefended = 0;

        statsKickRets = 0;
        statsKickRetYards = 0;
        statsKickRetTDs = 0;
        statsPuntRets = 0;
        statsPuntRetYards = 0;
        statsPuntRetTDs = 0;
        statsRetGames = 0;
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
        careerTargets = 0;
        careerIncomplete = 0;
        careerDefended = 0;

        careerKickRets = 0;
        careerKickRetYards = 0;
        careerKickRetTDs = 0;
        careerPuntRets = 0;
        careerPuntRetYards = 0;
        careerPuntRetTDs = 0;
        careerRetGames = 0;
    }

    @Override
    public int getHeismanScore() {
        return statsTackles * 25 + statsSacks * 425 + statsFumbles * 425 + statsInts * 425 + statsDefended * 100 + statsKickRetYards + statsKickRetTDs * 150 + statsPuntRetYards + statsPuntRetTDs * 150 + ratOvr * 10 + getConfPrestigeBonus();
    }

    @Override
    public int getCareerScore() {
        return statsTackles * 25 + statsSacks * 425 + statsFumbles * 425 + statsInts * 425 + statsDefended * 100 + ratOvr * 10 +
                careerTackles * 25 + careerSacks * 425 + careerFumbles * 425 + careerInts * 425 + careerDefended * 100
                + statsPuntRetYards + statsPuntRetTDs * 150 + careerKickRetYards + careerKickRetTDs * 150 + careerPuntRetYards + careerPuntRetTDs * 150 + ratOvr * 10 * year;

    }


    @Override
    public ArrayList<String> getDetailStatsList(int games) {
        ArrayList<String> pStats = stringPlayerAttributes();
        pStats.add("Coverage: " + getLetterGrade(ratCoverage) + ">Jumping: " + getLetterGrade(ratJump));
        pStats.add("Tackling: " + getLetterGrade(ratTackle) + ">Speed: " + getLetterGrade(ratSpeed));

        pStats.add("Tackles: " + (statsTackles) + " >Sacks: " + (statsSacks));
        pStats.add("Fumbles: " + (statsFumbles) + " >Interceptions: " + (statsInts));
        pStats.add("Defended: " + statsDefended + ">Shutdown Pct: " + (100 * (statsIncomplete) / (statsTargets + 1)) + "%");
        pStats.add("Games: " + gamesPlayed + " (" + statsWins + "-" + (gamesStarted - statsWins) + ")" + "> ");
        if (statsKickRets > 0) {
            pStats.add("Kick Rets: " + statsKickRets + ">Kick Ret Yards: " + statsKickRetYards + " yrds");
            pStats.add("Kick Ret TDs: " + statsKickRetTDs + ">Ret Avg: " + (double) (statsKickRetYards / statsKickRets));
        }
        if (statsPuntRets > 0) {
            pStats.add("Punt Rets: " + statsPuntRets + ">Punt Ret Yards: " + statsPuntRetYards + " yrds");
            pStats.add("Punt Ret TDs: " + statsPuntRetTDs + ">Ret Avg: " + (double) (statsPuntRetYards / statsPuntRets));
        }

        pStats.add(" > ");
        return pStats;
    }

    @Override
    public ArrayList<String> getDetailAllStatsList(int games) {
        ArrayList<String> pStats = stringPlayerAttributes();
        pStats.add("Coverage: " + getLetterGrade(ratCoverage) + ">Jumping: " + getLetterGrade(ratJump));
        pStats.add("Tackling: " + getLetterGrade(ratTackle) + ">Speed: " + getLetterGrade(ratSpeed));

        pStats.add("Tackles: " + (statsTackles) + " >Sacks: " + (statsSacks));
        pStats.add("Fumbles: " + (statsFumbles) + " >Interceptions: " + (statsInts));
        pStats.add("Defended: " + statsDefended + ">Shutdown Pct: " + (100 * (statsIncomplete) / (statsTargets + 1)) + "%");
        if (statsKickRets > 0) {
            pStats.add("Kick Rets: " + statsKickRets + ">Kick Ret Yards: " + statsKickRetYards + " yrds");
            pStats.add("Kick Ret TDs: " + statsKickRetTDs + ">Ret Avg: " + (double) (statsKickRetYards / statsKickRets));
        }
        if (statsPuntRets > 0) {
            pStats.add("Punt Rets: " + statsPuntRets + ">Punt Ret Yards: " + statsPuntRetYards + " yrds");
            pStats.add("Punt Ret TDs: " + statsPuntRetTDs + ">Ret Avg: " + (double) (statsPuntRetYards / statsPuntRets));
        }

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
        if (statsKickRets + careerKickRets > 0) {
            pStats.add("Kick Rets: " + (statsKickRets + careerKickRets) + ">Kick Ret Yards: " + (statsKickRetYards + careerKickRetYards) + " yrds");
            pStats.add("Kick Ret TDs: " + (statsKickRetTDs + careerKickRetTDs) + ">Ret Avg: " + (double) ((statsKickRetYards + careerKickRetYards) / (statsKickRets + careerKickRets)));
        }
        if (statsPuntRets + careerPuntRets > 0) {
            pStats.add("Punt Rets: " + (statsPuntRets + careerPuntRets) + ">Punt Ret Yards: " + (statsPuntRetYards + careerPuntRetYards) + " yrds");
            pStats.add("Punt Ret TDs: " + (statsPuntRetTDs + careerPuntRetTDs) + ">Ret Avg: " + (double) ((statsPuntRetYards + careerPuntRetYards) / (statsPuntRets + careerPuntRets)));
        }
        pStats.addAll(super.getCareerStatsList());
        return pStats;
    }


    @Override
    public String getInfoForLineup() {
        if (injury != null)
            return getInitialName() + " [" + getYrStr() + "] " + ratOvr + "/" + getPotRating(ratPot, ratOvr, year, team.HC.get(0).ratTalent) + " " + injury.toString();
        return getInitialName() + " [" + getYrStr() + "] " + ratOvr + "/" + getPotRating(ratPot, ratOvr, year, team.HC.get(0).ratTalent) + " (" +
                getLetterGrade(ratCoverage) + ", " + getLetterGrade(ratSpeed) + ", " + getLetterGrade(ratTackle) + ", " + getLetterGrade(ratJump) + ")";
    }

    public int getOverall() {
        int ovr;
        ovr = (ratCoverage * 2 + ratSpeed + ratTackle + ratJump) / 5;
        return ovr;
    }

}
