package comparator;

import java.util.Comparator;

import positions.PlayerK;

/**
 * Created by ahngu on 11/13/2017.
 */

public class CompPlayerFGpct implements Comparator<PlayerK> {
    @Override
    public int compare(PlayerK a, PlayerK b) {
        return (float)a.getFGpct() > (float)b.getFGpct() ? -1 :(float) a.getFGpct() == (float) b.getFGpct() ? 0 : 1;
    }
}
