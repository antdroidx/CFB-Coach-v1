package Comparator;

import java.util.Comparator;

import Simulation.Team;

public class CompConfTeamRanking implements Comparator<Team> {
    public int compare(Team a, Team b) {

        return a.teamPowerRating() > b.teamPowerRating() ? -1 : a.teamPowerRating() == b.teamPowerRating() ? 0 : 1;
    }
}
