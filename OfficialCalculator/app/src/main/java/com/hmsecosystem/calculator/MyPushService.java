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

import android.util.Log;

import com.huawei.hms.push.HmsMessageService;
import com.huawei.hms.push.RemoteMessage;

public class MyPushService extends HmsMessageService {
    private static final String TAG = "PushDemoLog";
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.i(TAG, "receive token:" + s);

    }


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getData().length() > 0) {
            Log.i(TAG, "Message data payload: " + remoteMessage.getData());
        }
        if (remoteMessage.getNotification() != null) {
            Log.i(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    @Override
    public void onMessageSent(String s) {
    }

    @Override
    public void onSendError(String s, Exception e) {
    }
}
