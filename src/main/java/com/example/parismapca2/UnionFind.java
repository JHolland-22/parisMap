package com.example.parismapca2;


import java.util.Arrays;

public class UnionFind {
    public int[] parent;

    public UnionFind(int size) {
        // Initialize the parent array with each element as its own representative
        parent = new int[size];
        for (int i = 0; i < size; i++) {
            parent[i] = i;
        }
    }

    // Find the representative (root) of the set that includes element i
    public int find(int i) {
        if (i < 0 || i >= parent.length) {
            throw new IllegalArgumentException("Index out of bounds: " + i);
        }

        System.out.println("Finding representative for index: " + i);
        System.out.println("Current parent of index " + i + ": " + parent[i]);

        if (parent[i] == -1) {
            return -1;
        }

        if (parent[i] == i) {
            return i; // i is the representative of its own set
        }

        // Recursively find the representative of the parent until reaching the root
        int representative = find(parent[i]); // Path compression
        parent[i] = representative;

        System.out.println("Updated parent of index " + i + " after path compression: " + parent[i]);

        return representative;
    }


    // Unite the set that includes element i and the set that includes element j
    public void union(int i, int j) {
        int irep = find(i); // Find the representative of set containing i
        int jrep = find(j); // Find the representative of set containing j

        // Make the representative of i's set be the representative of j's set
        parent[irep] = jrep;
    }

    public static void main(String[] args) {
        int size = 5; // Replace with your desired size
        UnionFind uf = new UnionFind(size);

        // Perform union operations as needed
        uf.union(1, 2);
        uf.union(3, 4);

        // Check if elements are in the same set
        boolean inSameSet = uf.find(1) == uf.find(2);
        System.out.println("Are 1 and 2 in the same set? " + inSameSet);
    }

    public void displayDSAsText(int width) {
        for (int i = 0; i < parent.length; i++)
            if (width != 0) {
                System.out.print(HelloApplication.ds.find(i) + ((i + 1) % width == 0 ? "\n" : " "));
            }
    }
}
