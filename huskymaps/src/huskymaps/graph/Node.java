package huskymaps.graph;

/** Vertex representation for the graph. */
public class Node {
    private final double lat;
    private final double lon;
    private final String name;
    private final long id;
    private final int importance;

    protected Node(long id, double lat, double lon, String name, int importance) {
        this.lat = lat;
        this.lon = lon;
        this.name = name;
        this.id = id;
        this.importance = importance;
    }

    public double lat() {
        return lat;
    }

    public double lon() {
        return lon;
    }

    public String name() {
        return name;
    }

    public long id() {
        return id;
    }

    public int importance() {
        return importance;
    }

    static class Builder {
        long id;
        double lat;
        double lon;
        String name;
        int importance;

        Builder setId(long id) {
            this.id = id;
            return this;
        }

        Builder setLat(double lat) {
            this.lat = lat;
            return this;
        }

        Builder setLon(double lon) {
            this.lon = lon;
            return this;
        }

        Builder setName(String name) {
            this.name = name;
            return this;
        }

        Builder setImportance(int importance) {
            this.importance = importance;
            return this;
        }

        Node createNode() {
            return new Node(id, lat, lon, name, importance);
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other.getClass() != this.getClass()) {
            return false;
        }
        Node otherNode = (Node) other;
        return this.id == otherNode.id;
    }

    @Override
    public int hashCode() {
        return (int) this.id;
    }

    @Override
    public String toString() {
        return "Node{" +
                "id=" + id +
                ", lat=" + lat +
                ", lon=" + lon +
                ", name='" + name + '\'' +
                ", importance=" + importance +
                '}';
    }
}
