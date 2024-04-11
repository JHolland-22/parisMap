package com.example.parismapca2;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to Paris Route Finder!");
    }



    @FXML
    ImageView view;
    @FXML
    ImageView finalView;
    @FXML
    ComboBox<String> start, destination, waypoints, pointsOfInterest;
    @FXML
    AnchorPane anchorpane;
    @FXML
    ListView waypointView, interestsView;
    @FXML
    ListView avoidView;
    @FXML
    TreeView<String> routeTreeView;
    @FXML
    AnchorPane mainPane;
    @FXML
    ComboBox<String> avoidRoom;
    @FXML
    ToggleButton startCorrdsButton, destinationCorrdsButton, breadthFirstButton;
    @FXML
    VBox breadthFirstBox;
    @FXML
    Label startCorrdsLabel, destinationCorrdsLabel, permNum, permLimitLabel;
    @FXML
    ColorPicker dijkstrasColorPicker, depthColorPicker, breadthColorPicker;
    @FXML
    TextField permChangeLimit;


    private ParisAPI parisAPI;
    private List<GraphNode<Route>> waypointsList;
    private List<String> interestsNames;
    private List<String> POI;
    private Pixels startPixel, destinationPixel;
    private Circle startCircle, endCircle;
    private Color dijkstraColor, depthColor, breathColor;
    private int permLimit;

    /**
     * On startup loads the map of the art gallery and makes all the connections for each room
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        parisAPI = parisAPI;
        this.waypointsList = parisAPI.getWaypointsList(start.getValue(), destination.getValue());
        this.interestsNames = parisAPI.getPointsOfInterestNames();
        this.POI = parisAPI.getPointsOfInterstList();
        view.setImage(parisAPI.getParisImage());
        //view.setImage(galleryAPI.getBreadthSearchImage());
        mainPane.setPrefHeight(view.getFitHeight());

        dijkstraColor = Color.BLUE;
        dijkstrasColorPicker.setValue(Color.BLUE);
        depthColor = Color.RED;
        depthColorPicker.setValue(Color.RED);
        breathColor = Color.ORANGE;
        breadthColorPicker.setValue(Color.ORANGE);

        permLimit = 20;
        permChangeLimit.setText(String.valueOf(20));
        permLimitLabel.setText("Limit: " + permLimit);
        permNum.setText("Total: ");

        startCircle = new Circle();
        startCircle.setRadius(3);
        startCircle.setFill(Color.RED);
        endCircle = new Circle();
        endCircle.setRadius(3);
        endCircle.setFill(Color.RED);

        ToggleGroup toggleGroup = new ToggleGroup();
        startCorrdsButton.setToggleGroup(toggleGroup);
        destinationCorrdsButton.setToggleGroup(toggleGroup);

        start.getItems().addAll(parisAPI.getNames());
        destination.getItems().addAll(parisAPI.getNames());
        avoidRoom.getItems().addAll(parisAPI.getNames());
        waypoints.getItems().addAll(parisAPI.getNames());
        pointsOfInterest.getItems().addAll(parisAPI.getPointsOfInterestNames());
        breadthFirstBox.setVisible(false);

        inputTooltip();

    }


    public void setDeadthFirstSearchPixels(MouseEvent e) {
        if (!breadthFirstButton.isSelected()) return;

        int x = (int) ((e.getX() / view.getFitWidth()) * parisAPI.getBreadthSearchImage(startPixel, destinationPixel).getWidth());
        int y = (int) ((e.getY() / view.getFitHeight()) * parisAPI.getBreadthSearchImage(startPixel, destinationPixel).getHeight());

        if (!parisAPI.getBreadthSearchImage(startPixel, destinationPixel).getPixelReader().getColor(x, y).equals(Color.BLACK)) {
            if (startCorrdsButton.isSelected()) {
                startPixel = new Pixels(x, y);
                startCorrdsLabel.setText("X: " + x + " Y: " + y);
                startCorrdsButton.setSelected(false);
                mainPane.getChildren().remove(startCircle);
                startCircle.setLayoutX(e.getX());
                startCircle.setLayoutY(e.getY());
                mainPane.getChildren().add(startCircle);
            }
            if (destinationCorrdsButton.isSelected()) {
                destinationPixel = new Pixels(x, y);
                destinationCorrdsLabel.setText("X: " + x + " Y: " + y);
                destinationCorrdsButton.setSelected(false);
                mainPane.getChildren().remove(endCircle);
                endCircle.setLayoutX(e.getX());
                endCircle.setLayoutY(e.getY());
                mainPane.getChildren().add(endCircle);
            }
        }
    }


    public void addWaypoint() {
        if (parisAPI.getWaypointsList(start.getValue(), destination.getValue()).contains(waypoints.getValue())) return;
        if (waypoints.getValue() == null) return;
        waypointView.getItems().addAll(waypoints.getValue());
        waypointsList.add((GraphNode<Route>) waypointsList);
    }

    public void addInterest() {
        if (parisAPI.getPointsOfInterstList().contains(pointsOfInterest.getValue())) return;
        if (pointsOfInterest.getValue() == null) return;
        interestsView.getItems().addAll(pointsOfInterest.getValue());
        parisAPI.getPointsOfInterstList().add(pointsOfInterest.getValue());
    }
     @FXML
    public void findDepthPath(ActionEvent actionEvent) {
        List<GraphNode<Route>> newPath = null; // Initialize newPath to null to handle the case where no conditions are met.

        // Check if waypointsList is not empty and use waypoint support to find a new path
        if (!waypointsList.isEmpty()) {
            newPath = parisAPI.getWaypointsList(start.getValue(), destination.getValue());
        }
        // If waypointsList is empty but POI is not, use interests support to find a new path
        else if (!POI.isEmpty()) {
            newPath = parisAPI.interestsSupport(start.getValue(), destination.getValue());
        }
        // If both waypointsList and POI are empty, newPath remains null

        // If a new path was found, draw it
        if (newPath != null) {
            drawSinglePath(newPath, depthColor);
        } else {
            // Handle the case where no path was found. You might want to log an error or inform the user.
            System.out.println("No path found.");
        }
    }



    public void findAllDepthPaths(ActionEvent actionEvent) {
        TreeItem<String> root = new TreeItem<>("Routes");
        int routeCounter = 1;

        List<List<GraphNode<Route>>> allPaths;

        if (!waypointsList.isEmpty()) {
            // Ensure waypointsList is of type List<GraphNode<Route>> and correctly initialized
            allPaths = parisAPI.findAllPathsUsingWaypoints(start.getValue(), destination.getValue(), waypointsList);
        } else {
            // Assuming findAllDepthFirstPaths returns List<List<GraphNode<Route>>>
            allPaths = parisAPI.findAllDepthFirstPaths(start.getValue(), destination.getValue());
        }

        if (allPaths == null || allPaths.isEmpty()) {
            permNum.setText("No paths found.");
            return;
        }

        for (List<GraphNode<Route>> path : allPaths) {
            TreeItem<String> routeItem = new TreeItem<>("Route " + routeCounter++);
            for (GraphNode<Route> node : path) {
                // Assuming each GraphNode's data can be cast to a Route object.
                TreeItem<String> routeStep = new TreeItem<>(node.data.getClass().getPackage().getName());
                routeItem.getChildren().add(routeStep);  // Add each step of the route to the route item.
            }
            root.getChildren().add(routeItem);  // Add the complete route to the root item.
        }

        // Set the constructed tree as the root for the TreeView and update UI elements accordingly.
        routeTreeView.setRoot(root);
        routeTreeView.setShowRoot(false);  // Optionally hide the root item to only show routes.
        permNum.setText("Total: " + allPaths.size());  // Update a UI element to show the number of paths found.
    }




    public void findbreadthpath(ActionEvent actionEvent) {
        List<GraphNode<Pixels>> pixels = (List<GraphNode<Pixels>>) parisAPI.getBreadthSearchImage(startPixel, destinationPixel);
        Image image = parisAPI.getParisImage();
        WritableImage writableImage = new WritableImage(image.getPixelReader(), (int) image.getWidth(), (int) image.getHeight());
        for (GraphNode<Pixels> p : pixels) {
            writableImage.getPixelWriter().setColor(p.data.getXCorrd(), p.data.getYCoord(), breathColor);
        }
        view.setImage(writableImage);
    }


    public void findPathDij(ActionEvent actionEvent) {
        List<GraphNode<Route>> pathList = new ArrayList<>();

        if (!waypointsList.isEmpty()) {
            // Assuming parisAPI provides a method to find a path considering waypoints without specifying an algorithm.
            pathList = parisAPI.getWaypointsList(start.getValue(), destination.getValue());
        } else if (!POI.isEmpty()) {
            // Assuming parisAPI provides a method to find a path considering points of interest without specifying an algorithm.
            pathList = parisAPI.interestsSupport(start.getValue(), destination.getValue());
        } else {
        }

        // Drawing the path with a specified color for Dijkstra's algorithm paths
        drawSinglePath(pathList, dijkstraColor);
    }


    public void drawSinglePath(List<GraphNode<Route>> pathList, Color c) {
        mainPane.getChildren().clear();
        for (int i = 0; i < pathList.size(); i++) {
            GraphNode<Route> node = (GraphNode<Route>) pathList.get(i);

            if (i + 1 < pathList.size()) {
                GraphNode<Route> nextNode = (GraphNode<Route>) pathList.get(i + 1);
                Line l = new Line(node.data.getXCoord(), node.data.getYCoord(), nextNode.data.getXCoord(), nextNode.data.getYCoord());
                l.setFill(c);
                l.setStroke(c);
                l.setStrokeWidth(5);
                mainPane.getChildren().add(l);
            }

        }
        inputTooltip();
    }

    public void drawMultiplePaths(List<List<GraphNode<Route>>> pathsList, Color c) {
        mainPane.getChildren().clear();  // Clear the pane to start fresh, remove if you want to keep existing drawings

        // Iterate over each path in the list of paths
        for (List<GraphNode<Route>> path : pathsList) {
            // Draw each path using the provided color
            for (int i = 0; i < path.size() - 1; i++) {  // Use size - 1 to avoid IndexOutOfBoundsException
                GraphNode<Route> currentNode = path.get(i);
                GraphNode<Route> nextNode = path.get(i + 1);

                // Create a line from the current node to the next node
                Line line = new Line(currentNode.data.getXCoord(), currentNode.data.getYCoord(),
                        nextNode.data.getXCoord(), nextNode.data.getYCoord());
                line.setStroke(c);  // Set the color of the line
                line.setStrokeWidth(5);  // Set the width of the line

                mainPane.getChildren().add(line);  // Add the line to the main pane
            }
        }

        inputTooltip();  // Refresh tooltips or perform additional actions after drawing
    }




































    public void clearMap() {
        mainPane.getChildren().clear();
        view.setImage(parisAPI.getParisImage());
        inputTooltip();
    }

    public void showBreadthSearchBox() {
        if (breadthFirstButton.isSelected()) {
            breadthFirstBox.setVisible(true);
        } else {
            breadthFirstBox.setVisible(false);
        }
    }

    public void changeDijstrasColor(ActionEvent actionEvent) {
        dijkstraColor = dijkstrasColorPicker.getValue();
    }

    public void changeDepthColor(ActionEvent actionEvent) {
        depthColor = depthColorPicker.getValue();
    }

    public void changeBreadthColor(ActionEvent actionEvent) {
        breathColor = breadthColorPicker.getValue();
    }

    public void changePermLimit(ActionEvent actionEvent) {
        try {
            permLimit = Integer.parseInt(permChangeLimit.getText());
            permLimitLabel.setText("Limit: " + permLimit);
        } catch (Exception e) {
            System.err.println("Error " + e);
        }
    }

    public void clearPOI(ActionEvent actionEvent) {
        interestsView.getItems().clear();
        parisAPI.getPointsOfInterstList().clear();
    }

    public void inputTooltip(){
        for (Circle c : parisAPI.rectangles){
            mainPane.getChildren().add(c);
        }
    }

    public void avoidThisRoom(ActionEvent event) {
    }

    public void resetAvoidedRoom(ActionEvent event) {

    }

    public void resetWaypoints(ActionEvent event) {

    }

    public void findDepthpath(ActionEvent event) {

    }

    public void findAllDepthpaths(ActionEvent event) {

    }
}











