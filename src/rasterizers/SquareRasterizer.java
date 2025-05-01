package rasterizers;
import models.Line;
import models.Point;
import rasters.Raster;

import java.awt.*;
import java.util.ArrayList;
public class SquareRasterizer implements rasterizers.Rasterizer{
    private Raster raster;

    public SquareRasterizer(Raster raster) {

        this.raster = raster;
    }

    @Override public void rasterize(Line line) {
        int x1 = line.getPoint1().getX();
        int y1 = line.getPoint1().getY();
        int x2 = line.getPoint2().getX();
        int y2 = line.getPoint2().getY();

        ArrayList<Line> lines = new ArrayList();


        Point p1 = new Point(0, 0);
        Point p2 = new Point(0, 0);;
        Point p3 = new Point(0, 0);;
        Point p4 = new Point(0, 0);;

        int dy = y1 - y2;
        int dx = x1 - x2;

        //debug
       // System.out.println("x1: " + x1  + " y1: " + y1 + " x2: " + x2 + " y2: " + y2 + " dx: " + dx + " dy: " + dy);

        //horní stěna
        if(Math.abs(dy) > dx && dy < 0) {
            int x = x1;
            dy = Math.abs(dy);

            p1.setX(x - dy);
            p1.setY(y2 + dy);

            p2.setX(x + dy);
            p2.setY(y2 + dy);

            p3.setX(x + dy);
            p3.setY(y1);

            p4.setX(x - dy);
            p4.setY(y1);
        }
        //pravá stěna
        else if(Math.abs(dx) > dy && dx < 0){
            int y = y1;
            dx = Math.abs(dx);

            p1.setX(x1 - dx);
            p1.setY(y + dx);

            p2.setX(x2);
            p2.setY(y + dx);

            p3.setX(x2);
            p3.setY(y - dx);

            p4.setX(x1 - dx);
            p4.setY(y - dx);
        }
        //spodní stěna
        else if(dy > dx){
            int x = x1;
            p1.setX(x - dy);
            p1.setY(y1 + dy);

            p2.setX(x + dy);
            p2.setY(y1 + dy);

            p3.setX(x + dy);
            p3.setY(y2);

            p4.setX(x - dy);
            p4.setY(y2);
        }

        //levá stěna
        else if(dx > dy){
            int y = y1;
            p1.setX(x2);
            p1.setY(y + dx);

            p2.setX(x1 + dx);
            p2.setY(y + dx);

            p3.setX(x1 + dx);
            p3.setY(y - dx);

            p4.setX(x2);
            p4.setY(y - dx);
        }

        Line topLine = new Line(p1, p2, Color.cyan);
        lines.add(topLine);
        for (int x = p1.getX(); x < p2.getX(); x++) {
            int y = p1.getY();

            raster.setPixel(x, y, line.getColor().getRGB());
        }

        Line rightLine = new Line(p2, p3, Color.cyan);
        lines.add(rightLine);
        for(int y = p2.getY(); y > p3.getY(); y--) {
            int x = p2.getX();

            raster.setPixel(x, y, line.getColor().getRGB());
        }

        Line bottomLine = new Line(p3, p4, Color.cyan);
        lines.add(bottomLine);
        for (int x = p3.getX(); x > p4.getX(); x--) {
            int y = p3.getY();

            raster.setPixel(x, y, line.getColor().getRGB());
        }

        Line leftLine = new Line(p4, p1, Color.cyan);
        lines.add(leftLine);
        for(int y = p4.getY(); y < p1.getY(); y++) {
            int x = p4.getX();

            raster.setPixel(x, y, line.getColor().getRGB());
        }


    }

    @Override
    public void rasterizeArray(ArrayList<Line> lines) {
        for (Line line : lines) {
            rasterize(line);
        }
    }


}
