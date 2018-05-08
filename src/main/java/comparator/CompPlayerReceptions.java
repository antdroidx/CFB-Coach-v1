package comparator;

import java.util.Comparator;

import positions.PlayerOffense;

/**
 * Created by ahngu on 11/13/2017.
 */

public class CompPlayerReceptions implements Comparator<PlayerOffense> {
    @Override
    public int compare(PlayerOffense a, PlayerOffense b) {
        return a.receptions > b.receptions ? -1 : a.receptions == b.receptions ? 0 : 1;
    }
}
