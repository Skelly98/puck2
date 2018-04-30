package graph;

import org.extendj.ast.ASTNode;
import org.extendj.ast.Program;

import java.util.*;
import java.util.stream.Collectors;

public class Graph {
    private Map<Integer, Node> nodes;
    private Map<Integer, Set<Edge>> fromIndex = new HashMap<>();
    private Map<Integer, Set<Edge>> toIndex = new HashMap<>();
    private Set<Edge> edges;
    private Program program;

    public Graph(Map<Integer, Node> nodes, Set<Edge> edges, Program program) {
        this.nodes = nodes;
        this.edges = edges;
        this.program = program;
        initIndexes();
    }

    private void initIndexes() {
        for (Edge e: edges) {
            Set<Edge> fromList = fromIndex.getOrDefault(e.getSource(), new HashSet<>());
            fromList.add(e);
            fromIndex.put(e.getSource(), fromList);
          

            Set<Edge> toList = toIndex.getOrDefault(e.getTarget(), new HashSet<>());
            toList.add(e);
            toIndex.put(e.getTarget(), toList);
        }
    }

    public Program getProgram() {
        return program;
    }

    public Node getNode(Integer id) {
        return nodes.get(id);
    }

    public List<Node> queryFrom(Integer id, Edge.Type type) {
        return fromIndex.getOrDefault(id, new HashSet<>()).stream()
                .filter((e) -> e.getType() == type)
                .map((e) -> nodes.get(e.getTarget()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public List<Node> queryTo(Integer id, Edge.Type type) {
        return toIndex.getOrDefault(id, new HashSet<>()).stream()
                .filter((e) -> e.getType() == type)
                .map((e) -> nodes.get(e.getTarget()))
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
