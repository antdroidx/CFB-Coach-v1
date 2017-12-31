package Simulation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class to hold all the all-time league season records like passing/rushing yards, TDs, etc
 * Created by Achi Jones on 3/19/2016.
 */
public class LeagueRecords {

    public class Record {
        private int number;
        private String holder;
        private int year;

        public Record(int n, String h, int y) {
            number = n;
            holder = h;
            year = y;
        }

        public int getNumber() {
            return number;
        }

        public String getHolder() {
            return holder;
        }

        public int getYear() {
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

    private HashMap<String, Record> records;

    public final String[] recordsList = {"TEAM", "Team PPG", "Team Opp PPG", "Team YPG", "Team Opp YPG", "Team TO Diff",
            "SEASON: OFFENSE", "Pass Yards", "Pass TDs", "Ints Thrown", "Comp Percent", "QB Rating",
            "Rush Yards", "Rush TDs", "Fumbles Lost",
            "Rec Yards", "Rec TDs", "Catch Percent", "SEASON: DEFENSE", "Tackles", "Sacks", "Fumbles Recovered", "Interceptions", "Passes Defended", "SEASON: KICKING", "Field Goals",
            "CAREER: OFFENSE", "Career Pass Yards", "Career Pass TDs", "Career Ints Thrown", "Career Comp PCT", "Career QB Rating",
            "Career Rush Yards", "Career Rush TDs", "Career Fumbles Lost",
            "Career Rec Yards", "Career Rec TDs", "CAREER: DEFENSE", "Career Tackles", "Career Sacks", "Career Fumbles Rec", "Career Interceptions", "Career Defended",
            "CAREER: KICKING", "Career Field Goals",
            "COACHING RECORDS", "Wins", "National Championships", "Conf Championships", "Bowl Wins", "Coach Awards", "Coach Year Score", "Coach Career Score"};

    public LeagueRecords(ArrayList<String> recordStrings) {
        records = new HashMap<String, Record>();
        String[] csv;
        for (String str : recordStrings) {
            csv = str.split(",");
            records.put(csv[0], new Record(Integer.parseInt(csv[1]), csv[2], Integer.parseInt(csv[3])));
        }
    }

    public LeagueRecords() {
        records = new HashMap<String, Record>();
        records.put("TEAM", null);
        records.put("Team PPG", new Record(0, "XXX", 0));
        records.put("Team Opp PPG", new Record(1000, "XXX", 0));
        records.put("Team YPG", new Record(0, "XXX", 0));
        records.put("Team Opp YPG", new Record(1000, "XXX", 0));
        records.put("Team TO Diff", new Record(0, "XXX", 0));
        records.put("SEASON: OFFENSE", null);
        records.put("Pass Yards", new Record(0, "XXX", 0));
        records.put("Pass TDs", new Record(0, "XXX", 0));
        records.put("Ints Thrown", new Record(0, "XXX", 0));
        records.put("Comp Percent", new Record(0, "XXX", 0));
        records.put("QB Rating", new Record(0, "XXX", 0));
        records.put("Rush Yards", new Record(0, "XXX", 0));
        records.put("Rush TDs", new Record(0, "XXX", 0));
        records.put("Fumbles Lost", new Record(0, "XXX", 0));
        records.put("Rec Yards", new Record(0, "XXX", 0));
        records.put("Rec TDs", new Record(0, "XXX", 0));
        records.put("Catch Percent", new Record(0, "XXX", 0));
        records.put("SEASON: DEFENSE", null);
        records.put("Tackles", new Record(0, "XXX", 0));
        records.put("Sacks", new Record(0, "XXX", 0));
        records.put("Fumbles Recovered", new Record(0, "XXX", 0));
        records.put("Interceptions", new Record(0, "XXX", 0));
        records.put("Passes Defended", new Record(0, "XXX", 0));
        records.put("SEASON: KICKING", null);
        records.put("Field Goals", new Record(0, "XXX", 0));
        records.put("CAREER: OFFENSE", null);
        records.put("Career Pass Yards", new Record(0, "XXX", 0));
        records.put("Career Pass TDs", new Record(0, "XXX", 0));
        records.put("Career Ints Thrown", new Record(0, "XXX", 0));
        records.put("Career Comp PCT", new Record(0, "XXX", 0));
        records.put("Career QB Rating", new Record(0, "XXX", 0));
        records.put("Career Rush Yards", new Record(0, "XXX", 0));
        records.put("Career Rush TDs", new Record(0, "XXX", 0));
        records.put("Career Fumbles Lost", new Record(0, "XXX", 0));
        records.put("Career Rec Yards", new Record(0, "XXX", 0));
        records.put("Career Rec TDs", new Record(0, "XXX", 0));
        records.put("CAREER: DEFENSE", null);
        records.put("Career Tackles", new Record(0, "XXX", 0));
        records.put("Career Sacks", new Record(0, "XXX", 0));
        records.put("Career Fumbles Rec", new Record(0, "XXX", 0));
        records.put("Career Interceptions", new Record(0, "XXX", 0));
        records.put("Career Defended", new Record(0, "XXX", 0));
        records.put("CAREER: KICKING", null);
        records.put("Career Field Goals", new Record(0, "XXX", 0));
        records.put("COACHING RECORDS",null);
        records.put("Wins", new Record(0, "XXX", 0));
        records.put("National Championships", new Record(0, "XXX", 0));
        records.put("Conf Championships", new Record(0, "XXX", 0));
        records.put("Bowl Wins", new Record(0, "XXX", 0));
        records.put("Coach Awards", new Record(0, "XXX", 0));
        records.put("Coach Year Score", new Record(0, "XXX", 0));
        records.put("Coach Career Score", new Record(0, "XXX", 0));
    }

    public void checkRecord(String record, int number, String holder, int year) {
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
            return key + "," + r.getNumber() + "," + r.getHolder() + "," + r.getYear();
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
            if (r.getValue() != null &&
                    r.getValue().getHolder().split(" ")[0].equals(abbr) &&
                    r.getValue().getYear() == year &&
                    !r.getKey().split(" ")[0].equals("Career")) {
                sb.append(r.getValue().getHolder() + " broke the record for " +
                        r.getKey() + " with " + r.getValue().getNumber() + "!\n");
            }
        }

        return sb.toString();
    }

}
