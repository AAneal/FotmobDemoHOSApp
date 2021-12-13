package com.fotmob.android.harmony.utils;

import com.fotmob.android.harmony.log.Log;
import com.fotmob.android.harmony.munch.Munch;
import ohos.agp.components.Component;
import ohos.agp.components.Image;
import ohos.app.Context;

public class MunchHelper {

    private final static String TAG = MunchHelper.class.getSimpleName();
    private static final int IMAGE_VERSION = 20;

    private static void load(Context context, Image image, String url, Integer placeHolderResId) {
        try {
            //Log.d(TAG, "load(%{public}s, %{public}s, %{public}s)", context, image, url);
            if (context == null || image == null) {
                return;
            }
            if (url == null || url.length() == 0) {
                if (placeHolderResId != null) {
                    image.setPixelMap(placeHolderResId);
                    image.setVisibility(Component.VISIBLE);
                } else {
                    image.setVisibility(Component.INVISIBLE);
                }
                return;
            }
            Munch.load(context, image, url, placeHolderResId);
        } catch (Exception | OutOfMemoryError e) {
            Log.e(e, "Got exception while trying to load image with url [%{public}s]. Ignoring problem.", url);
        }
    }

    public static void loadTeamLogo(Context context, Image image, Integer teamId, Integer placeHolderResId) {
        if (teamId == null || teamId <= 0) {
            if (image != null && placeHolderResId != null) {
                image.setPixelMap(placeHolderResId);
            }
            return;
        }
        load(context, image, "https://images.fotmob.com/image_resources/logo/teamlogo/" + teamId + "_small.png?id=" + IMAGE_VERSION, placeHolderResId);
    }

}
