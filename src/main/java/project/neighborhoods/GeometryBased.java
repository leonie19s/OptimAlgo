package project.neighborhoods;

import project.problems.PackingSolution;

import java.util.List;

public class GeometryBased implements Neighborhood<PackingSolution> {
    // here we create moves (move, rotation)
    // they are assessed and accepted/rejected by the algorithm (greedy, localsearch)
    @Override
    public List<PackingSolution> generateNeighbors(PackingSolution solution) {
        return List.of();
    }

}
