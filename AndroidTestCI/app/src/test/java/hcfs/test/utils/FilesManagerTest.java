package hcfs.test.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.io.File;

public class FilesManagerTest {

    @Test
    public void genDirInLevel() throws Exception{
        FilesManager filesMgr = new FilesManager(".", "testdir");
        String path = filesMgr.genDirInLevel(6);
        assertEquals("./testdir/dir1/dir2/dir3/dir4/dir5/dir6", path);
        assertTrue(new File(path).exists());
        assertTrue(new File(path).isDirectory());
        filesMgr.cleanup();
    }

    @Test
    public void genFileInLevel() throws Exception{
        FilesManager filesMgr = new FilesManager(".", "testdir");
        String path = filesMgr.genFileInLevel(6, 2 * 1024 * 1024);
        assertEquals("./testdir/dir1/dir2/dir3/dir4/dir5/dir6/level.file", path);
        assertTrue(new File(path).exists());
        assertTrue(new File(path).isFile());
        filesMgr.cleanup();
    }

    @Test
    public void genDirWithName() throws Exception{
        FilesManager filesMgr = new FilesManager(".", "testdir");
        String path = filesMgr.genDirWithName("asdasdasdad");
        File newDir = new File(path);
        assertTrue(newDir.exists());
        assertTrue(newDir.isDirectory());
        assertEquals("asdasdasdad", newDir.getName());
        filesMgr.cleanup();
    }

    @Test
    public void genFileWithName() throws Exception{
        FilesManager filesMgr = new FilesManager(".", "testdir");
        String path = filesMgr.genFileWithName("asdasdasdad", 2 *1024 *1024);
        File newFile = new File(path);
        assertTrue(newFile.exists());
        assertEquals("asdasdasdad", newFile.getName());
        assertTrue(newFile.isFile());
        filesMgr.cleanup();
    }
}
