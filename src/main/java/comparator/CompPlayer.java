package comparator;

/**
 * Created by ahngu on 11/13/2017.
 */

import java.util.Comparator;

import positions.Player;


//EXTRACTED FROM TEAM

/**
 * Comparator used to sort players by overall
 */
public class CompPlayer implements Comparator<Player> {
    @Override
    public int compare(Player a, Player b) {
        if (!a.isInjured && !b.isInjured) {
            // If both players aren't injured
            if (!a.isRedshirt && !b.isRedshirt) {
                if (!a.isTransfer && !b.isTransfer) {
                    // If both players aren't redshirted
                    if (!a.isSuspended && !b.isSuspended) {
                        if (a.ratOvr > b.ratOvr) return -1;
                        else if (a.ratOvr == b.ratOvr)
                            return a.ratPot > b.ratPot ? -1 : a.ratPot == b.ratPot ? 0 : 1;
                        else return 1;
                    } else if (!a.isSuspended) {
                        return -1;
                    } else if (!b.isSuspended) {
                        return 1;
                    }
                } else if (!a.isTransfer) {
                    return -1;
                } else if (!b.isTransfer) {
                    return 1;
                }
            } else if (!a.isRedshirt) {
                return -1;
            } else if (!b.isRedshirt) {
                return 1;
            } else {
                return a.ratOvr > b.ratOvr ? -1 : a.ratOvr == b.ratOvr ? 0 : 1;
            }
        } else if (!a.isInjured) {
            return -1;
        } else if (!b.isInjured) {
            return 1;
        } else {
            return a.ratOvr > b.ratOvr ? -1 : a.ratOvr == b.ratOvr ? 0 : 1;
        }
        return a.ratOvr > b.ratOvr ? -1 : a.ratOvr == b.ratOvr ? 0 : 1;

    }
}
