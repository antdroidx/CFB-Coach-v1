package Simulation;

import java.util.Comparator;

/**
 * Created by ahngu on 11/13/2017.
 */


class CompTeamSoW implements Comparator<Team> {
    @Override
    public int compare(Team a, Team b) {
        return a.teamStrengthOfWins > b.teamStrengthOfWins ? -1 : a.teamStrengthOfWins == b.teamStrengthOfWins ? 0 : 1;
    }
}
