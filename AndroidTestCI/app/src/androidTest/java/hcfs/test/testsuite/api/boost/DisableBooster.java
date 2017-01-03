package hcfs.test.testsuite.api.boost;

import com.hopebaytech.hcfsmgmt.utils.Logs;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import hcfs.test.wrapper.HCFSAPI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class DisableBooster {

    private static final String THIS_CLASS = DisableBooster.class.getSimpleName();

    /**
     * String disableBooster()
     * <li>True	0</li>
     * <li>False	Linux errors.</li>
     */

    @Before
    public void setUp() throws Exception {
        Logs.d(THIS_CLASS, "setUp", "");
        HCFSAPI.Result result = HCFSAPI.disableBooster();
        assertTrue(result.isSuccess);
    }

    @After
    public void tearDown() throws Exception {
        Logs.d(THIS_CLASS, "tearDown", "");
        HCFSAPI.Result result = HCFSAPI.disableBooster();
        assertTrue(result.isSuccess);
    }

    @Ignore("disableBooster not implemented")
    @Test
    public void checkAPISpecCase() throws Exception {
        Logs.d(THIS_CLASS, "checkAPISpecCase", "");
        HCFSAPI.Result result = HCFSAPI.disableBooster();
        assertTrue(result.isSuccess);
        assertEquals(0, result.code);
        assertEquals(0, result.data.size());
    }

    @Ignore("disableBooster not implemented")
    @Test
    public void multiTimesCase() throws Exception {
        Logs.d(THIS_CLASS, "multiTimesCase", "");
        final int times = 10;
        HCFSAPI.Result result = HCFSAPI.disableBooster();
        assertTrue(result.isSuccess);
        assertEquals(0, result.code);
        assertEquals(0, result.data.size());
        for(int i = 0; i < times; i++) {
            result = HCFSAPI.disableBooster();
            assertTrue(result.isSuccess);
            assertEquals(0, result.code);
            assertEquals(0, result.data.size());
        }
    }

    @Ignore("disableBooster not implemented")
    @Test
    public void enabledBoostCase() throws Exception {
        Logs.d(THIS_CLASS, "enabledBoostCase", "");
        final long boostSize = 1024 * 1024 * 100;
        HCFSAPI.Result result = HCFSAPI.enableBooster(boostSize);
        assertTrue(result.isSuccess);

        result = HCFSAPI.disableBooster();
        assertTrue(result.isSuccess);
        assertEquals(0, result.code);
        assertEquals(0, result.data.size());
    }

    @Ignore("Unimplemented")
    @Test
    public void boostingCase() throws Exception {
        Logs.d(THIS_CLASS, "boostingCase", "");
        final long boostSize = 1024 * 1024 * 100;
        HCFSAPI.Result result = HCFSAPI.enableBooster(boostSize);
        assertTrue(result.isSuccess);

        result = HCFSAPI.disableBooster();
        assertTrue(result.isSuccess);
        assertEquals(0, result.code);
        assertEquals(0, result.data.size());
    }

    @Ignore("Unimplemented")
    @Test
    public void unboostingCase() throws Exception {
        Logs.d(THIS_CLASS, "unboostingCase", "");
        final long boostSize = 1024 * 1024 * 100;
        HCFSAPI.Result result = HCFSAPI.enableBooster(boostSize);
        assertTrue(result.isSuccess);

        result = HCFSAPI.disableBooster();
        assertTrue(result.isSuccess);
        assertEquals(0, result.code);
        assertEquals(0, result.data.size());
    }
}
