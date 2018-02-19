package graph.nodes;

import graph.Edge;
import graph.Node;
import graph.XMLExporter;
import graph.readers.ProgramReader;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class TestXMLExporter {
    private static XMLExporter exporter;

    @BeforeClass
    public static void init() {
        ProgramLoader loader = new ProgramLoader();
        loader.addFile("testfiles/ExporterTest.java");

        Map<String, Node> nodes = new HashMap<>();
        List<Edge> edges = new ArrayList<>();
        ProgramReader r = new ProgramReader(loader.getProgram());
        r.readInto(nodes, edges);

        exporter = new XMLExporter();
        exporter.add(nodes, edges);
    }

    @Test
    public void generateXML() {
        String expected = "<?xml version=\"1.0\"?>\n" +
                "<DependencyGraphe>\n" +
                "\t<Nodes>\n" +
                "\t\t<Node type=\"class\" id=\"1\" name=\"TestClass\"/>\n" +
                "\t\t<Node type=\"package\" id=\"0\" name=\"exporter\"/>\n" +
                "\t</Nodes>\n" +
                "\t<Edges>\n" +
                "\t\t<Edge type=\"Contains\" src=\"0\" dest=\"1\"/>\n" +
                "\t</Edges>\n" +
                "</DependencyGraphe>\n";
        assertEquals(expected, exporter.generateXml());
    }
}