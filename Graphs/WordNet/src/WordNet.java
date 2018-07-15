import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class WordNet {
    private Digraph G;
    private HashMap<String, ArrayList<Integer>> synMap;
    private ArrayList<String> keyToString;
    private int size = 0;
    private SAP sap1;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null)
            throw new IllegalArgumentException("null synsets or hypernyms");
        parseSynsets(synsets);
        parseHypernyms(hypernyms);
        if (new DirectedCycle(G).hasCycle() || !judgeOneRoot())    //to ensure DAG and one root
            throw new IllegalArgumentException("not a rooted DAG");
        sap1 = new SAP(G);  //create SAP in constructor to reduce time in multiple calls
    }

    //core idea is that root's outdegree is 0
    private boolean judgeOneRoot() {
        int cnt = 0;
        for (int i = 0; i < G.V(); i++) {
            if (G.outdegree(i) == 0)
                cnt++;
        }
        if (cnt == 1)
            return true;
        else
            return false;
    }

    private void parseSynsets(String synsets) {
        synMap = new HashMap<>();
        keyToString = new ArrayList<>();
        In in = new In(synsets);
        while (in.hasNextLine()) {
            String[] temp = in.readLine().split(",");
            String[] names = temp[1].split(" ");
            for (String s : names) {
                if (synMap.containsKey(s)) {
                    synMap.get(s).add(size);
                } else {
                    ArrayList<Integer> nums = new ArrayList<>();
                    nums.add(size);
                    synMap.put(s, nums);
                }
            }
            keyToString.add(temp[1]);
            size++;
        }
    }

    private void parseHypernyms(String hypernyms) {
        G = new Digraph(size);
        In in = new In(hypernyms);
        while (in.hasNextLine()) {
            String[] temp = in.readLine().split(",");
            for (int i = 1; i < temp.length; i++) {
                G.addEdge(Integer.parseInt(temp[0]), Integer.parseInt(temp[i]));
            }
        }
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return synMap.keySet();     //just use the keySet can same time
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null)
            throw new IllegalArgumentException();
        Set<String> temp = (Set<String>) nouns();
        return temp.contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException();
        return sap1.length(synMap.get(nounA), synMap.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB))
            throw new IllegalArgumentException();
        return keyToString.get(sap1.ancestor(synMap.get(nounA), synMap.get(nounB)));
    }

    // do unit testing of this class
    public static void main(String[] args) {
//        WordNet wordNet = new WordNet("synsets.txt", "hypernyms.txt");
//        WordNet wordNet = new WordNet("synsets6.txt", "hypernyms6InvalidTwoRoots.txt");
//        System.out.println("finish");
//        StdOut.println(wordNet.synMap.get(17)[0]);
//        StdOut.println(wordNet.synMap.get(17)[1]);
//        StdOut.println(wordNet.G);
//        StdOut.println(wordNet.isNoun("ILOVECODING"));
//        StdOut.println(wordNet.isNoun("ASCII_character"));
//        StdOut.println(wordNet.sap("miracle", "dash"));
//        StdOut.println("ancestor:" + wordNet.sap("jump", "transition") + " length:" + wordNet.distance("jump", "transition"));
//        for (int i : wordNet.synMap.get("worm")) {
//            StdOut.println(i);
//        }
//        StdOut.println("ancestor:" + wordNet.sap("worm", "bird") + " length:" + wordNet.distance("worm", "bird"));
    }
}
