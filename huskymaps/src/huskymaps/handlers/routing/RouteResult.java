package huskymaps.handlers.routing;

import huskymaps.graph.Coordinate;

import java.util.Arrays;
import java.util.Objects;

/** The computed routing result in response to a browser request. */
public class RouteResult {

    /** A sequence of latitude/longitude coordinates along the route. */
    public final Coordinate[] coordinates;
    /** The HTML-friendly String representation of the navigation directions. */
    public final String directions;

    /**
     * Constructs a RouteResult instance and sets the success and distance fields.
     * @param coordinates The success field.
     * @param directions The directions field.
     */
    public RouteResult(Coordinate[] coordinates, String directions) {
        this.coordinates = coordinates;
        this.directions = directions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RouteResult that = (RouteResult) o;
        return Arrays.equals(coordinates, that.coordinates) &&
            Objects.equals(directions, that.directions);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(directions);
        result = 31 * result + Arrays.hashCode(coordinates);
        return result;
    }

    @Override
    public String toString() {
        return "RouteResult{" +
            "routeCoords=" + Arrays.toString(coordinates) +
            ", directions='" + directions + '\'' +
            '}';
    }
}
