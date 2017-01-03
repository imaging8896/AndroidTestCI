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

public class Unpin_LocalPath_UnpinnedCases {

	private static final String THIS_CLASS = Unpin_LocalPath_UnpinnedCases.class.getSimpleName();

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
	public void fileCase() throws Exception {
		Logs.d(THIS_CLASS, "fileCase", "");
		HCFSAPI.Result result = HCFSAPI.unpin(localFilePath);
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void dirCase() throws Exception {
 	    Logs.d(THIS_CLASS, "dirCase", "");
		HCFSAPI.Result result = HCFSAPI.unpin(localDirPath);
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void manyLevelsFileCase() throws Exception {
		Logs.d(THIS_CLASS, "manyLevelsFileCase", "");
		final String file = localFilesMgr.genFileInLevel(30, 2 * 1024* 1024);
		HCFSAPI.Result result = HCFSAPI.unpin(file);
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void manyLevelsDirCase() throws Exception {
		Logs.d(THIS_CLASS, "manyLevelsDirCase", "");
		final String dir = localFilesMgr.genDirInLevel(30);
		HCFSAPI.Result result = HCFSAPI.pin(dir, APIConst.PinType.NORMAL);
		assertTrue(result.isSuccess);

		result = HCFSAPI.unpin(dir);
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void maxNameLengthFileCase() throws Exception {
		Logs.d(THIS_CLASS, "maxNameLengthFileCase", "");
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
	public void maxNameLengthDirCase() throws Exception {
		Logs.d(THIS_CLASS, "maxNameLengthDirCase", "");
		final String dirName = "asdddkciefjjfjjccjsuduusudfusdfcusvdscmwecwumwcmwecmiwmcwcvdfvudfvdnvnsasdamdnqwedasicscsdcsdcsgvysdvvnnvsndnnsnvnsndcsdjfwefwefasdasdcaiasiaciasduwqdqwdnccasdcsndcnsdcwecwecewcnwencwnecnanscnasncnanqnwdqdqwdjiwqdjedwdewcscascxcascqwsascasddasdidiqwdiiii";
		final String maxNameDir = localFilesMgr.genDirWithName(dirName);
		HCFSAPI.Result result = HCFSAPI.pin(maxNameDir, APIConst.PinType.NORMAL);
		assertTrue(result.isSuccess);

		result = HCFSAPI.unpin(maxNameDir);
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(0, result.data.size());
	}

	@Ignore("I have no idea.")
	@Test
	public void specialFileCase() throws Exception {
		Logs.d(THIS_CLASS, "specialFileCase", "");
//		result = HCFSAPI.unpin(localFilePath);
//		assertTrue(result.isSuccess);
//		assertEquals(0, result.code);
//		assertEquals(0, result.data.size());
	}
}
