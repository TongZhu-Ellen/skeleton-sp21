package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {

    private int size;
    private Node stn; // sentinel;

    public LinkedListDeque() {
        size = 0;
        stn = new Node(); // stn is supposed to be an empty node;
        stn.prev = stn;
        stn.next = stn;
    }

    private class Node {
        Node prev; // stn.prev is the last node;
        T value;
        Node next; // stn.next is defined to be the first node;

        Node() {
            // empty node constructor;
        }

        Node(T entry) {
            value = entry; // valued node constructor;
        }
    }



    @Override
    public void addFirst(T item) {
        Node newFirst = new Node(item);
        Node oldFirst = stn.next;

        stn.next = newFirst;
        newFirst.next = oldFirst;
        newFirst.prev = stn;
        oldFirst.prev = newFirst;

        size += 1;

    }

    @Override
    public void addLast(T item) {
        Node newLast = new Node(item);
        Node oldLast = stn.prev;

        stn.prev = newLast;
        newLast.prev = oldLast;
        newLast.next = stn;
        oldLast.next = newLast;

        size += 1;

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
        if (isEmpty()) {
            return null;
        } else {
            Node newFirst = stn.next.next;
            Node oldFirst = stn.next;
            stn.next = newFirst;
            newFirst.prev = stn;
            size -= 1;
            return oldFirst.value;
        }

    }

    @Override
    public T removeLast() {
        if (isEmpty()) {
            return null;
        } else {
            Node newLast = stn.prev.prev;
            Node oldLast = stn.prev;
            stn.prev = newLast;
            newLast.next = stn;
            size -= 1;
            return oldLast.value;
        }
    }

    @Override
    public T get(int index) {
        if (index >= size) {
            return null;
        } else {
            Node current = stn;
            for (int i = 0; i <= index; i++) {
                current = current.next;

            }
            return current.value;
        }
    }

    public T getRecursive(int index) {
        return helpGetRecursive(stn, index);

    }


    private T helpGetRecursive(Node current, int index) {
        if (index == 0) {
            return current.next.value; // start off in stn.next which is the first node;
        } else {
            return helpGetRecursive(current.next, index - 1);
        }
    }


    public Iterator<T> iterator() {
        return new LldIterator();

    }

    private class LldIterator implements Iterator<T> {

        Node current = stn;


        @Override
        public boolean hasNext() {
            return (current.next != stn);
        }

        @Override
        public T next() {
            T toBeReturned = current.next.value;
            current = current.next;
            return toBeReturned;
        }
    }




    @Override
    public boolean equals(Object o) {
        if ((o instanceof Deque) == false) {
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
        Deque<Integer> dk = new LinkedListDeque<>();
        dk.addFirst(1);
        dk.addFirst(0);
        dk.addFirst(-1);
        dk.printDeque();;
    }
}
