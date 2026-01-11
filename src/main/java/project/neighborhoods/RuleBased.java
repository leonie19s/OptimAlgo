package project.neighborhoods;

import project.algorithms.Solution;
import project.problems.PackingRectangle;
import project.problems.PackingSolution;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RuleBased  implements Neighborhood<PackingSolution>  {
    private final Random random = new Random();

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
        List<PackingRectangle> currentPerm = solution.getPerm();

        RuleBasedMove move =
                RuleBasedMove.values()[random.nextInt(RuleBasedMove.values().length)];
        List<PackingRectangle> newPerm = getPerm(move,currentPerm);
        // pick random move and indices
        return solution.createNeighborFromPermutation(newPerm);
    }

    public List<PackingRectangle> getPerm(RuleBasedMove move, List<PackingRectangle>oldPerm) {
        int length = oldPerm.size();
        List<PackingRectangle> newPerm = new ArrayList<>(oldPerm);
        int i = random.nextInt(length);
        int j = random.nextInt(length);
        PackingRectangle temp = oldPerm.get(i);
        switch (move) {
            case SWAP:
                newPerm.set(i, oldPerm.get(j));
                newPerm.set(j, temp);
                return newPerm;

            case REVERSE:
                if (i >= j) return oldPerm;
                while (i < j) {
                    temp = oldPerm.get(i);
                    newPerm.set(i, oldPerm.get(j));
                    newPerm.set(j, temp);
                    i++;
                    j--;
                }

            case INSERT:
                if (i == j) return newPerm;
                PackingRectangle elem = newPerm.remove(i);
                newPerm.add(j, elem);
                return newPerm;
            default:
                System.out.println("Unexpected move rulebased: " + move);
                return null;

        }
    }


}
