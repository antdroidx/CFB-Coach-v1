package comparator;

import java.util.Comparator;

import simulation.Team;

/**
 * Created by ahngu on 11/13/2017.
 */

public class CompTeamPrestige implements Comparator<Team> {
    @Override

    public int compare(Team a, Team b) {
        if (a.teamPrestige > b.teamPrestige) {
            return -1;

        } else if (b.teamPrestige == a.teamPrestige) {
            //check for  tiebreaker
            if (a.confPrestige > b.confPrestige) {
                return -1;
            } else if (a.confPrestige < b.confPrestige) {
                return 1;
            } else {
                if (a.teamOffTalent + a.teamDefTalent > b.teamOffTalent + b.teamDefTalent) {
                    return -1;
                } else if (a.teamOffTalent + a.teamDefTalent < b.teamOffTalent + b.teamDefTalent) {
                    return 1;
                } else return 0;
            }

        } else return 1;
    }
}
