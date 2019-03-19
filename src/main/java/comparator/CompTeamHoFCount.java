package comparator;

import java.util.Comparator;

import simulation.Team;

public class CompTeamHoFCount implements Comparator<Team> {
    @Override
    public int compare(Team a, Team b) {
        return a.HoFCount > b.HoFCount ? -1 : a.HoFCount == b.HoFCount ? 0 : 1;
    }
}

