package rasterizers;

import models.Line;
import rasters.Raster;

import java.util.ArrayList;

public class LineRasterizerTrivial implements rasterizers.Rasterizer {
    private Raster raster;

    public LineRasterizerTrivial(Raster raster) {
        this.raster = raster;
    }

    @Override
    public void rasterize(Line line) {
        if (line.getPoint1() == null || line.getPoint2() == null) return;

        int x1 = line.getPoint1().getX();
        int y1 = line.getPoint1().getY();
        int x2 = line.getPoint2().getX();
        int y2 = line.getPoint2().getY();
        int color = line.getColor().getRGB();
        int thickness = line.getThickness();

        // Vertikální čára
        if (x1 == x2) {
            if (y1 > y2) {
                int temp = y1;
                y1 = y2;
                y2 = temp;
            }

            for (int y = y1; y <= y2; y++) {
                drawThickPixel(x1, y, thickness, color);
            }
        } else {
            // Nevertikální čára
            float k = (float) (y2 - y1) / (x2 - x1);
            float q = y1 - (k * x1);

            if (Math.abs(k) < 1) {
                if (x1 > x2) {
                    int temp = x1;
                    x1 = x2;
                    x2 = temp;
                }

                for (int x = x1; x <= x2; x++) {
                    int y = Math.round(k * x + q);
                    drawThickPixel(x, y, thickness, color);
                }
            } else {
                if (y1 > y2) {
                    int temp = y1;
                    y1 = y2;
                    y2 = temp;
                }

                for (int y = y1; y <= y2; y++) {
                    int x = Math.round((y - q) / k);
                    drawThickPixel(x, y, thickness, color);
                }
            }
        }
    }

    private void drawThickPixel(int x, int y, int thickness, int color) {
        int half = thickness / 2;
        for (int dx = -half; dx <= half; dx++) {
            for (int dy = -half; dy <= half; dy++) {
                raster.setPixel(x + dx, y + dy, color);
            }
        }
    }

    @Override
    public void rasterizeArray(ArrayList<Line> lines) {
        for (Line line : lines) {
            rasterize(line);
        }
    }
}