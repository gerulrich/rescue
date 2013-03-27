package net.cloudengine.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import net.cloudengine.api.BlobStore;
import net.cloudengine.model.commons.FileDescriptor;

import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZipUtils {
	
	private static Logger logger = LoggerFactory.getLogger(ZipUtils.class);
	private static final int BUFFER = 4096;	
	
	/**
	 * Descomprime un archivo en formato zip que se encuentra
	 * guardado en la base de datos de mongodb.
	 * @param descriptor
	 * @return
	 */
	public static String[] unzipp(FileDescriptor descriptor, BlobStore blobStore) {
		
		List<String> createdFiles = new ArrayList<String>();
		
		FileInputStream fin = null;
		ZipInputStream zin = null;
		File tempFile = null;
		
		try {
			
			tempFile = File.createTempFile("tempfile", "tmp");
			OutputStream strem = new FileOutputStream(tempFile);
			blobStore.retrieveFile(new ObjectId(descriptor.getFileId()), strem);

			long start = System.currentTimeMillis();
		
			fin = new FileInputStream(tempFile);
			zin = new ZipInputStream(fin);
			
			ZipEntry ze = null;
		
			while ((ze = zin.getNextEntry()) != null) {
				
				if (ze.isDirectory()) {
					continue;
				}
				
				createdFiles.add(decompressEntry(zin, ze));

			}

		    if (logger.isDebugEnabled()) {
		    	double time = (System.currentTimeMillis() - start)/1000d;
		    	logger.debug("Tiempo transcurrido para descomprimir archivo: {}", time);
		    }
			
		} catch (IOException e) {
			logger.error("Error al descomprimir archivo zip", e);
		} finally {
			IOUtils.closeQuietly(zin);
			IOUtils.closeQuietly(fin);
			if (tempFile != null) {
				tempFile.delete();
			}
		}
		
		return createdFiles.toArray(new String[0]);
	}
	
	/**
	 * Descomprime un archivo contenido dentro del archivo zip.
	 * @param zin
	 * @param ze
	 * @return
	 */
	private static String decompressEntry(ZipInputStream zin, ZipEntry ze) {
		String name = ze.getName();
		
		if (logger.isDebugEnabled()) {
			logger.debug("Descomprimiendo archivo: {}", name);
		}
		
		FileOutputStream fout = null;
		BufferedOutputStream bos = null;
		
		try {
			
			fout = new FileOutputStream(ze.getName());
			bos = new BufferedOutputStream(fout, BUFFER);
			
			int count;
			byte data[] = new byte[BUFFER];
		
			while ((count = zin.read(data, 0, BUFFER))!= -1) {
			       bos.write(data, 0, count);
			}
			
			zin.closeEntry();			
			
		} catch (IOException ioe) {
			
		} finally {
			
			IOUtils.closeQuietly(bos);
			IOUtils.closeQuietly(fout);
			
		}
        
        return name;
	}
	
	/**
	 * @param files
	 * @param extension
	 * @return
	 */
	public static String findByName(String[] files, String extension) {
		String fileName = null;
		for(String f : files) {
	    	if (f.endsWith(extension)) {
	    		fileName = f;
	    		break;
	    	}
	    }
		return fileName;
	}
	
	public static void deleteFiles(String files[]) {
		for(String file : files) {
			boolean deleted = new File(file).delete();
			if (logger.isDebugEnabled()) {
		    	logger.debug("Borrando archivo {}, {}", file, deleted);
		    }
		}
	}	

}
