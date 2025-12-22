package main.java.problems;

public class RectanglePackingProblem implements OptimizationProblem<PackingSolution>{

    @Override
    public PackingSolution createInitialSolution() {
        return null;
    }

    @Override
    public double evaluate(PackingSolution solution) {
        return 0;
    }

    @Override
    public boolean isFeasible(PackingSolution solution) {
        return false;
    }
}
