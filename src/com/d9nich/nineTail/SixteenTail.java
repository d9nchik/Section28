package com.d9nich.nineTail;

public class SixteenTail {
    public static void main(String[] args) {
        SixteenTailModel model = new SixteenTailModel();

        System.out.println("Solution doesn`t exist for " +
                (65_536 - model.tree.getNumberOfVerticesFound()) + " patterns.");
    }
}
