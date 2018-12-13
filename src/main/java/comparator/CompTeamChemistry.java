package comparator;

import java.util.Comparator;

import simulation.Team;

public class CompTeamChemistry  implements Comparator<Team> {
    @Override
    public int compare(Team a, Team b) {
        return a.getTeamChemistry() > b.getTeamChemistry() ? -1 : a.getTeamChemistry() == b.getTeamChemistry() ? 0 : 1;
    }
}
