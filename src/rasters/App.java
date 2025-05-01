package rasters;

import models.Line;
import models.LineCanvas;
import models.Point;
import rasterizers.LineCanvasRasterizer;
import rasterizers.Rasterizer;
import rasterizers.LineRasterizerTrivial;
import rasters.Raster;
import rasters.RasterBufferedImage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.Serial;

public class App {

    private final JPanel panel;
    private final Raster raster;
    private MouseAdapter mouseAdapter;
    private KeyAdapter keyAdapter;
    private Point point;
    private LineCanvasRasterizer rasterizer;
    private LineCanvas canvas;
    private boolean ctrlMode = false;
    private boolean shiftMode = false;
    private boolean squareMode = false;


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new App(800, 600).start());
    }

    public void clear(int color) {
        raster.setClearColor(color);
        raster.clear();
    }

    public void present(Graphics graphics) {
        raster.repaint(graphics);
    }

    public void start() {
        clear(0xaaaaaa);
        panel.repaint();
    }

    public App(int width, int height) {
        JFrame frame = new JFrame();

        frame.setLayout(new BorderLayout());

        frame.setTitle("UHK FIM PGRF : " + this.getClass().getName());
        frame.setResizable(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        raster = new RasterBufferedImage(width, height);

        panel = new JPanel() {
            @Serial
            private static final long serialVersionUID = 1L;

            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                present(g);
            }
        };
        panel.setPreferredSize(new Dimension(width, height));

        frame.add(panel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);

        rasterizer = new LineCanvasRasterizer(raster);
        canvas = new LineCanvas();

        createAdapters();
        panel.addMouseListener(mouseAdapter);
        panel.addMouseMotionListener(mouseAdapter);
        panel.addKeyListener(keyAdapter);

        panel.requestFocus();
        panel.requestFocusInWindow();
    }

    private void createAdapters() {
        mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                point = new Point(e.getX(), e.getY());
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                Point point2 = new Point(e.getX(), e.getY());


                checkBorder(point2);

                Line line = new Line(point, point2, Color.cyan);

                if (ctrlMode) {
                    canvas.addDottedLine(line);
                } else if (shiftMode) {
                    canvas.addStraightLine(line);
                }else if(squareMode) {
                    canvas.addSquareLine(line);
                }

                else {
                    canvas.addLine(line);
                }

                rasterizer.rasterizeCanvas(canvas);
                panel.repaint();

            }

            @Override
            public void mouseDragged(MouseEvent e) {
                Point point2 = new Point(e.getX(), e.getY());
                checkBorder(point2);
                Line line = new Line(point, point2, Color.cyan);

                raster.clear();


                rasterizer.rasterizeCanvas(canvas);
                if (ctrlMode) {
                    rasterizer.rasterizeDottedLine(line);
                } else if (shiftMode) {
                    checkBorder(point2);
                    rasterizer.rasterizeStraightLine(line);
                }
                else if(squareMode) {
                    rasterizer.rasterizeSquareLine(line);
                }
                else {
                    rasterizer.rasterizeLine(line);
                }

                panel.repaint();
            }
        };
        keyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
                    ctrlMode = true;
                }
                else if(e.getKeyCode() == KeyEvent.VK_SHIFT) {
                    shiftMode = true;
                }
                else if(e.getKeyCode() == KeyEvent.VK_C){
                    canvas.ClearAllLines();
                    raster.clear();
                    panel.repaint();
                }
                else if(e.getKeyCode() == KeyEvent.VK_S){
                    squareMode = true;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
                    ctrlMode = false;
                }
                else if(e.getKeyCode() == KeyEvent.VK_SHIFT) {
                    shiftMode = false;
                }
                else if(e.getKeyCode() == KeyEvent.VK_S){
                    squareMode = false;
                }
            }
        };



    }
    // line > šířka/výška rasteru = mimo
    //line < 1 = mimo
    private Point checkBorder(Point point){
        if(point.getX() >= raster.getWidth() && point.getY() >= raster.getHeight()) {
            point.setX(raster.getWidth() - 1);
            point.setY(raster.getHeight() - 1);
        }
        else if(point.getX() >= raster.getWidth() && point.getY() <= 1){
            point.setX(raster.getWidth() - 1);
            point.setY(2);
        }
        else if(point.getX() <= 1 && point.getY() >= raster.getHeight()){
            point.setX(2);
            point.setY(raster.getHeight() - 1);
        }
        else if(point.getX() <= 1 && point.getY() <= 1){
            point.setX(2);
            point.setY(2);
        }
        else if (point.getX() >= raster.getWidth()) {
            point.setX(raster.getWidth() - 1);
        } else if (point.getY() >= raster.getHeight()) {
            point.setY(raster.getHeight() - 1);
        }
        else if(point.getX() <= 1 && point.getY() <= 1) {
            point.setX(2);
            point.setY(2);
        }
        else if(point.getX() <= 1){
            point.setX(2);
        }
        else if(point.getY() <= 1){
            point.setY(2);
        }
        return point;
    }

}