package project.algorithms;

public interface Algorithm<I, O extends Solution> {
    O run(I input);
}