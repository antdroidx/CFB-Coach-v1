package comparator;

import java.util.Comparator;

import simulation.Team;

/**
 * Created by ahngu on 11/13/2017.
 */

public class CompTeamPoll implements Comparator<Team> {
    @Override
    public int compare(Team a, Team b) {
        if (a.teamPollScore > b.teamPollScore) {
            return -1;

        } else if (b.teamPollScore == a.teamPollScore) {
            //check for  tiebreaker
            if (a.teamStrengthOfWins > b.teamStrengthOfWins) {
                return -1;
            } else if (a.teamStrengthOfWins < b.teamStrengthOfWins) {
                return 1;
            } else return 0;

        } else return 1;
    }
}
