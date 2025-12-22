package main.java.problems;

import main.java.algorithms.Solution;

import java.util.List;
import java.util.Map;


/**
 * This class represents the current placement of Rectangles.
 */
public class PackingSolution implements Solution {
    private List<Box> boxes;
    private Map<Rectangle, Placement> placements;
    @Override
    public Solution copy() {
        return null;
    }

    public Placement getPlacement(Rectangle rect) {
        return placements.get(rect);
    }

    public void setPlacement(Rectangle rect, Placement placement) {
        placements.put(rect, placement);
    }
}
