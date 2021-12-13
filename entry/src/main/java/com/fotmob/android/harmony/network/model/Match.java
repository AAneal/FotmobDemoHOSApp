package com.fotmob.android.harmony.network.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "match", strict = false)
public class Match {

    @Attribute(required = false)
    private String Status;

    @Attribute(required = false)
    private String med;

    @Attribute(required = false)
    private String aTeam;

    @Attribute(required = false)
    private int hId;

    @Attribute(required = false)
    private String hTeam;

    @Attribute(required = false)
    private String gs;

    @Attribute(required = false)
    private String sId;

    @Attribute(required = false)
    private int hScore;

    @Attribute(required = false)
    private String stage;

    @Attribute(required = false)
    private int aScore;

    @Attribute(required = false)
    private String extid;

    @Attribute(required = false)
    private String id;

    @Attribute(required = false)
    private String time;

    @Attribute(required = false)
    private int aId;

    @Attribute(required = false)
    private String shs;

    public String getStatus() {
        return Status;
    }

    public void setStatus(String Status) {
        this.Status = Status;
    }

    public String getATeam() {
        return aTeam;
    }

    public void setATeam(String aTeam) {
        this.aTeam = aTeam;
    }

    public int getHId() {
        return hId;
    }

    public void setHId(int hId) {
        this.hId = hId;
    }

    public String getMed() {
        return med;
    }

    public void setMed(String med) {
        this.med = med;
    }

    public String getHTeam() {
        return hTeam;
    }

    public void setHTeam(String hTeam) {
        this.hTeam = hTeam;
    }

    public String getGs() {
        return gs;
    }

    public void setGs(String gs) {
        this.gs = gs;
    }

    public String getSId() {
        return sId;
    }

    public void setSId(String sId) {
        this.sId = sId;
    }

    public int getHScore() {
        return hScore;
    }

    public void setHScore(int hScore) {
        this.hScore = hScore;
    }

    public String getStage() {
        return stage;
    }

    public void setStage(String stage) {
        this.stage = stage;
    }

    public int getAScore() {
        return aScore;
    }

    public void setAScore(int aScore) {
        this.aScore = aScore;
    }

    public String getExtid() {
        return extid;
    }

    public void setExtid(String extid) {
        this.extid = extid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getAId() {
        return aId;
    }

    public void setAId(int aId) {
        this.aId = aId;
    }

    public String getShs() {
        return shs;
    }

    public void setShs(String shs) {
        this.shs = shs;
    }

    @Override
    public String toString() {
        return "ClassPojo [Status = " + Status + ", aTeam = " + aTeam + ", hId = " + hId + ", hTeam = " + hTeam + ", gs = " + gs + ", sId = " + sId + ", hScore = " + hScore + ", stage = " + stage + ", aScore = " + aScore + ", extid = " + extid + ", id = " + id + ", time = " + time + ", aId = " + aId + ", shs = " + shs + "]";
    }
}
