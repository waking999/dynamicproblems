package au.edu.cdu.dynamicproblems.io;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.junit.Test;

import junit.framework.Assert;

public class FileOperationTest {
	//@Ignore
	@Test
	public void testRetriveProblemInfoByEdgePair()
			throws FileNotFoundException, IOException {
		FileInfo fi = new FileInfo();
		fi.setInputFile("src/test/resources/KONECT/000027_zebra.konet");
		FileOperation fo = new FileOperation();
		fo.setFileInfo(fi);
		fo.retriveProblemInfoByEdgePair();

		List<String[]> am = fo.getAdjacencyMatrix();

		Assert.assertEquals(FileOperation.CONNECTED, am.get(0)[1]);
		Assert.assertEquals(FileOperation.UNCONNECTED, am.get(0)[15]);
	}

	//@Ignore
	@Test
	public void testRetriveProblemInfo() throws FileNotFoundException,
			IOException {
		FileInfo fi = new FileInfo();
		fi.setInputFile("src/test/resources/50_0.3_testcase_a.csv");
		FileOperation fo = new FileOperation();
		fo.setFileInfo(fi);
		fo.retriveProblemInfo();

		List<String[]> am = fo.getAdjacencyMatrix();

		Assert.assertEquals(FileOperation.CONNECTED, am.get(1)[2]);
		Assert.assertEquals(FileOperation.UNCONNECTED, am.get(0)[2]);
	}
}
