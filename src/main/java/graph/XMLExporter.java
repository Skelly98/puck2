package graph;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class XMLExporter {
    Map<Integer, Node> nodes;
    Set<Edge> edges;

    public XMLExporter() {
        nodes = new HashMap<>();
        edges  = new HashSet();
    }

    public void writeTo(String path) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(path));
        writer.write(generateXml());
        writer.close();
    }

    public void add(Map<Integer, Node> nodes, Set<Edge> edges) {
         this.nodes.putAll(nodes);
        this.edges.addAll(edges);
    }

    public String generateXml() {
        StringBuilder builder = new StringBuilder("<?xml version=\"1.0\"?>\n");
        builder.append("<DG>\n");
        
        for (Node n: nodes.values()) {
            builder.append(nodeToString(n));
        }

        int currentEdgeIndex = 0;
        for (Edge e: edges) {
            builder.append(edgeToString(e, currentEdgeIndex++));
        }

        builder.append("</DG>\n");

        return builder.toString();
    }

    private String nodeToString(Node node) {
        String formatString = "\t<node type=\"%s\" id=\"%d\" name=\"%s\"/>\n";
        String type = node.getType().toString().toLowerCase();
        Integer id = node.getId();
        String name = extractNodeName(node.getFullName());
        name = name.replace("<", "(");
        name = name.replace(">", ")");
        return String.format(formatString, type, id, name);
    }

    private String extractNodeName(String nodeName) {
        String[] nameParts = nodeName.split("\\.");

        return nameParts[nameParts.length - 1];
    }

    private String edgeToString(Edge edge, int id) {
        String formatString = "\t<edge type=\"%s\" src=\"%d\" dest=\"%d\" id=\"%d\" violation=\"%d\"/>\n";
        String type = edge.getType().toString().toLowerCase();
        Integer src = edge.getSource();
        Node tnode = nodes.get(edge.getTarget());

        if (tnode != null) {
            Integer dest = tnode.getId();
            return String.format(formatString, type, src, dest, id, edge.getViolation());
        }

        return "";
    }
}
