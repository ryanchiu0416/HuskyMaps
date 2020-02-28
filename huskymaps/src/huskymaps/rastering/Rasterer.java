package huskymaps.rastering;

import huskymaps.graph.Coordinate;

public interface Rasterer {
    /**
     * Given the bounding box of a user viewport, finds the grid of images that best matches the
     * viewport. These images will be combined into one big image (rastered) by the front end. <br>
     *
     * The grid of images must obey the following properties, where image in the grid is referred
     * to as a "tile".
     * <ul>
     *     <li>The tiles must be from the depth requested.</li>
     *     <li>The tiles collected must cover the most longitudinal distance per pixel
     *     (LonDPP) possible, while still covering less than or equal to the amount of
     *     longitudinal distance per pixel in the query box for the user viewport size. </li>
     *     <li>Contains all tiles that intersect the query bounding box that fulfill the
     *     above condition.</li>
     *     <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     * </ul>
     *
     * @param ul the upper-left coordinate of the user viewport
     * @param lr the lower-right coordinate of the user viewport
     * @param depth the depth of tiles to return
     * @return the grid of tiles meeting the conditions described
     */
    TileGrid rasterizeMap(Coordinate ul, Coordinate lr, int depth);
}
