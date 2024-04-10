package com.example.parismapca2;


import java.util.ArrayList;
import java.util.List;

public class GraphNode<T> {

    public T data;
    public int nodeValue= Integer.MAX_VALUE;

    public List<GraphLink> adjList = new ArrayList<>(); //Adjacency list now contains link objects = key!
    //Could use any concrete List implementation
    public GraphNode(T data) {
        this.data=data;
    }

    public void connectToNodeDirected(GraphNode<T> destNode,int cost) {
        adjList.add( new GraphLink(destNode,cost) ); //Add new link object to source adjacency list
    }
    public void connectToNodeUndirected(GraphNode<T> destNode,int cost) {
        adjList.add( new GraphLink(destNode,cost) ); //Add new link object to source adjacency list
        destNode.adjList.add( new GraphLink(this,cost) ); //Add new link object to destination adjacency list
    }

    public GraphNode<Route>[] getNeighbors() {
        return new GraphNode[0];
    }

    public Integer getCostTo(GraphNode<T> neighbor) {
        return null;
    }

    public Object getValue() {
        return null;
    }
}
