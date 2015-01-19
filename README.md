##EdemaCare ##
=========

 **EdemaCare** is a android app for edema patient , this app should be used with the special hardware. Bluetooth Lower Energy Technology is used in the system, which connect the mobile with the hardware, and data transfer is based on the ble connection.



## 
##App Screenshot

<img src="https://github.com/Timeszoro/EdemaCare/raw/master/ScreenShot/1.png" width="320" height="568" />
<img src="https://github.com/Timeszoro/EdemaCare/raw/master/ScreenShot/2.png" width="320" height="568" />

<img src="https://github.com/Timeszoro/EdemaCare/raw/master/ScreenShot/3.png" width="320" height="568" />
<img src="https://github.com/Timeszoro/EdemaCare/raw/master/ScreenShot/4.png" width="320" height="568" />



##OpenSource Used in this App

[MpAndroidChart](https://github.com/PhilJay/MPAndroidChart)

[Spinner Wheel](https://github.com/ai212983/android-spinnerwheel)

##Download the app
to be update

##Debug Log

**bug #01**--->can not find the R file in the project
    solution--->make sure the xml file(resource file ) is correct and try to clean the project and re build
    
**bug #02**---->can not find the support.v4 file
    solution--->the support.v4 file in the sdk\extras\android\support\..
    
**bug #03**:E/AndroidRuntime(10676): Caused by: java.lang.ClassNotFoundException: Didn't find class "com.timeszoro.edemacare.BledeviceActivity" on path: DexPathList[[zip file "/data/app/com.example.edemacare-2.apk"],nativeLibraryDirectories=[/data/app-lib/com.example.edemacare-2, /vendor/lib, /system/lib]]  
    solution--->1)Right click on your project and select Properties.
                2)Select Java Build Path from the menu on the left.
                3)Select the Order and Export tab.
                4)From the list make sure the libraries or external jars you added to your project are checked.
                5)Finally, clean your project & run.
                
                
**bug #04** error: Error retrieving parent for item: No resource found that matches the given name 
 'Theme.AppCompat.Light'.
    原因是无法找到原来的android-support-v7的支持包，需要重新添加。
    solution---->http://www.yoyong.com/archives/876

