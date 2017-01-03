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

public class GetPinStatus_LocalPath_NormalCases {

	private static final String THIS_CLASS = GetPinStatus_LocalPath_NormalCases.class.getSimpleName();

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
	
	private String localFilePath;
	private String localDirPath;

	private FilesManager localFileMgr;
	
	@Before
	public void setUp() throws Exception {
		Logs.d(THIS_CLASS, "setUp", "");
		localFileMgr = new FilesManager(Path.APK_PATH, Path.L_TEST_DIR);
		localFileMgr.genFiles(1, TEST_FILE_SIZE);
		localFilePath = localFileMgr.getFilePath(1);
		localDirPath = localFileMgr.getRootPath();
	}

	@After
	public void tearDown() throws Exception {
		Logs.d(THIS_CLASS, "tearDown", "");
		localFileMgr.cleanup();
	}

	@Test
	public void appDirCase() throws Exception {
		Logs.d(THIS_CLASS, "appDirCase", "");
		HCFSAPI.Result result = HCFSAPI.getPinStatus(Path.APK_PATH);
		assertTrue(result.isSuccess);
		assertEquals(APIConst.PinStatus.PINNED, result.code);
		assertEquals(0, result.data.size());
	    // TODO default app pinned
		//TODO Pin app?
	}

	@Test
	public void unpinnedFileCase() throws Exception {
		Logs.d(THIS_CLASS, "unpinnedFileCase", "");
		HCFSAPI.Result result = HCFSAPI.getPinStatus(localFilePath);
		assertTrue(result.isSuccess);
		assertEquals(APIConst.PinStatus.PINNED, result.code);
		assertEquals(0, result.data.size());
		// TODO default app pinned
	}

	@Ignore("Default app pinned")
	@Test
	public void pinnedFileCase() throws Exception {
		Logs.d(THIS_CLASS, "pinnedFileCase", "");
		HCFSAPI.Result result = HCFSAPI.pin(localFilePath, APIConst.PinType.NORMAL);
		assertTrue(result.isSuccess);

		result = HCFSAPI.getPinStatus(localFilePath);
		assertTrue(result.isSuccess);
		assertEquals(APIConst.PinStatus.PINNED, result.code);
		assertEquals(0, result.data.size());
	}

	@Ignore("Default app pinned")
	@Test
	public void priorityPinnedFileCase() throws Exception {
		Logs.d(THIS_CLASS, "priorityPinnedFileCase", "");
		HCFSAPI.Result result = HCFSAPI.pin(localFilePath, APIConst.PinType.PRIORITY);
		assertTrue(result.isSuccess);

		result = HCFSAPI.getPinStatus(localFilePath);
		assertTrue(result.isSuccess);
		assertEquals(APIConst.PinStatus.PRIORITY, result.code);
		assertEquals(0, result.data.size());
	}

	@Ignore("Default app pinned")
	@Test
	public void unpinnedDirCase() throws Exception {
		Logs.d(THIS_CLASS, "unpinnedDirCase", "");
		HCFSAPI.Result result = HCFSAPI.getPinStatus(localDirPath);
		assertTrue(result.isSuccess);
		assertEquals(APIConst.PinStatus.UNPINNED, result.code);
		assertEquals(0, result.data.size());
	}

	@Ignore("Default app pinned")
	@Test
	public void pinnedDirCase() throws Exception {
		Logs.d(THIS_CLASS, "pinnedDirCase", "");
		HCFSAPI.Result result = HCFSAPI.pin(localDirPath, APIConst.PinType.NORMAL);
		assertTrue(result.isSuccess);

		result = HCFSAPI.getPinStatus(localDirPath);
		assertTrue(result.isSuccess);
		assertEquals(APIConst.PinStatus.PINNED, result.code);
		assertEquals(0, result.data.size());
	}

	@Ignore("Default app pinned")
	@Test
	public void priorityPinnedDirCase() throws Exception {
		Logs.d(THIS_CLASS, "priorityPinnedDirCase", "");
		HCFSAPI.Result result = HCFSAPI.pin(localDirPath, APIConst.PinType.PRIORITY);
		assertTrue(result.isSuccess);

		result = HCFSAPI.getPinStatus(localDirPath);
		assertTrue(result.isSuccess);
		assertEquals(APIConst.PinStatus.PRIORITY, result.code);
		assertEquals(0, result.data.size());
	}
}
