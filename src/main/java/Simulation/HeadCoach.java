package Simulation;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by ahngu on 10/9/2017.
 * <p>
 * Head Coach Position, TBD
 */

public class HeadCoach extends Player {
    int age;
    int year;
    public int contractYear;
    public int contractLength;
    public int ratOvr;
    int ratOff;
    int ratDef;
    int ratTalent;
    int ratDiscipline;
    int offStrat;
    int defStrat;
    public int baselinePrestige;
    int teamWins;
    int teamLosses;
    int wins;
    int losses;
    int bowlwins;
    int bowllosses;
    int confchamp;
    int natchamp;
    int allamericans;
    int allconference;
    int confAward;
    int awards;
    Random rand = new Random();
    int max = 4;
    int min = 0;
    public ArrayList<String> history;


    public HeadCoach(String nm, Team t, int a, int yr, int cyr, int clength, int off, int def, int tal, int dis, int ostrat, int dstrat, int sPrs, int cWins, int cLosses,
                     int bwins, int blosses, int cchamps, int nchamps, int allconf, int allams, int caw, int aw) {
        team = t;
        name = nm;
        age = a;
        year = yr;
        contractYear = cyr;
        contractLength = clength;
        ratOvr = (off + def + tal + dis) / 4;
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
        ratOff = (int) (50 + year * 5 + stars * 5 - 25 * Math.random());
        ratDef = (int) (50 + year * 5 + stars * 5 - 25 * Math.random());
        ratTalent = (int) (50 + 50 * Math.random());
        ratDiscipline = (int) (45 + 50 * Math.random());
        ratOvr = (ratOff + ratDef + ratTalent + ratDiscipline) / 4;
        offStrat = rand.nextInt((max - min) + 1) + min;;
        defStrat = rand.nextInt((max - min) + 1) + min;;
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
        ratOff = (int) (45 + year * 5 + stars * 5 - 25 * Math.random());
        ratDef = (int) (45 + year * 5 + stars * 5 - 25 * Math.random());
        ratTalent = (int) (45 + 45 * Math.random());
        ratDiscipline = (int) (45 + 45 * Math.random());
        ratOvr = (ratOff + ratDef + ratTalent + ratDiscipline) / 4;
        offStrat = rand.nextInt((max - min) + 1) + min;;
        defStrat = rand.nextInt((max - min) + 1) + min;;
        baselinePrestige = team.calcSeasonPrestige()[0];
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
    //@Override
    public void advanceSeason(int prestigeDiff, int avgYards, int offTalent, int defTalent) {
        int oldOvr = ratOvr;
        int offpts, defpts;
        int bowl = (team.wins + team.losses - 12);
        age++;
        year++;
        contractYear++;

        // WIP
        ratTalent += prestigeDiff + bowl;
        int off = team.teamYards - avgYards;
        int def = avgYards - team.teamOppPassYards;
        int offTal = offTalent - team.teamOffTalent;
        int defTal = defTalent - team.teamDefTalent;
        offpts = ((off / avgYards) + (offTal / offTalent)) * 10;
        defpts = ((def / avgYards) + (defTal / defTalent)) * 10;

        ratOff += (int) (Math.random() * (prestigeDiff / 2 + offpts));
        if (ratOff > 99) ratOff = 99;

        ratDef += (int) (Math.random() * (prestigeDiff / 2 + defpts));
        if (ratDef > 99) ratDef = 99;
        
        if (ratDiscipline > 95) ratDiscipline = 95;
        if (ratTalent > 95) ratTalent = 95;

        ratOvr = (ratOff + ratDef + ratTalent + ratDiscipline) / 4;
        ratImprovement = ratOvr - oldOvr;

        wins += 0;
        losses += 0;
        confchamp += 0;
        natchamp += 0;
        allamericans += 0;
        allconference += 0;
        confAward += 0;
        awards += 0;
    }

    public int getCoachScore() {
        int[] newPrestige = team.calcSeasonPrestige();
        int prestigeDiff = newPrestige[0] - team.teamPrestige;

        return prestigeDiff * 10 + team.wins * 2 + (team.teamStrengthOfWins / 25);
    }

    public int getCoachCareerScore() {
        return 5*wins - 2*losses + 10 * natchamp + 3 * confchamp + 10 * awards + 3 * confAward + allconference + 2* allamericans;
    }

    @Override
    public ArrayList<String> getDetailStatsList(int games) {
        ArrayList<String> pStats = new ArrayList<>();
        pStats.add("Age: " + age + ">Years Coaching: " + year);
        if (team.league.isCareerMode()) {
            pStats.add("Contract Years Remaining: " + (contractLength - contractYear - 1) + ">Contract Length: " + contractLength);
        }
        pStats.add("Offense Playcalling: " + getLetterGrade(ratOff) + ">Defense Playcalling: " + getLetterGrade(ratDef));
        pStats.add("Offense Style: " + team.teamStratOff.getStratName() + ">Defense Style: " + team.teamStratDef.getStratName());
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
        pStats.add("Offense Philosophy: " + getLetterGrade(ratOff) + ">Defense Philosophy: " + getLetterGrade(ratDef));
        pStats.add("Offense Style: " + team.teamStratOff.getStratName() + ">Defense Style: " + team.teamStratDef.getStratName());
        pStats.add("Talent Progression: " + getLetterGrade(ratTalent) + ">Discipline: " + getLetterGrade(ratDiscipline));
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
}