package hcfs.test.utils;

import android.system.ErrnoException;
import android.system.Os;
import android.system.StructStat;

import java.io.File;

public class FileUtils {

    public static boolean isDir(String filePath) {
        File file = new File(filePath);
        return file.exists() && file.isDirectory();
    }

    public static boolean isFile(String filePath) {
        File file = new File(filePath);
        return file.exists() && file.isFile();
    }

    public static boolean isLink(String filePath) {
        String lsOpt = "-l";
        File file = new File(filePath);
        if(!file.exists())
            return false;
        if(file.isDirectory())
            lsOpt += "d";
        return CommandUtils.exec("ls " + lsOpt + " " + filePath).startsWith("l");
    }

    public static boolean isDirty(String filePath) {
        //TODO: Check from superblock(another way)
        for(String oneFileLsResult : getFileBlocksLsResult(filePath).split("\n")) {
            if(getStickyBit(oneFileLsResult) != '-')//T as dirty
                return true;
        }
        return false;
    }

    public static long getInode(String filePath) {
        return getStat(filePath).st_ino;
    }

    private static StructStat getStat(String filePath) {
        try {
            return Os.stat(filePath);
        } catch (ErrnoException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getFileBlocksLsResult(String filePath) {
        //TODO chmod o+rx -R /data/hcfs in gradle
        String cmd = "ls -Rl /data/hcfs/blockstorage/ | grep ' block" + getInode(filePath) + "'";
        // pipe command need scripted
        String lsResult = CommandUtils.execShell(cmd).trim();
        if (lsResult.isEmpty())
            throw new RuntimeException(
                    "Check non-existed file with path:" + filePath + "; ls result:" + CommandUtils.exec("ls -l " + filePath));
        return lsResult.trim();
    }

    private static char getStickyBit(String lsResultOneLine) {
        //-rw----r-x the last one is sticky bit
        return lsResultOneLine.charAt(9);
    }
}
