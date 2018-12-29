package comparator;

import java.util.Comparator;

import positions.HeadCoach;

public class CompCoachCOTY implements Comparator<HeadCoach> {
    @Override
    public int compare(HeadCoach a, HeadCoach b) {
        return a.awards > b.awards ? -1 : a.awards == b.awards ? 0 : 1;
    }
}