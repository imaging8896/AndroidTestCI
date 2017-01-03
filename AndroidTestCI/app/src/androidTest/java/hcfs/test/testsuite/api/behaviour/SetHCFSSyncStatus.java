package hcfs.test.testsuite.api.behaviour;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import hcfs.test.config.Path;
import hcfs.test.spec.APIConst;
import hcfs.test.spec.HCFSStat;
import hcfs.test.spec.JsonFields;
import hcfs.test.utils.DeviceUtils;
import hcfs.test.utils.FilesManager;
import hcfs.test.wrapper.HCFSAPI;

import static org.junit.Assert.*;

import com.hopebaytech.hcfsmgmt.utils.Logs;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SetHCFSSyncStatus {

	private static final String THIS_CLASS = SetHCFSSyncStatus.class.getSimpleName();

	/**	
	 * void HCFS_toggle_sync(char ** json_res,  	int32_t 	enabled 	)
	 * To toggle if hcfs can/can't sync data in local cache to cloud storage.
	 * Return code -	
	 *    True 	0
	 *    False 	Linux errors.
	 * 
	 * 	json_res		result string in json format.
	 * 	enabled 	1 to turn on sync, 0 to turn off sync.
	 */
	
	/**	
	 * void HCFS_get_sync_status(char ** json_res)
	 * To getByKey status of cloud sync.
	 * Return data dict in json_res -
	 * data: {
	 *     enabled: boolean,
	 *     }
	 * Return code -	
	 *    True 	0
	 *    False 	Linux errors.
	 * 
	 * 	json_res		result string in json format.
	 */

	private Context mContext;
	private FilesManager localFilesMgt;
	
	@Before
	public void setUp() throws Exception {
		Logs.i(THIS_CLASS, "setUp", "");
		mContext = InstrumentationRegistry.getInstrumentation().getContext();
		localFilesMgt = new FilesManager(Path.APK_PATH, Path.L_TEST_DIR);
	}

	@After
	public void tearDown() throws Exception {
		Logs.i(THIS_CLASS, "tearDown", "");
		localFilesMgt.cleanup();

		setWIFI(true);

		HCFSAPI.Result result = HCFSAPI.setHCFSSyncStatus(APIConst.SyncStatus.ENABLE);
		assertTrue(result.isSuccess);
	}
	
	@Test
	public void enableSyncWifiOnCase() throws Exception {
		Logs.i(THIS_CLASS, "enableSyncWifiOnCase", "");
		setWIFI(true);

		HCFSAPI.Result result = HCFSAPI.setHCFSSyncStatus(APIConst.SyncStatus.ENABLE);
		assertTrue(result.isSuccess);
		result = HCFSAPI.getHCFSSyncStatus();
		assertTrue(result.isSuccess);
		assertTrue((boolean) result.data.get(JsonFields.SYNC_STATUS));
		sleep(5);
		result = HCFSAPI.resetXfer();
		assertTrue(result.isSuccess);
		
		localFilesMgt.genFiles(1, 10485760L);//10M
		assertTrue(isTransferWithCloudWithin(120));
	}
	
	@Test
	public void disableSyncWifiOnCase() throws Exception {
		Logs.i(THIS_CLASS, "disableSyncWifiOnCase", "");
		setWIFI(true);

		HCFSAPI.Result result = HCFSAPI.setHCFSSyncStatus(APIConst.SyncStatus.DISABLE);
		assertTrue(result.isSuccess);
		result = HCFSAPI.getHCFSSyncStatus();
		assertTrue(result.isSuccess);
		assertFalse((boolean) result.data.get(JsonFields.SYNC_STATUS));
		sleep(5);
		result = HCFSAPI.resetXfer();
		assertTrue(result.isSuccess);
		
		localFilesMgt.genFiles(1, 10485760L);//10M
		sleep(30);
		result = HCFSAPI.getHCFSStat();
		assertTrue(result.isSuccess);
		assertEquals(0, result.data.get(HCFSStat.DAILY_DATA_DOWNLOAD));
		assertEquals(0, result.data.get(HCFSStat.DAILY_DATA_UPLOAD));
	}

	@Test
	public void enableSyncWifiOffCase() throws Exception {
		Logs.i(THIS_CLASS, "enableSyncWifiOffCase", "");
		setWIFI(false);

		HCFSAPI.Result result = HCFSAPI.setHCFSSyncStatus(APIConst.SyncStatus.ENABLE);
		assertTrue(result.isSuccess);
		result = HCFSAPI.getHCFSSyncStatus();
		assertTrue(result.isSuccess);
		assertTrue((boolean) result.data.get(JsonFields.SYNC_STATUS));
		sleep(5);
		result = HCFSAPI.resetXfer();
		assertTrue(result.isSuccess);
		
		localFilesMgt.genSparseFile(1, 2097152L);//2M
		sleep(30);
		result = HCFSAPI.getHCFSStat();
		assertTrue(result.isSuccess);
		assertEquals(0, result.data.get(HCFSStat.DAILY_DATA_DOWNLOAD));
		assertEquals(0, result.data.get(HCFSStat.DAILY_DATA_UPLOAD));
	}

	@Test
	public void disableSyncWifiOffCase() throws Exception {
		Logs.i(THIS_CLASS, "disableSyncWifiOffCase", "");
		setWIFI(false);

		HCFSAPI.Result result = HCFSAPI.setHCFSSyncStatus(APIConst.SyncStatus.DISABLE);
		assertTrue(result.isSuccess);
		result = HCFSAPI.getHCFSSyncStatus();
		assertTrue(result.isSuccess);
		assertFalse((boolean) result.data.get(JsonFields.SYNC_STATUS));
		sleep(5);
		result = HCFSAPI.resetXfer();
		assertTrue(result.isSuccess);
		
		localFilesMgt.genSparseFile(1, 2097152L);//2M
		sleep(30);
		result = HCFSAPI.getHCFSStat();
		assertTrue(result.isSuccess);
		assertEquals(0, result.data.get(HCFSStat.DAILY_DATA_DOWNLOAD));
		assertEquals(0, result.data.get(HCFSStat.DAILY_DATA_UPLOAD));
	}

	private boolean isTransferWithCloudWithin(int timeoutSec) {
		int curRemainTime = timeoutSec;
		HCFSAPI.Result result;
		do {
			result = HCFSAPI.getHCFSStat();
			assertTrue(result.isSuccess);
			if(!result.data.get(HCFSStat.DAILY_DATA_DOWNLOAD).equals(0))
				return true;
			if(!result.data.get(HCFSStat.DAILY_DATA_UPLOAD).equals(0))
				return true;
			sleep(1);
		} while(curRemainTime-- >= 0);
		return false;
	}

	private void setWIFI(boolean enable) {
		DeviceUtils.setWifiEnabled(mContext, enable);
		assertEquals(enable, DeviceUtils.isInternetConnected(mContext));
	}

	private void sleep(int timeoutSec) {
		try {Thread.sleep(timeoutSec * 1000);} catch (InterruptedException e) {throw new RuntimeException(e);}
	}
}
