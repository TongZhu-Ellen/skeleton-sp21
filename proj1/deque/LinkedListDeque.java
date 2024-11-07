package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T> {

    int size;
    Node stn; // sentinel;

    LinkedListDeque() {
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
        Iterator<T> helperIterator = iterator();
        while (helperIterator.hasNext()) {
            System.out.print(helperIterator.next() + " ");
        }
        System.out.println("");

    }

    @Override
    public T removeFirst() {
        if (isEmpty() == true) {
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
        if (isEmpty() == true) {
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
            return current.next.value; // so basically, start off in stn.next which is the first node;
        } else {
            return helpGetRecursive(current.next, index - 1);
        }
    }


    public Iterator<T> iterator() {
        return new lldIterator();

    }

    private class lldIterator implements Iterator<T> {

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
}
