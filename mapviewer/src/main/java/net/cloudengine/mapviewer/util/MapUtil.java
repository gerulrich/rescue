package net.cloudengine.mapviewer.util;

public class MapUtil {

    public static double position2lon(int x, int z) {
        double xmax = MapConstants.TILE_SIZE * (1 << z);
        return x / xmax * 360.0 - 180;
    }
	
    public static double position2lat(int y, int z) {
        double ymax = MapConstants.TILE_SIZE * (1 << z);
        return Math.toDegrees(Math.atan(Math.sinh(Math.PI - (2.0 * Math.PI * y) / ymax)));
    }

    public static double tile2lon(int x, int z) {
        return x / Math.pow(2.0, z) * 360.0 - 180;
    }

    public static double tile2lat(int y, int z) {
        return Math.toDegrees(Math.atan(Math.sinh(Math.PI - (2.0 * Math.PI * y) / Math.pow(2.0, z))));
    }

    public static int lon2position(double lon, int z) {
        double xmax = MapConstants.TILE_SIZE * (1 << z);
        return (int) Math.floor((lon + 180) / 360 * xmax);
    }

    public static int lat2position(double lat, int z) {
        double ymax = MapConstants.TILE_SIZE * (1 << z);
        return (int) Math.floor((1 - Math.log(Math.tan(Math.toRadians(lat)) + 1 / Math.cos(Math.toRadians(lat))) / Math.PI) / 2 * ymax);
    }
    
    /**
     * Calcula la cantidad de tiles en el sentido x que tiene el mapa
     * @return
     */
    public static int getXTileCount(int zoom) {
        return (1 << zoom);
    }

    /**
     * Calcula la cantidad de tiles en el sentido y que tiene el mapa
     * @return
     */
    public static int getYTileCount(int zoom) {
        return (1 << zoom);
    }

    /**
     * Calcula la cantidad de pixels en el sentido x que tiene el mapa
     * @return
     */
    public static int getXMax(int zoom) {
        return MapConstants.TILE_SIZE * getXTileCount(zoom);
    }

    /**
     * Calcula la cantidad de pixels en el sentido x que tiene el mapa
     * @return
     */
    public static int getYMax(int zoom) {
        return MapConstants.TILE_SIZE * getYTileCount(zoom);
    }
	
}
