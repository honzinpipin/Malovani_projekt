package rasterizers;

import models.Line;
import models.Point;
import rasters.Raster;

import java.awt.*;
import java.util.ArrayList;

public class RectangleRasterizer implements Rasterizer {
    private Raster raster;

    //Vykreslování obdélníků za pomocí diagonál a dopočítávání rohů
    //Rohy se najdou za pomocí nejmenší/největší souřadnice - např. topLeft bude mít obě souřadnice nejmenší
    //Poté jen spojíme 2 rohy čárou a přidáme čáru do Listu

    public RectangleRasterizer(Raster raster) {
        this.raster = raster;
    }

    @Override
    public void rasterize(Line diagonalLine) {
        int x1 = diagonalLine.getPoint1().getX();
        int y1 = diagonalLine.getPoint1().getY();
        int x2 = diagonalLine.getPoint2().getX();
        int y2 = diagonalLine.getPoint2().getY();

        ArrayList<Line> lines = new ArrayList();


        // Získáme rohy obdélníku
        Point topLeft = new Point(Math.min(x1, x2), Math.min(y1, y2));
        Point bottomRight = new Point(Math.max(x1, x2), Math.max(y1, y2));
        Point topRight = new Point(bottomRight.getX(), topLeft.getY());
        Point bottomLeft = new Point(topLeft.getX(), bottomRight.getY());

        // Vykreslení horní hrany
        Line topLine = new Line(topLeft, topRight, Color.cyan);
        lines.add(topLine);

        for (int x = topLeft.getX(); x <= topRight.getX(); x++) {
            raster.setPixel(x, topLeft.getY(), diagonalLine.getColor().getRGB());
        }

        // Vykreslení spodní hrany
        Line bottomLine = new Line(bottomRight, bottomLeft, Color.cyan);
        lines.add(bottomLine);

        for (int x = bottomLeft.getX(); x <= bottomRight.getX(); x++) {
            raster.setPixel(x, bottomLeft.getY(), diagonalLine.getColor().getRGB());
        }

        // Vykreslení levé hrany
        Line leftLine = new Line(bottomLeft, topLeft, Color.cyan);
        lines.add(leftLine);

        for (int y = topLeft.getY(); y <= bottomLeft.getY(); y++) {
            raster.setPixel(topLeft.getX(), y, diagonalLine.getColor().getRGB());
        }

        // Vykreslení pravé hrany
        Line rightLine = new Line(topRight, bottomRight, Color.cyan);
        lines.add(rightLine);

        for (int y = topRight.getY(); y <= bottomRight.getY(); y++) {
            raster.setPixel(topRight.getX(), y, diagonalLine.getColor().getRGB());
        }
    }

    @Override
    public void rasterizeArray(ArrayList<Line> lines) {
        for (Line line : lines) {
            rasterize(line);
        }
    }
}