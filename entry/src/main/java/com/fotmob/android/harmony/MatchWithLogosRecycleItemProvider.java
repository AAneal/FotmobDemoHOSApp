package com.fotmob.android.harmony;

import com.fotmob.android.WearableMatch;
import com.fotmob.android.harmony.ResourceTable;
import com.fotmob.android.harmony.log.Log;
import com.fotmob.android.harmony.utils.MunchHelper;
import ohos.aafwk.ability.AbilitySlice;
import ohos.agp.components.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class MatchWithLogosRecycleItemProvider extends BaseItemProvider {
    private List<WearableMatch> list;
    private AbilitySlice slice;

    public MatchWithLogosRecycleItemProvider(List<WearableMatch> list, AbilitySlice slice) {

        list.add(0, null);
        list.add(null);
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
                convertComponent = LayoutScatter.getInstance(slice).parse(ResourceTable.Layout_list_item_match_logos, null, false);
            }
            WearableMatch wearableMatch = list.get(position);
            if (wearableMatch != null) {
                convertComponent.findComponentById(ResourceTable.Id_layout_root).setVisibility(Component.VISIBLE);
                Text text;
                Image image = (Image) convertComponent.findComponentById(ResourceTable.Id_image_homeTeam);
                MunchHelper.loadTeamLogo(convertComponent.getContext(), image, wearableMatch.teamIdHome, ResourceTable.Media_ic_team_placeholder_24_px);
                image = (Image) convertComponent.findComponentById(ResourceTable.Id_image_awayTeam);
                MunchHelper.loadTeamLogo(convertComponent.getContext(), image, wearableMatch.teamIdAway, ResourceTable.Media_ic_team_placeholder_24_px);
                if (!"NotStarted".equals(wearableMatch.matchStatus)) {
                    text = (Text) convertComponent.findComponentById(ResourceTable.Id_text_scoreOrTime);
                    text.setText((wearableMatch.scoreHome) + " - " + wearableMatch.scoreAway);
                    text = (Text) convertComponent.findComponentById(ResourceTable.Id_text_elapsedTime);
                    DateFormat dateformat = new SimpleDateFormat("HH:mm");
                    text.setText(dateformat.format(Long.parseLong(wearableMatch.matchTime)));
                } else { // Not Started
                    text = (Text) convertComponent.findComponentById(ResourceTable.Id_text_scoreOrTime);
                    DateFormat dateformat = new SimpleDateFormat("HH:mm");
                    text.setText(dateformat.format(Long.parseLong(wearableMatch.matchTime)));
                    text = (Text) convertComponent.findComponentById(ResourceTable.Id_text_elapsedTime);
                    text.setText(null);
                    text.setVisibility(Component.HIDE);
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
