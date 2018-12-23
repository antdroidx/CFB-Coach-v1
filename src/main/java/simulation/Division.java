package simulation;

import java.util.ArrayList;

public class Division {

    public String divName;

    public final ArrayList<Team> divTeams;

    private final League league;

    public Division(String name, League league){
        divName = name;
        divTeams = new ArrayList<Team>();
        this.league = league;
    }

    public void setUpDivisionSchedule(int divWeeks) {
        int divSize = divTeams.size()-1;
        Team bye = new Team("BYE", "BYE", "BYE", 0, "BYE", 0, league);

        if(divSize % 2 != 0) {
            divTeams.add(bye);
            divSize = divTeams.size()-1;
            divWeeks++;
        }

        int games = divTeams.size()/2;
        for (int r = 0; r < divWeeks; ++r) {
            for (int g = 0; g < games; ++g) {
                Team a = divTeams.get((r + g) % divSize);
                Team b;
                if (g == 0) {
                    b = divTeams.get(divSize);
                } else {
                    b = divTeams.get((divSize - g + r) % divSize);
                }

                Game gm;

                if (r%2 == 0) {
                    gm = new Game(a, b, "Division");
                } else {
                    gm = new Game(b, a, "Division");
                }

                a.gameSchedule.add(gm);
                b.gameSchedule.add(gm);

            }
        }

        divTeams.remove(bye);

        if(divSize % 2 == 0) {
            for (int g = 0; g < divSize; ++g) {
                Team a = divTeams.get(g);
                a.gameSchedule.add(new Game(a, bye, "BYE WEEK"));
            }
        }
    }

}
