package Simulation;

/**
 * Created by ahngu on 11/13/2017.
 */
//EXTRACTED FROM TEAM

import java.util.Comparator;

/**
 * Comparator used to sort players by overall
 */
public class RecruitComparator implements Comparator<Player> {
    @Override
    public int compare(Player a, Player b) {
        return a.cost > b.cost ? -1 : a.cost == b.cost ? 0 : 1;
    }
}
