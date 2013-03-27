package net.cloudengine.reports

class ReportMetadata {
	
	String name;
	List<ParameterMetadata> parameters = new ArrayList<ParameterMetadata>();
	
	void addParameter(ParameterMetadata parameter) {
		parameters.add(parameter);
	}

}
