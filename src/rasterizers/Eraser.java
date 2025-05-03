package rasterizers;

import models.Point;
import rasters.Raster;

public class Eraser {

    private static final int defaultColor = 0xFFFFFF; // Bílá barva

    //Guma má nastaavenou defaultní barvu na bílou
    //Maže okolí bodu podle thickness
    //Pro plynulost je metoda eraseLine(), aby se nevynechávaly pixely při rychlém tažení myši


    // Vymaže okolí bodu v zadané tloušťce
    public static void erase(Raster raster, Point point, int thickness) {
        int radius = thickness / 2;

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                int px = point.getX() + dx;
                int py = point.getY() + dy;

                if (px >= 0 && py >= 0 && px < raster.getWidth() && py < raster.getHeight()) {
                    if (Math.sqrt(dx * dx + dy * dy) <= radius) {
                        raster.setPixel(px, py, defaultColor);
                    }
                }
            }
        }
    }

    // Spojí dva body a smaže celou cestu mezi nimi
    public static void eraseLine(Raster raster, Point p1, Point p2, int thickness) {
        int x0 = p1.getX();
        int y0 = p1.getY();
        int x1 = p2.getX();
        int y1 = p2.getY();

        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int err = dx - dy;

        while (true) {
            erase(raster, new Point(x0, y0), thickness);

            if (x0 == x1 && y0 == y1) break;

            int e2 = 2 * err;
            if (e2 > -dy) {
                err -= dy;
                x0 += sx;
            }
            if (e2 < dx) {
                err += dx;
                y0 += sy;
            }
        }
    }
}
