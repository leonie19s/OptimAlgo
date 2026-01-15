package project.algorithms;

import project.algorithms.stoppingCriteria.StoppingCriterion;
import project.neighborhoods.Neighborhood;
import project.problems.OptimizationProblem;
import project.selection.SelectionStrategy;

import java.util.List;

public class AlgoArgs<E, T extends Solution<E>> {

     private OptimizationProblem<T> problem;
     private SelectionStrategy<E> strategy;
     private int nNeigh;
     private List<StoppingCriterion<T>> criteria;
     private Neighborhood<T> neighborhood;
     private int nRecs;

    public AlgoArgs() {
        this.strategy = null;
        this.problem = null;
        this.nNeigh = 1;
        this.criteria = null;
        this.neighborhood = null;
        this.nRecs=0;
    }

    public void setNeighborhood(Neighborhood<T> neighborhood){
        this.neighborhood = neighborhood;
    }
    public void setnRecs(int n){
        this.nRecs = n;
    }
    public int getnRecs(){
        return this.nRecs;
    }
    public Neighborhood<T> getNeighborhood(){
        return this.neighborhood;
    }
    public void setProblem(OptimizationProblem<T> problem){
        this.problem = problem;
    }
    public OptimizationProblem<T> getProblem(){
        return this.problem;
    }

    public void setStrategy(SelectionStrategy<E> strategy){
        this.strategy = strategy;
    }

    public SelectionStrategy<E> getStrategy(){
        return this.strategy;
    }

    public void setnNeigh(int n){
        this.nNeigh = n;
    }

    public int getnNeigh(){
        return this.nNeigh;
    }
    public void setCriteria(List<StoppingCriterion<T>> criteria){
        this.criteria = criteria;
    }

    public List<StoppingCriterion<T>> getCriteria(){
        return this.criteria;
    }


}
