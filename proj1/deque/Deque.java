package deque;

public interface Deque<T> {

    void addFirst(T item);
    void addLast(T item);
    default boolean isEmpty() {
        return size() == 0; // 默认实现：当 size() 为 0 时返回 true
    }
    int size();
    void printDeque();
    T removeFirst();
    T removeLast();
    T get(int index);

    /* public Iterator<T> iterator();
    public boolean equals(Object o);
     */

}
