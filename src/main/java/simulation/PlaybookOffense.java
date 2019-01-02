package simulation;

public class PlaybookOffense {

    public Team team;
    private int runPref;
    private int runProtection;
    private int runPotential;
    private int runUsage;
    private int passPref;
    private int passProtection;
    private int passPotential;
    private int passUsage;
    public final int numPlaybooks = 6;


    private String stratName;
    private String stratDescription;

    public PlaybookOffense(String name, String descrip, int rPref, int rProtection, int rPotential, int rUsage, int pPref, int pProtection, int pPotential, int pUsage) {
        stratName = name;
        stratDescription = descrip;
        runPref = rPref;  //Run Frequency  Based on runPref / (runPref+passPref)
        runProtection = rProtection; //Block Bonus
        runPotential = rPotential; //RB Hole opening bonus
        runUsage = rUsage; //Use TE to Block
        passPref = pPref;  //Pass Frequency
        passProtection = pProtection; //Block Bonus + accuracy
        passPotential = pPotential; //Big Play Potential
        passUsage = pUsage; //use TE more often in passing
    }

    public PlaybookOffense(int playbook) {
        if (playbook < 1 || playbook > 6) playbook = (int) Math.random() * 6 + 1;

        if (playbook == 1) playBook1();
        else if (playbook == 2) playBook2();
        else if (playbook == 3) playBook3();
        else if (playbook == 4) playBook4();
        else if (playbook == 5) playBook5();
        else if (playbook == 6) playBook6();
        else playBook1();
    }

    public void playBook1() {
        stratName = "Pro-Style";
        stratDescription = "Play a normal balanced offense.";
        runPref = 1;
        runProtection = 0;
        runPotential = 0;
        runUsage = 1;
        passPref = 1;
        passProtection = 0;
        passPotential = 0;
        passUsage = 1;
    }

    public void playBook2() {
        stratName = "Smash Mouth";
        stratDescription = "Play a conservative run-heavy offense, setting up the passes as necessary.";
        runPref = 2;
        runProtection = 1;
        runPotential = -1;
        runUsage = 1;
        passPref = 1;
        passProtection = 2;
        passPotential = 1;
        passUsage = 0;
    }

    public void playBook3() {
        stratName = "West Coast";
        stratDescription = "Passing game dictates the run game with short accurate passes.";
        runPref = 2;
        runProtection = 0;
        runPotential = 1;
        runUsage = 0;
        passPref = 3;
        passProtection = 1;
        passPotential = -2;
        passUsage = 2;
    }

    public void playBook4() {
        stratName = "Spread";
        stratDescription = "Pass-heavy offense using many receivers with big play potential with risk.";
        runPref = 1;
        runProtection = -2;
        runPotential = 1;
        runUsage = 0;
        passPref = 2;
        passProtection = -2;
        passPotential = 1;
        passUsage = 1;
    }

    public void playBook5() {
        stratName = "Read-Option";
        stratDescription = "QB Option heavy offense, where QB options based on coverage and LB position.";
        runPref = 3;
        runProtection = -1;
        runPotential = 1;
        runUsage = 1;
        passPref = 2;
        passProtection = -1;
        passPotential = -1;
        passUsage = 0;
    }

    public void playBook6() {
        stratName = "Run-Pass Option";
        stratDescription = "Passing-Oriented version of the Read-Option";
        runPref = 2;
        runProtection = -1;
        runPotential = 1;
        runUsage = 1;
        passPref = 3;
        passProtection = -1;
        passPotential = -1;
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
