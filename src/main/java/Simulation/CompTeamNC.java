package Simulation;

import java.util.Comparator;

/**
 * Created by Anthony on 12/30/2017.
 */

public class CompTeamNC implements Comparator<Team> {
    @Override
    public int compare(Team a, Team b) {
        return a.totalNCs > b.totalNCs ? -1 : a.totalNCs == b.totalNCs ? 0 : 1;
    }
}
