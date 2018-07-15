#Tips for WordNet
1.要让SAP中Digraph为immutable，需用copy的constructor复制：
```java
// constructor takes a G (not necessarily a DAG)
public SAP(Digraph G) {
if (G == null)
throw new IllegalArgumentException();
this.G = new Digraph(G);
}
```

2.在多个方法中都需创建的，且复杂度高的，可以放在constructor中以提高效率：
```java
// constructor takes the name of the two input files
public WordNet(String synsets, String hypernyms) {
if (synsets == null || hypernyms == null)
throw new IllegalArgumentException("null synsets or hypernyms");
parseSynsets(synsets);
parseHypernyms(hypernyms);
if (new DirectedCycle(G).hasCycle() || !judgeOneRoot()) //to ensure DAG and one root
throw new IllegalArgumentException("not a rooted DAG");
sap1 = new SAP(G); //create SAP in constructor to reduce time in multiple calls
}
```


3.判断只有一个根节点：出度为0的点只有一个

4.对于nouns方法，直接return synMap的keySet最节省时间。这也是为什么数据结构要用HashMap<String, Arraylist<Integer>>的原因：
```java
// returns all WordNet nouns
public Iterable<String> nouns() {
return synMap.keySet(); //just use the keySet can same time
}
```


5.核心数据结构：
```java
private Digraph G;
private HashMap<String, ArrayList<Integer>> synMap;
private ArrayList<String> keyToString;
```
原因：需要根据noun来得到节点集合subset，因此是由noun对多个节点序号的key-value关系。
keyToString是为了由序号得ancestor的noun名字。