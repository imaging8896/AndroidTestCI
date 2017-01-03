package hcfs.test.testsuite.api.interf;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.hopebaytech.hcfsmgmt.utils.Logs;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import hcfs.test.spec.APIConst;
import hcfs.test.spec.Err;
import hcfs.test.utils.DeviceUtils;
import hcfs.test.wrapper.HCFSAPI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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

	@Before
	public void setUp() throws Exception {
		Logs.d(THIS_CLASS, "setUp", "");
		mContext = InstrumentationRegistry.getInstrumentation().getContext();
	}

	@After
	public void tearDown() throws Exception {
		Logs.d(THIS_CLASS, "tearDown", "");
		setWIFI(true);
		HCFSAPI.Result result = HCFSAPI.setHCFSSyncStatus(APIConst.SyncStatus.ENABLE);
		assertTrue(result.isSuccess);
	}
	
	@Test
	public void wifiOnEnableCase() throws Exception {
		Logs.d(THIS_CLASS, "wifiOnEnableCase", "");
		setWIFI(true);
		sleep(10);
		HCFSAPI.Result result = HCFSAPI.setHCFSSyncStatus(APIConst.SyncStatus.ENABLE);
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void wifiOffEnableCase() throws Exception {
		Logs.d(THIS_CLASS, "wifiOffEnableCase", "");
		setWIFI(false);
		sleep(10);
		HCFSAPI.Result result = HCFSAPI.setHCFSSyncStatus(APIConst.SyncStatus.ENABLE);
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void wifiOnDisableCase() throws Exception {
		Logs.d(THIS_CLASS, "wifiOnDisableCase", "");
		setWIFI(true);
		sleep(10);
		HCFSAPI.Result result = HCFSAPI.setHCFSSyncStatus(APIConst.SyncStatus.DISABLE);
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void wifiOffDisableCase() throws Exception {
		Logs.d(THIS_CLASS, "wifiOffDisableCase", "");
		setWIFI(false);
		sleep(10);
		HCFSAPI.Result result = HCFSAPI.setHCFSSyncStatus(APIConst.SyncStatus.DISABLE);
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void abnormalValueNegativeIntCase() throws Exception {
		Logs.d(THIS_CLASS, "abnormalValueNegativeIntCase", "");
		HCFSAPI.Result result = HCFSAPI.setHCFSSyncStatus(-1);
		assertFalse(result.isSuccess);
		assertEquals(Err.INVALID_ARG, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void abnormalValuePositiveIntCase() throws Exception {
		Logs.d(THIS_CLASS, "abnormalValuePositiveIntCase", "");
		HCFSAPI.Result result = HCFSAPI.setHCFSSyncStatus(123);
		assertFalse(result.isSuccess);
		assertEquals(Err.INVALID_ARG, result.code);
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
