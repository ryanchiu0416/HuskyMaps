package huskymaps.handlers.rastering;

import huskymaps.graph.Coordinate;
import huskymaps.handlers.APIRouteHandler;
import huskymaps.rastering.Rasterer;
import huskymaps.rastering.Tile;
import huskymaps.rastering.TileGrid;
import spark.Request;
import spark.Response;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Objects;

import static huskymaps.utils.Constants.IMG_ROOT;
import static huskymaps.utils.Constants.MAX_DEPTH;
import static huskymaps.utils.Constants.TILE_SIZE;

/**
 * Handles requests from the web browser for map images. These images will be
 * rastered into one large image to be displayed to the user.
 */
public class RasterAPIHandler extends APIRouteHandler<RasterRequest, RenderedRasterResult> {

    private final Rasterer rasterer;
    // Render the result as an image if successful

    public RasterAPIHandler(Rasterer rasterer) {
        this.rasterer = rasterer;
    }

    @Override
    protected RasterRequest parseRequest(Request request) {
        return RasterRequest.from(request);
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query.
     * @param request RasterRequest
     * @param response Ignored
     * @return RenderedRasterResult
     */
    @Override
    protected RenderedRasterResult processRequest(RasterRequest request, Response response) {
        Coordinate ul = new Coordinate(request.ullat, request.ullon);
        Coordinate lr = new Coordinate(request.lrlat, request.lrlon);
        TileGrid raster = rasterer.rasterizeMap(ul, lr, Math.min(request.depth, MAX_DEPTH));
        if (raster.grid != null) {
            BufferedImage image = render(raster);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            try {
                ImageIO.write(image, "png", os);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return new RenderedRasterResult(raster, Base64.getEncoder().encodeToString(os.toByteArray()));
        } else {
            return new RenderedRasterResult(raster);
        }
    }

    private BufferedImage render(TileGrid result) {
        int numVertTiles = result.grid.length;
        int numHorizTiles = result.grid[0].length;

        BufferedImage image = new BufferedImage(
                numHorizTiles * TILE_SIZE,
                numVertTiles * TILE_SIZE,
                BufferedImage.TYPE_INT_RGB
        );
        Graphics graphic = image.getGraphics();
        int x = 0;
        int y = 0;

        for (int r = 0; r < numVertTiles; r += 1) {
            for (int c = 0; c < numHorizTiles; c += 1) {
                graphic.drawImage(getTile(result.grid[r][c]), x, y, null);
                x += TILE_SIZE;
                if (x >= image.getWidth()) {
                    x = 0;
                    y += TILE_SIZE;
                }
            }
        }
        return image;
    }

    private BufferedImage getTile(Tile name) {
        String path = IMG_ROOT + name;
        BufferedImage tile = null;
        try {
            tile = ImageIO.read(Objects.requireNonNull(getClass().getResource(path)));
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
        return tile;
    }
}
