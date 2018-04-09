package Simulation;

public class PlayerReturner {
    public String team;
    public String name;
    public String position;
    public int ratSpeed;
    public int ratEvasion;
    public int kYards;
    public int kReturns;
    public int kTD;
    public int pYards;
    public int pReturns;
    public int pTD;
    public int startPos;

    public PlayerReturner(String tm, String nm, String pos, int speed, int evasion) {
        team = tm;
        name = nm;
        position = pos;
        ratSpeed = speed;
        ratEvasion = evasion;
        int yards = 0;
    }

    public String getInitialName() {
        String[] names = name.split(" ");
        return names[0].substring(0, 1) + ". " + names[1];
    }

}
