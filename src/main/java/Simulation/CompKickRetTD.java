package Simulation;

import java.util.Comparator;

public class CompKickRetTD implements Comparator<PlayerReturner> {

    public int compare(PlayerReturner a, PlayerReturner b) {
        return a.kTD > b.kTD ? -1 : a.kTD == b.kTD ? 0 : 1;
    }
}
