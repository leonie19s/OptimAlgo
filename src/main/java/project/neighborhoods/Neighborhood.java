package project.neighborhoods;

import project.algorithms.Solution;

import java.util.List;

public interface Neighborhood<T extends Solution> {
    List<T> generateNeighbors(T solution, int n);
    T generateNeighbor(T solution);
}
