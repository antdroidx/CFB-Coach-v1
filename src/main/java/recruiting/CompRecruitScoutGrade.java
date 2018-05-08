package recruiting;

import java.util.Comparator;

class CompRecruitScoutGrade implements Comparator<String> {
    @Override
    public int compare(String a, String b) {
        String[] psA = a.split(",");
        String[] psB = b.split(",");
        float ovrA = (4 * Integer.parseInt(psA[11]) + Integer.parseInt(psA[9])) / 5;
        float ovrB = (4 * Integer.parseInt(psB[11]) + Integer.parseInt(psB[9])) / 5;
        return ovrA > ovrB ? -1 : ovrA == ovrB ? 0 : 1;
    }
}