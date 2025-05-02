package rasterizers;

import models.Line;
import rasters.Raster;

import java.util.ArrayList;

public class DottedLineRasterizer implements Rasterizer {
    private Raster raster;

    public DottedLineRasterizer(Raster raster) {
        this.raster = raster;
    }

    @Override
    public void rasterize(Line line) {
        int x1 = line.getPoint1().getX();
        int y1 = line.getPoint1().getY();
        int x2 = line.getPoint2().getX();
        int y2 = line.getPoint2().getY();

        if (x1 == x2 && y1 == y2) {
            drawThickPixel(x1, y1, line.getThickness(), line.getColor().getRGB());
            return;
        }

        float k = (float) (y2 - y1) / (x2 - x1);
        float q = y1 - (k * x1);

        int thickness = line.getThickness();
        int color = line.getColor().getRGB();

        if (Math.abs(k) < 1) {
            if (x1 > x2) {
                int temp = x1;
                x1 = x2;
                x2 = temp;
            }

            for (int x = x1; x <= x2; x += 10) {
                int y = Math.round(k * x + q);
                drawThickPixel(x, y, thickness, color);
            }
        } else {
            if (y1 > y2) {
                int temp = y1;
                y1 = y2;
                y2 = temp;
            }

            for (int y = y1; y <= y2; y += 10) {
                int x = Math.round((y - q) / k);
                drawThickPixel(x, y, thickness, color);
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
