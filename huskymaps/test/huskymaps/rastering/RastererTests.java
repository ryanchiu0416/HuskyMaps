package huskymaps.rastering;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.washington.cse373.BaseTest;
import huskymaps.graph.Coordinate;
import huskymaps.handlers.rastering.RasterRequest;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;

import static huskymaps.TestConstants.TEST_DATA_ROOT;

/**
 * Some randomly-generated test cases for Rasterer.
 */
public class RastererTests extends BaseTest {
    private static final Path RASTERER_FILES_DIR = TEST_DATA_ROOT.resolve("rasterer");
    private static final String REQUEST_FORMAT = "request%d.json";
    private static final String RESULT_FORMAT = "result%d.json";
    private static final int NUM_TESTS = 10;

    private final Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();

    @Test
    void testRasterizeMap() throws IOException {
        for (int i = 0; i < NUM_TESTS; i += 1) {
            System.out.println(String.format("Running test: %d", i));
            RasterRequest request;
            request = loadRequest(i);
            Coordinate ul = new Coordinate(request.ullat, request.ullon);
            Coordinate lr = new Coordinate(request.lrlat, request.lrlon);
            TileGrid actual = new DefaultRasterer().rasterizeMap(ul, lr, request.depth);
            TileGrid expected;
            expected = loadResult(i);
            assertThat(actual).as("Your results did not match the expected results for input " + request + ".\n")
                    .isEqualTo(expected);
        }
    }

    protected RasterRequest loadRequest(int i) throws IOException {
        RasterRequest request;
        File file = RASTERER_FILES_DIR.resolve(String.format(REQUEST_FORMAT, i)).toFile();
        try (Reader reader = new FileReader(file)) {
            request = gson.fromJson(reader, RasterRequest.class);
        }
        return request;
    }

    protected TileGrid loadResult(int i) throws IOException {
        TileGrid expected;
        try (Reader reader = new FileReader(RASTERER_FILES_DIR.resolve(String.format(RESULT_FORMAT, i)).toFile())) {
            expected = gson.fromJson(reader, TileGrid.class);
        }
        return expected;
    }

}
