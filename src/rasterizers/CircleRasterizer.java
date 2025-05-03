package rasterizers;

import models.Line;
import models.Point;
import rasters.Raster;

import java.awt.*;
import java.util.ArrayList;

public class CircleRasterizer implements Rasterizer {

    //Tato třída přijímá Line, ze které se určí střed kruhu a okraj kruhu
    //Za pomocí proměnných dx a dy se vypočítá vzdálenost okraje od středu a poté se vypočítá poloměr
    //Vykreslování kruhu začíná na bodě (0, radius) a poté mění hodnoty x a y, aby vykreslil všechny body na okraji
    //Kontroluje zda se má měnit hodnota x nebo y
    //plotCirclePoints() vykresluje 8 symetrických bodů na obvodu kruhu a poté využívá symetrii bodů, protože kruh je symetrický ve 8 směrech



    private final Raster raster;

    public CircleRasterizer(Raster raster) {
        this.raster = raster;
    }

    @Override
    public void rasterize(Line line) {
        Point center = line.getPoint1();
        Point edge = line.getPoint2();

        int dx = edge.getX() - center.getX();
        int dy = edge.getY() - center.getY();
        int radius = (int) Math.sqrt(dx * dx + dy * dy);

        drawCircle(center.getX(), center.getY(), radius, line.getColor().getRGB());
    }

    private void drawCircle(int x0, int y0, int radius, int color) {
        int x = 0;
        int y = radius;
        int d = 1 - radius;

        plotCirclePoints(x0, y0, x, y, color);

        while (x < y) {
            if (d < 0) {
                d += 2 * x + 3;
            } else {
                d += 2 * (x - y) + 5;
                y--;
            }
            x++;
            plotCirclePoints(x0, y0, x, y, color);
        }
    }

    private void plotCirclePoints(int x0, int y0, int x, int y, int color) {
        raster.setPixel(x0 + x, y0 + y, color);
        raster.setPixel(x0 - x, y0 + y, color);
        raster.setPixel(x0 + x, y0 - y, color);
        raster.setPixel(x0 - x, y0 - y, color);
        raster.setPixel(x0 + y, y0 + x, color);
        raster.setPixel(x0 - y, y0 + x, color);
        raster.setPixel(x0 + y, y0 - x, color);
        raster.setPixel(x0 - y, y0 - x, color);
    }

    @Override
    public void rasterizeArray(ArrayList<Line> lines) {
        for (Line line : lines) {
            rasterize(line);
        }
    }
}