package net.cloudengine.reports

class ReportMetadata {
	
	String name = "";
	String datasource = "";
	List<ParameterMetadata> parameters = new ArrayList<ParameterMetadata>();
	
	void addParameter(ParameterMetadata parameter) {
		parameters.add(parameter);
	}

}
