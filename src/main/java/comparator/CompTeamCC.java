package comparator;

import java.util.Comparator;

import simulation.Team;

/**
 * Created by Anthony on 12/30/2017.
 */

public class CompTeamCC implements Comparator<Team> {
    @Override
    public int compare(Team a, Team b) {
        return a.totalCCs > b.totalCCs ? -1 : a.totalCCs == b.totalCCs ? 0 : 1;
    }
}
