package Simulation;

import java.util.ArrayList;

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

    public PlayerOL(String nm, Team t, int yr, int pot, int iq, int pow, int bkr, int bkp, boolean rs, int dur, int awr, int reg, int trait) {
        team = t;
        name = nm;
        year = yr;
        gamesPlayed = 0;
        isInjured = false;
        ratOvr = (pow * 3 + bkr * 2 + bkp * 2 + awr) / 8;
        ratPot = pot;
        ratFootIQ = iq;
        ratDur = dur;
        ratStrength = pow;
        ratRunBlock = bkr;
        ratPassBlock = bkp;
        ratAwareness = awr;
        isRedshirt = rs;
        if (isRedshirt) year = 0;
        position = "OL";
        region = reg;
        personality = trait;
        cost = (int) (Math.pow((float) ratOvr - 55, 2) / 4) + 50 + (int) (Math.random() * 100) - 50;

        wonHeisman = false;
        wonAllAmerican = false;
        wonAllConference = false;
        statsWins = 0;

        careerGamesPlayed = 0;
        careerHeismans = 0;
        careerAllAmerican = 0;
        careerAllConference = 0;
        careerWins = 0;
    }

    public PlayerOL(String nm, Team t, int yr, int pot, int iq, int pow, int bkr, int bkp, boolean rs, int dur, int awr,
                    int cGamesPlayed, int cHeismans, int cAA, int cAC, int cWins, boolean transfer, int reg, int trait) {
        team = t;
        name = nm;
        year = yr;
        gamesPlayed = 0;
        isInjured = false;
        ratOvr = (pow * 3 + bkr * 2 + bkp * 2 + awr) / 8;
        ratPot = pot;
        ratFootIQ = iq;
        ratDur = dur;
        ratStrength = pow;
        ratRunBlock = bkr;
        ratPassBlock = bkp;
        ratAwareness = awr;
        isRedshirt = rs;
        if (isRedshirt) year = 0;
        isTransfer = transfer;
        region = reg;
        personality = trait;
        position = "OL";

        cost = (int) (Math.pow((float) ratOvr - 55, 2) / 4) + 50 + (int) (Math.random() * 100) - 50;

        wonHeisman = false;
        wonAllAmerican = false;
        wonAllConference = false;
        statsWins = 0;

        careerGamesPlayed = cGamesPlayed;
        careerHeismans = cHeismans;
        careerAllAmerican = cAA;
        careerAllConference = cAC;
        careerWins = cWins;
    }

    public PlayerOL(String nm, int yr, int stars, Team t) {
        name = nm;
        year = yr;
        team = t;
        gamesPlayed = 0;
        isInjured = false;
        ratPot = (int) (50 + 50 * Math.random());
        ratFootIQ = (int) (50 + 50 * Math.random());
        ratDur = (int) (50 + 50 * Math.random());
        ratStrength = (int) (60 + year * 5 + stars * 5 - 25 * Math.random());
        ratRunBlock = (int) (60 + year * 5 + stars * 5 - 25 * Math.random());
        ratPassBlock = (int) (60 + year * 5 + stars * 5 - 25 * Math.random());
        ratAwareness = (int) (60 + year * 5 + stars * 5 - 25 * Math.random());
        ratOvr = (ratStrength * 3 + ratRunBlock * 2 + ratPassBlock * 2 + ratAwareness) / 8;
        position = "OL";
        region = (int)(Math.random()*10);
        personality = (int) (50 + 50 * Math.random());
        cost = (int) (Math.pow((float) ratOvr - 55, 2) / 4) + 50 + (int) (Math.random() * 100) - 50;

        int locFactor = Math.abs(team.location - region) - 5;
        cost = cost + (int)(Math.random()*(locFactor * 10));
        if (cost < 15) cost = (int)(Math.random()*20);

        wonHeisman = false;
        wonAllAmerican = false;
        wonAllConference = false;
        statsWins = 0;

        careerGamesPlayed = 0;
        careerHeismans = 0;
        careerAllAmerican = 0;
        careerAllConference = 0;
        careerWins = 0;

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

            ratFootIQ += (int) (Math.random() * (progression + gamesPlayed - 35)) / 10;
            ratStrength += (int) (Math.random() * (progression + gamesPlayed - 35)) / 10;
            ratRunBlock += (int) (Math.random() * (progression + gamesPlayed - 35)) / 10;
            ratPassBlock += (int) (Math.random() * (progression + gamesPlayed - 35)) / 10;
            ratAwareness += (int) (Math.random() * (progression + gamesPlayed - 35)) / 10;
            if (Math.random() * 100 < progression) {
                //breakthrough
                ratStrength += (int) (Math.random() * (progression + gamesPlayed - 40)) / 10;
                ratRunBlock += (int) (Math.random() * (progression + gamesPlayed - 40)) / 10;
                ratPassBlock += (int) (Math.random() * (progression + gamesPlayed - 40)) / 10;
                ratAwareness += (int) (Math.random() * (progression + gamesPlayed - 40)) / 10;
            }
        }
        ratOvr = (ratStrength * 3 + ratRunBlock * 2 + ratPassBlock * 2 + ratAwareness) / 8;
        ratImprovement = ratOvr - oldOvr;

        careerGamesPlayed += gamesPlayed;
        careerWins += statsWins;

        if (wonHeisman) careerHeismans++;
        if (wonAllAmerican) careerAllAmerican++;
        if (wonAllConference) careerAllConference++;

        if (isTransfer) {
            isTransfer = false;
            year -= 1;
        }
    }

    @Override
    public int getHeismanScore() {
        return ratOvr * 10 + team.confPrestige * 5;
    }

    @Override
    public ArrayList<String> getDetailStatsList(int games) {
        ArrayList<String> pStats = new ArrayList<>();
        pStats.add("Games: " + gamesPlayed + " (" + statsWins + "-" + (gamesPlayed - statsWins) + ")" + ">Durability: " + getLetterGrade(ratDur));
        pStats.add("Home Region: " + getRegion(region) + ">Personality: " + getLetterGrade(personality));
        pStats.add("Football IQ: " + getLetterGrade(ratFootIQ) + ">Strength: " + getLetterGrade(ratStrength));
        pStats.add("Run Block: " + getLetterGrade(ratRunBlock) + ">Pass Block: " + getLetterGrade(ratPassBlock));
        pStats.add("Awareness: " + getLetterGrade(ratAwareness) + ">Nothing");
        pStats.add(" > ");
        return pStats;
    }

    @Override
    public ArrayList<String> getDetailAllStatsList(int games) {
        ArrayList<String> pStats = new ArrayList<>();
        pStats.add("Games: " + gamesPlayed + " (" + statsWins + "-" + (gamesPlayed - statsWins) + ")" + ">Durability: " + getLetterGrade(ratDur));
        pStats.add("Home Region: " + getRegion(region) + ">Personality: " + getLetterGrade(personality));
        pStats.add("Football IQ: " + getLetterGrade(ratFootIQ) + ">Strength: " + getLetterGrade(ratStrength));
        pStats.add("Run Block: " + getLetterGrade(ratRunBlock) + ">Pass Block: " + getLetterGrade(ratPassBlock));
        pStats.add("Awareness: " + getLetterGrade(ratAwareness) + ">Nothing");
        pStats.add("[B]CAREER STATS:");
        pStats.addAll(getCareerStatsList());
        return pStats;
    }

    @Override
    public String getInfoForLineup() {
        if (injury != null)
            return getInitialName() + " [" + getYrStr() + "] " + ratOvr + "/" + getLetterGrade(ratPot) + " " + injury.toString();
        return getInitialName() + " [" + getYrStr() + "] " + ratOvr + "/" + getLetterGrade(ratPot) + " (" +
                getLetterGrade(ratStrength) + ", " + getLetterGrade(ratRunBlock) + ", " + getLetterGrade(ratPassBlock) + ", " + getLetterGrade(ratAwareness) + ")";
    }
}
