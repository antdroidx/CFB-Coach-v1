package comparator;

import java.util.Comparator;

import simulation.Team;

public class CompTeamProjPoll implements Comparator<Team> {
    @Override
    public int compare(Team a, Team b) {
        return a.projectedPollScore > b.projectedPollScore ? -1 : a.projectedPollScore == b.projectedPollScore ? 0 : 1;
    }
}
