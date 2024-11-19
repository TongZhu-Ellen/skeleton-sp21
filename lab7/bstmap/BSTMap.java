package bstmap;

import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {
    private BSTN<K, V> head;
    private int size;

    private static class BSTN<K extends Comparable<K>, V> { // BST Node;
        K key;
        V value;
        BSTN<K, V> left, right;

        BSTN(K key, V value) {
            this.key = key;
            this.value = value;
            this.left = null;
            this.right = null;
        }



        // starting from start node, try to find a node that contains key;
        // returns null if no such key found;
        static <K extends Comparable<K>, V> BSTN<K, V> find(BSTN<K, V> curNode, K key) {
            if (curNode == null) {
                return null;
            }
            K curKey = curNode.key;
            if (key.compareTo(curKey) < 0) {
                return find(curNode.left, key);
            } else if (key.compareTo(curKey) > 0) {
                return find(curNode.right, key);
            } else {
                return curNode;
            }
        }


        // returns an added version of node with key&value added to the appropriate position;
        static <K extends Comparable<K>, V> BSTN<K, V> added(BSTN<K, V> curNode, K key, V value) {
            if (curNode == null) {
                return new BSTN<>(key, value);
            }
            K curKey = curNode.key;
            if (key.compareTo(curKey) < 0) {
                curNode.left = added(curNode.left, key, value);
            } else if (key.compareTo(curKey) > 0) {
                curNode.right = added(curNode.right, key, value);
            }
            return curNode;
        }





    }







    @Override
    public void clear() {
        this.head = null;
        this.size = 0;
    }

    @Override
    public boolean containsKey(K key) {
        BSTN<K, V> candidateGN = BSTN.find(this.head, key);
        if (candidateGN == null) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public V get(K key) {
        BSTN<K, V> candidateGN = BSTN.find(this.head, key);
        if (candidateGN == null) {
            return null;
        } else {
            return candidateGN.value;
        }
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public void put(K key, V value) {
        if (!this.containsKey(key)) {
            this.head = BSTN.added(this.head, key, value);
            this.size += 1;
        }
    }

    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException();
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
        throw new UnsupportedOperationException();
    }

    public void printInOrder() {
        
    }
}
