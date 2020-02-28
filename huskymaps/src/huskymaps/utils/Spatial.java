package huskymaps.utils;

import huskymaps.graph.Coordinate;

import java.util.function.BiFunction;

import static huskymaps.utils.Constants.K0;
import static huskymaps.utils.Constants.R;
import static huskymaps.utils.Constants.ROOT_LAT;
import static huskymaps.utils.Constants.ROOT_LON;

/**
 * Utility methods for interfacing between coordinate systems. Because the
 * Earth is fairly spherical, longitudes and latitudes are curved, so it's not
 * appropriate for the k-d tree to compare coordinates. The k-d tree needs to
 * have points projected down to a 2-dimensional grid first.
 */
public class Spatial {
    /**
     * Returns the great-circle (haversine) distance between geographic coordinates.
     * @param v  The coordinates of the first vertex.
     * @param w  The coordinates of the second vertex.
     * @return The great-circle distance between the two vertices.
     * @source https://www.movable-type.co.uk/scripts/latlong.html
     */
    public static double greatCircleDistance(Coordinate v, Coordinate w) {
        double lonV = v.lon();
        double lonW = w.lon();
        double latV = v.lat();
        double latW = w.lat();
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double dphi = Math.toRadians(latW - latV);
        double dlambda = Math.toRadians(lonW - lonV);

        double a = Math.sin(dphi / 2.0) * Math.sin(dphi / 2.0);
        a += Math.cos(phi1) * Math.cos(phi2) * Math.sin(dlambda / 2.0) * Math.sin(dlambda / 2.0);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    /**
     * Return the Euclidean coordinates for some latitude/longitude point p.
     * Found by computing the Transverse Mercator projection centered on the root.
     * @param p The coordinate to convert.
     * @param pointFactory
     * @return The flattened, Euclidean x-value for p.
     * @source https://en.wikipedia.org/wiki/Transverse_Mercator_projection
     */
    public static <T> T projectToPoint(Coordinate p, BiFunction<Double, Double, T> pointFactory) {
        double lon = p.lon();
        double lat = p.lat();
        double dlon = Math.toRadians(lon - ROOT_LON);
        double phi = Math.toRadians(lat);

        double b = Math.sin(dlon) * Math.cos(phi);
        double x = (K0 / 2) * Math.log((1 + b) / (1 - b));

        double con = Math.atan(Math.tan(phi) / Math.cos(dlon));
        double y = K0 * (con - Math.toRadians(ROOT_LAT));

        return pointFactory.apply(x, y);
    }

    /**
     * Returns the initial bearing (angle) between vertices v and w in degrees.
     * The initial bearing is the angle that, if followed in a straight line
     * along a great-circle arc from the starting point, would take you to the
     * end point.
     * @param v  The coordinates of the first vertex.
     * @param w  The coordinates of the second vertex.
     * @return The initial bearing between the vertices.
     * @source https://www.movable-type.co.uk/scripts/latlong.html
     */
    public static double bearing(Coordinate v, Coordinate w) {
        double phi1 = Math.toRadians(v.lat());
        double phi2 = Math.toRadians(w.lat());
        double lambda1 = Math.toRadians(v.lon());
        double lambda2 = Math.toRadians(w.lon());

        double y = Math.sin(lambda2 - lambda1) * Math.cos(phi2);
        double x = Math.cos(phi1) * Math.sin(phi2);
        x -= Math.sin(phi1) * Math.cos(phi2) * Math.cos(lambda2 - lambda1);
        return Math.toDegrees(Math.atan2(y, x));
    }
}
