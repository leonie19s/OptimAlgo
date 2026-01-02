package main.java.problems;

import java.util.HashMap;
import java.util.List;

public class RectanglePackingProblem implements OptimizationProblem<PackingSolution> {

    private final List<Rectangle> rectangles;
    // evtl placement strategy

    public RectanglePackingProblem(List<Rectangle> rectangles) {
        this.rectangles = rectangles;
    }

    @Override
    public PackingSolution createInitialSolution() {
        // random
        // or each Rectangle own box
        return null;
    }

    @Override
    public double evaluate(PackingSolution solution) {
        // TODO: add penalties, eg for nearly empty boxes or overlap

        return solution.getNumberOfBoxes();
    }

    @Override
    public boolean isFeasible(PackingSolution solution) {
        // do all rectangles exist in solution
        boolean check = solution.areRectanglesInSolution(rectangles);

        // are all rectangle placements valid
        // are they non-overlapping

        return check;
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
