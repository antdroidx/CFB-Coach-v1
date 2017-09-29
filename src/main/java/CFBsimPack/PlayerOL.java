package CFBsimPack;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Class for the OL player. 5 on field at a time.
 * @author Achi
 */
public class PlayerOL extends Player {
    
    //public String name;
    //Overall rating, combination of other ratings
    //public int ratOvr;
    //Potential, affects how much he gets better in offseason
    //public int ratPot;
    //FootIQ, affects how smart he plays
    //public int ratFootIQ;
    //OLPow affects how strong he is against defending DL
    public int ratOLPow;
    //OLBkR affects how well he blocks for running plays
    public int ratOLBkR;
    //OLBkP affects how well he blocks for passing plays
    public int ratOLBkP;
    
    //public Vector ratingsVector;
    
    public PlayerOL( String nm, Team t, int yr, int pot, int iq, int pow, int bkr, int bkp, boolean rs, int dur ) {
        team = t;
        name = nm;
        year = yr;
        gamesPlayed = 0;
        isInjured = false;
        ratOvr = (pow*3 + bkr + bkp)/5;
        ratPot = pot;
        ratFootIQ = iq;
        ratDur = dur;
        ratOLPow = pow;
        ratOLBkR = bkr;
        ratOLBkP = bkp;
        isRedshirt = rs;
        if (isRedshirt) year = 0;
        position = "OL";

        cost = (int)(Math.pow((float)ratOvr - 55,2)/4) + 50 + (int)(Math.random()*100) - 50;
        
        ratingsVector = new Vector();
        ratingsVector.addElement(name+" ("+getYrStr()+")");
        ratingsVector.addElement(ratOvr+" (+"+ratImprovement+")");
        ratingsVector.addElement(ratPot);
        ratingsVector.addElement(ratFootIQ);
        ratingsVector.addElement(ratOLPow);
        ratingsVector.addElement(ratOLBkR);
        ratingsVector.addElement(ratOLBkP);

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

    public PlayerOL( String nm, Team t, int yr, int pot, int iq, int pow, int bkr, int bkp, boolean rs, int dur,
                     int cGamesPlayed, int cHeismans, int cAA, int cAC, int cWins ) {
        team = t;
        name = nm;
        year = yr;
        gamesPlayed = 0;
        isInjured = false;
        ratOvr = (pow*3 + bkr + bkp)/5;
        ratPot = pot;
        ratFootIQ = iq;
        ratDur = dur;
        ratOLPow = pow;
        ratOLBkR = bkr;
        ratOLBkP = bkp;
        isRedshirt = rs;
        if (isRedshirt) year = 0;
        position = "OL";

        cost = (int)(Math.pow((float)ratOvr - 55,2)/4) + 50 + (int)(Math.random()*100) - 50;

        ratingsVector = new Vector();
        ratingsVector.addElement(name+" ("+getYrStr()+")");
        ratingsVector.addElement(ratOvr+" (+"+ratImprovement+")");
        ratingsVector.addElement(ratPot);
        ratingsVector.addElement(ratFootIQ);
        ratingsVector.addElement(ratOLPow);
        ratingsVector.addElement(ratOLBkR);
        ratingsVector.addElement(ratOLBkP);

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
    
    public PlayerOL( String nm, int yr, int stars, Team t ) {
        name = nm;
        year = yr;
        team = t;
        gamesPlayed = 0;
        isInjured = false;
        ratPot = (int) (50 + 50*Math.random());
        ratFootIQ = (int) (50 + 50*Math.random());
        ratDur = (int) (50 + 50*Math.random());
        ratOLPow = (int) (60 + year*5 + stars*5 - 25*Math.random());
        ratOLBkR = (int) (60 + year*5 + stars*5 - 25*Math.random());
        ratOLBkP = (int) (60 + year*5 + stars*5 - 25*Math.random());
        ratOvr = (ratOLPow*3 + ratOLBkR + ratOLBkP)/5;
        position = "OL";

        cost = (int)(Math.pow((float)ratOvr - 55,2)/4) + 50 + (int)(Math.random()*100) - 50;
        
        ratingsVector = new Vector();
        ratingsVector.addElement(name+" ("+getYrStr()+")");
        ratingsVector.addElement(ratOvr+" (+"+ratImprovement+")");
        ratingsVector.addElement(ratPot);
        ratingsVector.addElement(ratFootIQ);
        ratingsVector.addElement(ratOLPow);
        ratingsVector.addElement(ratOLBkR);
        ratingsVector.addElement(ratOLBkP);

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
    
    public Vector getRatingsVector() {
        ratingsVector = new Vector();
        ratingsVector.addElement(name+" ("+getYrStr()+")");
        ratingsVector.addElement(ratOvr+" (+"+ratImprovement+")");
        ratingsVector.addElement(ratPot);
        ratingsVector.addElement(ratFootIQ);
        ratingsVector.addElement(ratOLPow);
        ratingsVector.addElement(ratOLBkR);
        ratingsVector.addElement(ratOLBkP);
        return ratingsVector;
    }
    
    @Override
    public void advanceSeason() {
        year++;
        int oldOvr = ratOvr;
        ratFootIQ += (int)(Math.random()*(ratPot + gamesPlayed - 35))/10;
        ratOLPow += (int)(Math.random()*(ratPot + gamesPlayed - 35))/10;
        ratOLBkR += (int)(Math.random()*(ratPot + gamesPlayed - 35))/10;
        ratOLBkP += (int)(Math.random()*(ratPot + gamesPlayed - 35))/10;
        if ( Math.random()*100 < ratPot ) {
            //breakthrough
            ratOLPow += (int)(Math.random()*(ratPot + gamesPlayed - 40))/10;
            ratOLBkR += (int)(Math.random()*(ratPot + gamesPlayed - 40))/10;
            ratOLBkP += (int)(Math.random()*(ratPot + gamesPlayed - 40))/10;
        }
        ratOvr = (ratOLPow*3 + ratOLBkR + ratOLBkP)/5;
        ratImprovement = ratOvr - oldOvr;

        careerGamesPlayed += gamesPlayed;
        careerWins += statsWins;

        if (wonHeisman) careerHeismans++;
        if (wonAllAmerican) careerAllAmerican++;
        if (wonAllConference) careerAllConference++;
    }

    @Override
    public ArrayList<String> getDetailStatsList(int games) {
        ArrayList<String> pStats = new ArrayList<>();
        pStats.add("Games: " + gamesPlayed + " (" + statsWins + "-" + (gamesPlayed-statsWins) + ")" + ">Durability: " + getLetterGrade(ratDur));
        pStats.add("Football IQ: " + getLetterGrade(ratFootIQ) + ">Strength: " + getLetterGrade(ratOLPow));
        pStats.add("Run Block: " + getLetterGrade(ratOLBkR) + ">Pass Block: " + getLetterGrade(ratOLBkP));
        pStats.add(" > ");
        return pStats;
    }

    @Override
    public ArrayList<String> getDetailAllStatsList(int games) {
        ArrayList<String> pStats = new ArrayList<>();
        pStats.add("Games: " + gamesPlayed + " (" + statsWins + "-" + (gamesPlayed-statsWins) + ")" + ">Durability: " + getLetterGrade(ratDur));
        pStats.add("Football IQ: " + getLetterGrade(ratFootIQ) + ">Strength: " + getLetterGrade(ratOLPow));
        pStats.add("Run Block: " + getLetterGrade(ratOLBkR) + ">Pass Block: " + getLetterGrade(ratOLBkP));
        pStats.add("[B]CAREER STATS:");
        pStats.addAll(getCareerStatsList());
        return pStats;
    }

    @Override
    public String getInfoForLineup() {
        if (injury != null) return getInitialName() + " [" + getYrStr() + "] " + ratOvr + "/" + getLetterGrade(ratPot) + " " + injury.toString();
        return getInitialName() + " [" + getYrStr() + "] " + ratOvr + "/" + getLetterGrade(ratPot) + " (" +
                getLetterGrade(ratOLPow) + ", " + getLetterGrade(ratOLBkR) + ", " + getLetterGrade(ratOLBkP) + ")";
    }
}
