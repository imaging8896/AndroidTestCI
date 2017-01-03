package hcfs.test.utils;

import android.content.ContentValues;
import android.content.Context;

import com.hopebaytech.hcfsmgmt.utils.Logs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hcfs.test.utils.db.UidDAO;
import hcfs.test.utils.db.info.UidInfoAdapter;
import hcfs.test.utils.db.info.UidInfoAdapterComparator;

public class MgmtAppDBUtils {

    private static final String THIS_CLASS = MgmtAppDBUtils.class.getSimpleName();

    private UidDAO mgmtAppDAO;

    public MgmtAppDBUtils(Context context) {
        mgmtAppDAO = new UidDAO(context);
    }

    public void setAppBoostStatus(String pkg, int boostStatus) {
        ContentValues cv = new ContentValues();
        cv.put(UidDAO.BOOST_STATUS_COLUMN, boostStatus);
        if(!mgmtAppDAO.update(pkg, cv))
            throw new RuntimeException("Unable to set " + pkg + " boost status " + boostStatus);
    }

    public void removeApp(String pkg) {
        if(!mgmtAppDAO.delete(pkg))
            throw new RuntimeException("Unable to delete " + pkg);
    }

    public List<UidInfoAdapter> getSortedAppsInfo() {
        List<UidInfoAdapter> result = mgmtAppDAO.getAll();
        Collections.sort(result, new UidInfoAdapterComparator());
        Logs.d(THIS_CLASS, "getSortedAppsInfo", "All app boost status map =" + result);
        return result;
    }

    public List<UidInfoAdapter> getSortedAppsInfoExcept(String excludeAppPkg) {
        List<UidInfoAdapter> result = mgmtAppDAO.getAll();
        for(UidInfoAdapter appInfo : new ArrayList<>(result)) {
            if(appInfo.getPackageName().equals(excludeAppPkg)) {
                result.remove(appInfo);
                break;
            }
        }
        Logs.d(THIS_CLASS, "getSortedAppsInfoExcept", "All app boost status map except " + excludeAppPkg + " =" + result);
        Collections.sort(result, new UidInfoAdapterComparator());
        return result;
    }

    public int getAppBoostStatus(String pkg) {
        return mgmtAppDAO.get(pkg).getBoostStatus();
    }
}
