package Simulation;

import java.util.Comparator;

/**
 * Created by ahngu on 11/13/2017.
 */

class CompPlayerFumblesRec implements Comparator<PlayerDefense> {
    @Override
    public int compare(PlayerDefense a, PlayerDefense b) {
        return a.fumbles > b.fumbles ? -1 : a.fumbles == b.fumbles ? 0 : 1;
    }
}