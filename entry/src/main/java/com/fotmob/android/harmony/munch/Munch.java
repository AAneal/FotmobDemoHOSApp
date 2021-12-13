package com.fotmob.android.harmony.munch;

import ohos.agp.components.Component;
import ohos.agp.components.Image;
import ohos.app.Context;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;
import ohos.media.image.ImageSource;
import ohos.media.image.PixelMap;
import ohos.media.image.common.Size;
import ohos.net.HttpResponseCache;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

public class Munch {

    private static final HiLogLabel HI_LOG_LABEL = new HiLogLabel(HiLog.LOG_APP, 0x1337, "Munch");
    private static final int MAX_CACHE_SIZE = 50;
    private static final int CACHE_EVICTION_SIZE = 10;
    private static Map<String, PixelMap> memoryCacheMap = new LinkedHashMap<>();

    public static void load(Context context, Image image, String url, Integer placeHolderResId) {
        if (context == null || image == null || url == null) {
            return;
        }
        PixelMap pixelMap = memoryCacheMap.get(url);
        if (pixelMap == null) {
            HiLog.info(HI_LOG_LABEL, "Memory cache MISS for %{public}s.", url);
            if (placeHolderResId != null) {
                image.setPixelMap(placeHolderResId);
                image.setVisibility(Component.VISIBLE);
            } else {
                image.setVisibility(Component.INVISIBLE);
            }
            new Thread(() -> {
                try {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(url).openConnection();
                    httpURLConnection.setUseCaches(true);
                    httpURLConnection.addRequestProperty("Cache-Control", "max-stale=" + (60 * 60 * 24 * 28));
                    httpURLConnection.setRequestMethod("GET");
                    httpURLConnection.setConnectTimeout(10000);
                    httpURLConnection.setReadTimeout(10000);
                    int statusCode = httpURLConnection.getResponseCode();
                    HiLog.info(HI_LOG_LABEL, "Got status code %{public}d.", statusCode);
                    if (statusCode == 200) {
                        InputStream inputStream = httpURLConnection.getInputStream();
                        ImageSource imageSource = ImageSource.create(inputStream, null);
                        ImageSource.DecodingOptions decodingOptions = new ImageSource.DecodingOptions();
                        decodingOptions.desiredSize = new Size(image.getWidth(), image.getHeight());
                        final PixelMap downloadedPixelMap = imageSource.createPixelmap(decodingOptions);
                        memoryCacheMap.put(url, downloadedPixelMap);
                        context.getUITaskDispatcher().asyncDispatch(() -> {
                            try {
                                image.setPixelMap(downloadedPixelMap);
                                image.setVisibility(Component.VISIBLE);
                                trimCache();
                            } catch (Exception e) {
                                logThrowable(e, "Got exception while trying to set pixel data from %{public}s.", url);
                            }
                        });
                    }
                } catch (Exception e) {
                    logThrowable(e, "Got exception while trying to load image from %{public}s.", url);
                }
            }).start();
        } else { // Got the bitmap cached
            HiLog.info(HI_LOG_LABEL, "Memory cache HIT for %{public}s.", url);
            image.setPixelMap(pixelMap);
            image.setVisibility(Component.VISIBLE);
        }
    }

    public static void initHttpResponseCache(Context context) {
        try {
            if (context == null) {
                return;
            }
            HttpResponseCache httpResponseCache = HttpResponseCache.getInstalled();
            if (httpResponseCache == null) {
                HiLog.info(HI_LOG_LABEL, "Installing cache.");
                HttpResponseCache.install(new File(context.getExternalCacheDir(), "http"), 10 * 1024 * 1024);
            }
        } catch (IOException e) {
            logThrowable(e, "Got exception while trying to set up disk cache. Ignoring problem and returning null.");
        }
    }

    private static void trimCache() {
        if (memoryCacheMap != null && memoryCacheMap.size() > MAX_CACHE_SIZE) {
            int newSize = MAX_CACHE_SIZE - CACHE_EVICTION_SIZE;
            for (String key : memoryCacheMap.keySet()) {
                memoryCacheMap.remove(key);
                if (memoryCacheMap.size() <= newSize) {
                    break;
                }
            }
        }
    }

    private static void logThrowable(Throwable t, String format, Object... args) {
        HiLog.error(HI_LOG_LABEL, format, args);
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        t.printStackTrace(printWriter);
        HiLog.error(HI_LOG_LABEL, stringWriter.toString());
    }

}
