package rasters;

import models.Line;
import models.LineCanvas;
import models.Point;
import models.SimplePolygon;
import rasterizers.BucketRasterizer;
import rasterizers.LineCanvasRasterizer;
import rasterizers.Rasterizer;

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

    private Color currentColor = Color.cyan;
    private int currentThickness = 1;
    private String currentLineStyle = "solid"; // solid, dotted, straight

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
                    if (polygon == null) polygon = new SimplePolygon();
                    Point p = new Point(e.getX(), e.getY());

                    if (polygon.isNearFirstPoint(p) && polygon.getPoints().size() > 3) {
                        polygon.setClosed(true);
                        Point last = polygon.getLastPoint();
                        Point first = polygon.getFirstPoint();
                        Line closing = new Line(last, first, currentColor);
                        closing.setThickness(currentThickness);
                        closing.setLineStyle(currentLineStyle);
                        canvas.addLine(closing);
                        rasterizer.rasterizeCanvas(canvas);
                        polygon.clear();
                        panel.repaint();
                    } else {
                        Point last = polygon.getLastPoint();
                        polygon.addPoint(p);
                        if (last != null) {
                            Line line = new Line(last, p, currentColor);
                            line.setThickness(currentThickness);
                            line.setLineStyle(currentLineStyle);
                            canvas.addLine(line);
                            rasterizer.rasterizeCanvas(canvas);
                            panel.repaint();
                        }
                    }
                } else if (bucketMode) {
                    new BucketRasterizer().BucketFill(raster.getImg(), new Point(e.getX(), e.getY()), currentColor);
                    rasterizer.rasterizeCanvas(canvas);
                    panel.repaint();
                } else {
                    point = new Point(e.getX(), e.getY());
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (polygonMode || bucketMode) return;

                Point point2 = new Point(e.getX(), e.getY());
                Line line = new Line(point, point2, currentColor);
                line.setThickness(currentThickness);
                line.setLineStyle(currentLineStyle);

                raster.clear();
                rasterizer.rasterizeCanvas(canvas);

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
                        default:
                            rasterizer.rasterizeLine(line);
                    }
                }

                panel.repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (polygonMode || bucketMode) return;

                Point end = new Point(e.getX(), e.getY());
                Line line = new Line(point, end, currentColor);
                line.setThickness(currentThickness);
                line.setLineStyle(currentLineStyle);

                if (squareMode) {
                    canvas.addSquareLine(line);
                } else if (rectangleMode) {
                    canvas.addRectangleLine(line);
                } else if (circleMode) {
                    canvas.addCircleLine(line);
                }
                else if(currentLineStyle.equals("dotted")){
                    canvas.addDottedLine(line);
                }
                else if(currentLineStyle.equals("straight")){
                    canvas.addStraightLine(line);
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

        JComboBox<String> styleBox = new JComboBox<>(new String[]{"solid", "dotted", "straight"});
        styleBox.setSelectedItem(currentLineStyle);
        styleBox.addActionListener(e -> currentLineStyle = (String) styleBox.getSelectedItem());

        JComboBox<String> shapeBox = new JComboBox<>(new String[]{"Line", "Circle", "Square", "Rectangle", "Polygon", "Bucket"});
        shapeBox.addActionListener(e -> {
            // Reset all modes
            bucketMode = circleMode = squareMode = rectangleMode = polygonMode = false;

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