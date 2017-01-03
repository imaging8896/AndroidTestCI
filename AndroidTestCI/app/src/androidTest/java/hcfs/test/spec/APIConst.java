package hcfs.test.spec;

import java.io.File;
import java.util.Arrays;

import hcfs.test.config.Path;

public class APIConst {

    public static class Logs {
        public static final String DIR = Path.SDCARD_PATH + File.separator + "TeraLog";
        public static final String DMESG = DIR + File.separator + "dmesg";
        public static final String HCFS = DIR + File.separator + "hcfs_android_log";
        public static final String HCFS1 = DIR + File.separator + "hcfs_android_log.1";
//        public static final String HCFS2 = DIR + File.separator + "hcfs_android_log.2";
//        public static final String HCFS3 = DIR + File.separator + "hcfs_android_log.3";
//        public static final String HCFS4 = DIR + File.separator + "hcfs_android_log.4";
//        public static final String HCFS5 = DIR + File.separator + "hcfs_android_log.5";
        public static final String LOGCAT = DIR + File.separator + "logcat";
    }

    public static class BoostStatus {
        public static final int ON = 0;
        public static final int OFF = 1;
        private static final Integer[] validNums = {0, 1};

        public static boolean isValidNum(int testOne) {
            return isIn(testOne, validNums);
        }
    }

    public static class PinStatus {
        public static final int UNPINNED = 0;
        public static final int PINNED = 1;
        public static final int PRIORITY = 2;
    }

    public static class PinType {
        public static final int NORMAL = 1;
        public static final int PRIORITY = 2;
    }

    public static class RestoreStatus {
        public static final int NOTRESTORING = 0;
        public static final int STAGE1 = 1;
        public static final int STAGE2 = 2;
        private static final Integer[] validNums = {0, 1, 2};

        public static boolean isValidNum(int testOne) {
            return isIn(testOne, validNums);
        }
    }

    public static class FileStatus {
        public static final int LOCAL = 0;
        public static final int CLOUD = 1;
        public static final int HYBRID = 2;
    }

    public static class SyncState {
        public static final int CLEAN = 1;
        public static final int DIRTY = 0;
        private static final Integer[] validNums = {0, 1};

        public static boolean isValidNum(int testOne) {
            return isIn(testOne, validNums);
        }
    }

    public static class SyncStatus {
        public static final int ENABLE = 1;
        public static final int DISABLE = 0;
    }

    public static class SyncPoint {
        public static final int CLEAR = 0;
        public static final int UNSET = 1;
        private static final Integer[] validNums = {0, 1};

        public static boolean isValidNum(int testOne) {
            return isIn(testOne, validNums);
        }
    }

    public static class BackendType {
        private static final String[] validTypes = {"swift", "none", "s3", "swifttoken"};

        public static boolean isValidType(String testOne) {
            return isIn(testOne, validTypes);
        }
    }

    public static class SwiftProtocol {
        private static final String[] validTypes = {"http", "https"};

        public static boolean isValidType(String testOne) {
            return isIn(testOne, validTypes);
        }
    }

    private static boolean isIn(int result, Integer[] expectedValues) {
        return Arrays.asList(expectedValues).contains(result);
    }

    private static boolean isIn(String result, String[] expectedValues) {
        return Arrays.asList(expectedValues).contains(result);
    }
}
