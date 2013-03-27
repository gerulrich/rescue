package net.cloudengine.web.statistics;

import net.cloudengine.web.MongoDBWrapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
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
	
	@Autowired
	public HistogramController(MongoDBWrapper mongoWrapper) {
		super();
		this.db = mongoWrapper.getFactory().getDb();
	}
	
	@RequestMapping("/stats/request")
	public @ResponseBody GraphSerie<Long, Long> requestGraph() {
		return new HistogramMapReduce().requestStats(db, "request_log", "time", TimeUnits.PER_MINUTE);
	}

}
