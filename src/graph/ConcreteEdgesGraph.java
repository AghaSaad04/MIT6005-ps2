package graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
/**
 * An implementation of Graph.
 * 
 * <p>PS2 instructions: you MUST use the provided rep.
 */
public class ConcreteEdgesGraph<L> implements Graph<L> {
    
    private final Set<L> vertices = new HashSet<>();
    private final List<Edge<L>> edges = new ArrayList<>();
    
    // Abstraction function:
    //   represents all edges in a graph as a pair of connected vertices
    //   with a source to target direction that carries weight 
    // Representation invariant:
    //   vertices is a set of objects of type L
    //   edges is a list of distinct weighted Edges made by 
    //      distinct pairs of vertices(no pair of vertices exists more than once).   
    //   An edge must be connected to at least v number of vertices, for example,
    //     2 edges require at least 3 vertices, 5 edges require at least 4 vertices
    //     vertices.size() >= Math.ceil(Math.sqrt(2*edges.size) + 0.5)
    //     (source) https://math.stackexchange.com/a/1954272
    //
    // Safety from rep exposure:
    //   All fields are private and final
    //   vertices and edges are mutable types, so operations use defensive copies and
    //   immutable wrappers to avoid sharing the rep's objects to clients
    
    public ConcreteEdgesGraph(){
    }
    private void checkRep(){
        final int sizeOfEdges = edges.size();
        final int sizeOfVertices = vertices.size();
        int minNumberOfVertices = 
                sizeOfEdges == 0 ? 0 : (int)Math.ceil(Math.sqrt(2 * sizeOfEdges) + 0.5);
        
        assert sizeOfVertices >= minNumberOfVertices;  
    }
    /** Returns true if vertex label is added*/
    @Override public boolean add(L vertex) {
        return vertices.add(vertex);
    }    
    @Override public int set(L source, L target, int weight) {
        assert weight >= 0;
        
        int indexOfEdge = indexOfEdgeInEdges(source, target);
        int previousWeight = 0;
        final Edge<L> previousEdge;
        
        if (weight > 0) {
            Edge<L> newEdge = new Edge<>(source, target, weight);
            if ( indexOfEdge < 0 ) {
                add(source);
                add(target);
                edges.add(newEdge);
            } else {
                previousEdge = edges.set(indexOfEdge, newEdge);
                previousWeight = previousEdge.getWeight();
            }
        } else if ( weight == 0 && indexOfEdge >= 0) {
            previousEdge = edges.remove(indexOfEdge);
            previousWeight = previousEdge.getWeight();
        }
        checkRep();
        return previousWeight;
    }
    //helper code
    /**
     * Checks if an edge exists in this graph
     * 
     * An edge exists in the graph if the source and target
     * specified match with an edge's source and target in this graph
     * The check is case-insensitive, for example
     * "vertex" will match an edge with labels "Vertex" or "vertex".
     *   
     * @param source string to compare an edge's source with
     * @param target string to compare an edge's target with
     * @return the index i for all 0 <= i, < edges.size()
     *             of the edge whose source and target
     *             match source and target, 
     *             -1 if no edge match was found
     */
    private int indexOfEdgeInEdges(L source, L target){        
        for
