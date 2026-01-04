package project.algorithms;

import project.problems.OptimizationProblem;
import project.selection.SelectionStrategy;

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
        return problem.createInitialSolution();

    }
}
