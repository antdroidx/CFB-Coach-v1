package comparator;

import java.util.Comparator;

import simulation.Team;

/**
 * Created by ahngu on 11/13/2017.
 */

public class CompTeamYPG implements Comparator<Team> {
    @Override
    public int compare(Team a, Team b) {
        return (float) a.teamYards / a.numGames() > (float) b.teamYards / b.numGames() ? -1 : (float) a.teamYards / a.numGames() == (float) b.teamYards / b.numGames() ? 0 : 1;
    }
}

