package graph.readers;

import java.util.ArrayList;

import org.extendj.ast.Access;
import org.extendj.ast.Block;
import org.extendj.ast.ConstructorDecl;
import org.extendj.ast.ParameterDeclaration;

import graph.Edge;
import graph.Graph;
import graph.Node;

public class ConstructorReader extends BodyDeclReader {
	private ConstructorDecl constructorDecl;
	private Node constructorNode;

	public ConstructorReader(ConstructorDecl constructorDecl, Graph graph) {
		super(constructorDecl, graph);
		this.constructorDecl = constructorDecl;
	}

	@Override
	public Graph read() {
		String f = getHostClassName() + "." + constructorDecl.name();
	     ArrayList <ParameterDeclaration> param_list = new ArrayList<ParameterDeclaration>();
	     String x = "";
	     for (ParameterDeclaration p : constructorDecl.getParameterList()) {
	        param_list.add(p);
	     }
		
	     if(param_list.size()> 0) {
	        	for (ParameterDeclaration p : param_list) {
	        		
	        		String t = ""+ p.type();
	        		String [] arr = t.split(".");
	        		
	        		if (t.compareTo("Unknown") ==0) {
	        			if( param_list.indexOf(p) == param_list.size() -1) {
	        				x+= unknownParameter(p);
		        		}
		        		else {
		        			x+= unknownParameter(p) + ",";
		        		}
	        		}
	        		
	        		else if(arr.length >0) {
		        		if( param_list.indexOf(p) == param_list.size() -1) {
		        			x += arr[arr.length-1];
		        		}
		        		else {
		        			 x += arr[arr.length-1] +", ";
		        		}
	        		}
	        		else {
	        			if( param_list.indexOf(p) == param_list.size() -1) {
		        			x += p.type();
		        		}
		        		else {
		        			 x += p.type() +", ";
		        		}
	        		}
	        	}
	      }
	        
	        
	        f += "("+x+")";
	        //f= f.replace("<", "(");
	        //f= f.replace(">", ")");
	     
	     
		constructorNode = addNode(f, Node.Type.Method, constructorDecl);
		addHostClassDependency();
		if(constructorDecl.hasParameter()) addParametersTypeDependency();
		if (constructorDecl.getBlock() != null) {
			Block b = constructorDecl.getBlock();
			MethodBodyReader mbreader = new MethodBodyReader(b, constructorNode, constructorDecl, graph);
			mbreader.read();
		}

		if( constructorDecl.hasExceptions()) {
			addExceptionsTypesDependencies();
		}

		return getGraph();
	}
	
	
	private String unknownParameter(ParameterDeclaration p) {
		String s =  p.prettyPrint();
		String [] arr = s.split(" ");		
		return arr[0];		
	}

	private void addHostClassDependency() {
		addEdge(getHostTypeName(), constructorNode.getFullName(), Edge.Type.Contains);
	}
	@Override
	String getFullName() {
		// TODO Auto-generated method stub
		return constructorNode != null ? constructorNode.getFullName() : "";
	}
	private String getHostClassName() {
		return constructorDecl.hostType().fullName();
	}
	private void addParametersTypeDependency() {
		for (ParameterDeclaration p: constructorDecl.getParameterList()) {
			ParametersReader r = new ParametersReader(p, constructorNode, getGraph());
			r.read();
		}
	}
	private void addExceptionsTypesDependencies() {
		for (Access a: constructorDecl.getExceptionList()) {
			addTypeDependency(a.type(), Edge.Type.Uses);
		}
	}

}
