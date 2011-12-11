package net.cloudengine.web.map;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import net.cloudengine.model.map.Tile;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractTileServer implements TileServer {
	
	private static Logger logger = LoggerFactory.getLogger(AbstractTileServer.class);
	
	protected abstract String getUrlTile(int zoom, int x, int y);

	@Override
	public Tile get(int zoom, int x, int y) {
		
		String url = this.getUrlTile(zoom, x, y);

		try {
			HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
			con.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/535.2 (KHTML, like Gecko) Chrome/15.0.874.121 Safari/535.2");
			con.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			con.setRequestProperty("Accept-Encoding", "gzip,deflate,sdch");
			con.setRequestProperty("Accept-Language", "es-ES,es;q=0.8");
			con.setRequestProperty("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.3");
			
			InputStream is = con.getInputStream();

			ByteArrayOutputStream buffer = new ByteArrayOutputStream();

			int nRead;
			byte[] data = new byte[16384];

			while ((nRead = is.read(data, 0, data.length)) != -1) {
			  buffer.write(data, 0, nRead);
			}

			buffer.flush();
			
			Tile tile = new Tile(x, y, zoom, this.getKey(), buffer.toByteArray());
			tile.setCacheable(true);
			return tile;
			
		} catch (MalformedURLException e) {
			logger.error("Error en la url de conexión para obtener tile del mapa", e);
			throw new RuntimeException(e);
		} catch (IOException e) {
			// si se produjo un error en la conexion envio como imagen del tile
			// un tile generico.
			try {
				InputStream is = MapController.class.getResourceAsStream("tile.png");

				ByteArrayOutputStream buffer = new ByteArrayOutputStream();
				IOUtils.copy(is, buffer);
				buffer.flush();
			
				Tile tile = new Tile(x, y, zoom, this.getKey(), buffer.toByteArray());
				return tile;
				
			} catch (Exception ex) {
				logger.error("Error al leer el tile por defualt del archivo.", e);
				throw new RuntimeException(ex);
			}
		}
	}

}
