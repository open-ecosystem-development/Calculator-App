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

import android.content.Context;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AFInAppEventType;
import com.appsflyer.AppsFlyerLib;

import java.util.HashMap;
import java.util.Map;

public class AttributionAppsFlyer {
    private final Context context;

    private AttributionAppsFlyer(Context ctx){
        context = ctx;
    }

    public static AttributionAppsFlyer createAttributionAppsFlyer(Context ctx) {
        return new AttributionAppsFlyer(ctx);
    }

    public void TrackEvent(){

        /* Track Events in real time */
        Map<String, Object> eventValue = new HashMap<>();
        eventValue.put(AFInAppEventParameterName.REVENUE, 200);
        eventValue.put(AFInAppEventParameterName.CONTENT_TYPE, "category_a");
        eventValue.put(AFInAppEventParameterName.CONTENT_ID, "1234567");
        eventValue.put(AFInAppEventParameterName.CURRENCY, "USD");

        logInAppEvent();

    }
    public void logInAppEvent(){

        Add2Wishlist();

    }
    public void Add2Wishlist(){
        Map<String, Object> eventValues = new HashMap<>();
        eventValues.put(AFInAppEventParameterName.PRICE, 1234.56);
        eventValues.put(AFInAppEventParameterName.CONTENT_ID,"1234567");

        AppsFlyerLib.getInstance().logEvent(context,
                AFInAppEventType.ADD_TO_WISH_LIST , eventValues);
    }

}
