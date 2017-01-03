package hcfs.test.testsuite.api.interf;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import static org.junit.Assert.*;

import com.hopebaytech.hcfsmgmt.utils.Logs;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import hcfs.test.config.Path;
import hcfs.test.spec.APIConst;
import hcfs.test.utils.DeviceUtils;
import hcfs.test.utils.FilesManager;
import hcfs.test.wrapper.HCFSAPI;


public class StopUploadTeraData {

	private static final String THIS_CLASS = StopUploadTeraData.class.getSimpleName();

	/**
     * String stopUploadTeraData()
     * <li>1 if no sync point is set.</li>
     * <li>0 when canceling the setting completed.</li>
     * <li>Negative error code in case that error occurs</li>
	 */

	private Context mContext;

	private FilesManager sdcardFilesMgr;

	@Before
	public void setUp() throws Exception {
		Logs.d(THIS_CLASS, "setUp", "");
		mContext = InstrumentationRegistry.getInstrumentation().getContext();

		setWIFI(true);
		sdcardFilesMgr = new FilesManager(Path.SDCARD_PATH, Path.S_TEST_DIR);
	}

	@After
	public void tearDown() throws Exception {
		Logs.d(THIS_CLASS, "tearDown", "");
		setWIFI(true);
		sdcardFilesMgr.cleanup();
	}

	@Test
	public void checkAPISpecCase() throws Exception {
		Logs.d(THIS_CLASS, "checkAPISpecCase", "");
		HCFSAPI.Result result = HCFSAPI.stopUploadTeraData();
		assertTrue(result.isSuccess);
		assertTrue(APIConst.SyncPoint.isValidNum(result.code));
		assertEquals(0, result.data.size());
	}

	@Test
	public void cancelUnsetSyncPointCase() throws Exception {
		Logs.d(THIS_CLASS, "cancelUnsetSyncPointCase", "");
		HCFSAPI.Result result = HCFSAPI.stopUploadTeraData();
		assertTrue(result.isSuccess);

		result = HCFSAPI.stopUploadTeraData();
		assertTrue(result.isSuccess);
		assertEquals(APIConst.SyncPoint.UNSET, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void cancelSyncPointCase() throws Exception {
		Logs.d(THIS_CLASS, "cancelSyncPointCase", "");
		sdcardFilesMgr.genFiles(10, 2 * 1024 * 1024);
		sleep(2);//Wait file into cache
		HCFSAPI.Result result = HCFSAPI.startUploadTeraData();
		assertTrue(result.isSuccess);

		result = HCFSAPI.stopUploadTeraData();
		assertTrue(result.isSuccess);
		assertEquals(APIConst.SyncPoint.CLEAR, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void cancelUnsetSyncPointWifiOffCase() throws Exception {
		Logs.d(THIS_CLASS, "cancelUnsetSyncPointWifiOffCase", "");
		HCFSAPI.Result result = HCFSAPI.stopUploadTeraData();
		assertTrue(result.isSuccess);
		setWIFI(false);
		sleep(20);

		result = HCFSAPI.stopUploadTeraData();
		assertTrue(result.isSuccess);
		assertEquals(APIConst.SyncPoint.UNSET, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void cancelSyncPointWifiOffCase() throws Exception {
		Logs.d(THIS_CLASS, "cancelSyncPointWifiOffCase", "");
		sdcardFilesMgr.genFiles(10, 2 * 1024 * 1024);
		sleep(2);//Wait file into cache
		HCFSAPI.Result result = HCFSAPI.startUploadTeraData();
		assertTrue(result.isSuccess);
		setWIFI(false);
		sleep(20);

		result = HCFSAPI.stopUploadTeraData();
		assertTrue(result.isSuccess);
		assertEquals(APIConst.SyncPoint.CLEAR, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void cancelSyncPointMultiTimesCase() throws Exception {
		Logs.d(THIS_CLASS, "cancelSyncPointMultiTimesCase", "");
		final int testTimes = 10;

		HCFSAPI.Result result = HCFSAPI.stopUploadTeraData();
		assertTrue(result.isSuccess);

		for(int i = 1; i <= testTimes; i++) {
			result = HCFSAPI.stopUploadTeraData();
			assertTrue(result.isSuccess);
			assertEquals(APIConst.SyncPoint.UNSET, result.code);
			assertEquals(0, result.data.size());
		}
	}

	private void setWIFI(boolean enable) {
		DeviceUtils.setWifiEnabled(mContext, enable);
		assertEquals(enable, DeviceUtils.isInternetConnected(mContext));
	}

	private void sleep(int timeoutSec) {
		try {Thread.sleep(timeoutSec * 1000);} catch (InterruptedException e) {throw new RuntimeException(e);}
	}
}
