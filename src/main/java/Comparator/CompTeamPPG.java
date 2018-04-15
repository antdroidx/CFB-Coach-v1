package Comparator;

import java.util.Comparator;
import Simulation.Team;

/**
 * Created by ahngu on 11/13/2017.
 */

public class CompTeamPPG implements Comparator<Team> {
    @Override
    public int compare(Team a, Team b) {
        return a.teamPoints / a.numGames() > b.teamPoints / b.numGames() ? -1 : a.teamPoints / a.numGames() == b.teamPoints / b.numGames() ? 0 : 1;
    }
}