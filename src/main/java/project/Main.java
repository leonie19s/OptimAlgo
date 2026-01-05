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
import project.selection.SelectionStrategy;
import project.selection.StratA;
import project.utils.AlgorithmFactory;
import project.utils.ConfigLoader;
import project.utils.InstanceGenerator;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        try
        {
            Properties config = ConfigLoader.load("config.properties");
            int upperLimW = Integer.parseInt(config.getProperty("rec.upperLimW"));
            int L = Integer.parseInt(config.getProperty("boxSize"));
            int upperLimH = Integer.parseInt(config.getProperty("rec.upperLimH"));
            int n = Integer.parseInt(config.getProperty("rec.N"));
            int seed = Integer.parseInt(config.getProperty("seed"));
            String algoName = config.getProperty("algorithm");

            InstanceGenerator gen = new InstanceGenerator(L, n,upperLimW, upperLimH, seed);
            RectanglePackingProblem problem = gen.generate();
            PackingSolution sol = problem.createInitialSolution();


            // TODO: Neighborhoodfactory
            Neighborhood<PackingSolution> neighborhood = new GeometryBased();
            // TODO: Strategyfactory

            SelectionStrategy<Rectangle> strategy = new StratA();

            Algorithm<?, PackingSolution> algorithm = AlgorithmFactory.createAlgorithm(
                    algoName,
                    problem,
                    neighborhood,
                    strategy
            );

            if (algorithm instanceof LocalSearch) {
                PackingSolution solution = ((Algorithm<Void, PackingSolution>) algorithm).run(null);
                System.out.println(solution);
            } else if (algorithm instanceof Greedy) {
                List<Rectangle> items = problem.getRectangles();
                PackingSolution solution = ((Algorithm<List<Rectangle>, PackingSolution>) algorithm).run(items);
                System.out.println(solution);
            }


        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }



    }
}