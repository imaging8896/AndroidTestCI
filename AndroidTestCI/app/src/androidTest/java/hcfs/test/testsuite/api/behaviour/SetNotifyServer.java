package hcfs.test.testsuite.api.behaviour;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import hcfs.test.spec.HCFSEvent;
import hcfs.test.utils.DeviceUtils;
import hcfs.test.utils.EventSocketManager;
import hcfs.test.utils.MgmtAppUtils;
import hcfs.test.wrapper.HCFSAPI;

import com.hopebaytech.hcfsmgmt.utils.Logs;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class SetNotifyServer {

	private static final String THIS_CLASS = SetNotifyServer.class.getSimpleName();

	/**
	 * void HCFS_set_notify_location(char ** json_res, char *server_loc) Setup
	 * notification server. Return code - True 0 False Linux errors.
	 * 
	 * json_res result string in json format. server_loc linux abstract
	 * namespace.
	 */

	private Context mContext;
	
	private EventSocketManager eventSocketMgr;

	@Before
	public void setUp() throws Exception {
		Logs.i(THIS_CLASS, "setUp", "");
		mContext = InstrumentationRegistry.getInstrumentation().getContext();

		setWIFI(true);
		
		eventSocketMgr = new EventSocketManager();
	}

	@After
	public void tearDown() throws Exception {
		Logs.i(THIS_CLASS, "tearDown", "");
		eventSocketMgr.cleanup();
		Logs.i(THIS_CLASS, "tearDown", "restoreEventNotifyServer");
		MgmtAppUtils.restoreEventNotifyServer();
	}

	@Test
	public void connectedEventCase() throws Exception {
		Logs.i(THIS_CLASS, "connectedEventCase", "");
		final String testSocketAddr = "test.connect.event.sock";
		final int socketId = eventSocketMgr.addAndStartEventSocket(HCFSEvent.CONNECTED, testSocketAddr);

		HCFSAPI.Result result = HCFSAPI.setNotifyServer(testSocketAddr);
		assertTrue(result.isSuccess);
		assertTrue("Time out while waiting for event.", eventSocketMgr.isReceiveEvent(socketId, 10));
	}

	@Test
	public void syncAllFinishedEventCase() throws Exception {
		Logs.i(THIS_CLASS, "syncAllFinishedEventCase", "");
		final String testSocketAddr = "test.sync.finish.event.sock";
		final int socketId = eventSocketMgr.addAndStartEventSocket(HCFSEvent.UPLOAD_COMPLETED, testSocketAddr);

		HCFSAPI.Result result = HCFSAPI.setNotifyServer(testSocketAddr);
		assertTrue(result.isSuccess);

		result = HCFSAPI.startUploadTeraData();
		assertTrue(result.isSuccess);
		assertTrue("Time out while waiting for event.", eventSocketMgr.isReceiveEvent(socketId, 180));
	}

	private void setWIFI(boolean enable) {
		DeviceUtils.setWifiEnabled(mContext, enable);
		assertEquals(enable, DeviceUtils.isInternetConnected(mContext));
	}
}
