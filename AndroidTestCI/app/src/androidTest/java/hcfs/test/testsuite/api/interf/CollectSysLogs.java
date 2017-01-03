package hcfs.test.testsuite.api.interf;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.hopebaytech.hcfsmgmt.utils.Logs;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import hcfs.test.spec.APIConst;
import hcfs.test.utils.DeviceUtils;
import hcfs.test.utils.FilesManager;
import hcfs.test.wrapper.HCFSAPI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class CollectSysLogs {

	private static final String THIS_CLASS = CollectSysLogs.class.getSimpleName();

	/**
     * String collectSysLogs()
     * <li>True	0 if successful</li>
	 * <li>False Linux errors</li>
	 */

	private Context mContext;

	@Before
	public void setUp() throws Exception {
		Logs.d(THIS_CLASS, "setUp", "");
		mContext = InstrumentationRegistry.getInstrumentation().getContext();

		File logDir = new File(APIConst.Logs.DIR);
		if(logDir.exists())
			FilesManager.deleteRecursively(logDir);
	}

	@After
	public void tearDown() throws Exception {
		Logs.d(THIS_CLASS, "tearDown", "");
		File logDir = new File(APIConst.Logs.DIR);
		if(logDir.exists())
			FilesManager.deleteRecursively(logDir);

		setWIFI(true);
	}

	@Test
	public void checkAPISpecCase() throws Exception {
		Logs.d(THIS_CLASS, "checkAPISpecCase", "");
		HCFSAPI.Result result = HCFSAPI.collectSysLogs();
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void wifiOnCase() throws Exception {
		Logs.d(THIS_CLASS, "wifiOnCase", "");
		setWIFI(true);
		sleep(5);
		HCFSAPI.Result result = HCFSAPI.collectSysLogs();
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void wifiOffCase() throws Exception {
		Logs.d(THIS_CLASS, "wifiOffCase", "");
		setWIFI(false);
		sleep(5);
		HCFSAPI.Result result = HCFSAPI.collectSysLogs();
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
