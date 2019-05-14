package graph.nodes;

import graph.Edge;
import graph.Graph;
import graph.Node;
import graph.XMLExporter;
import graph.readers.ProgramReader;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class TestXMLExporter {
    XMLExporter exporter;
    ProgramLoader loader;
    Graph graph;
    ProgramReader r;
    
    @Before
    public void beforeTests() {
        loader = new ProgramLoader();
        loader.addFile("testfiles/ExporterTest.java");
        r = new ProgramReader(loader.getProgram());
        graph = r.read();
        exporter = new XMLExporter();
        exporter.add(graph.getNodes(), graph.getEdges());
    }
    
  
    
    @After
    public void afterTests() {
    	exporter = null;
        loader  = null;
        graph  = null;
        r = null;
    }
    

    @Test
    public void testGenerateXML() {
        String expected = "<?xml version=\"1.0\"?>\n" +
                "<DG>\n" +
                "\t<node type=\"package\" id=\"0\" name=\"exporter\"/>\n" +
                "\t<node type=\"class\" id=\"1\" name=\"TestClass\"/>\n" +
                "\t<edge type=\"contains\" src=\"0\" dest=\"1\" id=\"0\" violation=\"0\"/>\n" +
                "</DG>\n";
        assertEquals(expected, exporter.generateXml());
    }
    
    
    
    @Test
    public void testAdd() {
    	Map<Integer, Node> nodes = new HashMap();
    	Set<Edge> edges = new HashSet();
    	Node n1 = new Node(0, "2", Node.Type.Package, null);
    	nodes.put(0, n1);
    	Edge e = new Edge(0, 2, Edge.Type.Contains);
    	edges.add(e);
    	
    	exporter.add(nodes,  edges);
    	
    	assertEquals(exporter.getNodes(),nodes);
    	assertEquals(exporter.getEdges(),edges);    	
    }
    
}
