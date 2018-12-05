package comparator;

import java.util.Comparator;

import simulation.Team;

public class CompTeamDivWins implements Comparator<Team> {
    @Override
    public int compare(Team a, Team b) {
        if (a.confChampion.equals("CC")) return -1;
        else if (b.confChampion.equals("CC")) return 1;
        else if (a.getDivWins() > b.getDivWins()) {
            return -1;
        } else if (a.getDivWins() == b.getDivWins()) {
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
