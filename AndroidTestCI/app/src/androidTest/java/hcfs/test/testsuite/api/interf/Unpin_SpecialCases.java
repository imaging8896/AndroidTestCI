package hcfs.test.testsuite.api.interf;

import com.hopebaytech.hcfsmgmt.utils.Logs;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import hcfs.test.config.Path;
import hcfs.test.spec.APIConst;
import hcfs.test.spec.Err;
import hcfs.test.wrapper.HCFSAPI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class Unpin_SpecialCases {

	private static final String THIS_CLASS = Unpin_SpecialCases.class.getSimpleName();

/**	
 * void HCFS_pin_path(char ** json_res,  	char * pin_path, char 	pin_type	)
 * Pin a file so that it will never be replaced when doing cache replacement. 
 * If the given (pin_path) is a directory, 
 * HCFS_pin_path() will recursively Pin all files and files in subdirectories.
 * Return code -	
 *    True 	0
 *    False 	ENOSPC when pinned space is not available, Linux errors.
 * 
 * 	json_res		result string in json format.
 * 	pin_path 	a valid pathname (cloud be a file or a directory).
 * 	pin_type 	1 for Pin, 2 for high-priority-Pin.
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
	public void nonExistsPathCase() throws Exception {
		Logs.d(THIS_CLASS, "nonExistsPathCase", "");
		HCFSAPI.Result result = HCFSAPI.unpin("/FileThat.DoesNotExist");
		assertFalse(result.isSuccess);
		assertEquals(Err.NO_SUCH_FILE, result.code);
		assertEquals(0, result.data.size());
	}
	
	@Test
	public void emptyPathCase() throws Exception {
		Logs.d(THIS_CLASS, "emptyPathCase", "");
		HCFSAPI.Result result = HCFSAPI.unpin("");
		assertFalse(result.isSuccess);
		assertEquals(Err.NO_SUCH_FILE, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void systemDirCase() throws Exception {
		Logs.d(THIS_CLASS, "systemDirCase", "");
		HCFSAPI.Result result = HCFSAPI.unpin(Path.SYSTEM_DIR);
		assertFalse(result.isSuccess);
		assertEquals(Err.NO_SUCH_FILE, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void systemFileCase() throws Exception {
		Logs.d(THIS_CLASS, "systemFileCase", "");
		HCFSAPI.Result result = HCFSAPI.unpin(Path.SYSTEM_FILE);
		assertFalse(result.isSuccess);
		assertEquals(Err.NO_SUCH_FILE, result.code);
		assertEquals(0, result.data.size());
	}
}
