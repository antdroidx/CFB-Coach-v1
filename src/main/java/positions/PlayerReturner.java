package positions;

public class PlayerReturner {
    public final String team;
    public final String name;
    public final String position;
    public int ratSpeed;
    public int ratEvasion;
    public int kYards;
    public int kReturns;
    public int kTD;
    public int pYards;
    public int pReturns;
    public int pTD;
    public int startPos;
    public float gameSpeed;

    public PlayerReturner(String tm, String nm, String pos, int speed, float gs) {
        team = tm;
        name = nm;
        position = pos;
        ratSpeed = speed;
        gameSpeed = gs;
        int yards = 0;
    }

    public PlayerReturner(String tm, String nm, String pos, int kr, int ky, int kt, int pr, int py, int pt) {
        team = tm;
        name = nm;
        position = pos;
        kReturns = kr;
        kYards = ky;
        kTD = kt;
        pReturns = pr;
        pYards = py;
        pTD = pt;
    }

    public String getInitialName() {
        String[] names = name.split(" ");
        return names[0].substring(0, 1) + ". " + names[1];
    }

}
