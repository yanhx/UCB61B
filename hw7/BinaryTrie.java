import edu.princeton.cs.algs4.MinPQ;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class BinaryTrie implements Serializable {
    private Node root;
    private static class Node implements Comparable<Node>, Serializable {
        private final char ch;
        private final int freq;
        private final Node left, right;

        Node(char ch, int freq, Node left, Node right) {
            this.ch    = ch;
            this.freq  = freq;
            this.left  = left;
            this.right = right;
        }

        // is the node a leaf node?
        private boolean isLeaf() {
            assert ((left == null) && (right == null)) || ((left != null) && (right != null));
            return (left == null) && (right == null);
        }

        // compare, based on frequency
        public int compareTo(Node that) {
            return this.freq - that.freq;
        }
    }
    public BinaryTrie(Map<Character, Integer> frequencyTable) {
        MinPQ<Node> pq = new MinPQ<Node>();
        for (Character c : frequencyTable.keySet()) {
            int freq = frequencyTable.get(c);
            if (freq > 0)
                pq.insert(new Node(c, freq, null, null));
        }
        while (pq.size() > 1) {
            Node left  = pq.delMin();
            Node right = pq.delMin();
            Node parent = new Node('\0', left.freq + right.freq, left, right);
            pq.insert(parent);
        }
        this.root = pq.delMin();

    }
    public Match longestPrefixMatch(BitSequence querySequence) {
        Node x = root;
        int i = 0;
        while (!x.isLeaf() && i < querySequence.length()) {
            boolean bit = (querySequence.bitAt(i) != 0);
            if (bit) x = x.right;
            else     x = x.left;
            i++;
        }
        return new Match(querySequence.firstNBits(i), x.ch);
    }
    public Map<Character, BitSequence> buildLookupTable() {
        Map<Character, BitSequence> map = new HashMap<>();
        buildLookupTable(root, map, new BitSequence());
        return map;
    }

    private void buildLookupTable(Node x, Map<Character, BitSequence> map, BitSequence b) {
        if (x.isLeaf()) {
            map.put(x.ch, b);
        }
        else {
            buildLookupTable(x.left, map, b.appended(0));
            buildLookupTable(x.right, map, b.appended(1));
        }
    }
}
