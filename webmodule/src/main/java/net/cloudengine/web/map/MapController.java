package net.cloudengine.web.map;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import net.cloudengine.maps.GoogleMapsTileServer;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MapController {
	
	private static final String URI = "/tiles/{zoom}/{x}/{y}";
	
	// zoom 16
	// x 22092
	// y 39464
	
	@RequestMapping(value = URI, method = RequestMethod.GET)
	public ResponseEntity<byte[]> tile(@PathVariable("zoom") int zoom, @PathVariable("x") int x, @PathVariable("y") int y) {
		System.out.println("Buscando tile");
		TileServer server = new GoogleMapsTileServer();
		String url = server.tileUrl(zoom, x, y);
		
		
			try {
				InputStream is = new URL(url).openConnection().getInputStream();

				ByteArrayOutputStream buffer = new ByteArrayOutputStream();

				int nRead;
				byte[] data = new byte[16384];

				while ((nRead = is.read(data, 0, data.length)) != -1) {
				  buffer.write(data, 0, nRead);
				}

				buffer.flush();
				byte[] imagen = buffer.toByteArray();

				HttpHeaders responseHeaders = new HttpHeaders();
				responseHeaders.set("Content-Type", "image/png");
				
				return new ResponseEntity<byte[]>(imagen, responseHeaders, HttpStatus.OK);		
							
			
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		
	}	
	

	
}
