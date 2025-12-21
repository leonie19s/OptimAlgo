package main.java.neighborhoods;

import main.java.algorithms.Solution;

import java.util.List;

public interface Neighborhood<T extends Solution> {
    List<T> generateNeighbors(T solution);
}
