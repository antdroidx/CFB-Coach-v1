package CFBsimPack;

/**
 * Team Strategy class. It works for both offense and defense, just applying the bonus different ways.
 * Created by Achi Jones on 3/10/2016.
 */
public class TeamStrategy {

    private int rushYdBonus;
    private int rushAgBonus;
    private int passYdBonus;
    private int passAgBonus;

    private String stratName;
    private String stratDescription;

    public TeamStrategy(String name, String descrip, int rYB, int rAB, int pYB, int pAB) {
        stratName = name;
        stratDescription = descrip;
        rushYdBonus = rYB;
        rushAgBonus = rAB;
        passYdBonus = pYB;
        passAgBonus = pAB;
    }

    public TeamStrategy() {
        stratName = "No Preference";
        stratDescription = "Will play a normal O/D with no bonus either way, but no penalties either.";
        rushYdBonus = 0;
        rushAgBonus = 0;
        passYdBonus = 0;
        passAgBonus = 0;
    }

    public String getStratName() {
        return stratName;
    }

    public String getStratDescription() {
        return stratDescription;
    }

    public int getRYB() {
        return rushYdBonus;
    }

    public int getRAB() {
        return rushAgBonus;
    }

    public int getPYB() {
        return passYdBonus;
    }

    public int getPAB() {
        return passAgBonus;
    }
}
