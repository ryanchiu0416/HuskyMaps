import intlist.LinkedIntList;
import org.junit.jupiter.api.Test;
import edu.washington.cse373.BaseTest;

public class LinkedIntListTest extends BaseTest {

    /** square: test 3-integer list functionality. */
    @Test
    public void testSquare() {
        LinkedIntList L = new LinkedIntList(1, 2, 3);
        LinkedIntList.square(L);
        assertThat(L).isEqualTo(new LinkedIntList(1, 4, 9));
    }

    /** iterativeSquared: test 3-integer list functionality. */
    @Test
    public void testIterativeSquared() {
        LinkedIntList L = new LinkedIntList(1, 2, 3);
        LinkedIntList output = LinkedIntList.iterativeSquared(L);
        assertThat(output).isEqualTo(new LinkedIntList(1, 4, 9));
        assertThat(L).isEqualTo(new LinkedIntList(1, 2, 3));
    }

    /** recursiveSquared: test 3-integer list functionality. */
    @Test
    public void testRecursiveSquared() {
        LinkedIntList L = new LinkedIntList(1, 2, 3);
        LinkedIntList output = LinkedIntList.recursiveSquared(L);
        assertThat(output).isEqualTo(new LinkedIntList(1, 4, 9));
        assertThat(L).isEqualTo(new LinkedIntList(1, 2, 3));
    }

    /** firstToLast: test empty list functionality. */
    @Test
    public void testEmptyFirstToLast() {
        LinkedIntList L = new LinkedIntList();
        LinkedIntList.firstToLast(L);
        assertThat(L).isEqualTo(new LinkedIntList());
    }

    /** firstToLast: test 1-integer list functionality. */
    @Test
    public void test1IntegerFirstToLast() {
        LinkedIntList L = new LinkedIntList(42);
        LinkedIntList.firstToLast(L);
        assertThat(L).isEqualTo(new LinkedIntList(42));
    }

    /** firstToLast: test 2-integer list functionality. */
    @Test
    public void test2IntegerFirstToLast() {
        LinkedIntList L = new LinkedIntList(42, 99);
        LinkedIntList.firstToLast(L);
        assertThat(L).isEqualTo(new LinkedIntList(99, 42));
    }

    /** firstToLast: test general functionality. */
    @Test
    public void testFirstToLast() {
        LinkedIntList L = new LinkedIntList(18, 4, 27, 9, 54, 5, 63);
        LinkedIntList.firstToLast(L);
        assertThat(L).isEqualTo(new LinkedIntList(4, 27, 9, 54, 5, 63, 18));
    }

    /** firstToLast: test duplicates in list. */
    @Test
    public void testDuplicatesFirstToLast() {
        LinkedIntList L = new LinkedIntList(3, 7, 3, 3);
        LinkedIntList.firstToLast(L);
        assertThat(L).isEqualTo(new LinkedIntList(7, 3, 3, 3));
    }

    /** extend: test general functionality. */
    @Test
    public void testExtend() {
        LinkedIntList A = new LinkedIntList(1, 2, 3);
        LinkedIntList B = new LinkedIntList(4, 5, 6);

        LinkedIntList exp = new LinkedIntList(1, 2, 3, 4, 5, 6);
        LinkedIntList.extend(A, B);
        assertThat(A).isEqualTo(exp);

        assertThat(B).isEqualTo(new LinkedIntList(4, 5, 6));
    }

    /** extend: test empty list functionality. */
    @Test
    public void testEmptyExtend() {
        LinkedIntList A = new LinkedIntList(1, 2, 3);
        LinkedIntList B = new LinkedIntList();
        LinkedIntList exp = new LinkedIntList(1, 2, 3);

        LinkedIntList.extend(A, B);
        assertThat(A).isEqualTo(exp);
        assertThat(B).isEqualTo(new LinkedIntList());

        A = new LinkedIntList();
        B = new LinkedIntList(1, 2, 3);
        LinkedIntList.extend(A, B);
        assertThat(A).isEqualTo(exp);
        assertThat(B).isEqualTo(exp);
    }

    /** concatenated: test general functionality. */
    @Test
    public void testConcatenated() {
        LinkedIntList A = new LinkedIntList(1, 2, 3);
        LinkedIntList B = new LinkedIntList(4, 5, 6);

        LinkedIntList exp = new LinkedIntList(1, 2, 3, 4, 5, 6);
        LinkedIntList res = LinkedIntList.concatenated(A, B);
        assertThat(res).isEqualTo(exp);

        assertThat(A).isEqualTo(new LinkedIntList(1, 2, 3));
    }

    /** concatenated: test empty list functionality. */
    @Test
    public void testEmptyConcatenated() {
        assertThat(LinkedIntList.concatenated(new LinkedIntList(), new LinkedIntList())).isEqualTo(new LinkedIntList());

        LinkedIntList A = new LinkedIntList(1, 2, 3);
        LinkedIntList exp = new LinkedIntList(1, 2, 3);
        assertThat(exp).isEqualTo(LinkedIntList.concatenated(new LinkedIntList(), A));
        assertThat(exp).isEqualTo(LinkedIntList.concatenated(A, new LinkedIntList()));

        assertThat(A).isEqualTo(exp);
    }
}
