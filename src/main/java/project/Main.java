package project;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import project.algorithms.Algorithm;
import project.algorithms.Greedy;
import project.algorithms.LocalSearch;
import project.neighborhoods.GeometryBased;
import project.neighborhoods.Neighborhood;
import project.problems.PackingSolution;
import project.problems.Rectangle;
import project.problems.RectanglePackingProblem;
import project.selection.LargestSidelengthFirst;
import project.selection.SelectionStrategy;
import project.selection.BiggestFirst;
import project.utils.AlgorithmFactory;
import project.utils.ConfigLoader;
import project.utils.InstanceGenerator;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

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
            String algoName = config.getProperty("algorithm");

            InstanceGenerator gen = new InstanceGenerator(L, n,upperLimW, lowerLimW, upperLimH, lowerLimH, seed);
            RectanglePackingProblem problem = gen.generate();



            // TODO: Neighborhoodfactory
            Neighborhood<PackingSolution> neighborhood = new GeometryBased();
            // TODO: Strategyfactory

            SelectionStrategy<Rectangle> strategy = new LargestSidelengthFirst();

            Algorithm<?, PackingSolution> algorithm = AlgorithmFactory.createAlgorithm(
                    algoName,
                    problem,
                    neighborhood,
                    strategy
            );

            if (algorithm instanceof LocalSearch) {
                PackingSolution sol = problem.createInitialSolution();
                sol.printSolution(sol);
                PackingSolution solution = ((Algorithm<Void, PackingSolution>) algorithm).run(null, sol);
                System.out.println(solution);
            } else if (algorithm instanceof Greedy) {
                List<Rectangle> items = problem.getRectangles();
                PackingSolution greedyState = problem.createGreedyState();

                PackingSolution solution = ((Algorithm<List<Rectangle>, PackingSolution>) algorithm).run(items, greedyState);
                solution.printSolution(solution);

            }


        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }



    }
}