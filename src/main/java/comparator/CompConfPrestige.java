package comparator;

import java.util.Comparator;

import simulation.Conference;
import simulation.Team;

public class CompConfPrestige implements Comparator<Conference> {
    public int compare(Conference a, Conference b) {

        return a.confPrestige > b.confPrestige ? -1 : a.confPrestige == b.confPrestige ? 0 : 1;
    }
}
