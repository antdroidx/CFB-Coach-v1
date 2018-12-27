package positions;

import java.text.DecimalFormat;
import java.util.ArrayList;

import simulation.Injury;
import simulation.Team;

/**
 * Base player class that others extend. Has name, overall, potential, and football IQ.
 *
 * @author Achi
 */
public class
Player {

    public Team team;
    public String name;
    public String position;
    public int year;
    public int ratOvr;
    public int ratOvrStart;
    public int ratPot;
    public int ratFootIQ;
    public int ratDur;
    public int ratImprovement;
    public int cost;
    int progression;
    public int homeState;
    public int personality;
    public int height;
    public int weight;
    public boolean isSuspended;
    public int weeksSuspended;
    public int troubledTimes;
    public double talentNFL;

    public int gamesStarted;
    public int gamesPlayed;
    public int statsWins;
    public boolean wonHeisman;
    public boolean wonAllAmerican;
    public boolean wonAllConference;
    public boolean wonTopFreshman;
    public boolean wonAllFreshman;

    public int careerGames;
    public int careerHeismans;
    public int careerTopFreshman;
    public int careerAllAmerican;
    public int careerAllConference;
    public int careerAllFreshman;
    public int careerWins;

    public boolean isRedshirt;
    public boolean wasRedshirt;
    public boolean isMedicalRS;
    public boolean isTransfer;
    public boolean isGradTransfer;
    public boolean isWalkOn;

    public int recruitRating; // 0 - 5 0 = walk-on ; 1-5 = star scout rating

    public boolean isInjured;
    public Injury injury;

    final int attrBase = 50;
    final int ratBase = 60;
    final int yearFactor = 5;
    final double starFactor = 2.5;
    final double customFactor = 4.5;
    final int ratTolerance = 20;
    int recruitTolerance = 50;
    final int costBaseRating = 35;
    final int locationDiscount = 15;
    final int minGamesPot = 4;
    final int allConfPotBonus = 4;
    final int allAmericanBonus = 5;
    final int allFreshmanBonus = 4;
    final int topBonus = 3;

    final int endseason = 40;
    final int endseasonFactor = 15;
    final int endseasonBonus = 30;

    final double qbImportance = 1;
    final double rbImportance = 1.5;
    final double wrImportance = 2;
    final double teImportance = 4;
    final double olImportance = 2.5;
    final double kImportance = 5;
    final double dlImportance = 2.5;
    final double lbImportance = 2.5;
    final double cbImportance = 2.5;
    final double sImportance = 1.5;

    //game simulation data
    public int gameSnaps;
    public int gameFatigue;
    public double gameSim; //will be used for game sim calculations
    public int posDepth;
    public int gamePassAtempts;
    public int gamePassComplete;
    public int gamePassYards;
    public int gamePassTDs;
    public int gamePassInts;
    public int gameRushAttempts;
    public int gameRushYards;
    public int gameRushTDs;
    public int gameTargets;
    public int gameReceptions;
    public int gameRecYards;
    public int gameRecTDs;
    public int gameDrops;
    public int gameFumbles;
    public int gameTackles;
    public int gameSacks;
    public int gameInterceptions;
    public int gameDefended;
    public int gameIncomplete;
    public int gameFGAttempts;
    public int gameFGMade;
    public int gameXPAttempts;
    public int gameXPMade;
    final DecimalFormat df2 = new DecimalFormat(".##");

    private final String[] letterGrades = {"F-", "F", "F+", "D-", "D", "D+", "C-", "C", "C+", "B-", "B", "B+", "A-", "A", "A+"};

    public void createGenericAttributes() {
        ratPot = (int) (attrBase + 50 * Math.random());
        ratFootIQ = (int) (attrBase + 50 * Math.random());
        ratDur = (int) (attrBase + 50 * Math.random());
        personality = (int) (attrBase + 50 * Math.random());
        homeState = (int) (Math.random() * 50);
    }

    public int yearCompensation(int stars) {
        int tmp = stars;
        if(year < 4) tmp = stars * (int)(Math.random()*(4-year));
        return tmp;
    }

    public String getYrStr() {
        if (year == 0) {
            return "RS";
        } else if (year == 1) {
            return "Fr";
        } else if (year == 2) {
            return "So";
        } else if (year == 3) {
            return "Jr";
        } else if (year >= 4) {
            return "Sr";
        }
        return "ERROR";
    }

    private String getFullYrStr() {
        if (year == 0) {
            return "Redshirt";
        } else if (year == 1) {
            return "Freshman";
        } else if (year == 2) {
            return "Sophomore";
        } else if (year == 3) {
            return "Junior";
        } else if (year >= 4) {
            return "Senior";
        }
        return "ERROR";
    }

    public boolean getWasRedshirtStatus() {
        wasRedshirt = false;
        int rs = (int) (Math.random() * 5);
        if (year > 1 && rs > 1) wasRedshirt = true;

        return wasRedshirt;
    }

    public void advanceSeason() {
        //add stuff
        if (!isMedicalRS) year++;
    }

    public void checkRedshirt() {

        if (isTransfer || isRedshirt || isMedicalRS) {
            isTransfer = false;
            isRedshirt = false;
            isMedicalRS = false;
            wasRedshirt = true;
        } else if(gamesPlayed <= 4) {
            wasRedshirt = true;
        } else {
            year++;
        }
    }

    public void addSeasonAwards() {
        if (wonHeisman) careerHeismans++;
        if (wonAllAmerican) careerAllAmerican++;
        if (wonAllConference) careerAllConference++;
        if (wonAllFreshman) careerAllFreshman++;
        if (wonTopFreshman) careerTopFreshman++;

    }

    public int getSeasonAwards() {
        int award = 0;
        if(wonAllFreshman) award = 1;
        if(wonAllConference) award = 2;
        if(wonAllAmerican) award = 3;
        if(wonHeisman) award = 4;
        return award;
    }

    public int getHeismanScore() {
        int adjGames = gamesStarted;
        if (adjGames > 11) adjGames = 11;
        return ratOvr * adjGames + team.confPrestige * 5;
    }

    public int getCareerScore() {
        int adjGames = careerGames;
        return ratOvr * adjGames + team.confPrestige * 5;
    }

    public String getInitialName() {
        String[] names = name.split(" ");
        if (names.length > 1) {
            return names[0].substring(0, 1) + ". " + names[1];
        } else {
            return name;
        }
    }

    int getScoutingGrade() {
        int pRat;
        int scout = (4 * ratOvr + ratPot) / 5;
        if (year < 2) {
            if (scout > team.five) pRat = 5;
            else if (scout > team.four) pRat = 4;
            else if (scout > team.three) pRat = 3;
            else if (scout > team.two) pRat = 2;
            else pRat = 1;
        } else {
            int calcOvr = ratOvr - (year * (100 - ratPot) / 7);
            if (calcOvr > team.five) pRat = 5;
            else if (calcOvr > team.four) pRat = 4;
            else if (calcOvr > team.three) pRat = 3;
            else if (calcOvr > team.two) pRat = 2;
            else pRat = 1;
        }
        if (isWalkOn) pRat = 0;

        return pRat;
    }

    String getScoutingGradeString() {
        String grade;

        if (recruitRating == 0) {
            grade = "Walk-On";
        } else if (recruitRating == 1) {
            grade = "1 Star";
        } else if (recruitRating == 2) {
            grade = "2 Star";
        } else if (recruitRating == 3) {
            grade = "3 Star";
        } else if (recruitRating == 4) {
            grade = "4 star";
        } else {
            grade = "5 Star";
        }

        return grade;
    }


    public String getPosNameYrOvrPot_Str() {
                if (injury != null) {
                    return position + "  " + getInitialName() + " [" + getYrStr() + "]" + ">" + injury.toString() + "  Ovr: " + ratOvr;
                }
                if (isSuspended) {
                    return position + "  " + getInitialName() + " [" + getYrStr() + "]" + ">Suspended (" + weeksSuspended + "wk)  Ovr: " + ratOvr;
                }
                return position + "  " + name + " [" + getYrStr() + "]>" + "Ovr: " + ratOvr;
    }

    public String getPosNameYrOvrPotTra_Str() {
        return position + " " + name + " [" + getYrStr() + "]>" + "Ovr: " + ratOvr + " [Transfer]";
    }

    public String getGraduatingPlayerInfo() {
        return position + " " + name + " [" + getYrStr() + "]>" + "Ovr: " + ratOvr;
    }

    public String getPosNameYrOvrPot_OneLine() {
        if (injury != null) {
            return position + " " + getInitialName() + " [" + getYrStr() + "]  Ovr: " + ratOvr + " " + injury.toString();
        }
        return position + " " + getInitialName() + " [" + getYrStr() + "] " + " Ovr: " + ratOvr;
    }

    public String getPosNameYrOvr_Str() {
        return team.name + ": " + position + " " + name + " [" + getYrStr() + "] Ovr: " + ratOvr;
    }


    public String getMockDraftStr(int round, int selection, String nflTeam) {
        return "Round " + round + ", Pick " + selection + " : " + nflTeam + "\n" + position + " " + name + "\n" + getFullYrStr()
                + ">\n" + team.name + "\n" + "Overall: " + ratOvr;
    }

    /**
     * Convert a rating into a letter grade. 90 -> A, 80 -> B, etc
     */
    protected String getLetterGrade(String num) {
        int ind = (int)((Integer.parseInt(num) - 50) / 3.33);
        if (ind > 14) ind = 14;
        if (ind < 0) ind = 0;
        return letterGrades[ind];
    }

    /**
     * Convert a rating into a letter grade for potential, so 50 is a C instead of F
     */
    protected String getLetterGradePot(String num) {
        int ind = (Integer.parseInt(num)) / 10;
        if (ind > 9) ind = 9;
        if (ind < 0) ind = 0;
        return letterGrades[ind];
    }

    /**
     * Convert a rating into a letter grade. 90 -> A, 80 -> B, etc
     */
    String getLetterGrade(int num) {
        int ind = (int)((num - 50) / 3.33);
        if (ind > 14) ind = 14;
        if (ind < 0) ind = 0;
        return letterGrades[ind];
    }

    /**
     * Potential Overall Score
     */
    public int getPotRating(int hc) {
        if (!team.league.showPotential) return 0;
        int potential;
        potential = ratOvr + ((3 * ratPot + 2 * hc) / 50) * (4 - year);
        return potential;
    }

    public ArrayList<String> getDetailAllStatsList(int games) {
        return null;
    }

    public ArrayList<String> getCareerStatsList() {
        ArrayList<String> pStats = new ArrayList<>();
        pStats.add("Games: " + (gamesStarted + careerGames) + ">Yrs: " + getYearsPlayed());
        pStats.add("Awards: " + getAwards() + "> ");
        return pStats;
    }

    private String getYearsPlayed() {
        int startYear = team.league.getYear() - year + 1;
        int endYear = team.league.getYear();
        return startYear + "-" + endYear;
    }

    private String getAwards() {
        ArrayList<String> awards = new ArrayList<>();
        int heis = careerHeismans + (wonHeisman ? 1 : 0);
        int aa = careerAllAmerican + (wonAllAmerican ? 1 : 0);
        int ac = careerAllConference + (wonAllConference ? 1 : 0);
        if (heis > 0) awards.add(heis + "x POTY");
        if (aa > 0) awards.add(aa + "x All-Amer");
        if (ac > 0) awards.add(ac + "x All-Conf");
        if (careerTopFreshman > 0 || wonTopFreshman) awards.add("Top Freshman");
        if (careerAllFreshman > 0 || wonAllFreshman) awards.add("All-Fresh");

        String awardsStr = "";
        for (int i = 0; i < awards.size(); ++i) {
            awardsStr += awards.get(i);
            if (i != awards.size() - 1) awardsStr += ", ";
        }

        return awardsStr;
    }

    int getConfPrestigeBonus() {
        return team.teamPrestige * 3 + team.confPrestige * 7 + ((120 - team.rankTeamPollScore) * 3);
    }

    int getInitialCost() {
        return (int) ((Math.pow((float) ratOvr - costBaseRating, 2) / 5) + (int) Math.random() * recruitTolerance);
    }

    int getLocationCost() {
        double locFactor = Math.abs(team.location - (homeState /10)) - 2.5;
        return cost + (int) (Math.random() * (locFactor * locationDiscount));
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

    public String getInfoLineupTransfer() {
        return getInitialName() + " [" + getYrStr() + "] " + "Ovr: " + ratOvr + "  Transfer";
    }

    public String getInfoLineupSuspended() {
        return getInitialName() + " [" + getYrStr() + "] " + "Ovr: " + ratOvr + "  Suspended";

    }

    int getGames() {
        int games = gamesStarted + (gamesPlayed - gamesStarted);
        if (games == 0) return 1;
        else return games;
    }

    public static int getPosNumber(String pos) {
        switch (pos) {
            case "QB":
                return 0;
            case "RB":
                return 1;
            case "WR":
                return 2;
            case "TE":
                return 3;
            case "OL":
                return 4;
            case "K":
                return 5;
            case "DL":
                return 6;
            case "LB":
                return 7;
            case "CB":
                return 8;
            case "S":
                return 9;
            default:
                return 10;

        }
    }


    public int getRegion() {
        int location;
        location = homeState/10;
        return location;
    }

    public String getHomeState(int region) {
        return team.league.states[region];
    }




    public String getPersonality(int personality) {
        String trait = "";
        if (personality > 91) trait = "Leader";
        else if (personality > 84) trait = "Motivated";
        else if (personality > 75) trait = "";
        else if (personality > 67) trait = "Average";
        else if (personality > 59) trait = "Team Player";
        else if (personality > 54) trait = "Trouble";
        else trait = "Undisciplined";

        return trait;
    }

    String getStatus() {
        if (isTransfer) {
            return "Transfer";
        } else if (isRedshirt) {
            return "Redshirt";
        } else if (isMedicalRS) {
            return "Medical";
        } else if (isInjured) {
            return "Injured";
        } else if (isSuspended) {
            return "Suspended";
        } else {
            return "Active";
        }
    }

    public String getTransferStatus() {
        if (isGradTransfer) return "Grad";
        else return getYrStr();
    }

    String getHeight() {

        int feet = height / 12;
        int inch = height % 12;

        return feet + "' " + inch + "\"";
    }

    String getWeight() {
        return weight + " lbs";
    }

    public int getProgression() {
        int num = (ratPot * 2 + team.HC.get(0).ratTalent * 1 + 3*team.teamFacilities + (int)(Math.random() * getChemistryProgression())) / 3;
        return num;
    }

    public int getProgressionOff() {
        int num = (ratPot * 4 + team.HC.get(0).ratTalent * 2 + team.HC.get(0).ratOff + 7*team.teamFacilities + (int)(Math.random() * getChemistryProgression())) / 7;
        return num;
    }

    public int getProgressionDef() {
        int num = (ratPot * 4 + team.HC.get(0).ratTalent * 2 + team.HC.get(0).ratDef + 7*team.teamFacilities + (int)(Math.random() * getChemistryProgression())) / 7;
        return num;
    }

    public double getGamesBonus() {
        double games = (double) (gamesStarted) + (double) ((gamesPlayed - gamesStarted) / 3);
        games = (games * 2.5);
        return games;
    }

    public double getMidSeasonBonus() {
        ratOvrStart = ratOvr;
        double games = (double) (gamesStarted / 2) + (double) ((gamesPlayed - gamesStarted) / 3);
        return games;
    }

    public double getChemistryProgression() {
        return team.teamChemistry - team.league.getAverageTeamChemistry();
    }

    public void durabilityProgression() {
        ratDur += Math.random() * 2*year;
    }

    public ArrayList<String> stringPlayerInfo() {
        ArrayList<String> pAttr = new ArrayList<>();
        pAttr.add("Team: " + team.name + ">Overall: " + ratOvr);
        pAttr.add("Height " + getHeight() + ">Weight: " + getWeight());
        pAttr.add("Home State: " + getHomeState(homeState) + ">Scout Grade: " + getScoutingGradeString());
        return pAttr;
    }
    
    public ArrayList<String> stringPlayerAttributes() {
        ArrayList<String> pAttr = new ArrayList<>();
        pAttr.add("Personality: " + getLetterGrade(personality) + " > " + getStatus());
        pAttr.add("Durability: " + getLetterGrade(ratDur) + ">Football IQ: " + getLetterGrade(ratFootIQ));
        return pAttr;
    }
}
