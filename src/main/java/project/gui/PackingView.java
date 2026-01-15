package project.gui;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import project.problems.Box;
import project.problems.PackingRectangle;
import project.problems.PackingSolution;
import project.problems.Placement;


import java.util.HashMap;
import java.util.Map;

public class PackingView extends Canvas {

    private static final int CELL_SIZE = 25; // pixels per unit
    private Map<PackingRectangle, Color> rectangleColors = new HashMap<>();
    private PackingSolution currSol;
    private PackingRectangle highlightRec;

    public PackingView(int width, int height) {
        super(width, height);
        // optional: add background color
        getGraphicsContext2D().setFill(Color.WHITE);
        getGraphicsContext2D().fillRect(0, 0, width, height);
        currSol = null;
        highlightRec = null;
    }



    /**
     * Draws a full solution: all boxes and their rectangles.
     */
    public void draw(PackingSolution solution) {
        this.currSol = solution;
        int logicalWidth = solution.getBoxSize();   // e.g. 100
        int logicalHeight = solution.getBoxSize();  // or max Y used

        double canvasWidth  = logicalWidth * CELL_SIZE;
        double canvasHeight = logicalHeight * CELL_SIZE;


        redraw();

    }

    public void highlightRec(PackingRectangle rec){
        highlightRec = rec;
        redraw();

    }

    public void redraw(){
        if (currSol == null){
            return;
        }

        GraphicsContext gc = getGraphicsContext2D();
        // clear canvas
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, getWidth(), getHeight());

        PackingRectangle rec2beUsed;
        if (highlightRec == null){
            rec2beUsed = currSol.getLastRec();
        }
        else{
            rec2beUsed = highlightRec;
        }

        int offsetX = 10; // spacing between boxes
        int offsetY = 10;
        int currentX = offsetX;
        int currentY = offsetY;

        int boxSize = currSol.getBoxSize();

        for (Box box : currSol.getBoxes()) {
            drawBox(gc, box, currSol, currentX, currentY, rec2beUsed);

            // move next box to the right; wrap to new row if needed
            currentX += (boxSize * CELL_SIZE + offsetX);
            if (currentX + boxSize * CELL_SIZE > getWidth()) {
                currentX = offsetX;
                currentY += boxSize * CELL_SIZE + offsetY;
            }
        }

    }

    /**
     * Draw a single box at position (startX, startY)
     */
    private void drawBox(GraphicsContext gc, Box box, PackingSolution solution, int startX, int startY, PackingRectangle lastRec) {
        int L = solution.getBoxSize();

        // draw box outline
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.strokeRect(startX, startY, L * CELL_SIZE, L * CELL_SIZE);
        Placement currP = null;
        // draw rectangles inside this box
        for (Map.Entry<PackingRectangle, Placement> entry : solution.getPlacements().entrySet()) {
            PackingRectangle r = entry.getKey();
            Placement p = entry.getValue();
            if (r.equals(lastRec)){
                currP =p;
                continue;
            }


            if (!p.getBox().equals(box)) continue;

            int w = p.getWidth(r);
            int h = p.getHeight(r);

            // compute canvas coordinates
            int x = startX + p.getX() * CELL_SIZE;
            int y = startY + (L - p.getY() - h) * CELL_SIZE; // invert y-axis

            // get or assign color for rectangle
            //Color color = rectangleColors.computeIfAbsent(r, k -> randomColorFor(k));
            gc.setFill(Color.LIGHTGRAY);
            gc.fillRect(x, y, w * CELL_SIZE, h * CELL_SIZE);

            // draw border
            gc.setStroke(Color.BLACK);
            gc.strokeRect(x, y, w * CELL_SIZE, h * CELL_SIZE);
        }

        if (lastRec != null)
        {
         // or any bright color

            if (currP.getBox().equals(box)){

            int w = currP.getWidth(lastRec);
            int h = currP.getHeight(lastRec);

            // compute canvas coordinates
            int x = startX + currP.getX() * CELL_SIZE;
            int y = startY + (L - currP.getY() - h) * CELL_SIZE; // invert y-axis

            // get or assign color for rectangle
            //Color color = rectangleColors.computeIfAbsent(r, k -> randomColorFor(k));
            gc.setFill(Color.LIGHTBLUE);
            gc.fillRect(x, y, w * CELL_SIZE, h * CELL_SIZE);

            // draw border
            gc.setStroke(Color.BLACK);
            gc.strokeRect(x, y, w * CELL_SIZE, h * CELL_SIZE);
        }}
    }

    /**
     * Assign a stable color for each rectangle
     */
    private Color randomColorFor(PackingRectangle r) {
        int hash = r.hashCode();
        return Color.hsb(Math.abs(hash) % 360, 0.6, 0.9);
    }

    @Override
    public boolean isResizable() {
        return true;
    }

    @Override
    public double prefWidth(double height) {
        return getWidth();
    }

    @Override
    public double prefHeight(double width) {
        return getHeight();
    }
}