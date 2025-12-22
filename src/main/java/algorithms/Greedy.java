package main.java.algorithms;

import main.java.problems.OptimizationProblem;
import main.java.selection.SelectionStrategy;

import java.util.List;

public class Greedy <E,T extends Solution> {
    private final OptimizationProblem<T> problem;
    private final SelectionStrategy<E> strategy;

    public Greedy(OptimizationProblem<T> problem, SelectionStrategy<E> strategy) {
        this.problem = problem;
        this.strategy = strategy;

    }

    public T run(List<E> elements) {
        // ...
        return null;

    }
}
