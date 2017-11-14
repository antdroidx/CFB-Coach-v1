package Simulation;

import java.util.Comparator;

/**
 * Created by ahngu on 11/13/2017.
 */

class CompPlayerRecYards implements Comparator<PlayerWR> {
    @Override
    public int compare(PlayerWR a, PlayerWR b) {
        return a.statsRecYards > b.statsRecYards ? -1 : a.statsRecYards == b.statsRecYards ? 0 : 1;
    }
}

