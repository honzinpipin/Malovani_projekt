package rasterizers;

import models.Line;
import rasters.Raster;

import java.util.ArrayList;

public class DottedLineRasterizer implements rasterizers.Rasterizer {
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


        float k = (float) (y2 - y1) / (x2 - x1);
        float q = y1 - (k * x1);

        // TODO ošetřit mimo hranice rastru

        //vodorovná čára
        if (Math.abs(k) < 1) {
            if (x1 > x2) {
                int x = x1;
                x1 = x2;
                x2 = x;
            }

            for (int x = x1; x <= x2; x += 10) {
                int y = Math.round(k * x + q);

                raster.setPixel(x, y, line.getColor().getRGB());
            }
        }
        //vertikální čára
        else {
            if (y1 > y2) {
                int y = y1;
                y1 = y2;
                y2 = y;
            }

            for (int y = y1; y < y2; y += 10) {
                int x = Math.round((y - q) / k);

                raster.setPixel(x, y, line.getColor().getRGB());
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