package project.neighborhoods;

import project.problems.PackingSolution;

import java.util.List;

public class RuleBased implements Neighborhood<PackingSolution>  {
    @Override
    public List<PackingSolution> generateNeighbors(PackingSolution solution, int n) {
        return List.of();
    }

    @Override
    public PackingSolution generateNeighbor(PackingSolution solution) {
        return null;
    }
}
