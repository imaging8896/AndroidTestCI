package hcfs.test.testsuite.api.interf;

import com.hopebaytech.hcfsmgmt.utils.Logs;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import hcfs.test.config.Path;
import hcfs.test.spec.APIConst;
import hcfs.test.utils.FilesManager;
import hcfs.test.wrapper.HCFSAPI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Pin_SdcardPath_PinnedCases {

	private static final String THIS_CLASS = Pin_SdcardPath_PinnedCases.class.getSimpleName();

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

	private String sdcardFilePath;
	private String sdcardDirPath;

	private FilesManager sdcardFilesMgr;
	
	@Before
	public void setUp() throws Exception {
		Logs.d(THIS_CLASS, "setUp", "");
		sdcardFilesMgr = new FilesManager(Path.SDCARD_PATH, Path.S_TEST_DIR);
		sdcardFilesMgr.genFiles(1, 2097152);//2M
		sdcardDirPath = sdcardFilesMgr.getRootPath();
		sdcardFilePath = sdcardFilesMgr.getFilePath(1);
	}

	@After
	public void tearDown() throws Exception {
		Logs.d(THIS_CLASS, "tearDown", "");
		sdcardFilesMgr.cleanup();
	}

	@Test
	public void fileCase() throws Exception {
		Logs.d(THIS_CLASS, "fileCase", "");
		HCFSAPI.Result result = HCFSAPI.pin(sdcardFilePath, APIConst.PinType.NORMAL);
		assertTrue(result.isSuccess);

		result = HCFSAPI.pin(sdcardFilePath, APIConst.PinType.NORMAL);
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void filePriorityPinCase() throws Exception {
		Logs.d(THIS_CLASS, "filePriorityPinCase", "");
		HCFSAPI.Result result = HCFSAPI.pin(sdcardFilePath, APIConst.PinType.NORMAL);
		assertTrue(result.isSuccess);

		result = HCFSAPI.pin(sdcardFilePath, APIConst.PinType.PRIORITY);
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void dirCase() throws Exception {
		Logs.d(THIS_CLASS, "dirCase", "");
		HCFSAPI.Result result = HCFSAPI.pin(sdcardDirPath, APIConst.PinType.NORMAL);
		assertTrue(result.isSuccess);

		result = HCFSAPI.pin(sdcardDirPath, APIConst.PinType.NORMAL);
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void dirPriorityPinCase() throws Exception {
		Logs.d(THIS_CLASS, "dirPriorityPinCase", "");
		HCFSAPI.Result result = HCFSAPI.pin(sdcardDirPath, APIConst.PinType.NORMAL);
		assertTrue(result.isSuccess);

		result = HCFSAPI.pin(sdcardDirPath, APIConst.PinType.PRIORITY);
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void priorityPinnedFileCase() throws Exception {
		Logs.d(THIS_CLASS, "priorityPinnedFileCase", "");
		HCFSAPI.Result result = HCFSAPI.pin(sdcardFilePath, APIConst.PinType.PRIORITY);
		assertTrue(result.isSuccess);

		result = HCFSAPI.pin(sdcardFilePath, APIConst.PinType.NORMAL);
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void priorityPinnedFilePriorityPinCase() throws Exception {
		Logs.d(THIS_CLASS, "priorityPinnedFilePriorityPinCase", "");
		HCFSAPI.Result result = HCFSAPI.pin(sdcardFilePath, APIConst.PinType.PRIORITY);
		assertTrue(result.isSuccess);

		result = HCFSAPI.pin(sdcardFilePath, APIConst.PinType.PRIORITY);
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void priorityPinnedDirCase() throws Exception {
		Logs.d(THIS_CLASS, "priorityPinnedDirCase", "");
		HCFSAPI.Result result = HCFSAPI.pin(sdcardDirPath, APIConst.PinType.PRIORITY);
		assertTrue(result.isSuccess);

		result = HCFSAPI.pin(sdcardDirPath, APIConst.PinType.NORMAL);
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void priorityPinnedDirPriorityPinCase() throws Exception {
		Logs.d(THIS_CLASS, "priorityPinnedDirPriorityPinCase", "");
		HCFSAPI.Result result = HCFSAPI.pin(sdcardDirPath, APIConst.PinType.PRIORITY);
		assertTrue(result.isSuccess);

		result = HCFSAPI.pin(sdcardDirPath, APIConst.PinType.PRIORITY);
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(0, result.data.size());
	}

	@Ignore("Unimplemented")
	@Test
	public void specialFileCase() throws Exception {
		Logs.d(THIS_CLASS, "specialFileCase", "");
//		HCFSAPI.Result result = HCFSAPI.Pin(sdcardFilePath, APIConst.PinType.PRIORITY);
//		assertTrue(result.isSuccess);
//
//		result = HCFSAPI.Pin(sdcardFilePath, APIConst.PinType.PRIORITY);
//		assertTrue(result.isSuccess);
//		assertEquals(0, result.code);
//		assertEquals(0, result.data.size());
	}
}
