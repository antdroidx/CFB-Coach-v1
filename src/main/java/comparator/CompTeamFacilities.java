package comparator;

import java.util.Comparator;

import simulation.Team;

public class CompTeamFacilities implements Comparator<Team> {
    @Override
    public int compare(Team a, Team b) {
        return a.teamFacilities > b.teamFacilities ? -1 : a.teamFacilities == b.teamFacilities ? 0 : 1;
    }
}