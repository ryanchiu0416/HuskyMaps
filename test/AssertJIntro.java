import edu.washington.cse373.BaseTest;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class AssertJIntro extends BaseTest {
    @Test
    public void testSimpleExamples() {
        // Some basic examples of assertions using AssertJ.
        // Typically, it's a bad idea to jam this many assertions into a single test method,
        // but we'll do it here for brevity

        // Booleans
        assertThat(true).isTrue();
        assertThat(false).isFalse();

        // Numbers
        assertThat(0).isEqualTo(0);
        assertThat(0).isNotEqualTo(1);

        assertThat(0).isLessThan(1);
        assertThat(0).isLessThanOrEqualTo(0);
        assertThat(0).isGreaterThan(-1);

        // Objects
        assertThat(String.valueOf(0)).isEqualTo("0"); // uses equals
        assertThat(String.valueOf(0)).isNotSameAs("0"); // uses reference equality
        assertThat("essay").isNotNull();

        // Arrays
        assertThat(0).isIn(0, 1, 2, 3);
        assertThat(new int[]{0, 1, 2, 3}).contains(0);
        assertThat(new int[]{0, 1, 2, 3}).hasSize(4);
        assertThat(new int[]{0, 1, 2, 3}).isNotEmpty();
        assertThat(new int[]{}).isEmpty();
        // There are many more asserts and variants for arrays--overwhelmingly many.
        // Feel free to explore on your own.

        // Maps
        assertThat(Map.of()).isEmpty();
        assertThat(Map.of(1, "A", 2, "Z", 3, " ")).containsKeys(1, 3);
        assertThat(Map.of(1, "A", 2, "Z", 3, " ")).containsValues("A", " ");
        // entry factory method from AssertJ
        assertThat(Map.of(1, "A", 2, "Z", 3, " ")).contains(entry(1, "A"));
        // or, more simply:
        assertThat(Map.of(1, "A", 2, "Z", 3, " ")).containsEntry(1, "A");

        assertThat(Map.of(1, "A", 2, "Z", 3, " ")).extractingByKeys(1, 2).noneMatch(String::isBlank);
        assertThat(Map.of(1, "A", 2, "Z", 3, " ")).extractingByKey(1).isEqualTo("A");
    }

    @Test
    public void testBasicFailingDescription() {
        assertThat(true).as("A test that always fails").isFalse();
    }

    @Test
    public void testBasicFailingErrorMessage() {
        assertThat(true).withFailMessage("is not false").isFalse();
    }

    @Test
    public void testGotchas() {
        // CORRECT:

        // Passes, as expected, but IntelliJ warns us about possible ambiguity
        assertThat(0).isIn(new Integer[]{0, 1, 2, 3});
        // More explicit:
        assertThat(0).isIn((Object[]) new Integer[]{0, 1, 2, 3});
        // Or, if possible, just doe this:
        assertThat(0).isIn(0, 1, 2, 3);

        // INCORRECT:

        // This fails, because arrays of primitives cannot be expanded into Objects by the varargs method:
        //      isIn(Object... args)
        // Since ints aren't Objects, it simply treats the entire array as a single Object.
        assertThat(0).isIn(new int[]{0, 1, 2, 3});
        // Doesn't compile:
        // assertThat(0).isIn((Object[]) new int[]{0, 1, 2, 3});
    }
}
