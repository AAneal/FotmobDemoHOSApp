package com.fotmob.android.harmony.network.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;


public class MatchFacts {

    @Element(required = false)
    public String MATCH;

    @Element(required = false)
    public String GD;

    @Element(required = false)
    public String GD2;

    @Element(required = false)
    public String LINEUP;

    @Element(required = false)
    public String MATCH_STATS;

    @Element(required = false)
    public String SUBST;

    @Element(required = false)
    public String SUBST2;

    @Element(required = false)
    public String GST;

    @Element(required = false)
    public String GST2;

    @Element(required = false)
    public String CCODE;

    @Element(required = false)
    public String COACH;

    @Element(required = false)
    public String ln;

    @Element(required = false)
    public String vn;

    @Element(required = false)
    public String ltc;

    @Element(required = false)
    public String h2h;

    @Element(required = false)
    public Object lodds;

    @Element(required = false)
    public String nw;

    @Element(required = false)
    public String nw2;

    @Element(required = false)
    public String at;

    @Element(required = false)
    public String detailedstats;

    @Element(required = false)
    public String tw;

    @Element(required = false)
    public String na;

    @Element(required = false)
    public boolean media;

    @Element(required = false)
    public String minfo;

    @Element(required = false)
    public String form_ht;

    @Element(required = false)
    public String form_at;

    @Element(required = false)
    public String web;

    @Element(required = false)
    public String CAPTAIN;

    @Element(required = false)
    public String h2hstats;

    @Element(required = false)
    public String buzz_langs;

    @Element(required = false)
    public String EXTERNAL_LINEUP;

    @Element(required = false)
    public String insights;

    @Element(required = false)
    public String mlist;

    @Element(required = false)
    public String oddspoll;

    @Element(required = false)
    public String resodds;

    @Element(required = false)
    public String venue;

    @Element(required = false)
    public String vote_result;

    public String getMATCH() {
        return MATCH;
    }

    public void setMATCH(String MATCH) {
        this.MATCH = MATCH;
    }

    public String getGD() {
        return GD;
    }

    public void setGD(String GD) {
        this.GD = GD;
    }

    public String getGD2() {
        return GD2;
    }

    public void setGD2(String GD2) {
        this.GD2 = GD2;
    }

    public String getLINEUP() {
        return LINEUP;
    }

    public void setLINEUP(String LINEUP) {
        this.LINEUP = LINEUP;
    }

    public String getMATCH_STATS() {
        return MATCH_STATS;
    }

    public void setMATCH_STATS(String MATCH_STATS) {
        this.MATCH_STATS = MATCH_STATS;
    }

    public String getSUBST() {
        return SUBST;
    }

    public void setSUBST(String SUBST) {
        this.SUBST = SUBST;
    }

    public String getSUBST2() {
        return SUBST2;
    }

    public void setSUBST2(String SUBST2) {
        this.SUBST2 = SUBST2;
    }

    public String getGST() {
        return GST;
    }

    public void setGST(String GST) {
        this.GST = GST;
    }

    public String getGST2() {
        return GST2;
    }

    public void setGST2(String GST2) {
        this.GST2 = GST2;
    }

    public String getCCODE() {
        return CCODE;
    }

    public void setCCODE(String CCODE) {
        this.CCODE = CCODE;
    }

    public String getCOACH() {
        return COACH;
    }

    public void setCOACH(String COACH) {
        this.COACH = COACH;
    }

    public String getLn() {
        return ln;
    }

    public void setLn(String ln) {
        this.ln = ln;
    }

    public String getVn() {
        return vn;
    }

    public void setVn(String vn) {
        this.vn = vn;
    }

    public String getLtc() {
        return ltc;
    }

    public void setLtc(String ltc) {
        this.ltc = ltc;
    }

    public String getH2h() {
        return h2h;
    }

    public void setH2h(String h2h) {
        this.h2h = h2h;
    }

    public Object getLodds() {
        return lodds;
    }

    public void setLodds(Object lodds) {
        this.lodds = lodds;
    }

    public String getNw() {
        return nw;
    }

    public void setNw(String nw) {
        this.nw = nw;
    }

    public String getNw2() {
        return nw2;
    }

    public void setNw2(String nw2) {
        this.nw2 = nw2;
    }

    public String getAt() {
        return at;
    }

    public void setAt(String at) {
        this.at = at;
    }

    public String getDetailedstats() {
        return detailedstats;
    }

    public void setDetailedstats(String detailedstats) {
        this.detailedstats = detailedstats;
    }

    public String getTw() {
        return tw;
    }

    public void setTw(String tw) {
        this.tw = tw;
    }

    public String getNa() {
        return na;
    }

    public void setNa(String na) {
        this.na = na;
    }

    public boolean isMedia() {
        return media;
    }

    public void setMedia(boolean media) {
        this.media = media;
    }

    public String getMinfo() {
        return minfo;
    }

    public void setMinfo(String minfo) {
        this.minfo = minfo;
    }

    public String getForm_ht() {
        return form_ht;
    }

    public void setForm_ht(String form_ht) {
        this.form_ht = form_ht;
    }

    public String getForm_at() {
        return form_at;
    }

    public void setForm_at(String form_at) {
        this.form_at = form_at;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getCAPTAIN() {
        return CAPTAIN;
    }

    public void setCAPTAIN(String CAPTAIN) {
        this.CAPTAIN = CAPTAIN;
    }

    public String getH2hstats() {
        return h2hstats;
    }

    public void setH2hstats(String h2hstats) {
        this.h2hstats = h2hstats;
    }

    public String getBuzz_langs() {
        return buzz_langs;
    }

    public void setBuzz_langs(String buzz_langs) {
        this.buzz_langs = buzz_langs;
    }

    public String getEXTERNAL_LINEUP() {
        return EXTERNAL_LINEUP;
    }

    public void setEXTERNAL_LINEUP(String EXTERNAL_LINEUP) {
        this.EXTERNAL_LINEUP = EXTERNAL_LINEUP;
    }

    public String getInsights() {
        return insights;
    }

    public void setInsights(String insights) {
        this.insights = insights;
    }

    public String getMlist() {
        return mlist;
    }

    public void setMlist(String mlist) {
        this.mlist = mlist;
    }

    public String getOddspoll() {
        return oddspoll;
    }

    public void setOddspoll(String oddspoll) {
        this.oddspoll = oddspoll;
    }

    public String getResodds() {
        return resodds;
    }

    public void setResodds(String resodds) {
        this.resodds = resodds;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getVote_result() {
        return vote_result;
    }

    public void setVote_result(String vote_result) {
        this.vote_result = vote_result;
    }
}
