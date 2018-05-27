package comparator;

import java.util.Comparator;

import simulation.Team;

/**
 * Created by ahngu on 11/14/2017.
 */

class CompTeamDiscipline implements Comparator<Team> {
    @Override
    public int compare(Team a, Team b) {
        return a.teamDiscipline > b.teamDiscipline ? -1 : a.teamDiscipline == b.teamDiscipline ? 0 : 1;
    }
}

