package CFBsimPack;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by ahngu on 10/9/2017.
 *
 * TE should be able to catch the ball, and help block for rushes or passing game
 * Currently using the WR role as base
 *
 */

public class PlayerTE extends Player{

    //public String name;
    //Overall rating, combination of other ratings
    //public int ratOvr;
    //Potential, affects how much he gets better in offseason
    //public int ratPot;
    //FootIQ, affects how smart he plays
    //public int ratFootIQ;
    //RecCat affects how good he is at catching
    public int ratRecCat;
    //BlkR Blocking for Rushes
    public int ratBlkR;
    //RecEva affects how easily he can dodge tackles
    public int ratRecEva;
//    public int ratRecSpd;

    //public Vector ratingsVector;

    //Stats
    public int statsTargets;
    public int statsReceptions;
    public int statsRecYards;
    public int statsTD;
    public int statsDrops;
    public int statsFumbles;

    public int careerTargets;
    public int careerReceptions;
    public int careerRecYards;
    public int careerTD;
    public int careerDrops;
    public int careerFumbles;

    public PlayerTE( String nm, Team t, int yr, int pot, int iq, int cat, int blk, int eva/*, int rsp*/, boolean rs, int dur ) {
        team = t;
        name = nm;
        year = yr;
        gamesPlayed = 0;
        isInjured = false;
        ratOvr = (cat*2 + blk*2 + eva)/5;
        ratPot = pot;
        ratFootIQ = iq;
        ratDur = dur;
        ratRecCat = cat;
        ratBlkR = blk;
        ratRecEva = eva;
//        ratRecSpd = rsp;
        isRedshirt = rs;
        if (isRedshirt) year = 0;

        cost = (int)(Math.pow((float)ratOvr - 55,2)/3.5) + 50 + (int)(Math.random()*100) - 50;

        ratingsVector = new Vector();
        ratingsVector.addElement(name+" ("+getYrStr()+")");
        ratingsVector.addElement(ratOvr+" (+"+ratImprovement+")");
        ratingsVector.addElement(ratPot);
        ratingsVector.addElement(ratFootIQ);
        ratingsVector.addElement(ratRecCat);
        ratingsVector.addElement(ratBlkR);
        ratingsVector.addElement(ratRecEva);
//        ratingsVector.addElement(ratRecSpd);

        statsTargets = 0;
        statsReceptions = 0;
        statsRecYards = 0;
        statsTD = 0;
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
        careerGamesPlayed = 0;
        careerHeismans = 0;
        careerAllAmerican = 0;
        careerAllConference = 0;
        careerWins = 0;

        position = "TE";
    }

    public PlayerTE( String nm, Team t, int yr, int pot, int iq, int cat, int blk, int eva/*, int rsp*/, boolean rs, int dur,
                     int cGamesPlayed, int cTargets, int cReceptions, int cRecYards, int cTD, int cDrops, int cFumbles,
                     int cHeismans, int cAA, int cAC, int cWins) {
        team = t;
        name = nm;
        year = yr;
        gamesPlayed = 0;
        isInjured = false;
        ratOvr = (cat*2 + blk*2 + eva)/5;
        ratPot = pot;
        ratFootIQ = iq;
        ratDur = dur;
        ratRecCat = cat;
        ratBlkR = blk;
        ratRecEva = eva;
//        ratRecSpd = rsp;
        isRedshirt = rs;
        if (isRedshirt) year = 0;

        cost = (int)(Math.pow((float)ratOvr - 55,2)/3.5) + 80 + (int)(Math.random()*100) - 50;

        ratingsVector = new Vector();
        ratingsVector.addElement(name+" ("+getYrStr()+")");
        ratingsVector.addElement(ratOvr+" (+"+ratImprovement+")");
        ratingsVector.addElement(ratPot);
        ratingsVector.addElement(ratFootIQ);
        ratingsVector.addElement(ratRecCat);
        ratingsVector.addElement(ratBlkR);
        ratingsVector.addElement(ratRecEva);
//        ratingsVector.addElement(ratRecSpd);


        statsTargets = 0;
        statsReceptions = 0;
        statsRecYards = 0;
        statsTD = 0;
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
        careerGamesPlayed = cGamesPlayed;
        careerHeismans = cHeismans;
        careerAllAmerican = cAA;
        careerAllConference = cAC;
        careerWins = cWins;

        position = "TE";
    }

    public PlayerTE( String nm, int yr, int stars, Team t ) {
        name = nm;
        year = yr;
        team = t;
        gamesPlayed = 0;
        isInjured = false;
        ratPot = (int) (50 + 50*Math.random());
        ratFootIQ = (int) (50 + 50*Math.random());
        ratDur = (int) (50 + 50*Math.random());
        ratRecCat = (int) (60 + year*5 + stars*5 - 25*Math.random());
        ratBlkR = (int) (60 + year*5 + stars*5 - 25*Math.random());
        ratRecEva = (int) (60 + year*5 + stars*5 - 25*Math.random());
//        ratRecSpd = (int) (60 + year*5 + stars*5 - 25*Math.random());
        ratOvr = (ratRecCat*2 + ratBlkR*2 + ratRecEva)/5;

        cost = (int)(Math.pow((float)ratOvr - 55,2)/3.5) + 80 + (int)(Math.random()*100) - 50;

        ratingsVector = new Vector();
        ratingsVector.addElement(name+" ("+getYrStr()+")");
        ratingsVector.addElement(ratOvr+" (+"+ratImprovement+")");
        ratingsVector.addElement(ratPot);
        ratingsVector.addElement(ratFootIQ);
        ratingsVector.addElement(ratRecCat);
        ratingsVector.addElement(ratBlkR);
        ratingsVector.addElement(ratRecEva);
//        ratingsVector.addElement(ratRecSpd);


        statsTargets = 0;
        statsReceptions = 0;
        statsRecYards = 0;
        statsTD = 0;
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
        careerGamesPlayed = 0;
        careerHeismans = 0;
        careerAllAmerican = 0;
        careerAllConference = 0;
        careerWins = 0;

        position = "TE";
    }

    public Vector getStatsVector() {
        Vector v = new Vector(7);
        v.add(statsReceptions);
        v.add(statsTargets);
        v.add(statsRecYards);
        v.add(statsTD);
        v.add(statsFumbles);
        v.add(statsDrops);
        v.add((float)((int)((float)statsRecYards/statsReceptions*100))/100);
        return v;
    }

    public Vector getRatingsVector() {
        ratingsVector = new Vector();
        ratingsVector.addElement(name + " (" + getYrStr() + ")");
        ratingsVector.addElement(ratOvr + " (+" + ratImprovement + ")");
        ratingsVector.addElement(ratPot);
        ratingsVector.addElement(ratFootIQ);
        ratingsVector.addElement(ratRecCat);
        ratingsVector.addElement(ratBlkR);
        ratingsVector.addElement(ratRecEva);
//        ratingsVector.addElement(ratRecSpd);

        return ratingsVector;
    }

    @Override
    public void advanceSeason() {
        year++;
        int oldOvr = ratOvr;
        ratFootIQ += (int)(Math.random()*(ratPot + gamesPlayed - 35))/10;
        ratRecCat += (int)(Math.random()*(ratPot + gamesPlayed - 35))/10;
        ratBlkR += (int)(Math.random()*(ratPot + gamesPlayed - 35))/10;
        ratRecEva += (int)(Math.random()*(ratPot + gamesPlayed - 35))/10;
//        ratRecSpd += (int)(Math.random()*(ratPot + gamesPlayed - 35))/10;
        if ( Math.random()*100 < ratPot ) {
            //breakthrough
            ratRecCat += (int)(Math.random()*(ratPot + gamesPlayed - 40))/10;
            ratBlkR += (int)(Math.random()*(ratPot + gamesPlayed - 40))/10;
            ratRecEva += (int)(Math.random()*(ratPot + gamesPlayed - 40))/10;
//            ratRecSpd += (int)(Math.random()*(ratPot + gamesPlayed - 35))/10;

        }
        ratOvr = (ratRecCat*2 + ratBlkR + ratRecEva)/4;
        ratImprovement = ratOvr - oldOvr;
        //reset stats (keep career stats?)
        careerTargets += statsTargets;
        careerReceptions += statsReceptions;
        careerRecYards += statsRecYards;
        careerTD += statsTD;
        careerDrops += statsDrops;
        careerFumbles += statsFumbles;
        careerGamesPlayed += gamesPlayed;
        careerWins += statsWins;

        if (wonHeisman) careerHeismans++;
        if (wonAllAmerican) careerAllAmerican++;
        if (wonAllConference) careerAllConference++;

        statsTargets = 0;
        statsReceptions = 0;
        statsRecYards = 0;
        statsTD = 0;
        statsDrops = 0;
        statsFumbles = 0;
    }

    @Override
    public int getHeismanScore() {
        return statsTD * 150 - statsFumbles * 100 - statsDrops * 50 + statsRecYards * 2;
    }

    @Override
    public ArrayList<String> getDetailStatsList(int games) {
        ArrayList<String> pStats = new ArrayList<>();
        pStats.add("TDs/Fumbles: " + statsTD + "/" + statsFumbles + ">Catch Percent: " + (100*statsReceptions/(statsTargets+1))+"%");
        pStats.add("Rec Yards: " + statsRecYards + " yds" + ">Yards/Tgt: " + ((double)(10*statsRecYards/(statsTargets+1))/10) + " yds");
        pStats.add("Yds/Game: " + (statsRecYards/getGamesPlayed()) + " yds/g>Drops: " + statsDrops);
        pStats.add("Games: " + gamesPlayed + " (" + statsWins + "-" + (gamesPlayed-statsWins) + ")" + ">Durability: " + getLetterGrade(ratDur));
        pStats.add("Football IQ: " + getLetterGrade(ratFootIQ) + ">Catching: " + getLetterGrade(ratRecCat));
        pStats.add("Blocking: " + getLetterGrade(ratBlkR) + ">Evasion: " + getLetterGrade(ratRecEva));
        pStats.add(" > ");
        return pStats;
    }

    @Override
    public ArrayList<String> getDetailAllStatsList(int games) {
        ArrayList<String> pStats = new ArrayList<>();
        pStats.add("TDs/Fumbles: " + statsTD + "/" + statsFumbles + ">Catch Percent: " + (100*statsReceptions/(statsTargets+1))+"%");
        pStats.add("Rec Yards: " + statsRecYards + " yds" + ">Yards/Tgt: " + ((double)(10*statsRecYards/(statsTargets+1))/10) + " yds");
        pStats.add("Yds/Game: " + (statsRecYards/getGamesPlayed()) + " yds/g>Drops: " + statsDrops);
        pStats.add("Games: " + gamesPlayed + " (" + statsWins + "-" + (gamesPlayed-statsWins) + ")" + ">Durability: " + getLetterGrade(ratDur));
        pStats.add("Football IQ: " + getLetterGrade(ratFootIQ) + ">Catching: " + getLetterGrade(ratRecCat));
        pStats.add("Blocking: " + getLetterGrade(ratBlkR) + ">Evasion: " + getLetterGrade(ratRecEva));
        pStats.add("[B]CAREER STATS:");
        pStats.addAll(getCareerStatsList());
        return pStats;
    }

    @Override
    public ArrayList<String> getCareerStatsList() {
        ArrayList<String> pStats = new ArrayList<>();
        pStats.add("TDs/Fumbles: " + (statsTD+careerTD) + "/" + (statsFumbles+careerFumbles) + ">Catch Percent: " + (100*(statsReceptions+careerReceptions)/(statsTargets+careerTargets+1))+"%");
        pStats.add("Rec Yards: " + (statsRecYards+careerRecYards) + " yds" + ">Yards/Tgt: " + ((double)(10*(statsRecYards+careerRecYards)/(statsTargets+careerTargets+1))/10) + " yds");
        pStats.add("Yds/Game: " + ((statsRecYards+careerRecYards)/(getGamesPlayed()+careerGamesPlayed)) + " yds/g>Drops: " + (statsDrops+careerDrops));
        pStats.addAll(super.getCareerStatsList());
        return pStats;
    }

    @Override
    public String getInfoForLineup() {
        if (injury != null) return getInitialName() + " [" + getYrStr() + "] " + ratOvr + "/" + getLetterGrade(ratPot) + " " + injury.toString();
        return getInitialName() + " [" + getYrStr() + "] " + ratOvr + "/" + getLetterGrade(ratPot) + " (" +
                getLetterGrade(ratRecCat) + ", " + getLetterGrade(ratBlkR) + ", " + getLetterGrade(ratRecEva) + ")";
    }

}
