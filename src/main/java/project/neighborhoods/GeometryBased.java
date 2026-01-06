package project.neighborhoods;

import project.problems.Box;
import project.problems.PackingSolution;
import project.problems.Rectangle;

import java.util.List;
import java.util.Random;

public class GeometryBased implements Neighborhood<PackingSolution> {

    // design choices
    int mindx = 1;
    int maxdx = 4;
    int mindy = 1;
    int maxdy = 4;


    // here we create moves (move, rotation)
    // they are assessed and accepted/rejected by the algorithm (greedy, localsearch)
    @Override
    public List<PackingSolution> generateNeighbors(PackingSolution solution) {
        return List.of();
    }

    @Override
    public PackingSolution generateNeighbor(PackingSolution solution) {
        Random random = new Random(123);// todo read seed from config prop
        Rectangle randRec = solution.getRandomRectangle(random);
        GeometryMove move =
                GeometryMove.values()[random.nextInt(GeometryMove.values().length)];
        System.out.println(move.toString());
        solution.printSolution(solution);
        return applyMove(move, solution, randRec,random );

    }

    public PackingSolution applyMove(GeometryMove move, PackingSolution solution, Rectangle rec, Random random){
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
                return solution.createNeighborByBoxSwitch(rec, targetIdx, false, random);

            default:
                System.out.println("Unexpected GeometryMove: " + move);
                return null;


    }}

}
