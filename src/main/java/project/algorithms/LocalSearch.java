package project.algorithms;

import project.neighborhoods.Neighborhood;
import project.problems.OptimizationProblem;

public class LocalSearch <T extends Solution> {

    // has optimizationproblem as private attribute
    private final OptimizationProblem<T> problem;

    // has neighborhood as private attribute
    private final Neighborhood<T> neighborhood;

    public LocalSearch(OptimizationProblem<T> problem, Neighborhood<T> neighborhood){
        this.problem = problem;
        this.neighborhood = neighborhood;

    }
    // method run
    public T run(){
        return problem.createInitialSolution();
    }
}


