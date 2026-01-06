package project.utils;

import project.problems.PackingSolution;
import project.problems.Rectangle;
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
        List<Rectangle> rectangles = new ArrayList<>();
        for (int i = 0; i < nRectangles; i++) {

            int h = rand.nextInt(upperLimitH-lowerLimitH +1)+ lowerLimitH;
            int w = rand.nextInt(upperLimitW-lowerLimitW +1)+ lowerLimitW;
            Rectangle thisRec = new Rectangle(w,h);
            rectangles.add(thisRec);

        }
        return new RectanglePackingProblem(rectangles,L);
    }

}

