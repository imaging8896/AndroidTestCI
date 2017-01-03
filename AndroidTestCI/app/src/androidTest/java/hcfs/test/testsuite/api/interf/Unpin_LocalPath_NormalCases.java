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

public class Unpin_LocalPath_NormalCases {

	private static final String THIS_CLASS = Unpin_LocalPath_NormalCases.class.getSimpleName();

/**	
 * void HCFS_unpin_path(char ** json_res,  	char * pin_path)
 * Unpin a file. If the given (pin_path) is a directory, 
 * HCFS_unpin_path() will recursively unpin all files and files in subdirectories.
 * Return code -	
 *    True 	0
 *    False 	Linux errors.
 * 
 * 	json_res		result string in json format.
 * 	pin_path 	a valid pathname (cloud be a file or a directory).
 */

	private String localFilePath;
	private String localDirPath;

	private FilesManager localFilesMgr;
	
	@Before
	public void setUp() throws Exception {
		Logs.d(THIS_CLASS, "setUp", "");
		localFilesMgr = new FilesManager(Path.APK_PATH, Path.L_TEST_DIR);
		localFilesMgr.genFiles(1, 2097152);//2M
		localDirPath = localFilesMgr.getRootPath();
		localFilePath = localFilesMgr.getFilePath(1);
	}

	@After
	public void tearDown() throws Exception {
		Logs.d(THIS_CLASS, "tearDown", "");
		localFilesMgr.cleanup();
	}
	
	@Test
	public void pinnedFileCase() throws Exception {
		Logs.d(THIS_CLASS, "pinnedFileCase", "");
		HCFSAPI.Result result = HCFSAPI.pin(localFilePath, APIConst.PinType.NORMAL);
		assertTrue(result.isSuccess);

		result = HCFSAPI.unpin(localFilePath);
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void priorityPinnedFileCase() throws Exception {
		Logs.d(THIS_CLASS, "priorityPinnedFileCase", "");
		HCFSAPI.Result result = HCFSAPI.pin(localFilePath, APIConst.PinType.PRIORITY);
		assertTrue(result.isSuccess);

		result = HCFSAPI.unpin(localFilePath);
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void pinnedDirCase() throws Exception {
 	    Logs.d(THIS_CLASS, "pinnedDirCase", "");
		HCFSAPI.Result result = HCFSAPI.pin(localDirPath, APIConst.PinType.NORMAL);
		assertTrue(result.isSuccess);

		result = HCFSAPI.unpin(localDirPath);
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void priorityPinnedDirCase() throws Exception {
		Logs.d(THIS_CLASS, "priorityPinnedDirCase", "");
		HCFSAPI.Result result = HCFSAPI.pin(localDirPath, APIConst.PinType.PRIORITY);
		assertTrue(result.isSuccess);

		result = HCFSAPI.unpin(localDirPath);
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void pinnedManyLevelsFileCase() throws Exception {
		Logs.d(THIS_CLASS, "pinnedManyLevelsFileCase", "");
		final String file = localFilesMgr.genFileInLevel(30, 2 * 1024* 1024);
		HCFSAPI.Result result = HCFSAPI.pin(file, APIConst.PinType.NORMAL);
		assertTrue(result.isSuccess);

		result = HCFSAPI.unpin(file);
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void pinnedManyLevelsDirCase() throws Exception {
		Logs.d(THIS_CLASS, "pinnedManyLevelsDirCase", "");
		final String dir = localFilesMgr.genDirInLevel(30);
		HCFSAPI.Result result = HCFSAPI.pin(dir, APIConst.PinType.NORMAL);
		assertTrue(result.isSuccess);

		result = HCFSAPI.unpin(dir);
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void pinnedMaxNameLengthFileCase() throws Exception {
		Logs.d(THIS_CLASS, "pinnedMaxNameLengthFileCase", "");
		final String fileName = "asdddkciefjjfjjccjsuduusudfusdfcusvdscmwecwumwcmwecmiwmcwcvdfvudfvdnvnsasdamdnqwedasicscsdcsdcsgvysdvvnnvsndnnsnvnsndcsdjfwefwefasdasdcaiasiaciasduwqdqwdnccasdcsndcnsdcwecwecewcnwencwnecnanscnasncnanqnwdqdqwdjiwqdjedwdewcscascxcascqwsascasddasdidiqwdiiii";
		final String maxNameFile = localFilesMgr.genFileWithName(fileName, 2 * 1024 * 1024);
		HCFSAPI.Result result = HCFSAPI.pin(maxNameFile, APIConst.PinType.NORMAL);
		assertTrue(result.isSuccess);

		result = HCFSAPI.unpin(maxNameFile);
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void pinnedMaxNameLengthDirCase() throws Exception {
		Logs.d(THIS_CLASS, "pinnedMaxNameLengthDirCase", "");
		final String dirName = "asdddkciefjjfjjccjsuduusudfusdfcusvdscmwecwumwcmwecmiwmcwcvdfvudfvdnvnsasdamdnqwedasicscsdcsdcsgvysdvvnnvsndnnsnvnsndcsdjfwefwefasdasdcaiasiaciasduwqdqwdnccasdcsndcnsdcwecwecewcnwencwnecnanscnasncnanqnwdqdqwdjiwqdjedwdewcscascxcascqwsascasddasdidiqwdiiii";
		final String maxNameDir = localFilesMgr.genDirWithName(dirName);
		HCFSAPI.Result result = HCFSAPI.pin(maxNameDir, APIConst.PinType.NORMAL);
		assertTrue(result.isSuccess);

		result = HCFSAPI.unpin(maxNameDir);
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(0, result.data.size());
	}

	@Ignore("Unimplemented")
	@Test
	public void pinnedSpecialFileCase() throws Exception {
		Logs.d(THIS_CLASS, "pinnedSpecialFileCase", "");
//		HCFSAPI.Result result = HCFSAPI.Pin(localFilePath, APIConst.PinType.PRIORITY);
//		assertTrue(result.isSuccess);
//
//		result = HCFSAPI.unpin(localFilePath);
//		assertTrue(result.isSuccess);
//		assertEquals(0, result.code);
//		assertEquals(0, result.data.size());
	}
}
