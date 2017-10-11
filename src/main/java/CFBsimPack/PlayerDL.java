package CFBsimPack;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by ahngu on 10/9/2017.
 *
 * I imagine the DL will be a 1:1 swap of the F7 category
 *
 */

public class PlayerDL extends Player {

    //public String name;
    //Overall rating, combination of other ratings
    //public int ratOvr;
    //Potential, affects how much he gets better in offseason
    //public int ratPot;
    //FootIQ, affects how smart he plays
    //public int ratFootIQ;
    //OLPow affects how strong he is against OL
    public int ratDLPow;
    //OLBkR affects how well he defends running plays
    public int ratDLRsh;
    //OLBkP affects how well he defends passing plays
    public int ratDLPas;

    //Stats
    public int statsTackles;
    public int statsSacks;
    public int statsFumbles;
    public int statsInts;

    public int careerTackles;
    public int careerSacks;
    public int careerFumbles;
    public int careerInts;

    //public Vector ratingsVector;

    public PlayerDL( String nm, Team t, int yr, int pot, int iq, int pow, int rsh, int pas, boolean rs, int dur ) {
        team = t;
        name = nm;
        year = yr;
        gamesPlayed = 0;
        isInjured = false;
        ratOvr = (pow*3 + rsh + pas)/5;
        ratPot = pot;
        ratFootIQ = iq;
        ratDur = dur;
        ratDLPow = pow;
        ratDLRsh = rsh;
        ratDLPas = pas;
        isRedshirt = rs;
        if (isRedshirt) year = 0;
        position = "DL";

        cost = (int)(Math.pow((float)ratOvr - 55,2)/6) + 50 + (int)(Math.random()*100) - 50;

        ratingsVector = new Vector();
        ratingsVector.addElement(name+" ("+getYrStr()+")");
        ratingsVector.addElement(ratOvr+" (+"+ratImprovement+")");
        ratingsVector.addElement(ratPot);
        ratingsVector.addElement(ratFootIQ);
        ratingsVector.addElement(ratDLPow);
        ratingsVector.addElement(ratDLRsh);
        ratingsVector.addElement(ratDLPas);

        wonHeisman = false;
        wonAllAmerican = false;
        wonAllConference = false;
        statsWins = 0;

        careerGamesPlayed = 0;
        careerHeismans = 0;
        careerAllAmerican = 0;
        careerAllConference = 0;
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

    public PlayerDL( String nm, Team t, int yr, int pot, int iq, int pow, int rsh, int pas, boolean rs, int dur,
                     int cGamesPlayed, int cTackles, int cSacks, int cFumbles, int cInts, int cHeismans, int cAA, int cAC, int cWins ) {
        team = t;
        name = nm;
        year = yr;
        gamesPlayed = 0;
        isInjured = false;
        ratOvr = (pow*3 + rsh + pas)/5;
        ratPot = pot;
        ratFootIQ = iq;
        ratDur = dur;
        ratDLPow = pow;
        ratDLRsh = rsh;
        ratDLPas = pas;
        isRedshirt = rs;
        if (isRedshirt) year = 0;
        position = "DL";

        cost = (int)(Math.pow((float)ratOvr - 55,2)/6) + 50 + (int)(Math.random()*100) - 50;

        ratingsVector = new Vector();
        ratingsVector.addElement(name+" ("+getYrStr()+")");
        ratingsVector.addElement(ratOvr+" (+"+ratImprovement+")");
        ratingsVector.addElement(ratPot);
        ratingsVector.addElement(ratFootIQ);
        ratingsVector.addElement(ratDLPow);
        ratingsVector.addElement(ratDLRsh);
        ratingsVector.addElement(ratDLPas);

        wonHeisman = false;
        wonAllAmerican = false;
        wonAllConference = false;
        statsWins = 0;

        careerGamesPlayed = cGamesPlayed;
        careerHeismans = cHeismans;
        careerAllAmerican = cAA;
        careerAllConference = cAC;
        careerWins = cWins;

        statsTackles = 0;
        statsSacks = 0;
        statsFumbles = 0;
        statsInts = 0;

        careerTackles = cTackles;
        careerSacks = cSacks;
        careerFumbles = cFumbles;
        careerInts = cInts;
    }

    public PlayerDL( String nm, int yr, int stars, Team t ) {
        name = nm;
        year = yr;
        team = t;
        gamesPlayed = 0;
        isInjured = false;
        ratPot = (int) (50 + 50*Math.random());
        ratFootIQ = (int) (50 + 50*Math.random());
        ratDur = (int) (50 + 50*Math.random());
        ratDLPow = (int) (60 + year*5 + stars*5 - 25*Math.random());
        ratDLRsh = (int) (60 + year*5 + stars*5 - 25*Math.random());
        ratDLPas = (int) (60 + year*5 + stars*5 - 25*Math.random());
        ratOvr = (ratDLPow*3 + ratDLRsh + ratDLPas)/5;
        position = "DL";

        cost = (int)(Math.pow((float)ratOvr - 55,2)/6) + 50 + (int)(Math.random()*100) - 50;

        ratingsVector = new Vector();
        ratingsVector.addElement(name+" ("+getYrStr()+")");
        ratingsVector.addElement(ratOvr+" (+"+ratImprovement+")");
        ratingsVector.addElement(ratPot);
        ratingsVector.addElement(ratFootIQ);
        ratingsVector.addElement(ratDLPow);
        ratingsVector.addElement(ratDLRsh);
        ratingsVector.addElement(ratDLPas);

        wonHeisman = false;
        wonAllAmerican = false;
        wonAllConference = false;
        statsWins = 0;

        careerGamesPlayed = 0;
        careerHeismans = 0;
        careerAllAmerican = 0;
        careerAllConference = 0;
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

    public Vector getStatsVector() {
        Vector v = new Vector(4);
        v.add(statsTackles);
        v.add(statsSacks);
        v.add(statsFumbles);
        v.add(statsInts);
        return v;
    }

    public Vector getRatingsVector() {
        ratingsVector = new Vector();
        ratingsVector.addElement(name+" ("+getYrStr()+")");
        ratingsVector.addElement(ratOvr+" (+"+ratImprovement+")");
        ratingsVector.addElement(ratPot);
        ratingsVector.addElement(ratFootIQ);
        ratingsVector.addElement(ratDLPow);
        ratingsVector.addElement(ratDLRsh);
        ratingsVector.addElement(ratDLPas);
        return ratingsVector;
    }

    @Override
    public void advanceSeason() {
        year++;
        int oldOvr = ratOvr;
        if (wonAllConference) ratPot++;
        if (wonAllAmerican) ratPot++;
        if (year > 2 && gamesPlayed < 4) ratPot -= (int)Math.random()*15;


        ratFootIQ += (int)(Math.random()*(ratPot + gamesPlayed - 35))/10;
        ratDLPow += (int)(Math.random()*(ratPot + gamesPlayed - 35))/10;
        ratDLRsh += (int)(Math.random()*(ratPot + gamesPlayed - 35))/10;
        ratDLPas += (int)(Math.random()*(ratPot + gamesPlayed - 35))/10;
        if ( Math.random()*100 < ratPot ) {
            //breakthrough
            ratDLPow += (int)(Math.random()*(ratPot + gamesPlayed - 40))/10;
            ratDLRsh += (int)(Math.random()*(ratPot + gamesPlayed - 40))/10;
            ratDLPas += (int)(Math.random()*(ratPot + gamesPlayed - 40))/10;
        }
        ratOvr = (ratDLPow*3 + ratDLRsh + ratDLPas)/5;
        ratImprovement = ratOvr - oldOvr;

        careerGamesPlayed += gamesPlayed;
        careerWins += statsWins;

        careerTackles += statsTackles;
        careerSacks += statsSacks;
        careerFumbles += statsFumbles;
        careerInts += statsInts;

        statsTackles = 0;
        statsSacks = 0;
        statsFumbles = 0;
        statsInts = 0;

        if (wonHeisman) careerHeismans++;
        if (wonAllAmerican) careerAllAmerican++;
        if (wonAllConference) careerAllConference++;
    }

    @Override
    public int getHeismanScore() {
        return statsTackles*37 + statsSacks*245 + statsFumbles*500 + statsInts*500 + ratOvr;
    }

    @Override
    public ArrayList<String> getDetailStatsList(int games) {
        ArrayList<String> pStats = new ArrayList<>();
        pStats.add("Tackles: " + (statsTackles + careerTackles) + " >Sacks: " + (statsSacks + careerSacks));
        pStats.add("Fumbles: " + (statsFumbles + careerFumbles) + " >Interceptions: " + (statsInts + careerInts));
        pStats.add("Games: " + gamesPlayed + " (" + statsWins + "-" + (gamesPlayed-statsWins) + ")" + ">Durability: " + getLetterGrade(ratDur));
        pStats.add("Football IQ: " + getLetterGrade(ratFootIQ) + ">Strength: " + getLetterGrade(ratDLPow));
        pStats.add("Run Stop: " + getLetterGrade(ratDLRsh) + ">Pass Pressure: " + getLetterGrade(ratDLPas));
        pStats.add(" > ");
        return pStats;
    }

    @Override
    public ArrayList<String> getDetailAllStatsList(int games) {
        ArrayList<String> pStats = new ArrayList<>();
        pStats.add("Tackles: " + (statsTackles + careerTackles) + " >Sacks: " + (statsSacks + careerSacks));
        pStats.add("Fumbles: " + (statsFumbles + careerFumbles) + " >Interceptions: " + (statsInts + careerInts));
        pStats.add("Games: " + gamesPlayed + " (" + statsWins + "-" + (gamesPlayed-statsWins) + ")" + ">Durability: " + getLetterGrade(ratDur));
        pStats.add("Football IQ: " + getLetterGrade(ratFootIQ) + ">Strength: " + getLetterGrade(ratDLPow));
        pStats.add("Run Stop: " + getLetterGrade(ratDLRsh) + ">Pass Pressure: " + getLetterGrade(ratDLPas));
        pStats.add("[B]CAREER STATS:");
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
        if (injury != null) return getInitialName() + " [" + getYrStr() + "] " + ratOvr + "/" + getLetterGrade(ratPot) + " " + injury.toString();
        return getInitialName() + " [" + getYrStr() + "] " + ratOvr + "/" + getLetterGrade(ratPot) + " (" +
                getLetterGrade(ratDLPow) + ", " + getLetterGrade(ratDLRsh) + ", " + getLetterGrade(ratDLPas) + ")";
    }

}
