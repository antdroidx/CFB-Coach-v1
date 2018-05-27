package comparator;

import java.util.Comparator;

import positions.Player;

/**
 * Created by Anthony on 1/2/2018.
 */

public class CompGamePlayerPicker implements Comparator<Player> {
    @Override
    public int compare(Player a, Player b) {
        return a.gameSim > b.gameSim ? -1 : a.gameSim == b.gameSim ? 0 : 1;
    }
}

