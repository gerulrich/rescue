package net.cloudengine.web.statistics;

import java.util.List;

import net.cloudengine.api.mongo.MongoDBWrapper;
import net.cloudengine.api.mongo.dao.MapReduceService;
import net.cloudengine.management.IgnoreTrace;
import net.cloudengine.rest.model.statistics.GraphSerie;
import net.cloudengine.rest.model.statistics.Tuple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mongodb.DB;

/**
 * Controller que permite generar diferentes graficos (histogramas).
 * @author German Ulrich
 *
 */
@Controller
public class HistogramController {

	private DB db;
	private HistogramService service;
	private MapReduceService rmService;
	
	@Autowired
	public HistogramController(MongoDBWrapper mongoWrapper, HistogramService service, MapReduceService rmService) {
		super();
		this.db = mongoWrapper.getFactory().getDb();
		this.service = service;
		this.rmService = rmService;
	}
	
	@RequestMapping("/stats/request/{period}")
	@IgnoreTrace
	public @ResponseBody GraphSerie<Long, Long> requestGraph(@PathVariable("period") TimeUnits period) {
		return service.count("time", "request_log", period);
	}
	
	@RequestMapping("/stats/avg/{period}")
	@IgnoreTrace
	public @ResponseBody GraphSerie<Long, Double> avgGraph(@PathVariable("period") TimeUnits period) {
		return service.avg("time", "executionTime", "request_log", period);
	}
	
	@RequestMapping("/stats/topMethods")
	@IgnoreTrace
	public @ResponseBody List<Tuple<String,Double>> topMethodsGraph() {
//		return new HistogramMapReduce().topMethods(db, "request_log", "time", TimeUnits.PER_MINUTE);
		return this.rmService.topMethods(TimeUnits.PER_MINUTE);
	}
	
	@RequestMapping("/stats/topErrorMethods")
	@IgnoreTrace
	public @ResponseBody List<Tuple<String,Double>> topErrorMethodsGraph() {
		return new HistogramMapReduce().topErrorMethods(db, "request_log", "time", TimeUnits.PER_MINUTE);
	}

}
