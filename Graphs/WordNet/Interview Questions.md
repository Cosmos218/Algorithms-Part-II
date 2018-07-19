# Interview Questions
## Nonrecursive depth-first search. Implement depth-first search in an undirected graph without using recursion.    
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
true non-recursive version:   
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