package com.d9nich;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter a URL:");
        try {
            Graph<Integer> graph = UnweightedGraph.getGraphFromSource(
                    new Scanner(new URL(input.nextLine()).openStream()));
            //try https://liveexample.pearsoncmg.com/test/GraphSample1.txt
            graph.printEdges();

            System.out.print("The graph is");
            if (!graph.isConnected()) {
                System.out.print(" not");
            }
            System.out.println(" connected.");
        } catch (MalformedURLException ex) {
            System.out.println("Incorrect URL");
        } catch (IOException ex) {
            System.out.println("Stream problem");
        }

    }
}
