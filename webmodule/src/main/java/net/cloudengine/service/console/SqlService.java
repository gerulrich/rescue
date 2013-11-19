package net.cloudengine.service.console;

import java.util.Collection;

import net.cloudengine.api.PagingResult;
import net.cloudengine.model.console.Row;
import net.cloudengine.model.console.Table;


public interface SqlService {
	
	Collection<Table> getTables();
	
	Table getTableDetail(String name);
	
	PagingResult<Row> getRows(String table, int page, int size);
	
	Long nextVal(String name);

}
