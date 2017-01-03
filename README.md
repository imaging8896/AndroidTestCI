HCFS API test
=========================================================

## Intro:
* The API test is divided into 3 parts.
    1. behaviour -> test with API behaviour
    2. boost -> boost API (because its test will disconnect Tera app db from Tera app, there has lots of problem)
    3. interface -> API interface test
    
* The tests can be automatically executed via gradle script(there are also shell and python startup scripts).

## Environment setup: Java 8, adb, sdk, gradle, others
* oracle java 8:
    - To install, please refer to: http://www.wikihow.com/Install-Oracle-Java-on-Ubuntu-Linux

* adb:
    - To install, please refer to: http://www.droidviews.com/setup-adb-usb-drivers-ubuntu-easily/
    - 14.04 refer to : http://bernaerts.dyndns.org/linux/74-ubuntu/328-ubuntu-trusty-android-adb-fastboot-qtadb

* SDK:
    - export SDK path as environment variable ANDROID_HOME or call setup_android.sh before run gradle script

* gradle

* Others
    - C++ libs (may cause gradle build fail when uninstalled these libs)
    
    ```bash
    sudo apt-get install lib32stdc++6 lib32z1
    ```
    - Python : 2.7
 


## Device environment setup:
* Need 'su' command by push 'su' into phone(need userdebug boot.img), you can simply call: 

```bash
python rootDevice.py
```
    
# Run the test:
* Terminal: 
Execute the following command to run instrumentation test on device:
```bash
cd test/api_test
echo pwd4root | sudo -S -H ./setup_python.sh
echo pwd4root | sudo -S ./setup_android.sh path_to_sdk
./start_test.sh -t apiInterfTest -d device_serial_num path_where_log_reside
```

* ps : -t apiInterfTest/boostTest/apiBehaviourTest/utilsUnitTest
    - apiInterfTest: API interface test
    - boostTest: boost API test
    - apiBehaviourTest: API behaviour test
    - utilsUnitTest: utils unit test

Execute the following command to run local unit test for test program self(or use android studio):
```bash
cd test/api_test/HCFS_API_Test
./gradlew test
```

# How to inspect test result:
* After you run instrumentation test on device, ther should be test reports in test/api_test/reports
    - reports/junit-noframes.html: merge all tests into html
    - reports/TESTS-TestSuites.xml: merge all tests into xml

* ps : html format in test/api_test/junit-style


# Impovement
* test in docker (there is an adb usb connective problem in docker)

# Trouble shooter
- gradle:
  * Execution failed for task ':lintVitalRelease'. > java.lang.NullPointerException (no error message)
		- local.properties, which should fix this error in the cases where the NDK is not installed
  * Cannot run program "/usr/local/android-sdk-linux/build-tools/19.0.3/aapt": error=2, No such file or directory
		- sudo apt-get install lib32stdc++6 lib32z1
  * Incorrect JAVA_HOME setting : 
        - Remove /usr/bin/gradle line 70
        
- VirtualBox :
	1. VM 設定 USB 自動連接感應 : 
		步驟 : USB 接上測試手機 ->「USB設定」->「USB裝置篩選器」-> 點選右邊「+」按鈕選取該手機 -> 點選右邊「o」按鈕編輯該手機設定 -> 「遠端」值改為「任何」
