package Positions;

import java.util.ArrayList;
import java.util.Random;

import Simulation.Team;

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
    private final int teamWins;
    private final int teamLosses;
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
    private final double potFactor = 1.33;

    public boolean promotionCandidate;

    //Leaderboard Stats
    public float careerScore;
    final public int leaderboardAge = 50;
    final public int leaderboardAge2 = 65;
    final public int leaderboardAge3 = 80;
    final public int leaderboardAge4 = 100;
    final public int leaderboardAge5 = 150;

    public HeadCoach(String nm, Team t, int a, int yr, int cyr, int clength, int pot, int off, int def, int tal, int dis, int ostrat, int dstrat, int sPrs, int cWins, int cLosses,
                     int bwins, int blosses, int cchamps, int nchamps, int allconf, int allams, int caw, int aw) {
        team = t;
        name = nm;
        age = a;
        year = yr;
        contractYear = cyr;
        contractLength = clength;
        ratOvr = (off + def + tal + dis)/4;
        ratPot = pot;
        ratOff = off;
        ratDef = def;
        ratTalent = tal;
        ratDiscipline = dis;
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
        history = new ArrayList<>();

        position = "HC";
    }

    //This one is for HC Free Agency Pool
    public HeadCoach(String nm, int a, int yr, int pot, int off, int def, int tal, int dis, int ostrat, int dstrat, int sPrs, int cWins, int cLosses,
                     int bwins, int blosses, int cchamps, int nchamps, int allconf, int allams, int caw, int aw) {
        name = nm;
        age = a;
        year = yr;
        contractYear = 0;
        contractLength = 0;
        ratOvr = (2*off + 2*def + tal + dis)/6;
        ratPot = pot;
        ratOff = off;
        ratDef = def;
        ratTalent = tal;
        ratDiscipline = dis;
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
        history = new ArrayList<>();

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
        ratOff = (int) (50 + stars * 5 - 15 * Math.random() + 15 * Math.random() );
        ratDef = (int) (50 + stars * 5 - 15 * Math.random() + 15 * Math.random() );
        ratTalent = (int) (45 + 50*Math.random());
        ratDiscipline = (int) (45 + 50 * Math.random());
        ratOvr = (ratOff + ratDef + ratTalent + ratDiscipline) / 4;
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
        ratOff = (int) (45 + stars * 5 - 20 * Math.random() + 20 * Math.random() );
        ratDef = (int) (45 + stars * 5 - 20 * Math.random() + 20 * Math.random() );
        ratTalent = (int) (45 + 45 * Math.random());
        ratDiscipline = (int) (45 + 45 * Math.random());
        ratOvr = (ratOff + ratDef + ratTalent + ratDiscipline) / 4;
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
        history = new ArrayList<>();

        position = "HC";
    }

    public void advanceSeason(int prestigeDiff, int avgYards, int offTalent, int defTalent) {
        int oldOvr = ratOvr;
        age++;
        year++;
        contractYear++;

        // WIP
        ratTalent += Math.random()*prestigeDiff;
        float off = team.teamYards - avgYards;
        float def = avgYards - team.teamOppYards;
        float offTal = offTalent - team.teamOffTalent;
        float defTal = defTalent - team.teamDefTalent;
        float offpts = ((off / avgYards) + (offTal / offTalent)) * 4;
        float defpts = ((def / avgYards) + (defTal / defTalent)) * 4;

        ratOff += ((prestigeDiff + offpts) * ((double)ratTalent/100));
        if (ratOff > 95) ratOff = 95;
        if (ratOff < 20) ratOff = 20;

        ratDef += ((prestigeDiff + defpts) * ((double)ratTalent/100));
        if (ratDef > 95) ratDef = 95;
        if (ratDef < 20) ratDef = 20;

        if (ratDiscipline > 90) ratDiscipline = 90;
        if (ratDiscipline < 15) ratDiscipline = 15;

        if (ratTalent > 95) ratTalent = 95;
        if (ratTalent < 20) ratTalent = 20;


        ratOvr = (ratOff + ratDef + ratTalent + ratDiscipline) / 4;
        ratImprovement = ratOvr - oldOvr;

        wins += team.wins;
        losses += team.losses;
        confchamp += 0;
        natchamp += 0;
        allamericans += 0;
        allconference += 0;
        confAward += 0;
        awards += 0;
    }

    public int getCoachScore() {
        int prestigeDiff;
        if (team.league.currentWeek < 14) {
            int[] newPrestige = team.calcSeasonPrestige();
            prestigeDiff = newPrestige[0] - team.teamPrestige;
        } else {
            prestigeDiff = team.teamPrestige - team.teamPrestigeStart;
        }

        return prestigeDiff * 10 + team.wins * 2 + (team.teamStrengthOfWins / 25);
    }

    //For future implementation: tally up the total prestige change over the years for scoring
    public int getCoachCareerScore() {
        if (year < 1) return 0;
        else return (5*(wins) - 2*(losses) + 10 * natchamp + 3 * confchamp + 10 * awards + 3 * confAward + allconference + 2* allamericans)/year;
    }

    @Override
    public ArrayList<String> getDetailStatsList(int games) {
        ArrayList<String> pStats = new ArrayList<>();
        pStats.add("Age: " + age + ">Years Coaching: " + year);
        if (team.league.isCareerMode()) {
            pStats.add("Contract Years Remaining: " + (contractLength - contractYear - 1) + ">Contract Length: " + contractLength);
        }
        pStats.add("Offense Playcalling: " + getLetterGrade(ratOff) + ">Defense Playcalling: " + getLetterGrade(ratDef));
        pStats.add("Offense Style: " + team.playbookOff.getStratName() + ">Defense Style: " + team.playbookDef.getStratName());
        pStats.add("Talent Progression: " + getLetterGrade(ratTalent) + ">Discipline: " + getLetterGrade(ratDiscipline));
        pStats.add("Baseline Prestige: " + baselinePrestige + ">Team Prestige: " + team.teamPrestige);
        pStats.add(" > ");
        return pStats;
    }

    @Override
    public ArrayList<String> getDetailAllStatsList(int games) {
        ArrayList<String> pStats = new ArrayList<>();
        pStats.add("Age: " + age + ">Years Coaching: " + year);
        if (team.league.isCareerMode()) {
            pStats.add("Contract Years Remaining: " + (contractLength - contractYear - 1) + ">Contract Length: " + contractLength);
        }
        pStats.add("Offense Philosophy: " + ratOff + ">Defense Philosophy: " + ratDef);
        pStats.add("Offense Style: " + team.playbookOff.getStratName() + ">Defense Style: " + team.playbookDef.getStratName());
        pStats.add("Talent Progression: " + ratTalent + ">Discipline: " + ratDiscipline);
        pStats.add("Baseline Prestige: " + baselinePrestige + ">Team Prestige: " + team.teamPrestige);
        pStats.add("[B]CAREER STATS:");
        pStats.addAll(getCareerStatsList());
        return pStats;
    }

    @Override
    public ArrayList<String> getCareerStatsList() {
        ArrayList<String> pStats = new ArrayList<>();
        pStats.add("Career Wins: " + wins + ">Career Losses: " + losses);
        pStats.add("Bowl Wins: " + bowlwins + ">Bowl Losses: " + bowllosses);
        pStats.add("Conf Champs: " + confchamp + ">National Champs: " + natchamp);
        pStats.add("All Conferences: " + allconference + ">All Americans: " + allamericans);
        pStats.add("Conference Coach: " + confAward + ">Coach of Year: " + awards);
        return pStats;
    }


    @Override
    public String getYrOvrPot_Str() {
        return "COACH ATTRIBUTES:";
    }

    @Override
    public String getYrStr() {
        return "Season " + year;
    }

    public String[] getCoachHistory() {
        String[] hist = new String[history.size()];

        for (int i = 0; i < history.size(); ++i) {
            hist[i] = history.get(i);
        }
        return hist;
    }

    public int getHCOverall() {
        ratOvr = (ratOff + ratDef + ratTalent + ratDiscipline) / 4;
        return ratOvr;
    }


    /* Leaderboard Google Play API Future Implementation
       This will be used to score players at various ages (50, 65, 80, 100, 150, etc)
       Score should only apply if they play Challenge Mode.
       Score will be a combination of Prestige gained over time, number of CCs, NCs, etc.

     */
    public float getLeaderboardScore() {
        float score = 0;

        score = careerScore + (5*(wins) - 2*(losses) + 10 * natchamp + 3 * confchamp + 10 * awards + 3 * confAward + allconference + 2* allamericans);

        return score;
    }
}