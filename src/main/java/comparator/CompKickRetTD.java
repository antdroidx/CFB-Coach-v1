package comparator;

import java.util.Comparator;

import positions.PlayerReturner;

public class CompKickRetTD implements Comparator<PlayerReturner> {

    public int compare(PlayerReturner a, PlayerReturner b) {
        return a.kTD > b.kTD ? -1 : a.kTD == b.kTD ? 0 : 1;
    }
}
