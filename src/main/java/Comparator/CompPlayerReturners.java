package Comparator;

import java.util.Comparator;

import Positions.PlayerReturner;

public class CompPlayerReturners implements Comparator<PlayerReturner> {

    public int compare(PlayerReturner a, PlayerReturner b) {
        return a.ratSpeed > b.ratSpeed ? -1 : a.ratSpeed == b.ratSpeed ? 0 : 1;
    }
}
