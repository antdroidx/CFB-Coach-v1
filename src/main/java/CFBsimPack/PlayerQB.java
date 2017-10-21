package CFBsimPack;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Class for QB player.
 * @author Achi
 */
public class PlayerQB extends Player {
    
    //public String name;
    //Overall rating, combination of other ratings
    //public int ratOvr;
    //Potential, affects how much he gets better in offseason
    //public int ratPot;
    //FootIQ, affects how smart he plays
    //public int ratFootIQ;
    //PassPow affects how far he can throw
    public int ratPassPow;
    //PassAcc affects how accurate his passes are
    public int ratPassAcc;
    //PassEva (evasiveness) affects how easily he can dodge sacks
    public int ratEvasion;
    public int ratSpeed;

    //Stats
    public int statsPassAtt;
    public int statsPassComp;
    public int statsPassTD;
    public int statsInt;
    public int statsPassYards;
    public int statsSacked;
    public int statsRushAtt;
    public int statsRushYards;
    public int statsRushTD;
    public int statsFumbles;

    public int careerPassAtt;
    public int careerPassComp;
    public int careerTDs;
    public int careerInt;
    public int careerPassYards;
    public int careerSacked;
    public int careerRushAtt;
    public int careerRushYards;
    public int careerRushTD;
    public int careerFumbles;



    public PlayerQB( String nm, Team t, int yr, int pot, int iq, int pow, int acc, int eva, boolean rs, int dur, int spd ) {
        team = t;
        name = nm;
        year = yr;
        gamesPlayed = 0;
        isInjured = false;
        ratOvr = (pow*3 + acc*4 + eva + spd)/9;
        ratPot = pot;
        ratFootIQ = iq;
        ratDur = dur;
        ratPassPow = pow;
        ratPassAcc = acc;
        ratEvasion = eva;
        ratSpeed = spd;
        isRedshirt = rs;
        if (isRedshirt) year = 0;

        cost = (int)(Math.pow((float)ratOvr - 55,2)/1.5) + 150 + (int)(Math.random()*100) - 50;

        ratingsVector = new Vector();
        ratingsVector.addElement(name+" ("+getYrStr()+")");
        ratingsVector.addElement(ratOvr+" (+"+ratImprovement+")");
        ratingsVector.addElement(ratPot);
        ratingsVector.addElement(ratFootIQ);
        ratingsVector.addElement(ratPassPow);
        ratingsVector.addElement(ratPassAcc);
        ratingsVector.addElement(ratEvasion);
        ratingsVector.addElement(ratSpeed);


        statsPassAtt = 0;
        statsPassComp = 0;
        statsPassTD = 0;
        statsInt = 0;
        statsPassYards = 0;
        statsSacked = 0;
        statsRushAtt= 0;
        statsRushYards= 0;
        statsRushTD= 0;
        statsFumbles = 0;
        wonHeisman = false;
        wonAllAmerican = false;
        wonAllConference = false;
        statsWins = 0;

        careerPassAtt = 0;
        careerPassComp = 0;
        careerTDs = 0;
        careerInt = 0;
        careerPassYards = 0;
        careerSacked = 0;
        careerRushAtt= 0;
        careerRushYards= 0;
        careerRushTD= 0;
        careerGamesPlayed = 0;
        careerHeismans = 0;
        careerAllAmerican = 0;
        careerAllConference = 0;
        careerWins = 0;

        position = "QB";
    }

    public PlayerQB( String nm, Team t, int yr, int pot, int iq, int pow, int acc, int eva, boolean rs, int dur, int spd,
                     int cGamesPlayed, int cPassAtt, int cPassComp, int cTDs, int cInt, int cPassYards, int cSacked,
                     int cRushAtt, int cRushYards, int cRushTD, int cFumbles, int cHeismans, int cAA, int cAC, int cWins) {
        team = t;
        name = nm;
        year = yr;
        gamesPlayed = 0;
        isInjured = false;
        ratOvr = (pow*3 + acc*4 + eva + spd)/9;
        ratPot = pot;
        ratFootIQ = iq;
        ratDur = dur;
        ratPassPow = pow;
        ratPassAcc = acc;
        ratEvasion = eva;
        ratSpeed = spd;
        isRedshirt = rs;
        if (isRedshirt) year = 0;

        cost = (int)(Math.pow((float)ratOvr - 55,2)/1.5) + 150 + (int)(Math.random()*100) - 50;

        ratingsVector = new Vector();
        ratingsVector.addElement(name+" ("+getYrStr()+")");
        ratingsVector.addElement(ratOvr+" (+"+ratImprovement+")");
        ratingsVector.addElement(ratPot);
        ratingsVector.addElement(ratFootIQ);
        ratingsVector.addElement(ratPassPow);
        ratingsVector.addElement(ratPassAcc);
        ratingsVector.addElement(ratEvasion);
        ratingsVector.addElement(ratSpeed);

        statsPassAtt = 0;
        statsPassComp = 0;
        statsPassTD = 0;
        statsInt = 0;
        statsPassYards = 0;
        statsSacked = 0;
        statsRushAtt= 0;
        statsRushYards= 0;
        statsRushTD= 0;
        statsFumbles = 0;

        wonHeisman = false;
        wonAllAmerican = false;
        wonAllConference = false;
        statsWins = 0;

        careerPassAtt = cPassAtt;
        careerPassComp = cPassComp;
        careerTDs = cTDs;
        careerInt = cInt;
        careerPassYards = cPassYards;
        careerSacked = cSacked;
        careerRushAtt= cRushAtt;
        careerRushYards= cRushYards;
        careerRushTD= cRushTD;
        careerFumbles = cFumbles;

        careerGamesPlayed = cGamesPlayed;
        careerHeismans = cHeismans;
        careerAllAmerican = cAA;
        careerAllConference = cAC;
        careerWins = cWins;

        position = "QB";
    }

    public PlayerQB( String nm, int yr, int stars, Team t ) {
        name = nm;
        year = yr;
        team = t;
        gamesPlayed = 0;
        isInjured = false;
        ratPot = (int) (50 + 50*Math.random());
        ratFootIQ = (int) (50 + 50*Math.random());
        ratDur = (int) (50 + 50*Math.random());
        ratPassPow = (int) (60 + year*5 + stars*5 - 25*Math.random());
        ratPassAcc = (int) (60 + year*5 + stars*5 - 25*Math.random());
        ratEvasion = (int) (60 + year*5 + stars*5 - 25*Math.random());
        ratSpeed = (int) (60 + year*5 + stars*5 - 25*Math.random());
        ratOvr = (ratPassPow*3 + ratPassAcc*4 + ratEvasion + ratSpeed)/9;

        cost = (int)(Math.pow((float)ratOvr - 55,2)/1.5) + 150 + (int)(Math.random()*100) - 50;

        ratingsVector = new Vector();
        ratingsVector.addElement(name+" ("+getYrStr()+")");
        ratingsVector.addElement(ratOvr+" (+"+ratImprovement+")");
        ratingsVector.addElement(ratPot);
        ratingsVector.addElement(ratFootIQ);
        ratingsVector.addElement(ratPassPow);
        ratingsVector.addElement(ratPassAcc);
        ratingsVector.addElement(ratEvasion);
        ratingsVector.addElement(ratSpeed);

        statsPassAtt = 0;
        statsPassComp = 0;
        statsPassTD = 0;
        statsInt = 0;
        statsPassYards = 0;
        statsSacked = 0;
        statsRushAtt= 0;
        statsRushYards= 0;
        statsRushTD= 0;
        statsFumbles = 0;

        wonHeisman = false;
        wonAllAmerican = false;
        wonAllConference = false;
        statsWins = 0;

        careerPassAtt = 0;
        careerPassComp = 0;
        careerTDs = 0;
        careerInt = 0;
        careerPassYards = 0;
        careerSacked = 0;
        careerRushAtt= 0;
        careerRushYards= 0;
        careerRushTD= 0;
        careerFumbles = 0;
        careerGamesPlayed = 0;
        careerHeismans = 0;
        careerAllAmerican = 0;
        careerAllConference = 0;
        careerWins = 0;

        position = "QB";
    }

    public Vector getStatsVector() {
        Vector v = new Vector(13);
        v.add(statsPassComp);
        v.add(statsPassAtt);
        v.add((float)((int)((float)statsPassComp/statsPassAtt*1000))/10);
        v.add(statsPassTD);
        v.add(statsInt);
        v.add(statsPassYards);
        v.add(statsPassYards);
        v.add((float)((int)((float)statsPassYards/statsPassAtt*100))/100);
        v.add(statsSacked);
        v.add(statsRushAtt);
        v.add(statsRushYards);
        v.add(statsRushTD);
        v.add(statsFumbles);
        return v;
    }

    public Vector getRatingsVector() {
        ratingsVector = new Vector();
        ratingsVector.addElement(name + " (" + getYrStr() + ")");
        ratingsVector.addElement(ratOvr + " (+" + ratImprovement + ")");
        ratingsVector.addElement(ratPot);
        ratingsVector.addElement(ratFootIQ);
        ratingsVector.addElement(ratPassPow);
        ratingsVector.addElement(ratPassAcc);
        ratingsVector.addElement(ratEvasion);
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

        ratFootIQ += (int)(Math.random()*(ratPot + gamesPlayed - 35))/10;
        ratPassPow += (int)(Math.random()*(ratPot + gamesPlayed - 35))/10;
        ratPassAcc += (int)(Math.random()*(ratPot + gamesPlayed - 35))/10;
        ratEvasion += (int)(Math.random()*(ratPot + gamesPlayed - 35))/10;
        ratSpeed += (int)(Math.random()*(ratPot + gamesPlayed - 35))/10;

        if ( Math.random()*100 < ratPot ) {
            //breakthrough
            ratPassPow += (int)(Math.random()*(ratPot + gamesPlayed - 40))/10;
            ratPassAcc += (int)(Math.random()*(ratPot + gamesPlayed - 40))/10;
            ratEvasion += (int)(Math.random()*(ratPot + gamesPlayed - 40))/10;
            ratSpeed += (int)(Math.random()*(ratPot + gamesPlayed - 40))/10;
        }
        ratOvr = (ratPassPow*3 + ratPassAcc*4 + ratEvasion + ratSpeed)/9;
        ratImprovement = ratOvr - oldOvr;
        //reset stats (keep career stats?)
        careerPassAtt += statsPassAtt;
        careerPassComp += statsPassComp;
        careerTDs += statsPassTD;
        careerInt += statsInt;
        careerPassYards += statsPassYards;
        careerSacked += statsSacked;
        careerRushAtt += statsRushAtt;
        careerRushYards += statsRushYards;
        careerRushTD += statsRushTD;
        careerFumbles += statsFumbles;
        careerGamesPlayed += gamesPlayed;
        careerWins += statsWins;

        if (wonHeisman) careerHeismans++;
        if (wonAllAmerican) careerAllAmerican++;
        if (wonAllConference) careerAllConference++;

        statsPassAtt = 0;
        statsPassComp = 0;
        statsPassTD = 0;
        statsInt = 0;
        statsPassYards = 0;
        statsSacked = 0;
        statsRushAtt= 0;
        statsRushYards = 0;
        statsRushTD= 0;
        statsFumbles = 0;
    }

    @Override
    public int getHeismanScore() {
        return statsPassTD * 142 - statsInt*250 + statsPassYards + statsRushTD*142 + statsRushYards + team.confPrestige*7;
    }

    public int getPasserRating() {
        int rating = (int)(((8.4*statsPassYards)+(300*statsPassTD)+(100*statsPassComp)-(200*statsInt))/statsPassAtt+1);
    return rating;
    }

    public int getPassPCT() {
        int rating = (int)(100*statsPassComp/(statsPassAtt+1));
        return rating;
    }

    @Override
    public ArrayList<String> getDetailStatsList(int games) {
        ArrayList<String> pStats = new ArrayList<>();
        pStats.add("Passer Rating " + getPasserRating() + ">Comp Percent: " + (100*statsPassComp/(statsPassAtt+1))+"%");
        pStats.add("Touchdowns: " + statsPassTD + ">Interceptions: " + statsInt);
        pStats.add("Pass Yards: " + statsPassYards + " yds>Yards/Att: " + ((double)(10*statsPassYards/(statsPassAtt+1))/10) + " yds");
        pStats.add("Yds/Game: " + (statsPassYards/getGamesPlayed()) + " yds/g>Sacks: " + statsSacked);
        pStats.add("Rush Yards: " + (statsRushYards) + ">Rush TDs: " + statsRushTD);
        pStats.add("Fumbles: " + statsFumbles + "> Games: " + gamesPlayed + " (" + statsWins + "-" + (gamesPlayed-statsWins) + ")");
        pStats.add("Durability: " + getLetterGrade(ratDur) + ">Football IQ: " + getLetterGrade(ratFootIQ));
        pStats.add("Pass Strength: " + getLetterGrade(ratPassPow) + ">Accuracy: " + getLetterGrade(ratPassAcc));
        pStats.add("Speed: " + getLetterGrade(ratSpeed)  + ">Evasion: " + getLetterGrade(ratEvasion));
        pStats.add(" > ");
        return pStats;
    }

    @Override
    public ArrayList<String> getDetailAllStatsList(int games) {
        ArrayList<String> pStats = new ArrayList<>();
        pStats.add("Passer Rating " + getPasserRating() + ">Comp Percent: " + (100*statsPassComp/(statsPassAtt+1))+"%");
        pStats.add("Touchdowns: " + statsPassTD + ">Interceptions: " + statsInt);
        pStats.add("Yds/Game: " + (statsPassYards/getGamesPlayed()) + " yds/g>Sacks: " + statsSacked);
        pStats.add("Rush Yards: " + (statsRushYards) + ">Rush TDs: " + statsRushTD);
        pStats.add("Fumbles: " + statsFumbles + "> Games: " + gamesPlayed + " (" + statsWins + "-" + (gamesPlayed-statsWins) + ")");
        pStats.add("Durability: " + getLetterGrade(ratDur) + ">Football IQ: " + getLetterGrade(ratFootIQ));
        pStats.add("Pass Strength: " + getLetterGrade(ratPassPow) + ">Accuracy: " + getLetterGrade(ratPassAcc));
        pStats.add("Speed: " + getLetterGrade(ratSpeed)  + ">Evasion: " + getLetterGrade(ratEvasion));
        pStats.add("[B]CAREER STATS:");
        pStats.addAll(getCareerStatsList());
        return pStats;
    }

    @Override
    public ArrayList<String> getCareerStatsList() {
        ArrayList<String> pStats = new ArrayList<>();
        pStats.add("Passer Rating " + (int)(((8.4*(statsPassYards+careerPassYards))+(300*(statsPassTD+careerTDs))+(100*(statsPassComp+careerPassComp))-(200*(statsInt+careerInt)))/(statsPassAtt+careerPassAtt+1)) + ">Comp Percent: " + (100*statsPassComp/(statsPassAtt+1))+"%");
        pStats.add("Touchdowns: " + statsPassTD + ">Interceptions: " + statsInt);
        pStats.add("Pass Yards: " + (statsPassYards+careerPassYards) + " yds>Yards/Att: " + ((double)(10*(statsPassYards+careerPassYards)/(statsPassAtt+careerPassAtt+1))/10) + " yds");
        pStats.add("Yds/Game: " + ((statsPassYards+careerPassYards)/(getGamesPlayed()+careerGamesPlayed)) + " yds/g>Sacks: " + (statsSacked+careerSacked));
        pStats.add("Rush Yards: " + (statsRushYards+careerRushYards) + ">Rush TDs: " + (statsRushTD+careerRushTD));
        pStats.addAll(super.getCareerStatsList());
        return pStats;
    }

    @Override
    public String getInfoForLineup() {
        if (injury != null) return getInitialName() + " [" + getYrStr() + "] " + ratOvr + "/" + getLetterGrade(ratPot) + " " + injury.toString();
        return getInitialName() + " [" + getYrStr() + "] " + ratOvr + "/" + getLetterGrade(ratPot) + " (" +
                getLetterGrade(ratPassPow) + ", " + getLetterGrade(ratPassAcc) + ", " + getLetterGrade(ratEvasion) + "," + getLetterGrade(ratSpeed) + ")";
    }
    
}
