package hcfs.test.testsuite.api.interf;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.hopebaytech.hcfsmgmt.utils.Logs;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import hcfs.test.spec.JsonFields;
import hcfs.test.utils.DeviceUtils;
import hcfs.test.wrapper.HCFSAPI;

import static org.junit.Assert.assertEquals;

import static org.junit.Assert.assertTrue;

public class GetHCFSSyncStatus {

	private static final String THIS_CLASS = GetHCFSSyncStatus.class.getSimpleName();

	/**
	 * void HCFS_get_sync_status(char ** json_res)
	 * To get status of cloud sync.
	 * 	data: {
	 * 		enabled: boolean,
	 * 	}
	 *
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
		setWIFI(true);
	}

	@After
	public void tearDown() throws Exception {
		Logs.d(THIS_CLASS, "tearDown", "");
		setWIFI(true);
	}

	@Test
	public void checkAPISpecCase() throws Exception {
		Logs.d(THIS_CLASS, "checkAPISpecCase", "");
		HCFSAPI.Result result = HCFSAPI.getHCFSSyncStatus();
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(1, result.data.size());
		assertTrue(result.data.get(JsonFields.SYNC_STATUS) instanceof Boolean);
	}

	@Test
	public void wifiOnCase() throws Exception {
		Logs.d(THIS_CLASS, "wifiOnCase", "");
		setWIFI(false);
		sleep(10);
		HCFSAPI.Result result = HCFSAPI.getHCFSSyncStatus();
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(1, result.data.size());
		assertTrue(result.data.get(JsonFields.SYNC_STATUS) instanceof Boolean);
	}

	@Test
	public void wifiOffCase() throws Exception {
		Logs.d(THIS_CLASS, "wifiOffCase", "");
		setWIFI(false);
		sleep(10);
		HCFSAPI.Result result = HCFSAPI.getHCFSSyncStatus();
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(1, result.data.size());
		assertTrue(result.data.get(JsonFields.SYNC_STATUS) instanceof Boolean);
	}

	private void setWIFI(boolean enable) {
		DeviceUtils.setWifiEnabled(mContext, enable);
		assertEquals(enable, DeviceUtils.isInternetConnected(mContext));
	}

	private void sleep(int timeoutSec) {
		try {Thread.sleep(timeoutSec * 1000);} catch (InterruptedException e) {throw new RuntimeException(e);}
	}
}
