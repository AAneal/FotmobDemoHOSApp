package com.fotmob.android.harmony.log;

import com.huawei.watch.kit.hiwear.p2p.HiWearMessage;
import ohos.hiviewdfx.HiLog;
import ohos.hiviewdfx.HiLogLabel;

import java.io.PrintWriter;
import java.io.StringWriter;

public class Log {

    private static HiLogLabel hiLogLabel = new HiLogLabel(HiLog.LOG_APP, 0x1337, "FotMob");

    public static void d(String label, String format, Object... args) {
        i(label + ": " + format, args);
    }

    public static void d(String format, Object... args) {
        HiLog.info(hiLogLabel, format, args); // Seems like we need to use info to get the output HiLog
    }

    public static void d(String prefix, HiWearMessage hiWearMessage) {
        d(prefix + ":hiWearMessage:type=%{public}d,desc=[%{public}s],data=[%{public}s]", hiWearMessage.getType(), hiWearMessage.getDescription(), new String(hiWearMessage.getData()));
    }

    public static void i(String label, String format, Object... args) {
        i(label + ": " + format, args);
    }

    public static void i(String format, Object... args) {
        HiLog.info(hiLogLabel, format, args);
    }

    public static void w(String label, String format, Object... args) {
        w(label + ": " + format, args);
    }

    public static void w(String format, Object... args) {
        HiLog.warn(hiLogLabel, format, args);
    }

    public static void e(String label, String format, Object... args) {
        e(label + ": " + format, args);
    }

    public static void e(String format, Object... args) {
        HiLog.error(hiLogLabel, format, args);
    }

    public static void e(Throwable t, String format, Object... args) {
        HiLog.error(hiLogLabel, format, args);
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        t.printStackTrace(printWriter);
        HiLog.error(hiLogLabel, stringWriter.toString());
    }

}
