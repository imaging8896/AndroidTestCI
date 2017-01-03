package hcfs.test.testsuite.api.behaviour;

import static org.junit.Assert.*;

import hcfs.test.spec.APIConst;
import hcfs.test.spec.Err;
import hcfs.test.config.Path;
import hcfs.test.spec.HCFSStat;
import hcfs.test.utils.FilesManager;
import hcfs.test.wrapper.HCFSAPI;

import com.hopebaytech.hcfsmgmt.utils.Logs;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class Pin {

	private static final String THIS_CLASS = Pin.class.getSimpleName();

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
	
	private static final long FILE_SIZE = 1024L * 1024L *1024L;//1G

	private FilesManager sdcardFilesMgt;
	private FilesManager localFilesMgt;

	private long pinMax;

	@Before
	public void setUp() throws Exception {
		Logs.i(THIS_CLASS, "setUp", "");
		HCFSAPI.Result result = HCFSAPI.getHCFSStat();
		pinMax = (long) result.data.get(HCFSStat.PIN_SPACE_MAX);
		Logs.i(THIS_CLASS, "setUp", "pinMax=" + pinMax);
		sdcardFilesMgt = new FilesManager(Path.SDCARD_PATH, Path.S_TEST_DIR);
		localFilesMgt = new FilesManager(Path.APK_PATH, Path.L_TEST_DIR);
	}

	@After
	public void tearDown() throws Exception {
		Logs.i(THIS_CLASS, "tearDown", "");
		sdcardFilesMgt.cleanup();
		localFilesMgt.cleanup();
	}
	
	@Test
	public void pinFilesOverPinSpaceSdcardCase() throws Exception {
		final int fileNum = (int)(pinMax / FILE_SIZE + 1);
		Logs.i(THIS_CLASS, "pinFilesOverPinSpaceSdcardCase", "gen  <" + fileNum + "> size <" + FILE_SIZE + ">");

		sdcardFilesMgt.genFiles(fileNum, FILE_SIZE);
		boolean canPin = true;
		for(int i = 1; i <= fileNum; i++) {
			String filePath = sdcardFilesMgt.getFilePath(i);
			HCFSAPI.Result result = HCFSAPI.pin(filePath, APIConst.PinType.NORMAL);
			assertEquals(0, result.data.size());
			if(!result.isSuccess) {
				canPin = false;
				assertEquals(Err.NO_SPACE, result.code);
				break;
			} else
				assertEquals(0, result.code);
		}

		assertTrue(!canPin);
	}

	@Ignore("Default app pinned")
	@Test
	public void pinFilesOverPinSpaceLocalCase() throws Exception {
		final int fileNum = (int)(pinMax / FILE_SIZE + 1);
		Logs.i(THIS_CLASS, "pinFilesOverPinSpaceLocalCase", "gen  <" + fileNum + "> size <" + FILE_SIZE + ">");

		localFilesMgt.genFiles(fileNum, FILE_SIZE);
		boolean canPin = true;
		for(int i = 1; i <= fileNum; i++) {
			String filePath = localFilesMgt.getFilePath(i);
			HCFSAPI.Result result = HCFSAPI.pin(filePath, APIConst.PinType.NORMAL);
			assertEquals(0, result.data.size());
			if(!result.isSuccess) {
				canPin = false;
				assertEquals(Err.NO_SPACE, result.code);
				break;
			} else
				assertEquals(0, result.code);
		}

		assertTrue(!canPin);
	}

	@Test
	public void pinOneFileOverPinSpaceSdcardCase() throws Exception {
		final long exceed_size = 1024 * 1024;//1M
		Logs.i(THIS_CLASS, "pinOneFileOverPinSpaceSdcardCase", "gen size <" + pinMax + exceed_size + ">");

		sdcardFilesMgt.genFiles(1, pinMax + exceed_size);
		String filePath = sdcardFilesMgt.getFilePath(1);
		HCFSAPI.Result result = HCFSAPI.pin(filePath, APIConst.PinType.NORMAL);
		assertFalse(result.isSuccess);
		assertEquals(Err.NO_SPACE, result.code);
		assertEquals(0, result.data.size());
	}

	@Ignore("Default app pinned")
	@Test
	public void pinOneFileOverPinSpaceLocalCase() throws Exception {
		final long exceed_size = 1024 * 1024;//1M
		Logs.i(THIS_CLASS, "pinLocalOneFileOverPinSpaceCase", "gen size <" + pinMax + exceed_size + ">");

		localFilesMgt.genFiles(1, pinMax + exceed_size);
		String filePath = localFilesMgt.getFilePath(1);
		HCFSAPI.Result result = HCFSAPI.pin(filePath, APIConst.PinType.NORMAL);
		assertFalse(result.isSuccess);
		assertEquals(Err.NO_SPACE, result.code);
		assertEquals(0, result.data.size());
	}
}
