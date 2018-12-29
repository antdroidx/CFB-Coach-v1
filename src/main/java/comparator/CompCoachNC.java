package comparator;

import java.util.Comparator;

import positions.HeadCoach;

public class CompCoachNC  implements Comparator<HeadCoach> {
    @Override
    public int compare(HeadCoach a, HeadCoach b) {
        return a.natchamp > b.natchamp ? -1 : a.natchamp == b.natchamp ? 0 : 1;
    }
}
