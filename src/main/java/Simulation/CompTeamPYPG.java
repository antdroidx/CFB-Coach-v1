package Simulation;

import java.util.Comparator;

/**
 * Created by ahngu on 11/13/2017.
 */

class CompTeamPYPG implements Comparator<Team> {
    @Override
    public int compare(Team a, Team b) {
        return a.teamPassYards / a.numGames() > b.teamPassYards / b.numGames() ? -1 : a.teamPassYards / a.numGames() == b.teamPassYards / b.numGames() ? 0 : 1;
    }
}
