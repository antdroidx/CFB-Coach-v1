package Simulation;

import java.util.Comparator;

public class CompKickRetYards implements Comparator<PlayerReturner> {

    public int compare(PlayerReturner a, PlayerReturner b) {
        return a.kYards > b.kYards ? -1 : a.kYards == b.kYards ? 0 : 1;
    }
}
