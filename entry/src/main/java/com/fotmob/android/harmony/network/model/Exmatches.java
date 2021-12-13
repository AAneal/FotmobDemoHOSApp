package com.fotmob.android.harmony.network.model;

import org.simpleframework.xml.*;

import java.util.List;

@Root(name = "exmatches", strict = false)
public class Exmatches {

    @Attribute(required = false)
    private String ads;

    @Attribute(required = false)
    private String msg3;

    @ElementList(name = "league", inline = true)
    private List<League> league;

    @Attribute(required = false)
    private String msgId;

    public String getAds() {
        return ads;
    }

    public void setAds(String ads) {
        this.ads = ads;
    }

    public String getMsg3() {
        return msg3;
    }

    public void setMsg3(String msg3) {
        this.msg3 = msg3;
    }

    public List<League> getLeague() {
        return league;
    }

    public void setLeague(List<League> league) {
        this.league = league;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }
}

