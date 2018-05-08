package comparator;

import java.util.Comparator;

import simulation.Team;

/**
 * Created by Anthony on 12/30/2017.
 */

public class CompTeamBowls implements Comparator<Team> {
    @Override
    public int compare(Team a, Team b) {
        return a.totalBowls > b.totalBowls ? -1 : a.totalBowls == b.totalBowls ? 0 : 1;
    }
}
