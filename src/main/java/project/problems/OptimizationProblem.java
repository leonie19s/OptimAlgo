package project.problems;

import project.algorithms.Solution;

public interface OptimizationProblem<T extends Solution>
{
    T createInitialSolution();
    double evaluate(T solution, boolean allowOverlap, int iteration); // this is the objective function, given a solution, evaluate how good it is
    // ranges?, for packing problem-> gives integer number of boxes
    // can also handle penalizations for overlapping, temporary regelverletzung,
    // or rewards for (heuristically!) nearly empty boxes, good intermediate states
    boolean isFeasible(T solution);

}
