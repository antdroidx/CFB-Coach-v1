package comparator;

import java.util.Comparator;

import simulation.Team;

public class CompTeamDisciplineScore implements Comparator<Team> {
    @Override
    public int compare(Team a, Team b) {
            return a.teamDisciplineScore > b.teamDisciplineScore ? -1 : a.teamDisciplineScore == b.teamDisciplineScore ? 0 : 1;
    }
}
