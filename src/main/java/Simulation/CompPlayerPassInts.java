package Simulation;

import java.util.Comparator;

/**
 * Created by ahngu on 11/13/2017.
 */

class CompPlayerPassInts implements Comparator<PlayerQB> {
    @Override
    public int compare(PlayerQB a, PlayerQB b) {
        return a.statsInt > b.statsInt ? -1 : a.statsInt == b.statsInt ? 0 : 1;
    }
}
