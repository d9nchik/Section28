package com.d9nich.nineTail;

import java.util.Scanner;

public class SixteenTail {
    public static void main(String[] args) {
        // Prompt the user to enter nine coins' Hs and Ts
        System.out.print("Enter the initial 16 coins Hs and Ts: ");
        Scanner input = new Scanner(System.in);
        String s = input.nextLine();
        char[] initialNode = s.toCharArray();
        SixteenTailModel model = new SixteenTailModel();
        java.util.List<Integer> path =
                model.getShortestPath(SixteenTailModel.getIndex(initialNode));
        if (path.size() == 1)
            System.out.println("Solution doesn`t exist!");
        else {
            System.out.println("The steps to flip the coins are ");
            for (Integer integer : path)
                SixteenTailModel.printNode(SixteenTailModel.getNode(integer));
        }
    }
}
