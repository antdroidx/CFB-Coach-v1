package Comparator;

import java.util.Comparator;

import Positions.HeadCoach;

/**
 * Created by ahngu on 11/13/2017.
 */
//LEAGUE
public class CompCoachScore implements Comparator<HeadCoach> {
    @Override
    public int compare(HeadCoach a, HeadCoach b) {
        return a.getCoachScore() > b.getCoachScore() ? -1 : a.getCoachScore() == b.getCoachScore() ? 0 : 1;
    }
}

