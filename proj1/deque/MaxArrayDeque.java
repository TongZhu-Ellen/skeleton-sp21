package deque;

import java.util.Comparator;
import java.util.Iterator;


public class MaxArrayDeque<T> extends ArrayDeque<T> {

    private Comparator<T> dfComparator; // default comparator;

    public MaxArrayDeque(Comparator<T> c) {
        super();
        this.dfComparator = c;

    }

    public T max() {
        return max(dfComparator);
    }


    public T max(Comparator<T> c) {
        if (this.isEmpty()) {
            return null;
        }
        Iterator<T> iter = this.iterator();

        T curMax = this.get(0); // we first set it to zero;

        while (iter.hasNext()) {
            T ourNext = iter.next();
            if (c.compare(curMax, ourNext) < 0) {
                curMax = ourNext;
            }
        }

        return curMax;
    }




}
