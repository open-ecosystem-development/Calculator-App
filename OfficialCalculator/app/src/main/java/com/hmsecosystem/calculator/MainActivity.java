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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.gms.common.GoogleApiAvailability;
import com.hmsecosystem.calculator.converter.UnitConverter;
import com.hmsecosystem.calculator.iap.CipherUtil;
import com.hmsecosystem.calculator.iap.IapApiCallback;
import com.hmsecosystem.calculator.iap.IapRequestHelper;
import com.huawei.agconnect.config.AGConnectServicesConfig;
import com.huawei.agconnect.remoteconfig.ConfigValues;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hms.aaid.HmsInstanceId;
import com.huawei.hms.ads.AdListener;
import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.HwAds;
import com.huawei.hms.ads.InterstitialAd;
import com.huawei.hms.ads.banner.BannerView;
import com.huawei.hms.ads.reward.RewardAd;
import com.huawei.hms.ads.reward.RewardAdLoadListener;
import com.huawei.hms.ads.reward.RewardAdStatusListener;
import com.huawei.hms.analytics.HiAnalytics;
import com.huawei.hms.analytics.HiAnalyticsInstance;
import com.huawei.hms.analytics.HiAnalyticsTools;
import com.huawei.hms.iap.Iap;
import com.huawei.hms.iap.IapClient;
import com.huawei.hms.iap.entity.InAppPurchaseData;
import com.huawei.hms.iap.entity.OwnedPurchasesResult;

import org.json.JSONException;
import java.util.List;

import com.huawei.agconnect.remoteconfig.AGConnectConfig;


public class MainActivity extends AppCompatActivity {

    private boolean adsFlag = false;
    private static final String REMOVE_ADS_PRODUCTID = "RemoveAds1";
    private boolean isRemoveAdsPurchased = false;

    private BannerView defaultBannerView;
    private static final int REFRESH_TIME = 60;

    private InterstitialAd interstitialAdHms;

    private RewardAd rewardedAdHms;

    private static final String TAG = "MainActivity";
    private String hmsPushToken = "";

    private IapClient hmsIapClient;
    public HiAnalyticsInstance hmsAnalyticsInstance;

    //Google AdMob
    private AdView gmsBannerAdView;
    private boolean gmsMode = false;
    public boolean gmsBannerAdIsLoading;
    private RewardedAd rewardedAdGms;
    private com.google.android.gms.ads.interstitial.InterstitialAd interstitialAdGms;

    //Remote config
    private static final String AdsHms_unitID_Interstitial = "Ads_unitID_Interstitial";
    private static final String AdsHms_unitID_Reward = "Ads_unitID_Reward";
    private static final String AdsHms_unitID_Banner = "Ads_unitID_Banner";
    private AGConnectConfig config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!isGmsAvailable()){
            initHms();
        }else{
            loadAdsGms();
        }
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.d(TAG, "onResume");
        if(!gmsMode){
            hmsIapQueryPurchases(null);
        }
    }

    @Override
    protected void onRestart(){
        super.onRestart();
        Log.d(TAG, "onRestart");
        if(!gmsMode){
            hmsIapQueryPurchases(null);
        }
    }

    public void onClick(View view)
    {
        if(view.getId()==R.id.calculatorButton) {
            startActivity(new Intent(this,CalculatorActivity.class));
        }
        else if(view.getId()==R.id.converterButton) {
            startActivity(new Intent(this, UnitConverter.class));
        }
    }

    private void initHms(){
        // Enable Analytics Kit logging.
        HiAnalyticsTools.enableLog();

        // Generate an Analytics Kit instance.
        hmsAnalyticsInstance = HiAnalytics.getInstance(this);
        hmsIapClient = Iap.getIapClient(this);

        loadAdsHms();
        hmsIapQueryPurchases(null);
        hmsPushGetToken();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        menu.clear();
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        if(gmsMode){
            menu.removeItem(R.id.remove_ads);
        }else if(!adsFlag){
            menu.removeItem(R.id.reward_ad);
            menu.removeItem(R.id.interstitial_ad);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent;
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.reward_ad:
                if(gmsMode){
                    showRewardAdGms();
                }else{
                    showRewardAdHms();
                }
                return true;
            case R.id.interstitial_ad:
                if(gmsMode){
                    showInterstitialGms();
                }else{
                    loadInterstitialAdHms();
                }
                return true;
            case R.id.remove_ads:
                intent = new Intent(MainActivity.this, NonConsumptionActivity.class);
                startActivity(intent);
                return true;
            case R.id.about:
                intent = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(intent);
                return true;
            case R.id.privacy_policy:
                intent = new Intent(MainActivity.this, PrivacyPolicyActivity.class);
                startActivity(intent);
                return true;
            case R.id.terms_of_service:
                intent = new Intent(MainActivity.this, TermsOfServiceActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void loadDefaultBannerAdHms() {
        defaultBannerView = findViewById(R.id.hw_banner_view);
        defaultBannerView.setBannerRefresh(REFRESH_TIME);
        config = AGConnectConfig.getInstance();

        config.fetch(0).addOnSuccessListener(new OnSuccessListener<ConfigValues>() {
            @Override
            public void onSuccess(ConfigValues configValues) {
                // Apply Network Config to Current Config
                config.apply(configValues);
                defaultBannerView.setAdId(config.getValueAsString(AdsHms_unitID_Banner));
                AdParam adParam = new AdParam.Builder().build();
                defaultBannerView.loadAd(adParam);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, "Remote_config_fetch_failed");
            }
        });

    }

    /**
     * get token
     */
    private void hmsPushGetToken() {
        Log.i(TAG, "get token: begin");
        new Thread() {
            @Override
            public void run() {
                try {
                    // read from agconnect-services.json
                    String appId = AGConnectServicesConfig.fromContext(MainActivity.this).getString("client/app_id");
                    hmsPushToken = HmsInstanceId.getInstance(MainActivity.this).getToken(appId, "HCM");
                    if(!TextUtils.isEmpty(hmsPushToken)) {
                        Log.i(TAG, "get token:" + hmsPushToken);
                    }
                } catch (Exception e) {
                    Log.i(TAG,"getToken failed, " + e);

                }
            }
        }.start();
    }

    private AdListener adListenerInterstitialHms = new AdListener() {
        @Override
        public void onAdLoaded() {
            Log.d(TAG, "onAdLoaded");
            super.onAdLoaded();
            showInterstitialHms();
        }

        @Override
        public void onAdClicked() {
            Log.d(TAG, "onAdClicked");
            super.onAdClicked();
        }

        @Override
        public void onAdOpened() {
            Log.d(TAG, "onAdOpened");
            super.onAdOpened();
        }
    };

    private void loadInterstitialAdHms() {
        interstitialAdHms = new InterstitialAd(this);

        config = AGConnectConfig.getInstance();

        config.fetch(0).addOnSuccessListener(new OnSuccessListener<ConfigValues>() {
            @Override
            public void onSuccess(ConfigValues configValues) {
                // Apply Network Config to Current Config
                config.apply(configValues);
                interstitialAdHms.setAdId(config.getValueAsString(AdsHms_unitID_Interstitial));
                interstitialAdHms.setAdListener(adListenerInterstitialHms);

                AdParam adParam = new AdParam.Builder().build();
                interstitialAdHms.loadAd(adParam);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, "Remote_config_fetch_failed");
            }
        });

    }

    private void showInterstitialHms() {
        // Display an interstitial ad.
        if (interstitialAdHms != null && interstitialAdHms.isLoaded()) {
            interstitialAdHms.show(this);
        }
    }

    /**
     * Load a rewarded ad.
     */
    private void loadRewardAdHms() {

        config = AGConnectConfig.getInstance();

        config.fetch(0).addOnSuccessListener(new OnSuccessListener<ConfigValues>() {
            @Override
            public void onSuccess(ConfigValues configValues) {
                // Apply Network Config to Current Config
                config.apply(configValues);

                if (rewardedAdHms == null) {
                    rewardedAdHms = new RewardAd(getBaseContext(), config.getValueAsString(AdsHms_unitID_Reward));
                }

                RewardAdLoadListener rewardAdLoadListener = new RewardAdLoadListener();

                rewardedAdHms.loadAd(new AdParam.Builder().build(), rewardAdLoadListener);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Log.d(TAG, "Remote_config_fetch_failed");
            }
        });
    }

    /**
     * Display a rewarded ad.
     */
    private void showRewardAdHms() {
        if (rewardedAdHms.isLoaded()) {
            rewardedAdHms.show(this, new RewardAdStatusListener() {
                @Override
                public void onRewardAdClosed() {
                    loadRewardAdHms();
                }

            });
        }
    }

    private void hmsIapQueryPurchases(final String continuationToken) {
        // Query users' purchased non-consumable products.
        IapRequestHelper.obtainOwnedPurchases(hmsIapClient, IapClient.PriceType.IN_APP_NONCONSUMABLE, continuationToken, new IapApiCallback<OwnedPurchasesResult>() {
            @Override
            public void onSuccess(OwnedPurchasesResult result) {
                Log.i(TAG, "@@@: obtainOwnedPurchases, success, result: " + result);
                checkRemoveAdsPurchaseState(result);
                if (result != null && !TextUtils.isEmpty(result.getContinuationToken())) {
                    hmsIapQueryPurchases(result.getContinuationToken());
                }
            }

            @Override
            public void onFail(Exception e) {
                Log.e(TAG, "@@@: obtainOwnedPurchases, type=" + IapClient.PriceType.IN_APP_NONCONSUMABLE + ", " + e.getMessage());
            }
        });
    }

    private void checkRemoveAdsPurchaseState(OwnedPurchasesResult result) {
        if (result == null || result.getInAppPurchaseDataList() == null) {
            Log.i(TAG, "@@@: result is null");
            return;
        }
        List<String> inAppPurchaseDataList = result.getInAppPurchaseDataList();
        List<String> inAppSignature= result.getInAppSignature();
        for (int i = 0; i < inAppPurchaseDataList.size(); i++) {
            // Check whether the signature of the purchase data is valid.
            if (CipherUtil.doCheck(inAppPurchaseDataList.get(i), inAppSignature.get(i), CipherUtil.getPublicKey())) {
                try {
                    InAppPurchaseData inAppPurchaseDataBean = new InAppPurchaseData(inAppPurchaseDataList.get(i));
                    if (inAppPurchaseDataBean.getPurchaseState() == InAppPurchaseData.PurchaseState.PURCHASED) {
                        // Check whether the purchased product is Remove Ads.
                        if (REMOVE_ADS_PRODUCTID.equals(inAppPurchaseDataBean.getProductId())) {
                            isRemoveAdsPurchased = true;
                        }
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "@@@: delivery:" + e.getMessage());
                }
            } else {
                Log.e(TAG, "@@@: delivery:" +  ", verify signature error");
            }
        }
        if (!isRemoveAdsPurchased) {
            showAdsHms();
        }
    }

    private void showAdsHms(){
        adsFlag=true;
        loadDefaultBannerAdHms();
    }

    private void loadAdsHms(){
        HwAds.init(this);
        loadRewardAdHms();
    }

    public boolean isGmsAvailable() {
        boolean isAvailable;
        int result = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(this);
        isAvailable = (com.google.android.gms.common.ConnectionResult.SUCCESS == result);
        Log.i(TAG, "isGmsAvailable: " + isAvailable);
        return isAvailable;
    }

    private void loadAdsGms(){
        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {}
        });

        MobileAds.setRequestConfiguration(new RequestConfiguration.Builder().build());
        loadBannerAdGms();
        loadRewardedAdGms();
        loadInterstitialAdGms();
        gmsMode = true;
    }

    private void loadBannerAdGms(){
        gmsBannerAdView = findViewById(R.id.ad_view_banner);
        // Create an ad request.
        AdRequest adRequest = new AdRequest.Builder().build();
        // Start loading the ad in the background.
        gmsBannerAdView.loadAd(adRequest);
    }

    private void loadRewardedAdGms() {
        if (rewardedAdGms == null) {
            Log.d(TAG, "loading GMS reward ad");
            gmsBannerAdIsLoading = true;
            AdRequest adRequest = new AdRequest.Builder().build();
            RewardedAd.load(
                    this,
                    getString(R.string.reward_ad_id_gms),
                    adRequest,
                    new RewardedAdLoadCallback() {
                        @Override
                        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                            // Handle the error.
                            Log.d(TAG, loadAdError.getMessage());
                            MainActivity.this.gmsBannerAdIsLoading = false;
                        }

                        @Override
                        public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                            MainActivity.this.rewardedAdGms = rewardedAd;
                            MainActivity.this.gmsBannerAdIsLoading = false;
                            Log.d(TAG, "loaded GMS reward ad");
                        }
                    });
        }
    }

    private void showRewardAdGms() {

        if (rewardedAdGms == null) {
            Log.d("TAG", "The rewarded ad wasn't ready yet.");
            return;
        }

        rewardedAdGms.setFullScreenContentCallback(
                new FullScreenContentCallback() {
                    @Override
                    public void onAdShowedFullScreenContent() {
                        // Called when ad is shown.
                        Log.d(TAG, "onAdShowedFullScreenContent");
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        // Called when ad fails to show.
                        Log.d(TAG, "onAdFailedToShowFullScreenContent");
                        // Don't forget to set the ad reference to null so you
                        // don't show the ad a second time.
                        rewardedAdHms = null;
                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Called when ad is dismissed.
                        // Don't forget to set the ad reference to null so you
                        // don't show the ad a second time.
                        rewardedAdHms = null;
                        Log.d(TAG, "onAdDismissedFullScreenContent");
                    }
                });

        Activity activityContext = MainActivity.this;
        rewardedAdGms.show(
                activityContext,
                new OnUserEarnedRewardListener() {
                    @Override
                    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                        // Handle the reward.
                        Log.d("TAG", "The user earned the reward.");
                    }
                });
    }

    public void loadInterstitialAdGms() {
        AdRequest adRequest = new AdRequest.Builder().build();
        com.google.android.gms.ads.interstitial.InterstitialAd.load(
                this,
                getString(R.string.interstitial_ad_id_gms),
                adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull com.google.android.gms.ads.interstitial.InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        MainActivity.this.interstitialAdGms = interstitialAd;
                        Log.i(TAG, "onAdLoaded");
                        interstitialAd.setFullScreenContentCallback(
                                new FullScreenContentCallback() {
                                    @Override
                                    public void onAdDismissedFullScreenContent() {
                                        // Called when fullscreen content is dismissed.
                                        MainActivity.this.interstitialAdGms = null;
                                        Log.d("TAG", "The ad was dismissed.");
                                    }

                                    @Override
                                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                                        // Called when fullscreen content failed to show.
                                        MainActivity.this.interstitialAdGms = null;
                                        Log.d("TAG", "The ad failed to show.");
                                    }

                                    @Override
                                    public void onAdShowedFullScreenContent() {
                                        // Called when fullscreen content is shown.
                                        Log.d("TAG", "The ad was shown.");
                                    }
                                });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i(TAG, loadAdError.getMessage());
                        interstitialAdGms = null;
                    }
                });
    }

    private void showInterstitialGms() {
        if (interstitialAdGms != null) {
            interstitialAdGms.show(this);
        }
    }

}
