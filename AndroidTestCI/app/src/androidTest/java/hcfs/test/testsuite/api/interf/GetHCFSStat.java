package hcfs.test.testsuite.api.interf;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import hcfs.test.spec.HCFSStat;
import hcfs.test.utils.DeviceUtils;
import hcfs.test.wrapper.HCFSAPI;

import static org.junit.Assert.*;

import com.hopebaytech.hcfsmgmt.utils.Logs;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GetHCFSStat {

	private static final String THIS_CLASS = GetHCFSStat.class.getSimpleName();

/**	
 * void HCFS_stat(char ** json_res)
 * To fetch HCFS sytem statistics.
 * 
 * data: {
 *		quota: Bytes,
 *		vol_used: Bytes,
 *		cloud_used: Bytes,
 *		cache_total: Bytes,
 *		cache_used: Bytes,
 *		cache_dirty: Bytes,
 *		pin_max: Bytes,
 *		pin_total: Bytes,
 *		xfer_up: Bytes,
 *		xfer_down: Bytes,
 *		cloud_conn: True|False,
 *		data_transfer: Integer (0 means no data transfer, 1 means data transfer in progress, 2 means data transfer in progress but slow.)
 * }
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
	}
	
	@Test
	public void checkAPISpecCase() throws Exception {
		Logs.d(THIS_CLASS, "checkAPISpecCase", "");
		HCFSAPI.Result result = HCFSAPI.getHCFSStat();
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(12, result.data.size());

		assertTrue(result.data.get(HCFSStat.CLOUD_CONNECTION) instanceof Boolean);
		assertTrue(isPositiveInteger(result.data.get(HCFSStat.BACKEND_QUOTA)));
		assertTrue(isPositiveInteger(result.data.get(HCFSStat.VOLUME_USED)));
		assertTrue(isPositiveInteger(result.data.get(HCFSStat.CLOUD_STORAGE_USED)));
		assertTrue(isPositiveInteger(result.data.get(HCFSStat.CACHE_STORAGE_TOTAL)));
		assertTrue(isPositiveInteger(result.data.get(HCFSStat.CACHE_STORAGE_USED)));
		assertTrue(isPositiveInteger(result.data.get(HCFSStat.CACHE_STORAGE_DIRTY)));
		assertTrue(isPositiveInteger(result.data.get(HCFSStat.PIN_SPACE_MAX)));
		assertTrue(isPositiveInteger(result.data.get(HCFSStat.PIN_SPACE_TOTAL_USED)));
		assertTrue(isPositiveInteger(result.data.get(HCFSStat.DAILY_DATA_UPLOAD)));
		assertTrue(isPositiveInteger(result.data.get(HCFSStat.DAILY_DATA_DOWNLOAD)));
		assertTrue(isPositiveInteger(result.data.get(HCFSStat.DATA_TRANSFER_RATE)));
	}

	@Test
	public void wifiOnCase() throws Exception {
		Logs.d(THIS_CLASS, "wifiOnCase", "");
		setWIFI(true);
		sleep(10);
		HCFSAPI.Result result = HCFSAPI.getHCFSStat();
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(12, result.data.size());

		assertTrue(result.data.get(HCFSStat.CLOUD_CONNECTION) instanceof Boolean);
		assertTrue(isPositiveInteger(result.data.get(HCFSStat.BACKEND_QUOTA)));
		assertTrue(isPositiveInteger(result.data.get(HCFSStat.VOLUME_USED)));
		assertTrue(isPositiveInteger(result.data.get(HCFSStat.CLOUD_STORAGE_USED)));
		assertTrue(isPositiveInteger(result.data.get(HCFSStat.CACHE_STORAGE_TOTAL)));
		assertTrue(isPositiveInteger(result.data.get(HCFSStat.CACHE_STORAGE_USED)));
		assertTrue(isPositiveInteger(result.data.get(HCFSStat.CACHE_STORAGE_DIRTY)));
		assertTrue(isPositiveInteger(result.data.get(HCFSStat.PIN_SPACE_MAX)));
		assertTrue(isPositiveInteger(result.data.get(HCFSStat.PIN_SPACE_TOTAL_USED)));
		assertTrue(isPositiveInteger(result.data.get(HCFSStat.DAILY_DATA_UPLOAD)));
		assertTrue(isPositiveInteger(result.data.get(HCFSStat.DAILY_DATA_DOWNLOAD)));
		assertTrue(isPositiveInteger(result.data.get(HCFSStat.DATA_TRANSFER_RATE)));
	}

	@Test
	public void wifiOffCase() throws Exception {
		Logs.d(THIS_CLASS, "wifiOffCase", "");
		setWIFI(false);
		sleep(10);
		HCFSAPI.Result result = HCFSAPI.getHCFSStat();
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(12, result.data.size());

		assertTrue(result.data.get(HCFSStat.CLOUD_CONNECTION) instanceof Boolean);
		assertTrue(isPositiveInteger(result.data.get(HCFSStat.BACKEND_QUOTA)));
		assertTrue(isPositiveInteger(result.data.get(HCFSStat.VOLUME_USED)));
		assertTrue(isPositiveInteger(result.data.get(HCFSStat.CLOUD_STORAGE_USED)));
		assertTrue(isPositiveInteger(result.data.get(HCFSStat.CACHE_STORAGE_TOTAL)));
		assertTrue(isPositiveInteger(result.data.get(HCFSStat.CACHE_STORAGE_USED)));
		assertTrue(isPositiveInteger(result.data.get(HCFSStat.CACHE_STORAGE_DIRTY)));
		assertTrue(isPositiveInteger(result.data.get(HCFSStat.PIN_SPACE_MAX)));
		assertTrue(isPositiveInteger(result.data.get(HCFSStat.PIN_SPACE_TOTAL_USED)));
		assertTrue(isPositiveInteger(result.data.get(HCFSStat.DAILY_DATA_UPLOAD)));
		assertTrue(isPositiveInteger(result.data.get(HCFSStat.DAILY_DATA_DOWNLOAD)));
		assertTrue(isPositiveInteger(result.data.get(HCFSStat.DATA_TRANSFER_RATE)));
	}

	private void setWIFI(boolean enable) {
		DeviceUtils.setWifiEnabled(mContext, enable);
		assertEquals(enable, DeviceUtils.isInternetConnected(mContext));
	}

	private boolean isPositiveInteger(Object obj) {
		if(obj instanceof Long && (long) obj >= 0)
			return true;
		return obj instanceof Integer && (int) obj >= 0;
	}

	private void sleep(int timeoutSec) {
		try {Thread.sleep(timeoutSec * 1000);} catch (InterruptedException e) {throw new RuntimeException(e);}
	}
}
