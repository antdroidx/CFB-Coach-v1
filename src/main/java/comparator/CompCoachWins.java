package comparator;

import java.util.Comparator;

import positions.HeadCoach;

public class CompCoachWins implements Comparator<HeadCoach> {
    @Override
    public int compare(HeadCoach a, HeadCoach b) {
        return a.wins+a.teamWins > b.wins+b.teamWins ? -1 : a.wins+a.teamWins == b.wins+b.teamWins ? 0 : 1;
    }
}
