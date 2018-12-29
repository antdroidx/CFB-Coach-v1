package comparator;

import java.util.Comparator;

import positions.HeadCoach;

public class CompCoachAllAmericans implements Comparator<HeadCoach> {
    @Override
    public int compare(HeadCoach a, HeadCoach b) {
        return a.allamericans > b.allamericans ? -1 : a.allamericans == b.allamericans ? 0 : 1;
    }
}
