# PedalPower
========

Android application that communicates with Adafruit's nrf8001 breakout board leveraging Adafruit's custom pre packaged UART service.

Require's circuit laid out Build a simple Bluefruit LE + Arduino circuit and load the Bluefruit LE library echoDemo on the Arduino.

Make sure bluetooth is enabled, then load the BLETest application (has a generic Android icon).  Once started the app will immediately search for BTLE devices and connect to the first one it finds with the UART service.  Status messages will be displayed in a text view on the screen.  

Once you see the connected and services discovered message, try typing text in the edit view at the top and click Send to send it to the Arduino.  From the Arduino side try sending text from its serial monitor to the BLETest app.

The source for this app is written in the latest version (3.0.0) of [Android Studio](http://developer.android.com/sdk/installing/studio.html).

Tested on Android 8.0 devices (Nexus 6P, Google Pixel XL). Needed to enable USB debugging and set USB configuration to "MTP (Media Transfer Protocl) in Developer Settings -> Select USB Configuration

Thanks to [Tony Dicola](https://github.com/tdicola/) for the example code.
