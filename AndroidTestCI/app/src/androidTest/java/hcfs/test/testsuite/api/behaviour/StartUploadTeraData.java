package hcfs.test.testsuite.api.behaviour;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import hcfs.test.config.Path;
import hcfs.test.spec.APIConst;
import hcfs.test.spec.HCFSEvent;
import hcfs.test.utils.FileUtils;
import hcfs.test.utils.MgmtAppUtils;
import hcfs.test.utils.DeviceUtils;
import hcfs.test.utils.EventSocketManager;
import hcfs.test.utils.FilesManager;
import hcfs.test.wrapper.HCFSAPI;

import com.hopebaytech.hcfsmgmt.utils.Logs;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class StartUploadTeraData {

	private static final String THIS_CLASS = StartUploadTeraData.class.getSimpleName();

	/**
	 * String startUploadTeraData()
	 * <li>1 if system is clean now. That is, there is no dirty data.</li>
     * <li>0 when setting sync point completed.</li>
     * <li>Negative error code in case that error occurs</li>
     * 
     * String stopUploadTeraData()
     * <li>1 if no sync point is set.</li>
     * <li>0 when canceling the setting completed.</li>
     * <li>Negative error code in case that error occurs</li>
	 */

	private Context mContext;

	private EventSocketManager eventSocketMgr;
	
	private FilesManager sdcardFilesMgr;
	private FilesManager localFilesMgr;

	@Before
	public void setUp() throws Exception {
		Logs.i(THIS_CLASS, "setUp", "");
		mContext = InstrumentationRegistry.getInstrumentation().getContext();

		setWIFI(true);
		eventSocketMgr = new EventSocketManager();
		sdcardFilesMgr = new FilesManager(Path.SDCARD_PATH, Path.S_TEST_DIR);
		localFilesMgr = new FilesManager(Path.APK_PATH, Path.L_TEST_DIR);

		HCFSAPI.Result result = HCFSAPI.stopUploadTeraData();
		assertTrue(result.isSuccess);
	}

	@After
	public void tearDown() throws Exception {
		Logs.i(THIS_CLASS, "tearDown", "");
		setWIFI(true);
		
		eventSocketMgr.cleanup();
		sdcardFilesMgr.cleanup();
		localFilesMgr.cleanup();

		Logs.i(THIS_CLASS, "tearDown", "restoreEventNotifyServer");
		MgmtAppUtils.restoreEventNotifyServer();
	}

	@Test
	public void syncCompleteLocalFilesCase() throws Exception {
		Logs.i(THIS_CLASS, "syncCompleteLocalFilesCase", "");
		final String testSocketAddr = "setsyncpoint.local.sock";
		final int testFilesNum = 10;
		final long testFilesSize = 2 * 1024L * 1024L;
		final int waitEventTimeout = 600;
		final int socketId = eventSocketMgr.addAndStartEventSocket(HCFSEvent.UPLOAD_COMPLETED, testSocketAddr);

		HCFSAPI.Result result = HCFSAPI.setNotifyServer(testSocketAddr);
		assertTrue(result.isSuccess);
		localFilesMgr.genFiles(testFilesNum, testFilesSize);
		sleep(2);//Wait file into cache
		result = HCFSAPI.startUploadTeraData();
		assertTrue(result.isSuccess);
		assertEquals(APIConst.SyncState.DIRTY, result.code);
		assertTrue("Timeout when wait event", eventSocketMgr.isReceiveEvent(socketId, waitEventTimeout));
		assertFilesOnCloud(localFilesMgr, 1, testFilesNum);
	}

	@Test
    public void syncCompleteSdcardFilesCase() throws Exception {
		Logs.i(THIS_CLASS, "syncCompleteSdcardFilesCase", "");
		final String testSocketAddr = "setsyncpoint.sdcard.sock";
		final int testFilesNum = 10;
		final long testFilesSize = 2 * 1024L * 1024L;
		final int waitEventTimeout = 600;
		final int socketId = eventSocketMgr.addAndStartEventSocket(HCFSEvent.UPLOAD_COMPLETED, testSocketAddr);

		HCFSAPI.Result result = HCFSAPI.setNotifyServer(testSocketAddr);
		assertTrue(result.isSuccess);
		sdcardFilesMgr.genFiles(testFilesNum, testFilesSize);
		sleep(2);//Wait file into cache
		result = HCFSAPI.startUploadTeraData();
		assertTrue(result.isSuccess);
		assertEquals(APIConst.SyncState.DIRTY, result.code);
		assertTrue("Timeout when wait event", eventSocketMgr.isReceiveEvent(socketId, waitEventTimeout));
		assertFilesOnCloud(sdcardFilesMgr, 1, testFilesNum);
	}

	@Test
	public void syncCompleteLocalAndSdcardFilesCase() throws Exception {
		Logs.i(THIS_CLASS, "syncCompleteLocalAndSdcardFilesCase", "");
		final String testSocketAddr = "setsyncpoint.localsdcard.sock";
		final int testFilesNum = 5;
		final long testFilesSize = 2 * 1024L * 1024L;
		final int waitEventTimeout = 600;
		final int socketId = eventSocketMgr.addAndStartEventSocket(HCFSEvent.UPLOAD_COMPLETED, testSocketAddr);

		HCFSAPI.Result result = HCFSAPI.setNotifyServer(testSocketAddr);
		assertTrue(result.isSuccess);
		localFilesMgr.genFiles(testFilesNum, testFilesSize);
		sdcardFilesMgr.genFiles(testFilesNum, testFilesSize);
		sleep(2);//Wait file into cache
		result = HCFSAPI.startUploadTeraData();
		assertTrue(result.isSuccess);
		assertEquals(APIConst.SyncState.DIRTY, result.code);
		assertTrue("Timeout when wait event", eventSocketMgr.isReceiveEvent(socketId, waitEventTimeout));
		assertFilesOnCloud(localFilesMgr, 1, testFilesNum);
		assertFilesOnCloud(sdcardFilesMgr, 1, testFilesNum);
	}

	@Test
    public void resetSyncPointLocalFilesCase() throws Exception {
		Logs.i(THIS_CLASS, "resetSyncPointLocalFilesCase", "");
		final String testSocketAddr = "resetsyncpoint.local.sock";
		final int testFilesNum = 10;
		final long testFilesSize = 2 * 1024L * 1024L;
		final int waitEventTimeout = 600;
		final int socketId = eventSocketMgr.addAndStartEventSocket(HCFSEvent.UPLOAD_COMPLETED, testSocketAddr);

		HCFSAPI.Result result = HCFSAPI.setNotifyServer(testSocketAddr);
		assertTrue(result.isSuccess);
		localFilesMgr.genFiles(testFilesNum, testFilesSize);
		sleep(2);//Wait file into cache
		result = HCFSAPI.startUploadTeraData();
		assertTrue(result.isSuccess);
		assertEquals(APIConst.SyncState.DIRTY, result.code);

		localFilesMgr.continueGenFiles(testFilesNum, testFilesSize);
		sleep(2);//Wait file into cache
		result = HCFSAPI.startUploadTeraData();
		assertTrue(result.isSuccess);
		assertEquals(APIConst.SyncState.DIRTY, result.code);
		assertTrue("Timeout when wait event", eventSocketMgr.isReceiveEvent(socketId, waitEventTimeout));
		assertFilesOnCloud(localFilesMgr, 1, testFilesNum * 2);
	}

	@Test
    public void resetSyncPointSdcardFilesCase() throws Exception {
		Logs.i(THIS_CLASS, "resetSyncPointSdcardFilesCase", "");
		final String testSocketAddr = "resetsyncpoint.sdcard.sock";
		final int testFilesNum = 5;
		final long testFilesSize = 2 * 1024L * 1024L;
		final int waitEventTimeout = 600;
		final int socketId = eventSocketMgr.addAndStartEventSocket(HCFSEvent.UPLOAD_COMPLETED, testSocketAddr);

		HCFSAPI.Result result = HCFSAPI.setNotifyServer(testSocketAddr);
		assertTrue(result.isSuccess);
		sdcardFilesMgr.genFiles(testFilesNum, testFilesSize);
		sleep(2);//Wait file into cache
		result = HCFSAPI.startUploadTeraData();
		assertTrue(result.isSuccess);
		assertEquals(APIConst.SyncState.DIRTY, result.code);

		sdcardFilesMgr.continueGenFiles(testFilesNum, testFilesSize);
		sleep(2);//Wait file into cache
		result = HCFSAPI.startUploadTeraData();
		assertTrue(result.isSuccess);
		assertEquals(APIConst.SyncState.DIRTY, result.code);
		assertTrue("Timeout when wait event", eventSocketMgr.isReceiveEvent(socketId, waitEventTimeout));
		assertFilesOnCloud(sdcardFilesMgr, 1, testFilesNum * 2);
	}

	@Test
    public void modifyDataLocalFilesCase() throws Exception {
		Logs.i(THIS_CLASS, "modifyDataLocalFilesCase", "");
		final String testSocketAddr = "setsyncpoint.modifylocal.sock";
		final int testFilesNum = 50;
		final long testFilesSize = 2 * 1024L * 1024L;
		final int waitEventTimeout = 600;
		final int socketId = eventSocketMgr.addAndStartEventSocket(HCFSEvent.UPLOAD_COMPLETED, testSocketAddr);

		HCFSAPI.Result result = HCFSAPI.setNotifyServer(testSocketAddr);
		assertTrue(result.isSuccess);
		localFilesMgr.genFiles(testFilesNum, testFilesSize);
		sleep(2);//Wait file into cache
		result = HCFSAPI.startUploadTeraData();
		assertTrue(result.isSuccess);
		assertEquals(APIConst.SyncState.DIRTY, result.code);
		localFilesMgr.modifyFilesBetween(26, 50, FilesManager.DATA2);
		assertTrue("Timeout when wait event", eventSocketMgr.isReceiveEvent(socketId, waitEventTimeout));
		assertFilesOnCloud(localFilesMgr, 1, testFilesNum / 2);
	}

	@Test
    public void modifyDataSdcardFilesCase() throws Exception {
		Logs.i(THIS_CLASS, "modifyDataSdcardFilesCase", "");
		final String testSocketAddr = "setsyncpoint.modifysdcard.sock";
		final int testFilesNum = 50;
		final long testFilesSize = 2 * 1024L * 1024L;
		final int waitEventTimeout = 600;
		final int socketId = eventSocketMgr.addAndStartEventSocket(HCFSEvent.UPLOAD_COMPLETED, testSocketAddr);

		HCFSAPI.Result result = HCFSAPI.setNotifyServer(testSocketAddr);
		assertTrue(result.isSuccess);
		sdcardFilesMgr.genFiles(testFilesNum, testFilesSize);
		sleep(2);//Wait file into cache
		result = HCFSAPI.startUploadTeraData();
		assertTrue(result.isSuccess);
		assertEquals(APIConst.SyncState.DIRTY, result.code);
		sdcardFilesMgr.modifyFilesBetween(26, 50, FilesManager.DATA2);
		assertTrue("Timeout when wait event", eventSocketMgr.isReceiveEvent(socketId, waitEventTimeout));
		assertFilesOnCloud(sdcardFilesMgr, 1, testFilesNum / 2);
	}

	@Test
    public void wifiInterruptLocalFilesCase() throws Exception {
		Logs.i(THIS_CLASS, "wifiInterruptLocalFilesCase", "");
		final String testSocketAddr = "setsyncpoint.net.local.sock";
		final int internetOffTime = 180;//seconds
		final int testFilesNum = 50;
		final long testFilesSize = 2 * 1024L * 1024L;
		final int waitEventTimeout = 600;
		final int socketId = eventSocketMgr.addAndStartEventSocket(HCFSEvent.UPLOAD_COMPLETED, testSocketAddr);

		HCFSAPI.Result result = HCFSAPI.setNotifyServer(testSocketAddr);
		assertTrue(result.isSuccess);
		localFilesMgr.genFiles(testFilesNum, testFilesSize);
		sleep(2);//Wait file into cache
		result = HCFSAPI.startUploadTeraData();
		assertTrue(result.isSuccess);
		assertEquals(APIConst.SyncState.DIRTY, result.code);

		sleep(5);//Wait some files upload
		setWIFI(false);
		sleep(internetOffTime);
		setWIFI(true);
		assertTrue("Timeout when wait event", eventSocketMgr.isReceiveEvent(socketId, waitEventTimeout));
		assertFilesOnCloud(localFilesMgr, 1, testFilesNum);
	}

	@Test
    public void wifiInterruptSdcardFilesCase() throws Exception {
		Logs.i(THIS_CLASS, "wifiInterruptSdcardFilesCase", "");
		final String testSocketAddr = "setsyncpoint.net.sdcard.sock";
		final int internetOffTime = 180;//seconds
		final int testFilesNum = 50;
		final long testFilesSize = 2 * 1024L * 1024L;
		final int waitEventTimeout = 600;
		final int socketId = eventSocketMgr.addAndStartEventSocket(HCFSEvent.UPLOAD_COMPLETED, testSocketAddr);

		HCFSAPI.Result result = HCFSAPI.setNotifyServer(testSocketAddr);
		assertTrue(result.isSuccess);
		sdcardFilesMgr.genFiles(testFilesNum, testFilesSize);
		sleep(2);//Wait file into cache
		result = HCFSAPI.startUploadTeraData();
		assertTrue(result.isSuccess);
		assertEquals(APIConst.SyncState.DIRTY, result.code);

		sleep(5);//Wait some files upload
		setWIFI(false);
		sleep(internetOffTime);
		setWIFI(true);
		assertTrue("Timeout when wait event", eventSocketMgr.isReceiveEvent(socketId, waitEventTimeout));
		assertFilesOnCloud(sdcardFilesMgr, 1, testFilesNum);
	}

	private void setWIFI(boolean enable) {
		DeviceUtils.setWifiEnabled(mContext, enable);
		assertEquals(enable, DeviceUtils.isInternetConnected(mContext));
	}

	private void sleep(int timeoutSec) {
		try {Thread.sleep(timeoutSec * 1000);} catch (InterruptedException e) {throw new RuntimeException(e);}
	}
	private void assertFilesOnCloud(FilesManager filesMgr, int from, int to) throws Exception {
		assertTrue("'from' must less than or equal to 'to'", from <= to);
		assertTrue("'from' must be positive number", from > 0);
		assertTrue("Invalid file name range", to <= filesMgr.getMaxFileNum());
		for(int i = from; i <= to; i++)
			assertFalse(FileUtils.isDirty(filesMgr.getFilePath(i)));

	}
}
