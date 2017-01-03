package hcfs.test.testsuite.api.interf;

import com.hopebaytech.hcfsmgmt.utils.Logs;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import hcfs.test.config.Path;
import hcfs.test.spec.APIConst;
import hcfs.test.utils.FilesManager;
import hcfs.test.wrapper.HCFSAPI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class GetPinStatus_SdcardPath_NormalCases {

	private static final String THIS_CLASS = GetPinStatus_SdcardPath_NormalCases.class.getSimpleName();

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

	private static final int TEST_FILE_SIZE = 2 * 1024 * 1024;
	
	private String sdcardFilePath;
	private String sdcardDirPath;

	private FilesManager sdcardFileMgr;
	
	@Before
	public void setUp() throws Exception {
		Logs.d(THIS_CLASS, "setUp", "");
		sdcardFileMgr = new FilesManager(Path.SDCARD_PATH, Path.S_TEST_DIR);
		sdcardFileMgr.genFiles(1, TEST_FILE_SIZE);
		sdcardFilePath = sdcardFileMgr.getFilePath(1);
		sdcardDirPath = sdcardFileMgr.getRootPath();
	}

	@After
	public void tearDown() throws Exception {
		Logs.d(THIS_CLASS, "tearDown", "");
		sdcardFileMgr.cleanup();
	}

	@Test
	public void unpinnedFileCase() throws Exception {
		Logs.d(THIS_CLASS, "unpinnedFileCase", "");
		HCFSAPI.Result result = HCFSAPI.getPinStatus(sdcardFilePath);
		assertTrue(result.isSuccess);
		assertEquals(APIConst.PinStatus.UNPINNED, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void pinnedFileCase() throws Exception {
		Logs.d(THIS_CLASS, "pinnedFileCase", "");
		HCFSAPI.Result result = HCFSAPI.pin(sdcardFilePath, APIConst.PinType.NORMAL);
		assertTrue(result.isSuccess);

		result = HCFSAPI.getPinStatus(sdcardFilePath);
		assertTrue(result.isSuccess);
		assertEquals(APIConst.PinStatus.PINNED, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void priorityPinnedFileCase() throws Exception {
		Logs.d(THIS_CLASS, "priorityPinnedFileCase", "");
		HCFSAPI.Result result = HCFSAPI.pin(sdcardFilePath, APIConst.PinType.PRIORITY);
		assertTrue(result.isSuccess);

		result = HCFSAPI.getPinStatus(sdcardFilePath);
		assertTrue(result.isSuccess);
		assertEquals(APIConst.PinStatus.PRIORITY, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void unpinnedDirCase() throws Exception {
		Logs.d(THIS_CLASS, "unpinnedDirCase", "");
		HCFSAPI.Result result = HCFSAPI.getPinStatus(sdcardDirPath);
		assertTrue(result.isSuccess);
		assertEquals(APIConst.PinStatus.UNPINNED, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void pinnedDirCase() throws Exception {
		Logs.d(THIS_CLASS, "pinnedDirCase", "");
		HCFSAPI.Result result = HCFSAPI.pin(sdcardDirPath, APIConst.PinType.NORMAL);
		assertTrue(result.isSuccess);

		result = HCFSAPI.getPinStatus(sdcardDirPath);
		assertTrue(result.isSuccess);
		assertEquals(APIConst.PinStatus.PINNED, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void priorityPinnedDirCase() throws Exception {
		Logs.d(THIS_CLASS, "priorityPinnedDirCase", "");
		HCFSAPI.Result result = HCFSAPI.pin(sdcardDirPath, APIConst.PinType.PRIORITY);
		assertTrue(result.isSuccess);

		result = HCFSAPI.getPinStatus(sdcardDirPath);
		assertTrue(result.isSuccess);
		assertEquals(APIConst.PinStatus.PRIORITY, result.code);
		assertEquals(0, result.data.size());
	}
}
