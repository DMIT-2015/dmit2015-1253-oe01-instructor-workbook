package dmit2015.model;

/**
 * This class models a read-only Rectangle shape.
 *
 * @author Sam Wu
 * @version 2026.05.08
 *
 */
public class Rectangle {

    private final double length;
    private final double width;

    public Rectangle(double length, double width) {
        if (length <= 0 || width <= 0) {
            throw new IllegalArgumentException("Length and Width must be positive non-zero value");
        }
        this.length = length;
        this.width = width;
    }

    public double getLength() {
        return length;
    }

    public double getWidth() {
        return width;
    }

    public double getArea() {
        return length * width;
    }

    public double getPerimeter() {
        return 2 * (length + width);
    }
}
