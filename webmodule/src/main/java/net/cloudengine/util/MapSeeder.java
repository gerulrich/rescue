package net.cloudengine.util;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MapSeeder {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		
		int zoomInicial = 12;
		int zoomFinal = 18;
		
		long xmin = 1382;
		long xmax = 1384;

		long ymin = 2467;
		long ymax = 2469;
		

		long contador = 0;
		for(int zoom = zoomInicial; zoom <= zoomFinal; zoom++) {
			
			for (long x = xmin; x <= xmax; x++) {
				
				for(long y = ymin; y <= ymax; y++) {

					String url = "http://localhost:8080/webmodule/tiles/"+zoom+"/"+x+"/"+y+"/"+"map.osm/";

					HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
					InputStream is = con.getInputStream();
//
					byte[] data = new byte[16384];
//
					while ((is.read(data, 0, data.length)) != -1) {
//
					}
					contador++;
				}
				
			}
			
			
			
			
			xmin = xmin*2;
			xmax = xmax*2;
			
			ymin = ymin*2;
			ymax = ymax*2;
			
		}
		
		System.out.println("Cantidad de tiles:"+contador);
		

	}

}
