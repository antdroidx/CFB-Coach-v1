package Comparator;

import java.util.Comparator;
import Simulation.Team;

/**
 * Created by Anthony on 1/1/2018.
 */

public class CompTeamWins implements Comparator<Team> {
    @Override
    public int compare(Team a, Team b) {
        return a.totalWins > b.totalWins ? -1 : a.totalWins == b.totalWins ? 0 : 1;
    }
}