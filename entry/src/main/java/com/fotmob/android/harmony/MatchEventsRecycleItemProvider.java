package com.fotmob.android.harmony;

import com.fotmob.android.WearableMatchEvent;
import com.fotmob.android.harmony.ResourceTable;
import com.fotmob.android.harmony.log.Log;
import ohos.aafwk.ability.AbilitySlice;
import ohos.agp.components.*;

import java.util.List;

public class MatchEventsRecycleItemProvider extends RecycleItemProvider {

    private final static String TAG = MatchEventsRecycleItemProvider.class.getSimpleName();
    
    private List<WearableMatchEvent> list;
    private AbilitySlice slice;

    public MatchEventsRecycleItemProvider(List<WearableMatchEvent> list, AbilitySlice slice, int numOfDummyElements) {
        if (numOfDummyElements > 0) {
            for (int i = 0; i < numOfDummyElements; i++) {
                list.add(0, null);
                list.add(null);
            }
        }
        this.list = list;
        this.slice = slice;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Component getComponent(int position, Component convertComponent, ComponentContainer componentContainer) {
        try {
            if (convertComponent == null) {
                convertComponent = LayoutScatter.getInstance(slice).parse(ResourceTable.Layout_list_item_event, null, false);
            }
            WearableMatchEvent wearableMatchEvent = list.get(position);
            if (wearableMatchEvent != null) {
                convertComponent.findComponentById(ResourceTable.Id_layout_root).setVisibility(Component.VISIBLE);
                Text text = (Text) convertComponent.findComponentById(ResourceTable.Id_text_elapsedTime);
                text.setText(wearableMatchEvent.elapsedTime);
                Image image = (Image) convertComponent.findComponentById(ResourceTable.Id_image_icon);
                setPixelMap(image, wearableMatchEvent.eventType);
                text = (Text) convertComponent.findComponentById(ResourceTable.Id_text_line1);
                text.setText(wearableMatchEvent.line1);
                text = (Text) convertComponent.findComponentById(ResourceTable.Id_text_line2);
                text.setText(wearableMatchEvent.line2);
            } else { // Dummy element
                convertComponent.findComponentById(ResourceTable.Id_layout_root).setVisibility(Component.INVISIBLE);
            }
        } catch (Exception e) {
            Log.e(e, "Got exception while trying to bind list item. Ignoring problem.");
        }
        return convertComponent;
    }

    private void setPixelMap(Image image, String eventType) {
        if (image == null) {
            return;
        }
        if (eventType == null) {
            image.setVisibility(Component.INVISIBLE);
            return;
        }
        image.setVisibility(Component.VISIBLE);
        switch (eventType) {
            case "Substituton": // Yes, that is a typo (from the client)
                image.setPixelMap(ResourceTable.Media_match_facts_innbytte);
                break;
            case "Goal":
            case "OwnGoal":
            case "Penalty":
                image.setPixelMap(ResourceTable.Media_icon_ball);
                break;
            case "MissedPenalty":
                image.setPixelMap(ResourceTable.Media_icon_ball_miss);
                break;
            case "Assist":
                image.setPixelMap(ResourceTable.Media_shoe);
                break;
            case "YellowCard":
                image.setPixelMap(ResourceTable.Media_match_facts_yellow_card);
                break;
            case "RedCard":
                image.setPixelMap(ResourceTable.Media_match_facts_red_card);
                break;
            case "RedCardTwoYellow":
                image.setPixelMap(ResourceTable.Media_match_facts_red_card_v2);
                break;
            default:
                Log.w(TAG, "Unsupported event type %{public}s. Hiding image.", eventType);
                image.setVisibility(Component.INVISIBLE);
                break;
        }
    }

}
