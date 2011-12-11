package net.cloudengine.mapviewer.tiles;

public enum TileServerType {
	
	LOCAL {
		public String getTileString(String baseUrl, int xtile, int ytile, int zoom) {
			String url = baseUrl+"/"+zoom+"/"+xtile+"/"+ytile;
			return url; // return URL for the tile
		}
	},
	OPENSTREET {
		public String getTileString(String baseUrl, int xtile, int ytile, int zoom) {
			String number = ("" + zoom + "/" + xtile + "/" + ytile);
			String url = baseUrl + number + ".png";
        	return url;
		}
	},
	GOOGLEMAPS {
		public String getTileString(String baseUrl, int xtile, int ytile, int zoom) {
			String url = baseUrl+"&x="+xtile+"&y="+ytile+"&z="+zoom+"&s=Galil";
			return url; // return URL for the tile
		}
	},
	WMS {
		public String getTileString(String baseUrl, int xtile, int ytile, int zoom) {
			throw new RuntimeException("Not yet implemented");
//			    BoundingBox bb = MapWidget.tile2boundingBox(xtile, ytile, zoom);
//	        	String url = tileServer.getURL()+"?SERVICE=WMS&VERSION=1.1.1&REQUEST" +
//	        	"=GetMap&layers=rosario:rosario_calles&STYLES=&EXCEPTIONS=application%2Fvnd.ogc.se_inimage&FORMAT=image%2Fjpeg&SRS=EPSG%3A4326&" +
//	        	"BBOX="+bb.west+","+bb.south+","+bb.east+","+bb.north+"&WIDTH=256&HEIGHT=256";
//	        	System.out.println(url);
//	         
//	        	return url;
		}
	};
	
	public abstract String getTileString(String baseUrl, int xtile, int ytile, int zoom);    
}