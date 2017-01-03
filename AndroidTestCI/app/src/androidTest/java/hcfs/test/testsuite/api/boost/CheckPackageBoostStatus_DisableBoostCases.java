package hcfs.test.testsuite.api.boost;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.hopebaytech.hcfsmgmt.utils.Logs;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import hcfs.test.spec.APIConst;
import hcfs.test.spec.Err;
import hcfs.test.utils.AppManager;
import hcfs.test.wrapper.HCFSAPI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class CheckPackageBoostStatus_DisableBoostCases {

	private static final String THIS_CLASS = CheckPackageBoostStatus_DisableBoostCases.class.getSimpleName();

	/**
     * String checkPackageBoostStatus(String package)
	 * <li>package : app package to check boost status</li>
     * <li>True	0 if package is boosted</li>
     * <li>True	1 if package is unboosted</li>
	 * <li>False	Linux errors.</li>
	 */

	private Context mContext;

	@Before
	public void setUp() throws Exception {
		Logs.d(THIS_CLASS, "setUp", "");
		mContext = InstrumentationRegistry.getInstrumentation().getContext();

		HCFSAPI.Result result = HCFSAPI.disableBooster();
		assertTrue(result.isSuccess);
	}

	@After
	public void tearDown() throws Exception {
		Logs.d(THIS_CLASS, "tearDown", "");
	}

	@Ignore("Disable boost is not implemented")
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

	@Ignore("Disable boost is not implemented")
	@Test
	public void systemAppCase() throws Exception {
		Logs.d(THIS_CLASS, "systemAppCase", "");
		final String systemApp = "com.google.android.gms";
		HCFSAPI.Result result = HCFSAPI.checkPackageBoostStatus(systemApp);
		assertTrue(result.isSuccess);
		assertEquals(APIConst.BoostStatus.OFF, result.code);
		assertEquals(0, result.data.size());
	}

	@Ignore("Disable boost is not implemented")
	@Test
	public void nonExistsAppCase() throws Exception {
		Logs.d(THIS_CLASS, "nonExistsAppCase", "");
		HCFSAPI.Result result = HCFSAPI.checkPackageBoostStatus("asd");
		assertFalse(result.isSuccess);
		assertEquals(Err.INVALID_ARG, result.code);
		assertEquals(0, result.data.size());
	}

	@Ignore("Disable boost is not implemented")
	@Test
	public void emptyStrCase() throws Exception {
		Logs.d(THIS_CLASS, "emptyStrCase", "");
		HCFSAPI.Result result = HCFSAPI.checkPackageBoostStatus("");
		assertFalse(result.isSuccess);
		assertEquals(Err.INVALID_ARG, result.code);
		assertEquals(0, result.data.size());
	}
}
