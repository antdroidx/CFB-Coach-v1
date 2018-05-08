package recruiting;

import java.util.Comparator;

class CompRecruitCost implements Comparator<String> {
    @Override
    public int compare(String a, String b) {
        String[] psA = a.split(",");
        String[] psB = b.split(",");
        int ovrA = Integer.parseInt(psA[12]);
        int ovrB = Integer.parseInt(psB[12]);
        return ovrA > ovrB ? -1 : ovrA == ovrB ? 0 : 1;
    }
}
