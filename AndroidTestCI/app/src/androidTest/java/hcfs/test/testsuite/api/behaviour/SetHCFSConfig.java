package hcfs.test.testsuite.api.behaviour;

import hcfs.test.annotation.Bug;
import hcfs.test.spec.ConfigFields;
import hcfs.test.spec.ConfigKeys;
import hcfs.test.spec.HCFSStat;
import hcfs.test.utils.MgmtAppUtils;
import hcfs.test.wrapper.HCFSAPI;

import com.hopebaytech.hcfsmgmt.utils.Logs;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

public class SetHCFSConfig {

	private static final String THIS_CLASS = SetHCFSConfig.class.getSimpleName();

/**	
 * void HCFS_set_config(char ** json_res, char * key	, char * value)
 * To set value of a specific field in HCFS configuration. 
 * (Supported keys list in hcfs.test.spec.ConfigKey.java).
 * Return code -	
 *    True 	0 
 *    False 	Linux errors.
 * 
 * 	json_res		result string in json format.
 * 	key 	field in HCFS configuration.
 * 	value 	value for input key.
 */
    // TODO restore full config

	private String originBackendType;

	@Before
	public void setUp() throws Exception {
		Logs.i(THIS_CLASS, "setUp", "");

		HCFSAPI.Result result = HCFSAPI.getHCFSConfig(ConfigKeys.CURRENT_BACKEND);
		assertTrue("Reserve config failure " + result, result.isSuccess);
		originBackendType = (String) result.data.get(ConfigKeys.CURRENT_BACKEND);
	}

	@After
	public void tearDown() throws Exception {
		Logs.i(THIS_CLASS, "tearDown", "");

		HCFSAPI.Result result = HCFSAPI.setHCFSConfig(ConfigFields.CURRENT_BACKEND, originBackendType);
		assertTrue("Re-set config failure " + result, result.isSuccess);
		result = HCFSAPI.reloadConfig();
		assertTrue("Reload config failure " + result, result.isSuccess);
		MgmtAppUtils.forceRefreshToken();
		sleep(10);
		waitCloudStatus(true, 30);
	}
	
	// TODO: write tests for all fields, the CONTAINER field might cuz a bug
	@Test
	public void normalValueValidBackendTypeCase() throws Exception {
		Logs.i(THIS_CLASS, "normalValueValidBackendTypeCase", "");
		final String configKey = ConfigFields.CURRENT_BACKEND;

		HCFSAPI.Result result;
		for(String normalBackendValue : new String[]{"none", "swift", "s3", "swifttoken"}) {
			result = HCFSAPI.setHCFSConfig(configKey, normalBackendValue);
			assertTrue(result.isSuccess);
			assertEquals(0, result.code);
			assertEquals(0, result.data.size());

			//TODO another verification without calling others HCFS API
			result = HCFSAPI.getHCFSConfig(configKey);
			assertTrue(result.isSuccess);
			String currentBackendType = (String) result.data.get(configKey);
			assertEquals(normalBackendValue, currentBackendType);
		}
	}

	@Bug("#10883")
	@Ignore
	@Test
	public void abnormalValueBackendTypeRandomStringCase() throws Exception {
		Logs.i(THIS_CLASS, "abnormalValueBackendTypeRandomStringCase", "");
		final String configKey = ConfigFields.CURRENT_BACKEND;
		HCFSAPI.Result result = HCFSAPI.setHCFSConfig(configKey, "HK3345678");
		String failedMessage = "Set config backend type 'random string' failure " + result;
		assertFalse(failedMessage, result.isSuccess);
	}

	@Bug("#10883")
	@Ignore
	@Test
	public void abnormalValueBackendTypeNewLineCharCase() throws Exception {
		Logs.i(THIS_CLASS, "abnormalValueBackendTypeNewLineCharCase", "");
		final String configKey = ConfigFields.CURRENT_BACKEND;
		HCFSAPI.Result result = HCFSAPI.setHCFSConfig(configKey, "\n");
		String failedMessage = "Set config backend type 'new line' failure " + result;
		assertFalse(failedMessage, result.isSuccess);
	}

	@Bug("#10883")
	@Ignore
	@Test
	public void abnormalValueBackendTypeIsEOFCase() throws Exception {
		Logs.i(THIS_CLASS, "abnormalValueBackendTypeIsEOFCase", "");
		final String configKey = ConfigFields.CURRENT_BACKEND;
		HCFSAPI.Result result = HCFSAPI.setHCFSConfig(configKey, "std::ios::eof()");
		String failedMessage = "Set config backend type 'std::ios::eof()' failure " + result;
		assertFalse(failedMessage, result.isSuccess);
	}

	@Bug("#10883")
	@Ignore
	@Test
	public void abnormalValueBackendTypeEOFCase() throws Exception {
		Logs.i(THIS_CLASS, "abnormalValueBackendTypeEOFCase", "");
		final String configKey = ConfigFields.CURRENT_BACKEND;
		HCFSAPI.Result result = HCFSAPI.setHCFSConfig(configKey, "\0");
		String failedMessage = "Set config backend type 'EOF' failure " + result;
		assertFalse(failedMessage, result.isSuccess);
	}

	@Bug("#10883")
	@Ignore
	@Test
	public void abnormalValueBackendTypeEmptyStringCase() throws Exception {
		Logs.i(THIS_CLASS, "abnormalValueBackendTypeEmptyStringCase", "");
		final String configKey = ConfigFields.CURRENT_BACKEND;
		HCFSAPI.Result result = HCFSAPI.setHCFSConfig(configKey, "");
		String failedMessage = "Set config backend type '' failure " + result;
		assertFalse(failedMessage, result.isSuccess);
	}

	@Bug("#10883")
	@Ignore
	@Test
	public void abnormalValueBackendTypePilcrowCase() throws Exception {
		Logs.i(THIS_CLASS, "abnormalValueBackendTypePilcrowCase", "");
		final String configKey = ConfigFields.CURRENT_BACKEND;
		HCFSAPI.Result result = HCFSAPI.setHCFSConfig(configKey, "¶");
		String failedMessage = "Set config backend type '¶' failure " + result;
		assertFalse(failedMessage, result.isSuccess);
	}

	@Bug("#10883")
	@Ignore
	@Test
	public void abnormalValueBackendTypeDollarSignCase() throws Exception {
		Logs.i(THIS_CLASS, "abnormalValueBackendTypeDollarSignCase", "");
		final String configKey = ConfigFields.CURRENT_BACKEND;
		HCFSAPI.Result result = HCFSAPI.setHCFSConfig(configKey, "$");
		String failedMessage = "Set config backend type '$' failure " + result;
		assertFalse(failedMessage, result.isSuccess);
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

	private void sleep(int timeoutSec) {
		try {Thread.sleep(timeoutSec * 1000);} catch (InterruptedException e) {throw new RuntimeException(e);}
	}
}
