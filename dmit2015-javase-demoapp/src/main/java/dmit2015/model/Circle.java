package dmit2015.model;

public class Circle {

    private double radius;

    public double getRadius() {
        return radius;
    }

    public void setRadius(double radius) {
        if (radius <= 0) {
            throw new IllegalArgumentException("Radius must be a positive non-zero number");
        }
        this.radius = radius;
    }

    public Circle() {
        setRadius(1.0);
    }

    public Circle(double radius) {
//        this.radius = radius;
        setRadius(radius);
    }

    public double getArea() {
        return Math.PI * radius * radius;
    }

}
