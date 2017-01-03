package hcfs.test.testsuite.api.interf;

import com.hopebaytech.hcfsmgmt.utils.Logs;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;

import hcfs.test.config.Path;
import hcfs.test.spec.APIConst;
import hcfs.test.utils.FilesManager;
import hcfs.test.wrapper.HCFSAPI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Pin_LocalPath_NormalCases {

	private static final String THIS_CLASS = Pin_LocalPath_NormalCases.class.getSimpleName();

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
	public void unpinnedFileCase() throws Exception {
		Logs.d(THIS_CLASS, "unpinnedFileCase", "");
		HCFSAPI.Result result = HCFSAPI.pin(localFilePath, APIConst.PinType.NORMAL);
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void unpinnedFilePriorityPinCase() throws Exception {
		Logs.d(THIS_CLASS, "unpinnedFilePriorityPinCase", "");
		HCFSAPI.Result result = HCFSAPI.pin(localFilePath, APIConst.PinType.PRIORITY);
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void unpinnedDirCase() throws Exception {
		Logs.d(THIS_CLASS, "unpinnedDirCase", "");
		HCFSAPI.Result result = HCFSAPI.pin(localDirPath, APIConst.PinType.NORMAL);
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void unpinnedDirPriorityPinCase() throws Exception {
		Logs.d(THIS_CLASS, "unpinnedDirPriorityPinCase", "");
		HCFSAPI.Result result = HCFSAPI.pin(localDirPath, APIConst.PinType.PRIORITY);
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void unpinnedManyLevelsFileCase() throws Exception {
		Logs.d(THIS_CLASS, "unpinnedManyLevelsFileCase", "");
		final String file = localFilesMgr.genFileInLevel(30, 2 * 1024* 1024);
		HCFSAPI.Result result = HCFSAPI.pin(file, APIConst.PinType.NORMAL);
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void unpinnedManyLevelsDirCase() throws Exception {
		Logs.d(THIS_CLASS, "unpinnedManyLevelsDirCase", "");
		final String file = localFilesMgr.genFileInLevel(30, 2 * 1024* 1024);
		final String dir = new File(file).getParent();
		HCFSAPI.Result result = HCFSAPI.pin(dir, APIConst.PinType.NORMAL);
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void unpinnedMaxNameLengthFileCase() throws Exception {
		Logs.d(THIS_CLASS, "unpinnedMaxNameLengthFileCase", "");
		final String fileName = "asdddkciefjjfjjccjsuduusudfusdfcusvdscmwecwumwcmwecmiwmcwcvdfvudfvdnvnsasdamdnqwedasicscsdcsdcsgvysdvvnnvsndnnsnvnsndcsdjfwefwefasdasdcaiasiaciasduwqdqwdnccasdcsndcnsdcwecwecewcnwencwnecnanscnasncnanqnwdqdqwdjiwqdjedwdewcscascxcascqwsascasddasdidiqwdiiii";
		final String maxNameFile = localFilesMgr.genFileWithName(fileName, 2 * 1024 * 1024);
		HCFSAPI.Result result = HCFSAPI.pin(maxNameFile, APIConst.PinType.NORMAL);
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void unpinnedMaxNameLengthDirCase() throws Exception {
		Logs.d(THIS_CLASS, "unpinnedMaxNameLengthDirCase", "");
		final String dirName = "asdddkciefjjfjjccjsuduusudfusdfcusvdscmwecwumwcmwecmiwmcwcvdfvudfvdnvnsasdamdnqwedasicscsdcsdcsgvysdvvnnvsndnnsnvnsndcsdjfwefwefasdasdcaiasiaciasduwqdqwdnccasdcsndcnsdcwecwecewcnwencwnecnanscnasncnanqnwdqdqwdjiwqdjedwdewcscascxcascqwsascasddasdidiqwdiiii";
		final String maxNameDir = localFilesMgr.genDirWithName(dirName);
		HCFSAPI.Result result = HCFSAPI.pin(maxNameDir, APIConst.PinType.NORMAL);
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(0, result.data.size());
	}


	@Ignore("Unimplemented")
	@Test
	public void unpinnedSpecialFileCase() throws Exception {
		Logs.d(THIS_CLASS, "abnormalValueSpecialFileCase", "");
//		HCFSAPI.Result result = HCFSAPI.Pin(localFilePath, APIConst.PinType.PRIORITY);
//		assertTrue(result.isSuccess);
//
//		result = HCFSAPI.Pin(localFilePath, APIConst.PinType.PRIORITY);
//		assertTrue(result.isSuccess);
//		assertEquals(0, result.code);
//		assertEquals(0, result.data.size());
	}
}
