package net.cloudengine.model.map.geo

class GeoData {
	
	String type;
	Object coordinates;
	
	GeoData(String type, Object coordinates) {
		super();
		this.type = type;
		this.coordinates = coordinates;
	}

}
