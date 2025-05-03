package models;

import java.awt.*;

public class Line {

    private Point point1;
    private Point point2;
    private Color color;
    private String lineStyle;
    private int thickness;

    public Line(Point point1, Point point2, Color color) {
        this.point1 = point1;
        this.point2 = point2;
        this.color = color;
        this.lineStyle = "solid";
        this.thickness = 1;
    }

    public Color getColor() {
        return color;
    }

    public Point getPoint1() {
        return point1;
    }

    public Point getPoint2() {
        return point2;
    }

    public String getLineStyle() {
        return lineStyle;
    }

    public int getThickness() {
        return thickness;
    }

    public void setLineStyle(String lineStyle) {
        this.lineStyle = lineStyle;
    }

    public void setThickness(int thickness) {
        this.thickness = thickness;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setPoints(Point point1, Point point2) {
        this.point1 = point1;
        this.point2 = point2;
    }
}