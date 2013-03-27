package net.cloudengine.web.console;

import java.util.Arrays;
import java.util.Collection;

import net.cloudengine.api.PagingResult;
import net.cloudengine.model.sql.Row;
import net.cloudengine.model.sql.Table;
import net.cloudengine.service.admin.SqlService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller()
public class SQLController {
	
	private SqlService sqlService;
	
	@Autowired
	public SQLController(SqlService sqlService) {
		super();
		this.sqlService = sqlService;
	}
	
	@RequestMapping(value="/sql/list", method = RequestMethod.GET)
	public ModelAndView sqlTables() {
		Collection<Table> tables = sqlService.getTables();
		ModelAndView mav = new ModelAndView();
		mav.addObject("tables", tables);
		mav.setViewName("/console/sql/sql");
		return mav;
	}
	
	@RequestMapping(value="/sql/table/{table}", method = RequestMethod.GET)
	public ModelAndView sqlTable(@PathVariable("table") String tableName) {
		Table table = sqlService.getTableDetail(tableName);
		ModelAndView mav = new ModelAndView();
		mav.addObject("table", table);
		mav.setViewName("/console/sql/table");
		return mav;
	}
	
	
	@RequestMapping(value="/sql/table/list/{table}", method = RequestMethod.GET)
		public ModelAndView listTable(@PathVariable("table") String tableName) {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/sql/table/list/{table}/1/25");
		return mav;
	}

	
	@RequestMapping(value="/sql/table/list/{table}/{page}/{size}", method = RequestMethod.GET)
	public ModelAndView listTable(@PathVariable("table") String tableName, @PathVariable("page") int page, @PathVariable("size") int size) {
		// FIXME chequear nombre de la tabla para evitar sql injection.
		
		PagingResult<Row> pageResult = sqlService.getRows(tableName, page, size);
		String fields[] = null;
		if (pageResult.getTotalSize() > 0) {
			fields = pageResult.getList().get(0).getAliases();
		} else {
			fields = new String[0];
		}
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("table", tableName);
		mav.addObject("rows", pageResult);
		mav.addObject("fields", Arrays.asList(fields));
		mav.addObject("rowsCount", pageResult.getPageSize());
		mav.setViewName("/console/sql/table_data");
		return mav;
	}	
	

	
	
}
