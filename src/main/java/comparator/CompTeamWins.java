package comparator;

import java.util.Comparator;

import simulation.Team;

/**
 * Created by Anthony on 1/1/2018.
 */

public class CompTeamWins implements Comparator<Team> {
    @Override
    public int compare(Team a, Team b) {
        if (a.league.currentWeek > 15)
            return a.totalWins > b.totalWins ? -1 : a.totalWins == b.totalWins ? 0 : 1;
        else
            return a.totalWins + a.wins > b.totalWins + b.wins ? -1 : a.totalWins + a.wins == b.totalWins + b.wins ? 0 : 1;
    }
}