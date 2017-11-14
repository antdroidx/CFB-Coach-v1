package Simulation;

import java.util.Comparator;

/**
 * Created by ahngu on 11/13/2017.
 */
//LEAGUE
class CompCoachScore implements Comparator<HeadCoach> {
    @Override
    public int compare(HeadCoach a, HeadCoach b) {
        return a.getCoachScore() > b.getCoachScore() ? -1 : a.getCoachScore() == b.getCoachScore() ? 0 : 1;
    }
}

