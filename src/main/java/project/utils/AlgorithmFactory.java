package project.utils;

import project.algorithms.*;
import project.neighborhoods.Neighborhood;
import project.problems.OptimizationProblem;
import project.selection.SelectionStrategy;

public class AlgorithmFactory {

    // Generic factory for algorithms
    public static <I, O extends Solution<I>> Algorithm<?, O> createAlgorithm(
            String algorithmName,// optional, null if unused
            AlgoArgs<I,O> algoArgs
    ) {
        return switch (algorithmName) {
            case "LocalSearch" -> new LocalSearch<>(algoArgs.getProblem(), algoArgs.getNeighborhood(), algoArgs.getnNeigh(), algoArgs.getCriteria());
            case "Greedy" -> new Greedy<>(algoArgs.getProblem(), algoArgs.getStrategy());
            default -> throw new IllegalArgumentException("Unknown algorithm: " + algorithmName);
        };
    }
}
