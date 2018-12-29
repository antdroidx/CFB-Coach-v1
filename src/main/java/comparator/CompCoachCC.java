package comparator;

import java.util.Comparator;

import positions.HeadCoach;

public class CompCoachCC  implements Comparator<HeadCoach> {
    @Override
    public int compare(HeadCoach a, HeadCoach b) {
        return a.confchamp > b.confchamp ? -1 : a.confchamp == b.confchamp ? 0 : 1;
    }
}