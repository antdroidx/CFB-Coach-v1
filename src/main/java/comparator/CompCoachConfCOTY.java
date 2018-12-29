package comparator;

import java.util.Comparator;

import positions.HeadCoach;

public class CompCoachConfCOTY implements Comparator<HeadCoach> {
    @Override
    public int compare(HeadCoach a, HeadCoach b) {
        return a.confAward > b.confAward ? -1 : a.confAward == b.confAward ? 0 : 1;
    }
}
