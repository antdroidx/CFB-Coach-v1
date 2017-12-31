package Simulation;

import java.util.Comparator;

/**
 * Created by ahngu on 11/13/2017.
 */

class CompPlayerRecYards implements Comparator<PlayerOffense> {
    @Override
    public int compare(PlayerOffense a, PlayerOffense b) {
        return a.receptionYards > b.receptionYards ? -1 : a.receptionYards == b.receptionYards ? 0 : 1;
    }
}

