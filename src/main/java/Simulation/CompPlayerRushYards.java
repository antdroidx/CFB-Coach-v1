package Simulation;

import java.util.Comparator;

/**
 * Created by ahngu on 11/13/2017.
 */

class CompPlayerRushYards implements Comparator<PlayerRB> {
    @Override
    public int compare(PlayerRB a, PlayerRB b) {
        return a.statsRushYards > b.statsRushYards ? -1 : a.statsRushYards == b.statsRushYards ? 0 : 1;
    }
}
