package CFBsimPack;

/**
 * Team Strategy class. It works for both offense and defense, just applying the bonus different ways.
 * Created by Achi Jones on 3/10/2016. // completely re-written by antdroid 10/2017
 */
public class TeamStrategy {

    public Team team;
    private int runPref;
    private int runProtection;
    private int runPotential;
    private int runUsage;
    private int passPref;
    private int passProtection;
    private int passPotential;
    private int passUsage;

    private String stratName;
    private String stratDescription;

    public TeamStrategy(String name, String descrip, int rPref, int rProtection, int rPotential, int rUsage, int pPref, int pProtection, int pPotential, int pUsage) {
        stratName = name;
        stratDescription = descrip;
        runPref = rPref;
        runProtection = rProtection;
        runPotential = rPotential;
        runUsage = rUsage;
        passPref = pPref;
        passProtection = pProtection;
        passPotential = pPotential;
        passUsage = pUsage;
    }

    public TeamStrategy() {
        stratName = "Balanced";
        stratDescription = "Balance Game Plan";
        runPref = 1;
        runProtection = 0;
        runPotential = 0;
        runUsage = 1;
        passPref = 1;
        passProtection = 0;
        passPotential = 0;
        passUsage = 1;
    }


    public String getStratName() {
        return stratName;
    }

    public String getStratDescription() {
        return stratDescription;
    }

    public int getRunPref() {
        return runPref;
    }

    public int getRunProtection() {
        return runProtection;
    }

    public int getRunPotential() {
        return runPotential;
    }

    public int getRunUsage() {
        return runUsage;
    }
    public int getPassPref() {
        return passPref;
    }

    public int getPassProtection() {
        return passProtection;
    }

    public int getPassPotential() {
        return passPotential;
    }

    public int getPassUsage() {
        return passUsage;
    }
}
