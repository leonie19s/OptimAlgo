package project.algorithms.stoppingCriteria;

import project.algorithms.Solution;
import project.problems.OptimizationProblem;

import java.util.List;

public class NoImprovement<T extends Solution<?>>
        implements StoppingCriterion<T> {

    private final int max;
    private int counter = 0;
    private T best;

    public NoImprovement(int max) {
        this.max = max;
    }

    @Override
    public void init(T initialSolution) {
        best = initialSolution;
    }

    @Override
    public void update(T current, T next, boolean wasBetter)
    {
        if ( wasBetter){
            best = current;
            counter = 0;
        }

        else
        {
            counter++;
        }
    }

    @Override
    public boolean shouldStop() {
        return counter >= max;
    }
}
