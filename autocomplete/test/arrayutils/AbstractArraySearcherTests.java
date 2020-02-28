package arrayutils;

import autocomplete.DefaultTerm;
import autocomplete.Term;
import edu.washington.cse373.BaseTest;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;

import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * We provide some unit tests here, but you'll want to add some more to test BinarRangeSearcher
 * more thoroughly.
 */
public abstract class AbstractArraySearcherTests extends BaseTest {

    protected ArraySearcher<String, String> createStringArraySearcher(String... strings) {
        return createArraySearcher(strings,
                (s1, s2) -> s1.substring(0, Math.min(s2.length(), s1.length())).compareTo(s2));
    }

    protected abstract <T, U> ArraySearcher<T, U> createArraySearcher(T[] array, ArraySearcher.Matcher<T, U> matcher);

    @Test
    void findAllMatchesIn_arrayOfTerms_returnsCorrectMatch() {
        Term[] terms = new DefaultTerm[] {
            new DefaultTerm("ArraySearcher", 0),
            new DefaultTerm("Autocomplete", 0),
            new DefaultTerm("AutocompleteGUI", 0),
            new DefaultTerm("BinaryRangeSearcher", 0),
            new DefaultTerm("DefaultTerm", 0),
            new DefaultTerm("LinearSearcher", 0),
            new DefaultTerm("Term", 0),
        };
        ArraySearcher<Term, String> searcher = createArraySearcher(terms, Term::matchesPrefix);
        assertThat(searcher.findAllMatches("Auto").unsorted()).containsExactlyInAnyOrder(terms[1], terms[2]);
    }

    @Test
    void constructWithNullArray_throwsIllegalArgument() {
        assertThatThrownBy(() -> createStringArraySearcher((String[]) null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void constructWithNullItems_throwsIllegalArgument() {
        assertThatThrownBy(() -> createStringArraySearcher((String) null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void constructWithEmptyArray_doesNotThrow() {
        assertThatCode(() -> createStringArraySearcher()).doesNotThrowAnyException();
    }

    @Test
    void findAllMatchesForNullTargetIn_emptyArray_throwsIllegalArgument() {
        assertThatThrownBy(() -> {
            ArraySearcher<String, String> brs = createStringArraySearcher();
            brs.findAllMatches(null);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void findAllMatchesForNullTargetIn_nonemptyArray_throwsIllegalArgument() {
        assertThatThrownBy(() -> {
            ArraySearcher<String, String> brs = createStringArraySearcher("a");
            brs.findAllMatches(null);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void findAllMatchesIn_arrayOfLength1WithNoMatches_returnsEmptyArrayForAnyCombinationOfLesserAndGreater() {
        assertAll(
                () -> {
                    ArraySearcher<String, String> searcher = createStringArraySearcher("10");
                    assertThat(searcher.findAllMatches("2").unsorted()).containsExactlyInAnyOrder();
                },
                () -> {
                    ArraySearcher<String, String> searcher = createStringArraySearcher("30");
                    assertThat(searcher.findAllMatches("2").unsorted()).containsExactlyInAnyOrder();
                }
        );
    }

    @Test
    void findAllMatchesIn_arrayOfLength1With1Match_returnsCorrectMatchForAnyCombinationOfLesserAndGreaterItems() {
        assertAll(
                () -> {
                    ArraySearcher<String, String> searcher = createStringArraySearcher("20");
                    assertThat(searcher.findAllMatches("2").unsorted()).containsExactlyInAnyOrder("20");
                }
        );
    }

    @Test
    void findAllMatchesSortedByStringLength_returnsStringsInCorrectOrder() {
        ArraySearcher<String, String> brs = createStringArraySearcher(
                "abc", "balloon",
                "cannons", "cat", "cypher", "cyan",
                "dolphin", "dexterity");
        ArraySearcher.AbstractMatchResult<String> result = brs.findAllMatches("c");
        String[] sorted = result.sortedBy(Comparator.comparing(String::length));
        assertThat(sorted).containsExactly("cat", "cyan", "cypher", "cannons");
    }

    @Test
    void findAllMatchesIn_arrayOfLength2WithNoMatches_returnsEmptyArrayForAnyCombinationOfLesserAndGreaterItems() {
        ArraySearcher<String, String> searcher = createStringArraySearcher("10", "15");
        String[] arr = searcher.findAllMatches("2").unsorted();
        System.out.println(Arrays.toString(arr));
    }
}
