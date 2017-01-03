package hcfs.test.testsuite.api.boost;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.hopebaytech.hcfsmgmt.utils.Logs;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import hcfs.test.annotation.Bug;
import hcfs.test.spec.Err;
import hcfs.test.utils.AppManager;
import hcfs.test.utils.MgmtAppDBUtils;
import hcfs.test.utils.db.info.UidInfoAdapter;
import hcfs.test.wrapper.HCFSAPI;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class EnableBooster {

    private static final String THIS_CLASS = EnableBooster.class.getSimpleName();

    /**
     * String enableBooster(long size)
     * <li>size : initial size of smart cache</li>
     * <li>True	0</li>
     * <li>False	Linux errors.</li>
     */

    @Before
    public void setUp() throws Exception {
        Logs.d(THIS_CLASS, "setUp", "");
        //TODO wait for disable API implementation
//        HCFSAPI.Result result = HCFSAPI.disableBooster();
//        assertTrue(result.isSuccess);
    }

    @After
    public void tearDown() throws Exception {
        Logs.d(THIS_CLASS, "tearDown", "");
        //TODO wait for disable API implementation
//        HCFSAPI.Result result = HCFSAPI.disableBooster();
//        assertTrue(result.isSuccess);
    }

    @Ignore("Disable boost is not implemented")
    @Test
    public void checkAPISpecCase() throws Exception {
        Logs.d(THIS_CLASS, "checkAPISpecCase", "");
        final long boostSize = 1024 * 1024 * 100;
        HCFSAPI.Result result = HCFSAPI.enableBooster(boostSize);
        assertTrue(result.isSuccess);
        assertEquals(0, result.code);
        assertEquals(0, result.data.size());
    }

    @Ignore("Disable boost is not implemented")
    @Test
    public void doubleCallCase() throws Exception {
        Logs.d(THIS_CLASS, "doubleCallCase", "");
        final long boostSize = 1024 * 1024 * 100;
        HCFSAPI.Result result = HCFSAPI.enableBooster(boostSize);
        assertTrue(result.isSuccess);
        assertEquals(0, result.code);
        assertEquals(0, result.data.size());

        result = HCFSAPI.enableBooster(boostSize);
        assertFalse(result.isSuccess);
        assertEquals(Err.OPERATION_NOT_PERMITTED, result.code);
        assertEquals(0, result.data.size());
    }

    @Ignore("Disable boost is not implemented")
    @Test
    public void sizeMoreThanPhoneCase() throws Exception {
        Logs.d(THIS_CLASS, "sizeMoreThanPhoneCase", "");
        final long boostSize = 1024L * 1024L * 1024L * 15L;
        HCFSAPI.Result result = HCFSAPI.enableBooster(boostSize);
        assertFalse(result.isSuccess);
        assertEquals(Err.OPERATION_NOT_PERMITTED, result.code);
        assertEquals(0, result.data.size());
    }

    @Ignore("Disable boost is not implemented")
    @Test
    public void zeroSizeCase() throws Exception {
        Logs.d(THIS_CLASS, "zeroSizeCase", "");
        HCFSAPI.Result result = HCFSAPI.enableBooster(0);
        assertFalse(result.isSuccess);
        assertEquals(Err.OPERATION_NOT_PERMITTED, result.code);
        assertEquals(0, result.data.size());
    }

    @Ignore("Disable boost is not implemented")
    @Test
    public void negativeSizeCase() throws Exception {
        Logs.d(THIS_CLASS, "negativeSizeCase", "");
        HCFSAPI.Result result = HCFSAPI.enableBooster(-6);
        assertFalse(result.isSuccess);
        assertEquals(Err.OPERATION_NOT_PERMITTED, result.code);
        assertEquals(0, result.data.size());
    }

    @Bug("#14473")
    @Ignore
    @Test
    public void immediatelyTriggerBoostCase() throws Exception {
        Logs.d(THIS_CLASS, "immediatelyTriggerBoostCase", "");
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        String testAppPkg = new AppManager(context).popOneInstalledApp();
        HCFSAPI.Result result = HCFSAPI.enableBooster(100 * 1024 *1024);
        assertTrue(result.isSuccess);
        assertEquals(0, result.code);
        assertEquals(0, result.data.size());
        new MgmtAppDBUtils(context).setAppBoostStatus(testAppPkg, UidInfoAdapter.BoostStatus.INIT_BOOST);
        result = HCFSAPI.triggerBoost();
        assertTrue(result.isSuccess);
        assertEquals(0, result.code);
        assertEquals(0, result.data.size());
    }
}
