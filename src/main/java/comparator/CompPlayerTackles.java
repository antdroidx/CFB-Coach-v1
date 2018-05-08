package comparator;

import java.util.Comparator;

import positions.PlayerDefense;

/**
 * Created by ahngu on 11/13/2017.
 */

public class CompPlayerTackles implements Comparator<PlayerDefense> {
    @Override
    public int compare(PlayerDefense a, PlayerDefense b) {
        return a.tackles > b.tackles ? -1 : a.tackles == b.tackles ? 0 : 1;
    }
}