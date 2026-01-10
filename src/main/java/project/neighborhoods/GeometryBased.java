package project.neighborhoods;

import project.problems.Box;
import project.problems.PackingSolution;
import project.problems.PackingRectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GeometryBased implements Neighborhood<PackingSolution> {

    // design choices
    int mindx = 1;
    int maxdx = 4;
    int mindy = 1;
    int maxdy = 4;
    private final Random random = new Random();


    // here we create moves (move, rotation)
    // they are assessed and accepted/rejected by the algorithm (greedy, localsearch)
    @Override
    public List<PackingSolution> generateNeighbors(PackingSolution solution, int n) {
        List<PackingSolution> neighbors = new ArrayList<>();
        for (int i = 0; i <n; i++){
            neighbors.add(generateNeighbor(solution));
        }
        return neighbors;

    }

    @Override
    public PackingSolution generateNeighbor(PackingSolution solution) {
        PackingRectangle randRec = solution.getRandomRectangle(random);
        GeometryMove move =
                GeometryMove.values()[random.nextInt(GeometryMove.values().length)];

        return applyMove(move, solution, randRec,random );

    }

    public PackingSolution applyMove(GeometryMove move, PackingSolution solution, PackingRectangle rec, Random random){

        switch (move)
        {
            case TRANSLATE:
                int dx = random.nextInt(maxdx-mindx +1) + mindx;
                int dy = random.nextInt(maxdy-mindy+1) + mindy;
                return solution.createNeighborByMove(rec,dx,dy);

            case ROTATE:
                return solution.createNeighborByRotate(rec);

            case CHANGE_BOX:
                List<Integer> allBoxIDs = solution.getBoxIds();
                Box srcBox = solution.getBoxOfRectangle(rec);
                allBoxIDs.remove(srcBox.getID());
                int targetIdx = random.nextInt(allBoxIDs.size());
                return solution.createNeighborByBoxSwitch(rec, targetIdx, true, random);

            default:
                System.out.println("Unexpected GeometryMove: " + move);
                return null;


    }}

}
