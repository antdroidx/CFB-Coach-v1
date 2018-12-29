package comparator;

import java.util.Comparator;

import positions.HeadCoach;

public class CompCoachWinPCT implements Comparator<HeadCoach> {
    @Override
    public int compare(HeadCoach a, HeadCoach b) {
        return a.getWinPCT() > b.getWinPCT() ? -1 : a.getWinPCT() == b.getWinPCT() ? 0 : 1;
    }
}