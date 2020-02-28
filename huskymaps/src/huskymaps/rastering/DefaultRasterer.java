package huskymaps.rastering;

import huskymaps.graph.Coordinate;
import huskymaps.utils.Constants;
/**
 * @see Rasterer
 */
public class DefaultRasterer implements Rasterer {
    @SuppressWarnings("checkstyle:LocalVariableName")
    public TileGrid rasterizeMap(Coordinate ul, Coordinate lr, int depth) {
        int[] xPair = new int[2];
        int[] yPair = new int[2];

        xPair[0] = (int) ((ul.lon() - Constants.ROOT_ULLON) / Constants.LON_PER_TILE[depth]);
        yPair[0] = (int) ((Constants.ROOT_ULLAT - ul.lat()) / Constants.LAT_PER_TILE[depth]);

        xPair[1] = (int) ((lr.lon() - Constants.ROOT_ULLON) / Constants.LON_PER_TILE[depth]);
        yPair[1] = (int) ((Constants.ROOT_ULLAT - lr.lat()) / Constants.LAT_PER_TILE[depth]);

        Tile[][] grid = new Tile[yPair[1] - yPair[0] + 1][xPair[1] - xPair[0] + 1];
        for (int y = 0; y < yPair[1] - yPair[0] + 1; y++) {
            for (int x = 0; x < xPair[1] - xPair[0] + 1; x++) {
                grid[y][x] = new Tile(depth, xPair[0] + x, yPair[0] + y);
            }
        }

        return new TileGrid(grid);
    }
}
