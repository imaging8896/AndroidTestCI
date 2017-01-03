package hcfs.test.testsuite.api.interf;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.hopebaytech.hcfsmgmt.utils.Logs;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import hcfs.test.spec.APIConst;
import hcfs.test.utils.DeviceUtils;
import hcfs.test.wrapper.HCFSAPI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class CheckRestoreStatus {

	private static final String THIS_CLASS = CheckRestoreStatus.class.getSimpleName();

	/**
     * String checkRestoreStatus()
     * <li>0 if not being restored</li>
     * <li>1 if in stage 1 of restoration process</li>
     * <li>2 if in stage 2 of restoration process</li>
	 * <li>Linux errors</li>
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
		HCFSAPI.Result result = HCFSAPI.checkRestoreStatus();
		assertTrue(result.isSuccess);
		assertTrue(APIConst.RestoreStatus.isValidNum(result.code));
		assertEquals(0, result.data.size());
	}

	@Test
	public void wifiOnCase() throws Exception {
		Logs.d(THIS_CLASS, "wifiOnCase", "");
		setWIFI(true);
		sleep(5);
		HCFSAPI.Result result = HCFSAPI.checkRestoreStatus();
		assertTrue(result.isSuccess);
		assertTrue(APIConst.RestoreStatus.isValidNum(result.code));
		assertEquals(0, result.data.size());
	}

	@Test
	public void wifiOffCase() throws Exception {
		Logs.d(THIS_CLASS, "wifiOffCase", "");
		setWIFI(false);
		sleep(5);
		HCFSAPI.Result result = HCFSAPI.checkRestoreStatus();
		assertTrue(result.isSuccess);
		assertTrue(APIConst.RestoreStatus.isValidNum(result.code));
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
