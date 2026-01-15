package project.algorithms.stoppingCriteria;

import project.algorithms.Solution;
import project.problems.OptimizationProblem;

import java.util.List;

public class MaxIterations<T extends Solution<?>>
        implements StoppingCriterion<T> {

    private final int max;
    private int iterations = 0;

    public MaxIterations(int max) {
        this.max = max;
    }


    @Override
    public void update(T current, T next, boolean wasBetter) {
        iterations++;
    }

    @Override
    public boolean shouldStop() {
        return iterations >= max;
    }
}
