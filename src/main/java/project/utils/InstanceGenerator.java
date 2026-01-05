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
    int seed;

    public InstanceGenerator(int L, int nRectangles, int upperLimitW, int upperLimitH, int seed){
        this.L = L;
        this.nRectangles = nRectangles;
        this.upperLimitH = upperLimitH;
        this.upperLimitW = upperLimitW;
        this.seed = seed;

    }

    public RectanglePackingProblem generate(){
        Random rand = new Random(this.seed);
        List<Rectangle> rectangles = new ArrayList<>();
        int min = 1;
        for (int i = 0; i < nRectangles; i++) {
            int h = rand.nextInt(upperLimitH-min +1)+ min;
            int w = rand.nextInt(upperLimitW-min +1)+ min;
            Rectangle thisRec = new Rectangle(w,h);

        }
        return new RectanglePackingProblem(rectangles,L);
    }

}

