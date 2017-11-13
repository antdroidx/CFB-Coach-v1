package Simulation;

import java.util.Comparator;

/**
 * Created by ahngu on 11/12/2017.
 */

public class Rankings {

}


//EXTRACTED FROM CONFERENCES

class TeamCompConfWins implements Comparator<Team> {
    @Override
    public int compare(Team a, Team b) {
        if (a.confChampion.equals("CC")) return -1;
        else if (b.confChampion.equals("CC")) return 1;
        else if (a.getConfWins() > b.getConfWins()) {
            return -1;
        } else if (a.getConfWins() == b.getConfWins()) {
            //check for h2h tiebreaker
            if (a.gameWinsAgainst.contains(b)) {
                return -1;
            } else if (b.gameWinsAgainst.contains(a)) {
                return 1;
            } else {
                return 0;
            }
        } else {
            return 1;
        }
    }
}


//EXTRACTED FROM TEAM

/**
 * Comparator used to sort players by overall
 */
class PlayerComparator implements Comparator<Player> {
    @Override
    public int compare(Player a, Player b) {
        if (!a.isInjured && !b.isInjured) {
            // If both players aren't injured
            if (a.year > 0 && b.year > 0) {
                if (!a.isTransfer && !b.isTransfer) {
                    // If both players aren't redshirted
                    if (a.ratOvr > b.ratOvr) return -1;
                    else if (a.ratOvr == b.ratOvr)
                        return a.ratPot > b.ratPot ? -1 : a.ratPot == b.ratPot ? 0 : 1;
                    else return 1;
                } else if (!a.isTransfer) {
                    return -1;
                } else if (!b.isTransfer) {
                    return 1;
                } else if (a.year > 0) {
                    return -1;
                } else if (b.year > 0) {
                    return 1;
                } else {
                    return a.ratOvr > b.ratOvr ? -1 : a.ratOvr == b.ratOvr ? 0 : 1;
                }
            }
        } else if (!a.isInjured) {
            return -1;
        } else if (!b.isInjured) {
            return 1;
        } else {
            return a.ratOvr > b.ratOvr ? -1 : a.ratOvr == b.ratOvr ? 0 : 1;
        }
        return a.ratOvr > b.ratOvr ? -1 : a.ratOvr == b.ratOvr ? 0 : 1;

    }
}


/**
 * Comparator used to sort players by position, QB-RB-WR-OL-K-S-CB-DL
 */
class PlayerPositionComparator implements Comparator<Player> {
    @Override
    public int compare(Player a, Player b) {
        int aPos = Player.getPosNumber(a.position);
        int bPos = Player.getPosNumber(b.position);
        return aPos < bPos ? -1 : aPos == bPos ? 0 : 1;
    }
}

/**
 * Comparator used to sort players by overall
 */
class RecruitComparator implements Comparator<Player> {
    @Override
    public int compare(Player a, Player b) {
        return a.cost > b.cost ? -1 : a.cost == b.cost ? 0 : 1;
    }
}

//EXTRACTED FROM LEAGUE

class CoachScoreComp implements Comparator<HeadCoach> {
    @Override
    public int compare(HeadCoach a, HeadCoach b) {
        return a.getCoachScore() > b.getCoachScore() ? -1 : a.getCoachScore() == b.getCoachScore() ? 0 : 1;
    }
}

class PlayerHeismanComp implements Comparator<Player> {
    @Override
    public int compare(Player a, Player b) {
        return a.getHeismanScore() > b.getHeismanScore() ? -1 : a.getHeismanScore() == b.getHeismanScore() ? 0 : 1;
    }
}

class PlayerPassRatingComp implements Comparator<PlayerQB> {
    @Override
    public int compare(PlayerQB a, PlayerQB b) {
        return a.getPasserRating() > b.getPasserRating() ? -1 : a.getPasserRating() == b.getPasserRating() ? 0 : 1;
    }
}

class PlayerPassYardsComp implements Comparator<PlayerQB> {
    @Override
    public int compare(PlayerQB a, PlayerQB b) {
        return a.statsPassYards > b.statsPassYards ? -1 : a.statsPassYards == b.statsPassYards ? 0 : 1;
    }
}

class PlayerPassTDsComp implements Comparator<PlayerQB> {
    @Override
    public int compare(PlayerQB a, PlayerQB b) {
        return a.statsPassTD > b.statsPassTD ? -1 : a.statsPassTD == b.statsPassTD ? 0 : 1;
    }
}

class PlayerPassIntsComp implements Comparator<PlayerQB> {
    @Override
    public int compare(PlayerQB a, PlayerQB b) {
        return a.statsInt > b.statsInt ? -1 : a.statsInt == b.statsInt ? 0 : 1;
    }
}

class PlayerPassPctComp implements Comparator<PlayerQB> {
    @Override
    public int compare(PlayerQB a, PlayerQB b) {
        return a.getPassPCT() > b.getPassPCT() ? -1 : a.getPassPCT() == b.getPassPCT() ? 0 : 1;
    }
}

class PlayerRushYardsComp implements Comparator<PlayerRB> {
    @Override
    public int compare(PlayerRB a, PlayerRB b) {
        return a.statsRushYards > b.statsRushYards ? -1 : a.statsRushYards == b.statsRushYards ? 0 : 1;
    }
}

class PlayerRushTDsComp implements Comparator<PlayerRB> {
    @Override
    public int compare(PlayerRB a, PlayerRB b) {
        return a.statsRushTD > b.statsRushTD ? -1 : a.statsRushTD == b.statsRushTD ? 0 : 1;
    }
}

class PlayerRecYardsComp implements Comparator<PlayerWR> {
    @Override
    public int compare(PlayerWR a, PlayerWR b) {
        return a.statsRecYards > b.statsRecYards ? -1 : a.statsRecYards == b.statsRecYards ? 0 : 1;
    }
}

class PlayerReceptionsComp implements Comparator<PlayerWR> {
    @Override
    public int compare(PlayerWR a, PlayerWR b) {
        return a.statsReceptions > b.statsReceptions ? -1 : a.statsReceptions == b.statsReceptions ? 0 : 1;
    }
}

class PlayerRecTDsComp implements Comparator<PlayerWR> {
    @Override
    public int compare(PlayerWR a, PlayerWR b) {
        return a.statsTD > b.statsTD ? -1 : a.statsTD == b.statsTD ? 0 : 1;
    }
}

class PlayerTacklesComp implements Comparator<PlayerDefense> {
    @Override
    public int compare(PlayerDefense a, PlayerDefense b) {
        return a.tackles > b.tackles ? -1 : a.tackles == b.tackles ? 0 : 1;
    }
}

class PlayerSacksComp implements Comparator<PlayerDefense> {
    @Override
    public int compare(PlayerDefense a, PlayerDefense b) {
        return a.sacks > b.sacks ? -1 : a.sacks == b.sacks ? 0 : 1;
    }
}

class PlayerFumblesRecComp implements Comparator<PlayerDefense> {
    @Override
    public int compare(PlayerDefense a, PlayerDefense b) {
        return a.fumbles > b.fumbles ? -1 : a.fumbles == b.fumbles ? 0 : 1;
    }
}

class PlayerInterceptionsComp implements Comparator<PlayerDefense> {
    @Override
    public int compare(PlayerDefense a, PlayerDefense b) {
        return a.interceptions > b.interceptions ? -1 : a.interceptions == b.interceptions ? 0 : 1;
    }
}

class TeamCompPoll implements Comparator<Team> {
    @Override
    public int compare(Team a, Team b) {
        return a.teamPollScore > b.teamPollScore ? -1 : a.teamPollScore == b.teamPollScore ? 0 : 1;
    }
}

class TeamCompSoW implements Comparator<Team> {
    @Override
    public int compare(Team a, Team b) {
        return a.teamStrengthOfWins > b.teamStrengthOfWins ? -1 : a.teamStrengthOfWins == b.teamStrengthOfWins ? 0 : 1;
    }
}


class TeamCompSoS implements Comparator<Team> {
    @Override
    public int compare(Team a, Team b) {
        return a.teamSOS > b.teamSOS ? -1 : a.teamSOS == b.teamSOS ? 0 : 1;
    }
}

class TeamCompPPG implements Comparator<Team> {
    @Override
    public int compare(Team a, Team b) {
        return a.teamPoints / a.numGames() > b.teamPoints / b.numGames() ? -1 : a.teamPoints / a.numGames() == b.teamPoints / b.numGames() ? 0 : 1;
    }
}

class TeamCompOPPG implements Comparator<Team> {
    @Override
    public int compare(Team a, Team b) {
        return a.teamOppPoints / a.numGames() < b.teamOppPoints / b.numGames() ? -1 : a.teamOppPoints / a.numGames() == b.teamOppPoints / b.numGames() ? 0 : 1;
    }
}

class TeamCompYPG implements Comparator<Team> {
    @Override
    public int compare(Team a, Team b) {
        return a.teamYards / a.numGames() > b.teamYards / b.numGames() ? -1 : a.teamYards / a.numGames() == b.teamYards / b.numGames() ? 0 : 1;
    }
}

class TeamCompOYPG implements Comparator<Team> {
    @Override
    public int compare(Team a, Team b) {
        return a.teamOppYards / a.numGames() < b.teamOppYards / b.numGames() ? -1 : a.teamOppYards / a.numGames() == b.teamOppYards / b.numGames() ? 0 : 1;
    }
}

class TeamCompOPYPG implements Comparator<Team> {
    @Override
    public int compare(Team a, Team b) {
        return a.teamOppPassYards / a.numGames() < b.teamOppPassYards / b.numGames() ? -1 : a.teamOppPassYards / a.numGames() == b.teamOppPassYards / b.numGames() ? 0 : 1;
    }
}

class TeamCompORYPG implements Comparator<Team> {
    @Override
    public int compare(Team a, Team b) {
        return a.teamOppRushYards / a.numGames() < b.teamOppRushYards / b.numGames() ? -1 : a.teamOppRushYards / a.numGames() == b.teamOppRushYards / b.numGames() ? 0 : 1;
    }
}

class TeamCompPYPG implements Comparator<Team> {
    @Override
    public int compare(Team a, Team b) {
        return a.teamPassYards / a.numGames() > b.teamPassYards / b.numGames() ? -1 : a.teamPassYards / a.numGames() == b.teamPassYards / b.numGames() ? 0 : 1;
    }
}

class TeamCompRYPG implements Comparator<Team> {
    @Override
    public int compare(Team a, Team b) {
        return a.teamRushYards / a.numGames() > b.teamRushYards / b.numGames() ? -1 : a.teamRushYards / a.numGames() == b.teamRushYards / b.numGames() ? 0 : 1;
    }
}

class TeamCompTODiff implements Comparator<Team> {
    @Override
    public int compare(Team a, Team b) {
        return a.teamTODiff > b.teamTODiff ? -1 : a.teamTODiff == b.teamTODiff ? 0 : 1;
    }
}

class TeamCompOffTalent implements Comparator<Team> {
    @Override
    public int compare(Team a, Team b) {
        return a.teamOffTalent > b.teamOffTalent ? -1 : a.teamOffTalent == b.teamOffTalent ? 0 : 1;
    }
}

class TeamCompDefTalent implements Comparator<Team> {
    @Override
    public int compare(Team a, Team b) {
        return a.teamDefTalent > b.teamDefTalent ? -1 : a.teamDefTalent == b.teamDefTalent ? 0 : 1;
    }
}

class TeamCompPrestige implements Comparator<Team> {
    @Override
    public int compare(Team a, Team b) {
        return a.teamPrestige > b.teamPrestige ? -1 : a.teamPrestige == b.teamPrestige ? 0 : 1;
    }
}

class TeamCompRecruitClass implements Comparator<Team> {
    @Override
    public int compare(Team a, Team b) {
        return a.getRecruitingClassRat() > b.getRecruitingClassRat() ? -1 : a.getRecruitingClassRat() == b.getRecruitingClassRat() ? 0 : 1;
    }
}