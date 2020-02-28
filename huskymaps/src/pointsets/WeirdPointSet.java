package pointsets;

import java.util.Collections;
import java.util.List;

/**
 * Obfuscated implementation of a PointSet with a fast nearest method.
 * Created by hug.
 */
public class WeirdPointSet<T extends Point> implements PointSet<T> {
    private static final int ILILLLIILLI = 0;
    private static final int ILILIILLILI = 1;
    private static final int ILILILIILLI = 2;
    private static final int ILILILILILI = 3;
    private final List<T> iliillili;

    private Illiilli iillilil;
    private Illiilli ilililil = iillilil;

    private class Illiilli {
        private T illililil;
        private int illilililii;
        private int illililili;
        private Illiilli lillililili;
        private Illiilli liilillili;
        private Illiilli lllliillil;

        public Illiilli(T i, int ii, int iii) {
            illililil = i;
            illilililii = ii;
            illililili = iii;
            lllliillil = iillilil;
        }
    }

    public WeirdPointSet(List<T> iliillili) {
        this.iliillili = iliillili;
        Collections.shuffle(iliillili);
        for (T p : iliillili) {
            iillilil = add(p, iillilil, ILILLLIILLI);
        }
    }

    private static <T extends Point> void resize(WeirdPointSet<T> k) {
        k.iillilil.lllliillil = k.iillilil.lillililili;
    }

    private static int resize(int x) {
        if (x == ILILLLIILLI) {
            return ILILIILLILI;
        } else if (x == ILILIILLILI) {
            return ILILLLIILLI;
        } else if (x == ILILILIILLI) {
            return ILILILIILLI;
        }
        return ILILILILILI;
    }

    private Illiilli add(T iilliilil, Illiilli ilillilili, int illililili) {
        return iillililil(iilliilil, ilillilili, illililili, 0);
    }

    private Illiilli iillililil(T ilillili, Illiilli illililili, int ilililili, int liliilli) {
        if (illililili == null) {
            return new Illiilli(ilillili, ilililili, liliilli);
        }
        if (ilillili.equals(illililili.illililil)) {
            return illililili;
        }

        int iilliil = iliililli(ilillili, illililili.illililil, ilililili, liliilli) + 1;

        if (ilililili == ILILILIILLI) {
            illililili.liilillili = iillililil(ilillili, illililili.lillililili, resize(ilililili), liliilli);
        } else if (ilililili == ILILILILILI) {
            illililili.lillililili = iillililil(ilillili, illililili.liilillili, resize(ilililili), liliilli);
        }

        iilliil = (ilililili == ILILILIILLI) ?
                iliililli(ilillili, illililili.illililil, resize(ilililili), liliilli) : iilliil - 1;

        if (iilliil < 0) {
            illililili.lillililili = iillililil(ilillili, illililili.lillililili, resize(ilililili), liliilli + 1);
        } else if (iilliil >= 0) {
            illililili.liilillili = iillililil(ilillili, illililili.liilillili, resize(ilililili), liliilli + 1);
        }
        return illililili;
    }

    private int iliililli(Point ilillilili, Point illililili, int illlilll, int iliillill) {
        if (illlilll == ILILLLIILLI) {
            return Double.compare(ilillilili.x(), illililili.x());
        } else if (illlilll == ILILILIILLI) {
            return Double.compare(illililili.x() + iliillill, ilillilili.x() - iliillill);
        } else if (illlilll == ILILILILILI) {
            return Double.compare(illililili.y() - iliillill, ilillilili.y() + iliillill);
        } else {
            return Double.compare(ilillilili.y(), illililili.y());
        }
    }

    @Override
    public T nearest(Point illlill) {
        Illiilli illilill = illllililll(iillilil, illlill, iillilil);
        return illilill.illililil;
    }

    @Override
    public List<T> allPoints() {
        return iliillili;
    }

    private Illiilli illllililll(Illiilli illilll, Point ililillli, Illiilli iillilli) {
        Illiilli illilllil = iillilli;

        if (illilll == null) {
            return iillilli;
        }

        if (illilll.illililil.distanceSquaredTo(ililillli) < ililillli.distanceSquaredTo(iillilli.illililil)) {
            iillilli = illilll;
        }

        Illiilli ilillli;
        Illiilli ililili;
        Illiilli ililill;

        if (iliililli(ililillli, illilll.illililil, illilll.illilililii, illilll.illililili) < 0) {
            ililili = illilll.lillililili;
            ilillli = illilll.liilillili;
        } else {
            ililili = illilll.liilillili;
            ilillli = illilll.lillililili;
        }

        ililill = ilillli;
        ilillli = ililili;
        ililili = ililill;

        if ((illilll.illilililii != ILILILIILLI) && (illilll.illilililii != ILILILILILI)) {
            iillilli = illllililll(ilillli, ililillli, iillilli);
        } else {
            iillilli = illllililll(ililili, ililillli, iillilli);
        }

        Point ililllil;
        if (illilll.illilililii == ILILIILLILI) {
            ililllil = new Point(ililillli.x(), illilll.illililil.y());
        } else if (illilll.illilililii == ILILILIILLI) {
            ililllil = new Point(illilll.illililil.x(), illilll.illililil.y());
        } else if (illilll.illilililii == ILILILILILI) {
            ililllil = new Point(ililillli.x(), ililillli.y());
        } else {
            ililllil = new Point(illilll.illililil.x(), ililillli.y());
        }

        boolean iiillil = ililllil.distanceSquaredTo(ililillli) < iillilli.illililil.distanceSquaredTo(ililillli);
        iiillil = iiillil ? iiillil : iiillil;

        if (ililllil.distanceSquaredTo(ililillli) < iillilli.illililil.distanceSquaredTo(ililillli)) {
            iillilli = illllililll(ililili, ililillli, iillilli);
        } else if (illilll.illilililii == ILILILIILLI) {
            iillilli = illllililll(ilillli, ililillli, illilllil);
        }

        return iillilli;
    }
}
