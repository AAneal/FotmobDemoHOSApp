package com.fotmob.android.harmony;

import com.fotmob.android.harmony.log.Log;
import com.fotmob.android.harmony.munch.Munch;
import com.fotmob.android.harmony.abilityslice.MainAbilitySlice;
import ohos.aafwk.ability.Ability;
import ohos.aafwk.content.Intent;
import ohos.net.NetHandle;
import ohos.net.NetManager;

import java.net.InetAddress;

public class MainAbility extends Ability {

    private final static String TAG = MainAbility.class.getSimpleName();

    private boolean hasInternetConnection = true;

    @Override
    public void onStart(Intent intent) {
        Log.d(TAG, "onStart()", null);
        super.onStart(intent);
        super.setMainRoute(MainAbilitySlice.class.getName());
        setSwipeToDismiss(true);
        Munch.initHttpResponseCache(getApplicationContext());
        checkForInternetConnection();
    }

    private void checkForInternetConnection() {
        new Thread(() -> {
            try {
                NetManager netManager = NetManager.getInstance(getContext());
                if (netManager != null) {
                    NetHandle defaultNet = netManager.getDefaultNet();
                    if (defaultNet != null) {
                        InetAddress inetAddress = defaultNet.getByName("images.fotmob.com");
                        if (inetAddress != null) {
                            Log.d("Looked up address %{public}s. Assuming there is Internet.", inetAddress);
                            hasInternetConnection = true;
                        } else {
                            Log.i("Unable to look up Internet address. Assuming there is no Internet.");
                            hasInternetConnection = false;
                        }
                    } else {
                        Log.i("Net manager is null. Assuming there is no Internet.");
                        hasInternetConnection = false;
                    }
                } else {
                    Log.i("Net manager is null. Assuming there is no Internet.");
                    hasInternetConnection = false;
                }
            } catch (Exception e) {
                Log.e(e, "Got exception while trying to check for Internet connection. Assuming there is no Internet.");
                hasInternetConnection = false;
            }
        }).start();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "onStop()", null);
        super.onStop();
    }

    public boolean hasInternetConnection() {
        return hasInternetConnection;
    }
}
