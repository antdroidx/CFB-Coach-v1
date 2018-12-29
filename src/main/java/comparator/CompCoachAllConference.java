package comparator;

import java.util.Comparator;

import positions.HeadCoach;

public class CompCoachAllConference implements Comparator<HeadCoach> {
    @Override
    public int compare(HeadCoach a, HeadCoach b) {
        return a.allconference > b.allconference ? -1 : a.allconference == b.allconference ? 0 : 1;
    }
}