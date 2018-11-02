package comparator;

import java.util.Comparator;

import simulation.Team;

/**
 * Created by ahngu on 11/13/2017.
 */


public class CompTeamOPYPG implements Comparator<Team> {
    @Override
    public int compare(Team a, Team b) {
        return (float) a.teamOppPassYards / a.numGames() < (float) b.teamOppPassYards / b.numGames() ? -1 : (float) a.teamOppPassYards / a.numGames() == (float) b.teamOppPassYards / b.numGames() ? 0 : 1;
    }
}
