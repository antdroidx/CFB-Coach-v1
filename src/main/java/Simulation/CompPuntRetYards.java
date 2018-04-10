package Simulation;

import java.util.Comparator;

public class CompPuntRetYards implements Comparator<PlayerReturner> {

    public int compare(PlayerReturner a, PlayerReturner b) {
        return a.pYards > b.pYards ? -1 : a.pYards == b.pYards ? 0 : 1;
    }
}
