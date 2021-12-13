package com.fotmob.android.harmony.network.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "live")
public class Live {

    @Element
    private Exmatches exmatches;

    public Exmatches getExmatches() {
        return exmatches;
    }

    public void setExmatches(Exmatches exmatches) {
        this.exmatches = exmatches;
    }

    @Override
    public String toString() {
        return "ClassPojo [exmatches = " + exmatches + "]";
    }
}

