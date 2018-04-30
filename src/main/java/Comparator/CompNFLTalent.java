package Comparator;

import java.util.Comparator;

import Positions.Player;

public class CompNFLTalent implements Comparator<Player> {
    @Override
    public int compare(Player a, Player b) {

        a.talentNFL = ((5*a.ratOvr + 2*a.ratPot + a.personality + a.ratDur + a.ratFootIQ) / 10);
        b.talentNFL = ((5*b.ratOvr + 2*b.ratPot + b.personality + b.ratDur + b.ratFootIQ) / 10);

        if(a.position.equals("QB")) a.talentNFL = (a.talentNFL*1.06);
        if(b.position.equals("QB")) b.talentNFL = (b.talentNFL*1.06);

        if(a.position.equals("RB")) a.talentNFL = (a.talentNFL*1.03);
        if(b.position.equals("RB")) b.talentNFL = (b.talentNFL*1.03);

        if(a.position.equals("WR")) a.talentNFL = (a.talentNFL*1.00);
        if(b.position.equals("WR")) b.talentNFL = (b.talentNFL*1.00);

        if(a.position.equals("TE")) a.talentNFL = (a.talentNFL*0.96);
        if(b.position.equals("TE")) b.talentNFL = (b.talentNFL*0.96);

        if(a.position.equals("OL")) a.talentNFL = (a.talentNFL*1.00);
        if(b.position.equals("OL")) b.talentNFL = (b.talentNFL*1.00);

        if(a.position.equals("K")) a.talentNFL = (a.talentNFL*0.92);
        if(b.position.equals("K")) b.talentNFL = (b.talentNFL*0.92);

        if(a.position.equals("DL")) a.talentNFL = (a.talentNFL*1.04);
        if(b.position.equals("DL")) b.talentNFL = (b.talentNFL*1.04);

        if(a.position.equals("LB")) a.talentNFL = (a.talentNFL*1.00);
        if(b.position.equals("LB")) b.talentNFL = (b.talentNFL*1.00);

        if(a.position.equals("CB")) a.talentNFL = (a.talentNFL*1.03);
        if(b.position.equals("CB")) b.talentNFL = (b.talentNFL*1.03);

        if(a.position.equals("S")) a.talentNFL = (a.talentNFL*1.00);
        if(b.position.equals("S")) b.talentNFL = (b.talentNFL*1.00);

        return a.talentNFL > b.talentNFL ? -1 :a.talentNFL == b.talentNFL ? 0 : 1;

    }
}
