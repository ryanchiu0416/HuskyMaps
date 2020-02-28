package huskymaps.graph;

import java.util.Objects;

/** A latitude/longitude coordinate. */
public class Coordinate {
    private double lat;
    private double lon;

    public Coordinate(double lat, double lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public static Coordinate fromNode(Node node) {
        return new Coordinate(node.lat(), node.lon());
    }

    public double lat() {
        return lat;
    }

    public double lon() {
        return lon;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Coordinate that = (Coordinate) o;
        return Double.compare(that.lat, lat) == 0 &&
            Double.compare(that.lon, lon) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(lat, lon);
    }

    @Override
    public String toString() {
        return "Coordinate{" +
            "lat=" + lat +
            ", lon=" + lon +
            '}';
    }
}
