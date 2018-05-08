package comparator;

import java.util.Comparator;

import simulation.Team;

/**
 * Created by ahngu on 11/13/2017.
 */

//EXTRACTED FROM CONFERENCES

public class CompTeamConfWins implements Comparator<Team> {
    @Override
    public int compare(Team a, Team b) {
        if (a.confChampion.equals("CC")) return -1;
        else if (b.confChampion.equals("CC")) return 1;
        else if (a.getConfWins() > b.getConfWins()) {
            return -1;
        } else if (a.getConfWins() == b.getConfWins()) {
            //check for h2h tiebreaker
            if (a.gameWinsAgainst.contains(b)) {
                return -1;
            } else if (b.gameWinsAgainst.contains(a)) {
                return 1;
            } else {
                return 0;
            }
        } else {
            return 1;
        }
    }
}
