package comparator;

import java.util.Comparator;

import simulation.Team;

/**
 * Created by ahngu on 11/13/2017.
 */

public class CompTeamPPG implements Comparator<Team> {
    @Override
    public int compare(Team a, Team b) {
        return (float) a.teamPoints / a.numGames() > (float) b.teamPoints / b.numGames() ? -1 : (float) a.teamPoints / a.numGames() == (float) b.teamPoints / b.numGames() ? 0 : 1;
    }
}