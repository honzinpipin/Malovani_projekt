package rasters;

import models.Line;
import models.LineCanvas;
import models.Point;
import models.SimplePolygon;
import rasterizers.BucketRasterizer;
import rasterizers.LineCanvasRasterizer;
import rasterizers.Eraser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class App {

    private final RasterBufferedImage raster;
    private final JPanel panel;
    private final LineCanvas canvas = new LineCanvas();
    private final LineCanvasRasterizer rasterizer;
    private Point point;
    private SimplePolygon polygon;

    private boolean bucketMode = false;
    private boolean squareMode = false;
    private boolean polygonMode = false;
    private boolean rectangleMode = false;
    private boolean circleMode = false;
    private boolean eraserMode = false;

    private Point lastEraserPoint = null;

    private Color currentColor = Color.cyan;
    private int currentThickness = 1;
    private String currentLineStyle = "solid";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new App(800, 600).start());
    }

    public App(int width, int height) {
        JFrame frame = new JFrame("UHK FIM PGRF : App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        raster = new RasterBufferedImage(width, height);
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                raster.repaint(g);
            }
        };
        panel.setPreferredSize(new Dimension(width, height));

        frame.add(panel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);

        rasterizer = new LineCanvasRasterizer(raster);

        createMouseAdapter();
        panel.addMouseListener(mouseAdapter);
        panel.addMouseMotionListener(mouseAdapter);

        addToolbar(frame);

        panel.setFocusable(true);
        panel.requestFocusInWindow();

        clear(0xaaaaaa);
    }

    public void start() {
        clear(0xaaaaaa);
        panel.repaint();
    }

    private void clear(int color) {
        raster.setClearColor(color);
        raster.clear();
    }

    private void createMouseAdapter() {
        mouseAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (polygonMode) {
                    // Initialize polygon if it doesn't exist
                    if (polygon == null) {
                        polygon = new SimplePolygon();
                        polygon.setColor(currentColor);
                        polygon.setThickness(currentThickness);
                        polygon.setLineStyle(currentLineStyle);
                    }

                    Point clickPoint = new Point(e.getX(), e.getY());
                    polygon.addPoint(clickPoint);

                    //když se polygon zavře, přidej do canvasu
                    if (polygon.isClosed()) {
                        polygon.addToCanvas(canvas);

                        raster.clear();
                        rasterizer.rasterizeCanvas(canvas);

                        //připrav pro další polygon
                        polygon = null;
                    } else {
                        raster.clear();
                        rasterizer.rasterizeCanvas(canvas);
                        polygon.draw(raster, rasterizer);
                    }

                    panel.repaint();
                } else if (bucketMode) {
                    Point clicked = new Point(e.getX(), e.getY());
                    BucketRasterizer bucket = new BucketRasterizer();
                    bucket.BucketFill(raster.getBufferedImage(), clicked, currentColor);
                    panel.repaint();
                    return;
                }
                //pro eraser se vytváří bod lastEraserPoint, aby zajistil plynulost gumy
                else if (eraserMode) {
                    lastEraserPoint = new Point(e.getX(), e.getY());
                    Eraser.erase(raster, lastEraserPoint, currentThickness);
                    panel.repaint();
                } else {
                    point = new Point(e.getX(), e.getY());
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                //polygon a bucket nepoužívájí mouseDragged
                if (polygonMode || bucketMode) return;

                if (eraserMode) {
                    Point current = new Point(e.getX(), e.getY());

                    // Volání erase pro více bodů, pokud se myš hýbe rychle
                    if (lastEraserPoint != null) {
                        int dx = current.getX() - lastEraserPoint.getX();
                        int dy = current.getY() - lastEraserPoint.getY();
                        int steps = Math.max(Math.abs(dx), Math.abs(dy));

                        for (int i = 0; i <= steps; i++) {
                            int x = lastEraserPoint.getX() + i * dx / steps;
                            int y = lastEraserPoint.getY() + i * dy / steps;
                            Eraser.erase(raster, new Point(x, y), currentThickness);
                        }
                    }

                    lastEraserPoint = current;
                    panel.repaint();
                    return;
                }

                Point point2 = new Point(e.getX(), e.getY());
                Line line = new Line(point, point2, currentColor);
                line.setThickness(currentThickness);
                line.setLineStyle(currentLineStyle);

                raster.clear();
                rasterizer.rasterizeCanvas(canvas);


                //podmínky pro "náhled" čar
                if (circleMode) {
                    rasterizer.rasterizeCircleLine(line);
                } else if (squareMode) {
                    rasterizer.rasterizeSquareLine(line);
                } else if (rectangleMode) {
                    rasterizer.rasterizeRectangleLine(line);
                } else {
                    switch (currentLineStyle) {
                        case "dotted":
                            rasterizer.rasterizeDottedLine(line);
                            break;
                        case "straight":
                            rasterizer.rasterizeStraightLine(line);
                            break;
                        case "dashed":
                            rasterizer.rasterizeDashedLine(line);
                            break;
                        default:
                            rasterizer.rasterizeLine(line);
                    }
                }

                panel.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                //polygon a bucket opět nevyužívájí
                if (polygonMode || bucketMode) return;

                if (eraserMode) {
                    lastEraserPoint = null;
                    return;
                }

                Point end = new Point(e.getX(), e.getY());
                Line line = new Line(point, end, currentColor);
                line.setThickness(currentThickness);
                line.setLineStyle(currentLineStyle);


                //Tady už se celá čára/objekt vykreslí
                if (squareMode) {
                    canvas.addSquareLine(line);
                } else if (rectangleMode) {
                    canvas.addRectangleLine(line);
                } else if (circleMode) {
                    canvas.addCircleLine(line);
                } else if (currentLineStyle.equals("dotted")) {
                    canvas.addDottedLine(line);
                } else if (currentLineStyle.equals("straight")) {
                    canvas.addStraightLine(line);
                }
                else if(currentLineStyle.equals("dashed")){
                    canvas.addDashedLine(line);
                }
                else {
                    canvas.addLine(line);
                }

                rasterizer.rasterizeCanvas(canvas);
                panel.repaint();
            }
        };
    }

    private MouseAdapter mouseAdapter;


    //Uživatelské rozhraní
    private void addToolbar(JFrame frame) {
        JPanel toolbar = new JPanel(new FlowLayout());

        JButton colorButton = new JButton("Barva");
        colorButton.addActionListener(e -> {
            Color selected = JColorChooser.showDialog(frame, "Zvol barvu", currentColor);
            if (selected != null) currentColor = selected;
        });

        JComboBox<Integer> thicknessBox = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});
        thicknessBox.setSelectedItem(currentThickness);
        thicknessBox.addActionListener(e -> currentThickness = (Integer) thicknessBox.getSelectedItem());

        JComboBox<String> styleBox = new JComboBox<>(new String[]{"solid", "dotted", "straight","dashed"});
        styleBox.setSelectedItem(currentLineStyle);
        styleBox.addActionListener(e -> currentLineStyle = (String) styleBox.getSelectedItem());

        JComboBox<String> shapeBox = new JComboBox<>(new String[]{"Line", "Circle", "Square", "Rectangle", "Polygon", "Bucket", "Eraser"});
        shapeBox.addActionListener(e -> {
            bucketMode = circleMode = squareMode = rectangleMode = polygonMode = eraserMode = false;

            switch ((String) shapeBox.getSelectedItem()) {
                case "Circle":
                    circleMode = true;
                    break;
                case "Square":
                    squareMode = true;
                    break;
                case "Rectangle":
                    rectangleMode = true;
                    break;
                case "Polygon":
                    polygonMode = true;
                    break;
                case "Bucket":
                    bucketMode = true;
                    break;
                case "Eraser":
                    eraserMode = true;
                    break;
            }
        });

        toolbar.add(colorButton);
        toolbar.add(new JLabel("Tloušťka:"));
        toolbar.add(thicknessBox);
        toolbar.add(new JLabel("Styl:"));
        toolbar.add(styleBox);
        toolbar.add(new JLabel("Tvar:"));
        toolbar.add(shapeBox);

        frame.add(toolbar, BorderLayout.NORTH);
    }
}
