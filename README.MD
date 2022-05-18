# Calculator App

## Table of Contents
- [Introduction](#introduction)
- [Setup](#setup)
- [Environment Requirements](#environment-requirements)
- [License](#license)
- [Acknowledgements](#acknowledgements)
- [Questions](#questions)

## Introduction  

This demo showcases the integration process and functions of Huawei IAP using the non-consumable product to remove ads and Huawei Push Kit.

The products in the demo are only used to demonstrate the purchase process, and will not be put into commercial use.

You can use the HMS Toolkit to quickly run the sample code. HMS Toolkit supports one-stop kit integration and provides functions such as free app debugging on remote, real devices. For details about HMS Toolkit, please refer to the [HMS Toolkit documentation](https://developer.huawei.com/consumer/en/doc/development/Tools-Guides/getting-started-0000001077381096?ha_source=hms1).

## Setup

This portion is for those that want to setup the demo with their own web host. Please use ‘app-release-signed.apk’ if you just want to test the demo as is.

### Huawei Developer Account

A Huawei Developer Account is needed to access and configure Huawei IAP and Push Kit (optional). You will need to create an account if you don’t already have one. Please refer to this  [guide](https://developer.huawei.com/consumer/en/doc/help/registerandlogin-0000001052613847).

1. Install Android Studio on your computer. Use Android Studio to open the project-level **build.gradle** file of the sample code.  
2. Create an app in AppGallery Connect and configure the app information. For details, please refer to [Configuring App Information in AppGallery Connect](https://developer.huawei.com/consumer/en/doc/development/HMSCore-Guides/config-agc-0000001050033072?ha_source=hms1).
3. Create and configure your products in AppGallery Connect. For details, please refer to [Configuring Your Products](https://developer.huawei.com/consumer/en/doc/development/HMSCore-Guides/config-product-0000001050033076?ha_source=hms1).
4. Import the demo to Android Studio 3.0 or later and build the demo.  
5. Configure the sample code:  
* Download the **agconnect-services.json** file of your app from AppGallery Connect, and add the file to the app-level directory of the demo.  
* Add the signing certificate and add configurations to the app-level **build.gradle** file.  
* Open the AndroidManifest file and change the package name to your app package name.  
* Replace **PUBLIC_KEY** in the **CipherUtil** class with the public key of your app. For details about how to obtain the public key, please refer to [Querying IAP Information](https://developer.huawei.com/consumer/en/doc/development/HMSCore-Guides/query-payment-info-0000001050166299?ha_source=hms1).
* Replace the products in this demo with your products.  
6. Run the sample code on an Android device or simulator.
7. (Optional) For push messages:
- Uncomment the following code inside of the getToken() method in MainActivity.java
```
     myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
     myClip = ClipData.newPlainText("Push token", pushtoken);  
     myClipboard.setPrimaryClip(myClip);
```
- Refer to the [documentation](https://developer.huawei.com/consumer/en/doc/development/HMSCore-Guides/android-basic-sendtestmsg-0000001087842114) for sending test messages.

## Environment Requirements  

Minumum Android SDK 22 and JDK 1.8 are recommended.

## License
This Android sample code is licensed under the [Apache License, version 2.0](http://www.apache.org/licenses/LICENSE-2.0)

## Acknowledgements
The code in this project has been modified from [Huawei IAP Demo](https://github.com/HMS-Core/hms-iap-clientdemo-android-studio) and [Android Browser Helper](https://github.com/GoogleChrome/android-browser-helper). Their contributions are greatly appreciated.

## Questions
If you have a questions - [Stack Overflow](https://stackoverflow.com/questions/tagged/huawei-mobile-services) is the best place for any programming inquiries. Be sure to include the tag `huawei-mobile-services`.