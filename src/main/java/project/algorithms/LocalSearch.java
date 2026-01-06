package project.algorithms;

import project.neighborhoods.Neighborhood;
import project.problems.OptimizationProblem;

public class LocalSearch<T extends Solution>
        implements Algorithm<Void, T> {

    // has optimizationproblem as private attribute
    private final OptimizationProblem<T> problem;

    // has neighborhood as private attribute
    private final Neighborhood<T> neighborhood;

    public LocalSearch(OptimizationProblem<T> problem, Neighborhood<T> neighborhood){
        this.problem = problem;
        this.neighborhood = neighborhood;

    }

    @Override
    public T run(Void input, T solutionState) {
        T currentSolution = solutionState;
        T neighbor = neighborhood.generateNeighbor(currentSolution);
        double neighborScore = problem.evaluate(neighbor);
        double currentScore = problem.evaluate(currentSolution);
        double bestScore = currentScore;
        // assuming minimization
        while(currentScore >= neighborScore){
            currentScore = neighborScore;
            currentSolution = neighbor;
            neighbor = neighborhood.generateNeighbor(currentSolution);
            neighborScore = problem.evaluate(neighbor);

        }
        return currentSolution;

    }
}


