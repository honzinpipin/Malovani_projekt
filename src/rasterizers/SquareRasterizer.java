package rasterizers;

import models.Line;
import models.Point;
import rasters.Raster;

import java.awt.*;
import java.util.ArrayList;

public class SquareRasterizer implements Rasterizer {
    private Raster raster;

    public SquareRasterizer(Raster raster) {
        //Funguje na stejné bázi jako obdélník, ale má přidanou korekci bodů, aby byl čtverec symetrický
        //xSign a ySign kontrolují, zda je rozdíl kladný nebo záporný = posun doleva nebo doprava


        this.raster = raster;
    }

    @Override
    public void rasterize(Line diagonalLine) {
        int x1 = diagonalLine.getPoint1().getX();
        int y1 = diagonalLine.getPoint1().getY();
        int x2 = diagonalLine.getPoint2().getX();
        int y2 = diagonalLine.getPoint2().getY();

        int side = Math.max(Math.abs(x2 - x1), Math.abs(y2 - y1));
        ArrayList<Line> lines = new ArrayList();

        // Korekce druhého bodu podle směru a délky strany
        int xSign = (x2 - x1) >= 0 ? 1 : -1;
        int ySign = (y2 - y1) >= 0 ? 1 : -1;

        int x2Adjusted = x1 + side * xSign;
        int y2Adjusted = y1 + side * ySign;

        // Spočítej 4 rohové body čtverce
        Point topLeft = new Point(Math.min(x1, x2Adjusted), Math.min(y1, y2Adjusted));
        Point bottomRight = new Point(Math.max(x1, x2Adjusted), Math.max(y1, y2Adjusted));
        Point topRight = new Point(bottomRight.getX(), topLeft.getY());
        Point bottomLeft = new Point(topLeft.getX(), bottomRight.getY());

        // Horní hrana
        Line topLine = new Line(topLeft, topRight, Color.cyan);
        lines.add(topLine);

        for (int x = topLeft.getX(); x <= topRight.getX(); x++) {
            raster.setPixel(x, topLeft.getY(), diagonalLine.getColor().getRGB());
        }

        // Spodní hrana
        Line bottomLine = new Line(bottomRight, bottomLeft, Color.cyan);
        lines.add(bottomLine);

        for (int x = bottomLeft.getX(); x <= bottomRight.getX(); x++) {
            raster.setPixel(x, bottomLeft.getY(), diagonalLine.getColor().getRGB());
        }

        // Levá hrana
        Line leftLine = new Line(bottomLeft, topLeft, Color.cyan);
        lines.add(leftLine);

        for (int y = topLeft.getY(); y <= bottomLeft.getY(); y++) {
            raster.setPixel(topLeft.getX(), y, diagonalLine.getColor().getRGB());
        }

        // Pravá hrana
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