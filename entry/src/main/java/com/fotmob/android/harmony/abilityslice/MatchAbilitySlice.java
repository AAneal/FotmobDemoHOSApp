package com.fotmob.android.harmony.abilityslice;

import com.fotmob.android.WearableMatch;
import com.fotmob.android.WearableMatchEvent;
import com.fotmob.android.harmony.network.Const;
import com.fotmob.android.harmony.network.Utils;
import com.fotmob.android.harmony.network.model.Live;
import com.fotmob.android.harmony.network.model.MatchFacts;
import com.fotmob.android.harmony.utils.FotMobHelper;
import com.fotmob.android.harmony.MainAbility;
import com.fotmob.android.harmony.MatchEventsRecycleItemProvider;
import com.fotmob.android.harmony.utils.MunchHelper;
import com.fotmob.android.harmony.ResourceTable;
import com.fotmob.android.harmony.log.Log;
import com.huawei.watch.kit.hiwear.p2p.HiWearMessage;
import com.huawei.watch.kit.hiwear.p2p.Receiver;
import com.huawei.watch.kit.hiwear.p2p.SendCallback;
import ohos.aafwk.ability.AbilitySlice;
import ohos.aafwk.content.Intent;
import ohos.aafwk.content.IntentParams;
import ohos.agp.components.*;
import ohos.agp.components.element.ShapeElement;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class MatchAbilitySlice extends AbilitySlice implements Receiver, Component.ClickedListener {

    private final static String TAG = MatchAbilitySlice.class.getSimpleName();
    private FotMobHelper fotmobHelper;
    private Map<String, Map<Integer, List<WearableMatchEvent>>> tempMultiMessages = new HashMap<>();
    private Text statusText;
    private Button retryButton;
    private ListContainer listContainer;
    private Timer refreshTimer;
    private WearableMatch wearableMatch;
    private long timerStartDelay = 2000;
    private boolean hasInternetConnection;

    public MatchAbilitySlice() {
        Log.e(TAG, "const");
    }

    @Override
    public void onStart(Intent intent) {
        super.onStart(intent);
        Log.d(TAG, "onStart()", null);
        // hasInternetConnection = ((MainAbility) getAbility()).hasInternetConnection();
        wearableMatch = new WearableMatch();
        if (intent != null) {
            IntentParams intentParams = intent.getParams();
            if (intentParams != null) {
                wearableMatch.matchId = (String) intentParams.getParam("matchId");
                wearableMatch.matchStatus = (String) intentParams.getParam("matchStatus");
                wearableMatch.elapsedTime = (String) intentParams.getParam("elapsedTime");
                wearableMatch.matchTime = (String) intentParams.getParam("matchTime");
                wearableMatch.scoreHome = (int) intentParams.getParam("scoreHome");
                wearableMatch.scoreAway = (int) intentParams.getParam("scoreAway");
                wearableMatch.teamIdHome = (Integer) intentParams.getParam("teamIdHome");
                wearableMatch.teamIdAway = (Integer) intentParams.getParam("teamIdAway");
                wearableMatch.teamNameHome = (String) intentParams.getParam("teamNameHome");
                wearableMatch.teamNameAway = (String) intentParams.getParam("teamNameAway");
            }
        }
        Log.d(TAG, "onStart(): %{public}s", wearableMatch);
        setUIContent(ResourceTable.Layout_ability_match);

        // managePageSlider();

        getMatchDetailsFromAPI();

        //fotmobHelper = new FotMobHelper(this);
    }

    private void getMatchDetailsFromAPI() {


        String url = Const.MATCH_FACTS_URL.concat(".3557709.fot.gz");

        Call<ResponseBody> call = Utils.getApiInstance().getMatchDetail(url);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {


                Log.d(TAG, "OnResponse", response.body().toString());

                try {
                    ParseMatchEvents(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {

                Log.d(TAG, "OnResponse", "Failed");

                //setStatusMessage(getContext().getString(ResourceTable.String_connection_failed));
            }
        });
    }

    private void ParseMatchEvents(String data) {



        StringTokenizer stEvents = new StringTokenizer(data, "#");

        Log.d(TAG, "OnResponse", stEvents);

    }


    private void managePageSlider() {

        PageSlider pageSlider = (PageSlider) findComponentById(ResourceTable.Id_pageSlider);
        pageSlider.setVisibility(Component.HIDE);

        Log.d(TAG, "onStart(): pageSlider %{public}s", pageSlider);

        PageSliderIndicator pageSliderIndicator = (PageSliderIndicator) findComponentById(ResourceTable.Id_pageSliderIndicator);
        pageSlider.setProvider(new PageSliderProvider() {
            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public Object createPageInContainer(ComponentContainer componentContainer, int position) {
                Log.d(TAG, "createPageInContainer(%{public}d):%{public}s", position, MatchAbilitySlice.this);
                if (position == 0) {
                    Component matchPageComponent = LayoutScatter.getInstance(MatchAbilitySlice.this).parse(ResourceTable.Layout_page_match_logos, componentContainer, true);

                    statusText = (Text) matchPageComponent.findComponentById(ResourceTable.Id_text_status);

                    updateUi(wearableMatch, null);

                    return 0;
                }
                if (position == 1) {
                    Component eventsPageComponent = LayoutScatter.getInstance(MatchAbilitySlice.this).parse(ResourceTable.Layout_page_events, componentContainer, true);
                    listContainer = (ListContainer) eventsPageComponent.findComponentById(ResourceTable.Id_list_container);
                    return 1;
                }
                return null;
            }

            @Override
            public void destroyPageFromContainer(ComponentContainer container, int position, Object object) {
                Log.d(TAG, "destroyPageFromContainer(%{public}d, %{public}s):%{public}s", position, object, MatchAbilitySlice.this);
                container.removeAllComponents();
            }

            @Override
            public boolean isPageMatchToObject(Component component, Object object) {
                Log.d(TAG, "isPageMatchToObject(%{public}s)", object);
                return true;
            }
        });
        pageSliderIndicator.setPageSlider(pageSlider);

        ShapeElement normalElement = new ShapeElement(this, ResourceTable.Graphic_indicator_normal);
        normalElement.setBounds(0, 0, 30, 30);
        pageSliderIndicator.setItemNormalElement(normalElement);
        ShapeElement selectedElement = new ShapeElement(this, ResourceTable.Graphic_indicator_selected);
        selectedElement.setBounds(0, 0, 40, 40);
        pageSliderIndicator.setItemSelectedElement(selectedElement);
        pageSliderIndicator.setItemOffset(12);
        pageSliderIndicator.setClickedListener(new Component.ClickedListener() {
            @Override
            public void onClick(Component component) {
                pageSlider.setCurrentPage((pageSlider.getCurrentPage() + 1) % 2); // Just switching between the two pages
            }
        });
    }

    @Override
    public void onActive() {
        Log.d(TAG, "onActive()", null);
        // super.onActive();
        // fotmobHelper.registerReceiver(this);
        //startTimer();
    }

    private void startTimer() {
        Log.d(TAG, "startTimer(), delay:%{public}s", timerStartDelay);
        refreshTimer = new Timer();
        refreshTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Log.d("run()");
                loadMatchDetails(false);
            }
        }, timerStartDelay, 59000);
    }

    private void stopTimer() {
        Log.d(TAG, "stopTimer()", null);
        if (refreshTimer != null) {
            refreshTimer.cancel();
            refreshTimer.purge();
            refreshTimer = null;
        }
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
        // fotmobHelper.unregisterReceiver(this);
        //stopTimer();
        //timerStartDelay = 0;
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

    private void showRefreshButton() {
        getUITaskDispatcher().asyncDispatch(new Runnable() {
            @Override
            public void run() {
                if (retryButton != null) {
                    retryButton.setVisibility(Component.VISIBLE);
                }
            }
        });
    }

    private void hideRefreshButton() {
        getUITaskDispatcher().asyncDispatch(new Runnable() {
            @Override
            public void run() {
                if (retryButton != null) {
                    retryButton.setVisibility(Component.HIDE);
                }
            }
        });
    }

    private void setStatusMessage(String message) {
        Log.d("setStatusMessage():" + message);
        Log.d(TAG, "" + this + ";" + statusText);
        getUITaskDispatcher().asyncDispatch(new Runnable() {
            @Override
            public void run() {
                if (statusText != null) {
                    statusText.setText(message);
                }
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
                    case "GET_MATCH_RESPONSE":
                        Log.d("matchdetails:%{public}s", fotMobMessage);
                        if (fotMobMessage.isMultiMessage()) { // Matches split into several messages
                            int multiMessageNumber = fotMobMessage.getMultiMessageNumber();
                            int totalNumOfMessages = fotMobMessage.getTotalNumberOfMessage();
                            int messagesSoFar = 1;
                            String id = fotMobMessage.getMultiMessageId();
                            if (tempMultiMessages.containsKey(id)) {
                                messagesSoFar += tempMultiMessages.get(id).size();
                            } else {
                                tempMultiMessages.put(id, new HashMap<>());
                            }
                            tempMultiMessages.get(id).put(multiMessageNumber, fotMobMessage.events);
                            if (messagesSoFar >= totalNumOfMessages) { // Got every part of message
                                Log.d(TAG, "Got all messages");
                                List<WearableMatchEvent> matchEvents = new ArrayList<>();
                                for (int i = 1; i <= totalNumOfMessages; i++) {
                                    matchEvents.addAll(tempMultiMessages.get(id).get(i));
                                }
                                tempMultiMessages.remove(id);
                                if (fotMobMessage.match != null) {
                                    wearableMatch = fotMobMessage.match;
                                }
                                setStatusMessage(null);
                                updateUi(wearableMatch, matchEvents);
                            } else {
                                Log.d(TAG, "Got multi message, but missing %{public}d message(s).", totalNumOfMessages - messagesSoFar);
                            }
                            hideRefreshButton();
                        } else { // Everything in one message
                            ArrayList<WearableMatchEvent> matchEvents = fotMobMessage.events;
                            if (fotMobMessage.match != null) {
                                wearableMatch = fotMobMessage.match;
                            }
                            setStatusMessage(null);
                            updateUi(wearableMatch, matchEvents);
                        }
                        break;
                    default:
                        Log.d(TAG, "Got unsupported FotMob message %{public}s. Ignoring it.", fotMobMessage);
                        break;
                }
            }
        } catch (Exception e) {
            Log.e(e, "Got exception trying to parse received message. Ignoring problem.");
            e.printStackTrace();
        }
    }

    private void updateUi(final WearableMatch wearableMatch, final List<WearableMatchEvent> matchEvents) {
        getUITaskDispatcher().asyncDispatch(() -> {
            if (wearableMatch != null) {
                Text text;
                if (hasInternetConnection) {
                    Image image = (Image) findComponentById(ResourceTable.Id_image_homeTeam);
                    MunchHelper.loadTeamLogo(getContext(), image, wearableMatch.teamIdHome, ResourceTable.Media_ic_team_placeholder_24_px);
                    image = (Image) findComponentById(ResourceTable.Id_image_awayTeam);
                    MunchHelper.loadTeamLogo(getContext(), image, wearableMatch.teamIdAway, ResourceTable.Media_ic_team_placeholder_24_px);
                } else { // No Internet - no logos
                    text = (Text) findComponentById(ResourceTable.Id_text_homeTeam);
                    text.startAutoScrolling();
                    text.setText(wearableMatch.teamNameHome);
                    text = (Text) findComponentById(ResourceTable.Id_text_awayTeam);
                    text.startAutoScrolling();
                    text.setText(wearableMatch.teamNameAway);
                }
                if ("Postponed".equals(wearableMatch.matchStatus)) {
                    text = (Text) findComponentById(ResourceTable.Id_text_scoreOrTime);
                    text.setText(wearableMatch.matchTime);
                    text = (Text) findComponentById(ResourceTable.Id_text_elapsedTime);
                    text.setText(wearableMatch.matchStatus);
                } else if (!"NotStarted".equals(wearableMatch.matchStatus)) {
                    text = (Text) findComponentById(ResourceTable.Id_text_scoreOrTime);
                    text.setText((wearableMatch.scoreHome) + " - " + wearableMatch.scoreAway);
                    text = (Text) findComponentById(ResourceTable.Id_text_elapsedTime);
                    text.setText(getNiceElapsedTime(wearableMatch.elapsedTime));
                } else { // Started
                    text = (Text) findComponentById(ResourceTable.Id_text_scoreOrTime);
                    text.setText(wearableMatch.matchTime);
                    text = (Text) findComponentById(ResourceTable.Id_text_elapsedTime);
                    text.setText(" ");
                }
            }
            if (listContainer != null) {
                listContainer.setVisibility(Component.VISIBLE);
                if (matchEvents != null && matchEvents.size() > 0) {
                    listContainer.setItemProvider(new MatchEventsRecycleItemProvider(matchEvents, MatchAbilitySlice.this, 2));
                } else {
                    List<WearableMatchEvent> dummyMatchEvents = new ArrayList<>();
                    dummyMatchEvents.add(new WearableMatchEvent("", "", "No match events", ""));
                    listContainer.setItemProvider(new MatchEventsRecycleItemProvider(dummyMatchEvents, MatchAbilitySlice.this, 2));
                }
            }
        });
    }

    private String getNiceElapsedTime(String elapsedTime) {
        if ("FT".equals(elapsedTime)) {
            return "Full time";
        }
        if ("HT".equals(elapsedTime)) {
            return "Half time";
        }
        return elapsedTime;
    }

    private void loadMatchDetails(boolean displayLoadingMessage) {
        fotmobHelper.sendMessage(new FotMobHelper.FotMobMessage("GET_MATCH", (String) wearableMatch.matchId), new SendCallback() {
            @Override
            public void onSendResult(int result) {
                Log.d(TAG, "GET_MATCH.onSendResult():%{public}d", result);
                if (result != 207) {
                    setStatusMessage("Failed to get match details. Please ensure that the phone app is running. Error code: " + result);
                    showRefreshButton();
                }
            }

            @Override
            public void onSendProgress(long progress) {
                //Log.d(TAG, "GET_MATCH.onSendProgress(%{public}s)", progress);
            }
        });
        if (displayLoadingMessage) {
            setStatusMessage("Fetching match details...");
        }
    }

    @Override
    public void onClick(Component component) {
        switch (component.getId()) {
            case ResourceTable.Id_button_retry:
                loadMatchDetails(true);
                break;
        }
    }

}
