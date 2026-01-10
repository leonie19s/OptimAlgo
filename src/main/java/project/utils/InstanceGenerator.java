package project.utils;

import project.problems.PackingRectangle;
import project.problems.RectanglePackingProblem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class InstanceGenerator {

    int L;
    int nRectangles;
    int upperLimitW;
    int upperLimitH;
    int lowerLimitW;
    int lowerLimitH;
    int seed;

    public InstanceGenerator(int L, int nRectangles, int upperLimitW, int lowerLimitW, int upperLimitH, int lowerLimitH, int seed){
        this.L = L;
        this.nRectangles = nRectangles;
        this.upperLimitH = upperLimitH;
        this.lowerLimitH = lowerLimitH;
        this.upperLimitW = upperLimitW;
        this.lowerLimitW = lowerLimitW;
        this.seed = seed;

    }

    public RectanglePackingProblem generate(){
        Random rand = new Random(this.seed);
        List<PackingRectangle> packingRectangles = new ArrayList<>();
        for (int i = 0; i < nRectangles; i++) {

            int h = rand.nextInt(upperLimitH-lowerLimitH +1)+ lowerLimitH;
            int w = rand.nextInt(upperLimitW-lowerLimitW +1)+ lowerLimitW;
            PackingRectangle thisRec = new PackingRectangle(w,h);
            packingRectangles.add(thisRec);

        }
        return new RectanglePackingProblem(packingRectangles,L);
    }

}

