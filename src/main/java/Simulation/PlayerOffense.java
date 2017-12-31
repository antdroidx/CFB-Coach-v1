package Simulation;

/**
 * Created by Anthony on 12/30/2017.
 */

public class PlayerOffense {


        public Team team;
        public String name;
        public String position;
        public int year;
        public int rushYards;
        public int rushTDs;
        public int receptions;
        public int receptionYards;
        public int receptionTDs;
        public int fumbles;

        public PlayerOffense(Team tm, String nm, String pos, int yr, int ryards, int rTDs, int rec, int recYards, int recTDs, int fmb) {
            team = tm;
            name = nm;
            position = pos;
            year = yr;
            rushYards = ryards;
            rushTDs = rTDs;
            receptions = rec;
            receptionYards = recYards;
            receptionTDs = recTDs;
            fumbles = fmb;
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
