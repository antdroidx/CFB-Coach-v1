package comparator;

import java.util.Comparator;

import positions.PlayerDefense;

/**
 * Created by ahngu on 11/13/2017.
 */

public class CompPlayerFumblesRec implements Comparator<PlayerDefense> {
    @Override
    public int compare(PlayerDefense a, PlayerDefense b) {
        return a.fumbles > b.fumbles ? -1 : a.fumbles == b.fumbles ? 0 : 1;
    }
}