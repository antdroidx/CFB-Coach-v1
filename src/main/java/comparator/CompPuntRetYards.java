package comparator;

import java.util.Comparator;

import positions.PlayerReturner;

public class CompPuntRetYards implements Comparator<PlayerReturner> {

    public int compare(PlayerReturner a, PlayerReturner b) {
        return a.pYards > b.pYards ? -1 : a.pYards == b.pYards ? 0 : 1;
    }
}
