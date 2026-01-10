package project.algorithms;

public interface Algorithm<I, O extends Solution> extends Steppable<O> {
    O run(I input, O solutionState);
    void initialize(I input, O solutionState);

}