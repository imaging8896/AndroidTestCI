package hcfs.test.utils;

import org.junit.Test;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class LogcatUtilsTest {


    @Test
    public void testToMap() throws Exception{
        String test = "11-04 15:47:21.595  4051  4235 W ctxmgr  : [AclManager]No 2 for (accnt=account#-413941514#, com.google.android.gms(10017):UserLocationProducer, vrsn=9877000, 0, 3pPkg = null ,  3pMdlId = null). Was: 2 for 1, account#-413941514#\n" +
                "11-04 15:47:22.630   578   578 E QCALOG  : [MessageQ] ProcessNewMessage: [LOWI-SERVER] unknown deliver target [OS-Agent]\n" +
                "11-04 15:47:26.613  4051  4235 W ctxmgr  : [ContextSpecificAclFactory]LocationConsent failed, ULR opt-in status is: false, account#-413941514#\n" +
                "11-04 15:47:26.614  4051  4235 W ctxmgr  : [AclManager]No 2 for (accnt=account#-413941514#, com.google.android.gms(10017):UserLocationProducer, vrsn=9877000, 0, 3pPkg = null ,  3pMdlId = null). Was: 2 for 1, account#-413941514#\n" +
                "11-04 15:47:28.602   952 30557 D NetlinkSocketObserver: NeighborEvent{elapsedMs=108923862, 172.16.31.254, [001485F27E49], RTM_NEWNEIGH, NUD_REACHABLE}\n" +
                "11-04 15:47:31.629  4051  4235 W ctxmgr  : [ContextSpecificAclFactory]LocationConsent failed, ULR opt-in status is: false, account#-413941514#\n" +
                "11-04 15:47:31.631  4051  4235 W ctxmgr  : [AclManager]No 2 for (accnt=account#-413941514#, com.google.android.gms(10017):UserLocationProducer, vrsn=9877000, 0, 3pPkg = null ,  3pMdlId = null). Was: 2 for 1, account#-413941514#\n" +
                "11-04 15:47:36.651  4051  4235 W ctxmgr  : [ContextSpecificAclFactory]LocationConsent failed, ULR opt-in status is: false, account#-413941514#\n" +
                "11-04 15:47:36.652  4051  4235 W ctxmgr  : [AclManager]No 2 for (accnt=account#-413941514#, com.google.android.gms(10017):UserLocationProducer, vrsn=9877000, 0, 3pPkg = null ,  3pMdlId = null). Was: 2 for 1, account#-413941514#\n" +
                "11-04 15:47:37.191   952  3070 D WifiStateMachine: starting scan for \"HOPEBAY_COM\"WPA_PSK with 2417\n" +
                "11-04 15:47:41.672  4051  4235 W ctxmgr  : [ContextSpecificAclFactory]LocationConsent failed, ULR opt-in status is: false, account#-413941514#\n" +
                "11-04 15:47:41.673  4051  4235 W ctxmgr  : [AclManager]No 2 for (accnt=account#-413941514#, com.google.android.gms(10017):UserLocationProducer, vrsn=9877000, 0, 3pPkg = null ,  3pMdlId = null). Was: 2 for 1, account#-413941514#\n" +
                "11-04 15:47:46.652   952 30557 D NetlinkSocketObserver: NeighborEvent{elapsedMs=108941912, 172.16.31.254, [001485F27E49], RTM_NEWNEIGH, NUD_STALE}\n" +
                "11-04 15:47:46.686  4051  4219 W ctxmgr  : [ContextSpecificAclFactory]LocationConsent failed, ULR opt-in status is: false, account#-413941514#\n" +
                "11-04 15:47:46.687  4051  4219 W ctxmgr  : [AclManager]No 2 for (accnt=account#-413941514#, com.google.android.gms(10017):UserLocationProducer, vrsn=9877000, 0, 3pPkg = null ,  3pMdlId = null). Was: 2 for 1, account#-413941514#\n" +
                "11-04 15:47:51.708  4051  4219 W ctxmgr  : [ContextSpecificAclFactory]LocationConsent failed, ULR opt-in status is: false, account#-413941514#\n" +
                "11-04 15:47:51.709  4051  4219 W ctxmgr  : [AclManager]No 2 for (accnt=account#-413941514#, com.google.android.gms(10017):UserLocationProducer, vrsn=9877000, 0, 3pPkg = null ,  3pMdlId = null). Was: 2 for 1, account#-413941514#\n" +
                "11-04 15:47:56.724  4051  4219 W ctxmgr  : [ContextSpecificAclFactory]LocationConsent failed, ULR opt-in status is: false, account#-413941514#\n" +
                "11-04 15:47:56.724  4051  4219 W ctxmgr  : [AclManager]No 2 for (accnt=account#-413941514#, com.google.android.gms(10017):UserLocationProducer, vrsn=9877000, 0, 3pPkg = null ,  3pMdlId = null). Was: 2 for 1, account#-413941514#\n" +
                "11-04 15:47:57.195   952  3070 D WifiStateMachine: starting scan for \"HOPEBAY_COM\"WPA_PSK with 2417\n" +
                "11-04 15:48:01.744  4051  4219 W ctxmgr  : [ContextSpecificAclFactory]LocationConsent failed, ULR opt-in status is: false, account#-413941514#\n" +
                "11-04 15:48:01.745  4051  4219 W ctxmgr  : [AclManager]No 2 for (accnt=account#-413941514#, com.google.android.gms(10017):UserLocationProducer, vrsn=9877000, 0, 3pPkg = null ,  3pMdlId = null). Was: 2 for 1, account#-413941514#\n" +
                "11-04 15:48:06.762  4051  4235 W ctxmgr  : [ContextSpecificAclFactory]LocationConsent failed, ULR opt-in status is: false, account#-413941514#\n" +
                "11-04 15:48:06.763  4051  4235 W ctxmgr  : [AclManager]No 2 for (accnt=account#-413941514#, com.google.android.gms(10017):UserLocationProducer, vrsn=9877000, 0, 3pPkg = null ,  3pMdlId = null). Was: 2 for 1, account#-413941514#\n" +
                "11-04 15:48:11.779  4051  4235 W ctxmgr  : [ContextSpecificAclFactory]LocationConsent failed, ULR opt-in status is: false, account#-413941514#\n" +
                "11-04 15:48:11.779  4051  4235 W ctxmgr  : [AclManager]No 2 for (accnt=account#-413941514#, com.google.android.gms(10017):UserLocationProducer, vrsn=9877000, 0, 3pPkg = null ,  3pMdlId = null). Was: 2 for 1, account#-413941514#\n" +
                "11-04 15:48:16.799  4051  4235 W ctxmgr  : [ContextSpecificAclFactory]LocationConsent failed, ULR opt-in status is: false, account#-413941514#\n" +
                "11-04 15:48:16.799  4051  4235 W ctxmgr  : [AclManager]No 2 for (accnt=account#-413941514#, com.google.android.gms(10017):UserLocationProducer, vrsn=9877000, 0, 3pPkg = null ,  3pMdlId = null). Was: 2 for 1, account#-413941514#\n" +
                "11-04 15:48:17.199   952  3070 D WifiStateMachine: starting scan for \"HOPEBAY_COM\"WPA_PSK with 2417\n" +
                "11-04 15:48:21.477  4051   784 I PhenotypeFlagCommitter: Experiment Configs successfully retrieved for com.google.android.gms.lockbox\n" +
                "11-04 15:48:21.482   952  3687 I AccountManagerService: getTypesVisibleToCaller: isPermitted? true\n" +
                "11-04 15:48:21.807  4051  4235 W ctxmgr  : [ContextSpecificAclFactory]LocationConsent failed, ULR opt-in status is: false, account#-413941514#\n" +
                "11-04 15:48:21.808  4051  4235 W ctxmgr  : [AclManager]No 2 for (accnt=account#-413941514#, com.google.android.gms(10017):UserLocationProducer, vrsn=9877000, 0, 3pPkg = null ,  3pMdlId = null). Was: 2 for 1, account#-413941514#\n" +
                "11-04 15:48:22.631   578   578 E QCALOG  : [MessageQ] ProcessNewMessage: [LOWI-SERVER] unknown deliver target [OS-Agent]\n" +
                "11-04 15:48:26.832  4051  4219 W ctxmgr  : [ContextSpecificAclFactory]LocationConsent failed, ULR opt-in status is: false, account#-413941514#\n" +
                "11-04 15:48:26.833  4051  4219 W ctxmgr  : [AclManager]No 2 for (accnt=account#-413941514#, com.google.android.gms(10017):UserLocationProducer, vrsn=9877000, 0, 3pPkg = null ,  3pMdlId = null). Was: 2 for 1, account#-413941514#\n" +
                "11-04 15:48:31.852  4051  4219 W ctxmgr  : [ContextSpecificAclFactory]LocationConsent failed, ULR opt-in status is: false, account#-413941514#\n" +
                "11-04 15:48:31.853  4051  4219 W ctxmgr  : [AclManager]No 2 for (accnt=account#-413941514#, com.google.android.gms(10017):UserLocationProducer, vrsn=9877000, 0, 3pPkg = null ,  3pMdlId = null). Was: 2 for 1, account#-413941514#\n" +
                "11-04 15:48:36.869  4051  4219 W ctxmgr  : [ContextSpecificAclFactory]LocationConsent failed, ULR opt-in status is: false, account#-413941514#\n" +
                "11-04 15:48:36.869  4051  4219 W ctxmgr  : [AclManager]No 2 for (accnt=account#-413941514#, com.google.android.gms(10017):UserLocationProducer, vrsn=9877000, 0, 3pPkg = null ,  3pMdlId = null). Was: 2 for 1, account#-413941514#\n" +
                "11-04 15:48:37.203   952  3070 D WifiStateMachine: starting scan for \"HOPEBAY_COM\"WPA_PSK with 2417\n" +
                "11-04 15:48:41.886  4051  4219 W ctxmgr  : [ContextSpecificAclFactory]LocationConsent failed, ULR opt-in status is: false, account#-413941514#\n" +
                "11-04 15:48:41.887  4051  4219 W ctxmgr  : [AclManager]No 2 for (accnt=account#-413941514#, com.google.android.gms(10017):UserLocationProducer, vrsn=9877000, 0, 3pPkg = null ,  3pMdlId = null). Was: 2 for 1, account#-413941514#\n" +
                "11-04 15:48:46.902  4051  4219 W ctxmgr  : [ContextSpecificAclFactory]LocationConsent failed, ULR opt-in status is: false, account#-413941514#\n" +
                "11-04 15:48:46.902  4051  4219 W ctxmgr  : [AclManager]No 2 for (accnt=account#-413941514#, com.google.android.gms(10017):UserLocationProducer, vrsn=9877000, 0, 3pPkg = null ,  3pMdlId = null). Was: 2 for 1, account#-413941514#\n" +
                "11-04 15:48:51.922  4051  4235 W ctxmgr  : [ContextSpecificAclFactory]LocationConsent failed, ULR opt-in status is: false, account#-413941514#\n" +
                "11-04 15:48:51.922  4051  4235 W ctxmgr  : [AclManager]No 2 for (accnt=account#-413941514#, com.google.android.gms(10017):UserLocationProducer, vrsn=9877000, 0, 3pPkg = null ,  3pMdlId = null). Was: 2 for 1, account#-413941514#\n" +
                "11-04 15:48:56.937  4051  4235 W ctxmgr  : [ContextSpecificAclFactory]LocationConsent failed, ULR opt-in status is: false, account#-413941514#\n" +
                "11-04 15:48:56.937  4051  4235 W ctxmgr  : [AclManager]No 2 for (accnt=account#-413941514#, com.google.android.gms(10017):UserLocationProducer, vrsn=9877000, 0, 3pPkg = null ,  3pMdlId = null). Was: 2 for 1, account#-413941514#\n" +
                "11-04 15:49:01.951  4051  4235 W ctxmgr  : [ContextSpecificAclFactory]LocationConsent failed, ULR opt-in status is: false, account#-413941514#\n" +
                "11-04 15:49:01.952  4051  4235 W ctxmgr  : [AclManager]No 2 for (accnt=account#-413941514#, com.google.android.gms(10017):UserLocationProducer, vrsn=9877000, 0, 3pPkg = null ,  3pMdlId = null). Was: 2 for 1, account#-413941514#\n" +
                "11-04 15:49:06.971  4051  4235 W ctxmgr  : [ContextSpecificAclFactory]LocationConsent failed, ULR opt-in status is: false, account#-413941514#\n" +
                "11-04 15:49:06.972  4051  4235 W ctxmgr  : [AclManager]No 2 for (accnt=account#-413941514#, com.google.android.gms(10017):UserLocationProducer, vrsn=9877000, 0, 3pPkg = null ,  3pMdlId = null). Was: 2 for 1, account#-413941514#\n" +
                "11-04 15:49:11.990  4051  4235 W ctxmgr  : [ContextSpecificAclFactory]LocationConsent failed, ULR opt-in status is: false, account#-413941514#\n" +
                "11-04 15:49:11.990  4051  4235 W ctxmgr  : [AclManager]No 2 for (accnt=account#-413941514#, com.google.android.gms(10017):UserLocationProducer, vrsn=9877000, 0, 3pPkg = null ,  3pMdlId = null). Was: 2 for 1, account#-413941514#\n" +
                "11-04 15:49:17.005  4051  4235 W ctxmgr  : [ContextSpecificAclFactory]LocationConsent failed, ULR opt-in status is: false, account#-413941514#\n" +
                "11-04 15:49:17.005  4051  4235 W ctxmgr  : [AclManager]No 2 for (accnt=account#-413941514#, com.google.android.gms(10017):UserLocationProducer, vrsn=9877000, 0, 3pPkg = null ,  3pMdlId = null). Was: 2 for 1, account#-413941514#\n" +
                "11-04 15:49:17.213   952  3070 D WifiStateMachine: starting scan for \"HOPEBAY_COM\"WPA_PSK with 2417\n" +
                "11-04 15:49:21.598   952  3183 I AccountManagerService: getTypesVisibleToCaller: isPermitted? true\n" +
                "11-04 15:49:22.021  4051  4219 W ctxmgr  : [ContextSpecificAclFactory]LocationConsent failed, ULR opt-in status is: false, account#-413941514#\n" +
                "11-04 15:49:22.022  4051  4219 W ctxmgr  : [AclManager]No 2 for (accnt=account#-413941514#, com.google.android.gms(10017):UserLocationProducer, vrsn=9877000, 0, 3pPkg = null ,  3pMdlId = null). Was: 2 for 1, account#-413941514#\n" +
                "11-04 15:49:22.632   578   578 E QCALOG  : [MessageQ] ProcessNewMessage: [LOWI-SERVER] unknown deliver target [OS-Agent]\n" +
                "11-04 15:49:27.038  4051  4219 W ctxmgr  : [ContextSpecificAclFactory]LocationConsent failed, ULR opt-in status is: false, account#-413941514#\n" +
                "11-04 15:49:27.039  4051  4219 W ctxmgr  : [AclManager]No 2 for (accnt=account#-413941514#, com.google.android.gms(10017):UserLocationProducer, vrsn=9877000, 0, 3pPkg = null ,  3pMdlId = null). Was: 2 for 1, account#-413941514#\n" +
                "11-04 15:49:29.702   952 30557 D NetlinkSocketObserver: NeighborEvent{elapsedMs=109044962, 172.16.31.254, [001485F27E49], RTM_NEWNEIGH, NUD_REACHABLE}\n" +
                "11-04 15:49:32.052  4051  4219 W ctxmgr  : [ContextSpecificAclFactory]LocationConsent failed, ULR opt-in status is: false, account#-413941514#\n" +
                "11-04 15:49:32.052  4051  4219 W ctxmgr  : [AclManager]No 2 for (accnt=account#-413941514#, com.google.android.gms(10017):UserLocationProducer, vrsn=9877000, 0, 3pPkg = null ,  3pMdlId = null). Was: 2 for 1, account#-413941514#\n" +
                "11-04 15:49:37.071  4051  4219 W ctxmgr  : [ContextSpecificAclFactory]LocationConsent failed, ULR opt-in status is: false, account#-413941514#\n" +
                "11-04 15:49:37.072  4051  4219 W ctxmgr  : [AclManager]No 2 for (accnt=account#-413941514#, com.google.android.gms(10017):UserLocationProducer, vrsn=9877000, 0, 3pPkg = null ,  3pMdlId = null). Was: 2 for 1, account#-413941514#\n" +
                "11-04 15:49:42.093  4051  4219 W ctxmgr  : [ContextSpecificAclFactory]LocationConsent failed, ULR opt-in status is: false, account#-413941514#\n" +
                "11-04 15:49:42.094  4051  4219 W ctxmgr  : [AclManager]No 2 for (accnt=account#-413941514#, com.google.android.gms(10017):UserLocationProducer, vrsn=9877000, 0, 3pPkg = null ,  3pMdlId = null). Was: 2 for 1, account#-413941514#\n" +
                "11-04 15:49:47.114  4051  4219 W ctxmgr  : [ContextSpecificAclFactory]LocationConsent failed, ULR opt-in status is: false, account#-413941514#\n" +
                "11-04 15:49:47.115  4051  4219 W ctxmgr  : [AclManager]No 2 for (accnt=account#-413941514#, com.google.android.gms(10017):UserLocationProducer, vrsn=9877000, 0, 3pPkg = null ,  3pMdlId = null). Was: 2 for 1, account#-413941514#\n" +
                "11-04 15:49:48.602   952 30557 D NetlinkSocketObserver: NeighborEvent{elapsedMs=109063863, 172.16.31.254, [001485F27E49], RTM_NEWNEIGH, NUD_STALE}\n" +
                "11-04 15:49:52.135  4051  4219 W ctxmgr  : [ContextSpecificAclFactory]LocationConsent failed, ULR opt-in status is: false, account#-413941514#\n" +
                "11-04 15:49:52.135  4051  4219 W ctxmgr  : [AclManager]No 2 for (accnt=account#-413941514#, com.google.android.gms(10017):UserLocationProducer, vrsn=9877000, 0, 3pPkg = null ,  3pMdlId = null). Was: 2 for 1, account#-413941514#\n" +
                "11-04 15:49:57.148  4051  4219 W ctxmgr  : [ContextSpecificAclFactory]LocationConsent failed, ULR opt-in status is: false, account#-413941514#\n" +
                "11-04 15:49:57.148  4051  4219 W ctxmgr  : [AclManager]No 2 for (accnt=account#-413941514#, com.google.android.gms(10017):UserLocationProducer, vrsn=9877000, 0, 3pPkg = null ,  3pMdlId = null). Was: 2 for 1, account#-413941514#\n" +
                "11-04 15:49:57.222   952  3070 D WifiStateMachine: starting scan for \"HOPEBAY_COM\"WPA_PSK with 2417\n" +
                "11-04 15:50:02.165  4051  4219 W ctxmgr  : [ContextSpecificAclFactory]LocationConsent failed, ULR opt-in status is: false, account#-413941514#\n" +
                "11-04 15:50:02.166  4051  4219 W ctxmgr  : [AclManager]No 2 for (accnt=account#-413941514#, com.google.android.gms(10017):UserLocationProducer, vrsn=9877000, 0, 3pPkg = null ,  3pMdlId = null). Was: 2 for 1, account#-413941514#\n" +
                "11-04 15:50:07.183  4051  4219 W ctxmgr  : [ContextSpecificAclFactory]LocationConsent failed, ULR opt-in status is: false, account#-413941514#\n" +
                "11-04 15:50:07.183  4051  4219 W ctxmgr  : [AclManager]No 2 for (accnt=account#-413941514#, com.google.android.gms(10017):UserLocationProducer, vrsn=9877000, 0, 3pPkg = null ,  3pMdlId = null). Was: 2 for 1, account#-413941514#\n" +
                "11-04 15:50:12.202  4051  4219 W ctxmgr  : [ContextSpecificAclFactory]LocationConsent failed, ULR opt-in status is: false, account#-413941514#\n" +
                "11-04 15:50:12.202  4051  4219 W ctxmgr  : [AclManager]No 2 for (accnt=account#-413941514#, com.google.android.gms(10017):UserLocationProducer, vrsn=9877000, 0, 3pPkg = null ,  3pMdlId = null). Was: 2 for 1, account#-413941514#\n" +
                "11-04 15:50:17.222  4051  4219 W ctxmgr  : [ContextSpecificAclFactory]LocationConsent failed, ULR opt-in status is: false, account#-413941514#\n" +
                "11-04 15:50:17.223  4051  4219 W ctxmgr  : [AclManager]No 2 for (accnt=account#-413941514#, com.google.android.gms(10017):UserLocationProducer, vrsn=9877000, 0, 3pPkg = null ,  3pMdlId = null). Was: 2 for 1, account#-413941514#\n" +
                "11-04 15:50:17.225   952  3070 D WifiStateMachine: starting scan for \"HOPEBAY_COM\"WPA_PSK with 2417\n" +
                "11-04 15:50:21.689   952  3677 I AccountManagerService: getTypesVisibleToCaller: isPermitted? true\n" +
                "11-04 15:50:22.242  4051  4219 W ctxmgr  : [ContextSpecificAclFactory]LocationConsent failed, ULR opt-in status is: false, account#-413941514#\n" +
                "11-04 15:50:22.242  4051  4219 W ctxmgr  : [AclManager]No 2 for (accnt=account#-413941514#, com.google.android.gms(10017):UserLocationProducer, vrsn=9877000, 0, 3pPkg = null ,  3pMdlId = null). Was: 2 for 1, account#-413941514#\n" +
                "11-04 15:50:22.634   578   578 E QCALOG  : [MessageQ] ProcessNewMessage: [LOWI-SERVER] unknown deliver target [OS-Agent]\n" +
                "11-04 15:50:27.257  4051  4219 W ctxmgr  : [ContextSpecificAclFactory]LocationConsent failed, ULR opt-in status is: false, account#-413941514#\n" +
                "11-04 15:50:27.257  4051  4219 W ctxmgr  : [AclManager]No 2 for (accnt=account#-413941514#, com.google.android.gms(10017):UserLocationProducer, vrsn=9877000, 0, 3pPkg = null ,  3pMdlId = null). Was: 2 for 1, account#-413941514#\n" +
                "11-04 15:50:29.761   952 30557 D NetlinkSocketObserver: NeighborEvent{elapsedMs=109105021, 172.16.31.254, [001485F27E49], RTM_NEWNEIGH, NUD_REACHABLE}\n" +
                "11-04 15:50:32.276  4051  4219 W ctxmgr  : [ContextSpecificAclFactory]LocationConsent failed, ULR opt-in status is: false, account#-413941514#\n" +
                "11-04 15:50:32.277  4051  4219 W ctxmgr  : [AclManager]No 2 for (accnt=account#-413941514#, com.google.android.gms(10017):UserLocationProducer, vrsn=9877000, 0, 3pPkg = null ,  3pMdlId = null). Was: 2 for 1, account#-413941514#\n" +
                "11-04 15:50:37.289  4051  4219 W ctxmgr  : [ContextSpecificAclFactory]LocationConsent failed, ULR opt-in status is: false, account#-413941514#\n" +
                "11-04 15:50:37.289  4051  4219 W ctxmgr  : [AclManager]No 2 for (accnt=account#-413941514#, com.google.android.gms(10017):UserLocationProducer, vrsn=9877000, 0, 3pPkg = null ,  3pMdlId = null). Was: 2 for 1, account#-413941514#\n" +
                "11-04 15:50:42.316  4051  4219 W ctxmgr  : [ContextSpecificAclFactory]LocationConsent failed, ULR opt-in status is: false, account#-413941514#\n" +
                "11-04 15:50:42.317  4051  4219 W ctxmgr  : [AclManager]No 2 for (accnt=account#-413941514#, com.google.android.gms(10017):UserLocationProducer, vrsn=9877000, 0, 3pPkg = null ,  3pMdlId = null). Was: 2 for 1, account#-413941514#\n" +
                "11-04 15:50:47.338  4051  4219 W ctxmgr  : [ContextSpecificAclFactory]LocationConsent failed, ULR opt-in status is: false, account#-413941514#\n" +
                "11-04 15:50:47.339  4051  4219 W ctxmgr  : [AclManager]No 2 for (accnt=account#-413941514#, com.google.android.gms(10017):UserLocationProducer, vrsn=9877000, 0, 3pPkg = null ,  3pMdlId = null). Was: 2 for 1, account#-413941514#\n" +
                "11-04 15:50:49.731   952 30557 D NetlinkSocketObserver: NeighborEvent{elapsedMs=109124992, 172.16.31.254, [001485F27E49], RTM_NEWNEIGH, NUD_STALE}\n" +
                "11-04 15:50:52.360  4051  4219 W ctxmgr  : [ContextSpecificAclFactory]LocationConsent failed, ULR opt-in status is: false, account#-413941514#\n" +
                "11-04 15:50:52.361  4051  4219 W ctxmgr  : [AclManager]No 2 for (accnt=account#-413941514#, com.google.android.gms(10017):UserLocationProducer, vrsn=9877000, 0, 3pPkg = null ,  3pMdlId = null). Was: 2 for 1, account#-413941514#\n" +
                "11-04 15:50:57.234   952  3070 D WifiStateMachine: starting scan for \"HOPEBAY_COM\"WPA_PSK with 2417\n" +
                "11-04 15:50:57.379  4051  4219 W ctxmgr  : [ContextSpecificAclFactory]LocationConsent failed, ULR opt-in status is: false, account#-413941514#\n" +
                "11-04 15:50:57.379  4051  4219 W ctxmgr  : [AclManager]No 2 for (accnt=account#-413941514#, com.google.android.gms(10017):UserLocationProducer, vrsn=9877000, 0, 3pPkg = null ,  3pMdlId = null). Was: 2 for 1, account#-413941514#\n" +
                "11-04 15:51:02.394  4051  4219 W ctxmgr  : [ContextSpecificAclFactory]LocationConsent failed, ULR opt-in status is: false, account#-413941514#\n" +
                "11-04 15:51:02.395  4051  4219 W ctxmgr  : [AclManager]No 2 for (accnt=account#-413941514#, com.google.android.gms(10017):UserLocationProducer, vrsn=9877000, 0, 3pPkg = null ,  3pMdlId = null). Was: 2 for 1, account#-413941514#\n" +
                "11-04 15:51:07.412  4051  4219 W ctxmgr  : [ContextSpecificAclFactory]LocationConsent failed, ULR opt-in status is: false, account#-413941514#\n" +
                "11-04 15:51:07.412  4051  4219 W ctxmgr  : [AclManager]No 2 for (accnt=account#-413941514#, com.google.android.gms(10017):UserLocationProducer, vrsn=9877000, 0, 3pPkg = null ,  3pMdlId = null). Was: 2 for 1, account#-413941514#\n" +
                "11-04 15:51:12.425  4051  4219 W ctxmgr  : [ContextSpecificAclFactory]LocationConsent failed, ULR opt-in status is: false, account#-413941514#\n" +
                "11-04 15:51:12.426  4051  4219 W ctxmgr  : [AclManager]No 2 for (accnt=account#-413941514#, com.google.android.gms(10017):UserLocationProducer, vrsn=9877000, 0, 3pPkg = null ,  3pMdlId = null). Was: 2 for 1, account#-413941514#\n" +
                "11-04 15:51:17.239   952  3070 D WifiStateMachine: starting scan for \"HOPEBAY_COM\"WPA_PSK with 2417\n" +
                "11-04 15:51:17.439  4051  4219 W ctxmgr  : [ContextSpecificAclFactory]LocationConsent failed, ULR opt-in status is: false, account#-413941514#\n" +
                "11-04 15:51:17.439  4051  4219 W ctxmgr  : [AclManager]No 2 for (accnt=account#-413941514#, com.google.android.gms(10017):UserLocationProducer, vrsn=9877000, 0, 3pPkg = null ,  3pMdlId = null). Was: 2 for 1, account#-413941514#\n" +
                "11-04 15:51:21.774   952  3687 I AccountManagerService: getTypesVisibleToCaller: isPermitted? true\n" +
                "11-04 15:51:22.454  4051  4235 W ctxmgr  : [ContextSpecificAclFactory]LocationConsent failed, ULR opt-in status is: false, account#-413941514#\n";
        Timestamp timestamp = new Timestamp(new Date().getTime());
        for(String line : test.split("\n")) {
            String[] result = parse(line);
            assertEquals(result.toString(), 8,  result.length);
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd hh:mm:ss.SSS");
            Date parsedDate = dateFormat.parse(result[0] + " " + result[1]);
            assertFalse(new Timestamp(parsedDate.getTime()).after(timestamp));
        }
    }

    @Test
    public void logContent() {
        String testLogcatLine = "11-04 15:47:21.595  4051  4235 W ctxmgr  : ttedd@#$%:::  :/";
        assertEquals("ttedd@#$%:::  :/", LogcatUtils.retrieveLogContent(testLogcatLine));
    }

    public String[] parse(String logcatLine) {
        //11-04 13:45:18.483 4051  4235 W ctxmgr  : [AclManager]No 2
        Pattern emptyPattern = Pattern.compile(" +");
        String[] element = emptyPattern.split(logcatLine, 8);
        return element;
    }
}