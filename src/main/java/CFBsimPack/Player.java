package CFBsimPack;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Base player class that others extend. Has name, overall, potential, and football IQ.
 * @author Achi
 */
public class Player {
    
    public Team team;
    public String name;
    public String position;
    public int year;
    public int ratOvr;
    public int ratPot;
    public int ratFootIQ;
    public int ratDur;
    public int ratImprovement;
    public int cost;

    public int gamesPlayed;
    public int statsWins;
    public boolean wonHeisman;
    public boolean wonAllAmerican;
    public boolean wonAllConference;

    public int careerGamesPlayed;
    public int careerHeismans;
    public int careerAllAmerican;
    public int careerAllConference;
    public int careerWins;

    public boolean isRedshirt;

    public boolean isInjured;
    public Injury injury;

    protected final String[] letterGrades = {"F", "F+", "D", "D+", "C", "C+", "B", "B+", "A", "A+"};
    
    public Vector ratingsVector;
    
    public String getYrStr() {
        if (year == 0) {
            return "RS";
        } else if ( year == 1 ) {
            return "Fr";
        } else if ( year == 2 ) {
            return "So";
        } else if ( year == 3 ) {
            return "Jr";
        } else if ( year == 4 ) {
            return "Sr";
        }
        return "ERROR";
    }
    
    public void advanceSeason() {
        //add stuff
        year++;
    }
    
    public int getHeismanScore() {
        int adjGames = gamesPlayed;
        if (adjGames > 10) adjGames = 10;
        return ratOvr * adjGames;
    }

    public String getInitialName() {
        String[] names = name.split(" ");
        return names[0].substring(0,1) + ". " + names[1];
    }

    public String getPosNameYrOvrPot_Str() {
        if (injury != null) {
            return "[I]" + position + " " + getInitialName() + " [" + getYrStr() + "] Ovr: " + ratOvr + ">" + injury.toString();
        }
        return position + " " + name + " [" + getYrStr() + "]>" + "Ovr: " + ratOvr + ", Pot: " + getLetterGrade(ratPot);
    }

    public String getPosNameYrOvrPot_OneLine() {
        if (injury != null) {
            return position + " " + getInitialName() + " [" + getYrStr() + "] Ovr: " + ratOvr + " " + injury.toString();
        }
        return position + " " + getInitialName() + " [" + getYrStr() + "] " + "Ovr: " + ratOvr + ", Pot: " + getLetterGrade(ratPot);
    }

    public String getPosNameYrOvr_Str() {
        return position + " " + name + " [" + getYrStr() + "] Ovr: " + ratOvr;
    }

    public String getYrOvrPot_Str() {
        return "[" + getYrStr() + "] Ovr: " + ratOvr + ", Pot: " + getLetterGrade(ratPot);
    }

    public String getPosNameYrOvrPot_NoInjury() {
        return position + " " + getInitialName() + " [" + getYrStr() + "] Ovr: " + ratOvr + ", Pot: " + getLetterGrade(ratPot);
    }

    public String getMockDraftStr() {
        return position + " " + getInitialName() + " [" + getYrStr() + "]>" + team.strRep();
    }

    /**
     * Convert a rating into a letter grade. 90 -> A, 80 -> B, etc
     */
    protected String getLetterGrade(String num) {
        int ind = (Integer.parseInt(num) - 50)/5;
        if (ind > 9) ind = 9;
        if (ind < 0) ind = 0;
        return letterGrades[ind];
    }

    /**
     * Convert a rating into a letter grade for potential, so 50 is a C instead of F
     */
    protected String getLetterGradePot(String num) {
        int ind = (Integer.parseInt(num))/10;
        if (ind > 9) ind = 9;
        if (ind < 0) ind = 0;
        return letterGrades[ind];
    }

    /**
     * Convert a rating into a letter grade. 90 -> A, 80 -> B, etc
     */
    protected String getLetterGrade(int num) {
        int ind = (num-50)/5;
        if (ind > 9) ind = 9;
        if (ind < 0) ind = 0;
        return letterGrades[ind];
    }

    /**
     * Convert a rating into a letter grade for potential, so 50 is a C instead of F
     */
    protected String getLetterGradePot(int num) {
        int ind = num/10;
        if (ind > 9) ind = 9;
        if (ind < 0) ind = 0;
        return letterGrades[ind];
    }

    public ArrayList<String> getDetailStatsList(int games) {
        return null;
    }

    public ArrayList<String> getDetailAllStatsList(int games) {
        return null;
    }

    public ArrayList<String> getCareerStatsList() {
        ArrayList<String> pStats = new ArrayList<>();
        pStats.add("Games: " + (gamesPlayed+careerGamesPlayed) + " (" + (statsWins+careerWins) + "-" + (gamesPlayed+careerGamesPlayed-(statsWins+careerWins)) + ")"
                + ">Yrs: " + getYearsPlayed());
        pStats.add("Awards: " + getAwards() + "> ");
        return pStats;
    }

    public String getYearsPlayed() {
        int startYear = team.league.getYear() - year + 1;
        int endYear = team.league.getYear();
        return startYear + "-" + endYear;
    }

    public String getAwards() {
        ArrayList<String> awards = new ArrayList<>();
        int heis = careerHeismans + (wonHeisman ? 1 : 0);
        int aa = careerAllAmerican + (wonAllAmerican ? 1 : 0);
        int ac = careerAllConference + (wonAllConference ? 1 : 0);
        if (heis > 0) awards.add(heis + "x POTY");
        if (aa > 0) awards.add(aa + "x All-Amer");
        if (ac > 0) awards.add(ac + "x All-Conf");

        String awardsStr = "";
        for (int i = 0; i < awards.size(); ++i) {
            awardsStr += awards.get(i);
            if (i != awards.size()-1) awardsStr += ", ";
        }

        return awardsStr;
    }

    public String getInfoForLineup() {
        return null;
    }

    public String getInfoLineupInjury() {
        if (injury != null) {
            return getInitialName() + " [" + getYrStr() + "] " + injury.toString();
        }
        return getInitialName() + " [" + getYrStr() + "] " + "Ovr: " + ratOvr + ", Pot: " + getLetterGrade(ratPot);
    }

    public int getGamesPlayed() {
        if (gamesPlayed == 0) return 1;
        else return gamesPlayed;
    }

    public static int getPosNumber(String pos) {
        switch (pos) {
            case "QB": return 0;
            case "RB": return 1;
            case "WR": return 2;
            case "OL": return 3;
            case "K": return 4;
            case "S": return 5;
            case "CB": return 6;
            case "F7": return 7;
            default: return 8;
        }
    }
    
}
