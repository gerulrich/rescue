package net.cloudengine.model.map.geo


import java.util.List;

class FatureCollection {

	String type = 'FeatureCollection';
	GeoFeature[] features;

	FatureCollection(List<? extends Feature> featuresList) {
		features = new GeoFeature[featuresList.size()];
		featuresList.eachWithIndex {  f, idx ->
			features[idx] = new GeoFeature(f);
		}
	}
}
