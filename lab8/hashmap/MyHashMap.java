package hashmap;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


public class MyHashMap<K, V> implements Map61B<K, V> {



    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    /* Instance Variables */
    private Collection<Node>[] buckets;
    private double lfl; // load factor limit;
    private HashSet<K> keySet;


    /** Constructors */
    public MyHashMap() {
        this(16);
    }

    public MyHashMap(int initialSize) {
        this(initialSize, 0.75);
    }


    public MyHashMap(int initialSize, double maxLoad) {
        this.buckets = this.createTable(initialSize);
        this.keySet = new HashSet<>();
        this.lfl = maxLoad;
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }


    protected Collection<Node> createBucket() {
        return new HashSet<Node>();
    }

    private Collection<Node>[] createTable(int tableSize) {
        Collection<Node>[] table = new Collection[tableSize];
        for (int i = 0; i < tableSize; i++) {
            table[i] = createBucket();
        }
        return table;
    }





    @Override
    public void clear() {
        this.buckets = null;
        this.keySet.clear();
    }

    @Override
    public boolean containsKey(K key) {
        return this.keySet.contains(key);
    }

    @Override
    public V get(K key) {
       if (this.containsKey(key)) {
           return this.getNode(key).value;
       } else {
           return null;
       }
    }

    // help get the Node of given key;
    private Node getNode(K key, int searchedRow) {
        Collection<Node> toBeSearched = this.buckets[searchedRow];
        for (Node n: toBeSearched) {
            if (n.key.equals(key)) {
                return n;
            }
        }
        return null; // will return null if no such Node in given row num;
    }

    private Node getNode(K key) {
        return getNode(key, this.rowNum(key));
    }

    private int rowNum(K key) {
        int numOfRows = this.buckets.length;
        int keyHash = key.hashCode();
        return posMod(keyHash, numOfRows);
    }

    // returns positive number a mod b give b is positive;
    private int posMod(int a, int b) {
        return (b + a % b) % b;
    }

    @Override
    public int size() {
        return this.keySet.size();
    }

    private void helpPut(K key, V value, int rowNum) {
        this.buckets[rowNum].add(new Node(key, value));
        this.keySet.add(key);
    }



    @Override
    public void put(K key, V value) {
        if (this.containsKey(key)) {
            this.getNode(key).value = value;
        } else {
            this.helpPut(key, value, this.rowNum(key));

        }
    }


    /*
    private boolean checkResize() {
        double loadFactor = (double) this.size() / this.buckets.length;
        return (loadFactor < this.lfl);
    }
   */



    @Override
    public Set<K> keySet() {
        return this.keySet;
    }

    @Override
    public V remove(K key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<K> iterator() {
        return this.keySet.iterator();
    }

}
