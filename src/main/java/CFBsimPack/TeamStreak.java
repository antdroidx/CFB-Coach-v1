package CFBsimPack;

/**
 * Class used by Teams to keep track of the longest winning streaks in the league.
 * Created by Achi Jones on 3/19/2016.
 */
public class TeamStreak {

    private int startYear;
    private int endYear;
    private int streakLength;
    private String streakTeam;

    public TeamStreak(int start, int end, int len, String t) {
        startYear = start;
        endYear = end;
        streakLength = len;
        streakTeam = t;
    }

    public void addWin(int year) {
        streakLength++;
        endYear = year;
    }

    public void resetStreak(int year) {
        startYear = year;
        endYear = year;
        streakLength = 0;
    }

    public void changeAbbr(String newAbbr) {
        streakTeam = newAbbr;
    }

    public int getStreakLength() {
        return streakLength;
    }

    public int getStartYear() {
        return startYear;
    }

    public int getEndYear() {
        return endYear;
    }

    public String getTeam() {
        return streakTeam;
    }

    public String getStreakCSV() {
        return streakLength + "," + streakTeam + "," + startYear + "," + endYear;
    }

}
