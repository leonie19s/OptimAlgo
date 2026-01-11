package project.problems;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RectanglePackingProblem implements OptimizationProblem<PackingSolution> {

    private final List<PackingRectangle> packingRectangles;
    private final int boxSize;
    public List<Box> initialBoxes;
    private int iterationTracker;
    // evtl placement strategy

    public RectanglePackingProblem(List<PackingRectangle> packingRectangles, int boxSize) {

        this.packingRectangles = packingRectangles;
        this.boxSize = boxSize;
        this.iterationTracker = 0;

    }

    public List<PackingRectangle> getRectangles() {
        return packingRectangles;
    }

    @Override
    public PackingSolution createInitialSolution() {
        // create new Box for each rectanlge
        PackingSolution solution = new PackingSolution(boxSize);

        for (PackingRectangle r : packingRectangles) {
            Box box = solution.createNewBox();

            Placement placement = new Placement(
                    box,
                    0,
                    0,
                    false   // keine Rotation
            );
            solution.setLastRec(r);

            solution.addPlacement(r, placement);
        }

        return solution;
    }

    public PackingSolution createGreedyState(){
        PackingSolution solution = new PackingSolution(boxSize);
        Box box1 = solution.createNewBox();
        return solution;
    }

    @Override
    public double evaluate(PackingSolution solution) {
        // TODO: add penalties, eg for nearly empty boxes
        double boxWeight = 0.75;
        Map<Box, Double> coverageMap = PackingSolution.computeCoverage(solution, true);

        double totalScore = 0.0;

        for (Double c : coverageMap.values()) {
            double boxScore;

            if (c >= 0.75) {
                boxScore = 0.0;           // near full coverage → best
            } else if (c <= 0.25) {
                boxScore = 0.3;           // low coverage → a bit worse
            } else {
                boxScore = 1.0;           // medium coverage → worst
            }

            totalScore += boxScore;
        }

        // include number of boxes (fewer is better)
        double boxEval = solution.getNumberOfBoxes() * boxWeight;

        // total evaluation: smaller = better
        return totalScore + boxEval;
    }

    @Override
    public boolean isFeasible(PackingSolution solution) {
        // TODO fix invalid usage of rectangle get width!!
        // do all rectangles exist in solution
        if (!solution.areRectanglesInSolution(packingRectangles)){
            return false;
        }
        // are all rectangles placed in a valid way within their box
        for (Map.Entry<PackingRectangle, Placement> entry : solution.getPlacements().entrySet()) {
            PackingRectangle r = entry.getKey();
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
                    List<PackingRectangle> rectanglesInThisBoxes = solution.getRectangleByBoxID(box.getID());
                    for (int i = 0; i < rectanglesInThisBoxes.size(); i++) {
                        PackingRectangle r1 = rectanglesInThisBoxes.get(i);

                        Placement p1 = solution.getPlacement(r1);
                        int h1 = p1.getHeight(r1);
                        int w1 = p1.getWidth(r1);
                        int x1 = p1.x;
                        int y1 = p1.y;

                        for (int j = i+1; j < rectanglesInThisBoxes.size(); j++)
                        {
                            PackingRectangle r2 = rectanglesInThisBoxes.get(j);
                            Placement p2 = solution.getPlacement(r2);

                            int x2 = p2.x;
                            int y2 = p2.y;
                            int h2 = p2.getHeight(r2);
                            int w2 = p2.getWidth(r2);
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
        HashMap<PackingRectangle, Placement> placements = (HashMap<PackingRectangle, Placement>) solution.getPlacements();

        for (PackingRectangle rec : packingRectangles) {
            Placement placement = placements.get(rec);
            boolean check = placement.isValid(rec);
            if (!check) {
                return false;
            }
        }
        return true;
    }
}
