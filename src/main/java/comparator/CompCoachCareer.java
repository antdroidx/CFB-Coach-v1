package comparator;

import java.util.Comparator;

import positions.HeadCoach;

/**
 * Created by Anthony on 12/30/2017.
 */

public class CompCoachCareer implements Comparator<HeadCoach> {
    @Override
    public int compare(HeadCoach a, HeadCoach b) {
        return a.getCoachCareerScore() > b.getCoachCareerScore() ? -1 : a.getCoachCareerScore() == b.getCoachCareerScore() ? 0 : 1;
    }
}
