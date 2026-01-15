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
    private double initialOverlap ;
    private int cycleCounter;

    public RectanglePackingProblem(List<PackingRectangle> packingRectangles, int boxSize) {
        this.packingRectangles = packingRectangles;
        this.boxSize = boxSize;
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
        this.initialOverlap = PackingSolution.getOverlapPerSolution(solution);
        return solution;
    }

    public PackingSolution createGreedyState(){
        PackingSolution solution = new PackingSolution(boxSize);
        Box box1 = solution.createNewBox();
        return solution;
    }

    public PackingSolution createInitialSolutionOverlap(){
        PackingSolution solution = new PackingSolution(boxSize);
        Box box = solution.createNewBox();
        for (PackingRectangle r : packingRectangles) {
            Placement placement = new Placement(
                    box,
                    0,
                    0,
                    false   // keine Rotation
            );
            solution.setLastRec(r);
            solution.addPlacement(r, placement);

        }
        this.initialOverlap = Math.pow(PackingSolution.getOverlapPerSolution(solution), 0.25);
        return solution;
    }


/*
    public double evaluateOverlap(PackingSolution solution){
        double boxesScore = normalEval(solution);

        System.out.print("overlapscore");
        System.out.println(overlapScore);
        System.out.print("normal");
        System.out.println(boxesScore);
        // parameters
  // small number to control smooth transition
        double alpha = 0.2;      // weight of boxes relative to overlap

        // overlap penalty
        double overlapPenalty = Math.pow(overlapScore, 2);

        double lambda = alpha * ((double) this.iterationTracker / maxIterations);
        System.out.println(lambda);
        // small tie-breaker to prevent flat landscape early
        double tieBreaker = 1e-5 * boxesScore;

        // total score (minimization)
        double score = overlapPenalty + lambda * boxesScore+ tieBreaker;
        return score;




    }*/

    public double getOverlapThresholdBasedOnIteration(int iteration){
        double decayRate = 0.95;
        double value = initialOverlap * (double) Math.pow(decayRate, iteration + 1);
        if (value < 0.1) {
            return 0;
        }
        return value;
    }
    @Override
    public double evaluate(PackingSolution solution, boolean allowOverlap, int iteration) {
        // TODO: add penalties, eg for nearly empty boxes
        double totalScore = 0;
        double overlapScore = 0;
        double overlapWeight = 1;
        boolean allowBonus = false;

        if (allowOverlap)
        {
             overlapScore = PackingSolution.getOverlapPerSolution(solution);
            if (overlapScore == 0){
                allowBonus = true;
            }

            double maxAllowedOverlap = getOverlapThresholdBasedOnIteration(iteration);
            overlapScore = (Math.pow(overlapScore, 0.25));
            if (overlapScore > maxAllowedOverlap) {
                System.out.print("probably wont happen");
                overlapWeight +=1;
            }

        }
        if(!allowOverlap || allowBonus)
        {

        double boxCountWeight = 0.5;
        double nonOverlapScore = 0.0;
        int nRecsToIgnore = 1; // ignore very empty boxes
        double minCoverage = 0.05;
        Map<Box, Double> coverageMap = PackingSolution.computeCoverage(solution, true);
        Map<Box, Integer> nRecMap = PackingSolution.getNumberActiveRectangles(solution);


        for (Box box : nRecMap.keySet()) {
            Integer nRecs = nRecMap.get(box);
            Double coverage = coverageMap.get(box);
            // we ignore very empty boxes as to not penalize them
            if (coverage <= minCoverage) continue;
            // we like full boxes

            // penalize mid range coverage the most
            double boxPenalty = Math.abs(Math.abs(0.5 - coverage) - 0.5) * 2;
            // Math.pow(1.0 - coverage, 2) * alpha;
            nonOverlapScore += boxPenalty;
        }

        nonOverlapScore += solution.getNumberOfBoxes() * boxCountWeight;
        if (allowBonus) {
            nonOverlapScore = - 0.5*(1/nonOverlapScore);
        }
        totalScore+= overlapScore* overlapWeight;
        totalScore+=nonOverlapScore;

        System.out.println(  totalScore);

    }
        return totalScore;}

    @Override
    public boolean isFeasible(PackingSolution solution) {

        if (!solution.areRectanglesInSolution(packingRectangles)) {
            return false;
        }

        // 2. All placements are valid
        for (Map.Entry<PackingRectangle, Placement> entry : solution.getPlacements().entrySet()) {
            if (!entry.getValue().isValid(entry.getKey())) {
                return false;
            }
        }

        // 3. Non-overlapping check per box
        for (Box box : solution.getBoxes()) {

            List<PackingRectangle> rects = solution.getRectangleByBoxID(box.getID());
            int n = rects.size();
            if (n < 2) continue;

            // Cache geometry to avoid repeated method calls
            int[] x = new int[n];
            int[] y = new int[n];
            int[] w = new int[n];
            int[] h = new int[n];

            for (int i = 0; i < n; i++) {
                PackingRectangle r = rects.get(i);
                Placement p = solution.getPlacement(r);
                x[i] = p.x;
                y[i] = p.y;
                w[i] = p.getWidth(r);
                h[i] = p.getHeight(r);
            }

            // Pairwise overlap check
            for (int i = 0; i < n; i++) {
                for (int j = i + 1; j < n; j++) {
                    if (overlaps(x[i], y[i], w[i], h[i], x[j], y[j], w[j], h[j])) {
                        return false;
                    }
                }
            }
        }

        return true;

    }



    public static boolean rectangleOverlap(PackingRectangle r1, PackingRectangle r2, Placement p1, Placement p2){
        int x1 = p1.x;
        int y1 = p1.y;
        int w1 = p1.getWidth(r1);
        int h1 = p1.getHeight(r1);

        int x2 = p2.x;
        int y2 = p2.y;
        int w2 = p2.getWidth(r2);
        int h2 = p2.getHeight(r2);
        return overlaps(x1,y1,w1,h1,x2,y2,w2,h2);
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
