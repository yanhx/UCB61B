public class HuffmanDecoder {
    public static void main(String[] args) {
        String input = args[0];
        String output = args[1];
        ObjectReader or = new ObjectReader(input);
        BinaryTrie bt = (BinaryTrie) or.readObject();
        int n = (Integer) or.readObject();
        BitSequence encoded = (BitSequence) or.readObject();
        char[] decoded = new char[n];
        int i = 0;
        while (encoded.length() > 0) {
            Match m = bt.longestPrefixMatch(encoded);
            decoded[i++] = m.getSymbol();
            encoded = encoded.allButFirstNBits(m.getSequence().length());
        }
        FileUtils.writeCharArray(output, decoded);

    }
}
