package graph.readers;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import graph.Graph;
import org.extendj.ast.*;

import graph.Edge;
import graph.Node;
import graph.UniqueIdGenerator;

public class MethodReader extends BodyDeclReader {
    private MethodDecl methodDecl;
    private Node methodNode;

    public MethodReader(MethodDecl methodDecl, Graph graph) {
        super(methodDecl, graph);
        this.methodDecl = methodDecl;
    }

    @Override
    protected String getFullName() {
        return methodNode != null ? methodNode.getFullName() : "";
    }

    @Override
    public Graph read() {
        String f = getHostClassName() + "." + methodDecl.name();
        String fullName = f.replace("java.lang.", "");      
        ArrayList <ParameterDeclaration> param_list = new ArrayList<ParameterDeclaration>();
        String x = "";
        for (ParameterDeclaration p : methodDecl.getParameterList()) {
        	param_list.add(p);
        }
        
        if(param_list.size()> 0) {
        	for (ParameterDeclaration p : param_list) {
        		if( param_list.indexOf(p) == param_list.size() -1) {
        			x += p.toString();
        		}
        		else {
        			 x += p.toString()+", ";
        		}
        	}
        }
        
        
        fullName += "("+x+")";
        
        methodNode = addNode(fullName, Node.Type.Method, methodDecl);

        addHostClassDependency();
        addReturnTypeDependency();
        addParametersTypeDependency();
        addExceptionsTypesDependencies();

        if (methodDecl.hasBlock()) {
            Block b = methodDecl.getBlock();
            MethodBodyReader mbreader = new MethodBodyReader(b, methodNode, methodDecl, graph);
            mbreader.read();
        }

        return getGraph();
    }
    

    private String getHostClassName() {
        return methodDecl.hostType().fullName();
    }

    private void addHostClassDependency() {
        addEdge(getHostTypeName(), methodNode.getFullName(), Edge.Type.Contains);
    }

    private void addReturnTypeDependency() {
        addTypeDependency(methodDecl.type(), Edge.Type.Uses);
    }

    private void addParametersTypeDependency() {
        for (ParameterDeclaration p: methodDecl.getParameterList()) {
            ParametersReader r = new ParametersReader(p, methodNode, getGraph());
            r.read();
        }
    }

    private void addExceptionsTypesDependencies() {
        for (Access a: methodDecl.getExceptionList()) {
            addTypeDependency(a.type(), Edge.Type.Uses);
        }
    }
}
