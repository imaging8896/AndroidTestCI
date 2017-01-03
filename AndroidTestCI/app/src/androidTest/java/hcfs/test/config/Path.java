package hcfs.test.config;

import android.os.Environment;

public class Path {

	public static final String DATADATA_DIR = "/data/data";
	// the apk path is intentionally defined without using android.context.Context
	public static final String APK_PATH = DATADATA_DIR + "/hcfs.test.testsuite";
	public static final String SDCARD_PATH = Environment.getExternalStorageDirectory().getPath();
	
	public static final String L_TEST_DIR = "apiLocalTestDir";
	public static final String S_TEST_DIR = "apiSdcardTestDir";
	
	public static final String SYSTEM_DIR = "/system";
	public static final String SYSTEM_FILE = "/system/usr/share/zoneinfo/tzdata";
}
