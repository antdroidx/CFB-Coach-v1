package Simulation;

/**
 * Base player class that others extend. Has name, overall, potential, and football IQ.
 *
 * @author Achi
 */
public class PlayerDefense {

    public Team team;
    public String name;
    public String position;
    public int year;
    public int tackles;
    public int sacks;
    public int fumbles;
    public int interceptions;

    public PlayerDefense(Team tm, String nm, String pos, int yr, int tkl, int sk, int fmb, int ints) {
        team = tm;
        name = nm;
        position = pos;
        year = yr;
        tackles = tkl;
        sacks = sk;
        fumbles = fmb;
        interceptions = ints;
    }


    public String getYrStr() {
        if (year == 0) {
            return "RS";
        } else if (year == 1) {
            return "Fr";
        } else if (year == 2) {
            return "So";
        } else if (year == 3) {
            return "Jr";
        } else if (year == 4) {
            return "Sr";
        }
        return "ERROR";
    }
}
