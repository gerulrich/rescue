package net.cloudengine.model.statistics

class MapReduceUtil {
	
	static String sumAndCountMapFunction(String key, String sumField, String countField) {
		return """\
			function Map() { emit(${key}, {time:this.executionTime, ${countField}:1});}
		"""
	}
	
	static String sumAndCountReduceFunction(String sumField, String countField) {
		return """\
			function Reduce(key, values) {
				var sum = 0, count = 0;
				for (var i = 0; i < values.length; i++) {
		  			sum+= values[i].${sumField};
		  			count+=values[i].${countField};
				}
				return {${sumField}: sum, ${countField}: count};
			}
		"""
	}
	
	static String divideFinalizeFunction(String dividendo, String divisor) {
		return """\
			function Finalize(key, reduced) { 
				return reduced.${dividendo} / reduced.${divisor}; 
			}
		"""
	}
	
	
	private static final String FUNCTION_MAP_VALUE = "function Map() { emit(this.%s - (this.%s %% %s), {time:this.executionTime, count:1});}";
}
