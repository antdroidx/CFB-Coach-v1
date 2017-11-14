package Simulation;

import java.util.Comparator;

/**
 * Created by ahngu on 11/13/2017.
 */

class CompPlayerReceptions implements Comparator<PlayerWR> {
    @Override
    public int compare(PlayerWR a, PlayerWR b) {
        return a.statsReceptions > b.statsReceptions ? -1 : a.statsReceptions == b.statsReceptions ? 0 : 1;
    }
}
