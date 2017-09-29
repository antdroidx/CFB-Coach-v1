package CFBsimPack;

import java.util.Random;

/**
 * Injury class.
 * Each injured player has one.
 * Describes what the injury is, and how long it lasts.
 * Created by Achi Jones on 4/3/2016.
 */
public class Injury {

    private int duration; // Duration of the injury (in games)
    private String description; // What the injury is
    private Player player; // Player that has this injury

    private static final String[] injuries = {"Knee", "Head", "Shoulder", "Wrist", "Ankle", "Foot", "Arm", "Back", "Brain"};
    private static final Random rando = new Random();

    public Injury(int dur, String descrip, Player p) {
        duration = dur;
        description = descrip;
        player = p;
        player.isInjured = true;
    }

    public Injury(Player p) {
        // Generate an injury
        duration = Math.abs((int)(rando.nextGaussian()*3 + 1));
        if (duration == 0) duration = 1;
        if (Math.random() < 0.01) duration = 15;
        description = injuries[ (int)(Math.random() * injuries.length) ];
        player = p;
        player.isInjured = true;
    }

    public int getDuration() {
        return duration;
    }

    public String getDescription() {
        return description;
    }

    public void advanceGame() {
        duration--;
        if (duration <= 0) {
            // Done with injury
            player.isInjured = false;
            player.injury = null;
        }
    }

    public String toString() {
        return description + " (" + duration + " wk)";
    }
}
