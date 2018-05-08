package comparator;

import java.util.Comparator;

import positions.PlayerK;

/**
 * Created by ahngu on 11/13/2017.
 */

public class CompPlayerFGMade implements Comparator<PlayerK> {
    @Override
    public int compare(PlayerK a, PlayerK b) {
        return a.statsFGMade > b.statsFGMade ? -1 : a.statsFGMade == b.statsFGMade ? 0 : 1;
    }
}
