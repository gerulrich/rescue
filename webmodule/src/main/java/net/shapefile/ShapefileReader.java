package net.shapefile;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import net.dbf.DBFReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShapefileReader {

	private static final Logger logger = LoggerFactory.getLogger(ShapefileReader.class);
	
	private static final int SHAPE_FILE_VERSION = 1000;
	private static final int SHAPE_MAGIC_NUMBER = 9994;
	
	public static final int SHAPETYPE_POINT      = 1;
	public static final int SHAPETYPE_POLYLINE   = 3;
	public static final int SHAPETYPE_POLYGON    = 5;
	public static final int SHAPETYPE_MULTIPOINT = 8;

	public static final int FIELDTYPE_CHARACTER  = 101;
	public static final int FIELDTYPE_NUMBER     = 102;
	public static final int FIELDTYPE_FLOAT      = 103;
	public static final int FIELDTYPE_DATE       = 104;
	public static final int FIELDTYPE_LOGICAL    = 105;
	
	private static final String[] SHAPEFILE_TYPES = {"Null Shape", "Point", "", "PolyLine", "", "Polygon", "", "", "MultiPoint"};

	protected BoundingBox boundingBox = null;
	private int type;
	private ArrayList<String> messages = new ArrayList<String>();
	
	private FileInputStream shpInputStream = null;
	private DataInputStream shpDataIs = null;
	private boolean eofReached = false;
	
	private ShapeObject shapeobject = null;
	
	private DBFReader dbf;
	
	public ShapefileReader(String s) {

		try {
			shpInputStream = new FileInputStream(s + ".shp");		
			shpDataIs = new DataInputStream(shpInputStream);
			readFileHeader(shpDataIs);
			
			dbf = new DBFReader(s+".dbf");
		
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void readFileHeader(DataInputStream shpDataIs) throws IOException {
		
		int fileCode = shpDataIs.readInt();
		if (fileCode != SHAPE_MAGIC_NUMBER)
			throw new InvalidFileException(fileCode + " is not a valid file code.");
		
		shpDataIs.skipBytes(20); // Bytes not used
		shpDataIs.skipBytes(4); // File length
		
		int fileVersion = swapBytes(shpDataIs.readInt());
		if (fileVersion != SHAPE_FILE_VERSION)
			throw new InvalidFileException(fileVersion + " is not a valid file version.");
		
		type = swapBytes(shpDataIs.readInt());
		
		if (logger.isDebugEnabled()) {
			logger.debug("ShapeFile Header, Type {}", SHAPEFILE_TYPES[type]);
		}
		
		boundingBox = readBoundingBox(shpDataIs);		
		shpDataIs.skip(32L); // Bytes not used
	}
	
	public boolean hasNext() {
		
		if (!eofReached) {
			
			shapeobject = null;
			switch (type) {
				
				case SHAPETYPE_POINT:
					shapeobject = readPoint(shpDataIs);
					break;

				case SHAPETYPE_MULTIPOINT:
					shapeobject = readMultipoint(shpDataIs);
					break;

				case SHAPETYPE_POLYLINE:
					shapeobject = readPolyline(shpDataIs);
					break;

				case SHAPETYPE_POLYGON:
					shapeobject = readPolygon(shpDataIs);
					break;
			}
			
			if (eofReached) {
				closeAllFiles();
			} else {
				try {
					shapeobject.setRecord(dbf.next());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			return shapeobject != null;
			
		} else {
			return false;
		}
	}
	
	private void closeAllFiles() {
		if (logger.isDebugEnabled()) {
			logger.info("closing files...");
		}
		try {
			shpDataIs.close();
			shpInputStream.close();
			dbf.close();
		} catch (IOException e) {
			logger.error("Error al cerrar los archivos", e);
		}
	}
	
	public ShapeObject next() {
		return shapeobject;
	}
	
	private ShapeObject readPoint(DataInputStream datainputstream) {

		try {

			int shapeType = readRecordHeader(datainputstream);
			
			if (shapeType != SHAPETYPE_POINT) // FIXME
				messages.add("Invalid shape type in shape record: Shape record type "+ shapeType + " in file of type " + getType());
			
			ShapeObject shapeobject = new ShapeObject(SHAPETYPE_POINT);
			Point point = new Point();
			point.setX(swapBytes(datainputstream.readDouble()));
			point.setY(swapBytes(datainputstream.readDouble()));
			shapeobject.addPoint(point);
			return shapeobject;

		} catch (EOFException eofexception) {
			eofReached = true;
			return null;
		} catch (IOException ioe) {
			throw new RuntimeException(ioe);
		}

	}

	private ShapeObject readPolyline(DataInputStream datainputstream) {

		try {

			int shapeType = readRecordHeader(datainputstream);			
			
			if (shapeType != SHAPETYPE_POLYLINE)
				messages.add("Invalid shape type in shape record: Shape record type "+ shapeType + " in file of type " + getType());
			
			ShapeObject shapeobject = new ShapeObject(SHAPETYPE_POLYLINE);
			shapeobject.setBoundingBox(readBoundingBox(datainputstream));
			
			int numParts = swapBytes(datainputstream.readInt());
			int numPoints = swapBytes(datainputstream.readInt());
			
			// read parts
			for (int i = 0; i < numParts; i++) {
				int part = swapBytes(datainputstream.readInt());
				shapeobject.addPart(part);
			}

			// read points
			for (int i = 0; i < numPoints; i++) {
				Point point = new Point();
				point.setX(swapBytes(datainputstream.readDouble()));
				point.setY(swapBytes(datainputstream.readDouble()));
				shapeobject.addPoint(point);
			}	
			
			return shapeobject;

		} catch (EOFException eofexception) {
			eofReached = true;
			return null;
		}  catch (IOException ioe) {
			throw new RuntimeException(ioe);
		}

	}

	private ShapeObject readMultipoint(DataInputStream datainputstream) {

		try {

			int shapeType = readRecordHeader(datainputstream);
			
			if (shapeType != SHAPETYPE_MULTIPOINT)
				messages.add("Invalid shape type in shape record: Shape record type "+ shapeType + " in file of type " + getType());
			
			ShapeObject shapeobject = new ShapeObject(SHAPETYPE_MULTIPOINT);
			shapeobject.setBoundingBox(readBoundingBox(datainputstream));
			
			int numPoints = swapBytes(datainputstream.readInt());
			for (int i1 = 0; i1 < numPoints; i1++) {
				Point point = new Point();
				point.setX(swapBytes(datainputstream.readDouble()));
				point.setY(swapBytes(datainputstream.readDouble()));
				shapeobject.addPoint(point);
			}

			return shapeobject;
		} catch (EOFException eofexception) {
			eofReached = true;
			return null;
		}  catch (IOException ioe) {
			throw new RuntimeException(ioe);
		}
	}

	private int readRecordHeader(DataInputStream datainputstream) throws IOException {
		
		int recordNumber = datainputstream.readInt();
		int contentLength = datainputstream.readInt();
		int shapeType = swapBytes(datainputstream.readInt());
		
		if (logger.isDebugEnabled()) {
			logger.debug("Shape object header, Record Number: {}, Content Length: {}, Type: {}", 
					new Object[] {recordNumber, contentLength, SHAPEFILE_TYPES[shapeType]});
		}
		return shapeType;
	}

	private ShapeObject readPolygon(DataInputStream datainputstream) {

		try {

			int shapeType = readRecordHeader(datainputstream);
			
			if (shapeType != SHAPETYPE_POLYGON)
				messages.add("Invalid shape type in shape record: Shape record type "+ shapeType + " in file of type " + getType());
			
			ShapeObject shapeobject = new ShapeObject(SHAPETYPE_POLYGON);
			shapeobject.setBoundingBox(readBoundingBox(datainputstream));
			
			int numParts = swapBytes(datainputstream.readInt());
			int numPoints = swapBytes(datainputstream.readInt());
			
			for (int j1 = 0; j1 < numParts; j1++) {
				int k1 = swapBytes(datainputstream.readInt());
				shapeobject.addPart(k1);
			}

			for (int l1 = 0; l1 < numPoints; l1++) {
				Point point = new Point();
				point.setX(swapBytes(datainputstream.readDouble()));
				point.setY(swapBytes(datainputstream.readDouble()));
				shapeobject.addPoint(point);
			}

			return shapeobject;

		} catch (EOFException eofexception) {
			eofReached = true;
			return null;
		}  catch (IOException ioe) {
			throw new RuntimeException(ioe);
		}
	}
	
	public BoundingBox readBoundingBox(DataInputStream datainputstream) throws IOException {
		
		double xMin = swapBytes(datainputstream.readDouble());
		double yMin = swapBytes(datainputstream.readDouble());
		double xMax = swapBytes(datainputstream.readDouble());
		double yMax = swapBytes(datainputstream.readDouble());
		
		if (logger.isDebugEnabled()) {
			logger.debug("Bounding Box, ({}, {}), ({}, {})", new Object[] { xMin, yMin, xMax, yMax });
		}
		
		return new BoundingBox(xMin, yMin, xMax, yMax);
		
	}

	public int getType() {
		return type;
	}

	public BoundingBox getBoundingBox() {
		return boundingBox;
	}

	protected static short swapBytes(short word0) {
		int i = (word0 & 0xff) << 8;
		int j = (word0 & 0xff00) >>> 8;
		return (short) (i | j);
	}

	protected static int swapBytes(int i) {
		int j = (i & 0xff) << 24;
		int k = (i & 0xff00) << 8;
		int l = (i & 0xff0000) >>> 8;
		int i1 = (i & 0xff000000) >>> 24;
		return j | k | l | i1;
	}

	protected static double swapBytes(double d) {
		long l = Double.doubleToRawLongBits(d);
		long l2 = (l & 255L) << 56;
		long l3 = (l & 65280L) << 40;
		long l4 = (l & 0xff0000L) << 24;
		long l5 = (l & 0xff000000L) << 8;
		long l6 = (l & 0xff00000000L) >>> 8;
		long l7 = (l & 0xff0000000000L) >>> 24;
		long l8 = (l & 0xff000000000000L) >>> 40;
		long l9 = (l & 0xff00000000000000L) >>> 56;
		long l1 = l2 | l3 | l4 | l5 | l6 | l7 | l8 | l9;
		return Double.longBitsToDouble(l1);
	}

	protected static double swapBytes(long l) {
		long l1 = l;
		long l3 = (l1 & 255L) << 56;
		long l4 = (l1 & 65280L) << 40;
		long l5 = (l1 & 0xff0000L) << 24;
		long l6 = (l1 & 0xff000000L) << 8;
		long l7 = (l1 & 0xff00000000L) >>> 8;
		long l8 = (l1 & 0xff0000000000L) >>> 24;
		long l9 = (l1 & 0xff000000000000L) >>> 40;
		long l10 = (l1 & 0xff00000000000000L) >>> 56;
		long l2 = l3 | l4 | l5 | l6 | l7 | l8 | l9 | l10;
		return Double.longBitsToDouble(l2);
	}

	protected static long swapBytesl(long l) {
		long l1 = l;
		long l3 = (l1 & 255L) << 56;
		long l4 = (l1 & 65280L) << 40;
		long l5 = (l1 & 0xff0000L) << 24;
		long l6 = (l1 & 0xff000000L) << 8;
		long l7 = (l1 & 0xff00000000L) >>> 8;
		long l8 = (l1 & 0xff0000000000L) >>> 24;
		long l9 = (l1 & 0xff000000000000L) >>> 40;
		long l10 = (l1 & 0xff00000000000000L) >>> 56;
		long l2 = l3 | l4 | l5 | l6 | l7 | l8 | l9 | l10;
		return l2;
	}

	protected static long swapBytesl(double d) {
		long l = Double.doubleToRawLongBits(d);
		long l2 = (l & 255L) << 56;
		long l3 = (l & 65280L) << 40;
		long l4 = (l & 0xff0000L) << 24;
		long l5 = (l & 0xff000000L) << 8;
		long l6 = (l & 0xff00000000L) >>> 8;
		long l7 = (l & 0xff0000000000L) >>> 24;
		long l8 = (l & 0xff000000000000L) >>> 40;
		long l9 = (l & 0xff00000000000000L) >>> 56;
		long l1 = l2 | l3 | l4 | l5 | l6 | l7 | l8 | l9;
		return l1;
	}

	public ArrayList<String> getWarningMessages() {
		return messages;
	}

}