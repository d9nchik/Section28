package com.d9nich;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("Enter a URL:");
        Graph<Integer> graph = UnweightedGraph.getGraphFromURL(input.nextLine());
        //try https://liveexample.pearsoncmg.com/test/GraphSample1.txt
        graph.printEdges();

        System.out.print("The graph is");
        if (!graph.isConnected()) {
            System.out.print(" not");
        }
        System.out.println(" connected.");
    }
}
