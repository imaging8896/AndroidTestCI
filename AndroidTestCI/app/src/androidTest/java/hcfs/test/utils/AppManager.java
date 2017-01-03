package hcfs.test.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.hopebaytech.hcfsmgmt.utils.Logs;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import hcfs.test.config.Path;

public class AppManager {

    private static final String THIS_CLASS = AppManager.class.getSimpleName();

    private static final String APK_DIR = Path.SDCARD_PATH + File.separator + "DCIM";

    private Context mContext;
    private int pkgIndex;
    private List<String> packageNames;

    public AppManager(Context context) {
        mContext = context;
        pkgIndex = 0;
        packageNames = new ArrayList<>();

        PackageManager pkgManager = mContext.getPackageManager();

        for(String apkName : getApkNames()) {
            String apkPath = APK_DIR + File.separator + apkName;
            PackageInfo pkgInfo = pkgManager.getPackageArchiveInfo(apkPath, PackageManager.GET_ACTIVITIES);
            if(pkgInfo != null) {
                if(isInstalled(mContext, pkgInfo.packageName))
                    packageNames.add(pkgInfo.packageName);
            }
        }
        Logs.d(THIS_CLASS, "AppManager", "Packages="+ packageNames);
        if(packageNames.isEmpty())
            throw new RuntimeException("No test app available.");
    }

    public String popOneInstalledApp() {
        if(pkgIndex == packageNames.size())
            throw new RuntimeException("Not enough test apps.");
        String returnAppPkg = packageNames.get(pkgIndex++);
        Logs.d(THIS_CLASS, "popOneInstalledApp", "app="+ returnAppPkg);
        return returnAppPkg;
    }

    public static String getAppPath(String pkg) {
        return "/data/data/" + pkg;
    }

    public static boolean isInstalled(Context context, String pkg) {
        try {
            context.getPackageManager().getPackageInfo(pkg, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private List<String> getApkNames() {
        List<String> apkNames = new ArrayList<>();
        for(File file : new File(APK_DIR).listFiles()) {
            if(file.getName().endsWith(".apk"))
                apkNames.add(file.getName());
        }
        return apkNames;
    }
}
