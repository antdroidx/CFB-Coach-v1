package Simulation;

import java.util.Comparator;

/**
 * Created by ahngu on 11/13/2017.
 */


class CompTeamOPYPG implements Comparator<Team> {
    @Override
    public int compare(Team a, Team b) {
        return a.teamOppPassYards / a.numGames() < b.teamOppPassYards / b.numGames() ? -1 : a.teamOppPassYards / a.numGames() == b.teamOppPassYards / b.numGames() ? 0 : 1;
    }
}
