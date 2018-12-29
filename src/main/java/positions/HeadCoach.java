package positions;

import java.util.ArrayList;
import java.util.Random;

import simulation.Team;

/**
 * Created by ahngu on 10/9/2017.
 * <p>
 * Head Coach Position, TBD
 */

public class HeadCoach extends Player {
    public int age;
    public int year;
    public int contractYear;
    public int contractLength;
    public int ratOvr;
    public int ratPot;
    public int ratOff;
    public int ratDef;
    public int ratTalent;
    public int ratDiscipline;
    public int offStrat;
    public int defStrat;
    public int baselinePrestige;
    public int cumulativePrestige;
    public int teamWins;
    public int teamLosses;
    public int wins;
    public int losses;
    public int bowlwins;
    public int bowllosses;
    public int confchamp;
    public int natchamp;
    public int allamericans;
    public int allconference;
    public int confAward;
    public int awards;
    private final Random rand = new Random();
    private final int max = 4;
    private final int min = 0;
    public final ArrayList<String> history;

    public boolean promotionCandidate;
    public boolean retirement;
    public boolean wonConfHC;
    public boolean wonTopHC;
    public boolean retired;

    //Leaderboard Stats
    public float careerScore;

    public HeadCoach(String nm, Team t, int a, int yr, int cyr, int clength, int pot, int off, int def, int tal, int dis, int ostrat, int dstrat, int sPrs, int cWins, int cLosses,
                     int bwins, int blosses, int cchamps, int nchamps, int allconf, int allams, int caw, int aw, int cpres) {
        team = t;
        name = nm;
        age = a;
        year = yr;
        contractYear = cyr;
        contractLength = clength;
        ratOvr = (off + def + tal + dis) / 4;
        ratPot = pot;
        ratOff = off;
        ratDef = def;
        ratTalent = tal;
        ratDiscipline = dis;
        ratOvr = getHCOverall();
        offStrat = ostrat;
        defStrat = dstrat;
        baselinePrestige = sPrs;
        teamWins = 0;
        teamLosses = 0;
        wins = cWins;
        losses = cLosses;
        confchamp = cchamps;
        natchamp = nchamps;
        bowlwins = bwins;
        bowllosses = blosses;
        allamericans = allams;
        allconference = allconf;
        confAward = caw;
        awards = aw;
        cumulativePrestige = cpres;
        history = new ArrayList<>();

        position = "HC";
    }

    //This one is for Retirement Coach Database
    public HeadCoach(String nm, int a, int yr, int pot, int off, int def, int tal, int dis, int ostrat, int dstrat, int sPrs, int cWins, int cLosses,
                     int bwins, int blosses, int cchamps, int nchamps, int allconf, int allams, int caw, int aw, int cpres, boolean ret) {
        name = nm;
        age = a;
        year = yr;
        contractYear = 0;
        contractLength = 0;
        ratPot = pot;
        ratOff = off;
        ratDef = def;
        ratTalent = tal;
        ratDiscipline = dis;
        ratOvr = getHCOverall();
        offStrat = ostrat;
        defStrat = dstrat;
        baselinePrestige = sPrs;
        teamWins = 0;
        teamLosses = 0;
        wins = cWins;
        losses = cLosses;
        confchamp = cchamps;
        natchamp = nchamps;
        bowlwins = bwins;
        bowllosses = blosses;
        allamericans = allams;
        allconference = allconf;
        confAward = caw;
        awards = aw;
        cumulativePrestige = cpres;
        history = new ArrayList<>();
        retired = ret;

        position = "HC";
    }

    public HeadCoach(String nm, int yr, int stars, Team t) {
        name = nm;
        year = yr;
        team = t;

        age = 30 + (int) (Math.random() * 28);
        year = 0;
        contractYear = (int) (6 * Math.random());
        contractLength = 6;
        ratPot = (int) (50 + 50 * Math.random());
        ratOff = (int) (50 + stars * 5 - 15 * Math.random() + 15 * Math.random());
        ratDef = (int) (50 + stars * 5 - 15 * Math.random() + 15 * Math.random());
        ratTalent = (int) (45 + 50 * Math.random());
        ratDiscipline = (int) (45 + 50 * Math.random());
        ratOvr = getHCOverall();
        offStrat = rand.nextInt((max - min) + 1) + min;
        if (offStrat > 4) offStrat = 4;
        defStrat = rand.nextInt((max - min) + 1) + min;
        if (defStrat > 4) defStrat = 4;
        baselinePrestige = team.teamPrestige;
        teamWins = 0;
        teamLosses = 0;
        wins = 0;
        losses = 0;
        confchamp = 0;
        natchamp = 0;
        bowlwins = 0;
        bowllosses = 0;
        allamericans = 0;
        allconference = 0;
        confAward = 0;
        awards = 0;
        cumulativePrestige = 0;
        history = new ArrayList<>();

        position = "HC";
    }

    public HeadCoach(String nm, int yr, int stars, Team t, boolean newhire) {
        name = nm;
        year = yr;
        team = t;
        boolean promote = newhire;

        age = 30 + (int) (Math.random() * 10);
        year = 0;
        contractYear = 0;
        contractLength = 6;
        ratPot = (int) (50 + 50 * Math.random());
        ratOff = (int) (45 + stars * 5 - 20 * Math.random() + 20 * Math.random());
        ratDef = (int) (45 + stars * 5 - 20 * Math.random() + 20 * Math.random());
        ratTalent = (int) (45 + 45 * Math.random());
        ratDiscipline = (int) (45 + 45 * Math.random());
        ratOvr = getHCOverall();
        offStrat = rand.nextInt((max - min) + 1) + min;
        if (offStrat > 4) offStrat = 4;
        defStrat = rand.nextInt((max - min) + 1) + min;
        if (defStrat > 4) defStrat = 4;
        baselinePrestige = team.teamPrestige;
        teamWins = 0;
        teamLosses = 0;
        wins = 0;
        losses = 0;
        confchamp = 0;
        natchamp = 0;
        bowlwins = 0;
        bowllosses = 0;
        allamericans = 0;
        allconference = 0;
        confAward = 0;
        awards = 0;
        cumulativePrestige = 0;
        history = new ArrayList<>();

        position = "HC";
    }

    public void advanceSeason(int avgYards, int offTalent, int defTalent) {
        int prestigeDiff = team.teamPrestige - team.teamPrestigeStart - team.disciplinePts;

        int oldOvr = getHCOverall();
        age++;
        year++;
        contractYear++;

        double off = team.teamYards - avgYards;
        double def = avgYards - team.teamOppYards;
        double offTal = offTalent - team.teamStartOffTal;
        double defTal = defTalent - team.teamStartDefTal;
        double offpts = ((off / avgYards) + (offTal / offTalent)) * 4;
        double defpts = ((def / avgYards) + (defTal / defTalent)) * 4;
        double coachScore = (getCoachScore() - team.confPrestige)/10;
        if (coachScore < -4) coachScore = -4;


        ratOff += (2*prestigeDiff + offpts + coachScore)/4;
        if (ratOff > 95) ratOff = 95;
        if (ratOff < 20) ratOff = 20;

        ratDef += (2*prestigeDiff + defpts + coachScore)/4;
        if (ratDef > 95) ratDef = 95;
        if (ratDef < 20) ratDef = 20;

        ratTalent += (2*prestigeDiff  + coachScore)/3;
        if (ratTalent > 95) ratTalent = 95;
        if (ratTalent < 20) ratTalent = 20;

        if (ratDiscipline > 90) ratDiscipline = 90;
        if (ratDiscipline < 15) ratDiscipline = 15;


        if (age > 65 && !team.userControlled) {
            ratOff -= (int) Math.random() * 4;
            ratDef -= (int) Math.random() * 4;
            ratTalent -= (int) Math.random() * 4;
            ratDiscipline -= (int) Math.random() * 4;
        }

        if (age > 70 && team.userControlled && team.league.isCareerMode() && !team.league.neverRetire ) {
            ratOff -= (int) Math.random() * (age / 20);
            ratDef -= (int) Math.random() * (age / 20);
            ratTalent -= (int) Math.random() * (age / 20);
            ratDiscipline -= (int) Math.random() * (age / 20);
        }

        ratOvr = getHCOverall();
        ratImprovement = ratOvr - oldOvr;

        wins += teamWins;
        losses += teamLosses;
        teamWins = 0;
        teamLosses = 0;

        confchamp += 0;
        natchamp += 0;
        allamericans += 0;
        allconference += 0;
        confAward += 0;
        awards += 0;
        cumulativePrestige += prestigeDiff;
    }

    public int getCoachScore() {
        int prestigeDiff;
        if (team.league.currentWeek < 15) {
            int[] newPrestige = team.calcSeasonPrestige();
            prestigeDiff = newPrestige[0] - team.teamPrestige;
        } else {
            prestigeDiff = team.teamPrestige - team.teamPrestigeStart;
        }

        return prestigeDiff * 10 + (team.teamStrengthOfWins / 20) + 3 * teamWins - 1 * teamLosses + team.confPrestige;
    }

    //For future implementation: tally up the total prestige change over the years for scoring
    public int getCoachCareerScore() {
        if (year < 1) return 0;
        else
            return (5 * (wins + teamWins) - 2 * (losses + teamLosses) + 10 * natchamp + 3 * confchamp + 10 * awards + 3 * confAward + allconference + 2 * allamericans + 5*cumulativePrestige) / year;
    }

    @Override
    public ArrayList<String> getDetailAllStatsList() {
        ArrayList<String> pStats = new ArrayList<>();
        pStats.add("Age: " + age + ">Years Coaching: " + year);
        if (team.league.isCareerMode()) {
            pStats.add("Contract Years Left: " + (contractLength - contractYear - 1) + ">Contract Length: " + contractLength);
        }
        pStats.add(" > ");
        pStats.add("[B]COACHING STYLE");

        pStats.add("Offense: " + team.playbookOff.getStratName() + ">Defense: " + team.playbookDef.getStratName());
        pStats.add("Offense: " + ratOff + ">Defense: " + ratDef);
        pStats.add("Talent Progression: " + ratTalent + ">Discipline: " + ratDiscipline);
        pStats.add(" > ");
        pStats.add("[B]TEAM STATUS");

        pStats.add("Baseline Prestige: " + baselinePrestige + ">Team Prestige: " + team.teamPrestige);
        pStats.add("Job Status: " + coachStatus() + "> ");

        pStats.add("[B]CAREER STATS");
        pStats.addAll(getCareerStatsList());
        return pStats;
    }

    @Override
    public ArrayList<String> getCareerStatsList() {
        ArrayList<String> pStats = new ArrayList<>();
        pStats.add("Current Status: " + (wins+teamWins) + ">Career Losses: " + (losses+teamLosses));
        pStats.add("Career Wins: " + (wins+teamWins) + ">Career Losses: " + (losses+teamLosses));
        pStats.add("Bowl Wins: " + bowlwins + ">Bowl Losses: " + bowllosses);
        pStats.add("Conf Champs: " + confchamp + ">National Champs: " + natchamp);
        pStats.add("All Conferences: " + allconference + ">All Americans: " + allamericans);
        pStats.add("Conference Coach: " + confAward + ">Coach of Year: " + awards);
        pStats.add("Prestige Change: " + cumulativePrestige + ">Coach Score: " + getCoachCareerScore());
        return pStats;
    }

    @Override
    public String getYrStr() {
        return " Seasons: " + year;
    }

    public String[] getCoachHistory() {
        String[] hist = new String[history.size()];

        for (int i = 0; i < history.size(); ++i) {
            hist[i] = history.get(i);
        }
        return hist;
    }

    public String getHCString() {
        if (team.league.currentWeek > 15 && year > 0 && ratImprovement > 0) return "Head Coach " + name + ">" + "Ovr: " + getHCOverall() + " (+" + ratImprovement + ")";
        else if (team.league.currentWeek > 15 && year > 0 && ratImprovement < 0) return "Head Coach " + name + ">" + "Ovr: " + getHCOverall() + " (" + ratImprovement + ")";
        else return "Head Coach " + name + ">" + "Ovr: " + ratOvr;
    }

    public int getHCOverall() {
        ratOvr = (ratOff + ratDef + ratTalent + ratDiscipline) / 4;
        return ratOvr;
    }

    public float getHCHiring() {
        float value = 0;

        if(!retired) {
            value = ((float)(getHCOverall() + getCoachCareerScore() - (age/4)) / 2);
        }

        return value;
    }

    /* Leaderboard Google Play API Future Implementation
       This will be used to score players at various ages (50, 65, 80, 100, 150, etc)
       Score should only apply if they play Challenge Mode.
       Score will be a combination of Prestige gained over time, number of CCs, NCs, etc.

     */
    public float getLeaderboardScore() {
        float score = 0;

        score = careerScore + (5 * (wins+teamWins) - 2 * (losses+teamLosses) + 10 * natchamp + 3 * confchamp + 10 * awards + 3 * confAward + allconference + 2 * allamericans);

        return score;
    }

    public String coachStatus() {
        String status = "Normal";
        if(baselinePrestige > (team.teamPrestige + 5)) status = "Hot Seat";
        else if(baselinePrestige + 7 < (team.teamPrestige)) status = "Secure";
        else if(baselinePrestige + 3 < (team.teamPrestige)) status = "Safe";
        else if (baselinePrestige > (team.teamPrestige + 3)) status = "Unsafe";
        else status = "OK";

        return status;
    }

    public int getSeasonAwards() {
        int award = 0;
        if(wonConfHC) award = 2;
        if(wonTopHC) award = 4;

        return award;
    }

    public double getWinPCT() {
       double pct = 0;
       if(wins+teamWins + losses+teamLosses > 0) {
           pct = 100*(double)(wins+teamWins)/(wins+losses+teamWins+teamLosses);
       }
       return pct;
    }
}