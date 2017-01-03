package hcfs.test.testsuite.api.boost;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.hopebaytech.hcfsmgmt.utils.Logs;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import hcfs.test.annotation.Bug;
import hcfs.test.spec.APIConst;
import hcfs.test.spec.Err;
import hcfs.test.utils.AppManager;
import hcfs.test.wrapper.HCFSAPI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;


public class CheckPackageBoostStatus_EnableBoostCases {

	private static final String THIS_CLASS = CheckPackageBoostStatus_EnableBoostCases.class.getSimpleName();

	/**
     * String checkPackageBoostStatus(String package)
	 * <li>package : app package to check boost status</li>
     * <li>True	0 if package is boosted</li>
     * <li>True	1 if package is unboosted</li>
	 * <li>False	Linux errors.</li>
	 */

	private static final long boostSize = 1024 * 1024 * 100;
	private Context mContext;

	@Before
	public void setUp() throws Exception {
		Logs.d(THIS_CLASS, "setUp", "");
		mContext = InstrumentationRegistry.getInstrumentation().getContext();
		// TODO Uncomment when disableBooster implemented
//		HCFSAPI.Result result = HCFSAPI.enableBooster(boostSize);
//		assertTrue(result.isSuccess);
	}

	@After
	public void tearDown() throws Exception {
		Logs.d(THIS_CLASS, "tearDown", "");

		// TODO Uncomment when disableBooster implemented
//		HCFSAPI.Result result = HCFSAPI.disableBooster();
//		assertTrue(result.isSuccess);
	}

	@Test
	public void checkAPISpecCase() throws Exception {
		Logs.d(THIS_CLASS, "checkAPISpecCase", "");
		AppManager appManager = new AppManager(mContext);
		final String testApp = appManager.popOneInstalledApp();

		HCFSAPI.Result result = HCFSAPI.checkPackageBoostStatus(testApp);
		assertTrue(result.isSuccess);
		assertTrue(APIConst.BoostStatus.isValidNum(result.code));
		assertEquals(0, result.data.size());
	}

	@Ignore("Boost app API not test")
	@Test
	public void boostedAppCase() throws Exception {
		Logs.d(THIS_CLASS, "boostedAppCase", "");
		AppManager appManager = new AppManager(mContext);
		final String testApp = appManager.popOneInstalledApp();

		//boost app

		HCFSAPI.Result result = HCFSAPI.checkPackageBoostStatus(testApp);
		assertTrue(result.isSuccess);
		assertEquals(APIConst.BoostStatus.ON, result.code);
		assertEquals(0, result.data.size());
	}

	@Ignore("Boost app API not test")
	@Test
	public void unboostedAppCase() throws Exception {
		Logs.d(THIS_CLASS, "unboostedAppCase", "");
		AppManager appManager = new AppManager(mContext);
		final String testApp = appManager.popOneInstalledApp();

		//unboost app

		HCFSAPI.Result result = HCFSAPI.checkPackageBoostStatus(testApp);
		assertTrue(result.isSuccess);
		assertEquals(APIConst.BoostStatus.OFF, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void systemAppCase() throws Exception {
		Logs.d(THIS_CLASS, "systemAppCase", "");
		final String systemApp = "com.google.android.gms";
		HCFSAPI.Result result = HCFSAPI.checkPackageBoostStatus(systemApp);
		assertTrue(result.isSuccess);
		assertEquals(APIConst.BoostStatus.OFF, result.code);
		assertEquals(0, result.data.size());
	}

	@Test
	public void nonExistsAppCase() throws Exception {
		Logs.d(THIS_CLASS, "nonExistsAppCase", "");
		HCFSAPI.Result result = HCFSAPI.checkPackageBoostStatus("asd");
		assertFalse(result.isSuccess);
		assertEquals(Err.NO_SUCH_FILE, result.code);
		assertEquals(0, result.data.size());
	}

	@Bug("#14541")
	@Ignore
	@Test
	public void emptyStrCase() throws Exception {
		Logs.d(THIS_CLASS, "emptyStrCase", "");
		HCFSAPI.Result result = HCFSAPI.checkPackageBoostStatus("");
		assertFalse(result.isSuccess);
		assertEquals(Err.INVALID_ARG, result.code);
		assertEquals(0, result.data.size());
	}
}
