package hcfs.test.testsuite.api.interf;

import com.hopebaytech.hcfsmgmt.utils.Logs;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import hcfs.test.spec.ConfigKeys;
import hcfs.test.wrapper.HCFSAPI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SetHCFSConfig_ValidKeyCases {

	private static final String THIS_CLASS = SetHCFSConfig_ValidKeyCases.class.getSimpleName();

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

	@Before
	public void setUp() throws Exception {
		Logs.d(THIS_CLASS, "setUp", "");
	}

	@After
	public void tearDown() throws Exception {
		Logs.d(THIS_CLASS, "tearDown", "");
	}

	@Test
	public void abnormalValueInvalidKeyCase() throws Exception {
		Logs.d(THIS_CLASS, "abnormalValueInvalidKeyCase", "");
		HCFSAPI.Result result = HCFSAPI.setHCFSConfig("asdddd", "1234");
		assertFalse(result.isSuccess);
	}


	@Test
	public void currentBackendCase() throws Exception {
		Logs.d(THIS_CLASS, "currentBackendCase", "");
		preserveCondfig(ConfigKeys.CURRENT_BACKEND);

		HCFSAPI.Result result = HCFSAPI.setHCFSConfig(ConfigKeys.CURRENT_BACKEND, "none");
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(0, result.data.size());

		restoreCondfig();
	}

	@Test
	public void swiftAccountCase() throws Exception {
		Logs.d(THIS_CLASS, "swiftAccountCase", "");
		preserveCondfig(ConfigKeys.SWIFT_ACCOUNT);

		HCFSAPI.Result result = HCFSAPI.setHCFSConfig(ConfigKeys.SWIFT_ACCOUNT, "asdasd");
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(0, result.data.size());

		restoreCondfig();
	}

	@Test
	public void swiftUserCase() throws Exception {
		Logs.d(THIS_CLASS, "swiftUserCase", "");
		preserveCondfig(ConfigKeys.SWIFT_USER);

		HCFSAPI.Result result = HCFSAPI.setHCFSConfig(ConfigKeys.SWIFT_USER, "asdasd");
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(0, result.data.size());

		restoreCondfig();
	}

	@Test
	public void swiftPassCase() throws Exception {
		Logs.d(THIS_CLASS, "swiftPassCase", "");
		preserveCondfig(ConfigKeys.SWIFT_PASS);

		HCFSAPI.Result result = HCFSAPI.setHCFSConfig(ConfigKeys.SWIFT_PASS, "asdasd");
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(0, result.data.size());

		restoreCondfig();
	}

	@Test
	public void swiftUrlCase() throws Exception {
		Logs.d(THIS_CLASS, "swiftUrlCase", "");
		preserveCondfig(ConfigKeys.SWIFT_URL);

		HCFSAPI.Result result = HCFSAPI.setHCFSConfig(ConfigKeys.SWIFT_URL, "https://123.123.123.123:5566");
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(0, result.data.size());

		restoreCondfig();
	}

	@Test
	public void swiftContainerCase() throws Exception {
		Logs.d(THIS_CLASS, "swiftContainerCase", "");
		preserveCondfig(ConfigKeys.SWIFT_CONTAINER);

		HCFSAPI.Result result = HCFSAPI.setHCFSConfig(ConfigKeys.SWIFT_CONTAINER, "asd");
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(0, result.data.size());

		restoreCondfig();
	}

	@Test
	public void swiftProtocolCase() throws Exception {
		Logs.d(THIS_CLASS, "normalValueSwiftProtocolCase", "");
		preserveCondfig(ConfigKeys.SWIFT_PROTOCOL);

		HCFSAPI.Result result = HCFSAPI.setHCFSConfig(ConfigKeys.SWIFT_PROTOCOL, "http");
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(0, result.data.size());

		restoreCondfig();
	}

	private String preservedKey = null;
	private String preservedValue = null;

	private void preserveCondfig(String key) {
		preservedKey = key;
		HCFSAPI.Result result = HCFSAPI.getHCFSConfig(preservedKey);
		preservedValue = (String) result.data.get(preservedKey);
	}

	private void restoreCondfig() {
		if(preservedKey == null || preservedValue == null)
			throw new RuntimeException("You didn't preserve config. Fail to restore config.");
		HCFSAPI.Result result = HCFSAPI.setHCFSConfig(preservedKey, preservedValue);
		if(!result.isSuccess || result.code != 0 || result.data.size() != 0)
			throw new RuntimeException("Fail to call setHCFSConfig to restore config. (" + result + ")");
		preservedKey = null;
		preservedValue = null;
	}
}
