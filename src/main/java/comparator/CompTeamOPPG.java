package comparator;

import java.util.Comparator;

import simulation.Team;

/**
 * Created by ahngu on 11/13/2017.
 */

public class CompTeamOPPG implements Comparator<Team> {
    @Override
    public int compare(Team a, Team b) {
        return a.teamOppPoints / a.numGames() < b.teamOppPoints / b.numGames() ? -1 : a.teamOppPoints / a.numGames() == b.teamOppPoints / b.numGames() ? 0 : 1;
    }
}
