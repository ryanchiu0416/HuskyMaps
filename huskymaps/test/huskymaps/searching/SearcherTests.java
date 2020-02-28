package huskymaps.searching;

import edu.washington.cse373.BaseTest;
import huskymaps.graph.Node;
import huskymaps.graph.StreetMapGraph;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Objects;

public class SearcherTests extends BaseTest {
    protected Searcher createSearcher(List<Node> nodes) {
        DummyGraph graph = new DummyGraph(nodes);
        return new DefaultSearcher(graph);
    }

    @Test
    void getLocations_withSingleMatch_returnsMatch() {
        List<Node> nodes = List.of(
            new SimpleNode("c", 2),
            new SimpleNode("ca", 0),
            new SimpleNode("cat", 1)
        );

        Searcher searcher = createSearcher(nodes);
        List<Node> matches = searcher.getLocations("ca");
        assertThat(matches).containsExactlyInAnyOrder(new SimpleNode("ca", 0));
    }

    @Test
    void getLocations_withMultipleMatches_returnMatches() {
        List<Node> nodes = List.of(
            new SimpleNode("c", 2),
            new SimpleNode("c", 2),
            new SimpleNode("ca", 0),
            new SimpleNode("ca", 0),
            new SimpleNode("ca", 0),
            new SimpleNode("cat", 1),
            new SimpleNode("cat", 1)
        );

        Searcher searcher = createSearcher(nodes);
        List<Node> matches = searcher.getLocations("ca");
        assertThat(matches).containsExactlyInAnyOrder(
            new SimpleNode("ca", 0),
            new SimpleNode("ca", 0),
            new SimpleNode("ca", 0));
    }

    @Test
    void getLocationsByPrefix_withMultipleDuplicateMatches_returnMatchesInOrder() {
        List<Node> nodes = List.of(
            new SimpleNode("c", 2),
            new SimpleNode("c", 2),
            new SimpleNode("ca", 0),
            new SimpleNode("ca", 0),
            new SimpleNode("ca", 0),
            new SimpleNode("cat", 1),
            new SimpleNode("cat", 1)
        );

        Searcher searcher = createSearcher(nodes);
        List<String> matches = searcher.getLocationsByPrefix("ca");
        assertThat(matches).containsExactly("cat", "ca");
    }

    @Test
    void getLocationsByPrefix_ignoresNullNames() {
        List<Node> nodes = List.of(
            new SimpleNode(null, 3),
            new SimpleNode("c", 2),
            new SimpleNode("ca", 0),
            new SimpleNode("ca", 0),
            new SimpleNode("ca", 0),
            new SimpleNode("cat", 1),
            new SimpleNode("cat", 1)
        );

        Searcher searcher = createSearcher(nodes);
        List<String> matches = searcher.getLocationsByPrefix("");
        assertThat(matches).containsExactly("c", "cat", "ca");
    }

    protected static class DummyGraph extends StreetMapGraph {
        public DummyGraph(List<Node> nodes) {
            this.nodes = nodes;
        }
    }

    protected static class SimpleNode extends Node {
        public SimpleNode(String name, int importance) {
            super(0, 0, 0, name, importance);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            SimpleNode that = (SimpleNode) o;
            return Objects.equals(this.name(), that.name()) &&
                this.importance() == that.importance();
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.name(), this.importance());
        }
    }
}
