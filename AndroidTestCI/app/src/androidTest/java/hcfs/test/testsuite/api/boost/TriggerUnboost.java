package hcfs.test.testsuite.api.boost;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import com.hopebaytech.hcfsmgmt.utils.Logs;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import hcfs.test.spec.HCFSEvent;
import hcfs.test.utils.AppManager;
import hcfs.test.utils.BoostUtils;
import hcfs.test.utils.BoostVerifier;
import hcfs.test.utils.MgmtAppDBUtils;
import hcfs.test.utils.MgmtAppUtils;
import hcfs.test.utils.db.info.UidInfoAdapter;
import hcfs.test.wrapper.HCFSAPI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TriggerUnboost {

    private static final String THIS_CLASS = TriggerUnboost.class.getSimpleName();

    /**
     * String triggerBoost()
     * <li>True	0</li>
     * <li>False	Linux errors.</li>
     */

    private static final int BOOST_SIZE = 1024 * 1024 * 100;

    private Context mContext;
    private String testAppPkg;
    private MgmtAppDBUtils appDBUtils;

    @Before
    public void setUp() throws Exception {
        Logs.d(THIS_CLASS, "setUp", "");
        //1.Get instrumentation test app context
        mContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        //2.Get test app package name
        AppManager appManager = new AppManager(mContext);
        testAppPkg = appManager.popOneInstalledApp();

        //3.Prepare management app DB helper
        appDBUtils = new MgmtAppDBUtils(mContext);

        //4.Prepare boost environment
        // TODO wait disableBooster implement
//        HCFSAPI.Result result = HCFSAPI.disableBooster();
//        assertTrue(result.isSuccess);
        if(!BoostVerifier.isBoostEnabled())
            assertTrue(BoostUtils.enableBoost(BOOST_SIZE));

        //5.Unboost test app
        BoostUtils.unboostApp(mContext, testAppPkg);
        assertEquals(UidInfoAdapter.BoostStatus.UNBOOSTED, appDBUtils.getAppBoostStatus(testAppPkg));
    }

    @After
    public void tearDown() throws Exception {
        Logs.d(THIS_CLASS, "tearDown", "");
        //1.Unboost test app
        if(BoostVerifier.getAppActualStatus(mContext, testAppPkg) == BoostVerifier.AppActualStatus.BROKEN)
            BoostUtils.uninstallApp(mContext, testAppPkg);
        else if(BoostVerifier.getAppActualStatus(mContext, testAppPkg) == BoostVerifier.AppActualStatus.BOOSTED) {
            BoostUtils.restoreBoostStatus(mContext, testAppPkg, UidInfoAdapter.BoostStatus.BOOSTED);
            BoostUtils.unboostApp(mContext, testAppPkg);
        } else if(BoostVerifier.getAppActualStatus(mContext, testAppPkg) == BoostVerifier.AppActualStatus.UNBOOSTED)
            BoostUtils.restoreBoostStatus(mContext, testAppPkg, UidInfoAdapter.BoostStatus.UNBOOSTED);

        // TODO wait disableBooster implement
        //2.Cleanup boost environment
//        HCFSAPI.Result result = HCFSAPI.disableBooster();
//        assertTrue(result.isSuccess);
    }

    @Test
    public void noUnboostSetCase() throws Exception {
        Logs.d(THIS_CLASS, "noBoostSetCase", "");
        //1.Record all apps info.
        final List originSortedAppsInfo = appDBUtils.getSortedAppsInfo();

        //2.Call triggerUnboost
        Timestamp beforeTriggerUnboost = new Timestamp(new Date().getTime());
        HCFSAPI.Result result = HCFSAPI.triggerUnboost();

        //3.Check API result
        assertTrue(result.isSuccess);
        assertEquals(0, result.code);
        assertEquals(0, result.data.size());

        //4.Check TeraService receives event.
        int waitingSec = 120;
        int receivedEvent = MgmtAppUtils.isReceiveEventsUntil(
                new int[]{HCFSEvent.BOOSTER_PROCESS_COMPLETED,
                        HCFSEvent.BOOSTER_PROCESS_FAILED}, beforeTriggerUnboost, waitingSec);
        assertEquals(HCFSEvent.BOOSTER_PROCESS_COMPLETED, receivedEvent);

        //5.Check all apps info unchanged.
        assertEquals(originSortedAppsInfo, appDBUtils.getSortedAppsInfo());
    }

    @Test
    public void boostedAppCase() throws Exception {
        Logs.d(THIS_CLASS, "boostedAppCase", "");
        //1.Record all apps info except test app.
        final List originSortedAppsInfoExceptTestApp = appDBUtils.getSortedAppsInfoExcept(testAppPkg);

        //2.Boost test app.
        boostApp(appDBUtils, testAppPkg);

        //3.Check test app boost status is 6(boosted).
        assertEquals(UidInfoAdapter.BoostStatus.BOOSTED, appDBUtils.getAppBoostStatus(testAppPkg));

        //4.Set test app boost status 1(init unboost).
        appDBUtils.setAppBoostStatus(testAppPkg, UidInfoAdapter.BoostStatus.INIT_UNBOOST);
        assertEquals(UidInfoAdapter.BoostStatus.INIT_UNBOOST, appDBUtils.getAppBoostStatus(testAppPkg));

        //5.Call triggerUnboost
        Timestamp beforeTriggerUnboost = new Timestamp(new Date().getTime());
        HCFSAPI.Result result = HCFSAPI.triggerUnboost();

        //6.Check API result
        assertTrue(result.isSuccess);
        assertEquals(0, result.code);
        assertEquals(0, result.data.size());

        //7.Check TeraService receives event.
        int waitingSec = 120;
        int receivedEvent = MgmtAppUtils.isReceiveEventsUntil(
                new int[]{HCFSEvent.BOOSTER_PROCESS_COMPLETED,
                        HCFSEvent.BOOSTER_PROCESS_FAILED}, beforeTriggerUnboost, waitingSec);
        assertEquals(HCFSEvent.BOOSTER_PROCESS_COMPLETED, receivedEvent);

        //8.Check test app boost status.
        assertEquals(UidInfoAdapter.BoostStatus.UNBOOSTED, appDBUtils.getAppBoostStatus(testAppPkg));

        //9.Check all apps info unchanged except test app.
        assertEquals(originSortedAppsInfoExceptTestApp, appDBUtils.getSortedAppsInfoExcept(testAppPkg));
    }


    @Test
    public void unboostedAppCase() throws Exception {
        Logs.d(THIS_CLASS, "unboostedAppCase", "");
        //1.Record all apps info except test app.
        final List originSortedAppsInfoExceptTestApp = appDBUtils.getSortedAppsInfoExcept(testAppPkg);

        //2.Check test app boost status is 2(unboosted).
        assertEquals(UidInfoAdapter.BoostStatus.UNBOOSTED, appDBUtils.getAppBoostStatus(testAppPkg));

        //3.Set test app boost status 1(init unboost).
        appDBUtils.setAppBoostStatus(testAppPkg, UidInfoAdapter.BoostStatus.INIT_UNBOOST);
        assertEquals(UidInfoAdapter.BoostStatus.INIT_UNBOOST, appDBUtils.getAppBoostStatus(testAppPkg));

        //4.Call triggerBoost
        Timestamp beforeTriggerUnboost = new Timestamp(new Date().getTime());
        HCFSAPI.Result result = HCFSAPI.triggerUnboost();

        //5.Check API result
        assertTrue(result.isSuccess);
        assertEquals(0, result.code);
        assertEquals(0, result.data.size());

        //6.Check TeraService receives event.
        int waitingSec = 120;
        int receivedEvent = MgmtAppUtils.isReceiveEventsUntil(
                new int[]{HCFSEvent.BOOSTER_PROCESS_COMPLETED,
                        HCFSEvent.BOOSTER_PROCESS_FAILED}, beforeTriggerUnboost, waitingSec);
        assertEquals(HCFSEvent.BOOSTER_PROCESS_COMPLETED, receivedEvent);

        //7.Check test app boost status.
        assertEquals(UidInfoAdapter.BoostStatus.UNBOOSTED, appDBUtils.getAppBoostStatus(testAppPkg));

        //8.Check all apps info unchanged except test app.
        assertEquals(originSortedAppsInfoExceptTestApp, appDBUtils.getSortedAppsInfoExcept(testAppPkg));
    }

    private void boostApp(MgmtAppDBUtils dbUtils, String appPkg) {
        Timestamp beforeTriggerBoost = new Timestamp(new Date().getTime());
        dbUtils.setAppBoostStatus(appPkg, UidInfoAdapter.BoostStatus.INIT_BOOST);
        HCFSAPI.Result result = HCFSAPI.triggerBoost();
        assertTrue(result.isSuccess);
        final int waitingSec = 120;
        int receivedEvent = MgmtAppUtils.isReceiveEventsUntil(
                new int[]{HCFSEvent.BOOSTER_PROCESS_COMPLETED,
                        HCFSEvent.BOOSTER_PROCESS_FAILED}, beforeTriggerBoost, waitingSec);
        assertEquals(HCFSEvent.BOOSTER_PROCESS_COMPLETED, receivedEvent);
    }
}
