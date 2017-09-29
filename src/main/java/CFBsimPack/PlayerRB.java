package CFBsimPack;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Class for the RB player.
 * @author Achi
 */
public class PlayerRB extends Player {
    
    //public String name;
    //Overall rating, combination of other ratings
    //public int ratOvr;
    //Potential, affects how much he gets better in offseason
    //public int ratPot;
    //FootIQ, affects how smart he plays
    //public int ratFootIQ;
    //RushPow affects how consistenly he can get past line of scrimmage
    public int ratRushPow;
    //RushSpd affects how long he can run
    public int ratRushSpd;
    //RushEva affects how easily he can dodge tackles
    public int ratRushEva;
    
    //public Vector ratingsVector;
    
    //Stats
    public int statsRushAtt;
    public int statsRushYards;
    public int statsTD;
    public int statsFumbles;

    public int careerRushAtt;
    public int careerRushYards;
    public int careerTDs;
    public int careerFumbles;


    public PlayerRB( String nm, Team t, int yr, int pot, int iq, int pow, int spd, int eva, boolean rs, int dur ) {
        team = t;
        name = nm;
        year = yr;
        gamesPlayed = 0;
        isInjured = false;
        ratOvr = (pow + spd + eva)/3;
        ratPot = pot;
        ratFootIQ = iq;
        ratDur = dur;
        ratRushPow = pow;
        ratRushSpd = spd;
        ratRushEva = eva;
        isRedshirt = rs;
        if (isRedshirt) year = 0;

        cost = (int)(Math.pow((float)ratOvr - 55,2)/2) + 70 + (int)(Math.random()*100) - 50;

        ratingsVector = new Vector();
        ratingsVector.addElement(name+" ("+getYrStr()+")");
        ratingsVector.addElement(ratOvr+" (+"+ratImprovement+")");
        ratingsVector.addElement(ratPot);
        ratingsVector.addElement(ratFootIQ);
        ratingsVector.addElement(ratRushPow);
        ratingsVector.addElement(ratRushSpd);
        ratingsVector.addElement(ratRushEva);

        statsRushAtt = 0;
        statsRushYards = 0;
        statsTD = 0;
        statsFumbles = 0;
        wonHeisman = false;
        wonAllAmerican = false;
        wonAllConference = false;
        statsWins = 0;

        careerRushAtt = 0;
        careerRushYards = 0;
        careerTDs = 0;
        careerFumbles = 0;
        careerGamesPlayed = 0;
        careerHeismans = 0;
        careerAllAmerican = 0;
        careerAllConference = 0;
        careerWins = 0;

        position = "RB";
    }

    public PlayerRB( String nm, Team t, int yr, int pot, int iq, int pow, int spd, int eva, boolean rs, int dur,
                     int cGamesPlayed, int cRushAtt, int cRushYards, int cTDs, int cFumbles,
                     int cHeismans, int cAA, int cAC, int cWins) {
        team = t;
        name = nm;
        year = yr;
        gamesPlayed = 0;
        isInjured = false;
        ratOvr = (pow + spd + eva)/3;
        ratPot = pot;
        ratFootIQ = iq;
        ratDur = dur;
        ratRushPow = pow;
        ratRushSpd = spd;
        ratRushEva = eva;
        isRedshirt = rs;
        if (isRedshirt) year = 0;

        cost = (int)(Math.pow((float)ratOvr - 55,2)/2) + 70 + (int)(Math.random()*100) - 50;
        
        ratingsVector = new Vector();
        ratingsVector.addElement(name+" ("+getYrStr()+")");
        ratingsVector.addElement(ratOvr+" (+"+ratImprovement+")");
        ratingsVector.addElement(ratPot);
        ratingsVector.addElement(ratFootIQ);
        ratingsVector.addElement(ratRushPow);
        ratingsVector.addElement(ratRushSpd);
        ratingsVector.addElement(ratRushEva);
        
        statsRushAtt = 0;
        statsRushYards = 0;
        statsTD = 0;
        statsFumbles = 0;
        wonHeisman = false;
        wonAllAmerican = false;
        wonAllConference = false;
        statsWins = 0;

        careerRushAtt = cRushAtt;
        careerRushYards = cRushYards;
        careerTDs = cTDs;
        careerFumbles = cFumbles;
        careerGamesPlayed = cGamesPlayed;
        careerHeismans = cHeismans;
        careerAllAmerican = cAA;
        careerAllConference = cAC;
        careerWins = cWins;

        position = "RB";
    }
    
    public PlayerRB( String nm, int yr, int stars, Team t ) {
        name = nm;
        year = yr;
        team = t;
        gamesPlayed = 0;
        isInjured = false;
        ratPot = (int) (50 + 50*Math.random());
        ratFootIQ = (int) (50 + 50*Math.random());
        ratDur = (int) (50 + 50*Math.random());
        ratRushPow = (int) (60 + year*5 + stars*5 - 25*Math.random());
        ratRushSpd = (int) (60 + year*5 + stars*5 - 25*Math.random());
        ratRushEva = (int) (60 + year*5 + stars*5 - 25*Math.random());
        ratOvr = (ratRushPow + ratRushSpd + ratRushEva)/3;

        cost = (int)(Math.pow((float)ratOvr - 55,2)/2) + 70 + (int)(Math.random()*100) - 50;
        
        ratingsVector = new Vector();
        ratingsVector.addElement(name+" ("+getYrStr()+")");
        ratingsVector.addElement(ratOvr+" (+"+ratImprovement+")");
        ratingsVector.addElement(ratPot);
        ratingsVector.addElement(ratFootIQ);
        ratingsVector.addElement(ratRushPow);
        ratingsVector.addElement(ratRushSpd);
        ratingsVector.addElement(ratRushEva);

        statsRushAtt = 0;
        statsRushYards = 0;
        statsTD = 0;
        statsFumbles = 0;
        wonHeisman = false;
        wonAllAmerican = false;
        wonAllConference = false;
        statsWins = 0;

        careerRushAtt = 0;
        careerRushYards = 0;
        careerTDs = 0;
        careerFumbles = 0;
        careerGamesPlayed = 0;
        careerHeismans = 0;
        careerAllAmerican = 0;
        careerAllConference = 0;
        careerWins = 0;

        position = "RB";
    }
    
    public Vector getStatsVector() {
        Vector v = new Vector(5);
        v.add(statsRushAtt);
        v.add(statsRushYards);
        v.add(statsTD);
        v.add(statsFumbles);
        v.add((float)((int)((float)statsRushYards/statsRushAtt*100))/100);
        return v;
    }
    
    public Vector getRatingsVector() {
        ratingsVector = new Vector();
        ratingsVector.addElement(name+" ("+getYrStr()+")");
        ratingsVector.addElement(ratOvr+" (+"+ratImprovement+")");
        ratingsVector.addElement(ratPot);
        ratingsVector.addElement(ratFootIQ);
        ratingsVector.addElement(ratRushPow);
        ratingsVector.addElement(ratRushSpd);
        ratingsVector.addElement(ratRushEva);
        return ratingsVector;
    }
    
    @Override
    public void advanceSeason() {
        year++;
        int oldOvr = ratOvr;
        ratFootIQ += (int)(Math.random()*(ratPot + gamesPlayed - 35))/10;
        ratRushPow += (int)(Math.random()*(ratPot + gamesPlayed - 35))/10;
        ratRushSpd += (int)(Math.random()*(ratPot + gamesPlayed - 35))/10;
        ratRushEva += (int)(Math.random()*(ratPot + gamesPlayed - 35))/10;
        if ( Math.random()*100 < ratPot ) {
            //breakthrough
            ratRushPow += (int)(Math.random()*(ratPot + gamesPlayed - 40))/10;
            ratRushSpd += (int)(Math.random()*(ratPot + gamesPlayed - 40))/10;
            ratRushEva += (int)(Math.random()*(ratPot + gamesPlayed - 40))/10;
        }
        ratOvr = (ratRushPow + ratRushSpd + ratRushEva)/3;
        ratImprovement = ratOvr - oldOvr;
        //reset stats (keep career stats?)
        careerRushAtt += statsRushAtt;
        careerRushYards += statsRushYards;
        careerTDs += statsTD;
        careerFumbles += statsFumbles;
        careerGamesPlayed += gamesPlayed;
        careerWins += statsWins;

        if (wonHeisman) careerHeismans++;
        if (wonAllAmerican) careerAllAmerican++;
        if (wonAllConference) careerAllConference++;

        statsRushAtt = 0;
        statsRushYards = 0;
        statsTD = 0;
        statsFumbles = 0;
    }
    
    @Override
    public int getHeismanScore() {
        return statsTD * 100 - statsFumbles * 80 + (int)(statsRushYards * 2.35);
    }

    @Override
    public ArrayList<String> getDetailStatsList(int games) {
        ArrayList<String> pStats = new ArrayList<>();
        pStats.add("TDs: " + statsTD + ">Fumbles: " + statsFumbles);
        pStats.add("Rush Yards: " + statsRushYards + " yds>Yards/Att: " + ((double)(10*statsRushYards/(statsRushAtt+1))/10) + " yds");
        pStats.add("Yds/Game: " + (statsRushYards/getGamesPlayed()) + " yds/g>Rush Att: " + statsRushAtt);
        pStats.add("Games: " + gamesPlayed + " (" + statsWins + "-" + (gamesPlayed-statsWins) + ")" + ">Durability: " + getLetterGrade(ratDur));
        pStats.add("Football IQ: " + getLetterGrade(ratFootIQ) + ">Rush Power: " + getLetterGrade(ratRushPow));
        pStats.add("Rush Speed: " + getLetterGrade(ratRushSpd) + ">Evasion: " + getLetterGrade(ratRushEva));
        pStats.add(" > ");
        return pStats;
    }

    @Override
    public ArrayList<String> getDetailAllStatsList(int games) {
        ArrayList<String> pStats = new ArrayList<>();
        pStats.add("TDs: " + statsTD + ">Fumbles: " + statsFumbles);
        pStats.add("Rush Yards: " + statsRushYards + " yds>Yards/Att: " + ((double)(10*statsRushYards/(statsRushAtt+1))/10) + " yds");
        pStats.add("Yds/Game: " + (statsRushYards/getGamesPlayed()) + " yds/g>Rush Att: " + statsRushAtt);
        pStats.add("Games: " + gamesPlayed + " (" + statsWins + "-" + (gamesPlayed-statsWins) + ")" + ">Durability: " + getLetterGrade(ratDur));
        pStats.add("Football IQ: " + getLetterGrade(ratFootIQ) + ">Rush Power: " + getLetterGrade(ratRushPow));
        pStats.add("Rush Speed: " + getLetterGrade(ratRushSpd) + ">Evasion: " + getLetterGrade(ratRushEva));
        pStats.add("[B]CAREER STATS:");
        pStats.addAll(getCareerStatsList());
        return pStats;
    }

    @Override
    public ArrayList<String> getCareerStatsList() {
        ArrayList<String> pStats = new ArrayList<>();
        pStats.add("TDs: " + (statsTD+careerTDs) + ">Fumbles: " + (statsFumbles+careerFumbles));
        pStats.add("Rush Yards: " + (statsRushYards+careerRushYards) + " yds>Yards/Att: " + ((double)(10*(statsRushYards+careerRushYards)/(statsRushAtt+careerRushAtt+1))/10) + " yds");
        pStats.add("Yds/Game: " + ((statsRushYards+careerRushYards)/(getGamesPlayed()+careerGamesPlayed)) + " yds/g>Rush Att: " + (statsRushAtt+careerRushAtt));
        pStats.addAll(super.getCareerStatsList());
        return pStats;
    }

    @Override
    public String getInfoForLineup() {
        if (injury != null) return getInitialName() + " [" + getYrStr() + "] " + ratOvr + "/" + getLetterGrade(ratPot) + " " + injury.toString();
        return getInitialName() + " [" + getYrStr() + "] " + ratOvr + "/" + getLetterGrade(ratPot) + " (" +
                getLetterGrade(ratRushPow) + ", " + getLetterGrade(ratRushSpd) + ", " + getLetterGrade(ratRushEva) + ")";
    }
    
}
