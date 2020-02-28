package pointsets;

import edu.washington.cse373.BaseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class KDTreePointSetTests extends BaseTest {
    protected PointSet<Point> createPointSet(List<Point> points) {
        // return new NaivePointSet<>(points);
        return new KDTreePointSet<>(points);
    }

    @Test
    void edgeCaseA() {
        PointSet<Point> set = createInterestingSet();
        Point actual = set.nearest(2.9, 1.4);
        System.out.println(actual);
        assertThat(actual).isEqualTo(new Point(4, 2));
    }

    @Test
    void edgeCaseB() {
        PointSet<Point> set = createInterestingSet();
        Point actual = set.nearest(7, 2.9);
        assertThat(actual).isEqualTo(new Point(7, 5));
    }

    @Test
    void edgeCaseC() {
        PointSet<Point> set = createInterestingSet();
        Point actual = set.nearest(8, 8);
        assertThat(actual).isEqualTo(new Point(6, 8));
    }

    @Test
    void edgeCaseD() {
        PointSet<Point> set = createInterestingSet();
        Point actual = set.nearest(3.5, 8.5);
        assertThat(actual).isEqualTo(new Point(2, 7));
    }

    @Test
    void edgeCaseE() {
        PointSet<Point> set = createInterestingSet();
        Point actual = set.nearest(4, 6);
        assertThat(actual).isEqualTo(new Point(2, 7));
    }

    private PointSet<Point> createInterestingSet() {
        List<Point> points = new ArrayList<>();
        points.add(new Point(3, 3));
        points.add(new Point(2, 7));
        points.add(new Point(1, 1));
        points.add(new Point(4, 2));
        points.add(new Point(7, 5));
        points.add(new Point(6, 8));
        points.add(new Point(8, .66666));
        return createPointSet(points);
    }

    @Test
    @DisplayName("Construct and Call Nearest With 40 Points On The Line y=x Returns Correct Point")
    void testPointsOnLine() {
        List<Point> points = new ArrayList<>();
        for (int i = 0; i < 41; i++) {
            points.add(new Point(i, i));
        }
        PointSet<Point> set = createPointSet(points);
        Point target = new Point(41, 41);

        Point actual = set.nearest(target);
        System.out.println(actual);
        assertThat(actual).isEqualTo(new Point(40, 40));

    }

    @Test
    @DisplayName("Check Does Not Prune Valid Branch (A)")
    void pruningA() {
        List<Point> points = new ArrayList<>();
        points.add(new Point(0, 0));
        points.add(new Point(.001, .4));

        PointSet<Point> set = createPointSet(points);
        Point actual = set.nearest(-.3, .4);
        assertThat(actual).isEqualTo(new Point(.001, .4));
    }

    @Test
    @DisplayName("Check Does Not Prune Valid Branch (B)")
    void pruningB() {
        List<Point> points = new ArrayList<>();
        points.add(new Point(0, 0));
        points.add(new Point(.4, .001));

        PointSet<Point> set = createPointSet(points);
        Point actual = set.nearest(.4, -.3);
        assertThat(actual).isEqualTo(new Point(.4, .001));
    }
}
