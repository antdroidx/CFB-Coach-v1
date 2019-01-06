package simulation;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class LeagueRecords {
    private final DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
    private final DecimalFormat df2 = new DecimalFormat("#.##", symbols);
    class Record {
        private final float number;
        private String holder;
        private final int year;

        Record(float n, String h, int y) {
            number = n;
            holder = h;
            year = y;
        }

        float getNumber() {
            return number;
        }

        String getHolder() {
            return holder;
        }

        int getYear() {
            return year;
        }

        private void changeAbbr(String newAbbr) {
            String[] split = holder.split(" ");
            holder = newAbbr;
            for (int i = 1; i < split.length; ++i) {
                holder += " " + split[i];
            }
        }
    }

    private final HashMap<String, Record> records;

    private final String[] recordsList = {"TEAM", "Team PPG", "Team Opp PPG", "Team YPG", "Team Opp YPG", "Team TO Diff",
            "SEASON: OFFENSE", "Pass Yards", "Pass TDs", "Ints Thrown", "Comp Percent", "QB Rating",
            "Rush Yards", "Rush TDs", "Fumbles Lost", "Receptions",
            "Rec Yards", "Rec TDs", "SEASON: DEFENSE", "Tackles", "Sacks", "Fumbles Recovered", "Interceptions", "Passes Defended", "SEASON: ST", "Field Goals", "Kick Ret Yards", "Kick Ret TDs", "Punt Ret Yards", "Punt Ret TDs",
            "CAREER: OFFENSE", "Career Pass Yards", "Career Pass TDs", "Career Ints Thrown", "Career Comp PCT", "Career QB Rating",
            "Career Rush Yards", "Career Rush TDs", "Career Fumbles Lost", "Career Receptions",
            "Career Rec Yards", "Career Rec TDs", "CAREER: DEFENSE", "Career Tackles", "Career Sacks", "Career Fumbles Rec", "Career Interceptions", "Career Defended",
            "CAREER: SPEC TEAMS", "Career Field Goals", "Career KR Yards", "Career KR TDs", "Career PR Yards", "Career PR TDs",
            "COACHING RECORDS", "Wins", "National Championships", "Conf Championships", "Bowl Wins", "Bowl Appearances", "Coach Awards", "All-Americans", "All-Conferences", "Coach Year Score"};

    public LeagueRecords(ArrayList<String> recordStrings) {
        records = new HashMap<>();
        String[] csv;
        for (String str : recordStrings) {
            csv = str.split(",");
            records.put(csv[0], new Record(Float.parseFloat(csv[1]), csv[2], Integer.parseInt(csv[3])));
        }
    }

    public LeagueRecords() {
        records = new HashMap<>();
        records.put("TEAM", null);
        records.put("Team PPG", new Record(0, "XXX%XXX", 0));
        records.put("Team Opp PPG", new Record(1000, "XXX%XXX", 0));
        records.put("Team YPG", new Record(0, "XXX%XXX", 0));
        records.put("Team Opp YPG", new Record(1000, "XXX%XXX", 0));
        records.put("Team TO Diff", new Record(0, "XXX%XXX", 0));
        records.put("SEASON: OFFENSE", null);
        records.put("Pass Yards", new Record(0, "XXX%XXX", 0));
        records.put("Pass TDs", new Record(0, "XXX%XXX", 0));
        records.put("Ints Thrown", new Record(0, "XXX%XXX", 0));
        records.put("Comp Percent", new Record(0, "XXX%XXX", 0));
        records.put("QB Rating", new Record(0, "XXX%XXX", 0));
        records.put("Rush Yards", new Record(0, "XXX%XXX", 0));
        records.put("Rush TDs", new Record(0, "XXX%XXX", 0));
        records.put("Fumbles Lost", new Record(0, "XXX%XXX", 0));
        records.put("Receptions", new Record(0, "XXX%XXX", 0));
        records.put("Rec Yards", new Record(0, "XXX%XXX", 0));
        records.put("Rec TDs", new Record(0, "XXX%XXX", 0));
        records.put("SEASON: DEFENSE", null);
        records.put("Tackles", new Record(0, "XXX%XXX", 0));
        records.put("Sacks", new Record(0, "XXX%XXX", 0));
        records.put("Fumbles Recovered", new Record(0, "XXX%XXX", 0));
        records.put("Interceptions", new Record(0, "XXX%XXX", 0));
        records.put("Passes Defended", new Record(0, "XXX%XXX", 0));
        records.put("SEASON: ST", null);
        records.put("Field Goals", new Record(0, "XXX%XXX", 0));
        records.put("Kick Ret Yards", new Record(0, "XXX%XXX", 0));
        records.put("Kick Ret TDs", new Record(0, "XXX%XXX", 0));
        records.put("Punt Ret Yards", new Record(0, "XXX%XXX", 0));
        records.put("Punt Ret TDs", new Record(0, "XXX%XXX", 0));
        records.put("CAREER: OFFENSE", null);
        records.put("Career Pass Yards", new Record(0, "XXX%XXX", 0));
        records.put("Career Pass TDs", new Record(0, "XXX%XXX", 0));
        records.put("Career Ints Thrown", new Record(0, "XXX%XXX", 0));
        records.put("Career Comp PCT", new Record(0, "XXX%XXX", 0));
        records.put("Career QB Rating", new Record(0, "XXX%XXX", 0));
        records.put("Career Rush Yards", new Record(0, "XXX%XXX", 0));
        records.put("Career Rush TDs", new Record(0, "XXX%XXX", 0));
        records.put("Career Fumbles Lost", new Record(0, "XXX%XXX", 0));
        records.put("Career Receptions", new Record(0, "XXX%XXX", 0));
        records.put("Career Rec Yards", new Record(0, "XXX%XXX", 0));
        records.put("Career Rec TDs", new Record(0, "XXX%XXX", 0));
        records.put("CAREER: DEFENSE", null);
        records.put("Career Tackles", new Record(0, "XXX%XXX", 0));
        records.put("Career Sacks", new Record(0, "XXX%XXX", 0));
        records.put("Career Fumbles Rec", new Record(0, "XXX%XXX", 0));
        records.put("Career Interceptions", new Record(0, "XXX%XXX", 0));
        records.put("Career Defended", new Record(0, "XXX%XXX", 0));
        records.put("CAREER: SPEC TEAMS", null);
        records.put("Career Field Goals", new Record(0, "XXX%XXX", 0));
        records.put("Career KR Yards", new Record(0, "XXX%XXX", 0));
        records.put("Career KR TDs", new Record(0, "XXX%XXX", 0));
        records.put("Career PR Yards", new Record(0, "XXX%XXX", 0));
        records.put("Career PR TDs", new Record(0, "XXX%XXX", 0));
        records.put("COACHING RECORDS", null);
        records.put("Wins", new Record(0, "XXX%XXX", 0));
        records.put("National Championships", new Record(0, "XXX%XXX", 0));
        records.put("Conf Championships", new Record(0, "XXX%XXX", 0));
        records.put("Bowl Wins", new Record(0, "XXX%XXX", 0));
        records.put("Bowl Appearances", new Record(0, "XXX%XXX", 0));
        records.put("Coach Awards", new Record(0, "XXX%XXX", 0));
        records.put("All-Americans", new Record(0, "XXX%XXX", 0));
        records.put("All-Conferences", new Record(0, "XXX%XXX", 0));
        records.put("Coach Year Score", new Record(0, "XXX%XXX", 0));
    }

    public void checkRecord(String record, float number, String holder, int year) {
        if (holder.split("%").length < 2) holder = holder + "% ";
        if (record.equals("Team Opp PPG") || record.equals("Team Opp YPG")) {
            // Is a record where lower = better
            if ((records.containsKey(record) && number < records.get(record).getNumber())) {
                records.remove(record);
                records.put(record, new Record(number, holder, year));
            } else if (!records.containsKey(record)) {
                records.put(record, new Record(number, holder, year));
            }
        } else {
            // Is a record where higher = better
            if ((records.containsKey(record) && number > records.get(record).getNumber())) {
                records.remove(record);
                records.put(record, new Record(number, holder, year));
            } else if (!records.containsKey(record)) {
                records.put(record, new Record(number, holder, year));
            }
        }
    }

    public void changeAbbrRecords(String oldAbbr, String newAbbr) {
        Record r;
        for (String s : recordsList) {
            r = records.get(s);
            if (r != null && r.getHolder().split(" ")[0].equals(oldAbbr)) {
                r.changeAbbr(newAbbr);
            }
        }
    }

    public String getRecordsStr() {
        StringBuilder sb = new StringBuilder();
        for (String s : recordsList) {
            sb.append(recordStrCSV(s) + "\n");
        }
        return sb.toString();
    }

    private String recordStrCSV(String key) {
        if (records.containsKey(key)) {
            Record r = records.get(key);
            if (r == null) return key + ",-1,-1,-1";
            return key + "," + df2.format(r.getNumber())+ "," + r.getHolder() + "," + r.getYear();
        } else return "ERROR,ERROR,ERROR,ERROR";
    }

    /**
     * Print out string of all the records broken by a team that year
     *
     * @return string of all records broken
     */
    public String brokenRecordsStr(int year, String abbr) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Record> r : records.entrySet()) {
            if (r.getValue() != null && r.getValue().getHolder().split("%").length > 1) {
                if (r.getValue().getHolder().split("%")[1].equals(abbr) &&
                        r.getValue().getYear() == year) {
                    sb.append("+ " + r.getValue().getHolder().split("%")[0] + " League broke the record for " +
                            r.getKey() + " with " + df2.format(r.getValue().getNumber()) + "!\n");
                }
            }
        }
        if (sb.length() == 0) sb.append("None");

        return sb.toString();
    }

}
