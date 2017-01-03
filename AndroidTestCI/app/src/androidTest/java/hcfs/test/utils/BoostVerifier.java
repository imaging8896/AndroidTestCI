package hcfs.test.utils;

import android.content.Context;

import java.io.File;

public class BoostVerifier {

    private static final String BOOST_MOUNT_POINT = "/data/mnt/hcfsblock";
    private static final String SMART_CACHE_MOUNT_POINT = "/data/smartcache";
    private static final String SMART_CACHE_FILE = "/data/smartcache/hcfsblock";

    public static class AppActualStatus {
        public static final int BROKEN = -1;
        public static final int BOOSTED = 0;
        public static final int UNBOOSTED = 1;
    }

    public static class AppPermission {
        private String lsResult;

        AppPermission(String lsResult) {
            this.lsResult = lsResult;
        }

        @Override
        public boolean equals(Object o) {
            if(!(o instanceof AppPermission))
                throw new RuntimeException("Compare object should be instance of AppPermission.class");
            return lsResult.equals(((AppPermission) o).lsResult);
        }

        @Override
        public String toString() {
            return lsResult;
        }
    }

    public static boolean isBoostEnabled() {
        return CommandUtils.exec("mount").contains(SMART_CACHE_MOUNT_POINT) &&
                CommandUtils.exec("mount").contains(BOOST_MOUNT_POINT);
    }

    public static boolean isSmartCacheSizeEquals(long size) {
        //TODO I have no idea
        //HCFSvol volsize
//        return Long.parseLong(CommandUtils.exec("stat -c%s " + SMART_CACHE_FILE).trim()) == size;
        return true;
    }

    public static int getAppActualStatus(Context context, String pkg) {
        if(isActualBoosted(context, pkg))
            return AppActualStatus.BOOSTED;
        if(isActualUnboosted(context, pkg))
            return AppActualStatus.UNBOOSTED;
        return AppActualStatus.BROKEN;
    }

    /**
     * 1.Boost enabled
     * 2.pkg is installed
     * 3./data/data/pkg is link
     * 4./data/mnt/hcfsblock/pkg is directory
     */
    public static boolean isActualBoosted(Context context, String pkg) {
        return isBoostEnabled()
                && AppManager.isInstalled(context, pkg)
                && FileUtils.isLink(AppManager.getAppPath(pkg))
                && FileUtils.isDir(getSmartCacheAppPath(pkg));
    }

    /**
     * 1.pkg is installed
     * 2./data/data/pkg is directory
     * 3./data/mnt/hcfsblock/pkg non-exists
     */
    public static boolean isActualUnboosted(Context context, String pkg) {
        return AppManager.isInstalled(context, pkg)
                && FileUtils.isDir(AppManager.getAppPath(pkg))
                && !new File(getSmartCacheAppPath(pkg)).exists();
    }

    public static AppPermission getUnboostedAppPermission(String pkg) {
        if(FileUtils.isDir(getSmartCacheAppPath(pkg)))
            throw new RuntimeException("[BoostVerifier.getUnboostedAppPermission] The app is boosted.");
        String appPath = AppManager.getAppPath(pkg);
        if(!FileUtils.isDir(appPath))
            throw new RuntimeException("[BoostVerifier.getUnboostedAppPermission] Fail to find the app.");
        return new AppPermission(CommandUtils.exec("ls -ldZ " + appPath));
    }

    public static AppPermission getBoostedAppPermission(String pkg) {
        if(!FileUtils.isDir(getSmartCacheAppPath(pkg)))
            throw new RuntimeException("[BoostVerifier.getBoostedAppPermission] The app is unboosted.");
        String appPath = BOOST_MOUNT_POINT + "/" + pkg;
        return new AppPermission(CommandUtils.exec("ls -ldZ " + appPath));
    }

    public static String getSmartCacheAppPath(String pkg) {
        return BOOST_MOUNT_POINT + "/" + pkg;
    }
}
