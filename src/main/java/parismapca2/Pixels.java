
package parismapca2;

import java.util.Objects;

public class Pixels {

    private int xCorrd;
    private int yCoord;

    public Pixels(int xCorrd, int yCoord) {
        this.xCorrd = xCorrd;
        this.yCoord = yCoord;
    }

    public int getXCorrd() {
        return xCorrd;
    }

    public void setXCorrd(int xCorrd) {
        this.xCorrd = xCorrd;
    }

    public int getYCoord() {
        return yCoord;
    }

    public void setYCoord(int yCoord) {
        this.yCoord = yCoord;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pixels pixel = (Pixels) o;
        return xCorrd == pixel.xCorrd && yCoord == pixel.yCoord;
    }

    @Override
    public int hashCode() {
        return Objects.hash(xCorrd, yCoord);
    }

    @Override
    public String toString() {
        return "Pixel{" +
                "xCorrd=" + xCorrd +
                ", yCoord=" + yCoord +
                '}';
    }
}
