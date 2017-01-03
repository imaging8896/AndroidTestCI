package hcfs.test.testsuite.api.interf;

import com.hopebaytech.hcfsmgmt.utils.Logs;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import hcfs.test.spec.Err;
import hcfs.test.wrapper.HCFSAPI;

import static org.junit.Assert.*;

public class SetHCFSConfig_InvalidKeyCases {

	private static final String THIS_CLASS = SetHCFSConfig_InvalidKeyCases.class.getSimpleName();

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
	public void unsupportedKeyCase() throws Exception {
		Logs.d(THIS_CLASS, "unsupportedKeyCase", "");
		HCFSAPI.Result result = HCFSAPI.setHCFSConfig("asdddd", "1234");
		assertFalse(result.isSuccess);
		assertEquals(Err.INVALID_ARG, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void emptyStringKeyCase() throws Exception {
		Logs.d(THIS_CLASS, "emptyStringKeyCase", "");
		HCFSAPI.Result result = HCFSAPI.setHCFSConfig("", "1234");
		assertFalse(result.isSuccess);
		assertEquals(Err.INVALID_ARG, result.code);
		assertEquals(0, result.data.size());
	}
}
