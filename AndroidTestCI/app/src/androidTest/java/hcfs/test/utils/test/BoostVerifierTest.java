package hcfs.test.utils.test;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import org.junit.Test;

import hcfs.test.utils.BoostVerifier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class BoostVerifierTest {

    private static final boolean IS_BOOST_ENABLED = true;
    private static final long BOOST_SIZE = 100 * 1024 * 1024;
    private static final String BOOSTED_APP = "com.trello";
    private static final String UNBOOSTED_APP = "com.ddm.iptools";
    private static final String UNINSTALLED_APP = "a.b.c";

    @Test
    public void testIsBoostEnabled() {
        assertEquals(IS_BOOST_ENABLED, BoostVerifier.isBoostEnabled());
        assertTrue(BoostVerifier.isSmartCacheSizeEquals(BOOST_SIZE));
    }

    @Test
    public void testBoostedAppIsActualBoosted() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertTrue(BoostVerifier.isActualBoosted(context, BOOSTED_APP));
    }

    @Test
    public void testUnboostedAppIsActualBoosted() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertFalse(BoostVerifier.isActualBoosted(context, UNBOOSTED_APP));
    }

    @Test
    public void testBoostedAppIsActualUnboosted() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertFalse(BoostVerifier.isActualUnboosted(context, BOOSTED_APP));
    }

    @Test
    public void testUnboostedAppIsActualUnboosted() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertTrue(BoostVerifier.isActualUnboosted(context, UNBOOSTED_APP));
    }

    @Test
    public void testBoostedGetUnboostedAppPermission() {
        try {
            BoostVerifier.getUnboostedAppPermission(BOOSTED_APP);
            fail();
        } catch (RuntimeException e) {
            assertEquals("[BoostVerifier.getUnboostedAppPermission] The app is boosted.", e.getMessage());
        }
    }

    @Test
    public void testUninstalledAppGetUnboostedAppPermission() {
        try {
            BoostVerifier.getUnboostedAppPermission(UNINSTALLED_APP);
            fail();
        } catch (RuntimeException e) {
            assertEquals("[BoostVerifier.getUnboostedAppPermission] Fail to find the app.", e.getMessage());
        }
    }

    @Test
    public void testUnboostedAppGetUnboostedAppPermission() {
        assertFalse(BoostVerifier.getUnboostedAppPermission(UNBOOSTED_APP).toString().isEmpty());
    }

    @Test
    public void testBoostedGetBoostedAppPermission() {
        assertFalse(BoostVerifier.getBoostedAppPermission(BOOSTED_APP).toString().isEmpty());
    }

    @Test
    public void testUninstalledAppGetBoostedAppPermission() {
        try {
            BoostVerifier.getBoostedAppPermission(UNINSTALLED_APP);
            fail();
        } catch (RuntimeException e) {
            assertEquals("[BoostVerifier.getBoostedAppPermission] The app is unboosted.", e.getMessage());
        }
    }

    @Test
    public void testUnboostedAppGetBoostedAppPermission() {
        try {
            BoostVerifier.getBoostedAppPermission(UNBOOSTED_APP);
            fail();
        } catch (RuntimeException e) {
            assertEquals("[BoostVerifier.getBoostedAppPermission] The app is unboosted.", e.getMessage());
        }
    }
}
