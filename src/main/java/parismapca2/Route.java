package parismapca2;

public class Route {

    private String routeName;
    private String landMark;
    private int xCoord;
    private int yCoord;

    public Route(String routeName, int xCoord, int yCoord, String landMark, String value) {
        this.routeName = routeName;
        this.xCoord = xCoord;
        this.yCoord = yCoord;
        this.landMark = landMark;
    }

    public String getRouteName() {
        return routeName;
    }

    public int getXCoord() {
        return xCoord;
    }

    public int getYCoord(){
        return yCoord;
    }

    public void SetRouteName(String routeName) {
        this.routeName = routeName;
    }

    public String getLandMark() {
        return landMark;
    }

    public void setLandMark(String landMark) {
        this.landMark = landMark;
    }

    public int getxCoord() {
        return xCoord;
    }

    public void setxCoord(int xCoord) {
        this.xCoord = xCoord;
    }

    public int getyCoord() {
        return yCoord;
    }

    public void setyCoord(int yCoord) {
        this.yCoord = yCoord;
    }




    @Override
    public String toString() {
        return "Room{" +
                "roomName='" + routeName + '\'' +
                ", xCoord=" + xCoord +
                ", yCoord=" + yCoord +
                '}';
    }
}