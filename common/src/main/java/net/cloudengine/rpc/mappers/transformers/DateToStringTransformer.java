package net.cloudengine.rpc.mappers.transformers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.cloudengine.rpc.mappers.ValueTransformer;
import net.cloudengine.util.DateUtil;

public class DateToStringTransformer implements ValueTransformer {

	@Override
	public Object transform(Object source) {
		if (source == null) {
			return null;
		}
		if (!(source instanceof Date)) {
			throw new IllegalArgumentException("The argument must be a date");
		}
		DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return format.format((Date)source);
	}

	@Override
	public Object inverse(Object target) {
		if (target != null)
			return DateUtil.convertToDate(target.toString());
		return null;
	}

	
}
