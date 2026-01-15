package project.algorithms;

import project.algorithms.stoppingCriteria.StoppingCriterion;
import project.neighborhoods.Neighborhood;
import project.problems.OptimizationProblem;

import java.util.List;

public class LocalSearch<T extends Solution<?>>
        implements Algorithm<Void, T>, Steppable<T> {

    // has optimizationproblem as private attribute
    private final OptimizationProblem<T> problem;

    // has neighborhood as private attribute
    private final Neighborhood<T> neighborhood;
    private int nNeigh;
    private final List<StoppingCriterion<T>> criteria;
    private T current;
    private boolean finished = false;
    private int iteration;


    public LocalSearch(OptimizationProblem<T> problem, Neighborhood<T> neighborhood, int nNeigh, List<StoppingCriterion<T>> criteria){
        this.problem = problem;
        this.neighborhood = neighborhood;
        this.nNeigh = nNeigh;
        this.criteria = criteria;
        this.iteration = 0;

    }


    @Override
    public T run(Void input, T solutionState) {

        current = solutionState;
        T neighbor = neighborhood.generateNeighbor(current);
        // assuming minimization
        while(better(current,neighbor)){
            current = neighbor;
            neighbor = neighborhood.generateNeighbor(current);
        }
        return current;

    }

    @Override
    public void initialize(Void input, T solutionState) {
        this.current = solutionState;
        //assert this.problem.isFeasible(solutionState);

    }

    public boolean neighborBetter(double currentScore, T neighbor){
        double neighScore = problem.evaluate(neighbor, neighborhood.allowsOverlap(), this.iteration);
        return neighScore <= currentScore;
    }
    public boolean better(T currentSolution, T neighbor){

        double currScore = problem.evaluate(currentSolution, neighborhood.allowsOverlap(), this.iteration);
        double neighScore = problem.evaluate(neighbor, neighborhood.allowsOverlap(), this.iteration);
        return neighScore <= currScore;

    }
    @Override
    public boolean hasNext() {
        return !finished;
    }

    @Override
    public T getCurrent(){
        return current;
    }

    @Override
    public T next() {
        List<T> neighbor = neighborhood.generateNeighbors(current,nNeigh);
        this.iteration++;
        // there are no more valid neighbors
        if (neighbor.isEmpty()){
            finished = true;
            return current;
        }
        boolean wasBetter = false;
        T previous = current;
        T bestNeigh = current;
        double bestScore = problem.evaluate(current, neighborhood.allowsOverlap(), this.iteration);


        for (T n : neighbor) {

            if (neighborBetter(bestScore, n)) {
                wasBetter = true;
                bestNeigh = n;
                break;
            }
        }
        if(wasBetter)
        {
            current= bestNeigh;

        }

        for (StoppingCriterion<T> c : criteria)
        {
            c.update(previous, current, wasBetter);
            if (c.shouldStop()) {
                if(!neighborhood.allowsOverlap() || problem.isFeasible(current)) {
                    finished = true;
                }
            }
        }
        //assert problem.isFeasible(current);

        return current;
    }
}


