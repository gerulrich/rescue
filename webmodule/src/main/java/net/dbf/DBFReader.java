package net.dbf;

import java.io.Closeable;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * <p>Title: DBF4J</p>
 * <p>Description: Used to read database (DBF) files.<p>
 *
 * Create a DBFReader object passing a file name to be opened, and use hasNextRecord and nextRecord
 * functions to iterate through the records of the file. <br>
 * The getFieldCount and getField methods allow you to find out what are the fields of the database file. </p>
 */
public class DBFReader implements Closeable {
	
	private static final Logger logger = LoggerFactory.getLogger(DBFReader.class);

    private DataInputStream stream = null;
    private byte nextRecord[] = null;
    
    private List<TableDescriptor> descriptors;

    /**
     * Opens a DBF file for reading.
     * @param s String
     * @throws JDBFException
     */
    public DBFReader(String s) throws JDBFException {
        stream = null;
        nextRecord = null;
        try {
            init(new FileInputStream(s.trim()));
        }
        catch (FileNotFoundException filenotfoundexception) {
            throw new JDBFException(filenotfoundexception);
        }
    }

    /**
     * Opens a stream, containing DBF for reading.
     * @param inputstream InputStream
     * @throws JDBFException
     */
    public DBFReader(InputStream inputstream) throws JDBFException {
        stream = null;
        nextRecord = null;
        init(inputstream);
    }

    /**
     * Initialises the reader
     * @param inputstream InputStream
     * @throws JDBFException
     */
    private void init(InputStream inputstream) throws JDBFException {
        try {
            stream = new DataInputStream(inputstream);
            int fieldCount = readHeader();
            
            int j = 1;
            descriptors = readFieldHeaders(fieldCount);
            for (TableDescriptor td : descriptors) {
            	j += td.getWidth();
            }
            stream.readByte();

            nextRecord = new byte[j];
            try {
                stream.readFully(nextRecord);
            }
            catch (EOFException eofexception) {
                nextRecord = null;
                stream.close();
            }
        }
        catch (IOException ioexception) {
            throw new JDBFException(ioexception);
        }
    }

    /**
     * Reads header
     * @throws IOException
     * @throws JDBFException
     * @return int
     */
    private int readHeader() throws IOException, JDBFException {
        
    	byte abyte0[] = new byte[16];
        try {
            stream.readFully(abyte0);
        }
        catch (EOFException eofexception) {
            throw new JDBFException("Unexpected end of file reached.");
        }
        int i = abyte0[8];
        if (i < 0)
            i += 256;
        i += 256 * abyte0[9];
        i = --i / 32;
        i--;
        
        try {
            stream.readFully(abyte0);
        }
        catch (EOFException eofexception1) {
            throw new JDBFException("Unexpected end of file reached.");
        }
        return i;
    }

    /**
     * Reads field header
     * @throws IOException
     * @throws JDBFException
     * @return JDBField
     */
    private List<TableDescriptor> readFieldHeaders(int fieldCount) throws IOException, JDBFException {
    	
    	List<TableDescriptor> descriptors = new ArrayList<TableDescriptor>();
    
    	for(int hh = 0; hh < fieldCount; hh++) {
    	
    		byte abyte0[] = new byte[16];
    		stream.readFully(abyte0);
    		StringBuffer stringbuffer = new StringBuffer(10);
    		for (int i = 0; i < 10; i++) {
    			if (abyte0[i] == 0)
    				break;
    			stringbuffer.append((char)abyte0[i]);
    		}
        
    		char c = (char)abyte0[11];
    		stream.readFully(abyte0);
    	
    		int j = abyte0[0];
    		int k = abyte0[1];
    		if (j < 0)
    			j += 256;
    		if (k < 0)
    			k += 256;
    		
    		String fieldName = stringbuffer.toString();
    		if (logger.isDebugEnabled()) {
    			logger.debug("Field Header. name: {}, width: {}, type: {}, presicion {}", new Object[] {fieldName, j, c, k});
    		}
      
    		descriptors.add(new TableDescriptor(fieldName, c, j));
   		
    	} 
        
    	return descriptors;
    
    }

    /**
     * Returns the field count of the database file.
     * @return int
     */
    public int getFieldCount() {
        return descriptors.size();
    }

    /**
     * Returns a field at a specified position.
     * @param i int
     * @return JDBField
     */
    public TableDescriptor getField(int i) {
        return descriptors.get(i);
    }

    /**
     * Checks to see if there are more records in the file.
     * @return boolean
     */
    public boolean hasNext() {
        return nextRecord != null;
    }
    
    /**
     * Returns an array of objects, representing one record in the database file.
     * @throws JDBFException
     * @return Object[]
     */
    public Record next() throws JDBFException {
        if (!hasNext())
            throw new JDBFException("No more records available.");
        
        int fieldCount = descriptors.size();
        
        Record record = new Record();
        
        for (int j = 0, offset = 1; j < fieldCount; j++) {
            
        	int length = descriptors.get(j).getWidth();
            
            Object value = FieldParser.parse(new String(nextRecord, offset, length), 'F');
            
            RecordField field = new RecordField(this.getField(j).getName(), value.toString());
            record.addField(field);
            
            offset += length;
        }

        try {
            stream.readFully(nextRecord);
        }
        catch (EOFException eofexception) {
            nextRecord = null;
        }
        catch (IOException ioexception) {
            throw new JDBFException(ioexception);
        }
        return record;
    }
    
    @Override
    public void close() throws IOException {
		stream.close();
    }
}