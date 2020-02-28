package graphpathfinding;

/** Represents a weighted, directed edge, with an optional name. */
public class WeightedEdge<VERTEX> {
    private VERTEX from;
    private VERTEX to;
    private double weight;
    private String name;

    public WeightedEdge(VERTEX from, VERTEX to, double weight) {
        this(from, to, weight, null);
    }

    public WeightedEdge(VERTEX from, VERTEX to, double weight, String name) {
        this.from = from;
        this.to = to;
        this.weight = weight;
        this.name = name;
    }

    public VERTEX from() {
        return from;
    }

    public VERTEX to() {
        return to;
    }

    public double weight() {
        return weight;
    }

    public String name() {
        return name;
    }

    @Override
    public String toString() {
        return "WeightedEdge{" +
            "from=" + from +
            ", to=" + to +
            ", weight=" + weight +
            ", name='" + name + '\'' +
            '}';
    }
}
