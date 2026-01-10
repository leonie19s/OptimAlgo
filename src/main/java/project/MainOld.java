package project;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import project.algorithms.AlgoArgs;
import project.algorithms.Algorithm;
import project.algorithms.Greedy;
import project.algorithms.LocalSearch;
import project.algorithms.stoppingCriteria.StoppingCriterion;
import project.neighborhoods.Neighborhood;
import project.problems.PackingSolution;
import project.problems.PackingRectangle;
import project.problems.RectanglePackingProblem;
import project.selection.SelectionStrategy;
import project.utils.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class MainOld {
    public static void main2(String[] args) {

        try
        {
            Properties config = ConfigLoader.load("src/main/resources/config.properties");
            int upperLimW = Integer.parseInt(config.getProperty("rec.upperLimW"));
            int lowerLimW = Integer.parseInt(config.getProperty("rec.lowerLimW"));
            int L = Integer.parseInt(config.getProperty("boxSize"));
            int upperLimH = Integer.parseInt(config.getProperty("rec.upperLimH"));
            int lowerLimH = Integer.parseInt(config.getProperty("rec.lowerLimH"));
            int n = Integer.parseInt(config.getProperty("rec.N"));
            int seed = Integer.parseInt(config.getProperty("seed"));
            int nNeigh = Integer.parseInt(config.getProperty("nNeigh"));
            String algoName = config.getProperty("algorithm");
            String selectionStrategy = config.getProperty("selectionStrategy");
            String neighborhood = config.getProperty("neighborhood");
            String criteria = config.getProperty("criteria");
            InstanceGenerator gen = new InstanceGenerator(L, n,upperLimW, lowerLimW, upperLimH, lowerLimH, seed);
            RectanglePackingProblem problem = gen.generate();

            List<StoppingCriterion<PackingSolution>> criteriaList = CriteriaFactory.create(criteria);
            SelectionStrategy<PackingRectangle> strategy = SelectionStrategyFactory.create(selectionStrategy);
            Neighborhood<PackingSolution> thisNeighborhood = NeighborhoodFactory.create(neighborhood);
            AlgoArgs<PackingRectangle, PackingSolution> algoArgs = new AlgoArgs<>();
            algoArgs.setCriteria(criteriaList);
            algoArgs.setnNeigh(nNeigh);
            algoArgs.setProblem(problem);
            algoArgs.setStrategy(strategy);
            algoArgs.setNeighborhood(thisNeighborhood);

            Algorithm<?, PackingSolution> algorithm = AlgorithmFactory.createAlgorithm(
                    algoName,
                    algoArgs
            );

            if (algorithm instanceof LocalSearch) {
                PackingSolution sol = problem.createInitialSolution();
                sol.printSolution(sol);
                PackingSolution solution = ((Algorithm<Void, PackingSolution>) algorithm).run(null, sol);
                System.out.println(solution);
            } else if (algorithm instanceof Greedy) {
                List<PackingRectangle> items = problem.getRectangles();
                PackingSolution greedyState = problem.createGreedyState();
                PackingSolution solution = ((Algorithm<List<PackingRectangle>, PackingSolution>) algorithm).run(items, greedyState);
                solution.printSolution(solution);

            }


        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }



    }
}