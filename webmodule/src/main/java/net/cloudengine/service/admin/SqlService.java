package net.cloudengine.service.admin;

import java.util.Collection;

import net.cloudengine.api.PagingResult;
import net.cloudengine.model.sql.Row;
import net.cloudengine.model.sql.Table;


public interface SqlService {
	
	Collection<Table> getTables();
	
	Table getTableDetail(String name);
	
	PagingResult<Row> getRows(String table, int page, int size);
	
	Long nextVal(String name);

}
