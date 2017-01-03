package hcfs.test.testsuite.api.interf;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.hopebaytech.hcfsmgmt.utils.Logs;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import hcfs.test.utils.DeviceUtils;
import hcfs.test.utils.MgmtAppUtils;
import hcfs.test.wrapper.HCFSAPI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class SetSwiftToken {

	private static final String THIS_CLASS = SetSwiftToken.class.getSimpleName();

	/**
	 * void HCFS_set_notify_location(char ** json_res, char *server_loc) Setup
	 * notification server. Return code - True 0 False Linux errors.
	 * 
	 * json_res result string in json format. server_loc linux abstract
	 * namespace.
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
		Logs.d(THIS_CLASS, "tearDown", "refresh token");
		MgmtAppUtils.forceRefreshToken();
	}

	@Test
	public void checkAPISpecCase() throws Exception {
		Logs.d(THIS_CLASS, "checkAPISpecCase", "");
		HCFSAPI.Result result = HCFSAPI.setSwiftToken("asdasd", "asdasd");
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void wifiOnCase() throws Exception {
		Logs.d(THIS_CLASS, "wifiOnCase", "");
		setWIFI(true);
		HCFSAPI.Result result = HCFSAPI.setSwiftToken("asdasd", "asdasd");
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void wifiOffCase() throws Exception {
		Logs.d(THIS_CLASS, "wifiOffCase", "");
		setWIFI(false);
		HCFSAPI.Result result = HCFSAPI.setSwiftToken("asdasd", "asdasd");
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(0, result.data.size());
	}

	private void setWIFI(boolean enable) {
		DeviceUtils.setWifiEnabled(mContext, enable);
		assertEquals(enable, DeviceUtils.isInternetConnected(mContext));
	}
}
