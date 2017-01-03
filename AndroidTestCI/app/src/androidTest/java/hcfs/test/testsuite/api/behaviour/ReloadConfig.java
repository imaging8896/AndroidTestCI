package hcfs.test.testsuite.api.behaviour;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import static org.junit.Assert.*;

import com.hopebaytech.hcfsmgmt.utils.Logs;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import hcfs.test.annotation.Bug;
import hcfs.test.spec.ConfigFields;
import hcfs.test.spec.Err;
import hcfs.test.spec.HCFSStat;
import hcfs.test.utils.DeviceUtils;
import hcfs.test.utils.MgmtAppUtils;
import hcfs.test.wrapper.HCFSAPI;

public class ReloadConfig {

	private static final String THIS_CLASS = ReloadConfig.class.getSimpleName();

	/**	
	 * void HCFS_reload_config(char ** json_res)
	 *	Reload HCFS configuration file. Backend can be changed from NONE to NONE/SWIFT/S3.
	 * Return code -	
	 *    True 	0
	 *    False 	Linux errors.
	 * 
	 * 	json_res		result string in json format.
	 */

	private static final String TEST_CONF_KEY = ConfigFields.CURRENT_BACKEND;

	private Context mContext;

	private String _reservedConfigValue;

	@Before
	public void setUp() throws Exception {
		Logs.i(THIS_CLASS, "setUp", "");
		mContext = InstrumentationRegistry.getInstrumentation().getContext();
		reservedConfigByKey();
	}

	@After
	public void tearDown() throws Exception {
		Logs.i(THIS_CLASS, "tearDown", "");
		if(!getConfigByKey(TEST_CONF_KEY).equalsIgnoreCase(_reservedConfigValue)) {
			restoreConfig();
			HCFSAPI.Result result = HCFSAPI.reloadConfig();
			assertTrue(result.isSuccess);
			MgmtAppUtils.forceRefreshToken();
			sleep(10);
			waitCloudStatus(true, 30);
		}
	}

	/*
	 * tests for reloadConfig() // assume that already logged into TeraFonn
	 * TODO: reload config returns false execution result...
	 */
	@Test
	public void invalidBackendTypeCase() throws Exception {
		Logs.i(THIS_CLASS, "invalidBackendTypeCase", "");
		setWIFI(true);
		// backup config field for tear down
		HCFSAPI.Result result = HCFSAPI.getHCFSConfig(ConfigFields.CURRENT_BACKEND);
		assertTrue(result.isSuccess);
		String originalCurrentBackend = (String) result.data.get(ConfigFields.CURRENT_BACKEND);

		// using setConfig to mess up HCFS.conf
		result = HCFSAPI.setHCFSConfig(ConfigFields.CURRENT_BACKEND, "M$Azure");
		assertTrue(result.isSuccess);
		// reload config
		result = HCFSAPI.reloadConfig();
		assertFalse(result.isSuccess);
		assertEquals(Err.OPERATION_NOT_PERMITTED, result.code);

		// using setConfig to set the config back
		result = HCFSAPI.setHCFSConfig(ConfigFields.CURRENT_BACKEND, originalCurrentBackend);
		assertTrue(result.isSuccess);
		// assert the field is set back in the config
		result = HCFSAPI.getHCFSConfig(ConfigFields.CURRENT_BACKEND);
		assertTrue(result.isSuccess);
		assertEquals(originalCurrentBackend, result.data.get(ConfigFields.CURRENT_BACKEND));

		// reload config
		assertTrue(result.isSuccess);
		// assert that connected to cloud
		result = HCFSAPI.getHCFSStat();
		assertTrue(result.isSuccess);
		MgmtAppUtils.forceRefreshToken();
		sleep(10);
		waitCloudStatus(true, 30);
	}
	
	//06
	// TODO
	@Bug("#11304")
	@Ignore
	@Test
	public void operationRejectedOnInitializedDeviceCase() throws Exception {
 		Logs.i(THIS_CLASS, "operationRejectedOnInitializedDeviceCase", "");
		HCFSAPI.Result result = HCFSAPI.reloadConfig();
		assertFalse(result.isSuccess);
	}

	private void waitCloudStatus(boolean isConnected, int timeoutSec) {
		int curRemainTime = timeoutSec;
		HCFSAPI.Result result;
		do {
			result = HCFSAPI.getHCFSStat();
			assertTrue(result.isSuccess);
			if((boolean)result.data.get(HCFSStat.CLOUD_CONNECTION) == isConnected)
				return;
			sleep(1);
		} while(curRemainTime-- >= 0);
		throw new RuntimeException("Timeout while wait cloud connected.");
	}

	private void reservedConfigByKey() {
		_reservedConfigValue = getConfigByKey(TEST_CONF_KEY);
	}

	private void restoreConfig() {
		HCFSAPI.Result result = HCFSAPI.setHCFSConfig(TEST_CONF_KEY, _reservedConfigValue);
		assertTrue("Restore config failure " + result, result.isSuccess);
	}

	private void setWIFI(boolean enable) {
		DeviceUtils.setWifiEnabled(mContext, enable);
		assertEquals(enable, DeviceUtils.isInternetConnected(mContext));
	}

	private String getConfigByKey(String key) {
		HCFSAPI.Result result = HCFSAPI.getHCFSConfig(key);
		assertTrue("Reserve config failure " + result, result.isSuccess);
		return (String) result.data.get(key);
	}

	private void sleep(int timeoutSec) {
		try {Thread.sleep(timeoutSec * 1000);} catch (InterruptedException e) {throw new RuntimeException(e);}
	}
}
