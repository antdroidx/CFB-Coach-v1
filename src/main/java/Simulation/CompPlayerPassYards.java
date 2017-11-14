package Simulation;

import java.util.Comparator;

/**
 * Created by ahngu on 11/13/2017.
 */

class CompPlayerPassYards implements Comparator<PlayerQB> {
    @Override
    public int compare(PlayerQB a, PlayerQB b) {
        return a.statsPassYards > b.statsPassYards ? -1 : a.statsPassYards == b.statsPassYards ? 0 : 1;
    }
}
