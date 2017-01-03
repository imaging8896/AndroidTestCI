package hcfs.test.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.hopebaytech.hcfsmgmt.utils.Logs;

import java.sql.Timestamp;
import java.util.Date;

import hcfs.test.spec.HCFSEvent;
import hcfs.test.utils.db.info.UidInfoAdapter;
import hcfs.test.wrapper.HCFSAPI;

public class BoostUtils {

    private static final String THIS_CLASS = BoostUtils.class.getSimpleName();

    public static boolean enableBoost(long boostSize) {
        if(!BoostVerifier.isBoostEnabled()) {
            int maxWaitSec = 60;
            HCFSAPI.Result result = HCFSAPI.enableBooster(boostSize);
            if(result.isSuccess) {
                while(!BoostVerifier.isBoostEnabled() && maxWaitSec-- > 0)
                    sleep(1);
                //There should be delay
                sleep(10);
                return true;
            }
        }
        return false;
    }

    public static void unboostApp(Context context, String pkg) {
        Logs.d(THIS_CLASS, "unboostApp", "");
        MgmtAppDBUtils dbUtils = new MgmtAppDBUtils(context);
        if(AppManager.isInstalled(context, pkg) && BoostVerifier.isBoostEnabled()) {
            Logs.d(THIS_CLASS, "unboostApp", pkg + " is installed and boost enabled");
            if(dbUtils.getAppBoostStatus(pkg) == UidInfoAdapter.BoostStatus.BOOSTED) {
                concreteUnboostApp(context, pkg);

                if(BoostVerifier.getAppActualStatus(context, pkg)
                        == BoostVerifier.AppActualStatus.BOOSTED)
                    restoreBoostStatus(context, pkg, UidInfoAdapter.BoostStatus.BOOSTED);
                else if(BoostVerifier.getAppActualStatus(context, pkg)
                        == BoostVerifier.AppActualStatus.UNBOOSTED)
                    restoreBoostStatus(context, pkg, UidInfoAdapter.BoostStatus.UNBOOSTED);
                else
                    uninstallApp(context, pkg);
            }
        }
    }

    public static void restoreBoostStatus(Context context, String pkg, int boostStatus) {
        Logs.d(THIS_CLASS, "restoreBoostStatus", "");
        new MgmtAppDBUtils(context).setAppBoostStatus(pkg, boostStatus);
    }

    public static void uninstallApp(Context context, String pkg) {
        Logs.d(THIS_CLASS, "uninstallApp", "Start");
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setData(Uri.parse("package:" + pkg));
        context.startActivity(intent);

        new MgmtAppDBUtils(context).removeApp(pkg);
        Logs.d(THIS_CLASS, "uninstallApp", "End");
    }

    private static void concreteUnboostApp(Context context, String pkg) {
        Timestamp beforeTriggerUnboost = new Timestamp(new Date().getTime());
        int waitingSec = 120;
        new MgmtAppDBUtils(context).setAppBoostStatus(pkg, UidInfoAdapter.BoostStatus.INIT_UNBOOST);
        HCFSAPI.triggerUnboost();
        MgmtAppUtils.isReceiveEventsUntil(
                new int[]{HCFSEvent.BOOSTER_PROCESS_COMPLETED, HCFSEvent.BOOSTER_PROCESS_FAILED},
                beforeTriggerUnboost, waitingSec);
    }

    private static void sleep(int timeoutSec) {
        try {Thread.sleep(timeoutSec * 1000);} catch (InterruptedException e) {throw new RuntimeException(e);}
    }
}
