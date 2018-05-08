package comparator;


import java.util.Comparator;

import positions.PlayerST;

public class CompPlayerSTSpeed implements Comparator<PlayerST> {

    public int compare(PlayerST a, PlayerST b) {
        return a.ratSpeed > b.ratSpeed ? -1 : a.ratSpeed == b.ratSpeed ? 0 : 1;
    }
}
