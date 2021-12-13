package com.fotmob.android.harmony.utils;

import com.fotmob.android.WearableMatch;
import com.fotmob.android.WearableMatchEvent;
import com.fotmob.android.harmony.log.Log;
import com.google.gson.Gson;
import com.huawei.watch.kit.hiwear.HiWear;
import com.huawei.watch.kit.hiwear.p2p.*;
import ohos.app.Context;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;


public class FotMobHelper {

    private P2pClient p2pClient;
    private Gson gson;

    public FotMobHelper(Context context) {
        p2pClient = HiWear.getP2pClient(context);
        p2pClient.setPeerPkgName("com.mobilefootie.wc2010");
        p2pClient.setPeerFingerPrint("99EB36651CAF7752DC34770EE1AFBF5B5F4E07A906243904104EB5ACC9FFBAF4");
        gson = new Gson();
    }

    public void ping(PingCallback pingCallback) {
        p2pClient.ping(pingCallback);
    }

    public void sendMessage(FotMobMessage fotMobMessage, SendCallback sendCallback) {
        HiWearMessage hiWearMessage = new HiWearMessage.Builder()
                .setPayload(gson.toJson(fotMobMessage)
                        .getBytes(StandardCharsets.UTF_8))
                .build();
        Log.d("sendMessage()", hiWearMessage);
        p2pClient.send(hiWearMessage, sendCallback);
    }

    public FotMobMessage getFotMobMessage(HiWearMessage hiWearMessage) {
        try {
            return gson.fromJson(new String(hiWearMessage.getData(), StandardCharsets.UTF_8), FotMobMessage.class);
        } catch (Exception e) {
            Log.e(e, "Got exception while trying to parse message [%{public]s]. Returning null.", hiWearMessage.getDescription());
        }
        return null;
    }

    public void registerReceiver(Receiver receiver) {
        if (p2pClient != null && receiver != null) {
            p2pClient.registerReceiver(receiver);
        }
    }

    public void unregisterReceiver(Receiver receiver) {
        if (p2pClient != null && receiver != null) {
            p2pClient.unregisterReceiver(receiver);
        }
    }

    /**
     * Notice the non-optimal structure of this message object. It used to be better, but was cut to the bone
     * (less generic to more specific, but covering quite a few different cases) to improve the performance
     * of the basic Huawei watches (JavaScript only versions.
     * <p>
     * Fields with null values should not be sent over the wire.
     * <p>
     * This class represents both a list of matches (GET_MATCHES_RESPONSE) and match + events (GET_MATCH_RESPONSE).
     */
    public static class FotMobMessage {

        public final String cmd;
        private final String status; // Used for telling about errors
        private final String message; // Used for messages when there's a status (in practice error message)
        private final String eTag;
        private final HashMap<String, Object> multi; // Using HashMap directly so we don't get a really funky JSON lib Map implementation
        private final String matchId;
        public final ArrayList<WearableMatch> matches; // Using ArrayList directly so we don't get a really funky JSON lib Map implementation
        public final WearableMatch match;
        public final ArrayList<WearableMatchEvent> events; // Using ArrayList directly so we don't get a really funky JSON lib Map implementation
        public final ArrayList<Integer> teamIds; // Using ArrayList directly so we don't get a really funky JSON lib Map implementation

        public FotMobMessage(String command, String status, String message, String eTag, HashMap<String, Object> multiMessageMap, String matchId, ArrayList<WearableMatch> matches, WearableMatch match, ArrayList<WearableMatchEvent> events, ArrayList<Integer> teamIds) {
            this.cmd = command;
            this.status = status;
            this.message = message;
            this.eTag = eTag;
            this.multi = multiMessageMap;
            this.matchId = matchId;
            this.matches = matches;
            this.match = match;
            this.events = events;
            this.teamIds = teamIds;
        }

        public FotMobMessage(String command) {
            this(command, null, null, null, null, null, null, null, null, null);
        }

        public FotMobMessage(String command, String matchId) {
            this(command, null, null, null, null, matchId, null, null, null, null);
        }

        public FotMobMessage(String command, String eTag, HashMap<String, Object> multiMessageMap, ArrayList<WearableMatch> matches) {
            this(command, null, null, eTag, multiMessageMap, null, matches, null, null, null);
        }

        public FotMobMessage(String command, HashMap<String, Object> multiMessageMap, WearableMatch wearableMatch, ArrayList<WearableMatchEvent> wearableMatchEvents, String eTag) {
            this(command, null, null, eTag, multiMessageMap, null, null, wearableMatch, wearableMatchEvents, null);
        }

        @Override
        public String toString() {
            return "FotMobMessage{" +
                    "cmd='" + cmd + '\'' +
                    ", status='" + status + '\'' +
                    ", message='" + message + '\'' +
                    ", eTag='" + eTag + '\'' +
                    ", multi=" + multi +
                    ", matchId='" + matchId + '\'' +
                    ", matches=" + matches +
                    ", match=" + match +
                    ", events=" + events +
                    ", teamId=" + teamIds + '\'' +
                    '}';
        }

        public boolean isMultiMessage() {
            return multi != null && multi.size() > 0;
        }

        public int getMultiMessageNumber() {
            if (multi != null && multi.containsKey("num")) {
                return ((Double) multi.get("num")).intValue();
            }
            return 1;
        }

        public int getTotalNumberOfMessage() {
            if (multi != null && multi.containsKey("total")) {
                return ((Double) multi.get("total")).intValue();
            }
            return 1;
        }

        public String getMultiMessageId() {
            if (multi != null && multi.containsKey("id")) {
                return (String) multi.get("id");
            }
            return "";
        }
    }

}
