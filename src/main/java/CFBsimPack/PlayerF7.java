/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CFBsimPack;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Class for the "Front Seven" of defenses. 7 on field.
 * @author Achi
 */
public class PlayerF7 extends Player {
    
    //public String name;
    //Overall rating, combination of other ratings
    //public int ratOvr;
    //Potential, affects how much he gets better in offseason
    //public int ratPot;
    //FootIQ, affects how smart he plays
    //public int ratFootIQ;
    //OLPow affects how strong he is against OL
    public int ratF7Pow;
    //OLBkR affects how well he defends running plays
    public int ratF7Rsh;
    //OLBkP affects how well he defends passing plays
    public int ratF7Pas;
    
    //public Vector ratingsVector;
    
    public PlayerF7( String nm, Team t, int yr, int pot, int iq, int pow, int rsh, int pas, boolean rs, int dur ) {
        team = t;
        name = nm;
        year = yr;
        gamesPlayed = 0;
        isInjured = false;
        ratOvr = (pow*3 + rsh + pas)/5;
        ratPot = pot;
        ratFootIQ = iq;
        ratDur = dur;
        ratF7Pow = pow;
        ratF7Rsh = rsh;
        ratF7Pas = pas;
        isRedshirt = rs;
        if (isRedshirt) year = 0;
        position = "F7";

        cost = (int)(Math.pow((float)ratOvr - 55,2)/6) + 50 + (int)(Math.random()*100) - 50;
        
        ratingsVector = new Vector();
        ratingsVector.addElement(name+" ("+getYrStr()+")");
        ratingsVector.addElement(ratOvr+" (+"+ratImprovement+")");
        ratingsVector.addElement(ratPot);
        ratingsVector.addElement(ratFootIQ);
        ratingsVector.addElement(ratF7Pow);
        ratingsVector.addElement(ratF7Rsh);
        ratingsVector.addElement(ratF7Pas);

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

    public PlayerF7( String nm, Team t, int yr, int pot, int iq, int pow, int rsh, int pas, boolean rs, int dur,
                     int cGamesPlayed, int cHeismans, int cAA, int cAC, int cWins ) {
        team = t;
        name = nm;
        year = yr;
        gamesPlayed = 0;
        isInjured = false;
        ratOvr = (pow*3 + rsh + pas)/5;
        ratPot = pot;
        ratFootIQ = iq;
        ratDur = dur;
        ratF7Pow = pow;
        ratF7Rsh = rsh;
        ratF7Pas = pas;
        isRedshirt = rs;
        if (isRedshirt) year = 0;
        position = "F7";

        cost = (int)(Math.pow((float)ratOvr - 55,2)/6) + 50 + (int)(Math.random()*100) - 50;

        ratingsVector = new Vector();
        ratingsVector.addElement(name+" ("+getYrStr()+")");
        ratingsVector.addElement(ratOvr+" (+"+ratImprovement+")");
        ratingsVector.addElement(ratPot);
        ratingsVector.addElement(ratFootIQ);
        ratingsVector.addElement(ratF7Pow);
        ratingsVector.addElement(ratF7Rsh);
        ratingsVector.addElement(ratF7Pas);

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
    
    public PlayerF7( String nm, int yr, int stars, Team t ) {
        name = nm;
        year = yr;
        team = t;
        gamesPlayed = 0;
        isInjured = false;
        ratPot = (int) (50 + 50*Math.random());
        ratFootIQ = (int) (50 + 50*Math.random());
        ratDur = (int) (50 + 50*Math.random());
        ratF7Pow = (int) (60 + year*5 + stars*5 - 25*Math.random());
        ratF7Rsh = (int) (60 + year*5 + stars*5 - 25*Math.random());
        ratF7Pas = (int) (60 + year*5 + stars*5 - 25*Math.random());
        ratOvr = (ratF7Pow*3 + ratF7Rsh + ratF7Pas)/5;
        position = "F7";

        cost = (int)(Math.pow((float)ratOvr - 55,2)/6) + 50 + (int)(Math.random()*100) - 50;
        
        ratingsVector = new Vector();
        ratingsVector.addElement(name+" ("+getYrStr()+")");
        ratingsVector.addElement(ratOvr+" (+"+ratImprovement+")");
        ratingsVector.addElement(ratPot);
        ratingsVector.addElement(ratFootIQ);
        ratingsVector.addElement(ratF7Pow);
        ratingsVector.addElement(ratF7Rsh);
        ratingsVector.addElement(ratF7Pas);

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
        ratingsVector.addElement(ratF7Pow);
        ratingsVector.addElement(ratF7Rsh);
        ratingsVector.addElement(ratF7Pas);
        return ratingsVector;
    }
    
    @Override
    public void advanceSeason() {
        year++;
        int oldOvr = ratOvr;
        ratFootIQ += (int)(Math.random()*(ratPot + gamesPlayed - 35))/10;
        ratF7Pow += (int)(Math.random()*(ratPot + gamesPlayed - 35))/10;
        ratF7Rsh += (int)(Math.random()*(ratPot + gamesPlayed - 35))/10;
        ratF7Pas += (int)(Math.random()*(ratPot + gamesPlayed - 35))/10;
        if ( Math.random()*100 < ratPot ) {
            //breakthrough
            ratF7Pow += (int)(Math.random()*(ratPot + gamesPlayed - 40))/10;
            ratF7Rsh += (int)(Math.random()*(ratPot + gamesPlayed - 40))/10;
            ratF7Pas += (int)(Math.random()*(ratPot + gamesPlayed - 40))/10;
        }
        ratOvr = (ratF7Pow*3 + ratF7Rsh + ratF7Pas)/5;
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
        pStats.add("Football IQ: " + getLetterGrade(ratFootIQ) + ">Strength: " + getLetterGrade(ratF7Pow));
        pStats.add("Run Stop: " + getLetterGrade(ratF7Rsh) + ">Pass Pressure: " + getLetterGrade(ratF7Pas));
        pStats.add(" > ");
        return pStats;
    }

    @Override
    public ArrayList<String> getDetailAllStatsList(int games) {
        ArrayList<String> pStats = new ArrayList<>();
        pStats.add("Games: " + gamesPlayed + " (" + statsWins + "-" + (gamesPlayed-statsWins) + ")" + ">Durability: " + getLetterGrade(ratDur));
        pStats.add("Football IQ: " + getLetterGrade(ratFootIQ) + ">Strength: " + getLetterGrade(ratF7Pow));
        pStats.add("Run Stop: " + getLetterGrade(ratF7Rsh) + ">Pass Pressure: " + getLetterGrade(ratF7Pas));
        pStats.add("[B]CAREER STATS:");
        pStats.addAll(getCareerStatsList());
        return pStats;
    }

    @Override
    public String getInfoForLineup() {
        if (injury != null) return getInitialName() + " [" + getYrStr() + "] " + ratOvr + "/" + getLetterGrade(ratPot) + " " + injury.toString();
        return getInitialName() + " [" + getYrStr() + "] " + ratOvr + "/" + getLetterGrade(ratPot) + " (" +
                getLetterGrade(ratF7Pow) + ", " + getLetterGrade(ratF7Rsh) + ", " + getLetterGrade(ratF7Pas) + ")";
    }
    
}
