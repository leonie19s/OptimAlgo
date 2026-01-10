package project.algorithms;

import project.problems.OptimizationProblem;
import project.selection.SelectionStrategy;

import java.util.List;

public class Greedy<E, T extends Solution<E>>
        implements Algorithm<List<E>, T>  {
    private final OptimizationProblem<T> problem;
    private final SelectionStrategy<E> strategy;
    private List<E> elems;
    private boolean isFinished;
    private T state;

    public Greedy(OptimizationProblem<T> problem, SelectionStrategy<E> strategy) {
        this.problem = problem;
        this.strategy = strategy;
        this.elems = null;
        this.isFinished = false;
        this.state = null;




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

    @Override
    public void initialize(List<E> elements, T solutionState){
        this.elems = strategy.orderElements(elements);
        this.isFinished= false;
        this.state = solutionState;


    }

    @Override
    public boolean hasNext() {
        return !isFinished;
    }

    @Override
    public T next() {

        if (elems.size() == 1)
        {
            this.isFinished = true;
        }
        E currElem = elems.remove(0);
        state.applyChange(currElem);
        assert problem.isFeasible(state);
        return state;
    }

    @Override
    public T getCurrent() {
        return this.state;
    }
}
