package net.dbf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import net.shapefile.InvalidDescriptorNameException;
import net.shapefile.InvalidFieldNameException;

public class Record
{
  private ArrayList<RecordField> fields = new ArrayList<RecordField>();

  public Record() {
  }

  public Record(Collection<RecordField> paramCollection) {
    fields = new ArrayList<RecordField>(paramCollection);
  }

  public void setField(RecordField paramRecordField) {
    boolean existField = false;
    for (RecordField localRecordField : fields) {
    	if (localRecordField.getName().equals(paramRecordField.getName())) {
            localRecordField.setName(paramRecordField.getName());
            localRecordField.setValue(paramRecordField.getValue());
            existField = true;
            break;
    	}
    }
    if (!existField)
      throw new InvalidFieldNameException(paramRecordField.getName() + " is not the name of a current RecordField.");
  }

  public void setFields(Collection<RecordField> paramCollection) {
    fields = new ArrayList<RecordField>(paramCollection);
  }

  public void addField(RecordField paramRecordField) {
	boolean existField = false;
    for (RecordField localRecordField : fields) {
    	if (localRecordField.getName().equals(paramRecordField.getName())) {
            existField = true;
            break;
    	}
    }
    if (existField)
      throw new InvalidFieldNameException("There already exists a RecordField with the name " + paramRecordField.getName());
    fields.add(new RecordField(paramRecordField.getName(), paramRecordField.getValue()));
  }

  public void removeField(String paramString) {
    Iterator<RecordField> localIterator = fields.iterator();
    int i = 0;
    for (int j = 0; localIterator.hasNext(); j++) {
      RecordField localRecordField = (RecordField)localIterator.next();
      if (!localRecordField.getName().equals(paramString))
        continue;
      i = 1;
      fields.remove(j);
      break;
    }
    if (i != 1)
      throw new InvalidDescriptorNameException(paramString + " is not the name of a current RecordField");
  }

  public void removeField(int paramInt) {
    fields.remove(paramInt);
  }

  public RecordField getField(String paramString) {
    for (RecordField localRecordField : fields) {
      if (localRecordField.getName().equals(paramString))
        return localRecordField;
    }
    return null;
  }

  public RecordField getField(int paramInt) {
    return (RecordField)fields.get(paramInt);
  }

  public ArrayList<RecordField> getFields() {
    return fields;
  }

  public int getFieldCount() {
    return fields.size();
  }
}