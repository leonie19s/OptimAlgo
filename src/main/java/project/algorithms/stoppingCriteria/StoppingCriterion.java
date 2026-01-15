package project.algorithms.stoppingCriteria;

import project.algorithms.Solution;
import project.problems.OptimizationProblem;

import java.util.List;

public interface StoppingCriterion  <T extends Solution<?>>{


    default void init(T initialSolution){
    }

    void update(T current, T next, boolean wasBetter);

    boolean shouldStop();
}
