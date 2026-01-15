package project.neighborhoods;

import project.problems.Box;
import project.problems.PackingSolution;
import project.problems.PackingRectangle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GeometryBased implements Neighborhood<PackingSolution> {
    private final Random random = new Random();

    @Override
    public List<PackingSolution> generateNeighbors(PackingSolution solution, int n) {
        int origBoxes = solution.getNumberOfBoxes();
        List<PackingSolution> neighbors = new ArrayList<>();

        for (int i = 0; i <n; i++){
            PackingSolution newSol = generateNeighbor(solution);
            if (newSol == null){
                continue;
            }
            neighbors.add(newSol);
            if (newSol.getNumberOfBoxes() < origBoxes){
                Collections.reverse(neighbors);
                return neighbors;
            }

        }
        return neighbors;

    }

    @Override
    public PackingSolution generateNeighbor(PackingSolution solution) {
        PackingRectangle randRec = solution.getRandomRectangle(random);
        return solution.createNeighborByBoxSwitch(randRec, random.nextBoolean(), random);

    }

    @Override
    public boolean allowsOverlap() {
        return false;
    }


}
