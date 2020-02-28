package pointsets;

import java.util.Collections;
import java.util.List;

/**
 * Fast nearest-neighbor implementation using a k-d tree.
 */
public class KDTreePointSet<T extends Point> implements PointSet<T> {
    private List<T> allPoints;
    private PointNode overallRoot;

    /**
     * Instantiates a new KDTreePointSet with a shuffled version of the given points.
     *
     * Randomizing the point order decreases likeliness of ending up with a spindly tree if the
     * points are sorted somehow.
     *
     * @param points a non-null, non-empty list of points to include.
     *               Assumes that the list will not be used externally afterwards (and thus may
     *               directly store and mutate the array).
     */
    public static <T extends Point> KDTreePointSet<T> createAfterShuffling(List<T> points) {
        Collections.shuffle(points);
        return new KDTreePointSet<T>(points);
    }

    /**
     * Instantiates a new KDTreePointSet with the given points.
     *
     * @param points a non-null, non-empty list of points to include.
     *               Assumes that the list will not be used externally afterwards (and thus may
     *               directly store and mutate the array).
     */
    KDTreePointSet(List<T> points) {
        this.allPoints = points;

        // construct 2-d tree
        for (T item : this.allPoints) {
            overallRoot = insertNode(overallRoot, new PointNode(item), true);
        }
    }

    private PointNode insertNode(PointNode root, PointNode curr, boolean isCompareX) {
        if (root == null) {
            root = curr;
        } else if (isCompareX) {
            if (curr.x() < root.x()) {
                root.left = insertNode(root.left, curr, false);
            } else {
                root.right = insertNode(root.right, curr, false);
            }
        } else { // is comparing Y case
            if (curr.y() < root.y()) {
                root.left = insertNode(root.left, curr, true);
            } else {
                root.right = insertNode(root.right, curr, true);
            }
        }
        return root;
    }


    /**
     * Returns the point in this set closest to the given point in (usually) O(log N) time, where
     * N is the number of points in this set.
     */
    @Override
    public T nearest(Point target) {
        return findNearest(overallRoot, target, true, overallRoot.getItem(), Double.MAX_VALUE);
    }

    private T findNearest(PointNode root, Point target,
                          boolean isCompareX, T minNode, double minDistance) {
        if (root == null) {
            return minNode;
        } else {
            double currDist = root.distanceSquaredTo(target);
            if (currDist < minDistance) {
                minDistance = currDist;
                minNode = root.getItem();
            }

            T point;
            if ((isCompareX && target.x() > root.x()) || (!isCompareX && target.y() > root.y())) {
                point = findNearest(root.right, target, !isCompareX, minNode, minDistance);
                double currMinDist = point.distanceSquaredTo(target);
                if (((isCompareX && Math.pow(target.x() - root.x(), 2) < currMinDist) ||
                    (!isCompareX && Math.pow(target.y() - root.y(), 2) < currMinDist))) {
                    point = findNearest(root.left, target, !isCompareX, point, currMinDist);
                }
            } else {
                point = findNearest(root.left, target, !isCompareX, minNode, minDistance);
                double currMinDist = point.distanceSquaredTo(target);
                if (((isCompareX && Math.pow(target.x() - root.x(), 2) < currMinDist) ||
                    (!isCompareX && Math.pow(target.y() - root.y(), 2) < currMinDist))) {
                    point = findNearest(root.right, target, !isCompareX, point, currMinDist);
                }
            }
            return point;
        }
    }


    @Override
    public List<T> allPoints() {
        return this.allPoints;
    }


    private class PointNode extends Point {
        public PointNode left;
        public PointNode right;
        private T item;

        public PointNode(T item, PointNode left, PointNode right) {
            super(item.x(), item.y());
            this.item = item;
            this.left = left;
            this.right = right;
        }

        public PointNode(T item) {
            this(item, null, null);
        }

        public T getItem() {
            return this.item;
        }
    }
}
