package com.d9nich;

import com.d9nich.graph.Edge;
import com.d9nich.graph.Graph;
import com.d9nich.graph.UnweightedGraph;
import javafx.application.Application;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class ConnectedRectangle extends Application {

    private final static double SIDE_LENGTH = 30;

    public static void main(String[] args) {
        launch(args);
    }

    public static boolean overlaps(Rectangle rectangle1, Rectangle rectangle2) {
        return Math.abs(rectangle1.getX() - rectangle2.getX()) <= SIDE_LENGTH &&
                Math.abs(rectangle1.getY() - rectangle2.getY()) <= SIDE_LENGTH;
    }

    @Override
    public void start(Stage primaryStage) {
        // Create a scene and place it in the stage
        Scene scene = new Scene(new RectanglePane(), 450, 350);
        primaryStage.setTitle("ConnectedRectangles"); // Set the stage title
        primaryStage.setScene(scene); // Place the scene in the stage
        primaryStage.show(); // Display the stage
    }

    /**
     * Pane for displaying rectangles
     */
    static class RectanglePane extends Pane {
        public RectanglePane() {
            setOnMouseClicked(e -> {
                final Node rectangle = getCircleContainsPoint(new Point2D(e.getX(), e.getY()));
                if (rectangle == null) {
                    // Add a new rectangle
                    final Rectangle rectangle1 = new Rectangle(e.getX() - SIDE_LENGTH / 2,
                            e.getY() - SIDE_LENGTH / 2, SIDE_LENGTH, SIDE_LENGTH);
                    rectangle1.setOnMouseDragged(e1 -> {
                        rectangle1.setX(e1.getX() - SIDE_LENGTH / 2);
                        rectangle1.setY(e1.getY() - SIDE_LENGTH / 2);
                    });
                    getChildren().add(rectangle1);

                } else if (e.getButton() == MouseButton.SECONDARY)
                    getChildren().remove(rectangle);
                colorIfConnected();
            });
        }

        /**
         * Returns true if the point is inside an existing rectangle
         */
        private Node getCircleContainsPoint(Point2D p) {
            for (Node rectangle : this.getChildren())
                if (rectangle.contains(p))
                    return rectangle;
            return null;
        }

        /**
         * Color all rectangles if they are connected
         */
        private void colorIfConnected() {
            if (getChildren().size() == 0)
                return; // No circles in the pane
            // Build the edges
            java.util.List<Edge> edges
                    = new java.util.ArrayList<>();
            for (int i = 0; i < getChildren().size(); i++)
                for (int j = i + 1; j < getChildren().size(); j++)
                    if (overlaps((Rectangle) (getChildren().get(i)),
                            (Rectangle) (getChildren().get(j)))) {
                        edges.add(new Edge(i, j));
                        edges.add(new Edge(j, i));
                    }
            // Create a graph with circles as vertices
            Graph<Node> graph = new UnweightedGraph<>(getChildren(), edges);
            UnweightedGraph<Node>.SearchTree tree = graph.dfs(0);
            boolean isAllRectanglesConnected = getChildren().size() == tree
                    .getNumberOfVerticesFound();
            for (Node circle : getChildren()) {
                if (isAllRectanglesConnected) { // All circles are connected
                    ((Rectangle) circle).setFill(Color.RED);
                } else {
                    ((Rectangle) circle).setStroke(Color.BLACK);
                    ((Rectangle) circle).setFill(Color.WHITE);
                }
            }
        }
    }
}
