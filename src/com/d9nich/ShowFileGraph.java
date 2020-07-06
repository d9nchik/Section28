package com.d9nich;

import com.d9nich.graph.UnweightedGraph;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ShowFileGraph extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private static UnweightedGraph<GraphNode> getFromFile(String path) {
        UnweightedGraph<GraphNode> unweightedGraph = new UnweightedGraph<>();
        try (Scanner input = new Scanner(new File(path))) {
            final int vertexNumber = Integer.parseInt(input.nextLine());
            for (int i = 0; i < vertexNumber; i++)
                unweightedGraph.addVertex(new GraphNode(i + ""));

            while (input.hasNext()) {
                final String line = input.nextLine();
                String[] numbers = line.split("[ ]+");
                int vertex = Integer.parseInt(numbers[0]);
                GraphNode currentNode = unweightedGraph.getVertex(vertex);
                currentNode.x = Integer.parseInt(numbers[1]);
                currentNode.y = Integer.parseInt(numbers[2]);
                for (int i = 3; i < numbers.length; i++)
                    unweightedGraph.addEdge(vertex, Integer.parseInt(numbers[i]));

            }

        } catch (FileNotFoundException ex) {
            System.out.println("File not found!");
        }
        return unweightedGraph;
    }

    @Override
    public void start(Stage primaryStage) {
        BorderPane pane = new BorderPane();
        Button openFileButton = new Button("Open File");
        TextField path = new TextField("Enter graph file path here");
        path.setPrefColumnCount(50);
        pane.setTop(new HBox(path, openFileButton));

        openFileButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open graph file");
            File file = fileChooser.showOpenDialog(primaryStage);
            if (file != null) {
                String text = file.getAbsolutePath();
                pane.setCenter(new GraphView(getFromFile(text)));
                path.setText(text);
            }
        });
        path.setOnAction(e -> pane.setCenter(new GraphView(getFromFile(path.getText()))));

        // Create a scene and place it in the stage
        Scene scene = new Scene(pane, 750, 450);
        primaryStage.setTitle("Display Graph"); // Set the stage title
        primaryStage.setScene(scene); // PlGraph<GraphNode> graph = getFromFile();ace the scene in the stage
        primaryStage.show(); // Display the stage

    }

    static class GraphNode implements Displayable {
        private final String name;
        private double x;
        private double y;

        GraphNode(String name, double x, double y) {
            this.name = name;
            this.x = x;
            this.y = y;
        }

        GraphNode(String name) {
            this(name, 0, 0);
        }

        @Override
        public double getX() {
            return x;
        }

        @Override
        public double getY() {
            return y;
        }

        @Override
        public String getName() {
            return name;
        }
    }
}
