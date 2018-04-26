package Comparator;

import java.util.Comparator;

import Positions.HeadCoach;

public class CompCoachOvr implements Comparator<HeadCoach> {
    @Override
    public int compare(HeadCoach a, HeadCoach b) {
        return a.ratOvr > b.ratOvr ? -1 : a.ratOvr == b.ratOvr ? 0 : 1;
    }
}
