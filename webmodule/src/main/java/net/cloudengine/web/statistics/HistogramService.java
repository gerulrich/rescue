package net.cloudengine.web.statistics;

import net.cloudengine.rest.model.statistics.GraphSerie;


public interface HistogramService {
	
	GraphSerie<Long, Long> count(String timestampColumn, String collection, TimeUnits time);
	
	GraphSerie<Long, Double> avg(String timestampColumn, String valueColumn, String collection, TimeUnits time);

}
