package com.fotmob.android.harmony.abilityslice;

import com.fotmob.android.WearableMatch;
import com.fotmob.android.harmony.MatchWithLogosRecycleItemProvider;
import com.fotmob.android.harmony.network.Utils;
import com.fotmob.android.harmony.network.model.Exmatches;
import com.fotmob.android.harmony.network.model.Live;
import com.fotmob.android.harmony.network.model.Match;
import com.fotmob.android.harmony.network.model.XmlResponse;
import com.fotmob.android.harmony.utils.FotMobHelper;
import com.fotmob.android.harmony.ResourceTable;
import com.fotmob.android.harmony.log.Log;
import com.fotmob.android.harmony.utils.utils;
import com.github.ybq.core.style.DoubleBounce;
import com.huawei.watch.kit.hiwear.p2p.HiWearMessage;
import com.huawei.watch.kit.hiwear.p2p.PingCallback;
import com.huawei.watch.kit.hiwear.p2p.Receiver;
import com.huawei.watch.kit.hiwear.p2p.SendCallback;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.agp.components.*;
import ohos.data.DatabaseHelper;
import ohos.data.preferences.Preferences;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * First slice start:
 * onForeground() -> onStart() --> onActive()
 * <p>
 * Screen goes off:
 * onInactive() --> onBackground()
 * Screen comes on:
 * onForeground() --> onActive()
 * <p>
 * Click match:
 * onInactive() --> onBackground()
 * Back from match:
 * onForeground() --> onActive()
 */
public class MainAbilitySlice extends AbilitySlice implements Receiver, Component.ClickedListener, ListContainer.ItemClickedListener {

    private final static String TAG = MainAbilitySlice.class.getSimpleName();
    private FotMobHelper fotmobHelper;
    private Map<String, Map<Integer, List<WearableMatch>>> tempMultiMessages = new HashMap<>();
    private Text statusText;
    private Button retryButton;
    private ProgressBar circularLoader;
    private PageSlider pageSlider;
    private ListContainer listContainer;
    private Boolean isConnectionFailed = false;
    private Image icon;

    private final String PREFERENCE_NAME = "favTeamIds";

    @Override
    public void onStart(Intent intent) {
        Log.d(TAG, "onStart()", null);
        super.onStart(intent);
        super.setUIContent(ResourceTable.Layout_ability_main);
        statusText = (Text) findComponentById(ResourceTable.Id_text_status);
        retryButton = (Button) findComponentById(ResourceTable.Id_button_retry);
        icon = (Image) findComponentById(ResourceTable.Id_icon);
        retryButton.setClickedListener(this);

        circularLoader = (ProgressBar) findComponentById(ResourceTable.Id_progressbar);

        pageSlider = (PageSlider) findComponentById(ResourceTable.Id_qrPageSlider);

        initListContainer();

        initFotMobHelper();

        manageSpinKit();

        if (!checkIfFavTeamsStoredInPreference()) {

            setComponentVisibility(circularLoader, true);
            manageQRPageSlider();
            checkIfAppInstalledOnPhone();

        } else {

            getTodayMatchListFromAPI();
        }
    }

    private void manageSpinKit() {

        DoubleBounce doubleBounce = new DoubleBounce();
        doubleBounce.setPaintColor(0XFF1AAF5D);
        doubleBounce.onBoundsChange(0, 0, circularLoader.getWidth(), circularLoader.getHeight());
        doubleBounce.setComponent(circularLoader);
        circularLoader.setProgressElement(doubleBounce);
        circularLoader.setIndeterminate(true);
        circularLoader.addDrawTask((component, canvas) -> doubleBounce.drawToCanvas(canvas));

    }

    private void initFotMobHelper() {

        fotmobHelper = new FotMobHelper(this);
        fotmobHelper.registerReceiver(this);
    }

    private void initListContainer() {

        //Manage List container
        listContainer = (ListContainer) findComponentById(ResourceTable.Id_list_container);
        listContainer.enableScrollBar(Component.AXIS_Y, true);

        //Set oval mode
        listContainer.setMode(Component.OVAL_MODE);

       // listContainer.setItemClickedListener(this);

    }

    private boolean checkIfFavTeamsStoredInPreference() {

        ArrayList<Integer> teamIds = getFavTeamIdsFromPreference();

        if (teamIds == null || teamIds.size() == 0) {
            return false;
        } else {
            return true;
        }
    }

    private ArrayList<Integer> getFavTeamIdsFromPreference() {

        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        String filename = PREFERENCE_NAME;
        Preferences preferences = databaseHelper.getPreferences(filename);

        int size = preferences.getInt("size", 0);

        Log.d(TAG, "Fav Team Ids size from preference", size);

        ArrayList<Integer> teamIds = new ArrayList<>();
        for (int i = 0; i < size; i++) {

            teamIds.add(preferences.getInt("" + i, 0));

        }

        preferences.flush();
        return teamIds;
    }

    private void storeFavTeamIdsInPreference(ArrayList<Integer> teamIds) {

        DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
        String filename = PREFERENCE_NAME;
        Preferences preferences = databaseHelper.getPreferences(filename);

        preferences.putInt("size", teamIds.size());

        for (int i = 0; i < teamIds.size(); i++) {

            preferences.putInt("" + i, teamIds.get(i));
        }

        preferences.flush();
    }

    private void getTodayMatchListFromAPI() {

        String pattern = "yyyyMMdd";
        SimpleDateFormat Date = new SimpleDateFormat(pattern);
        Calendar cals = Calendar.getInstance();
        String currentDate = Date.format(cals.getTime());

        Map<String, String> map = new HashMap<>();
        map.put("date", currentDate);
        map.put("tz", "7200000");
        map.put("appver", "8775");
        map.put("ongoing", "true");

        Log.d(TAG, "On API calling", " API called");

        setComponentVisibility(circularLoader, true);

       /* Call<ResponseBody> call = Utils.getApiInstance().getMatchData(map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                Log.d(TAG, "OnResponse", response.body().toString());
                try {
                    String data = response.body().string();
                    String dataNew = data.concat("a");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {

                Log.d(TAG, "OnResponse", "Failed");

                //setStatusMessage(getContext().getString(ResourceTable.String_connection_failed));
            }
        });*/


/*
        Call<XmlResponse> call = Utils.getApiInstance().getMatchData(map);

        call.enqueue(new Callback<XmlResponse>() {
            @Override
            public void onResponse(Call<XmlResponse> call, Response<XmlResponse> response) {

                isConnectionFailed = false;
                setComponentVisibility(circularLoader, false);

                Log.d(TAG, "OnResponse", response.body());

                ArrayList<Integer> favTeamIds = getFavTeamIdsFromPreference();

                List<WearableMatch> matches = filterFavMatchList(favTeamIds, response.body().getLive().getExmatches());

                if (matches.size() == 0) {

                    setComponentVisibility(statusText, true);
                    setComponentVisibility(retryButton, true);
                    setStatusMessage(getContext().getString(ResourceTable.String_no_fav_team_playing));
                } else {

                    //remove duplicate matches
                    Set<WearableMatch> s = new HashSet<WearableMatch>();
                    s.addAll(matches);

                    ArrayList updatedMatches = new ArrayList<WearableMatch>();
                    updatedMatches.addAll(s);

                    //sorting matches with start time
                    Collections.sort(updatedMatches);

                    //List the matches in the List Container
                    updateMatchList(updatedMatches);
                }
            }

            @Override
            public void onFailure(Call<XmlResponse> call, Throwable throwable) {


                isConnectionFailed = true;

                setComponentVisibility(circularLoader, false);
                setComponentVisibility(statusText, true);
                setComponentVisibility(retryButton, true);

                setStatusMessage(getContext().getString(ResourceTable.String_connection_failed));
            }
        });
*/


        Call<Live> call = Utils.getApiInstance().getMatchData(map);
        call.enqueue(new Callback<Live>() {
            @Override
            public void onResponse(Call<Live> call, Response<Live> response) {

                isConnectionFailed = false;
                setComponentVisibility(circularLoader, false);

                Log.d(TAG, "OnResponse", response.body());

                ArrayList<Integer> favTeamIds = getFavTeamIdsFromPreference();

                List<WearableMatch> matches = filterFavMatchList(favTeamIds, response.body().getExmatches());

                if (matches.size() == 0) {

                    setComponentVisibility(statusText, true);
                    setComponentVisibility(retryButton, true);
                    setStatusMessage(getContext().getString(ResourceTable.String_no_fav_team_playing));
                } else {

                    //remove duplicate matches
                    Set<WearableMatch> s = new HashSet<WearableMatch>();
                    s.addAll(matches);

                    ArrayList updatedMatches = new ArrayList<WearableMatch>();
                    updatedMatches.addAll(s);

                    //sorting matches with start time
                    Collections.sort(updatedMatches);

                    //List the matches in the List Container
                    updateMatchList(updatedMatches);
                }
            }

            @Override
            public void onFailure(Call<Live> call, Throwable throwable) {

                isConnectionFailed = true;

                setComponentVisibility(circularLoader, false);
                setComponentVisibility(statusText, true);
                setComponentVisibility(retryButton, true);

                setStatusMessage(getContext().getString(ResourceTable.String_connection_failed));
            }
        });
    }

    private void updateMatchList(final List<WearableMatch> matches) {


        getUITaskDispatcher().asyncDispatch(new Runnable() {
            @Override
            public void run() {

                if (listContainer != null) {

                    circularLoader.setVisibility(Component.HIDE);

                    if (matches != null && matches.size() > 0) {

                        listContainer.setVisibility(Component.VISIBLE);

                        listContainer.setItemProvider(new MatchWithLogosRecycleItemProvider(matches, MainAbilitySlice.this));

                    } else {
                        listContainer.setVisibility(Component.HIDE);
                    }
                }
            }
        });
    }

    private List<WearableMatch> filterFavMatchList(ArrayList<Integer> favTeamIds, Exmatches exmatches) {

        ArrayList<WearableMatch> matchList = new ArrayList<>();

        for (int i = 0; i < exmatches.getLeague().size(); i++) {

            for (int j = 0; j < exmatches.getLeague().get(i).getMatch().size(); j++) {

                for (int k = 0; k < favTeamIds.size(); k++) {

                    int teamIdAway = exmatches.getLeague().get(i).getMatch().get(j).getAId();
                    int teamIdHome = exmatches.getLeague().get(i).getMatch().get(j).getHId();

                    if (teamIdAway == favTeamIds.get(k) || teamIdHome == favTeamIds.get(k)) {

                        Match tempMatch = exmatches.getLeague().get(i).getMatch().get(j);

                        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
                        try {
                            Date matchDate = sdf.parse(tempMatch.getTime());
                            String millisecs = matchDate.getTime() + "";

                            // Date elapsedTime = sdf.parse(tempMatch.getShs());
                            //String elapsedMillisecs = elapsedTime.getTime() + "";
                            WearableMatch match = new WearableMatch(tempMatch.getId(), 0, 0, tempMatch.getHId(), tempMatch.getHTeam(), tempMatch.getHScore(), tempMatch.getAId(), tempMatch.getATeam(), tempMatch.getAScore(), millisecs, "", tempMatch.getStatus().equals("F") ? "started" : "NotStarted");
                            matchList.add(match);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        return matchList;
    }

    @Override
    public void onActive() {
        Log.d(TAG, "onActive()", null);
        super.onActive();
    }

    @Override
    protected void onForeground(Intent intent) {
        Log.d(TAG, "onForeground()", null);
        super.onForeground(intent);
    }

    @Override
    protected void onBackground() {
        Log.d(TAG, "onBackground()", null);
        super.onBackground();
    }

    @Override
    protected void onInactive() {
        Log.d(TAG, "onInactive()", null);
        fotmobHelper.unregisterReceiver(this);
        super.onInactive();
    }

    @Override
    protected void onBackPressed() {
        Log.d(TAG, "onBackPressed()", null);
        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop()", null);
        super.onStop();
    }

    private void checkIfAppInstalledOnPhone() {

        //When the wearable device sends the ping command to the phone.
        //204= Result code displayed when the app has not been installed on the phone.
        //205= Result code displayed when the app has been installed on the phone.
        fotmobHelper.ping(new PingCallback() {
            @Override
            public void onResult(int result) {
                try {
                    if (result == 204) {

                        Log.d(TAG, getNiceErrorMessage(204));

                        setComponentVisibility(pageSlider, true);
                        setComponentVisibility(circularLoader, false);
                    } else {

                        Log.d(TAG, getNiceErrorMessage(205));

                        setComponentVisibility(pageSlider, false);
                        setComponentVisibility(retryButton, false);
                        sendMessageToPhoneApp();
                    }
                } catch (Exception e) {

                    Log.e(e, "Got exception while trying to handle ping result.");

                    e.printStackTrace();
                }
            }
        });
    }

    private void setComponentVisibility(Component component, Boolean bool) {

        getUITaskDispatcher().asyncDispatch(new Runnable() {
            @Override
            public void run() {

                component.setVisibility((bool == true) ? Component.VISIBLE : Component.HIDE);
            }
        });

    }

    private void manageQRPageSlider() {

        //PageSlider pageSlider = (PageSlider) findComponentById(ResourceTable.Id_qrPageSlider);
        pageSlider.setCircularModeEnabled(false);
        pageSlider.setOrientation(Component.VERTICAL);
        pageSlider.setReboundEffect(true);
        pageSlider.setSlidingPossible(true);
        pageSlider.setMode(Component.OVAL_MODE);

        pageSlider.addPageChangedListener(new PageSlider.PageChangedListener() {
            @Override
            public void onPageSliding(int i, float v, int i1) {

            }

            @Override
            public void onPageSlideStateChanged(int i) {

            }

            @Override
            public void onPageChosen(int i) {

                if (i == 0) {

                    icon.setPixelMap(ResourceTable.Media_icon_small);

                } else if (i == 1) {
                    icon.setPixelMap(ResourceTable.Media_appgallery_logo);

                } else if (i == 2) {

                    icon.setPixelMap(ResourceTable.Media_playstore_logo);
                }

            }
        });

        pageSlider.setProvider(new PageSliderProvider() {
            @Override
            public int getCount() {
                return 3;
            }

            @Override
            public Object createPageInContainer(ComponentContainer componentContainer, int position) {

                if (position == 0) {

                    Component matchPageComponent = LayoutScatter.getInstance(getContext()).parse(ResourceTable.Layout_page_qrtext, componentContainer, true);

                    //icon.setPixelMap(ResourceTable.Media_icon_ball);
                    return 0;
                }
                if (position == 1) {

                    Component eventsPageComponent = LayoutScatter.getInstance(getContext()).parse(ResourceTable.Layout_page_qrcode, componentContainer, true);
                    Image qrImage = (Image) eventsPageComponent.findComponentById(ResourceTable.Id_qr_image);

                    //icon.setPixelMap(ResourceTable.Media_icon_ball);
                    String url = "https://appgallery.huawei.com/app/C101679351";
                    utils.generateCommonQrCode(url, qrImage);
                    return 1;
                }
                if (position == 2) {

                    Component eventsPageComponent = LayoutScatter.getInstance(getContext()).parse(ResourceTable.Layout_page_qrcode, componentContainer, true);
                    Image qrImage = (Image) eventsPageComponent.findComponentById(ResourceTable.Id_qr_image);

                    //icon.setPixelMap(ResourceTable.Media_icon_small);
                    String url = "https://play.google.com/store/apps/details?id=com.mobilefootie.wc2010";
                    utils.generateCommonQrCode(url, qrImage);
                    return 2;
                }
                return null;
            }

            @Override
            public void destroyPageFromContainer(ComponentContainer container, int position, Object object) {
                container.removeAllComponents();
            }

            @Override
            public boolean isPageMatchToObject(Component component, Object object) {
                return true;
            }
        });
    }

    private void sendMessageToPhoneApp() {
        //The wearable device communicates with the phone.
        //206 = Result code displayed when the service communications fail.
        //207 = Result code displayed when the service communications succeed.
        //150002 = Result code when the bluetooth connection is not made with the mobile device
        fotmobHelper.sendMessage(new FotMobHelper.FotMobMessage("GET_FAV_TEAMS"), new SendCallback() {
            @Override
            public void onSendResult(int result) {
                setComponentVisibility(circularLoader, false);
                Log.d("GET_MATCHES.onSendResult():%{public}d", result);
                if (result == 206) {

                    setComponentVisibility(statusText, true);
                    setStatusMessage(getContext().getString(ResourceTable.String_open_app_sync_data));
                    setComponentVisibility(retryButton, true);
                } else if (result == 207) {

                    setComponentVisibility(circularLoader, true);
                    setComponentVisibility(statusText, false);
                    setComponentVisibility(retryButton, false);
                } else {

                    setComponentVisibility(statusText, true);
                    setStatusMessage(getContext().getString(ResourceTable.String_bluetooth_connection_failed));
                    setComponentVisibility(retryButton, true);
                }
            }

            @Override
            public void onSendProgress(long progress) {


                Log.d("GET_MATCHES.onSendProgress(%{public}s)", progress);
            }
        });
    }

    private String getNiceErrorMessage(int errorCode) {
        switch (errorCode) {
            case 203:
            case 204:
                return "The app is not installed on the phone";
            case 206:
                return "Is FotMob running on the phone?\nPlease relaunch the app and try again.";
            case 207:
                return "Message sent successfully to the phone";
        }
        return "Message sent successfully to the phone";
    }

    private void setStatusMessage(String message) {

        Log.d("setStatusMessage():" + message);

        getUITaskDispatcher().asyncDispatch(new Runnable() {
            @Override
            public void run() {

                statusText.setText(message);
            }
        });
    }

    @Override
    public void onReceiveMessage(HiWearMessage hiWearMessage) {
        try {
            Log.d(TAG, "onReceivedMessage():%{public}s", hiWearMessage);
            FotMobHelper.FotMobMessage fotMobMessage = fotmobHelper.getFotMobMessage(hiWearMessage);
            if (fotMobMessage != null) {
                switch (fotMobMessage.cmd) {
                    case "PING":
                        Log.d("Got PING. Sending back PONG!");
                        fotmobHelper.sendMessage(new FotMobHelper.FotMobMessage("PONG"), new SendCallback() {
                            @Override
                            public void onSendResult(int result) {
                                Log.d("PONG.onSendResult(%{public}s)", result);
                            }

                            @Override
                            public void onSendProgress(long progress) {
                                Log.d("PONG.onSendProgress(%{public}s)", progress);
                            }
                        });
                        break;
                    case "GET_MATCHES_RESPONSE":
                        if (fotMobMessage.isMultiMessage()) { // Matches split into several messages

                        } else { // Everything in one message
                            List<WearableMatch> matches = fotMobMessage.matches;
                            statusText.setVisibility(Component.VISIBLE);
                            if (matches == null || matches.size() == 0) {

                                setStatusMessage("No favorite teams are playing today. And there are no matches marked as favorite or that have alerts.");
                                setComponentVisibility(retryButton, true);
                            } else {


                            }
                        }
                        break;

                    case "GET_FAV_TEAMS_RESPONSE":
                        if (fotMobMessage.isMultiMessage()) { // Matches split into several messages

                        } else { // Everything in one message
                            ArrayList<Integer> teamIds = fotMobMessage.teamIds;

                            if (teamIds == null || teamIds.size() == 0) {


                                //retryButton.setVisibility(Component.INVISIBLE);
                                setComponentVisibility(retryButton, true);
                                setComponentVisibility(statusText, true);
                                setComponentVisibility(circularLoader, false);
                                setStatusMessage("No favorite teams are playing today. And there are no matches marked as favorite or that have alerts.");
                            } else {
                                setComponentVisibility(circularLoader, false);
                                setComponentVisibility(statusText, false);
                                setComponentVisibility(retryButton, false);
                                storeFavTeamIdsInPreference(teamIds);
                                getTodayMatchListFromAPI();
                            }
                        }
                        break;
                    default:
                        Log.d("Got unsupported FotMob message %{public}s. Ignoring it.", fotMobMessage);
                        break;
                }
            }
        } catch (Exception e) {
            Log.e(e, "Got exception trying to parse received message. Ignoring problem.");
            e.printStackTrace();
        }
    }


    // Retry Button OnClick
    @Override
    public void onClick(Component component) {

        setComponentVisibility(statusText, false);
        setComponentVisibility(retryButton, false);
        setComponentVisibility(circularLoader, true);
        if (isConnectionFailed)
            getTodayMatchListFromAPI();
        else
            sendMessageToPhoneApp();
    }

    //List Container Click
    @Override
    public void onItemClicked(ListContainer listContainer, Component component, int position, long l) {

        WearableMatch wearableMatch = (WearableMatch) listContainer.getItemProvider().getItem(position);
        present(new MatchAbilitySlice(), new Intent()
                .setParam("matchId", wearableMatch.matchId)
                .setParam("matchStatus", wearableMatch.matchStatus)
                .setParam("elapsedTime", wearableMatch.elapsedTime)
                .setParam("matchTime", wearableMatch.matchTime)
                .setParam("scoreHome", wearableMatch.scoreHome)
                .setParam("scoreAway", wearableMatch.scoreAway)
                .setParam("teamIdHome", wearableMatch.teamIdHome)
                .setParam("teamIdAway", wearableMatch.teamIdAway)
                .setParam("teamNameHome", wearableMatch.teamNameHome)
                .setParam("teamNameAway", wearableMatch.teamNameAway));
    }
}

