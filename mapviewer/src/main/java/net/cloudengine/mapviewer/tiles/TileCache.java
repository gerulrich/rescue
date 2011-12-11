package net.cloudengine.mapviewer.tiles;

import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.swt.widgets.Display;

public class TileCache {
	
	private static final long serialVersionUID = 1L;
	private static final int CACHE_SIZE = 256; //FIXME
	private LinkedHashMap<Tile, AsyncImage> map = new LinkedHashMap<Tile, AsyncImage>(CACHE_SIZE, 0.75f, true) {

		protected boolean removeEldestEntry(Map.Entry<Tile, AsyncImage> eldest) {
			boolean remove = size() > CACHE_SIZE;
			if (remove)
				eldest.getValue().dispose(Display.getCurrent());
			return remove;
		}
	};

	public void put(TileServer tileServer, int x, int y, int z, AsyncImage image) {
		map.put(new Tile(tileServer.getURL(), x, y, z), image);
	}

	public AsyncImage get(TileServer tileServer, int x, int y, int z) {
		return map.get(new Tile(tileServer.getURL(), x, y, z));
	}

	public void remove(TileServer tileServer, int x, int y, int z) {
		map.remove(new Tile(tileServer.getURL(), x, y, z));
	}

	public int getSize() {
		return map.size();
	}
}