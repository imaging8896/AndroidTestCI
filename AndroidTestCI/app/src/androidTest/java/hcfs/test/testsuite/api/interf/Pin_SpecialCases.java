package hcfs.test.testsuite.api.interf;

import static org.junit.Assert.*;

import hcfs.test.spec.APIConst;
import hcfs.test.spec.Err;
import hcfs.test.config.Path;
import hcfs.test.wrapper.HCFSAPI;
import com.hopebaytech.hcfsmgmt.utils.Logs;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class Pin_SpecialCases {

	private static final String THIS_CLASS = Pin_SpecialCases.class.getSimpleName();

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
		HCFSAPI.Result result = HCFSAPI.pin("/FileThat.DoesNotExist", APIConst.PinType.NORMAL);
		assertFalse(result.isSuccess);
		assertEquals(Err.NO_SUCH_FILE, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void nonExistsPathPriorityPinCase() throws Exception {
		Logs.d(THIS_CLASS, "nonExistsPathPriorityPinCase", "");
		HCFSAPI.Result result = HCFSAPI.pin("/FileThat.DoesNotExist", APIConst.PinType.PRIORITY);
		assertFalse(result.isSuccess);
		assertEquals(Err.NO_SUCH_FILE, result.code);
		assertEquals(0, result.data.size());
	}
	
	@Test
	public void emptyPathCase() throws Exception {
		Logs.d(THIS_CLASS, "emptyPathCase", "");
		HCFSAPI.Result result = HCFSAPI.pin("", APIConst.PinType.NORMAL);
		assertFalse(result.isSuccess);
		assertEquals(Err.NO_SUCH_FILE, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void emptyPathPriorityPinCase() throws Exception {
		Logs.d(THIS_CLASS, "emptyPathPriorityPinCase", "");
		HCFSAPI.Result result = HCFSAPI.pin("", APIConst.PinType.PRIORITY);
		assertFalse(result.isSuccess);
		assertEquals(Err.NO_SUCH_FILE, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void systemDirCase() throws Exception {
		Logs.d(THIS_CLASS, "systemDirCase", "");
		HCFSAPI.Result result = HCFSAPI.pin(Path.SYSTEM_DIR, APIConst.PinType.NORMAL);
		assertFalse(result.isSuccess);
		assertEquals(Err.NO_SUCH_FILE, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void systemDirPriorityPinCase() throws Exception {
		Logs.d(THIS_CLASS, "systemDirCase", "");
		HCFSAPI.Result result = HCFSAPI.pin(Path.SYSTEM_DIR, APIConst.PinType.PRIORITY);
		assertFalse(result.isSuccess);
		assertEquals(Err.NO_SUCH_FILE, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void systemFileCase() throws Exception {
		Logs.d(THIS_CLASS, "systemFileCase", "");
		HCFSAPI.Result result = HCFSAPI.pin(Path.SYSTEM_FILE, APIConst.PinType.NORMAL);
		assertFalse(result.isSuccess);
		assertEquals(Err.NO_SUCH_FILE, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void systemFilePriorityPinCase() throws Exception {
		Logs.d(THIS_CLASS, "systemFileCase", "");
		HCFSAPI.Result result = HCFSAPI.pin(Path.SYSTEM_FILE, APIConst.PinType.PRIORITY);
		assertFalse(result.isSuccess);
		assertEquals(Err.NO_SUCH_FILE, result.code);
		assertEquals(0, result.data.size());
	}
}
