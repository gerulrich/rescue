package net.cloudengine.service;

import java.util.Collection;

import net.cloudengine.dao.support.Page;
import net.cloudengine.model.console.Row;
import net.cloudengine.model.console.Table;


public interface SqlService {
	
	Collection<Table> getTables();
	
	Table getTableDetail(String name);
	
	Page<Row> getRows(String table, int page, int size);
	
	Long nextVal(String name);

}
