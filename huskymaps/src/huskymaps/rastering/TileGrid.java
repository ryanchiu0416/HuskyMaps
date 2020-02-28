package huskymaps.rastering;

import java.util.Arrays;

/**
 * Represents a grid of {@link Tile}s to be served in response to a browser's raster request.
 */
public class TileGrid {

    /** The 2-dimensional string grid of map tiles. */
    public final Tile[][] grid;
    /** The bounding upper-left, lower-right latitudes and longitudes of the final image. */
    public final double ullat;
    public final double ullon;
    public final double lrlat;
    public final double lrlon;

    /** Return a new TileGrid with the given grid. */
    public TileGrid(Tile[][] grid) {
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            this.grid = null;
            this.ullat = 0;
            this.ullon = 0;
            this.lrlat = 0;
            this.lrlon = 0;
        } else {
            this.grid = grid;
            Tile gridUl = grid[0][0];
            Tile gridLr = grid[grid.length - 1][grid[0].length - 1].offset();
            this.ullat = gridUl.lat();
            this.ullon = gridUl.lon();
            this.lrlat = gridLr.lat();
            this.lrlon = gridLr.lon();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TileGrid that = (TileGrid) o;
        return Arrays.deepEquals(grid, that.grid);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(grid);
    }

    @Override
    public String toString() {
        return "TileGrid{" +
                "grid=" + Arrays.deepToString(grid) +
                '}';
    }
}
