package hcfs.test.testsuite.api.interf;

import static org.junit.Assert.*;

import hcfs.test.spec.Err;
import hcfs.test.config.Path;
import hcfs.test.wrapper.HCFSAPI;

import com.hopebaytech.hcfsmgmt.utils.Logs;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GetPinStatus_SpecialCases {

	private static final String THIS_CLASS = GetPinStatus_SpecialCases.class.getSimpleName();

/**	
 * void HCFS_pin_status(char ** json_res, char * pathname	)
 * To getByKey the status of (pathname).
 * Return code -	
 *    True 	0 if object is not pinned.
 *    				1 if object is pinned.
 *    False 	Linux errors.
 * 
 * 	json_res		result string in json format.
 * 	pathname 	target pathname.
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
	public void emptyPathCase() throws Exception {
		Logs.d(THIS_CLASS, "emptyPathCase", "");
		HCFSAPI.Result result = HCFSAPI.getPinStatus("");
		assertFalse(result.isSuccess);
		assertEquals(Err.NO_SUCH_FILE, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void nonExistsPathCase() throws Exception {
		Logs.d(THIS_CLASS, "nonExistsPathCase", "");
		HCFSAPI.Result result = HCFSAPI.getPinStatus("/FolderThatDoesNotExist");
		assertFalse(result.isSuccess);
		assertEquals(Err.NO_SUCH_FILE, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void systemFileCase() throws Exception {
		//TODO priority pinned app or system file?
		Logs.d(THIS_CLASS, "systemFileCase", "");
		HCFSAPI.Result result = HCFSAPI.getPinStatus(Path.SYSTEM_FILE);
		assertFalse(result.isSuccess);
		assertEquals(Err.NO_SUCH_FILE, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void systemDirCase() throws Exception {
		Logs.d(THIS_CLASS, "systemDirCase", "");
		HCFSAPI.Result result = HCFSAPI.getPinStatus(Path.SYSTEM_DIR);
		assertFalse(result.isSuccess);
		assertEquals(Err.NO_SUCH_FILE, result.code);
		assertEquals(0, result.data.size());
	}
}
