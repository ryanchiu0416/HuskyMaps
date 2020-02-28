package huskymaps.routing;

import edu.washington.cse373.BaseTest;
import huskymaps.graph.Coordinate;
import huskymaps.graph.Node;
import huskymaps.graph.StreetMapGraph;
import huskymaps.handlers.routing.RouteRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.File;
import java.util.List;
import java.util.Map;

import static huskymaps.TestConstants.TEST_DATA_ROOT;
import static huskymaps.utils.Constants.PLACES_RESOURCE_NAME;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RouterTinyTests extends BaseTest {
    private static final File OSM_DB_PATH_TINY = TEST_DATA_ROOT.resolve("tiny.osm.gz").toFile();
    private static StreetMapGraph tinyGraph;
    private static boolean initialized = false;

    @BeforeAll
    void setUp() {
        if (initialized) {
            return;
        }
        tinyGraph = StreetMapGraph.fromFileAndResource(OSM_DB_PATH_TINY, PLACES_RESOURCE_NAME);
        initialized = true;
    }

    @Test
    void test22to66() {
        RouteRequest request = RouteRequest.from(Map.of(
                "start_lat", 47.55,
                "start_lon", -122.45,
                "end_lat", 47.75,
                "end_lon", -122.2
        ));
        Coordinate start = new Coordinate(request.startLat, request.startLon);
        Coordinate end = new Coordinate(request.endLat, request.endLon);
        List<Node> actual = new DefaultRouter(tinyGraph).shortestPath(start, end);
        assertThat(actual).extracting(Node::id).containsExactly(22L, 46L, 66L);
    }

    @Test
    void test22to11() {
        RouteRequest request = RouteRequest.from(Map.of(
                "start_lat", 47.55,
                "start_lon", -122.45,
                "end_lat", 47.5,
                "end_lon", -122.5
        ));
        Coordinate start = new Coordinate(request.startLat, request.startLon);
        Coordinate end = new Coordinate(request.endLat, request.endLon);
        List<Node> actual = new DefaultRouter(tinyGraph).shortestPath(start, end);
        assertThat(actual).extracting(Node::id).containsExactly(22L, 11L);
    }

    @Test
    void test41to46() {
        RouteRequest request = RouteRequest.from(Map.of(
                "start_lat", 47.5,
                "start_lon", -122.3,
                "end_lat", 47.75,
                "end_lon", -122.3
        ));
        Coordinate start = new Coordinate(request.startLat, request.startLon);
        Coordinate end = new Coordinate(request.endLat, request.endLon);
        List<Node> actual = new DefaultRouter(tinyGraph).shortestPath(start, end);
        assertThat(actual).extracting(Node::id).containsExactly(41L, 63L, 66L, 46L);
    }

    @Test
    void test66to55() {
        RouteRequest request = RouteRequest.from(Map.of(
                "start_lat", 47.75,
                "start_lon", -122.2,
                "end_lat", 47.7,
                "end_lon", -122.25
        ));
        Coordinate start = new Coordinate(request.startLat, request.startLon);
        Coordinate end = new Coordinate(request.endLat, request.endLon);
        List<Node> actual = new DefaultRouter(tinyGraph).shortestPath(start, end);
        assertThat(actual).extracting(Node::id).containsExactly(66L, 63L, 55L);
    }
}
