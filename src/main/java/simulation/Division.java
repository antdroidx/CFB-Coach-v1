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

}
