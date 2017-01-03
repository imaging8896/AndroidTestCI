package hcfs.test.utils.test;

import com.hopebaytech.hcfsmgmt.utils.Logs;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import hcfs.test.config.Path;
import hcfs.test.utils.FileUtils;
import hcfs.test.utils.FilesManager;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class FileUtilsTest {
    private static final String THIS_CLASS = FileUtilsTest.class.getSimpleName();

    private static final String APP_DIR = "/data/data/";
    private static final String LINKED_APP = "com.trello";
    private static final String LINKED_APP_PATH = APP_DIR + LINKED_APP;
    private static final String INSTALLED_APP = "hcfs.test.testsuite";
    private static final String INSTALLED_APP_PATH = APP_DIR + INSTALLED_APP;
    private static final String BOOST_MOUNT_PATH = "/data/mnt/hcfsblock";

    private FilesManager localFilesMgr;
    private String testFile;

    @Before
    public void setUp() throws Exception {
        Logs.d(THIS_CLASS, "setUp", "");
        localFilesMgr = new FilesManager(Path.APK_PATH, Path.L_TEST_DIR);
        testFile = localFilesMgr.genFileWithName("testFile", 1024L * 1024L * 1L);
    }

    @After
    public void tearDown() throws Exception {
        Logs.d(THIS_CLASS, "tearDown", "");
        localFilesMgr.cleanup();
    }

    @Test
    public void testIsDir() {
        Logs.d(THIS_CLASS, "testIsDir", "1");
        assertTrue(FileUtils.isDir(INSTALLED_APP_PATH));
        Logs.d(THIS_CLASS, "testIsDir", "2");
        assertTrue(FileUtils.isDir(BOOST_MOUNT_PATH));
        Logs.d(THIS_CLASS, "testIsDir", "3");
        assertFalse(FileUtils.isDir(testFile + "ssss"));
        Logs.d(THIS_CLASS, "testIsDir", "4");
        assertFalse(FileUtils.isDir(testFile));
    }

    @Test
    public void testIsFile() {
        Logs.d(THIS_CLASS, "testIsFile", "1");
        assertTrue(FileUtils.isFile(testFile));
        assertFalse(FileUtils.isFile(testFile + "ssss"));
        assertFalse(FileUtils.isFile(BOOST_MOUNT_PATH));
    }

    @Test
    public void testIsLink() {
        Logs.d(THIS_CLASS, "testIsLink", "1");
        assertTrue(FileUtils.isLink(LINKED_APP_PATH));
        assertFalse(FileUtils.isLink(INSTALLED_APP_PATH));
        assertFalse(FileUtils.isLink(testFile));
        assertFalse(FileUtils.isLink(testFile + "asd"));
    }

    @Test
    public void testIsDirty() throws Exception {
        Logs.d(THIS_CLASS, "testIsDirty", "1");
        String test1File = localFilesMgr.genFileWithName("test1File", 1L);
        String test1GFile = localFilesMgr.genFileWithName("test1GFile", 1024L * 1024L * 1000L);
        assertTrue(FileUtils.isDirty(test1GFile));

        int waitSec = 120;
        while(waitSec-- > 0) {
            if(!FileUtils.isDirty(test1File))
                return;
            sleep(1);
        }
        fail();
    }

    private void sleep(int timeoutSec) {
        try {Thread.sleep(timeoutSec * 1000);} catch (InterruptedException e) {throw new RuntimeException(e);}
    }
}
