package models;

import java.util.ArrayList;

public class SimplePolygon {
    private final ArrayList<Point> points = new ArrayList<>();
    private boolean isClosed = false;
    private static final int CLOSING_RADIUS = 10;

    public void addPoint(Point p) {
        if (!points.isEmpty() && isNearFirstPoint(p)) {
            isClosed = true;
        } else if (!isClosed) {
            points.add(p);
        }
    }

    public boolean isNearFirstPoint(Point p) {
        if (points.isEmpty()) return false;
        Point first = points.get(0);
        int dx = p.getX() - first.getX();
        int dy = p.getY() - first.getY();
        return Math.sqrt(dx * dx + dy * dy) < CLOSING_RADIUS;
    }

    public ArrayList<Point> getPoints() {
        return points;
    }

    public boolean isClosed() {
        return isClosed;
    }

    public Point getLastPoint() {
        if (points.isEmpty()) return null;
        return points.get(points.size() - 1);
    }

    public Point getFirstPoint() {
        if (points.isEmpty()) return null;
        return points.get(0);
    }

    public void setClosed(boolean closed) {
        isClosed = closed;
    }


    public void clear() {
        points.clear();
        isClosed = false;
    }
}
