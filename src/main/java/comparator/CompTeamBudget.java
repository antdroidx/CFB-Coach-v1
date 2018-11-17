package comparator;

import java.util.Comparator;

import simulation.Team;

public class CompTeamBudget implements Comparator<Team> {
    @Override
    public int compare(Team a, Team b) {
        return a.teamBudget > b.teamBudget ? -1 : a.teamBudget == b.teamBudget ? 0 : 1;
    }
}

