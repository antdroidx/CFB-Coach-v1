package CFBsimPack;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by ahngu on 10/9/2017.
 *
 * Linebacker needs to stop the run and defend TE and most importantly, tackle.
 *
 * Currently using the F7 role as base
 *
 */

public class PlayerLB extends Player{

    //public String name;
    //Overall rating, combination of other ratings
    //public int ratOvr;
    //Potential, affects how much he gets better in offseason
    //public int ratPot;
    //FootIQ, affects how smart he plays
    //public int ratFootIQ;
    //OLcov affects how strong he is against OL
    
    //Coverage against TEs
    public int ratCoverage;
    //OLBkR affects how well he defends running plays
    public int ratRunStop;
    //LB Tackle Ability
    public int ratTackle;
    //public int ratSpeed;
    public int ratSpeed;


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

    public PlayerLB( String nm, Team t, int yr, int pot, int iq, int cov, int rsh, int tkl, boolean rs, int dur, int spd) {
        team = t;
        name = nm;
        year = yr;
        gamesPlayed = 0;
        isInjured = false;
        ratOvr = (cov + 2*rsh + 2*tkl + spd)/6;
        ratPot = pot;
        ratFootIQ = iq;
        ratDur = dur;
        ratCoverage = cov;
        ratRunStop = rsh;
        ratTackle = tkl;
        ratSpeed = spd;
        isRedshirt = rs;
        if (isRedshirt) year = 0;
        position = "LB";

        cost = (int)(Math.pow((float)ratOvr - 55,2)/4) + 70 + (int)(Math.random()*100) - 50;

        ratingsVector = new Vector();
        ratingsVector.addElement(name+" ("+getYrStr()+")");
        ratingsVector.addElement(ratOvr+" (+"+ratImprovement+")");
        ratingsVector.addElement(ratPot);
        ratingsVector.addElement(ratFootIQ);
        ratingsVector.addElement(ratCoverage);
        ratingsVector.addElement(ratRunStop);
        ratingsVector.addElement(ratTackle);
        ratingsVector.addElement(ratSpeed);

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

    public PlayerLB( String nm, Team t, int yr, int pot, int iq, int cov, int rsh, int tkl, boolean rs, int dur, int spd,
                     int cGamesPlayed, int cTackles, int cSacks, int cFumbles, int cInts, int cHeismans, int cAA, int cAC, int cWins ) {
        team = t;
        name = nm;
        year = yr;
        gamesPlayed = 0;
        isInjured = false;
        ratOvr = (cov + rsh*2 + tkl*2)/5;
        ratPot = pot;
        ratFootIQ = iq;
        ratDur = dur;
        ratCoverage = cov;
        ratRunStop = rsh;
        ratTackle = tkl;
        ratSpeed = spd;
        isRedshirt = rs;
        if (isRedshirt) year = 0;
        position = "LB";

        cost = (int)(Math.pow((float)ratOvr - 55,2)/4) + 70 + (int)(Math.random()*100) - 50;

        ratingsVector = new Vector();
        ratingsVector.addElement(name+" ("+getYrStr()+")");
        ratingsVector.addElement(ratOvr+" (+"+ratImprovement+")");
        ratingsVector.addElement(ratPot);
        ratingsVector.addElement(ratFootIQ);
        ratingsVector.addElement(ratCoverage);
        ratingsVector.addElement(ratRunStop);
        ratingsVector.addElement(ratTackle);
        ratingsVector.addElement(ratSpeed);

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

    public PlayerLB( String nm, int yr, int stars, Team t ) {
        name = nm;
        year = yr;
        team = t;
        gamesPlayed = 0;
        isInjured = false;
        ratPot = (int) (50 + 50*Math.random());
        ratFootIQ = (int) (50 + 50*Math.random());
        ratDur = (int) (50 + 50*Math.random());
        ratCoverage = (int) (60 + year*5 + stars*5 - 25*Math.random());
        ratRunStop = (int) (60 + year*5 + stars*5 - 25*Math.random());
        ratTackle = (int) (60 + year*5 + stars*5 - 25*Math.random());
        ratSpeed = (int) (60 + year*5 + stars*5 - 25*Math.random());
        ratOvr = (ratCoverage + ratRunStop *2 + ratTackle *2 + ratSpeed)/6;
        position = "LB";

        cost = (int)(Math.pow((float)ratOvr - 55,2)/4) + 70 + (int)(Math.random()*100) - 50;

        ratingsVector = new Vector();
        ratingsVector.addElement(name+" ("+getYrStr()+")");
        ratingsVector.addElement(ratOvr+" (+"+ratImprovement+")");
        ratingsVector.addElement(ratPot);
        ratingsVector.addElement(ratFootIQ);
        ratingsVector.addElement(ratCoverage);
        ratingsVector.addElement(ratRunStop);
        ratingsVector.addElement(ratTackle);
        ratingsVector.addElement(ratSpeed);


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

    @Override
    public int getHeismanScore() {
        return statsTackles*30 + statsSacks*550 + statsFumbles*550 + statsInts*500 + 15*ratOvr - (2*team.teamOppYards) - (2*team.teamOppPoints);
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
        ratingsVector.addElement(ratCoverage);
        ratingsVector.addElement(ratRunStop);
        ratingsVector.addElement(ratTackle);
        ratingsVector.addElement(ratSpeed);
        return ratingsVector;
    }

    @Override
    public void advanceSeason() {
        year++;
        int oldOvr = ratOvr;
        if (wonAllConference) ratPot++;
        if (wonAllAmerican) ratPot++;
        if (year > 2 && gamesPlayed < 4) ratPot -= (int)Math.random()*15;
        if (ratPot < 0) ratPot = 0;

        ratFootIQ += (int)(Math.random()*(ratPot + gamesPlayed - 35))/10;
        ratCoverage += (int)(Math.random()*(ratPot + gamesPlayed - 35))/10;
        ratRunStop += (int)(Math.random()*(ratPot + gamesPlayed - 35))/10;
        ratTackle += (int)(Math.random()*(ratPot + gamesPlayed - 35))/10;
        ratSpeed += (int)(Math.random()*(ratPot + gamesPlayed - 35))/10;
        if ( Math.random()*100 < ratPot ) {
            //breakthrough
            ratCoverage += (int)(Math.random()*(ratPot + gamesPlayed - 40))/10;
            ratRunStop += (int)(Math.random()*(ratPot + gamesPlayed - 40))/10;
            ratTackle += (int)(Math.random()*(ratPot + gamesPlayed - 40))/10;
            ratSpeed += (int)(Math.random()*(ratPot + gamesPlayed - 40))/10;
        }
        ratOvr = (ratCoverage + ratRunStop *2 + ratTackle *2 + ratSpeed)/6;
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
    public ArrayList<String> getDetailStatsList(int games) {
        ArrayList<String> pStats = new ArrayList<>();
        pStats.add("Tackles: " + (statsTackles) + " >Sacks: " + (statsSacks));
        pStats.add("Fumbles: " + (statsFumbles) + " >Interceptions: " + (statsInts));
        pStats.add("Games: " + gamesPlayed + " (" + statsWins + "-" + (gamesPlayed-statsWins) + ")" + ">Durability: " + getLetterGrade(ratDur));
        pStats.add("Football IQ: " + getLetterGrade(ratFootIQ) + ">Coverage: " + getLetterGrade(ratCoverage));
        pStats.add("Run Stop: " + getLetterGrade(ratRunStop) + ">Tackling: " + getLetterGrade(ratTackle));
        pStats.add("Speed: " + getLetterGrade(ratSpeed) + ">Nothing ");
        pStats.add(" > ");
        return pStats;
    }

    @Override
    public ArrayList<String> getDetailAllStatsList(int games) {
        ArrayList<String> pStats = new ArrayList<>();
        pStats.add("Tackles: " + (statsTackles) + " >Sacks: " + (statsSacks));
        pStats.add("Fumbles: " + (statsFumbles) + " >Interceptions: " + (statsInts));
        pStats.add("Games: " + gamesPlayed + " (" + statsWins + "-" + (gamesPlayed-statsWins) + ")" + ">Durability: " + getLetterGrade(ratDur));
        pStats.add("Football IQ: " + getLetterGrade(ratFootIQ) + ">Coverage: " + getLetterGrade(ratCoverage));
        pStats.add("Run Stop: " + getLetterGrade(ratRunStop) + ">Tackling: " + getLetterGrade(ratTackle));
        pStats.add("Speed: " + getLetterGrade(ratSpeed) + ">Nothing ");
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
                getLetterGrade(ratCoverage) + ", " + getLetterGrade(ratRunStop) + ", " + getLetterGrade(ratTackle) +", " + getLetterGrade(ratSpeed) + ")";
    }

}
