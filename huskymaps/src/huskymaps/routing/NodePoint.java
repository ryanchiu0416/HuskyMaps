package huskymaps.routing;

import huskymaps.graph.Node;
import pointsets.Point;

/**
 * A {@link Point} with an additional reference to the {@link Node} it represents.
 */
class NodePoint extends Point {
    private final Node node;

    public NodePoint(double x, double y, Node node) {
        super(x, y);
        this.node = node;
    }

    public Node node() {
        return node;
    }
}
