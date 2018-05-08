package positions;

public class PlayerST {
    private final String team;
    private final String name;
    private final String position;
    public final int ratSpeed;
    private final int ratTackle;

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
