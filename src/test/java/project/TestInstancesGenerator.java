package project;

import project.algorithms.Algorithm;
import project.neighborhoods.GeometryBased;
import project.neighborhoods.Neighborhood;
import project.neighborhoods.RuleBased;
import project.problems.PackingSolution;
import project.problems.PackingRectangle;
import project.problems.RectanglePackingProblem;
import project.selection.BiggestFirst;
import project.selection.LargestSidelengthFirst;
import project.selection.SelectionStrategy;
import project.utils.AlgorithmFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestInstancesGenerator {
    int boxSize;
    int upperW;
    int upperH;
    int seed;
    int nRecs;
    int nStartBox;
    String algorithm;
    String neighborhood;
    String selectionStrategy;


    public TestInstancesGenerator(int boxSize, int upperW, int upperH, int seed, int nRecs, int nStartBox, String algorithm){
        this.boxSize = boxSize;
        this.upperW = upperW;
        this.upperH = upperH;
        this.seed = seed;
        this.nRecs = nRecs;
        this.nStartBox = nStartBox;
        this.algorithm = algorithm;



    }

    public List<PackingRectangle>generateRandomRecs()
    {
        Random rand = new Random(this.seed);
        List<PackingRectangle> packingRectangles = new ArrayList<>();
        int min = 1;
            for (int i = 0; i < this.nRecs; i++) {
                int h = rand.nextInt(upperH - min + 1) + min;
                int w = rand.nextInt(upperW - min + 1) + min;
                PackingRectangle thisRec = new PackingRectangle(w, h);
                packingRectangles.add(thisRec);
            }
        return packingRectangles;
    }
    public RectanglePackingProblem getProblem(List<PackingRectangle> recs){
    return new RectanglePackingProblem(recs,this.boxSize);}

    public SelectionStrategy<PackingRectangle> getSelectionStrategy(String selectionStrategy){
        if (selectionStrategy.equals("LargestSideLengthFirst")) {
            return new LargestSidelengthFirst();
        }
        return new BiggestFirst();

    }
    public Neighborhood<PackingSolution> getNeighborhood(String neighborhood){
        if (neighborhood.equals("RuleBased")) {
            return new RuleBased();
        }
        return new GeometryBased();
    }

    public Algorithm<?, PackingSolution> getAlgorithm(List<PackingRectangle> recs, RectanglePackingProblem problem, Neighborhood<PackingSolution> neighborhood, SelectionStrategy<PackingRectangle> selectionStrategy) {
        return AlgorithmFactory.createAlgorithm(
                this.algorithm,
                problem,
                neighborhood,
                selectionStrategy
        );
    }


}


