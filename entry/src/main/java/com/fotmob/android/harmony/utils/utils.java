package com.fotmob.android.harmony.utils;

import com.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import ohos.agp.components.Image;
import ohos.media.image.PixelMap;

public class utils {

    public static String getNiceErrorMessage(int errorCode) {
        switch (errorCode) {
            case 203:
            case 150002:
                return "Error connecting to the phone.\nPlease ensure that the phone is connected to the watch.";
            case 206:
                return "Is FotMob running on the phone?\nPlease relaunch the app and try again.";
        }
        return "Failed to connect to phone app. Error code: " + errorCode;
    }

    public static void generateCommonQrCode(String content, Image image) {

        PixelMap pixelMap = QRCodeEncoder.syncEncodeQRCode(content, 250, 250);
        image.setPixelMap(pixelMap);
    }
}
