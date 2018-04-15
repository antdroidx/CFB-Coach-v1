package Comparator;

import java.util.Comparator;

import Positions.PlayerReturner;

public class CompPuntRetTDs implements Comparator<PlayerReturner> {

    public int compare(PlayerReturner a, PlayerReturner b) {
        return a.pTD > b.pTD ? -1 : a.pTD == b.pTD ? 0 : 1;
    }
}
