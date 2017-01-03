package hcfs.test.utils;

import android.net.LocalSocket;
import android.net.LocalSocketAddress;

import com.hopebaytech.hcfsmgmt.utils.Logs;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.Date;

import hcfs.test.spec.HCFSEvent;
import hcfs.test.spec.LogcatPattern;
import hcfs.test.wrapper.HCFSAPI;

public class MgmtAppUtils {

    private static final String THIS_CLASS = MgmtAppUtils.class.getSimpleName();

    public static String EVENT_SERVER_SOCKET_ADDRESS = "mgmt.api.sock";
    public static String PACKAGE = "com.hopebaytech.hcfsmgmt";
    public static String TERA_SERVICE_TAG = "TeraService";

    private static final LogcatUtils logcatUtils = LogcatUtils.getLogcatUtils(TERA_SERVICE_TAG);

    public static void restoreEventNotifyServer() {
        final int retryTimes = 3;
        Timestamp timestamp = new Timestamp(new Date().getTime());
        sleep(10);
        for(int i = 0; i < retryTimes; i++) {
            if(!HCFSAPI.setNotifyServer(EVENT_SERVER_SOCKET_ADDRESS).isSuccess
                    && !sendEventToNotifyServer(HCFSEvent.CONNECTED))
                continue;
            if(isReceiveEventUntil(HCFSEvent.CONNECTED, timestamp, 60))
                return;
        }
        throw new RuntimeException("Fail to connect event notify server.");
    }

    public static void forceRefreshToken() {
        Timestamp timestamp = new Timestamp(new Date().getTime());
        sleep(3);
        sendEventToNotifyServer(HCFSEvent.TOKEN_EXPIRED);
        if(!isReceiveEventUntil(HCFSEvent.TOKEN_EXPIRED, timestamp, 20))
            throw new RuntimeException("Fail to refresh token.");
    }

    public static boolean sendEventToNotifyServer(int event) {
        try(LocalSocket socket = new LocalSocket()) {
            LocalSocketAddress address = new LocalSocketAddress(EVENT_SERVER_SOCKET_ADDRESS);
            socket.connect(address);
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            writer.print("[{\"event_id\":" + event + "}]\n");
            writer.flush();
            int ret = socket.getInputStream().read();
            Logs.d(THIS_CLASS, "sendEventToNotifyServer", "HCFSEvent <" + event + ">, Result code <" + ret + ">");
            return ret == 0;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isReceiveEventUntil(int event, Timestamp fromTimestamp, int timeoutSec) {
        String eventPattern = LogcatPattern.getHCFSEventPattern(event);
        sleep(3);
        return logcatUtils.findLineContainAfter(eventPattern, fromTimestamp, timeoutSec) != null;
    }

    public static int isReceiveEventsUntil(int[] events, Timestamp fromTimestamp, int timeoutSec) {
        String[] eventPatterns = new String[events.length];
        for(int i = 0; i < events.length; i++)
            eventPatterns[i] = LogcatPattern.getHCFSEventPattern(events[i]);
        sleep(3);
        String finded;
        if((finded = logcatUtils.findLineContainAfter(eventPatterns, fromTimestamp, timeoutSec)) != null)
            return Integer.parseInt(finded.trim().split(" = ")[1]);
        return HCFSEvent.ERROR;
    }

    private static void sleep(int timeoutSec) {
        try {Thread.sleep(timeoutSec * 1000);} catch (InterruptedException e) {throw new RuntimeException(e);}
    }
}
