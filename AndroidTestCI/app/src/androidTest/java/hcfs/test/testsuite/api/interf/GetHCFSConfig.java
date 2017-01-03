package hcfs.test.testsuite.api.interf;

import static org.junit.Assert.*;

import hcfs.test.spec.APIConst;
import hcfs.test.spec.Err;
import hcfs.test.spec.ConfigKeys;
import hcfs.test.wrapper.HCFSAPI;

import com.hopebaytech.hcfsmgmt.utils.Logs;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GetHCFSConfig {

	private static final String THIS_CLASS = GetHCFSConfig.class.getSimpleName();

/**	
 * void HCFS_get_config(char ** json_res, char * key	)
 * Get the Value of specific field. (Supported keys list in hcfs.test.spec.ConfigKey.java).
 * Return code -	
 *    True 	0 
 *    False 	Linux errors.
 * 
 * 	json_res		result string in json format.
 * 	key 	field in HCFS configuration.
 */

    @Before
	public void setUp() throws Exception {
		Logs.d(THIS_CLASS, "setUp", "");
	}

	@After
	public void tearDown() throws Exception {
		Logs.d(THIS_CLASS, "tearDown", "");
	}
	
	@Test
	public void normalValueCurrentBackendCase() throws Exception {
		Logs.d(THIS_CLASS, "normalValueCurrentBackendCase", "");
		HCFSAPI.Result result = HCFSAPI.getHCFSConfig(ConfigKeys.CURRENT_BACKEND);
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(1, result.data.size());
		
		String backendUsed = (String) result.data.get(ConfigKeys.CURRENT_BACKEND);
		assertTrue(APIConst.BackendType.isValidType(backendUsed));
	}

	@Test
	public void normalValueSwiftAccountCase() throws Exception {
		Logs.d(THIS_CLASS, "normalValueSwiftAccountCase", "");
		HCFSAPI.Result result = HCFSAPI.getHCFSConfig(ConfigKeys.SWIFT_ACCOUNT);
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(1, result.data.size());

		Object swiftAccount = result.data.get(ConfigKeys.SWIFT_ACCOUNT);
		assertTrue(swiftAccount instanceof String);
	}

	@Test
	public void normalValueSwiftUserCase() throws Exception {
		Logs.d(THIS_CLASS, "normalValueSwiftUserCase", "");
		HCFSAPI.Result result = HCFSAPI.getHCFSConfig(ConfigKeys.SWIFT_USER);
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(1, result.data.size());

		Object swiftUser = result.data.get(ConfigKeys.SWIFT_USER);
		assertTrue(swiftUser instanceof String);
	}

	@Test
	public void normalValueSwiftPassCase() throws Exception {
		Logs.d(THIS_CLASS, "normalValueSwiftPassCase", "");
		HCFSAPI.Result result = HCFSAPI.getHCFSConfig(ConfigKeys.SWIFT_PASS);
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(1, result.data.size());

		Object swiftPass = result.data.get(ConfigKeys.SWIFT_PASS);
		assertTrue(swiftPass instanceof String);
	}

	@Test
	public void normalValueSwiftUrlCase() throws Exception {
		Logs.d(THIS_CLASS, "normalValueSwiftUrlCase", "");
		HCFSAPI.Result result = HCFSAPI.getHCFSConfig(ConfigKeys.SWIFT_URL);
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(1, result.data.size());

		Object swiftUrl = result.data.get(ConfigKeys.SWIFT_URL);
		assertTrue(swiftUrl instanceof String);
	}

	@Test
	public void normalValueSwiftContainerCase() throws Exception {
		Logs.d(THIS_CLASS, "normalValueSwiftContainerCase", "");
		HCFSAPI.Result result = HCFSAPI.getHCFSConfig(ConfigKeys.SWIFT_CONTAINER);
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(1, result.data.size());

		Object swiftContainer = result.data.get(ConfigKeys.SWIFT_CONTAINER);
		assertTrue(swiftContainer instanceof String);
	}

	@Test
	public void normalValueSwiftProtocolCase() throws Exception {
		Logs.d(THIS_CLASS, "normalValueSwiftProtocolCase", "");
		HCFSAPI.Result result = HCFSAPI.getHCFSConfig(ConfigKeys.SWIFT_PROTOCOL);
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(1, result.data.size());

		String swiftProtocol = (String) result.data.get(ConfigKeys.SWIFT_PROTOCOL);
		assertTrue(APIConst.SwiftProtocol.isValidType(swiftProtocol));
	}

	@Test
	public void abnormalValueNonExistsKeyCase() throws Exception {
		Logs.d(THIS_CLASS, "abnormalValueNonExistsKeyCase", "");
		HCFSAPI.Result result = HCFSAPI.getHCFSConfig("nonExistsKey");
		assertFalse(result.isSuccess);
		assertEquals(Err.OPERATION_NOT_PERMITTED, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void abnormalValueEmptyStringCase() throws Exception {
		Logs.d(THIS_CLASS, "abnormalValueEmptyStringCase", "");
		HCFSAPI.Result result = HCFSAPI.getHCFSConfig("");
		assertFalse(result.isSuccess);
		assertEquals(Err.OPERATION_NOT_PERMITTED, result.code);
		assertEquals(0, result.data.size());
	}
}
