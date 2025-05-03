package rasterizers;

import models.Line;
import rasters.Raster;

import java.util.ArrayList;

public class DashedLineRasterizer implements Rasterizer {
    //Vstup je Line, ze které jsou následně vytaženy 4 body
    //kontrola zda se má vykreslovat vertikálně nebo horizontálně
    //vynechávání pixelů je vytvořeno tak, že když je hodnota x ve forloopu dělitelná 5, zapne se cyklus na 2 kroky,
    // kde se vynechájí pixely = 2 pixelová mezera / 5 pixelová čára
    //umožňuje nakreslit tlustou čáru, kde se veme thickness, vydělí se 2 a dokreslí se 1 polovina nahoru a druhá dolu



    private Raster raster;

    public DashedLineRasterizer(Raster raster) {
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

            int counterX = 0;
            for (int x = x1; x <= x2; x ++) {
                if(x % 5 != 0) {
                    if(counterX == 0){
                        int y = Math.round(k * x + q);
                        drawThickPixel(x, y, thickness, color);
                    }
                    else{
                        counterX--;
                    }
                }
                else{
                    counterX = 2;
                }
            }
        } else {
            if (y1 > y2) {
                int temp = y1;
                y1 = y2;
                y2 = temp;
            }

            int counterY = 0;
            for (int y = y1; y <= y2; y ++) {
                if(y % 5 != 0) {
                    if(counterY == 0){
                    int x = Math.round((y - q) / k);
                    drawThickPixel(x, y, thickness, color);
                    }
                    else{
                        counterY--;
                    }
                }
                else{
                    counterY = 2;
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