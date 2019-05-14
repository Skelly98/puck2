package graph;

import graph.nodes.ProgramLoader;

import org.extendj.ast.ASTNode;
import org.extendj.ast.Program;
import org.extendj.ast.VarAccess;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TestGraph {
    Program program;
    ProgramLoader loader;

    @Before
    public void beforeTest() {
        loader = new ProgramLoader();
        loader.addFile("testfiles/Test.java");
        program = loader.getProgram();
    }
    
    @After
    public void afterTest() {
    	loader = null;
    	program = null;
    }
    

    @Test
    public void testAddNode() {
        Graph graph = new Graph(program);
        graph.addNode("testPackage", Node.Type.Package, null);
        Node expected = new Node(0, "testPackage", Node.Type.Package, null);
        assertEquals(expected, graph.getNode(0));
        assertEquals(expected, graph.getNode("testPackage"));
    }
    
    @Test
    public void testAddEdge() {
    	  Graph graph = new Graph(program);
    	  graph.addNode("node1", Node.Type.Package, null);
    	  graph.addNode("node2", Node.Type.Package, null);
    	  graph.addEdge("node1", "node2", Edge.Type.IsA);
    	  Edge expected = new Edge(0,1,Edge.Type.IsA);
    	  Iterator <Edge> it = graph.getEdges().iterator();
    	  Edge e = it.next();
    	  assertEquals(expected,e); 
    }
    
    @Test
    public void testAddReference() {
    	  Graph graph = new Graph(program);
    	  graph.addNode("testPackage", Node.Type.Package, null);
    	  VarAccess ref = new VarAccess();
    	  ref.setID("0");
    	  graph.addReference("node1", ref);
    	  
    	  assertTrue(true);
    }
    
    
    @Test
    public void testRenameNodePositif() {
    	  Graph graph = new Graph(program);
    	  graph.addNode("A", Node.Type.Package, null); //id 0
    	  graph.renameNode(0, "B"); 
    	  assertEquals(graph.getNode(0).getFullName(),"B");
    }
    
    @Test
    public void testRenameNodeNegatif() {
    	  Graph graph = new Graph(program);
    	  graph.addNode("A", Node.Type.Package, null); //id 0
    	  graph.renameNode(0, "B"); 
    	  assertEquals(graph.getNode(0).getFullName(),"A");
    }
    
 
    
}
