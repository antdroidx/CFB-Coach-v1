package Simulation;

import java.util.Comparator;

/**
 * Created by ahngu on 11/13/2017.
 */

class CompPlayerRecTDs implements Comparator<PlayerWR> {
    @Override
    public int compare(PlayerWR a, PlayerWR b) {
        return a.statsTD > b.statsTD ? -1 : a.statsTD == b.statsTD ? 0 : 1;
    }
}