package app;


import graph.CodeGenerator;
import graph.Graph;
import javafx.application.Application;

import org.extendj.ast.CompilationUnit;
import org.xml.sax.SAXException;
import refactoring.RefactoringBase;
import refactoring.RefactoringError;
import refactoring.RefactoringExecutor;
import refactoring.rename.Rename;
import refactoring.rename.RenameBase;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Puck2Main {
    static Pattern saveGraph = Pattern.compile("saveGraph (.+)");
    static Pattern rename = Pattern.compile("rename (.+) (.+)");
    static Pattern execPlan = Pattern.compile("execPlan (.+)");
    static Pattern saveCode = Pattern.compile("saveCode (.+)");

    public static void main(String args[]) throws Exception {
//    	System.out.println("Nombre d'arguments : " + args.length);
        switch (args.length) {
            case 0: launchGui(); break;
            case 1: run(args[0]);break;
            case 2: {
                run(args[0]).outputToFile(args[1]);
                break;
            }
            default: System.out.println("Usage: java -jar puck2.jar projectPath (outputDirectory)?"+
            							"\n projectPath : chemin du dossier du projet"+
            							"\n outDirectory : chemin du dossier de destination");
        }
    }

    private static Puck2Runner run(String projectPath) {
        Puck2Runner runner = initRunner(projectPath);
   
        Scanner input = new Scanner(System.in);

        System.out.println("Please input a command among the following :\n" +
        					"display : afficher le graphe de dépendances sur la sortie standard\n"+
        					"prettyPrint : afficher le code source du projet\n"+
        					"saveGraph <fichier> : sauvegarder le graphe de dépendances au format XML dans le\n" + 
        					"fichier spécifié\n"+
        					"rename <id> <nouveauNom> : renommer l’entité d’identifiant id\n" +
        					"rename <nom> <nouveauNom> : renommer l’entité de nom complet nom\n" +
        					"execPlan <fichier> : charger et exécuter le plan de refactoring décrit dans le fichier\n" + 
        					"spécifié\n" + 
        					"saveCode <dossier> : écrire le code source modifié (après un refactoring) dans le dossier spécifique\n");
        while (input.hasNext()) {
            String command = input.nextLine();
            if (command.equals("display")) {
                runner.displayGraph();
            } else if (command.equals("prettyPrint")) {
                execPrettyPrint(runner);
            } else if (command.startsWith("saveGraph")) {
                execSaveGraph(runner, saveGraph.matcher(command));
            } else if (command.startsWith("rename")) {
                try {
                    execRenameId(runner, rename.matcher(command));
                } catch (RefactoringError e) { System.err.println(e.getMessage()); }
            } else if (command.startsWith("execPlan")) {
                execPlan(runner, execPlan.matcher(command));
            } else if (command.startsWith("saveCode")) {
                execSaveCode(runner, saveCode.matcher(command));
            } else {
                System.err.println("Invalid command");
            }
        }
        return runner;
    }

    private static Puck2Runner initRunner(String projectPath) {
        Puck2Runner runner = new Puck2Runner(projectPath);
        try {
            runner.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return runner;
    }

    private static void execSaveGraph(Puck2Runner runner, Matcher m) {
        if (! m.find()) {
            System.err.println("ERROR: invalid command");
            return;
        }

        String path = m.group(1);
        try {
            runner.outputToFile(path);
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
        } finally {
            System.out.println("DONE");
        }
    }

    private static void execRenameId(Puck2Runner runner, Matcher m) {
        if (! m.find()) {
            System.err.println("ERROR: invalid command");
            return;
        }

        List<RenameBase> rb = getRenameStrategy(m.group(1), m.group(2), runner.getGraph());
        for (RenameBase r: rb) {
            try {
                r.refactor();
            } catch (RefactoringError e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
    }

    private static List<RenameBase> getRenameStrategy(String target, String newName, Graph graph) {
        List<RenameBase> rb;
        try {
            Integer id = Integer.valueOf(target);
            rb = Rename.newRenameStrategy(id, newName, graph);
        } catch (NumberFormatException n) {
            rb = Rename.newRenameStartegy(target, newName, graph);
        }
        return rb;
    }

    private static void execPlan(Puck2Runner runner, Matcher m) {
        if (! m.find()) {
            System.err.println("ERROR: invalid command");
            return;
        }
        RefactoringExecutor executor = null;
        try {
            executor = new RefactoringExecutor(runner.getGraph(), m.group(1));
            executor.execute();
        } catch (IOException | SAXException | ParserConfigurationException e) {
            e.printStackTrace();
        } catch (RefactoringError e) {
            System.out.print("error: ");
            for (RefactoringBase r: executor.getCommands()) {
                try {
                    r.check();
                } catch (Exception ex) {
                    System.out.print(((RenameBase) r).getId() + " ");
                }
            }
            System.out.print("\n");
        } finally {
            System.out.print("DONE: ");
            for (RefactoringBase r: executor.getCommands()) {
                System.out.print(((RenameBase) r).getId() + " ");
            }
            System.out.print("\n");
        }
    }

    private static void execSaveCode(Puck2Runner runner, Matcher m) {
        if (! m.find()) {
            System.err.println("Error: invalid command");
            return;
        }
        CodeGenerator generator = new CodeGenerator(runner.getProgram(), runner.getProjectPath());
        try {
            generator.generateCode(m.group(1));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.out.println("DONE");
        }
    }

    private static void execPrettyPrint(Puck2Runner runner) {
        for (CompilationUnit cu: runner.getProgram().getCompilationUnits()) {
            System.out.println("####" + cu.pathName() + "####");
            System.out.println(cu.prettyPrint() + "\n\n");
        }
    }

    private static void launchGui() {
       Application.launch(ConfigurationUI.class, "");
    }
}
