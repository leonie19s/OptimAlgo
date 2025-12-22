package main.java.problems;

import java.util.List;

public class RectanglePackingProblem implements OptimizationProblem<PackingSolution>{

    private final List<Rectangle> rectangles;
    // evtl placement strategy

    public RectanglePackingProblem(List<Rectangle> rectangles){
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
        // do all rectangles have a valid placement
        boolean check = solution.areRectanglesInSolution(rectangles);

        // are they non-overlapping

        return check;
    }
}
