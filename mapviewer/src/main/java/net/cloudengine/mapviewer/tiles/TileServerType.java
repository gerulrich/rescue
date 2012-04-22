package net.cloudengine.mapviewer.tiles;

public enum TileServerType {
	
	GOOGLEMAPS {
		public String getTileString(String baseUrl, int xtile, int ytile, int zoom) {
			return baseUrl+"/"+zoom+"/"+xtile+"/"+ytile+"/map.google.street/";
		}
	},
	
	GOOGLESAT {
		public String getTileString(String baseUrl, int xtile, int ytile, int zoom) {
			return baseUrl+"/"+zoom+"/"+xtile+"/"+ytile+"/map.google.sat/";
		}
	},
	
	OPENSTREET {
		public String getTileString(String baseUrl, int xtile, int ytile, int zoom) {
			return baseUrl+"/"+zoom+"/"+xtile+"/"+ytile+"/map.osm/";
		}
	},
	
	GOOGLEMAPS_DIRECT {
		public String getTileString(String baseUrl, int xtile, int ytile, int zoom) {
			String url = baseUrl+"&x="+xtile+"&y="+ytile+"&z="+zoom+"&s=Galil";
			return url; // return URL for the tile
		}
	},
	
	WMS {
		public String getTileString(String baseUrl, int xtile, int ytile, int zoom) {
			throw new RuntimeException("Not yet implemented");
		}
	};
	
	public abstract String getTileString(String baseUrl, int xtile, int ytile, int zoom);    
}