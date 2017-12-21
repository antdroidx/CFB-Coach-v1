package Simulation;

import java.util.Random;

/**
 * Injury class.
 * Each injured player has one.
 * Describes what the injury is, and how long it lasts.
 * Created by Achi Jones on 4/3/2016.
 */
public class Injury {

    private static final String[] injuries = {"Knee", "Thigh", "Shoulder", "Wrist", "Ankle", "Foot", "Arm", "Back", "Head"};
    private static final Random rando = new Random();
    public League league;
    private int duration; // Duration of the injury (in games)
    private String description; // What the injury is
    private Player player; // Player that has this injury

    public Injury(int dur, String descrip, Player p) {
        duration = dur;
        description = descrip;
        player = p;
        player.isInjured = true;
    }

    public Injury(Player p) {
        // Generate an injury
        duration = Math.abs((int) (rando.nextGaussian() * 3 + 1));
        if (duration == 0) duration = 1;
        if (Math.random() < 0.01) duration = 15;
        description = injuries[(int) (Math.random() * injuries.length)];
        player = p;
        player.isInjured = true;
        player.ratPot -= duration / 1.5;
        player.ratDur -= duration;
        if (player.ratDur < 0) player.ratDur = 0;
        if (player.ratOvr > 80 && duration > 4) {
            player.team.league.newsStories.get(player.team.league.currentWeek + 1).add("Injury Report>A major injury was sustained by " + player.team.name + "'s star " + player.position + ", " + player.name + " today. During the game, " + player.name + " suffered a " + description
                    + " injury and will be out for " + duration + " weeks.");
        }

        if (duration > 12 && player.team.league.currentWeek < 6 && player.gamesPlayed < 4) {
            player.isMedicalRS = true;
            duration = 26;
            if (player.team.userControlled) {
                player.team.league.newsStories.get(player.team.league.currentWeek + 1).add("Medical Redshirt>" + player.name + " sustained a major " + description + " injury and will be out for the season. A medical redshirt has been granted.");
            }
            if (!player.team.userControlled && player.ratOvr > 79) {
                player.team.league.newsStories.get(player.team.league.currentWeek + 1).add("Medical Redshirt>" + player.name + " sustained a major " + description + " injury and will be out for the season. A medical redshirt has been granted.");
            }
        }

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
        if (player.isMedicalRS) {
            return description + " (" + duration + " wk) MedRS";
        } else {
            return description + " (" + duration + " wk)";
        }
    }
}
