package Simulation;

import java.util.Comparator;

/**
 * Created by ahngu on 11/13/2017.
 */

class CompPlayerRushYards implements Comparator<PlayerOffense> {
    @Override
    public int compare(PlayerOffense a, PlayerOffense b) {
        return a.rushYards > b.rushYards ? -1 : a.rushYards == b.rushYards ? 0 : 1;
    }
}
