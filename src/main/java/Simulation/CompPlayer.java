package Simulation;

/**
 * Created by ahngu on 11/13/2017.
 */

import java.util.Comparator;


//EXTRACTED FROM TEAM

/**
 * Comparator used to sort players by overall
 */
public class CompPlayer implements Comparator<Player> {
    @Override
    public int compare(Player a, Player b) {
        if (!a.isInjured && !b.isInjured) {
            // If both players aren't injured
            if (a.year > 0 && b.year > 0) {
                if (!a.isTransfer && !b.isTransfer) {
                    // If both players aren't redshirted
                    if (a.ratOvr > b.ratOvr) return -1;
                    else if (a.ratOvr == b.ratOvr)
                        return a.ratPot > b.ratPot ? -1 : a.ratPot == b.ratPot ? 0 : 1;
                    else return 1;
                } else if (!a.isTransfer) {
                    return -1;
                } else if (!b.isTransfer) {
                    return 1;
                } else if (a.year > 0) {
                    return -1;
                } else if (b.year > 0) {
                    return 1;
                } else {
                    return a.ratOvr > b.ratOvr ? -1 : a.ratOvr == b.ratOvr ? 0 : 1;
                }
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
