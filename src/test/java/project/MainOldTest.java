package project;

import org.junit.jupiter.api.Test;
import project.algorithms.Algorithm;
import project.neighborhoods.Neighborhood;
import project.problems.*;
import project.selection.SelectionStrategy;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MainOldTest {

    int boxSize = 7;
    int upperW = 4;
    int upperH = 5;
    int seed = 3;
    int nRecs = 28;
    int nStartBox = 1;
    String algorithm = "Greedy";
    String neighborhood = "GeometryBased";


    TestInstancesGenerator gen = new TestInstancesGenerator( boxSize,  upperW,  upperH,  seed,  nRecs,  nStartBox,  algorithm);
    @Test
    void GreedyTest(){
        List<PackingRectangle> recs = gen.generateRandomRecs();
        RectanglePackingProblem problem = gen.getProblem(recs);
        Neighborhood<PackingSolution> neigh = gen.getNeighborhood(neighborhood);

        String selectionStrategy = "BiggestFirst";
        SelectionStrategy<PackingRectangle> strat = gen.getSelectionStrategy(selectionStrategy);
        Algorithm<?, PackingSolution> algorithm = gen.getAlgorithm(recs, problem, neigh, strat);
        PackingSolution greedyState = problem.createGreedyState();
        PackingSolution solution = ((Algorithm<List<PackingRectangle>, PackingSolution>) algorithm).run(recs, greedyState);
        assertTrue(problem.isFeasible(solution));
        PackingRectangle largestRec = selectRectangle(recs, RectangleCriterion.AREA,true);
        PackingRectangle smallestRec = selectRectangle(recs, RectangleCriterion.AREA,false);
        // assert that largestRec is in first box, smallestRec is in last Box
        Map<PackingRectangle, Placement> finalPlacements = solution.getPlacements();
        Placement largest = finalPlacements.get(largestRec);
        Placement smallest = finalPlacements.get(smallestRec);
        List<Box> boxes = solution.getBoxes();
        for (Box box : boxes){
            System.out.println(box.getID());
        }
        int biggestBoxId = boxes.get(boxes.size()-1).getID();
        assertEquals(0, largest.getBox().getID());
        solution.printSolution(solution);

        PackingRectangle largestSidelength = selectRectangle(recs, RectangleCriterion.MAX_SIDE,true);
        PackingRectangle smallestSidelength = selectRectangle(recs, RectangleCriterion.MAX_SIDE,false);

    }
    public static PackingRectangle selectRectangle(
            List<PackingRectangle> packingRectangles,
            RectangleCriterion criterion,
            boolean selectLargest
    ) {
        if (packingRectangles == null || packingRectangles.isEmpty()) {
            throw new IllegalArgumentException("Rectangle list is empty");
        }

        Comparator<PackingRectangle> comparator;

        switch (criterion) {
            case AREA:
                comparator = Comparator.comparingInt(
                        r -> r.width * r.height
                );
                break;

            case MAX_SIDE:
                comparator = Comparator.comparingInt(
                        r -> Math.max(r.width, r.height)
                );
                break;

            default:
                throw new IllegalStateException("Unexpected criterion");
        }

        return selectLargest
                ? packingRectangles.stream().max(comparator).get()
                : packingRectangles.stream().min(comparator).get();
    }

}
