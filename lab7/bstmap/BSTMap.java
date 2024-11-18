package bstmap;

import java.util.Iterator;
import java.util.Set;


public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    BSTNode<K, V> head;
    int size;



    private static class BSTNode<K extends Comparable<K>, V> {
        K key;
        V value;
        BSTNode<K, V> left, right;

        BSTNode(K inputKey, V inputValue) {
            this.key = inputKey;
            this.value = inputValue;
            this.left = null;
            this.right = null;
        }


        // try finding node that contains the key,
        // traverse all possible children;
        // return null if there isn't;
        static <K extends Comparable<K>, V> BSTNode<K, V> tryFind(BSTNode<K, V> curNode, K key) {
            if (curNode == null) {
                return null;
            }
            K curKey = curNode.key;
            if (key.compareTo(curKey) < 0) {
                return tryFind(curNode.left, key);
            } else if (key.compareTo(curKey) > 0) {
                return tryFind(curNode.right, key);
            } else {
                return curNode;
            }
        }

        static <K extends Comparable<K>, V> BSTNode<K, V> insert(BSTNode<K, V> oriNode, K key, V value) {
            if (oriNode == null) {
                return new BSTNode<>(key, value);
            }
            K oriKey = oriNode.key;
            if (key.compareTo(oriKey) < 0) {
                oriNode.left = insert(oriNode.left, key, value);
            } else if (key.compareTo(oriKey) > 0) {
                oriNode.right = insert(oriNode.right, key, value);
            }

            return oriNode;
        }


    }





    @Override
    public void clear() {
        this.head = null;
        this.size = 0;
    }
    
    


    @Override
    public boolean containsKey(K key) {
        BSTNode<K, V> candidateGN = BSTNode.tryFind(this.head, key);
        return (candidateGN != null);
    }


    @Override
    public V get(K key) {
        BSTNode<K, V> candidateGN = BSTNode.tryFind(this.head, key);
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
            this.head = BSTNode.insert(this.head, key, value);
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
}
