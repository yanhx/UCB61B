package lab9;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 * Implementation of interface Map61B with BST as core data structure.
 *
 * @author Your name here
 */
public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V> {

    private class Node {
        /* (K, V) pair stored in this Node. */
        private K key;
        private V value;

        /* Children of this Node. */
        private Node left;
        private Node right;

        private Node(K k, V v) {
            key = k;
            value = v;
        }
    }

    private Node root;  /* Root node of the tree. */
    private int size; /* The number of key-value pairs in the tree */

    /* Creates an empty BSTMap. */
    public BSTMap() {
        this.clear();
    }

    /* Removes all of the mappings from this map. */
    @Override
    public void clear() {
        root = null;
        size = 0;
    }

    /** Returns the value mapped to by KEY in the subtree rooted in P.
     *  or null if this map contains no mapping for the key.
     */
    private V getHelper(K key, Node p) {
        if (p == null)
            return null;
        int cmp = p.key.compareTo(key);
        if (cmp == 0)
            return p.value;
        if (cmp < 0)
            return getHelper(key, p.left);
        return getHelper(key, p.right);
    }

    /** Returns the value to which the specified key is mapped, or null if this
     *  map contains no mapping for the key.
     */
    @Override
    public V get(K key) {
        if (key == null)
            throw new IllegalArgumentException();
        return getHelper(key, root);
    }

    /** Returns a BSTMap rooted in p with (KEY, VALUE) added as a key-value mapping.
      * Or if p is null, it returns a one node BSTMap containing (KEY, VALUE).
     */
    private Node putHelper(K key, V value, Node p) {
        if (p == null)
            return new Node(key, value);
        int cmp = p.key.compareTo(key);
        if (cmp == 0) {
            p.value = value;
        }
        else if (cmp < 0) {
            p.left = putHelper(key, value, p.left);
        }
        else p.right = putHelper(key, value, p.right);
        return p;
    }

    /** Inserts the key KEY
     *  If it is already present, updates value to be VALUE.
     */
    @Override
    public void put(K key, V value) {
        if (key == null || value == null)
            throw new IllegalArgumentException();
        if (!containsKey(key))
            size++;
        root = putHelper(key, value, root);
    }

    /* Returns the number of key-value mappings in this map. */
    @Override
    public int size() {
        return size;
    }

    //////////////// EVERYTHING BELOW THIS LINE IS OPTIONAL ////////////////

    /* Returns a Set view of the keys contained in this map. */
    @Override
    public Set<K> keySet() {
        Set<K> keySet = new TreeSet<>();
        return keySetHelper(root, keySet);
    }

    private Set<K> keySetHelper(Node p, Set<K> s) {
        if (p == null)
            return s;
        s.add(p.key);
        s = keySetHelper(p.left, s);
        s = keySetHelper(p.right, s);
        return s;
    }

    /** Removes KEY from the tree if present
     *  returns VALUE removed,
     *  null on failed removal.
     */
    @Override
    public V remove(K key) {
        if (key == null)
            throw new IllegalArgumentException();
        V val = get(key);
        if (val == null)
            return null;
        root = removeHelper(key, root);
        size--;
        return val;
    }

    private Node removeHelper(K key, Node p) {
        if (p == null)
            return null;
        int cmp = p.key.compareTo(key);
        if (cmp == 0) {
            if (p.left == null)
                return p.right;
            if (p.right == null)
                return p.left;
            Node newP = min(p.right);
            remove(newP.key);
            p.value = newP.value;
            p.key = newP.key;
            return p;
        }

        if (cmp < 0) {
            return removeHelper(key, p.left);
        }
        return removeHelper(key, p.right);
    }

    private Node min(Node p) {
        if (p == null)
            return null;
        if (p.left != null)
            return min(p.left);
        return p;
    }

    /** Removes the key-value entry for the specified key only if it is
     *  currently mapped to the specified value.  Returns the VALUE removed,
     *  null on failed removal.
     **/
    @Override
    public V remove(K key, V value) {
        if (key == null || value == null)
            throw new IllegalArgumentException();
        V val = get(key);
        if (val == value)
            return remove(key);
        else return null;
    }

    @Override
    public Iterator<K> iterator() {
        return keySet().iterator();
    }
}
