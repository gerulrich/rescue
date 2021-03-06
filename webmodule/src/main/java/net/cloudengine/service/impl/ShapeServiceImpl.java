package net.cloudengine.service.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import net.cloudengine.dao.jpa.StreetBlockRepository;
import net.cloudengine.dao.jpa.ZoneRepository;
import net.cloudengine.dao.support.BlobStore;
import net.cloudengine.dao.support.Repository;
import net.cloudengine.dao.support.RepositoryLocator;
import net.cloudengine.model.commons.FileDescriptor;
import net.cloudengine.model.geo.POI;
import net.cloudengine.model.geo.StreetBlock;
import net.cloudengine.model.geo.Zone;
import net.cloudengine.service.ShapefileService;
import net.cloudengine.web.map.ShapeController;
import net.dbf.DBFReader;
import net.dbf.JDBFException;
import net.dbf.TableDescriptor;
import net.shapefile.Point;
import net.shapefile.ShapeObject;
import net.shapefile.ShapefileReader;
import net.shapefile.geometry.WKTUtils;

import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.io.IOUtils;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ShapeServiceImpl implements ShapefileService {

	private static Logger logger = LoggerFactory.getLogger(ShapeController.class);
	private static final int BUFFER = 4096;
	
	private BlobStore blobStore;
	private Repository<POI, ObjectId> poiRepository;
	private StreetBlockRepository streetDao;
	private ZoneRepository zoneRepository;
	
	
	@Autowired
	public ShapeServiceImpl(BlobStore blobStore, RepositoryLocator repositoryLocator,
			StreetBlockRepository streetDao, ZoneRepository zoneRepository) {
		super();
		this.blobStore = blobStore;
		this.poiRepository = repositoryLocator.getRepository(POI.class);
		this.streetDao = streetDao;
		this.zoneRepository = zoneRepository;
	}
	
	public long shp2Poi(FileDescriptor descriptor, String nameField, String typeField, boolean overwrite, ProgressListener listener) {
		long count = 0;
		String files[] = unzipp(descriptor);
		String shapeFileName = findByExtension(files, "shp");
		
		if (shapeFileName != null) {
		   	ShapefileReader reader = new ShapefileReader(shapeFileName.replaceAll("\\.shp", ""));
		   	
		   	if (overwrite) {
		   		poiRepository.deleteAll();
		   	}
		   	
		   	while (reader.hasNext()) {
		    		
		   		ShapeObject obj = reader.next();
		   		Point point = obj.getPoint(0);
		    		
		   		POI poi = new POI();
		   		poi.setName(obj.getRecord().getField(nameField).getValue());
		   		poi.setType(obj.getRecord().getField(typeField).getValue());
		   		poi.setX(point.getX());
		   		poi.setY(point.getY());
		    		
		   		poiRepository.save(poi);
		   		
		   		if (listener != null) {
	   				listener.update(reader.getBytesReaded(), reader.getFileLenght(), 1);
	   			}
		   		
		   		count++;
		   	}
		}
		
		deleteFiles(files);
		
		return count;
	}
	
	@Transactional
	public long shp2Street(FileDescriptor descriptor, Map<String,String> fieldNames, boolean overwrite, ProgressListener listener) {
		long count = 0;
		String files[] = unzipp(descriptor);
		String shapeFileName = findByExtension(files, "shp");
		
		boolean firtRecord = true;
		
		
		if (shapeFileName != null) {
		   	ShapefileReader reader = new ShapefileReader(shapeFileName.replaceAll("\\.shp", ""));
		   	
		   	while (reader.hasNext()) {
		    		
		   		ShapeObject obj = reader.next();
		   		
		   		// Verifico que sea de tipo polyline.
		   		if (obj.getType() == 3) {
		    		
		   			if (firtRecord && overwrite) {
		   				streetDao.deleteAll();
		   				firtRecord = false;
		   			}
		   			
		   			String nameField = fieldNames.get(NOMBRE); 
		   			String typeField = fieldNames.get(TIPO);
		   			String fromLeftField = fieldNames.get(ALT_II); 
		   			String toLeftField = fieldNames.get(ALT_IF);
		   			String fromRightField = fieldNames.get(ALT_DI);
		   			String toRightField = fieldNames.get(ALT_DF);
		   			String vstart = fieldNames.get(VINICIO);
		   			String vend = fieldNames.get(VFIN);
		   			
		   			StreetBlock street = new StreetBlock();
		   			street.setName(obj.getRecord().getField(nameField).getValue());
		   			street.setType(obj.getRecord().getField(typeField).getValue());
		   			
		   			street.setFromLeft(Integer.parseInt(obj.getRecord().getField(fromLeftField).getValue()));
		   			street.setToLeft(Integer.parseInt(obj.getRecord().getField(toLeftField).getValue()));
		   					
		   			street.setFromRight(Integer.parseInt(obj.getRecord().getField(fromRightField).getValue()));
		   			street.setToRight(Integer.parseInt(obj.getRecord().getField(toRightField).getValue()));
		   			
		   			street.setVstart(Integer.parseInt(obj.getRecord().getField(vstart).getValue()));
		   			street.setVend(Integer.parseInt(obj.getRecord().getField(vend).getValue()));
		   			
		   			street.setGeom(WKTUtils.toGeometry(obj));
		   			
		   			streetDao.save(street);
		   			count++;
		   			if (listener != null) {
		   				listener.update(reader.getBytesReaded(), reader.getFileLenght(), 1);
		   			}
		   		}
		   	}
		}
		
		deleteFiles(files);
		return count;
	}
	
	@Transactional
	public long shp2Zone(FileDescriptor descriptor, String nameField, String type, boolean overwrite, ProgressListener listener) {
		long count = 0;
		String files[] = unzipp(descriptor);
		String shapeFileName = findByExtension(files, "shp");
		
		boolean firtRecord = true;
		
		if (shapeFileName != null) {
		   	ShapefileReader reader = new ShapefileReader(shapeFileName.replaceAll("\\.shp", ""));
		   	
		   	while (reader.hasNext()) {

		   		ShapeObject obj = reader.next();
		   		
		   		// Verifico que sea de tipo polygon.
		   		if (obj.getType() == 5) {
		    		
		   			if (firtRecord && overwrite) {
		   				zoneRepository.deleteAll();
		   				firtRecord = false;
		   			}
		   			
		   			Zone zone = new Zone();
		   			zone.setName(obj.getRecord().getField(nameField).getValue());
		   			zone.setType(type);
		   			zone.setGeom(WKTUtils.toGeometry(obj));
		   			zoneRepository.save(zone);
		   			
		   			if (listener != null) {
		   				listener.update(reader.getBytesReaded(), reader.getFileLenght(), 1);
		   			}
		   			count++;
		   		}
		   		
		   	}
		}
		
		deleteFiles(files);
		return count;
		
	}
	
	/**
	 * Descomprime un archivo en formato zip que se encuentra
	 * guardado en la base de datos de mongodb.
	 * @param descriptor
	 * @return
	 */
	private String[] unzipp(FileDescriptor descriptor) {
		
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
	private String decompressEntry(ZipInputStream zin, ZipEntry ze) {
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
	
	private void deleteFiles(String files[]) {
		for(String file : files) {
			boolean deleted = new File(file).delete();
			if (logger.isDebugEnabled()) {
		    	logger.debug("Borrando archivo {}, {}", file, deleted);
		    }
		}
	}

	@Override
	public String[] readFileFields(FileDescriptor descriptor) {
		String files[] = unzipp(descriptor);
		String dbfFileName = findByExtension(files, "dbf");
		
		TableDescriptor td[] = null;
		String fileds[] = null;
		
		DBFReader reader = null;
		
		if (dbfFileName != null) {
			try {
				reader = new DBFReader(dbfFileName);
				td = new TableDescriptor[reader.getFieldCount()];
				fileds = new String[reader.getFieldCount()];
				for(int i = 0; i < td.length; i++) {
					td[i] = reader.getField(i);
					fileds[i] = reader.getField(i).getName();
				}
				
			} catch (JDBFException e) {

				logger.error("Error al leer los header del archivo dbf", e);
				
			} finally {
				IOUtils.closeQuietly(reader);
			}
		}
		deleteFiles(files);
		
		return fileds;
	}

	private String findByExtension(String[] files, String extension) {
		String dbfFileName = null;
		for(String f : files) {
	    	if (f.endsWith(extension)) {
	    		dbfFileName = f;
	    		break;
	    	}
	    }
		return dbfFileName;
	}
	
}
