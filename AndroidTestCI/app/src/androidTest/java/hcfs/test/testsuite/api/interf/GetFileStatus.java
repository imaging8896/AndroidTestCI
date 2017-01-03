package hcfs.test.testsuite.api.interf;

import hcfs.test.spec.APIConst;
import hcfs.test.spec.Err;
import hcfs.test.config.Path;
import hcfs.test.utils.FilesManager;
import hcfs.test.wrapper.HCFSAPI;

import com.hopebaytech.hcfsmgmt.utils.Logs;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class GetFileStatus {

	private static final String THIS_CLASS = GetFileStatus.class.getSimpleName();

	/**	
	 * void HCFS_file_status(char ** json_res, char * pathname	)
	 * To getByKey the status of (pathname).
	 * Return code -	
	 *    True 	0 if the file status is "local"
	 *   				1 if the file status is "cloud"
	 *    				2 if the file status is "hybrid"
	 *    False 	Linux errors.
	 * 
	 * 	json_res		result string in json format.
	 * 	pathname 	target pathname.
	 */

	private FilesManager localFilesMgr;
	private FilesManager sdcardFilesMgr;
	
	@Before
	public void setUp() throws Exception {
		Logs.d(THIS_CLASS, "setUp", "");
		localFilesMgr = new FilesManager(Path.APK_PATH, Path.L_TEST_DIR);
		sdcardFilesMgr = new FilesManager(Path.SDCARD_PATH, Path.S_TEST_DIR);
	}

	@After
	public void tearDown() throws Exception {
		Logs.d(THIS_CLASS, "tearDown", "");
		localFilesMgr.cleanup();
		sdcardFilesMgr.cleanup();
	}

	// TODO page out should be some in local some in cloud
	
	@Test
	public void normalValueLocalFileCase() throws Exception {
		Logs.d(THIS_CLASS, "normalValueLocalFileCase", "");
		final int fileSize = 2 * 1024 * 1024;
		localFilesMgr.genFiles(1, fileSize);
		final String localFilePath = localFilesMgr.getFilePath(1);

		HCFSAPI.Result result = HCFSAPI.getFileStatus(localFilePath);
		assertTrue(result.isSuccess);
		assertEquals(APIConst.FileStatus.LOCAL, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void normalValueManyLevelsLocalFileCase() throws Exception {
		Logs.d(THIS_CLASS, "normalValueManyLevelsLocalFileCase", "");
		final int level = 30;
		final String levelsFile = localFilesMgr.genFileInLevel(level, 2 * 1024 * 1024);
		HCFSAPI.Result result = HCFSAPI.getFileStatus(levelsFile);
		assertTrue(result.isSuccess);
		assertEquals(APIConst.FileStatus.LOCAL, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void normalValueMaxNameLengthLocalFileCase() throws Exception {
		Logs.d(THIS_CLASS, "normalValueMaxNameLengthLocalFileCase", "");
		final String fileName = "asdddkciefjjfjjccjsuduusudfusdfcusvdscmwecwumwcmwecmiwmcwcvdfvudfvdnvnsasdamdnqwedasicscsdcsdcsgvysdvvnnvsndnnsnvnsndcsdjfwefwefasdasdcaiasiaciasduwqdqwdnccasdcsndcnsdcwecwecewcnwencwnecnanscnasncnanqnwdqdqwdjiwqdjedwdewcscascxcascqwsascasddasdidiqwdiiii";
		final String maxNameFile = localFilesMgr.genFileWithName(fileName, 2 * 1024 * 1024);
		HCFSAPI.Result result = HCFSAPI.getFileStatus(maxNameFile);
		assertTrue(result.isSuccess);
		assertEquals(APIConst.FileStatus.LOCAL, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void normalValueSdcardFileCase() throws Exception {
		Logs.d(THIS_CLASS, "normalValueSdcardFileCase", "");
		final int fileSize = 2 * 1024 * 1024;
		sdcardFilesMgr.genFiles(1, fileSize);
		final String sdcardFilePath = sdcardFilesMgr.getFilePath(1);

		HCFSAPI.Result result = HCFSAPI.getFileStatus(sdcardFilePath);
		assertTrue(result.isSuccess);
		assertEquals(APIConst.FileStatus.LOCAL, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void normalValueManyLevelsSdcardFileCase() throws Exception {
		Logs.d(THIS_CLASS, "normalValueManyLevelsSdcardFileCase", "");
		final int level = 30;
		final String levelsFile = sdcardFilesMgr.genFileInLevel(level, 2 * 1024 * 1024);
		HCFSAPI.Result result = HCFSAPI.getFileStatus(levelsFile);
		assertTrue(result.isSuccess);
		assertEquals(APIConst.FileStatus.LOCAL, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void normalValueMaxNameLengthSdcardFileCase() throws Exception {
		Logs.d(THIS_CLASS, "normalValueMaxNameLengthSdcardFileCase", "");
		final String fileName = "asdddkciefjjfjjccjsuduusudfusdfcusvdscmwecwumwcmwecmiwmcwcvdfvudfvdnvnsasdamdnqwedasicscsdcsdcsgvysdvvnnvsndnnsnvnsndcsdjfwefwefasdasdcaiasiaciasduwqdqwdnccasdcsndcnsdcwecwecewcnwencwnecnanscnasncnanqnwdqdqwdjiwqdjedwdewcscascxcascqwsascasddasdidiqwdiiii";
		final String maxNameFile = sdcardFilesMgr.genFileWithName(fileName, 2 * 1024 * 1024);
		HCFSAPI.Result result = HCFSAPI.getFileStatus(maxNameFile);
		assertTrue(result.isSuccess);
		assertEquals(APIConst.FileStatus.LOCAL, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void normalValueDirCase() throws Exception {
		Logs.d(THIS_CLASS, "normalValueDirCase", "");
		final String dirName = "testdir";
		final String dir = sdcardFilesMgr.genDirWithName(dirName);
		HCFSAPI.Result result = HCFSAPI.getFileStatus(dir);
		assertTrue(result.isSuccess);
		assertEquals(APIConst.FileStatus.LOCAL, result.code);
		assertEquals(0, result.data.size());
	}

	@Ignore("Unimplemented")
	@Test
	public void normalValueSpecialFileCase() throws Exception {
		Logs.d(THIS_CLASS, "normalValueSpecialFileCase", "");
		//TODO I have no idea
//		HCFSAPI.Result result = HCFSAPI.getFileStatus(file);
//		assertTrue(result.isSuccess);
//		assertEquals(APIConst.FileStatus.LOCAL, result.code);
//		assertEquals(0, result.data.size());
	}

	@Test
	public void abnormalValueNonExistsLongPathCase() throws Exception {
		Logs.d(THIS_CLASS, "abnormalValueNonExistsLongPathCase", "");
		HCFSAPI.Result result = HCFSAPI.getFileStatus("/FileThat.DoesNotExist");
		assertFalse(result.isSuccess);
		assertEquals(Err.NO_SUCH_FILE, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void abnormalValueNonExistsPathCase() throws Exception {
		Logs.d(THIS_CLASS, "abnormalValueNonExistsPathCase", "");
		HCFSAPI.Result result = HCFSAPI.getFileStatus("/FileThat.DoesNotExist");
		assertFalse(result.isSuccess);
		assertEquals(Err.NO_SUCH_FILE, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void abnormalValueEmptyPathCase() throws Exception {
		Logs.d(THIS_CLASS, "abnormalValueEmptyPathCase", "");
		HCFSAPI.Result result = HCFSAPI.getFileStatus("");
		assertFalse(result.isSuccess);
		assertEquals(Err.NO_SUCH_FILE, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void specialValueSystemFileCase() throws Exception {
		Logs.d(THIS_CLASS, "specialValueSystemFileCase", "");
		HCFSAPI.Result result = HCFSAPI.getFileStatus(Path.SYSTEM_FILE);
		assertFalse(result.isSuccess);
		assertEquals(Err.NO_SUCH_FILE, result.code);
		assertEquals(0, result.data.size());
	}
}
