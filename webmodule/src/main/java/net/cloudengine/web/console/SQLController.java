package net.cloudengine.web.console;

import java.util.Arrays;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import net.cloudengine.api.PagingResult;
import net.cloudengine.model.console.Row;
import net.cloudengine.model.console.Table;
import net.cloudengine.service.console.SqlService;
import net.cloudengine.util.HexString;

import org.apache.commons.codec.DecoderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@Controller()
public class SQLController {
	
	private static final Logger logger = LoggerFactory.getLogger(SQLController.class);
	
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
		Table table = sqlService.getTableDetail(HexString.decode(tableName));
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
		PagingResult<Row> pageResult = sqlService.getRows(HexString.decode(tableName), page, size);
		String fields[] = null;
		if (pageResult.getTotalSize() > 0) {
			fields = pageResult.getList().get(0).getAliases();
		} else {
			fields = new String[0];
		}
		
		ModelAndView mav = new ModelAndView();
		mav.addObject("table", HexString.decode(tableName));
		mav.addObject("tableEncodedName", tableName);
		mav.addObject("rows", pageResult);
		mav.addObject("fields", Arrays.asList(fields));
		mav.addObject("rowsCount", pageResult.getPageSize());
		mav.setViewName("/console/sql/table_data");
		return mav;
	}
	
	@ExceptionHandler(DecoderException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Invalid table name")
	public void handleDecoderException(DecoderException ex, HttpServletRequest request) {
		logger.error("Invalid table name", ex);
	}
	
	@ExceptionHandler(BadSqlGrammarException.class)
	@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Invalid table name")
	public void handleBadSqlGrammarException(BadSqlGrammarException ex, HttpServletRequest request) {
		logger.error("Invalid table name", ex);
	}
	
}
