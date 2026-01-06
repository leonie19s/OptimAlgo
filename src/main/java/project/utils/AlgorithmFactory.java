package project.utils;

import project.algorithms.Algorithm;
import project.algorithms.Greedy;
import project.algorithms.LocalSearch;
import project.algorithms.Solution;
import project.neighborhoods.Neighborhood;
import project.problems.OptimizationProblem;
import project.selection.SelectionStrategy;

public class AlgorithmFactory {

    // Generic factory for algorithms
    public static <I, O extends Solution<I>> Algorithm<?, O> createAlgorithm(
            String algorithmName,
            OptimizationProblem<O> problem,
            Neighborhood<O> neighborhood,          // optional, null if unused
            SelectionStrategy<I> strategy          // optional, null if unused
    ) {
        return switch (algorithmName) {
            case "LocalSearch" -> new LocalSearch<>(problem, neighborhood);
            case "Greedy" -> new Greedy<>(problem, strategy);
            default -> throw new IllegalArgumentException("Unknown algorithm: " + algorithmName);
        };
    }
}
