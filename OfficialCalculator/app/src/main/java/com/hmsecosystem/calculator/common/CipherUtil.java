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

package com.hmsecosystem.calculator.common;

import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

public class CipherUtil {
    private static final String TAG = "CipherUtil";

    // The SHA256WithRSA algorithm.
    private static final String SIGN_ALGORITHMS = "SHA256WithRSA";

    // The Iap public key of this App.
    private static final String PUBLIC_KEY = "MIIBojANBgkqhkiG9w0BAQEFAAOCAY8AMIIBigKCAYEAiE2h1f2uAFJkIxAu8aOmWjv5UC7RJfIvl1Ra7UV3UZUhXysD/a9QOtNxfp4KoI8+KAs94rAKqkObvaE8uGLl4AiLdT3SGWBVpas9WhuSWotP2Rwcxa35uzvVWkebO07sbEwhgomCPBGq3JMR1FQPm6ecZIB+IF1idbZQKknboSJ2Yjum5Yzuk1bpbNIHsxw7nAX24+eN3equYI/qVbPtZHyf2R7deKrhh11HPCvMa1j0d2Ad7ViHBdbzTlFZjiVW9F7Ry+4zmaSinfZY3CfKWVcdiAL1UbbppCdeEicbwcqrwxhFWRoFwrG6udOQZ223tVm71c+PFL+Tje6rfMbmUCy7CGizAXRMVQRaQbNO8ALW+1tvIFTc8Yy8FUn52HdmLVtM/j+iKTtLb97iFciRq6KThjkr7JCBwQFg//XzQv2nyys3BuyhK8jaeH9dwYjBP/3ecUQ4wdPU87pwLwV+cb1liPTjF9u3rpR64M7EmRPbbdZfy/Wk09iaBRUPdBE7AgMBAAE=";

    /**
     * The method to check the signature for the data returned from the interface.
     *
     * @param content Unsigned data.
     * @param sign The signature for content.
     * @param publicKey The public of the application.
     * @return boolean
     */
    public static boolean doCheck(String content, String sign, String publicKey) {
        if (TextUtils.isEmpty(publicKey)) {
            Log.e(TAG, "publicKey is null");
            return false;
        }

        if (TextUtils.isEmpty(content) || TextUtils.isEmpty(sign)) {
            Log.e(TAG, "data is error");
            return false;
        }

        try {
            Log.e(TAG, "@@@ content: " + content);
            Log.e(TAG, "@@@ sign: " + sign);
            Log.e(TAG, "@@@ publickey: " + publicKey);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            byte[] encodedKey = Base64.decode(publicKey, Base64.DEFAULT);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));

            java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
            Log.e(TAG, "@@@ pubKey: " + pubKey);
            signature.initVerify(pubKey);
            signature.update(content.getBytes("utf-8"));

            boolean bverify = signature.verify(Base64.decode(sign, Base64.DEFAULT));
            Log.e(TAG, "@@@ bverify: " + bverify);
            return bverify;

        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, "doCheck NoSuchAlgorithmException" + e);
        } catch (InvalidKeySpecException e) {
            Log.e(TAG, "doCheck InvalidKeySpecException" + e);
        } catch (InvalidKeyException e) {
            Log.e(TAG, "doCheck InvalidKeyException" + e);
        } catch (SignatureException e) {
            Log.e(TAG, "doCheck SignatureException" + e);
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, "doCheck UnsupportedEncodingException" + e);
        }
        return false;
    }

    /**
     * Get the publicKey of the application.
     * During the encoding process, avoid storing the public key in clear text.
     *
     * @return publickey
     */
    public static String getPublicKey(){
        return PUBLIC_KEY;
    }

}
