package rasterizers;

import models.Line;
import models.LineCanvas;
import rasters.Raster;

public class LineCanvasRasterizer {

    private Raster raster;

    private Rasterizer lineRasterizer;
    private Rasterizer dottedLineRasterizer;
    private Rasterizer straightLineRasterizer;
    private Rasterizer squareLineRasterizer;

    public LineCanvasRasterizer(Raster raster) {
        this.raster = raster;

        lineRasterizer = new LineRasterizerTrivial(raster);
        dottedLineRasterizer = new DottedLineRasterizer(raster);
        straightLineRasterizer = new StraightLineRasterizer(raster);
        squareLineRasterizer = new SquareRasterizer(raster);
    }

    public void rasterizeCanvas(LineCanvas canvas) {
        lineRasterizer.rasterizeArray(canvas.getLines());
        dottedLineRasterizer.rasterizeArray(canvas.getDottedLines());
        straightLineRasterizer.rasterizeArray(canvas.getStraightLines());
        squareLineRasterizer.rasterizeArray(canvas.getSquareLines());
    }

    public void rasterizeLine(Line line) {
        lineRasterizer.rasterize(line);
    }

    public void rasterizeDottedLine(Line line) {
        dottedLineRasterizer.rasterize(line);
    }

    public void rasterizeStraightLine(Line line) {straightLineRasterizer.rasterize(line);}

    public void rasterizeSquareLine(Line line) { squareLineRasterizer.rasterize(line);}
}