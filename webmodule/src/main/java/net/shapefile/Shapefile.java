package net.shapefile;


public class Shapefile {

//	public static final int SHAPETYPE_POINT = 1;
//	public static final int SHAPETYPE_POLYLINE = 3;
//	public static final int SHAPETYPE_POLYGON = 5;
//	public static final int SHAPETYPE_MULTIPOINT = 8;
//
//	public static final int FIELDTYPE_CHARACTER = 101;
//	public static final int FIELDTYPE_NUMBER = 102;
//	public static final int FIELDTYPE_FLOAT = 103;
//	public static final int FIELDTYPE_DATE = 104;
//	public static final int FIELDTYPE_LOGICAL = 105;
//
//	protected TableDescription tableDescription = new TableDescription();
//	protected BoundingBox boundingBox = new BoundingBox();
//	private int type;
//	private ArrayList<ShapeObject> shapeObjects = new ArrayList<ShapeObject>();
//	private ArrayList<String> messages = new ArrayList<String>();
//
//	public Shapefile(int type) {
//		this.type = type;
//	}
//
//	public Shapefile(String s) throws IOException {
//		FileInputStream shpInputStream = null;
//		FileInputStream shxInputStream = null;
//		FileInputStream bdfInputStream = null;
//		shpInputStream = new FileInputStream(s + ".shp");
//		try {
//			shxInputStream = new FileInputStream(s + ".shx");
//		} catch (FileNotFoundException filenotfoundexception) {
//			shxInputStream = null;
//		}
//		try {
//			bdfInputStream = new FileInputStream(s + ".dbf");
//		} catch (FileNotFoundException filenotfoundexception1) {
//			bdfInputStream = null;
//		}
//		a(shpInputStream, shxInputStream, bdfInputStream);
//	}
//
//	public Shapefile(InputStream shpInputStream, InputStream shxInputStream, InputStream bdfInputStream) throws IOException {
//		a(shpInputStream, shxInputStream, bdfInputStream);
//	}
//
//	@SuppressWarnings("unused")
//	private void a(InputStream shpInputStream, InputStream shxInputStream, InputStream bdfInputStream) throws IOException {
//		
//		DataInputStream datainputstream = null;
//		DataInputStream datainputstream1 = null;
//		DataInputStream datainputstream2 = null;
//		datainputstream = new DataInputStream(shpInputStream);
//		datainputstream1 = new DataInputStream(shxInputStream);
//		boolean existesDbfFile = true;
//		boolean flag1 = true;
//
//		if (bdfInputStream == null) {
//			existesDbfFile = false;
//			messages.add("Unable to access the records stream for reading. The shapefile will lack record attributes.");
//		} else {
//			datainputstream2 = new DataInputStream(bdfInputStream);
//		}
//		if (shxInputStream == null) {
//			boolean flag2 = false;
//			messages.add(new String(
//					"Unable to access the index stream for reading. This will not affect the accuracy of the shapefile."));
//		} else {
//			datainputstream1 = new DataInputStream(shxInputStream);
//		}
//		int i = datainputstream.readInt();
//		if (i != 9994)
//			throw new InvalidFileException(i + " is not a valid file code.");
//		
//		datainputstream.skipBytes(20);
//		datainputstream.skipBytes(4);
//		
//		int j = swapBytes(datainputstream.readInt());
//		if (j != 1000)
//			throw new InvalidFileException(j + " is not a valid file version.");
//		
//		type = swapBytes(datainputstream.readInt());
//		boundingBox.setXMin(swapBytes(datainputstream.readDouble()));
//		boundingBox.setYMin(swapBytes(datainputstream.readDouble()));
//		boundingBox.setXMax(swapBytes(datainputstream.readDouble()));
//		boundingBox.setYMax(swapBytes(datainputstream.readDouble()));
//		datainputstream.skip(32L);
//		long ppp = 0;
//		ShapeObject shapeobject = null;
//		do
//			switch (type) {
//			case 1: // '\001'
//				shapeobject = _mthdo(datainputstream);
//				if (shapeobject != null)
//					shapeObjects.add(shapeobject);
//				break;
//
//			case 8: // '\b'
//				shapeobject = _mthif(datainputstream);
//				if (shapeobject != null)
//					shapeObjects.add(shapeobject);
//				break;
//
//			case 3: // '\003'
//				shapeobject = a(datainputstream);
//				if (shapeobject != null) {
//					shapeObjects.add(shapeobject);
//				}
//				break;
//
//			case 5: // '\005'
//				shapeobject = _mthfor(datainputstream);
//				if (shapeobject != null)
//					shapeObjects.add(shapeobject);
//				break;
//			}
//		while (shapeobject != null);
//		
//		System.out.println("done");
//		
//		if (existesDbfFile) {
//			ArrayList<Record> records = new ArrayList<Record>();
//			
//			a(datainputstream2, getShapeObjectCount(), tableDescription, records);
//			
//			for (int k = 0; k < getShapeObjectCount(); k++) {
//				ShapeObject shapeobject1 = (ShapeObject) shapeObjects.get(k);
//				shapeobject1.setRecord((Record) records.get(k));
//			}
//
//		}
//		
//		
////		if (shapeobject != null) {
////		System.out.println("Shape numero: ===================================================================================");
////		ShapeObject obj = shapeobject;
////		System.out.println("Shape object: type " + obj.getType());
////		System.out.println("\tBoundingBox");
////		if(obj.getType() != Shapefile.SHAPETYPE_POINT)
////		{
////			BoundingBox box = obj.getBoundingBox();
////			System.out.println("\t\tXmin: \t" + box.getXMin());
////			System.out.println("\t\tXmax: \t" + box.getXMax());
////			System.out.println("\t\tYmin: \t" + box.getYMin());
////			System.out.println("\t\tYmax: \t" + box.getYMax());
////			
////		}
////		Iterator<Point> itrPoints = obj.getPoints().iterator();
////		while(itrPoints.hasNext())
////		{
////			Point pt = (Point)itrPoints.next();
////			System.out.println(i + ":\t\tx: " + pt.getX() + " y: " + pt.getY());	
////		}
////		
////		System.out.println("\trecords: ");
////		Record rec = obj.getRecord();
////		Iterator<RecordField> itrFields = rec.getFields().iterator();
////		while(itrFields.hasNext())
////		{
////			RecordField rf = itrFields.next();
////			System.out.println("\t\tname: " + rf.getName() + "\tvalue: " + rf.getValue());
////		}
////		}
//		
//	}
//
//	@SuppressWarnings("unused")
//	private ShapeObject _mthdo(DataInputStream datainputstream)
//			throws IOException {
//
//		try {
//
//			ShapeObject shapeobject;
//			int i = datainputstream.readInt();
//			int j = datainputstream.readInt();
//			int k = swapBytes(datainputstream.readInt());
//			if (k != 1)
//				messages.add(new String(
//						"Invalid shape type in shape record: Shape record type "
//								+ k + " in file of type " + getType()));
//			shapeobject = new ShapeObject(1);
//			Point point = new Point();
//			point.setX(swapBytes(datainputstream.readDouble()));
//			point.setY(swapBytes(datainputstream.readDouble()));
//			shapeobject.addPoint(point);
//			return shapeobject;
//
//		} catch (EOFException eofexception) {
//			return null;
//		}
//
//	}
//
//	@SuppressWarnings("unused")
//	private ShapeObject a(DataInputStream datainputstream) throws IOException {
//
//		try {
//
//			ShapeObject shapeobject;
//			int i = datainputstream.readInt();
//			int j = datainputstream.readInt();
//			int k = swapBytes(datainputstream.readInt());
//			if (k != 3)
//				messages.add(new String(
//						"Invalid shape type in shape record: Shape record type "
//								+ k + " in file of type " + getType()));
//			shapeobject = new ShapeObject(3);
//			BoundingBox boundingbox = new BoundingBox();
//			boundingbox.setXMin(swapBytes(datainputstream.readDouble()));
//			boundingbox.setYMin(swapBytes(datainputstream.readDouble()));
//			boundingbox.setXMax(swapBytes(datainputstream.readDouble()));
//			boundingbox.setYMax(swapBytes(datainputstream.readDouble()));
//			shapeobject.setBoundingBox(boundingbox);
//			int l = swapBytes(datainputstream.readInt());
//			int i1 = swapBytes(datainputstream.readInt());
//			for (int j1 = 0; j1 < l; j1++) {
//				int k1 = swapBytes(datainputstream.readInt());
//				shapeobject.addPart(k1);
//			}
//
//			for (int l1 = 0; l1 < i1; l1++) {
//				Point point = new Point();
//				point.setX(swapBytes(datainputstream.readDouble()));
//				point.setY(swapBytes(datainputstream.readDouble()));
//				shapeobject.addPoint(point);
//			}
//
//	
//			
//			return shapeobject;
//
//		} catch (EOFException eofexception) {
//			return null;
//		}
//
//	}
//
//	@SuppressWarnings("unused")
//	private ShapeObject _mthif(DataInputStream datainputstream)
//			throws IOException {
//
//		try {
//
//			ShapeObject shapeobject;
//			int i = datainputstream.readInt();
//			int j = datainputstream.readInt();
//			shapeobject = new ShapeObject(8);
//			int k = swapBytes(datainputstream.readInt());
//			if (k != 8)
//				messages.add(new String(
//						"Invalid shape type in shape record: Shape record type "
//								+ k + " in file of type " + getType()));
//			BoundingBox boundingbox = new BoundingBox();
//			boundingbox.setXMin(swapBytes(datainputstream.readDouble()));
//			boundingbox.setYMin(swapBytes(datainputstream.readDouble()));
//			boundingbox.setXMax(swapBytes(datainputstream.readDouble()));
//			boundingbox.setYMax(swapBytes(datainputstream.readDouble()));
//			shapeobject.setBoundingBox(boundingbox);
//			int l = swapBytes(datainputstream.readInt());
//			for (int i1 = 0; i1 < l; i1++) {
//				Point point = new Point();
//				point.setX(swapBytes(datainputstream.readDouble()));
//				point.setY(swapBytes(datainputstream.readDouble()));
//				shapeobject.addPoint(point);
//			}
//
//			return shapeobject;
//		} catch (EOFException eofexception) {
//			return null;
//		}
//	}
//
//	@SuppressWarnings("unused")
//	private ShapeObject _mthfor(DataInputStream datainputstream)
//			throws IOException {
//
//		try {
//
//			ShapeObject shapeobject;
//			int i = datainputstream.readInt();
//			int j = datainputstream.readInt();
//			int k = swapBytes(datainputstream.readInt());
//			if (k != 5)
//				messages.add(new String(
//						"Invalid shape type in shape record: Shape record type "
//								+ k + " in file of type " + getType()));
//			shapeobject = new ShapeObject(5);
//			BoundingBox boundingbox = new BoundingBox();
//			boundingbox.setXMin(swapBytes(datainputstream.readDouble()));
//			boundingbox.setYMin(swapBytes(datainputstream.readDouble()));
//			boundingbox.setXMax(swapBytes(datainputstream.readDouble()));
//			boundingbox.setYMax(swapBytes(datainputstream.readDouble()));
//			shapeobject.setBoundingBox(boundingbox);
//			int l = swapBytes(datainputstream.readInt());
//			int i1 = swapBytes(datainputstream.readInt());
//			for (int j1 = 0; j1 < l; j1++) {
//				int k1 = swapBytes(datainputstream.readInt());
//				shapeobject.addPart(k1);
//			}
//
//			for (int l1 = 0; l1 < i1; l1++) {
//				Point point = new Point();
//				point.setX(swapBytes(datainputstream.readDouble()));
//				point.setY(swapBytes(datainputstream.readDouble()));
//				shapeobject.addPoint(point);
//			}
//
//			return shapeobject;
//
//		} catch (EOFException eofexception) {
//			return null;
//		}
//	}
//
//	public int getShapeObjectCount() {
//		return shapeObjects.size();
//	}
//
//	public void write(String fileName) throws IOException {
//		write(
//			new FileOutputStream(fileName + ".shp"), 
//			new FileOutputStream(fileName + ".shx"), 
//			new FileOutputStream(fileName + ".dbf")
//		);
//	}
//
//	public void write(OutputStream outputstream, OutputStream outputstream1, OutputStream outputstream2) throws IOException {
//		boolean flag = true;
//		boolean flag1 = true;
//		messages = new ArrayList<String>();
//		DataOutputStream dataoutputstream = new DataOutputStream(outputstream);
//		DataOutputStream dataoutputstream1 = null;
//		DataOutputStream dataoutputstream2 = null;
//		if (outputstream1 == null) {
//			flag = false;
//			messages.add("Unable to open index stream for writing. The shapefile will lack an .shx file.");
//		} else {
//			dataoutputstream1 = new DataOutputStream(outputstream1);
//		}
//		if (outputstream2 == null) {
//			flag1 = false;
//			messages.add("Unable to open records stream for writing. The shapefile will lack a .dbf file.");
//		} else {
//			dataoutputstream2 = new DataOutputStream(outputstream2);
//		}
//		if (boundingBox.getXMin() == 0.0D && boundingBox.getXMax() == 0.0D
//				&& boundingBox.getYMin() == 0.0D
//				&& boundingBox.getYMax() == 0.0D)
//			computeExtents();
//		dataoutputstream.writeInt(9994);
//		if (flag)
//			dataoutputstream1.writeInt(9994);
//		for (int i = 0; i < 5; i++) {
//			dataoutputstream.writeInt(0);
//			if (flag)
//				dataoutputstream1.writeInt(0);
//		}
//
//		int j = 0;
//		switch (type) {
//		case 2: // '\002'
//		case 4: // '\004'
//		case 6: // '\006'
//		case 7: // '\007'
//		default:
//			break;
//
//		case 1: // '\001'
//			j = (getShapeObjectCount() * 28) / 2 + 50;
//			break;
//
//		case 3: // '\003'
//		case 5: // '\005'
//			for (int k = 0; k < getShapeObjectCount(); k++) {
//				ShapeObject shapeobject = getShapeObject(k);
//				j += (52 + shapeobject.getPointCount() * 16 + shapeobject
//						.getPartCount() * 4) / 2;
//			}
//
//			j += 50;
//			break;
//
//		case 8: // '\b'
//			for (int l = 0; l < getShapeObjectCount(); l++) {
//				ShapeObject shapeobject1 = getShapeObject(l);
//				j += (48 + shapeobject1.getPointCount() * 16) / 2;
//			}
//
//			j += 50;
//			break;
//		}
//		dataoutputstream.writeInt(j);
//		j = (getShapeObjectCount() * 8) / 2 + 50;
//		if (flag)
//			dataoutputstream1.writeInt(j);
//		dataoutputstream.writeInt(swapBytes(1000));
//		if (flag)
//			dataoutputstream1.writeInt(swapBytes(1000));
//		dataoutputstream.writeInt(swapBytes(type));
//		if (flag)
//			dataoutputstream1.writeInt(swapBytes(type));
//		dataoutputstream.writeLong(swapBytesl(boundingBox.getXMin()));
//		dataoutputstream.writeLong(swapBytesl(boundingBox.getYMin()));
//		dataoutputstream.writeLong(swapBytesl(boundingBox.getXMax()));
//		dataoutputstream.writeLong(swapBytesl(boundingBox.getYMax()));
//		dataoutputstream.writeLong(swapBytesl(0L));
//		dataoutputstream.writeLong(swapBytesl(0L));
//		dataoutputstream.writeLong(swapBytesl(0L));
//		dataoutputstream.writeLong(swapBytesl(0L));
//		if (flag) {
//			dataoutputstream1.writeLong(swapBytesl(boundingBox.getXMin()));
//			dataoutputstream1.writeLong(swapBytesl(boundingBox.getYMin()));
//			dataoutputstream1.writeLong(swapBytesl(boundingBox.getXMax()));
//			dataoutputstream1.writeLong(swapBytesl(boundingBox.getYMax()));
//			dataoutputstream1.writeLong(swapBytesl(0L));
//			dataoutputstream1.writeLong(swapBytesl(0L));
//			dataoutputstream1.writeLong(swapBytesl(0L));
//			dataoutputstream1.writeLong(swapBytesl(0L));
//		}
//		int i1 = 50;
//		int j1 = 1;
//		for (int k1 = 0; k1 < getShapeObjectCount(); k1++) {
//			switch (type) {
//			case 1: // '\001'
//				i1 = a(dataoutputstream, dataoutputstream1, i1,
//						getShapeObject(k1), j1, flag);
//				break;
//
//			case 3: // '\003'
//				i1 = _mthif(dataoutputstream, dataoutputstream1, i1,
//						getShapeObject(k1), j1, flag);
//				break;
//
//			case 5: // '\005'
//				i1 = _mthdo(dataoutputstream, dataoutputstream1, i1,
//						getShapeObject(k1), j1, flag);
//				break;
//
//			case 8: // '\b'
//				i1 = _mthfor(dataoutputstream, dataoutputstream1, i1,
//						getShapeObject(k1), j1, flag);
//				break;
//			}
//			j1++;
//		}
//
//		ArrayList<Record> arraylist = new ArrayList<Record>();
//		for (int l1 = 0; l1 < getShapeObjectCount(); l1++) {
//			ShapeObject shapeobject2 = getShapeObject(l1);
//			Record record = shapeobject2.getRecord();
//			arraylist.add(record);
//		}
//
//		if (flag1)
//			a(dataoutputstream2, getShapeObjectCount(), tableDescription,
//					arraylist);
//		dataoutputstream.close();
//		if (flag)
//			dataoutputstream1.close();
//		if (flag1)
//			dataoutputstream2.close();
//	}
//
//	private int a(DataOutputStream dataoutputstream,
//			DataOutputStream dataoutputstream1, int i, ShapeObject shapeobject,
//			int j, boolean flag) throws IOException {
//		dataoutputstream.writeInt(j);
//		if (flag)
//			dataoutputstream1.writeInt(i);
//		i += 14;
//		dataoutputstream.writeInt(10);
//		if (flag)
//			dataoutputstream1.writeInt(10);
//		dataoutputstream.writeInt(swapBytes(1));
//		Point point = shapeobject.getPoint(0);
//		dataoutputstream.writeLong(swapBytesl(point.getX()));
//		dataoutputstream.writeLong(swapBytesl(point.getY()));
//		return i;
//	}
//
//	private int _mthfor(DataOutputStream dataoutputstream,
//			DataOutputStream dataoutputstream1, int i, ShapeObject shapeobject,
//			int j, boolean flag) throws IOException {
//		if (shapeobject.getBoundingBox().getXMin() == 0.0D
//				&& shapeobject.getBoundingBox().getXMax() == 0.0D
//				&& shapeobject.getBoundingBox().getYMin() == 0.0D
//				&& shapeobject.getBoundingBox().getYMax() == 0.0D)
//			shapeobject.computeExtents();
//		dataoutputstream.writeInt(j);
//		if (flag)
//			dataoutputstream1.writeInt(i);
//		i += (48 + shapeobject.getPointCount() * 16) / 2;
//		dataoutputstream.writeInt((40 + shapeobject.getPointCount() * 16) / 2);
//		if (flag)
//			dataoutputstream1
//					.writeInt((40 + shapeobject.getPointCount() * 16) / 2);
//		dataoutputstream.writeInt(swapBytes(8));
//		dataoutputstream.writeLong(swapBytesl(shapeobject.getBoundingBox()
//				.getXMin()));
//		dataoutputstream.writeLong(swapBytesl(shapeobject.getBoundingBox()
//				.getYMin()));
//		dataoutputstream.writeLong(swapBytesl(shapeobject.getBoundingBox()
//				.getXMax()));
//		dataoutputstream.writeLong(swapBytesl(shapeobject.getBoundingBox()
//				.getYMax()));
//		dataoutputstream.writeInt(swapBytes(shapeobject.getPointCount()));
//		for (int k = 0; k < shapeobject.getPointCount(); k++) {
//			Point point = shapeobject.getPoint(k);
//			dataoutputstream.writeLong(swapBytesl(point.getX()));
//			dataoutputstream.writeLong(swapBytesl(point.getY()));
//		}
//
//		return i;
//	}
//
//	private int _mthif(DataOutputStream dataoutputstream,
//			DataOutputStream dataoutputstream1, int i, ShapeObject shapeobject,
//			int j, boolean flag) throws IOException {
//		if (shapeobject.getBoundingBox().getXMin() == 0.0D
//				&& shapeobject.getBoundingBox().getXMax() == 0.0D
//				&& shapeobject.getBoundingBox().getYMin() == 0.0D
//				&& shapeobject.getBoundingBox().getYMax() == 0.0D)
//			shapeobject.computeExtents();
//		dataoutputstream.writeInt(j);
//		if (flag)
//			dataoutputstream1.writeInt(i);
//		i += (52 + shapeobject.getPartCount() * 4 + shapeobject.getPointCount() * 16) / 2;
//		dataoutputstream
//				.writeInt((44 + shapeobject.getPartCount() * 4 + shapeobject
//						.getPointCount() * 16) / 2);
//		if (flag)
//			dataoutputstream1
//					.writeInt((44 + shapeobject.getPartCount() * 4 + shapeobject
//							.getPointCount() * 16) / 2);
//		dataoutputstream.writeInt(swapBytes(3));
//		dataoutputstream.writeLong(swapBytesl(shapeobject.getBoundingBox()
//				.getXMin()));
//		dataoutputstream.writeLong(swapBytesl(shapeobject.getBoundingBox()
//				.getYMin()));
//		dataoutputstream.writeLong(swapBytesl(shapeobject.getBoundingBox()
//				.getXMax()));
//		dataoutputstream.writeLong(swapBytesl(shapeobject.getBoundingBox()
//				.getYMax()));
//		dataoutputstream.writeInt(swapBytes(shapeobject.getPartCount()));
//		dataoutputstream.writeInt(swapBytes(shapeobject.getPointCount()));
//		dataoutputstream.writeInt(0);
//		for (int k = 1; k < shapeobject.getPartCount(); k++) {
//			int l = shapeobject.getPart(k);
//			dataoutputstream.writeInt(swapBytes(l));
//		}
//
//		for (int i1 = 0; i1 < shapeobject.getPointCount(); i1++) {
//			Point point = shapeobject.getPoint(i1);
//			dataoutputstream.writeLong(swapBytesl(point.getX()));
//			dataoutputstream.writeLong(swapBytesl(point.getY()));
//		}
//
//		return i;
//	}
//
//	private int _mthdo(DataOutputStream dataoutputstream,
//			DataOutputStream dataoutputstream1, int i, ShapeObject shapeobject,
//			int j, boolean flag) throws IOException {
//		if (shapeobject.getBoundingBox().getXMin() == 0.0D
//				&& shapeobject.getBoundingBox().getXMax() == 0.0D
//				&& shapeobject.getBoundingBox().getYMin() == 0.0D
//				&& shapeobject.getBoundingBox().getYMax() == 0.0D)
//			shapeobject.computeExtents();
//		dataoutputstream.writeInt(j);
//		if (flag)
//			dataoutputstream1.writeInt(i);
//		i += (52 + shapeobject.getPartCount() * 4 + shapeobject.getPointCount() * 16) / 2;
//		dataoutputstream
//				.writeInt((44 + shapeobject.getPartCount() * 4 + shapeobject
//						.getPointCount() * 16) / 2);
//		if (flag)
//			dataoutputstream1
//					.writeInt((44 + shapeobject.getPartCount() * 4 + shapeobject
//							.getPointCount() * 16) / 2);
//		dataoutputstream.writeInt(swapBytes(5));
//		dataoutputstream.writeLong(swapBytesl(shapeobject.getBoundingBox()
//				.getXMin()));
//		dataoutputstream.writeLong(swapBytesl(shapeobject.getBoundingBox()
//				.getYMin()));
//		dataoutputstream.writeLong(swapBytesl(shapeobject.getBoundingBox()
//				.getXMax()));
//		dataoutputstream.writeLong(swapBytesl(shapeobject.getBoundingBox()
//				.getYMax()));
//		dataoutputstream.writeInt(swapBytes(shapeobject.getPartCount()));
//		dataoutputstream.writeInt(swapBytes(shapeobject.getPointCount()));
//		dataoutputstream.writeInt(0);
//		for (int k = 1; k < shapeobject.getPartCount(); k++) {
//			int l = shapeobject.getPart(k);
//			dataoutputstream.writeInt(swapBytes(l));
//		}
//
//		for (int i1 = 0; i1 < shapeobject.getPointCount(); i1++) {
//			Point point = shapeobject.getPoint(i1);
//			dataoutputstream.writeLong(swapBytesl(point.getX()));
//			dataoutputstream.writeLong(swapBytesl(point.getY()));
//		}
//
//		return i;
//	}
//
//	public int getType() {
//		return type;
//	}
//
//	public void setType(int i) {
//		type = i;
//	}
//
//	public void addShapeObject(ShapeObject shapeobject) {
//
//		if (shapeobject.getType() != type && shapeobject.getType() != 0)
//			throw new InvalidShapeTypeException("Tried to add ShapeObject of type " + shapeobject.getType()+ " to Shapefile of type " + type);
//		
//		for (Iterator<RecordField> iterator = shapeobject.getRecord()
//				.getFields().iterator(); iterator.hasNext();) {
//			RecordField recordfield = (RecordField) iterator.next();
//			if (!tableDescription.contains(recordfield.getName()))
//				throw new InvalidFieldNameException(recordfield.getName()
//						+ " does not exist in the Shapefile's TableDescription");
//		}
//
//		shapeObjects.add(shapeobject);
//
//	}
//
//	public void removeShapeObject(int i) {
//		shapeObjects.remove(i);
//	}
//
//	public ShapeObject getShapeObject(int i) {
//		return (ShapeObject) shapeObjects.get(i);
//	}
//
//	public ArrayList<ShapeObject> getShapeObjects() {
//		return shapeObjects;
//	}
//
//	public void setShapeObjects(Collection<ShapeObject> collection) {
//
//		shapeObjects = new ArrayList<ShapeObject>();
//		ShapeObject shapeobject;
//		for (Iterator<ShapeObject> iterator = collection.iterator(); iterator
//				.hasNext(); addShapeObject(shapeobject))
//			shapeobject = (ShapeObject) iterator.next();
//
//	}
//
//	public BoundingBox getBoundingBox() {
//		return boundingBox;
//	}
//
//	public void setBoundingBox(BoundingBox boundingbox) {
//		boundingBox = boundingbox;
//	}
//
//	private void a(InputStream inputstream, int i,
//			TableDescription tabledescription, ArrayList<Record> arraylist)
//			throws IOException {
//		DataInputStream datainputstream = new DataInputStream(inputstream);
//		datainputstream.skip(4L);
//		int j = swapBytes(datainputstream.readInt());
//		if (j != i)
//			throw new RuntimeException(
//					"Inconsistant records file: the number of records does not match the number of shape objects");
//		datainputstream.skip(2L);
//		datainputstream.skip(2L);
//		datainputstream.skip(3L);
//		datainputstream.skip(13L);
//		datainputstream.skip(4L);
//		byte byte0 = datainputstream.readByte();
//
//		do {
//			byte abyte0[] = new byte[10];
//			TableDescriptor tabledescriptor = new TableDescriptor();
//			abyte0[0] = byte0;
//			for (int l = 1; l < 10; l++)
//				abyte0[l] = datainputstream.readByte();
//
//			datainputstream.skip(1L);
//			tabledescriptor.setName((new String(abyte0)).trim());
//			char c = (char) datainputstream.readByte();
//			switch (c) {
//			case 67: // 'C'
//				tabledescriptor.setType(101);
//				break;
//
//			case 78: // 'N'
//				tabledescriptor.setType(102);
//				break;
//
//			case 70: // 'F'
//				tabledescriptor.setType(103);
//				break;
//
//			case 68: // 'D'
//				tabledescriptor.setType(104);
//				break;
//
//			case 76: // 'L'
//				tabledescriptor.setType(105);
//				break;
//			}
//			datainputstream.skip(4L);
//			tabledescriptor.setWidth(datainputstream.readByte());
//			tabledescriptor.setPrecision(datainputstream.readByte());
//			datainputstream.skip(2L);
//			datainputstream.skip(1L);
//			datainputstream.skip(2L);
//			datainputstream.skip(1L);
//			datainputstream.skip(8L);
//			byte0 = datainputstream.readByte();
//			tabledescription.addTableDescriptor(tabledescriptor);
//		} while (byte0 != 13);
//		for (int k = 0; k < j; k++) {
//			Record record = new Record();
//			datainputstream.skip(1L);
//			for (int i1 = 0; i1 < tabledescription.getFieldCount(); i1++) {
//				TableDescriptor tabledescriptor1 = tabledescription
//						.getTableDescriptor(i1);
//				
////				int width = tabledescriptor1.getWidth() >= 0 ? tabledescriptor1.getWidth() : 0;
//				byte abyte1[] = new byte[tabledescriptor1.getWidth()];
//				for (int j1 = 0; j1 < tabledescriptor1.getWidth(); j1++)
//					abyte1[j1] = datainputstream.readByte();
//
//				record.addField(new RecordField(tabledescriptor1.getName(),
//						(new String(abyte1)).trim()));
//				
//				
//			}
//
//			arraylist.add(record);
//			System.out.println("Leyendo registros: "+k+" de "+j);
//		}
//
//	}
//
//	private void a(OutputStream outputstream, int i,
//			TableDescription tabledescription, ArrayList<Record> arraylist)
//			throws IOException {
//		DataOutputStream dataoutputstream = new DataOutputStream(outputstream);
//		if (tabledescription.getFieldCount() == 0) {
//			tabledescription
//					.addTableDescriptor(new TableDescriptor(" ", 101, 1));
//			// arraylist = new ArrayList();
//			for (int j = 0; j < i; j++) {
//				RecordField recordfield = new RecordField(" ", " ");
//				Record record = new Record();
//				record.addField(recordfield);
//				arraylist.add(record);
//			}
//
//		} else {
//			Iterator<Record> iterator2 = arraylist.iterator();
//			boolean flag = false;
//			while (iterator2.hasNext()) {
//				Record record1 = (Record) iterator2.next();
//				for (Iterator<TableDescriptor> iterator1 = tabledescription
//						.getTableDescriptors().iterator(); iterator1.hasNext();) {
//					TableDescriptor tabledescriptor = (TableDescriptor) iterator1
//							.next();
//					for (Iterator<RecordField> iterator = record1.getFields()
//							.iterator(); iterator.hasNext();) {
//						RecordField recordfield1 = (RecordField) iterator
//								.next();
//						if (recordfield1.getName().equals(
//								tabledescriptor.getName()))
//							flag = true;
//					}
//
//					if (!flag)
//						record1.addField(new RecordField(tabledescriptor
//								.getName(), " "));
//					flag = false;
//				}
//
//			}
//		}
//		dataoutputstream.writeByte(3);
//		Calendar calendar = Calendar.getInstance();
//		int k = calendar.get(1);
//		k -= 2000;
//		dataoutputstream.writeByte((byte) k);
//		dataoutputstream.writeByte((byte) (calendar.get(2) + 1));
//		dataoutputstream.writeByte((byte) calendar.get(5));
//		dataoutputstream.writeInt(swapBytes(arraylist.size()));
//		short word0 = (short) (tabledescription.getFieldCount() * 32 + 32 + 1);
//		dataoutputstream.writeShort(swapBytes(word0));
//		short word1 = 0;
//		for (int l = 0; l < tabledescription.getFieldCount(); l++) {
//			TableDescriptor tabledescriptor1 = tabledescription
//					.getTableDescriptor(l);
//			if (tabledescriptor1.getType() == 105)
//				tabledescriptor1.setWidth(1);
//			if (tabledescriptor1.getWidth() == 0) {
//				int j1 = 0;
//				for (Iterator<Record> iterator3 = arraylist.iterator(); iterator3
//						.hasNext();) {
//					Record record2 = iterator3.next();
//					RecordField recordfield2 = record2
//							.getField(tabledescriptor1.getName());
//					if (j1 < recordfield2.getValue().length())
//						j1 = recordfield2.getValue().length();
//				}
//
//				tabledescriptor1.setWidth(j1);
//			}
//			word1 += tabledescriptor1.getWidth();
//		}
//
//		word1++;
//		dataoutputstream.writeShort(swapBytes(word1));
//		for (int i1 = 0; i1 < 3; i1++)
//			dataoutputstream.writeByte(0);
//
//		for (int k1 = 0; k1 < 13; k1++)
//			dataoutputstream.writeByte(0);
//
//		for (int l1 = 0; l1 < 4; l1++)
//			dataoutputstream.writeByte(0);
//
//		for (int i2 = 0; i2 < tabledescription.getFieldCount(); i2++) {
//			TableDescriptor tabledescriptor2 = tabledescription
//					.getTableDescriptor(i2);
//			String s = tabledescriptor2.getName();
//			byte abyte0[] = s.getBytes();
//			for (int l2 = 0; l2 < 10; l2++)
//				if (abyte0.length > l2)
//					dataoutputstream.writeByte(abyte0[l2]);
//				else
//					dataoutputstream.writeByte(0);
//
//			dataoutputstream.writeByte(0);
//			switch (tabledescriptor2.getType()) {
//			case 101: // 'e'
//				dataoutputstream.writeByte(67);
//				break;
//
//			case 104: // 'h'
//				dataoutputstream.writeByte(68);
//				break;
//
//			case 102: // 'f'
//				dataoutputstream.writeByte(78);
//				break;
//
//			case 103: // 'g'
//				dataoutputstream.writeByte(70);
//				break;
//
//			case 105: // 'i'
//				dataoutputstream.writeByte(76);
//				break;
//			}
//			for (int i3 = 0; i3 < 4; i3++)
//				dataoutputstream.writeByte(0);
//
//			dataoutputstream.writeByte(tabledescriptor2.getWidth());
//			dataoutputstream.writeByte(tabledescriptor2.getPrecision());
//			dataoutputstream.writeByte(0);
//			dataoutputstream.writeByte(0);
//			dataoutputstream.writeByte(0);
//			dataoutputstream.writeByte(0);
//			dataoutputstream.writeByte(0);
//			dataoutputstream.writeByte(0);
//			for (int j3 = 0; j3 < 8; j3++)
//				dataoutputstream.writeByte(0);
//
//		}
//
//		dataoutputstream.writeByte(13);
//		for (int j2 = 0; j2 < arraylist.size(); j2++) {
//			dataoutputstream.writeByte(32);
//			Record record3 = (Record) arraylist.get(j2);
//			for (int k2 = 0; k2 < tabledescription.getFieldCount(); k2++) {
//				TableDescriptor tabledescriptor3 = tabledescription
//						.getTableDescriptor(k2);
//				RecordField recordfield3 = record3.getField(tabledescriptor3
//						.getName());
//				String s1 = recordfield3.getValue();
//				if (tabledescriptor3.getType() == 103) {
//					DecimalFormat decimalformat = new DecimalFormat("0.###E000");
//					String s2 = decimalformat.format(Double.parseDouble(s1));
//					StringBuffer stringbuffer = new StringBuffer(s2);
//					int k4 = s2.indexOf("-");
//					if (k4 == -1)
//						stringbuffer.insert(s2.indexOf("E") + 1, '+');
//					s1 = stringbuffer.toString();
//				}
//				byte abyte1[] = null;
//				// Object obj = null;
//				if (tabledescriptor3.getType() == 102
//						|| tabledescriptor3.getType() == 103
//						|| tabledescriptor3.getType() == 104) {
//					abyte1 = new byte[tabledescriptor3.getWidth()];
//					byte abyte2[] = s1.getBytes();
//					for (int k3 = 0; k3 < abyte1.length; k3++)
//						abyte1[k3] = 32;
//
//					int l4 = abyte1.length - 1;
//					for (int j5 = abyte2.length - 1; l4 > -1 && j5 > -1; j5--) {
//						abyte1[l4] = abyte2[j5];
//						l4--;
//					}
//
//				} else if (tabledescriptor3.getType() == 101) {
//					abyte1 = new byte[tabledescriptor3.getWidth()];
//					byte abyte3[] = s1.getBytes();
//					for (int l3 = 0; l3 < abyte1.length; l3++)
//						abyte1[l3] = 32;
//
//					int i5 = 0;
//					for (int k5 = 0; i5 < abyte1.length && k5 < abyte3.length; k5++) {
//						abyte1[i5] = abyte3[k5];
//						i5++;
//					}
//
//				} else if (tabledescriptor3.getType() == 105) {
//					abyte1 = new byte[1];
//					if (s1.equals("T") || s1.equals("t"))
//						abyte1[0] = 84;
//					else if (s1.equals("F") || s1.equals("f"))
//						abyte1[0] = 70;
//					else
//						abyte1[0] = 63;
//				} else {
//					abyte1 = new byte[tabledescriptor3.getWidth()];
//					for (int i4 = 0; i4 < abyte1.length; i4++)
//						abyte1[i4] = 32;
//
//				}
//				for (int j4 = 0; j4 < tabledescriptor3.getWidth(); j4++)
//					dataoutputstream.writeByte(abyte1[j4]);
//
//			}
//
//		}
//
//		dataoutputstream.writeByte(26);
//	}
//
//	public void setTableDescription(TableDescription tabledescription) {
//		tableDescription = tabledescription;
//	}
//
//	public void computeExtents() {
//		switch (type) {
//		case 2: // '\002'
//		case 4: // '\004'
//		case 6: // '\006'
//		case 7: // '\007'
//		default:
//			break;
//
//		case 1: // '\001'
//			if (getShapeObjectCount() > 0) {
//				ShapeObject shapeobject = (ShapeObject) shapeObjects.get(0);
//				if (shapeobject.getPointCount() > 0) {
//					Point point = shapeobject.getPoint(0);
//					boundingBox.setXMin(point.getX());
//					boundingBox.setXMax(point.getX());
//					boundingBox.setYMin(point.getY());
//					boundingBox.setYMax(point.getY());
//				}
//			}
//			for (int i = 0; i < getShapeObjectCount(); i++) {
//				ShapeObject shapeobject1 = (ShapeObject) shapeObjects.get(i);
//				if (shapeobject1.getPointCount() > 0) {
//					Point point1 = shapeobject1.getPoint(0);
//					if (boundingBox.getXMin() > point1.getX())
//						boundingBox.setXMin(point1.getX());
//					if (boundingBox.getXMax() < point1.getX())
//						boundingBox.setXMax(point1.getX());
//					if (boundingBox.getYMin() > point1.getY())
//						boundingBox.setYMin(point1.getY());
//					if (boundingBox.getYMax() < point1.getY())
//						boundingBox.setYMax(point1.getY());
//				}
//			}
//
//			break;
//
//		case 3: // '\003'
//		case 5: // '\005'
//		case 8: // '\b'
//			if (getShapeObjectCount() > 0) {
//				ShapeObject shapeobject2 = (ShapeObject) shapeObjects.get(0);
//				if (shapeobject2.getPointCount() > 0) {
//					Point point2 = shapeobject2.getPoint(0);
//					boundingBox.setXMin(point2.getX());
//					boundingBox.setXMax(point2.getX());
//					boundingBox.setYMin(point2.getY());
//					boundingBox.setYMax(point2.getY());
//				}
//			}
//			for (int j = 0; j < getShapeObjectCount(); j++) {
//				ShapeObject shapeobject3 = (ShapeObject) shapeObjects.get(j);
//				for (int k = 0; k < shapeobject3.getPointCount(); k++) {
//					Point point3 = shapeobject3.getPoint(k);
//					if (boundingBox.getXMin() > point3.getX())
//						boundingBox.setXMin(point3.getX());
//					if (boundingBox.getXMax() < point3.getX())
//						boundingBox.setXMax(point3.getX());
//					if (boundingBox.getYMin() > point3.getY())
//						boundingBox.setYMin(point3.getY());
//					if (boundingBox.getYMax() < point3.getY())
//						boundingBox.setYMax(point3.getY());
//				}
//
//			}
//
//			break;
//		}
//	}
//
//	public TableDescription getTableDescription() {
//		return tableDescription;
//	}
//
//	protected static short swapBytes(short word0) {
//		int i = (word0 & 0xff) << 8;
//		int j = (word0 & 0xff00) >>> 8;
//		return (short) (i | j);
//	}
//
//	protected static int swapBytes(int i) {
//		int j = (i & 0xff) << 24;
//		int k = (i & 0xff00) << 8;
//		int l = (i & 0xff0000) >>> 8;
//		int i1 = (i & 0xff000000) >>> 24;
//		return j | k | l | i1;
//	}
//
//	protected static double swapBytes(double d) {
//		long l = Double.doubleToRawLongBits(d);
//		long l2 = (l & 255L) << 56;
//		long l3 = (l & 65280L) << 40;
//		long l4 = (l & 0xff0000L) << 24;
//		long l5 = (l & 0xff000000L) << 8;
//		long l6 = (l & 0xff00000000L) >>> 8;
//		long l7 = (l & 0xff0000000000L) >>> 24;
//		long l8 = (l & 0xff000000000000L) >>> 40;
//		long l9 = (l & 0xff00000000000000L) >>> 56;
//		long l1 = l2 | l3 | l4 | l5 | l6 | l7 | l8 | l9;
//		return Double.longBitsToDouble(l1);
//	}
//
//	protected static double swapBytes(long l) {
//		long l1 = l;
//		long l3 = (l1 & 255L) << 56;
//		long l4 = (l1 & 65280L) << 40;
//		long l5 = (l1 & 0xff0000L) << 24;
//		long l6 = (l1 & 0xff000000L) << 8;
//		long l7 = (l1 & 0xff00000000L) >>> 8;
//		long l8 = (l1 & 0xff0000000000L) >>> 24;
//		long l9 = (l1 & 0xff000000000000L) >>> 40;
//		long l10 = (l1 & 0xff00000000000000L) >>> 56;
//		long l2 = l3 | l4 | l5 | l6 | l7 | l8 | l9 | l10;
//		return Double.longBitsToDouble(l2);
//	}
//
//	protected static long swapBytesl(long l) {
//		long l1 = l;
//		long l3 = (l1 & 255L) << 56;
//		long l4 = (l1 & 65280L) << 40;
//		long l5 = (l1 & 0xff0000L) << 24;
//		long l6 = (l1 & 0xff000000L) << 8;
//		long l7 = (l1 & 0xff00000000L) >>> 8;
//		long l8 = (l1 & 0xff0000000000L) >>> 24;
//		long l9 = (l1 & 0xff000000000000L) >>> 40;
//		long l10 = (l1 & 0xff00000000000000L) >>> 56;
//		long l2 = l3 | l4 | l5 | l6 | l7 | l8 | l9 | l10;
//		return l2;
//	}
//
//	protected static long swapBytesl(double d) {
//		long l = Double.doubleToRawLongBits(d);
//		long l2 = (l & 255L) << 56;
//		long l3 = (l & 65280L) << 40;
//		long l4 = (l & 0xff0000L) << 24;
//		long l5 = (l & 0xff000000L) << 8;
//		long l6 = (l & 0xff00000000L) >>> 8;
//		long l7 = (l & 0xff0000000000L) >>> 24;
//		long l8 = (l & 0xff000000000000L) >>> 40;
//		long l9 = (l & 0xff00000000000000L) >>> 56;
//		long l1 = l2 | l3 | l4 | l5 | l6 | l7 | l8 | l9;
//		return l1;
//	}
//
//	public ArrayList<String> getWarningMessages() {
//		return messages;
//	}

}