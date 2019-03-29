package app;

import graph.Edge;
import graph.Graph;
import graph.Node;
import graph.XMLExporter;
import graph.readers.ProgramReader;
import org.extendj.ast.Program;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

public class Puck2Runner {
    String projectPath;
    Graph graph; //
    Program program; //extendJ

    public Puck2Runner(String path) {
        projectPath = path;
        program = new Program();
        graph = new Graph(program);
    }

    public Program getProgram() {
        return program;
    }

    public Graph getGraph() {
        return graph;
    }

    public String getProjectPath() {
        return projectPath;
    }

    
    public void run() throws IOException {
        loadProgram(projectPath);
        ProgramReader reader = new ProgramReader(program);
        graph = reader.read();
    }

    public void outputToFile(String outputFile) throws Exception {
    	XMLExporter exporter = new XMLExporter();
        exporter.add(graph.getNodes(), graph.getEdges());
        exporter.writeTo(outputFile);
    }

    public void displayGraph() {
    	
    	System.out.println("JE DISPLAY UN GRAPH");
        for (Node node: graph.getNodes().values()) {
            System.out.println(node);
        }

        for (Edge edge: graph.getEdges()) {
            if (graph.getNode(edge.getSource()) != null && graph.getNode(edge.getTarget()) != null) {
                System.out.println(edge);
            }
        }
    }
    
    public void XMLValidation()throws Exception{
    	 XMLExporter exporter = new XMLExporter();
    	 exporter.add(graph.getNodes(), graph.getEdges());
    	  File temp = File.createTempFile("file", ".tmp");
          BufferedWriter writer = new BufferedWriter(new FileWriter(temp));
          writer.write(exporter.generateXml());
          writer.close();
          this.XMLValidator(temp.getPath());
    }
    
    public void XMLValidator(String outputfile) throws Exception {
    	 // parse an XML document into a DOM tree
        DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document document = parser.parse(new File(outputfile));

        // create a SchemaFactory capable of understanding WXS schemas
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);

        // load a WXS schema, represented by a Schema instance
        Source schemaFile = new StreamSource(new File("XMLValidator/dg.xsd"));
        Schema schema = factory.newSchema(schemaFile);

        // create a Validator instance, which can be used to validate an instance document
        Validator validator = schema.newValidator();

        // validate the DOM tree
        try {
            validator.validate(new DOMSource(document));
        } catch (SAXException e) {
            // instance document is invalid!
        	System.out.println(e.getMessage());
        	throw new Exception();
        }
    }

    
    //parse et ajoute tous les fichiers à la liste de compilation 
    private void loadProgram(String path) throws IOException {
        File f = new File(path); 
        if (f.isDirectory()) { //si f est un répertoire
            for (File innerFile: f.listFiles()) {
                loadProgram(innerFile.getAbsolutePath()); //récursivement ? on charge chaque fichier du projet
            }
        } else if (getFileExtension(path).equals("java")) { //si c'est un fichier java
            program.addSourceFile(path); //parse le fichier et ajoute to compilation unit à la liste de compilation unit
        }
    }

    private String getFileExtension(String fileName) {
        int index = fileName.lastIndexOf(".");

        if (index == -1 || index == fileName.length() - 1) {
            return "";
        }

        return fileName.substring(index + 1);
    }
}
