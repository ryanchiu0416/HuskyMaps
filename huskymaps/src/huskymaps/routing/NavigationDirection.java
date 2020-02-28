package huskymaps.routing;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class to represent a navigation direction, which consists of 3 attributes:
 * a direction to go, a way, and the distance to travel for.
 */
public class NavigationDirection {

    /** Integer constants representing directions. */
    public static final int START = 0;
    public static final int STRAIGHT = 1;
    public static final int SLIGHT_LEFT = 2;
    public static final int SLIGHT_RIGHT = 3;
    public static final int RIGHT = 4;
    public static final int LEFT = 5;
    public static final int SHARP_LEFT = 6;
    public static final int SHARP_RIGHT = 7;

    /** Number of directions supported. */
    public static final int NUM_DIRECTIONS = 8;

    /** A mapping of integer values to directions.*/
    public static final String[] DIRECTIONS = new String[NUM_DIRECTIONS];

    /** Default name for an unknown way. */
    public static final String UNKNOWN_ROAD = "unknown road";

    /** Static initializer. */
    static {
        DIRECTIONS[START] = "Start";
        DIRECTIONS[STRAIGHT] = "Go straight";
        DIRECTIONS[SLIGHT_LEFT] = "Slight left";
        DIRECTIONS[SLIGHT_RIGHT] = "Slight right";
        DIRECTIONS[LEFT] = "Turn left";
        DIRECTIONS[RIGHT] = "Turn right";
        DIRECTIONS[SHARP_LEFT] = "Sharp left";
        DIRECTIONS[SHARP_RIGHT] = "Sharp right";
    }

    /** The direction a given NavigationDirection represents.*/
    int direction;
    /** The name of the way I represent. */
    String way;
    /** The distance along this way I represent. */
    double distance;

    /**
     * Create a default, anonymous NavigationDirection.
     */
    public NavigationDirection() {
        this.direction = STRAIGHT;
        this.way = UNKNOWN_ROAD;
        this.distance = 0.0;
    }

    public String toString() {
        return String.format("%s on %s and continue for %.3f miles.",
                DIRECTIONS[direction], way, distance);
    }

    /**
     * Takes the string representation of a navigation direction and converts it into
     * a Navigation Direction object.
     * @param dirAsString The string representation of the NavigationDirection.
     * @return A NavigationDirection object representing the input string.
     */
    public static NavigationDirection fromString(String dirAsString) {
        String regex = "([a-zA-Z\\s]+) on ([\\w\\s]*) and continue for ([0-9\\.]+) miles\\.";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(dirAsString);
        NavigationDirection nd = new NavigationDirection();
        if (m.matches()) {
            String direction = m.group(1);
            if (direction.equals("Start")) {
                nd.direction = NavigationDirection.START;
            } else if (direction.equals("Go straight")) {
                nd.direction = NavigationDirection.STRAIGHT;
            } else if (direction.equals("Slight left")) {
                nd.direction = NavigationDirection.SLIGHT_LEFT;
            } else if (direction.equals("Slight right")) {
                nd.direction = NavigationDirection.SLIGHT_RIGHT;
            } else if (direction.equals("Turn right")) {
                nd.direction = NavigationDirection.RIGHT;
            } else if (direction.equals("Turn left")) {
                nd.direction = NavigationDirection.LEFT;
            } else if (direction.equals("Sharp left")) {
                nd.direction = NavigationDirection.SHARP_LEFT;
            } else if (direction.equals("Sharp right")) {
                nd.direction = NavigationDirection.SHARP_RIGHT;
            } else {
                return null;
            }

            nd.way = m.group(2);
            try {
                nd.distance = Double.parseDouble(m.group(3));
            } catch (NumberFormatException e) {
                return null;
            }
            return nd;
        } else {
            // not a valid nd
            return null;
        }
    }

    /** Checks that a value is between the given ranges.*/
    private static boolean numInRange(double value, double from, double to) {
        return value >= from && value <= to;
    }

    /**
     * Calculates what direction we are going based on the two bearings, which
     * are the angles from true north. We compare the angles to see whether
     * we are making a left turn or right turn. Then we can just use the absolute value of the
     * difference to give us the degree of turn (straight, sharp, left, or right).
     * @param prevBearing A double in [0, 360.0]
     * @param currBearing A double in [0, 360.0]
     * @return the Navigation Direction type
     */
    static int getDirection(double prevBearing, double currBearing) {
        double absDiff = Math.abs(currBearing - prevBearing);
        if (numInRange(absDiff, 0.0, 15.0)) {
            return NavigationDirection.STRAIGHT;

        }
        if ((currBearing > prevBearing && absDiff < 180.0)
            || (currBearing < prevBearing && absDiff > 180.0)) {
            // we're going right
            if (numInRange(absDiff, 15.0, 30.0) || absDiff > 330.0) {
                // example of high abs diff is prev = 355 and curr = 2
                return NavigationDirection.SLIGHT_LEFT;
            } else if (numInRange(absDiff, 30.0, 100.0) || absDiff > 260.0) {
                return NavigationDirection.LEFT;
            } else {
                return NavigationDirection.SHARP_LEFT;
            }
        } else {
            // we're going left
            if (numInRange(absDiff, 15.0, 30.0) || absDiff > 330.0) {
                return NavigationDirection.SLIGHT_RIGHT;
            } else if (numInRange(absDiff, 30.0, 100.0) || absDiff > 260.0) {
                return NavigationDirection.RIGHT;
            } else {
                return NavigationDirection.SHARP_RIGHT;
            }
        }
        // if ((currBearing > prevBearing && absDiff < 180.0)
        //         || (currBearing < prevBearing && absDiff > 180.0)) {
        //     // we're going right
        //     if (numInRange(absDiff, 15.0, 30.0) || absDiff > 330.0) {
        //         // example of high abs diff is prev = 355 and curr = 2
        //         return NavigationDirection.SLIGHT_RIGHT;
        //     } else if (numInRange(absDiff, 30.0, 100.0) || absDiff > 260.0) {
        //         return NavigationDirection.RIGHT;
        //     } else {
        //         return NavigationDirection.SHARP_RIGHT;
        //     }
        // } else {
        //     // we're going left
        //     if (numInRange(absDiff, 15.0, 30.0) || absDiff > 330.0) {
        //         return NavigationDirection.SLIGHT_LEFT;
        //     } else if (numInRange(absDiff, 30.0, 100.0) || absDiff > 260.0) {
        //         return NavigationDirection.LEFT;
        //     } else {
        //         return NavigationDirection.SHARP_LEFT;
        //     }
        // }

    }


    @Override
    public boolean equals(Object o) {
        if (o instanceof NavigationDirection) {
            return direction == ((NavigationDirection) o).direction
                && way.equals(((NavigationDirection) o).way)
                && distance == ((NavigationDirection) o).distance;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(direction, way, distance);
    }
}
