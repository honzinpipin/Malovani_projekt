package rasterizers;

import models.Line;
import rasters.Raster;

import java.util.ArrayList;

public class StraightLineRasterizer implements Rasterizer {

    private Raster raster;

    public StraightLineRasterizer(Raster raster) {
        this.raster = raster;
    }

    @Override
    public void rasterize(Line line) {
        int x1 = line.getPoint1().getX();
        int y1 = line.getPoint1().getY();
        int x2 = line.getPoint2().getX();
        int y2 = line.getPoint2().getY();

        double angle = Math.toDegrees(Math.atan2(y2 - y1, x2 - x1));
        angle = (angle + 360) % 360;

        int dx = x2 - x1;
        int dy = y2 - y1;
        int distance = (int) Math.sqrt(dx * dx + dy * dy);
        int snapAngle = (int)(Math.round(angle / 45) * 45) % 360;

        switch (snapAngle) {
            case 0:
            case 360:
                x2 = x1 + distance;
                y2 = y1;
                break;
            case 45:
                int diag45 = (int)(distance / Math.sqrt(2));
                x2 = x1 + diag45;
                y2 = y1 + diag45;
                break;
            case 90:
                x2 = x1;
                y2 = y1 + distance;
                break;
            case 135:
                int diag135 = (int)(distance / Math.sqrt(2));
                x2 = x1 - diag135;
                y2 = y1 + diag135;
                break;
            case 180:
                x2 = x1 - distance;
                y2 = y1;
                break;
            case 225:
                int diag225 = (int)(distance / Math.sqrt(2));
                x2 = x1 - diag225;
                y2 = y1 - diag225;
                break;
            case 270:
                x2 = x1;
                y2 = y1 - distance;
                break;
            case 315:
                int diag315 = (int)(distance / Math.sqrt(2));
                x2 = x1 + diag315;
                y2 = y1 - diag315;
                break;
        }

        drawLine(x1, y1, x2, y2, line.getColor().getRGB(), line.getThickness());
    }

    private void drawLine(int x1, int y1, int x2, int y2, int color, int thickness) {
        int dx = Math.abs(x2 - x1);
        int dy = Math.abs(y2 - y1);
        int sx = x1 < x2 ? 1 : -1;
        int sy = y1 < y2 ? 1 : -1;
        int err = dx - dy;

        while (true) {
            drawThickPixel(x1, y1, thickness, color);

            if (x1 == x2 && y1 == y2) break;

            int e2 = err * 2;
            if (e2 > -dy) {
                err -= dy;
                x1 += sx;
            }
            if (e2 < dx) {
                err += dx;
                y1 += sy;
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
    public void rasterizeArray(ArrayList<Line> arrayList) {
        for (Line line : arrayList) {
            rasterize(line);
        }
    }
}
