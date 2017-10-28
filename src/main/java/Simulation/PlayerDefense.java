package Simulation;

import java.util.ArrayList;

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

    public PlayerDefense(Team tm, String nm, String pos, int yr, int tkl, int sk, int fmb, int ints){
        team = tm;
        name = nm;
        position = pos;
        year = yr;
        tackles = tkl;
        sacks = sk;
        fumbles = fmb;
        interceptions = ints;
    }


    protected final String[] letterGrades = {"F", "F+", "D", "D+", "C", "C+", "B", "B+", "A", "A+"};

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

    public static int getPosNumber(String pos) {
        switch (pos) {
            case "QB":
                return 0;
            case "RB":
                return 1;
            case "WR":
                return 2;
            case "TE":
                return 3;
            case "OL":
                return 4;
            case "K":
                return 5;
            case "DL":
                return 6;
            case "LB":
                return 7;
            case "CB":
                return 8;
            case "S":
                return 9;
            default:
                return 10;

        }
    }

}
