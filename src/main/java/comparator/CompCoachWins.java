package comparator;

import java.util.Comparator;

import positions.HeadCoach;

public class CompCoachWins implements Comparator<HeadCoach> {
    @Override
    public int compare(HeadCoach a, HeadCoach b) {
        return a.wins+a.careerWins > b.wins+a.careerWins ? -1 : a.wins+a.careerWins == b.wins+a.careerWins ? 0 : 1;
    }
}
