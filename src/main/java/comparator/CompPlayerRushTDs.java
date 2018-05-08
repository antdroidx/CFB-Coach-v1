package comparator;

import java.util.Comparator;

import positions.PlayerOffense;

/**
 * Created by ahngu on 11/13/2017.
 */

public class CompPlayerRushTDs implements Comparator<PlayerOffense> {
    @Override
    public int compare(PlayerOffense a, PlayerOffense b) {
        return a.rushTDs > b.rushTDs ? -1 : a.rushTDs == b.rushTDs ? 0 : 1;
    }
}
