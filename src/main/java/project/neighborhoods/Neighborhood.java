package project.neighborhoods;

import project.algorithms.Solution;
import project.problems.PackingSolution;

import java.util.List;

public interface Neighborhood<Solution> {
    List<Solution> generateNeighbors(Solution solution, int n);
    Solution generateNeighbor(Solution solution);
}
