package comparator;

import java.util.Comparator;

import positions.PlayerQB;

/**
 * Created by ahngu on 11/13/2017.
 */

public class CompPlayerPassPCT implements Comparator<PlayerQB> {
    @Override
    public int compare(PlayerQB a, PlayerQB b) {
        return a.getPassPCT() > b.getPassPCT() ? -1 : a.getPassPCT() == b.getPassPCT() ? 0 : 1;
    }
}
