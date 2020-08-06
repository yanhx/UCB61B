import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class HuffmanEncoder {
    public static Map<Character, Integer> buildFrequencyTable(char[] inputSymbols) {
        Map<Character, Integer> map = new HashMap<>();

        for (char c : inputSymbols) {
            if (!map.containsKey(c))
                map.put(c, 1);
            else
                map.put(c, map.get(c)+1);
        }
        return map;
    }
    public static void main(String[] args) {
        String output = args[0] + ".huf";
        char[] inputSymbols = FileUtils.readFile(args[0]);
        Map<Character, Integer> map = buildFrequencyTable(inputSymbols);
        BinaryTrie bt = new BinaryTrie(map);
        ObjectWriter ow = new ObjectWriter(output);
        ow.writeObject(bt);
        ow.writeObject(inputSymbols.length);
        Map<Character, BitSequence> lookup = bt.buildLookupTable();
        List<BitSequence> encoded = new LinkedList<>();
        for (char c : inputSymbols) {
            encoded.add(lookup.get(c));
        }
        BitSequence assembled = BitSequence.assemble(encoded);
        ow.writeObject(assembled);
    }
}
