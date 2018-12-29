package comparator;

import java.util.Comparator;

import positions.HeadCoach;

public class CompCoachBowlWins implements Comparator<HeadCoach> {
    @Override
    public int compare(HeadCoach a, HeadCoach b) {
        return a.bowlwins > b.bowlwins ? -1 : a.bowlwins == b.bowlwins ? 0 : 1;
    }
}
