package com.fotmob.android;

public class WearableMatchEvent {

    public String elapsedTime;
    public String eventType;
    public String line1;
    public String line2;

    public WearableMatchEvent(String elapsedTime, String eventType, String line1, String line2) {
        this.elapsedTime = elapsedTime;
        this.eventType = eventType;
        this.line1 = line1;
        this.line2 = line2;
    }
}
