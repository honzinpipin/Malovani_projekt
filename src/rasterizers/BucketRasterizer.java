package rasterizers;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Stack;
import models.Point;

public class BucketRasterizer {

    public void BucketFill(BufferedImage image, Point startPoint, Color newColor) {
        int width = image.getWidth();
        int height = image.getHeight();
        int targetRGB = image.getRGB(startPoint.getX(), startPoint.getY());
        int fillRGB = newColor.getRGB();

        if (targetRGB == fillRGB) return;

        Stack<Point> stack = new Stack<>();
        stack.push(startPoint);

        // Všech 8 směrů
        int[] dx = {-1, -1, 0, 1, 1, 1, 0, -1};
        int[] dy = {0, -1, -1, -1, 0, 1, 1, 1};

        while (!stack.isEmpty()) {
            Point point = stack.pop();
            int x = point.getX();
            int y = point.getY();

            //podmínka abych nevyšel z rasteru
            if (x < 0 || x >= width || y < 0 || y >= height) continue;
            if (image.getRGB(x, y) != targetRGB) continue;

            image.setRGB(x, y, fillRGB);

            for (int i = 0; i < 8; i++) {
                int newX = x + dx[i];
                int newY = y + dy[i];
                stack.push(new Point(newX, newY));
            }
        }
    }
}
