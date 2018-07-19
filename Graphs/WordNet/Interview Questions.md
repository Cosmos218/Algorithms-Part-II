# Interview Questions

## 1.Nonrecursive depth-first search. Implement depth-first search in an undirected graph without using recursion

recursive version:

```java
private void dfs(Graph G, int v) {
    marked[v] = true;
    for (int w : G.adj(v)) {
        if (!marked[w]) {
            dfs(w);
        }
    }
}
```

false non-recursive version:

```java
private void dfs(Graph G, int v) {
    Stack<Integer> stack = new Stack<Integer>();
    stack.push(v);
    marked[v] = true;
    while (!stack.isEmpty()) {
        int w = stack.pop();
        for (int x : G.adj(w)) {
            if (!marked[x]) {
                stack.push(x);
                marked[x] = true;
            }
        }
    }
}
```

the reason to be false is that when you iterate through adj, it comes like bfs.

true non-recursive version:

```java
private void dfs(Graph G, int v) {
    Iterator<Integer[] adj = (Iterator<Integer>[]) new Iteraor[G.V()];
    Stack<Integer> stack = new Stack<Integer>();
    for (int x : G.V())
        adj[x] = G.adj(x).iterator();
    stack.push(v);
    marked[v] = true;
    while (!stack.isEmpty()) {
        int w = stack.peek();
        if (adj[w].hasNext()) {
            int x = adj[w].next();
            if (!marked[x]) {       //always remember to check if already marked
                marked[x] = true;
                stack.push(x);
            }
        }
        else
            stack.pop(w); //finish depth search all the sub node of w;
    }
}
```

We can see the core idea is that the top one on stack always has to be the current one's next node. If no next node, then pop.

## 2.Diameter and center of a tree. Given a connected graph with no cycles

Diameter: design a linear-time algorithm to find the longest simple path in the graph.
Center: design a linear-time algorithm to find a vertex such that its maximum distance from any other vertex is minimized.

<https://leetcode.com/problems/diameter-of-binary-tree/description/>

***Pf1:Have to use recursion, cause it's bottom-up.***

```java
public class Solution {
    int max = 0;
    
    public int diameterOfBinaryTree(TreeNode root) {
        maxDepth(root);
        return max;
    }
    
    private int maxDepth(TreeNode root) {
        if (root == null) return 0;
        
        int left = maxDepth(root.left);
        int right = maxDepth(root.right);
        
        max = Math.max(max, left + right);
        
        return Math.max(left, right) + 1;
    }
}
```

**Pf2:**<https://stackoverflow.com/questions/5055964/centre-node-in-a-tree>

## 3.Euler cycle. An Euler cycle in a graph is a cycle (not necessarily simple) that uses every edge in the graph exactly one

Show that a connected graph has an Euler cycle if and only if every vertex has even degree.
Design a linear-time algorithm to determine whether a graph has an Euler cycle, and if so, find one.

**Pf1:** Imagine a directed graph. When you draw an Euler cycle, you get to a node and then have to get out that node. So indegree equals to outdegree in every node.

**Pf2:** Remain to be finished.