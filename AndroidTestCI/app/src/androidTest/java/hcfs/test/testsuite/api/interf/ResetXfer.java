package hcfs.test.testsuite.api.interf;

import hcfs.test.utils.DeviceUtils;
import hcfs.test.wrapper.HCFSAPI;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import static org.junit.Assert.*;

import com.hopebaytech.hcfsmgmt.utils.Logs;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ResetXfer {

	private static final String THIS_CLASS = ResetXfer.class.getSimpleName();

	/**	
	 * void HCFS_reset_xfer(char ** json_res)
	 *	To reset the value of upload/download statistic.
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
		HCFSAPI.Result result = HCFSAPI.resetXfer();
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void wifiOnCase() throws Exception {
		Logs.d(THIS_CLASS, "wifiOnCase", "");
		setWIFI(true);
		sleep(10);

		HCFSAPI.Result result = HCFSAPI.resetXfer();
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void wifiOffCase() throws Exception {
		Logs.d(THIS_CLASS, "wifiOffCase", "");
		setWIFI(false);
		sleep(10);

		HCFSAPI.Result result = HCFSAPI.resetXfer();
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
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
