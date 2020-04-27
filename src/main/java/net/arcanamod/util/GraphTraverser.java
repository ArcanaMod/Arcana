package net.arcanamod.util;

import com.google.common.graph.Graph;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

// Google Guava 23.1+ has the class Traverser, which would do all of this for me.
// Unfortunately, we're stuck on 21.0.
@SuppressWarnings("UnstableApiUsage")
public final class GraphTraverser<N>{
	
	Set<N> done = new HashSet<>();
	Graph<N> graph;
	
	private GraphTraverser(Graph<N> graph){
		this.graph = graph;
	}
	
	public static <N> GraphTraverser<N> of(Graph<N> graph){
		return new GraphTraverser<>(graph);
	}
	
	/**
	 * Runs the given consumer on every node reachable from the given start node
	 * once, including the start node. The order is not guaranteed.
	 *
	 * @param user
	 * 		A consumer to be run over each node.
	 * @param start
	 * 		The start node.
	 */
	// Turns out a recursive internal iterator is much simpler than an external one would be.
	public void traverse(Consumer<N> user, N start){
		for(N node : graph.adjacentNodes(start))
			if(!done.contains(node)){
				done.add(node);
				user.accept(node);
				traverse(user, node);
			}
	}
}