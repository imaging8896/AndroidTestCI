package hcfs.test.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

public class LogcatUtils {
    /**
     * Need
     * 1.AndroidManifest.xml : <uses-permission android:name="android.permission.READ_LOGS"/>
     * 2.build.xml : su 0 pm grant android.permission.READ_LOGS
     */

    private static final int RETRY_SEC = 1;

    private static Map<String, LogcatUtils> tagLogcatUtilsMap;

    private final List<Logcat> logcats = new ArrayList<>();
    private String tag;

    private LogcatUtils(String tag) {
        this.tag = tag;
    }

    public static LogcatUtils getLogcatUtils(String tag) {
        if(tagLogcatUtilsMap == null)
            tagLogcatUtilsMap = new HashMap<>();
        if(!tagLogcatUtilsMap.containsKey(tag)) {
            synchronized (LogcatUtils.class) {
                if(!tagLogcatUtilsMap.containsKey(tag))
                    tagLogcatUtilsMap.put(tag, new LogcatUtils(tag));
            }
        }
        return tagLogcatUtilsMap.get(tag);
    }

    public String findLineContainAfter(String containStr, Timestamp timestamp, int timeoutSec) {
        int remainTimeSec = timeoutSec;
        String logcatLine;
        while((logcatLine = findLineOnesContain(containStr, timestamp)) == null && remainTimeSec > 0) {
            sleep(RETRY_SEC);
            remainTimeSec -= RETRY_SEC;
        }
        return logcatLine;
    }

    public String findLineContainAfter(String[] containStrs, Timestamp timestamp, int timeoutSec) {
        int remainTimeSec = timeoutSec;
        String logcatLine;
        while(remainTimeSec > 0) {
            for(String containStr : containStrs) {
                if((logcatLine = findLineOnesContain(containStr, timestamp)) != null )
                    return logcatLine;
            }

            sleep(RETRY_SEC);
            remainTimeSec -= RETRY_SEC;
        }
        return null;
    }

    public static String retrieveLogContent(String logcatLine) {
        return Logcat.parse(logcatLine).log;
    }

    private static class Logcat {

        private static final String TIMESTAMP_FMT = "yyyy-MM-dd hh:mm:ss.SSS";
        //date time PID-TID/package priority/tag: message
        //11-04 13:45:18.483  4051  4235 W ctxmgr  : [AclManager]No 2
        String rawString;
        Timestamp timestamp;
        int pid;
        int tid;
        String level;
        String tag;
        String log;

        static Logcat parse(String logcatLine) {
            Logcat logcat = new Logcat();
            logcat.rawString = logcatLine;
            //11-04 13:45:18.483 4051  4235 W ctxmgr  : [AclManager]No 2
            Pattern emptyPattern = Pattern.compile(" +");
            String[] element = emptyPattern.split(logcatLine, 8);
            if(element.length != 8)
                throw new RuntimeException("Fail to split line of logcat to 8");
            logcat.timestamp = parseTime(element[0] + " " + element[1]);
            logcat.pid = Integer.parseInt(element[2]);
            logcat.tid = Integer.parseInt(element[3]);
            logcat.level = element[4];
            logcat.tag = element[5];
            logcat.log = element[7].trim();
            return logcat;
        }

        private static Timestamp parseTime(String rawString) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat(TIMESTAMP_FMT, Locale.TAIWAN);
                Date now = new Date();
                Calendar cal = Calendar.getInstance();
                cal.setTime(now);
                int year = cal.get(Calendar.YEAR);
                Date parsedDate = dateFormat.parse(year + "-" + rawString);
                return new Timestamp(parsedDate.getTime());
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void getLogcats() {
        List<Logcat> newLogcats = new ArrayList<>();
        String command = tag != null ? "logcat -d -s " + tag : "logcat -d";
        String logcatStr = CommandUtils.exec(command);
        List<String> logcatLines = Arrays.asList(logcatStr.split("\n"));
        if(!logcats.isEmpty()) {
            String lastOne = logcats.get(logcats.size() - 1).rawString;
            if(logcatLines.contains(lastOne)) {
                if(logcatLines.indexOf(lastOne) == logcatLines.size() - 1)
                    logcatLines = new ArrayList<>();
                else
                    logcatLines = logcatLines.subList(logcatLines.indexOf(lastOne) + 1, logcatLines.size());
            }
        }
        for(String logcatLine : logcatLines)
            if(!logcatLine.startsWith("--"))
                newLogcats.add(Logcat.parse(logcatLine));
        logcats.addAll(newLogcats);
    }

    private String findLineOnesContain(String containStr, Timestamp timestamp) {
        getLogcats();

        for(Logcat logcat : logcats) {
            if(logcat.timestamp.after(timestamp)) {
                if(logcat.log.contains(containStr))
                    return logcat.log;
            }
        }
        return null;
    }

    private static void sleep(int timeoutSec) {
        try {Thread.sleep(timeoutSec * 1000);} catch (InterruptedException e) {throw new RuntimeException(e);}
    }
}
