package Comparator;

import java.util.Comparator;

import Positions.PlayerReturner;

public class CompKickRetYards implements Comparator<PlayerReturner> {

    public int compare(PlayerReturner a, PlayerReturner b) {
        return a.kYards > b.kYards ? -1 : a.kYards == b.kYards ? 0 : 1;
    }
}
