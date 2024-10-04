package com.example.versionchecker;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.jsoup.Jsoup;

import java.io.IOException;

public class VersionChecker extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("checkVersion")) {
            this.checkVersion(callbackContext);
            return true;
        }
        return false;
    }

    private void checkVersion(CallbackContext callbackContext) {
        // Get installed app version
        try {
            PackageManager pm = this.cordova.getActivity().getPackageManager();
            PackageInfo pInfo = pm.getPackageInfo(this.cordova.getActivity().getPackageName(), 0);
            String currentVersion = pInfo.versionName;

            // Check latest version from Play Store
            new Thread(() -> {
                try {
                    String latestVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + this.cordova.getActivity().getPackageName() + "&hl=en&gl=US")
                            .timeout(30000)
                            .get()
                            .select("div.hAyfc:nth-child(4) > span:nth-child(2) > div:nth-child(1) > span:nth-child(1)")
                            .first()
                            .ownText();
                    Log.d("VersionChecker", "Installed version: " + currentVersion + ", Play Store version: " + latestVersion);

                    if (!latestVersion.equals(currentVersion)) {
                        callbackContext.success("Update available: " + latestVersion);
                    } else {
                        callbackContext.success("App is up to date");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    callbackContext.error("Failed to fetch the latest version");
                }
            }).start();

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            callbackContext.error("Failed to get the installed version");
        }
    }
}
