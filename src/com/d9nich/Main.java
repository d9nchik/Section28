package com.d9nich;

import com.d9nich.graph.Graph;
import com.d9nich.graph.UnweightedGraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter a file name: ");//src/com/d9nich/testFile/GraphSample1.txt
        try {
            Graph<Integer> graph = UnweightedGraph.getGraphFromSource(new Scanner(new File(input.nextLine())));
            System.out.print("Enter two vertices (integer indexes): ");
            int start = input.nextInt();
            int finish = input.nextInt();
            graph.printEdges();
            System.out.print("The path is ");
            graph.bfs(finish).getPath(start).forEach(e -> System.out.print(e + " "));
            System.out.println();
        } catch (FileNotFoundException e) {
            System.out.println("File name is incorrect!");
        }
    }
}
