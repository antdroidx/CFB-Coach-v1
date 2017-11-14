package Simulation;

import java.util.Comparator;

/**
 * Created by ahngu on 11/13/2017.
 */
//League

class CompPlayerPassRating implements Comparator<PlayerQB> {
    @Override
    public int compare(PlayerQB a, PlayerQB b) {
        return a.getPasserRating() > b.getPasserRating() ? -1 : a.getPasserRating() == b.getPasserRating() ? 0 : 1;
    }
}

