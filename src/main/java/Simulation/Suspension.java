package Simulation;

import java.util.Random;

public class Suspension {

    private static final String[] issue = {"Academics", "Fighting", "DUI", "Skipping Class", "Skipping Practice"};
    private static final Random rando = new Random();
    public League league;
    private int duration;
    private String description;
    private Player player;

    public Suspension(int dur, String descrip, Player p) {
        duration = dur;
        description = descrip;
        player = p;
        player.isSuspended = true;
    }

    public Suspension(Player p) {
        duration = (int)(Math.random()*(65 - player.personality));
        if (duration == 0) duration = 1;
        if (Math.random() < 0.01) duration = 15;
        description = issue[(int) (Math.random() * issue.length)];
        player = p;
        player.isSuspended = true;
        player.ratPot -= duration / 1.5;
        player.ratFootIQ -= duration * 2;

        if (player.ratOvr > 75) {
            player.team.league.newsStories.get(player.team.league.currentWeek + 1).add("Star Suspension>" + player.team.name + "'s star " + player.position + ", " + player.name + " was suspended from the team today. The team cited the reason as: " + description
                    + " . The player will be suspended for " + duration + " weeks.");
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
            player.isSuspended = false;
            player.suspension = null;
        }
    }

    public String toString() {
        return description + " (" + duration + " wk) Sus";
    }
}