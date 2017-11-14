package Simulation;

import java.util.Comparator;

/**
 * Created by ahngu on 11/13/2017.
 */

class CompTeamYPG implements Comparator<Team> {
    @Override
    public int compare(Team a, Team b) {
        return a.teamYards / a.numGames() > b.teamYards / b.numGames() ? -1 : a.teamYards / a.numGames() == b.teamYards / b.numGames() ? 0 : 1;
    }
}

