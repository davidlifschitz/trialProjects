package code;

import java.util.LinkedList;

public class BitVectorTrie<Value> {

    private static final int R = 2;
    private Node root = new Node();
    private int n;

    private static class Node {
        private Object val;
        private Node[] next = new Node[R];
    }

    /****************************************************
     * Is the key in the symbol table?
     ****************************************************/
    public boolean isRoutable(BitVector key) {
        return get(key) != null;
    }

    /****************************************************
     * get needs the most changes since its result depends not on the entire key but
     * on its longest matching prefix
     ****************************************************/
    public Value get(BitVector key) {
        return get(root, key, 0, null);
    }

    private Value get(Node x, BitVector key, int d, Value bestSoFar) {
        if (x == null)
            return bestSoFar;
        if (d == key.size())
            return (Value) x.val;
        String ipString = key.toString();
        int c = ipString.charAt(d) - 48;
        // if(x.next[c] == null) return bestSoFar;
        if (x.val != null)
            bestSoFar = (Value) x.val;
        return get(x.next[c], key, d + 1, bestSoFar);
        // finish this
    }

    /****************************************************
     * Insert Value value into the prefix Trie. If a different value exists for the
     * same key throw an IllegalArgumentException
     ****************************************************/
    public void put(BitVector key, Value port) {
        root = put(root, key, port, 0);
    }

    private Node put(Node x, BitVector key, Value port, int d) {
        if (port == null)
            throw new IllegalArgumentException();
        if (x == null) x = new Node();
        if (d == key.size()) {
            if (x.val == null) {
                n++;
                x.val = port;
            } else {
                if (x.val != port) {
                    System.err.println("Trying to input another value into a node that already has a value.");
                } else {
                    return x;
                }
            }
            return x;
        }
        int c = new String(key.toString()).charAt(d) - 48;
        x.next[c] = put(x.next[c], key, port, d + 1);
        return x;
    }

    /****************************************************
     * Delete the value for a key. If no value exists for this key throw and
     * IllegalArgumentException
     ****************************************************/
    public void delete(BitVector key) {
        root = delete(root, key, 0);
    }

    private Node delete(Node x, BitVector key, int d) {
        int size = key.size();
        if (x == null)
            return null;
        if (d == key.size()) {
            if (x.val != null)
                n--;
            x.val = null;
        } else {
            int c = new String(key.toString()).charAt(d) - 48;
            x.next[c] = delete(x.next[c], key, d + 1);
        }

        // remove subtrie rooted at x if it is completely empty
        if (x.val != null)
            return x;
        for (int c = 0; c < R; c++)
            if (x.next[c] != null)
                return x;
        return null;
    }

}
