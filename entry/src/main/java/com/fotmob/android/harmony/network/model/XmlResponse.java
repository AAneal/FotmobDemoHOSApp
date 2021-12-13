package com.fotmob.android.harmony.network.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "abc")
public class XmlResponse {

    @Element
    private Live live;

    public Live getLive() {
        return live;
    }

    public void setLive(Live exmatches) {
        this.live = live;
    }

    @Override
    public String toString() {
        return "ClassPojo [Live = " + live + "]";
    }
}
