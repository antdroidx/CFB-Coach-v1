package comparator;

/**
 * Created by ahngu on 11/13/2017.
 */

//EXTRACTED FROM TEAM

import java.util.Comparator;

import positions.Player;

/**
 * Comparator used to sort players by position, QB-RB-WR-OL-K-S-CB-DL
 */
public class CompPlayerPosition implements Comparator<Player> {
    @Override
    public int compare(Player a, Player b) {
        int aPos = Player.getPosNumber(a.position);
        int bPos = Player.getPosNumber(b.position);
        return aPos < bPos ? -1 : aPos == bPos ? 0 : 1;
    }
}
