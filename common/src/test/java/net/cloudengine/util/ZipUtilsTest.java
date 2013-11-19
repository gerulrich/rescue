package net.cloudengine.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.junit.Assert;
import org.junit.Test;

public class ZipUtilsTest {

	static {
		TestCoverageUtil.reflectiveInvokePrivateConstructor(ZipUtils.class);
	}
	
	@Test
	public void testUncompressFile() throws IOException {
		File file = createZipFileWithDirectory(2048);
		FileInputStream is = new FileInputStream(file);
		String files[] = ZipUtils.unzipp(is);
		Assert.assertNotNull(files);
		Assert.assertEquals(1, files.length);
		Assert.assertTrue(files[0].startsWith("random.txt"));
		file.delete();
		ZipUtils.deleteFiles(files);
	}
	
	@Test
	public void testFindByName() throws IOException {
		File file = createZipFileWithDirectory(2048);
		FileInputStream is = new FileInputStream(file);
		String files[] = ZipUtils.unzipp(is);
		Assert.assertNotNull(files);
		Assert.assertEquals("random.txt", ZipUtils.findByName(files, "txt"));
		
		file.delete();
		ZipUtils.deleteFiles(files);
	}
	
    private File createZipFileWithDirectory(int uncompressedSize) {
    	try {
    		File result = File.createTempFile("ZipFileTest", "zip");
    		result.deleteOnExit();

    		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(result));
    		ZipEntry entry = new ZipEntry("random.txt");
    		out.putNextEntry(entry);

    		byte[] writeBuffer = new byte[8192];
    		Random random = new Random();
    		for (int i = 0; i < uncompressedSize; i += writeBuffer.length) {
    			random.nextBytes(writeBuffer);
    			out.write(writeBuffer, 0, Math.min(writeBuffer.length, uncompressedSize - i));
    		}
    		
    		ZipEntry entry2 = new ZipEntry("folder/");
    		out.putNextEntry(entry2);

    		out.closeEntry();
    		out.close();
    		return result;
    	} catch(IOException e) {
    		UncheckedThrow.throwUnchecked(e);
    	}
    	return null;
    }

}
