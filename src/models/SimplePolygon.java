package models;

import java.util.ArrayList;
import java.awt.Color;
import rasters.RasterBufferedImage;
import rasterizers.LineCanvasRasterizer;

public class SimplePolygon {
    private final ArrayList<Point> points = new ArrayList<>();
    private boolean isClosed = false;
    private static final int CLOSING_RADIUS = 10;
    private Color color = Color.cyan;
    private int thickness = 1;
    private String lineStyle = "solid";

    // Obsahuje list bodů
    // kontroluje zda už je polygon zavřený nebo jestli má furt přidávat další body
    // má nastavenou closing_radius na 10, to znamená, že když uživatel klikne na první bod v rádiusu 10 pixelů, polygon se uzavře
    // pokud uživatel klikne mimo closing_radius, přidá se další bod

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

    public void setColor(Color color) {
        this.color = color;
    }

    public void setThickness(int thickness) {
        this.thickness = thickness;
    }

    public void setLineStyle(String lineStyle) {
        this.lineStyle = lineStyle;
    }

    public void draw(RasterBufferedImage raster, LineCanvasRasterizer rasterizer) {
        if (points.size() < 2) return;

        //Vykresli čáru
        for (int i = 0; i < points.size() - 1; i++) {
            Line line = new Line(points.get(i), points.get(i + 1), color);
            line.setThickness(thickness);
            line.setLineStyle(lineStyle);
            rasterizer.rasterizeLine(line);
        }

        //nakresli poslední čáru
        if (isClosed && points.size() >= 3) {
            Line closingLine = new Line(getLastPoint(), getFirstPoint(), color);
            closingLine.setThickness(thickness);
            closingLine.setLineStyle(lineStyle);
            rasterizer.rasterizeLine(closingLine);
        }
    }

    public void addToCanvas(LineCanvas canvas) {
        if (points.size() < 2) return;
        //přidání všech čar do kanvasu
        for (int i = 0; i < points.size() - 1; i++) {
            Line line = new Line(points.get(i), points.get(i + 1), color);
            line.setThickness(thickness);
            line.setLineStyle(lineStyle);
            addLineToCanvas(canvas, line);
        }
            //přidat poslední čáru, když je polygon uzavřen
        if (isClosed && points.size() >= 3) {
            Line closingLine = new Line(getLastPoint(), getFirstPoint(), color);
            closingLine.setThickness(thickness);
            closingLine.setLineStyle(lineStyle);
            addLineToCanvas(canvas, closingLine);
        }
    }

    private void addLineToCanvas(LineCanvas canvas, Line line) {
        switch (lineStyle) {
            case "dotted":
                canvas.addDottedLine(line);
                break;
            case "straight":
                canvas.addStraightLine(line);
                break;
            case "dashed":
                canvas.addDashedLine(line);
                break;
            default:
                canvas.addLine(line);
        }
    }
}