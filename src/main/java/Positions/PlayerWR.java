/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Positions;

import java.util.ArrayList;

import Simulation.Team;

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

        ratOvr = (cat * 2 + spd + eva + jmp) / 5;
        ratPot = pot;
        ratFootIQ = iq;
        ratDur = dur;
        ratCatch = cat;
        ratSpeed = spd;
        ratEvasion = eva;
        ratJump = jmp;
        isRedshirt = rs;
        if (isRedshirt) year = 0;
        wasRedshirt = wasRS;
        region = reg;
        personality = trait;
        recruitRating = scout;
        height = h;
        weight = w;

        resetSeasonStats();
        resetCareerStats();
    }

    public PlayerWR(Team t, String nm, int yr, int reg, int trait, int iq, int scout, boolean transfer, boolean wasRS,  boolean wo, int pot, int dur, boolean rs, int cGamesPlayed, int cWins, int cHeismans, int cAA, int cAC, int cTF, int cAF,
                    int cat, int spd, int eva, int jmp, int h, int w, int cTargets, int cReceptions, int cRecYards, int cTD, int cDrops, int cFumbles, int kret, int kyds, int ktd, int pret, int pyds, int ptd) {
        position = "WR";
        team = t;
        name = nm;
        year = yr;

        ratOvr = (cat * 2 + spd + eva + jmp) / 5;
        ratPot = pot;
        ratFootIQ = iq;
        ratDur = dur;
        ratCatch = cat;
        ratSpeed = spd;
        ratEvasion = eva;
        ratJump = jmp;
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

    public PlayerWR(String nm, int yr, int stars, Team t) {
        position = "WR";
        height = hAvg + (int) (Math.random() * ((hMax - hMin) + 1)) + hMin;
        weight = wAvg + (int) (Math.random() * ((wMax - wMin) + 1)) + wMin;
        name = nm;
        year = yr;
        team = t;

        ratPot = (int) (attrBase + 50 * Math.random());
        ratFootIQ = (int) (attrBase + 50 * Math.random());
        ratDur = (int) (attrBase + 50 * Math.random());
        ratCatch = (int) (ratBase + year * yearFactor + stars * starFactor - ratTolerance * Math.random());
        ratSpeed = (int) (ratBase + year * yearFactor + stars * starFactor - ratTolerance * Math.random());
        ratEvasion = (int) (ratBase + year * yearFactor + stars * starFactor - ratTolerance * Math.random());
        ratJump = (int) (ratBase + year * yearFactor + stars * starFactor - ratTolerance * Math.random());
        ratOvr = (ratCatch * 2 + ratSpeed + ratEvasion + ratJump) / 5;
        region = (int) (Math.random() * 5);
        personality = (int) (attrBase + 50 * Math.random());

        recruitRating = getScoutingGrade();

        recruitTolerance = (int) ((60 - team.teamPrestige) / wrImportance);
        cost = (int) ((Math.pow((float) ratOvr - costBaseRating, 2) / 5) + (int) Math.random() * recruitTolerance);

        cost = (int) (cost / wrImportance);

        double locFactor = Math.abs(team.location - region) - 2.5;
        cost = cost + (int) (Math.random() * (locFactor * locationDiscount));
        if (cost < 0) cost = (int) Math.random() * 7 + 1;


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

        ratPot = (int) (attrBase + 50 * Math.random());
        ratFootIQ = (int) (attrBase + 50 * Math.random());
        ratDur = (int) (attrBase + 50 * Math.random());
        ratCatch = (int) (ratBase + stars * customFactor - ratTolerance * Math.random());
        ratSpeed = (int) (ratBase + stars * customFactor - ratTolerance * Math.random());
        ratEvasion = (int) (ratBase + stars * customFactor - ratTolerance * Math.random());
        ratJump = (int) (ratBase + stars * customFactor - ratTolerance * Math.random());
        ratOvr = (ratCatch * 2 + ratSpeed + ratEvasion + ratJump) / 5;
        region = (int) (Math.random() * 5);
        personality = (int) (attrBase + 50 * Math.random());

        if(custom) isWalkOn = true;
        recruitRating = getScoutingGrade();

        resetSeasonStats();
        resetCareerStats();
    }

    @Override
    public void advanceSeason() {
        int oldOvr = ratOvr;
        progression = (ratPot * 3 + team.HC.get(0).ratTalent * 2 + team.HC.get(0).ratOff) / 6;
        int games = gamesStarted + (gamesPlayed - gamesStarted) / 3;

        if (!isMedicalRS) {
            year++;
            if (wonAllConference) ratPot += (int) Math.random() * allConfPotBonus;
            if (wonAllAmerican) ratPot += (int) Math.random() * allAmericanBonus;
            if (wonAllFreshman) ratPot += (int)Math.random()*allFreshmanBonus;
            if (wonTopFreshman) ratPot += (int)Math.random()*topBonus;
            if (wonHeisman) ratPot += (int)Math.random()*topBonus;

            if (year > 2 && games < minGamesPot) ratPot -= (int) (Math.random() * 15);

            ratFootIQ += (int) (Math.random() * (progression + games - 35)) / 10;
            ratCatch += (int) (Math.random() * (progression + games - 35)) / 10;
            ratSpeed += (int) (Math.random() * (progression + games - 35)) / 10;
            ratEvasion += (int) (Math.random() * (progression + games - 35)) / 10;
            ratJump += (int) (Math.random() * (progression + games - 25)) / 10;
            if (Math.random() * 100 < progression) {
                //breakthrough
                ratCatch += (int) (Math.random() * (progression + games - 40)) / 10;
                ratSpeed += (int) (Math.random() * (progression + games - 40)) / 10;
                ratEvasion += (int) (Math.random() * (progression + games - 40)) / 10;
                ratJump += (int) (Math.random() * (progression + games - 30)) / 10;
            }
        }
        ratOvr = (ratCatch * 2 + ratSpeed + ratEvasion + ratJump) / 5;
        ratImprovement = ratOvr - oldOvr;

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

        if (wonHeisman) careerHeismans++;
        if (wonAllAmerican) careerAllAmerican++;
        if (wonAllConference) careerAllConference++;
        if (wonAllFreshman) careerAllFreshman++;
        if (wonTopFreshman) careerTopFreshman++;

        resetSeasonStats();

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
        return statsRecTD * 150 - statsFumbles * 75 + statsReceptions * 2 - statsDrops * 25 + (int) (statsRecYards * 2.65) + statsKickRetYards + statsKickRetTDs * 150 + statsPuntRetYards + statsPuntRetTDs * 150 + ratOvr * 10 + getConfPrestigeBonus();
    }

    @Override
    public int getCareerScore() {
        return statsRecTD * 150 - statsFumbles * 75 + statsReceptions * 2 - statsDrops * 25 + (int) (statsRecYards * 2.65) + ratOvr * 10
                + careerTD * 150 - careerFumbles * 75 + careerReceptions * 2 - careerDrops * 25 + (int) (careerRecYards * 2.65)
                + statsPuntRetYards + statsPuntRetTDs * 150 + careerKickRetYards + careerKickRetTDs * 150 + careerPuntRetYards + careerPuntRetTDs * 150 + ratOvr * 10 * year;

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
        if (statsKickRets > 0) {
            pStats.add("Kick Rets: " + statsKickRets + ">Kick Ret Yards: " + statsKickRetYards + " yrds");
            pStats.add("Kick Ret TDs: " + statsKickRetTDs + ">Ret Avg: " + (double) (statsKickRetYards / statsKickRets));
        }
        if (statsPuntRets > 0) {
            pStats.add("Punt Rets: " + statsPuntRets + ">Punt Ret Yards: " + statsPuntRetYards + " yrds");
            pStats.add("Punt Ret TDs: " + statsPuntRetTDs + ">Ret Avg: " + (double) (statsPuntRetYards / statsPuntRets));
        }
        pStats.add("Catching: " + getLetterGrade(ratCatch) + ">Rec Speed: " + getLetterGrade(ratSpeed));
        pStats.add("Evasion: " + getLetterGrade(ratEvasion) + ">Jumping: " + getLetterGrade(ratJump));
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
        if (statsKickRets > 0) {
            pStats.add("Kick Rets: " + statsKickRets + ">Kick Ret Yards: " + statsKickRetYards + " yrds");
            pStats.add("Kick Ret TDs: " + statsKickRetTDs + ">Ret Avg: " + (double) (statsKickRetYards / statsKickRets));
        }
        if (statsPuntRets > 0) {
            pStats.add("Punt Rets: " + statsPuntRets + ">Punt Ret Yards: " + statsPuntRetYards + " yrds");
            pStats.add("Punt Ret TDs: " + statsPuntRetTDs + ">Ret Avg: " + (double) (statsPuntRetYards / statsPuntRets));
        }
        pStats.add("Catching: " + getLetterGrade(ratCatch) + ">Rec Speed: " + getLetterGrade(ratSpeed));
        pStats.add("Evasion: " + getLetterGrade(ratEvasion) + ">Jumping: " + getLetterGrade(ratJump));
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
                getLetterGrade(ratCatch) + ", " + getLetterGrade(ratSpeed) + ", " + getLetterGrade(ratEvasion) + ", " + getLetterGrade(ratJump) + ")";
    }
}
