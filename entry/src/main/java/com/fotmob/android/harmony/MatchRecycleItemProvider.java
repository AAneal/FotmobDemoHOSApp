package com.fotmob.android.harmony;

import com.fotmob.android.WearableMatch;
import com.fotmob.android.harmony.ResourceTable;
import com.fotmob.android.harmony.log.Log;
import com.fotmob.android.harmony.munch.Munch;
import ohos.aafwk.ability.AbilitySlice;
import ohos.agp.components.*;

import java.util.List;

public class MatchRecycleItemProvider extends RecycleItemProvider {
    private List<WearableMatch> list;
    private AbilitySlice slice;

    public MatchRecycleItemProvider(List<WearableMatch> list, AbilitySlice slice, int numOfDummyElements) {
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
                convertComponent = LayoutScatter.getInstance(slice).parse(ResourceTable.Layout_list_item_match, null, false);
            }
            WearableMatch wearableMatch = list.get(position);
            if (wearableMatch != null) {
                convertComponent.findComponentById(ResourceTable.Id_layout_root).setVisibility(Component.VISIBLE);
                Text text = (Text) convertComponent.findComponentById(ResourceTable.Id_text_homeTeam);
                text.startAutoScrolling();
                text.setText(wearableMatch.teamNameHome);
                text = (Text) convertComponent.findComponentById(ResourceTable.Id_text_awayTeam);
                text.startAutoScrolling();
                text.setText(wearableMatch.teamNameAway);
                if (!"NotStarted".equals(wearableMatch.matchStatus)) {
                    text = (Text) convertComponent.findComponentById(ResourceTable.Id_text_scoreOrTime);
                    text.setText((wearableMatch.scoreHome) + " - " + wearableMatch.scoreAway);
                    text = (Text) convertComponent.findComponentById(ResourceTable.Id_text_elapsedTime);
                    text.setText(wearableMatch.elapsedTime);
                } else { // Started
                    text = (Text) convertComponent.findComponentById(ResourceTable.Id_text_scoreOrTime);
                    text.setText(wearableMatch.matchTime);
                    text = (Text) convertComponent.findComponentById(ResourceTable.Id_text_elapsedTime);
                    text.setText(null);
                }
            } else { // Dummy element
                convertComponent.findComponentById(ResourceTable.Id_layout_root).setVisibility(Component.INVISIBLE);
            }
        } catch (Exception e) {
            Log.e(e, "Got exception while trying to bind list item. Ignoring problem.");
        }
        return convertComponent;
    }

}
