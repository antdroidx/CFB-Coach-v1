package comparator;

import java.util.Comparator;

import simulation.Team;

/**
 * Created by ahngu on 11/13/2017.
 */

public class CompTeamPYPG implements Comparator<Team> {
    @Override
    public int compare(Team a, Team b) {
        return (float) a.teamPassYards / a.numGames() > (float) b.teamPassYards / b.numGames() ? -1 : (float) a.teamPassYards / a.numGames() == (float) b.teamPassYards / b.numGames() ? 0 : 1;
    }
}
