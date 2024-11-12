package deque;

import java.util.Iterator;

public class ArrayDeque<T> implements Deque<T>, Iterable<T> {

    private int start; // candidate position of addFirst;
    private int end; // candidate position of addLast;
    private int size;
    private T[] array;

    public ArrayDeque() {
        size = 0;
        start = 0;
        end = 1;
        array = (T[]) new Object[8];
    }

    private int position(int n) { // output: n mod array.length;
        return (n + array.length) % array.length;

    }
    
    
    // check and reshape if needed;
    private void checkReshapeUp() {
        if ((double) size / array.length > 0.75) {
            reshape(2 * array.length);
        }
    }

    private void checkReshapeDown() {
        if ((double) size / array.length < 0.25) {
            reshape(array.length / 2);
        }

    }

    private void reshape(int newArrayLength) {
        T[] newArray = (T[]) new Object[newArrayLength];
        for (int i = 0; i < size; i++) {
            newArray[i] = get(i);
        }
        array = newArray;
        start = position(-1);
        end = position(size);

    }


    @Override
    public void addFirst(T item) {
        array[start] = item;
        start = position(start - 1);
        size += 1;
        checkReshapeUp();


    }

    @Override
    public void addLast(T item) {
        array[end] = item;
        end = position(end + 1);
        size += 1;
        checkReshapeUp();


    }

    @Override
    public boolean isEmpty() {
        return (size == 0);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public void printDeque() {

        for (T i: this) {
            System.out.print(
                    i + " ");
        }
        System.out.println("");

    }

    @Override
    public T removeFirst() {
        if (this.isEmpty()) {
            return null;
        } else {
            T removed = array[position(start + 1)];
            start = position(start + 1);
            size -= 1;
            checkReshapeDown();
            return removed;
        }
    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        } else {
            T removed = array[position(end - 1)];
            end = position(end - 1);
            size -= 1;
            checkReshapeDown();
            return removed;
        }
    }

    @Override
    public T get(int index) {
        if (index >= size) {
            return null; // index should be <= size - 1;
        } else {
            return array[position(start + 1 + index)];
        }
    }




    // generates an iterator based on current status;
    public Iterator<T> iterator() {
        return new AdIterator();
    }

    private class AdIterator implements Iterator<T> {

        int curIndex = -1; //

        @Override
        public boolean hasNext() {
            // hasNext when nextIndex, which is (curIndex+1), is in [0, size-1];
            // because basically, [0, size-1] would be valid indexing range here;
            // and we need the nextIndex to between that;
            return (curIndex + 1 <= size - 1);
        }

        @Override
        public T next() {
            T nextItem = get(curIndex + 1);
            curIndex = curIndex + 1;
            return nextItem;
        }
    }

    @Override
    public boolean equals(Object o) {
        if ((o instanceof Deque)== false) {
            return false;
        }
        Deque that = (Deque) o;

        if (this.size() != that.size()) {
            return false;
        }




        for (int i = 0; i < this.size(); i++) {
            if (this.get(i).equals(that.get(i)) != true ) {
                return false;
            }

        }

        return true;


    }

    public static void main(String[] args) {
        Deque<Integer> dk = new ArrayDeque<>();
        dk.addFirst(1);
        dk.addFirst(0);
        dk.addFirst(-1);
        dk.printDeque();;
    }
}
