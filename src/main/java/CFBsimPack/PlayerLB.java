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
    public int ratLBCov;
    //OLBkR affects how well he defends running plays
    public int ratLBRsh;
    //LB Tackle Ability
    public int ratLBTkl;
    //public int ratCBSpd;

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

    public PlayerLB( String nm, Team t, int yr, int pot, int iq, int cov, int rsh, int tkl/*, int spd*/, boolean rs, int dur ) {
        team = t;
        name = nm;
        year = yr;
        gamesPlayed = 0;
        isInjured = false;
        ratOvr = (cov + 2*rsh + 2*tkl)/5;
        ratPot = pot;
        ratFootIQ = iq;
        ratDur = dur;
        ratLBCov = cov;
        ratLBRsh = rsh;
        ratLBTkl = tkl;
        //ratCBSpd = spd;
        isRedshirt = rs;
        if (isRedshirt) year = 0;
        position = "LB";

        cost = (int)(Math.pow((float)ratOvr - 55,2)/5) + 50 + (int)(Math.random()*100) - 50;

        ratingsVector = new Vector();
        ratingsVector.addElement(name+" ("+getYrStr()+")");
        ratingsVector.addElement(ratOvr+" (+"+ratImprovement+")");
        ratingsVector.addElement(ratPot);
        ratingsVector.addElement(ratFootIQ);
        ratingsVector.addElement(ratLBCov);
        ratingsVector.addElement(ratLBRsh);
        ratingsVector.addElement(ratLBTkl);
        //ratingsVector.addElement(ratCBSpd);

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

    public PlayerLB( String nm, Team t, int yr, int pot, int iq, int cov, int rsh, int tkl/*, int spd*/, boolean rs, int dur,
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
        ratLBCov = cov;
        ratLBRsh = rsh;
        ratLBTkl = tkl;
        //ratCBSpd = spd;
        isRedshirt = rs;
        if (isRedshirt) year = 0;
        position = "LB";

        cost = (int)(Math.pow((float)ratOvr - 55,2)/6) + 50 + (int)(Math.random()*100) - 50;

        ratingsVector = new Vector();
        ratingsVector.addElement(name+" ("+getYrStr()+")");
        ratingsVector.addElement(ratOvr+" (+"+ratImprovement+")");
        ratingsVector.addElement(ratPot);
        ratingsVector.addElement(ratFootIQ);
        ratingsVector.addElement(ratLBCov);
        ratingsVector.addElement(ratLBRsh);
        ratingsVector.addElement(ratLBTkl);
        //ratingsVector.addElement(ratCBSpd);

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
        ratLBCov = (int) (60 + year*5 + stars*5 - 25*Math.random());
        ratLBRsh = (int) (60 + year*5 + stars*5 - 25*Math.random());
        ratLBTkl = (int) (60 + year*5 + stars*5 - 25*Math.random());
        //ratCBSpd = (int) (60 + year*5 + stars*5 - 25*Math.random());
        ratOvr = (ratLBCov + ratLBRsh*2 + ratLBTkl*2)/5;
        position = "LB";

        cost = (int)(Math.pow((float)ratOvr - 55,2)/6) + 50 + (int)(Math.random()*100) - 50;

        ratingsVector = new Vector();
        ratingsVector.addElement(name+" ("+getYrStr()+")");
        ratingsVector.addElement(ratOvr+" (+"+ratImprovement+")");
        ratingsVector.addElement(ratPot);
        ratingsVector.addElement(ratFootIQ);
        ratingsVector.addElement(ratLBCov);
        ratingsVector.addElement(ratLBRsh);
        ratingsVector.addElement(ratLBTkl);
        //ratingsVector.addElement(ratCBSpd);


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
        return statsTackles*35 + statsSacks*250 + statsFumbles*500 + statsInts*500 + 12*ratOvr - (int)(4*team.teamOppYards) - (int)(5*team.teamOppPoints);
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
        ratingsVector.addElement(ratLBCov);
        ratingsVector.addElement(ratLBRsh);
        ratingsVector.addElement(ratLBTkl);
        //ratingsVector.addElement(ratCBSpd);
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
        ratLBCov += (int)(Math.random()*(ratPot + gamesPlayed - 35))/10;
        ratLBRsh += (int)(Math.random()*(ratPot + gamesPlayed - 35))/10;
        ratLBTkl += (int)(Math.random()*(ratPot + gamesPlayed - 35))/10;
        //ratCBSpd += (int)(Math.random()*(ratPot + gamesPlayed - 35))/10;
        if ( Math.random()*100 < ratPot ) {
            //breakthrough
            ratLBCov += (int)(Math.random()*(ratPot + gamesPlayed - 40))/10;
            ratLBRsh += (int)(Math.random()*(ratPot + gamesPlayed - 40))/10;
            ratLBTkl += (int)(Math.random()*(ratPot + gamesPlayed - 40))/10;
            //ratCBSpd += (int)(Math.random()*(ratPot + gamesPlayed - 40))/10;
        }
        ratOvr = (ratLBCov + ratLBRsh*2 + ratLBTkl*2)/5;
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
        pStats.add("Football IQ: " + getLetterGrade(ratFootIQ) + ">Coverage: " + getLetterGrade(ratLBCov));
        pStats.add("Run Stop: " + getLetterGrade(ratLBRsh) + ">Tackling: " + getLetterGrade(ratLBTkl));
        pStats.add(" > ");
        return pStats;
    }

    @Override
    public ArrayList<String> getDetailAllStatsList(int games) {
        ArrayList<String> pStats = new ArrayList<>();
        pStats.add("Tackles: " + (statsTackles) + " >Sacks: " + (statsSacks));
        pStats.add("Fumbles: " + (statsFumbles) + " >Interceptions: " + (statsInts));
        pStats.add("Games: " + gamesPlayed + " (" + statsWins + "-" + (gamesPlayed-statsWins) + ")" + ">Durability: " + getLetterGrade(ratDur));
        pStats.add("Football IQ: " + getLetterGrade(ratFootIQ) + ">Coverage: " + getLetterGrade(ratLBCov));
        pStats.add("Run Stop: " + getLetterGrade(ratLBRsh) + ">Tackling: " + getLetterGrade(ratLBTkl));
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
                getLetterGrade(ratLBCov) + ", " + getLetterGrade(ratLBRsh) + ", " + getLetterGrade(ratLBTkl) + ")";
    }

}
