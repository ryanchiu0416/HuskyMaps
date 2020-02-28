package huskymaps.routing;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.washington.cse373.BaseTest;
import huskymaps.graph.Coordinate;
import huskymaps.graph.Node;
import huskymaps.graph.StreetMapGraph;
import huskymaps.handlers.routing.RouteRequest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.util.List;

import static huskymaps.TestConstants.TEST_DATA_ROOT;
import static huskymaps.utils.Constants.OSM_GZ_RESOURCE_NAME;
import static huskymaps.utils.Constants.PLACES_RESOURCE_NAME;
/**
 * Some randomly-generated test cases for Router.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RouterTests extends BaseTest {
    private static final Path RASTERER_FILES_DIR = TEST_DATA_ROOT.resolve("router");
    private static final String REQUEST_FORMAT = "request%d.json";
    private static final String RESULT_FORMAT = "result%d.json";
    private static final int NUM_TESTS = 10;

    private boolean initialized = false;
    private StreetMapGraph graph;
    private final Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    @BeforeAll
    void setUp() {
        if (initialized) {
            return;
        }
        graph = StreetMapGraph.fromResources(OSM_GZ_RESOURCE_NAME, PLACES_RESOURCE_NAME);
        initialized = true;
    }

    @Test
    void testShortestPath() throws IOException {
        for (int i = 0; i < NUM_TESTS; i += 1) {
            System.out.println(String.format("Running test: %d", i));
            RouteRequest request;
            request = loadRequest(i);
            Coordinate start = new Coordinate(request.startLat, request.startLon);
            Coordinate end = new Coordinate(request.endLat, request.endLon);
            List<Node> actual = new DefaultRouter(this.graph).shortestPath(start, end);
            Long[] expected;
            expected = loadResult(i);
            assertThat(actual).extracting(Node::id).containsExactly(expected);
        }
    }

    protected RouteRequest loadRequest(int i) throws IOException {
        RouteRequest request;
        try (Reader reader = new FileReader(RASTERER_FILES_DIR.resolve(String.format(REQUEST_FORMAT, i)).toFile())) {
            request = gson.fromJson(reader, RouteRequest.class);
        }
        return request;
    }

    protected Long[] loadResult(int i) throws IOException {
        Long[] expected;
        try (Reader reader = new FileReader(RASTERER_FILES_DIR.resolve(String.format(RESULT_FORMAT, i)).toFile())) {
            expected = gson.fromJson(reader, Long[].class);
        }
        return expected;
    }
}
