package net.cloudengine.service.admin;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import net.cloudengine.api.PagingResult;
import net.cloudengine.api.SimplePagingResult;
import net.cloudengine.model.sql.Column;
import net.cloudengine.model.sql.Row;
import net.cloudengine.model.sql.Table;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SqlServiceImpl implements SqlService {

    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}


	@Override
	@Transactional(readOnly=true)
	public Collection<Table> getTables() {
		List<Table> tables = this.jdbcTemplate.query(
			"SELECT TABLE_NAME, ENGINE, TABLE_ROWS, DATA_LENGTH , INDEX_LENGTH "+ 
			"  FROM information_schema.TABLES "+
			"  WHERE TABLES.TABLE_SCHEMA = DATABASE() "+
			"  AND TABLES.TABLE_TYPE = 'BASE TABLE'",
		    new RowMapper<Table>() {
				public Table mapRow(ResultSet rs, int rowNum) throws SQLException {
					Table table = new Table();
					table.setName(rs.getString("TABLE_NAME"));
					table.setEngine(rs.getString("ENGINE"));
					table.setRows(rs.getLong("TABLE_ROWS"));
					table.setSize(rs.getLong("DATA_LENGTH"));
					table.setIndexSize(rs.getLong("INDEX_LENGTH"));
	                return table;
	            }
			});
		
		return tables;

	}
	
	
	@Override
	@Transactional
	public Table getTableDetail(String name) {
		List<Column> columns = this.jdbcTemplate.query(
			"SHOW COLUMNS FROM "+name,
			new RowMapper<Column>() {
				public Column mapRow(ResultSet rs, int rowNum) throws SQLException {
					Column column = new Column();
			    	column.setName(rs.getString("Field"));
			    	column.setType(rs.getString("Type"));	
			        return column;
				}
		});
		
		Table t = new Table();
		t.setName(name);
		t.setColumns(columns);
		return t;
	}
	
	@Transactional
	@Override
	public PagingResult<Row> getRows(String table, int page, int size) {
		long offset = (page-1)*size;
		
		long count = this.jdbcTemplate.queryForLong(
			"SELECT count(*) FROM "+table
		);
		
		List<Row> tuples = this.jdbcTemplate.query(
			"SELECT * FROM "+table+" LIMIT "+offset+","+size,
			new RowMapper<Row>() {
			public Row mapRow(ResultSet rs, int rowNum) throws SQLException {
				ResultSetMetaData rsMetaData = rs.getMetaData();
			    int numberOfColumns = rsMetaData.getColumnCount();
			    String aliases[] = new String[numberOfColumns];
			    Object tuple[] = new Object[numberOfColumns];
			    for (int i = 0; i < numberOfColumns; i++) {
			    	aliases[i] = rsMetaData.getColumnName(i+1);
			    	tuple[i] = rs.getObject(i+1);
			    }
				Row row = new Row(tuple,aliases);
		        return row;
			}
		});
		
		return new SimplePagingResult<Row>(page, size, count, tuples);

	}

	@Override
	@Transactional	
	public Long nextVal(String name) {
		return this.jdbcTemplate.queryForLong("SELECT get_next_value('"+name+"')"
		);
	}	

}