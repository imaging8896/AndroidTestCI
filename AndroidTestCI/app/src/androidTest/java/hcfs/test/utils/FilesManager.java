package hcfs.test.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class FilesManager {

	public static final int DATA1 = 1;
	public static final int DATA2 = 2;

	private static final int BUF_SIZE = 1048576;
	private static final int RAND_SEED1 = 40;
	private static final int RAND_SEED2 = 50;

	private String rootDirPath;
	private File rootDir;
	private int fileNum;
	private byte[] fillData1 = new byte[BUF_SIZE];
	private byte[] fillData2 = new byte[BUF_SIZE];

	public FilesManager(String path, String newDirName) {
		File file = new File(path);
		if(file.exists() && file.isDirectory()) {
			String root = path + File.separator + newDirName;
			file = new File(root);
			if(!file.exists()) {
				if(!file.mkdir())
					throw new RuntimeException("Fail to create root file");
			} else {
				if(file.isFile())
					throw new RuntimeException("input path and name is a file.");
			}
			this.rootDirPath = root;
			this.rootDir = file;
			this.fileNum = 0;
			cleanFiles();
			new Random(RAND_SEED1).nextBytes(fillData1);
			new Random(RAND_SEED2).nextBytes(fillData2);
		} else
			throw new RuntimeException("no such directory <" + path + ">");
	}

	public void genSparseFile(int number, long size) {
		try {
			for (File file : getFiles(1, number)) {
				if(!file.createNewFile())
					throw new RuntimeException("(genSparseFile)Fail to create file.");
				RandomAccessFile rf = new RandomAccessFile(file, "rw");
				rf.setLength(size);
				rf.close();
				if (!file.exists() || !file.isFile())
					throw new RuntimeException("File <" + file.getName() + "> doesn't exist in <" + rootDirPath + ">, failed to create");
				if (file.length() != size)
					throw new RuntimeException("File <" + file.getName() + "> size <" + file.length() + "> doesn't match <" + size + ">");
			}
			fileNum = number;
		} catch (IOException ioe) {
			throw new RuntimeException(ioe);
		}
	}

	public String genFileInLevel(int level, long size) {
		String filePath = genDirInLevel(level) + File.separator + "level.file";
		File levelFile = new File(filePath);
		writeFile(levelFile, size, fillData1);
		return filePath;
	}

	public String genDirInLevel(int level) {
		if(level <= 0)
			throw new RuntimeException("Level should be greater than 0.");
		String path = rootDirPath;
		for (int i = 1; i <= level; i++)
			path += File.separator + "dir" + i;
		if(!new File(path).mkdirs())
			throw new RuntimeException("Fail to mkdirs with path(" + path + ") level(" + level + ")");
		return path;
	}

	public String genDirWithName(String dirName) {
		String path = rootDirPath + File.separator + dirName;
		if(!new File(path).mkdirs())
			throw new RuntimeException("Fail to mkdirs with path(" + path + ")");
		return path;
	}

	public String genFileWithName(String fileName, long size) {
		String path = rootDirPath + File.separator + fileName;
		writeFile(new File(path), size, fillData1);
		return path;
	}

	public void genFiles(int number, long size) {
		fileNum = number;
		genFilesBetween(1, fileNum, size, fillData1);
	}

	public void continueGenFiles(int number, long size) {
		int maxExistedNum = fileNum;
		fileNum = fileNum + number;
		genFilesBetween(maxExistedNum + 1, fileNum, size, fillData1);
	}

	/**
	 *
	 * @param fromFileNum inclusive
	 * @param toFileNum inclusive
	 * @param fillDataType DATA1 or DATA2
     */
	public void modifyFilesBetween(int fromFileNum, int toFileNum, int fillDataType) {
		if(fromFileNum > toFileNum)
			throw new RuntimeException("'fromFileNum' must be less than 'toFileNum'");
		if(fromFileNum <= 0  || fromFileNum > fileNum)
			throw new RuntimeException("'fromFileNum' must be between 1~current file number(both inclusive)");
		if(toFileNum <= 0  || toFileNum > fileNum)
			throw new RuntimeException("'toFileNum' must be between 1~current file number(both inclusive)");
		if(fillDataType != DATA1  && fillDataType != DATA2)
			throw new RuntimeException("'fillDataType' must be one of (DTAD1, DATA2)");
		long size = new File(getFilePath(fromFileNum)).length();
		genFilesBetween(fromFileNum, toFileNum, size, fillDataType == DATA1 ? fillData1 : fillData2);
	}

	public void cleanup() {
		deleteRecursively(rootDir);
	}

	public void cleanFiles() {
		deleteRecursively(rootDir);
		if(!rootDir.mkdir())
			throw new RuntimeException("Fail to re-create root dir");
	}

	public String  getRootPath() {
		return rootDirPath;
	}

	public String  getFilePath(int num) {
		return rootDirPath + File.separator + getFileName(num);
	}

	public int getMaxFileNum() {
		return fileNum;
	}

	public String getFileName(int num) {
		return "test" + num + ".file";
	}

	public static void deleteRecursively(File file) {
		if(!file.exists())
			return;
		if(file.isDirectory()) {
			for(File child : Arrays.asList(file.listFiles()))
				deleteRecursively(child);
		}
		if(!file.delete())
			throw new RuntimeException("Fail to delete file:" + file.getAbsoluteFile());
	}

	private void genFilesBetween(int fromFileNum, int toFileNum, long size, byte[] fillData) {
		for (File file : getFiles(fromFileNum, toFileNum)) {
			writeFile(file, size, fillData);
			if (!file.exists() || !file.isFile())
				throw new RuntimeException("File <" + file.getName() + "> doesn't exist in <" + rootDirPath + ">, failed to create");
			if (file.length() != size)
				throw new RuntimeException("File <" + file.getName() + "> size <" + file.length() + "> doesn't match <" + size + ">");
		}
	}

	private void writeFile(File file, long requestSize, byte[] fillData) {
		try(BufferedOutputStream bos =
					new BufferedOutputStream(new FileOutputStream(file))) {
			while (requestSize > BUF_SIZE) {
                bos.write(fillData);
                requestSize -= BUF_SIZE;
            }
			bos.write(Arrays.copyOfRange(fillData, 0, (int) requestSize));
		} catch (IOException e) {
			// TODO maybe fill up cache need this
//			if("write failed: ENOSPC (No space left on device)".equals(e.getMessage()))
//				throw new NoSpaceException(e);
			throw new RuntimeException(e);
		}
	}

	//file number from 1
	private List<File> getFiles(int fromFileNum, int toFileNum) {
		List<File> files = new ArrayList<>();
		for (int i = fromFileNum; i <= toFileNum; i++) {
			File file = new File(rootDirPath + File.separator + getFileName(i));
			files.add(file);
		}
		return files;
	}
}
