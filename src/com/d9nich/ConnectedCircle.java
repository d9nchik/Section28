package com.d9nich;

import com.d9nich.graph.Edge;
import com.d9nich.graph.Graph;
import com.d9nich.graph.UnweightedGraph;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class ConnectedCircle extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Create a scene and place it in the stage
        Scene scene = new Scene(new CirclePane(), 450, 350);
        primaryStage.setTitle("ConnectedCircles"); // Set the stage title
        primaryStage.setScene(scene); // Place the scene in the stage
        primaryStage.show(); // Display the stage
    }

    /**
     * Pane for displaying circles
     */
    static class CirclePane extends Pane {
        public CirclePane() {
            this.setOnMouseClicked(e -> {
                final Node circle = getCircleContainsPoint(new Point2D(e.getX(), e.getY()));
                if (circle == null)
                    // Add a new circle
                    getChildren().add(new Circle(e.getX(), e.getY(), 20));
                else
                    getChildren().remove(circle);
                colorIfConnected();
            });
        }

        /**
         * Returns true if the point is inside an existing circle
         */
        private Node getCircleContainsPoint(Point2D p) {
            for (Node circle : this.getChildren())
                if (circle.contains(p))
                    return circle;
            return null;
        }

        /**
         * Color all circles if they are connected
         */
        private void colorIfConnected() {
            if (getChildren().size() == 0)
                return; // No circles in the pane
            // Build the edges
            java.util.List<Edge> edges
                    = new java.util.ArrayList<>();
            for (int i = 0; i < getChildren().size(); i++)
                for (int j = i + 1; j < getChildren().size(); j++)
                    if (overlaps((Circle) (getChildren().get(i)),
                            (Circle) (getChildren().get(j)))) {
                        edges.add(new Edge(i, j));
                        edges.add(new Edge(j, i));
                    }
            // Create a graph with circles as vertices
            Graph<Node> graph = new UnweightedGraph<>(getChildren(), edges);
            UnweightedGraph<Node>.SearchTree tree = graph.dfs(0);
            boolean isAllCirclesConnected = getChildren().size() == tree
                    .getNumberOfVerticesFound();
            for (Node circle : getChildren()) {
                if (isAllCirclesConnected) { // All circles are connected
                    ((Circle) circle).setFill(Color.RED);
                } else {
                    ((Circle) circle).setStroke(Color.BLACK);
                    ((Circle) circle).setFill(Color.WHITE);
                }
            }
        }
    }

    public static boolean overlaps(Circle circle1, Circle circle2) {
        return new Point2D(circle1.getCenterX(), circle1.getCenterY()).
                distance(circle2.getCenterX(), circle2.getCenterY())
                <= circle1.getRadius() + circle2.getRadius();
    }
}
