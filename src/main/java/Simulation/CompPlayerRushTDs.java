package Simulation;

import java.util.Comparator;

/**
 * Created by ahngu on 11/13/2017.
 */

class CompPlayerRushTDs implements Comparator<PlayerRB> {
    @Override
    public int compare(PlayerRB a, PlayerRB b) {
        return a.statsRushTD > b.statsRushTD ? -1 : a.statsRushTD == b.statsRushTD ? 0 : 1;
    }
}
