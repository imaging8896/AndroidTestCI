package hcfs.test.testsuite.api.interf;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.hopebaytech.hcfsmgmt.utils.Logs;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import hcfs.test.config.Path;
import hcfs.test.spec.APIConst;
import hcfs.test.utils.DeviceUtils;
import hcfs.test.utils.FilesManager;
import hcfs.test.wrapper.HCFSAPI;

import static org.junit.Assert.*;


public class StartUploadTeraData {

	private static final String THIS_CLASS = StartUploadTeraData.class.getSimpleName();

	/**
	 * String startUploadTeraData()
	 * <li>1 if system is clean now. That is, there is no dirty data.</li>
     * <li>0 when setting sync point completed.</li>
     * <li>Negative error code in case that error occurs</li>
     *
     * String stopUploadTeraData()
     * <li>1 if no sync point is set.</li>
     * <li>0 when canceling the setting completed.</li>
     * <li>Negative error code in case that error occurs</li>
	 */

	private Context mContext;

	private FilesManager localFilesMgr;

	@Before
	public void setUp() throws Exception {
		Logs.d(THIS_CLASS, "setUp", "");
		mContext = InstrumentationRegistry.getInstrumentation().getContext();

		setWIFI(true);
		localFilesMgr = new FilesManager(Path.APK_PATH, Path.L_TEST_DIR);

		HCFSAPI.Result result = HCFSAPI.stopUploadTeraData();
		assertTrue(result.isSuccess);
	}

	@After
	public void tearDown() throws Exception {
		Logs.d(THIS_CLASS, "tearDown", "");
		setWIFI(true);

		localFilesMgr.cleanup();
	}

	@Test
	public void cacheDirtyCase() throws Exception {
		Logs.d(THIS_CLASS, "cacheDirtyCase", "");
		final int testFilesNum = 100;
		final long testFilesSize = 2 * 1024L * 1024L;

		localFilesMgr.genFiles(testFilesNum, testFilesSize);
		sleep(2);//Wait file into cache
		HCFSAPI.Result result = HCFSAPI.startUploadTeraData();
		assertTrue(result.isSuccess);
		assertEquals(APIConst.SyncState.DIRTY, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void callAPIMultipleTimesCase() throws Exception {
		Logs.d(THIS_CLASS, "callAPIMultipleTimesCase", "");
		final int testTimes = 10;
		final int testFilesNum = 10;
		final long testFilesSize = 2 * 1024L * 1024L;

		for(int i = 1; i <= testTimes; i++) {
			localFilesMgr.genFiles(testFilesNum, testFilesSize);
			sleep(2);//Wait file into cache
			HCFSAPI.Result result = HCFSAPI.startUploadTeraData();
			assertTrue(result.isSuccess);
			assertEquals(APIConst.SyncState.DIRTY, result.code);
			assertEquals(0, result.data.size());
		}
	}

	@Test
	public void wifiOnCase() throws Exception {
		Logs.d(THIS_CLASS, "wifiOnCase", "");
		setWIFI(true);
		HCFSAPI.Result result = HCFSAPI.startUploadTeraData();
		assertTrue(result.isSuccess);
		assertTrue(APIConst.SyncState.isValidNum(result.code));
		assertEquals(0, result.data.size());
	}

	@Test
    public void wifiOffCase() throws Exception {
		Logs.d(THIS_CLASS, "wifiOffCase", "");
		setWIFI(false);
		HCFSAPI.Result result = HCFSAPI.startUploadTeraData();
		assertTrue(result.isSuccess);
		assertTrue(APIConst.SyncState.isValidNum(result.code));
		assertEquals(0, result.data.size());
	}

	private void setWIFI(boolean enable) {
		DeviceUtils.setWifiEnabled(mContext, enable);
		assertEquals(enable, DeviceUtils.isInternetConnected(mContext));
	}

	private void sleep(int timeoutSec) {
		try {Thread.sleep(timeoutSec * 1000);} catch (InterruptedException e) {throw new RuntimeException(e);}
	}
}
