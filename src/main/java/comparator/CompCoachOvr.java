package comparator;

import java.util.Comparator;

import positions.HeadCoach;

public class CompCoachOvr implements Comparator<HeadCoach> {
    @Override
    public int compare(HeadCoach a, HeadCoach b) {
        return a.getHCOverall() > b.getHCOverall() ? -1 : a.getHCOverall() == b.getHCOverall() ? 0 : 1;
    }
}
