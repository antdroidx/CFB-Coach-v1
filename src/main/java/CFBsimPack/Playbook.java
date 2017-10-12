package CFBsimPack;

/**
 * Created by ahngu on 10/12/2017.
 */

public class Playbook {

    public Team team;

    public int getCPUDefense() {
        int DP, DR, DS = 0;
        DP = team.getPassProf();
        DR = team.getRushProf();
        if(DR < (DP + 5)) {
            DS = 0;
        } else if(DP < (DR + 5)) {
            DS = 2;
        } else DS = 1;
        return DS;
    }

    /**
     * Generate all the offense team strategies that can be selected
     * @return array of all the offense team strats
     */
    public TeamStrategy[] getTeamStrategiesOff() {
        TeamStrategy[] ts = new TeamStrategy[4];

        ts[0] = new TeamStrategy("Pro-Style",
                "Play a normal balanced offense.", 1, 0, 0, 0, 1, 0, 0, 0);

        ts[1] = new TeamStrategy("Smash Mouth",
                "Play a conservative run-heavy offense, setting up the passes as necessary.", 3, 1, 0, 1, 2, 0, 0, 0);

        ts[2] = new TeamStrategy("West Coast",
                "Passing game dictates the run game with short accurate passes.", 2, 0, 0, 0, 3, 1, 0, 1);

        ts[3] = new TeamStrategy("Spread",
                "Pass-heavy offense using many receivers with big play potential with risk.", 3, 1, 0, 0, 1, 0, 2, 1);

        return ts;
    }



    /**
     * Generate all the defense team strategies that can be selected
     * @return array of all the defense team strats
     */
    public TeamStrategy[] getTeamStrategiesDef() {
        TeamStrategy[] ts = new TeamStrategy[4];

        ts[0] = new TeamStrategy("4-3",
                "Play a standard 4-3 man-to-man balanced defense.", 1, 0, 0, 1, 1, 0 ,0, 1);

        ts[1] = new TeamStrategy("46 Bear Defense",
                "Focus on stopping the run. Will give up more big passing plays but will allow less runing yards and far less big plays from runing.",  1, 0, 0, 1, 1, 0 ,0, 1);

        ts[2] = new TeamStrategy("Cover 2",
                "Play a zone defense with safety help in the back against the pass, while LBs cover the run game. ",  1, 0, 0, 1, 1, 0 ,0, 1);

        ts[3] = new TeamStrategy("Cover 3",
                "Play a zone defense to stop the big plays, but allows soft zone coverage underneath.", 1, 0, 0, 1, 1, 0 ,0, 1);

        return ts;
    }

}
