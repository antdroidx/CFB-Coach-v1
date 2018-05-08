package comparator;

import java.util.Comparator;
import simulation.Team;

/**
 * Created by ahngu on 11/13/2017.
 */

public class CompTeamPoll implements Comparator<Team> {
    @Override
    public int compare(Team a, Team b) {
        return a.teamPollScore > b.teamPollScore ? -1 : a.teamPollScore == b.teamPollScore ? 0 : 1;
    }
}
