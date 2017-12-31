package Simulation;

import java.util.Comparator;

/**
 * Created by ahngu on 11/13/2017.
 */

class CompPlayerRecTDs implements Comparator<PlayerOffense> {
    @Override
    public int compare(PlayerOffense a, PlayerOffense b) {
        return a.receptionTDs > b.receptionTDs ? -1 : a.receptionTDs == b.receptionTDs ? 0 : 1;
    }
}