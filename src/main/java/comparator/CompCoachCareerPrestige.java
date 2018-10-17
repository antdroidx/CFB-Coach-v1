package comparator;

import java.util.Comparator;

import positions.HeadCoach;

public class CompCoachCareerPrestige implements Comparator<HeadCoach> {
    @Override
    public int compare(HeadCoach a, HeadCoach b) {
        return a.cumulativePrestige > b.cumulativePrestige ? -1 : a.cumulativePrestige == b.cumulativePrestige ? 0 : 1;
    }
}

