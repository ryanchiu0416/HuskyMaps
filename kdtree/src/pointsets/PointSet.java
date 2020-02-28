package pointsets;

import java.util.List;

public interface PointSet<T extends Point> {
    /** Returns the point in this set closest to (x, y). */
    default T nearest(double x, double y) {
        return nearest(new Point(x, y));
    }

    /** Returns the point in this set closest to the given point. */
    T nearest(Point target);

    /** Returns a list of all points in this set, not necessarily in any particular order. */
    List<T> allPoints();
}
