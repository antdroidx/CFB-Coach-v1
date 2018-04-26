package Simulation;

public class TeamStrategy {

    public Team team;
    private final int runPref;
    private final int runProtection;
    private final int runPotential;
    private final int runUsage;
    private final int passPref;
    private final int passProtection;
    private final int passPotential;
    private final int passUsage;

    private final String stratName;
    private final String stratDescription;

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
