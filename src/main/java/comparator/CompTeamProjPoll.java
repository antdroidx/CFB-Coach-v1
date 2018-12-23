package comparator;

import java.util.Comparator;

import simulation.Team;

public class CompTeamProjPoll implements Comparator<Team> {
    @Override
    public int compare(Team a, Team b) {
        if (a.projectedPollScore > b.projectedPollScore) {
            return -1;

        } else if (b.projectedPollScore == a.projectedPollScore) {
            //check for  tiebreaker
            if (a.teamOffTalent + a.teamDefTalent > b.teamOffTalent + b.teamDefTalent) {
                return -1;
            } else if (a.teamOffTalent + a.teamDefTalent < b.teamOffTalent + b.teamDefTalent) {
                return 1;
            } else return 0;

        } else return 1;
    }
}
