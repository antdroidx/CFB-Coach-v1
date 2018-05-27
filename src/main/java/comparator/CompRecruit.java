package comparator;

/**
 * Created by ahngu on 11/13/2017.
 */
//EXTRACTED FROM TEAM

import java.util.Comparator;

import positions.Player;

/**
 * Comparator used to sort players by overall
 */
public class CompRecruit implements Comparator<Player> {
    @Override
    public int compare(Player a, Player b) {
        float aRating = (float) ((4 * a.ratOvr + a.ratPot) / 5);
        float bRating = (float) ((4 * b.ratOvr + b.ratPot) / 5);

        return aRating > bRating ? -1 : aRating == bRating ? 0 : 1;
    }
}
