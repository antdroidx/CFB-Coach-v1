package Simulation;

import java.util.Comparator;

/**
 * Created by ahngu on 11/13/2017.
 */

class CompTeamRecruitClass implements Comparator<Team> {
    @Override
    public int compare(Team a, Team b) {
        return a.getRecruitingClassRat() > b.getRecruitingClassRat() ? -1 : a.getRecruitingClassRat() == b.getRecruitingClassRat() ? 0 : 1;
    }
}
