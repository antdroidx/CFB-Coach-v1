package Positions;

public class PlayerST {
    public String team;
    public String name;
    public String position;
    public int ratSpeed;
    public int ratTackle;

    public PlayerST(String tm, String nm, String pos, int speed, int tackle) {
        team = tm;
        name = nm;
        position = pos;
        ratSpeed = speed;
        ratTackle = tackle;
    }

    public String getInitialName() {
        String[] names = name.split(" ");
        return names[0].substring(0, 1) + ". " + names[1];
    }
}
