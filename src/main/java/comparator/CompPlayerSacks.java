package comparator;

import java.util.Comparator;

import positions.PlayerDefense;

/**
 * Created by ahngu on 11/13/2017.
 */

public class CompPlayerSacks implements Comparator<PlayerDefense> {
    @Override
    public int compare(PlayerDefense a, PlayerDefense b) {
        return a.sacks > b.sacks ? -1 : a.sacks == b.sacks ? 0 : 1;
    }
}
