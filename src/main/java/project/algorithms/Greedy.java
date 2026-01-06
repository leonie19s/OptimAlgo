package project.algorithms;

import project.problems.OptimizationProblem;
import project.selection.SelectionStrategy;

import java.util.List;

public class Greedy<E, T extends Solution<E>>
        implements Algorithm<List<E>, T>  {
    private final OptimizationProblem<T> problem;
    private final SelectionStrategy<E> strategy;

    public Greedy(OptimizationProblem<T> problem, SelectionStrategy<E> strategy) {
        this.problem = problem;
        this.strategy = strategy;

    }
    @Override
    public T run(List<E> elements, T solutionState) {
        List<E> orderedElems = strategy.orderElements(elements);
        for(E elem : orderedElems)
        {
            solutionState.applyChange(elem);
        }
        assert problem.isFeasible(solutionState);
        return solutionState;

    }


}
