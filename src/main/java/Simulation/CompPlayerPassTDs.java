package Simulation;

import java.util.Comparator;

/**
 * Created by ahngu on 11/13/2017.
 */

class CompPlayerPassTDs implements Comparator<PlayerQB> {
    @Override
    public int compare(PlayerQB a, PlayerQB b) {
        return a.statsPassTD > b.statsPassTD ? -1 : a.statsPassTD == b.statsPassTD ? 0 : 1;
    }
}