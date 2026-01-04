package project.problems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RectanglePackingProblem implements OptimizationProblem<PackingSolution> {

    private final List<Rectangle> rectangles;
    private final int boxSize;
    // evtl placement strategy

    public RectanglePackingProblem(List<Rectangle> rectangles, int boxSize) {

        this.rectangles = rectangles;
        this.boxSize = boxSize;
    }

    @Override
    public PackingSolution createInitialSolution() {
        // create new Box for each rectanlge
        PackingSolution solution = new PackingSolution(boxSize);

        for (Rectangle r : rectangles) {
            Box box = solution.createNewBox();

            Placement placement = new Placement(
                    box,
                    0,
                    0,
                    false   // keine Rotation
            );

            solution.addPlacement(r, placement);
        }

        return solution;
    }

    @Override
    public double evaluate(PackingSolution solution) {
        // TODO: add penalties, eg for nearly empty boxes or overlap

        return solution.getNumberOfBoxes();
    }

    @Override
    public boolean isFeasible(PackingSolution solution) {

        // do all rectangles exist in solution
        if (!solution.areRectanglesInSolution(rectangles)){
            return false;
        }
        // are all rectangles placed in a valid way within their box
        for (Map.Entry<Rectangle, Placement> entry : solution.getPlacements().entrySet()) {
            Rectangle r = entry.getKey();
            Placement p = entry.getValue();
            if (!p.isValid(r)) {
                return false;
            }
        }
        // are they non-overlapping
        ArrayList<Box> boxes = (ArrayList<Box>) solution.getBoxes();
            for (Box box : boxes)
                {
                try {
                    List<Rectangle> rectanglesInThisBox = solution.getRectangleByBoxID(box.getID());
                    for (int i = 0; i < rectanglesInThisBox.size(); i++) {
                        Rectangle r1 = rectanglesInThisBox.get(i);
                        int h1 = r1.getHeight();
                        int w1 = r1.getWidth();
                        Placement p1 = solution.getPlacement(r1);
                        int x1 = p1.x;
                        int y1 = p1.y;

                        for (int j = i+1; j < rectanglesInThisBox.size(); j++)
                        {
                            Rectangle r2 = rectanglesInThisBox.get(j);
                            Placement p2 = solution.getPlacement(r2);
                            int h2 = r2.getHeight();
                            int w2 = r2.getWidth();
                            int x2 = p2.x;
                            int y2 = p2.y;
                            if (overlaps(x1,y1,w1,h1,x2,y2,w2,h2)){
                                return false;
                            }

                        }
                    }

                }
                catch (Exception e)
                {
                    System.out.println("Invalid Box ID");
                }
                }
        return true;
    }

    public static boolean overlaps(int x1, int y1, int w1, int h1, int x2, int y2, int w2, int h2){
        // offen disjunkt, ansonsten <= und >=
        return x1 < x2 + w2 &&
                        x1 + w1 > x2 &&
                        y1 < y2 + h2 &&
                        y1 + h1 > y2;
            }
    /*
    Accepts a PackingSolution, checks if all rectangles are placed in a valid way within the boxes
     */
    public boolean areRectanglesWithinBoxLength(PackingSolution solution) {
        HashMap<Rectangle, Placement> placements = (HashMap<Rectangle, Placement>) solution.getPlacements();

        for (Rectangle rec : rectangles) {
            Placement placement = placements.get(rec);
            boolean check = placement.isValid(rec);
            if (!check) {
                return false;
            }
        }
        return true;
    }
}
