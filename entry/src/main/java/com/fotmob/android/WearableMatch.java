package com.fotmob.android;

import java.util.Objects;

/**
 * Simple representation of match used on wearables.
 */
public class WearableMatch implements Comparable {
    public String matchId;
    public Integer leagueId;
    public Integer parentLeagueId;
    public int teamIdHome;
    public int teamIdAway;
    public String teamNameHome;
    public String teamNameAway;
    public int scoreHome;
    public int scoreAway;
    public String matchTime;
    public String elapsedTime;
    public String matchStatus;

    public WearableMatch(String matchId, int leagueId, int parentLeagueId, int teamIdHome, String teamNameHome, int scoreHome, int teamIdAway, String teamNameAway, int scoreAway, String matchTime, String elapsedTime, String matchStatus) {
        this.matchId = matchId;
        this.leagueId = leagueId;
        this.parentLeagueId = parentLeagueId;
        this.teamIdHome = teamIdHome;
        this.teamIdAway = teamIdAway;
        this.teamNameHome = teamNameHome;
        this.teamNameAway = teamNameAway;
        this.scoreHome = scoreHome;
        this.scoreAway = scoreAway;
        this.matchTime = matchTime;
        this.elapsedTime = elapsedTime;
        this.matchStatus = matchStatus;
    }

    public WearableMatch() {
        /* no-op */
    }

    @Override
    public String toString() {
        return "WearableMatch{" +
                "matchId='" + matchId + '\'' +
                ", teamIdHome=" + teamIdHome +
                ", teamIdAway=" + teamIdAway +
                ", teamNameHome='" + teamNameHome + '\'' +
                ", teamNameAway='" + teamNameAway + '\'' +
                ", scoreHome=" + scoreHome +
                ", scoreAway=" + scoreAway +
                ", matchTime='" + matchTime + '\'' +
                ", elapsedTime='" + elapsedTime + '\'' +
                ", matchStatus='" + matchStatus + '\'' +
                '}';
    }


    @Override
    public int compareTo(Object match) {

        long compareTime = Long.parseLong((((WearableMatch) match).matchTime));

        long diff = Long.parseLong(this.matchTime) - compareTime;

        if (diff > 0)
            return 1;
        else
            return -1;
    }

    @Override
    public boolean equals(Object o) {

        if (o instanceof WearableMatch) {

            WearableMatch match = (WearableMatch) o;
            if (this.matchId.equals(match.matchId))

                return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(matchId, leagueId, parentLeagueId, teamIdHome, teamIdAway, teamNameHome, teamNameAway, scoreHome, scoreAway, matchTime, elapsedTime, matchStatus);
    }
}
