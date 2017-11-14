package Simulation;

import java.util.Comparator;

/**
 * Created by ahngu on 11/13/2017.
 */

class CompPlayerInterceptions implements Comparator<PlayerDefense> {
    @Override
    public int compare(PlayerDefense a, PlayerDefense b) {
        return a.interceptions > b.interceptions ? -1 : a.interceptions == b.interceptions ? 0 : 1;
    }
}
