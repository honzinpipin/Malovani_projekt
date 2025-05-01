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

        if (line.getPoint1() == null || line.getPoint2() == null) {
            return; // ochrana proti chybě
        }

        int x1 = line.getPoint1().getX();
        int y1 = line.getPoint1().getY();
        int x2 = line.getPoint2().getX();
        int y2 = line.getPoint2().getY();


        //vertikální čára
        if (x1 == x2) {

            if (y1 > y2) {
                int temp = y1;
                y1 = y2;
                y2 = temp;
            }

            for (int y = y1; y <= y2; y++) {
                raster.setPixel(x1, y, line.getColor().getRGB());
            }
        } else {
            // Nevertikální čára

            float k = (float) (y2 - y1) / (x2 - x1);
            float q = y1 - (k * x1);

            //když je k < 1, jedná se o vodorovnou čáru
            if (Math.abs(k) < 1) {
                if (x1 > x2) {
                    int temp = x1;
                    x1 = x2;
                    x2 = temp;
                }

                for (int x = x1; x <= x2; x++) {
                    int y = Math.round(k * x + q);
                    raster.setPixel(x, y, line.getColor().getRGB());
                }
            } else {
                if (y1 > y2) {
                    int temp = y1;
                    y1 = y2;
                    y2 = temp;
                }

                for (int y = y1; y <= y2; y++) {
                    int x = Math.round((y - q) / k);
                    raster.setPixel(x, y, line.getColor().getRGB());
                }
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