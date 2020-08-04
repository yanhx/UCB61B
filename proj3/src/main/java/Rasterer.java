import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {

    public Rasterer() {
        // YOUR CODE HERE
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     * <p>
     * The grid of images must obey the following properties, where image in the
     *  is referred to as a "tile".
     * <ul>
     *     <li>The tiles collected must cover the most longitudinal distance per pixel
     *     (LonDPP) possible, while still covering less than or equal to the amount of
     *     longitudinal distance per pixel in the query box for the user viewport size. </li>
     *     <li>Contains all tiles that intersect the query bounding box that fulfill the
     *     above condition.</li>
     *     <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     * </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     * forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        //System.out.println(params);
        Map<String, Object> results = new HashMap<>();
        if (!isParamsVaild(params)) {
            results.put("query_success", false);
            return results;
        }
        //System.out.println("Since you haven't implemented getMapRaster, nothing is displayed in "
        //                   + "your browser.");
        double lrlon = params.get("lrlon"), ullon = params.get("ullon"), lrlat = params.get("lrlat"), ullat = params.get("ullat");
        double lonDPP = (lrlon - ullon) / params.get("w");
        int d = 0, tileNum = 1;
        double picLonDPP = (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON) / MapServer.TILE_SIZE;
        while (picLonDPP > lonDPP && d <= 6) {
            picLonDPP /= 2;
            d++;
            tileNum *= 2;
        }
        int startlon = (int) ((ullon - MapServer.ROOT_ULLON) * (1 - Double.MIN_VALUE) / (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON) * tileNum),
                endlon = (int) ((lrlon - MapServer.ROOT_ULLON) * (1 - Double.MIN_VALUE) / (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON) * tileNum),
                startlat = (int) ((ullat - MapServer.ROOT_ULLAT) * (1 - Double.MIN_VALUE) / (MapServer.ROOT_LRLAT - MapServer.ROOT_ULLAT) * tileNum),
                endlat = (int) ((lrlat - MapServer.ROOT_ULLAT) * (1 - Double.MIN_VALUE) / (MapServer.ROOT_LRLAT - MapServer.ROOT_ULLAT) * tileNum);

        String[][] render_grid = new String[endlat - startlat + 1][endlon - startlon + 1];
        for (int i = startlat; i <= endlat; i++) {
            for (int j = startlon; j <= endlon; j++) {
                render_grid[i - startlat][j - startlon] = "d" + d + "_x" + j + "_y" + i + ".png";
            }
        }
        results.put("render_grid", render_grid);
        results.put("raster_ul_lon", getBoundary(tileNum, startlon, startlat)[0]);
        results.put("raster_ul_lat", getBoundary(tileNum, startlon, startlat)[1]);
        results.put("raster_lr_lon", getBoundary(tileNum, endlon, endlat)[2]);
        results.put("raster_lr_lat", getBoundary(tileNum, endlon, endlat)[3]);
        results.put("depth", d);
        results.put("query_success", true);
        return results;
    }

    private boolean isParamsVaild(Map<String, Double> params) {
        double lrlon = params.get("lrlon"), ullon = params.get("ullon"), lrlat = params.get("lrlat"), ullat = params.get("ullat"), w = params.get("w"), h = params.get("h");
        if (lrlat >= ullat || lrlon <= ullon || h <= 0 || w <= 0) {
            return false;
        }
        if (lrlat >= MapServer.ROOT_ULLAT || ullat <= MapServer.ROOT_LRLAT || lrlon <= MapServer.ROOT_ULLON || ullon >= MapServer.ROOT_LRLON) {
            return false;
        }
        return true;
    }

    public double[] getBoundary(int tileNum, int x, int y) {
        double[] boundary = new double[4];
        double tmp = MapServer.ROOT_ULLON * (double) x / tileNum + MapServer.ROOT_LRLON * (double) (tileNum - x) / tileNum;
        boundary[0] = MapServer.ROOT_ULLON * (tileNum - x) / tileNum + MapServer.ROOT_LRLON * x / tileNum;
        boundary[1] = MapServer.ROOT_ULLAT * (tileNum - y) / tileNum + MapServer.ROOT_LRLAT * y / tileNum;
        boundary[2] = MapServer.ROOT_ULLON * (tileNum - x - 1) / tileNum + MapServer.ROOT_LRLON * (x + 1) / tileNum;
        boundary[3] = MapServer.ROOT_ULLAT * (tileNum - y - 1) / tileNum + MapServer.ROOT_LRLAT * (y + 1) / tileNum;
        return boundary;
    }

}
