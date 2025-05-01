package models;

import java.util.ArrayList;

public class LineCanvas {

    private ArrayList<Line> lines;

    private ArrayList<Line> dottedLines;

    private ArrayList<Line> straightLines;

    private ArrayList<Line> squareLines;

    public LineCanvas() {

        this.lines = new ArrayList<>();
        this.dottedLines = new ArrayList<>();
        this.straightLines = new ArrayList<>();
        this.squareLines = new ArrayList<>();
    }

    public void addLine(Line line) {
        this.lines.add(line);
    }

    public void addDottedLine(Line line) {this.dottedLines.add(line);}

    public void addStraightLine(Line line) {this.straightLines.add(line);}

    public void addSquareLine(Line line) {this.squareLines.add(line);}

    public void clearLines() {
        this.lines.clear();
    }

    public void clearDottedLines() {this.dottedLines.clear();}

    public void clearStraightLines() {this.straightLines.clear();}

    public void clearSquareLines() {this.squareLines.clear();}

    public ArrayList<Line> getLines() {
        return lines;
    }

    public ArrayList<Line> getDottedLines() {return dottedLines;}

    public ArrayList<Line> getStraightLines() {return straightLines;}

    public ArrayList<Line> getSquareLines() {return squareLines;}

    public void ClearAllLines() {
        this.lines.clear();
        this.dottedLines.clear();
        this.straightLines.clear();
        this.squareLines.clear();
    }
}