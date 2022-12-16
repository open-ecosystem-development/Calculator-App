/*
      Copyright 2021. Futurewei Technologies Inc. All rights reserved.
      Licensed under the Apache License, Version 2.0 (the "License");
      you may not use this file except in compliance with the License.
      You may obtain a copy of the License at
        http:  www.apache.org/licenses/LICENSE-2.0
      Unless required by applicable law or agreed to in writing, software
      distributed under the License is distributed on an "AS IS" BASIS,
      WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
      See the License for the specific language governing permissions and
      limitations under the License.
*/

package com.hmsecosystem.calculator;

import android.content.res.Resources;
import android.util.Log;

import androidx.multidex.MultiDexApplication;

import com.appsflyer.AppsFlyerConversionListener;
import com.appsflyer.AppsFlyerLib;

import com.onesignal.OneSignal;
import java.util.Map;

public class App extends MultiDexApplication {
    private static Resources resources;
    //public static final String ONESIGNAL_APP_ID = "fd60d8f1-4cda-4d2f-b7f7-fba1256c1794";
    public static final String ONESIGNAL_APP_ID = "a1f5c5ff-239b-4b03-9fbc-ee52a342aef9";

    static final String TAG = "App";

    @Override
    public void onCreate() {
        super.onCreate();
        resources = getResources();
        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.setAppId(ONESIGNAL_APP_ID);
        OneSignal.initWithContext(this);

        // promptForPushNotifications will show the native Android notification permission prompt.
        //OneSignal.promptForPushNotifications();

        final String AF_DEV_KEY = resources.getString(R.string.AF_DEV_KEY);

        AppsFlyerLib.getInstance().setOutOfStore("AG_Connect");

        AppsFlyerConversionListener conversionListener = new AppsFlyerConversionListener(){
            @Override
            public void onConversionDataSuccess(Map<String, Object> conversionData) {
                for (String attrName : conversionData.keySet()) {
                    Log.d(TAG, "attribute: " + attrName + " = " + conversionData.get(attrName));
                }
            }

            @Override
            public void onConversionDataFail(String errorMessage) {
                Log.d(TAG, "error getting conversion data: " + errorMessage);
            }

            /* Called only when a Deep Link is opened */
            @Override
            public void onAppOpenAttribution(Map<String, String> conversionData) {
                for (String attrName : conversionData.keySet()) {
                    Log.d(TAG, "attribute: " + attrName + " = " + conversionData.get(attrName));
                }
            }

            @Override
            public void onAttributionFailure(String errorMessage) {
                Log.d(TAG, "error onAttributionFailure : " + errorMessage);
            }
        };

        /* This API enables AppsFlyer to detect installations, sessions, and updates. */
        AppsFlyerLib.getInstance().init(AF_DEV_KEY , conversionListener , getApplicationContext());
        AppsFlyerLib.getInstance().start(this);

    }

    public static Resources getAppResources() {
        return resources;
    }

}