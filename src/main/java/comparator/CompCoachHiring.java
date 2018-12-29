package comparator;

import java.util.Comparator;

import positions.HeadCoach;

public class CompCoachHiring implements Comparator<HeadCoach> {
    @Override
    public int compare(HeadCoach a, HeadCoach b) {
        return a.getHCHiring() > b.getHCHiring() ? -1 : a.getHCHiring() == b.getHCHiring() ? 0 : 1;
    }
}
