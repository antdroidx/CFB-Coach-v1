/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package positions;

import java.util.ArrayList;

import simulation.Team;

public class PlayerWR extends Player {

    //RecCat affects how good he is at catching
    public int ratCatch;
    //RecSpd affects how long his passes are
    public int ratSpeed;
    //RecEva affects how easily he can dodge tackles
    public int ratEvasion;
    public int ratJump;

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
    private final int hAvg = 73;
    private final int hMax = 4;
    private final int hMin = -5;
    private final int wAvg = 206;
    private final int wMax = 30;
    private final int wMin = -40;

    public PlayerWR(Team t, String nm, int yr, int reg, int trait, int iq, int scout, boolean transfer, boolean wasRS, int pot, int dur, boolean rs, int cat, int spd, int eva, int jmp, int h, int w) {
        position = "WR";
        team = t;
        name = nm;
        year = yr;

        ratPot = pot;
        ratFootIQ = iq;
        ratDur = dur;
        ratCatch = cat;
        ratSpeed = spd;
        ratEvasion = eva;
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

    public PlayerWR(Team t, String nm, int yr, int reg, int trait, int iq, int scout, boolean transfer, boolean wasRS, boolean wo, int pot, int dur, boolean rs, int cGamesPlayed, int cWins, int cHeismans, int cAA, int cAC, int cTF, int cAF,
                    int cat, int spd, int eva, int jmp, int h, int w, int cTargets, int cReceptions, int cRecYards, int cTD, int cDrops, int cFumbles, int kret, int kyds, int ktd, int pret, int pyds, int ptd) {
        position = "WR";
        team = t;
        name = nm;
        year = yr;

        ratPot = pot;
        ratFootIQ = iq;
        ratDur = dur;
        ratCatch = cat;
        ratSpeed = spd;
        ratEvasion = eva;
        ratJump = jmp;
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

        careerKickRets = kret;
        careerKickRetYards = kyds;
        careerKickRetTDs = ktd;
        careerPuntRets = pret;
        careerPuntRetYards = pyds;
        careerPuntRetTDs = ptd;
    }

    public PlayerWR(Team t, String nm, int yr, int reg, int trait, int iq, int scout, boolean transfer, boolean wasRS, boolean wo, int pot, int dur, boolean rs, int cGamesPlayed, int cWins, int cHeismans, int cAA, int cAC, int cTF, int cAF,
                    int cat, int spd, int eva, int jmp, int h, int w, int cTargets, int cReceptions, int cRecYards, int cTD, int cDrops, int cFumbles, int kret, int kyds, int ktd, int pret, int pyds, int ptd,
                    int seaTar, int seaRec, int seaRecYards, int seaRecTD, int seaDrops, int seaFum, int seaKRet, int seaKYds, int seaKTD, int seaPRet, int seaPYrd, int seaPTD,
                    int seaGames, int seaGPlayed, int seaWins, boolean inj, boolean medRS, int tt, int ratImp, boolean wonHeis, boolean wonAA, boolean wonAC, boolean wonTF, boolean wonAF) {
        position = "WR";
        team = t;
        name = nm;
        year = yr;

        ratPot = pot;
        ratFootIQ = iq;
        ratDur = dur;
        ratCatch = cat;
        ratSpeed = spd;
        ratEvasion = eva;
        ratJump = jmp;
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

        careerKickRets = kret;
        careerKickRetYards = kyds;
        careerKickRetTDs = ktd;
        careerPuntRets = pret;
        careerPuntRetYards = pyds;
        careerPuntRetTDs = ptd;

        statsTargets = seaTar;
        statsReceptions = seaRec;
        statsRecYards = seaRecYards;
        statsRecTD = seaRecTD;
        statsDrops = seaDrops;
        statsFumbles = seaFum;

        statsKickRets = seaKRet;
        statsKickRetYards = seaKYds;
        statsKickRetTDs = seaKTD;
        statsPuntRets = seaPRet;
        statsPuntRetYards = seaPYrd;
        statsPuntRetTDs = seaPTD;

        gamesStarted = seaGames;
        gamesPlayed = seaGPlayed;
        statsWins = seaWins;

        isInjured = inj;
        isMedicalRS = medRS;
        troubledTimes = tt;
        ratImprovement = ratImp;

        wonHeisman = wonHeis;
        wonAllAmerican = wonAA;
        wonAllConference = wonAC;
        wonTopFreshman = wonTF;
        wonAllFreshman = wonAF;
    }

    public PlayerWR(String nm, int yr, int stars, Team t) {
        position = "WR";
        height = hAvg + (int) (Math.random() * ((hMax - hMin) + 1)) + hMin;
        weight = wAvg + (int) (Math.random() * ((wMax - wMin) + 1)) + wMin;
        name = nm;
        year = yr;
        team = t;

        wasRedshirt = getWasRedshirtStatus();

        createGenericAttributes();

        ratCatch = (int) (ratBase + year * yearFactor + stars * starFactor - ratTolerance * Math.random());
        ratSpeed = (int) (ratBase + year * yearFactor + stars * starFactor - ratTolerance * Math.random());
        ratEvasion = (int) (ratBase + year * yearFactor + stars * starFactor - ratTolerance * Math.random());
        ratJump = (int) (ratBase + year * yearFactor + stars * starFactor - ratTolerance * Math.random());
        ratOvr = getOverall();

        recruitRating = getScoutingGrade();

        recruitTolerance = (int) ((60 - team.teamPrestige) / wrImportance);
        cost = getInitialCost();
        cost = (int) (cost / wrImportance);
        cost = getLocationCost();
        if (cost < 0) cost = (int) Math.random() * 5 + 1;


        resetSeasonStats();
        resetCareerStats();
    }

    public PlayerWR(String nm, int yr, int stars, Team t, boolean custom) {
        position = "WR";
        height = hAvg + (int) (Math.random() * ((hMax - hMin) + 1)) + hMin;
        weight = wAvg + (int) (Math.random() * ((wMax - wMin) + 1)) + wMin;
        name = nm;
        year = yr;
        team = t;

        wasRedshirt = getWasRedshirtStatus();

        createGenericAttributes();

        ratCatch = (int) (ratBase + stars * customFactor - ratTolerance * Math.random());
        ratSpeed = (int) (ratBase + stars * customFactor - ratTolerance * Math.random());
        ratEvasion = (int) (ratBase + stars * customFactor - ratTolerance * Math.random());
        ratJump = (int) (ratBase + stars * customFactor - ratTolerance * Math.random());
        ratOvr = getOverall();

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
        ratSpeed += (int) (Math.random() * games);
        ratEvasion += (int) (Math.random() * games);
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
            progression = getProgressionOff();

            if (year > 2 && games < minGamesPot) ratPot -= (int) (Math.random() * 15);

            ratFootIQ += (int) (Math.random() * (progression + games - endseason)) / endseasonFactor;
            ratCatch += (int) (Math.random() * (progression + games - endseason)) / endseasonFactor;
            ratSpeed += (int) (Math.random() * (progression + games - endseason)) / endseasonFactor;
            ratEvasion += (int) (Math.random() * (progression + games - endseason)) / endseasonFactor;
            ratJump += (int) (Math.random() * (progression + games - endseason)) / endseasonFactor;
            if (Math.random() * 100 < progression) {
                //breakthrough
                ratCatch += (int) (Math.random() * (progression + games - endseasonBonus)) / endseasonFactor;
                ratSpeed += (int) (Math.random() * (progression + games - endseasonBonus)) / endseasonFactor;
                ratEvasion += (int) (Math.random() * (progression + games - endseasonBonus)) / endseasonFactor;
                ratJump += (int) (Math.random() * (progression + games - endseasonBonus)) / endseasonFactor;
            }

            durabilityProgression();

        }
        ratOvr = getOverall();
        ratImprovement = ratOvr - ratOvrStart;

        careerTargets += statsTargets;
        careerReceptions += statsReceptions;
        careerRecYards += statsRecYards;
        careerTD += statsRecTD;
        careerDrops += statsDrops;
        careerFumbles += statsFumbles;
        careerGames += gamesPlayed;
        careerWins += statsWins;

        careerKickRets += statsKickRets;
        careerKickRetYards += statsKickRetYards;
        careerKickRetTDs += statsKickRetTDs;
        careerPuntRets += statsPuntRets;
        careerPuntRetYards += statsPuntRetYards;
        careerPuntRetTDs += statsPuntRetTDs;
        careerRetGames += statsRetGames;

        addSeasonAwards();
        checkRedshirt();

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

        statsKickRets = 0;
        statsKickRetYards = 0;
        statsKickRetTDs = 0;
        statsPuntRets = 0;
        statsPuntRetYards = 0;
        statsPuntRetTDs = 0;
        statsRetGames = 0;

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
        careerWins = 0;

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
        return statsRecTD * 150 - statsFumbles * 75 + statsReceptions * 2 - statsDrops * 25 + (int) (statsRecYards * 2.65) + statsKickRetYards + statsKickRetTDs * 120 + statsPuntRetYards + statsPuntRetTDs * 120 + ratOvr * 10 + getConfPrestigeBonus();
    }

    @Override
    public int getCareerScore() {
        return statsRecTD * 150 - statsFumbles * 75 + statsReceptions * 2 - statsDrops * 25 + (int) (statsRecYards * 2.65) + ratOvr * 10
                + careerTD * 150 - careerFumbles * 75 + careerReceptions * 2 - careerDrops * 25 + (int) (careerRecYards * 2.65)
                + statsPuntRetYards + statsPuntRetTDs * 150 + careerKickRetYards + careerKickRetTDs * 150 + careerPuntRetYards + careerPuntRetTDs * 150 + ratOvr * 10 * year;

    }

    private float getCareerYardsperTGT() {
        if (careerReceptions < 1) {
            return 0;
        } else {
            float rating = (float)(statsRecYards + careerRecYards) / (statsTargets + careerTargets);
            return rating;
        }
    }

    private float getYardsperTGT() {
        if (statsReceptions < 1) {
            return 0;
        } else {
            float rating = (float)(statsRecYards) / (statsTargets);
            return rating;
        }
    }

    private float getCareerCatchPCT() {
        if (careerTargets < 1) {
            return 0;
        } else {
            float rating = (float)(statsReceptions + careerReceptions) / (statsTargets + careerTargets)*100;
            return rating;
        }
    }

    private float getCatchPCT() {
        if (statsTargets < 1) {
            return 0;
        } else {
            float rating = (float)(statsReceptions) / (statsTargets)*100;
            return rating;
        }
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
        pStats.add("Catching: " + getLetterGrade(ratCatch) + ">Rec Speed: " + getLetterGrade(ratSpeed));
        pStats.add("Evasion: " + getLetterGrade(ratEvasion) + ">Jumping: " + getLetterGrade(ratJump));
        pStats.add(" > ");
        pStats.add("[B]SEASON STATS:");


        pStats.add("TDs: " + statsRecTD + ">Fumbles: " + statsFumbles);
        pStats.add("Rec Yards: " + statsRecYards + " yds>Receptions: " + statsReceptions);
        pStats.add("Catch Percent: " + df2.format(getCatchPCT()) + "%>Yards/Tgt: " + df2.format(getYardsperTGT()) + " yds");
        pStats.add("Yds/Game: " + (statsRecYards / getGames()) + " yds/g>Drops: " + statsDrops);
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
        pStats.add("[B]CAREER STATS:");
        pStats.addAll(getCareerStatsList());
        return pStats;
    }

    @Override
    public ArrayList<String> getCareerStatsList() {
        ArrayList<String> pStats = new ArrayList<>();
        pStats.add("TDs: " + (statsRecTD + careerTD) + ">Fumbles: " + (statsFumbles + careerFumbles));
        pStats.add("Rec Yards: " + (statsRecYards + careerRecYards) + " yds>Receptions: " + (statsReceptions + careerReceptions));
        pStats.add("Catch Percent: " + df2.format(getCareerCatchPCT()) + "%>Yards/Tgt: " + df2.format(getCareerYardsperTGT()) + " yds");
        pStats.add("Yds/Game: " + ((statsRecYards + careerRecYards) / (getGames() + careerGames)) + " yds/g>Drops: " + (statsDrops + careerDrops));
        if (statsKickRets > 0) {
            pStats.add("Kick Rets: " + statsKickRets + ">Kick Ret Yards: " + statsKickRetYards + " yrds");
            pStats.add("Kick Ret TDs: " + statsKickRetTDs + ">Ret Avg: " + df2.format((float) (statsKickRetYards / statsKickRets)));
        }
        if (statsPuntRets > 0) {
            pStats.add("Punt Rets: " + statsPuntRets + ">Punt Ret Yards: " + statsPuntRetYards + " yrds");
            pStats.add("Punt Ret TDs: " + statsPuntRetTDs + ">Ret Avg: " + df2.format((float) (statsPuntRetYards / statsPuntRets)));
        }
        pStats.addAll(super.getCareerStatsList());
        return pStats;
    }

    @Override
    public String getInfoForLineup() {
        if (injury != null)
            return getInitialName() + " [" + getYrStr() + "] " + ratOvr + "/" + getPotRating(team.HC.get(0).ratTalent) + " " + injury.toString();
        return getInitialName() + " [" + getYrStr() + "] " + ratOvr + "/" + getPotRating(team.HC.get(0).ratTalent) + " (" +
                getLetterGrade(ratCatch) + ", " + getLetterGrade(ratSpeed) + ", " + getLetterGrade(ratEvasion) + ", " + getLetterGrade(ratJump) + ")";
    }

    public int getOverall() {
        int ovr;
        ovr = (ratCatch * 2 + ratSpeed + ratEvasion + ratJump) / 5;
        return ovr;
    }

}
