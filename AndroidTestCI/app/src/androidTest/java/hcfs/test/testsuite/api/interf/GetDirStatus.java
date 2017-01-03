package hcfs.test.testsuite.api.interf;

import hcfs.test.annotation.Bug;
import hcfs.test.spec.Err;
import hcfs.test.config.Path;
import hcfs.test.spec.DirStatus;
import hcfs.test.utils.FilesManager;
import hcfs.test.wrapper.HCFSAPI;

import static org.junit.Assert.*;

import com.hopebaytech.hcfsmgmt.utils.Logs;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;

public class GetDirStatus {

	private static final String THIS_CLASS = GetDirStatus.class.getSimpleName();

	/**	
	 * void HCFS_dir_status(char ** json_res ,char * pathname	)
	 * To getByKey the status of (pathname).
	 * 
	 * Return data dict in json_res -
	 * data: {
	 *     num_local: num,
	 *     num_cloud: num,
	 *     num_hybrid: num,
	 * }
	 * Return code -	
	 *    True 	0
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
	public void normalValueLocalDirCase() throws Exception {
		Logs.d(THIS_CLASS, "normalValueLocalDirCase", "");
		final int fileNum = 20;
		final int fileSize = 2 * 1024 * 1024;
		localFilesMgr.genFiles(fileNum, fileSize);
		final String localDirPath = localFilesMgr.getRootPath();

		HCFSAPI.Result result = HCFSAPI.getDirStatus(localDirPath);
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(3, result.data.size());

		int total = (int) result.data.get(DirStatus.NUM_LOCAL) +
				(int) result.data.get(DirStatus.NUM_CLOUD) +
				(int) result.data.get(DirStatus.NUM_HYBRID);
		assertEquals(fileNum, total);
	}

	@Test
	public void normalValueManyLevelsLocalDirCase() throws Exception {
		Logs.d(THIS_CLASS, "normalValueManyLevelsLocalDirCase", "");
		final int level = 30;
		final String file = localFilesMgr.genFileInLevel(level, 2 * 1024 * 1024);
		final String levelsDir = new File(file).getParent();
		HCFSAPI.Result result = HCFSAPI.getDirStatus(levelsDir);
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(3, result.data.size());

		int total = (int) result.data.get(DirStatus.NUM_LOCAL) +
				(int) result.data.get(DirStatus.NUM_CLOUD) +
				(int) result.data.get(DirStatus.NUM_HYBRID);
		assertEquals(1, total);
	}

	@Test
	public void normalValueMaxNameLengthLocalDirCase() throws Exception {
		Logs.d(THIS_CLASS, "normalValueMaxNameLengthLocalDirCase", "");
		final String dirname = "asdddkciefjjfjjccjsuduusudfusdfcusvdscmwecwumwcmwecmiwmcwcvdfvudfvdnvnsasdamdnqwedasicscsdcsdcsgvysdvvnnvsndnnsnvnsndcsdjfwefwefasdasdcaiasiaciasduwqdqwdnccasdcsndcnsdcwecwecewcnwencwnecnanscnasncnanqnwdqdqwdjiwqdjedwdewcscascxcascqwsascasddasdidiqwdiiii";
		final String maxNameDir = localFilesMgr.genDirWithName(dirname);
		HCFSAPI.Result result = HCFSAPI.getDirStatus(maxNameDir);
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(3, result.data.size());

		int total = (int) result.data.get(DirStatus.NUM_LOCAL) +
				(int) result.data.get(DirStatus.NUM_CLOUD) +
				(int) result.data.get(DirStatus.NUM_HYBRID);
		assertEquals(0, total);
	}

	@Test
	public void normalValueSdcardDirCase() throws Exception {
		Logs.d(THIS_CLASS, "normalValueSdcardDirCase", "");
		final int fileNum = 20;
		final int fileSize = 2 * 1024 * 1024;
		sdcardFilesMgr.genFiles(fileNum, fileSize);
		final String sdcardDirPath = sdcardFilesMgr.getRootPath();

		HCFSAPI.Result result = HCFSAPI.getDirStatus(sdcardDirPath);
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(3, result.data.size());

		int total = (int) result.data.get(DirStatus.NUM_LOCAL) +
				(int) result.data.get(DirStatus.NUM_CLOUD) +
				(int) result.data.get(DirStatus.NUM_HYBRID);
		assertEquals(fileNum, total);
	}

	@Test
	public void normalValueManyLevelsSdcardDirCase() throws Exception {
		Logs.d(THIS_CLASS, "normalValueManyLevelsSdcardDirCase", "");
		final int level = 30;
		final String file = sdcardFilesMgr.genFileInLevel(level, 2 * 1024 * 1024);
		final String levelsDir = new File(file).getParent();
		HCFSAPI.Result result = HCFSAPI.getDirStatus(levelsDir);
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(3, result.data.size());

		int total = (int) result.data.get(DirStatus.NUM_LOCAL) +
				(int) result.data.get(DirStatus.NUM_CLOUD) +
				(int) result.data.get(DirStatus.NUM_HYBRID);
		assertEquals(1, total);
	}

	@Test
	public void normalValueMaxNameLengthSdcardDirCase() throws Exception {
		Logs.d(THIS_CLASS, "normalValueMaxNameLengthLocalDirCase", "");
		final String dirname = "asdddkciefjjfjjccjsuduusudfusdfcusvdscmwecwumwcmwecmiwmcwcvdfvudfvdnvnsasdamdnqwedasicscsdcsdcsgvysdvvnnvsndnnsnvnsndcsdjfwefwefasdasdcaiasiaciasduwqdqwdnccasdcsndcnsdcwecwecewcnwencwnecnanscnasncnanqnwdqdqwdjiwqdjedwdewcscascxcascqwsascasddasdidiqwdiiii";
		final String maxNameDir = sdcardFilesMgr.genDirWithName(dirname);
		HCFSAPI.Result result = HCFSAPI.getDirStatus(maxNameDir);
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(3, result.data.size());

		int total = (int) result.data.get(DirStatus.NUM_LOCAL) +
				(int) result.data.get(DirStatus.NUM_CLOUD) +
				(int) result.data.get(DirStatus.NUM_HYBRID);
		assertEquals(0, total);
	}

	@Bug("#14016")
	@Ignore
	@Test
	public void abnormalValueFileCase() throws Exception {
		Logs.d(THIS_CLASS, "abnormalValueFileCase", "");
		final String fileName = "test.file";
		final String file = sdcardFilesMgr.genFileWithName(fileName, 2 * 1024 * 1024);
		HCFSAPI.Result result = HCFSAPI.getDirStatus(file);
		assertFalse(result.isSuccess);
		assertEquals(Err.NOT_DIR, result.code);
		assertEquals(0, result.data.size());
	}

	@Ignore("Unimplemented")
	@Test
	public void abnormalValueSpecialFileCase() throws Exception {
		Logs.d(THIS_CLASS, "abnormalValueSpecialFileCase", "");
		//TODO I have no idea
//		HCFSAPI.Result result = HCFSAPI.getDirStatus(file);
//		assertFalse(result.isSuccess);
//		assertEquals(Err.NO_SUCH_FILE, result.code);
//		assertEquals(0, result.data.size());
	}

	@Test
	public void abnormalValueNonExistsLongPathCase() throws Exception {
		Logs.d(THIS_CLASS, "abnormalValueNonExistsLongPathCase", "");
		final String path = "asdasdadfewfwvwevrvreverververvrveververververveveververververvev/d/da/d/as/d/dad/as/d/asd/asd/asdasd/as/da/sd/a/sd/as/d/as/d/asd/as/da/sd/as/dddasdasfvsfcecfevgvvgregevcrgergxergervgergvregbervgecxercgfvervgergvcxwwexwfxqww/dq/f/f/wfef/wefewefwefwefwefw/ef/wf/ewf//ewf/ewf/we/f/e";
		HCFSAPI.Result result = HCFSAPI.getDirStatus(path);
		assertFalse(result.isSuccess);
		assertEquals(Err.NO_SUCH_FILE, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void abnormalValueNonExistsPathCase() throws Exception {
		Logs.d(THIS_CLASS, "abnormalValueNonExistsPathCase", "");
		HCFSAPI.Result result = HCFSAPI.getDirStatus("/DirThatDoesntExist");
		assertFalse(result.isSuccess);
		assertEquals(Err.NO_SUCH_FILE, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void abnormalValueEmptyPathCase() throws Exception {
		Logs.d(THIS_CLASS, "abnormalValueEmptyPathCase", "");
		HCFSAPI.Result result = HCFSAPI.getDirStatus("");
		assertFalse(result.isSuccess);
		assertEquals(Err.NO_SUCH_FILE, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void specialValueSystemDirCase() throws Exception {
		Logs.d(THIS_CLASS, "specialValueSystemDirCase", "");
		HCFSAPI.Result result = HCFSAPI.getDirStatus(Path.SYSTEM_DIR);
		assertFalse(result.isSuccess);
		assertEquals(Err.NO_SUCH_FILE, result.code);
		assertEquals(0, result.data.size());
	}
}
