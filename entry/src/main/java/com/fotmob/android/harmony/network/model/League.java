package com.fotmob.android.harmony.network.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "league", strict = false)
public class League {

    @Attribute(required = false)
    private String ccode;

    @Attribute(required = false)
    private String plName;

    @Attribute(required = false)
    private String isGrp;

    @Attribute(required = false)
    private String lr;

    @Attribute(required = false)
    private String name;

    @ElementList(name = "match", inline = true)
    private List<Match> match;

    @Attribute(required = false)
    private String grpName;

    @Attribute(required = false)
    private String ir;

    @Attribute(required = false)
    private String sl;

    @Attribute(required = false)
    private String id;

    @Attribute(required = false)
    private String pl;

    public String getCcode() {
        return ccode;
    }

    public void setCcode(String ccode) {
        this.ccode = ccode;
    }

    public String getPlName() {
        return plName;
    }

    public void setPlName(String plName) {
        this.plName = plName;
    }

    public String getIsGrp() {
        return isGrp;
    }

    public void setIsGrp(String isGrp) {
        this.isGrp = isGrp;
    }

    public String getLr() {
        return lr;
    }

    public void setLr(String lr) {
        this.lr = lr;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Match> getMatch() {
        return match;
    }

    public void setMatch(List<Match> match) {
        this.match = match;
    }

    public String getGrpName() {
        return grpName;
    }

    public void setGrpName(String grpName) {
        this.grpName = grpName;
    }

    public String getIr() {
        return ir;
    }

    public void setIr(String ir) {
        this.ir = ir;
    }

    public String getSl() {
        return sl;
    }

    public void setSl(String sl) {
        this.sl = sl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPl() {
        return pl;
    }

    public void setPl(String pl) {
        this.pl = pl;
    }

    @Override
    public String toString() {
        return "ClassPojo [ccode = " + ccode + ", plName = " + plName + ", isGrp = " + isGrp + ", lr = " + lr + ", name = " + name + ", match = " + match + ", grpName = " + grpName + ", ir = " + ir + ", sl = " + sl + ", id = " + id + ", pl = " + pl + "]";
    }
}
