package net.cloudengine.api.mongo.dao;

import java.util.List;

import net.cloudengine.rest.model.statistics.Tuple;
import net.cloudengine.web.statistics.TimeUnits;

public interface MapReduceService {
	
	List<Tuple<String,Double>> topMethods(TimeUnits time);

}
