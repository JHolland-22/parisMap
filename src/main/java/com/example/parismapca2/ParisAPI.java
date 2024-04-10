package com.example.parismapca2;


import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import parismapca2.Pixels;
import parismapca2.Route;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;


public class ParisAPI {

    private static final int MAX_PATH_LENGTH = 0;
    private List<Route> routes;
    private HashMap<String, GraphNode<Route>> routesHashMap;
    private List<String> names;
    private List<GraphNode<Route>> routeNodes;
    private List<GraphNode<Pixels>> pixelNodes;
    private HashMap<String, GraphNode<Pixels>> hashMap;
    private Image parisImage;
    private Image breadthSearchImage;
    private List<String> waypointsList;

    private List<String> pointsOfInterestNames;
    private List<String> pointsOfInterstList;


    public List<Circle> rectangles;


    public ParisAPI() {
        this.waypointsList = new LinkedList<>();
        this.hashMap = new HashMap<>();
        this.routes = new LinkedList<>();
        this.names = new ArrayList<>();
        this.pointsOfInterstList = new ArrayList<>();
        this.pointsOfInterestNames = new ArrayList<>();
        this.routeNodes = new LinkedList<>();
        this.pixelNodes = new LinkedList<>();
        this.routesHashMap = new HashMap<>();
        this.parisImage = new Image(getClass().getResourceAsStream("@../../../../../../../../../PARISSS.png"));
        this.breadthSearchImage = new Image(getClass().getResourceAsStream("@../../../../../../../../../PARISSS.png"));
        this.rectangles = new LinkedList();
        readInDatabase();
        connectRoutes();
        buildPixelGraph();
    }

    public List<String> getPointsOfInterstList() {
        return pointsOfInterstList;
    }

    public void setPointsOfInterstList(List<String> pointsOfInterstList) {
        this.pointsOfInterstList = pointsOfInterstList;
    }

    public List<String> getPointsOfInterestNames() {
        return pointsOfInterestNames;
    }

    public void setPointsOfInterestNames(List<String> pointsOfInterestNames) {
        this.pointsOfInterestNames = pointsOfInterestNames;
    }

    public List<GraphNode<Pixels>> getPixelNodes() {
        return pixelNodes;
    }

    public void setPixelNodes(List<GraphNode<Pixels>> pixelNodes) {
        this.pixelNodes = pixelNodes;
    }

    public Image getBreadthSearchImage() {
        return breadthSearchImage;
    }

    public void setBreadthSearchImage(Image breadthSearchImage) {
        this.breadthSearchImage = breadthSearchImage;
    }

    public List<String> getWaypointsList() {
        return waypointsList;
    }

    public void setWaypointsList(List<String> waypointsList) {
        this.waypointsList = waypointsList;
    }


    public List<Route> getRoutes() {
        return routes;
    }

    public void setRooms(List<Route> route) {
        this.routes = route;
    }

    public List<GraphNode<Route>> getRouteNodes() {
        return routeNodes;
    }

    public void setRoomNodes(List<GraphNode<Route>> roomNodes) {
        this.routeNodes = roomNodes;
    }


    public GraphNode<Route> findGraphNodeByInterest(String interest) {
        for (Route r : routes) {
            //System.out.println(r.getRoomName() + ", " + r.getExhibit());
            if (r.getLandMark().equals(interest)) {
                return findGraphNode(r.getRouteName());
            }
        }
        return null;
    }

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }

    public Image getGalleryImage() {
        return parisImage;
    }

    public List<List<GraphNode<?>>> generateMultipleRoutes(String start, String destination) {
        GraphNode<?> startNode = routesHashMap.get(start);
        GraphNode<?> destNode = routesHashMap.get(destination);

        List<List<GraphNode<?>>> allPaths = new ArrayList<>();
        findPathsDepthFirst(startNode, destNode, new ArrayList<>(), allPaths);

        // Optional: Filter paths by some criteria, like length
        allPaths.removeIf(path -> path.size() > MAX_PATH_LENGTH); // Define MAX_PATH_LENGTH as per your criteria

        return allPaths;
    }

    private void findPathsDepthFirst(GraphNode<?> current, GraphNode<?> destination, List<GraphNode<?>> path, List<List<GraphNode<?>>> allPaths) {
        path.add(current);

        if (current.equals(destination)) {
            allPaths.add(new ArrayList<>(path)); // Found a path, add a copy to the results
        } else {
            // Recurse to all neighbors
            for (GraphNode<?> neighbor : current.getNeighbors()) {
                if (!path.contains(neighbor)) { // Avoid cycles
                    findPathsDepthFirst(neighbor, destination, path, allPaths);
                }
            }
        }

        path.remove(path.size() - 1); // Backtrack
    }

// Assuming GraphNode<?> has a getNeighbors() method to retrieve adjacent nodes


    private void readInDatabase() {
        String line = "";
        try {
            File file = new File("src/main/resources/csv/mappings.csv");
            BufferedReader br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                Route r = new Route(values[0], Integer.parseInt(values[1]), Integer.parseInt(values[2]), values[3], values[4]);
                //System.out.println(values[0] + ", " + values[1] + ", " + values[2] + ", " + values[3] + ", " + values[4]);
                GraphNode<Route> node = new GraphNode<>(r);
                routeNodes.add(node);
                routesHashMap.put(values[0], node);
                names.add(values[0]);


                Circle c = new Circle(Integer.parseInt(values[1]), Integer.parseInt(values[2]), 6);

                c.setFill(Color.TRANSPARENT);
                rectangles.add(c);
                Tooltip t = new Tooltip("Room Number: " + r.getRouteName() + "\nLandMark : " + r.getLandMark());
                Tooltip.install(c, t);


                if (!pointsOfInterestNames.contains(values[3])) pointsOfInterestNames.add(values[3]);
                routes.add(r);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void connectNodes(String nodeA, String nodeB) {
        GraphNode<Route> roomA = routesHashMap.get(nodeA);
        GraphNode<Route> roomB = routesHashMap.get(nodeB);
        roomA.connectToNodeUndirected(roomB, 1);
    }

    private void connectRoutes() {

        String line = "";
        try {
            File file = new File("src/main/resources/csv/RouteConnection.csv");
            BufferedReader br = new BufferedReader(new FileReader(file));
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                connectNodes(values[0], values[1]);
                //System.out.println(values[0] + values[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public List<GraphNode<Route>> interestsSupport(String start, String destination) {
        List<GraphNode<Route>> pathList = new LinkedList<>();
        GraphNode<Route> startNode = findGraphNode(start);
        GraphNode<Route> endNode = findGraphNode(destination);

        List<GraphNode<Route>> finalPath = searchGraphWithDijkstra(startNode, endNode);

        if (finalPath != null) {
            pathList.addAll(finalPath);
        }

        return pathList;
    }

    // Simplified Dijkstra's algorithm for finding the shortest path
    private List<GraphNode<Route>> searchGraphWithDijkstra(GraphNode<Route> start, GraphNode<Route> end) {
        Map<GraphNode<Route>, GraphNode<Route>> predecessors = new HashMap<>();
        Map<GraphNode<Route>, Integer> distances = new HashMap<>();
        PriorityQueue<GraphNode<Route>> queue = new PriorityQueue<>(Comparator.comparingInt(distances::get));

        // Initialize distances
        for (GraphNode<Route> node : getAllNodes()) { // Assuming you have a method to get all nodes
            distances.put(node, Integer.MAX_VALUE);
            predecessors.put(node, null);
            queue.add(node);
        }
        distances.put(start, 0);
        queue.add(start);

        while (!queue.isEmpty()) {
            GraphNode<Route> current = queue.poll();

            if (current.equals(end)) {
                break; // Found the shortest path to the destination
            }

            for (GraphNode<Route> neighbor : current.getNeighbors()) {
                int newDist = distances.get(current) + current.getCostTo(neighbor);
                if (newDist < distances.get(neighbor)) {
                    distances.put(neighbor, newDist);
                    predecessors.put(neighbor, current);
                    queue.add(neighbor);
                }
            }
        }

        return buildPath(predecessors, end);
    }

    // Utility method to build the path from the start node to the end node
    private List<GraphNode<Route>> buildPath(Map<GraphNode<Route>, GraphNode<Route>> predecessors, GraphNode<Route> end) {
        LinkedList<GraphNode<Route>> path = new LinkedList<>();
        GraphNode<Route> step = end;

        if (predecessors.get(step) == null) {
            return null; // No path found
        }

        path.add(step);
        while (predecessors.get(step) != null) {
            step = predecessors.get(step);
            path.add(step);
        }

        Collections.reverse(path);
        return path;
    }

    // Placeholder for finding a GraphNode by some identifier
    GraphNode<Route> findGraphNode(String identifier) {
        // Actual implementation will depend on your graph structure
        return null;
    }

    // Placeholder for a method that returns all nodes in the graph
    private Set<GraphNode<Route>> getAllNodes() {
        // Actual implementation will depend on your graph structure
        return new HashSet<>();
    }

    public void buildPixelGraph() {
        int cost = 1;
        for (int x = 0; x < breadthSearchImage.getWidth(); x++) {
            for (int y = 0; y < breadthSearchImage.getHeight(); y++) {
                if (breadthSearchImage.getPixelReader().getColor(x, y).equals(Color.WHITE)) {
                    GraphNode<Pixels> node = new GraphNode<>(new Pixels(x, y));
                    pixelNodes.add(node);
                    hashMap.put(node.data.toString(), node);
                }
            }
        }
        for (int x = 0; x < breadthSearchImage.getWidth(); x++) {
            for (int y = 0; y < breadthSearchImage.getHeight(); y++) {
                if (breadthSearchImage.getPixelReader().getColor(x, y).equals(Color.WHITE)) {

                    GraphNode<Pixels> current = findPixel(new Pixels(x, y));
                    //Below current pixel
                    int belowX = x;
                    int belowY = y + 1;
                    if (belowY < breadthSearchImage.getHeight()) {
                        if (breadthSearchImage.getPixelReader().getColor(belowX, belowY).equals(Color.WHITE)) {
                            GraphNode<Pixels> pixel = findPixel(new Pixels(belowX, belowY));
                            current.connectToNodeUndirected(pixel, cost);
                        }
                    }
                    //Right of current pixel
                    int rightX = x + 1;
                    int rightY = y;
                    if (rightX < breadthSearchImage.getWidth()) {
                        if (breadthSearchImage.getPixelReader().getColor(rightX, rightY).equals(Color.WHITE)) {
                            GraphNode<Pixels> pixel = findPixel(new Pixels(rightX, rightY));
                            current.connectToNodeUndirected(pixel, cost);
                        }
                    }
                    //diagonal down right to pixel
                    int diagX = x + 1;
                    int diagY = y + 1;
                    if (diagX < breadthSearchImage.getWidth() && diagY < breadthSearchImage.getHeight()) {
                        if (breadthSearchImage.getPixelReader().getColor(diagX, diagY).equals(Color.WHITE)) {
                            GraphNode<Pixels> pixel = findPixel(new Pixels(diagX, diagY));
                            current.connectToNodeUndirected(pixel, cost);
                        }
                    }
                }
            }
        }
    }

    public void buildPixelGraph(Image image) {
        int cost = 1;
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                if (image.getPixelReader().getColor(x, y).equals(Color.BLACK)) continue;
                pixelNodes.add(new GraphNode<>(new Pixels(x, y)));

            }
        }

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                if (image.getPixelReader().getColor(x, y).equals(Color.BLACK)) continue;

                GraphNode<Pixels> current = findPixel(new Pixels(x, y));
                //System.out.println(current.data);
                //Below current pixel
                int belowX = x;
                int belowY = y + 1;
                if (belowY < image.getHeight()) {
                    if (!image.getPixelReader().getColor(belowX, belowY).equals(Color.BLACK)) {
                        GraphNode<Pixels> pixel = findPixel(new Pixels(belowX, belowY));
                        current.connectToNodeUndirected(pixel, cost);
                    }
                }
                //Right of current pixel
                int rightX = x + 1;
                int rightY = y;
                if (rightX < image.getWidth()) {
                    if (!image.getPixelReader().getColor(rightX, rightY).equals(Color.BLACK)) {
                        GraphNode<Pixels> pixel = findPixel(new Pixels(rightX, rightY));
                        current.connectToNodeUndirected(pixel, cost);
                    }
                }
                //diagonal down right to pixel
                int diagX = x + 1;
                int diagY = y + 1;
                if (diagX < image.getWidth() && diagY < image.getHeight()) {
                    if (!image.getPixelReader().getColor(diagX, diagY).equals(Color.BLACK)) {
                        GraphNode<Pixels> pixel = findPixel(new Pixels(diagX, diagY));
                        current.connectToNodeUndirected(pixel, cost);
                    }
                }

            }
        }
        //System.out.println(pixelNodes.size());
    }

    public GraphNode<Pixels> findPixel(Pixels lookingFor) {
        return hashMap.get(lookingFor.toString());
    }

    public boolean containsPixel(Pixels pixel) {
        for (GraphNode<Pixels> node : pixelNodes) {
            Pixels p = node.data;
            if (p.equals(pixel)) return true;
        }
        return false;
    }
}
