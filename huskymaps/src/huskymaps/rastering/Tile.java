package huskymaps.rastering;

import java.util.Objects;

import static huskymaps.utils.Constants.MIN_X_TILE_AT_DEPTH;
import static huskymaps.utils.Constants.MIN_Y_TILE_AT_DEPTH;
import static huskymaps.utils.Constants.MIN_ZOOM_LEVEL;

/**
 * Represents the name of a single image tile.
 */
public class Tile {
    public final int depth;
    public final int x;
    public final int y;

    public Tile(int depth, int x, int y) {
        this.depth = depth;
        this.x = x;
        this.y = y;
    }

    public Tile offset() {
        return new Tile(depth, x + 1, y + 1);
    }

    /**
     * Return the latitude of the upper-left corner of the given slippy map tile.
     * @return latitude of the upper-left corner
     * @source https://wiki.openstreetmap.org/wiki/Slippy_map_tilenames
     */
    public double lat() {
        double n = Math.pow(2.0, MIN_ZOOM_LEVEL + depth);
        int slippyY = MIN_Y_TILE_AT_DEPTH[depth] + y;
        double latRad = Math.atan(Math.sinh(Math.PI * (1 - 2 * slippyY / n)));
        return Math.toDegrees(latRad);
    }

    /**
     * Return the longitude of the upper-left corner of the given slippy map tile.
     * @return longitude of the upper-left corner
     * @source https://wiki.openstreetmap.org/wiki/Slippy_map_tilenames
     */
    public double lon() {
        double n = Math.pow(2.0, MIN_ZOOM_LEVEL + depth);
        int slippyX = MIN_X_TILE_AT_DEPTH[depth] + x;
        return slippyX / n * 360.0 - 180.0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Tile tile = (Tile) o;
        return depth == tile.depth &&
                x == tile.x &&
                y == tile.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(depth, x, y);
    }

    @Override
    public String toString() {
        return "d" + depth + "_x" + x + "_y" + y + ".jpg";
    }
}
