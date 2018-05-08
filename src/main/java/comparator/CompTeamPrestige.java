package comparator;

import java.util.Comparator;
import simulation.Team;

/**
 * Created by ahngu on 11/13/2017.
 */

public class CompTeamPrestige implements Comparator<Team> {
    @Override
    public int compare(Team a, Team b) {
        return a.teamPrestige > b.teamPrestige ? -1 : a.teamPrestige == b.teamPrestige ? 0 : 1;
    }
}
