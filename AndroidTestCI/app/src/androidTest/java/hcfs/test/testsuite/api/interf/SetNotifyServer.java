package hcfs.test.testsuite.api.interf;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.hopebaytech.hcfsmgmt.utils.Logs;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import hcfs.test.spec.Err;
import hcfs.test.spec.HCFSEvent;
import hcfs.test.utils.DeviceUtils;
import hcfs.test.utils.EventSocketManager;
import hcfs.test.utils.MgmtAppUtils;
import hcfs.test.wrapper.HCFSAPI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
		Logs.d(THIS_CLASS, "setUp", "");
		mContext = InstrumentationRegistry.getInstrumentation().getContext();

		setWIFI(true);
		eventSocketMgr = new EventSocketManager();
	}

	@After
	public void tearDown() throws Exception {
		Logs.d(THIS_CLASS, "tearDown", "");
		eventSocketMgr.cleanup();
		Logs.d(THIS_CLASS, "tearDown", "restoreEventNotifyServer");
		MgmtAppUtils.restoreEventNotifyServer();
		setWIFI(true);
	}

	@Test
	public void normalValueMgmtNotifyServerCase() throws Exception {
		Logs.d(THIS_CLASS, "normalValueMgmtNotifyServerCase", "");
		HCFSAPI.Result result = HCFSAPI.setNotifyServer(MgmtAppUtils.EVENT_SERVER_SOCKET_ADDRESS);
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void normalValueValidSocketCase() throws Exception {
		Logs.d(THIS_CLASS, "normalValueValidSocketCase", "");
		final String testSocketAddr = "test.sync.finish.event.sock";
		final int socketId = eventSocketMgr.addAndStartEventSocket(HCFSEvent.CONNECTED, testSocketAddr);

		HCFSAPI.Result result = HCFSAPI.setNotifyServer(testSocketAddr);
		assertTrue(result.isSuccess);
		assertEquals(0, result.code);
		assertEquals(0, result.data.size());
		assertTrue(eventSocketMgr.isReceiveEvent(socketId, 60));
	}

	@Test
	public void abnormalValueNonExistsSocketAddressCase() throws Exception {
		Logs.d(THIS_CLASS, "abnormalValueNonExistsSocketAddressCase", "");
		final String testSocketAddr = "test.nonExist.event.sock";
		HCFSAPI.Result result = HCFSAPI.setNotifyServer(testSocketAddr);
		assertFalse(result.isSuccess);
		assertEquals(Err.CONNECTION_REFUSED, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void abnormalValueEmptySocketAddressCase() throws Exception {
		Logs.d(THIS_CLASS, "abnormalValueEmptySocketAddressCase", "");
		HCFSAPI.Result result = HCFSAPI.setNotifyServer("");
		assertFalse(result.isSuccess);
		assertEquals(Err.CONNECTION_REFUSED, result.code);
		assertEquals(0, result.data.size());
	}

	private void setWIFI(boolean enable) {
		DeviceUtils.setWifiEnabled(mContext, enable);
		assertEquals(enable, DeviceUtils.isInternetConnected(mContext));
	}
}
