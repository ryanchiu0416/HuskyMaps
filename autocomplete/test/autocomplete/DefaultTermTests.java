package autocomplete;

import edu.washington.cse373.BaseTest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * These unit tests are fairly comprehensive, but feel free to add your own.
 */
public class DefaultTermTests extends BaseTest {

    protected Term createTerm(String query, long weight) {
        return new DefaultTerm(query, weight);
    }

    @Test
    void constructWithNullQuery_throwsIllegalArgument() {
        assertThatThrownBy(() -> createTerm(null, 1)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void constructWithNegativeWeight_throwsIllegalArgument() {
        assertThatThrownBy(() -> createTerm("foo", -1)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void constructWithEmptyQuery_doesNotThrow() {
        assertThatCode(() -> createTerm("", 1)).doesNotThrowAnyException();
    }

    @Test
    void constructWithZeroWeight_doesNotThrow() {
        assertThatCode(() -> createTerm("foo", 0)).doesNotThrowAnyException();
    }

    @Test
    void query_returnsCorrectValue() {
        Term term = createTerm("something", 1);
        assertThat(term.query()).isEqualTo("something");
    }

    @Test
    void weight_returnsCorrectValue() {
        Term term = createTerm("something", 1);
        assertThat(term.weight()).isEqualTo(1);
    }

    @Nested
    class QueryOrder {
        @Test
        void queryOrderWithNullTerm_throwsNullPointer() {
            Term term = createTerm("something", 1);
            assertThatThrownBy(() -> term.queryOrder(null)).isInstanceOf(NullPointerException.class);
        }

        @Test
        void queryOrderWithSelf_returns0() {
            Term term = createTerm("something", 1);
            assertThat(term.queryOrder(term)).isEqualTo(0);
        }

        @Test
        void queryOrderWithSameQueryAndSameWeight_returns0() {
            Term term1 = createTerm("something", 1);
            Term term2 = createTerm("something", 1);
            assertThat(term1.queryOrder(term2)).isEqualTo(0);
        }

        @Test
        void queryOrderWithLesserQueryAndSameWeight_returnsPositive() {
            Term term1 = createTerm("something", 1);
            Term term2 = createTerm("abc", 1);
            assertThat(term1.queryOrder(term2)).isGreaterThan(0);
        }

        @Test
        void queryOrderWithGreaterQueryAndSameWeight_returnsNegative() {
            Term term1 = createTerm("something", 1);
            Term term2 = createTerm("zzz", 1);
            assertThat(term1.queryOrder(term2)).isLessThan(0);
        }
    }

    @Nested
    class ReverseWeightOrder {
        @Test
        void reverseWeightOrderWithNullTerm_throwsNullPointer() {
            Term term = createTerm("something", 7);
            assertThatThrownBy(() -> term.reverseWeightOrder(null)).isInstanceOf(NullPointerException.class);
        }

        @Test
        void reverseWeightOrderWithSelf_returns0() {
            Term term = createTerm("something", 7);
            assertThat(term.reverseWeightOrder(term)).isEqualTo(0);
        }

        @Test
        void reverseWeightOrderWithSameWeightAndSameQuery_returns0() {
            Term term1 = createTerm("something", 7);
            Term term2 = createTerm("something", 7);
            assertThat(term1.reverseWeightOrder(term2)).isEqualTo(0);
        }

        @Test
        void reverseWeightOrderWithLesserWeightAndSameQuery_returnsNegative() {
            Term term1 = createTerm("something", 5);
            Term term2 = createTerm("something", 2);
            assertThat(term1.reverseWeightOrder(term2)).isLessThan(0);
        }

        @Test
        void reverseWeightOrderWithGreaterWeightAndSameQuery_returnsPositive() {
            Term term1 = createTerm("something", 10);
            Term term2 = createTerm("something", 100);
            assertThat(term1.reverseWeightOrder(term2)).isGreaterThan(0);
        }
    }

    @Nested
    class MatchesPrefix {
        @Test
        void prefixMatchesWithNullPrefix_throwsNullPointer() {
            Term term = createTerm("something", 7);
            assertThatThrownBy(() -> term.matchesPrefix(null)).isInstanceOf(NullPointerException.class);
        }

        @Test
        void prefixMatchesWithEmptyPrefix_returns0() {
            Term term = createTerm("something", 7);
            assertThat(term.matchesPrefix("")).isEqualTo(0);
        }

        @Test
        void prefixMatchesWithExactMatch_returns0() {
            Term term = createTerm("something", 7);
            assertThat(term.matchesPrefix("something")).isEqualTo(0);
        }

        @Test
        void prefixMatchesWithPrefix_returns0() {
            Term term = createTerm("something", 7);
            assertThat(term.matchesPrefix("some")).isEqualTo(0);
        }

        @Test
        void prefixMatchesWithLesserUnrelatedString_returnsPositive() {
            Term term = createTerm("something", 7);
            assertThat(term.matchesPrefix("bb")).isGreaterThan(0);
        }

        @Test
        void prefixMatchesWithLesserStringWithCommonPrefix_returnsPositive() {
            Term term = createTerm("something", 7);
            assertThat(term.matchesPrefix("someone")).isGreaterThan(0);
        }

        @Test
        void prefixMatchesWithLesserSuffix_returnsPositive() {
            Term term = createTerm("something", 7);
            assertThat(term.matchesPrefix("omething")).isGreaterThan(0);
        }

        @Test
        void prefixMatchesWithGreaterUnrelatedString_returnsNegative() {
            Term term = createTerm("something", 7);
            assertThat(term.matchesPrefix("www")).isLessThan(0);
        }

        @Test
        void prefixMatchesWithGreaterStringWithCommonPrefix_returnsNegative() {
            Term term = createTerm("something", 7);
            assertThat(term.matchesPrefix("somewhere")).isLessThan(0);
        }

        @Test
        void prefixMatchesWithGreaterSuffix_returnsNegative() {
            Term term = createTerm("something", 7);
            assertThat(term.matchesPrefix("thing")).isLessThan(0);
        }

        @Test
        void prefixMatchesWhereThisIsPrefixOfInput_returnsNegative() {
            Term term = createTerm("something", 7);
            assertThat(term.matchesPrefix("somethingelse")).isLessThan(0);
        }
    }
}
